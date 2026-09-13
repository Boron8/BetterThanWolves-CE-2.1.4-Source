package btw.item.items;

import btw.block.BTWBlocks;
import net.minecraft.src.Block;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EnumToolMaterial;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;

public class AxeItem extends ToolItem {
   public AxeItem(int iItemID, EnumToolMaterial material) {
      super(iItemID, 3, material);
   }

   @Override
   public float getStrVsBlock(ItemStack toolItemStack, World world, Block block, int i, int j, int k) {
      int iToolLevel = this.toolMaterial.getHarvestLevel();
      int iBlockToolLevel = block.getEfficientToolLevel(world, i, j, k);
      if (iBlockToolLevel > iToolLevel) {
         return 1.0F;
      } else {
         return block.getIsProblemToRemove(toolItemStack, world, i, j, k) ? 1.0F : super.getStrVsBlock(toolItemStack, world, block, i, j, k);
      }
   }

   @Override
   public boolean canHarvestBlock(ItemStack stack, World world, Block block, int i, int j, int k) {
      int iToolLevel = this.toolMaterial.getHarvestLevel();
      int iBlockToolLevel = block.getHarvestToolLevel(world, i, j, k);
      if (iBlockToolLevel > iToolLevel) {
         return false;
      } else if (block.getIsProblemToRemove(stack, world, i, j, k)) {
         return false;
      } else {
         return this.isToolTypeEfficientVsBlockType(block) ? true : super.canHarvestBlock(stack, world, block, i, j, k);
      }
   }

   @Override
   public boolean isEfficientVsBlock(ItemStack stack, World world, Block block, int i, int j, int k) {
      int iToolLevel = this.toolMaterial.getHarvestLevel();
      int iBlockToolLevel = block.getEfficientToolLevel(world, i, j, k);
      if (iBlockToolLevel > iToolLevel) {
         return false;
      } else {
         return block.getIsProblemToRemove(stack, world, i, j, k) ? false : super.isEfficientVsBlock(stack, world, block, i, j, k);
      }
   }

   @Override
   public boolean isConsumedInCrafting() {
      return this.toolMaterial.getHarvestLevel() <= 2;
   }

   @Override
   public boolean isDamagedInCrafting() {
      return this.toolMaterial.getHarvestLevel() <= 2;
   }

   @Override
   public void onUsedInCrafting(EntityPlayer player, ItemStack outputStack) {
      playChopSoundOnPlayer(player);
   }

   @Override
   public void onBrokenInCrafting(EntityPlayer player) {
      playBreakSoundOnPlayer(player);
   }

   @Override
   public boolean onBlockDestroyed(ItemStack stack, World world, int iBlockID, int i, int j, int k, EntityLiving destroyingEntityLiving) {
      if (!this.getIsDamagedByVegetation()) {
         Block block = Block.blocksList[iBlockID];
         if (block != null && block.blockMaterial.getAxesTreatAsVegetation()) {
            return true;
         }
      }

      return super.onBlockDestroyed(stack, world, iBlockID, i, j, k, destroyingEntityLiving);
   }

   @Override
   public float getExhaustionOnUsedToHarvestBlock(int iBlockID, World world, int i, int j, int k, int iBlockMetadata) {
      if (!this.getConsumesHungerOnZeroHardnessVegetation()) {
         Block block = Block.blocksList[iBlockID];
         if (block != null && block.getBlockHardness(world, i, j, k) == 0.0 && block.blockMaterial.getAxesTreatAsVegetation()) {
            return 0.0F;
         }
      }

      return super.getExhaustionOnUsedToHarvestBlock(iBlockID, world, i, j, k, iBlockMetadata);
   }

   @Override
   public boolean isToolTypeEfficientVsBlockType(Block block) {
      return block.blockMaterial.getAxesEfficientOn() || block.areAxesEffectiveOn();
   }

   @Override
   public boolean canToolStickInBlock(ItemStack stack, Block block, World world, int i, int j, int k) {
      return block.blockMaterial != BTWBlocks.logMaterial && block.blockMaterial != BTWBlocks.plankMaterial
         ? super.canToolStickInBlock(stack, block, world, i, j, k)
         : true;
   }

   @Override
   public void playPlacementSound(ItemStack stack, Block blockStuckIn, World world, int i, int j, int k) {
      world.playSoundEffect(i + 0.5F, j + 0.5F, k + 0.5F, "mob.zombie.woodbreak", 0.25F, 1.25F + world.rand.nextFloat() * 0.25F);
   }

   public boolean getConsumesHungerOnZeroHardnessVegetation() {
      return this.toolMaterial.getHarvestLevel() <= 1;
   }

   public boolean getIsDamagedByVegetation() {
      return this.toolMaterial.getHarvestLevel() <= 2;
   }

   public static void playChopSoundOnPlayer(EntityPlayer player) {
      if (player.timesCraftedThisTick == 0) {
         player.playSound("mob.zombie.wood", 0.5F, 2.5F);
      }
   }

   public static void playBreakSoundOnPlayer(EntityPlayer player) {
      player.playSound("random.break", 0.8F, 0.8F + player.worldObj.rand.nextFloat() * 0.4F);
   }
}
