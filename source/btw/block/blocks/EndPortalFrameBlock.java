package btw.block.blocks;

import btw.block.util.RayTraceUtils;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.BlockEndPortalFrame;
import net.minecraft.src.Entity;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.ItemStack;
import net.minecraft.src.MovingObjectPosition;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.Vec3;
import net.minecraft.src.World;

public class EndPortalFrameBlock extends BlockEndPortalFrame {
   public EndPortalFrameBlock(int iBlockID) {
      super(iBlockID);
      this.initBlockBounds(0.0, 0.0, 0.0, 1.0, 0.8125, 1.0);
   }

   @Override
   public boolean renderAsNormalBlock() {
      return false;
   }

   @Override
   public void addCollisionBoxesToList(World world, int i, int j, int k, AxisAlignedBB intersectingBox, List list, Entity entity) {
      AxisAlignedBB tempBox = this.b(world, i, j, k);
      tempBox.addToListIfIntersects(intersectingBox, list);
      if (d(world.getBlockMetadata(i, j, k))) {
         tempBox = AxisAlignedBB.getAABBPool().getAABB(0.3125, 0.8125, 0.3125, 0.6875, 1.0, 0.6875).offset(i, j, k);
         tempBox.addToListIfIntersects(intersectingBox, list);
      }
   }

   @Override
   public MovingObjectPosition collisionRayTrace(World world, int i, int j, int k, Vec3 startRay, Vec3 endRay) {
      RayTraceUtils rayTrace = new RayTraceUtils(world, i, j, k, startRay, endRay);
      rayTrace.addBoxWithLocalCoordsToIntersectionList(this.getFixedBlockBoundsFromPool());
      if (d(world.getBlockMetadata(i, j, k))) {
         rayTrace.addBoxWithLocalCoordsToIntersectionList(0.25, 0.8125, 0.25, 0.75, 1.0, 0.75);
      }

      return rayTrace.getFirstIntersection();
   }

   @Override
   public ItemStack getStackRetrievedByBlockDispenser(World world, int i, int j, int k) {
      return null;
   }

   @Override
   public int getMobilityFlag() {
      return 2;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean renderBlock(RenderBlocks renderer, int i, int j, int k) {
      return renderer.renderBlockEndPortalFrame(this, i, j, k);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean shouldSideBeRendered(IBlockAccess blockAccess, int i, int j, int k, int iSide) {
      return iSide != 1 ? !blockAccess.isBlockOpaqueCube(i, j, k) : true;
   }
}
