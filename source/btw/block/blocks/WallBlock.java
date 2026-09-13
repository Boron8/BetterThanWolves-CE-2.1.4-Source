package btw.block.blocks;

import btw.block.util.RayTraceUtils;
import btw.item.BTWItems;
import btw.world.util.BlockPos;
import btw.world.util.WorldUtils;
import java.util.List;
import java.util.Random;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.BlockPane;
import net.minecraft.src.BlockWall;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.Entity;
import net.minecraft.src.Facing;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Icon;
import net.minecraft.src.ItemStack;
import net.minecraft.src.MovingObjectPosition;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.Vec3;
import net.minecraft.src.World;

public class WallBlock extends BlockWall {
   public WallBlock(int iBlockID, Block baseBlock) {
      super(iBlockID, baseBlock);
      this.a(CreativeTabs.tabDecorations);
   }

   @Override
   public boolean hasCenterHardPointToFacing(IBlockAccess blockAccess, int i, int j, int k, int iFacing, boolean bIgnoreTransparency) {
      return iFacing == 0 || iFacing == 1;
   }

   @Override
   public int getWeightOnPathBlocked(IBlockAccess blockAccess, int i, int j, int k) {
      return -3;
   }

   @Override
   public int idDropped(int iMetadata, Random rand, int iFortuneModifier) {
      int iType = iMetadata & 1;
      return iType == 0 ? BTWItems.stone.itemID : super.a(iMetadata, rand, iFortuneModifier);
   }

   @Override
   public int damageDropped(int metadata) {
      int type = metadata & 1;
      return type == 0 ? metadata >> 2 : super.damageDropped(metadata);
   }

   @Override
   public void dropBlockAsItemWithChance(World world, int i, int j, int k, int iMetadata, float fChance, int iFortuneModifier) {
      int iType = iMetadata & 1;
      if (iType == 0) {
         if (!world.isRemote) {
            int iNumDropped = 4;

            for (int k1 = 0; k1 < iNumDropped; k1++) {
               int iItemID = this.idDropped(iMetadata, world.rand, iFortuneModifier);
               if (iItemID > 0) {
                  this.b(world, i, j, k, new ItemStack(iItemID, 1, this.damageDropped(iMetadata)));
               }
            }
         }
      } else {
         super.a(world, i, j, k, iMetadata, fChance, iFortuneModifier);
      }
   }

   @Override
   public boolean canGroundCoverRestOnBlock(World world, int i, int j, int k) {
      return world.doesBlockHaveSolidTopSurface(i, j - 1, k);
   }

   @Override
   public float groundCoverRestingOnVisualOffset(IBlockAccess blockAccess, int i, int j, int k) {
      return -1.0F;
   }

   @Override
   public void setBlockBoundsBasedOnState(IBlockAccess blockAccess, int i, int j, int k) {
   }

   @Override
   public AxisAlignedBB getBlockBoundsFromPoolBasedOnState(IBlockAccess blockAccess, int x, int y, int z) {
      boolean post = wallHasPost(blockAccess, x, y, z, true, true);
      boolean east = canConnectToBlockToFacing(blockAccess, x, y, z, 4);
      boolean west = canConnectToBlockToFacing(blockAccess, x, y, z, 5);
      boolean north = canConnectToBlockToFacing(blockAccess, x, y, z, 2);
      boolean south = canConnectToBlockToFacing(blockAccess, x, y, z, 3);
      boolean eastFullWall = shouldHaveFullHeightWallToFacing(blockAccess, x, y, z, 4);
      boolean westFullWall = shouldHaveFullHeightWallToFacing(blockAccess, x, y, z, 5);
      boolean northFullWall = shouldHaveFullHeightWallToFacing(blockAccess, x, y, z, 2);
      boolean southFullWall = shouldHaveFullHeightWallToFacing(blockAccess, x, y, z, 3);
      double minX = 0.3125;
      double minZ = 0.3125;
      double maxX = 0.6875;
      double maxZ = 0.6875;
      double height = 0.8125;
      if (eastFullWall || westFullWall || northFullWall || southFullWall || post) {
         height = 1.0;
      }

      if (post) {
         minX = 0.25;
         minZ = 0.25;
         maxX = 0.75;
         maxZ = 0.75;
      }

      if (east) {
         minX = 0.0;
      }

      if (west) {
         maxX = 1.0;
      }

      if (north) {
         minZ = 0.0;
      }

      if (south) {
         maxZ = 1.0;
      }

      return AxisAlignedBB.getAABBPool().getAABB(minX, 0.0, minZ, maxX, height, maxZ);
   }

