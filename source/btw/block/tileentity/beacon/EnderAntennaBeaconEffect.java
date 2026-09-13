package btw.block.tileentity.beacon;

import btw.entity.mob.EndermanEntity;
import net.minecraft.src.Block;
import net.minecraft.src.Chunk;
import net.minecraft.src.EntityList;
import net.minecraft.src.EntitySilverfish;
import net.minecraft.src.EnumCreatureType;
import net.minecraft.src.Material;

public class EnderAntennaBeaconEffect extends BeaconEffect {
   public static final int[] enderAntennaBeaconSpawnRange = new int[]{0, 8, 16, 32, 64};
   private static final int ENDERMAN_SPAWN_FREQUENCY = 1200;
   public static final int[] endermanSpawnChancePerLevel = new int[]{0, 1, 8, 27, 64};
   private static final int SILVERFISH_SPAWN_FREQUENCY = 1200;
   public static final int[] silverfishSpawnChancePerLevel = new int[]{0, 1, 8, 27, 64};

   @Override
   public void onUpdate(BeaconTileEntity beacon) {
      if (!beacon.worldObj.isRemote) {
         if (beacon.worldObj.provider.dimensionId != 1) {
            this.checkForEndermanSpawn(beacon);
         } else {
            this.checkForSilverfishSpawn(beacon);
         }
      }
   }

   private void checkForEndermanSpawn(BeaconTileEntity beacon) {
      EnumCreatureType creatureType = EnumCreatureType.monster;
      int mobCount = beacon.worldObj.countEntitiesThatApplyToSpawnCap(creatureType.getCreatureClass());
      int maxMobCount = creatureType.getMaxNumberOfCreature();
      maxMobCount += maxMobCount >> 2;
      if (mobCount < maxMobCount) {
         int beaconLevel = beacon.l();
         if (beacon.worldObj.rand.nextInt(1200) < endermanSpawnChancePerLevel[beaconLevel]) {
            int range = enderAntennaBeaconSpawnRange[beaconLevel];
            int x = beacon.xCoord + beacon.worldObj.rand.nextInt(range * 2 + 1) - range;
            int z = beacon.zCoord + beacon.worldObj.rand.nextInt(range * 2 + 1) - range;
            Chunk chunk = beacon.worldObj.getChunkFromBlockCoords(x, z);
            int y;
            if (chunk == null) {
               y = beacon.worldObj.rand.nextInt(beacon.worldObj.getActualHeight());
            } else {
               y = beacon.worldObj.rand.nextInt(chunk.getTopFilledSegment() + 16 - 1);
            }

            Material targetMaterial = beacon.worldObj.getBlockMaterial(x, y, z);
            Block blockBelow = Block.blocksList[beacon.worldObj.getBlockId(x, y - 1, z)];
            if (!targetMaterial.isSolid() && !targetMaterial.isLiquid() && blockBelow != null && blockBelow.canMobsSpawnOn(beacon.worldObj, x, y - 1, z)) {
               EndermanEntity enderman = (EndermanEntity)EntityList.createEntityOfType(EndermanEntity.class, beacon.worldObj);
               enderman.preInitCreature();
               double spawnOffset = blockBelow.mobSpawnOnVerticalOffset(beacon.worldObj, x, y - 1, z);
               enderman.b(x + 0.5, y + spawnOffset, z + 0.5, beacon.worldObj.rand.nextFloat() * 360.0F, 0.0F);
               if (beacon.worldObj.checkNoEntityCollision(enderman.boundingBox)
                  && beacon.worldObj.getCollidingBoundingBoxes(enderman, enderman.boundingBox).isEmpty()
                  && !beacon.worldObj.isAnyLiquid(enderman.boundingBox)) {
                  beacon.worldObj.spawnEntityInWorld(enderman);
                  enderman.initCreature();
                  beacon.worldObj.playAuxSFX(2247, x, y, z, 0);
               }
            }
         }
      }
   }

   private void checkForSilverfishSpawn(BeaconTileEntity beacon) {
      EnumCreatureType creatureType = EnumCreatureType.monster;
      int mobCount = beacon.worldObj.countEntitiesThatApplyToSpawnCap(creatureType.getCreatureClass());
      int maxMobCount = creatureType.getMaxNumberOfCreature();
      maxMobCount += maxMobCount >> 2;
      if (mobCount < maxMobCount) {
         int beaconLevel = beacon.l();
         if (beacon.worldObj.rand.nextInt(1200) < silverfishSpawnChancePerLevel[beaconLevel]) {
            int range = enderAntennaBeaconSpawnRange[beaconLevel];
            int x = beacon.xCoord + beacon.worldObj.rand.nextInt(range * 2 + 1) - range;
            int z = beacon.zCoord + beacon.worldObj.rand.nextInt(range * 2 + 1) - range;
            Chunk chunk = beacon.worldObj.getChunkFromBlockCoords(x, z);
            int y;
            if (chunk == null) {
               y = beacon.worldObj.rand.nextInt(beacon.worldObj.getActualHeight());
            } else {
               y = beacon.worldObj.rand.nextInt(chunk.getTopFilledSegment() + 16 - 1);
            }

            Material targetMaterial = beacon.worldObj.getBlockMaterial(x, y, z);
            Block blockBelow = Block.blocksList[beacon.worldObj.getBlockId(x, y - 1, z)];
            if (!targetMaterial.isSolid() && !targetMaterial.isLiquid() && blockBelow != null && blockBelow.canMobsSpawnOn(beacon.worldObj, x, y - 1, z)) {
               EntitySilverfish silverfish = (EntitySilverfish)EntityList.createEntityOfType(EntitySilverfish.class, beacon.worldObj);
               silverfish.preInitCreature();
               double spawnOffset = blockBelow.mobSpawnOnVerticalOffset(beacon.worldObj, x, y - 1, z);
               silverfish.b(x + 0.5, y + spawnOffset, z + 0.5, beacon.worldObj.rand.nextFloat() * 360.0F, 0.0F);
               if (beacon.worldObj.checkNoEntityCollision(silverfish.boundingBox)
                  && beacon.worldObj.getCollidingBoundingBoxes(silverfish, silverfish.boundingBox).isEmpty()
                  && !beacon.worldObj.isAnyLiquid(silverfish.boundingBox)) {
                  beacon.worldObj.spawnEntityInWorld(silverfish);
                  silverfish.bJ();
                  beacon.worldObj.playAuxSFX(2247, x, y, z, 0);
               }
            }
         }
      }
   }
}
