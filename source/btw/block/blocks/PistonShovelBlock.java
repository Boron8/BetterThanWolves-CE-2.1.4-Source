package btw.block.blocks;

import btw.block.model.BlockModel;
import btw.block.model.PistonShovelModel;
import btw.block.util.RayTraceUtils;
import btw.util.MiscUtils;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Icon;
import net.minecraft.src.IconRegister;
import net.minecraft.src.Material;
import net.minecraft.src.MathHelper;
import net.minecraft.src.MovingObjectPosition;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.Vec3;
import net.minecraft.src.World;

public class PistonShovelBlock extends Block {
   protected PistonShovelModel model = new PistonShovelModel();
   @Environment(EnvType.CLIENT)
   private Icon iconEdge;
   @Environment(EnvType.CLIENT)
   private Icon iconEdgeBack;
   @Environment(EnvType.CLIENT)
   private Icon iconEdgeMiddle;

   public PistonShovelBlock(int iBlockID) {
      super(iBlockID, Material.iron);
      this.c(5.0F);
      this.setPicksEffectiveOn();
      this.initBlockBounds(0.0, 0.0, 0.0, 1.0, 1.0, 1.0);
      this.a(k);
      this.c("fcBlockShovel");
      this.a(CreativeTabs.tabRedstone);
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
   public void addCollisionBoxesToList(World world, int i, int j, int k, AxisAlignedBB boundingBox, List list, Entity entity) {
      BlockModel transformedModel = this.getTransformedModelForMetadata(this.model.collisionModel, world.getBlockMetadata(i, j, k));
      transformedModel.addIntersectingBoxesToCollisionList(world, i, j, k, boundingBox, list);
   }

   @Override
   public int onBlockPlaced(World world, int i, int j, int k, int iFacing, float fClickX, float fClickY, float fClickZ, int iMetadata) {
      int iVerticalOrientation = 1;
      int iBlockFacing = 2;
      if (iFacing >= 2) {
         if (fClickY > 0.5F) {
            iVerticalOrientation = 0;
         }

         iBlockFacing = iFacing;
      } else if (iFacing == 0) {
         iVerticalOrientation = 0;
      }

      iMetadata = this.setFacing(iMetadata, iBlockFacing);
      return this.setVerticalOrientation(iMetadata, iVerticalOrientation);
   }

   @Override
   public int preBlockPlacedBy(World world, int i, int j, int k, int iMetadata, EntityLiving entityBy) {
      int iFacing = MiscUtils.convertOrientationToFlatBlockFacingReversed(entityBy);
      return this.setFacing(iMetadata, iFacing);
   }

   @Override
   public MovingObjectPosition collisionRayTrace(World world, int i, int j, int k, Vec3 startRay, Vec3 endRay) {
      RayTraceUtils rayTrace = new RayTraceUtils(world, i, j, k, startRay, endRay);
      BlockModel transformedModel = this.getTransformedModelForMetadata(this.model.rayTraceModel, world.getBlockMetadata(i, j, k));
      transformedModel.addToRayTrace(rayTrace);
      return rayTrace.getFirstIntersection();
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
   public boolean canRotateOnTurntable(IBlockAccess iBlockAccess, int i, int j, int k) {
      return true;
   }

   @Override
   public boolean canTransmitRotationHorizontallyOnTurntable(IBlockAccess blockAccess, int i, int j, int k) {
      return true;
   }

   @Override
   public boolean canTransmitRotationVerticallyOnTurntable(IBlockAccess blockAccess, int i, int j, int k) {
      return true;
   }

   @Override
   public int getPistonShovelEjectDirection(World world, int i, int j, int k, int iToFacing) {
      int iMetadata = world.getBlockMetadata(i, j, k);
      if (iToFacing >= 2) {
         if (iToFacing == this.getFacing(iMetadata)) {
            return this.getVerticalOrientation(iMetadata);
         }
      } else if (iToFacing == this.getVerticalOrientation(iMetadata)) {
         return this.getFacing(iMetadata);
      }

      return -1;
   }

   public int getVerticalOrientation(IBlockAccess blockAccess, int i, int j, int k) {
      return this.getVerticalOrientation(blockAccess.getBlockMetadata(i, j, k));
   }

   public int getVerticalOrientation(int iMetadata) {
      return (iMetadata & 12) >> 2;
   }

   public void setVerticalOrientation(World world, int i, int j, int k, int iLevel) {
      int iMetadata = this.setVerticalOrientation(world.getBlockMetadata(i, j, k), iLevel);
      world.setBlockMetadataWithNotify(i, j, k, iMetadata);
   }

   public int setVerticalOrientation(int iMetadata, int iLevel) {
      iMetadata &= -13;
      return iMetadata | iLevel << 2;
   }

   private BlockModel getTransformedModelForMetadata(BlockModel model, int iMetadata) {
      BlockModel transformedModel = model.makeTemporaryCopy();
      if (this.getVerticalOrientation(iMetadata) == 0) {
         transformedModel.tiltToFacingAlongY(0);
      }

      transformedModel.rotateAroundYToFacing(this.getFacing(iMetadata));
      return transformedModel;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void registerIcons(IconRegister register) {
      super.registerIcons(register);
      this.iconEdge = register.registerIcon("fcBlockShovel_edge");
      this.iconEdgeBack = register.registerIcon("fcBlockShovel_edge_back");
      this.iconEdgeMiddle = register.registerIcon("fcBlockShovel_edge_middle");
   }

   @Environment(EnvType.CLIENT)
   @Override
   public Icon getIconByIndex(int iIndex) {
      if (iIndex == 1) {
         return this.iconEdge;
      } else if (iIndex == 2) {
         return this.iconEdgeBack;
      } else {
         return iIndex == 3 ? this.iconEdgeMiddle : this.blockIcon;
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean shouldSideBeRendered(IBlockAccess blockAccess, int iNeighborI, int iNeighborJ, int iNeighborK, int iSide) {
      return this.currentBlockRenderer.shouldSideBeRenderedBasedOnCurrentBounds(iNeighborI, iNeighborJ, iNeighborK, iSide);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean renderBlock(RenderBlocks renderBlocks, int i, int j, int k) {
      BlockModel transformedModel = this.getTransformedModelForMetadata(this.model, renderBlocks.blockAccess.getBlockMetadata(i, j, k));
      return transformedModel.renderAsBlockWithColorMultiplier(renderBlocks, this, i, j, k);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void renderBlockAsItem(RenderBlocks renderBlocks, int iItemDamage, float fBrightness) {
      BlockModel transformedModel = this.model.makeTemporaryCopy();
      transformedModel.rotateAroundYToFacing(3);
      transformedModel.renderAsItemBlock(renderBlocks, this, iItemDamage);
   }
}
