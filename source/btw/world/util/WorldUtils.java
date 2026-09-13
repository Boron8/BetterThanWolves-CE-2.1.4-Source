package btw.world.util;

import btw.block.BTWBlocks;
import btw.block.FluidSource;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.Block;
import net.minecraft.src.Entity;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.MathHelper;
import net.minecraft.src.MovingObjectPosition;
import net.minecraft.src.NetServerHandler;
import net.minecraft.src.Packet;
import net.minecraft.src.Vec3;
import net.minecraft.src.World;
import net.minecraft.src.WorldServer;

public class WorldUtils {
   public static boolean isReplaceableBlock(World world, int i, int j, int k) {
      int iBlockID = world.getBlockId(i, j, k);
      Block block = Block.blocksList[iBlockID];
      return block == null || block.blockMaterial.isReplaceable();
   }

   public static MovingObjectPosition rayTraceBlocksAlwaysHitWaterAndLava(World world, Vec3 vec3d, Vec3 vec3d1, boolean flag, boolean flag1) {
      if (Double.isNaN(vec3d.xCoord) || Double.isNaN(vec3d.yCoord) || Double.isNaN(vec3d.zCoord)) {
         return null;
      } else if (!Double.isNaN(vec3d1.xCoord) && !Double.isNaN(vec3d1.yCoord) && !Double.isNaN(vec3d1.zCoord)) {
         int i = MathHelper.floor_double(vec3d1.xCoord);
         int j = MathHelper.floor_double(vec3d1.yCoord);
         int k = MathHelper.floor_double(vec3d1.zCoord);
         int l = MathHelper.floor_double(vec3d.xCoord);
         int i1 = MathHelper.floor_double(vec3d.yCoord);
         int j1 = MathHelper.floor_double(vec3d.zCoord);
         int k1 = world.getBlockId(l, i1, j1);
         int i2 = world.getBlockMetadata(l, i1, j1);
         Block block = Block.blocksList[k1];
         if ((!flag1 || block == null || block.getCollisionBoundingBoxFromPool(world, l, i1, j1) != null)
            && k1 > 0
            && (
               block.canCollideCheck(i2, flag)
                  || k1 == Block.waterMoving.blockID
                  || k1 == Block.waterStill.blockID
                  || k1 == Block.lavaMoving.blockID
                  || k1 == Block.lavaStill.blockID
            )) {
            MovingObjectPosition movingobjectposition = block.collisionRayTrace(world, l, i1, j1, vec3d, vec3d1);
            if (movingobjectposition != null) {
               return movingobjectposition;
            }
         }

         int l1 = 200;

         while (l1-- >= 0) {
            if (Double.isNaN(vec3d.xCoord) || Double.isNaN(vec3d.yCoord) || Double.isNaN(vec3d.zCoord)) {
               return null;
            }

            if (l == i && i1 == j && j1 == k) {
               return null;
            }

            boolean flag2 = true;
            boolean flag3 = true;
            boolean flag4 = true;
            double d = 999.0;
            double d1 = 999.0;
            double d2 = 999.0;
            if (i > l) {
               d = l + 1.0;
            } else if (i < l) {
               d = l + 0.0;
            } else {
               flag2 = false;
            }

            if (j > i1) {
               d1 = i1 + 1.0;
            } else if (j < i1) {
               d1 = i1 + 0.0;
            } else {
               flag3 = false;
            }

            if (k > j1) {
               d2 = j1 + 1.0;
            } else if (k < j1) {
               d2 = j1 + 0.0;
            } else {
               flag4 = false;
            }

            double d3 = 999.0;
            double d4 = 999.0;
            double d5 = 999.0;
            double d6 = vec3d1.xCoord - vec3d.xCoord;
            double d7 = vec3d1.yCoord - vec3d.yCoord;
            double d8 = vec3d1.zCoord - vec3d.zCoord;
            if (flag2) {
               d3 = (d - vec3d.xCoord) / d6;
            }

            if (flag3) {
               d4 = (d1 - vec3d.yCoord) / d7;
            }

            if (flag4) {
               d5 = (d2 - vec3d.zCoord) / d8;
            }

            byte byte0 = 0;
            if (d3 < d4 && d3 < d5) {
               if (i > l) {
                  byte0 = 4;
               } else {
                  byte0 = 5;
               }

               vec3d.xCoord = d;
               vec3d.yCoord += d7 * d3;
               vec3d.zCoord += d8 * d3;
            } else if (d4 < d5) {
               if (j > i1) {
                  byte0 = 0;
               } else {
                  byte0 = 1;
               }

               vec3d.xCoord += d6 * d4;
               vec3d.yCoord = d1;
               vec3d.zCoord += d8 * d4;
            } else {
               if (k > j1) {
                  byte0 = 2;
               } else {
                  byte0 = 3;
               }

               vec3d.xCoord += d6 * d5;
               vec3d.yCoord += d7 * d5;
               vec3d.zCoord = d2;
            }

            Vec3 vec3d2 = Vec3.createVectorHelper(vec3d.xCoord, vec3d.yCoord, vec3d.zCoord);
            l = (int)(vec3d2.xCoord = MathHelper.floor_double(vec3d.xCoord));
            if (byte0 == 5) {
               l--;
               vec3d2.xCoord++;
            }

            i1 = (int)(vec3d2.yCoord = MathHelper.floor_double(vec3d.yCoord));
            if (byte0 == 1) {
               i1--;
               vec3d2.yCoord++;
            }

            j1 = (int)(vec3d2.zCoord = MathHelper.floor_double(vec3d.zCoord));
            if (byte0 == 3) {
               j1--;
               vec3d2.zCoord++;
            }

            int j2 = world.getBlockId(l, i1, j1);
            int k2 = world.getBlockMetadata(l, i1, j1);
            Block block1 = Block.blocksList[j2];
            if ((!flag1 || block1 == null || block1.getCollisionBoundingBoxFromPool(world, l, i1, j1) != null)
               && j2 > 0
               && (
                  block1.canCollideCheck(k2, flag)
                     || j2 == Block.waterMoving.blockID
                     || j2 == Block.waterStill.blockID
                     || j2 == Block.lavaMoving.blockID
                     || j2 == Block.lavaStill.blockID
               )) {
               MovingObjectPosition movingobjectposition1 = block1.collisionRayTrace(world, l, i1, j1, vec3d, vec3d1);
               if (movingobjectposition1 != null) {
                  return movingobjectposition1;
               }
            }
         }

         return null;
      } else {
         return null;
      }
   }

