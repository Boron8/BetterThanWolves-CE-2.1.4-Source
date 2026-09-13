package btw.util;

import btw.block.BTWBlocks;
import btw.block.FluidSource;
import btw.block.blocks.ScrewPumpBlock;
import btw.world.util.BlockPos;
import btw.world.util.WorldUtils;
import java.util.List;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.BiomeGenBase;
import net.minecraft.src.Block;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityAnimal;
import net.minecraft.src.EntityCreature;
import net.minecraft.src.EntityItem;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EntityPlayerMP;
import net.minecraft.src.Facing;
import net.minecraft.src.Material;
import net.minecraft.src.MathHelper;
import net.minecraft.src.MovingObjectPosition;
import net.minecraft.src.Packet13PlayerLookMove;
import net.minecraft.src.Vec3;
import net.minecraft.src.World;

public class MiscUtils {
   public static final int TICKS_PER_SECOND = 20;
   public static final int TICKS_PER_MINUTE = 1200;
   public static final int TICKS_PER_GAME_DAY = 24000;

   public static int convertPlacingEntityOrientationToBlockFacingReversed(EntityLiving entityLiving) {
      float pitch = entityLiving.rotationPitch;
      if (pitch > 60.0F) {
         return entityLiving.isUsingSpecialKey() ? 0 : 1;
      } else if (pitch < -60.0F) {
         return entityLiving.isUsingSpecialKey() ? 1 : 0;
      } else {
         return convertOrientationToFlatBlockFacingReversed(entityLiving);
      }
   }

   public static int convertOrientationToFlatBlockFacingReversed(EntityLiving entityLiving) {
      int l = MathHelper.floor_double(entityLiving.rotationYaw * 4.0F / 360.0F + 0.5) & 3;
      int iFacing;
      if (l == 0) {
         iFacing = 2;
      } else if (l == 1) {
         iFacing = 5;
      } else if (l == 2) {
         iFacing = 3;
      } else {
         iFacing = 4;
      }

      if (entityLiving.isUsingSpecialKey()) {
         iFacing = Facing.oppositeSide[iFacing];
      }

      return iFacing;
   }

   public static int convertOrientationToFlatBlockFacing(EntityLiving entityLiving) {
      int l = MathHelper.floor_double(entityLiving.rotationYaw * 4.0F / 360.0F + 0.5) & 3;
      int iFacing;
      if (l == 0) {
         iFacing = 3;
      } else if (l == 1) {
         iFacing = 4;
      } else if (l == 2) {
         iFacing = 2;
      } else {
         iFacing = 5;
      }

      if (entityLiving.isUsingSpecialKey()) {
         iFacing = Facing.oppositeSide[iFacing];
      }

      return iFacing;
   }

   public static boolean isIKInColdBiome(World world, int i, int k) {
      BiomeGenBase biomegenbase = world.getBiomeGenForCoords(i, k);
      float f = biomegenbase.getFloatTemperature();
      return !(f > 0.15F);
   }

   public static void positionAllNonPlayerMoveableEntitiesOutsideOfLocation(World world, int i, int j, int k) {
      List list = world.getEntitiesWithinAABBExcludingEntity(null, AxisAlignedBB.getAABBPool().getAABB(i, j, k, i + 1.0, j + 1.0, k + 1.0));
      if (list != null && list.size() > 0) {
         for (int listIndex = 0; listIndex < list.size(); listIndex++) {
            Entity entity = (Entity)list.get(listIndex);
            if ((entity.canBePushed() || entity instanceof EntityItem) && !(entity instanceof EntityPlayer)) {
               positionEntityOutsideOfLocation(world, entity, i, j, k);
            }
         }
      }
   }

