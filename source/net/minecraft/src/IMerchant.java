package net.minecraft.src;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

public interface IMerchant {
   void setCustomer(EntityPlayer var1);

   EntityPlayer getCustomer();

   MerchantRecipeList getRecipes(EntityPlayer var1);

   @Environment(EnvType.CLIENT)
   void setRecipes(MerchantRecipeList var1);

   void useRecipe(MerchantRecipe var1);

   int getCurrentTradeLevel();

   int getCurrentTradeXP();

   int getCurrentTradeMaxXP();
}
