package btw.block.blocks;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.Block;
import net.minecraft.src.Icon;

public class ButtonBlockStone extends ButtonBlock {
   public ButtonBlockStone(int iBlockID) {
      super(iBlockID, false);
      this.setPicksEffectiveOn(true);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public Icon getIcon(int iSide, int iMetadata) {
      return Block.stone.getBlockTextureFromSide(1);
   }
}