   private static void positionEntityOutsideOfLocation(World world, Entity entity, int i, int j, int k) {
      double minPosX = i;
      double minPosY = j;
      double minPosZ = k;
      double maxPosX = i + 1;
      double maxPosY = j + 1;
      double maxPosZ = k + 1;
      boolean xOverlap = false;
      boolean yOverlap = false;
      boolean zOverlap = false;
      double xOffset = 0.0;
      double yOffset = 0.0;
      double zOffset = 0.0;
      if (entity.boundingBox.minX <= maxPosX && entity.boundingBox.maxX >= minPosX) {
         xOverlap = true;
         if (Math.abs(maxPosX - entity.boundingBox.minX) < Math.abs(minPosX - entity.boundingBox.maxX)) {
            xOffset = maxPosX - entity.boundingBox.minX + 0.01;
         } else {
            xOffset = minPosX - entity.boundingBox.maxX - 0.01;
         }
      }

      if (entity.boundingBox.minY <= maxPosY && entity.boundingBox.maxY >= minPosY) {
         yOverlap = true;
         if (Math.abs(maxPosY - entity.boundingBox.minY) < Math.abs(minPosY - entity.boundingBox.maxY)) {
            yOffset = maxPosY - entity.boundingBox.minY + 0.01;
         } else {
            yOffset = minPosY - entity.boundingBox.maxY - 0.01;
         }
      }

      if (entity.boundingBox.minZ <= maxPosZ && entity.boundingBox.maxZ >= minPosZ) {
         zOverlap = true;
         if (Math.abs(maxPosZ - entity.boundingBox.minZ) < Math.abs(minPosZ - entity.boundingBox.maxZ)) {
            zOffset = maxPosZ - entity.boundingBox.minZ + 0.01;
         } else {
            zOffset = minPosZ - entity.boundingBox.maxZ - 0.01;
         }
      }

      double entityX = entity.posX;
      double entityY = entity.posY;
      double entityZ = entity.posZ;
      if (!xOverlap
         || !(Math.abs(xOffset) < 0.2)
         || yOverlap && !(Math.abs(xOffset) < Math.abs(yOffset))
         || zOverlap && !(Math.abs(xOffset) < Math.abs(zOffset))) {
         if (!yOverlap || !(Math.abs(yOffset) < 0.2) || zOverlap && !(Math.abs(yOffset) < Math.abs(zOffset))) {
            if (zOverlap && Math.abs(zOffset) < 0.2) {
               entityZ += zOffset;
            }
         } else {
            entityY += yOffset;
         }
      } else {
         entityX += xOffset;
      }

      entity.setPosition(entityX, entityY, entityZ);
      if (entity instanceof EntityPlayerMP) {
         EntityPlayerMP player = (EntityPlayerMP)entity;
         WorldUtils.sendPacketToPlayer(
            player.playerNetServerHandler,
            new Packet13PlayerLookMove(entityX, entityY + 1.62F, entityY, entityZ, player.rotationYaw, player.rotationPitch, false)
         );
      }
   }

   public static void serverPositionAllPlayerEntitiesOutsideOfLocation(World world, int i, int j, int k) {
      List list = world.getEntitiesWithinAABB(EntityPlayerMP.class, AxisAlignedBB.getAABBPool().getAABB(i, j, k, i + 1.0, j + 1.0, k + 1.0));
      if (list != null && list.size() > 0) {
         for (int listIndex = 0; listIndex < list.size(); listIndex++) {
            EntityPlayerMP player = (EntityPlayerMP)list.get(listIndex);
            serverPositionPlayerEntityOutsideOfLocation(world, player, i, j, k);
         }
      }
   }

