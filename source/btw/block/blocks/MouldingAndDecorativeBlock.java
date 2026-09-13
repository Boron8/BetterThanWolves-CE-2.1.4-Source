package btw.block.blocks;

import btw.block.BTWBlocks;
import btw.block.util.RayTraceUtils;
import btw.client.render.util.RenderUtils;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.Entity;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Icon;
import net.minecraft.src.IconRegister;
import net.minecraft.src.Material;
import net.minecraft.src.MovingObjectPosition;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.StepSound;
import net.minecraft.src.Vec3;
import net.minecraft.src.World;

public class MouldingAndDecorativeBlock extends MouldingBlock {
   public static final int SUBTYPE_COLUMN = 12;
   public static final int SUBTYPE_PEDESTAL_UP = 13;
   public static final int SUBTYPE_PEDESTAL_DOWN = 14;
   public static final int SUBTYPE_TABLE = 15;
   protected static final double COLUM_WIDTH = 0.625;
   protected static final double COLUM_HALF_WIDTH = 0.3125;
   protected static final double PEDESTAL_BASE_HEIGHT = 0.75;
   protected static final double PEDESTAL_MIDDLE_HEIGHT = 0.125;
   protected static final double PEDESTAL_MIDDLE_WIDTH = 0.875;
   protected static final double PEDESTAL_MIDDLE_HALF_WIDTH = 0.4375;
   protected static final double PEDESTAL_TOP_HEIGHT = 0.125;
   protected static final double PEDESTAL_TOP_WIDTH = 0.75;
   protected static final double PEDESTAL_TOP_HALF_WIDTH = 0.375;
   protected static final double TABLE_TOP_HEIGHT = 0.125;
   protected static final double TABLE_LEG_HEIGHT = 0.875;
   protected static final double TABLE_LEG_WIDTH = 0.25;
   protected static final double TABLE_LEG_HALF_WIDTH = 0.125;
   String columnSideTextureName;
   String columnTopAndBottomTextureName;
   String pedestalSideTextureName;
   String pedestalTopAndBottomTextureName;
   @Environment(EnvType.CLIENT)
   private Icon iconColumnSide;
   @Environment(EnvType.CLIENT)
   private Icon iconColumnTopAndBottom;
   @Environment(EnvType.CLIENT)
   private Icon iconPedestalSide;
   @Environment(EnvType.CLIENT)
   private Icon iconPedestalTopAndBottom;

   public MouldingAndDecorativeBlock(
      int iBlockID,
      Material material,
      String sTextureName,
      String sColumnSideTextureName,
      int iMatchingCornerBlockID,
      float fHardness,
      float fResistance,
      StepSound stepSound,
      String name
   ) {
      super(iBlockID, material, sTextureName, iMatchingCornerBlockID, fHardness, fResistance, stepSound, name);
      this.columnSideTextureName = sColumnSideTextureName;
      this.columnTopAndBottomTextureName = sTextureName;
      this.pedestalSideTextureName = sTextureName;
      this.pedestalTopAndBottomTextureName = sTextureName;
   }

   public MouldingAndDecorativeBlock(
      int iBlockID,
      Material material,
      String sTextureName,
      String sColumnSideTextureName,
      String sColumnTopAndBottomTextureName,
      String sPedestalSideTextureName,
      String sPedestalTopAndBottomTextureName,
      int iMatchingCornerBlockID,
      float fHardness,
      float fResistance,
      StepSound stepSound,
      String name
   ) {
      super(iBlockID, material, sTextureName, iMatchingCornerBlockID, fHardness, fResistance, stepSound, name);
      this.columnSideTextureName = sColumnSideTextureName;
      this.columnTopAndBottomTextureName = sColumnTopAndBottomTextureName;
      this.pedestalSideTextureName = sPedestalSideTextureName;
      this.pedestalTopAndBottomTextureName = sPedestalTopAndBottomTextureName;
   }

   @Override
   public int damageDropped(int iMetadata) {
      if (!this.isDecorative(iMetadata)) {
         return super.a(iMetadata);
      } else {
         if (iMetadata == 14) {
            iMetadata = 13;
         }

         return iMetadata;
      }
   }

