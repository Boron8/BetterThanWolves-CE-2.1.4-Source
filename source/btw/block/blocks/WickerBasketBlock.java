package btw.block.blocks;

import btw.block.model.BlockModel;
import btw.block.tileentity.WickerBasketTileEntity;
import btw.block.util.RayTraceUtils;
import btw.item.BTWItems;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.EntityFallingSand;
import net.minecraft.src.EntityItem;
import net.minecraft.src.EntityList;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Icon;
import net.minecraft.src.IconRegister;
import net.minecraft.src.ItemStack;
import net.minecraft.src.MovingObjectPosition;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.TileEntity;
import net.minecraft.src.Vec3;
import net.minecraft.src.World;

public class WickerBasketBlock extends BasketBlock {
   private static final float BASKET_OPEN_HEIGHT = 0.75F;
   private static final float BASKET_HEIGHT = 0.5F;
   private static final float BASKET_RIM_WIDTH = 0.0625F;
   private static final float BASKET_WIDTH_LIP = 0.0F;
   private static final float BASKET_DEPTH_LIP = 0.0625F;
   private static final float BASKET_LID_HEIGHT = 0.125F;
   private static final float BASKET_LID_LAYER_HEIGHT = 0.0625F;
   private static final float BASKET_LID_LAYER_WIDTH_GAP = 0.0625F;
   private static final float BASKET_HANDLE_HEIGHT = 0.0625F;
   private static final float BASKET_HANDLE_WIDTH = 0.125F;
   private static final float BASKET_HANDLE_HALF_WIDTH = 0.0625F;
   private static final float BASKET_HANDLE_LENGTH = 0.25F;
   private static final float BASKET_HANDLE_HALF_LENGTH = 0.125F;
   private static final float BASKET_INTERIOR_WALL_THICKNESS = 0.0625F;
   private static final float MIND_THE_GAP = 0.001F;
   private static final double LID_OPEN_LIP_HEIGHT = 0.0625;
   private static final double LID_OPEN_LIP_Y_POS = 0.9375;
   private static final double LID_OPEN_LIP_WIDTH = 0.125;
   private static final double LID_OPEN_LIP_LENGTH = 1.0;
   private static final double LID_OPEN_LIP_HALF_LENGTH = 0.5;
   private static final double LID_OPEN_LIP_HORIZONTAL_OFFSET = 0.3125;
   public BlockModel blockModelBase;
   public BlockModel blockModelBaseOpenCollision;
   public BlockModel blockModelLid;
   public BlockModel blockModelLidFull;
   public BlockModel blockModelInterior;
   private static AxisAlignedBB boxCollisionLidOpenLip = new AxisAlignedBB(0.0, 0.9375, 0.3125, 1.0, 1.0, 0.4375);
   private static final Vec3 lidRotationPoint = Vec3.createVectorHelper(0.5, 0.375, 0.875);
   @Environment(EnvType.CLIENT)
   private Icon iconBaseOpenTop;
   @Environment(EnvType.CLIENT)
   private Icon iconFront;
   @Environment(EnvType.CLIENT)
   private Icon iconTop;
   @Environment(EnvType.CLIENT)
   private Icon iconBottom;
   @Environment(EnvType.CLIENT)
   private boolean renderingBase = false;
   @Environment(EnvType.CLIENT)
   private boolean renderingInterior = false;

   public WickerBasketBlock(int iBlockID) {
      super(iBlockID);
      this.initBlockBounds(0.0, 0.0, 0.0, 1.0, 0.5, 1.0);
      this.initModelBase();
      this.initModelBaseOpenCollison();
      this.initModelLid();
      this.initModelLidFull();
      this.initModelInterior();
      this.c("fcBlockBasketWicker");
   }

   @Override
   public TileEntity createNewTileEntity(World world) {
      return new WickerBasketTileEntity();
   }

