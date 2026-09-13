package btw.block.blocks;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.Material;
import net.minecraft.src.RenderBlocks;

public class FallingFullBlock extends FallingBlock {
   public FallingFullBlock(int iBlockID, Material material) {
      super(iBlockID, material);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean renderBlock(RenderBlocks renderer, int i, int j, int k) {
      return renderer.renderStandardFullBlock(this, i, j, k);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean doesItemRenderAsBlock(int iItemDamage) {
      return true;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void renderBlockMovedByPiston(RenderBlocks renderBlocks, int i, int j, int k) {
      renderBlocks.renderStandardFullBlockMovedByPiston(this, i, j, k);
   }
}
