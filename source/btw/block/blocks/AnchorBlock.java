package btw.block.blocks;

import btw.block.BTWBlocks;
import btw.block.tileentity.PulleyTileEntity;
import btw.client.render.util.RenderUtils;
import btw.entity.mechanical.platform.MovingAnchorEntity;
import btw.item.BTWItems;
import btw.item.util.ItemUtils;
import btw.world.util.BlockPos;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EntityList;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Icon;
import net.minecraft.src.IconRegister;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.World;

public class AnchorBlock extends Block {
   public static double anchorBaseHeight = 0.375;
   @Environment(EnvType.CLIENT)
   private Icon iconFront;
   @Environment(EnvType.CLIENT)
   public Icon iconNub;
   @Environment(EnvType.CLIENT)
   private Icon iconRope;

   public AnchorBlock(int iBlockID) {
      super(iBlockID, Material.rock);
      this.c(2.0F);
      this.initBlockBounds(0.0, 0.0, 0.0, 1.0, anchorBaseHeight, 1.0);
      this.a(j);
      this.c("fcBlockAnchor");
      this.a(CreativeTabs.tabTransport);
   }

   @Override
   public AxisAlignedBB getBlockBoundsFromPoolBasedOnState(IBlockAccess blockAccess, int i, int j, int k) {
      int iFacing = this.getFacing(blockAccess, i, j, k);
      switch (iFacing) {
         case 0:
            return AxisAlignedBB.getAABBPool().getAABB(0.0, 1.0 - anchorBaseHeight, 0.0, 1.0, 1.0, 1.0);
         case 1:
            return AxisAlignedBB.getAABBPool().getAABB(0.0, 0.0, 0.0, 1.0, anchorBaseHeight, 1.0);
         case 2:
            return AxisAlignedBB.getAABBPool().getAABB(0.0, 0.0, 1.0 - anchorBaseHeight, 1.0, 1.0, 1.0);
         case 3:
            return AxisAlignedBB.getAABBPool().getAABB(0.0, 0.0, 0.0, 1.0, 1.0, anchorBaseHeight);
         case 4:
            return AxisAlignedBB.getAABBPool().getAABB(1.0 - anchorBaseHeight, 0.0, 0.0, 1.0, 1.0, 1.0);
         default:
            return AxisAlignedBB.getAABBPool().getAABB(0.0, 0.0, 0.0, anchorBaseHeight, 1.0, 1.0);
      }
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
   public int onBlockPlaced(World world, int i, int j, int k, int iFacing, float fClickX, float fClickY, float fClickZ, int iMetadata) {
      return this.setFacing(iMetadata, iFacing);
   }

   @Override
   public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer player, int iFacing, float fXClick, float fYClick, float fZClick) {
      ItemStack playerEquippedItem = player.getCurrentEquippedItem();
      if (playerEquippedItem != null) {
         return false;
      } else {
         this.retractRope(world, i, j, k, player);
         return true;
      }
   }

   @Override
   public int getFacing(int iMetadata) {
      return iMetadata;
   }

   @Override
   public int setFacing(int iMetadata, int iFacing) {
      return iFacing;
   }

