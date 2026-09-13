package btw.block.blocks;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.Block;
import net.minecraft.src.Icon;
import net.minecraft.src.IconRegister;

public class WhiteStoneStairsBlock extends StairsBlock {
   @Environment(EnvType.CLIENT)
   private Icon iconWhiteCobble;

   public WhiteStoneStairsBlock(int iBlockID) {
      super(iBlockID, Block.stone, 0);
      this.c(1.5F);
      this.b(10.0F);
      this.setPicksEffectiveOn();
      this.c("fcBlockWhiteStoneStairs");
   }

   @Override
   public int damageDropped(int iMetadata) {
      return iMetadata & 8;
   }

   public boolean getIsCobbleFromMetadata(int iMetadata) {
      return (iMetadata & 8) > 0;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void registerIcons(IconRegister register) {
      this.blockIcon = register.registerIcon("fcBlockWhiteStone");
      this.iconWhiteCobble = register.registerIcon("fcBlockWhiteCobble");
   }

   @Environment(EnvType.CLIENT)
   @Override
   public Icon getIcon(int iSide, int iMetadata) {
      return this.getIsCobbleFromMetadata(iMetadata) ? this.iconWhiteCobble : this.blockIcon;
   }
}