   @Override
   public void addCollisionBoxesToList(World world, int x, int y, int z, AxisAlignedBB aabb, List collisionList, Entity entity) {
      boolean post = wallHasPost(world, x, y, z, true, true);
      boolean east = canConnectToBlockToFacing(world, x, y, z, 4);
      boolean west = canConnectToBlockToFacing(world, x, y, z, 5);
      boolean north = canConnectToBlockToFacing(world, x, y, z, 2);
      boolean south = canConnectToBlockToFacing(world, x, y, z, 3);
      if (post) {
         AxisAlignedBB.getAABBPool().getAABB(0.25, 0.0, 0.25, 0.75, 1.5, 0.75).offset(x, y, z).addToListIfIntersects(aabb, collisionList);
      }

      double height = 1.5;
      if (east) {
         AxisAlignedBB.getAABBPool().getAABB(0.0, 0.0, 0.3125, 0.5, height, 0.6875).offset(x, y, z).addToListIfIntersects(aabb, collisionList);
      }

      if (west) {
         AxisAlignedBB.getAABBPool().getAABB(0.5, 0.0, 0.3125, 1.0, height, 0.6875).offset(x, y, z).addToListIfIntersects(aabb, collisionList);
      }

      if (north) {
         AxisAlignedBB.getAABBPool().getAABB(0.3125, 0.0, 0.0, 0.6875, height, 0.5).offset(x, y, z).addToListIfIntersects(aabb, collisionList);
      }

      if (south) {
         AxisAlignedBB.getAABBPool().getAABB(0.3125, 0.0, 0.5, 0.6875, height, 1.0).offset(x, y, z).addToListIfIntersects(aabb, collisionList);
      }
   }