   private static void serverPositionPlayerEntityOutsideOfLocation(World world, EntityPlayerMP player, int i, int j, int k) {
      double minPosX = i;
      double minPosY = j;
      double minPosZ = k;
      double maxPosX = i + 1;
      double maxPosY = j + 1;
      double maxPosZ = k + 1;
      boolean xOverlap = false;
      boolean yOverlap = false;
      boolean zOverlap = false;
      double xOffset = 0.0;
      double yOffset = 0.0;
      double zOffset = 0.0;
      if (player.boundingBox.minX <= maxPosX && player.boundingBox.maxX >= minPosX) {
         xOverlap = true;
         if (Math.abs(maxPosX - player.boundingBox.minX) < Math.abs(minPosX - player.boundingBox.maxX)) {
            xOffset = maxPosX - player.boundingBox.minX + 0.01;
         } else {
            xOffset = minPosX - player.boundingBox.maxX - 0.01;
         }
      }

      if (player.boundingBox.minY <= maxPosY && player.boundingBox.maxY >= minPosY) {
         yOverlap = true;
         if (Math.abs(maxPosY - player.boundingBox.minY) < Math.abs(minPosY - player.boundingBox.maxY)) {
            yOffset = maxPosY - player.boundingBox.minY + 0.01;
         } else {
            yOffset = minPosY - player.boundingBox.maxY - 0.01;
         }
      }

      if (player.boundingBox.minZ <= maxPosZ && player.boundingBox.maxZ >= minPosZ) {
         zOverlap = true;
         if (Math.abs(maxPosZ - player.boundingBox.minZ) < Math.abs(minPosZ - player.boundingBox.maxZ)) {
            zOffset = maxPosZ - player.boundingBox.minZ + 0.01;
         } else {
            zOffset = minPosZ - player.boundingBox.maxZ - 0.01;
         }
      }

      double entityX = player.posX;
      double entityY = player.posY;
      double entityZ = player.posZ;
      if (!xOverlap
         || !(Math.abs(xOffset) < 0.2)
         || yOverlap && !(Math.abs(xOffset) < Math.abs(yOffset))
         || zOverlap && !(Math.abs(xOffset) < Math.abs(zOffset))) {
         if (!yOverlap || !(Math.abs(yOffset) < 0.2) || zOverlap && !(Math.abs(yOffset) < Math.abs(zOffset))) {
            if (zOverlap && Math.abs(zOffset) < 0.2) {
               entityZ += zOffset;
            }
         } else {
            entityY += yOffset;
         }
      } else {
         entityX += xOffset;
      }

      player.b(entityX, entityY, entityZ);
      WorldUtils.sendPacketToPlayer(
         player.playerNetServerHandler, new Packet13PlayerLookMove(entityX, entityY + 1.62F, entityY, entityZ, player.rotationYaw, player.rotationPitch, false)
      );
   }

   public static void playPlaceSoundForBlock(World world, int i, int j, int k) {
      int iTargetBlockID = world.getBlockId(i, j, k);
      Block targetBlock = Block.blocksList[iTargetBlockID];
      if (targetBlock != null) {
         world.playSoundEffect(
            i + 0.5F,
            j + 0.5F,
            k + 0.5F,
            targetBlock.stepSound.getPlaceSound(),
            (targetBlock.stepSound.getPlaceVolume() + 1.0F) / 2.0F,
            targetBlock.stepSound.getPlacePitch() * 0.8F
         );
      }
   }

   public static boolean isCreatureWearingBreedingHarness(EntityCreature creature) {
      if (creature instanceof EntityAnimal) {
         EntityAnimal animal = (EntityAnimal)creature;
         return animal.getWearingBreedingHarness();
      } else {
         return false;
      }
   }

   public static boolean standardRotateAroundY(Block block, World world, int i, int j, int k, boolean bReverse) {
      int iMetadata = world.getBlockMetadata(i, j, k);
      int iNewMetadata = standardRotateMetadataAroundY(block, iMetadata, bReverse);
      if (iNewMetadata != iMetadata) {
         world.setBlockMetadataWithNotify(i, j, k, iNewMetadata);
         world.markBlockRangeForRenderUpdate(i, j, k, i, j, k);
         return true;
      } else {
         return false;
      }
   }

   public static int standardRotateMetadataAroundY(Block block, int iMetadata, boolean bReverse) {
      int iFacing = block.getFacing(iMetadata);
      int iNewFacing = Block.rotateFacingAroundY(iFacing, bReverse);
      return block.setFacing(iMetadata, iNewFacing);
   }

