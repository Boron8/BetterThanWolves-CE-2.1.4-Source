package net.minecraft.src;

public class ItemColored extends ItemBlock {
   private final Block blockRef = Block.blocksList[this.g()];
   private String[] blockNames;

   public ItemColored(int var1, boolean var2) {
      super(var1);
      if (var2) {
         this.e(0);
         this.a(true);
      }
   }

   @Override
   public int getColorFromItemStack(ItemStack var1, int var2) {
      return this.blockRef.getRenderColor(var1.getItemDamage());
   }

   @Override
   public Icon getIconFromDamage(int var1) {
      return this.blockRef.getIcon(0, var1);
   }

   @Override
   public int getMetadata(int var1) {
      return var1;
   }

   public ItemColored setBlockNames(String[] var1) {
      this.blockNames = var1;
      return this;
   }

   @Override
   public String getUnlocalizedName(ItemStack var1) {
      if (this.blockNames == null) {
         return super.getUnlocalizedName(var1);
      } else {
         int var2 = var1.getItemDamage();
         return var2 >= 0 && var2 < this.blockNames.length ? super.getUnlocalizedName(var1) + "." + this.blockNames[var2] : super.getUnlocalizedName(var1);
      }
   }
}
