package btw.block.blocks;

import btw.block.BTWBlocks;
import btw.block.util.RayTraceUtils;
import btw.client.render.util.RenderUtils;
import btw.world.util.BlockPos;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.Entity;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.MovingObjectPosition;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.StepSound;
import net.minecraft.src.Tessellator;
import net.minecraft.src.Vec3;
import net.minecraft.src.World;
import org.lwjgl.opengl.GL11;

public class SidingAndCornerAndDecorativeBlock extends SidingAndCornerBlock {
   public static final int SUBTYPE_BENCH = 12;
   public static final int SUBTYPE_FENCE = 14;
   protected static final float BENCH_TOP_HEIGHT = 0.125F;
   protected static final float BENCH_LEG_HEIGHT = 0.375F;
   protected static final float BENCH_LEG_WIDTH = 0.25F;
   protected static final float BENCH_LEG_HALF_WIDTH = 0.125F;
   public static final int OAK_BENCH_TOP_TEXTURE_ID = 93;
   public static final int OAK_BENCH_LEG_TEXTURE_ID = 94;

   public SidingAndCornerAndDecorativeBlock(
      int iBlockID, Material material, String sTextureName, float fHardness, float fResistance, StepSound stepSound, String name
   ) {
      super(iBlockID, material, sTextureName, fHardness, fResistance, stepSound, name);
   }

   @Override
   public void addCollisionBoxesToList(World world, int i, int j, int k, AxisAlignedBB axisalignedbb, List list, Entity entity) {
      int iSubtype = world.getBlockMetadata(i, j, k);
      if (iSubtype == 14) {
         this.addCollisionBoxesToListForFence(world, i, j, k, axisalignedbb, list, entity);
      } else {
         super.a(world, i, j, k, axisalignedbb, list, entity);
      }
   }

   @Override
   public AxisAlignedBB getBlockBoundsFromPoolBasedOnState(IBlockAccess blockAccess, int i, int j, int k) {
      int iSubtype = blockAccess.getBlockMetadata(i, j, k);
      if (iSubtype == 12) {
         return this.getBlockBoundsFromPoolForBench(blockAccess, i, j, k);
      } else {
         return iSubtype == 14 ? this.getBlockBoundsFromPoolForFence(blockAccess, i, j, k) : super.getBlockBoundsFromPoolBasedOnState(blockAccess, i, j, k);
      }
   }

   @Override
   public MovingObjectPosition collisionRayTrace(World world, int i, int j, int k, Vec3 startRay, Vec3 endRay) {
      int iBlockID = world.getBlockId(i, j, k);
      if (this.isBlockBench(world, i, j, k) && this.doesBenchHaveLeg(world, i, j, k)) {
         return this.collisionRayTraceBenchWithLeg(world, i, j, k, startRay, endRay);
      } else {
         return (iBlockID != this.blockID || world.getBlockMetadata(i, j, k) != 14) && iBlockID != Block.fenceGate.blockID
            ? super.a(world, i, j, k, startRay, endRay)
            : this.collisionRayTraceFence(world, i, j, k, startRay, endRay);
      }
   }

   @Override
   public int onBlockPlaced(World world, int i, int j, int k, int iFacing, float fClickX, float fClickY, float fClickZ, int iMetadata) {
      int iSubtype = world.getBlockMetadata(i, j, k);
      return iSubtype != 12 && iSubtype != 14 ? super.onBlockPlaced(world, i, j, k, iFacing, fClickX, fClickY, fClickZ, iMetadata) : iMetadata;
   }

   @Override
   public boolean hasCenterHardPointToFacing(IBlockAccess blockAccess, int i, int j, int k, int iFacing, boolean bIgnoreTransparency) {
      int iSubtype = blockAccess.getBlockMetadata(i, j, k);
      if (iSubtype == 12) {
         return iFacing == 0;
      } else {
         return iSubtype != 14 ? super.hasCenterHardPointToFacing(blockAccess, i, j, k, iFacing, bIgnoreTransparency) : iFacing == 0 || iFacing == 1;
      }
   }

