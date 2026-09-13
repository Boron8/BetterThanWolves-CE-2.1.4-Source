package net.minecraft.src;

import btw.AddonHandler;
import btw.BTWAddon;
import btw.block.BTWBlocks;
import btw.block.tileentity.beacon.BeaconEffectLocation;
import btw.block.tileentity.beacon.MagneticPoint;
import btw.entity.LightningBoltEntity;
import btw.network.packet.PlayerSyncPacket;
import btw.util.hardcorespawn.SpawnLocation;
import btw.world.chunk.ChunkTracker;
import btw.world.feature.trees.BonusBasketGenerator;
import btw.world.util.BlockPos;
import btw.world.util.WorldData;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;
import java.util.Map.Entry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.server.MinecraftServer;

public class WorldServer extends World {
   private final MinecraftServer mcServer;
   private final EntityTracker theEntityTracker;
   private final ChunkTracker chunkTracker;
   private Set field_73064_N;
   private TreeSet pendingTickListEntries;
   public ChunkProviderServer theChunkProviderServer;
   public boolean canNotSave;
   private boolean allPlayersSleeping;
   private int updateEntityTick = 0;
   private final Teleporter field_85177_Q;
   private ServerBlockEventList[] blockEventCache = new ServerBlockEventList[]{
      new ServerBlockEventList((ServerBlockEvent)null), new ServerBlockEventList((ServerBlockEvent)null)
   };
   private int blockEventCacheIndex = 0;
   private static final WeightedRandomChestContent[] bonusChestContent = new WeightedRandomChestContent[]{
      new WeightedRandomChestContent(Item.stick.itemID, 0, 1, 3, 10),
      new WeightedRandomChestContent(Block.planks.blockID, 0, 1, 3, 10),
      new WeightedRandomChestContent(Block.wood.blockID, 0, 1, 3, 10),
      new WeightedRandomChestContent(Item.axeStone.itemID, 0, 1, 1, 3),
      new WeightedRandomChestContent(Item.axeWood.itemID, 0, 1, 1, 5),
      new WeightedRandomChestContent(Item.pickaxeStone.itemID, 0, 1, 1, 3),
      new WeightedRandomChestContent(Item.pickaxeWood.itemID, 0, 1, 1, 5),
      new WeightedRandomChestContent(Item.appleRed.itemID, 0, 2, 3, 5),
      new WeightedRandomChestContent(Item.bread.itemID, 0, 2, 3, 3)
   };
   private ArrayList field_94579_S = new ArrayList();
   private IntHashMap entityIdMap;
   private boolean hasTicked = false;
   protected LinkedList<ChunkCoordIntPair> chunksToCheckForUnloadList = new LinkedList<>();
   private long noPlayersOnServerTickCount = 0L;
   private final int chunksAroundSpawnToCheckForUnload = 13;

   public WorldServer(
      MinecraftServer par1MinecraftServer,
      ISaveHandler par2ISaveHandler,
      String par3Str,
      int par4,
      WorldSettings par5WorldSettings,
      Profiler par6Profiler,
      ILogAgent par7ILogAgent
   ) {
      super(par2ISaveHandler, par3Str, par5WorldSettings, WorldProvider.getProviderForDimension(par4), par6Profiler, par7ILogAgent);
      this.mcServer = par1MinecraftServer;
      this.saveHandler.loadModSpecificData(this);
      this.theEntityTracker = new EntityTracker(this);
      this.chunkTracker = new ChunkTracker(this, par1MinecraftServer.getConfigurationManager().getViewDistance());
      if (this.entityIdMap == null) {
         this.entityIdMap = new IntHashMap();
      }

      if (this.field_73064_N == null) {
         this.field_73064_N = new HashSet();
      }

      if (this.pendingTickListEntries == null) {
         this.pendingTickListEntries = new TreeSet();
      }

      this.field_85177_Q = new Teleporter(this);
      this.worldScoreboard = new ServerScoreboard(par1MinecraftServer);
      ScoreboardSaveData var8 = (ScoreboardSaveData)this.mapStorage.loadData(ScoreboardSaveData.class, "scoreboard");
      if (var8 == null) {
         var8 = new ScoreboardSaveData();
         this.mapStorage.setData("scoreboard", var8);
      }

      var8.func_96499_a(this.worldScoreboard);
      ((ServerScoreboard)this.worldScoreboard).func_96547_a(var8);
   }

