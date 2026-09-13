package btw.block.blocks;

import btw.BTWMod;
import btw.block.BTWBlocks;
import btw.block.tileentity.dispenser.BlockDispenserTileEntity;
import btw.inventory.BTWContainers;
import btw.inventory.container.BlockDispenserContainer;
import btw.inventory.util.InventoryUtils;
import btw.util.MiscUtils;
import btw.world.util.BlockPos;
import java.util.List;
import java.util.Random;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.BlockContainer;
import net.minecraft.src.Container;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityItem;
import net.minecraft.src.EntityList;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EntityPlayerMP;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.IInventory;
import net.minecraft.src.Icon;
import net.minecraft.src.IconRegister;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.TileEntity;
import net.minecraft.src.TileEntityDispenser;
import net.minecraft.src.World;

public class BlockDispenserBlock extends BlockContainer {
   private static final int BLOCK_DISPENSER_TICK_RATE = 4;
   @Environment(EnvType.CLIENT)
   private Icon[] iconBySideArray = new Icon[6];
   @Environment(EnvType.CLIENT)
   private Icon iconFront;

   public BlockDispenserBlock(int iBlockID) {
      super(iBlockID, Material.rock);
      this.c(3.5F);
      this.b(true);
      this.a(Block.soundStoneFootstep);
      this.c("fcBlockBlockDispenser");
      this.a(CreativeTabs.tabRedstone);
   }

   @Override
   public int tickRate(World world) {
      return 4;
   }

   @Override
   public int idDropped(int i, Random random, int iFortuneModifier) {
      return BTWBlocks.blockDispenser.blockID;
   }

   @Override
   public void onBlockAdded(World world, int i, int j, int k) {
      world.scheduleBlockUpdate(i, j, k, this.blockID, this.tickRate(world));
   }

   @Override
   public int onBlockPlaced(World world, int i, int j, int k, int iFacing, float fClickX, float fClickY, float fClickZ, int iMetadata) {
      return this.setFacing(iMetadata, Block.getOppositeFacing(iFacing));
   }

   @Override
   public void onBlockPlacedBy(World world, int i, int j, int k, EntityLiving entityLiving, ItemStack stack) {
      int iFacing = MiscUtils.convertPlacingEntityOrientationToBlockFacingReversed(entityLiving);
      this.setFacing(world, i, j, k, iFacing);
      world.scheduleBlockUpdate(i, j, k, this.blockID, this.tickRate(world));
   }

