package btw.block.blocks;

import btw.BTWMod;
import btw.block.BTWBlocks;
import btw.block.MechanicalBlock;
import btw.block.tileentity.HopperTileEntity;
import btw.block.util.Flammability;
import btw.block.util.MechPowerUtils;
import btw.block.util.RayTraceUtils;
import btw.client.render.util.RenderUtils;
import btw.crafting.manager.HopperFilteringCraftingManager;
import btw.crafting.recipe.types.HopperFilterRecipe;
import btw.inventory.BTWContainers;
import btw.inventory.container.HopperContainer;
import btw.inventory.util.InventoryUtils;
import btw.item.BTWItems;
import java.util.Random;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.BlockContainer;
import net.minecraft.src.Container;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityArrow;
import net.minecraft.src.EntityItem;
import net.minecraft.src.EntityList;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EntityPlayerMP;
import net.minecraft.src.EntityXPOrb;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.IInventory;
import net.minecraft.src.Icon;
import net.minecraft.src.IconRegister;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.MovingObjectPosition;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.Tessellator;
import net.minecraft.src.TileEntity;
import net.minecraft.src.Vec3;
import net.minecraft.src.World;
import org.lwjgl.opengl.GL11;

public class HopperBlock extends BlockContainer implements MechanicalBlock {
   protected static final int TICK_RATE = 10;
   protected static final float SPOUT_HEIGHT = 0.25F;
   protected static final float SPOUT_WIDTH = 0.375F;
   protected static final float SPOUT_HALF_WIDTH = 0.1875F;
   @Environment(EnvType.CLIENT)
   private Icon[] iconBinBySideArray = new Icon[6];
   @Environment(EnvType.CLIENT)
   private Icon[] iconNozzleBySideArray = new Icon[6];
   @Environment(EnvType.CLIENT)
   private Icon iconInteriorSide;
   @Environment(EnvType.CLIENT)
   private Icon iconInteriorTop;
   @Environment(EnvType.CLIENT)
   private Icon iconContents;
   @Environment(EnvType.CLIENT)
   private boolean isRenderingNozzle = false;

   public HopperBlock(int iBlockID) {
      super(iBlockID, BTWBlocks.plankMaterial);
      this.c(2.0F);
      this.setAxesEffectiveOn(true);
      this.setBuoyancy(1.0F);
      this.setFireProperties(Flammability.PLANKS);
      this.initBlockBounds(0.0, 0.0, 0.0, 1.0, 1.0, 1.0);
      this.a(g);
      this.c("fcBlockHopper");
      this.b(true);
      this.a(CreativeTabs.tabRedstone);
   }

   @Override
   public int tickRate(World world) {
      return 10;
   }

   @Override
   public void onBlockAdded(World world, int i, int j, int k) {
      super.onBlockAdded(world, i, j, k);
      world.scheduleBlockUpdate(i, j, k, this.blockID, this.tickRate(world));
   }

   @Override
   public boolean isOpaqueCube() {
      return false;
   }

   @Override
   public boolean renderAsNormalBlock() {
      return false;
   }

   @Override
   public void onNeighborBlockChange(World world, int i, int j, int k, int iBlockID) {
      boolean bReceivingPower = this.isInputtingMechanicalPower(world, i, j, k);
      if (this.isBlockOn(world, i, j, k) != bReceivingPower && !world.isUpdatePendingThisTickForBlock(i, j, k, this.blockID)) {
         world.scheduleBlockUpdate(i, j, k, this.blockID, this.tickRate(world));
      }

      ((HopperTileEntity)world.getBlockTileEntity(i, j, k)).outputBlocked = false;
   }

