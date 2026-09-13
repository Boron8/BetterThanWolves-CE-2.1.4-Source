package btw.block.blocks;

import btw.BTWMod;
import btw.block.util.RayTraceUtils;
import btw.client.render.util.RenderUtils;
import btw.world.util.BlockPos;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Material;
import net.minecraft.src.MovingObjectPosition;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.Vec3;
import net.minecraft.src.World;

public class GroundCoverBlock extends Block {
   public static final float VISUAL_HEIGHT = 0.125F;

   protected GroundCoverBlock(int iBlockID, Material material) {
      super(iBlockID, material);
      this.initBlockBounds(0.0, 0.0, 0.0, 1.0, 0.125, 1.0);
      this.c(0.1F);
      this.setShovelsEffectiveOn();
      this.setBuoyant();
      this.k(0);
      this.a(CreativeTabs.tabDecorations);
   }

   @Override
   public boolean isOpaqueCube() {
      return false;
   }

   @Override
   public boolean renderAsNormalBlock() {
      return false;
   }

   @Override
   public boolean canPlaceBlockAt(World world, int i, int j, int k) {
      int iBlockBelowID = world.getBlockId(i, j - 1, k);
      Block blockBelow = Block.blocksList[iBlockBelowID];
      return blockBelow != null ? blockBelow.canGroundCoverRestOnBlock(world, i, j - 1, k) : false;
   }

   @Override
   public void onNeighborBlockChange(World world, int i, int j, int k, int iNeighborBlockID) {
      if (!this.canPlaceBlockAt(world, i, j, k)) {
         world.setBlockToAir(i, j, k);
      }
   }

   @Override
   public MovingObjectPosition collisionRayTrace(World world, int i, int j, int k, Vec3 startRay, Vec3 endRay) {
      float fVisualOffset = 0.0F;
      int iBlockBelowID = world.getBlockId(i, j - 1, k);
      Block blockBelow = Block.blocksList[iBlockBelowID];
      if (blockBelow != null) {
         fVisualOffset = blockBelow.groundCoverRestingOnVisualOffset(world, i, j - 1, k);
      }

      RayTraceUtils rayTrace = new RayTraceUtils(world, i, j, k, startRay, endRay);
      rayTrace.addBoxWithLocalCoordsToIntersectionList(0.0, fVisualOffset, 0.0, 1.0, 0.125F + fVisualOffset, 1.0);
      return rayTrace.getFirstIntersection();
   }

   @Override
   public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4) {
      return null;
   }

   @Override
   public boolean isGroundCover() {
      return true;
   }

   public static void clearAnyGroundCoverRestingOnBlock(World world, int i, int j, int k) {
      Block blockAbove = Block.blocksList[world.getBlockId(i, j + 1, k)];
      if (blockAbove != null) {
         if (blockAbove.isGroundCover()) {
            world.setBlockToAir(i, j + 1, k);
         } else if (blockAbove.groundCoverRestingOnVisualOffset(world, i, j + 1, k) < -0.99F) {
            Block block2Above = Block.blocksList[world.getBlockId(i, j + 2, k)];
            if (block2Above != null && block2Above.isGroundCover()) {
               world.setBlockToAir(i, j + 2, k);
            }
         }
      }
   }

   public static boolean isGroundCoverRestingOnBlock(World world, int i, int j, int k) {
      Block blockAbove = Block.blocksList[world.getBlockId(i, j + 1, k)];
      if (blockAbove != null) {
         if (blockAbove.isGroundCover()) {
            return true;
         }

         if (blockAbove.groundCoverRestingOnVisualOffset(world, i, j + 1, k) < -0.99F) {
            Block block2Above = Block.blocksList[world.getBlockId(i, j + 2, k)];
            if (block2Above != null && block2Above.isGroundCover()) {
               return true;
            }
         }
      }

      return false;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void clientBreakBlock(World world, int i, int j, int k, int iBlockID, int iMetadata) {
      world.markBlockRangeForRenderUpdate(i, j - 1, k, i, j - 2, k);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int i, int j, int k) {
      float fVisualOffset = 0.0F;
      int iBlockBelowID = world.getBlockId(i, j - 1, k);
      Block blockBelow = Block.blocksList[iBlockBelowID];
      if (blockBelow != null) {
         fVisualOffset = blockBelow.groundCoverRestingOnVisualOffset(world, i, j - 1, k);
      }

      return this.getBlockBoundsFromPoolBasedOnState(world, i, j, k).offset(i, j + fVisualOffset, k);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean renderBlock(RenderBlocks renderBlocks, int x, int y, int z) {
      IBlockAccess blockAccess = renderBlocks.blockAccess;
      if (blockAccess.getBlockId(x, y - 1, z) != 0) {
         float fVisualOffset = 0.0F;
         int iBlockBelowID = blockAccess.getBlockId(x, y - 1, z);
         Block blockBelow = Block.blocksList[iBlockBelowID];
         int iBlockHeight = BTWMod.enableSnowRework ? (blockAccess.getBlockMetadata(x, y, z) & 7) + 1 : 1;
         if (blockBelow != null) {
            fVisualOffset = blockBelow.groundCoverRestingOnVisualOffset(blockAccess, x, y - 1, z);
            if (fVisualOffset < 0.0F) {
               y--;
               fVisualOffset++;
            }
         }

         float fHeight = 0.125F * iBlockHeight;
         renderBlocks.setRenderBounds(0.0, fVisualOffset, 0.0, 1.0, fHeight + fVisualOffset, 1.0);
         RenderUtils.renderStandardBlockWithTexture(renderBlocks, this, x, y, z, this.blockIcon);
      }

      return true;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean shouldSideBeRendered(IBlockAccess blockAccess, int iNeighborI, int iNeighborJ, int iNeighborK, int iSide) {
      if (iSide >= 2) {
         if (blockAccess.getBlockId(iNeighborI, iNeighborJ, iNeighborK) == this.blockID) {
            BlockPos thisBlockPos = new BlockPos(iNeighborI, iNeighborJ, iNeighborK, Block.getOppositeFacing(iSide));
            if (blockAccess.getBlockId(thisBlockPos.x, thisBlockPos.y, thisBlockPos.z) == this.blockID) {
               if (blockAccess.isBlockOpaqueCube(iNeighborI, iNeighborJ, iNeighborK)) {
                  return false;
               }

               if (blockAccess.getBlockMetadata(thisBlockPos.x, thisBlockPos.y, thisBlockPos.z)
                  <= blockAccess.getBlockMetadata(iNeighborI, iNeighborJ, iNeighborK)) {
                  int iBlockBelowID = blockAccess.getBlockId(iNeighborI, iNeighborJ - 1, iNeighborK);
                  if (iBlockBelowID != 0 && r[iBlockBelowID].groundCoverRestingOnVisualOffset(blockAccess, iNeighborI, iNeighborJ - 1, iNeighborK) > -0.01F) {
                     return false;
                  }
               }
            }
         }

         return true;
      } else {
         return iSide == 1 ? true : super.shouldSideBeRendered(blockAccess, iNeighborI, iNeighborJ, iNeighborK, iSide);
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean shouldRenderNeighborFullFaceSide(IBlockAccess blockAccess, int i, int j, int k, int iNeighborSide) {
      if (iNeighborSide == 1) {
         int iBlockBelowID = blockAccess.getBlockId(i, j - 1, k);
         if (iBlockBelowID != 0 && r[iBlockBelowID].groundCoverRestingOnVisualOffset(blockAccess, i, j - 1, k) > -0.125F) {
            return false;
         }
      }

      return true;
   }
}
