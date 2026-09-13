package net.minecraft.src;

import btw.item.items.ArmorItemMod;
import com.prupe.mcpatcher.cc.ColorizeEntity;
import java.util.ArrayList;
import net.minecraft.server.MinecraftServer;

public class RecipesArmorDyes implements IRecipe {
   @Override
   public boolean matches(InventoryCrafting par1InventoryCrafting, World par2World) {
      ItemStack var3 = null;
      ArrayList var4 = new ArrayList();

      for (int var5 = 0; var5 < par1InventoryCrafting.getSizeInventory(); var5++) {
         ItemStack var6 = par1InventoryCrafting.getStackInSlot(var5);
         if (var6 != null) {
            if (var6.getItem() instanceof ItemArmor) {
               ItemArmor var7 = (ItemArmor)var6.getItem();
               if (var7 instanceof ArmorItemMod) {
                  if (!((ArmorItemMod)var7).hasCustomColors() || var3 != null) {
                     return false;
                  }
               } else if (var7.getArmorMaterial() != EnumArmorMaterial.CLOTH || var3 != null) {
                  return false;
               }

               var3 = var6;
            } else {
               if (var6.itemID != Item.dyePowder.itemID) {
                  return false;
               }

               var4.add(var6);
            }
         }
      }

      return var3 != null && !var4.isEmpty();
   }

   @Override
   public ItemStack getCraftingResult(InventoryCrafting par1InventoryCrafting) {
      ItemStack var2 = null;
      int[] var3 = new int[3];
      int var4 = 0;
      int var5 = 0;
      ItemArmor var6 = null;

      for (int var7 = 0; var7 < par1InventoryCrafting.getSizeInventory(); var7++) {
         ItemStack var8 = par1InventoryCrafting.getStackInSlot(var7);
         if (var8 != null) {
            if (var8.getItem() instanceof ItemArmor) {
               var6 = (ItemArmor)var8.getItem();
               if (var6 instanceof ArmorItemMod) {
                  if (!((ArmorItemMod)var6).hasCustomColors() || var2 != null) {
                     return null;
                  }
               } else if (var6.getArmorMaterial() != EnumArmorMaterial.CLOTH || var2 != null) {
                  return null;
               }

               var2 = var8.copy();
               var2.stackSize = 1;
               if (var6.hasColor(var8)) {
                  int var9 = var6.getColor(var2);
                  float var10 = (var9 >> 16 & 0xFF) / 255.0F;
                  float var11 = (var9 >> 8 & 0xFF) / 255.0F;
                  float var12 = (var9 & 0xFF) / 255.0F;
                  var4 = (int)(var4 + Math.max(var10, Math.max(var11, var12)) * 255.0F);
                  var3[0] = (int)(var3[0] + var10 * 255.0F);
                  var3[1] = (int)(var3[1] + var11 * 255.0F);
                  var3[2] = (int)(var3[2] + var12 * 255.0F);
                  var5++;
               }
            } else {
               if (var8.itemID != Item.dyePowder.itemID) {
                  return null;
               }

               float[] var14;
               if (MinecraftServer.getIsServer()) {
                  var14 = EntitySheep.fleeceColorTable[BlockCloth.getBlockFromDye(var8.getItemDamage())];
               } else {
                  var14 = ColorizeEntity.getArmorDyeColor(
                     EntitySheep.fleeceColorTable[BlockCloth.getBlockFromDye(var8.getItemDamage())], BlockCloth.getBlockFromDye(var8.getItemDamage())
                  );
               }

               int var16 = (int)(var14[0] * 255.0F);
               int var15 = (int)(var14[1] * 255.0F);
               int var17 = (int)(var14[2] * 255.0F);
               var4 += Math.max(var16, Math.max(var15, var17));
               var3[0] += var16;
               var3[1] += var15;
               var3[2] += var17;
               var5++;
            }
         }
      }

      if (var6 == null) {
         return null;
      } else {
         int var161 = var3[0] / var5;
         int var13 = var3[1] / var5;
         int var9 = var3[2] / var5;
         float var10 = (float)var4 / var5;
         float var11 = Math.max(var161, Math.max(var13, var9));
         var161 = (int)(var161 * var10 / var11);
         var13 = (int)(var13 * var10 / var11);
         var9 = (int)(var9 * var10 / var11);
         int var17 = (var161 << 8) + var13;
         var17 = (var17 << 8) + var9;
         var6.func_82813_b(var2, var17);
         return var2;
      }
   }

   @Override
   public int getRecipeSize() {
      return 10;
   }

   @Override
   public ItemStack getRecipeOutput() {
      return null;
   }

   @Override
   public boolean matches(IRecipe recipe) {
      return false;
   }

   @Override
   public boolean hasSecondaryOutput() {
      return false;
   }

   @Override
   public ItemStack[] getSecondaryOutput(IInventory inventory) {
      return null;
   }
}
