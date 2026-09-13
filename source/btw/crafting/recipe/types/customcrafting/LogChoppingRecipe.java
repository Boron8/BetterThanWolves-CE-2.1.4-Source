package btw.crafting.recipe.types.customcrafting;

import btw.item.items.AxeItem;
import net.minecraft.src.IInventory;
import net.minecraft.src.IRecipe;
import net.minecraft.src.InventoryCrafting;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;

public class LogChoppingRecipe implements IRecipe {
   private ItemStack output;
   private ItemStack[] secondaryOutputs;
   private ItemStack outputLowQuality;
   private ItemStack[] secondaryOutputsLowQuality;
   private ItemStack input;
   private boolean hasLowQualityOutput = false;

   public LogChoppingRecipe(ItemStack output, ItemStack[] secondaryOutputs, ItemStack input) {
      this.output = output;
      this.secondaryOutputs = secondaryOutputs;
      this.input = input;
   }

   public LogChoppingRecipe(ItemStack output, ItemStack[] secondaryOutputs, ItemStack outputLowQuality, ItemStack[] secondaryOutputsLowQuality, ItemStack input) {
      this(output, secondaryOutputs, input);
      this.outputLowQuality = outputLowQuality;
      this.secondaryOutputsLowQuality = secondaryOutputsLowQuality;
      this.hasLowQualityOutput = true;
   }

   @Override
   public boolean matches(InventoryCrafting inventory, World world) {
      ItemStack axeStack = null;
      ItemStack inputStack = null;

      for (int i = 0; i < inventory.getSizeInventory(); i++) {
         ItemStack stack = inventory.getStackInSlot(i);
         if (stack != null) {
            if (this.isAxe(stack)) {
               if (axeStack != null) {
                  return false;
               }

               axeStack = stack;
            } else {
               if (stack.itemID != this.input.itemID || stack.getItemDamage() != this.input.getItemDamage() && this.input.getItemDamage() != 32767) {
                  return false;
               }

               if (inputStack != null) {
                  return false;
               }

               inputStack = stack;
            }
         }
      }

      return axeStack != null && inputStack != null;
   }

   @Override
   public ItemStack getCraftingResult(InventoryCrafting inventory) {
      ItemStack axeStack = null;
      ItemStack inputStack = null;

      for (int i = 0; i < inventory.getSizeInventory(); i++) {
         ItemStack stack = inventory.getStackInSlot(i);
         if (stack != null) {
            if (this.isAxe(stack)) {
               if (axeStack != null) {
                  return null;
               }

               axeStack = stack;
            } else {
               if (stack.itemID != this.input.itemID || stack.getItemDamage() != this.input.getItemDamage() && this.input.getItemDamage() != 32767) {
                  return null;
               }

               if (inputStack != null) {
                  return null;
               }

               inputStack = stack;
            }
         }
      }

      if (inputStack != null && axeStack != null) {
         ItemStack resultStack = null;
         AxeItem axeItem = (AxeItem)axeStack.getItem();
         if (this.hasLowQualityOutput && this.isLowQualityAxe(axeStack)) {
            resultStack = this.outputLowQuality.copy();
         } else {
            resultStack = this.output.copy();
         }

         return resultStack;
      } else {
         return null;
      }
   }

   @Override
   public boolean matches(IRecipe recipe) {
      if (recipe instanceof LogChoppingRecipe) {
         LogChoppingRecipe logRecipe = (LogChoppingRecipe)recipe;
         if (logRecipe.input.isItemEqual(this.input)) {
            return true;
         }
      }

      return false;
   }

   @Override
   public boolean hasSecondaryOutput() {
      return this.secondaryOutputs != null;
   }

   @Override
   public ItemStack[] getSecondaryOutput(IInventory inventory) {
      ItemStack axeStack = null;
      ItemStack inputStack = null;

      for (int i = 0; i < inventory.getSizeInventory(); i++) {
         ItemStack stack = inventory.getStackInSlot(i);
         if (stack != null) {
            if (this.isAxe(stack)) {
               if (axeStack != null) {
                  return null;
               }

               axeStack = stack;
            } else {
               if (stack.itemID != this.input.itemID) {
                  return null;
               }

               if (inputStack != null) {
                  return null;
               }

               inputStack = stack;
            }
         }
      }

      if (inputStack != null && axeStack != null) {
         ItemStack[] resultStacks = null;
         AxeItem axeItem = (AxeItem)axeStack.getItem();
         if (this.hasLowQualityOutput && this.isLowQualityAxe(axeStack)) {
            resultStacks = this.secondaryOutputsLowQuality;
         } else {
            resultStacks = this.secondaryOutputs;
         }

         return resultStacks;
      } else {
         return null;
      }
   }

   @Override
   public int getRecipeSize() {
      return 2;
   }

   @Override
   public ItemStack getRecipeOutput() {
      return this.output;
   }

   public ItemStack[] getSecondaryOutput() {
      return this.secondaryOutputs;
   }

   public ItemStack getRecipeOutputLowQuality() {
      return this.outputLowQuality;
   }

   public ItemStack[] getSecondaryOutputLowQuality() {
      return this.secondaryOutputsLowQuality;
   }

   public boolean getHasLowQualityOutputs() {
      return this.hasLowQualityOutput;
   }

   public ItemStack getInput() {
      return this.input;
   }

   private boolean isAxe(ItemStack stack) {
      return stack.getItem() instanceof AxeItem;
   }

   private boolean isLowQualityAxe(ItemStack stack) {
      return this.isAxe(stack) && ((AxeItem)stack.getItem()).toolMaterial.getHarvestLevel() <= 1;
   }
}
