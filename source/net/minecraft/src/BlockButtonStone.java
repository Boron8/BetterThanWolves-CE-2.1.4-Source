package net.minecraft.src;

public class BlockButtonStone extends BlockButton {
   protected BlockButtonStone(int var1) {
      super(var1, false);
   }

   @Override
   public Icon getIcon(int var1, int var2) {
      return Block.stone.getBlockTextureFromSide(1);
   }
}
