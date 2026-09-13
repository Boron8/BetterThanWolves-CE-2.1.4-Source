package btw.block.blocks;

import btw.block.util.RayTraceUtils;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.Entity;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Material;
import net.minecraft.src.MovingObjectPosition;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.StepSound;
import net.minecraft.src.Tessellator;
import net.minecraft.src.Vec3;
import net.minecraft.src.World;
import org.lwjgl.opengl.GL11;

public class SidingAndCornerAndDecorativeWallBlock extends SidingAndCornerAndDecorativeBlock {
   protected static final float BENCH_WALL_LEG_WIDTH = 0.5F;
   protected static final float BENCH_WALL_LEG_HALF_WIDTH = 0.25F;

   public SidingAndCornerAndDecorativeWallBlock(
      int blockID, Material material, String textureName, float hardness, float resistance, StepSound stepSound, String name
   ) {
      super(blockID, material, textureName, hardness, resistance, stepSound, name);
   }

   @Override
   public AxisAlignedBB getBlockBoundsFromPoolForFence(IBlockAccess blockAccess, int i, int j, int k) {
      return Block.cobblestoneWall.getBlockBoundsFromPoolBasedOnState(blockAccess, i, j, k);
   }

   @Override
   public MovingObjectPosition collisionRayTraceFence(World world, int x, int y, int z, Vec3 var5, Vec3 var6) {
      return Block.cobblestoneWall.collisionRayTrace(world, x, y, z, var5, var6);
   }

   @Override
   public MovingObjectPosition collisionRayTraceBenchWithLeg(World world, int x, int y, int z, Vec3 startRay, Vec3 endRay) {
      RayTraceUtils rayTrace = new RayTraceUtils(world, x, y, z, startRay, endRay);
      rayTrace.addBoxWithLocalCoordsToIntersectionList(0.0, 0.375, 0.0, 1.0, 0.5, 1.0);
      rayTrace.addBoxWithLocalCoordsToIntersectionList(0.25, 0.0, 0.25, 0.75, 0.375, 0.75);
      if (this.doesBenchHaveLeg(world, x - 1, y, z)
         && Block.blocksList[world.getBlockId(x - 1, y, z)] instanceof SidingAndCornerAndDecorativeWallBlock
         && world.getBlockMetadata(x - 1, y, z) == 12) {
         rayTrace.addBoxWithLocalCoordsToIntersectionList(0.0, 0.0, 0.3125, 0.25, 0.375, 0.6875);
      }

      if (this.doesBenchHaveLeg(world, x, y, z - 1)
         && Block.blocksList[world.getBlockId(x, y, z - 1)] instanceof SidingAndCornerAndDecorativeWallBlock
         && world.getBlockMetadata(x, y, z - 1) == 12) {
         rayTrace.addBoxWithLocalCoordsToIntersectionList(0.3125, 0.0, 0.0, 0.6875, 0.375, 0.25);
      }

      if (this.doesBenchHaveLeg(world, x + 1, y, z)
         && Block.blocksList[world.getBlockId(x + 1, y, z)] instanceof SidingAndCornerAndDecorativeWallBlock
         && world.getBlockMetadata(x + 1, y, z) == 12) {
         rayTrace.addBoxWithLocalCoordsToIntersectionList(0.75, 0.0, 0.3125, 1.0, 0.375, 0.6875);
      }

      if (this.doesBenchHaveLeg(world, x, y, z + 1)
         && Block.blocksList[world.getBlockId(x, y, z + 1)] instanceof SidingAndCornerAndDecorativeWallBlock
         && world.getBlockMetadata(x, y, z + 1) == 12) {
         rayTrace.addBoxWithLocalCoordsToIntersectionList(0.3125, 0.0, 0.75, 0.6875, 0.375, 1.0);
      }

      return rayTrace.getFirstIntersection();
   }

   @Override
   public void addCollisionBoxesToListForFence(World world, int x, int y, int z, AxisAlignedBB aabb, List collisionList, Entity entity) {
      Block.cobblestoneWall.addCollisionBoxesToList(world, x, y, z, aabb, collisionList, entity);
   }

   @Override
   public boolean isWall(int metadata) {
      return metadata == 14;
   }

   @Override
   public boolean isFence(int metadata) {
      return false;
   }

