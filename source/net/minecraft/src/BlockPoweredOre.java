package net.minecraft.src;

public class BlockPoweredOre extends BlockOreStorage {
   public BlockPoweredOre(int var1) {
      super(var1);
      this.a(CreativeTabs.tabRedstone);
   }

   @Override
   public boolean canProvidePower() {
      return true;
   }

   @Override
   public int isProvidingWeakPower(IBlockAccess var1, int var2, int var3, int var4, int var5) {
      return 15;
   }
}