   @Override
   public boolean hasLargeCenterHardPointToFacing(IBlockAccess blockAccess, int i, int j, int k, int iFacing, boolean bIgnoreTransparency) {
      int iSubtype = blockAccess.getBlockMetadata(i, j, k);
      return iSubtype != 12 && iSubtype != 14 ? super.hasLargeCenterHardPointToFacing(blockAccess, i, j, k, iFacing, bIgnoreTransparency) : false;
   }

   @Override
   public int damageDropped(int iMetadata) {
      return isDecorativeFromMetadata(iMetadata) ? iMetadata : super.damageDropped(iMetadata);
   }

   @Override
   public boolean getBlocksMovement(IBlockAccess blockAccess, int i, int j, int k) {
      int iSubtype = blockAccess.getBlockMetadata(i, j, k);
      return iSubtype == 14 ? false : super.b(blockAccess, i, j, k);
   }

   @Override
   public boolean canGroundCoverRestOnBlock(World world, int i, int j, int k) {
      int iMetadata = world.getBlockMetadata(i, j, k);
      if (iMetadata == 12) {
         return true;
      } else {
         return isDecorativeFromMetadata(iMetadata) ? world.doesBlockHaveSolidTopSurface(i, j, k) : super.canGroundCoverRestOnBlock(world, i, j, k);
      }
   }

   @Override
   public float groundCoverRestingOnVisualOffset(IBlockAccess blockAccess, int i, int j, int k) {
      int iMetadata = blockAccess.getBlockMetadata(i, j, k);
      if (iMetadata == 12) {
         return -0.5F;
      } else {
         return isDecorativeFromMetadata(iMetadata) ? 0.0F : super.groundCoverRestingOnVisualOffset(blockAccess, i, j, k);
      }
   }

   @Override
   public int getWeightOnPathBlocked(IBlockAccess blockAccess, int i, int j, int k) {
      int iMetadata = blockAccess.getBlockMetadata(i, j, k);
      return iMetadata == 14 ? -3 : 0;
   }

   @Override
   public int getFacing(int iMetadata) {
      return iMetadata != 12 && iMetadata != 14 ? super.getFacing(iMetadata) : 0;
   }

   @Override
   public int setFacing(int iMetadata, int iFacing) {
      return iMetadata != 12 && iMetadata != 14 ? super.setFacing(iMetadata, iFacing) : iMetadata;
   }

   @Override
   public boolean canRotateOnTurntable(IBlockAccess blockAccess, int i, int j, int k) {
      int iSubtype = blockAccess.getBlockMetadata(i, j, k);
      return iSubtype != 12 && iSubtype != 14 ? super.canRotateOnTurntable(blockAccess, i, j, k) : true;
   }

   @Override
   public boolean canTransmitRotationVerticallyOnTurntable(IBlockAccess blockAccess, int i, int j, int k) {
      int iSubtype = blockAccess.getBlockMetadata(i, j, k);
      if (iSubtype == 14) {
         return true;
      } else {
         return iSubtype == 12 ? false : super.canTransmitRotationVerticallyOnTurntable(blockAccess, i, j, k);
      }
   }

   @Override
   public int rotateMetadataAroundJAxis(int iMetadata, boolean bReverse) {
      return iMetadata != 12 && iMetadata != 14 ? super.rotateMetadataAroundJAxis(iMetadata, bReverse) : iMetadata;
   }

   @Override
   public boolean toggleFacing(World world, int i, int j, int k, boolean bReverse) {
      int iSubtype = world.getBlockMetadata(i, j, k);
      return iSubtype != 12 && iSubtype != 14 ? super.toggleFacing(world, i, j, k, bReverse) : false;
   }

   @Override
   public float mobSpawnOnVerticalOffset(World world, int i, int j, int k) {
      int iSubtype = world.getBlockMetadata(i, j, k);
      if (iSubtype == 14) {
         return 0.5F;
      } else {
         return iSubtype == 12 ? -0.5F : super.mobSpawnOnVerticalOffset(world, i, j, k);
      }
   }

   @Override
   public boolean isFence(int metadata) {
      return metadata == 14;
   }

   @Override
   public boolean isBenchOrTable(int metadata) {
      return metadata == 12;
   }

   @Override
   public boolean shouldWallFormPostBelowThisBlock(IBlockAccess blockAccess, int x, int y, int z) {
      return blockAccess.getBlockMetadata(x, y, z) == 12 && this.doesBenchHaveLeg(blockAccess, x, y, z);
   }