   public static MovingObjectPosition rayTraceBlocksAlwaysHitWaterAndLavaAndFire(World world, Vec3 vec3d, Vec3 vec3d1, boolean flag, boolean flag1) {
      if (Double.isNaN(vec3d.xCoord) || Double.isNaN(vec3d.yCoord) || Double.isNaN(vec3d.zCoord)) {
         return null;
      } else if (!Double.isNaN(vec3d1.xCoord) && !Double.isNaN(vec3d1.yCoord) && !Double.isNaN(vec3d1.zCoord)) {
         int i = MathHelper.floor_double(vec3d1.xCoord);
         int j = MathHelper.floor_double(vec3d1.yCoord);
         int k = MathHelper.floor_double(vec3d1.zCoord);
         int l = MathHelper.floor_double(vec3d.xCoord);
         int i1 = MathHelper.floor_double(vec3d.yCoord);
         int j1 = MathHelper.floor_double(vec3d.zCoord);
         int k1 = world.getBlockId(l, i1, j1);
         int i2 = world.getBlockMetadata(l, i1, j1);
         Block block = Block.blocksList[k1];
         if ((!flag1 || block == null || block.getCollisionBoundingBoxFromPool(world, l, i1, j1) != null)
            && k1 > 0
            && (
               block.canCollideCheck(i2, flag)
                  || k1 == Block.waterMoving.blockID
                  || k1 == Block.waterStill.blockID
                  || k1 == Block.lavaMoving.blockID
                  || k1 == Block.lavaStill.blockID
                  || k1 == Block.fire.blockID
                  || k1 == BTWBlocks.stokedFire.blockID
            )) {
            MovingObjectPosition movingobjectposition = block.collisionRayTrace(world, l, i1, j1, vec3d, vec3d1);
            if (movingobjectposition != null) {
               return movingobjectposition;
            }
         }

         int l1 = 200;

         while (l1-- >= 0) {
            if (Double.isNaN(vec3d.xCoord) || Double.isNaN(vec3d.yCoord) || Double.isNaN(vec3d.zCoord)) {
               return null;
            }

            if (l == i && i1 == j && j1 == k) {
               return null;
            }

            boolean flag2 = true;
            boolean flag3 = true;
            boolean flag4 = true;
            double d = 999.0;
            double d1 = 999.0;
            double d2 = 999.0;
            if (i > l) {
               d = l + 1.0;
            } else if (i < l) {
               d = l + 0.0;
            } else {
               flag2 = false;
            }

            if (j > i1) {
               d1 = i1 + 1.0;
            } else if (j < i1) {
               d1 = i1 + 0.0;
            } else {
               flag3 = false;
            }

            if (k > j1) {
               d2 = j1 + 1.0;
            } else if (k < j1) {
               d2 = j1 + 0.0;
            } else {
               flag4 = false;
            }

            double d3 = 999.0;
            double d4 = 999.0;
            double d5 = 999.0;
            double d6 = vec3d1.xCoord - vec3d.xCoord;
            double d7 = vec3d1.yCoord - vec3d.yCoord;
            double d8 = vec3d1.zCoord - vec3d.zCoord;
            if (flag2) {
               d3 = (d - vec3d.xCoord) / d6;
            }

            if (flag3) {
               d4 = (d1 - vec3d.yCoord) / d7;
            }

            if (flag4) {
               d5 = (d2 - vec3d.zCoord) / d8;
            }

            byte byte0 = 0;
            if (d3 < d4 && d3 < d5) {
               if (i > l) {
                  byte0 = 4;
               } else {
                  byte0 = 5;
               }

               vec3d.xCoord = d;
               vec3d.yCoord += d7 * d3;
               vec3d.zCoord += d8 * d3;
            } else if (d4 < d5) {
               if (j > i1) {
                  byte0 = 0;
               } else {
                  byte0 = 1;
               }

               vec3d.xCoord += d6 * d4;
               vec3d.yCoord = d1;
               vec3d.zCoord += d8 * d4;
            } else {
               if (k > j1) {
                  byte0 = 2;
               } else {
                  byte0 = 3;
               }

               vec3d.xCoord += d6 * d5;
               vec3d.yCoord += d7 * d5;
               vec3d.zCoord = d2;
            }

            Vec3 vec3d2 = Vec3.createVectorHelper(vec3d.xCoord, vec3d.yCoord, vec3d.zCoord);
            l = (int)(vec3d2.xCoord = MathHelper.floor_double(vec3d.xCoord));
            if (byte0 == 5) {
               l--;
               vec3d2.xCoord++;
            }

            i1 = (int)(vec3d2.yCoord = MathHelper.floor_double(vec3d.yCoord));
            if (byte0 == 1) {
               i1--;
               vec3d2.yCoord++;
            }

            j1 = (int)(vec3d2.zCoord = MathHelper.floor_double(vec3d.zCoord));
            if (byte0 == 3) {
               j1--;
               vec3d2.zCoord++;
            }

            int j2 = world.getBlockId(l, i1, j1);
            int k2 = world.getBlockMetadata(l, i1, j1);
            Block block1 = Block.blocksList[j2];
            if ((!flag1 || block1 == null || block1.getCollisionBoundingBoxFromPool(world, l, i1, j1) != null)
               && j2 > 0
               && (
                  block1.canCollideCheck(k2, flag)
                     || j2 == Block.waterMoving.blockID
                     || j2 == Block.waterStill.blockID
                     || j2 == Block.lavaMoving.blockID
                     || j2 == Block.lavaStill.blockID
                     || j2 == Block.fire.blockID
                     || j2 == BTWBlocks.stokedFire.blockID
               )) {
               MovingObjectPosition movingobjectposition1 = block1.collisionRayTrace(world, l, i1, j1, vec3d, vec3d1);
               if (movingobjectposition1 != null) {
                  return movingobjectposition1;
               }
            }
         }

         return null;
      } else {
         return null;
      }
   }

