package net.minecraft.src;

public class BlockButtonWood extends BlockButton {
   protected BlockButtonWood(int var1) {
      super(var1, true);
   }

   @Override
   public Icon getIcon(int var1, int var2) {
      return Block.planks.getBlockTextureFromSide(1);
   }
}
