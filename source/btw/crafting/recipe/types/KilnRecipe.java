package btw.crafting.recipe.types;

import java.util.Arrays;
import net.minecraft.src.Block;
import net.minecraft.src.ItemStack;

public class KilnRecipe {
   private final ItemStack[] output;
   private final Block block;
   private final int[] metadatas;
   private final byte cookTimeMultiplier;

   public KilnRecipe(ItemStack[] output, Block block, int[] metadatas, byte cookTimeMultiplier) {
      this.output = output;
      this.block = block;
      this.metadatas = metadatas;
      this.cookTimeMultiplier = cookTimeMultiplier;
   }

   public boolean ignoreMetadata() {
      return this.metadatas.length == 1 && this.metadatas[0] == 32767;
   }

   public boolean matchesRecipe(KilnRecipe recipe) {
      return this.block != recipe.block || !Arrays.equals(this.metadatas, recipe.metadatas) && (!this.ignoreMetadata() || !recipe.ignoreMetadata())
         ? false
         : this.output.equals(recipe.output);
   }

   public boolean matchesInputs(Block block, int metadata) {
      boolean containsGivenMetadata = false;

      for (int i : this.metadatas) {
         if (i == metadata) {
            containsGivenMetadata = true;
            break;
         }
      }

      return this.block.blockID == block.blockID && (containsGivenMetadata || this.ignoreMetadata());
   }

   public boolean matchesInputs(Block block, int[] metadatas) {
      return this.block.blockID == block.blockID && (Arrays.equals(this.metadatas, metadatas) || this.ignoreMetadata());
   }

   public Block getInputblock() {
      return this.block;
   }

   public ItemStack[] getOutput() {
      return this.output;
   }

   public int[] getInputMetadata() {
      return this.metadatas;
   }

   public byte getCookTimeMultiplier() {
      return this.cookTimeMultiplier;
   }
}