   public static int isValidSourceForFluidBlockToFacing(World world, int i, int j, int k, int iFacing) {
      BlockPos targetPos = new BlockPos(i, j, k);
      targetPos.addFacingAsOffset(iFacing);
      int iTargetBlockID = world.getBlockId(targetPos.x, targetPos.y, targetPos.z);
      if (BTWBlocks.potentialFluidSources[iTargetBlockID]) {
         FluidSource targetBlock = (FluidSource)Block.blocksList[iTargetBlockID];
         return targetBlock.isSourceToFluidBlockAtFacing(world, targetPos.x, targetPos.y, targetPos.z, Block.getOppositeFacing(iFacing));
      } else {
         return -1;
      }
   }

   public static boolean isValidLightLevelForMobSpawning(World world, int x, int y, int z) {
      return world.provider.dimensionId == -1
         || world.getBlockNaturalLightValue(x, y, z) <= 7 && world.getBlockLightValueNoSky(x, y, z) == 0 && world.getLightBrightness(x, y, z) <= 0.5F;
   }

   public static boolean canMobsSpawnHere(World world, int i, int j, int k) {
      if (!world.isBlockNormalCube(i, j, k) && !world.getBlockMaterial(i, j, k).isLiquid()) {
         Block blockBelow = Block.blocksList[world.getBlockId(i, j - 1, k)];
         if (blockBelow != null
            && blockBelow.canMobsSpawnOn(world, i, j - 1, k)
            && blockBelow != Block.leaves
            && isValidLightLevelForMobSpawning(world, i, j, k)) {
            Block blockIn = Block.blocksList[world.getBlockId(i, j, k)];
            return blockIn == null || blockIn.getCollisionBoundingBoxFromPool(world, i, j, k) == null;
         }
      }

      return false;
   }

