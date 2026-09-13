package net.minecraft.src;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class NpcMerchant implements IMerchant {
   private InventoryMerchant theMerchantInventory;
   private EntityPlayer customer;
   private MerchantRecipeList recipeList;

   public NpcMerchant(EntityPlayer par1EntityPlayer) {
      this.customer = par1EntityPlayer;
      this.theMerchantInventory = new InventoryMerchant(par1EntityPlayer, this);
   }

   @Override
   public EntityPlayer getCustomer() {
      return this.customer;
   }

   @Override
   public void setCustomer(EntityPlayer par1EntityPlayer) {
   }

   @Override
   public MerchantRecipeList getRecipes(EntityPlayer par1EntityPlayer) {
      return this.recipeList;
   }

   @Override
   public void setRecipes(MerchantRecipeList par1MerchantRecipeList) {
      this.recipeList = par1MerchantRecipeList;
   }

   @Override
   public void useRecipe(MerchantRecipe par1MerchantRecipe) {
      par1MerchantRecipe.incrementToolUses();
   }

   @Override
   public int getCurrentTradeLevel() {
      return 0;
   }

   @Override
   public int getCurrentTradeXP() {
      return 0;
   }

   @Override
   public int getCurrentTradeMaxXP() {
      return 0;
   }
}
