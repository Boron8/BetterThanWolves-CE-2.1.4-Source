package btw.block.blocks;

import btw.BTWMod;
import btw.block.model.AnvilModel;
import btw.block.model.BlockModel;
import btw.inventory.BTWContainers;
import btw.inventory.container.WorkbenchContainer;
import btw.item.BTWItems;
import btw.util.MiscUtils;
import btw.world.util.WorldUtils;
import java.util.Random;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EnchantmentHelper;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EntityPlayerMP;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.IconRegister;
import net.minecraft.src.Material;
import net.minecraft.src.MovingObjectPosition;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.Vec3;
import net.minecraft.src.World;

public class AnvilBlock extends Block {
   AnvilModel model = new AnvilModel();

   public AnvilBlock(int iBlockID) {
      super(iBlockID, Material.anvil);
      this.c(50.0F);
      this.b(10.0F);
      this.setPicksEffectiveOn();
      this.k(0);
      this.a(q);
      this.c("anvil");
      this.a(CreativeTabs.tabDecorations);
   }

   @Override
   public boolean renderAsNormalBlock() {
      return false;
   }

   @Override
   public boolean isOpaqueCube() {
      return false;
   }

   @Override
   public int quantityDropped(Random rand) {
      return 7;
   }

   @Override
   public int idDropped(int iMetaData, Random random, int iFortuneModifier) {
      return BTWItems.metalFragment.itemID;
   }

   @Override
   public void harvestBlock(World world, EntityPlayer player, int i, int j, int k, int iMetadata) {
      if (!this.canSilkHarvest(iMetadata) || !EnchantmentHelper.getSilkTouchModifier(player)) {
         world.playAuxSFX(2272, i, j, k, this.blockID + (iMetadata << 12));
      }

      super.harvestBlock(world, player, i, j, k, iMetadata);
   }

   @Override
   public void onBlockDestroyedWithImproperTool(World world, EntityPlayer player, int i, int j, int k, int iMetadata) {
      this.c(world, i, j, k, iMetadata, 0);
   }

   @Override
   protected boolean canSilkHarvest(int iMetadata) {
      return true;
   }

   @Override
   public boolean canPlaceBlockAt(World world, int i, int j, int k) {
      return !WorldUtils.doesBlockHaveLargeCenterHardpointToFacing(world, i, j - 1, k, 1, true) ? false : super.canPlaceBlockAt(world, i, j, k);
   }

   @Override
   public int onBlockPlaced(World world, int i, int j, int k, int iFacing, float fClickX, float fClickY, float fClickZ, int iMetadata) {
      return this.setFacing(iMetadata, iFacing);
   }

   @Override
   public int preBlockPlacedBy(World world, int i, int j, int k, int iMetadata, EntityLiving entityBy) {
      int iFacing = MiscUtils.convertOrientationToFlatBlockFacing(entityBy);
      return this.setFacing(iMetadata, iFacing);
   }

   @Override
   public void onNeighborBlockChange(World world, int i, int j, int k, int iBlockID) {
      if (!WorldUtils.doesBlockHaveLargeCenterHardpointToFacing(world, i, j - 1, k, 1, true)) {
         int iMetadata = world.getBlockMetadata(i, j, k);
         world.playAuxSFX(2272, i, j, k, this.blockID + (iMetadata << 12));
         this.c(world, i, j, k, iMetadata, 0);
         world.setBlockToAir(i, j, k);
      }
   }

   @Override
   public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer player, int iFacing, float fXClick, float fYClick, float fZClick) {
      if (!world.isRemote && !WorldUtils.doesBlockHaveLargeCenterHardpointToFacing(world, i, j + 1, k, 0) && player instanceof EntityPlayerMP) {
         WorkbenchContainer container = new WorkbenchContainer(player.inventory, world, i, j, k);
         BTWMod.serverOpenCustomInterface((EntityPlayerMP)player, container, BTWContainers.anvilContainerID);
      }

      return true;
   }

   @Override
   public MovingObjectPosition collisionRayTrace(World world, int i, int j, int k, Vec3 startRay, Vec3 endRay) {
      int iFacing = this.getFacing(world, i, j, k);
      BlockModel transformedModel = this.model.makeTemporaryCopy();
      transformedModel.rotateAroundYToFacing(iFacing);
      return transformedModel.collisionRayTrace(world, i, j, k, startRay, endRay);
   }

   @Override
   public int getFacing(int iMetadata) {
      int iOrientation = iMetadata & 3;
      if ((iOrientation & 1) == 0) {
         return iOrientation == 0 ? 3 : 2;
      } else {
         return iOrientation == 1 ? 4 : 5;
      }
   }

   @Override
   public int setFacing(int iMetadata, int iFacing) {
      int iOrientation;
      if (iFacing == 2) {
         iOrientation = 2;
      } else if (iFacing == 3) {
         iOrientation = 0;
      } else if (iFacing == 4) {
         iOrientation = 1;
      } else {
         iOrientation = 3;
      }

      iMetadata &= -4;
      return iMetadata | iOrientation;
   }

   @Override
   public boolean canRotateOnTurntable(IBlockAccess blockAccess, int i, int j, int k) {
      return true;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void registerIcons(IconRegister register) {
      this.blockIcon = register.registerIcon("anvil_base");
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean renderBlock(RenderBlocks renderer, int i, int j, int k) {
      int iMetadata = renderer.blockAccess.getBlockMetadata(i, j, k);
      int iFacing = this.getFacing(iMetadata);
      BlockModel transformedModel = this.model.makeTemporaryCopy();
      transformedModel.rotateAroundYToFacing(iFacing);
      renderer.setUVRotateTop(this.convertFacingToTopTextureRotation(iFacing));
      renderer.setUVRotateBottom(this.convertFacingToBottomTextureRotation(iFacing));
      boolean bReturnValue = transformedModel.renderAsBlock(renderer, this, i, j, k);
      renderer.clearUVRotation();
      return bReturnValue;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean shouldSideBeRendered(IBlockAccess blockAccess, int iNeighborI, int iNeighborJ, int iNeighborK, int iSide) {
      return this.currentBlockRenderer.shouldSideBeRenderedBasedOnCurrentBounds(iNeighborI, iNeighborJ, iNeighborK, iSide);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void renderBlockAsItem(RenderBlocks renderer, int iItemDamage, float fBrightness) {
      this.model.renderAsItemBlock(renderer, this, iItemDamage);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int i, int j, int k) {
      AxisAlignedBB transformedBox = this.model.boxSelection.makeTemporaryCopy();
      transformedBox.rotateAroundYToFacing(this.getFacing(world, i, j, k));
      transformedBox.offset(i, j, k);
      return transformedBox;
   }
}
