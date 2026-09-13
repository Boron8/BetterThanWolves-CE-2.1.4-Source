package net.minecraft.src;

final class CreativeTabSearch extends CreativeTabs {
   CreativeTabSearch(int var1, String var2) {
      super(var1, var2);
   }

   @Override
   public int getTabIconItemIndex() {
      return Item.compass.itemID;
   }
}
