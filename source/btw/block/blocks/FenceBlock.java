package btw.block.blocks;

import btw.block.model.BlockModel;
import btw.block.model.FenceModel;
import btw.world.util.BlockPos;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.Entity;
import net.minecraft.src.Facing;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.IconRegister;
import net.minecraft.src.Material;
import net.minecraft.src.MovingObjectPosition;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.Vec3;
import net.minecraft.src.World;

public class FenceBlock extends Block {
   protected static final FenceModel model = new FenceModel();
   protected final String iconName;

   public FenceBlock(int iBlockID, String sIconName, Material material) {
      super(iBlockID, material);
      this.iconName = sIconName;
      this.initBlockBounds(0.0, 0.0, 0.0, 1.0, 1.5, 1.0);
      this.a(CreativeTabs.tabDecorations);
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
   public boolean getBlocksMovement(IBlockAccess blockAccess, int i, int j, int k) {
      return false;
   }

   @Override
   public MovingObjectPosition collisionRayTrace(World world, int i, int j, int k, Vec3 startRay, Vec3 endRay) {
      BlockModel tempModel = this.assembleTemporaryModel(world, i, j, k);
      return tempModel.collisionRayTrace(world, i, j, k, startRay, endRay);
   }

   @Override
   public void addCollisionBoxesToList(World world, int i, int j, int k, AxisAlignedBB boundingBox, List list, Entity entity) {
      AxisAlignedBB tempBox = model.boxCollisionCenter.makeTemporaryCopy();
      tempBox.offset(i, j, k);
      if (tempBox.intersectsWith(boundingBox)) {
         list.add(tempBox);
      }

      for (int iTempFacing = 2; iTempFacing <= 5; iTempFacing++) {
         if (canConnectToBlockToFacing(world, i, j, k, iTempFacing)) {
            tempBox = model.boxCollisionStruts.makeTemporaryCopy();
            tempBox.rotateAroundYToFacing(iTempFacing);
            tempBox.offset(i, j, k);
            if (tempBox.intersectsWith(boundingBox)) {
               list.add(tempBox);
            }
         }
      }
   }

   @Override
   public AxisAlignedBB getBlockBoundsFromPoolBasedOnState(IBlockAccess blockAccess, int i, int j, int k) {
      AxisAlignedBB fenceBox = AxisAlignedBB.getAABBPool().getAABB(0.375, 0.0, 0.375, 0.625, 1.0, 0.625);
      if (canConnectToBlockToFacing(blockAccess, i, j, k, 2)) {
         fenceBox.minZ = 0.0;
      }

      if (canConnectToBlockToFacing(blockAccess, i, j, k, 3)) {
         fenceBox.maxZ = 1.0;
      }

      if (canConnectToBlockToFacing(blockAccess, i, j, k, 4)) {
         fenceBox.minX = 0.0;
      }

      if (canConnectToBlockToFacing(blockAccess, i, j, k, 5)) {
         fenceBox.maxX = 1.0;
      }

      return fenceBox;
   }

   @Override
   public int getWeightOnPathBlocked(IBlockAccess blockAccess, int i, int j, int k) {
      return -3;
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
   public boolean hasCenterHardPointToFacing(IBlockAccess blockAccess, int i, int j, int k, int iFacing, boolean bIgnoreTransparency) {
      return iFacing == 0 || iFacing == 1;
   }

   @Override
   public float mobSpawnOnVerticalOffset(World world, int i, int j, int k) {
      return 0.5F;
   }

   @Override
   public boolean isFence(int metadata) {
      return true;
   }

   public static boolean canConnectToBlockToFacing(IBlockAccess blockAccess, int x, int y, int z, int facing) {
      BlockPos blockPos = new BlockPos(x, y, z, facing);
      Block block = Block.blocksList[blockAccess.getBlockId(blockPos.x, blockPos.y, blockPos.z)];
      return block != null ? block.shouldFenceConnectToThisBlockToFacing(blockAccess, blockPos.x, blockPos.y, blockPos.z, Facing.oppositeSide[facing]) : false;
   }

   protected BlockModel assembleTemporaryModel(IBlockAccess blockAccess, int i, int j, int k) {
      BlockModel tempModel = model.makeTemporaryCopy();

      for (int iTempFacing = 2; iTempFacing <= 5; iTempFacing++) {
         if (canConnectToBlockToFacing(blockAccess, i, j, k, iTempFacing)) {
            BlockModel tempSupportsModel = model.modelStruts.makeTemporaryCopy();
            tempSupportsModel.rotateAroundYToFacing(iTempFacing);
            tempSupportsModel.makeTemporaryCopyOfPrimitiveList(tempModel);
         }
      }

      return tempModel;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void registerIcons(IconRegister par1IconRegister) {
      this.blockIcon = par1IconRegister.registerIcon(this.iconName);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean shouldSideBeRendered(IBlockAccess blockAccess, int iNeighborI, int iNeighborJ, int iNeighborK, int iSide) {
      return this.currentBlockRenderer.shouldSideBeRenderedBasedOnCurrentBounds(iNeighborI, iNeighborJ, iNeighborK, iSide);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean renderBlock(RenderBlocks renderBlocks, int i, int j, int k) {
      BlockModel tempModel = this.assembleTemporaryModel(renderBlocks.blockAccess, i, j, k);
      return tempModel.renderAsBlock(renderBlocks, this, i, j, k);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void renderBlockAsItem(RenderBlocks renderBlocks, int iItemDamage, float fBrightness) {
      model.renderAsItemBlock(renderBlocks, this, iItemDamage);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, MovingObjectPosition rayTraceHit) {
      AxisAlignedBB tempBox = model.boxBoundsCenter.makeTemporaryCopy();
      return tempBox.offset(rayTraceHit.blockX, rayTraceHit.blockY, rayTraceHit.blockZ);
   }
}
