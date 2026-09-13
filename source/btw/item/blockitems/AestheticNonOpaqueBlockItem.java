package btw.item.blockitems;

import btw.block.BTWBlocks;
import btw.block.blocks.AestheticNonOpaqueBlock;
import btw.world.util.BlockPos;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.Block;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ItemBlock;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;

public class AestheticNonOpaqueBlockItem extends ItemBlock {
   public AestheticNonOpaqueBlockItem(int iItemID) {
      super(iItemID);
      this.e(0);
      this.a(true);
      this.b("fcBlockAestheticNonOpaque");
   }

   @Override
   public int getMetadata(int iItemDamage) {
      return iItemDamage;
   }

   @Override
   public String getUnlocalizedName(ItemStack itemstack) {
      switch (itemstack.getItemDamage()) {
         case 0:
            return super.getUnlocalizedName() + "." + "urn";
         case 1:
            return super.getUnlocalizedName() + "." + "column";
         case 2:
         case 3:
            return super.getUnlocalizedName() + "." + "pedestal";
         case 4:
            return super.getUnlocalizedName() + "." + "table";
         case 5:
            return super.getUnlocalizedName() + "." + "wickerslab";
         case 6:
         case 7:
         case 8:
         case 9:
         case 11:
         default:
            return super.getUnlocalizedName();
         case 10:
            return super.getUnlocalizedName() + "." + "whitecobbleslab";
         case 12:
            return super.getUnlocalizedName() + "." + "lightningrod";
      }
   }

   @Override
   public boolean onItemUse(
      ItemStack itemStack, EntityPlayer player, World world, int i, int j, int k, int iFacing, float fClickX, float fClickY, float fClickZ
   ) {
      if (itemStack.getItemDamage() == 12) {
         BlockPos targetPos = new BlockPos(i, j, k);
         targetPos.addFacingAsOffset(iFacing);
         return AestheticNonOpaqueBlock.canLightningRodStay(world, targetPos.x, targetPos.y, targetPos.z)
            ? super.onItemUse(itemStack, player, world, i, j, k, iFacing, fClickX, fClickY, fClickZ)
            : false;
      } else if (itemStack.getItemDamage() != 5 && itemStack.getItemDamage() != 10) {
         return super.onItemUse(itemStack, player, world, i, j, k, iFacing, fClickX, fClickY, fClickZ);
      } else if (itemStack.stackSize == 0) {
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

   @Override
   public float getBuoyancy(int iItemDamage) {
      switch (iItemDamage) {
         case 0:
         case 4:
         case 5:
         case 6:
         case 7:
         case 8:
         case 9:
            return 1.0F;
         case 1:
         case 2:
         case 3:
         default:
            return super.getBuoyancy(iItemDamage);
      }
   }

   public boolean canCombineWithBlock(World world, int i, int j, int k, int iItemDamage) {
      int iBlockID = world.getBlockId(i, j, k);
      if (iItemDamage == 5) {
         if (iBlockID == BTWBlocks.aestheticNonOpaque.blockID) {
            int iBlockMetadata = world.getBlockMetadata(i, j, k);
            if (iBlockMetadata == 5 || iBlockMetadata == 9) {
               return true;
            }
         }
      } else if (iItemDamage == 10 && iBlockID == BTWBlocks.aestheticNonOpaque.blockID) {
         int iBlockMetadata = world.getBlockMetadata(i, j, k);
         if (iBlockMetadata == 10 || iBlockMetadata == 11) {
            return true;
         }
      }

      return false;
   }

   public boolean convertToFullBlock(World world, int i, int j, int k) {
      int iBlockID = world.getBlockId(i, j, k);
      if (iBlockID == BTWBlocks.aestheticNonOpaque.blockID) {
         int iBlockMetadata = world.getBlockMetadata(i, j, k);
         if (iBlockMetadata == 5 || iBlockMetadata == 9) {
            int iTargetBlockID = BTWBlocks.aestheticOpaque.blockID;
            int iTargetMetadata = 0;
            return world.setBlockAndMetadataWithNotify(i, j, k, iTargetBlockID, iTargetMetadata);
         }

         if (iBlockMetadata == 10 || iBlockMetadata == 11) {
            int iTargetBlockID = BTWBlocks.aestheticOpaque.blockID;
            int iTargetMetadata = 10;
            return world.setBlockAndMetadataWithNotify(i, j, k, iTargetBlockID, iTargetMetadata);
         }
      }

      return false;
   }

   public boolean isSlabUpsideDown(int iBlockID, int iMetadata) {
      return iBlockID == BTWBlocks.aestheticNonOpaque.blockID && (iMetadata == 9 || iMetadata == 11);
   }

   @Environment(EnvType.CLIENT)
   public boolean attemptToCombineWithBlock(ItemStack itemStack, EntityPlayer player, World world, int i, int j, int k, int iFacing, boolean bFacingTest) {
      if (this.canCombineWithBlock(world, i, j, k, itemStack.getItemDamage())) {
         int iTargetBlockID = world.getBlockId(i, j, k);
         Block targetBlock = Block.blocksList[iTargetBlockID];
         if (targetBlock != null) {
            int iTargetMetadata = world.getBlockMetadata(i, j, k);
            boolean bIsTargetUpsideDown = this.isSlabUpsideDown(iTargetBlockID, iTargetMetadata);
            if (!bFacingTest || iFacing == 1 && !bIsTargetUpsideDown || iFacing == 0 && bIsTargetUpsideDown) {
               if (world.checkNoEntityCollision(Block.getFullBlockBoundingBoxFromPool(world, i, j, k)) && this.convertToFullBlock(world, i, j, k)) {
                  world.playSoundEffect(
                     i + 0.5F,
                     j + 0.5F,
                     k + 0.5F,
                     targetBlock.getStepSound(world, i, j, k).getPlaceSound(),
                     (targetBlock.getStepSound(world, i, j, k).getPlaceVolume() + 1.0F) / 2.0F,
                     targetBlock.getStepSound(world, i, j, k).getPlacePitch() * 0.8F
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

   @Override
   public boolean canPlaceItemBlockOnSide(World world, int i, int j, int k, int iFacing, EntityPlayer player, ItemStack itemStack) {
      BlockPos targetPos = new BlockPos(i, j, k);
      if (this.canCombineWithBlock(world, targetPos.x, targetPos.y, targetPos.z, itemStack.getItemDamage())) {
         int iTargetBlockID = world.getBlockId(targetPos.x, targetPos.y, targetPos.z);
         int iTargetMetadata = world.getBlockMetadata(targetPos.x, targetPos.y, targetPos.z);
         boolean bIsUpsideDown = this.isSlabUpsideDown(iTargetBlockID, iTargetMetadata);
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
