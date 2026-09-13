package net.minecraft.src;

public class ItemMultiTextureTile extends ItemBlock {
   private final Block theBlock;
   private final String[] field_82804_b;

   public ItemMultiTextureTile(int var1, Block var2, String[] var3) {
      super(var1);
      this.theBlock = var2;
      this.field_82804_b = var3;
      this.e(0);
      this.a(true);
   }

   @Override
   public Icon getIconFromDamage(int var1) {
      return this.theBlock.getIcon(2, var1);
   }

   @Override
   public int getMetadata(int var1) {
      return var1;
   }

   @Override
   public String getUnlocalizedName(ItemStack var1) {
      int var2 = var1.getItemDamage();
      if (var2 < 0 || var2 >= this.field_82804_b.length) {
         var2 = 0;
      }

      return super.getUnlocalizedName() + "." + this.field_82804_b[var2];
   }
}
