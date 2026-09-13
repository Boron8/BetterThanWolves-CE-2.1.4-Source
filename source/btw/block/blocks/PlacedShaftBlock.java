package btw.block.blocks;

import btw.client.render.util.RenderUtils;
import btw.item.items.ToolItem;
import btw.world.util.BlockPos;
import btw.world.util.WorldUtils;
import java.util.Random;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.EntityFallingSand;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Item;
import net.minecraft.src.Material;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.World;

public class PlacedShaftBlock extends Block {
   protected static final double SHAFT_WIDTH = 0.125;
   protected static final double SHAFT_HALF_WIDTH = 0.0625;
   protected static final double SHAFT_HEIGHT = 0.75;
   protected static final double SELECTION_BOX_WIDTH = 0.25;
   protected static final double SELECTION_BOX_HALF_WIDTH = 0.125;
   protected static final double SELECTION_BOX_HEIGHT = 0.8125;
   private static final AxisAlignedBB boxShaft = new AxisAlignedBB(0.4375, 0.25, 0.4375, 0.5625, 1.0, 0.5625);
   private static final AxisAlignedBB boxShaftSupporting = new AxisAlignedBB(0.4375, 0.0, 0.4375, 0.5625, 1.0, 0.5625);
   private static final AxisAlignedBB boxSelection = new AxisAlignedBB(0.375, 0.1875, 0.375, 0.625, 1.0, 0.625);
   private static final AxisAlignedBB boxSelectionSupporting = new AxisAlignedBB(0.375, 0.0, 0.375, 0.625, 1.0, 0.625);

   public PlacedShaftBlock(int iBlockID) {
      super(iBlockID, Material.circuits);
      this.c(0.0F);
      this.b(0.0F);
      this.a(g);
      this.c("fcBlockShaft");
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
   public boolean canPlaceBlockOnSide(World world, int i, int j, int k, int iSide) {
      BlockPos targetPos = new BlockPos(i, j, k, Block.getOppositeFacing(iSide));
      if (WorldUtils.doesBlockHaveCenterHardpointToFacing(world, targetPos.x, targetPos.y, targetPos.z, iSide)) {
         int iTargetID = world.getBlockId(targetPos.x, targetPos.y, targetPos.z);
         if (this.canStickInBlockType(iTargetID)) {
            return true;
         }
      }

      return false;
   }

   @Override
   public int onBlockPlaced(World world, int i, int j, int k, int iFacing, float fClickX, float fClickY, float fClickZ, int iMetadata) {
      iMetadata = this.setFacing(iMetadata, Block.getOppositeFacing(iFacing));
      BlockPos anchorPos = new BlockPos(i, j, k, Block.getOppositeFacing(iFacing));
      int iAnchorID = world.getBlockId(anchorPos.x, anchorPos.y, anchorPos.z);
      Block anchorBlock = Block.blocksList[iAnchorID];
      if (anchorBlock != null) {
         world.playSoundEffect(
            i + 0.5F,
            j - 0.5F,
            k + 0.5F,
            anchorBlock.stepSound.getPlaceSound(),
            anchorBlock.stepSound.getPlaceVolume() / 2.0F,
            anchorBlock.stepSound.getStepPitch() * 0.8F
         );
         if (!world.isRemote) {
            anchorBlock.onPlayerWalksOnBlock(world, anchorPos.x, anchorPos.y, anchorPos.z, null);
         }
      }

      return iMetadata;
   }

   @Override
   public int idDropped(int iMetaData, Random random, int iFortuneModifier) {
      return Item.stick.itemID;
   }

   @Override
   public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k) {
      return null;
   }

   @Override
   public AxisAlignedBB getBlockBoundsFromPoolBasedOnState(IBlockAccess blockAccess, int i, int j, int k) {
      AxisAlignedBB transformedBox;
      if (this.isSupportingOtherBlock(blockAccess, i, j, k)) {
         transformedBox = boxShaftSupporting.makeTemporaryCopy();
      } else {
         transformedBox = boxShaft.makeTemporaryCopy();
      }

      transformedBox.tiltToFacingAlongY(this.getFacing(blockAccess, i, j, k));
      return transformedBox;
   }

