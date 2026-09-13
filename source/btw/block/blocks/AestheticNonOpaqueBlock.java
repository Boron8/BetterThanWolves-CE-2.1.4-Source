package btw.block.blocks;

import btw.block.BTWBlocks;
import btw.client.render.util.RenderUtils;
import btw.item.BTWItems;
import btw.world.util.WorldUtils;
import java.util.List;
import java.util.Random;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityRenderer;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Icon;
import net.minecraft.src.IconRegister;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.Tessellator;
import net.minecraft.src.World;

public class AestheticNonOpaqueBlock extends Block {
   public static final int SUBTYPE_URN = 0;
   public static final int SUBTYPE_COLUMN = 1;
   public static final int SUBTYPE_PEDESTAL_UP = 2;
   public static final int SUBTYPE_PEDESTAL_DOWN = 3;
   public static final int SUBTYPE_TABLE = 4;
   public static final int SUBTYPE_WICKER_SLAB = 5;
   public static final int SUBTYPE_GRATE = 6;
   public static final int SUBTYPE_WICKER = 7;
   public static final int SUBTYPE_SLATS = 8;
   public static final int SUBTYPE_WICKER_SLAB_UPSIDE_DOWN = 9;
   public static final int SUBTYPE_WHITE_COBBLE_SLAB = 10;
   public static final int SUBTYPE_WHITE_COBBLE_SLAB_UPSIDE_DOWN = 11;
   public static final int SUBTYPE_LIGHTNING_ROD = 12;
   public static final int NUM_SUBTYPES = 13;
   private static final float DEFAULT_HARDNESS = 2.0F;
   private static final float COLUM_WIDTH = 0.625F;
   private static final float COLUM_HALF_WIDTH = 0.3125F;
   private static final float PEDESTAL_BASE_HEIGHT = 0.75F;
   private static final float PEDESTAL_MIDDLE_HEIGHT = 0.125F;
   private static final float PEDESTAL_MIDDLE_WIDTH = 0.875F;
   private static final float PEDESTAL_MIDDLE_HALF_WIDTH = 0.4375F;
   private static final float PEDESTAL_TOP_HEIGHT = 0.125F;
   private static final float PEDESTAL_TOP_WIDTH = 0.75F;
   private static final float PEDESTAL_TOP_HALF_WIDTH = 0.375F;
   private static final float TABLE_TOP_HEIGHT = 0.125F;
   private static final float TABLE_LEG_HEIGHT = 0.875F;
   private static final float TABLE_LEG_WIDTH = 0.25F;
   private static final float TABLE_LEG_HALF_WIDTH = 0.125F;
   private static final float LIGHTNING_ROD_SHAFT_WIDTH = 0.0625F;
   private static final float LIGHTNING_ROD_SHAFT_HALF_WIDTH = 0.03125F;
   private static final float LIGHTNING_ROD_BASE_WIDTH = 0.25F;
   private static final float LIGHTNING_ROD_BASE_HALF_WIDTH = 0.125F;
   private static final float LIGHTNING_ROD_BASE_HEIGHT = 0.125F;
   private static final float LIGHTNING_ROD_BASE_HALF_HEIGHT = 0.0625F;
   private static final float LIGHTNING_ROD_BALL_WIDTH = 0.1875F;
   private static final float LIGHTNING_ROD_BALL_HALF_WIDTH = 0.09375F;
   private static final float LIGHTNING_ROD_BALL_VERTICAL_OFFSET = 0.625F;
   private static final float LIGHTNING_ROD_CANDLE_HOLDER_WIDTH = 0.25F;
   private static final float LIGHTNING_ROD_CANDLE_HOLDER_HALF_WIDTH = 0.125F;
   private static final float LIGHTNING_ROD_CANDLE_HOLDER_HEIGHT = 0.0625F;
   private static final float LIGHTNING_ROD_CANDLE_HOLDER_HALF_HEIGHT = 0.03125F;
   private static final float LIGHTNING_ROD_CANDLE_HOLDER_VERTICAL_OFFSET = 0.99609375F;
   @Environment(EnvType.CLIENT)
   private Icon iconUrn;
   @Environment(EnvType.CLIENT)
   private Icon iconColumnStoneTop;
   @Environment(EnvType.CLIENT)
   private Icon iconColumnStoneSide;
   @Environment(EnvType.CLIENT)
   private Icon iconPedestalStoneTop;
   @Environment(EnvType.CLIENT)
   private Icon iconPedestalStoneSide;
   @Environment(EnvType.CLIENT)
   private Icon iconSlabWicker;
   @Environment(EnvType.CLIENT)
   private Icon iconGrate;
   @Environment(EnvType.CLIENT)
   private Icon iconWicker;
   @Environment(EnvType.CLIENT)
   private Icon iconSlats;
   @Environment(EnvType.CLIENT)
   private Icon iconSlatsSide;
   @Environment(EnvType.CLIENT)
   private Icon iconWhiteCobble;
   @Environment(EnvType.CLIENT)
   private Icon iconLightningRod;
   @Environment(EnvType.CLIENT)
   public Icon iconTableWoodOakTop;
   @Environment(EnvType.CLIENT)
   public Icon iconTableWoodOakLeg;

