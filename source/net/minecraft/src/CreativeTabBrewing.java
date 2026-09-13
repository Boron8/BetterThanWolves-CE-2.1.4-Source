package net.minecraft.src;

final class CreativeTabBrewing extends CreativeTabs {
   CreativeTabBrewing(int var1, String var2) {
      super(var1, var2);
   }

   @Override
   public int getTabIconItemIndex() {
      return Item.potion.itemID;
   }
}
