package net.minecraft.src;

public class ItemAnvilBlock extends ItemMultiTextureTile {
   public ItemAnvilBlock(Block var1) {
      super(var1.blockID - 256, var1, BlockAnvil.statuses);
   }

   @Override
   public int getMetadata(int var1) {
      return var1 << 2;
   }
}
