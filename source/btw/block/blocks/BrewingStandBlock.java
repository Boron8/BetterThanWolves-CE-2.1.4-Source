package btw.block.blocks;

import btw.block.util.RayTraceUtils;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.BlockBrewingStand;
import net.minecraft.src.Entity;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.MovingObjectPosition;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.Vec3;
import net.minecraft.src.World;

public class BrewingStandBlock extends BlockBrewingStand {
   protected static final double BASE_HEIGHT = 0.125;
   protected static final double BASE_WIDTH = 0.875;
   protected static final double BASE_HALF_WIDTH = 0.4375;
   protected static final double CENTER_COLUMN_WIDTH = 0.125;
   protected static final double CENTER_COLUMN_HALF_WIDTH = 0.0625;
   protected static final double CENTER_ASSEMBLY_WIDTH = 0.625;
   protected static final double CENTER_ASSEMBLY_HALF_WIDTH = 0.3125;

   public BrewingStandBlock(int iBlockID) {
      super(iBlockID);
   }

   @Override
   public boolean doesBlockHopperInsert(World world, int i, int j, int k) {
      return true;
   }

   @Override
   public void addCollisionBoxesToList(World world, int i, int j, int k, AxisAlignedBB boundingBox, List list, Entity entity) {
      AxisAlignedBB tempBox = AxisAlignedBB.getAABBPool().getAABB(0.0, 0.0, 0.0, 1.0, 0.125, 1.0).offset(i, j, k);
      tempBox.addToListIfIntersects(boundingBox, list);
      tempBox = AxisAlignedBB.getAABBPool().getAABB(0.4375, 0.0, 0.4375, 0.5625, 1.0, 0.5625).offset(i, j, k);
      tempBox.addToListIfIntersects(boundingBox, list);
   }

   @Override
   public MovingObjectPosition collisionRayTrace(World world, int i, int j, int k, Vec3 startRay, Vec3 endRay) {
      RayTraceUtils rayTrace = new RayTraceUtils(world, i, j, k, startRay, endRay);
      rayTrace.addBoxWithLocalCoordsToIntersectionList(0.0625, 0.0, 0.0625, 0.9375, 0.125, 0.9375);
      rayTrace.addBoxWithLocalCoordsToIntersectionList(0.1875, 0.125, 0.1875, 0.8125, 1.0, 0.8125);
      return rayTrace.getFirstIntersection();
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean renderBlock(RenderBlocks renderer, int i, int j, int k) {
      return renderer.renderBlockBrewingStand(this, i, j, k);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean shouldSideBeRendered(IBlockAccess blockAccess, int iNeighborI, int iNeighborJ, int iNeighborK, int iSide) {
      return this.currentBlockRenderer.shouldSideBeRenderedBasedOnCurrentBounds(iNeighborI, iNeighborJ, iNeighborK, iSide);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int i, int j, int k) {
      return AxisAlignedBB.getAABBPool().getAABB(0.0625, 0.0, 0.0625, 0.9375, 1.0, 0.9375).offset(i, j, k);
   }
}