   public static boolean doesBlockHaveSmallCenterHardpointToFacing(IBlockAccess blockAccess, int i, int j, int k, int iFacing, boolean bIgnoreTransparency) {
      int iBlockID = blockAccess.getBlockId(i, j, k);
      Block block = Block.blocksList[iBlockID];
      return block != null ? block.hasSmallCenterHardPointToFacing(blockAccess, i, j, k, iFacing, bIgnoreTransparency) : false;
   }

   public static boolean doesBlockHaveSmallCenterHardpointToFacing(IBlockAccess blockAccess, int i, int j, int k, int iFacing) {
      return doesBlockHaveSmallCenterHardpointToFacing(blockAccess, i, j, k, iFacing, false);
   }

   public static boolean doesBlockHaveCenterHardpointToFacing(IBlockAccess blockAccess, int i, int j, int k, int iFacing, boolean bIgnoreTransparency) {
      int iBlockID = blockAccess.getBlockId(i, j, k);
      Block block = Block.blocksList[iBlockID];
      return block != null ? block.hasCenterHardPointToFacing(blockAccess, i, j, k, iFacing, bIgnoreTransparency) : false;
   }

   public static boolean doesBlockHaveCenterHardpointToFacing(IBlockAccess blockAccess, int i, int j, int k, int iFacing) {
      return doesBlockHaveCenterHardpointToFacing(blockAccess, i, j, k, iFacing, false);
   }

   public static boolean doesBlockHaveLargeCenterHardpointToFacing(IBlockAccess blockAccess, int i, int j, int k, int iFacing, boolean bIgnoreTransparency) {
      int iBlockID = blockAccess.getBlockId(i, j, k);
      Block block = Block.blocksList[iBlockID];
      return block != null ? block.hasLargeCenterHardPointToFacing(blockAccess, i, j, k, iFacing, bIgnoreTransparency) : false;
   }

   public static boolean doesBlockHaveLargeCenterHardpointToFacing(IBlockAccess blockAccess, int i, int j, int k, int iFacing) {
      return doesBlockHaveLargeCenterHardpointToFacing(blockAccess, i, j, k, iFacing, false);
   }

   public static boolean isBlockRestingOnThatBelow(IBlockAccess blockAccess, int i, int j, int k) {
      int iBlockID = blockAccess.getBlockId(i, j, k);
      Block block = Block.blocksList[iBlockID];
      return block != null ? block.isBlockRestingOnThatBelow(blockAccess, i, j, k) : false;
   }

   public static boolean doesBlockHaveSolidTopSurface(IBlockAccess blockAccess, int i, int j, int k) {
      Block block = Block.blocksList[blockAccess.getBlockId(i, j, k)];
      return block != null && block.hasLargeCenterHardPointToFacing(blockAccess, i, j, k, 1);
   }