   public AestheticNonOpaqueBlock(int blockID) {
      super(blockID, BTWBlocks.miscMaterial);
      this.c(2.0F);
      this.setAxesEffectiveOn(true);
      this.setPicksEffectiveOn(true);
      this.a(j);
      this.c("fcBlockAestheticNonOpaque");
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
   public int idDropped(int metadata, Random random, int fortuneModifier) {
      if (metadata == 0) {
         return BTWItems.urn.itemID;
      } else if (metadata == 5 || metadata == 9) {
         return BTWBlocks.wickerSlab.blockID;
      } else if (metadata == 6) {
         return BTWBlocks.gratePane.blockID;
      } else if (metadata == 7) {
         return BTWBlocks.wickerPane.blockID;
      } else if (metadata == 8) {
         return BTWBlocks.slatsPane.blockID;
      } else if (metadata == 3 || metadata == 2 || metadata == 1) {
         return BTWBlocks.stoneMouldingAndDecorative.blockID;
      } else {
         return metadata == 4 ? BTWItems.woodMouldingDecorativeStubID : this.blockID;
      }
   }

   @Override
   public int damageDropped(int metadata) {
      if (metadata == 3 || metadata == 2) {
         metadata = 13;
      } else if (metadata == 1) {
         metadata = 12;
      } else if (metadata == 11) {
         metadata = 10;
      } else {
         if (metadata == 4) {
            return 8;
         }

         if (metadata == 0 || metadata == 5 || metadata == 9 || metadata == 6 || metadata == 7 || metadata == 8) {
            metadata = 0;
         }
      }

      return metadata;
   }

   @Override
   public int onBlockPlaced(World world, int i, int j, int k, int facing, float clickX, float clickY, float clickZ, int metadata) {
      if (metadata == 2) {
         if (facing == 0 || facing != 1 && clickY > 0.5) {
            return 3;
         }
      } else if (metadata == 5) {
         if (facing == 0 || facing != 1 && clickY > 0.5) {
            return 9;
         }
      } else if (metadata == 10 && (facing == 0 || facing != 1 && clickY > 0.5)) {
         return 11;
      }

      return metadata;
   }

   @Override
   public AxisAlignedBB getBlockBoundsFromPoolBasedOnState(IBlockAccess blockAccess, int i, int j, int k) {
      int iSubType = blockAccess.getBlockMetadata(i, j, k);
      switch (iSubType) {
         case 0:
            AxisAlignedBB urnBox = AxisAlignedBB.getAABBPool().getAABB(0.3125, 0.0, 0.3125, 0.6875, 0.625, 0.6875);
            if (blockAccess.getBlockId(i, j + 1, k) == BTWBlocks.hopper.blockID) {
               urnBox.offset(0.0, 0.375, 0.0);
            }

            return urnBox;
         case 1:
            return AxisAlignedBB.getAABBPool().getAABB(0.1875, 0.0, 0.1875, 0.8125, 1.0, 0.8125);
         case 2:
         case 3:
         default:
            return super.getBlockBoundsFromPoolBasedOnState(blockAccess, i, j, k);
         case 4:
            if (!this.isTableOnCorner(blockAccess, i, j, k)) {
               return AxisAlignedBB.getAABBPool().getAABB(0.0, 0.875, 0.0, 1.0, 1.0, 1.0);
            }

            return AxisAlignedBB.getAABBPool().getAABB(0.0, 0.0, 0.0, 1.0, 1.0, 1.0);
         case 5:
         case 10:
            return AxisAlignedBB.getAABBPool().getAABB(0.0, 0.0, 0.0, 1.0, 0.5, 1.0);
         case 6:
         case 7:
         case 8:
            return this.getBlockBoundsFromPoolForPane(blockAccess, i, j, k, iSubType);
         case 9:
         case 11:
            return AxisAlignedBB.getAABBPool().getAABB(0.0, 0.5, 0.0, 1.0, 1.0, 1.0);
         case 12:
            return AxisAlignedBB.getAABBPool().getAABB(0.375, 0.0, 0.375, 0.625, 1.0, 0.625);
      }
   }

   @Override
   public void addCollisionBoxesToList(World world, int i, int j, int k, AxisAlignedBB intersectingBox, List list, Entity entity) {
      int iSubType = world.getBlockMetadata(i, j, k);
      if (iSubType != 6 && iSubType != 7 && iSubType != 8) {
         super.addCollisionBoxesToList(world, i, j, k, intersectingBox, list, entity);
      } else {
         boolean bKNeg = this.shouldPaneConnectToBlock(world, i, j, k - 1, iSubType);
         boolean bKPos = this.shouldPaneConnectToBlock(world, i, j, k + 1, iSubType);
         boolean bINeg = this.shouldPaneConnectToBlock(world, i - 1, j, k, iSubType);
         boolean bIPos = this.shouldPaneConnectToBlock(world, i + 1, j, k, iSubType);
         if ((!bINeg || !bIPos) && (bINeg || bIPos || bKNeg || bKPos)) {
            if (bINeg && !bIPos) {
               AxisAlignedBB tempBox = AxisAlignedBB.getAABBPool().getAABB(0.0, 0.0, 0.4375, 0.5, 1.0, 0.5625).offset(i, j, k);
               tempBox.addToListIfIntersects(intersectingBox, list);
            } else if (!bINeg && bIPos) {
               AxisAlignedBB tempBox = AxisAlignedBB.getAABBPool().getAABB(0.5, 0.0, 0.4375, 1.0, 1.0, 0.5625).offset(i, j, k);
               tempBox.addToListIfIntersects(intersectingBox, list);
            }
         } else {
            AxisAlignedBB tempBox = AxisAlignedBB.getAABBPool().getAABB(0.0, 0.0, 0.4375, 1.0, 1.0, 0.5625).offset(i, j, k);
            tempBox.addToListIfIntersects(intersectingBox, list);
         }

         if ((!bKNeg || !bKPos) && (bINeg || bIPos || bKNeg || bKPos)) {
            if (bKNeg && !bKPos) {
               AxisAlignedBB tempBox = AxisAlignedBB.getAABBPool().getAABB(0.4375, 0.0, 0.0, 0.5625, 1.0, 0.5).offset(i, j, k);
               tempBox.addToListIfIntersects(intersectingBox, list);
            } else if (!bKNeg && bKPos) {
               AxisAlignedBB tempBox = AxisAlignedBB.getAABBPool().getAABB(0.4375, 0.0, 0.5, 0.5625, 1.0, 1.0).offset(i, j, k);
               tempBox.addToListIfIntersects(intersectingBox, list);
            }
         } else {
            AxisAlignedBB tempBox = AxisAlignedBB.getAABBPool().getAABB(0.4375, 0.0, 0.0, 0.5625, 1.0, 1.0).offset(i, j, k);
            tempBox.addToListIfIntersects(intersectingBox, list);
         }
      }
   }

   @Override
   public void onNeighborBlockChange(World world, int i, int j, int k, int changedBlockID) {
      super.onNeighborBlockChange(world, i, j, k, changedBlockID);
      int iSubtype = this.getSubtype(world, i, j, k);
      if (iSubtype == 12) {
         if (!canLightningRodStay(world, i, j, k)) {
            if (world.getBlockId(i, j, k) == this.blockID) {
               this.c(world, i, j, k, world.getBlockMetadata(i, j, k), 0);
               world.setBlockWithNotify(i, j, k, 0);
            }
         } else {
            world.markBlockRangeForRenderUpdate(i, j, k, i, j, k);
         }
      }
   }

   @Override
   public boolean hasCenterHardPointToFacing(IBlockAccess blockAccess, int i, int j, int k, int facing, boolean ignoreTransparency) {
      int iSubtype = blockAccess.getBlockMetadata(i, j, k);
      switch (iSubtype) {
         case 1:
            return facing == 0 || facing == 1;
         case 2:
         case 3:
            return true;
         default:
            return super.hasCenterHardPointToFacing(blockAccess, i, j, k, facing, ignoreTransparency);
      }
   }

   @Override
   public boolean hasLargeCenterHardPointToFacing(IBlockAccess blockAccess, int i, int j, int k, int facing, boolean ignoreTransparency) {
      int iSubtype = blockAccess.getBlockMetadata(i, j, k);
      switch (iSubtype) {
         case 2:
            return facing == 0;
         case 3:
            return facing == 1;
         case 4:
            return facing == 1;
         case 5:
         case 10:
            return facing == 0;
         case 6:
         case 7:
         case 8:
         default:
            return super.hasLargeCenterHardPointToFacing(blockAccess, i, j, k, facing, ignoreTransparency);
         case 9:
         case 11:
            return facing == 1;
      }
   }

   @Override
   public boolean hasSmallCenterHardPointToFacing(IBlockAccess blockAccess, int i, int j, int k, int facing, boolean ignoreTransparency) {
      int iSubtype = blockAccess.getBlockMetadata(i, j, k);
      return iSubtype == 12 ? facing == 1 : super.hasSmallCenterHardPointToFacing(blockAccess, i, j, k, facing, ignoreTransparency);
   }

   @Override
   public boolean doesBlockBreakSaw(World world, int i, int j, int k) {
      int iSubtype = world.getBlockMetadata(i, j, k);
      return iSubtype != 0 && iSubtype != 4 && iSubtype != 5 && iSubtype != 9 && iSubtype != 6 && iSubtype != 7 && iSubtype != 8;
   }

   @Override
   public float getMovementModifier(World world, int i, int j, int k) {
      return 1.2F;
   }

   @Override
   public boolean canGroundCoverRestOnBlock(World world, int i, int j, int k) {
      int iSubtype = world.getBlockMetadata(i, j, k);
      if (iSubtype == 5 || iSubtype == 10) {
         return true;
      } else {
         return iSubtype == 0 ? world.doesBlockHaveSolidTopSurface(i, j - 1, k) : super.canGroundCoverRestOnBlock(world, i, j, k);
      }
   }

   @Override
   public float groundCoverRestingOnVisualOffset(IBlockAccess blockAccess, int i, int j, int k) {
      int iSubtype = blockAccess.getBlockMetadata(i, j, k);
      if (iSubtype == 5 || iSubtype == 10) {
         return -0.5F;
      } else {
         return iSubtype == 0 ? -1.0F : super.groundCoverRestingOnVisualOffset(blockAccess, i, j, k);
      }
   }

   @Override
   public boolean canToolsStickInBlock(IBlockAccess blockAccess, int i, int j, int k) {
      int iSubtype = blockAccess.getBlockMetadata(i, j, k);
      return iSubtype != 5;
   }

   public int getSubtype(IBlockAccess blockAccess, int i, int j, int k) {
      return blockAccess.getBlockMetadata(i, j, k);
   }

   public void setSubtype(World world, int i, int j, int k, int subtype) {
      world.setBlockMetadata(i, j, k, subtype);
   }

   public boolean isBlockTable(IBlockAccess blockAccess, int i, int j, int k) {
      return blockAccess.getBlockId(i, j, k) == BTWBlocks.aestheticNonOpaque.blockID && blockAccess.getBlockMetadata(i, j, k) == 4;
   }

   public boolean isTableOnCorner(IBlockAccess blockAccess, int i, int j, int k) {
      boolean positiveITable = this.isBlockTable(blockAccess, i + 1, j, k);
      boolean negativeITable = this.isBlockTable(blockAccess, i - 1, j, k);
      boolean positiveKTable = this.isBlockTable(blockAccess, i, j, k + 1);
      boolean negativeKTable = this.isBlockTable(blockAccess, i, j, k - 1);
      return !positiveITable && (!positiveKTable || !negativeKTable) || !negativeITable && (!positiveKTable || !negativeKTable);
   }

   private boolean shouldPaneConnectToBlock(IBlockAccess blockAccess, int i, int j, int k, int subType) {
      int iBlockID = blockAccess.getBlockId(i, j, k);
      if (Block.opaqueCubeLookup[iBlockID] || iBlockID == Block.glass.blockID) {
         return true;
      } else if (iBlockID == this.blockID) {
         int iTargetSubType = blockAccess.getBlockMetadata(i, j, k);
         return iTargetSubType == subType;
      } else {
         return false;
      }
   }

   public AxisAlignedBB getBlockBoundsFromPoolForPane(IBlockAccess blockAccess, int i, int j, int k, int subType) {
      float fXMin = 0.4375F;
      float fXMax = 0.5625F;
      float fZMin = 0.4375F;
      float fZMax = 0.5625F;
      boolean bKNeg = this.shouldPaneConnectToBlock(blockAccess, i, j, k - 1, subType);
      boolean bKPos = this.shouldPaneConnectToBlock(blockAccess, i, j, k + 1, subType);
      boolean bINeg = this.shouldPaneConnectToBlock(blockAccess, i - 1, j, k, subType);
      boolean bIPos = this.shouldPaneConnectToBlock(blockAccess, i + 1, j, k, subType);
      if ((!bINeg || !bIPos) && (bINeg || bIPos || bKNeg || bKPos)) {
         if (bINeg && !bIPos) {
            fXMin = 0.0F;
         } else if (!bINeg && bIPos) {
            fXMax = 1.0F;
         }
      } else {
         fXMin = 0.0F;
         fXMax = 1.0F;
      }

      if ((!bKNeg || !bKPos) && (bINeg || bIPos || bKNeg || bKPos)) {
         if (bKNeg && !bKPos) {
            fZMin = 0.0F;
         } else if (!bKNeg && bKPos) {
            fZMax = 1.0F;
         }
      } else {
         fZMin = 0.0F;
         fZMax = 1.0F;
      }

      return AxisAlignedBB.getAABBPool().getAABB(fXMin, 0.0, fZMin, fXMax, 1.0, fZMax);
   }

   public static boolean canLightningRodStay(World world, int i, int j, int k) {
      int iBlockBelowID = world.getBlockId(i, j - 1, k);
      return iBlockBelowID == BTWBlocks.aestheticNonOpaque.blockID && world.getBlockMetadata(i, j - 1, k) == 12
         ? true
         : WorldUtils.doesBlockHaveCenterHardpointToFacing(world, i, j - 1, k, 1, true);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void registerIcons(IconRegister register) {
      this.blockIcon = register.registerIcon("stone");
      this.iconUrn = register.registerIcon("fcBlockUrn");
      this.iconColumnStoneTop = register.registerIcon("fcBlockColumnStone_top");
      this.iconColumnStoneSide = register.registerIcon("fcBlockColumnStone_side");
      this.iconPedestalStoneTop = register.registerIcon("fcBlockPedestalStone_top");
      this.iconPedestalStoneSide = register.registerIcon("fcBlockPedestalStone_side");
      this.iconTableWoodOakTop = register.registerIcon("fcBlockTableWoodOak_top");
      this.iconTableWoodOakLeg = register.registerIcon("fcBlockTableWoodOak_leg");
      this.iconSlabWicker = register.registerIcon("fcBlockSlabWicker");
      this.iconGrate = register.registerIcon("fcBlockGrate");
      this.iconWicker = register.registerIcon("fcBlockWicker");
      this.iconSlats = register.registerIcon("fcBlockSlats");
      this.iconSlatsSide = register.registerIcon("fcBlockSlats_side");
      this.iconWhiteCobble = register.registerIcon("fcBlockWhiteCobble");
      this.iconLightningRod = register.registerIcon("fcBlockLightningRodOld");
   }

   @Environment(EnvType.CLIENT)
   @Override
   public Icon getIcon(int side, int metadata) {
      switch (metadata) {
         case 1:
            if (side < 2) {
               return this.iconColumnStoneTop;
            }

            return this.iconColumnStoneSide;
         case 2:
         case 3:
            if (side < 2) {
               return this.iconPedestalStoneTop;
            }

            return this.iconPedestalStoneSide;
         case 4:
            return this.iconTableWoodOakTop;
         case 5:
         case 9:
            return this.iconSlabWicker;
         case 6:
            return this.iconGrate;
         case 7:
            return this.iconWicker;
         case 8:
            return this.iconSlats;
         case 10:
         case 11:
            return this.iconWhiteCobble;
         case 12:
            return this.iconLightningRod;
         default:
            return this.blockIcon;
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean shouldSideBeRendered(IBlockAccess blockAccess, int neighborI, int neighborJ, int neighborK, int side) {
      return this.currentBlockRenderer.shouldSideBeRenderedBasedOnCurrentBounds(neighborI, neighborJ, neighborK, side);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void getSubBlocks(int blockID, CreativeTabs creativeTabs, List list) {
   }

   @Environment(EnvType.CLIENT)
   @Override
   public int idPicked(World world, int i, int j, int k) {
      return this.idDropped(world.getBlockMetadata(i, j, k), world.rand, 0);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean renderBlock(RenderBlocks renderer, int i, int j, int k) {
      IBlockAccess blockAccess = renderer.blockAccess;
      int iSubType = blockAccess.getBlockMetadata(i, j, k);
      switch (iSubType) {
         case 0:
            float fVerticalOffset = 0.0F;
            if (blockAccess.getBlockId(i, j + 1, k) == BTWBlocks.hopper.blockID) {
               fVerticalOffset = 0.375F;
            }

            return UnfiredPotteryBlock.renderUnfiredUrn(renderer, blockAccess, i, j, k, this, this.iconUrn, fVerticalOffset);
         case 1:
         case 5:
         case 9:
         case 10:
         case 11:
         default:
            renderer.setRenderBounds(this.getBlockBoundsFromPoolBasedOnState(renderer.blockAccess, i, j, k));
            return renderer.renderStandardBlock(this, i, j, k);
         case 2:
            return this.renderPedestalUp(renderer, blockAccess, i, j, k, this);
         case 3:
            return this.renderPedestalDown(renderer, blockAccess, i, j, k, this);
         case 4:
            return this.renderTable(renderer, blockAccess, i, j, k, this);
         case 6:
         case 7:
         case 8:
            return this.renderPane(renderer, blockAccess, i, j, k, this, iSubType);
         case 12:
            return this.renderLightningRod(renderer, blockAccess, i, j, k, this);
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void renderBlockAsItem(RenderBlocks renderBlocks, int itemDamage, float brightness) {
      switch (itemDamage) {
         case 0:
            UnfiredPotteryBlock.renderUnfiredUrnInvBlock(renderBlocks, this, itemDamage, this.iconUrn);
            return;
         case 1:
            renderBlocks.setRenderBounds(0.1875, 0.0, 0.1875, 0.8125, 1.0, 0.8125);
            break;
         case 2:
            this.renderPedestalUpInvBlock(renderBlocks, this);
            return;
         case 3:
            this.renderPedestalDownInvBlock(renderBlocks, this);
            return;
         case 4:
            this.renderTableInvBlock(renderBlocks, this);
            return;
         case 5:
         case 10:
            renderBlocks.setRenderBounds(0.0, 0.0, 0.0, 1.0, 0.5, 1.0);
            break;
         case 6:
         case 7:
         case 8:
         default:
            renderBlocks.setRenderBounds(0.0, 0.0, 0.0, 1.0, 1.0, 1.0);
            break;
         case 9:
         case 11:
            renderBlocks.setRenderBounds(0.0, 0.5, 0.0, 1.0, 1.0, 1.0);
            break;
         case 12:
            this.renderLightningRodInvBlock(renderBlocks, this);
            return;
      }

      RenderUtils.renderInvBlockWithMetadata(renderBlocks, this, -0.5F, -0.5F, -0.5F, itemDamage);
   }

   @Environment(EnvType.CLIENT)
   public boolean renderPedestalUp(RenderBlocks renderBlocks, IBlockAccess blockAccess, int i, int j, int k, Block block) {
      renderBlocks.setRenderBounds(0.0, 0.0, 0.0, 1.0, 0.75, 1.0);
      renderBlocks.renderStandardBlock(block, i, j, k);
      renderBlocks.setRenderBounds(0.0625, 0.75, 0.0625, 0.9375, 0.875, 0.9375);
      renderBlocks.renderStandardBlock(block, i, j, k);
      renderBlocks.setRenderBounds(0.125, 0.875, 0.125, 0.875, 1.0, 0.875);
      renderBlocks.renderStandardBlock(block, i, j, k);
      return true;
   }

   @Environment(EnvType.CLIENT)
   public void renderPedestalUpInvBlock(RenderBlocks renderBlocks, Block block) {
      renderBlocks.setRenderBounds(0.0, 0.0, 0.0, 1.0, 0.75, 1.0);
      RenderUtils.renderInvBlockWithMetadata(renderBlocks, block, -0.5F, -0.5F, -0.5F, 2);
      renderBlocks.setRenderBounds(0.0625, 0.75, 0.0625, 0.9375, 0.875, 0.9375);
      RenderUtils.renderInvBlockWithMetadata(renderBlocks, block, -0.5F, -0.5F, -0.5F, 2);
      renderBlocks.setRenderBounds(0.125, 0.875, 0.125, 0.875, 1.0, 0.875);
      RenderUtils.renderInvBlockWithMetadata(renderBlocks, block, -0.5F, -0.5F, -0.5F, 2);
   }

   @Environment(EnvType.CLIENT)
   public boolean renderPedestalDown(RenderBlocks renderBlocks, IBlockAccess blockAccess, int i, int j, int k, Block block) {
      renderBlocks.setRenderBounds(0.0, 0.25, 0.0, 1.0, 1.0, 1.0);
      renderBlocks.renderStandardBlock(block, i, j, k);
      renderBlocks.setRenderBounds(0.0625, 0.125, 0.0625, 0.9375, 0.25, 0.9375);
      renderBlocks.renderStandardBlock(block, i, j, k);
      renderBlocks.setRenderBounds(0.125, 0.0, 0.125, 0.875, 0.125, 0.875);
      renderBlocks.renderStandardBlock(block, i, j, k);
      return true;
   }

   @Environment(EnvType.CLIENT)
   public void renderPedestalDownInvBlock(RenderBlocks renderBlocks, Block block) {
      renderBlocks.setRenderBounds(0.0, 0.25, 0.0, 1.0, 1.0, 1.0);
      RenderUtils.renderInvBlockWithMetadata(renderBlocks, block, -0.5F, -0.5F, -0.5F, 3);
      renderBlocks.setRenderBounds(0.0625, 0.125, 0.0625, 0.9375, 0.25, 0.9375);
      RenderUtils.renderInvBlockWithMetadata(renderBlocks, block, -0.5F, -0.5F, -0.5F, 3);
      renderBlocks.setRenderBounds(0.125, 0.0, 0.125, 0.875, 0.125, 0.875);
      RenderUtils.renderInvBlockWithMetadata(renderBlocks, block, -0.5F, -0.5F, -0.5F, 3);
   }

   @Environment(EnvType.CLIENT)
   public boolean renderTable(RenderBlocks renderBlocks, IBlockAccess blockAccess, int i, int j, int k, Block block) {
      renderBlocks.setRenderBounds(0.0, 0.875, 0.0, 1.0, 1.0, 1.0);
      RenderUtils.renderStandardBlockWithTexture(renderBlocks, block, i, j, k, this.iconTableWoodOakTop);
      if (this.isTableOnCorner(blockAccess, i, j, k)) {
         renderBlocks.setRenderBounds(0.375, 0.0, 0.375, 0.625, 0.875, 0.625);
         RenderUtils.renderStandardBlockWithTexture(renderBlocks, block, i, j, k, this.iconTableWoodOakLeg);
      }

      return true;
   }

   @Environment(EnvType.CLIENT)
   public void renderTableInvBlock(RenderBlocks renderBlocks, Block block) {
      renderBlocks.setRenderBounds(0.0, 0.875, 0.0, 1.0, 1.0, 1.0);
      RenderUtils.renderInvBlockWithTexture(renderBlocks, block, -0.5F, -0.5F, -0.5F, this.iconTableWoodOakTop);
      renderBlocks.setRenderBounds(0.375, 0.0, 0.375, 0.625, 0.875, 0.625);
      RenderUtils.renderInvBlockWithTexture(renderBlocks, block, -0.5F, -0.5F, -0.5F, this.iconTableWoodOakLeg);
   }

   @Environment(EnvType.CLIENT)
   public boolean renderPane(RenderBlocks renderBlocks, IBlockAccess blockAccess, int i, int j, int k, Block block, int subType) {
      int iWorldHeight = 256;
      Tessellator tessellator = Tessellator.instance;
      tessellator.setBrightness(block.getMixedBrightnessForBlock(blockAccess, i, j, k));
      int iColor = block.colorMultiplier(blockAccess, i, j, k);
      float iColorRed = (iColor >> 16 & 0xFF) / 255.0F;
      float iColorGreen = (iColor >> 8 & 0xFF) / 255.0F;
      float iColorBlue = (iColor & 0xFF) / 255.0F;
      if (EntityRenderer.anaglyphEnable) {
         iColorRed = (iColorRed * 30.0F + iColorGreen * 59.0F + iColorBlue * 11.0F) / 100.0F;
         iColorGreen = (iColorRed * 30.0F + iColorGreen * 70.0F) / 100.0F;
         iColorBlue = (iColorRed * 30.0F + iColorBlue * 70.0F) / 100.0F;
      }

      tessellator.setColorOpaque_F(iColorRed, iColorGreen, iColorBlue);
      Icon paneTexture;
      Icon sideTexture;
      if (renderBlocks.hasOverrideBlockTexture()) {
         paneTexture = renderBlocks.getOverrideTexture();
         sideTexture = renderBlocks.getOverrideTexture();
      } else {
         int iMetadata = blockAccess.getBlockMetadata(i, j, k);
         paneTexture = block.getIcon(0, iMetadata);
         sideTexture = block.getIcon(0, iMetadata);
         if (subType == 8) {
            sideTexture = this.iconSlatsSide;
         }
      }

      int iPaneTextureOriginX = paneTexture.getOriginX();
      int iPaneTextureOriginY = paneTexture.getOriginY();
      double dPaneTextureMinU = paneTexture.getMinU();
      double dPaneTextureInterpolatedMidU = paneTexture.getInterpolatedU(8.0);
      double dPaneTextureMaxU = paneTexture.getMaxU();
      double dPaneTextureMinV = paneTexture.getMinV();
      double dPaneTextureMaxV = paneTexture.getMaxV();
      int iSideTextureOriginX = sideTexture.getOriginX();
      int iSideTextureOriginY = sideTexture.getOriginY();
      double dSideTextureInterpolatedMinU = sideTexture.getInterpolatedU(7.0);
      double dSideTextureInterpolatedMaxU = sideTexture.getInterpolatedU(9.0);
      double dSideTextureMinV = sideTexture.getMinV();
      double dSideTextureInterpolatedMidV = sideTexture.getInterpolatedV(8.0);
      double dSideTextureMaxV = sideTexture.getMaxV();
      double dPosXMin = i;
      double dPosXMid = i + 0.5;
      double dPosXMax = i + 1;
      double dPosZMin = k;
      double dPosZMid = k + 0.5;
      double dPosZMax = k + 1;
      double var50 = i + 0.5 - 0.0625;
      double var52 = i + 0.5 + 0.0625;
      double var54 = k + 0.5 - 0.0625;
      double var56 = k + 0.5 + 0.0625;
      boolean var58 = this.shouldPaneConnectToBlock(blockAccess, i, j, k - 1, subType);
      boolean var59 = this.shouldPaneConnectToBlock(blockAccess, i, j, k + 1, subType);
      boolean var60 = this.shouldPaneConnectToBlock(blockAccess, i - 1, j, k, subType);
      boolean var61 = this.shouldPaneConnectToBlock(blockAccess, i + 1, j, k, subType);
      boolean var62 = !this.shouldPaneConnectToBlock(blockAccess, i, j + 1, k, subType);
      boolean var63 = !this.shouldPaneConnectToBlock(blockAccess, i, j - 1, k, subType);
      if ((!var60 || !var61) && (var60 || var61 || var58 || var59)) {
         if (var60 && !var61) {
            tessellator.addVertexWithUV(dPosXMin, j + 1, dPosZMid, dPaneTextureMinU, dPaneTextureMinV);
            tessellator.addVertexWithUV(dPosXMin, j + 0, dPosZMid, dPaneTextureMinU, dPaneTextureMaxV);
            tessellator.addVertexWithUV(dPosXMid, j + 0, dPosZMid, dPaneTextureInterpolatedMidU, dPaneTextureMaxV);
            tessellator.addVertexWithUV(dPosXMid, j + 1, dPosZMid, dPaneTextureInterpolatedMidU, dPaneTextureMinV);
            tessellator.addVertexWithUV(dPosXMid, j + 1, dPosZMid, dPaneTextureMinU, dPaneTextureMinV);
            tessellator.addVertexWithUV(dPosXMid, j + 0, dPosZMid, dPaneTextureMinU, dPaneTextureMaxV);
            tessellator.addVertexWithUV(dPosXMin, j + 0, dPosZMid, dPaneTextureInterpolatedMidU, dPaneTextureMaxV);
            tessellator.addVertexWithUV(dPosXMin, j + 1, dPosZMid, dPaneTextureInterpolatedMidU, dPaneTextureMinV);
            if (!var59 && !var58) {
               tessellator.addVertexWithUV(dPosXMid, j + 1, var56, dSideTextureInterpolatedMinU, dSideTextureMinV);
               tessellator.addVertexWithUV(dPosXMid, j + 0, var56, dSideTextureInterpolatedMinU, dSideTextureMaxV);
               tessellator.addVertexWithUV(dPosXMid, j + 0, var54, dSideTextureInterpolatedMaxU, dSideTextureMaxV);
               tessellator.addVertexWithUV(dPosXMid, j + 1, var54, dSideTextureInterpolatedMaxU, dSideTextureMinV);
               tessellator.addVertexWithUV(dPosXMid, j + 1, var54, dSideTextureInterpolatedMinU, dSideTextureMinV);
               tessellator.addVertexWithUV(dPosXMid, j + 0, var54, dSideTextureInterpolatedMinU, dSideTextureMaxV);
               tessellator.addVertexWithUV(dPosXMid, j + 0, var56, dSideTextureInterpolatedMaxU, dSideTextureMaxV);
               tessellator.addVertexWithUV(dPosXMid, j + 1, var56, dSideTextureInterpolatedMaxU, dSideTextureMinV);
            }

            if (var62 || j < iWorldHeight - 1 && blockAccess.isAirBlock(i - 1, j + 1, k)) {
               tessellator.addVertexWithUV(dPosXMin, j + 1 + 0.01, var56, dSideTextureInterpolatedMaxU, dSideTextureInterpolatedMidV);
               tessellator.addVertexWithUV(dPosXMid, j + 1 + 0.01, var56, dSideTextureInterpolatedMaxU, dSideTextureMaxV);
               tessellator.addVertexWithUV(dPosXMid, j + 1 + 0.01, var54, dSideTextureInterpolatedMinU, dSideTextureMaxV);
               tessellator.addVertexWithUV(dPosXMin, j + 1 + 0.01, var54, dSideTextureInterpolatedMinU, dSideTextureInterpolatedMidV);
               tessellator.addVertexWithUV(dPosXMid, j + 1 + 0.01, var56, dSideTextureInterpolatedMaxU, dSideTextureInterpolatedMidV);
               tessellator.addVertexWithUV(dPosXMin, j + 1 + 0.01, var56, dSideTextureInterpolatedMaxU, dSideTextureMaxV);
               tessellator.addVertexWithUV(dPosXMin, j + 1 + 0.01, var54, dSideTextureInterpolatedMinU, dSideTextureMaxV);
               tessellator.addVertexWithUV(dPosXMid, j + 1 + 0.01, var54, dSideTextureInterpolatedMinU, dSideTextureInterpolatedMidV);
            }

            if (var63 || j > 1 && blockAccess.isAirBlock(i - 1, j - 1, k)) {
               tessellator.addVertexWithUV(dPosXMin, j - 0.01, var56, dSideTextureInterpolatedMaxU, dSideTextureInterpolatedMidV);
               tessellator.addVertexWithUV(dPosXMid, j - 0.01, var56, dSideTextureInterpolatedMaxU, dSideTextureMaxV);
               tessellator.addVertexWithUV(dPosXMid, j - 0.01, var54, dSideTextureInterpolatedMinU, dSideTextureMaxV);
               tessellator.addVertexWithUV(dPosXMin, j - 0.01, var54, dSideTextureInterpolatedMinU, dSideTextureInterpolatedMidV);
               tessellator.addVertexWithUV(dPosXMid, j - 0.01, var56, dSideTextureInterpolatedMaxU, dSideTextureInterpolatedMidV);
               tessellator.addVertexWithUV(dPosXMin, j - 0.01, var56, dSideTextureInterpolatedMaxU, dSideTextureMaxV);
               tessellator.addVertexWithUV(dPosXMin, j - 0.01, var54, dSideTextureInterpolatedMinU, dSideTextureMaxV);
               tessellator.addVertexWithUV(dPosXMid, j - 0.01, var54, dSideTextureInterpolatedMinU, dSideTextureInterpolatedMidV);
            }
         } else if (!var60 && var61) {
            tessellator.addVertexWithUV(dPosXMid, j + 1, dPosZMid, dPaneTextureInterpolatedMidU, dPaneTextureMinV);
            tessellator.addVertexWithUV(dPosXMid, j + 0, dPosZMid, dPaneTextureInterpolatedMidU, dPaneTextureMaxV);
            tessellator.addVertexWithUV(dPosXMax, j + 0, dPosZMid, dPaneTextureMaxU, dPaneTextureMaxV);
            tessellator.addVertexWithUV(dPosXMax, j + 1, dPosZMid, dPaneTextureMaxU, dPaneTextureMinV);
            tessellator.addVertexWithUV(dPosXMax, j + 1, dPosZMid, dPaneTextureInterpolatedMidU, dPaneTextureMinV);
            tessellator.addVertexWithUV(dPosXMax, j + 0, dPosZMid, dPaneTextureInterpolatedMidU, dPaneTextureMaxV);
            tessellator.addVertexWithUV(dPosXMid, j + 0, dPosZMid, dPaneTextureMaxU, dPaneTextureMaxV);
            tessellator.addVertexWithUV(dPosXMid, j + 1, dPosZMid, dPaneTextureMaxU, dPaneTextureMinV);
            if (!var59 && !var58) {
               tessellator.addVertexWithUV(dPosXMid, j + 1, var54, dSideTextureInterpolatedMinU, dSideTextureMinV);
               tessellator.addVertexWithUV(dPosXMid, j + 0, var54, dSideTextureInterpolatedMinU, dSideTextureMaxV);
               tessellator.addVertexWithUV(dPosXMid, j + 0, var56, dSideTextureInterpolatedMaxU, dSideTextureMaxV);
               tessellator.addVertexWithUV(dPosXMid, j + 1, var56, dSideTextureInterpolatedMaxU, dSideTextureMinV);
               tessellator.addVertexWithUV(dPosXMid, j + 1, var56, dSideTextureInterpolatedMinU, dSideTextureMinV);
               tessellator.addVertexWithUV(dPosXMid, j + 0, var56, dSideTextureInterpolatedMinU, dSideTextureMaxV);
               tessellator.addVertexWithUV(dPosXMid, j + 0, var54, dSideTextureInterpolatedMaxU, dSideTextureMaxV);
               tessellator.addVertexWithUV(dPosXMid, j + 1, var54, dSideTextureInterpolatedMaxU, dSideTextureMinV);
            }

            if (var62 || j < iWorldHeight - 1 && blockAccess.isAirBlock(i + 1, j + 1, k)) {
               tessellator.addVertexWithUV(dPosXMid, j + 1 + 0.01, var56, dSideTextureInterpolatedMaxU, dSideTextureMinV);
               tessellator.addVertexWithUV(dPosXMax, j + 1 + 0.01, var56, dSideTextureInterpolatedMaxU, dSideTextureInterpolatedMidV);
               tessellator.addVertexWithUV(dPosXMax, j + 1 + 0.01, var54, dSideTextureInterpolatedMinU, dSideTextureInterpolatedMidV);
               tessellator.addVertexWithUV(dPosXMid, j + 1 + 0.01, var54, dSideTextureInterpolatedMinU, dSideTextureMinV);
               tessellator.addVertexWithUV(dPosXMax, j + 1 + 0.01, var56, dSideTextureInterpolatedMaxU, dSideTextureMinV);
               tessellator.addVertexWithUV(dPosXMid, j + 1 + 0.01, var56, dSideTextureInterpolatedMaxU, dSideTextureInterpolatedMidV);
               tessellator.addVertexWithUV(dPosXMid, j + 1 + 0.01, var54, dSideTextureInterpolatedMinU, dSideTextureInterpolatedMidV);
               tessellator.addVertexWithUV(dPosXMax, j + 1 + 0.01, var54, dSideTextureInterpolatedMinU, dSideTextureMinV);
            }

            if (var63 || j > 1 && blockAccess.isAirBlock(i + 1, j - 1, k)) {
               tessellator.addVertexWithUV(dPosXMid, j - 0.01, var56, dSideTextureInterpolatedMaxU, dSideTextureMinV);
               tessellator.addVertexWithUV(dPosXMax, j - 0.01, var56, dSideTextureInterpolatedMaxU, dSideTextureInterpolatedMidV);
               tessellator.addVertexWithUV(dPosXMax, j - 0.01, var54, dSideTextureInterpolatedMinU, dSideTextureInterpolatedMidV);
               tessellator.addVertexWithUV(dPosXMid, j - 0.01, var54, dSideTextureInterpolatedMinU, dSideTextureMinV);
               tessellator.addVertexWithUV(dPosXMax, j - 0.01, var56, dSideTextureInterpolatedMaxU, dSideTextureMinV);
               tessellator.addVertexWithUV(dPosXMid, j - 0.01, var56, dSideTextureInterpolatedMaxU, dSideTextureInterpolatedMidV);
               tessellator.addVertexWithUV(dPosXMid, j - 0.01, var54, dSideTextureInterpolatedMinU, dSideTextureInterpolatedMidV);
               tessellator.addVertexWithUV(dPosXMax, j - 0.01, var54, dSideTextureInterpolatedMinU, dSideTextureMinV);
            }
         }
      } else {
         tessellator.addVertexWithUV(dPosXMin, j + 1, dPosZMid, dPaneTextureMinU, dPaneTextureMinV);
         tessellator.addVertexWithUV(dPosXMin, j + 0, dPosZMid, dPaneTextureMinU, dPaneTextureMaxV);
         tessellator.addVertexWithUV(dPosXMax, j + 0, dPosZMid, dPaneTextureMaxU, dPaneTextureMaxV);
         tessellator.addVertexWithUV(dPosXMax, j + 1, dPosZMid, dPaneTextureMaxU, dPaneTextureMinV);
         tessellator.addVertexWithUV(dPosXMax, j + 1, dPosZMid, dPaneTextureMinU, dPaneTextureMinV);
         tessellator.addVertexWithUV(dPosXMax, j + 0, dPosZMid, dPaneTextureMinU, dPaneTextureMaxV);
         tessellator.addVertexWithUV(dPosXMin, j + 0, dPosZMid, dPaneTextureMaxU, dPaneTextureMaxV);
         tessellator.addVertexWithUV(dPosXMin, j + 1, dPosZMid, dPaneTextureMaxU, dPaneTextureMinV);
         if (var62) {
            tessellator.addVertexWithUV(dPosXMin, j + 1 + 0.01, var56, dSideTextureInterpolatedMaxU, dSideTextureMaxV);
            tessellator.addVertexWithUV(dPosXMax, j + 1 + 0.01, var56, dSideTextureInterpolatedMaxU, dSideTextureMinV);
            tessellator.addVertexWithUV(dPosXMax, j + 1 + 0.01, var54, dSideTextureInterpolatedMinU, dSideTextureMinV);
            tessellator.addVertexWithUV(dPosXMin, j + 1 + 0.01, var54, dSideTextureInterpolatedMinU, dSideTextureMaxV);
            tessellator.addVertexWithUV(dPosXMax, j + 1 + 0.01, var56, dSideTextureInterpolatedMaxU, dSideTextureMaxV);
            tessellator.addVertexWithUV(dPosXMin, j + 1 + 0.01, var56, dSideTextureInterpolatedMaxU, dSideTextureMinV);
            tessellator.addVertexWithUV(dPosXMin, j + 1 + 0.01, var54, dSideTextureInterpolatedMinU, dSideTextureMinV);
            tessellator.addVertexWithUV(dPosXMax, j + 1 + 0.01, var54, dSideTextureInterpolatedMinU, dSideTextureMaxV);
         } else {
            if (j < iWorldHeight - 1 && blockAccess.isAirBlock(i - 1, j + 1, k)) {
               tessellator.addVertexWithUV(dPosXMin, j + 1 + 0.01, var56, dSideTextureInterpolatedMaxU, dSideTextureInterpolatedMidV);
               tessellator.addVertexWithUV(dPosXMid, j + 1 + 0.01, var56, dSideTextureInterpolatedMaxU, dSideTextureMaxV);
               tessellator.addVertexWithUV(dPosXMid, j + 1 + 0.01, var54, dSideTextureInterpolatedMinU, dSideTextureMaxV);
               tessellator.addVertexWithUV(dPosXMin, j + 1 + 0.01, var54, dSideTextureInterpolatedMinU, dSideTextureInterpolatedMidV);
               tessellator.addVertexWithUV(dPosXMid, j + 1 + 0.01, var56, dSideTextureInterpolatedMaxU, dSideTextureInterpolatedMidV);
               tessellator.addVertexWithUV(dPosXMin, j + 1 + 0.01, var56, dSideTextureInterpolatedMaxU, dSideTextureMaxV);
               tessellator.addVertexWithUV(dPosXMin, j + 1 + 0.01, var54, dSideTextureInterpolatedMinU, dSideTextureMaxV);
               tessellator.addVertexWithUV(dPosXMid, j + 1 + 0.01, var54, dSideTextureInterpolatedMinU, dSideTextureInterpolatedMidV);
            }

            if (j < iWorldHeight - 1 && blockAccess.isAirBlock(i + 1, j + 1, k)) {
               tessellator.addVertexWithUV(dPosXMid, j + 1 + 0.01, var56, dSideTextureInterpolatedMaxU, dSideTextureMinV);
               tessellator.addVertexWithUV(dPosXMax, j + 1 + 0.01, var56, dSideTextureInterpolatedMaxU, dSideTextureInterpolatedMidV);
               tessellator.addVertexWithUV(dPosXMax, j + 1 + 0.01, var54, dSideTextureInterpolatedMinU, dSideTextureInterpolatedMidV);
               tessellator.addVertexWithUV(dPosXMid, j + 1 + 0.01, var54, dSideTextureInterpolatedMinU, dSideTextureMinV);
               tessellator.addVertexWithUV(dPosXMax, j + 1 + 0.01, var56, dSideTextureInterpolatedMaxU, dSideTextureMinV);
               tessellator.addVertexWithUV(dPosXMid, j + 1 + 0.01, var56, dSideTextureInterpolatedMaxU, dSideTextureInterpolatedMidV);
               tessellator.addVertexWithUV(dPosXMid, j + 1 + 0.01, var54, dSideTextureInterpolatedMinU, dSideTextureInterpolatedMidV);
               tessellator.addVertexWithUV(dPosXMax, j + 1 + 0.01, var54, dSideTextureInterpolatedMinU, dSideTextureMinV);
            }
         }

         if (var63) {
            tessellator.addVertexWithUV(dPosXMin, j - 0.01, var56, dSideTextureInterpolatedMaxU, dSideTextureMaxV);
            tessellator.addVertexWithUV(dPosXMax, j - 0.01, var56, dSideTextureInterpolatedMaxU, dSideTextureMinV);
            tessellator.addVertexWithUV(dPosXMax, j - 0.01, var54, dSideTextureInterpolatedMinU, dSideTextureMinV);
            tessellator.addVertexWithUV(dPosXMin, j - 0.01, var54, dSideTextureInterpolatedMinU, dSideTextureMaxV);
            tessellator.addVertexWithUV(dPosXMax, j - 0.01, var56, dSideTextureInterpolatedMaxU, dSideTextureMaxV);
            tessellator.addVertexWithUV(dPosXMin, j - 0.01, var56, dSideTextureInterpolatedMaxU, dSideTextureMinV);
            tessellator.addVertexWithUV(dPosXMin, j - 0.01, var54, dSideTextureInterpolatedMinU, dSideTextureMinV);
            tessellator.addVertexWithUV(dPosXMax, j - 0.01, var54, dSideTextureInterpolatedMinU, dSideTextureMaxV);
         } else {
            if (j > 1 && blockAccess.isAirBlock(i - 1, j - 1, k)) {
               tessellator.addVertexWithUV(dPosXMin, j - 0.01, var56, dSideTextureInterpolatedMaxU, dSideTextureInterpolatedMidV);
               tessellator.addVertexWithUV(dPosXMid, j - 0.01, var56, dSideTextureInterpolatedMaxU, dSideTextureMaxV);
               tessellator.addVertexWithUV(dPosXMid, j - 0.01, var54, dSideTextureInterpolatedMinU, dSideTextureMaxV);
               tessellator.addVertexWithUV(dPosXMin, j - 0.01, var54, dSideTextureInterpolatedMinU, dSideTextureInterpolatedMidV);
               tessellator.addVertexWithUV(dPosXMid, j - 0.01, var56, dSideTextureInterpolatedMaxU, dSideTextureInterpolatedMidV);
               tessellator.addVertexWithUV(dPosXMin, j - 0.01, var56, dSideTextureInterpolatedMaxU, dSideTextureMaxV);
               tessellator.addVertexWithUV(dPosXMin, j - 0.01, var54, dSideTextureInterpolatedMinU, dSideTextureMaxV);
               tessellator.addVertexWithUV(dPosXMid, j - 0.01, var54, dSideTextureInterpolatedMinU, dSideTextureInterpolatedMidV);
            }

            if (j > 1 && blockAccess.isAirBlock(i + 1, j - 1, k)) {
               tessellator.addVertexWithUV(dPosXMid, j - 0.01, var56, dSideTextureInterpolatedMaxU, dSideTextureMinV);
               tessellator.addVertexWithUV(dPosXMax, j - 0.01, var56, dSideTextureInterpolatedMaxU, dSideTextureInterpolatedMidV);
               tessellator.addVertexWithUV(dPosXMax, j - 0.01, var54, dSideTextureInterpolatedMinU, dSideTextureInterpolatedMidV);
               tessellator.addVertexWithUV(dPosXMid, j - 0.01, var54, dSideTextureInterpolatedMinU, dSideTextureMinV);
               tessellator.addVertexWithUV(dPosXMax, j - 0.01, var56, dSideTextureInterpolatedMaxU, dSideTextureMinV);
               tessellator.addVertexWithUV(dPosXMid, j - 0.01, var56, dSideTextureInterpolatedMaxU, dSideTextureInterpolatedMidV);
               tessellator.addVertexWithUV(dPosXMid, j - 0.01, var54, dSideTextureInterpolatedMinU, dSideTextureInterpolatedMidV);
               tessellator.addVertexWithUV(dPosXMax, j - 0.01, var54, dSideTextureInterpolatedMinU, dSideTextureMinV);
            }
         }
      }

      if ((!var58 || !var59) && (var60 || var61 || var58 || var59)) {
         if (var58 && !var59) {
            tessellator.addVertexWithUV(dPosXMid, j + 1, dPosZMin, dPaneTextureMinU, dPaneTextureMinV);
            tessellator.addVertexWithUV(dPosXMid, j + 0, dPosZMin, dPaneTextureMinU, dPaneTextureMaxV);
            tessellator.addVertexWithUV(dPosXMid, j + 0, dPosZMid, dPaneTextureInterpolatedMidU, dPaneTextureMaxV);
            tessellator.addVertexWithUV(dPosXMid, j + 1, dPosZMid, dPaneTextureInterpolatedMidU, dPaneTextureMinV);
            tessellator.addVertexWithUV(dPosXMid, j + 1, dPosZMid, dPaneTextureMinU, dPaneTextureMinV);
            tessellator.addVertexWithUV(dPosXMid, j + 0, dPosZMid, dPaneTextureMinU, dPaneTextureMaxV);
            tessellator.addVertexWithUV(dPosXMid, j + 0, dPosZMin, dPaneTextureInterpolatedMidU, dPaneTextureMaxV);
            tessellator.addVertexWithUV(dPosXMid, j + 1, dPosZMin, dPaneTextureInterpolatedMidU, dPaneTextureMinV);
            if (!var61 && !var60) {
               tessellator.addVertexWithUV(var50, j + 1, dPosZMid, dSideTextureInterpolatedMinU, dSideTextureMinV);
               tessellator.addVertexWithUV(var50, j + 0, dPosZMid, dSideTextureInterpolatedMinU, dSideTextureMaxV);
               tessellator.addVertexWithUV(var52, j + 0, dPosZMid, dSideTextureInterpolatedMaxU, dSideTextureMaxV);
               tessellator.addVertexWithUV(var52, j + 1, dPosZMid, dSideTextureInterpolatedMaxU, dSideTextureMinV);
               tessellator.addVertexWithUV(var52, j + 1, dPosZMid, dSideTextureInterpolatedMinU, dSideTextureMinV);
               tessellator.addVertexWithUV(var52, j + 0, dPosZMid, dSideTextureInterpolatedMinU, dSideTextureMaxV);
               tessellator.addVertexWithUV(var50, j + 0, dPosZMid, dSideTextureInterpolatedMaxU, dSideTextureMaxV);
               tessellator.addVertexWithUV(var50, j + 1, dPosZMid, dSideTextureInterpolatedMaxU, dSideTextureMinV);
            }

            if (var62 || j < iWorldHeight - 1 && blockAccess.isAirBlock(i, j + 1, k - 1)) {
               tessellator.addVertexWithUV(var50, j + 1 + 0.005, dPosZMin, dSideTextureInterpolatedMaxU, dSideTextureMinV);
               tessellator.addVertexWithUV(var50, j + 1 + 0.005, dPosZMid, dSideTextureInterpolatedMaxU, dSideTextureInterpolatedMidV);
               tessellator.addVertexWithUV(var52, j + 1 + 0.005, dPosZMid, dSideTextureInterpolatedMinU, dSideTextureInterpolatedMidV);
               tessellator.addVertexWithUV(var52, j + 1 + 0.005, dPosZMin, dSideTextureInterpolatedMinU, dSideTextureMinV);
               tessellator.addVertexWithUV(var50, j + 1 + 0.005, dPosZMid, dSideTextureInterpolatedMaxU, dSideTextureMinV);
               tessellator.addVertexWithUV(var50, j + 1 + 0.005, dPosZMin, dSideTextureInterpolatedMaxU, dSideTextureInterpolatedMidV);
               tessellator.addVertexWithUV(var52, j + 1 + 0.005, dPosZMin, dSideTextureInterpolatedMinU, dSideTextureInterpolatedMidV);
               tessellator.addVertexWithUV(var52, j + 1 + 0.005, dPosZMid, dSideTextureInterpolatedMinU, dSideTextureMinV);
            }

            if (var63 || j > 1 && blockAccess.isAirBlock(i, j - 1, k - 1)) {
               tessellator.addVertexWithUV(var50, j - 0.005, dPosZMin, dSideTextureInterpolatedMaxU, dSideTextureMinV);
               tessellator.addVertexWithUV(var50, j - 0.005, dPosZMid, dSideTextureInterpolatedMaxU, dSideTextureInterpolatedMidV);
               tessellator.addVertexWithUV(var52, j - 0.005, dPosZMid, dSideTextureInterpolatedMinU, dSideTextureInterpolatedMidV);
               tessellator.addVertexWithUV(var52, j - 0.005, dPosZMin, dSideTextureInterpolatedMinU, dSideTextureMinV);
               tessellator.addVertexWithUV(var50, j - 0.005, dPosZMid, dSideTextureInterpolatedMaxU, dSideTextureMinV);
               tessellator.addVertexWithUV(var50, j - 0.005, dPosZMin, dSideTextureInterpolatedMaxU, dSideTextureInterpolatedMidV);
               tessellator.addVertexWithUV(var52, j - 0.005, dPosZMin, dSideTextureInterpolatedMinU, dSideTextureInterpolatedMidV);
               tessellator.addVertexWithUV(var52, j - 0.005, dPosZMid, dSideTextureInterpolatedMinU, dSideTextureMinV);
            }
         } else if (!var58 && var59) {
            tessellator.addVertexWithUV(dPosXMid, j + 1, dPosZMid, dPaneTextureInterpolatedMidU, dPaneTextureMinV);
            tessellator.addVertexWithUV(dPosXMid, j + 0, dPosZMid, dPaneTextureInterpolatedMidU, dPaneTextureMaxV);
            tessellator.addVertexWithUV(dPosXMid, j + 0, dPosZMax, dPaneTextureMaxU, dPaneTextureMaxV);
            tessellator.addVertexWithUV(dPosXMid, j + 1, dPosZMax, dPaneTextureMaxU, dPaneTextureMinV);
            tessellator.addVertexWithUV(dPosXMid, j + 1, dPosZMax, dPaneTextureInterpolatedMidU, dPaneTextureMinV);
            tessellator.addVertexWithUV(dPosXMid, j + 0, dPosZMax, dPaneTextureInterpolatedMidU, dPaneTextureMaxV);
            tessellator.addVertexWithUV(dPosXMid, j + 0, dPosZMid, dPaneTextureMaxU, dPaneTextureMaxV);
            tessellator.addVertexWithUV(dPosXMid, j + 1, dPosZMid, dPaneTextureMaxU, dPaneTextureMinV);
            if (!var61 && !var60) {
               tessellator.addVertexWithUV(var52, j + 1, dPosZMid, dSideTextureInterpolatedMinU, dSideTextureMinV);
               tessellator.addVertexWithUV(var52, j + 0, dPosZMid, dSideTextureInterpolatedMinU, dSideTextureMaxV);
               tessellator.addVertexWithUV(var50, j + 0, dPosZMid, dSideTextureInterpolatedMaxU, dSideTextureMaxV);
               tessellator.addVertexWithUV(var50, j + 1, dPosZMid, dSideTextureInterpolatedMaxU, dSideTextureMinV);
               tessellator.addVertexWithUV(var50, j + 1, dPosZMid, dSideTextureInterpolatedMinU, dSideTextureMinV);
               tessellator.addVertexWithUV(var50, j + 0, dPosZMid, dSideTextureInterpolatedMinU, dSideTextureMaxV);
               tessellator.addVertexWithUV(var52, j + 0, dPosZMid, dSideTextureInterpolatedMaxU, dSideTextureMaxV);
               tessellator.addVertexWithUV(var52, j + 1, dPosZMid, dSideTextureInterpolatedMaxU, dSideTextureMinV);
            }

            if (var62 || j < iWorldHeight - 1 && blockAccess.isAirBlock(i, j + 1, k + 1)) {
               tessellator.addVertexWithUV(var50, j + 1 + 0.005, dPosZMid, dSideTextureInterpolatedMinU, dSideTextureInterpolatedMidV);
               tessellator.addVertexWithUV(var50, j + 1 + 0.005, dPosZMax, dSideTextureInterpolatedMinU, dSideTextureMaxV);
               tessellator.addVertexWithUV(var52, j + 1 + 0.005, dPosZMax, dSideTextureInterpolatedMaxU, dSideTextureMaxV);
               tessellator.addVertexWithUV(var52, j + 1 + 0.005, dPosZMid, dSideTextureInterpolatedMaxU, dSideTextureInterpolatedMidV);
               tessellator.addVertexWithUV(var50, j + 1 + 0.005, dPosZMax, dSideTextureInterpolatedMinU, dSideTextureInterpolatedMidV);
               tessellator.addVertexWithUV(var50, j + 1 + 0.005, dPosZMid, dSideTextureInterpolatedMinU, dSideTextureMaxV);
               tessellator.addVertexWithUV(var52, j + 1 + 0.005, dPosZMid, dSideTextureInterpolatedMaxU, dSideTextureMaxV);
               tessellator.addVertexWithUV(var52, j + 1 + 0.005, dPosZMax, dSideTextureInterpolatedMaxU, dSideTextureInterpolatedMidV);
            }

            if (var63 || j > 1 && blockAccess.isAirBlock(i, j - 1, k + 1)) {
               tessellator.addVertexWithUV(var50, j - 0.005, dPosZMid, dSideTextureInterpolatedMinU, dSideTextureInterpolatedMidV);
               tessellator.addVertexWithUV(var50, j - 0.005, dPosZMax, dSideTextureInterpolatedMinU, dSideTextureMaxV);
               tessellator.addVertexWithUV(var52, j - 0.005, dPosZMax, dSideTextureInterpolatedMaxU, dSideTextureMaxV);
               tessellator.addVertexWithUV(var52, j - 0.005, dPosZMid, dSideTextureInterpolatedMaxU, dSideTextureInterpolatedMidV);
               tessellator.addVertexWithUV(var50, j - 0.005, dPosZMax, dSideTextureInterpolatedMinU, dSideTextureInterpolatedMidV);
               tessellator.addVertexWithUV(var50, j - 0.005, dPosZMid, dSideTextureInterpolatedMinU, dSideTextureMaxV);
               tessellator.addVertexWithUV(var52, j - 0.005, dPosZMid, dSideTextureInterpolatedMaxU, dSideTextureMaxV);
               tessellator.addVertexWithUV(var52, j - 0.005, dPosZMax, dSideTextureInterpolatedMaxU, dSideTextureInterpolatedMidV);
            }
         }
      } else {
         tessellator.addVertexWithUV(dPosXMid, j + 1, dPosZMax, dPaneTextureMinU, dPaneTextureMinV);
         tessellator.addVertexWithUV(dPosXMid, j + 0, dPosZMax, dPaneTextureMinU, dPaneTextureMaxV);
         tessellator.addVertexWithUV(dPosXMid, j + 0, dPosZMin, dPaneTextureMaxU, dPaneTextureMaxV);
         tessellator.addVertexWithUV(dPosXMid, j + 1, dPosZMin, dPaneTextureMaxU, dPaneTextureMinV);
         tessellator.addVertexWithUV(dPosXMid, j + 1, dPosZMin, dPaneTextureMinU, dPaneTextureMinV);
         tessellator.addVertexWithUV(dPosXMid, j + 0, dPosZMin, dPaneTextureMinU, dPaneTextureMaxV);
         tessellator.addVertexWithUV(dPosXMid, j + 0, dPosZMax, dPaneTextureMaxU, dPaneTextureMaxV);
         tessellator.addVertexWithUV(dPosXMid, j + 1, dPosZMax, dPaneTextureMaxU, dPaneTextureMinV);
         if (var62) {
            tessellator.addVertexWithUV(var52, j + 1 + 0.005, dPosZMax, dSideTextureInterpolatedMaxU, dSideTextureMaxV);
            tessellator.addVertexWithUV(var52, j + 1 + 0.005, dPosZMin, dSideTextureInterpolatedMaxU, dSideTextureMinV);
            tessellator.addVertexWithUV(var50, j + 1 + 0.005, dPosZMin, dSideTextureInterpolatedMinU, dSideTextureMinV);
            tessellator.addVertexWithUV(var50, j + 1 + 0.005, dPosZMax, dSideTextureInterpolatedMinU, dSideTextureMaxV);
            tessellator.addVertexWithUV(var52, j + 1 + 0.005, dPosZMin, dSideTextureInterpolatedMaxU, dSideTextureMaxV);
            tessellator.addVertexWithUV(var52, j + 1 + 0.005, dPosZMax, dSideTextureInterpolatedMaxU, dSideTextureMinV);
            tessellator.addVertexWithUV(var50, j + 1 + 0.005, dPosZMax, dSideTextureInterpolatedMinU, dSideTextureMinV);
            tessellator.addVertexWithUV(var50, j + 1 + 0.005, dPosZMin, dSideTextureInterpolatedMinU, dSideTextureMaxV);
         } else {
            if (j < iWorldHeight - 1 && blockAccess.isAirBlock(i, j + 1, k - 1)) {
               tessellator.addVertexWithUV(var50, j + 1 + 0.005, dPosZMin, dSideTextureInterpolatedMaxU, dSideTextureMinV);
               tessellator.addVertexWithUV(var50, j + 1 + 0.005, dPosZMid, dSideTextureInterpolatedMaxU, dSideTextureInterpolatedMidV);
               tessellator.addVertexWithUV(var52, j + 1 + 0.005, dPosZMid, dSideTextureInterpolatedMinU, dSideTextureInterpolatedMidV);
               tessellator.addVertexWithUV(var52, j + 1 + 0.005, dPosZMin, dSideTextureInterpolatedMinU, dSideTextureMinV);
               tessellator.addVertexWithUV(var50, j + 1 + 0.005, dPosZMid, dSideTextureInterpolatedMaxU, dSideTextureMinV);
               tessellator.addVertexWithUV(var50, j + 1 + 0.005, dPosZMin, dSideTextureInterpolatedMaxU, dSideTextureInterpolatedMidV);
               tessellator.addVertexWithUV(var52, j + 1 + 0.005, dPosZMin, dSideTextureInterpolatedMinU, dSideTextureInterpolatedMidV);
               tessellator.addVertexWithUV(var52, j + 1 + 0.005, dPosZMid, dSideTextureInterpolatedMinU, dSideTextureMinV);
            }

            if (j < iWorldHeight - 1 && blockAccess.isAirBlock(i, j + 1, k + 1)) {
               tessellator.addVertexWithUV(var50, j + 1 + 0.005, dPosZMid, dSideTextureInterpolatedMinU, dSideTextureInterpolatedMidV);
               tessellator.addVertexWithUV(var50, j + 1 + 0.005, dPosZMax, dSideTextureInterpolatedMinU, dSideTextureMaxV);
               tessellator.addVertexWithUV(var52, j + 1 + 0.005, dPosZMax, dSideTextureInterpolatedMaxU, dSideTextureMaxV);
               tessellator.addVertexWithUV(var52, j + 1 + 0.005, dPosZMid, dSideTextureInterpolatedMaxU, dSideTextureInterpolatedMidV);
               tessellator.addVertexWithUV(var50, j + 1 + 0.005, dPosZMax, dSideTextureInterpolatedMinU, dSideTextureInterpolatedMidV);
               tessellator.addVertexWithUV(var50, j + 1 + 0.005, dPosZMid, dSideTextureInterpolatedMinU, dSideTextureMaxV);
               tessellator.addVertexWithUV(var52, j + 1 + 0.005, dPosZMid, dSideTextureInterpolatedMaxU, dSideTextureMaxV);
               tessellator.addVertexWithUV(var52, j + 1 + 0.005, dPosZMax, dSideTextureInterpolatedMaxU, dSideTextureInterpolatedMidV);
            }
         }

         if (var63) {
            tessellator.addVertexWithUV(var52, j - 0.005, dPosZMax, dSideTextureInterpolatedMaxU, dSideTextureMaxV);
            tessellator.addVertexWithUV(var52, j - 0.005, dPosZMin, dSideTextureInterpolatedMaxU, dSideTextureMinV);
            tessellator.addVertexWithUV(var50, j - 0.005, dPosZMin, dSideTextureInterpolatedMinU, dSideTextureMinV);
            tessellator.addVertexWithUV(var50, j - 0.005, dPosZMax, dSideTextureInterpolatedMinU, dSideTextureMaxV);
            tessellator.addVertexWithUV(var52, j - 0.005, dPosZMin, dSideTextureInterpolatedMaxU, dSideTextureMaxV);
            tessellator.addVertexWithUV(var52, j - 0.005, dPosZMax, dSideTextureInterpolatedMaxU, dSideTextureMinV);
            tessellator.addVertexWithUV(var50, j - 0.005, dPosZMax, dSideTextureInterpolatedMinU, dSideTextureMinV);
            tessellator.addVertexWithUV(var50, j - 0.005, dPosZMin, dSideTextureInterpolatedMinU, dSideTextureMaxV);
         } else {
            if (j > 1 && blockAccess.isAirBlock(i, j - 1, k - 1)) {
               tessellator.addVertexWithUV(var50, j - 0.005, dPosZMin, dSideTextureInterpolatedMaxU, dSideTextureMinV);
               tessellator.addVertexWithUV(var50, j - 0.005, dPosZMid, dSideTextureInterpolatedMaxU, dSideTextureInterpolatedMidV);
               tessellator.addVertexWithUV(var52, j - 0.005, dPosZMid, dSideTextureInterpolatedMinU, dSideTextureInterpolatedMidV);
               tessellator.addVertexWithUV(var52, j - 0.005, dPosZMin, dSideTextureInterpolatedMinU, dSideTextureMinV);
               tessellator.addVertexWithUV(var50, j - 0.005, dPosZMid, dSideTextureInterpolatedMaxU, dSideTextureMinV);
               tessellator.addVertexWithUV(var50, j - 0.005, dPosZMin, dSideTextureInterpolatedMaxU, dSideTextureInterpolatedMidV);
               tessellator.addVertexWithUV(var52, j - 0.005, dPosZMin, dSideTextureInterpolatedMinU, dSideTextureInterpolatedMidV);
               tessellator.addVertexWithUV(var52, j - 0.005, dPosZMid, dSideTextureInterpolatedMinU, dSideTextureMinV);
            }

            if (j > 1 && blockAccess.isAirBlock(i, j - 1, k + 1)) {
               tessellator.addVertexWithUV(var50, j - 0.005, dPosZMid, dSideTextureInterpolatedMinU, dSideTextureInterpolatedMidV);
               tessellator.addVertexWithUV(var50, j - 0.005, dPosZMax, dSideTextureInterpolatedMinU, dSideTextureMaxV);
               tessellator.addVertexWithUV(var52, j - 0.005, dPosZMax, dSideTextureInterpolatedMaxU, dSideTextureMaxV);
               tessellator.addVertexWithUV(var52, j - 0.005, dPosZMid, dSideTextureInterpolatedMaxU, dSideTextureInterpolatedMidV);
               tessellator.addVertexWithUV(var50, j - 0.005, dPosZMax, dSideTextureInterpolatedMinU, dSideTextureInterpolatedMidV);
               tessellator.addVertexWithUV(var50, j - 0.005, dPosZMid, dSideTextureInterpolatedMinU, dSideTextureMaxV);
               tessellator.addVertexWithUV(var52, j - 0.005, dPosZMid, dSideTextureInterpolatedMaxU, dSideTextureMaxV);
               tessellator.addVertexWithUV(var52, j - 0.005, dPosZMax, dSideTextureInterpolatedMaxU, dSideTextureInterpolatedMidV);
            }
         }
      }

      return true;
   }

   @Environment(EnvType.CLIENT)
   public boolean renderLightningRod(RenderBlocks renderBlocks, IBlockAccess blockAccess, int i, int j, int k, Block block) {
      Icon texture = this.iconLightningRod;
      renderBlocks.setRenderBounds(0.46875, 0.0, 0.46875, 0.53125, 1.0, 0.53125);
      RenderUtils.renderStandardBlockWithTexture(renderBlocks, block, i, j, k, this.iconLightningRod);
      if (blockAccess.getBlockId(i, j - 1, k) != BTWBlocks.aestheticNonOpaque.blockID || blockAccess.getBlockMetadata(i, j - 1, k) != 12) {
         renderBlocks.setRenderBounds(0.375, 0.0, 0.375, 0.625, 0.125, 0.625);
         RenderUtils.renderStandardBlockWithTexture(renderBlocks, block, i, j, k, this.iconLightningRod);
      }

      int iBlockAboveID = blockAccess.getBlockId(i, j + 1, k);
      int iBlockAboveMetadata = blockAccess.getBlockMetadata(i, j + 1, k);
      if (iBlockAboveID != BTWBlocks.aestheticNonOpaque.blockID || iBlockAboveMetadata != 12) {
         if (iBlockAboveID == BTWBlocks.legacyCandle.blockID) {
            renderBlocks.setRenderBounds(0.375, 0.99609375, 0.375, 0.625, 1.0585938F, 0.625);
         } else {
            renderBlocks.setRenderBounds(0.40625, 0.625, 0.40625, 0.59375, 0.8125, 0.59375);
         }

         RenderUtils.renderStandardBlockWithTexture(renderBlocks, block, i, j, k, this.iconLightningRod);
      }

      return true;
   }

   @Environment(EnvType.CLIENT)
   public void renderLightningRodInvBlock(RenderBlocks renderBlocks, Block block) {
      Icon texture = this.iconLightningRod;
      renderBlocks.setRenderBounds(0.46875, 0.0, 0.46875, 0.53125, 1.0, 0.53125);
      RenderUtils.renderInvBlockWithTexture(renderBlocks, block, -0.5F, -0.5F, -0.5F, texture);
      renderBlocks.setRenderBounds(0.375, 0.0, 0.375, 0.625, 0.125, 0.625);
      RenderUtils.renderInvBlockWithTexture(renderBlocks, block, -0.5F, -0.5F, -0.5F, texture);
      renderBlocks.setRenderBounds(0.40625, 0.625, 0.40625, 0.59375, 0.8125, 0.59375);
      RenderUtils.renderInvBlockWithTexture(renderBlocks, block, -0.5F, -0.5F, -0.5F, texture);
   }
}
