package net.minecraft.src;

import java.util.Random;

public class EnchantmentThorns extends Enchantment {
   public EnchantmentThorns(int var1, int var2) {
      super(var1, var2, EnumEnchantmentType.armor_torso);
      this.b("thorns");
   }

   @Override
   public int getMinEnchantability(int var1) {
      return 10 + 20 * (var1 - 1);
   }

   @Override
   public int getMaxEnchantability(int var1) {
      return super.getMinEnchantability(var1) + 50;
   }

   @Override
   public int getMaxLevel() {
      return 3;
   }

   @Override
   public boolean canApply(ItemStack var1) {
      return var1.getItem() instanceof ItemArmor ? true : super.canApply(var1);
   }

   public static boolean func_92094_a(int var0, Random var1) {
      return var0 <= 0 ? false : var1.nextFloat() < 0.15F * var0;
   }

   public static int func_92095_b(int var0, Random var1) {
      return var0 > 10 ? var0 - 10 : 1 + var1.nextInt(4);
   }

   public static void func_92096_a(Entity var0, EntityLiving var1, Random var2) {
      int var3 = EnchantmentHelper.func_92098_i(var1);
      ItemStack var4 = EnchantmentHelper.func_92099_a(Enchantment.thorns, var1);
      if (func_92094_a(var3, var2)) {
         var0.attackEntityFrom(DamageSource.causeThornsDamage(var1), func_92095_b(var3, var2));
         var0.playSound("damage.thorns", 0.5F, 1.0F);
         if (var4 != null) {
            var4.damageItem(3, var1);
         }
      } else if (var4 != null) {
         var4.damageItem(1, var1);
      }
   }
}
