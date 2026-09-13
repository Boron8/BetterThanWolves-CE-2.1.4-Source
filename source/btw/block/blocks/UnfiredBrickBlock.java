package btw.block.blocks;

import btw.block.BTWBlocks;
import btw.block.tileentity.UnfiredBrickTileEntity;
import btw.client.render.util.RenderUtils;
import btw.item.BTWItems;
import btw.util.MiscUtils;
import btw.world.util.WorldUtils;
import java.util.List;
import java.util.Random;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.BlockContainer;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityAmbientCreature;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Icon;
import net.minecraft.src.IconRegister;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;

public class UnfiredBrickBlock extends BlockContainer {
   public static final float BRICK_HEIGHT = 0.25F;
   public static final float BRICK_WIDTH = 0.375F;
   public static final float BRICK_HALF_WIDTH = 0.1875F;
   public static final float BRICK_LENGTH = 0.75F;
   public static final float BRICK_HALF_LENGTH = 0.375F;
   @Environment(EnvType.CLIENT)
   private Icon[] cookIcons;

   public UnfiredBrickBlock(int iBlockID) {
      super(iBlockID, Material.circuits);
      this.c(0.0F);
      this.setShovelsEffectiveOn(true);
      this.a(BTWBlocks.stepSoundSquish);
      this.c("fcBlockUnfiredBrick");
   }

   @Override
   public TileEntity createNewTileEntity(World world) {
      return new UnfiredBrickTileEntity();
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
      return Item.clay.itemID;
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
      return WorldUtils.doesBlockHaveLargeCenterHardpointToFacing(world, i, j - 1, k, 1, true) && world.getBlockId(i, j - 1, k) != Block.leaves.blockID;
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
   public void onEntityCollidedWithBlock(World world, int i, int j, int k, Entity entity) {
      if (!world.isRemote && !entity.isDead && entity instanceof EntityLiving && !(entity instanceof EntityAmbientCreature)) {
         List collisionList = world.getEntitiesWithinAABB(EntityLiving.class, this.getVisualBB(world, i, j, k));
         if (collisionList != null && collisionList.size() > 0) {
            world.playSoundEffect(
               i + 0.5, j + 0.5, k + 0.5, this.stepSound.getStepSound(), (this.stepSound.getStepVolume() + 1.0F) / 2.0F, this.stepSound.getStepPitch() * 0.8F
            );
            this.c(world, i, j, k, world.getBlockMetadata(i, j, k), 0);
            world.setBlockWithNotify(i, j, k, 0);
         }
      }
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

   public void onFinishedCooking(World world, int i, int j, int k) {
      int iMetadata = world.getBlockMetadata(i, j, k) & 1;
      world.setBlockAndMetadataWithNotify(i, j, k, BTWBlocks.placedBrick.blockID, iMetadata);
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

   public void setCookLevel(World world, int i, int j, int k, int iCookLevel) {
      int iMetadata = this.setCookLevel(world.getBlockMetadata(i, j, k), iCookLevel);
      world.setBlockMetadataWithNotify(i, j, k, iMetadata);
   }

   public int setCookLevel(int iMetadata, int iCookLevel) {
      iMetadata &= 1;
      return iMetadata | iCookLevel << 1;
   }

   public int getCookLevel(IBlockAccess blockAccess, int i, int j, int k) {
      return this.getCookLevel(blockAccess.getBlockMetadata(i, j, k));
   }

   public int getCookLevel(int iMetadata) {
      return iMetadata >> 1;
   }

   public AxisAlignedBB getVisualBB(IBlockAccess blockAccess, int i, int j, int k) {
      return this.getIsIAligned(blockAccess, i, j, k)
         ? AxisAlignedBB.getAABBPool().getAABB(i + 0.125F, j, k + 0.3125F, i + 0.875F, j + 0.25F, k + 0.6875F)
         : AxisAlignedBB.getAABBPool().getAABB(i + 0.3125F, j, k + 0.125F, i + 0.6875F, j + 0.25F, k + 0.875F);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public int idPicked(World world, int x, int y, int z) {
      return BTWItems.unfiredBrick.itemID;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void registerIcons(IconRegister register) {
      super.a(register);
      this.cookIcons = new Icon[7];

      for (int iTempIndex = 0; iTempIndex < 7; iTempIndex++) {
         this.cookIcons[iTempIndex] = register.registerIcon("fcOverlayUnfiredBrick_" + (iTempIndex + 1));
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean shouldSideBeRendered(IBlockAccess blockAccess, int iNeighborI, int iNeighborJ, int iNeighborK, int iSide) {
      return iSide == 0 ? RenderUtils.shouldRenderNeighborFullFaceSide(blockAccess, iNeighborI, iNeighborJ, iNeighborK, iSide) : true;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void renderBlockSecondPass(RenderBlocks renderBlocks, int i, int j, int k, boolean bFirstPassResult) {
      if (bFirstPassResult) {
         IBlockAccess blockAccess = renderBlocks.blockAccess;
         int iCookLevel = this.getCookLevel(blockAccess, i, j, k);
         int iBlockBelowID = blockAccess.getBlockId(i, j - 1, k);
         if (iBlockBelowID == BTWBlocks.kiln.blockID) {
            int iKilnCookLevel = BTWBlocks.kiln.getCookCounter(blockAccess, i, j - 1, k) / 2;
            if (iKilnCookLevel > iCookLevel) {
               iCookLevel = iKilnCookLevel;
            }
         }

         if (iCookLevel > 0 && iCookLevel <= 7) {
            this.renderBlockWithTexture(renderBlocks, i, j, k, this.cookIcons[iCookLevel - 1]);
         }
      }
   }
}