   @Override
   public int onBlockPlaced(World world, int i, int j, int k, int iFacing, float fClickX, float fClickY, float fClickZ, int iMetadata) {
      if (iMetadata == 13) {
         if (iFacing == 0 || iFacing != 1 && fClickY > 0.5) {
            return 14;
         }
      } else if (!this.isDecorative(iMetadata)) {
         return super.onBlockPlaced(world, i, j, k, iFacing, fClickX, fClickY, fClickZ, iMetadata);
      }

      return iMetadata;
   }

   @Override
   public AxisAlignedBB getBlockBoundsFromPoolBasedOnState(IBlockAccess blockAccess, int i, int j, int k) {
      int iMetadata = blockAccess.getBlockMetadata(i, j, k);
      if (this.isDecorative(iMetadata)) {
         switch (iMetadata) {
            case 12:
               return AxisAlignedBB.getAABBPool().getAABB(0.1875, 0.0, 0.1875, 0.8125, 1.0, 0.8125);
            case 13:
            case 14:
               return AxisAlignedBB.getAABBPool().getAABB(0.0, 0.0, 0.0, 1.0, 1.0, 1.0);
            case 15:
               if (!this.doesTableHaveLeg(blockAccess, i, j, k)) {
                  return AxisAlignedBB.getAABBPool().getAABB(0.0, 0.875, 0.0, 1.0, 1.0, 1.0);
               }

               return AxisAlignedBB.getAABBPool().getAABB(0.0, 0.0, 0.0, 1.0, 1.0, 1.0);
            default:
               return AxisAlignedBB.getAABBPool().getAABB(0.0, 0.0, 0.0, 1.0, 1.0, 1.0);
         }
      } else {
         return super.getBlockBoundsFromPoolBasedOnState(blockAccess, i, j, k);
      }
   }

   @Override
   public MovingObjectPosition collisionRayTrace(World world, int i, int j, int k, Vec3 startRay, Vec3 endRay) {
      if (this.isDecorative(world, i, j, k)) {
         return this.isBlockTable(world, i, j, k) && this.doesTableHaveLeg(world, i, j, k)
            ? this.collisionRayTraceTableWithLeg(world, i, j, k, startRay, endRay)
            : this.collisionRayTraceVsBlockBounds(world, i, j, k, startRay, endRay);
      } else {
         return super.collisionRayTrace(world, i, j, k, startRay, endRay);
      }
   }

   @Override
   public void addCollisionBoxesToList(World world, int i, int j, int k, AxisAlignedBB intersectingBox, List list, Entity entity) {
      if (this.isDecorative(world, i, j, k)) {
         this.b(world, i, j, k).addToListIfIntersects(intersectingBox, list);
      } else {
         super.addCollisionBoxesToList(world, i, j, k, intersectingBox, list, entity);
      }
   }

   @Override
   public boolean hasCenterHardPointToFacing(IBlockAccess blockAccess, int i, int j, int k, int iFacing, boolean bIgnoreTransparency) {
      int iSubtype = blockAccess.getBlockMetadata(i, j, k);
      switch (iSubtype) {
         case 12:
            return iFacing == 0 || iFacing == 1;
         case 13:
         case 14:
            return true;
         default:
            return super.hasCenterHardPointToFacing(blockAccess, i, j, k, iFacing, bIgnoreTransparency);
      }
   }

   @Override
   public boolean hasLargeCenterHardPointToFacing(IBlockAccess blockAccess, int i, int j, int k, int iFacing, boolean bIgnoreTransparency) {
      int iSubtype = blockAccess.getBlockMetadata(i, j, k);
      switch (iSubtype) {
         case 13:
            return iFacing == 0;
         case 14:
            return iFacing == 1;
         case 15:
            return iFacing == 1;
         default:
            return super.hasLargeCenterHardPointToFacing(blockAccess, i, j, k, iFacing, bIgnoreTransparency);
      }
   }