   @Override
   public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer player, int iFacing, float fXClick, float fYClick, float fZClick) {
      int iMetadata = world.getBlockMetadata(i, j, k);
      if (!this.getIsOpen(iMetadata)) {
         if (!world.isRemote) {
            this.setIsOpen(world, i, j, k, true);
         } else {
            player.playSound("step.gravel", 0.25F + world.rand.nextFloat() * 0.1F, 0.5F + world.rand.nextFloat() * 0.1F);
         }

         return true;
      } else {
         if (this.isClickingOnLid(world, i, j, k, iFacing, fXClick, fYClick, fZClick)) {
            WickerBasketTileEntity tileEntity = (WickerBasketTileEntity)world.getBlockTileEntity(i, j, k);
            if (!tileEntity.closing) {
               if (!world.isRemote) {
                  tileEntity.startClosingServerSide();
               }

               return true;
            }
         } else {
            if (this.getHasContents(iMetadata)) {
               if (world.isRemote) {
                  player.playSound("step.gravel", 0.5F + world.rand.nextFloat() * 0.25F, 1.0F + world.rand.nextFloat() * 0.25F);
               } else {
                  this.ejectStorageStack(world, i, j, k);
               }

               this.setHasContents(world, i, j, k, false);
               return true;
            }

            ItemStack heldStack = player.getCurrentEquippedItem();
            if (heldStack != null) {
               if (world.isRemote) {
                  player.playSound("step.gravel", 0.5F + world.rand.nextFloat() * 0.25F, 0.5F + world.rand.nextFloat() * 0.25F);
               } else {
                  WickerBasketTileEntity tileEntity = (WickerBasketTileEntity)world.getBlockTileEntity(i, j, k);
                  tileEntity.setStorageStack(heldStack);
               }

               heldStack.stackSize = 0;
               this.setHasContents(world, i, j, k, true);
               return true;
            }
         }

         return false;
      }
   }

   private void ejectStorageStack(World world, int i, int j, int k) {
      WickerBasketTileEntity tileEntity = (WickerBasketTileEntity)world.getBlockTileEntity(i, j, k);
      ItemStack storageStack = tileEntity.getStorageStack();
      if (storageStack != null) {
         float xOffset = 0.5F;
         float yOffset = 0.4F;
         float zOffset = 0.5F;
         double xPos = i + xOffset;
         double yPos = j + yOffset;
         double zPos = k + zOffset;
         EntityItem entityitem = (EntityItem)EntityList.createEntityOfType(EntityItem.class, world, xPos, yPos, zPos, storageStack);
         entityitem.motionY = 0.2;
         double fFacingFactor = 0.15;
         double fRandomFactor = 0.05;
         int iFacing = this.getFacing(world, i, j, k);
         if (iFacing <= 3) {
            entityitem.motionX = (world.rand.nextDouble() * 2.0 - 1.0) * fRandomFactor;
            if (iFacing == 2) {
               entityitem.motionZ = -fFacingFactor;
            } else {
               entityitem.motionZ = fFacingFactor;
            }
         } else {
            entityitem.motionZ = (world.rand.nextDouble() * 2.0 - 1.0) * fRandomFactor;
            if (iFacing == 4) {
               entityitem.motionX = -fFacingFactor;
            } else {
               entityitem.motionX = fFacingFactor;
            }
         }

         entityitem.delayBeforeCanPickup = 10;
         world.spawnEntityInWorld(entityitem);
         tileEntity.setStorageStack(null);
      }
   }

   @Override
   public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k) {
      return this.getFixedBlockBoundsFromPool().offset(i, j, k);
   }

   @Override
   public AxisAlignedBB getBlockBoundsFromPoolBasedOnState(IBlockAccess blockAccess, int i, int j, int k) {
      return !this.getIsOpen(blockAccess, i, j, k)
         ? AxisAlignedBB.getAABBPool().getAABB(0.0, 0.0, 0.0, 1.0, 0.5, 1.0)
         : AxisAlignedBB.getAABBPool().getAABB(0.0, 0.0, 0.0, 1.0, 0.75, 1.0);
   }

   @Override
   public MovingObjectPosition collisionRayTrace(World world, int i, int j, int k, Vec3 startRay, Vec3 endRay) {
      RayTraceUtils rayTrace = new RayTraceUtils(world, i, j, k, startRay, endRay);
      int iMetadata = world.getBlockMetadata(i, j, k);
      int iFacing = this.getFacing(iMetadata);
      BlockModel tempBaseModel;
      if (!this.getIsOpen(iMetadata)) {
         tempBaseModel = this.blockModelBase.makeTemporaryCopy();
         BlockModel tempLidModel;
         if (this.getHasContents(iMetadata)) {
            tempLidModel = this.blockModelLidFull.makeTemporaryCopy();
         } else {
            tempLidModel = this.blockModelLid.makeTemporaryCopy();
         }

         tempLidModel.rotateAroundYToFacing(iFacing);
         tempLidModel.addToRayTrace(rayTrace);
      } else {
         tempBaseModel = this.blockModelBaseOpenCollision.makeTemporaryCopy();
         WickerBasketTileEntity tileEntity = (WickerBasketTileEntity)world.getBlockTileEntity(i, j, k);
         if (tileEntity.lidOpenRatio > 0.95F) {
            AxisAlignedBB tempLidBox = boxCollisionLidOpenLip.makeTemporaryCopy();
            tempLidBox.rotateAroundYToFacing(iFacing);
            rayTrace.addBoxWithLocalCoordsToIntersectionList(tempLidBox);
         }
      }

      tempBaseModel.rotateAroundYToFacing(iFacing);
      tempBaseModel.addToRayTrace(rayTrace);
      return rayTrace.getFirstIntersection();
   }

   @Override
   public void onCrushedByFallingEntity(World world, int i, int j, int k, EntityFallingSand entity) {
      if (!world.isRemote) {
         this.dropItemsIndividually(world, i, j, k, BTWItems.wickerPane.itemID, 1, 0, 0.75F);
      }
   }

   @Override
   public BlockModel getLidModel(int iMetadata) {
      return this.getHasContents(iMetadata) ? this.blockModelLidFull : this.blockModelLid;
   }

   @Override
   public Vec3 getLidRotationPoint() {
      return lidRotationPoint;
   }

   @Override
   public float mobSpawnOnVerticalOffset(World world, int i, int j, int k) {
      return -0.5F;
   }

   private void initModelBase() {
      this.blockModelBase = new BlockModel();
      this.blockModelBase.addBox(0.0625, 0.0, 0.125, 0.9375, 0.375, 0.875);
   }

   private void initModelBaseOpenCollison() {
      this.blockModelBaseOpenCollision = new BlockModel();
      this.blockModelBaseOpenCollision.addBox(0.0625, 0.0, 0.125, 0.9375, 0.75, 0.875);
   }

   private void initModelLid() {
      this.blockModelLid = new BlockModel();
      this.blockModelLid.addBox(0.0, 0.375, 0.0625, 1.0, 0.4375, 0.9375);
      this.blockModelLid.addBox(0.0625, 0.4375, 0.125, 0.9375, 0.5, 0.875);
      this.blockModelLid.addBox(0.375, 0.5, 0.4375, 0.625, 0.5625, 0.5625);
   }

   private void initModelLidFull() {
      this.blockModelLidFull = new BlockModel();
      this.blockModelLidFull.addBox(0.0, 0.375, 0.0625, 1.0, 0.4375, 0.9375);
      this.blockModelLidFull.addBox(0.0625, 0.4375, 0.125, 0.9375, 0.5, 0.875);
      this.blockModelLidFull.addBox(0.125, 0.5, 0.1875, 0.875, 0.5625, 0.8125);
   }

   private void initModelInterior() {
      this.blockModelInterior = new BlockModel();
      this.blockModelInterior.addBox(0.8760000000474975, 0.375, 0.8135000000474975, 0.124F, 0.0625, 0.1865F);
   }

   private boolean isClickingOnLid(World world, int i, int j, int k, int iSideClicked, float fXClick, float fYClick, float fZClick) {
      return fYClick > 0.75F;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void registerIcons(IconRegister register) {
      super.a(register);
      this.iconBaseOpenTop = register.registerIcon("fcBlockBasketWicker_open_top");
      this.iconFront = register.registerIcon("fcBlockBasketWicker_front");
      this.iconTop = register.registerIcon("fcBlockBasketWicker_top");
      this.iconBottom = register.registerIcon("fcBlockBasketWicker_bottom");
   }

   @Environment(EnvType.CLIENT)
   @Override
   public Icon getIcon(int iSide, int iMetadata) {
      if (iSide == 1 && this.renderingBase) {
         return this.iconBaseOpenTop;
      } else {
         if (!this.renderingInterior) {
            if (iSide == 1) {
               return this.iconTop;
            }

            if (iSide == 0) {
               return this.iconBottom;
            }

            int iFacing = this.getFacing(iMetadata);
            if (iSide == iFacing) {
               return this.iconFront;
            }
         }

         return super.a(iSide, iMetadata);
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean shouldSideBeRendered(IBlockAccess blockAccess, int iNeighborI, int iNeighborJ, int iNeighborK, int iSide) {
      if (iSide == 0) {
         return this.renderingInterior ? false : !this.renderingBase || super.a(blockAccess, iNeighborI, iNeighborJ, iNeighborK, iSide);
      } else {
         return true;
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean renderBlock(RenderBlocks renderer, int i, int j, int k) {
      int iMetadata = renderer.blockAccess.getBlockMetadata(i, j, k);
      int iFacing = this.getFacing(iMetadata);
      this.renderingBase = true;
      BlockModel transformedModel = this.blockModelBase.makeTemporaryCopy();
      transformedModel.rotateAroundYToFacing(this.getFacing(renderer.blockAccess, i, j, k));
      renderer.setUVRotateTop(this.convertFacingToTopTextureRotation(iFacing));
      renderer.setUVRotateBottom(this.convertFacingToBottomTextureRotation(iFacing));
      boolean bReturnValue = transformedModel.renderAsBlock(renderer, this, i, j, k);
      this.renderingBase = false;
      if (!this.getIsOpen(iMetadata)) {
         if (this.getHasContents(iMetadata)) {
            transformedModel = this.blockModelLidFull.makeTemporaryCopy();
         } else {
            transformedModel = this.blockModelLid.makeTemporaryCopy();
         }

         transformedModel.rotateAroundYToFacing(this.getFacing(renderer.blockAccess, i, j, k));
         transformedModel.renderAsBlockWithColorMultiplier(renderer, this, i, j, k);
      } else {
         transformedModel = this.blockModelInterior.makeTemporaryCopy();
         transformedModel.rotateAroundYToFacing(iFacing);
         this.renderingInterior = true;
         transformedModel.renderAsBlockWithColorMultiplier(renderer, this, i, j, k);
         this.renderingInterior = false;
      }

      renderer.clearUVRotation();
      return bReturnValue;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void renderBlockAsItem(RenderBlocks renderBlocks, int iItemDamage, float fBrightness) {
      this.blockModelLid.renderAsItemBlock(renderBlocks, this, iItemDamage);
      this.blockModelBase.renderAsItemBlock(renderBlocks, this, iItemDamage);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, MovingObjectPosition rayTraceHit) {
      int i = rayTraceHit.blockX;
      int j = rayTraceHit.blockY;
      int k = rayTraceHit.blockZ;
      int iMetadata = world.getBlockMetadata(i, j, k);
      int iFacing = this.getFacing(iMetadata);
      double minYBox = j;
      double maxYBox = j + 0.5F;
      if (this.getIsOpen(iMetadata)) {
         if (rayTraceHit.hitVec.yCoord - minYBox >= 0.9364999999525025) {
            AxisAlignedBB tempLidBox = boxCollisionLidOpenLip.makeTemporaryCopy();
            tempLidBox.rotateAroundYToFacing(iFacing);
            return tempLidBox.offset(i, j, k);
         }

         maxYBox -= 0.125;
      }

      double minXBox;
      double maxXBox;
      double minZBox;
      double maxZBox;
      if (iFacing != 2 && iFacing != 3) {
         minXBox = i + 0.0625 + 0.0625;
         maxXBox = i + 1.0 - 0.0625 - 0.0625;
         minZBox = k + 0.0625 + 0.0;
         maxZBox = k + 1.0 - 0.0625 - 0.0;
      } else {
         minXBox = i + 0.0625 + 0.0;
         maxXBox = i + 1.0 - 0.0625 - 0.0;
         minZBox = k + 0.0625 + 0.0625;
         maxZBox = k + 1.0 - 0.0625 - 0.0625;
      }

      return AxisAlignedBB.getAABBPool().getAABB(minXBox, minYBox, minZBox, maxXBox, maxYBox, maxZBox);
   }
}
