package btw.block.blocks;

import btw.block.BTWBlocks;
import btw.client.render.util.RenderUtils;
import btw.item.BTWItems;
import btw.util.MiscUtils;
import btw.world.util.WorldUtils;
import java.util.List;
import java.util.Random;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Icon;
import net.minecraft.src.IconRegister;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.MovingObjectPosition;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.Vec3;
import net.minecraft.src.World;

public class UnfiredPotteryBlock extends Block {
   public static final int NUM_SUBTYPES = 14;
   public static final int NUM_SPUN_POTTERY_TYPES = 4;
   public static final int SUBTYPE_CRUCIBLE = 0;
   public static final int SUBTYPE_PLANTER = 1;
   public static final int SUBTYPE_VASE = 2;
   public static final int SUBTYPE_URN = 3;
   public static final int SUBTYPE_MOULD = 4;
   public static final int SUBTYPE_CLAY_BRICK = 5;
   public static final int SUBTYPE_CLAY_BRICK_X_ALIGNED = 6;
   public static final int SUBTYPE_NETHER_BRICK = 7;
   public static final int SUBTYPE_NETHER_BRICK_X_ALIGNED = 8;
   public static final int SUBTYPE_UNCOOKED_CAKE = 9;
   public static final int SUBTYPE_UNCOOKED_COOKIES = 10;
   public static final int SUBTYPE_UNCOOKED_COOKIES_I_ALIGNED = 11;
   public static final int SUBTYPE_UNCOOKED_PUMPKIN_PIE = 12;
   public static final int SUBTYPE_UNCOOKED_BREAD = 13;
   public static final int SUBTYPE_UNCOOKED_BREAD_I_ALIGNED = 14;
   public static final int ROTATIONS_ON_TURNTABLE_TO_CHANG_STATE = 8;
   public static final float UNFIRED_POTTERY_URN_BASE_WIDTH = 0.25F;
   public static final float UNFIRED_POTTERY_URN_BASE_HALF_WIDTH = 0.125F;
   public static final float UNFIRED_POTTERY_URN_BASE_HEIGHT = 0.0625F;
   public static final float UNFIRED_POTTERY_URN_BODY_WIDTH = 0.375F;
   public static final float UNFIRED_POTTERY_URN_BODY_HALF_WIDTH = 0.1875F;
   public static final float UNFIRED_POTTERY_URN_BODY_HEIGHT = 0.375F;
   public static final float UNFIRED_POTTERY_URN_NECK_WIDTH = 0.25F;
   public static final float UNFIRED_POTTERY_URN_NECK_HALF_WIDTH = 0.125F;
   public static final float UNFIRED_POTTERY_URN_NECK_HEIGHT = 0.0625F;
   public static final float UNFIRED_POTTERY_URN_TOP_WIDTH = 0.375F;
   public static final float UNFIRED_POTTERY_URN_TOP_HALF_WIDTH = 0.1875F;
   public static final float UNFIRED_POTTERY_URN_TOP_HEIGHT = 0.0625F;
   public static final float UNFIRED_POTTERY_URN_LID_WIDTH = 0.25F;
   public static final float UNFIRED_POTTERY_URN_LID_HALF_WIDTH = 0.125F;
   public static final float UNFIRED_POTTERY_URN_LID_HEIGHT = 0.0625F;
   public static final float UNFIRED_POTTERY_URN_HEIGHT = 0.625F;
   public static final float UNFIRED_POTTERY_MOULD_HEIGHT = 0.125F;
   public static final float UNFIRED_POTTERY_MOULD_WIDTH = 0.375F;
   public static final float UNFIRED_POTTERY_MOULD_HALF_WIDTH = 0.1875F;
   public static final float UNFIRED_POTTERY_BRICK_HEIGHT = 0.25F;
   public static final float UNFIRED_POTTERY_BRICK_WIDTH = 0.375F;
   public static final float UNFIRED_POTTERY_BRICK_HALF_WIDTH = 0.1875F;
   public static final float UNFIRED_POTTERY_BRICK_LENGTH = 0.75F;
   public static final float UNFIRED_POTTERY_BRICK_HALF_LENGTH = 0.375F;
   public static final float UNFIRED_POTTERY_UNCOOKED_CAKE_HEIGHT = 0.5F;
   public static final float UNFIRED_POTTERY_UNCOOKED_CAKE_WIDTH = 0.875F;
   public static final float UNFIRED_POTTERY_UNCOOKED_CAKE_HALF_WIDTH = 0.4375F;
   public static final float UNFIRED_POTTERY_UNCOOKED_CAKE_LENGTH = 0.875F;
   public static final float UNFIRED_POTTERY_UNCOOKED_CAKE_HALF_LENGTH = 0.4375F;
   public static final float UNFIRED_POTTERY_UNCOOKED_COOKIES_HEIGHT = 0.0625F;
   public static final float UNFIRED_POTTERY_UNCOOKED_COOKIES_WIDTH = 0.375F;
   public static final float UNFIRED_POTTERY_UNCOOKED_COOKIES_HALF_WIDTH = 0.1875F;
   public static final float UNFIRED_POTTERY_UNCOOKED_COOKIES_LENGTH = 0.875F;
   public static final float UNFIRED_POTTERY_UNCOOKED_COOKIES_HALF_LENGTH = 0.4375F;
   public static final float UNFIRED_POTTERY_UNCOOKED_COOKIES_INDIVIDUAL_WIDTH = 0.125F;
   public static final float UNFIRED_POTTERY_UNCOOKED_COOKIES_INDIVIDUAL_HALF_WIDTH = 0.0625F;
   public static final float UNFIRED_POTTERY_UNCOOKED_PUMPKIN_PIE_HEIGHT = 0.25F;
   public static final float UNFIRED_POTTERY_UNCOOKED_PUMPKIN_PIE_WIDTH = 0.75F;
   public static final float UNFIRED_POTTERY_UNCOOKED_PUMPKIN_PIE_HALF_WIDTH = 0.375F;
   public static final float UNFIRED_POTTERY_UNCOOKED_PUMPKIN_PIE_LENGTH = 0.75F;
   public static final float UNFIRED_POTTERY_UNCOOKED_PUMPKIN_PIE_HALF_LENGTH = 0.375F;
   @Environment(EnvType.CLIENT)
   private Icon iconNetherSludge;
   @Environment(EnvType.CLIENT)
   private Icon iconUncookedPastry;
   @Environment(EnvType.CLIENT)
   private Icon iconUncookedPumpkinPieTop;