   @Override
   public int rotateMetadataAroundJAxis(int iMetadata, boolean bReverse) {
      return !this.isDecorative(iMetadata) ? super.rotateMetadataAroundJAxis(iMetadata, bReverse) : iMetadata;
   }

   @Override
   public boolean toggleFacing(World world, int i, int j, int k, boolean bReverse) {
      return !this.isDecorative(world, i, j, k) ? super.toggleFacing(world, i, j, k, bReverse) : false;
   }

   @Override
   public boolean canTransmitRotationHorizontallyOnTurntable(IBlockAccess blockAccess, int i, int j, int k) {
      int iMetadata = blockAccess.getBlockMetadata(i, j, k);
      return this.isDecorative(iMetadata) ? iMetadata == 13 || iMetadata == 14 : super.canTransmitRotationHorizontallyOnTurntable(blockAccess, i, j, k);
   }

   @Override
   public boolean canTransmitRotationVerticallyOnTurntable(IBlockAccess blockAccess, int i, int j, int k) {
      int iMetadata = blockAccess.getBlockMetadata(i, j, k);
      return this.isDecorative(iMetadata) ? iMetadata != 15 : super.canTransmitRotationVerticallyOnTurntable(blockAccess, i, j, k);
   }

   @Override
   public float mobSpawnOnVerticalOffset(World world, int i, int j, int k) {
      int iSubtype = world.getBlockMetadata(i, j, k);
      return iSubtype < 12 ? super.mobSpawnOnVerticalOffset(world, i, j, k) : 0.0F;
   }

   @Override
   public boolean isBenchOrTable(int metadata) {
      return metadata == 15;
   }

   @Override
   public boolean shouldWallFormPostBelowThisBlock(IBlockAccess blockAccess, int x, int y, int z) {
      return blockAccess.getBlockMetadata(x, y, z) == 15 && this.doesTableHaveLeg(blockAccess, x, y, z);
   }

   @Override
   protected boolean isMouldingOfSameType(IBlockAccess blockAccess, int i, int j, int k) {
      return blockAccess.getBlockId(i, j, k) == this.blockID && !this.isDecorative(blockAccess, i, j, k);
   }

   public boolean isDecorative(IBlockAccess blockAccess, int i, int j, int k) {
      return this.isDecorative(blockAccess.getBlockMetadata(i, j, k));
   }

   public boolean isDecorative(int iMetadata) {
      return iMetadata >= 12;
   }

   public boolean isBlockTable(IBlockAccess blockAccess, int i, int j, int k) {
      return blockAccess.getBlockId(i, j, k) == this.blockID && blockAccess.getBlockMetadata(i, j, k) == 15;
   }

   public boolean doesTableHaveLeg(IBlockAccess blockAccess, int i, int j, int k) {
      int iBlockBelowID = blockAccess.getBlockId(i, j - 1, k);
      if (this.blockID == BTWBlocks.netherBrickMouldingAndDecorative.blockID) {
         if (iBlockBelowID == Block.netherFence.blockID) {
            return true;
         }
      } else if (iBlockBelowID == this.matchingCornerBlockID) {
         int iBlockBelowMetadata = blockAccess.getBlockMetadata(i, j - 1, k);
         if (iBlockBelowMetadata == 14) {
            return true;
         }
      }

      boolean positiveITable = this.isBlockTable(blockAccess, i + 1, j, k);
      boolean negativeITable = this.isBlockTable(blockAccess, i - 1, j, k);
      boolean positiveKTable = this.isBlockTable(blockAccess, i, j, k + 1);
      boolean negativeKTable = this.isBlockTable(blockAccess, i, j, k - 1);
      return !positiveITable && (!positiveKTable || !negativeKTable) || !negativeITable && (!positiveKTable || !negativeKTable);
   }

