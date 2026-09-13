package net.minecraft.src;

final class CreativeTabBlock extends CreativeTabs {
   CreativeTabBlock(int var1, String var2) {
      super(var1, var2);
   }

   @Override
   public int getTabIconItemIndex() {
      return Block.brick.blockID;
   }
}