   @Override
   public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer player, int iFacing, float fXClick, float fYClick, float fZClick) {
      if (!world.isRemote) {
         BlockDispenserTileEntity tileEntity = (BlockDispenserTileEntity)world.getBlockTileEntity(i, j, k);
         if (player instanceof EntityPlayerMP) {
            BlockDispenserContainer container = new BlockDispenserContainer(player.inventory, tileEntity);
            BTWMod.serverOpenCustomInterface((EntityPlayerMP)player, container, BTWContainers.blockDispenserContainerID);
         }
      }

      return true;
   }

   @Override
   public TileEntity createNewTileEntity(World world) {
      return new BlockDispenserTileEntity();
   }

   @Override
   public void breakBlock(World world, int i, int j, int k, int iBlockID, int iMetadata) {
      InventoryUtils.ejectInventoryContents(world, i, j, k, (IInventory)world.getBlockTileEntity(i, j, k));
      super.breakBlock(world, i, j, k, iBlockID, iMetadata);
   }

   @Override
   public void updateTick(World world, int i, int j, int k, Random random) {
      this.validateBlockDispenser(world, i, j, k);
      boolean bIsPowered = this.isReceivingRedstonePower(world, i, j, k);
      if (bIsPowered) {
         if (!this.isRedstoneOn(world, i, j, k)) {
            this.setRedstoneOn(world, i, j, k, true);
            this.dispenseBlockOrItem(world, i, j, k);
         }
      } else if (this.isRedstoneOn(world, i, j, k)) {
         this.setRedstoneOn(world, i, j, k, false);
         this.consumeFacingBlock(world, i, j, k);
      }
   }

   @Override
   public void onNeighborBlockChange(World world, int i, int j, int k, int iChangedBlockID) {
      if (!this.isCurrentStateValid(world, i, j, k) && !world.isUpdatePendingThisTickForBlock(i, j, k, this.blockID)) {
         world.scheduleBlockUpdate(i, j, k, this.blockID, this.tickRate(world));
      }
   }

   @Override
   public void randomUpdateTick(World world, int i, int j, int k, Random rand) {
      if (!this.isCurrentStateValid(world, i, j, k) && !world.isUpdateScheduledForBlock(i, j, k, this.blockID)) {
         world.scheduleBlockUpdate(i, j, k, this.blockID, this.tickRate(world));
      }
   }

   @Override
   public int getFacing(int iMetadata) {
      return iMetadata & -9;
   }

   @Override
   public int setFacing(int iMetadata, int iFacing) {
      iMetadata &= 8;
      return iMetadata | iFacing;
   }

   @Override
   public boolean rotateAroundJAxis(World world, int i, int j, int k, boolean bReverse) {
      if (super.rotateAroundJAxis(world, i, j, k, bReverse)) {
         world.markBlockForUpdate(i, j, k);
         return true;
      } else {
         return false;
      }
   }

   @Override
   public boolean toggleFacing(World world, int i, int j, int k, boolean bReverse) {
      int iFacing = this.getFacing(world, i, j, k);
      iFacing = Block.cycleFacing(iFacing, bReverse);
      this.setFacing(world, i, j, k, iFacing);
      world.markBlockRangeForRenderUpdate(i, j, k, i, j, k);
      return true;
   }

   @Override
   public boolean hasComparatorInputOverride() {
      return true;
   }

   @Override
   public int getComparatorInputOverride(World par1World, int par2, int par3, int par4, int par5) {
      return Container.calcRedstoneFromInventory((IInventory)par1World.getBlockTileEntity(par2, par3, par4));
   }

   public boolean isCurrentStateValid(World world, int i, int j, int k) {
      return this.isRedstoneOn(world, i, j, k) == this.isReceivingRedstonePower(world, i, j, k);
   }

   public boolean isRedstoneOn(World world, int i, int j, int k) {
      int iMetaData = world.getBlockMetadata(i, j, k);
      return (iMetaData & 8) > 0;
   }

   private void setRedstoneOn(World world, int i, int j, int k, boolean bOn) {
      int iMetaData = world.getBlockMetadata(i, j, k);
      if (bOn) {
         iMetaData |= 8;
      } else {
         iMetaData &= -9;
      }

      world.setBlockMetadataWithNotify(i, j, k, iMetaData);
   }

   private boolean isReceivingRedstonePower(World world, int i, int j, int k) {
      return world.isBlockGettingPowered(i, j, k) || world.isBlockGettingPowered(i, j + 1, k);
   }

   private boolean addBlockToInventory(World world, int i, int j, int k, Block targetBlock, BlockPos targetPos) {
      this.validateBlockDispenser(world, i, j, k);
      ItemStack stack = targetBlock.getStackRetrievedByBlockDispenser(world, targetPos.x, targetPos.y, targetPos.z);
      if (stack == null) {
         return false;
      } else {
         BlockDispenserTileEntity tileEntityDispenser = (BlockDispenserTileEntity)world.getBlockTileEntity(i, j, k);
         int iInitialSize = stack.stackSize;
         boolean bWholeStackAdded = InventoryUtils.addItemStackToInventory(tileEntityDispenser, stack);
         return bWholeStackAdded || stack.stackSize < iInitialSize;
      }
   }

   private boolean consumeEntityAtTargetLoc(World world, int i, int j, int k, int targeti, int targetj, int targetk) {
      this.validateBlockDispenser(world, i, j, k);
      List list = null;
      list = world.getEntitiesWithinAABB(Entity.class, AxisAlignedBB.getAABBPool().getAABB(targeti, targetj, targetk, targeti + 1, targetj + 1, targetk + 1));
      if (list != null && list.size() > 0) {
         BlockDispenserTileEntity tileEentityDispenser = (BlockDispenserTileEntity)world.getBlockTileEntity(i, j, k);

         for (int listIndex = 0; listIndex < list.size(); listIndex++) {
            Entity targetEntity = (Entity)list.get(listIndex);
            if (!targetEntity.isDead && targetEntity.onBlockDispenserConsume(this, tileEentityDispenser)) {
               return true;
            }
         }
      }

      return false;
   }

   private void consumeFacingBlock(World world, int i, int j, int k) {
      int iFacingDirection = this.getFacing(world, i, j, k);
      BlockPos targetPos = new BlockPos(i, j, k);
      targetPos.addFacingAsOffset(iFacingDirection);
      if (!this.consumeEntityAtTargetLoc(world, i, j, k, targetPos.x, targetPos.y, targetPos.z) && !world.isAirBlock(targetPos.x, targetPos.y, targetPos.z)) {
         int iTargetBlockID = world.getBlockId(targetPos.x, targetPos.y, targetPos.z);
         Block targetBlock = Block.blocksList[iTargetBlockID];
         if (targetBlock != null) {
            int iTargetMetadata = world.getBlockMetadata(targetPos.x, targetPos.y, targetPos.z);
            if (this.addBlockToInventory(world, i, j, k, targetBlock, targetPos) || targetBlock.isBlockDestroyedByBlockDispenser(iTargetMetadata)) {
               targetBlock.onRemovedByBlockDispenser(world, targetPos.x, targetPos.y, targetPos.z);
            }
         }
      }
   }

   @Override
   public void onRemovedByBlockDispenser(World world, int i, int j, int k) {
      BlockDispenserTileEntity tileEntity = (BlockDispenserTileEntity)world.getBlockTileEntity(i, j, k);
      InventoryUtils.clearInventoryContents(tileEntity);
      super.onRemovedByBlockDispenser(world, i, j, k);
   }

   private boolean dispenseBlockOrItem(World world, int i, int j, int k) {
      this.validateBlockDispenser(world, i, j, k);
      int iFacing = this.getFacing(world, i, j, k);
      BlockPos targetPos = new BlockPos(i, j, k, iFacing);
      Block targetBlock = Block.blocksList[world.getBlockId(targetPos.x, targetPos.y, targetPos.z)];
      if (targetBlock == null || targetBlock.blockMaterial.isReplaceable() || !targetBlock.blockMaterial.isSolid()) {
         BlockDispenserTileEntity tileEntityBlockDispenser = (BlockDispenserTileEntity)world.getBlockTileEntity(i, j, k);
         ItemStack itemstack = tileEntityBlockDispenser.getCurrentItemToDispense();
         if (itemstack != null) {
            Block newBlock = null;
            int iNewBlockMetadata = -1;
            if (itemstack.getItem().onItemUsedByBlockDispenser(itemstack, world, i, j, k, iFacing)) {
               world.playAuxSFX(2241, i, j, k, iFacing);
               tileEntityBlockDispenser.onDispenseCurrentSlot();
               return true;
            }
         }
      }

      world.playAuxSFX(2238, i, j, k, 0);
      return false;
   }

   private boolean validateBlockDispenser(World world, int i, int j, int k) {
      TileEntity oldTileEntity = world.getBlockTileEntity(i, j, k);
      if (oldTileEntity instanceof BlockDispenserTileEntity) {
         return true;
      } else {
         BlockDispenserTileEntity newTileEntity = new BlockDispenserTileEntity();
         if (oldTileEntity instanceof TileEntityDispenser) {
            TileEntityDispenser oldTileEntityDispenser = (TileEntityDispenser)oldTileEntity;
            int iOldInventorySize = oldTileEntityDispenser.getSizeInventory();
            int iNewInventorySize = newTileEntity.getSizeInventory();

            for (int tempSlot = 0; tempSlot < iOldInventorySize && tempSlot < iNewInventorySize; tempSlot++) {
               ItemStack tempStack = oldTileEntityDispenser.getStackInSlot(tempSlot);
               if (tempStack != null) {
                  newTileEntity.setInventorySlotContents(tempSlot, tempStack.copy());
               }
            }
         }

         world.setBlockTileEntity(i, j, k, newTileEntity);
         return false;
      }
   }

   public void spitOutItem(World world, int i, int j, int k, ItemStack itemstack) {
      int iFacing = this.getFacing(world, i, j, k);
      BlockPos offsetPos = new BlockPos(0, 0, 0, iFacing);
      double dXPos = i + offsetPos.x * 0.5 + 0.5;
      double dYPos = j + offsetPos.y + 0.2;
      double dZPos = k + offsetPos.z * 0.5 + 0.5;
      double dYHeading;
      if (iFacing > 2) {
         dYHeading = 0.1;
      } else {
         dYHeading = offsetPos.y;
      }

      EntityItem entityitem = (EntityItem)EntityList.createEntityOfType(EntityItem.class, world, dXPos, dYPos, dZPos, itemstack);
      double dRandVel = world.rand.nextDouble() * 0.1 + 0.2;
      entityitem.motionX = offsetPos.x * dRandVel;
      entityitem.motionY = dYHeading * dRandVel + 0.2;
      entityitem.motionZ = offsetPos.z * dRandVel;
      entityitem.motionX = entityitem.motionX + world.rand.nextGaussian() * 0.0075 * 6.0;
      entityitem.motionY = entityitem.motionY + world.rand.nextGaussian() * 0.0075 * 6.0;
      entityitem.motionZ = entityitem.motionZ + world.rand.nextGaussian() * 0.0075 * 6.0;
      world.spawnEntityInWorld(entityitem);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void registerIcons(IconRegister register) {
      Icon topIcon = register.registerIcon("fcBlockBlockDispenser_top");
      this.blockIcon = topIcon;
      this.iconFront = register.registerIcon("fcBlockBlockDispenser_front");
      this.iconBySideArray[0] = register.registerIcon("fcBlockBlockDispenser_bottom");
      this.iconBySideArray[1] = topIcon;
      Icon sideIcon = register.registerIcon("fcBlockBlockDispenser_side");
      this.iconBySideArray[2] = sideIcon;
      this.iconBySideArray[3] = sideIcon;
      this.iconBySideArray[4] = sideIcon;
      this.iconBySideArray[5] = sideIcon;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public Icon getIcon(int iSide, int iMetadata) {
      return iSide == 3 ? this.iconFront : this.iconBySideArray[iSide];
   }

   @Environment(EnvType.CLIENT)
   @Override
   public Icon getBlockTexture(IBlockAccess blockAccess, int i, int j, int k, int iSide) {
      int iFacing = this.getFacing(blockAccess, i, j, k);
      return iFacing == iSide ? this.iconFront : this.iconBySideArray[iSide];
   }
}
