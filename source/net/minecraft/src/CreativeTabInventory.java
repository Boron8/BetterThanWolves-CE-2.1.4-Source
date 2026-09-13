package net.minecraft.src;

final class CreativeTabInventory extends CreativeTabs {
   CreativeTabInventory(int var1, String var2) {
      super(var1, var2);
   }

   @Override
   public int getTabIconItemIndex() {
      return Block.chest.blockID;
   }
}
