package net.minecraft.src;

final class CreativeTabTransport extends CreativeTabs {
   CreativeTabTransport(int var1, String var2) {
      super(var1, var2);
   }

   @Override
   public int getTabIconItemIndex() {
      return Block.railPowered.blockID;
   }
}