   public static boolean isStairBlock(IBlockAccess blockAccess, int i, int j, int k) {
      int iBlockID = blockAccess.getBlockId(i, j, k);
      Block block = Block.blocksList[iBlockID];
      return block != null ? block.isStairBlock() : false;
   }

   public static long getOverworldTimeServerOnly() {
      return MinecraftServer.getServer() != null ? MinecraftServer.getServer().worldServers[0].I() : 0L;
   }

   public static boolean gameProgressHasNetherBeenAccessedServerOnly() {
      return MinecraftServer.getServer() != null ? MinecraftServer.getServer().worldServers[0].worldInfo.hasNetherBeenAccessed() : false;
   }

   public static void gameProgressSetNetherBeenAccessedServerOnly() {
      if (MinecraftServer.getServer() != null) {
         MinecraftServer.getServer().worldServers[0].worldInfo.setNetherBeenAccessed();
      }
   }

   public static boolean gameProgressHasWitherBeenSummonedServerOnly() {
      return MinecraftServer.getServer() != null ? MinecraftServer.getServer().worldServers[0].worldInfo.hasWitherBeenSummoned() : false;
   }

   public static void gameProgressSetWitherHasBeenSummonedServerOnly() {
      if (MinecraftServer.getServer() != null) {
         MinecraftServer.getServer().worldServers[0].worldInfo.setWitherHasBeenSummoned();
      }
   }

   public static boolean gameProgressHasEndDimensionBeenAccessedServerOnly() {
      return MinecraftServer.getServer() != null ? MinecraftServer.getServer().worldServers[0].worldInfo.hasEndDimensionBeenAccessed() : false;
   }

   public static void gameProgressSetEndDimensionHasBeenAccessedServerOnly() {
      if (MinecraftServer.getServer() != null) {
         MinecraftServer.getServer().worldServers[0].worldInfo.setEndDimensionHasBeenAccessed();
      }
   }

   public static int rotateFacingForCoordBaseMode(int iFacing, int iCoordBaseMode) {
      if (iCoordBaseMode == 0) {
         if (iFacing == 2) {
            return 3;
         }

         if (iFacing == 3) {
            return 2;
         }
      } else if (iCoordBaseMode == 1) {
         if (iFacing == 2) {
            return 4;
         }

         if (iFacing == 3) {
            return 5;
         }

         if (iFacing == 4) {
            return 2;
         }

         if (iFacing == 5) {
            return 3;
         }
      } else if (iCoordBaseMode == 3) {
         if (iFacing == 2) {
            return 5;
         }

         if (iFacing == 3) {
            return 4;
         }

         if (iFacing == 4) {
            return 2;
         }

         if (iFacing == 5) {
            return 3;
         }
      }

      return iFacing;
   }

   public static void sendPacketToAllPlayersTrackingEntity(WorldServer world, Entity entity, Packet packet) {
      world.getEntityTracker().sendPacketToAllPlayersTrackingEntity(entity, packet);
   }

   public static void sendPacketToPlayer(NetServerHandler handler, Packet packet) {
      handler.sendPacketToPlayer(packet);
   }

   public static boolean hasNeighborWithMortarInFullFaceContactToFacing(World world, int i, int j, int k, int iFacing) {
      BlockPos tempBlockPos = new BlockPos(i, j, k, iFacing);
      int iTempBlockID = world.getBlockId(tempBlockPos.x, tempBlockPos.y, tempBlockPos.z);
      Block tempBlock = Block.blocksList[iTempBlockID];
      return tempBlock != null
         && tempBlock.hasMortar(world, tempBlockPos.x, tempBlockPos.y, tempBlockPos.z)
         && tempBlock.hasContactPointToFullFace(world, tempBlockPos.x, tempBlockPos.y, tempBlockPos.z, Block.getOppositeFacing(iFacing));
   }

   public static boolean hasNeighborWithMortarInSlabSideContactToFacing(World world, int i, int j, int k, int iFacing, boolean bIsSlabUpsideDown) {
      BlockPos tempBlockPos = new BlockPos(i, j, k, iFacing);
      int iTempBlockID = world.getBlockId(tempBlockPos.x, tempBlockPos.y, tempBlockPos.z);
      Block tempBlock = Block.blocksList[iTempBlockID];
      return tempBlock != null
         && tempBlock.hasMortar(world, tempBlockPos.x, tempBlockPos.y, tempBlockPos.z)
         && tempBlock.hasContactPointToSlabSideFace(world, tempBlockPos.x, tempBlockPos.y, tempBlockPos.z, Block.getOppositeFacing(iFacing), bIsSlabUpsideDown);
   }

