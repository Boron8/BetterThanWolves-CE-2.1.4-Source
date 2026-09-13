package btw.block.blocks;

import btw.block.BTWBlocks;
import btw.block.model.BlockModel;
import btw.block.model.BucketModel;
import btw.world.util.BlockPos;
import btw.world.util.WorldUtils;
import java.util.Random;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Icon;
import net.minecraft.src.IconRegister;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.MovingObjectPosition;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.Vec3;
import net.minecraft.src.World;

public class BucketBlock extends FallingBlock {
   protected BucketModel model;
   protected BlockModel modelTransformed;
   @Environment(EnvType.CLIENT)
   private Icon iconOpenTop;
   @Environment(EnvType.CLIENT)
   private Icon iconOpenSide;
   @Environment(EnvType.CLIENT)
   private static AxisAlignedBB selectionBox = new AxisAlignedBB(0.28125, 0.0625, 0.28125, 0.71875, 0.5, 0.71875);

   public BucketBlock(int iBlockID) {
      super(iBlockID, BTWBlocks.miscMaterial);
      this.c(0.0F);
      this.b(0.0F);
      this.initBlockBounds(0.28125, 0.0625, 0.28125, 0.71875, 0.5, 0.71875);
      this.a(Block.soundMetalFootstep);
      this.c("bucket");
      this.initModels();
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
      return this.setFacing(iMetadata, iFacing);
   }

   @Override
   public void onBlockPlacedBy(World world, int i, int j, int k, EntityLiving entityLiving, ItemStack stack) {
      this.setFacing(world, i, j, k, 1);
   }

   @Override
   public int idDropped(int iMetadata, Random rand, int iFortuneMod) {
      return Item.bucketEmpty.itemID;
   }

   @Override
   public boolean dropComponentItemsOnBadBreak(World world, int i, int j, int k, int iMetadata, float fChance) {
      this.dropItemsIndividually(world, i, j, k, Item.bucketEmpty.itemID, 1, 0, fChance);
      return true;
   }

   @Override
   public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k) {
      return null;
   }

   @Override
   public MovingObjectPosition collisionRayTrace(World world, int i, int j, int k, Vec3 startRay, Vec3 endRay) {
      this.modelTransformed = this.model.makeTemporaryCopy();
      int iFacing = this.getFacing(world, i, j, k);
      this.modelTransformed.tiltToFacingAlongY(iFacing);
      BucketModel.offsetModelForFacing(this.modelTransformed, iFacing);
      return this.modelTransformed.collisionRayTrace(world, i, j, k, startRay, endRay);
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
   public int onPreBlockPlacedByPiston(World world, int i, int j, int k, int iMetadata, int iDirectionMoved) {
      if (!WorldUtils.doesBlockHaveCenterHardpointToFacing(world, i, j - 1, k, 1, true) && iDirectionMoved >= 2) {
         int iFacing = this.getFacing(iMetadata);
         if (iFacing == 0) {
            iFacing = Block.getOppositeFacing(iDirectionMoved);
         } else if (iFacing == 1) {
            iFacing = iDirectionMoved;
         } else if (iFacing == iDirectionMoved) {
            iFacing = 0;
         } else if (iFacing == Block.getOppositeFacing(iDirectionMoved)) {
            iFacing = 1;
         }

         iMetadata = this.setFacing(iMetadata, iFacing);
      }

      return iMetadata;
   }

   @Override
   public boolean getPreventsFluidFlow(World world, int i, int j, int k, Block fluidBlock) {
      return false;
   }

   @Override
   public boolean canGroundCoverRestOnBlock(World world, int i, int j, int k) {
      return world.doesBlockHaveSolidTopSurface(i, j - 1, k);
   }

   @Override
   public float groundCoverRestingOnVisualOffset(IBlockAccess blockAccess, int i, int j, int k) {
      return -1.0F;
   }

   protected void initModels() {
      this.model = new BucketModel();
      this.modelTransformed = this.model;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void registerIcons(IconRegister register) {
      this.blockIcon = register.registerIcon("fcBlockBucketEmpty");
      this.iconOpenTop = register.registerIcon("fcBlockBucketEmpty_top");
      this.iconOpenSide = register.registerIcon("fcBlockBucketEmpty_top_side");
   }

   @Environment(EnvType.CLIENT)
   @Override
   public Icon getIcon(int iSide, int iMetadata) {
      if (this.modelTransformed.getActivePrimitiveID() == 2) {
         int iFacing = this.getFacing(iMetadata);
         if (iFacing == iSide || iFacing == Block.getOppositeFacing(iSide)) {
            if (iFacing < 2) {
               return this.iconOpenTop;
            }

            return this.iconOpenSide;
         }
      }

      return super.a(iSide, iMetadata);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean shouldSideBeRendered(IBlockAccess blockAccess, int iNeighborI, int iNeighborJ, int iNeighborK, int iSide) {
      BlockPos myPos = new BlockPos(iNeighborI, iNeighborJ, iNeighborK, Block.getOppositeFacing(iSide));
      int iMetadata = blockAccess.getBlockMetadata(myPos.x, myPos.y, myPos.z);
      return this.shouldSideBeRenderedOnFallingBlock(iSide, iMetadata);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean shouldSideBeRenderedOnFallingBlock(int iSide, int iMetadata) {
      int iFacing = this.getFacing(iMetadata);
      int iActiveID = this.modelTransformed.getActivePrimitiveID();
      if (iActiveID == 3) {
         return iSide != Block.getOppositeFacing(iFacing);
      } else {
         return iSide == iFacing ? iActiveID == 2 : true;
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean renderBlock(RenderBlocks renderBlocks, int i, int j, int k) {
      this.modelTransformed = this.model.makeTemporaryCopy();
      int iFacing = this.getFacing(renderBlocks.blockAccess, i, j, k);
      this.modelTransformed.tiltToFacingAlongY(iFacing);
      BucketModel.offsetModelForFacing(this.modelTransformed, iFacing);
      return this.modelTransformed.renderAsBlock(renderBlocks, this, i, j, k);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void renderBlockAsItem(RenderBlocks renderBlocks, int iItemDamage, float fBrightness) {
      this.modelTransformed = this.model;
      this.modelTransformed.renderAsItemBlock(renderBlocks, this, iItemDamage);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void renderFallingBlock(RenderBlocks renderBlocks, int i, int j, int k, int iMetadata) {
      this.modelTransformed = this.model.makeTemporaryCopy();
      int iFacing = this.getFacing(iMetadata);
      this.modelTransformed.tiltToFacingAlongY(iFacing);
      BucketModel.offsetModelForFacing(this.modelTransformed, iFacing);
      this.modelTransformed.renderAsFallingBlock(renderBlocks, this, i, j, k, iMetadata);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void renderBlockMovedByPiston(RenderBlocks renderBlocks, int i, int j, int k) {
      this.renderBlock(renderBlocks, i, j, k);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, MovingObjectPosition rayTraceHit) {
      int i = rayTraceHit.blockX;
      int j = rayTraceHit.blockY;
      int k = rayTraceHit.blockZ;
      int iFacing = this.getFacing(world, i, j, k);
      AxisAlignedBB tempBox = selectionBox.makeTemporaryCopy();
      if (iFacing != 1) {
         tempBox.tiltToFacingAlongY(iFacing);
         Vec3 offset = BucketModel.getOffsetForFacing(iFacing);
         tempBox.translate(offset.xCoord, offset.yCoord, offset.zCoord);
      }

      return tempBox.offset(i, j, k);
   }
}
