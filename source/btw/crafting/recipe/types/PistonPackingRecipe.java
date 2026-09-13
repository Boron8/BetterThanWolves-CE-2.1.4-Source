package btw.crafting.recipe.types;

import net.minecraft.src.Block;
import net.minecraft.src.ItemStack;

public class PistonPackingRecipe {
   private final Block output;
   private final int outputMetadata;
   private final ItemStack[] input;

   public PistonPackingRecipe(Block output, int outputMetadata, ItemStack[] input) {
      this.output = output;
      this.outputMetadata = outputMetadata;
      this.input = input;
   }

   public boolean matchesRecipe(PistonPackingRecipe recipe) {
      return this.output.blockID == recipe.output.blockID && this.outputMetadata == recipe.outputMetadata ? this.input.equals(recipe.input) : false;
   }

   public boolean matchesInputs(ItemStack[] inputToMatch) {
      if (this.input.length == inputToMatch.length) {
         for (int i = 0; i < this.input.length; i++) {
            if (this.input[i].getItem().itemID == inputToMatch[i].getItem().itemID
               && this.input[i].getItemDamage() != inputToMatch[i].getItemDamage()
               && this.input[i].getItemDamage() != 32767) {
               return false;
            }
         }

         return true;
      } else {
         return false;
      }
   }

   public Block getOutput() {
      return this.output;
   }

   public int getOutputMetadata() {
      return this.outputMetadata;
   }

   public ItemStack[] getInput() {
      return this.input;
   }
}