   @Override
   public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer player, int iFacing, float fXClick, float fYClick, float fZClick) {
      if (!world.isRemote) {
         HopperTileEntity tileEntityHopper = (HopperTileEntity)world.getBlockTileEntity(i, j, k);
         if (player instanceof EntityPlayerMP) {
            HopperContainer container = new HopperContainer(player.inventory, tileEntityHopper);
            BTWMod.serverOpenCustomInterface((EntityPlayerMP)player, container, BTWContainers.hopperContainerID);
         }
      }

      return true;
   }

   @Override
   public TileEntity createNewTileEntity(World world) {
      return new HopperTileEntity();
   }

   @Override
   public void updateTick(World world, int i, int j, int k, Random random) {
      boolean bReceivingPower = this.isInputtingMechanicalPower(world, i, j, k);
      boolean bOn = this.isBlockOn(world, i, j, k);
      if (bOn != bReceivingPower) {
         world.playAuxSFX(2233, i, j, k, 0);
         this.setBlockOn(world, i, j, k, bReceivingPower);
      }
   }

   @Override
   public void breakBlock(World world, int i, int j, int k, int iBlockID, int iMetadata) {
      InventoryUtils.ejectInventoryContents(world, i, j, k, (IInventory)world.getBlockTileEntity(i, j, k));
      super.breakBlock(world, i, j, k, iBlockID, iMetadata);
   }

   @Override
   public void onEntityCollidedWithBlock(World world, int i, int j, int k, Entity entity) {
      if (!world.isRemote) {
         if (entity instanceof EntityItem) {
            this.onEntityItemCollidedWithBlock(world, i, j, k, (EntityItem)entity);
         } else if (entity instanceof EntityXPOrb) {
            this.onEntityXPOrbCollidedWithBlock(world, i, j, k, (EntityXPOrb)entity);
         }
      }
   }

   @Override
   public MovingObjectPosition collisionRayTrace(World world, int i, int j, int k, Vec3 startRay, Vec3 endRay) {
      RayTraceUtils rayTrace = new RayTraceUtils(world, i, j, k, startRay, endRay);
      rayTrace.addBoxWithLocalCoordsToIntersectionList(0.3125, 0.0, 0.3125, 0.6875, 0.25, 0.6875);
      rayTrace.addBoxWithLocalCoordsToIntersectionList(0.0, 0.25, 0.0, 1.0, 1.0, 1.0);
      return rayTrace.getFirstIntersection();
   }

   @Override
   public void onArrowCollide(World world, int i, int j, int k, EntityArrow arrow) {
      if (!world.isRemote && !arrow.isDead && arrow.canHopperCollect()) {
         Vec3 arrowPos = Vec3.createVectorHelper(arrow.posX, arrow.posY, arrow.posZ);
         AxisAlignedBB collectionZone = AxisAlignedBB.getAABBPool().getAABB(i, j + 0.9F, k, i + 1, j + 1.1F, k + 1);
         if (collectionZone.isVecInside(arrowPos)) {
            HopperTileEntity tileEntityHopper = (HopperTileEntity)world.getBlockTileEntity(i, j, k);
            ItemStack newItemStack = new ItemStack(arrow.getCorrespondingItem(), 1, 0);
            if (tileEntityHopper.canCurrentFilterProcessItem(newItemStack)
               && InventoryUtils.addItemStackToInventoryInSlotRange(tileEntityHopper, newItemStack, 0, 17)) {
               arrow.w();
               world.playAuxSFX(2231, i, j, k, 0);
            }
         }
      }
   }

   @Override
   public int getHarvestToolLevel(IBlockAccess blockAccess, int i, int j, int k) {
      return 2;
   }

   @Override
   public boolean dropComponentItemsOnBadBreak(World world, int i, int j, int k, int iMetadata, float fChanceOfDrop) {
      this.dropItemsIndividually(world, i, j, k, Item.stick.itemID, 2, 0, fChanceOfDrop);
      this.dropItemsIndividually(world, i, j, k, BTWItems.sawDust.itemID, 2, 0, fChanceOfDrop);
      this.dropItemsIndividually(world, i, j, k, BTWItems.gear.itemID, 1, 0, fChanceOfDrop);
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

   @Override
   public boolean canOutputMechanicalPower() {
      return false;
   }

   @Override
   public boolean canInputMechanicalPower() {
      return true;
   }

   @Override
   public boolean isInputtingMechanicalPower(World world, int i, int j, int k) {
      return MechPowerUtils.isBlockPoweredByAxle(world, i, j, k, this) || MechPowerUtils.isBlockPoweredByHandCrank(world, i, j, k);
   }

   @Override
   public boolean canInputAxlePowerToFacing(World world, int i, int j, int k, int iFacing) {
      return iFacing >= 2;
   }

   @Override
   public boolean isOutputtingMechanicalPower(World world, int i, int j, int k) {
      return false;
   }

   @Override
   public void overpower(World world, int i, int j, int k) {
      this.breakHopper(world, i, j, k);
   }

   public boolean isBlockOn(IBlockAccess iBlockAccess, int i, int j, int k) {
      return (iBlockAccess.getBlockMetadata(i, j, k) & 1) > 0;
   }

   public void setBlockOn(World world, int i, int j, int k, boolean bOn) {
      int iMetaData = world.getBlockMetadata(i, j, k);
      if (bOn) {
         iMetaData |= 1;
      } else {
         iMetaData &= -2;
      }

      world.setBlockMetadataWithNotifyNoClient(i, j, k, iMetaData);
   }

   public boolean hasFilter(IBlockAccess iBlockAccess, int i, int j, int k) {
      return (iBlockAccess.getBlockMetadata(i, j, k) & 8) > 0;
   }

   public void setHasFilter(World world, int i, int j, int k, boolean bOn) {
      int iMetaData = world.getBlockMetadata(i, j, k);
      if (bOn) {
         iMetaData |= 8;
      } else {
         iMetaData &= -9;
      }

      world.setBlockMetadataWithNotifyNoClient(i, j, k, iMetaData);
   }

   public void breakHopper(World world, int i, int j, int k) {
      this.dropComponentItemsOnBadBreak(world, i, j, k, world.getBlockMetadata(i, j, k), 1.0F);
      world.playAuxSFX(2235, i, j, k, 0);
      world.setBlockWithNotify(i, j, k, 0);
   }

   private void onEntityItemCollidedWithBlock(World world, int x, int y, int z, EntityItem entityItem) {
      float hopperHeight = 1.0F;
      AxisAlignedBB collectionZone = AxisAlignedBB.getAABBPool().getAABB(x, y + hopperHeight, z, x + 1, y + hopperHeight + 0.05F, z + 1);
      if (entityItem.boundingBox.intersectsWith(collectionZone) && !entityItem.isDead) {
         Item targetItem = Item.itemsList[entityItem.getEntityItem().itemID];
         ItemStack targetStack = entityItem.getEntityItem();
         HopperTileEntity tileEntityHopper = (HopperTileEntity)world.getBlockTileEntity(x, y, z);
         if (tileEntityHopper.canCurrentFilterProcessItem(targetStack)) {
            Item filterItem = tileEntityHopper.getFilterItem();
            HopperFilterRecipe recipe = filterItem == null ? null : HopperFilteringCraftingManager.instance.getRecipe(targetStack, new ItemStack(filterItem));
            if (recipe != null) {
               if (recipe.getContainsSouls()) {
                  ItemStack filteredOutput = recipe.getFilteredOutput();
                  this.convertItemAndIncrementSouls(world, x, y, z, entityItem, filteredOutput.itemID, filteredOutput.getItemDamage());
               } else {
                  ItemStack hopperOutput = recipe.getHopperOutput().copy();
                  int quantityToAttemptToSwallow = targetStack.stackSize * hopperOutput.stackSize;
                  int quantityToSwallow = 0;
                  int availableSpots = InventoryUtils.getMaxNumberOfItemsForTransferInRange(tileEntityHopper, hopperOutput, quantityToAttemptToSwallow, 0, 17);
                  if (availableSpots == 0) {
                     return;
                  }

                  if (quantityToAttemptToSwallow <= availableSpots) {
                     quantityToSwallow = quantityToAttemptToSwallow;
                     entityItem.w();
                  } else {
                     quantityToSwallow = availableSpots / hopperOutput.stackSize;
                     quantityToSwallow *= hopperOutput.stackSize;
                  }

                  int quantityProcessed = quantityToSwallow / hopperOutput.stackSize;
                  if (!entityItem.isDead) {
                     entityItem.getEntityItem().stackSize -= quantityProcessed;
                  }

                  if (quantityProcessed > 0) {
                     while (quantityToSwallow > 0) {
                        ItemStack stackToTransfer = hopperOutput.copy();
                        if (quantityToSwallow >= stackToTransfer.getMaxStackSize()) {
                           stackToTransfer.stackSize = stackToTransfer.getMaxStackSize();
                        } else {
                           stackToTransfer.stackSize = quantityToSwallow;
                        }

                        quantityToSwallow -= stackToTransfer.stackSize;
                        InventoryUtils.addItemStackToInventoryInSlotRange(tileEntityHopper, stackToTransfer, 0, 17);
                     }

                     world.playAuxSFX(2231, x, y, z, 0);
                     if (recipe.getFilteredOutput() != null) {
                        ItemStack filteredOutput = recipe.getFilteredOutput().copy();
                        int outputCount = filteredOutput.stackSize;

                        for (int i = 0; i < outputCount; i++) {
                           filteredOutput.stackSize = quantityProcessed;
                           EntityItem filteredEntity = (EntityItem)EntityList.createEntityOfType(
                              EntityItem.class, world, entityItem.posX, entityItem.posY, entityItem.posZ, filteredOutput.copy()
                           );
                           filteredEntity.delayBeforeCanPickup = 10;
                           world.spawnEntityInWorld(filteredEntity);
                        }
                     }
                  }
               }
            } else if (InventoryUtils.addItemStackToInventoryInSlotRange(tileEntityHopper, entityItem.getEntityItem(), 0, 17)) {
               world.playAuxSFX(2231, x, y, z, 0);
               entityItem.w();
            }
         }
      }
   }

   private void convertItemAndIncrementSouls(World world, int i, int j, int k, EntityItem inputEntityItem, int iOutputItemID, int iOutputItemDamage) {
      HopperTileEntity tileEntityHopper = (HopperTileEntity)world.getBlockTileEntity(i, j, k);
      ItemStack outputItemStack = new ItemStack(iOutputItemID, inputEntityItem.getEntityItem().stackSize, iOutputItemDamage);
      EntityItem outputEntityItem = (EntityItem)EntityList.createEntityOfType(
         EntityItem.class, world, inputEntityItem.posX, inputEntityItem.posY, inputEntityItem.posZ, outputItemStack
      );
      outputEntityItem.delayBeforeCanPickup = 10;
      tileEntityHopper.incrementContainedSoulCount(outputItemStack.stackSize);
      world.spawnEntityInWorld(outputEntityItem);
      world.playAuxSFX(2228, i, j, k, 0);
      inputEntityItem.w();
   }

   private void onEntityXPOrbCollidedWithBlock(World world, int i, int j, int k, EntityXPOrb entityXPOrb) {
      if (!entityXPOrb.isDead) {
         float fHopperHeight = 1.0F;
         AxisAlignedBB collectionZone = AxisAlignedBB.getAABBPool().getAABB(i, j + fHopperHeight, k, i + 1, j + fHopperHeight + 0.05F, k + 1);
         if (entityXPOrb.boundingBox.intersectsWith(collectionZone)) {
            HopperTileEntity tileEntityHopper = (HopperTileEntity)world.getBlockTileEntity(i, j, k);
            Item filterItem = tileEntityHopper.getFilterItem();
            if (filterItem != null && filterItem.itemID == Block.slowSand.blockID && tileEntityHopper.attemptToSwallowXPOrb(world, i, j, k, entityXPOrb)) {
               world.playAuxSFX(2231, i, j, k, 0);
            }
         }
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void registerIcons(IconRegister register) {
      this.iconBinBySideArray[0] = register.registerIcon("fcBlockHopperBin_bottom");
      this.iconBinBySideArray[1] = register.registerIcon("fcBlockHopperBin_top");
      Icon sideIcon = register.registerIcon("fcBlockHopperBin_side");
      this.iconBinBySideArray[2] = sideIcon;
      this.iconBinBySideArray[3] = sideIcon;
      this.iconBinBySideArray[4] = sideIcon;
      this.iconBinBySideArray[5] = sideIcon;
      this.blockIcon = sideIcon;
      this.iconNozzleBySideArray[0] = register.registerIcon("fcBlockHopperNozzle_bottom");
      this.iconNozzleBySideArray[1] = this.iconBinBySideArray[1];
      sideIcon = register.registerIcon("fcBlockHopperNozzle_side");
      this.iconNozzleBySideArray[2] = sideIcon;
      this.iconNozzleBySideArray[3] = sideIcon;
      this.iconNozzleBySideArray[4] = sideIcon;
      this.iconNozzleBySideArray[5] = sideIcon;
      this.iconInteriorSide = register.registerIcon("fcBlockHopperInterior_side");
      this.iconInteriorTop = register.registerIcon("fcBlockHopperInterior_top");
      this.iconContents = register.registerIcon("fcBlockHopper_contents");
   }

   @Environment(EnvType.CLIENT)
   @Override
   public Icon getIcon(int iSide, int iMetadata) {
      return this.isRenderingNozzle ? this.iconNozzleBySideArray[iSide] : this.iconBinBySideArray[iSide];
   }

   @Environment(EnvType.CLIENT)
   @Override
   public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int i, int j, int k) {
      return AxisAlignedBB.getAABBPool().getAABB(0.0, 0.25, 0.0, 1.0, 1.0, 1.0).offset(i, j, k);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean shouldSideBeRendered(IBlockAccess blockAccess, int iNeighborI, int iNeighborJ, int iNeighborK, int iSide) {
      return true;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean renderBlock(RenderBlocks renderer, int i, int j, int k) {
      IBlockAccess blockAccess = renderer.blockAccess;
      this.isRenderingNozzle = false;
      renderer.setRenderBounds(0.0, 0.25, 0.0, 1.0, 1.0, 1.0);
      renderer.renderStandardBlock(this, i, j, k);
      Tessellator tesselator = Tessellator.instance;
      tesselator.setBrightness(this.e(blockAccess, i, j, k));
      float fInteriorBrightnessMultiplier = 0.66F;
      int iColorMultiplier = this.c(blockAccess, i, j, k);
      float iColorRed = (iColorMultiplier >> 16 & 0xFF) / 255.0F;
      float iColorGreen = (iColorMultiplier >> 8 & 0xFF) / 255.0F;
      float iColorBlue = (iColorMultiplier & 0xFF) / 255.0F;
      tesselator.setColorOpaque_F(0.66F * iColorRed, 0.66F * iColorGreen, 0.66F * iColorBlue);
      double dInteriorOffset = 0.124;
      renderer.renderFaceXPos(this, i - 1.0 + 0.124, j, k, this.iconInteriorSide);
      renderer.renderFaceXNeg(this, i + 1.0 - 0.124, j, k, this.iconInteriorSide);
      renderer.renderFaceZPos(this, i, j, k - 1.0 + 0.124, this.iconInteriorSide);
      renderer.renderFaceZNeg(this, i, j, k + 1.0 - 0.124, this.iconInteriorSide);
      renderer.renderFaceYPos(this, i, j - 1.0 + 0.25, k, this.iconInteriorTop);
      this.isRenderingNozzle = true;
      renderer.setRenderBounds(0.3125, 0.0, 0.3125, 0.6875, 0.25, 0.6875);
      renderer.renderStandardBlock(this, i, j, k);
      TileEntity tileEntity = blockAccess.getBlockTileEntity(i, j, k);
      if (tileEntity instanceof HopperTileEntity) {
         HopperTileEntity tileEntityHopper = (HopperTileEntity)tileEntity;
         short iItemCount = ((HopperTileEntity)tileEntity).storageSlotsOccupied;
         if (iItemCount > 0) {
            float fHeightRatio = iItemCount / 18.0F;
            float fBottom = 0.375F;
            float fTop = fBottom + 0.0625F + (0.875F - (fBottom + 0.0625F)) * fHeightRatio;
            renderer.setRenderBounds(0.125, fBottom, 0.125, 0.875, fTop, 0.875);
            RenderUtils.renderStandardBlockWithTexture(renderer, this, i, j, k, this.iconContents);
         }

         Item filterItem = tileEntityHopper.getCurrentFilterItem();
         if (filterItem != null) {
            Icon filterIcon = filterItem.getHopperFilterIcon();
            if (filterIcon != null) {
               renderer.setRenderBounds(0.125, 0.875, 0.125, 0.875, 0.9375, 0.875);
               RenderUtils.renderStandardBlockWithTexture(renderer, this, i, j, k, filterIcon);
            }
         }
      }

      return true;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void renderBlockAsItem(RenderBlocks renderBlocks, int iItemDamage, float fBrightness) {
      IBlockAccess blockAccess = renderBlocks.blockAccess;
      this.isRenderingNozzle = false;
      renderBlocks.setRenderBounds(0.0, 0.25, 0.0, 1.0, 1.0, 1.0);
      RenderUtils.renderInvBlockWithMetadata(renderBlocks, this, -0.5F, -0.5F, -0.5F, 0);
      Tessellator tessellator = Tessellator.instance;
      double dInteriorOffset = 0.124;
      GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
      tessellator.startDrawingQuads();
      tessellator.setNormal(0.0F, 1.0F, 0.0F);
      renderBlocks.renderFaceYPos(this, 0.0, -0.75, 0.0, this.iconInteriorTop);
      tessellator.draw();
      tessellator.startDrawingQuads();
      tessellator.setNormal(0.0F, 0.0F, -1.0F);
      renderBlocks.renderFaceZNeg(this, 0.0, 0.0, 0.876, this.iconInteriorSide);
      tessellator.draw();
      tessellator.startDrawingQuads();
      tessellator.setNormal(0.0F, 0.0F, 1.0F);
      renderBlocks.renderFaceZPos(this, 0.0, 0.0, -0.876, this.iconInteriorSide);
      tessellator.draw();
      tessellator.startDrawingQuads();
      tessellator.setNormal(-1.0F, 0.0F, 0.0F);
      renderBlocks.renderFaceXNeg(this, 0.876, 0.0, 0.0, this.iconInteriorSide);
      tessellator.draw();
      tessellator.startDrawingQuads();
      tessellator.setNormal(1.0F, 0.0F, 0.0F);
      renderBlocks.renderFaceXPos(this, -0.876, 0.0, 0.0, this.iconInteriorSide);
      tessellator.draw();
      GL11.glTranslatef(0.5F, 0.5F, 0.5F);
      this.isRenderingNozzle = true;
      renderBlocks.setRenderBounds(0.3125, 0.0, 0.3125, 0.6875, 0.25, 0.6875);
      RenderUtils.renderInvBlockWithMetadata(renderBlocks, this, -0.5F, -0.5F, -0.5F, 0);
   }
}
