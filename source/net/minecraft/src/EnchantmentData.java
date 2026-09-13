package net.minecraft.src;

public class EnchantmentData extends WeightedRandomItem {
   public final Enchantment enchantmentobj;
   public final int enchantmentLevel;

   public EnchantmentData(Enchantment var1, int var2) {
      super(var1.getWeight());
      this.enchantmentobj = var1;
      this.enchantmentLevel = var2;
   }

   public EnchantmentData(int var1, int var2) {
      this(Enchantment.enchantmentsList[var1], var2);
   }
}
