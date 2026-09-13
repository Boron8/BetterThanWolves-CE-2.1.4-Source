package btw.block.blocks;

import btw.client.render.util.RenderUtils;
import btw.world.util.BlockPos;
import btw.world.util.WorldUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.World;

public class GlowingDetectorLogicBlock extends DetectorLogicBlock {
   private static final float LIT_FACE_THICKNESS = 0.01F;

   public GlowingDetectorLogicBlock(int iBlockID) {
      super(iBlockID);
      this.a(1.0F);
      this.c("fcBlockDetectorLogicGlowing");
   }

   @Override
   protected void removeSelf(World world, int i, int j, int k) {
      world.setBlock(i, j, k, 0, 0, 2);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public int getMixedBrightnessForBlock(IBlockAccess iblockaccess, int i, int j, int k) {
      return 983280;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public int getRenderBlockPass() {
      return 1;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean shouldSideBeRendered(IBlockAccess blockAccess, int iNeighborI, int iNeighborJ, int iNeighborK, int iSide) {
      return this.currentBlockRenderer.shouldSideBeRenderedBasedOnCurrentBounds(iNeighborI, iNeighborJ, iNeighborK, iSide);
   }

   @Environment(EnvType.CLIENT)
   public void setRenderBoundsToRenderLitFace(RenderBlocks renderBlocks, int iFacing) {
      switch (iFacing) {
         case 0:
            renderBlocks.setRenderBounds(0.0, 0.0, 0.0, 1.0, 0.01F, 1.0);
            break;
         case 1:
            renderBlocks.setRenderBounds(0.0, 0.99F, 0.0, 1.0, 1.0, 1.0);
            break;
         case 2:
            renderBlocks.setRenderBounds(0.0, 0.0, 0.0, 1.0, 1.0, 0.01F);
            break;
         case 3:
            renderBlocks.setRenderBounds(0.0, 0.0, 0.99F, 1.0, 1.0, 1.0);
            break;
         case 4:
            renderBlocks.setRenderBounds(0.0, 0.0, 0.0, 0.01F, 1.0, 1.0);
            break;
         default:
            renderBlocks.setRenderBounds(0.99F, 0.0, 0.0, 1.0, 1.0, 1.0);
      }
   }

   @Environment(EnvType.CLIENT)
   public boolean shouldVisiblyProjectToFacing(IBlockAccess blockAccess, int i, int j, int k, int iFacing) {
      BlockPos targetPos = new BlockPos(i, j, k, iFacing);
      return iFacing == 0
         ? WorldUtils.doesBlockHaveSolidTopSurface(blockAccess, targetPos.x, targetPos.y, targetPos.z)
         : blockAccess.isBlockNormalCube(targetPos.x, targetPos.y, targetPos.z);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean renderBlock(RenderBlocks renderer, int i, int j, int k) {
      IBlockAccess blockAccess = renderer.blockAccess;

      for (int iTempFacing = 0; iTempFacing <= 5; iTempFacing++) {
         if (this.shouldVisiblyProjectToFacing(blockAccess, i, j, k, iTempFacing)) {
            this.setRenderBoundsToRenderLitFace(renderer, iTempFacing);
            RenderUtils.renderBlockFullBrightWithTexture(renderer, blockAccess, i, j, k, this.blockIcon);
         }
      }

      return true;
   }
}
