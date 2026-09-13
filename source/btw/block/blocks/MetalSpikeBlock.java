package btw.block.blocks;

import btw.block.model.BlockModel;
import btw.block.model.MetalSpikeModel;
import btw.world.util.BlockPos;
import btw.world.util.WorldUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Material;
import net.minecraft.src.MovingObjectPosition;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.Vec3;
import net.minecraft.src.World;

public class MetalSpikeBlock extends Block {
   protected static MetalSpikeModel model = new MetalSpikeModel();

   public MetalSpikeBlock(int iBlockID) {
      super(iBlockID, Material.iron);
      this.c(2.0F);
      this.setPicksEffectiveOn();
      this.initBlockBounds(model.boxCollisionCenter);
      this.a(k);
      this.c("fcBlockSpikeIron");
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
   public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k) {
      int iFacing = this.getFacing(world, i, j, k);
      if (iFacing >= 2) {
         AxisAlignedBB tempBox = model.boxCollisionStrut.makeTemporaryCopy();
         tempBox.rotateAroundYToFacing(iFacing);
         tempBox.offset(i, j, k);
         return tempBox;
      } else {
         return model.boxCollisionCenter.makeTemporaryCopy().offset(i, j, k);
      }
   }

   @Override
   public MovingObjectPosition collisionRayTrace(World world, int i, int j, int k, Vec3 startRay, Vec3 endRay) {
      BlockModel m_modelTransformed = new BlockModel();
      m_modelTransformed.addPrimitive(model.boxCollisionCenter.makeTemporaryCopy());
      BlockModel transformedSupportsModel = this.getSideSupportsTemporaryModel(world, i, j, k);
      if (transformedSupportsModel != null) {
         transformedSupportsModel.makeTemporaryCopyOfPrimitiveList(m_modelTransformed);
      }

      return m_modelTransformed.collisionRayTrace(world, i, j, k, startRay, endRay);
   }

   @Override
   public boolean canPlaceBlockOnSide(World world, int i, int j, int k, int iSide) {
      return this.canConnectToBlockToFacing(world, i, j, k, Block.getOppositeFacing(iSide));
   }

   @Override
   public int onBlockPlaced(World world, int i, int j, int k, int iFacing, float fClickX, float fClickY, float fClickZ, int iMetadata) {
      return this.setFacing(iMetadata, Block.getOppositeFacing(iFacing));
   }

