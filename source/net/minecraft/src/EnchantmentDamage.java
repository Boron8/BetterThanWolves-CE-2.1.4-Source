package net.minecraft.src;

public class EnchantmentDamage extends Enchantment {
   private static final String[] protectionName = new String[]{"all", "undead", "arthropods"};
   private static final int[] baseEnchantability = new int[]{1, 5, 5};
   private static final int[] levelEnchantability = new int[]{11, 8, 8};
   private static final int[] thresholdEnchantability = new int[]{20, 20, 20};
   public final int damageType;

   public EnchantmentDamage(int var1, int var2, int var3) {
      super(var1, var2, EnumEnchantmentType.weapon);
      this.damageType = var3;
   }

   @Override
   public int getMinEnchantability(int var1) {
      return baseEnchantability[this.damageType] + (var1 - 1) * levelEnchantability[this.damageType];
   }

   @Override
   public int getMaxEnchantability(int var1) {
      return this.getMinEnchantability(var1) + thresholdEnchantability[this.damageType];
   }

   @Override
   public int getMaxLevel() {
      return 5;
   }

   @Override
   public int calcModifierLiving(int var1, EntityLiving var2) {
      if (this.damageType == 0) {
         return MathHelper.floor_float(var1 * 2.75F);
      } else if (this.damageType == 1 && var2.getCreatureAttribute() == EnumCreatureAttribute.UNDEAD) {
         return MathHelper.floor_float(var1 * 4.5F);
      } else {
         return this.damageType == 2 && var2.getCreatureAttribute() == EnumCreatureAttribute.ARTHROPOD ? MathHelper.floor_float(var1 * 4.5F) : 0;
      }
   }

   @Override
   public String getName() {
      return "enchantment.damage." + protectionName[this.damageType];
   }

   @Override
   public boolean canApplyTogether(Enchantment var1) {
      return !(var1 instanceof EnchantmentDamage);
   }

   @Override
   public boolean canApply(ItemStack var1) {
      return var1.getItem() instanceof ItemAxe ? true : super.canApply(var1);
   }
}