   public UnfiredPotteryBlock(int iBlockID) {
      super(iBlockID, Material.clay);
      this.c(0.6F);
      this.setShovelsEffectiveOn(true);
      this.a(BTWBlocks.stepSoundSquish);
      this.c("fcBlockUnfiredPottery");
      this.a(CreativeTabs.tabDecorations);
   }

   @Override
   public void onBlockAdded(World world, int i, int j, int k) {
      if (!WorldUtils.doesBlockHaveLargeCenterHardpointToFacing(world, i, j - 1, k, 1, true)) {
         this.c(world, i, j, k, world.getBlockMetadata(i, j, k), 0);
         world.setBlockWithNotify(i, j, k, 0);
      }
   }

   @Override
   public int onBlockPlaced(World world, int i, int j, int k, int iFacing, float fClickX, float fClickY, float fClickZ, int iMetadata) {
      if (iFacing == 4 || iFacing == 5) {
         if (iMetadata == 5) {
            iMetadata = 6;
         } else if (iMetadata == 7) {
            iMetadata = 8;
         } else if (iMetadata == 10) {
            iMetadata = 11;
         } else if (iMetadata == 13) {
            iMetadata = 14;
         }
      }

      return iMetadata;
   }

   @Override
   public void onBlockPlacedBy(World world, int i, int j, int k, EntityLiving placingEntity, ItemStack stack) {
      int iFacing = MiscUtils.convertOrientationToFlatBlockFacingReversed(placingEntity);
      if (iFacing == 4 || iFacing == 5) {
         int iMetadata = world.getBlockMetadata(i, j, k);
         if (iMetadata == 5) {
            world.setBlockMetadataWithNotify(i, j, k, 6);
         } else if (iMetadata == 7) {
            world.setBlockMetadataWithNotify(i, j, k, 8);
         } else if (iMetadata == 10) {
            world.setBlockMetadataWithNotify(i, j, k, 11);
         } else if (iMetadata == 13) {
            world.setBlockMetadataWithNotify(i, j, k, 14);
         }
      }
   }

