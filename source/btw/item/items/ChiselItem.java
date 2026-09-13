package btw.item.items;

import btw.block.BTWBlocks;
import net.minecraft.src.Block;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EnumToolMaterial;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;

public class ChiselItem extends ToolItem {
   protected ChiselItem(int iItemID, EnumToolMaterial toolMaterial, int iNumUses) {
      super(iItemID, 1, toolMaterial);
      this.e(iNumUses);
      this.damageVsEntity = 1;
   }

   @Override
   public boolean hitEntity(ItemStack stack, EntityLiving defendingEntity, EntityLiving attackingEntity) {
      return false;
   }

   @Override
   public boolean canHarvestBlock(ItemStack stack, World world, Block block, int i, int j, int k) {
      return block.canChiselsHarvest() ? this.toolMaterial.getHarvestLevel() >= block.getHarvestToolLevel(world, i, j, k) : false;
   }

   @Override
   public boolean isEfficientVsBlock(ItemStack stack, World world, Block block, int i, int j, int k) {
      if (block.getIsProblemToRemove(stack, world, i, j, k)) {
         return false;
      } else {
         int iToolLevel = this.toolMaterial.getHarvestLevel();
         int iBlockToolLevel = block.getEfficientToolLevel(world, i, j, k);
         return iBlockToolLevel > iToolLevel ? false : block.arechiselseffectiveon(world, i, j, k);
      }
   }

   @Override
   public int getItemEnchantability() {
      return 0;
   }

   @Override
   public boolean isToolTypeEfficientVsBlockType(Block block) {
      return block.arechiselseffectiveon();
   }

   @Override
   public boolean getCanBePlacedAsBlock() {
      return false;
   }

   @Override
   public void playPlacementSound(ItemStack stack, Block blockStuckIn, World world, int i, int j, int k) {
      if (((ToolItem)h).isToolTypeEfficientVsBlockType(blockStuckIn)) {
         world.playSoundEffect(i + 0.5F, j + 0.5F, k + 0.5F, "random.anvil_land", 0.5F, world.rand.nextFloat() * 0.25F + 1.75F);
      } else if (!((ToolItem)ChiselItem.i).isToolTypeEfficientVsBlockType(blockStuckIn)
         && blockStuckIn.blockMaterial != BTWBlocks.logMaterial
         && blockStuckIn.blockMaterial != BTWBlocks.plankMaterial) {
         super.playPlacementSound(stack, blockStuckIn, world, i, j, k);
      } else {
         world.playSoundEffect(i + 0.5F, j + 0.5F, k + 0.5F, "mob.zombie.woodbreak", 0.25F, 1.25F + world.rand.nextFloat() * 0.25F);
      }
   }

   @Override
   public float getVisualVerticalOffsetAsBlock() {
      return 0.45F;
   }

   @Override
   public float getBlockBoundingBoxHeight() {
      return 0.3F;
   }

   @Override
   public float getBlockBoundingBoxWidth() {
      return 0.5F;
   }
}
