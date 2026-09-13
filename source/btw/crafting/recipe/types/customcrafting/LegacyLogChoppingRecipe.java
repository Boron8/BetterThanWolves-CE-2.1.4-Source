package btw.crafting.recipe.types.customcrafting;

import btw.block.BTWBlocks;
import btw.item.BTWItems;
import btw.item.items.AxeItem;
import net.minecraft.src.Block;
import net.minecraft.src.IInventory;
import net.minecraft.src.IRecipe;
import net.minecraft.src.InventoryCrafting;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;

@Deprecated
public class LegacyLogChoppingRecipe implements IRecipe {
   @Override
   public boolean matches(InventoryCrafting craftingInventory, World world) {
      ItemStack axeStack = null;
      ItemStack logStack = null;

      for (int iTempSlot = 0; iTempSlot < craftingInventory.getSizeInventory(); iTempSlot++) {
         ItemStack tempStack = craftingInventory.getStackInSlot(iTempSlot);
         if (tempStack != null) {
            if (this.isAxe(tempStack)) {
               if (axeStack != null) {
                  return false;
               }

               axeStack = tempStack;
            } else {
               if (!this.isLog(tempStack)) {
                  return false;
               }

               if (logStack != null) {
                  return false;
               }

               logStack = tempStack;
            }
         }
      }

      return axeStack != null && logStack != null;
   }

   @Override
   public ItemStack getCraftingResult(InventoryCrafting craftingInventory) {
      ItemStack axeStack = null;
      ItemStack logStack = null;

      for (int iTempSlot = 0; iTempSlot < craftingInventory.getSizeInventory(); iTempSlot++) {
         ItemStack tempStack = craftingInventory.getStackInSlot(iTempSlot);
         if (tempStack != null) {
            if (this.isAxe(tempStack)) {
               if (axeStack != null) {
                  return null;
               }

               axeStack = tempStack;
            } else {
               if (!this.isLog(tempStack)) {
                  return null;
               }

               if (logStack != null) {
                  return null;
               }

               logStack = tempStack;
            }
         }
      }

      if (logStack != null && axeStack != null) {
         ItemStack resultStack = null;
         AxeItem axeItem = (AxeItem)axeStack.getItem();
         if (axeItem.toolMaterial.getHarvestLevel() <= 1) {
            resultStack = new ItemStack(Item.stick, 2);
         } else {
            int iLogID = logStack.itemID;
            if (iLogID == BTWBlocks.bloodWoodLog.blockID) {
               resultStack = new ItemStack(Block.planks.blockID, 2, 4);
            } else {
               resultStack = new ItemStack(Block.planks.blockID, 2, logStack.getItemDamage());
            }
         }

         return resultStack;
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
      return null;
   }

   @Override
   public boolean matches(IRecipe recipe) {
      return false;
   }

   @Override
   public boolean hasSecondaryOutput() {
      return true;
   }

   @Override
   public ItemStack[] getSecondaryOutput(IInventory inventory) {
      ItemStack result = this.getCraftingResult((InventoryCrafting)inventory);
      ItemStack logStack = null;

      for (int i = 0; i < inventory.getSizeInventory(); i++) {
         ItemStack craftingStack = inventory.getStackInSlot(i);
         if (craftingStack != null && this.isLog(craftingStack)) {
            logStack = craftingStack.copy();
         }
      }

      ItemStack[] outputs;
      if (logStack.itemID == BTWBlocks.bloodWoodLog.blockID) {
         outputs = new ItemStack[3];
         if (result.itemID == Block.planks.blockID) {
            outputs[0] = new ItemStack(BTWItems.sawDust, 1);
            outputs[1] = new ItemStack(BTWItems.soulDust, 1);
            outputs[2] = new ItemStack(BTWItems.bark, 1, logStack.getItemDamage());
         } else {
            outputs[0] = new ItemStack(BTWItems.sawDust, 3);
            outputs[1] = new ItemStack(BTWItems.soulDust, 1);
            outputs[2] = new ItemStack(BTWItems.bark, 1, logStack.getItemDamage());
         }
      } else {
         outputs = new ItemStack[2];
         if (result.itemID == Block.planks.blockID) {
            outputs[0] = new ItemStack(BTWItems.sawDust, 2);
            outputs[1] = new ItemStack(BTWItems.bark, 1, logStack.getItemDamage());
         } else {
            outputs[0] = new ItemStack(BTWItems.sawDust, 4);
            outputs[1] = new ItemStack(BTWItems.bark, 1, logStack.getItemDamage());
         }
      }

      return outputs;
   }

   private boolean isAxe(ItemStack stack) {
      int iItemID = stack.itemID;
      return stack.getItem() instanceof AxeItem;
   }

   private boolean isLog(ItemStack stack) {
      int iItemID = stack.itemID;
      return iItemID == BTWBlocks.bloodWoodLog.blockID || iItemID == Block.wood.blockID;
   }
}
