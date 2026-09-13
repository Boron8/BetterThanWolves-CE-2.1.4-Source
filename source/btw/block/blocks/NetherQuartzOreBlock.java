package btw.block.blocks;

import btw.block.BTWBlocks;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.BlockOre;
import net.minecraft.src.IconRegister;
import net.minecraft.src.RenderBlocks;

public class NetherQuartzOreBlock extends BlockOre {
   public NetherQuartzOreBlock(int iBlockID) {
      super(iBlockID);
      this.setBlockMaterial(BTWBlocks.netherRockMaterial);
      this.c(1.0F);
      this.b(5.0F);
      this.a(j);
      this.c("netherquartz");
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void registerIcons(IconRegister register) {
      this.blockIcon = register.registerIcon("fcBlockNetherQuartz");
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