   public boolean isDecorative(IBlockAccess blockAccess, int i, int j, int k) {
      return isDecorativeFromMetadata(blockAccess.getBlockMetadata(i, j, k));
   }

   public static boolean isDecorativeFromMetadata(int iMetadata) {
      return iMetadata == 12 || iMetadata == 14;
   }

   public AxisAlignedBB getBlockBoundsFromPoolForBench(IBlockAccess blockAccess, int i, int j, int k) {
      return !this.doesBenchHaveLeg(blockAccess, i, j, k)
         ? AxisAlignedBB.getAABBPool().getAABB(0.0, 0.375, 0.0, 1.0, 0.5, 1.0)
         : AxisAlignedBB.getAABBPool().getAABB(0.0, 0.0, 0.0, 1.0, 0.5, 1.0);
   }

   public AxisAlignedBB getBlockBoundsFromPoolForFence(IBlockAccess blockAccess, int i, int j, int k) {
      AxisAlignedBB fenceBox = AxisAlignedBB.getAABBPool().getAABB(0.375, 0.0, 0.375, 0.625, 1.0, 0.625);
      if (FenceBlock.canConnectToBlockToFacing(blockAccess, i, j, k, 2)) {
         fenceBox.minZ = 0.0;
      }

      if (FenceBlock.canConnectToBlockToFacing(blockAccess, i, j, k, 3)) {
         fenceBox.maxZ = 1.0;
      }

      if (FenceBlock.canConnectToBlockToFacing(blockAccess, i, j, k, 4)) {
         fenceBox.minX = 0.0;
      }

      if (FenceBlock.canConnectToBlockToFacing(blockAccess, i, j, k, 5)) {
         fenceBox.maxX = 1.0;
      }

      return fenceBox;
   }

   public void addCollisionBoxesToListForFence(World world, int i, int j, int k, AxisAlignedBB intersectingBox, List list, Entity entity) {
      boolean bConnectsNegativeK = FenceBlock.canConnectToBlockToFacing(world, i, j, k, 2);
      boolean bConnectsPositiveK = FenceBlock.canConnectToBlockToFacing(world, i, j, k, 3);
      boolean bConnectsNegativeI = FenceBlock.canConnectToBlockToFacing(world, i, j, k, 4);
      boolean bConnectsPositiveI = FenceBlock.canConnectToBlockToFacing(world, i, j, k, 5);
      float fXMin = 0.375F;
      float fXMax = 0.625F;
      float fZMin = 0.375F;
      float fZMax = 0.625F;
      if (bConnectsNegativeK) {
         fZMin = 0.0F;
      }

      if (bConnectsPositiveK) {
         fZMax = 1.0F;
      }

      if (bConnectsNegativeK || bConnectsPositiveK) {
         AxisAlignedBB.getAABBPool().getAABB(fXMin, 0.0, fZMin, fXMax, 1.5, fZMax).offset(i, j, k).addToListIfIntersects(intersectingBox, list);
      }

      if (bConnectsNegativeI) {
         fXMin = 0.0F;
      }

      if (bConnectsPositiveI) {
         fXMax = 1.0F;
      }

      if (bConnectsNegativeI || bConnectsPositiveI || !bConnectsNegativeK && !bConnectsPositiveK) {
         AxisAlignedBB.getAABBPool().getAABB(fXMin, 0.0, 0.375, fXMax, 1.5, 0.625).offset(i, j, k).addToListIfIntersects(intersectingBox, list);
      }
   }

   public boolean doesBenchHaveLeg(IBlockAccess blockAccess, int i, int j, int k) {
      int iBlockBelowID = blockAccess.getBlockId(i, j - 1, k);
      if (this.blockID == BTWBlocks.netherBrickSidingAndCorner.blockID) {
         if (iBlockBelowID == Block.netherFence.blockID) {
            return true;
         }
      } else if (this.blockID == iBlockBelowID) {
         int iBlockBelowMetadata = blockAccess.getBlockMetadata(i, j - 1, k);
         if (iBlockBelowMetadata == 14) {
            return true;
         }
      }

      boolean positiveIBench = this.isBlockBench(blockAccess, i + 1, j, k);
      boolean negativeIBench = this.isBlockBench(blockAccess, i - 1, j, k);
      boolean positiveKBench = this.isBlockBench(blockAccess, i, j, k + 1);
      boolean negativeKBench = this.isBlockBench(blockAccess, i, j, k - 1);
      return !positiveIBench && (!positiveKBench || !negativeKBench) || !negativeIBench && (!positiveKBench || !negativeKBench);
   }

