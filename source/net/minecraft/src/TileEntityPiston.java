package net.minecraft.src;

import btw.block.BTWBlocks;
import btw.crafting.manager.PistonPackingCraftingManager;
import btw.crafting.recipe.types.PistonPackingRecipe;
import btw.item.util.ItemUtils;
import btw.world.util.BlockPos;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

public class TileEntityPiston extends TileEntity {
   private int storedBlockID;
   private int storedMetadata;
   public NBTTagCompound storedTileEntityData;
   public TileEntity cachedTileEntity;
   private int storedOrientation;
   private boolean extending;
   private boolean shouldHeadBeRendered;
   private float progress;
   private float lastProgress;
   private List pushedObjects = new ArrayList();
   private boolean shoveledBlock = false;

   public TileEntityPiston(int iBlockID, int iMetadata, int iFacing, boolean bExtending, boolean bShouldHeadBeRendered, boolean bShoveledBlock) {
      this(iBlockID, iMetadata, iFacing, bExtending, bShouldHeadBeRendered);
      this.shoveledBlock = true;
   }

   public TileEntityPiston() {
   }

   public TileEntityPiston(int par1, int par2, int par3, boolean par4, boolean par5) {
      this.storedBlockID = par1;
      this.storedMetadata = par2;
      this.storedOrientation = par3;
      this.extending = par4;
      this.shouldHeadBeRendered = par5;
   }

   public void storeTileEntity(NBTTagCompound tileEntityData) {
      this.storedTileEntityData = tileEntityData;
      if (this.storedTileEntityData != null) {
         this.storeCachedTileEntity();
      }
   }

   public void storeCachedTileEntity() {
      this.cachedTileEntity = TileEntity.createAndLoadEntity(this.storedTileEntityData);
      this.cachedTileEntity.setWorldObj(this.worldObj);
      this.cachedTileEntity.blockMetadata = this.storedMetadata;
      this.cachedTileEntity.blockType = Block.blocksList[this.getStoredBlockID()];
   }

   public int getStoredBlockID() {
      return this.storedBlockID;
   }

   @Override
   public int getBlockMetadata() {
      return this.storedMetadata;
   }

   public boolean isExtending() {
      return this.extending;
   }

   public int getPistonOrientation() {
      return this.storedOrientation;
   }

   @Environment(EnvType.CLIENT)
   public boolean shouldRenderHead() {
      return this.shouldHeadBeRendered;
   }

   public float getProgress(float par1) {
      if (par1 > 1.0F) {
         par1 = 1.0F;
      }

      return this.lastProgress + (this.progress - this.lastProgress) * par1;
   }

   @Environment(EnvType.CLIENT)
   public float getOffsetX(float par1) {
      return this.extending
         ? (this.getProgress(par1) - 1.0F) * Facing.offsetsXForSide[this.storedOrientation]
         : (1.0F - this.getProgress(par1)) * Facing.offsetsXForSide[this.storedOrientation];
   }

   @Environment(EnvType.CLIENT)
   public float getOffsetY(float par1) {
      return this.extending
         ? (this.getProgress(par1) - 1.0F) * Facing.offsetsYForSide[this.storedOrientation]
         : (1.0F - this.getProgress(par1)) * Facing.offsetsYForSide[this.storedOrientation];
   }

   @Environment(EnvType.CLIENT)
   public float getOffsetZ(float par1) {
      return this.extending
         ? (this.getProgress(par1) - 1.0F) * Facing.offsetsZForSide[this.storedOrientation]
         : (1.0F - this.getProgress(par1)) * Facing.offsetsZForSide[this.storedOrientation];
   }

   private void updatePushedObjects(float par1, float par2) {
      if (this.extending) {
         par1 = 1.0F - par1;
      } else {
         par1--;
      }

      AxisAlignedBB var3 = Block.pistonMoving
         .getAxisAlignedBB(this.worldObj, this.xCoord, this.yCoord, this.zCoord, this.storedBlockID, par1, this.storedOrientation);
      if (var3 != null) {
         List var4 = this.worldObj.getEntitiesWithinAABBExcludingEntity((Entity)null, var3);
         if (!var4.isEmpty()) {
            this.pushedObjects.addAll(var4);

            for (Entity var6 : this.pushedObjects) {
               var6.moveEntity(
                  par2 * Facing.offsetsXForSide[this.storedOrientation],
                  par2 * Facing.offsetsYForSide[this.storedOrientation],
                  par2 * Facing.offsetsZForSide[this.storedOrientation]
               );
            }

            this.pushedObjects.clear();
         }
      }
   }

   public void clearPistonTileEntity() {
      if (this.lastProgress < 1.0F && this.worldObj != null) {
         this.lastProgress = this.progress = 1.0F;
         this.worldObj.removeBlockTileEntity(this.xCoord, this.yCoord, this.zCoord);
         this.w_();
         if (this.worldObj.getBlockId(this.xCoord, this.yCoord, this.zCoord) == Block.pistonMoving.blockID) {
            if (this.destroyAndDropIfShoveled()) {
               return;
            }

            this.preBlockPlaced();
            this.worldObj.setBlock(this.xCoord, this.yCoord, this.zCoord, this.storedBlockID, this.storedMetadata, 3);
            if (this.storedTileEntityData != null) {
               this.restoreBlockTileEntity();
            }

            this.worldObj.notifyBlockOfNeighborChange(this.xCoord, this.yCoord, this.zCoord, this.storedBlockID);
         }
      }
   }