   @Override
   public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k) {
      AxisAlignedBB box = this.getBlockBoundsFromPoolBasedOnState(world, i, j, k);
      box.maxY = 1.5;
      return box.offset(i, j, k);
   }

   @Override
   public MovingObjectPosition collisionRayTrace(World world, int x, int y, int z, Vec3 var5, Vec3 var6) {
      boolean post = wallHasPost(world, x, y, z, true, true);
      boolean east = canConnectToBlockToFacing(world, x, y, z, 4);
      boolean west = canConnectToBlockToFacing(world, x, y, z, 5);
      boolean north = canConnectToBlockToFacing(world, x, y, z, 2);
      boolean south = canConnectToBlockToFacing(world, x, y, z, 3);
      boolean eastFullWall = shouldHaveFullHeightWallToFacing(world, x, y, z, 4);
      boolean westFullWall = shouldHaveFullHeightWallToFacing(world, x, y, z, 5);
      boolean northFullWall = shouldHaveFullHeightWallToFacing(world, x, y, z, 2);
      boolean southFullWall = shouldHaveFullHeightWallToFacing(world, x, y, z, 3);
      RayTraceUtils raytracer = new RayTraceUtils(world, x, y, z, var5, var6);
      if (post) {
         raytracer.addBoxWithLocalCoordsToIntersectionList(0.25, 0.0, 0.25, 0.75, 1.0, 0.75);
      }

      if (east) {
         double height = 0.8125;
         if (eastFullWall) {
            height = 1.0;
         }

         raytracer.addBoxWithLocalCoordsToIntersectionList(0.0, 0.0, 0.3125, 0.5, height, 0.6875);
      }

      if (west) {
         double height = 0.8125;
         if (westFullWall) {
            height = 1.0;
         }

         raytracer.addBoxWithLocalCoordsToIntersectionList(0.5, 0.0, 0.3125, 1.0, height, 0.6875);
      }

      if (north) {
         double height = 0.8125;
         if (northFullWall) {
            height = 1.0;
         }

         raytracer.addBoxWithLocalCoordsToIntersectionList(0.3125, 0.0, 0.0, 0.6875, height, 0.5);
      }

      if (south) {
         double height = 0.8125;
         if (southFullWall) {
            height = 1.0;
         }

         raytracer.addBoxWithLocalCoordsToIntersectionList(0.3125, 0.0, 0.5, 0.6875, height, 1.0);
      }

      return raytracer.getFirstIntersection();
   }

   @Override
   public boolean isWall(int metadata) {
      return true;
   }

   @Override
   public boolean shouldPaneConnectToThisBlockToFacing(IBlockAccess blockAccess, int x, int y, int z, int facing) {
      return true;
   }

   public int getStoneType(int metadata) {
      return metadata & 1;
   }

   public int getStrata(IBlockAccess blockAccess, int i, int j, int k) {
      return this.getStrata(blockAccess.getBlockMetadata(i, j, k));
   }

   public int getStrata(int iMetadata) {
      return (iMetadata & 12) >>> 2;
   }

   public static boolean canConnectToBlockToFacing(IBlockAccess blockAccess, int x, int y, int z, int facing) {
      BlockPos blockPos = new BlockPos(x, y, z, facing);
      Block block = Block.blocksList[blockAccess.getBlockId(blockPos.x, blockPos.y, blockPos.z)];
      return block != null ? block.shouldWallConnectToThisBlockToFacing(blockAccess, blockPos.x, blockPos.y, blockPos.z, Facing.oppositeSide[facing]) : false;
   }

   public static boolean wallHasPost(IBlockAccess blockAccess, int x, int y, int z, boolean checkAbove, boolean checkBelow) {
      int idAbove = blockAccess.getBlockId(x, y + 1, z);
      Block blockAbove = Block.blocksList[idAbove];
      int metaAbove = blockAccess.getBlockMetadata(x, y + 1, z);
      int idBelow = blockAccess.getBlockId(x, y - 1, z);
      Block blockBelow = Block.blocksList[idBelow];
      int metaBelow = blockAccess.getBlockMetadata(x, y - 1, z);
      boolean north = canConnectToBlockToFacing(blockAccess, x, y, z, 2);
      boolean south = canConnectToBlockToFacing(blockAccess, x, y, z, 3);
      boolean east = canConnectToBlockToFacing(blockAccess, x, y, z, 4);
      boolean west = canConnectToBlockToFacing(blockAccess, x, y, z, 5);
      boolean NS = north && south && !east && !west;
      boolean EW = !north && !south && east && west;
      if (!NS && !EW) {
         return true;
      } else {
         if (blockAbove != null && blockAbove.isWall(metaAbove) || blockBelow != null && blockBelow.isWall(metaBelow)) {
            if (blockAbove != null && blockAbove.isWall(metaAbove) && checkAbove) {
               return wallHasPost(blockAccess, x, y + 1, z, true, false);
            }

            if (blockBelow != null && blockBelow.isWall(metaBelow) && checkBelow) {
            }
         }

         boolean airAbove = blockAccess.isAirBlock(x, y + 1, z) || WorldUtils.isGroundCoverOnBlock(blockAccess, x, y, z);
         boolean solidSurface = blockAbove != null && blockAbove.hasLargeCenterHardPointToFacing(blockAccess, x, y + 1, z, 0);
         boolean paneAbove = blockAbove instanceof BlockPane;
         boolean paneToSide = false;

         for (int i = 0; i < 4; i++) {
            BlockPos blockPos = new BlockPos(x, y, z, i + 2);
            int idOffset = blockAccess.getBlockId(blockPos.x, blockPos.y, blockPos.z);
            if (Block.blocksList[idOffset] instanceof BlockPane) {
               paneToSide = true;
            }
         }

         if (airAbove && !paneToSide) {
            return false;
         } else if (!solidSurface && !paneToSide) {
            return blockAbove != null && blockAbove.shouldWallFormPostBelowThisBlock(blockAccess, x, y + 1, z);
         } else if (NS) {
            boolean northFullWall = shouldHaveFullHeightWallToFacing(blockAccess, x, y, z, 2);
            boolean southFullWall = shouldHaveFullHeightWallToFacing(blockAccess, x, y, z, 3);
            return !northFullWall || !southFullWall;
         } else {
            boolean eastFullWall = shouldHaveFullHeightWallToFacing(blockAccess, x, y, z, 4);
            boolean westFullWall = shouldHaveFullHeightWallToFacing(blockAccess, x, y, z, 5);
            return !eastFullWall || !westFullWall;
         }
      }
   }

   public static boolean shouldHaveFullHeightWallToFacing(IBlockAccess blockAccess, int x, int y, int z, int facing) {
      boolean connect = canConnectToBlockToFacing(blockAccess, x, y, z, facing);
      if (!connect) {
         return false;
      } else {
         int idAbove = blockAccess.getBlockId(x, y + 1, z);
         int metaAbove = blockAccess.getBlockMetadata(x, y + 1, z);
         Block blockAbove = Block.blocksList[idAbove];
         boolean solidSurfaceAbove = blockAbove != null && blockAbove.hasLargeCenterHardPointToFacing(blockAccess, x, y + 1, z, 0);
         boolean paneAbove = blockAbove instanceof BlockPane;
         boolean canPaneAboveConnectToFacing = false;
         if (paneAbove) {
            BlockPos blockPosPane = new BlockPos(x, y + 1, z, facing);
            canPaneAboveConnectToFacing = PaneBlock.canConnectToBlockToFacing(
               blockAccess, blockPosPane.x, blockPosPane.y, blockPosPane.z, Facing.oppositeSide[facing]
            );
         }

         BlockPos blockPos = new BlockPos(x, y, z, facing);
         Block sideBlock = Block.blocksList[blockAccess.getBlockId(blockPos.x, blockPos.y, blockPos.z)];
         boolean solidSide = sideBlock.hasLargeCenterHardPointToFacing(blockAccess, blockPos.x, blockPos.y, blockPos.z, Facing.oppositeSide[facing]);
         int idOffset = blockAccess.getBlockId(blockPos.x, blockPos.y, blockPos.z);
         boolean paneToSide = Block.blocksList[idOffset] instanceof BlockPane;
         int idAboveOffset = blockAccess.getBlockId(blockPos.x, blockPos.y + 1, blockPos.z);
         int metaAboveOffset = blockAccess.getBlockMetadata(blockPos.x, blockPos.y + 1, blockPos.z);
         Block blockAboveOffset = Block.blocksList[idAboveOffset];
         boolean solidSurfaceDiagonal = blockAboveOffset != null
            && blockAboveOffset.hasLargeCenterHardPointToFacing(blockAccess, blockPos.x, blockPos.y + 1, blockPos.z, 0);
         boolean paneAboveDiagonal = blockAboveOffset instanceof BlockPane;
         if (blockAbove != null && blockAbove.isWall(metaAbove) && !solidSurfaceDiagonal && !paneToSide) {
            return canConnectToBlockToFacing(blockAccess, x, y + 1, z, facing);
         } else {
            boolean wallOrBenchAbove = blockAbove != null && (blockAbove.isWall(metaAbove) || blockAbove.isBenchOrTable(metaAbove));
            boolean paneAboveInSameDirection = paneAbove && canPaneAboveConnectToFacing;
            boolean aboveConditionMet = wallOrBenchAbove || solidSurfaceAbove || paneAboveInSameDirection;
            boolean wallOrBenchDiagonal = blockAboveOffset != null
               && (blockAboveOffset.isWall(metaAboveOffset) || blockAboveOffset.isBenchOrTable(metaAboveOffset));
            boolean canIgnoreDiagonals = solidSide && (!paneAbove || canPaneAboveConnectToFacing);
            boolean diagonalConditionMet = wallOrBenchDiagonal || solidSurfaceDiagonal || canIgnoreDiagonals || paneAboveDiagonal;
            return paneToSide || aboveConditionMet && diagonalConditionMet;
         }
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean renderBlock(RenderBlocks render, int x, int y, int z) {
      return renderWall(render, this, x, y, z);
   }

   @Environment(EnvType.CLIENT)
   public static boolean renderWall(RenderBlocks render, Block block, int x, int y, int z) {
      boolean post = wallHasPost(render.blockAccess, x, y, z, true, true);
      boolean east = canConnectToBlockToFacing(render.blockAccess, x, y, z, 4);
      boolean west = canConnectToBlockToFacing(render.blockAccess, x, y, z, 5);
      boolean north = canConnectToBlockToFacing(render.blockAccess, x, y, z, 2);
      boolean south = canConnectToBlockToFacing(render.blockAccess, x, y, z, 3);
      boolean eastFullWall = shouldHaveFullHeightWallToFacing(render.blockAccess, x, y, z, 4);
      boolean westFullWall = shouldHaveFullHeightWallToFacing(render.blockAccess, x, y, z, 5);
      boolean northFullWall = shouldHaveFullHeightWallToFacing(render.blockAccess, x, y, z, 2);
      boolean southFullWall = shouldHaveFullHeightWallToFacing(render.blockAccess, x, y, z, 3);
      if (post) {
         render.setRenderBounds(0.25, 0.0, 0.25, 0.75, 1.0, 0.75);
         render.renderStandardBlock(block, x, y, z);
      }

      if (east) {
         double height = 0.8125;
         if (eastFullWall) {
            height = 1.0;
         }

         render.setRenderBounds(0.0, 0.0, 0.3125, 0.5, height, 0.6875);
         render.renderStandardBlock(block, x, y, z);
      }

      if (west) {
         double height = 0.8125;
         if (westFullWall) {
            height = 1.0;
         }

         render.setRenderBounds(0.5, 0.0, 0.3125, 1.0, height, 0.6875);
         render.renderStandardBlock(block, x, y, z);
      }

      if (north) {
         double height = 0.8125;
         if (northFullWall) {
            height = 1.0;
         }

         render.setRenderBounds(0.3125, 0.0, 0.0, 0.6875, height, 0.5);
         render.renderStandardBlock(block, x, y, z);
      }

      if (south) {
         double height = 0.8125;
         if (southFullWall) {
            height = 1.0;
         }

         render.setRenderBounds(0.3125, 0.0, 0.5, 0.6875, height, 1.0);
         render.renderStandardBlock(block, x, y, z);
      }

      return true;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean shouldSideBeRendered(IBlockAccess blockAccess, int iNeighborI, int iNeighborJ, int iNeighborK, int iSide) {
      return this.currentBlockRenderer.shouldSideBeRenderedBasedOnCurrentBounds(iNeighborI, iNeighborJ, iNeighborK, iSide);
   }

   @Override
   public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3List) {
      for (int i = 0; i < 3; i++) {
         par3List.add(new ItemStack(par1, 1, 0 + i << 2));
         par3List.add(new ItemStack(par1, 1, 1 + (i << 2)));
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public Icon getIcon(int side, int metadata) {
      int strata = this.getStrata(metadata);
      int type = this.getStoneType(metadata);
      return type == 1 ? Block.cobblestoneMossy.getIcon(side, strata) : Block.cobblestone.getIcon(side, strata);
   }
}