   public static Vec3 convertBlockFacingToVector(int iFacing) {
      Vec3 vector = Vec3.createVectorHelper(0.0, 0.0, 0.0);
      switch (iFacing) {
         case 0:
            vector.yCoord += -1.0;
            break;
         case 1:
            vector.yCoord++;
            break;
         case 2:
            vector.zCoord--;
            break;
         case 3:
            vector.zCoord++;
            break;
         case 4:
            vector.xCoord--;
            break;
         default:
            vector.xCoord++;
      }

      return vector;
   }

   public static void placeNonPersistentWater(World world, int i, int j, int k) {
      world.setBlockAndMetadataWithNotify(i, j, k, Block.waterMoving.blockID, 1);
      flowWaterIntoBlockIfPossible(world, i + 1, j, k, 2);
      flowWaterIntoBlockIfPossible(world, i - 1, j, k, 2);
      flowWaterIntoBlockIfPossible(world, i, j, k + 1, 2);
      flowWaterIntoBlockIfPossible(world, i, j, k - 1, 2);
   }

   public static void placeNonPersistentWaterMinorSpread(World world, int i, int j, int k) {
      int iSpread = 5;
      world.setBlockAndMetadataWithNotify(i, j, k, Block.waterMoving.blockID, iSpread);
      flowWaterIntoBlockSafe(world, i + 1, j, k, iSpread + 1);
      flowWaterIntoBlockSafe(world, i - 1, j, k, iSpread + 1);
      flowWaterIntoBlockSafe(world, i, j, k + 1, iSpread + 1);
      flowWaterIntoBlockSafe(world, i, j, k - 1, iSpread + 1);
   }

   public static void flowWaterIntoBlockSafe(World world, int i, int j, int k, int iDecayLevel) {
      if (world.isAirBlock(i, j, k)) {
         flowWaterIntoBlockIfPossible(world, i, j, k, iDecayLevel);
      }
   }

   public static void flowWaterIntoBlockIfPossible(World world, int i, int j, int k, int iDecayLevel) {
      if (canWaterDisplaceBlock(world, i, j, k)) {
         int iTargetBlockID = world.getBlockId(i, j, k);
         if (iTargetBlockID > 0) {
            Block.blocksList[iTargetBlockID].onFluidFlowIntoBlock(world, i, j, k, Block.waterMoving);
         }

         world.setBlockAndMetadataWithNotify(i, j, k, Block.waterMoving.blockID, iDecayLevel);
      }
   }

   public static boolean canWaterDisplaceBlock(World world, int i, int j, int k) {
      Material material = world.getBlockMaterial(i, j, k);
      if (material == Block.waterMoving.blockMaterial) {
         return false;
      } else if (material == Material.lava) {
         return false;
      } else {
         Block block = Block.blocksList[world.getBlockId(i, j, k)];
         return block == null || !block.getPreventsFluidFlow(world, i, j, k, Block.waterMoving);
      }
   }

   public static MovingObjectPosition getMovingObjectPositionFromPlayerHitWaterAndLava(World par1World, EntityPlayer par2EntityPlayer, boolean par3) {
      float f = 1.0F;
      float f1 = par2EntityPlayer.prevRotationPitch + (par2EntityPlayer.rotationPitch - par2EntityPlayer.prevRotationPitch) * f;
      float f2 = par2EntityPlayer.prevRotationYaw + (par2EntityPlayer.rotationYaw - par2EntityPlayer.prevRotationYaw) * f;
      double d = par2EntityPlayer.prevPosX + (par2EntityPlayer.posX - par2EntityPlayer.prevPosX) * f;
      double d1 = par2EntityPlayer.prevPosY + (par2EntityPlayer.posY - par2EntityPlayer.prevPosY) * f + 1.62 - par2EntityPlayer.yOffset;
      double d2 = par2EntityPlayer.prevPosZ + (par2EntityPlayer.posZ - par2EntityPlayer.prevPosZ) * f;
      Vec3 vec3 = par1World.getWorldVec3Pool().getVecFromPool(d, d1, d2);
      float f3 = MathHelper.cos(-f2 * 0.01745329F - (float) Math.PI);
      float f4 = MathHelper.sin(-f2 * 0.01745329F - (float) Math.PI);
      float f5 = -MathHelper.cos(-f1 * 0.01745329F);
      float f6 = MathHelper.sin(-f1 * 0.01745329F);
      float f7 = f4 * f5;
      float f9 = f3 * f5;
      double d3 = 5.0;
      Vec3 vec3_1 = vec3.addVector(f7 * d3, f6 * d3, f9 * d3);
      return WorldUtils.rayTraceBlocksAlwaysHitWaterAndLava(par1World, vec3, vec3_1, par3, !par3);
   }

