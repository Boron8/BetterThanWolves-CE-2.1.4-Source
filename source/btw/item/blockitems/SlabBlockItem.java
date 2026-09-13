package btw.item.blockitems;

import btw.block.blocks.SlabBlock;
import btw.world.util.BlockPos;
import net.minecraft.src.Block;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ItemBlock;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;

public class SlabBlockItem extends ItemBlock {
   public SlabBlockItem(int iItemID) {
      super(iItemID);
      this.a(true);
   }

   @Override
   public boolean onItemUse(
      ItemStack itemStack, EntityPlayer player, World world, int i, int j, int k, int iFacing, float fClickX, float fClickY, float fClickZ
   ) {
      if (itemStack.stackSize == 0) {
         return false;
      } else if (!player.canPlayerEdit(i, j, k, iFacing, itemStack)) {
         return false;
      } else if (this.attemptToCombineWithBlock(itemStack, player, world, i, j, k, iFacing, true)) {
         return true;
      } else {
         BlockPos targetPos = new BlockPos(i, j, k);
         targetPos.addFacingAsOffset(iFacing);
         return this.attemptToCombineWithBlock(itemStack, player, world, targetPos.x, targetPos.y, targetPos.z, iFacing, false)
            ? true
            : super.onItemUse(itemStack, player, world, i, j, k, iFacing, fClickX, fClickY, fClickZ);
      }
   }

   public boolean attemptToCombineWithBlock(ItemStack itemStack, EntityPlayer player, World world, int i, int j, int k, int iFacing, boolean bFacingTest) {
      if (this.canCombineWithBlock(world, i, j, k, itemStack.getItemDamage())) {
         int iTargetBlockID = world.getBlockId(i, j, k);
         Block targetBlock = Block.blocksList[iTargetBlockID];
         if (targetBlock != null && targetBlock instanceof SlabBlock) {
            SlabBlock slabBlock = (SlabBlock)targetBlock;
            boolean bIsTargetUpsideDown = slabBlock.getIsUpsideDown(world, i, j, k);
            if (!bFacingTest || iFacing == 1 && !bIsTargetUpsideDown || iFacing == 0 && bIsTargetUpsideDown) {
               if (world.checkNoEntityCollision(Block.getFullBlockBoundingBoxFromPool(world, i, j, k)) && this.convertToFullBlock(world, i, j, k)) {
                  world.playSoundEffect(
                     i + 0.5F,
                     j + 0.5F,
                     k + 0.5F,
                     slabBlock.getStepSound(world, i, j, k).getPlaceSound(),
                     (slabBlock.getStepSound(world, i, j, k).getPlaceVolume() + 1.0F) / 2.0F,
                     slabBlock.getStepSound(world, i, j, k).getPlacePitch() * 0.8F
                  );
                  itemStack.stackSize--;
                  Block newBlock = Block.blocksList[world.getBlockId(i, j, k)];
                  if (newBlock != null) {
                     world.notifyNearbyAnimalsOfPlayerBlockAddOrRemove(player, newBlock, i, j, k);
                  }
               }

               return true;
            }
         }
      }

      return false;
   }

   public boolean canCombineWithBlock(World world, int i, int j, int k, int iItemDamage) {
      int iBlockID = world.getBlockId(i, j, k);
      if (iBlockID == this.g()) {
         Block block = Block.blocksList[iBlockID];
         if (block instanceof SlabBlock) {
            int iMetadata = ((SlabBlock)block).setIsUpsideDown(world.getBlockMetadata(i, j, k), false);
            if (iMetadata == iItemDamage) {
               return true;
            }
         }
      }

      return false;
   }

   public boolean convertToFullBlock(World world, int i, int j, int k) {
      int iBlockID = world.getBlockId(i, j, k);
      Block block = Block.blocksList[iBlockID];
      return block instanceof SlabBlock ? ((SlabBlock)block).convertToFullBlock(world, i, j, k) : false;
   }

   @Override
   public boolean canPlaceItemBlockOnSide(World world, int i, int j, int k, int iFacing, EntityPlayer player, ItemStack itemStack) {
      BlockPos targetPos = new BlockPos(i, j, k);
      if (this.canCombineWithBlock(world, targetPos.x, targetPos.y, targetPos.z, itemStack.getItemDamage())) {
         int iTargetBlockID = world.getBlockId(targetPos.x, targetPos.y, targetPos.z);
         SlabBlock slab = (SlabBlock)Block.blocksList[iTargetBlockID];
         boolean bIsUpsideDown = slab.getIsUpsideDown(world, targetPos.x, targetPos.y, targetPos.z);
         if (iFacing == 1 && !bIsUpsideDown || iFacing == 0 && bIsUpsideDown) {
            return true;
         }
      }

      targetPos.addFacingAsOffset(iFacing);
      return this.canCombineWithBlock(world, targetPos.x, targetPos.y, targetPos.z, itemStack.getItemDamage())
         ? true
         : super.canPlaceItemBlockOnSide(world, i, j, k, iFacing, player, itemStack);
   }
}
