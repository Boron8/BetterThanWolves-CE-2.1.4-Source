package btw.util.hardcorespawn;

import btw.BTWMod;
import btw.world.util.WorldUtils;
import java.util.ArrayList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.BiomeGenBase;
import net.minecraft.src.ChunkCoordinates;
import net.minecraft.src.EntityPlayerMP;
import net.minecraft.src.EnumGameType;
import net.minecraft.src.Material;
import net.minecraft.src.MathHelper;
import net.minecraft.src.Packet70GameEvent;
import net.minecraft.src.StringTranslate;
import net.minecraft.src.World;
import net.minecraft.src.WorldServer;
import net.minecraft.src.WorldType;

public class HardcoreSpawnUtils {
   public static final int HARDCORE_SPAWN_TIME_BETWEEN_REASSIGNMENTS = 10800;
   private static final double BASE_RADIUS = 2000.0;
   private static final double BASE_EXCLUSION_RADIUS = 1000.0;
   private static final double RAPID_RESPAWN_RADIUS = 100.0;
   private static final double ABANDONED_VILLAGE_RADIUS = 2250.0;
   private static final double PARTIALLY_ABADONED_VILLAGE_RADIUS = 3000.0;
   private static final double LOOTED_TEMPLE_RADIUS = 2250.0;
   private static final double LARGE_BIOMES_MULTIPLIER = 4.0;
   private static final int SPAWN_ATTEMPT_COUNT = 20;
   public static ArrayList<BiomeGenBase> blacklistedBiomes = new ArrayList<>();

   public static double getPlayerSpawnRadius(World world) {
      return 2000.0 * getWorldTypeRadiusMultiplier(world) * getGameProgressRadiusMultiplier(world);
   }

   public static double getPlayerSpawnExclusionRadius(World world) {
      return 1000.0 * getWorldTypeRadiusMultiplier(world);
   }

   public static double getPlayerMultipleRespawnRadius() {
      return 100.0;
   }

   public static double getAbandonedVillageRadius(World world) {
      return 2250.0 * getWorldTypeRadiusMultiplier(world) * getWorldDifficultyRadiusMultiplier(world);
   }

   public static double getPartiallyAbandonedVillageRadius(World world) {
      return 3000.0 * getWorldTypeRadiusMultiplier(world) * getWorldDifficultyRadiusMultiplier(world);
   }

   public static double getLootedTempleRadius(World world) {
      return 2250.0 * getWorldTypeRadiusMultiplier(world) * getWorldDifficultyRadiusMultiplier(world);
   }

   public static double getWorldTypeRadiusMultiplier(World world) {
      return world.worldInfo.getTerrainType() == WorldType.LARGE_BIOMES && BTWMod.increaseLargeBiomeHCS ? 4.0 : 1.0;
   }

   public static double getWorldDifficultyRadiusMultiplier(World world) {
      return world.worldInfo.getDifficulty().getAbandonmentRangeMultiplier();
   }

   public static double getGameProgressRadiusMultiplier(World world) {
      if (!world.worldInfo.getDifficulty().shouldHCSRangeIncrease()) {
         return 1.0;
      } else if (WorldUtils.gameProgressHasEndDimensionBeenAccessedServerOnly()) {
         return 2.5;
      } else if (WorldUtils.gameProgressHasWitherBeenSummonedServerOnly()) {
         return 2.0;
      } else {
         return WorldUtils.gameProgressHasNetherBeenAccessedServerOnly() ? 1.5 : 1.0;
      }
   }