   public static MovingObjectPosition getMovingObjectPositionFromPlayerHitWaterAndLavaAndFire(World par1World, EntityPlayer par2EntityPlayer, boolean par3) {
      float f = 1.0F;
      float f1 = par2EntityPlayer.prevRotationPitch + (par2EntityPlayer.rotationPitch - par2EntityPlayer.prevRotationPitch) * f;
      float f2 = par2EntityPlayer.prevRotationYaw + (par2EntityPlayer.rotationYaw - par2EntityPlayer.prevRotationYaw) * f;
      double d = par2EntityPlayer.prevPosX + (par2EntityPlayer.posX - par2EntityPlayer.prevPosX) * f;
      double d1 = par2EntityPlayer.prevPosY + (par2EntityPlayer.posY - par2EntityPlayer.prevPosY) * f + 1.62 - par2EntityPlayer.yOffset;
      double d2 = par2EntityPlayer.prevPosZ + (par2EntityPlayer.posZ - par2EntityPlayer.prevPosZ) * f;
      Vec3 vec3 = par1World.getWorldVec3Pool().getVecFromPool(d, d1, d2);
      float f3 = MathHelper.cos(-f2 * 0.01745329F - (float) Math.PI);
      float f4 = MathHelper.sin(-f2 * 0.01745329F - (float) Math.PI);
      float f5 = -MathHelper.cos(-f1 * 0.01745329F);
      float f6 = MathHelper.sin(-f1 * 0.01745329F);
      float f7 = f4 * f5;
      float f9 = f3 * f5;
      double d3 = 5.0;
      Vec3 vec3_1 = vec3.addVector(f7 * d3, f6 * d3, f9 * d3);
      return WorldUtils.rayTraceBlocksAlwaysHitWaterAndLavaAndFire(par1World, vec3, vec3_1, par3, !par3);
   }

