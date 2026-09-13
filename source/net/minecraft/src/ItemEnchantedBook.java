package net.minecraft.src;

import java.util.List;
import java.util.Random;

public class ItemEnchantedBook extends Item {
   public ItemEnchantedBook(int var1) {
      super(var1);
   }

   @Override
   public boolean hasEffect(ItemStack var1) {
      return true;
   }

   @Override
   public boolean isItemTool(ItemStack var1) {
      return false;
   }

   @Override
   public EnumRarity getRarity(ItemStack var1) {
      return this.func_92110_g(var1).tagCount() > 0 ? EnumRarity.uncommon : super.getRarity(var1);
   }

   public NBTTagList func_92110_g(ItemStack var1) {
      return var1.stackTagCompound != null && var1.stackTagCompound.hasKey("StoredEnchantments")
         ? (NBTTagList)var1.stackTagCompound.getTag("StoredEnchantments")
         : new NBTTagList();
   }

   @Override
   public void addInformation(ItemStack var1, EntityPlayer var2, List var3, boolean var4) {
      super.addInformation(var1, var2, var3, var4);
      NBTTagList var5 = this.func_92110_g(var1);
      if (var5 != null) {
         for (int var6 = 0; var6 < var5.tagCount(); var6++) {
            short var7 = ((NBTTagCompound)var5.tagAt(var6)).getShort("id");
            short var8 = ((NBTTagCompound)var5.tagAt(var6)).getShort("lvl");
            if (Enchantment.enchantmentsList[var7] != null) {
               var3.add(Enchantment.enchantmentsList[var7].getTranslatedName(var8));
            }
         }
      }
   }

   public void func_92115_a(ItemStack var1, EnchantmentData var2) {
      NBTTagList var3 = this.func_92110_g(var1);
      boolean var4 = true;

      for (int var5 = 0; var5 < var3.tagCount(); var5++) {
         NBTTagCompound var6 = (NBTTagCompound)var3.tagAt(var5);
         if (var6.getShort("id") == var2.enchantmentobj.effectId) {
            if (var6.getShort("lvl") < var2.enchantmentLevel) {
               var6.setShort("lvl", (short)var2.enchantmentLevel);
            }

            var4 = false;
            break;
         }
      }

      if (var4) {
         NBTTagCompound var7 = new NBTTagCompound();
         var7.setShort("id", (short)var2.enchantmentobj.effectId);
         var7.setShort("lvl", (short)var2.enchantmentLevel);
         var3.appendTag(var7);
      }

      if (!var1.hasTagCompound()) {
         var1.setTagCompound(new NBTTagCompound());
      }

      var1.getTagCompound().setTag("StoredEnchantments", var3);
   }

   public ItemStack func_92111_a(EnchantmentData var1) {
      ItemStack var2 = new ItemStack(this);
      this.func_92115_a(var2, var1);
      return var2;
   }

   public void func_92113_a(Enchantment var1, List var2) {
      for (int var3 = var1.getMinLevel(); var3 <= var1.getMaxLevel(); var3++) {
         var2.add(this.func_92111_a(new EnchantmentData(var1, var3)));
      }
   }

   public ItemStack func_92109_a(Random var1) {
      Enchantment var2 = Enchantment.field_92090_c[var1.nextInt(Enchantment.field_92090_c.length)];
      ItemStack var3 = new ItemStack(this.itemID, 1, 0);
      int var4 = MathHelper.getRandomIntegerInRange(var1, var2.getMinLevel(), var2.getMaxLevel());
      this.func_92115_a(var3, new EnchantmentData(var2, var4));
      return var3;
   }

   public WeightedRandomChestContent func_92114_b(Random var1) {
      return this.func_92112_a(var1, 1, 1, 1);
   }

   public WeightedRandomChestContent func_92112_a(Random var1, int var2, int var3, int var4) {
      Enchantment var5 = Enchantment.field_92090_c[var1.nextInt(Enchantment.field_92090_c.length)];
      ItemStack var6 = new ItemStack(this.itemID, 1, 0);
      int var7 = MathHelper.getRandomIntegerInRange(var1, var5.getMinLevel(), var5.getMaxLevel());
      this.func_92115_a(var6, new EnchantmentData(var5, var7));
      return new WeightedRandomChestContent(var6, var2, var3, var4);
   }
}
