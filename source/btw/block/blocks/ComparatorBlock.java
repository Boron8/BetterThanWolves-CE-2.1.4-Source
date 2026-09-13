package btw.block.blocks;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.Block;
import net.minecraft.src.BlockComparator;
import net.minecraft.src.Icon;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.Tessellator;

public class ComparatorBlock extends BlockComparator {
   public ComparatorBlock(int id, boolean powered) {
      super(id, powered);
      this.initBlockBounds(0.0, 0.0, 0.0, 1.0, 0.125, 1.0);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean renderBlock(RenderBlocks render, int par2, int par3, int par4) {
      Tessellator var5 = Tessellator.instance;
      var5.setBrightness(this.e(render.blockAccess, par2, par3, par4));
      var5.setColorOpaque_F(1.0F, 1.0F, 1.0F);
      int var6 = render.blockAccess.getBlockMetadata(par2, par3, par4);
      int var7 = var6 & 3;
      double var8 = 0.0;
      double var10 = -0.1875;
      double var12 = 0.0;
      double var14 = 0.0;
      double var16 = 0.0;
      Icon var18;
      if (this.d(var6)) {
         var18 = Block.torchRedstoneActive.getBlockTextureFromSide(0);
      } else {
         var10 -= 0.1875;
         var18 = Block.torchRedstoneIdle.getBlockTextureFromSide(0);
      }

      switch (var7) {
         case 0:
            var12 = -0.3125;
            var16 = 1.0;
            break;
         case 1:
            var8 = 0.3125;
            var14 = -1.0;
            break;
         case 2:
            var12 = 0.3125;
            var16 = -1.0;
            break;
         case 3:
            var8 = -0.3125;
            var14 = 1.0;
      }

      render.renderTorchAtAngle(this, par2 + 0.25 * var14 + 0.1875 * var16, par3 - 0.1875F, par4 + 0.25 * var16 + 0.1875 * var14, 0.0, 0.0, var6);
      render.renderTorchAtAngle(this, par2 + 0.25 * var14 + -0.1875 * var16, par3 - 0.1875F, par4 + 0.25 * var16 + -0.1875 * var14, 0.0, 0.0, var6);
      render.setOverrideBlockTexture(var18);
      render.renderTorchAtAngle(this, par2 + var8, par3 + var10, par4 + var12, 0.0, 0.0, var6);
      render.clearOverrideBlockTexture();
      render.setRenderBounds(this.getFixedBlockBoundsFromPool());
      render.renderBlockRedstoneLogicMetadata(this, par2, par3, par4, var7);
      return true;
   }
}
