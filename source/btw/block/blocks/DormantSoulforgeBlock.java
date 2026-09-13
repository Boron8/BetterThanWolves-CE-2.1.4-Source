package btw.block.blocks;

import btw.block.model.BlockModel;
import btw.block.model.SoulforgeModel;
import btw.util.MiscUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.MovingObjectPosition;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.Vec3;
import net.minecraft.src.World;

public class DormantSoulforgeBlock extends Block {
   private final BlockModel blockModel = new SoulforgeModel();

   public DormantSoulforgeBlock(int iBlockID) {
      super(iBlockID, Material.iron);
      this.c(3.0F);
      this.a(k);
      this.c("fcBlockSoulforgeDormant");
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
   public int onBlockPlaced(World world, int i, int j, int k, int iFacing, float fClickX, float fClickY, float fClickZ, int iMetadata) {
      if (iFacing < 2) {
         iFacing = 2;
      } else {
         iFacing = Block.getOppositeFacing(iFacing);
      }

      return this.setFacing(iMetadata, iFacing);
   }

   @Override
   public void onBlockPlacedBy(World world, int i, int j, int k, EntityLiving entityLiving, ItemStack stack) {
      int iFacing = MiscUtils.convertOrientationToFlatBlockFacingReversed(entityLiving);
      this.setFacing(world, i, j, k, iFacing);
   }

   @Override
   public AxisAlignedBB getBlockBoundsFromPoolBasedOnState(IBlockAccess blockAccess, int i, int j, int k) {
      int iFacing = this.getFacing(blockAccess, i, j, k);
      return iFacing != 2 && iFacing != 3
         ? AxisAlignedBB.getAABBPool().getAABB(0.0, 0.0, 0.25, 1.0, 1.0, 0.75)
         : AxisAlignedBB.getAABBPool().getAABB(0.25, 0.0, 0.0, 0.75, 1.0, 1.0);
   }

   @Override
   public MovingObjectPosition collisionRayTrace(World world, int i, int j, int k, Vec3 startRay, Vec3 endRay) {
      int iFacing = this.getFacing(world, i, j, k);
      BlockModel transformedModel = this.blockModel.makeTemporaryCopy();
      transformedModel.rotateAroundYToFacing(iFacing);
      return transformedModel.collisionRayTrace(world, i, j, k, startRay, endRay);
   }

   @Override
   public int getFacing(int iMetadata) {
      return iMetadata;
   }

   @Override
   public int setFacing(int iMetadata, int iFacing) {
      return iFacing;
   }

   @Override
   public boolean canRotateOnTurntable(IBlockAccess iBlockAccess, int i, int j, int k) {
      return true;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean shouldSideBeRendered(IBlockAccess blockAccess, int iNeighborI, int iNeighborJ, int iNeighborK, int iSide) {
      return this.currentBlockRenderer.shouldSideBeRenderedBasedOnCurrentBounds(iNeighborI, iNeighborJ, iNeighborK, iSide);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean renderBlock(RenderBlocks renderBlocks, int i, int j, int k) {
      int iFacing = this.getFacing(renderBlocks.blockAccess, i, j, k);
      BlockModel transformedModel = this.blockModel.makeTemporaryCopy();
      transformedModel.rotateAroundYToFacing(iFacing);
      return transformedModel.renderAsBlock(renderBlocks, this, i, j, k);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void renderBlockAsItem(RenderBlocks renderBlocks, int iItemDamage, float fBrightness) {
      this.blockModel.renderAsItemBlock(renderBlocks, this, iItemDamage);
   }
}