   public static MovingObjectPosition rayTraceWithBox(World world, int i, int j, int k, Vec3 boxMin, Vec3 boxMax, Vec3 startRay, Vec3 endRay) {
      startRay = startRay.addVector(-i, -j, -k);
      endRay = endRay.addVector(-i, -j, -k);
      Vec3 vec3 = startRay.getIntermediateWithXValue(endRay, boxMin.xCoord);
      Vec3 vec3_1 = startRay.getIntermediateWithXValue(endRay, boxMax.xCoord);
      Vec3 vec3_2 = startRay.getIntermediateWithYValue(endRay, boxMin.yCoord);
      Vec3 vec3_3 = startRay.getIntermediateWithYValue(endRay, boxMax.yCoord);
      Vec3 vec3_4 = startRay.getIntermediateWithZValue(endRay, boxMin.zCoord);
      Vec3 vec3_5 = startRay.getIntermediateWithZValue(endRay, boxMax.zCoord);
      if (!isVecInsideYZBounds(vec3, boxMin, boxMax)) {
         vec3 = null;
      }

      if (!isVecInsideYZBounds(vec3_1, boxMin, boxMax)) {
         vec3_1 = null;
      }

      if (!isVecInsideXZBounds(vec3_2, boxMin, boxMax)) {
         vec3_2 = null;
      }

      if (!isVecInsideXZBounds(vec3_3, boxMin, boxMax)) {
         vec3_3 = null;
      }

      if (!isVecInsideXYBounds(vec3_4, boxMin, boxMax)) {
         vec3_4 = null;
      }

      if (!isVecInsideXYBounds(vec3_5, boxMin, boxMax)) {
         vec3_5 = null;
      }

      Vec3 vec3_6 = null;
      if (vec3 != null && (vec3_6 == null || startRay.squareDistanceTo(vec3) < startRay.squareDistanceTo(vec3_6))) {
         vec3_6 = vec3;
      }

      if (vec3_1 != null && (vec3_6 == null || startRay.squareDistanceTo(vec3_1) < startRay.squareDistanceTo(vec3_6))) {
         vec3_6 = vec3_1;
      }

      if (vec3_2 != null && (vec3_6 == null || startRay.squareDistanceTo(vec3_2) < startRay.squareDistanceTo(vec3_6))) {
         vec3_6 = vec3_2;
      }

      if (vec3_3 != null && (vec3_6 == null || startRay.squareDistanceTo(vec3_3) < startRay.squareDistanceTo(vec3_6))) {
         vec3_6 = vec3_3;
      }

      if (vec3_4 != null && (vec3_6 == null || startRay.squareDistanceTo(vec3_4) < startRay.squareDistanceTo(vec3_6))) {
         vec3_6 = vec3_4;
      }

      if (vec3_5 != null && (vec3_6 == null || startRay.squareDistanceTo(vec3_5) < startRay.squareDistanceTo(vec3_6))) {
         vec3_6 = vec3_5;
      }

      if (vec3_6 == null) {
         return null;
      } else {
         byte byte0 = -1;
         if (vec3_6 == vec3) {
            byte0 = 4;
         }

         if (vec3_6 == vec3_1) {
            byte0 = 5;
         }

         if (vec3_6 == vec3_2) {
            byte0 = 0;
         }

         if (vec3_6 == vec3_3) {
            byte0 = 1;
         }

         if (vec3_6 == vec3_4) {
            byte0 = 2;
         }

         if (vec3_6 == vec3_5) {
            byte0 = 3;
         }

         return new MovingObjectPosition(i, j, k, byte0, vec3_6.addVector(i, j, k));
      }
   }

   public static boolean isVecInsideYZBounds(Vec3 par1Vec3, Vec3 min, Vec3 max) {
      return par1Vec3 == null
         ? false
         : par1Vec3.yCoord >= min.yCoord && par1Vec3.yCoord <= max.yCoord && par1Vec3.zCoord >= min.zCoord && par1Vec3.zCoord <= max.zCoord;
   }

   public static boolean isVecInsideXZBounds(Vec3 par1Vec3, Vec3 min, Vec3 max) {
      return par1Vec3 == null
         ? false
         : par1Vec3.xCoord >= min.xCoord && par1Vec3.xCoord <= max.xCoord && par1Vec3.zCoord >= min.zCoord && par1Vec3.zCoord <= max.zCoord;
   }

   public static boolean isVecInsideXYBounds(Vec3 par1Vec3, Vec3 min, Vec3 max) {
      return par1Vec3 == null
         ? false
         : par1Vec3.xCoord >= min.xCoord && par1Vec3.xCoord <= max.xCoord && par1Vec3.yCoord >= min.yCoord && par1Vec3.yCoord <= max.yCoord;
   }

   public static boolean doesWaterHaveValidSource(World world, int i, int j, int k, int iDistanceToCheck) {
      return doesWaterHaveValidSourceRecursive(world, i, j, k, i, j, k, iDistanceToCheck);
   }