   @Override
   public void tick() {
      super.tick();
      if (this.M().isHardcoreModeEnabled() && this.difficultySetting < 3) {
         this.difficultySetting = 3;
      } else if (this.difficultySetting < 2) {
         this.difficultySetting = 2;
      }

      this.provider.worldChunkMgr.cleanupCache();
      this.theProfiler.startSection("mobSpawner");
      if (this.N().getGameRuleBooleanValue("doMobSpawning")) {
         SpawnerAnimals.findChunksForSpawning(this, this.spawnHostileMobs, this.spawnPeacefulMobs, false);
      }

      this.theProfiler.endStartSection("chunkSource");
      this.chunkProvider.unloadQueuedChunks();
      int var4 = this.a(1.0F);
      if (var4 != this.skylightSubtracted) {
         this.skylightSubtracted = var4;
      }

      this.worldInfo.incrementTotalWorldTime(this.worldInfo.getWorldTotalTime() + 1L);
      this.worldInfo.setWorldTime(this.worldInfo.getWorldTime() + 1L);
      this.theProfiler.endStartSection("tickPending");
      this.tickUpdates(false);
      this.theProfiler.endStartSection("tickTiles");
      this.tickBlocksAndAmbiance();
      this.theProfiler.endStartSection("chunkMap");
      this.chunkTracker.update();
      this.theProfiler.endStartSection("village");
      this.villageCollectionObj.tick();
      this.theProfiler.endStartSection("portalForcer");
      this.field_85177_Q.removeStalePortalLocations(this.H());
      this.theProfiler.endSection();
      this.sendAndApplyBlockEvents();
      if (this.H() % 200L == 0L) {
         for (int i = 0; i < this.playerEntities.size(); i++) {
            EntityPlayer playerEntity = (EntityPlayer)this.playerEntities.get(i);
            this.mcServer
               .getConfigurationManager()
               .sendToAllNearExcept(
                  playerEntity,
                  playerEntity.posX,
                  playerEntity.posY,
                  playerEntity.posZ,
                  100.0,
                  this.provider.dimensionId,
                  new PlayerSyncPacket(playerEntity.username)
               );
         }
      }
   }

   public SpawnListEntry spawnRandomCreature(EnumCreatureType par1EnumCreatureType, int par2, int par3, int par4) {
      List var5 = this.K().getPossibleCreatures(par1EnumCreatureType, par2, par3, par4);
      return var5 != null && !var5.isEmpty() ? (SpawnListEntry)WeightedRandom.getRandomItem(this.rand, var5) : null;
   }

   @Override
   public void updateAllPlayersSleepingFlag() {
      this.allPlayersSleeping = !this.playerEntities.isEmpty();

      for (EntityPlayer var2 : this.playerEntities) {
         if (!var2.isPlayerSleeping()) {
            this.allPlayersSleeping = false;
            break;
         }
      }
   }

   protected void wakeAllPlayers() {
      this.allPlayersSleeping = false;

      for (EntityPlayer var2 : this.playerEntities) {
         if (var2.isPlayerSleeping()) {
            var2.wakeUpPlayer(false, false, true);
         }
      }

      this.resetRainAndThunder();
   }

   private void resetRainAndThunder() {
      this.worldInfo.setRainTime(0);
      this.worldInfo.setRaining(false);
      this.worldInfo.setThunderTime(0);
      this.worldInfo.setThundering(false);
   }

