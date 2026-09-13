package net.minecraft.src;

final class CreativeTabFood extends CreativeTabs {
   CreativeTabFood(int var1, String var2) {
      super(var1, var2);
   }

   @Override
   public int getTabIconItemIndex() {
      return Item.appleRed.itemID;
   }
}
