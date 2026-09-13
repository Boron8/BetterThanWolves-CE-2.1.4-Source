package net.minecraft.src;

import java.util.List;

public class ItemAppleGold extends ItemFood {
   public ItemAppleGold(int var1, int var2, float var3, boolean var4) {
      super(var1, var2, var3, var4);
      this.a(true);
   }

   @Override
   public boolean hasEffect(ItemStack var1) {
      return var1.getItemDamage() > 0;
   }

   @Override
   public EnumRarity getRarity(ItemStack var1) {
      return var1.getItemDamage() == 0 ? EnumRarity.rare : EnumRarity.epic;
   }

   @Override
   protected void onFoodEaten(ItemStack var1, World var2, EntityPlayer var3) {
      if (var1.getItemDamage() > 0) {
         if (!var2.isRemote) {
            var3.d(new PotionEffect(Potion.regeneration.id, 600, 3));
            var3.d(new PotionEffect(Potion.resistance.id, 6000, 0));
            var3.d(new PotionEffect(Potion.fireResistance.id, 6000, 0));
         }
      } else {
         super.onFoodEaten(var1, var2, var3);
      }
   }

   @Override
   public void getSubItems(int var1, CreativeTabs var2, List var3) {
      var3.add(new ItemStack(var1, 1, 0));
      var3.add(new ItemStack(var1, 1, 1));
   }
}
