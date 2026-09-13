package net.minecraft.src;

public class ItemCloth extends ItemBlock {
   public ItemCloth(int var1) {
      super(var1);
      this.e(0);
      this.a(true);
   }

   @Override
   public Icon getIconFromDamage(int var1) {
      return Block.cloth.getIcon(2, BlockCloth.getBlockFromDye(var1));
   }

   @Override
   public int getMetadata(int var1) {
      return var1;
   }

   @Override
   public String getUnlocalizedName(ItemStack var1) {
      return super.getUnlocalizedName() + "." + ItemDye.dyeColorNames[BlockCloth.getBlockFromDye(var1.getItemDamage())];
   }
}