   @Override
   public int idDropped(int iMetadata, Random random, int iFortuneModifier) {
      if (iMetadata == 5 || iMetadata == 6) {
         return Item.clay.itemID;
      } else if (iMetadata == 7 || iMetadata == 8) {
         return BTWItems.netherSludge.itemID;
      } else if (iMetadata == 9) {
         return BTWItems.unbakedCake.itemID;
      } else if (iMetadata == 10 || iMetadata == 11) {
         return BTWItems.unbakedCookies.itemID;
      } else if (iMetadata == 12) {
         return BTWItems.unbakedPumpkinPie.itemID;
      } else {
         return iMetadata != 13 && iMetadata != 14 ? this.blockID : BTWItems.breadDough.itemID;
      }
   }

   @Override
   public int damageDropped(int iMetadata) {
      return iMetadata < 4 ? iMetadata : 0;
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
   public AxisAlignedBB getBlockBoundsFromPoolBasedOnState(IBlockAccess blockAccess, int i, int j, int k) {
      int iMetaData = blockAccess.getBlockMetadata(i, j, k);
      switch (iMetaData) {
         case 0:
         case 1:
         default:
            return AxisAlignedBB.getAABBPool().getAABB(0.0, 0.0, 0.0, 1.0, 1.0, 1.0);
         case 2:
            return AxisAlignedBB.getAABBPool().getAABB(0.1875, 0.0, 0.1875, 0.8125, 1.0, 0.8125);
         case 3:
            return AxisAlignedBB.getAABBPool().getAABB(0.3125, 0.0, 0.3125, 0.6875, 0.625, 0.6875);
         case 4:
            return AxisAlignedBB.getAABBPool().getAABB(0.3125, 0.0, 0.3125, 0.6875, 0.125, 0.6875);
         case 5:
         case 7:
         case 13:
            return AxisAlignedBB.getAABBPool().getAABB(0.3125, 0.0, 0.125, 0.6875, 0.25, 0.875);
         case 6:
         case 8:
         case 14:
            return AxisAlignedBB.getAABBPool().getAABB(0.125, 0.0, 0.3125, 0.875, 0.25, 0.6875);
         case 9:
            return AxisAlignedBB.getAABBPool().getAABB(0.0625, 0.0, 0.0625, 0.9375, 0.5, 0.9375);
         case 10:
            return AxisAlignedBB.getAABBPool().getAABB(0.3125, 0.0, 0.0625, 0.6875, 0.0625, 0.9375);
         case 11:
            return AxisAlignedBB.getAABBPool().getAABB(0.0625, 0.0, 0.3125, 0.9375, 0.0625, 0.6875);
         case 12:
            return AxisAlignedBB.getAABBPool().getAABB(0.125, 0.0, 0.125, 0.875, 0.25, 0.875);
      }
   }

   @Override
   public boolean canPlaceBlockAt(World world, int i, int j, int k) {
      return WorldUtils.doesBlockHaveLargeCenterHardpointToFacing(world, i, j - 1, k, 1, true);
   }

   @Override
   public void updateTick(World world, int i, int j, int k, Random rand) {
      if (!WorldUtils.doesBlockHaveLargeCenterHardpointToFacing(world, i, j - 1, k, 1, true)) {
         this.c(world, i, j, k, world.getBlockMetadata(i, j, k), 0);
         world.setBlockToAir(i, j, k);
      }
   }

   @Override
   public void onNeighborBlockChange(World world, int i, int j, int k, int iBlockID) {
      if (!WorldUtils.doesBlockHaveLargeCenterHardpointToFacing(world, i, j - 1, k, 1, true) && !world.isUpdatePendingThisTickForBlock(i, j, k, this.blockID)) {
         world.scheduleBlockUpdate(i, j, k, this.blockID, 1);
      }
   }

   @Override
   public MovingObjectPosition collisionRayTrace(World world, int i, int j, int k, Vec3 startRay, Vec3 endRay) {
      int iMetadata = world.getBlockMetadata(i, j, k);
      if (iMetadata == 2) {
         return BTWBlocks.vase.collisionRayTrace(world, i, j, k, startRay, endRay);
      } else {
         return iMetadata == 1
            ? BTWBlocks.planter.collisionRayTrace(world, i, j, k, startRay, endRay)
            : super.collisionRayTrace(world, i, j, k, startRay, endRay);
      }
   }

   @Override
   public boolean canRotateOnTurntable(IBlockAccess blockAccess, int i, int j, int k) {
      return true;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public int rotateMetadataAroundJAxis(int iMetadata, boolean bReverse) {
      if (iMetadata == 5) {
         iMetadata = 6;
      } else if (iMetadata == 6) {
         iMetadata = 5;
      } else if (iMetadata == 7) {
         iMetadata = 8;
      } else if (iMetadata == 8) {
         iMetadata = 7;
      } else if (iMetadata == 10) {
         iMetadata = 11;
      } else if (iMetadata == 11) {
         iMetadata = 10;
      } else if (iMetadata == 13) {
         iMetadata = 14;
      } else if (iMetadata == 14) {
         iMetadata = 13;
      }

      return iMetadata;
   }

   @Override
   public boolean canBePistonShoveled(World world, int i, int j, int k) {
      return true;
   }

   @Override
   public boolean canGroundCoverRestOnBlock(World world, int i, int j, int k) {
      int iSubtype = world.getBlockMetadata(i, j, k);
      return iSubtype != 2 && iSubtype != 3 ? super.canGroundCoverRestOnBlock(world, i, j, k) : world.doesBlockHaveSolidTopSurface(i, j - 1, k);
   }

   @Override
   public float groundCoverRestingOnVisualOffset(IBlockAccess blockAccess, int i, int j, int k) {
      int iSubtype = blockAccess.getBlockMetadata(i, j, k);
      return iSubtype != 2 && iSubtype != 3 ? super.groundCoverRestingOnVisualOffset(blockAccess, i, j, k) : -1.0F;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean shouldSideBeRendered(IBlockAccess blockAccess, int iNeighborI, int iNeighborJ, int iNeighborK, int iSide) {
      return this.currentBlockRenderer.shouldSideBeRenderedBasedOnCurrentBounds(iNeighborI, iNeighborJ, iNeighborK, iSide);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void registerIcons(IconRegister register) {
      super.registerIcons(register);
      this.iconNetherSludge = register.registerIcon("fcBlockNetherSludge");
      this.iconUncookedPastry = register.registerIcon("fcBlockPastryUncooked");
      this.iconUncookedPumpkinPieTop = register.registerIcon("fcBlockUncookedPumpkinPie_top");
   }

   @Environment(EnvType.CLIENT)
   @Override
   public Icon getIcon(int iSide, int iMetadata) {
      if (iMetadata == 7 || iMetadata == 8) {
         return this.iconNetherSludge;
      } else if (iMetadata < 9) {
         return this.blockIcon;
      } else {
         return iSide == 1 && iMetadata == 12 ? this.iconUncookedPumpkinPieTop : this.iconUncookedPastry;
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void getSubBlocks(int iBlockID, CreativeTabs creativeTabs, List list) {
      list.add(new ItemStack(iBlockID, 1, 0));
      list.add(new ItemStack(iBlockID, 1, 1));
      list.add(new ItemStack(iBlockID, 1, 2));
      list.add(new ItemStack(iBlockID, 1, 3));
   }

   @Environment(EnvType.CLIENT)
   @Override
   public int idPicked(World world, int x, int y, int z) {
      int metadata = world.getBlockMetadata(x, y, z);
      return metadata != 7 && metadata != 8 ? this.idDropped(metadata, world.rand, 0) : BTWItems.unfiredNetherBrick.itemID;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean renderBlock(RenderBlocks renderBlocks, int i, int j, int k) {
      IBlockAccess blockAccess = renderBlocks.blockAccess;
      int iMetaData = blockAccess.getBlockMetadata(i, j, k);
      switch (iMetaData) {
         case 0:
            Icon crucibleTexture = this.b_(blockAccess, i, j, k, 0);
            renderUnfiredCrucible(renderBlocks, blockAccess, i, j, k, this, crucibleTexture);
            break;
         case 1:
            PlanterBlock.renderEmptyPlanterBlock(renderBlocks, blockAccess, i, j, k, this);
            break;
         case 2:
            VaseBlock.renderVaseBlock(renderBlocks, blockAccess, i, j, k, this);
            break;
         case 3:
            Icon urnTexture = this.b_(blockAccess, i, j, k, 0);
            renderUnfiredUrn(renderBlocks, blockAccess, i, j, k, this, urnTexture, 0.0F);
            break;
         case 4:
         case 5:
         case 6:
         case 7:
         case 8:
         case 9:
         default:
            super.renderBlock(renderBlocks, i, j, k);
            break;
         case 10:
         case 11:
            renderUncookedCookies(renderBlocks, blockAccess, i, j, k, this, iMetaData == 11);
      }

      return true;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void renderBlockSecondPass(RenderBlocks renderBlocks, int i, int j, int k, boolean bFirstPassResult) {
      this.renderCookingByKiLnOverlay(renderBlocks, i, j, k, bFirstPassResult);
   }

   @Environment(EnvType.CLIENT)
   public static boolean renderUnfiredUrn(
      RenderBlocks renderBlocks, IBlockAccess blockAccess, int i, int j, int k, Block block, Icon texture, float fVerticalOffset
   ) {
      renderBlocks.setRenderBounds(0.375, 0.0F + fVerticalOffset, 0.375, 0.625, 0.0625F + fVerticalOffset, 0.625);
      RenderUtils.renderStandardBlockWithTexture(renderBlocks, block, i, j, k, texture);
      renderBlocks.setRenderBounds(0.3125, 0.0625F + fVerticalOffset, 0.3125, 0.6875, 0.4375F + fVerticalOffset, 0.6875);
      RenderUtils.renderStandardBlockWithTexture(renderBlocks, block, i, j, k, texture);
      renderBlocks.setRenderBounds(0.375, 0.4375F + fVerticalOffset, 0.375, 0.625, 0.5F + fVerticalOffset, 0.625);
      RenderUtils.renderStandardBlockWithTexture(renderBlocks, block, i, j, k, texture);
      renderBlocks.setRenderBounds(0.3125, 0.5F + fVerticalOffset, 0.3125, 0.6875, 0.5625F + fVerticalOffset, 0.6875);
      RenderUtils.renderStandardBlockWithTexture(renderBlocks, block, i, j, k, texture);
      renderBlocks.setRenderBounds(0.375, 0.5625F + fVerticalOffset, 0.375, 0.625, 0.625F + fVerticalOffset, 0.625);
      RenderUtils.renderStandardBlockWithTexture(renderBlocks, block, i, j, k, texture);
      return true;
   }

   @Environment(EnvType.CLIENT)
   public static boolean renderUnfiredCrucible(RenderBlocks renderBlocks, IBlockAccess blockAccess, int i, int j, int k, Block block, Icon texture) {
      renderBlocks.setRenderBounds(0.0625, 0.0, 0.0625, 0.1875, 1.0, 0.8125);
      RenderUtils.renderStandardBlockWithTexture(renderBlocks, block, i, j, k, texture);
      renderBlocks.setRenderBounds(0.0625, 0.0, 0.8125, 0.8125, 1.0, 0.9375);
      RenderUtils.renderStandardBlockWithTexture(renderBlocks, block, i, j, k, texture);
      renderBlocks.setRenderBounds(0.8125, 0.0, 0.1875, 0.9375, 1.0, 0.9375);
      RenderUtils.renderStandardBlockWithTexture(renderBlocks, block, i, j, k, texture);
      renderBlocks.setRenderBounds(0.1875, 0.0, 0.0625, 0.9375, 1.0, 0.1875);
      RenderUtils.renderStandardBlockWithTexture(renderBlocks, block, i, j, k, texture);
      renderBlocks.setRenderBounds(0.1875, 0.0, 0.1875, 0.8125, 0.125, 0.8125);
      RenderUtils.renderStandardBlockWithTexture(renderBlocks, block, i, j, k, texture);
      renderBlocks.setRenderBounds(0.0, 0.125, 0.0, 0.125, 0.875, 0.875);
      RenderUtils.renderStandardBlockWithTexture(renderBlocks, block, i, j, k, texture);
      renderBlocks.setRenderBounds(0.0, 0.125, 0.875, 0.875, 0.875, 1.0);
      RenderUtils.renderStandardBlockWithTexture(renderBlocks, block, i, j, k, texture);
      renderBlocks.setRenderBounds(0.875, 0.125, 0.125, 1.0, 0.875, 1.0);
      RenderUtils.renderStandardBlockWithTexture(renderBlocks, block, i, j, k, texture);
      renderBlocks.setRenderBounds(0.125, 0.125, 0.0, 1.0, 0.875, 0.125);
      RenderUtils.renderStandardBlockWithTexture(renderBlocks, block, i, j, k, texture);
      return true;
   }

   @Environment(EnvType.CLIENT)
   public static boolean renderUncookedCookies(RenderBlocks renderBlocks, IBlockAccess blockAccess, int i, int j, int k, Block block, boolean bIAligned) {
      int iCookiesAlongI;
      int iCookiesAlongK;
      float fStartCenterX;
      float fStartCenterZ;
      if (!bIAligned) {
         iCookiesAlongI = 2;
         iCookiesAlongK = 4;
         fStartCenterX = 0.375F;
         fStartCenterZ = 0.125F;
      } else {
         iCookiesAlongI = 4;
         iCookiesAlongK = 2;
         fStartCenterX = 0.125F;
         fStartCenterZ = 0.375F;
      }

      for (int iCountAlongI = 0; iCountAlongI < iCookiesAlongI; iCountAlongI++) {
         for (int iCountAlongK = 0; iCountAlongK < iCookiesAlongK; iCountAlongK++) {
            float fCenterCookieX = fStartCenterX + iCountAlongI * 0.125F * 2.0F;
            float fCenterCookieZ = fStartCenterZ + iCountAlongK * 0.125F * 2.0F;
            renderBlocks.setRenderBounds(fCenterCookieX - 0.0625F, 0.0, fCenterCookieZ - 0.0625F, fCenterCookieX + 0.0625F, 0.0625, fCenterCookieZ + 0.0625F);
            renderBlocks.renderStandardBlock(block, i, j, k);
         }
      }

      return true;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void renderBlockAsItem(RenderBlocks renderBlocks, int iItemDamage, float fBrightness) {
      switch (iItemDamage) {
         case 0:
            renderUnfiredCrucibleInvBlock(renderBlocks, this, iItemDamage, this.blockIcon);
            break;
         case 1:
            PlanterBlock.renderEmptyPlanterInvBlock(renderBlocks, this, iItemDamage);
            break;
         case 2:
            VaseBlock.renderInvBlock(renderBlocks, this, iItemDamage);
            break;
         case 3:
            renderUnfiredUrnInvBlock(renderBlocks, this, iItemDamage, this.blockIcon);
            break;
         case 4:
            renderBlocks.setRenderBounds(0.3125, 0.0, 0.3125, 0.6875, 0.125, 0.6875);
         default:
            RenderUtils.renderInvBlockWithMetadata(renderBlocks, this, -0.5F, -0.5F, -0.5F, iItemDamage);
      }
   }

   @Environment(EnvType.CLIENT)
   public static void renderUnfiredUrnInvBlock(RenderBlocks renderBlocks, Block block, int iItemDamage, Icon texture) {
      renderBlocks.setRenderBounds(0.375, 0.0, 0.375, 0.625, 0.0625, 0.625);
      RenderUtils.renderInvBlockWithTexture(renderBlocks, block, -0.5F, -0.5F, -0.5F, texture);
      renderBlocks.setRenderBounds(0.3125, 0.0625, 0.3125, 0.6875, 0.4375, 0.6875);
      RenderUtils.renderInvBlockWithTexture(renderBlocks, block, -0.5F, -0.5F, -0.5F, texture);
      renderBlocks.setRenderBounds(0.375, 0.4375, 0.375, 0.625, 0.5, 0.625);
      RenderUtils.renderInvBlockWithTexture(renderBlocks, block, -0.5F, -0.5F, -0.5F, texture);
      renderBlocks.setRenderBounds(0.3125, 0.5, 0.3125, 0.6875, 0.5625, 0.6875);
      RenderUtils.renderInvBlockWithTexture(renderBlocks, block, -0.5F, -0.5F, -0.5F, texture);
      renderBlocks.setRenderBounds(0.375, 0.5625, 0.375, 0.625, 0.625, 0.625);
      RenderUtils.renderInvBlockWithTexture(renderBlocks, block, -0.5F, -0.5F, -0.5F, texture);
   }

   @Environment(EnvType.CLIENT)
   public static void renderUnfiredCrucibleInvBlock(RenderBlocks renderBlocks, Block block, int iItemDamage, Icon texture) {
      renderBlocks.setRenderBounds(0.0625, 0.0, 0.0625, 0.1875, 1.0, 0.8125);
      RenderUtils.renderInvBlockWithMetadata(renderBlocks, block, -0.5F, -0.5F, -0.5F, 0);
      renderBlocks.setRenderBounds(0.0625, 0.0, 0.8125, 0.8125, 1.0, 0.9375);
      RenderUtils.renderInvBlockWithMetadata(renderBlocks, block, -0.5F, -0.5F, -0.5F, 0);
      renderBlocks.setRenderBounds(0.8125, 0.0, 0.1875, 0.9375, 1.0, 0.9375);
      RenderUtils.renderInvBlockWithMetadata(renderBlocks, block, -0.5F, -0.5F, -0.5F, 0);
      renderBlocks.setRenderBounds(0.1875, 0.0, 0.0625, 0.9375, 1.0, 0.1875);
      RenderUtils.renderInvBlockWithMetadata(renderBlocks, block, -0.5F, -0.5F, -0.5F, 0);
      renderBlocks.setRenderBounds(0.1875, 0.0, 0.1875, 0.8125, 0.125, 0.8125);
      RenderUtils.renderInvBlockWithMetadata(renderBlocks, block, -0.5F, -0.5F, -0.5F, 0);
      renderBlocks.setRenderBounds(0.0, 0.125, 0.0, 0.125, 0.875, 0.875);
      RenderUtils.renderInvBlockWithMetadata(renderBlocks, block, -0.5F, -0.5F, -0.5F, 0);
      renderBlocks.setRenderBounds(0.0, 0.125, 0.875, 0.875, 0.875, 1.0);
      RenderUtils.renderInvBlockWithMetadata(renderBlocks, block, -0.5F, -0.5F, -0.5F, 0);
      renderBlocks.setRenderBounds(0.875, 0.125, 0.125, 1.0, 0.875, 1.0);
      RenderUtils.renderInvBlockWithMetadata(renderBlocks, block, -0.5F, -0.5F, -0.5F, 0);
      renderBlocks.setRenderBounds(0.125, 0.125, 0.0, 1.0, 0.875, 0.125);
      RenderUtils.renderInvBlockWithMetadata(renderBlocks, block, -0.5F, -0.5F, -0.5F, 0);
   }
}