   public static void handleHardcoreSpawn(MinecraftServer server, EntityPlayerMP oldPlayer, EntityPlayerMP newPlayer) {
      WorldServer newWorld = server.worldServerForDimension(newPlayer.dimension);
      if (oldPlayer.playerConqueredTheEnd) {
         returnPlayerToOriginalSpawn(newWorld, newPlayer);
      } else {
         long overworldTime = WorldUtils.getOverworldTimeServerOnly();
         long timeOfLastPlayerSpawnAssignment = oldPlayer.getTimeOfLastSpawnAssignment();
         long deltaTimeSinceLastRespawnAssignment = overworldTime - timeOfLastPlayerSpawnAssignment;
         boolean softRespawn = false;
         if (timeOfLastPlayerSpawnAssignment > 0L && deltaTimeSinceLastRespawnAssignment >= 0L && deltaTimeSinceLastRespawnAssignment < 10800L) {
            softRespawn = true;
            newPlayer.health = 10;
            int foodLevel = oldPlayer.foodStats.getFoodLevel();
            foodLevel -= 6;
            if (foodLevel < 24) {
               foodLevel = 24;
            }

            newPlayer.foodStats.setFoodLevel(foodLevel);
         }

         if (!softRespawn) {
            newPlayer.sendChatToPlayer(StringTranslate.getInstance().translateKey("message.death.newSpawn"));
            newPlayer.deathCount++;
            newPlayer.lastDeathDimension = oldPlayer.dimension;
            newPlayer.lastDeathLocationX = MathHelper.floor_double(oldPlayer.posX);
            newPlayer.lastDeathLocationY = MathHelper.floor_double(oldPlayer.boundingBox.minY);
            newPlayer.lastDeathLocationZ = MathHelper.floor_double(oldPlayer.posZ);
         } else {
            newPlayer.sendChatToPlayer(StringTranslate.getInstance().translateKey("message.death.oldSpawn"));
         }

         if (!WorldUtils.gameProgressHasNetherBeenAccessedServerOnly() || BTWMod.alwaysSpawnTogether) {
            SpawnLocation recentLocation = newWorld.getSpawnLocationList().getMostRecentSpawnLocation();
            if (recentLocation != null) {
               long lDeltaTime = overworldTime - recentLocation.spawnTime;
               if (lDeltaTime > 0L
                  && lDeltaTime < 10800L
                  && assignPlayerToOldSpawnPosWithVariance(
                     newWorld, newPlayer, new ChunkCoordinates(recentLocation.posX, recentLocation.posY, recentLocation.posZ), recentLocation.spawnTime
                  )) {
                  return;
               }
            }
         }

         ChunkCoordinates oldSpawnPos = oldPlayer.hardcoreSpawnChunk;
         if ((oldSpawnPos == null || !softRespawn || !assignPlayerToOldSpawnPosWithVariance(newWorld, newPlayer, oldSpawnPos, timeOfLastPlayerSpawnAssignment))
            && !assignNewHardcoreSpawnLocation(newWorld, server, newPlayer)) {
            returnPlayerToOriginalSpawn(newWorld, newPlayer);
         } else {
            ChunkCoordinates newSpawnPos = newPlayer.hardcoreSpawnChunk;
            if (newSpawnPos != null) {
               newWorld.getSpawnLocationList()
                  .addPointIfNotAlreadyPresent(newSpawnPos.posX, newSpawnPos.posY, newSpawnPos.posZ, newPlayer.getTimeOfLastSpawnAssignment());
            }
         }
      }
   }

