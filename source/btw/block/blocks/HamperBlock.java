package btw.block.blocks;

import btw.BTWMod;
import btw.block.model.BlockModel;
import btw.block.model.HamperModel;
import btw.block.tileentity.HamperTileEntity;
import btw.inventory.BTWContainers;
import btw.inventory.container.HamperContainer;
import btw.item.BTWItems;
import btw.world.util.WorldUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.EntityFallingSand;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EntityPlayerMP;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Icon;
import net.minecraft.src.IconRegister;
import net.minecraft.src.MovingObjectPosition;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.TileEntity;
import net.minecraft.src.Vec3;
import net.minecraft.src.World;

public class HamperBlock extends BasketBlock {
   public static final HamperModel model = new HamperModel();
   @Environment(EnvType.CLIENT)
   private boolean renderingBase = false;
   @Environment(EnvType.CLIENT)
   private Icon iconBaseOpenTop;
   @Environment(EnvType.CLIENT)
   private Icon iconFront;
   @Environment(EnvType.CLIENT)
   private Icon iconTop;
   @Environment(EnvType.CLIENT)
   private Icon iconBottom;

   public HamperBlock(int iBlockID) {
      super(iBlockID);
      this.c("fcBlockHamper");
      this.initBlockBounds(0.0625, 0.0, 0.0625, 0.9375, 1.0, 0.9375);
   }

   @Override
   public TileEntity createNewTileEntity(World world) {
      return new HamperTileEntity();
   }

   @Override
   public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer player, int iFacing, float fXClick, float fYClick, float fZClick) {
      if (!world.isRemote
         && !WorldUtils.doesBlockHaveCenterHardpointToFacing(world, i, j + 1, k, 0, true)
         && !WorldUtils.isBlockRestingOnThatBelow(world, i, j + 1, k)) {
         HamperTileEntity tileEntity = (HamperTileEntity)world.getBlockTileEntity(i, j, k);
         if (player instanceof EntityPlayerMP) {
            HamperContainer container = new HamperContainer(player.inventory, tileEntity);
            BTWMod.serverOpenCustomInterface((EntityPlayerMP)player, container, BTWContainers.hamperContainerID);
         }
      }

      return true;
   }

   @Override
   public boolean hasCenterHardPointToFacing(IBlockAccess blockAccess, int i, int j, int k, int iFacing, boolean bIgnoreTransparency) {
      return iFacing == 0 || iFacing == 1 && !this.getIsOpen(blockAccess, i, j, k);
   }

   @Override
   public void onCrushedByFallingEntity(World world, int i, int j, int k, EntityFallingSand entity) {
      if (!world.isRemote) {
         this.dropItemsIndividually(world, i, j, k, BTWItems.wickerPane.itemID, 2, 0, 0.75F);
      }
   }

   @Override
   public BlockModel getLidModel(int iMetadata) {
      return model.lid;
   }

   @Override
   public Vec3 getLidRotationPoint() {
      return model.getLidRotationPoint();
   }

   @Override
   public MovingObjectPosition collisionRayTrace(World world, int i, int j, int k, Vec3 startRay, Vec3 endRay) {
      int iFacing = this.getFacing(world, i, j, k);
      BlockModel transformedModel = model.makeTemporaryCopy();
      transformedModel.rotateAroundYToFacing(iFacing);
      return transformedModel.collisionRayTrace(world, i, j, k, startRay, endRay);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void registerIcons(IconRegister register) {
      super.a(register);
      this.iconBaseOpenTop = register.registerIcon("fcBlockHamper_open_top");
      this.iconFront = register.registerIcon("fcBlockHamper_front");
      this.iconTop = register.registerIcon("fcBlockHamper_top");
      this.iconBottom = register.registerIcon("fcBlockHamper_bottom");
   }

   @Environment(EnvType.CLIENT)
   @Override
   public Icon getIcon(int iSide, int iMetadata) {
      if (iSide == 1) {
         return this.renderingBase ? this.iconBaseOpenTop : this.iconTop;
      } else if (iSide == 0) {
         return this.iconBottom;
      } else {
         int iFacing = this.getFacing(iMetadata);
         return iSide == iFacing ? this.iconFront : super.a(iSide, iMetadata);
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean shouldSideBeRendered(IBlockAccess blockAccess, int iNeighborI, int iNeighborJ, int iNeighborK, int iSide) {
      return true;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean renderBlock(RenderBlocks renderBlocks, int i, int j, int k) {
      int iMetadata = renderBlocks.blockAccess.getBlockMetadata(i, j, k);
      int iFacing = this.getFacing(iMetadata);
      BlockModel transformedModel = model.makeTemporaryCopy();
      transformedModel.rotateAroundYToFacing(iFacing);
      renderBlocks.setUVRotateTop(this.convertFacingToTopTextureRotation(iFacing));
      renderBlocks.setUVRotateBottom(this.convertFacingToBottomTextureRotation(iFacing));
      this.renderingBase = true;
      boolean bReturnValue = transformedModel.renderAsBlock(renderBlocks, this, i, j, k);
      this.renderingBase = false;
      if (!this.getIsOpen(iMetadata)) {
         transformedModel = model.lid.makeTemporaryCopy();
         transformedModel.rotateAroundYToFacing(iFacing);
         transformedModel.renderAsBlock(renderBlocks, this, i, j, k);
      }

      renderBlocks.clearUVRotation();
      return false;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void renderBlockAsItem(RenderBlocks renderBlocks, int iItemDamage, float fBrightness) {
      model.renderAsItemBlock(renderBlocks, this, iItemDamage);
      model.lid.renderAsItemBlock(renderBlocks, this, iItemDamage);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int i, int j, int k) {
      int iMetadata = world.getBlockMetadata(i, j, k);
      AxisAlignedBB transformedBox;
      if (this.getIsOpen(iMetadata)) {
         transformedBox = model.selectionBoxOpen.makeTemporaryCopy();
      } else {
         transformedBox = model.selectionBox.makeTemporaryCopy();
      }

      transformedBox.rotateAroundYToFacing(this.getFacing(iMetadata));
      transformedBox.offset(i, j, k);
      return transformedBox;
   }
}
