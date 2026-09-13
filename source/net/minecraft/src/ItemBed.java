package net.minecraft.src;

public class ItemBed extends Item {
   public ItemBed(int var1) {
      super(var1);
      this.a(CreativeTabs.tabDecorations);
   }

   @Override
   public boolean onItemUse(ItemStack var1, EntityPlayer var2, World var3, int var4, int var5, int var6, int var7, float var8, float var9, float var10) {
      if (var3.isRemote) {
         return true;
      } else if (var7 != 1) {
         return false;
      } else {
         var5++;
         BlockBed var11 = (BlockBed)Block.bed;
         int var12 = MathHelper.floor_double(var2.rotationYaw * 4.0F / 360.0F + 0.5) & 3;
         byte var13 = 0;
         byte var14 = 0;
         if (var12 == 0) {
            var14 = 1;
         }

         if (var12 == 1) {
            var13 = -1;
         }

         if (var12 == 2) {
            var14 = -1;
         }

         if (var12 == 3) {
            var13 = 1;
         }

         if (!var2.canPlayerEdit(var4, var5, var6, var7, var1) || !var2.canPlayerEdit(var4 + var13, var5, var6 + var14, var7, var1)) {
            return false;
         } else if (var3.isAirBlock(var4, var5, var6)
            && var3.isAirBlock(var4 + var13, var5, var6 + var14)
            && var3.doesBlockHaveSolidTopSurface(var4, var5 - 1, var6)
            && var3.doesBlockHaveSolidTopSurface(var4 + var13, var5 - 1, var6 + var14)) {
            var3.setBlock(var4, var5, var6, var11.blockID, var12, 3);
            if (var3.getBlockId(var4, var5, var6) == var11.blockID) {
               var3.setBlock(var4 + var13, var5, var6 + var14, var11.blockID, var12 + 8, 3);
            }

            var1.stackSize--;
            return true;
         } else {
            return false;
         }
      }
   }
}