   @Override
   public boolean canRotateOnTurntable(IBlockAccess blockAccess, int i, int j, int k) {
      int iFacing = this.getFacing(blockAccess, i, j, k);
      return iFacing != 0;
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
   public boolean hasLargeCenterHardPointToFacing(IBlockAccess blockAccess, int i, int j, int k, int iFacing, boolean bIgnoreTransparency) {
      return Block.getOppositeFacing(iFacing) == this.getFacing(blockAccess, i, j, k);
   }

   void retractRope(World world, int i, int j, int k, EntityPlayer entityPlayer) {
      for (int tempj = j - 1; tempj >= 0; tempj--) {
         int iTempBlockID = world.getBlockId(i, tempj, k);
         if (iTempBlockID != BTWBlocks.ropeBlock.blockID) {
            break;
         }

         if (world.getBlockId(i, tempj - 1, k) != BTWBlocks.ropeBlock.blockID) {
            this.addRopeToPlayerInventory(world, i, j, k, entityPlayer);
            Block targetBlock = BTWBlocks.ropeBlock;
            if (!world.isRemote) {
               world.playAuxSFX(2001, i, j, k, iTempBlockID);
               world.setBlockWithNotify(i, tempj, k, 0);
            }
            break;
         }
      }
   }

   private void addRopeToPlayerInventory(World world, int i, int j, int k, EntityPlayer entityPlayer) {
      ItemStack ropeStack = new ItemStack(BTWItems.rope);
      if (entityPlayer.inventory.addItemStackToInventory(ropeStack)) {
         world.playSoundAtEntity(entityPlayer, "random.pop", 0.2F, ((world.rand.nextFloat() - world.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
      } else {
         ItemUtils.ejectStackWithRandomOffset(world, i, j, k, ropeStack);
      }
   }

   public boolean notifyAnchorBlockOfAttachedPulleyStateChange(PulleyTileEntity tileEntityPulley, World world, int i, int j, int k) {
      int iMovementDirection = 0;
      if (tileEntityPulley.isRaising()) {
         if (world.getBlockId(i, j + 1, k) == BTWBlocks.ropeBlock.blockID) {
            iMovementDirection = 1;
         }
      } else if (tileEntityPulley.isLowering() && (world.isAirBlock(i, j - 1, k) || world.getBlockId(i, j - 1, k) == BTWBlocks.platform.blockID)) {
         iMovementDirection = -1;
      }

      if (iMovementDirection != 0) {
         this.convertAnchorToEntity(world, i, j, k, tileEntityPulley, iMovementDirection);
         return true;
      } else {
         return false;
      }
   }

   private void convertAnchorToEntity(World world, int i, int j, int k, PulleyTileEntity attachedTileEntityPulley, int iMovementDirection) {
      BlockPos pulleyPos = new BlockPos(attachedTileEntityPulley.xCoord, attachedTileEntityPulley.yCoord, attachedTileEntityPulley.zCoord);
      MovingAnchorEntity entityAnchor = (MovingAnchorEntity)EntityList.createEntityOfType(
         MovingAnchorEntity.class, world, i + 0.5F, j + 0.5F, k + 0.5F, pulleyPos, iMovementDirection
      );
      world.spawnEntityInWorld(entityAnchor);
      this.convertConnectedPlatformsToEntities(world, i, j, k, entityAnchor);
      world.setBlockWithNotify(i, j, k, 0);
   }

   private void convertConnectedPlatformsToEntities(World world, int i, int j, int k, MovingAnchorEntity associatedAnchorEntity) {
      int iTargetJ = j - 1;
      int iTargetBlockID = world.getBlockId(i, iTargetJ, k);
      if (iTargetBlockID == BTWBlocks.platform.blockID) {
         ((PlatformBlock)BTWBlocks.platform).covertToEntitiesFromThisPlatform(world, i, iTargetJ, k, associatedAnchorEntity);
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void registerIcons(IconRegister register) {
      super.registerIcons(register);
      this.iconFront = register.registerIcon("fcBlockAnchor_front");
      this.iconNub = register.registerIcon("fcBlockAnchor_nub");
      this.iconRope = register.registerIcon("fcBlockRope");
   }

   @Environment(EnvType.CLIENT)
   @Override
   public Icon getIcon(int iSide, int iMetadata) {
      return iSide < 2 ? this.iconFront : this.blockIcon;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public Icon getBlockTexture(IBlockAccess blockAccess, int i, int j, int k, int iSide) {
      int iFacing = blockAccess.getBlockMetadata(i, j, k);
      return iSide != iFacing && iSide != Block.getOppositeFacing(iFacing) ? this.blockIcon : this.iconFront;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean shouldSideBeRendered(IBlockAccess blockAccess, int iNeighborI, int iNeighborJ, int iNeighborK, int iSide) {
      return this.currentBlockRenderer.shouldSideBeRenderedBasedOnCurrentBounds(iNeighborI, iNeighborJ, iNeighborK, iSide);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean renderBlock(RenderBlocks renderer, int i, int j, int k) {
      IBlockAccess blockAccess = renderer.blockAccess;
      renderer.setRenderBounds(this.getBlockBoundsFromPoolBasedOnState(renderer.blockAccess, i, j, k));
      renderer.renderStandardBlock(this, i, j, k);
      int iFacing = this.getFacing(blockAccess, i, j, k);
      double dHalfLength = 0.125;
      double dHalfWidth = 0.125;
      double dBlockHeight = 0.25;
      renderer.setRenderBounds(0.5 - dHalfWidth, anchorBaseHeight, 0.5 - dHalfLength, 0.5 + dHalfWidth, anchorBaseHeight + dBlockHeight, 0.5 + dHalfLength);
      RenderUtils.renderStandardBlockWithTexture(renderer, this, i, j, k, this.iconNub);
      boolean bRenderRope = false;
      dHalfLength = 0.0625;
      dHalfWidth = 0.0625;
      dBlockHeight = anchorBaseHeight;
      if (iFacing == 1) {
         int iBlockAboveId = blockAccess.getBlockId(i, j + 1, k);
         if (iBlockAboveId == BTWBlocks.ropeBlock.blockID || iBlockAboveId == BTWBlocks.pulley.blockID) {
            renderer.setRenderBounds(0.5 - dHalfWidth, dBlockHeight, 0.5 - dHalfLength, 0.5 + dHalfWidth, 1.0, 0.5 + dHalfLength);
            bRenderRope = true;
         }
      } else if (blockAccess.getBlockId(i, j - 1, k) == BTWBlocks.ropeBlock.blockID) {
         renderer.setRenderBounds(0.5 - dHalfWidth, 0.0, 0.5 - dHalfLength, 0.5 + dHalfWidth, dBlockHeight, 0.5 + dHalfLength);
         bRenderRope = true;
      }

      if (bRenderRope) {
         RenderUtils.renderStandardBlockWithTexture(renderer, this, i, j, k, this.iconRope);
      }

      return true;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void renderBlockAsItem(RenderBlocks renderBlocks, int iItemDamage, float fBrightness) {
      renderBlocks.setRenderBounds(0.0, 0.0, 0.0, 1.0, anchorBaseHeight, 1.0);
      RenderUtils.renderInvBlockWithMetadata(renderBlocks, this, -0.5F, -0.25F, -0.5F, 1);
      float fHalfLength = 0.125F;
      float fHalfWidth = 0.125F;
      float fBlockHeight = 0.25F;
      renderBlocks.setRenderBounds(
         0.5F - fHalfWidth, anchorBaseHeight, 0.5F - fHalfLength, 0.5F + fHalfWidth, anchorBaseHeight + fBlockHeight, 0.5F + fHalfLength
      );
      RenderUtils.renderInvBlockWithTexture(renderBlocks, this, -0.5F, -0.25F, -0.5F, this.iconNub);
   }
}
