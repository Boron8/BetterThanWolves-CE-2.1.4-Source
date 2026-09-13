package btw.block.blocks;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.BlockQuartz;
import net.minecraft.src.Icon;
import net.minecraft.src.IconRegister;
import net.minecraft.src.RenderBlocks;

public class BlackStoneBlock extends BlockQuartz {
   @Environment(EnvType.CLIENT)
   private Icon iconChiseled;
   @Environment(EnvType.CLIENT)
   private Icon iconLinesSide;
   @Environment(EnvType.CLIENT)
   private Icon iconLinesTop;

   public BlackStoneBlock(int iBlockID) {
      super(iBlockID);
      this.c(2.0F);
      this.a(j);
      this.c("quartzBlock");
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void registerIcons(IconRegister register) {
      this.blockIcon = register.registerIcon("fcBlockBlackStone");
      this.iconChiseled = register.registerIcon("fcBlockBlackStone_chiseled");
      this.iconLinesSide = register.registerIcon("fcBlockBlackStone_lines");
      this.iconLinesTop = register.registerIcon("fcBlockBlackStone_lines_top");
   }

   @Environment(EnvType.CLIENT)
   @Override
   public Icon getIcon(int iSide, int iMetadata) {
      if (iMetadata == 1) {
         return this.iconChiseled;
      } else if (iMetadata == 2) {
         return iSide <= 1 ? this.iconLinesTop : this.iconLinesSide;
      } else if (iMetadata == 3) {
         return iSide >= 4 ? this.iconLinesTop : this.iconLinesSide;
      } else if (iMetadata == 4) {
         return iSide != 2 && iSide != 3 ? this.iconLinesSide : this.iconLinesTop;
      } else {
         return this.blockIcon;
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean renderBlock(RenderBlocks renderer, int i, int j, int k) {
      renderer.setRenderBounds(this.getBlockBoundsFromPoolBasedOnState(renderer.blockAccess, i, j, k));
      return renderer.renderBlockQuartz(this, i, j, k);
   }
}