   public static boolean assignNewHardcoreSpawnLocation(World world, MinecraftServer server, EntityPlayerMP player) {
      boolean locationFound = false;
      boolean blacklistedLocationFound = false;
      double spawnRadius = getPlayerSpawnRadius(world);
      double exclusionRadius = getPlayerSpawnExclusionRadius(world);
      double spawnDeltaRadius = spawnRadius - exclusionRadius;
      double exclusionRadiusSq = exclusionRadius * exclusionRadius;
      double deltaSquaredRadii = spawnRadius * spawnRadius - exclusionRadiusSq;

      for (int attempts = 0; attempts < 20; attempts++) {
         double spawnDistance = Math.sqrt(world.rand.nextDouble() * deltaSquaredRadii + exclusionRadiusSq);
         double spawnYaw = world.rand.nextDouble() * Math.PI * 2.0;
         double xOffset = -Math.sin(spawnYaw) * spawnDistance;
         double zOffset = Math.cos(spawnYaw) * spawnDistance;
         int newSpawnX = MathHelper.floor_double(xOffset) + world.worldInfo.getSpawnX();
         int newSpawnZ = MathHelper.floor_double(zOffset) + world.worldInfo.getSpawnZ();
         int newSpawnY = world.getTopSolidOrLiquidBlock(newSpawnX, newSpawnZ);
         BiomeGenBase respawnBiome = world.getBiomeGenForCoords(newSpawnX, newSpawnZ);
         boolean isBiomeBlacklisted = blacklistedBiomes.contains(respawnBiome);
         if (newSpawnY >= world.provider.getAverageGroundLevel()) {
            Material targetMaterial = world.getBlockMaterial(newSpawnX, newSpawnY, newSpawnZ);
            if (targetMaterial == null || !targetMaterial.isLiquid()) {
               player.b(newSpawnX + 0.5, newSpawnY + 1.5, newSpawnZ + 0.5, world.rand.nextFloat() * 360.0F, 0.0F);
               bumpPlayerPosUpwardsUntilValidSpawnReached(player);
               long overworldTime = WorldUtils.getOverworldTimeServerOnly();
               if (BTWMod.isSinglePlayerNonLan() || MinecraftServer.getServer().getCurrentPlayerCount() == 0) {
                  overworldTime = (overworldTime / 24000L + 1L) * 24000L;

                  for (int i = 0; i < MinecraftServer.getServer().worldServers.length; i++) {
                     WorldServer tempServer = MinecraftServer.getServer().worldServers[i];
                     tempServer.b(overworldTime);
                     if (tempServer.worldInfo.isThundering()) {
                        tempServer.worldInfo.setThundering(false);
                        server.getConfigurationManager().sendPacketToAllPlayers(new Packet70GameEvent(8, 0));
                     }
                  }
               }

               player.setTimeOfLastSpawnAssignment(overworldTime);
               ChunkCoordinates newSpawnPos = new ChunkCoordinates(
                  MathHelper.floor_double(player.posX), MathHelper.floor_double(player.posY), MathHelper.floor_double(player.posZ)
               );
               player.hardcoreSpawnChunk = newSpawnPos;
               if (!isBiomeBlacklisted) {
                  locationFound = true;
                  break;
               }

               blacklistedLocationFound = true;
            }
         }
      }

      return locationFound || blacklistedLocationFound;
   }

   private static boolean assignPlayerToOldSpawnPosWithVariance(
      World world, EntityPlayerMP player, ChunkCoordinates spawnPos, long timeOfLastPlayerSpawnAssignment
   ) {
      for (int i = 0; i < 20; i++) {
         double spawnDistance = Math.sqrt(world.rand.nextDouble()) * getPlayerMultipleRespawnRadius();
         double spawnYaw = world.rand.nextDouble() * Math.PI * 2.0;
         double xOffset = -Math.sin(spawnYaw) * spawnDistance;
         double zOffset = Math.cos(spawnYaw) * spawnDistance;
         int newSpawnX = MathHelper.floor_double(xOffset) + spawnPos.posX;
         int newSpawnZ = MathHelper.floor_double(zOffset) + spawnPos.posZ;
         int newSpawnY = world.getTopSolidOrLiquidBlock(newSpawnX, newSpawnZ);
         if (newSpawnY >= world.provider.getAverageGroundLevel()) {
            Material targetMaterial = world.getBlockMaterial(newSpawnX, newSpawnY, newSpawnZ);
            if (targetMaterial == null || !targetMaterial.isLiquid()) {
               player.b(newSpawnX + 0.5, newSpawnY + 1.5, newSpawnZ + 0.5, world.rand.nextFloat() * 360.0F, 0.0F);
               bumpPlayerPosUpwardsUntilValidSpawnReached(player);
               player.setTimeOfLastSpawnAssignment(timeOfLastPlayerSpawnAssignment);
               player.hardcoreSpawnChunk = spawnPos;
               return true;
            }
         }
      }

      return assignPlayerToOldSpawnPos(world, player, spawnPos, timeOfLastPlayerSpawnAssignment);
   }