   @Override
   public void onNeighborBlockChange(World world, int i, int j, int k, int iNeighborBlockID) {
      int iFacing = this.getFacing(world, i, j, k);
      if (!this.canPlaceBlockOnSide(world, i, j, k, Block.getOppositeFacing(iFacing))) {
         this.c(world, i, j, k, world.getBlockMetadata(i, j, k), 0);
         world.setBlockWithNotify(i, j, k, 0);
      }
   }

   @Override
   public boolean hasSmallCenterHardPointToFacing(IBlockAccess blockAccess, int i, int j, int k, int iFacing, boolean bIgnoreTransparency) {
      return iFacing == 1 && this.getFacing(blockAccess, i, j, k) == 0;
   }

   @Override
   public boolean canBeCrushedByFallingEntity(World world, int i, int j, int k, EntityFallingSand entity) {
      return true;
   }

   @Override
   public void onCrushedByFallingEntity(World world, int i, int j, int k, EntityFallingSand entity) {
      if (!world.isRemote) {
         this.c(world, i, j, k, world.getBlockMetadata(i, j, k), 0);
      }
   }

   @Override
   public int getFacing(int iMetadata) {
      return iMetadata & 7;
   }

   @Override
   public int setFacing(int iMetadata, int iFacing) {
      iMetadata &= -8;
      return iMetadata | iFacing;
   }

   @Override
   public boolean canRotateAroundBlockOnTurntableToFacing(World world, int i, int j, int k, int iFacing) {
      return iFacing == this.getFacing(world, i, j, k);
   }

   @Override
   public int getNewMetadataRotatedAroundBlockOnTurntableToFacing(World world, int i, int j, int k, int iInitialFacing, int iRotatedFacing) {
      int iOldMetadata = world.getBlockMetadata(i, j, k);
      return this.setFacing(iOldMetadata, iRotatedFacing);
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
      if (iToFacing == this.getFacing(world, i, j, k)) {
         this.c(world, i, j, k, world.getBlockMetadata(i, j, k), 0);
         world.setBlockWithNotify(i, j, k, 0);
      }
   }

   public boolean canStickInBlockType(int iBlockID) {
      Block block = Block.blocksList[iBlockID];
      return block != null && ((ToolItem)Item.shovelWood).isToolTypeEfficientVsBlockType(block);
   }

   public boolean isSupportingOtherBlock(IBlockAccess blockAccess, int i, int j, int k) {
      return this.getFacing(blockAccess, i, j, k) == 0 && WorldUtils.isBlockRestingOnThatBelow(blockAccess, i, j + 1, k);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public int idPicked(World world, int i, int j, int k) {
      return Item.stick.itemID;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean shouldSideBeRendered(IBlockAccess blockAccess, int iNeighborI, int iNeighborJ, int iNeighborK, int iSide) {
      BlockPos myPos = new BlockPos(iNeighborI, iNeighborJ, iNeighborK, Block.getOppositeFacing(iSide));
      int iFacing = this.getFacing(blockAccess, myPos.x, myPos.y, myPos.z);
      return iSide == iFacing ? !RenderUtils.shouldRenderNeighborFullFaceSide(blockAccess, iNeighborI, iNeighborJ, iNeighborK, iSide) : true;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean renderBlock(RenderBlocks renderBlocks, int i, int j, int k) {
      AxisAlignedBB transformedBox;
      if (this.isSupportingOtherBlock(renderBlocks.blockAccess, i, j, k)) {
         transformedBox = boxShaftSupporting.makeTemporaryCopy();
      } else {
         transformedBox = boxShaft.makeTemporaryCopy();
      }

      transformedBox.tiltToFacingAlongY(this.getFacing(renderBlocks.blockAccess, i, j, k));
      return transformedBox.renderAsBlock(renderBlocks, this, i, j, k);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int i, int j, int k) {
      AxisAlignedBB transformedBox;
      if (this.isSupportingOtherBlock(world, i, j, k)) {
         transformedBox = boxSelectionSupporting.makeTemporaryCopy();
      } else {
         transformedBox = boxSelection.makeTemporaryCopy();
      }

      transformedBox.tiltToFacingAlongY(this.getFacing(world, i, j, k));
      return transformedBox.offset(i, j, k);
   }
}
