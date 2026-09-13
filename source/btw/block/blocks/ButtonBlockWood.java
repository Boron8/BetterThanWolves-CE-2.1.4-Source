package btw.block.blocks;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.Block;
import net.minecraft.src.Icon;

public class ButtonBlockWood extends ButtonBlock {
   public ButtonBlockWood(int iBlockID) {
      super(iBlockID, true);
      this.setAxesEffectiveOn(true);
      this.setBuoyant();
   }

   @Environment(EnvType.CLIENT)
   @Override
   public Icon getIcon(int iSide, int iMetadata) {
      return Block.planks.getBlockTextureFromSide(1);
   }
}