   public void restoreBlockTileEntity() {
      TileEntity tileEntity = TileEntity.createAndLoadEntity(this.storedTileEntityData);
      tileEntity.xCoord = this.xCoord;
      tileEntity.yCoord = this.yCoord;
      tileEntity.zCoord = this.zCoord;
      this.cachedTileEntity = null;
      this.worldObj.setBlockTileEntity(this.xCoord, this.yCoord, this.zCoord, tileEntity);
   }

   @Override
   public void updateEntity() {
      this.lastProgress = this.progress;
      if (this.lastProgress >= 1.0F) {
         this.updatePushedObjects(1.0F, 0.25F);
         this.attemptToPackItems();
         this.worldObj.removeBlockTileEntity(this.xCoord, this.yCoord, this.zCoord);
         this.w_();
         if (this.worldObj.getBlockId(this.xCoord, this.yCoord, this.zCoord) == Block.pistonMoving.blockID) {
            if (this.destroyAndDropIfShoveled()) {
               return;
            }

            this.preBlockPlaced();
            if (this.storedBlockID == BTWBlocks.chest.blockID
               && !Block.blocksList[this.storedBlockID].canPlaceBlockAt(this.worldObj, this.xCoord, this.yCoord, this.zCoord)) {
               this.restoreStoredBlock();
               this.worldObj.destroyBlock(this.xCoord, this.yCoord, this.zCoord, true);
            } else {
               this.restoreStoredBlock();
            }
         }
      } else {
         this.progress += 0.5F;
         if (this.progress >= 1.0F) {
            this.progress = 1.0F;
         }

         if (this.extending) {
            this.updatePushedObjects(this.progress, this.progress - this.lastProgress + 0.0625F);
         }
      }
   }

   public void restoreStoredBlock() {
      this.worldObj.setBlock(this.xCoord, this.yCoord, this.zCoord, this.storedBlockID, this.storedMetadata, 3);
      if (this.storedTileEntityData != null) {
         this.restoreBlockTileEntity();
      }

      this.worldObj.notifyBlockOfNeighborChange(this.xCoord, this.yCoord, this.zCoord, this.storedBlockID);
   }

   @Override
   public void readFromNBT(NBTTagCompound par1NBTTagCompound) {
      super.readFromNBT(par1NBTTagCompound);
      this.storedBlockID = par1NBTTagCompound.getInteger("blockId");
      this.storedMetadata = par1NBTTagCompound.getInteger("blockData");
      this.storedOrientation = par1NBTTagCompound.getInteger("facing");
      this.lastProgress = this.progress = par1NBTTagCompound.getFloat("progress");
      this.extending = par1NBTTagCompound.getBoolean("extending");
      if (par1NBTTagCompound.hasKey("fcShovel")) {
         this.shoveledBlock = par1NBTTagCompound.getBoolean("fcShovel");
      }

      if (par1NBTTagCompound.hasKey("fcTileEntityData")) {
         this.storedTileEntityData = par1NBTTagCompound.getCompoundTag("fcTileEntityData");
      }

      if (par1NBTTagCompound.hasKey("fcCachedTileEntity")) {
         this.storedTileEntityData = par1NBTTagCompound.getCompoundTag("fcCachedTileEntity");
         if (this.storedTileEntityData != null) {
            this.storeCachedTileEntity();
         }
      }
   }

   @Override
   public void writeToNBT(NBTTagCompound par1NBTTagCompound) {
      super.writeToNBT(par1NBTTagCompound);
      par1NBTTagCompound.setInteger("blockId", this.storedBlockID);
      par1NBTTagCompound.setInteger("blockData", this.storedMetadata);
      par1NBTTagCompound.setInteger("facing", this.storedOrientation);
      par1NBTTagCompound.setFloat("progress", this.lastProgress);
      par1NBTTagCompound.setBoolean("extending", this.extending);
      par1NBTTagCompound.setBoolean("fcShovel", this.shoveledBlock);
      if (this.storedTileEntityData != null) {
         par1NBTTagCompound.setCompoundTag("fcTileEntityData", this.storedTileEntityData);
      }
   }

