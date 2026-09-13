package net.minecraft.src;

public class ItemLeaves extends ItemBlock {
   public ItemLeaves(int var1) {
      super(var1);
      this.e(0);
      this.a(true);
   }

   @Override
   public int getMetadata(int var1) {
      return var1 | 4;
   }

   @Override
   public Icon getIconFromDamage(int var1) {
      return Block.leaves.getIcon(0, var1);
   }

   @Override
   public int getColorFromItemStack(ItemStack var1, int var2) {
      int var3 = var1.getItemDamage();
      if ((var3 & 1) == 1) {
         return ColorizerFoliage.getFoliageColorPine();
      } else {
         return (var3 & 2) == 2 ? ColorizerFoliage.getFoliageColorBirch() : ColorizerFoliage.getFoliageColorBasic();
      }
   }

   @Override
   public String getUnlocalizedName(ItemStack var1) {
      int var2 = var1.getItemDamage();
      if (var2 < 0 || var2 >= BlockLeaves.LEAF_TYPES.length) {
         var2 = 0;
      }

      return super.getUnlocalizedName() + "." + BlockLeaves.LEAF_TYPES[var2];
   }
}
