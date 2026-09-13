package net.minecraft.src;

public class ItemTool extends Item {
   private Block[] blocksEffectiveAgainst;
   protected float efficiencyOnProperMaterial = 4.0F;
   private int damageVsEntity;
   protected EnumToolMaterial toolMaterial;

   protected ItemTool(int var1, int var2, EnumToolMaterial var3, Block[] var4) {
      super(var1);
      this.toolMaterial = var3;
      this.blocksEffectiveAgainst = var4;
      this.maxStackSize = 1;
      this.e(var3.getMaxUses());
      this.efficiencyOnProperMaterial = var3.getEfficiencyOnProperMaterial();
      this.damageVsEntity = var2 + var3.getDamageVsEntity();
      this.a(CreativeTabs.tabTools);
   }

   public float getStrVsBlock(ItemStack var1, Block var2) {
      for (int var3 = 0; var3 < this.blocksEffectiveAgainst.length; var3++) {
         if (this.blocksEffectiveAgainst[var3] == var2) {
            return this.efficiencyOnProperMaterial;
         }
      }

      return 1.0F;
   }

   @Override
   public boolean hitEntity(ItemStack var1, EntityLiving var2, EntityLiving var3) {
      var1.damageItem(2, var3);
      return true;
   }

   @Override
   public boolean onBlockDestroyed(ItemStack var1, World var2, int var3, int var4, int var5, int var6, EntityLiving var7) {
      if (Block.blocksList[var3].getBlockHardness(var2, var4, var5, var6) != 0.0) {
         var1.damageItem(1, var7);
      }

      return true;
   }

   @Override
   public int getDamageVsEntity(Entity var1) {
      return this.damageVsEntity;
   }

   @Override
   public boolean isFull3D() {
      return true;
   }

   @Override
   public int getItemEnchantability() {
      return this.toolMaterial.getEnchantability();
   }

   public String getToolMaterialName() {
      return this.toolMaterial.toString();
   }

   @Override
   public boolean getIsRepairable(ItemStack var1, ItemStack var2) {
      return this.toolMaterial.getToolCraftingMaterial() == var2.itemID ? true : super.getIsRepairable(var1, var2);
   }
}