   public static boolean hasNeighborWithMortarInStairShapedContactToFacing(World world, int i, int j, int k, int iFacing) {
      BlockPos tempBlockPos = new BlockPos(i, j, k, iFacing);
      int iTempBlockID = world.getBlockId(tempBlockPos.x, tempBlockPos.y, tempBlockPos.z);
      Block tempBlock = Block.blocksList[iTempBlockID];
      return tempBlock != null
         && tempBlock.hasMortar(world, tempBlockPos.x, tempBlockPos.y, tempBlockPos.z)
         && tempBlock.hasContactPointToStairShapedFace(world, tempBlockPos.x, tempBlockPos.y, tempBlockPos.z, Block.getOppositeFacing(iFacing));
   }

   public static boolean hasNeighborWithMortarInStairNarrowVerticalContactToFacing(World world, int i, int j, int k, int iFacing, int iStairFacing) {
      BlockPos tempBlockPos = new BlockPos(i, j, k, iFacing);
      int iTempBlockID = world.getBlockId(tempBlockPos.x, tempBlockPos.y, tempBlockPos.z);
      Block tempBlock = Block.blocksList[iTempBlockID];
      return tempBlock != null
         && tempBlock.hasMortar(world, tempBlockPos.x, tempBlockPos.y, tempBlockPos.z)
         && tempBlock.hasContactPointToStairNarrowVerticalFace(
            world, tempBlockPos.x, tempBlockPos.y, tempBlockPos.z, Block.getOppositeFacing(iFacing), iStairFacing
         );
   }

   public static boolean hasStickySnowNeighborInFullFaceContactToFacing(World world, int i, int j, int k, int iFacing) {
      BlockPos tempBlockPos = new BlockPos(i, j, k, iFacing);
      int iTempBlockID = world.getBlockId(tempBlockPos.x, tempBlockPos.y, tempBlockPos.z);
      Block tempBlock = Block.blocksList[iTempBlockID];
      return tempBlock != null
         && tempBlock.isStickyToSnow(world, tempBlockPos.x, tempBlockPos.y, tempBlockPos.z)
         && tempBlock.hasContactPointToFullFace(world, tempBlockPos.x, tempBlockPos.y, tempBlockPos.z, Block.getOppositeFacing(iFacing));
   }

   public static boolean hasStickySnowNeighborInSlabSideContactToFacing(World world, int i, int j, int k, int iFacing, boolean bIsSlabUpsideDown) {
      BlockPos tempBlockPos = new BlockPos(i, j, k, iFacing);
      int iTempBlockID = world.getBlockId(tempBlockPos.x, tempBlockPos.y, tempBlockPos.z);
      Block tempBlock = Block.blocksList[iTempBlockID];
      return tempBlock != null
         && tempBlock.isStickyToSnow(world, tempBlockPos.x, tempBlockPos.y, tempBlockPos.z)
         && tempBlock.hasContactPointToSlabSideFace(world, tempBlockPos.x, tempBlockPos.y, tempBlockPos.z, Block.getOppositeFacing(iFacing), bIsSlabUpsideDown);
   }

   public static void clearAnyGroundCoverOnBlock(World world, int i, int j, int k) {
      if (isGroundCoverOnBlock(world, i, j, k)) {
         world.setBlockToAir(i, j + 1, k);
      }
   }

   public static boolean isGroundCoverOnBlock(IBlockAccess blockAccess, int i, int j, int k) {
      int iBlockAboveID = blockAccess.getBlockId(i, j + 1, k);
      if (iBlockAboveID != 0) {
         Block blockAbove = Block.blocksList[iBlockAboveID];
         if (blockAbove.isGroundCover()) {
            return true;
         }
      }

      return false;
   }

   public static boolean isWaterSourceBlock(World world, int i, int j, int k) {
      int iBlockID = world.getBlockId(i, j, k);
      return (iBlockID == Block.waterMoving.blockID || iBlockID == Block.waterStill.blockID) && world.getBlockMetadata(i, j, k) == 0;
   }
}