   @Override
   public boolean shouldPaneConnectToThisBlockToFacing(IBlockAccess blockAccess, int x, int y, int z, int facing) {
      return true;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean renderBench(RenderBlocks renderBlocks, int x, int y, int z) {
      renderBlocks.setRenderBounds(0.0, 0.375, 0.0, 1.0, 0.5, 1.0);
      renderBlocks.renderStandardBlock(this, x, y, z);
      if (this.doesBenchHaveLeg(renderBlocks.blockAccess, x, y, z)) {
         renderBlocks.setRenderBounds(0.25, 0.0, 0.25, 0.75, 0.375, 0.75);
         renderBlocks.renderStandardBlock(this, x, y, z);
         if (this.doesBenchHaveLeg(renderBlocks.blockAccess, x - 1, y, z)
            && Block.blocksList[renderBlocks.blockAccess.getBlockId(x - 1, y, z)] instanceof SidingAndCornerAndDecorativeWallBlock
            && renderBlocks.blockAccess.getBlockMetadata(x - 1, y, z) == 12) {
            renderBlocks.setRenderBounds(0.25, 0.0, 0.6875, 0.0, 0.375, 0.3125);
            renderBlocks.renderStandardBlock(this, x, y, z);
         }

         if (this.doesBenchHaveLeg(renderBlocks.blockAccess, x, y, z - 1)
            && Block.blocksList[renderBlocks.blockAccess.getBlockId(x, y, z - 1)] instanceof SidingAndCornerAndDecorativeWallBlock
            && renderBlocks.blockAccess.getBlockMetadata(x, y, z - 1) == 12) {
            renderBlocks.setRenderBounds(0.6875, 0.0, 0.25, 0.3125, 0.375, 0.0);
            renderBlocks.renderStandardBlock(this, x, y, z);
         }

         if (this.doesBenchHaveLeg(renderBlocks.blockAccess, x + 1, y, z)
            && Block.blocksList[renderBlocks.blockAccess.getBlockId(x + 1, y, z)] instanceof SidingAndCornerAndDecorativeWallBlock
            && renderBlocks.blockAccess.getBlockMetadata(x + 1, y, z) == 12) {
            renderBlocks.setRenderBounds(1.0, 0.0, 0.6875, 0.75, 0.375, 0.3125);
            renderBlocks.renderStandardBlock(this, x, y, z);
         }

         if (this.doesBenchHaveLeg(renderBlocks.blockAccess, x, y, z + 1)
            && Block.blocksList[renderBlocks.blockAccess.getBlockId(x, y, z + 1)] instanceof SidingAndCornerAndDecorativeWallBlock
            && renderBlocks.blockAccess.getBlockMetadata(x, y, z + 1) == 12) {
            renderBlocks.setRenderBounds(0.6875, 0.0, 1.0, 0.3125, 0.375, 0.75);
            renderBlocks.renderStandardBlock(this, x, y, z);
         }
      }

      return true;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean renderFence(RenderBlocks render, int x, int y, int z) {
      return WallBlock.renderWall(render, this, x, y, z);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void renderFenceInvBlock(RenderBlocks renderBlocks, Block block, int itemDamage) {
      Tessellator var3x = Tessellator.instance;

      for (int var4 = 0; var4 < 2; var4++) {
         float var5 = 0.125F;
         if (var4 == 0) {
            renderBlocks.setRenderBounds(0.25, 0.0, 0.25, 0.75, 1.0, 0.75);
         }

         if (var4 == 1) {
            renderBlocks.setRenderBounds(0.0, 0.0, 0.3125, 1.0, 0.8125, 0.6875);
         }

         GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
         var3x.startDrawingQuads();
         var3x.setNormal(0.0F, -1.0F, 0.0F);
         renderBlocks.renderFaceYNeg(block, 0.0, 0.0, 0.0, block.getBlockTextureFromSide(0));
         var3x.draw();
         var3x.startDrawingQuads();
         var3x.setNormal(0.0F, 1.0F, 0.0F);
         renderBlocks.renderFaceYPos(block, 0.0, 0.0, 0.0, block.getBlockTextureFromSide(1));
         var3x.draw();
         var3x.startDrawingQuads();
         var3x.setNormal(0.0F, 0.0F, -1.0F);
         renderBlocks.renderFaceZNeg(block, 0.0, 0.0, 0.0, block.getBlockTextureFromSide(2));
         var3x.draw();
         var3x.startDrawingQuads();
         var3x.setNormal(0.0F, 0.0F, 1.0F);
         renderBlocks.renderFaceZPos(block, 0.0, 0.0, 0.0, block.getBlockTextureFromSide(3));
         var3x.draw();
         var3x.startDrawingQuads();
         var3x.setNormal(-1.0F, 0.0F, 0.0F);
         renderBlocks.renderFaceXNeg(block, 0.0, 0.0, 0.0, block.getBlockTextureFromSide(4));
         var3x.draw();
         var3x.startDrawingQuads();
         var3x.setNormal(1.0F, 0.0F, 0.0F);
         renderBlocks.renderFaceXPos(block, 0.0, 0.0, 0.0, block.getBlockTextureFromSide(5));
         var3x.draw();
         GL11.glTranslatef(0.5F, 0.5F, 0.5F);
      }

      renderBlocks.setRenderBounds(0.0, 0.0, 0.0, 1.0, 1.0, 1.0);
   }
}
