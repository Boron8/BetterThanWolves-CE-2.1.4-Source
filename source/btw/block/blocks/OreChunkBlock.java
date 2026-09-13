package btw.block.blocks;

import btw.block.model.BlockModel;
import btw.block.model.OreChunkModel;
import btw.world.util.WorldUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Material;
import net.minecraft.src.MathHelper;
import net.minecraft.src.MovingObjectPosition;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.Vec3;
import net.minecraft.src.World;

public class OreChunkBlock extends Block {
   protected static OreChunkModel model = new OreChunkModel();

   protected OreChunkBlock(int iBlockID) {
      super(iBlockID, Material.circuits);
      this.c(0.0F);
      this.setPicksEffectiveOn(true);
      this.initBlockBounds(0.5 - 0.125, 0.03125, 0.5 - 0.125, 0.5 + 0.125, 0.03125 + 0.1875, 0.5 + 0.125);
      this.a(j);
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
      int iBlockFacing = world.rand.nextInt(4) + 2;
      return this.setFacing(iMetadata, iBlockFacing);
   }

   @Override
   public boolean canPlaceBlockAt(World world, int i, int j, int k) {
      return WorldUtils.doesBlockHaveSmallCenterHardpointToFacing(world, i, j - 1, k, 1, true) ? super.canPlaceBlockAt(world, i, j, k) : false;
   }

   @Override
   public boolean isBlockRestingOnThatBelow(IBlockAccess blockAccess, int i, int j, int k) {
      return true;
   }

   @Override
   public void onNeighborBlockChange(World world, int i, int j, int k, int iBlockID) {
      if (!WorldUtils.doesBlockHaveSmallCenterHardpointToFacing(world, i, j - 1, k, 1, true)) {
         this.c(world, i, j, k, world.getBlockMetadata(i, j, k), 0);
         world.setBlockToAir(i, j, k);
      }
   }

   @Override
   public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k) {
      return null;
   }

   @Override
   public MovingObjectPosition collisionRayTrace(World world, int i, int j, int k, Vec3 startRay, Vec3 endRay) {
      BlockModel m_modelTransformed = model.makeTemporaryCopy();
      int iFacing = this.getFacing(world, i, j, k);
      m_modelTransformed.rotateAroundYToFacing(iFacing);
      return m_modelTransformed.collisionRayTrace(world, i, j, k, startRay, endRay);
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
      return (iMetadata & 3) + 2;
   }

   @Override
   public int setFacing(int iMetadata, int iFacing) {
      iMetadata &= -4;
      return iMetadata | MathHelper.clamp_int(iFacing, 2, 5) - 2;
   }

   @Override
   public boolean canRotateOnTurntable(IBlockAccess blockAccess, int i, int j, int k) {
      return true;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean shouldSideBeRendered(IBlockAccess blockAccess, int iNeighborI, int iNeighborJ, int iNeighborK, int iSide) {
      return true;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean renderBlock(RenderBlocks renderBlocks, int i, int j, int k) {
      BlockModel transformedModel = model.makeTemporaryCopy();
      transformedModel.rotateAroundYToFacing(this.getFacing(renderBlocks.blockAccess, i, j, k));
      return transformedModel.renderAsBlock(renderBlocks, this, i, j, k);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void renderBlockAsItem(RenderBlocks renderBlocks, int iItemDamage, float fBrightness) {
      model.renderAsItemBlock(renderBlocks, this, iItemDamage);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void renderBlockSecondPass(RenderBlocks renderBlocks, int i, int j, int k, boolean bFirstPassResult) {
      this.renderCookingByKiLnOverlay(renderBlocks, i, j, k, bFirstPassResult);
   }
}