   public MovingObjectPosition collisionRayTraceTableWithLeg(World world, int i, int j, int k, Vec3 startRay, Vec3 endRay) {
      RayTraceUtils rayTrace = new RayTraceUtils(world, i, j, k, startRay, endRay);
      rayTrace.addBoxWithLocalCoordsToIntersectionList(0.0, 0.875, 0.0, 1.0, 1.0, 1.0);
      rayTrace.addBoxWithLocalCoordsToIntersectionList(0.375, 0.0, 0.375, 0.625, 0.875, 0.625);
      return rayTrace.getFirstIntersection();
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void registerIcons(IconRegister register) {
      super.registerIcons(register);
      this.iconColumnSide = register.registerIcon(this.columnSideTextureName);
      this.iconColumnTopAndBottom = register.registerIcon(this.columnTopAndBottomTextureName);
      this.iconPedestalSide = register.registerIcon(this.pedestalSideTextureName);
      this.iconPedestalTopAndBottom = register.registerIcon(this.pedestalTopAndBottomTextureName);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public Icon getIcon(int iSide, int iMetadata) {
      if (iMetadata == 12) {
         return iSide < 2 ? this.iconColumnTopAndBottom : this.iconColumnSide;
      } else if (iMetadata != 13 && iMetadata != 14) {
         return super.a(iSide, iMetadata);
      } else {
         return iSide < 2 ? this.iconPedestalTopAndBottom : this.iconPedestalSide;
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean renderBlock(RenderBlocks renderBlocks, int i, int j, int k) {
      IBlockAccess blockAccess = renderBlocks.blockAccess;
      int iMetadata = blockAccess.getBlockMetadata(i, j, k);
      if (!this.isDecorative(iMetadata)) {
         return super.renderBlock(renderBlocks, i, j, k);
      } else {
         switch (iMetadata) {
            case 13:
               return this.renderPedestalUp(renderBlocks, i, j, k);
            case 14:
               return this.renderPedestalDown(renderBlocks, i, j, k);
            case 15:
               return this.renderTable(renderBlocks, i, j, k);
            default:
               renderBlocks.setRenderBounds(this.getBlockBoundsFromPoolBasedOnState(renderBlocks.blockAccess, i, j, k));
               return renderBlocks.renderStandardBlock(this, i, j, k);
         }
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void renderBlockAsItem(RenderBlocks renderBlocks, int iItemDamage, float fBrightness) {
      if (this.isDecorative(iItemDamage)) {
         this.renderDecorativeInvBlock(renderBlocks, this, iItemDamage, fBrightness);
      } else {
         super.renderBlockAsItem(renderBlocks, iItemDamage, fBrightness);
      }
   }

   @Environment(EnvType.CLIENT)
   protected void renderDecorativeInvBlock(RenderBlocks renderBlocks, Block block, int iItemDamage, float fBrightness) {
      switch (iItemDamage) {
         case 12:
            renderBlocks.setRenderBounds(0.1875, 0.0, 0.1875, 0.8125, 1.0, 0.8125);
            RenderUtils.renderInvBlockWithMetadata(renderBlocks, block, -0.5F, -0.5F, -0.5F, iItemDamage);
            break;
         case 13:
            this.renderPedestalUpInvBlock(renderBlocks, block);
            break;
         case 14:
            this.renderPedestalDownInvBlock(renderBlocks, block);
            break;
         case 15:
            this.renderTableInvBlock(renderBlocks, block);
            break;
         default:
            renderBlocks.renderBlockAsItemVanilla(block, iItemDamage, fBrightness);
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int i, int j, int k) {
      int iMetadata = world.getBlockMetadata(i, j, k);
      return this.isDecorative(iMetadata)
         ? this.getBlockBoundsFromPoolBasedOnState(world, i, j, k).offset(i, j, k)
         : super.getSelectedBoundingBoxFromPool(world, i, j, k);
   }

   @Environment(EnvType.CLIENT)
   public boolean renderPedestalUp(RenderBlocks renderBlocks, int i, int j, int k) {
      renderBlocks.setRenderBounds(0.0, 0.0, 0.0, 1.0, 0.75, 1.0);
      renderBlocks.renderStandardBlock(this, i, j, k);
      renderBlocks.setRenderBounds(0.0625, 0.75, 0.0625, 0.9375, 0.875, 0.9375);
      renderBlocks.renderStandardBlock(this, i, j, k);
      renderBlocks.setRenderBounds(0.125, 0.875, 0.125, 0.875, 1.0, 0.875);
      renderBlocks.renderStandardBlock(this, i, j, k);
      return true;
   }

   @Environment(EnvType.CLIENT)
   public void renderPedestalUpInvBlock(RenderBlocks renderBlocks, Block block) {
      renderBlocks.setRenderBounds(0.0, 0.0, 0.0, 1.0, 0.75, 1.0);
      RenderUtils.renderInvBlockWithMetadata(renderBlocks, block, -0.5F, -0.5F, -0.5F, 13);
      renderBlocks.setRenderBounds(0.0625, 0.75, 0.0625, 0.9375, 0.875, 0.9375);
      RenderUtils.renderInvBlockWithMetadata(renderBlocks, block, -0.5F, -0.5F, -0.5F, 13);
      renderBlocks.setRenderBounds(0.125, 0.875, 0.125, 0.875, 1.0, 0.875);
      RenderUtils.renderInvBlockWithMetadata(renderBlocks, block, -0.5F, -0.5F, -0.5F, 13);
   }

   @Environment(EnvType.CLIENT)
   public boolean renderPedestalDown(RenderBlocks renderBlocks, int i, int j, int k) {
      renderBlocks.setRenderBounds(0.0, 0.25, 0.0, 1.0, 1.0, 1.0);
      renderBlocks.renderStandardBlock(this, i, j, k);
      renderBlocks.setRenderBounds(0.0625, 0.125, 0.0625, 0.9375, 0.25, 0.9375);
      renderBlocks.renderStandardBlock(this, i, j, k);
      renderBlocks.setRenderBounds(0.125, 0.0, 0.125, 0.875, 0.125, 0.875);
      renderBlocks.renderStandardBlock(this, i, j, k);
      return true;
   }

   @Environment(EnvType.CLIENT)
   public void renderPedestalDownInvBlock(RenderBlocks renderBlocks, Block block) {
      renderBlocks.setRenderBounds(0.0, 0.25, 0.0, 1.0, 1.0, 1.0);
      RenderUtils.renderInvBlockWithMetadata(renderBlocks, block, -0.5F, -0.5F, -0.5F, 14);
      renderBlocks.setRenderBounds(0.0625, 0.125, 0.0625, 0.9375, 0.25, 0.9375);
      RenderUtils.renderInvBlockWithMetadata(renderBlocks, block, -0.5F, -0.5F, -0.5F, 14);
      renderBlocks.setRenderBounds(0.125, 0.0, 0.125, 0.875, 0.125, 0.875);
      RenderUtils.renderInvBlockWithMetadata(renderBlocks, block, -0.5F, -0.5F, -0.5F, 14);
   }

   @Environment(EnvType.CLIENT)
   public boolean renderTable(RenderBlocks renderBlocks, int i, int j, int k) {
      renderBlocks.setRenderBounds(0.0, 0.875, 0.0, 1.0, 1.0, 1.0);
      renderBlocks.renderStandardBlock(this, i, j, k);
      if (this.doesTableHaveLeg(renderBlocks.blockAccess, i, j, k)) {
         renderBlocks.setRenderBounds(0.375, 0.0, 0.375, 0.625, 0.875, 0.625);
         renderBlocks.renderStandardBlock(this, i, j, k);
      }

      return true;
   }

   @Environment(EnvType.CLIENT)
   public void renderTableInvBlock(RenderBlocks renderBlocks, Block block) {
      renderBlocks.setRenderBounds(0.0, 0.875, 0.0, 1.0, 1.0, 1.0);
      RenderUtils.renderInvBlockWithMetadata(renderBlocks, block, -0.5F, -0.5F, -0.5F, 15);
      renderBlocks.setRenderBounds(0.375, 0.0, 0.375, 0.625, 0.875, 0.625);
      RenderUtils.renderInvBlockWithMetadata(renderBlocks, block, -0.5F, -0.5F, -0.5F, 15);
   }
}