   public boolean areAllPlayersAsleep() {
      if (this.allPlayersSleeping && !this.isRemote) {
         for (EntityPlayer var2 : this.playerEntities) {
            if (!var2.isPlayerFullyAsleep()) {
               return false;
            }
         }

         return true;
      } else {
         return false;
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void setSpawnLocation() {
      if (this.worldInfo.getSpawnY() <= 0) {
         this.worldInfo.setSpawnY(64);
      }

      int var1 = this.worldInfo.getSpawnX();
      int var2 = this.worldInfo.getSpawnZ();
      int var3 = 0;

      while (this.b(var1, var2) == 0) {
         var1 += this.rand.nextInt(8) - this.rand.nextInt(8);
         var2 += this.rand.nextInt(8) - this.rand.nextInt(8);
         if (++var3 == 10000) {
            break;
         }
      }

      this.worldInfo.setSpawnX(var1);
      this.worldInfo.setSpawnZ(var2);
   }

   @Override
   protected void tickBlocksAndAmbiance() {
      super.tickBlocksAndAmbiance();
      int var1 = 0;
      int var2 = 0;

      for (ChunkCoordIntPair var4 : this.activeChunksCoordsList) {
         int var5 = var4.chunkXPos * 16;
         int var6 = var4.chunkZPos * 16;
         this.theProfiler.startSection("getChunk");
         Chunk var7 = this.e(var4.chunkXPos, var4.chunkZPos);
         this.a(var5, var6, var7);
         this.theProfiler.endStartSection("tickChunk");
         var7.updateSkylight();
         this.theProfiler.endStartSection("thunder");
         if (this.rand.nextInt(50000) == 0 && this.P() && this.O()) {
            this.updateLCG = this.updateLCG * 3 + 1013904223;
            int var8 = this.updateLCG >> 2;
            int var9 = var5 + (var8 & 15);
            int var10 = var6 + (var8 >> 8 & 15);
            int var11 = this.h(var9, var10);
            if (this.canLightningStrikeAtPos(var9, var11, var10)) {
               BlockPos strikePos = new BlockPos(var9, var11, var10);
               BlockPos adjustedStrikePos = this.getAdjustedLightningStrikeLocation(strikePos);
               if (this.isBlockPosActive(adjustedStrikePos.x, adjustedStrikePos.y, adjustedStrikePos.z)) {
                  this.addWeatherEffect(
                     EntityList.createEntityOfType(LightningBoltEntity.class, this, adjustedStrikePos.x + 0.5, adjustedStrikePos.y, adjustedStrikePos.z + 0.5)
                  );
               }
            }
         }

         this.theProfiler.endStartSection("iceandsnow");
         if (this.rand.nextInt(16) == 0) {
            this.updateLCG = this.updateLCG * 3 + 1013904223;
            int var8 = this.updateLCG >> 2;
            int var9 = var8 & 15;
            int var10 = var8 >> 8 & 15;
            int var11 = this.h(var9 + var5, var10 + var6);
            if (this.y(var9 + var5, var11 - 1, var10 + var6)) {
               this.c(var9 + var5, var11 - 1, var10 + var6, Block.ice.blockID);
            }

            if (this.P() && this.z(var9 + var5, var11, var10 + var6)) {
               this.c(var9 + var5, var11, var10 + var6, Block.snow.blockID);
            } else if (this.P() && this.z(var9 + var5, var11 + 1, var10 + var6)) {
               this.c(var9 + var5, var11 + 1, var10 + var6, Block.snow.blockID);
            }

            if (this.P()) {
               BiomeGenBase var12 = this.a(var9 + var5, var10 + var6);
               if (var12.canRainInBiome()) {
                  int var13 = this.a(var9 + var5, var11 - 1, var10 + var6);
                  if (var13 != 0) {
                     Block.blocksList[var13].fillWithRain(this, var9 + var5, var11 - 1, var10 + var6);
                  }
               }
            }
         }

         this.theProfiler.endStartSection("tickTiles");

         for (ExtendedBlockStorage var21 : var7.getBlockStorageArray()) {
            if (var21 != null && var21.getNeedsRandomTick()) {
               for (int var20 = 0; var20 < 3; var20++) {
                  this.updateLCG = this.updateLCG * 3 + 1013904223;
                  int var13 = this.updateLCG >> 2;
                  int var14 = var13 & 15;
                  int var15 = var13 >> 8 & 15;
                  int var16 = var13 >> 16 & 15;
                  int var17 = var21.getExtBlockID(var14, var16, var15);
                  var2++;
                  Block var18 = Block.blocksList[var17];
                  if (var18 != null && var18.getTickRandomly()) {
                     var1++;
                     var18.randomUpdateTick(this, var14 + var5, var16 + var21.getYLocation(), var15 + var6, this.rand);
                  }
               }
            }
         }

         this.theProfiler.endSection();
      }

      this.modUpdateTick();
   }

   @Override
   public boolean isBlockTickScheduled(int par1, int par2, int par3, int par4) {
      NextTickListEntry var5 = new NextTickListEntry(par1, par2, par3, par4);
      return this.field_94579_S.contains(var5);
   }

   @Override
   public void scheduleBlockUpdate(int par1, int par2, int par3, int par4, int par5) {
      this.func_82740_a(par1, par2, par3, par4, par5, 0);
   }

   @Override
   public void func_82740_a(int par1, int par2, int par3, int par4, int par5, int par6) {
      NextTickListEntry var7 = new NextTickListEntry(par1, par2, par3, par4);
      if (this.scheduledUpdatesAreImmediate && par4 > 0) {
         if (Block.blocksList[par4].func_82506_l()) {
            if (this.isBlockPosActive(var7.xCoord, var7.yCoord, var7.zCoord)) {
               int var9 = this.a(var7.xCoord, var7.yCoord, var7.zCoord);
               if (var9 == var7.blockID && var9 > 0) {
                  Block.blocksList[var9].updateTick(this, var7.xCoord, var7.yCoord, var7.zCoord, this.rand);
               }
            }

            return;
         }

         par5 = 1;
      }

      if (this.isBlockPosActive(par1, par2, par3)) {
         if (par4 > 0) {
            var7.setScheduledTime(par5 + this.worldInfo.getWorldTotalTime());
            var7.func_82753_a(par6);
         }

         if (!this.field_73064_N.contains(var7)) {
            this.field_73064_N.add(var7);
            this.pendingTickListEntries.add(var7);
         }
      }
   }

   @Override
   public void scheduleBlockUpdateFromLoad(int par1, int par2, int par3, int par4, int par5, int par6) {
      NextTickListEntry var7 = new NextTickListEntry(par1, par2, par3, par4);
      var7.func_82753_a(par6);
      if (par4 > 0) {
         var7.setScheduledTime(par5 + this.worldInfo.getWorldTotalTime());
      }

      if (!this.field_73064_N.contains(var7)) {
         this.field_73064_N.add(var7);
         this.pendingTickListEntries.add(var7);
      }
   }

   @Override
   public void updateEntities() {
      super.updateEntities();
   }

   public void resetUpdateEntityTick() {
      this.updateEntityTick = 0;
   }

   @Override
   public boolean tickUpdates(boolean par1) {
      int var2 = this.pendingTickListEntries.size();
      if (var2 != this.field_73064_N.size()) {
         throw new IllegalStateException("TickNextTick list out of synch");
      } else {
         if (var2 > 1000) {
            var2 = 1000;
         }

         this.theProfiler.startSection("cleaning");

         for (int var3 = 0; var3 < var2; var3++) {
            NextTickListEntry var4 = (NextTickListEntry)this.pendingTickListEntries.first();
            if (!par1 && var4.scheduledTime > this.worldInfo.getWorldTotalTime()) {
               break;
            }

            this.pendingTickListEntries.remove(var4);
            this.field_73064_N.remove(var4);
            this.field_94579_S.add(var4);
         }

         this.theProfiler.endSection();
         this.theProfiler.startSection("ticking");
         Iterator var14 = this.field_94579_S.iterator();

         while (var14.hasNext()) {
            NextTickListEntry var4 = (NextTickListEntry)var14.next();
            var14.remove();
            if (this.isBlockPosActive(var4.xCoord, var4.yCoord, var4.zCoord)) {
               int var6 = this.a(var4.xCoord, var4.yCoord, var4.zCoord);
               if (var6 > 0 && Block.isAssociatedBlockID(var6, var4.blockID)) {
                  try {
                     Block.blocksList[var6].updateTick(this, var4.xCoord, var4.yCoord, var4.zCoord, this.rand);
                  } catch (Throwable var12) {
                     CrashReport var8 = CrashReport.makeCrashReport(var12, "Exception while ticking a block");
                     CrashReportCategory var9 = var8.makeCategory("Block being ticked");

                     int var10;
                     try {
                        var10 = this.h(var4.xCoord, var4.yCoord, var4.zCoord);
                     } catch (Throwable var11) {
                        var10 = -1;
                     }

                     CrashReportCategory.func_85068_a(var9, var4.xCoord, var4.yCoord, var4.zCoord, var6, var10);
                     throw new ReportedException(var8);
                  }
               }
            }
         }

         this.theProfiler.endSection();
         this.field_94579_S.clear();
         return !this.pendingTickListEntries.isEmpty();
      }
   }

   @Override
   public List getPendingBlockUpdates(Chunk par1Chunk, boolean par2) {
      ArrayList var3 = null;
      ChunkCoordIntPair var4 = par1Chunk.getChunkCoordIntPair();
      int var5 = (var4.chunkXPos << 4) - 2;
      int var6 = var5 + 16 + 2;
      int var7 = (var4.chunkZPos << 4) - 2;
      int var8 = var7 + 16 + 2;

      for (int var9 = 0; var9 < 2; var9++) {
         Iterator var10;
         if (var9 == 0) {
            var10 = this.pendingTickListEntries.iterator();
         } else {
            var10 = this.field_94579_S.iterator();
            if (!this.field_94579_S.isEmpty()) {
               System.out.println(this.field_94579_S.size());
            }
         }

         while (var10.hasNext()) {
            NextTickListEntry var11 = (NextTickListEntry)var10.next();
            if (var11.xCoord >= var5 && var11.xCoord < var6 && var11.zCoord >= var7 && var11.zCoord < var8) {
               if (par2) {
                  this.field_73064_N.remove(var11);
                  var10.remove();
               }

               if (var3 == null) {
                  var3 = new ArrayList();
               }

               var3.add(var11);
            }
         }
      }

      return var3;
   }

   @Override
   public void updateEntityWithOptionalForce(Entity par1Entity, boolean par2) {
      if (!this.mcServer.getCanSpawnAnimals() && (par1Entity instanceof EntityAnimal || par1Entity instanceof EntityWaterMob)) {
         par1Entity.setDead();
      }

      if (!this.mcServer.getCanSpawnNPCs() && par1Entity instanceof INpc) {
         par1Entity.setDead();
      }

      if (!(par1Entity.riddenByEntity instanceof EntityPlayer)) {
         int iEntityI = MathHelper.floor_double(par1Entity.posX);
         int iEntityK = MathHelper.floor_double(par1Entity.posZ);
         if (par2 && !this.isBlockPosActive(iEntityI, 0, iEntityK) && par1Entity.addedToChunk && !(par1Entity instanceof EntityPlayer)) {
            if (par1Entity.ridingEntity == null) {
               par1Entity.outOfUpdateRangeUpdate();
            }

            return;
         }

         super.updateEntityWithOptionalForce(par1Entity, par2);
      }
   }

   public void uncheckedUpdateEntity(Entity par1Entity, boolean par2) {
      super.updateEntityWithOptionalForce(par1Entity, par2);
   }

   @Override
   protected IChunkProvider createChunkProvider() {
      IChunkLoader var1 = this.saveHandler.getChunkLoader(this.provider);
      this.theChunkProviderServer = new ChunkProviderServer(this, var1, this.provider.createChunkGenerator());
      return this.theChunkProviderServer;
   }

   public List getAllTileEntityInBox(int par1, int par2, int par3, int par4, int par5, int par6) {
      ArrayList var7 = new ArrayList();

      for (int var8 = 0; var8 < this.loadedTileEntityList.size(); var8++) {
         TileEntity var9 = (TileEntity)this.loadedTileEntityList.get(var8);
         if (var9.xCoord >= par1 && var9.yCoord >= par2 && var9.zCoord >= par3 && var9.xCoord < par4 && var9.yCoord < par5 && var9.zCoord < par6) {
            var7.add(var9);
         }
      }

      return var7;
   }

   @Override
   public boolean canMineBlock(EntityPlayer par1EntityPlayer, int par2, int par3, int par4) {
      return !this.mcServer.func_96290_a(this, par2, par3, par4, par1EntityPlayer);
   }

   @Override
   protected void initialize(WorldSettings par1WorldSettings) {
      if (this.entityIdMap == null) {
         this.entityIdMap = new IntHashMap();
      }

      if (this.field_73064_N == null) {
         this.field_73064_N = new HashSet();
      }

      if (this.pendingTickListEntries == null) {
         this.pendingTickListEntries = new TreeSet();
      }

      this.createSpawnPosition(par1WorldSettings);
      super.initialize(par1WorldSettings);
   }

   protected void createSpawnPosition(WorldSettings par1WorldSettings) {
      if (!this.provider.canRespawnHere()) {
         this.worldInfo.setSpawnPosition(0, this.provider.getAverageGroundLevel(), 0);
      } else {
         this.findingSpawnPoint = true;
         WorldChunkManager var2 = this.provider.worldChunkMgr;
         List var3 = var2.getBiomesToSpawnIn();
         Random var4 = new Random(this.G());
         ChunkPosition var5 = var2.findBiomePosition(0, 0, 256, var3, var4);
         int var6 = 0;
         int var7 = this.provider.getAverageGroundLevel();
         int var8 = 0;
         if (var5 != null) {
            var6 = var5.x;
            var8 = var5.z;
         } else {
            this.X().logWarning("Unable to find spawn biome");
         }

         int var9 = 0;

         while (!this.provider.canCoordinateBeSpawn(var6, var8)) {
            var6 += var4.nextInt(64) - var4.nextInt(64);
            var8 += var4.nextInt(64) - var4.nextInt(64);
            if (++var9 == 1000) {
               break;
            }
         }

         this.worldInfo.setSpawnPosition(var6, var7, var8);
         this.findingSpawnPoint = false;
         if (par1WorldSettings.isBonusChestEnabled()) {
            this.createBonusChest();
         }
      }
   }

   protected void createBonusChest() {
      BonusBasketGenerator var1 = new BonusBasketGenerator();

      for (int var2 = 0; var2 < 10; var2++) {
         int var3 = this.worldInfo.getSpawnX() + this.rand.nextInt(6) - this.rand.nextInt(6);
         int var4 = this.worldInfo.getSpawnZ() + this.rand.nextInt(6) - this.rand.nextInt(6);
         int var5 = this.i(var3, var4) + 1;
         if (var1.generate(this, this.rand, var3, var5, var4)) {
            break;
         }
      }
   }

   public ChunkCoordinates getEntrancePortalLocation() {
      return this.provider.getEntrancePortalLocation();
   }

   public void saveAllChunks(boolean par1, IProgressUpdate par2IProgressUpdate) throws MinecraftException {
      if (this.chunkProvider.canSave()) {
         if (par2IProgressUpdate != null) {
            par2IProgressUpdate.displayProgressMessage("Saving level");
         }

         this.saveLevel();
         if (par2IProgressUpdate != null) {
            par2IProgressUpdate.resetProgresAndWorkingMessage("Saving chunks");
         }

         this.chunkProvider.saveChunks(par1, par2IProgressUpdate);
         this.saveHandler.saveModSpecificData(this);
      }
   }

   public void func_104140_m() {
      if (this.chunkProvider.canSave()) {
         this.chunkProvider.func_104112_b();
      }
   }

   protected void saveLevel() throws MinecraftException {
      this.F();
      this.saveHandler.saveWorldInfoWithPlayer(this.worldInfo, this.mcServer.getConfigurationManager().getHostPlayerData());
      this.mapStorage.saveAllData();
   }

   @Override
   protected void obtainEntitySkin(Entity par1Entity) {
      super.obtainEntitySkin(par1Entity);
      this.entityIdMap.addKey(par1Entity.entityId, par1Entity);
      Entity[] var2 = par1Entity.getParts();
      if (var2 != null) {
         for (int var3 = 0; var3 < var2.length; var3++) {
            this.entityIdMap.addKey(var2[var3].entityId, var2[var3]);
         }
      }
   }

   @Override
   protected void releaseEntitySkin(Entity par1Entity) {
      super.releaseEntitySkin(par1Entity);
      this.entityIdMap.removeObject(par1Entity.entityId);
      Entity[] var2 = par1Entity.getParts();
      if (var2 != null) {
         for (int var3 = 0; var3 < var2.length; var3++) {
            this.entityIdMap.removeObject(var2[var3].entityId);
         }
      }
   }

   @Override
   public Entity getEntityByID(int par1) {
      return (Entity)this.entityIdMap.lookup(par1);
   }

   @Override
   public boolean addWeatherEffect(Entity par1Entity) {
      if (super.addWeatherEffect(par1Entity)) {
         Packet71Weather packet = new Packet71Weather(par1Entity);
         if (par1Entity instanceof LightningBoltEntity) {
            packet.isLightningBolt = 1;
         }

         this.mcServer.getConfigurationManager().sendToAllNear(par1Entity.posX, par1Entity.posY, par1Entity.posZ, 512.0, this.provider.dimensionId, packet);
         return true;
      } else {
         return false;
      }
   }

   @Override
   public void setEntityState(Entity par1Entity, byte par2) {
      Packet38EntityStatus var3 = new Packet38EntityStatus(par1Entity.entityId, par2);
      this.getEntityTracker().sendPacketToAllAssociatedPlayers(par1Entity, var3);
   }

   @Override
   public Explosion newExplosion(Entity par1Entity, double par2, double par4, double par6, float par8, boolean par9, boolean par10) {
      Explosion var11 = new Explosion(this, par1Entity, par2, par4, par6, par8);
      var11.isFlaming = par9;
      var11.isSmoking = par10;
      var11.doExplosionA();
      var11.doExplosionB(false);
      if (!par10) {
         var11.affectedBlockPositions.clear();
      }

      for (EntityPlayer var13 : this.playerEntities) {
         if (var13.e(par2, par4, par6) < 4096.0) {
            ((EntityPlayerMP)var13)
               .playerNetServerHandler
               .sendPacketToPlayer(new Packet60Explosion(par2, par4, par6, par8, var11.affectedBlockPositions, (Vec3)var11.func_77277_b().get(var13)));
         }
      }

      return var11;
   }

   @Override
   public void addBlockEvent(int par1, int par2, int par3, int par4, int par5, int par6) {
      BlockEventData var7 = new BlockEventData(par1, par2, par3, par4, par5, par6);

      for (BlockEventData var9 : this.blockEventCache[this.blockEventCacheIndex]) {
         if (var9.equals(var7)) {
            return;
         }
      }

      this.blockEventCache[this.blockEventCacheIndex].add(var7);
   }

   private void sendAndApplyBlockEvents() {
      while (!this.blockEventCache[this.blockEventCacheIndex].isEmpty()) {
         int var1 = this.blockEventCacheIndex;
         this.blockEventCacheIndex ^= 1;

         for (BlockEventData var3 : this.blockEventCache[var1]) {
            if (this.onBlockEventReceived(var3)) {
               this.mcServer
                  .getConfigurationManager()
                  .sendToAllNear(
                     var3.getX(),
                     var3.getY(),
                     var3.getZ(),
                     64.0,
                     this.provider.dimensionId,
                     new Packet54PlayNoteBlock(var3.getX(), var3.getY(), var3.getZ(), var3.getBlockID(), var3.getEventID(), var3.getEventParameter())
                  );
            }
         }

         this.blockEventCache[var1].clear();
      }
   }

   private boolean onBlockEventReceived(BlockEventData par1BlockEventData) {
      int var2 = this.a(par1BlockEventData.getX(), par1BlockEventData.getY(), par1BlockEventData.getZ());
      return var2 == par1BlockEventData.getBlockID()
         ? Block.blocksList[var2]
            .onBlockEventReceived(
               this,
               par1BlockEventData.getX(),
               par1BlockEventData.getY(),
               par1BlockEventData.getZ(),
               par1BlockEventData.getEventID(),
               par1BlockEventData.getEventParameter()
            )
         : false;
   }

   public void flush() {
      this.saveHandler.flush();
   }

   public MinecraftServer getMinecraftServer() {
      return this.mcServer;
   }

   public EntityTracker getEntityTracker() {
      return this.theEntityTracker;
   }

   public Teleporter getDefaultTeleporter() {
      return this.field_85177_Q;
   }

   @Override
   public void modSpecificTick() {
      if (!this.hasTicked) {
         this.hasTicked = true;
         this.markChunksAroundSpawnToCheckForUnload();
      }

      this.checkChunksToUnloadList();
   }

   public void addChunkToCheckForUnloadList(int iChunkX, int iChunkZ) {
      this.chunksToCheckForUnloadList.add(new ChunkCoordIntPair(iChunkX, iChunkZ));
   }

   public void addChunkRangeToCheckForUnloadList(int iMinChunkX, int iMinChunkZ, int iMaxChunkX, int iMaxChunkZ) {
      for (int iTempChunkX = iMinChunkX; iTempChunkX <= iMaxChunkX; iTempChunkX++) {
         for (int iTempChunkZ = iMinChunkZ; iTempChunkZ <= iMaxChunkZ; iTempChunkZ++) {
            this.addChunkToCheckForUnloadList(iTempChunkX, iTempChunkZ);
         }
      }
   }

   private void checkChunksToUnloadList() {
      if (!this.chunksToCheckForUnloadList.isEmpty()) {
         for (ChunkCoordIntPair tempCoord : this.chunksToCheckForUnloadList) {
            if (this.checkChunkShouldBeUnloaded(tempCoord.chunkXPos, tempCoord.chunkZPos)) {
               this.theChunkProviderServer.forceAddToChunksToUnload(tempCoord.chunkXPos, tempCoord.chunkZPos);
            }
         }

         this.chunksToCheckForUnloadList.clear();
      }
   }

   private boolean checkChunkShouldBeUnloaded(int iChunkX, int iChunkZ) {
      return this.c(iChunkX, iChunkZ)
         && !this.chunkTracker.isChunkBeingWatched(iChunkX, iChunkZ)
         && !this.theChunkProviderServer.isSpawnChunk(iChunkX, iChunkZ);
   }

   private void markChunksAroundSpawnToCheckForUnload() {
      if (this.provider.canRespawnHere()) {
         int iSpawnChunkX = this.worldInfo.getSpawnX() >> 4;
         int iSpawnChunkZ = this.worldInfo.getSpawnZ() >> 4;
         this.addChunkRangeToCheckForUnloadList(iSpawnChunkX - 13, iSpawnChunkZ - 13, iSpawnChunkX + 13, iSpawnChunkZ + 13);
      }
   }

   @Override
   public boolean isUpdateScheduledForBlock(int i, int j, int k, int iBlockID) {
      NextTickListEntry tempEntry = new NextTickListEntry(i, j, k, iBlockID);
      return this.field_73064_N.contains(tempEntry);
   }

   @Override
   protected void updateWeather() {
      super.updateWeather();
      if (this.worldInfo.previouslyRaining != this.worldInfo.isRaining()) {
         if (this.worldInfo.isRaining()) {
            this.mcServer.getConfigurationManager().sendPacketToAllPlayers(new Packet70GameEvent(1, 0));
         } else {
            this.mcServer.getConfigurationManager().sendPacketToAllPlayers(new Packet70GameEvent(2, 0));
         }

         this.worldInfo.previouslyRaining = this.worldInfo.isRaining();
      }

      if (this.worldInfo.previouslyThundering != this.worldInfo.isThundering()) {
         if (this.worldInfo.isThundering()) {
            this.mcServer.getConfigurationManager().sendPacketToAllPlayers(new Packet70GameEvent(7, 0));
         } else {
            this.mcServer.getConfigurationManager().sendPacketToAllPlayers(new Packet70GameEvent(8, 0));
         }

         this.worldInfo.previouslyThundering = this.worldInfo.isThundering();
      }
   }

   private void modUpdateTick() {
      this.validateMagneticPointList();
      this.validateAmbientBeaconList();
      this.validateSpawnLocationList();
   }

   private void validateMagneticPointList() {
      int iTimeFactor = (int)this.I();
      if ((iTimeFactor & 15) == 0) {
         int iListLength = this.magneticPointList.magneticPoints.size();
         if (iListLength > 0) {
            iTimeFactor >>= 4;
            int iTempIndex = iTimeFactor % iListLength;
            MagneticPoint tempPoint = (MagneticPoint)this.magneticPointList.magneticPoints.get(iTempIndex);
            if (this.e(tempPoint.posX, 0, tempPoint.posZ, tempPoint.posX, 0, tempPoint.posZ)
               && this.a(tempPoint.posX, tempPoint.posY, tempPoint.posZ) != Block.beacon.blockID) {
               this.magneticPointList.magneticPoints.remove(iTempIndex);
            }
         }
      }
   }

   private void validateAmbientBeaconList() {
      int timeFactor = (int)this.I();
      if ((timeFactor & 15) == 0) {
         int listLength = this.ambientBeaconLocationList.effectLocations.size();
         if (listLength > 0) {
            timeFactor >>= 4;
            int i = timeFactor % listLength;
            BeaconEffectLocation tempPoint = (BeaconEffectLocation)this.ambientBeaconLocationList.effectLocations.get(i);
            if (this.e(tempPoint.posX, 0, tempPoint.posZ, tempPoint.posX, 0, tempPoint.posZ)
               && this.a(tempPoint.posX, tempPoint.posY, tempPoint.posZ) != Block.beacon.blockID) {
               this.ambientBeaconLocationList.effectLocations.remove(i);
            }
         }
      }
   }

   private void validateSpawnLocationList() {
      long lWorldTime = this.I();
      if ((lWorldTime & 15L) == 0L) {
         Iterator tempIterator = this.spawnLocationList.spawnLocations.iterator();

         while (tempIterator.hasNext()) {
            SpawnLocation tempPoint = (SpawnLocation)tempIterator.next();
            if (lWorldTime < tempPoint.spawnTime || lWorldTime - tempPoint.spawnTime > 10800L) {
               tempIterator.remove();
            }
         }
      }
   }

   protected BlockPos getAdjustedLightningStrikeLocation(BlockPos strikePos) {
      BlockPos entityCheckPos = this.getHighestEntityForLightningStrike(strikePos, 16).orElse(strikePos);
      BlockPos firstBlockPassPos = this.getHighestPointForLightningStrike(entityCheckPos, 16, false).orElse(entityCheckPos);
      BlockPos secondBlockPassPos = this.getHighestPointForLightningStrike(firstBlockPassPos, 16, false).orElse(firstBlockPassPos);
      return this.getHighestPointForLightningStrike(secondBlockPassPos, 32, BTWBlocks.lightningRod.blockID, true).orElse(secondBlockPassPos);
   }

   protected Optional<BlockPos> getHighestPointForLightningStrike(BlockPos strikePos, int radius, boolean ignoreBiomeCheck) {
      return this.getHighestPointForLightningStrike(strikePos, radius, new HashSet<>(), ignoreBiomeCheck);
   }

   protected Optional<BlockPos> getHighestPointForLightningStrike(BlockPos strikePos, int radius, int blockID, boolean ignoreBiomeCheck) {
      Set<Integer> blockIDs = new HashSet<>();
      blockIDs.add(blockID);
      return this.getHighestPointForLightningStrike(strikePos, radius, blockIDs, ignoreBiomeCheck);
   }

   protected Optional<BlockPos> getHighestPointForLightningStrike(BlockPos strikePos, int radius, Set<Integer> blockIDs, boolean ignoreBiomeCheck) {
      int minX = strikePos.x - radius;
      int maxX = strikePos.x + radius;
      int minZ = strikePos.z - radius;
      int maxZ = strikePos.z + radius;
      int strikeX = 0;
      int strikeY = strikePos.y;
      int strikeZ = 0;
      boolean foundValidStrikeLocation = false;

      for (int i = minX; i <= maxX; i++) {
         for (int k = minZ; k <= maxZ; k++) {
            int j = this.h(i, k);
            if (j > strikeY && (this.canLightningStrikeAtPos(i, j, k) || ignoreBiomeCheck) && (blockIDs.isEmpty() || blockIDs.contains(this.a(i, j - 1, k)))) {
               strikeX = i;
               strikeY = j;
               strikeZ = k;
               foundValidStrikeLocation = true;
            }
         }
      }

      return foundValidStrikeLocation ? Optional.of(new BlockPos(strikeX, strikeY, strikeZ)) : Optional.empty();
   }

   protected Optional<BlockPos> getHighestEntityForLightningStrike(BlockPos strikePos, int radius) {
      int minX = strikePos.x - radius;
      int maxX = strikePos.x + radius;
      int minZ = strikePos.z - radius;
      int maxZ = strikePos.z + radius;
      int strikeX = 0;
      int strikeY = strikePos.y;
      int strikeZ = 0;
      boolean foundValidStrikeLocation = false;

      for (Entity entity : this.a(Entity.class, AxisAlignedBB.getAABBPool().getAABB(minX, strikeY, minZ, maxX + 1, 256.0, maxZ + 1))) {
         if (entity.isEntityAlive() && entity.attractsLightning()) {
            int entityMaxY = (int)entity.boundingBox.maxY + 1;
            if (entityMaxY > strikeY) {
               int entityX = MathHelper.floor_double(entity.posX);
               int entityZ = MathHelper.floor_double(entity.posZ);
               int blockHeightAtEntity = this.h(entityX, entityZ);
               if (blockHeightAtEntity <= entityMaxY && this.canLightningStrikeAtPos(entityX, blockHeightAtEntity, entityZ)) {
                  strikeX = entityX;
                  strikeY = blockHeightAtEntity;
                  strikeZ = entityZ;
                  foundValidStrikeLocation = true;
               }
            }
         }
      }

      return foundValidStrikeLocation ? Optional.of(new BlockPos(strikeX, strikeY, strikeZ)) : Optional.empty();
   }

   @Override
   public int getClampedViewDistanceInChunks() {
      int iRange = this.getMinecraftServer().getConfigurationManager().getViewDistance();
      return MathHelper.clamp_int(iRange, 3, 15);
   }

   @Override
   protected void updateActiveChunkMap() {
      super.updateActiveChunkMap();
      this.updateServerIdleState();
      if (this.provider.dimensionId == 0 && !this.isServerIdle()) {
         ChunkCoordinates originalSpawn = this.J();
         this.addAreaAroundChunkToActiveChunkMap(originalSpawn.posX >> 4, originalSpawn.posZ >> 4);
      }
   }

   public ChunkTracker getChunkTracker() {
      return this.chunkTracker;
   }

   protected void updateServerIdleState() {
      if (!this.areAnyPlayersOnServer()) {
         this.noPlayersOnServerTickCount++;
      } else {
         this.noPlayersOnServerTickCount = 0L;
      }
   }

   protected boolean isServerIdle() {
      return this.noPlayersOnServerTickCount >= 1200L;
   }

   protected boolean areAnyPlayersOnServer() {
      return this.mcServer.getCurrentPlayerCount() > 0;
   }

   public void saveWorldDataToNBT(File dimensionDirectory) {
      for (Entry<Class<? extends BTWAddon>, WorldData> entry : AddonHandler.addonWorldDataMap.entrySet()) {
         NBTTagCompound modData = new NBTTagCompound();
         entry.getValue().saveWorldDataToNBT(this, modData);
         NBTTagCompound fileData = new NBTTagCompound();
         fileData.setTag("Data", modData);

         try {
            File modSaveFile = new File(dimensionDirectory, entry.getValue().getFilename() + ".dat");
            if (modSaveFile.exists()) {
               modSaveFile.delete();
            }

            CompressedStreamTools.writeCompressed(fileData, new FileOutputStream(modSaveFile));
         } catch (Exception var7) {
            var7.printStackTrace();
         }
      }
   }

   public void loadWorldDataFromNBT(File dimensionDirectory) {
      for (Entry<Class<? extends BTWAddon>, WorldData> entry : AddonHandler.addonWorldDataMap.entrySet()) {
         File modSaveFile = null;

         try {
            modSaveFile = new File(dimensionDirectory, entry.getValue().getFilename() + ".dat");
         } catch (Exception var8) {
            var8.printStackTrace();
         }

         if (modSaveFile != null && modSaveFile.exists()) {
            try {
               NBTTagCompound fileTag = CompressedStreamTools.readCompressed(new FileInputStream(modSaveFile));
               NBTTagCompound dataTag = fileTag.getCompoundTag("Data");
               entry.getValue().loadWorldDataFromNBT(this, dataTag);
            } catch (Exception var7) {
               var7.printStackTrace();
            }
         }
      }
   }

   public float getMinSpeedModifier() {
      if (!this.isRemote && !this.playerEntities.isEmpty()) {
         float minSpeedModifier = Float.MAX_VALUE;

         for (EntityPlayer player : this.playerEntities) {
            if (player != null) {
               float speedModifier = player.getTimerSpeedModifier();
               if (speedModifier < minSpeedModifier) {
                  minSpeedModifier = speedModifier;
               }
            }
         }

         return minSpeedModifier;
      } else {
         return 1.0F;
      }
   }
}
