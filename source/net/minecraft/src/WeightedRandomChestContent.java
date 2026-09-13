package net.minecraft.src;

import java.util.Random;

public class WeightedRandomChestContent extends WeightedRandomItem {
   private ItemStack theItemId = null;
   private int theMinimumChanceToGenerateItem;
   private int theMaximumChanceToGenerateItem;

   public WeightedRandomChestContent(int var1, int var2, int var3, int var4, int var5) {
      super(var5);
      this.theItemId = new ItemStack(var1, 1, var2);
      this.theMinimumChanceToGenerateItem = var3;
      this.theMaximumChanceToGenerateItem = var4;
   }

   public WeightedRandomChestContent(ItemStack var1, int var2, int var3, int var4) {
      super(var4);
      this.theItemId = var1;
      this.theMinimumChanceToGenerateItem = var2;
      this.theMaximumChanceToGenerateItem = var3;
   }

   public static void generateChestContents(Random var0, WeightedRandomChestContent[] var1, IInventory var2, int var3) {
      for (int var4 = 0; var4 < var3; var4++) {
         WeightedRandomChestContent var5 = (WeightedRandomChestContent)WeightedRandom.getRandomItem(var0, var1);
         int var6 = var5.theMinimumChanceToGenerateItem + var0.nextInt(var5.theMaximumChanceToGenerateItem - var5.theMinimumChanceToGenerateItem + 1);
         if (var5.theItemId.getMaxStackSize() >= var6) {
            ItemStack var9 = var5.theItemId.copy();
            var9.stackSize = var6;
            var2.setInventorySlotContents(var0.nextInt(var2.getSizeInventory()), var9);
         } else {
            for (int var7 = 0; var7 < var6; var7++) {
               ItemStack var8 = var5.theItemId.copy();
               var8.stackSize = 1;
               var2.setInventorySlotContents(var0.nextInt(var2.getSizeInventory()), var8);
            }
         }
      }
   }

   public static void generateDispenserContents(Random var0, WeightedRandomChestContent[] var1, TileEntityDispenser var2, int var3) {
      for (int var4 = 0; var4 < var3; var4++) {
         WeightedRandomChestContent var5 = (WeightedRandomChestContent)WeightedRandom.getRandomItem(var0, var1);
         int var6 = var5.theMinimumChanceToGenerateItem + var0.nextInt(var5.theMaximumChanceToGenerateItem - var5.theMinimumChanceToGenerateItem + 1);
         if (var5.theItemId.getMaxStackSize() >= var6) {
            ItemStack var9 = var5.theItemId.copy();
            var9.stackSize = var6;
            var2.setInventorySlotContents(var0.nextInt(var2.getSizeInventory()), var9);
         } else {
            for (int var7 = 0; var7 < var6; var7++) {
               ItemStack var8 = var5.theItemId.copy();
               var8.stackSize = 1;
               var2.setInventorySlotContents(var0.nextInt(var2.getSizeInventory()), var8);
            }
         }
      }
   }

   public static WeightedRandomChestContent[] func_92080_a(WeightedRandomChestContent[] var0, WeightedRandomChestContent... var1) {
      WeightedRandomChestContent[] var2 = new WeightedRandomChestContent[var0.length + var1.length];
      int var3 = 0;

      for (int var4 = 0; var4 < var0.length; var4++) {
         var2[var3++] = var0[var4];
      }

      for (WeightedRandomChestContent var7 : var1) {
         var2[var3++] = var7;
      }

      return var2;
   }
}