   public boolean isBlockBench(IBlockAccess blockAccess, int i, int j, int k) {
      return blockAccess.getBlockId(i, j, k) == this.blockID && blockAccess.getBlockMetadata(i, j, k) == 12;
   }

   public MovingObjectPosition collisionRayTraceBenchWithLeg(World world, int i, int j, int k, Vec3 startRay, Vec3 endRay) {
      RayTraceUtils rayTrace = new RayTraceUtils(world, i, j, k, startRay, endRay);
      rayTrace.addBoxWithLocalCoordsToIntersectionList(0.0, 0.375, 0.0, 1.0, 0.5, 1.0);
      rayTrace.addBoxWithLocalCoordsToIntersectionList(0.375, 0.0, 0.375, 0.625, 0.375, 0.625);
      return rayTrace.getFirstIntersection();
   }

   public MovingObjectPosition collisionRayTraceFence(World world, int i, int j, int k, Vec3 startRay, Vec3 endRay) {
      RayTraceUtils rayTrace = new RayTraceUtils(world, i, j, k, startRay, endRay);
      rayTrace.addBoxWithLocalCoordsToIntersectionList(0.375, 0.0, 0.375, 0.625, 1.0, 0.625);
      boolean bConnectsAlongI = false;
      boolean bConnectsNegativeK = FenceBlock.canConnectToBlockToFacing(world, i, j, k, 2);
      boolean bConnectsPositiveK = FenceBlock.canConnectToBlockToFacing(world, i, j, k, 3);
      boolean bConnectsNegativeI = FenceBlock.canConnectToBlockToFacing(world, i, j, k, 4);
      boolean bConnectsPositiveI = FenceBlock.canConnectToBlockToFacing(world, i, j, k, 5);
      if (bConnectsNegativeI || bConnectsPositiveI) {
         bConnectsAlongI = true;
      }

      boolean bConnectsAlongK = false;
      if (bConnectsNegativeK || bConnectsPositiveK) {
         bConnectsAlongK = true;
      }

      if (!bConnectsAlongI && !bConnectsAlongK) {
         bConnectsAlongI = true;
      }

      float var6x = 0.4375F;
      float var7 = 0.5625F;
      float var14 = 0.75F;
      float var15 = 0.9375F;
      float var16 = bConnectsNegativeI ? 0.0F : var6x;
      float var17 = bConnectsPositiveI ? 1.0F : var7;
      float var18 = bConnectsNegativeK ? 0.0F : var6x;
      float var19 = bConnectsPositiveK ? 1.0F : var7;
      if (bConnectsAlongI) {
         rayTrace.addBoxWithLocalCoordsToIntersectionList(var16, var14, var6x, var17, var15, var7);
      }

      if (bConnectsAlongK) {
         rayTrace.addBoxWithLocalCoordsToIntersectionList(var6x, var14, var18, var7, var15, var19);
      }

      var14 = 0.375F;
      var15 = 0.5625F;
      if (bConnectsAlongI) {
         rayTrace.addBoxWithLocalCoordsToIntersectionList(var16, var14, var6x, var17, var15, var7);
      }

      if (bConnectsAlongK) {
         rayTrace.addBoxWithLocalCoordsToIntersectionList(var6x, var14, var18, var7, var15, var19);
      }

      return rayTrace.getFirstIntersection();
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void getSubBlocks(int iBlockID, CreativeTabs creativeTabs, List list) {
      super.getSubBlocks(iBlockID, creativeTabs, list);
      list.add(new ItemStack(iBlockID, 1, 12));
      if (iBlockID != BTWBlocks.netherBrickSidingAndCorner.blockID) {
         list.add(new ItemStack(iBlockID, 1, 14));
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean shouldSideBeRendered(IBlockAccess blockAccess, int iNeighborI, int iNeighborJ, int iNeighborK, int iSide) {
      BlockPos thisPos = new BlockPos(iNeighborI, iNeighborJ, iNeighborK, Block.getOppositeFacing(iSide));
      int iSubtype = blockAccess.getBlockMetadata(thisPos.x, thisPos.y, thisPos.z);
      return iSubtype != 14 && iSubtype != 12 ? super.shouldSideBeRendered(blockAccess, iNeighborI, iNeighborJ, iNeighborK, iSide) : true;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean renderBlock(RenderBlocks renderBlocks, int i, int j, int k) {
      IBlockAccess blockAccess = renderBlocks.blockAccess;
      int iSubtype = blockAccess.getBlockMetadata(i, j, k);
      if (iSubtype == 12) {
         return this.renderBench(renderBlocks, i, j, k);
      } else {
         return iSubtype == 14 ? this.renderFence(renderBlocks, i, j, k) : super.renderBlock(renderBlocks, i, j, k);
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void renderBlockAsItem(RenderBlocks renderBlocks, int iItemDamage, float fBrightness) {
      if (iItemDamage == 12) {
         this.renderBenchInvBlock(renderBlocks, this, iItemDamage);
      } else if (iItemDamage == 14) {
         this.renderFenceInvBlock(renderBlocks, this, iItemDamage);
      } else {
         super.renderBlockAsItem(renderBlocks, iItemDamage, fBrightness);
      }
   }

   @Environment(EnvType.CLIENT)
   public boolean renderBench(RenderBlocks renderBlocks, int i, int j, int k) {
      renderBlocks.setRenderBounds(0.0, 0.375, 0.0, 1.0, 0.5, 1.0);
      renderBlocks.renderStandardBlock(this, i, j, k);
      if (this.doesBenchHaveLeg(renderBlocks.blockAccess, i, j, k)) {
         renderBlocks.setRenderBounds(0.375, 0.0, 0.375, 0.625, 0.375, 0.625);
         renderBlocks.renderStandardBlock(this, i, j, k);
      }

      return true;
   }

   @Environment(EnvType.CLIENT)
   public void renderBenchInvBlock(RenderBlocks renderBlocks, Block block, int iItemDamage) {
      renderBlocks.setRenderBounds(0.0, 0.375, 0.0, 1.0, 0.5, 1.0);
      RenderUtils.renderInvBlockWithMetadata(renderBlocks, block, -0.5F, -0.5F, -0.5F, 12);
      renderBlocks.setRenderBounds(0.375, 0.0, 0.375, 0.625, 0.375, 0.625);
      RenderUtils.renderInvBlockWithMetadata(renderBlocks, block, -0.5F, -0.5F, -0.5F, 12);
   }

   @Environment(EnvType.CLIENT)
   public boolean renderFence(RenderBlocks renderBlocks, int i, int j, int k) {
      renderBlocks.setRenderBounds(0.375, 0.0, 0.375, 0.625, 1.0, 0.625);
      renderBlocks.renderStandardBlock(this, i, j, k);
      boolean bConnectsAlongI = false;
      boolean bConnectsNegativeK = FenceBlock.canConnectToBlockToFacing(renderBlocks.blockAccess, i, j, k, 2);
      boolean bConnectsPositiveK = FenceBlock.canConnectToBlockToFacing(renderBlocks.blockAccess, i, j, k, 3);
      boolean bConnectsNegativeI = FenceBlock.canConnectToBlockToFacing(renderBlocks.blockAccess, i, j, k, 4);
      boolean bConnectsPositiveI = FenceBlock.canConnectToBlockToFacing(renderBlocks.blockAccess, i, j, k, 5);
      if (bConnectsNegativeI || bConnectsPositiveI) {
         bConnectsAlongI = true;
      }

      boolean bConnectsAlongK = false;
      if (bConnectsNegativeK || bConnectsPositiveK) {
         bConnectsAlongK = true;
      }

      if (!bConnectsAlongI && !bConnectsAlongK) {
         bConnectsAlongI = true;
      }

      float var6 = 0.4375F;
      float var7 = 0.5625F;
      float var14 = 0.75F;
      float var15 = 0.9375F;
      float var16 = bConnectsNegativeI ? 0.0F : var6;
      float var17 = bConnectsPositiveI ? 1.0F : var7;
      float var18 = bConnectsNegativeK ? 0.0F : var6;
      float var19 = bConnectsPositiveK ? 1.0F : var7;
      if (bConnectsAlongI) {
         renderBlocks.setRenderBounds(var16, var14, var6, var17, var15, var7);
         renderBlocks.renderStandardBlock(this, i, j, k);
      }

      if (bConnectsAlongK) {
         renderBlocks.setRenderBounds(var6, var14, var18, var7, var15, var19);
         renderBlocks.renderStandardBlock(this, i, j, k);
      }

      var14 = 0.375F;
      var15 = 0.5625F;
      if (bConnectsAlongI) {
         renderBlocks.setRenderBounds(var16, var14, var6, var17, var15, var7);
         renderBlocks.renderStandardBlock(this, i, j, k);
      }

      if (bConnectsAlongK) {
         renderBlocks.setRenderBounds(var6, var14, var18, var7, var15, var19);
         renderBlocks.renderStandardBlock(this, i, j, k);
      }

      return true;
   }

   @Environment(EnvType.CLIENT)
   public void renderFenceInvBlock(RenderBlocks renderBlocks, Block block, int itemDamage) {
      Tessellator tessellator = Tessellator.instance;

      for (int i = 0; i < 4; i++) {
         float var5 = 0.125F;
         if (i == 0) {
            renderBlocks.setRenderBounds(0.5F - var5, 0.0, 0.0, 0.5F + var5, 1.0, var5 * 2.0F);
         }

         if (i == 1) {
            renderBlocks.setRenderBounds(0.5F - var5, 0.0, 1.0F - var5 * 2.0F, 0.5F + var5, 1.0, 1.0);
         }

         var5 = 0.0625F;
         if (i == 2) {
            renderBlocks.setRenderBounds(0.5F - var5, 1.0F - var5 * 4.0F, -var5 * 2.0F, 0.5F + var5, 1.0F - var5, 1.0F + var5 * 2.0F);
         }

         if (i == 3) {
            renderBlocks.setRenderBounds(0.5F - var5, 0.5F - var5 * 2.0F, -var5 * 2.0F, 0.5F + var5, 0.5F + var5, 1.0F + var5 * 2.0F);
         }

         GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
         tessellator.startDrawingQuads();
         tessellator.setNormal(0.0F, -1.0F, 0.0F);
         renderBlocks.renderFaceYNeg(block, 0.0, 0.0, 0.0, block.getBlockTextureFromSide(0));
         tessellator.draw();
         tessellator.startDrawingQuads();
         tessellator.setNormal(0.0F, 1.0F, 0.0F);
         renderBlocks.renderFaceYPos(block, 0.0, 0.0, 0.0, block.getBlockTextureFromSide(1));
         tessellator.draw();
         tessellator.startDrawingQuads();
         tessellator.setNormal(0.0F, 0.0F, -1.0F);
         renderBlocks.renderFaceZNeg(block, 0.0, 0.0, 0.0, block.getBlockTextureFromSide(2));
         tessellator.draw();
         tessellator.startDrawingQuads();
         tessellator.setNormal(0.0F, 0.0F, 1.0F);
         renderBlocks.renderFaceZPos(block, 0.0, 0.0, 0.0, block.getBlockTextureFromSide(3));
         tessellator.draw();
         tessellator.startDrawingQuads();
         tessellator.setNormal(-1.0F, 0.0F, 0.0F);
         renderBlocks.renderFaceXNeg(block, 0.0, 0.0, 0.0, block.getBlockTextureFromSide(4));
         tessellator.draw();
         tessellator.startDrawingQuads();
         tessellator.setNormal(1.0F, 0.0F, 0.0F);
         renderBlocks.renderFaceXPos(block, 0.0, 0.0, 0.0, block.getBlockTextureFromSide(5));
         tessellator.draw();
         GL11.glTranslatef(0.5F, 0.5F, 0.5F);
      }

      renderBlocks.setRenderBounds(0.0, 0.0, 0.0, 1.0, 1.0, 1.0);
   }
}
