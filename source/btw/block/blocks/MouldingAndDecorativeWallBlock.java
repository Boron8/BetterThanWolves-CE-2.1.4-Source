package btw.block.blocks;

import btw.block.util.RayTraceUtils;
import btw.client.render.util.RenderUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.Block;
import net.minecraft.src.Material;
import net.minecraft.src.MovingObjectPosition;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.StepSound;
import net.minecraft.src.Vec3;
import net.minecraft.src.World;

public class MouldingAndDecorativeWallBlock extends MouldingAndDecorativeBlock {
   protected static final double TABLE_WALL_LEG_WIDTH = 0.5;
   protected static final double TABLE_WALL_LEG_HALF_WIDTH = 0.25;

   public MouldingAndDecorativeWallBlock(
      int blockID,
      Material material,
      String textureName,
      String columnSideTextureName,
      int matchingCornerBlockID,
      float hardness,
      float resistance,
      StepSound stepSound,
      String name
   ) {
      super(blockID, material, textureName, columnSideTextureName, matchingCornerBlockID, hardness, resistance, stepSound, name);
   }

   public MouldingAndDecorativeWallBlock(
      int blockID,
      Material material,
      String textureName,
      String columnSideTextureName,
      String columnTopAndBottomTextureName,
      String pedestalSideTextureName,
      String pedestalTopAndBottomTextureName,
      int matchingCornerBlockID,
      float hardness,
      float resistance,
      StepSound stepSound,
      String name
   ) {
      super(
         blockID,
         material,
         textureName,
         columnSideTextureName,
         columnTopAndBottomTextureName,
         pedestalSideTextureName,
         pedestalTopAndBottomTextureName,
         matchingCornerBlockID,
         hardness,
         resistance,
         stepSound,
         name
      );
   }

   @Override
   public MovingObjectPosition collisionRayTraceTableWithLeg(World world, int x, int y, int z, Vec3 startRay, Vec3 endRay) {
      RayTraceUtils rayTrace = new RayTraceUtils(world, x, y, z, startRay, endRay);
      rayTrace.addBoxWithLocalCoordsToIntersectionList(0.0, 0.375, 0.0, 1.0, 0.5, 1.0);
      rayTrace.addBoxWithLocalCoordsToIntersectionList(0.25, 0.0, 0.25, 0.75, 0.125, 0.75);
      if (this.doesTableHaveLeg(world, x - 1, y, z)
         && Block.blocksList[world.getBlockId(x - 1, y, z)] instanceof MouldingAndDecorativeWallBlock
         && world.getBlockMetadata(x - 1, y, z) == 15) {
         rayTrace.addBoxWithLocalCoordsToIntersectionList(0.0, 0.0, 0.3125, 0.25, 0.875, 0.6875);
      }

      if (this.doesTableHaveLeg(world, x, y, z - 1)
         && Block.blocksList[world.getBlockId(x, y, z - 1)] instanceof MouldingAndDecorativeWallBlock
         && world.getBlockMetadata(x, y, z - 1) == 15) {
         rayTrace.addBoxWithLocalCoordsToIntersectionList(0.3125, 0.0, 0.0, 0.6875, 0.875, 0.25);
      }

      if (this.doesTableHaveLeg(world, x + 1, y, z)
         && Block.blocksList[world.getBlockId(x + 1, y, z)] instanceof MouldingAndDecorativeWallBlock
         && world.getBlockMetadata(x + 1, y, z) == 15) {
         rayTrace.addBoxWithLocalCoordsToIntersectionList(0.75, 0.0, 0.3125, 1.0, 0.875, 0.6875);
      }

      if (this.doesTableHaveLeg(world, x, y, z + 1)
         && Block.blocksList[world.getBlockId(x, y, z + 1)] instanceof MouldingAndDecorativeWallBlock
         && world.getBlockMetadata(x, y, z + 1) == 15) {
         rayTrace.addBoxWithLocalCoordsToIntersectionList(0.3125, 0.0, 0.75, 0.6875, 0.875, 1.0);
      }

      return rayTrace.getFirstIntersection();
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean renderTable(RenderBlocks renderBlocks, int x, int y, int z) {
      renderBlocks.setRenderBounds(0.0, 0.875, 0.0, 1.0, 1.0, 1.0);
      renderBlocks.renderStandardBlock(this, x, y, z);
      if (this.doesTableHaveLeg(renderBlocks.blockAccess, x, y, z)) {
         renderBlocks.setRenderBounds(0.25, 0.0, 0.25, 0.75, 0.875, 0.75);
         renderBlocks.renderStandardBlock(this, x, y, z);
         if (this.doesTableHaveLeg(renderBlocks.blockAccess, x - 1, y, z)
            && Block.blocksList[renderBlocks.blockAccess.getBlockId(x - 1, y, z)] instanceof MouldingAndDecorativeWallBlock
            && renderBlocks.blockAccess.getBlockMetadata(x - 1, y, z) == 15) {
            renderBlocks.setRenderBounds(0.25, 0.0, 0.6875, 0.0, 0.875, 0.3125);
            renderBlocks.renderStandardBlock(this, x, y, z);
         }

         if (this.doesTableHaveLeg(renderBlocks.blockAccess, x, y, z - 1)
            && Block.blocksList[renderBlocks.blockAccess.getBlockId(x, y, z - 1)] instanceof MouldingAndDecorativeWallBlock
            && renderBlocks.blockAccess.getBlockMetadata(x, y, z - 1) == 15) {
            renderBlocks.setRenderBounds(0.6875, 0.0, 0.25, 0.3125, 0.875, 0.0);
            renderBlocks.renderStandardBlock(this, x, y, z);
         }

         if (this.doesTableHaveLeg(renderBlocks.blockAccess, x + 1, y, z)
            && Block.blocksList[renderBlocks.blockAccess.getBlockId(x + 1, y, z)] instanceof MouldingAndDecorativeWallBlock
            && renderBlocks.blockAccess.getBlockMetadata(x + 1, y, z) == 15) {
            renderBlocks.setRenderBounds(1.0, 0.0, 0.6875, 0.75, 0.875, 0.3125);
            renderBlocks.renderStandardBlock(this, x, y, z);
         }

         if (this.doesTableHaveLeg(renderBlocks.blockAccess, x, y, z + 1)
            && Block.blocksList[renderBlocks.blockAccess.getBlockId(x, y, z + 1)] instanceof MouldingAndDecorativeWallBlock
            && renderBlocks.blockAccess.getBlockMetadata(x, y, z + 1) == 15) {
            renderBlocks.setRenderBounds(0.6875, 0.0, 1.0, 0.3125, 0.875, 0.75);
            renderBlocks.renderStandardBlock(this, x, y, z);
         }
      }

      return true;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void renderTableInvBlock(RenderBlocks renderBlocks, Block block) {
      renderBlocks.setRenderBounds(0.0, 0.875, 0.0, 1.0, 1.0, 1.0);
      RenderUtils.renderInvBlockWithMetadata(renderBlocks, block, -0.5F, -0.5F, -0.5F, 15);
      renderBlocks.setRenderBounds(0.25, 0.0, 0.25, 0.75, 0.875, 0.75);
      RenderUtils.renderInvBlockWithMetadata(renderBlocks, block, -0.5F, -0.5F, -0.5F, 15);
   }
}
