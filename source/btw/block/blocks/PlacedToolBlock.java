package btw.block.blocks;

import btw.block.tileentity.PlacedToolTileEntity;
import btw.item.PlaceableAsItem;
import btw.item.util.ItemUtils;
import btw.world.util.BlockPos;
import btw.world.util.WorldUtils;
import java.util.Random;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.BlockContainer;
import net.minecraft.src.EntityFallingSand;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.IconRegister;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.MathHelper;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;

public class PlacedToolBlock extends BlockContainer {
   private static final double BOUNDING_THICKNESS = 0.125;
   private static final double BOUNDING_HALF_THICKNESS = 0.0625;

   public PlacedToolBlock(int iBlockID) {
      super(iBlockID, Material.circuits);
      this.c(0.05F);
      this.a(g);
      this.c("fcBlockToolPlaced");
   }

   @Override
   public boolean canPlaceBlockAt(World world, int x, int y, int z) {
      return world.isAirBlock(x, y, z);
   }

   @Override
   public TileEntity createNewTileEntity(World world) {
      return new PlacedToolTileEntity();
   }

   @Override
   public void breakBlock(World world, int i, int j, int k, int iBlockID, int iMetadata) {
      PlacedToolTileEntity tileEntity = (PlacedToolTileEntity)world.getBlockTileEntity(i, j, k);
      tileEntity.ejectContents();
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
   public int idDropped(int iMetadata, Random rand, int iFortuneModifier) {
      return 0;
   }

   @Override
   protected boolean canSilkHarvest() {
      return false;
   }

   @Override
   public int onBlockPlaced(World world, int i, int j, int k, int iFacing, float fClickX, float fClickY, float fClickZ, int iMetadata) {
      return this.setFacing(iMetadata, iFacing);
   }

   @Override
   public void onNeighborBlockChange(World world, int i, int j, int k, int iBlockID) {
      int iBlockFacing = this.getAttachedToFacing(world, i, j, k);
      BlockPos attachedPos = new BlockPos(i, j, k, iBlockFacing);
      if (!WorldUtils.doesBlockHaveCenterHardpointToFacing(world, attachedPos.x, attachedPos.y, attachedPos.z, Block.getOppositeFacing(iBlockFacing), true)) {
         world.setBlockWithNotify(i, j, k, 0);
      }
   }

   @Override
   public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer player, int iFacing, float fXClick, float fYClick, float fZClick) {
      PlacedToolTileEntity tileEntity = (PlacedToolTileEntity)world.getBlockTileEntity(i, j, k);
      ItemStack cookStack = tileEntity.getToolStack();
      if (cookStack != null) {
         ItemUtils.givePlayerStackOrEjectFavorEmptyHand(player, cookStack, i, j, k);
         tileEntity.setToolStack(null);
         world.setBlockWithNotify(i, j, k, 0);
         return true;
      } else {
         return false;
      }
   }

   @Override
   public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k) {
      return null;
   }

   @Override
   public AxisAlignedBB getBlockBoundsFromPoolBasedOnState(IBlockAccess blockAccess, int i, int j, int k) {
      int iMetadata = blockAccess.getBlockMetadata(i, j, k);
      int iFacing = this.getFacing(iMetadata);
      int iLevel = this.getVerticalOrientation(iMetadata);
      float fHeight = 0.75F;
      float fWidth = 0.75F;
      PlacedToolTileEntity tileEntity = (PlacedToolTileEntity)blockAccess.getBlockTileEntity(i, j, k);
      if (tileEntity != null) {
         ItemStack toolStack = tileEntity.getToolStack();
         if (toolStack != null) {
            fHeight = ((PlaceableAsItem)toolStack.getItem()).getBlockBoundingBoxHeight();
            fWidth = ((PlaceableAsItem)toolStack.getItem()).getBlockBoundingBoxWidth();
         }
      }

      double fXMin = 0.5F - fWidth / 2.0F;
      double fXMax = 0.5F + fWidth / 2.0F;
      double fZMin = fXMin;
      double fZMax = fXMax;
      double fYMin = fXMin;
      double fYMax = fXMax;
      if (iFacing < 4) {
         fXMin = 0.4375;
         fXMax = 0.5625;
      } else {
         fZMin = 0.4375;
         fZMax = 0.5625;
      }

      if (iLevel == 0) {
         fYMin = 0.0;
         fYMax = fHeight;
      } else if (iLevel == 1) {
         fYMin = 1.0F - fHeight;
         fYMax = 1.0;
      } else if (iFacing == 2) {
         fZMin = 0.0;
         fZMax = fHeight;
      } else if (iFacing == 3) {
         fZMin = 1.0F - fHeight;
         fZMax = 1.0;
      } else if (iFacing == 4) {
         fXMin = 0.0;
         fXMax = fHeight;
      } else if (iFacing == 5) {
         fXMin = 1.0F - fHeight;
         fXMax = 1.0;
      }

      return AxisAlignedBB.getAABBPool().getAABB(fXMin, fYMin, fZMin, fXMax, fYMax, fZMax);
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
   public boolean canRotateAroundBlockOnTurntableToFacing(World world, int i, int j, int k, int iFacing) {
      int iMetadata = world.getBlockMetadata(i, j, k);
      return this.getVerticalOrientation(iMetadata) == 2 ? iFacing == this.getFacing(iMetadata) : false;
   }

   @Override
   public boolean onRotatedAroundBlockOnTurntableToFacing(World world, int i, int j, int k, int iFacing) {
      world.setBlockToAir(i, j, k);
      return false;
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
   public void onNeighborDisrupted(World world, int i, int j, int k, int iToFacing) {
      if (iToFacing == this.getAttachedToFacing(world, i, j, k)) {
         world.setBlockWithNotify(i, j, k, 0);
      }
   }

   public int getVerticalOrientation(IBlockAccess blockAccess, int i, int j, int k) {
      return this.getVerticalOrientation(blockAccess.getBlockMetadata(i, j, k));
   }

   public int getVerticalOrientation(int iMetadata) {
      return (iMetadata & 12) >> 2;
   }

   public void setVerticalOrientation(World world, int i, int j, int k, int iLevel) {
      int iMetadata = this.setVerticalOrientation(world.getBlockMetadata(i, j, k), iLevel);
      world.setBlockMetadataWithNotify(i, j, k, iMetadata);
   }

   public int setVerticalOrientation(int iMetadata, int iLevel) {
      iMetadata &= -13;
      return iMetadata | iLevel << 2;
   }

   protected int getAttachedToFacing(IBlockAccess blockAccess, int i, int j, int k) {
      int iFacing = this.getVerticalOrientation(blockAccess, i, j, k);
      if (iFacing >= 2) {
         iFacing = this.getFacing(blockAccess, i, j, k);
      }

      return iFacing;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void registerIcons(IconRegister register) {
      this.blockIcon = register.registerIcon("stone");
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean renderBlock(RenderBlocks renderBlocks, int i, int j, int k) {
      return false;
   }
}
