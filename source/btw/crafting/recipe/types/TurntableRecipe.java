package btw.crafting.recipe.types;

import java.util.Arrays;
import net.minecraft.src.Block;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;

public class TurntableRecipe {
   private final Block output;
   private final int outputMetadata;
   private final ItemStack[] itemsEjected;
   private final Block block;
   private final int[] metadatas;
   private final int rotationsToCraft;
   private TurntableRecipe.TurntableEffect effect;
   private TurntableRecipe.TurntableEffect completionEffect;

   public TurntableRecipe(Block output, int outputMetadata, ItemStack[] itemsEjected, Block block, int[] metadatas, int rotationsToCraft) {
      this.output = output;
      this.outputMetadata = outputMetadata;
      this.itemsEjected = itemsEjected;
      this.block = block;
      this.metadatas = metadatas;
      this.rotationsToCraft = rotationsToCraft;
   }

   public boolean ignoreMetadata() {
      return this.metadatas.length == 1 && this.metadatas[0] == 32767;
   }

   public boolean matchesRecipe(TurntableRecipe recipe) {
      return this.block == recipe.block && (Arrays.equals(this.metadatas, recipe.metadatas) || this.ignoreMetadata() && this.ignoreMetadata())
         ? this.output.blockID == recipe.output.blockID
         : false;
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

   public int[] getInputMetadata() {
      return this.metadatas;
   }

   public Block getOutputBlock() {
      return this.output;
   }

   public int getOutputMetadata() {
      return this.outputMetadata;
   }

   public ItemStack[] getItemsEjected() {
      return this.itemsEjected;
   }

   public int getRotationsToCraft() {
      return this.rotationsToCraft;
   }

   public TurntableRecipe setEffect(TurntableRecipe.TurntableEffect effect) {
      this.effect = effect;
      return this;
   }

   public TurntableRecipe setCompletionEffect(TurntableRecipe.TurntableEffect effect) {
      this.completionEffect = effect;
      return this;
   }

   public void playEffect(World world, int x, int y, int z) {
      if (this.effect != null) {
         this.effect.playEffect(world, x, y, z);
      }
   }

   public void playCompletionEffect(World world, int x, int y, int z) {
      if (this.completionEffect != null) {
         this.completionEffect.playEffect(world, x, y, z);
      } else if (this.effect != null) {
         this.effect.playEffect(world, x, y, z);
      }
   }

   public interface TurntableEffect {
      void playEffect(World var1, int var2, int var3, int var4);
   }
}
