package btw.block.blocks;

import btw.client.render.util.RenderUtils;
import btw.util.MiscUtils;
import btw.world.util.WorldUtils;
import java.util.Random;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.World;

public class FiredBrickBlock extends Block {
   public static final double BRICK_HEIGHT = 0.25;
   public static final double BRICK_WIDTH = 0.375;
   public static final double BRICK_HALF_WIDTH = 0.1875;
   public static final double BRICK_LENGTH = 0.75;
   public static final double BRICK_HALF_LENGTH = 0.375;

   public FiredBrickBlock(int iBlockID) {
      super(iBlockID, Material.circuits);
      this.c(0.0F);
      this.setPicksEffectiveOn(true);
      this.a(j);
      this.c("fcBlockCookedBrick");
   }

   @Override
   public int onBlockPlaced(World world, int i, int j, int k, int iFacing, float fClickX, float fClickY, float fClickZ, int iMetadata) {
      return this.setIAligned(iMetadata, this.isFacingIAligned(iFacing));
   }

   @Override
   public void onBlockPlacedBy(World world, int i, int j, int k, EntityLiving entityLiving, ItemStack stack) {
      int iFacing = MiscUtils.convertOrientationToFlatBlockFacingReversed(entityLiving);
      this.setIAligned(world, i, j, k, this.isFacingIAligned(iFacing));
   }

   @Override
   public int idDropped(int iMetadata, Random random, int iFortuneModifier) {
      return Item.brick.itemID;
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
   public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k) {
      return null;
   }

   @Override
   public AxisAlignedBB getBlockBoundsFromPoolBasedOnState(IBlockAccess blockAccess, int i, int j, int k) {
      return this.getIsIAligned(blockAccess, i, j, k)
         ? AxisAlignedBB.getAABBPool().getAABB(0.125, 0.0, 0.3125, 0.875, 0.25, 0.6875)
         : AxisAlignedBB.getAABBPool().getAABB(0.3125, 0.0, 0.125, 0.6875, 0.25, 0.875);
   }

   @Override
   public boolean canPlaceBlockAt(World world, int i, int j, int k) {
      return WorldUtils.doesBlockHaveLargeCenterHardpointToFacing(world, i, j - 1, k, 1, true);
   }

   @Override
   public void onNeighborBlockChange(World world, int i, int j, int k, int iBlockID) {
      if (!WorldUtils.doesBlockHaveLargeCenterHardpointToFacing(world, i, j - 1, k, 1, true)) {
         this.c(world, i, j, k, world.getBlockMetadata(i, j, k), 0);
         world.setBlockWithNotify(i, j, k, 0);
      }
   }

   @Override
   public boolean canGroundCoverRestOnBlock(World world, int i, int j, int k) {
      return world.doesBlockHaveSolidTopSurface(i, j - 1, k);
   }

   @Override
   public float groundCoverRestingOnVisualOffset(IBlockAccess blockAccess, int i, int j, int k) {
      return -1.0F;
   }

   @Override
   public int getFacing(int iMetadata) {
      return this.getIsIAligned(iMetadata) ? 4 : 2;
   }

   @Override
   public int setFacing(int iMetadata, int iFacing) {
      return this.setIAligned(iMetadata, this.isFacingIAligned(iFacing));
   }

   @Override
   public boolean canRotateOnTurntable(IBlockAccess blockAccess, int i, int j, int k) {
      return true;
   }

   @Override
   public int rotateMetadataAroundJAxis(int iMetadata, boolean bReverse) {
      return this.setIAligned(iMetadata, !this.getIsIAligned(iMetadata));
   }

   public void setIAligned(World world, int i, int j, int k, boolean bIAligned) {
      int iMetadata = this.setIAligned(world.getBlockMetadata(i, j, k), bIAligned);
      world.setBlockMetadataWithNotify(i, j, k, iMetadata);
   }

   public int setIAligned(int iMetadata, boolean bIAligned) {
      if (bIAligned) {
         iMetadata |= 1;
      } else {
         iMetadata &= -2;
      }

      return iMetadata;
   }

   public boolean getIsIAligned(IBlockAccess blockAccess, int i, int j, int k) {
      return this.getIsIAligned(blockAccess.getBlockMetadata(i, j, k));
   }

   public boolean getIsIAligned(int iMetadata) {
      return (iMetadata & 1) != 0;
   }

   public boolean isFacingIAligned(int iFacing) {
      return iFacing >= 4;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean shouldSideBeRendered(IBlockAccess blockAccess, int iNeighborI, int iNeighborJ, int iNeighborK, int iSide) {
      return iSide == 0 ? RenderUtils.shouldRenderNeighborFullFaceSide(blockAccess, iNeighborI, iNeighborJ, iNeighborK, iSide) : true;
   }
}