   @Override
   public void onNeighborBlockChange(World world, int i, int j, int k, int iNeighborBlockID) {
      super.onNeighborBlockChange(world, i, j, k, iNeighborBlockID);
      if (!this.canConnectToBlockToFacing(world, i, j, k, this.getFacing(world, i, j, k))) {
         this.c(world, i, j, k, world.getBlockMetadata(i, j, k), 0);
         world.setBlockToAir(i, j, k);
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
   public boolean hasSmallCenterHardPointToFacing(IBlockAccess blockAccess, int i, int j, int k, int iFacing, boolean bIgnoreTransparency) {
      return iFacing < 2;
   }

   @Override
   public boolean canGroundCoverRestOnBlock(World world, int i, int j, int k) {
      return world.doesBlockHaveSolidTopSurface(i, j - 1, k);
   }

   @Override
   public float groundCoverRestingOnVisualOffset(IBlockAccess blockAccess, int i, int j, int k) {
      return -1.0F;
   }

   protected boolean canConnectToBlockToFacing(IBlockAccess blockAccess, int i, int j, int k, int iFacing) {
      BlockPos targetPos = new BlockPos(i, j, k, iFacing);
      return WorldUtils.doesBlockHaveSmallCenterHardpointToFacing(blockAccess, targetPos.x, targetPos.y, targetPos.z, Block.getOppositeFacing(iFacing))
         || blockAccess.getBlockId(targetPos.x, targetPos.y, targetPos.z) == this.blockID;
   }

   protected boolean isConnectedSpikeToFacing(IBlockAccess blockAccess, int i, int j, int k, int iFacing) {
      BlockPos targetPos = new BlockPos(i, j, k, iFacing);
      return blockAccess.getBlockId(targetPos.x, targetPos.y, targetPos.z) == this.blockID
         && this.getFacing(blockAccess, targetPos.x, targetPos.y, targetPos.z) == Block.getOppositeFacing(iFacing);
   }

   BlockModel getSideSupportsTemporaryModel(IBlockAccess blockAccess, int i, int j, int k) {
      BlockModel supportsModel = null;
      int iBlockFacing = this.getFacing(blockAccess, i, j, k);

      for (int iTempFacing = 2; iTempFacing <= 5; iTempFacing++) {
         if (iTempFacing == iBlockFacing || this.isConnectedSpikeToFacing(blockAccess, i, j, k, iTempFacing)) {
            BlockModel tempSupportsModel = model.modelSideSupport.makeTemporaryCopy();
            tempSupportsModel.rotateAroundYToFacing(iTempFacing);
            if (supportsModel == null) {
               supportsModel = tempSupportsModel;
            } else {
               tempSupportsModel.makeTemporaryCopyOfPrimitiveList(supportsModel);
            }
         }
      }

      return supportsModel;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean shouldSideBeRendered(IBlockAccess blockAccess, int iNeighborI, int iNeighborJ, int iNeighborK, int iSide) {
      return true;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean renderBlock(RenderBlocks renderBlocks, int i, int j, int k) {
      IBlockAccess blockAccess = renderBlocks.blockAccess;
      int iFacing = this.getFacing(blockAccess, i, j, k);
      BlockModel transformedSupportsModel = this.getSideSupportsTemporaryModel(blockAccess, i, j, k);
      BlockModel transformedCenterModel = model.makeTemporaryCopy();
      if ((iFacing == 0 && blockAccess.getBlockId(i, j - 1, k) != this.blockID || iFacing == 1 && blockAccess.getBlockId(i, j + 1, k) != this.blockID)
         && !WorldUtils.isGroundCoverOnBlock(renderBlocks.blockAccess, i, j, k)) {
         model.modelBase.makeTemporaryCopyOfPrimitiveList(transformedCenterModel);
      }

      if (iFacing == 1) {
         int iBlockBelowID = blockAccess.getBlockId(i, j - 1, k);
         if (iBlockBelowID != this.blockID && transformedSupportsModel == null) {
            model.modelBall.makeTemporaryCopyOfPrimitiveList(transformedCenterModel);
         }
      } else {
         int iBlockAboveID = blockAccess.getBlockId(i, j + 1, k);
         if (iBlockAboveID != this.blockID) {
            if (WorldUtils.isBlockRestingOnThatBelow(blockAccess, i, j + 1, k)) {
               Block block = Block.blocksList[blockAccess.getBlockId(i, j + 1, k)];
               if (block instanceof CandleBlock && ((CandleBlock)block).getCandleCount(blockAccess, i, j + 1, k) > 1) {
                  model.holderModelLarge.makeTemporaryCopyOfPrimitiveList(transformedCenterModel);
               } else {
                  model.modelHolder.makeTemporaryCopyOfPrimitiveList(transformedCenterModel);
               }
            } else if (transformedSupportsModel == null) {
               model.modelBall.makeTemporaryCopyOfPrimitiveList(transformedCenterModel);
            }
         }
      }

      if (iFacing == 1) {
         transformedCenterModel.tiltToFacingAlongY(0);
      }

      if (transformedSupportsModel != null) {
         transformedSupportsModel.renderAsBlock(renderBlocks, this, i, j, k);
         model.modelCenterBrace.makeTemporaryCopyOfPrimitiveList(transformedCenterModel);
      }

      return transformedCenterModel.renderAsBlock(renderBlocks, this, i, j, k);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void renderBlockAsItem(RenderBlocks renderBlocks, int iItemDamage, float fBrightness) {
      model.renderAsItemBlock(renderBlocks, this, iItemDamage);
   }
}
