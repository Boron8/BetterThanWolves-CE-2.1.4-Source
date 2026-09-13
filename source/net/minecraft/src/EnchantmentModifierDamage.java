package net.minecraft.src;

final class EnchantmentModifierDamage implements IEnchantmentModifier {
   public int damageModifier;
   public DamageSource source;

   private EnchantmentModifierDamage() {
   }

   @Override
   public void calculateModifier(Enchantment var1, int var2) {
      this.damageModifier = this.damageModifier + var1.calcModifierDamage(var2, this.source);
   }
}
