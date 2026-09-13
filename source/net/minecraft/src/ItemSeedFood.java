package net.minecraft.src;

public class ItemSeedFood extends ItemFood {
   private int cropId;
   private int soilId;

   public ItemSeedFood(int var1, int var2, float var3, int var4, int var5) {
      super(var1, var2, var3, false);
      this.cropId = var4;
      this.soilId = var5;
   }

   @Override
   public boolean onItemUse(ItemStack var1, EntityPlayer var2, World var3, int var4, int var5, int var6, int var7, float var8, float var9, float var10) {
      if (var7 != 1) {
         return false;
      } else if (var2.canPlayerEdit(var4, var5, var6, var7, var1) && var2.canPlayerEdit(var4, var5 + 1, var6, var7, var1)) {
         int var11 = var3.getBlockId(var4, var5, var6);
         if (var11 == this.soilId && var3.isAirBlock(var4, var5 + 1, var6)) {
            var3.setBlock(var4, var5 + 1, var6, this.cropId);
            var1.stackSize--;
            return true;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }
}