   private void attemptToPackItems() {
      if (!this.worldObj.isRemote
         && this.isExtending()
         && (this.storedBlockID == Block.pistonExtension.blockID || Block.isNormalCube(this.storedBlockID) || this.storedBlockID == Block.glass.blockID)) {
         BlockPos pos = new BlockPos(this.xCoord, this.yCoord, this.zCoord, this.storedOrientation);
         if (this.isLocationSuitableForPacking(pos.x, pos.y, pos.z, Block.getOppositeFacing(this.storedOrientation))) {
            AxisAlignedBB targetBox = AxisAlignedBB.getAABBPool().getAABB(pos.x, pos.y, pos.z, pos.x + 1.0, pos.y + 1.0, pos.z + 1.0);
            List<EntityItem> itemsWithinBox = this.worldObj.getEntitiesWithinAABB(EntityItem.class, targetBox);
            if (!itemsWithinBox.isEmpty()) {
               PistonPackingRecipe recipe = PistonPackingCraftingManager.instance.getValidRecipeFromItemList(itemsWithinBox);
               if (recipe != null) {
                  for (ItemStack stack : recipe.getInput()) {
                     this.removeItemsOfTypeFromList(stack, stack.stackSize, itemsWithinBox);
                  }

                  this.createPackedBlockOfTypeAtLocation(recipe.getOutput().blockID, recipe.getOutputMetadata(), pos.x, pos.y, pos.z);
               }
            }
         }
      }
   }

   private boolean isLocationSuitableForPacking(int i, int j, int k, int iPistonDirection) {
      if (this.worldObj.isAirBlock(i, j, k)) {
         for (int iTempFacing = 0; iTempFacing < 6; iTempFacing++) {
            if (iTempFacing != iPistonDirection) {
               BlockPos tempPos = new BlockPos(i, j, k, iTempFacing);
               if (!this.isBlockSuitableForPackingToFacing(tempPos.x, tempPos.y, tempPos.z, Block.getOppositeFacing(iTempFacing))) {
                  return false;
               }
            }
         }

         return true;
      } else {
         return false;
      }
   }

   private boolean isBlockSuitableForPackingToFacing(int i, int j, int k, int iFacing) {
      Block block = Block.blocksList[this.worldObj.getBlockId(i, j, k)];
      return block != null ? block.canContainPistonPackingToFacing(this.worldObj, i, j, k, iFacing) : false;
   }

   private void createPackedBlockOfTypeAtLocation(int blockID, int metadata, int x, int y, int z) {
      this.worldObj.setBlockAndMetadataWithNotify(x, y, z, blockID, metadata);
      this.worldObj.playAuxSFX(2236, x, y, z, blockID);
   }

   private int countItemsOfTypeInList(ItemStack stack, List list) {
      Iterator itemIterator = list.iterator();
      int iCount = 0;

      while (itemIterator.hasNext()) {
         EntityItem tempItem = (EntityItem)itemIterator.next();
         if (!tempItem.isDead) {
            ItemStack tempStack = tempItem.getEntityItem();
            if (tempStack.itemID == stack.itemID) {
               iCount += tempStack.stackSize;
            }
         }
      }

      return iCount;
   }

   private void removeItemsOfTypeFromList(ItemStack stack, int iCount, List list) {
      for (EntityItem tempItem : list) {
         if (!tempItem.isDead) {
            ItemStack tempStack = tempItem.getEntityItem();
            if (tempStack.itemID == stack.itemID) {
               if (tempStack.stackSize > iCount) {
                  tempStack.stackSize -= iCount;
                  break;
               }

               iCount -= tempStack.stackSize;
               tempStack.stackSize = 0;
               tempItem.w();
               if (iCount <= 0) {
                  break;
               }
            }
         }
      }
   }

   private boolean destroyAndDropIfShoveled() {
      if (this.shoveledBlock) {
         Block tempBlock = Block.blocksList[this.storedBlockID];
         if (tempBlock != null && !this.worldObj.isRemote) {
            ItemStack tempStack = null;
            if (tempBlock.canSilkHarvest(this.storedMetadata)) {
               tempStack = tempBlock.createStackedBlock(this.storedMetadata);
            } else {
               int idDropped = tempBlock.idDropped(this.storedMetadata, this.worldObj.rand, 0);
               if (idDropped != 0) {
                  tempStack = new ItemStack(idDropped, tempBlock.quantityDropped(this.worldObj.rand), tempBlock.damageDropped(this.storedMetadata));
               }
            }

            if (tempStack != null) {
               this.ejectStackOnShoveled(tempStack);
            }
         }

         this.worldObj.setBlockToAir(this.xCoord, this.yCoord, this.zCoord);
         this.worldObj.notifyBlockOfNeighborChange(this.xCoord, this.yCoord, this.zCoord, this.storedBlockID);
         return true;
      } else {
         return false;
      }
   }

   private void ejectStackOnShoveled(ItemStack stack) {
      BlockPos sourcePos = new BlockPos(this.xCoord, this.yCoord, this.zCoord, Block.getOppositeFacing(this.storedOrientation));
      ItemUtils.ejectStackFromBlockTowardsFacing(this.worldObj, sourcePos.x, sourcePos.y, sourcePos.z, stack, this.storedOrientation);
   }

   private void preBlockPlaced() {
      Block tempBlock = Block.blocksList[this.storedBlockID];
      if (tempBlock != null && !this.worldObj.isRemote) {
         this.storedMetadata = tempBlock.onPreBlockPlacedByPiston(
            this.worldObj, this.xCoord, this.yCoord, this.zCoord, this.storedMetadata, this.getDirectionMoving()
         );
      }
   }

   private int getDirectionMoving() {
      return !this.extending ? Block.getOppositeFacing(this.storedOrientation) : this.storedOrientation;
   }
}
