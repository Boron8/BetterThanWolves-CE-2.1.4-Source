package net.minecraft.src;

final class CreativeTabMaterial extends CreativeTabs {
   CreativeTabMaterial(int var1, String var2) {
      super(var1, var2);
   }

   @Override
   public int getTabIconItemIndex() {
      return Item.stick.itemID;
   }
}