   private static boolean assignPlayerToOldSpawnPos(World world, EntityPlayerMP player, ChunkCoordinates spawnPos, long timeOfLastPlayerSpawnAssignment) {
      int spawnX = MathHelper.floor_double(spawnPos.posX);
      int spawnY = MathHelper.floor_double(spawnPos.posZ);
      int spawnZ = world.getTopSolidOrLiquidBlock(spawnX, spawnY);
      player.b(spawnX + 0.5F, spawnZ + 1.5F, spawnY + 0.5F, world.rand.nextFloat() * 360.0F, 0.0F);
      Material targetMaterial = world.getBlockMaterial(spawnX, spawnZ + 1, spawnY);
      if (offsetPlayerPositionUntilValidSpawn(world, player)) {
         bumpPlayerPosUpwardsUntilValidSpawnReached(player);
         player.setTimeOfLastSpawnAssignment(timeOfLastPlayerSpawnAssignment);
         ChunkCoordinates newSpawnPos = new ChunkCoordinates(
            MathHelper.floor_double(player.posX), MathHelper.floor_double(player.posY), MathHelper.floor_double(player.posZ)
         );
         player.hardcoreSpawnChunk = newSpawnPos;
         return true;
      } else {
         return false;
      }
   }

   private static boolean offsetPlayerPositionUntilValidSpawn(World world, EntityPlayerMP player) {
      int spawnX = MathHelper.floor_double(player.posX);
      int spawnZ = MathHelper.floor_double(player.posZ);

      for (int i = 0; i < 20; i++) {
         int spawnY = world.getTopSolidOrLiquidBlock(spawnX, spawnZ);
         Material targetMaterial = world.getBlockMaterial(spawnX, spawnY, spawnZ);
         if (targetMaterial == null || !targetMaterial.isLiquid()) {
            player.b(spawnX + 0.5, player.posY, spawnZ + 0.5, player.rotationYaw, player.rotationPitch);
            return true;
         }

         spawnX += world.rand.nextInt(11) - 5;
         spawnZ += world.rand.nextInt(11) - 5;
      }

      return false;
   }

   private static void returnPlayerToOriginalSpawn(World world, EntityPlayerMP player) {
      ChunkCoordinates spawnPos = world.getSpawnPoint();
      int spawnX = spawnPos.posX;
      int spawnY = spawnPos.posY;
      int spawnZ = spawnPos.posZ;
      if (!world.provider.hasNoSky && world.getWorldInfo().getGameType() != EnumGameType.ADVENTURE) {
         spawnX += world.rand.nextInt(20) - 10;
         spawnY = world.getTopSolidOrLiquidBlock(spawnX, spawnZ);
         spawnZ += world.rand.nextInt(20) - 10;
      }

      player.b(spawnX + 0.5, spawnY + 1.5, spawnZ + 0.5, 0.0F, 0.0F);
      bumpPlayerPosUpwardsUntilValidSpawnReached(player);
      player.setTimeOfLastSpawnAssignment(0L);
      player.hardcoreSpawnChunk = null;
   }

   private static void bumpPlayerPosUpwardsUntilValidSpawnReached(EntityPlayerMP player) {
      while (!(player.posY <= 0.0)) {
         player.b(player.posX, player.posY, player.posZ, player.rotationYaw, player.rotationPitch);
         if (!player.worldObj.getCollidingBoundingBoxes(player, player.boundingBox).isEmpty()) {
            player.posY++;
            continue;
         }
         break;
      }
   }

   public static boolean isInLootedTempleRadius(World world, int x, int z) {
      int spawnX = world.getWorldInfo().getSpawnX();
      int spawnZ = world.getWorldInfo().getSpawnZ();
      double deltaX = spawnX - x;
      double deltaZ = spawnZ - z;
      double distSqFromSpawn = deltaX * deltaX + deltaZ * deltaZ;
      double lootedRadius = getLootedTempleRadius(world);
      return distSqFromSpawn < lootedRadius * lootedRadius;
   }

   static {
      blacklistedBiomes.add(BiomeGenBase.jungle);
      blacklistedBiomes.add(BiomeGenBase.jungleHills);
      blacklistedBiomes.add(BiomeGenBase.ocean);
      blacklistedBiomes.add(BiomeGenBase.frozenOcean);
      blacklistedBiomes.add(BiomeGenBase.river);
      blacklistedBiomes.add(BiomeGenBase.beach);
   }
}
