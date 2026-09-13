package net.minecraft.src;

final class CreativeTabRedstone extends CreativeTabs {
   CreativeTabRedstone(int var1, String var2) {
      super(var1, var2);
   }

   @Override
   public int getTabIconItemIndex() {
      return Item.redstone.itemID;
   }
}
