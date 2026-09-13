package net.minecraft.src;

public class ItemHoe extends Item {
   protected EnumToolMaterial theToolMaterial;

   public ItemHoe(int var1, EnumToolMaterial var2) {
      super(var1);
      this.theToolMaterial = var2;
      this.maxStackSize = 1;
      this.e(var2.getMaxUses());
      this.a(CreativeTabs.tabTools);
   }

   @Override
   public boolean onItemUse(ItemStack var1, EntityPlayer var2, World var3, int var4, int var5, int var6, int var7, float var8, float var9, float var10) {
      if (!var2.canPlayerEdit(var4, var5, var6, var7, var1)) {
         return false;
      } else {
         int var11 = var3.getBlockId(var4, var5, var6);
         int var12 = var3.getBlockId(var4, var5 + 1, var6);
         if ((var7 == 0 || var12 != 0 || var11 != Block.grass.blockID) && var11 != Block.dirt.blockID) {
            return false;
         } else {
            Block var13 = Block.tilledField;
            var3.playSoundEffect(
               var4 + 0.5F,
               var5 + 0.5F,
               var6 + 0.5F,
               var13.stepSound.getStepSound(),
               (var13.stepSound.getVolume() + 1.0F) / 2.0F,
               var13.stepSound.getPitch() * 0.8F
            );
            if (var3.isRemote) {
               return true;
            } else {
               var3.setBlock(var4, var5, var6, var13.blockID);
               var1.damageItem(1, var2);
               return true;
            }
         }
      }
   }

   @Override
   public boolean isFull3D() {
      return true;
   }

   public String getMaterialName() {
      return this.theToolMaterial.toString();
   }
}
