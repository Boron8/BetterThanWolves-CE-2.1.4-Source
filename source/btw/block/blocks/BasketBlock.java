package btw.block.blocks;

import btw.block.BTWBlocks;
import btw.block.model.BlockModel;
import btw.block.tileentity.BasketTileEntity;
import btw.block.util.Flammability;
import btw.util.MiscUtils;
import btw.world.util.WorldUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.Block;
import net.minecraft.src.BlockContainer;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EntityFallingSand;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.MathHelper;
import net.minecraft.src.Vec3;
import net.minecraft.src.World;

public abstract class BasketBlock extends BlockContainer {
   @Environment(EnvType.CLIENT)
   public boolean renderingOpenLid = false;
   @Environment(EnvType.CLIENT)
   public int openLidBrightness;

   protected BasketBlock(int iBlockID) {
      super(iBlockID, BTWBlocks.basketMaterial);
      this.c(0.05F);
      this.setBuoyant();
      this.setFireProperties(Flammability.WICKER);
      this.a(i);
      this.a(CreativeTabs.tabDecorations);
   }

   @Override
   public void breakBlock(World world, int i, int j, int k, int iBlockID, int iMetadata) {
      BasketTileEntity tileEntity = (BasketTileEntity)world.getBlockTileEntity(i, j, k);
      if (tileEntity != null) {
         tileEntity.ejectContents();
      }

      super.breakBlock(world, i, j, k, iBlockID, iMetadata);
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
   public int preBlockPlacedBy(World world, int i, int j, int k, int iMetadata, EntityLiving entityBy) {
      int iFacing = MiscUtils.convertOrientationToFlatBlockFacingReversed(entityBy);
      return this.setFacing(iMetadata, iFacing);
   }

   @Override
   public boolean canPlaceBlockAt(World world, int i, int j, int k) {
      return !WorldUtils.doesBlockHaveLargeCenterHardpointToFacing(world, i, j - 1, k, 1, true) ? false : super.c(world, i, j, k);
   }

   @Override
   public void onNeighborBlockChange(World world, int i, int j, int k, int iBlockID) {
      if (!WorldUtils.doesBlockHaveLargeCenterHardpointToFacing(world, i, j - 1, k, 1, true)) {
         this.c(world, i, j, k, world.getBlockMetadata(i, j, k), 0);
         world.setBlockToAir(i, j, k);
      }
   }

   @Override
   public boolean canBeCrushedByFallingEntity(World world, int i, int j, int k, EntityFallingSand entity) {
      return true;
   }

   @Override
   public int getFacing(int iMetadata) {
      return (iMetadata & 3) + 2;
   }

   @Override
   public int setFacing(int iMetadata, int iFacing) {
      iMetadata &= -4;
      return iMetadata | MathHelper.clamp_int(iFacing, 2, 5) - 2;
   }

   @Override
   public boolean canRotateOnTurntable(IBlockAccess iBlockAccess, int i, int j, int k) {
      return true;
   }

   @Override
   public boolean getPreventsFluidFlow(World world, int i, int j, int k, Block fluidBlock) {
      return false;
   }

   public void setHasContents(World world, int i, int j, int k, boolean bHasContents) {
      int iMetadata = this.setHasContents(world.getBlockMetadata(i, j, k), bHasContents);
      world.setBlockMetadataWithNotify(i, j, k, iMetadata);
   }

   public int setHasContents(int iMetadata, boolean bHasContents) {
      if (bHasContents) {
         iMetadata |= 4;
      } else {
         iMetadata &= -5;
      }

      return iMetadata;
   }

   public boolean getHasContents(IBlockAccess blockAccess, int i, int j, int k) {
      return this.getHasContents(blockAccess.getBlockMetadata(i, j, k));
   }

   public boolean getHasContents(int iMetadata) {
      return (iMetadata & 4) != 0;
   }

   public void setIsOpen(World world, int i, int j, int k, boolean bOpen) {
      int iMetadata = this.setIsOpen(world.getBlockMetadata(i, j, k), bOpen);
      world.setBlockMetadataWithNotify(i, j, k, iMetadata);
   }

   public int setIsOpen(int iMetadata, boolean bOpen) {
      if (bOpen) {
         iMetadata |= 8;
      } else {
         iMetadata &= -9;
      }

      return iMetadata;
   }

   public boolean getIsOpen(IBlockAccess blockAccess, int i, int j, int k) {
      return this.getIsOpen(blockAccess.getBlockMetadata(i, j, k));
   }

   public boolean getIsOpen(int iMetadata) {
      return (iMetadata & 8) != 0;
   }

   public abstract BlockModel getLidModel(int var1);

   public abstract Vec3 getLidRotationPoint();

   @Environment(EnvType.CLIENT)
   @Override
   public int getMixedBrightnessForBlock(IBlockAccess par1IBlockAccess, int par2, int par3, int par4) {
      return this.renderingOpenLid ? this.openLidBrightness : super.e(par1IBlockAccess, par2, par3, par4);
   }
}
