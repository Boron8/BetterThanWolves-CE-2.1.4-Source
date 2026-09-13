package net.minecraft.src;

final class CreativeTabDeco extends CreativeTabs {
   CreativeTabDeco(int var1, String var2) {
      super(var1, var2);
   }

   @Override
   public int getTabIconItemIndex() {
      return Block.plantRed.blockID;
   }
}
