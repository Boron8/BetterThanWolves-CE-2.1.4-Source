package net.minecraft.src;

public class BlockPressurePlateWeighted extends BlockBasePressurePlate {
   private final int maxItemsWeighted;

   protected BlockPressurePlateWeighted(int var1, String var2, Material var3, int var4) {
      super(var1, var2, var3);
      this.maxItemsWeighted = var4;
   }

   @Override
   protected int getPlateState(World var1, int var2, int var3, int var4) {
      int var5 = 0;

      for (EntityItem var7 : var1.getEntitiesWithinAABB(EntityItem.class, this.a(var2, var3, var4))) {
         var5 += var7.getEntityItem().stackSize;
         if (var5 >= this.maxItemsWeighted) {
            break;
         }
      }

      if (var5 <= 0) {
         return 0;
      } else {
         float var8 = (float)Math.min(this.maxItemsWeighted, var5) / this.maxItemsWeighted;
         return MathHelper.ceiling_float_int(var8 * 15.0F);
      }
   }

   @Override
   protected int getPowerSupply(int var1) {
      return var1;
   }

   @Override
   protected int getMetaFromWeight(int var1) {
      return var1;
   }

   @Override
   public int tickRate(World var1) {
      return 10;
   }
}