   private static boolean doesWaterHaveValidSourceRecursive(World world, int i, int j, int k, int startI, int startJ, int startK, int iDistanceToCheck) {
      if (iDistanceToCheck <= 0) {
         return true;
      } else if (!world.checkChunksExist(i - 1, 64, k - 1, i + 1, 64, k + 1)) {
         return true;
      } else {
         int iThisBlockHeight = world.getBlockMetadata(i, j, k);
         if (iThisBlockHeight == 0) {
            return true;
         } else if (iThisBlockHeight >= 8) {
            BlockPos targetPos = new BlockPos(i, j, k);
            targetPos.addFacingAsOffset(1);
            int iTargetBlockID = world.getBlockId(targetPos.x, targetPos.y, targetPos.z);
            if (iTargetBlockID == Block.waterMoving.blockID || iTargetBlockID == Block.waterStill.blockID) {
               if (doesWaterHaveValidSourceWithSourceCheck(world, targetPos.x, targetPos.y, targetPos.z, startI, startJ, startK, iDistanceToCheck - 1)) {
                  return true;
               }

               for (int iFacing = 2; iFacing < 6; iFacing++) {
                  targetPos = new BlockPos(i, j, k);
                  targetPos.addFacingAsOffset(iFacing);
                  iTargetBlockID = world.getBlockId(targetPos.x, targetPos.y, targetPos.z);
                  if (iTargetBlockID == Block.waterMoving.blockID || iTargetBlockID == Block.waterStill.blockID) {
                     int iTargetHeight = world.getBlockMetadata(targetPos.x, targetPos.y, targetPos.z);
                     boolean bTargetIsHigher = false;
                     if (iTargetHeight == 0) {
                        return true;
                     }
                  }
               }
            }

            return false;
         } else {
            int iDownBlockID = world.getBlockId(i, j - 1, k);
            if (BTWBlocks.potentialFluidSources[iDownBlockID]) {
               FluidSource targetBlock = (FluidSource)Block.blocksList[iDownBlockID];
               if (targetBlock.isSourceToFluidBlockAtFacing(world, i, j - 1, k, 1) >= 0) {
                  if (iDownBlockID != BTWBlocks.screwPump.blockID) {
                     return true;
                  }

                  if (j - 1 < startJ) {
                     return true;
                  }

                  BlockPos targetPos = new BlockPos(i, j - 1, k);
                  int iTargetFacing = ((ScrewPumpBlock)BTWBlocks.screwPump).getFacing(world, targetPos.x, targetPos.y, targetPos.z);
                  targetPos.addFacingAsOffset(iTargetFacing);
                  if (doesWaterHaveValidSourceWithSourceCheck(world, targetPos.x, targetPos.y, targetPos.z, startI, startJ, startK, iDistanceToCheck - 1)) {
                     return true;
                  }
               }
            }

            for (int iFacingx = 2; iFacingx < 6; iFacingx++) {
               BlockPos targetPos = new BlockPos(i, j, k);
               targetPos.addFacingAsOffset(iFacingx);
               int iTargetBlockID = world.getBlockId(targetPos.x, targetPos.y, targetPos.z);
               if (iTargetBlockID == Block.waterMoving.blockID || iTargetBlockID == Block.waterStill.blockID) {
                  int iTargetHeight = world.getBlockMetadata(targetPos.x, targetPos.y, targetPos.z);
                  boolean bTargetIsHigher = false;
                  if (iTargetHeight >= 8) {
                     bTargetIsHigher = true;
                  } else if (iTargetHeight < iThisBlockHeight) {
                     bTargetIsHigher = true;
                  }

                  if (bTargetIsHigher
                     && doesWaterHaveValidSourceWithSourceCheck(world, targetPos.x, targetPos.y, targetPos.z, startI, startJ, startK, iDistanceToCheck - 1)) {
                     return true;
                  }
               } else if (BTWBlocks.potentialFluidSources[iTargetBlockID]) {
                  FluidSource targetBlock = (FluidSource)Block.blocksList[iTargetBlockID];
                  if (targetBlock.isSourceToFluidBlockAtFacing(world, targetPos.x, targetPos.y, targetPos.z, Block.getOppositeFacing(iFacingx)) >= 0) {
                     return true;
                  }
               }
            }

            return false;
         }
      }
   }

   private static boolean doesWaterHaveValidSourceWithSourceCheck(World world, int i, int j, int k, int startI, int startJ, int startK, int iDistanceToCheck) {
      return i == startI && j == startJ && k == startK ? false : doesWaterHaveValidSourceRecursive(world, i, j, k, startI, startJ, startK, iDistanceToCheck);
   }
}
