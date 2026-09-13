package btw.item.items;

import btw.world.util.BlockPos;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.StepSound;
import net.minecraft.src.World;

public class PlaceAsBlockItem extends Item {
   protected int blockID;
   protected int blockMetadata = 0;
   protected boolean requireNoEntitiesInTargetBlock = false;

   public PlaceAsBlockItem(int iItemID, int iBlockID) {
      super(iItemID);
      this.blockID = iBlockID;
   }

   public PlaceAsBlockItem(int iItemID, int iBlockID, int iBlockMetadata) {
      this(iItemID, iBlockID);
      this.blockMetadata = iBlockMetadata;
   }

   public PlaceAsBlockItem(int iItemID, int iBlockID, int iBlockMetadata, String sItemName) {
      this(iItemID, iBlockID, iBlockMetadata);
      this.b(sItemName);
   }

   protected PlaceAsBlockItem(int iItemID) {
      super(iItemID);
      this.blockID = iItemID + 256;
      this.blockMetadata = 0;
   }

   @Override
   public boolean onItemUse(
      ItemStack itemStack, EntityPlayer player, World world, int i, int j, int k, int iFacing, float fClickX, float fClickY, float fClickZ
   ) {
      int iNewBlockID = this.getBlockIDToPlace(itemStack.getItemDamage(), iFacing, fClickX, fClickY, fClickZ);
      if (itemStack.stackSize != 0
         && (player == null || player.canPlayerEdit(i, j, k, iFacing, itemStack))
         && (j != 255 || !Block.blocksList[iNewBlockID].blockMaterial.isSolid())) {
         BlockPos targetPos = new BlockPos(i, j, k);
         int iOldBlockID = world.getBlockId(i, j, k);
         Block oldBlock = Block.blocksList[iOldBlockID];
         if (oldBlock != null) {
            if (oldBlock.isGroundCover()) {
               iFacing = 1;
            } else if (!oldBlock.blockMaterial.isReplaceable()) {
               targetPos.addFacingAsOffset(iFacing);
            }
         }

         if ((!this.requireNoEntitiesInTargetBlock || this.isTargetFreeOfObstructingEntities(world, targetPos.x, targetPos.y, targetPos.z))
            && world.canPlaceEntityOnSide(iNewBlockID, targetPos.x, targetPos.y, targetPos.z, false, iFacing, player, itemStack)) {
            Block newBlock = Block.blocksList[iNewBlockID];
            int iNewMetadata = this.getMetadata(itemStack.getItemDamage());
            iNewMetadata = newBlock.onBlockPlaced(world, targetPos.x, targetPos.y, targetPos.z, iFacing, fClickX, fClickY, fClickZ, iNewMetadata);
            iNewMetadata = newBlock.preBlockPlacedBy(world, targetPos.x, targetPos.y, targetPos.z, iNewMetadata, player);
            if (world.setBlockAndMetadataWithNotify(targetPos.x, targetPos.y, targetPos.z, iNewBlockID, iNewMetadata)) {
               if (world.getBlockId(targetPos.x, targetPos.y, targetPos.z) == iNewBlockID) {
                  newBlock.onBlockPlacedBy(world, targetPos.x, targetPos.y, targetPos.z, player, itemStack);
                  newBlock.onPostBlockPlaced(world, targetPos.x, targetPos.y, targetPos.z, iNewMetadata);
                  world.notifyNearbyAnimalsOfPlayerBlockAddOrRemove(player, newBlock, targetPos.x, targetPos.y, targetPos.z);
               }

               this.playPlaceSound(world, targetPos.x, targetPos.y, targetPos.z, newBlock);
               itemStack.stackSize--;
            }

            return true;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   @Override
   public int getMetadata(int iItemDamage) {
      return this.blockMetadata;
   }

   @Override
   public boolean canItemBeUsedByPlayer(World world, int i, int j, int k, int iFacing, EntityPlayer player, ItemStack stack) {
      return this.canPlaceItemBlockOnSide(world, i, j, k, iFacing, player, stack);
   }

   @Override
   public boolean onItemUsedByBlockDispenser(ItemStack stack, World world, int i, int j, int k, int iFacing) {
      BlockPos targetPos = new BlockPos(i, j, k, iFacing);
      int iTargetDirection = this.getTargetFacingPlacedByBlockDispenser(iFacing);
      int iBlockID = this.getBlockIDToPlace(stack.getItemDamage(), iTargetDirection, 0.5F, 0.25F, 0.5F);
      Block newBlock = Block.blocksList[iBlockID];
      if (newBlock != null && world.canPlaceEntityOnSide(iBlockID, targetPos.x, targetPos.y, targetPos.z, true, iTargetDirection, null, stack)) {
         int iBlockMetadata = this.getMetadata(stack.getItemDamage());
         iBlockMetadata = newBlock.onBlockPlaced(world, targetPos.x, targetPos.y, targetPos.z, iTargetDirection, 0.5F, 0.25F, 0.5F, iBlockMetadata);
         world.setBlockAndMetadataWithNotify(targetPos.x, targetPos.y, targetPos.z, iBlockID, iBlockMetadata);
         newBlock.onPostBlockPlaced(world, targetPos.x, targetPos.y, targetPos.z, iBlockMetadata);
         world.playAuxSFX(2236, i, j, k, iBlockID);
         return true;
      } else {
         return false;
      }
   }

   public int g() {
      return this.blockID;
   }

   public boolean canPlaceItemBlockOnSide(World world, int i, int j, int k, int iFacing, EntityPlayer player, ItemStack stack) {
      int iTargetBlockID = world.getBlockId(i, j, k);
      Block iTargetBlock = Block.blocksList[iTargetBlockID];
      BlockPos targetPos = new BlockPos(i, j, k);
      if (iTargetBlock != null) {
         if (iTargetBlock.isGroundCover()) {
            iFacing = 1;
         } else if (!iTargetBlock.blockMaterial.isReplaceable()) {
            targetPos.addFacingAsOffset(iFacing);
         }
      }

      int iNewBlockID = this.getBlockIDToPlace(stack.getItemDamage(), iFacing, 0.5F, 0.5F, 0.5F);
      return world.canPlaceEntityOnSide(iNewBlockID, targetPos.x, targetPos.y, targetPos.z, false, iFacing, (Entity)null, stack);
   }

   public PlaceAsBlockItem setAssociatedBlockID(int iBlockID) {
      this.blockID = iBlockID;
      return this;
   }

   public int getBlockIDToPlace(int iItemDamage, int iFacing, float fClickX, float fClickY, float fClickZ) {
      return this.g();
   }

   protected boolean isTargetFreeOfObstructingEntities(World world, int i, int j, int k) {
      AxisAlignedBB blockBounds = AxisAlignedBB.getAABBPool().getAABB(i, j, k, i + 1, j + 1, k + 1);
      return world.checkNoEntityCollision(blockBounds);
   }

   protected void playPlaceSound(World world, int i, int j, int k, Block block) {
      StepSound stepSound = block.getStepSound(world, i, j, k);
      world.playSoundEffect(i + 0.5, j + 0.5, k + 0.5, stepSound.getPlaceSound(), (stepSound.getPlaceVolume() + 1.0F) / 2.0F, stepSound.getPlacePitch() * 0.8F);
   }

   public int getTargetFacingPlacedByBlockDispenser(int iDispenserFacing) {
      return Block.getOppositeFacing(iDispenserFacing);
   }
}
