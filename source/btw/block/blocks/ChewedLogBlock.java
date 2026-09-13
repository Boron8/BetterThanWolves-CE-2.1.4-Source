package btw.block.blocks;

import btw.block.BTWBlocks;
import btw.block.model.BlockModel;
import btw.item.BTWItems;
import btw.item.items.AxeItem;
import btw.item.items.ChiselItem;
import btw.item.util.ItemUtils;
import btw.world.util.BlockPos;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Explosion;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Icon;
import net.minecraft.src.IconRegister;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.MovingObjectPosition;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.Vec3;
import net.minecraft.src.World;

public class ChewedLogBlock extends Block {
   public static final float HARDNESS = 2.0F;
   private BlockModel[] blockModels;
   private BlockModel[] blockModelsNarrowOneSide;
   private BlockModel[] blockModelsNarrowTwoSides;
   private AxisAlignedBB[] boxSelectionArray;
   protected String sideTexture;
   protected String topTexture;
   protected String stumpTopTexture;
   Block logSpike;
   private static final float RIM_WIDTH = 0.0625F;
   private static final float LAYER_HEIGHT = 0.125F;
   private static final float FIRST_LAYER_HEIGHT = 0.1875F;
   private static final float LAYER_WIDTH_GAP = 0.0625F;
   private boolean tempPosNarrow = false;
   private boolean tempNegNarrow = false;
   private BlockModel currentModel;
   @Environment(EnvType.CLIENT)
   private Icon iconSide;
   @Environment(EnvType.CLIENT)
   private Icon iconStumpTop;

   public ChewedLogBlock(int blockID, String sideTexture, String topTexture, String stumpTopTexture, Block logSpike) {
      super(blockID, BTWBlocks.logMaterial);
      this.c(2.0F);
      this.setAxesEffectiveOn();
      this.setChiselsEffectiveOn();
      this.setBuoyant();
      this.setFireProperties(5, 5);
      this.initBlockBounds(0.0, 0.0, 0.0, 1.0, 1.0, 1.0);
      this.initModels();
      Block.useNeighborBrightness[blockID] = true;
      this.k(8);
      this.a(g);
      this.sideTexture = sideTexture;
      this.topTexture = topTexture;
      this.stumpTopTexture = stumpTopTexture;
      this.logSpike = logSpike;
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
   public float getBlockHardness(World world, int i, int j, int k) {
      float fHardness = super.getBlockHardness(world, i, j, k);
      int iMetadata = world.getBlockMetadata(i, j, k);
      if (getIsStump(world, i, j, k)) {
         fHardness *= 3.0F;
      }

      return fHardness;
   }

   @Override
   public int onBlockPlaced(World world, int i, int j, int k, int iFacing, float fClickX, float fClickY, float fClickZ, int iMetadata) {
      if (iFacing <= 1) {
         iMetadata = this.setOrientation(iMetadata, 0);
      } else if (iFacing <= 3) {
         iMetadata = this.setOrientation(iMetadata, 2);
      } else {
         iMetadata = this.setOrientation(iMetadata, 1);
      }

      return iMetadata;
   }

   @Override
   public void onNeighborBlockChange(World world, int i, int j, int k, int iBlockID) {
      int iMetadata = world.getBlockMetadata(i, j, k);
      this.checkForReplaceWithSpike(world, i, j, k, iMetadata);
   }

   @Override
   public MovingObjectPosition collisionRayTrace(World world, int i, int j, int k, Vec3 startRay, Vec3 endRay) {
      int iFacing = this.setCurrentModelForBlock(world, i, j, k);
      BlockModel transformedModel = this.currentModel.makeTemporaryCopy();
      transformedModel.tiltToFacingAlongY(iFacing);
      return transformedModel.collisionRayTrace(world, i, j, k, startRay, endRay);
   }

   @Override
   public boolean hasCenterHardPointToFacing(IBlockAccess blockAccess, int i, int j, int k, int iFacing, boolean bIgnoreTransparency) {
      int iOrientation = this.getOrientation(blockAccess, i, j, k);
      if (iOrientation == 0) {
         return iFacing <= 1;
      } else {
         return iOrientation == 1 ? iFacing >= 4 : iFacing == 2 || iFacing == 3;
      }
   }

   @Override
   public void onBlockDestroyedWithImproperTool(World world, EntityPlayer player, int i, int j, int k, int iMetadata) {
      boolean bIsStump = getIsStump(iMetadata);
      if (bIsStump) {
         this.b(world, i, j, k, new ItemStack(BTWItems.sawDust));
      }

      this.b(world, i, j, k, new ItemStack(BTWItems.sawDust));
   }

   @Override
   public boolean canConvertBlock(ItemStack stack, World world, int i, int j, int k) {
      return true;
   }

   @Override
   public boolean convertBlock(ItemStack stack, World world, int i, int j, int k, int iFromSide) {
      int iOldMetadata = world.getBlockMetadata(i, j, k);
      int iDamageLevel = this.getDamageLevel(iOldMetadata);
      if (iDamageLevel >= 3) {
         if (!world.isRemote) {
            if (getIsStump(iOldMetadata)) {
               ItemUtils.dropStackAsIfBlockHarvested(world, i, j, k, new ItemStack(BTWItems.sawDust, 1));
            }

            ItemUtils.dropStackAsIfBlockHarvested(world, i, j, k, new ItemStack(BTWItems.sawDust, 1));
         }

         return false;
      } else {
         this.setDamageLevel(world, i, j, k, ++iDamageLevel);
         if (!world.isRemote) {
            if (getIsStump(iOldMetadata)) {
               ItemUtils.ejectStackFromBlockTowardsFacing(world, i, j, k, new ItemStack(BTWItems.sawDust, 1), iFromSide);
               ItemUtils.ejectStackFromBlockTowardsFacing(world, i, j, k, new ItemStack(BTWItems.sawDust, 1), iFromSide);
            } else if (iDamageLevel != 1 && iDamageLevel != 3) {
               ItemUtils.ejectStackFromBlockTowardsFacing(world, i, j, k, new ItemStack(BTWItems.sawDust, 1), iFromSide);
            } else {
               world.playAuxSFX(2268, i, j, k, 0);
               ItemUtils.ejectStackFromBlockTowardsFacing(world, i, j, k, new ItemStack(Item.stick, 1), iFromSide);
            }
         }

         return true;
      }
   }

   @Override
   public boolean getIsProblemToRemove(ItemStack toolStack, IBlockAccess blockAccess, int i, int j, int k) {
      return getIsStump(blockAccess, i, j, k);
   }

   @Override
   public boolean getDoesStumpRemoverWorkOnBlock(IBlockAccess blockAccess, int i, int j, int k) {
      return getIsStump(blockAccess, i, j, k);
   }

   @Override
   public boolean canDropFromExplosion(Explosion explosion) {
      return false;
   }

   @Override
   public void onBlockDestroyedByExplosion(World world, int i, int j, int k, Explosion explosion) {
      float fChanceOfPileDrop = 1.0F;
      if (explosion != null) {
         fChanceOfPileDrop = 1.0F / explosion.explosionSize;
      }

      this.dropItemsIndividually(world, i, j, k, BTWItems.sawDust.itemID, 1, 0, fChanceOfPileDrop);
   }

   @Override
   public boolean getCanBlockBeIncinerated(World world, int i, int j, int k) {
      return !getIsStump(world, i, j, k);
   }

   @Override
   public int getHarvestToolLevel(IBlockAccess blockAccess, int i, int j, int k) {
      return 1000;
   }

   @Override
   public void onDestroyedByFire(World world, int i, int j, int k, int iFireAge, boolean bForcedFireSpread) {
      if (getIsStump(world, i, j, k)) {
         int iNewMetadata = BTWBlocks.charredStump.setDamageLevel(0, this.getDamageLevel(world, i, j, k));
         world.setBlockAndMetadataWithNotify(i, j, k, BTWBlocks.charredStump.blockID, iNewMetadata);
      } else {
         super.onDestroyedByFire(world, i, j, k, iFireAge, bForcedFireSpread);
      }
   }

   protected void initModels() {
      this.blockModels = new BlockModel[4];
      this.blockModelsNarrowOneSide = new BlockModel[4];
      this.blockModelsNarrowTwoSides = new BlockModel[4];
      this.boxSelectionArray = new AxisAlignedBB[4];

      for (int iTempIndex = 0; iTempIndex < 4; iTempIndex++) {
         BlockModel tempModel = this.blockModels[iTempIndex] = new BlockModel();
         BlockModel tempNarrowOneSide = this.blockModelsNarrowOneSide[iTempIndex] = new BlockModel();
         BlockModel tempNarrowTwoSides = this.blockModelsNarrowTwoSides[iTempIndex] = new BlockModel();
         float fCenterColumnWidthGap = 0.0625F + 0.0625F * iTempIndex;
         float fCenterColumnHeightGap = 0.0F;
         if (iTempIndex > 0) {
            fCenterColumnHeightGap = 0.1875F + 0.125F * (iTempIndex - 1);
         }

         tempModel.addBox(
            fCenterColumnWidthGap,
            fCenterColumnHeightGap,
            fCenterColumnWidthGap,
            1.0F - fCenterColumnWidthGap,
            1.0F - fCenterColumnHeightGap,
            1.0F - fCenterColumnWidthGap
         );
         tempNarrowOneSide.addBox(
            fCenterColumnWidthGap, fCenterColumnHeightGap, fCenterColumnWidthGap, 1.0F - fCenterColumnWidthGap, 1.0, 1.0F - fCenterColumnWidthGap
         );
         tempNarrowTwoSides.addBox(fCenterColumnWidthGap, 0.0, fCenterColumnWidthGap, 1.0F - fCenterColumnWidthGap, 1.0, 1.0F - fCenterColumnWidthGap);
         AxisAlignedBB var7 = this.boxSelectionArray[iTempIndex] = new AxisAlignedBB(
            fCenterColumnWidthGap, 0.0, fCenterColumnWidthGap, 1.0F - fCenterColumnWidthGap, 1.0, 1.0F - fCenterColumnWidthGap
         );
      }

      for (int iTempIndex = 1; iTempIndex < 4; iTempIndex++) {
         this.blockModels[iTempIndex].addBox(0.0625, 0.0, 0.0625, 0.9375, 0.1875, 0.9375);
         this.blockModelsNarrowOneSide[iTempIndex].addBox(0.0625, 0.0, 0.0625, 0.9375, 0.1875, 0.9375);
         this.blockModels[iTempIndex].addBox(0.0625, 0.8125, 0.0625, 0.9375, 1.0, 0.9375);
      }

      float fWidthGap = 0.125F;
      float fHeightGap = 0.1875F;

      for (int iTempIndex = 2; iTempIndex < 4; iTempIndex++) {
         this.blockModels[iTempIndex].addBox(fWidthGap, fHeightGap, fWidthGap, 1.0F - fWidthGap, fHeightGap + 0.125F, 1.0F - fWidthGap);
         this.blockModelsNarrowOneSide[iTempIndex].addBox(fWidthGap, fHeightGap, fWidthGap, 1.0F - fWidthGap, fHeightGap + 0.125F, 1.0F - fWidthGap);
         this.blockModels[iTempIndex].addBox(fWidthGap, 1.0F - fHeightGap - 0.125F, fWidthGap, 1.0F - fWidthGap, 1.0F - fHeightGap, 1.0F - fWidthGap);
      }

      fWidthGap = 0.1875F;
      fHeightGap = 0.3125F;
      this.blockModels[3].addBox(fWidthGap, fHeightGap, fWidthGap, 1.0F - fWidthGap, fHeightGap + 0.125F, 1.0F - fWidthGap);
      this.blockModelsNarrowOneSide[3].addBox(fWidthGap, fHeightGap, fWidthGap, 1.0F - fWidthGap, fHeightGap + 0.125F, 1.0F - fWidthGap);
      this.blockModels[3].addBox(fWidthGap, 1.0F - fHeightGap - 0.125F, fWidthGap, 1.0F - fWidthGap, 0.875, 1.0F - fWidthGap);
   }

   public void setDamageLevel(World world, int i, int j, int k, int iDamageLevel) {
      int iMetadata = world.getBlockMetadata(i, j, k) & -4;
      iMetadata |= iDamageLevel;
      if (!this.checkForReplaceWithSpike(world, i, j, k, iMetadata)) {
         world.setBlockMetadataWithNotify(i, j, k, iMetadata);
      }
   }

   private boolean checkForReplaceWithSpike(World world, int i, int j, int k, int iMetadata) {
      if (this.getDamageLevel(iMetadata) == 3 && !getIsStump(iMetadata)) {
         int iFacing = this.setConnectionFlagsForBlock(world, i, j, k, iMetadata);
         if (this.tempPosNarrow != this.tempNegNarrow) {
            BlockPos targetPos = new BlockPos(i, j, k);
            targetPos.addFacingAsOffset(iFacing);
            int iTargetBlockID = world.getBlockId(targetPos.x, targetPos.y, targetPos.z);
            Block targetBlock = Block.blocksList[iTargetBlockID];
            if (iTargetBlockID != this.blockID || this.getOrientation(iMetadata) != this.getOrientation(world, targetPos.x, targetPos.y, targetPos.z)) {
               world.setBlockAndMetadataWithNotify(i, j, k, this.logSpike.blockID, iFacing);
               return true;
            }
         }
      }

      return false;
   }

   public int getDamageLevel(IBlockAccess blockAccess, int i, int j, int k) {
      return this.getDamageLevel(blockAccess.getBlockMetadata(i, j, k));
   }

   public int getDamageLevel(int iMetadata) {
      return iMetadata & 3;
   }

   public int getOrientation(IBlockAccess blockAccess, int i, int j, int k) {
      return this.getOrientation(blockAccess.getBlockMetadata(i, j, k));
   }

   public int getOrientation(int iMetadata) {
      int iOrientation = iMetadata >> 2 & 3;
      if (iOrientation == 3) {
         iOrientation = 0;
      }

      return iOrientation;
   }

   public int setOrientation(int iMetadata, int iOrientation) {
      if (!getIsStump(iMetadata)) {
         iMetadata |= iOrientation << 2;
      }

      return iMetadata;
   }

   public static boolean getIsStump(IBlockAccess blockAccess, int i, int j, int k) {
      return getIsStump(blockAccess.getBlockMetadata(i, j, k));
   }

   public static boolean getIsStump(int iMetadata) {
      return (iMetadata & 12) == 12;
   }

   public int setIsStump(int iMetadata) {
      return iMetadata | 12;
   }

   public int setCurrentModelForBlock(IBlockAccess blockAccess, int i, int j, int k) {
      int iFacing = this.setConnectionFlagsForBlock(blockAccess, i, j, k);
      int iDamageLevel = this.getDamageLevel(blockAccess, i, j, k);
      if (this.tempPosNarrow) {
         if (this.tempNegNarrow) {
            this.currentModel = this.blockModelsNarrowTwoSides[iDamageLevel];
         } else {
            this.currentModel = this.blockModelsNarrowOneSide[iDamageLevel];
         }
      } else {
         this.currentModel = this.blockModels[iDamageLevel];
      }

      return iFacing;
   }

   public int setConnectionFlagsForBlock(IBlockAccess blockAccess, int i, int j, int k) {
      return this.setConnectionFlagsForBlock(blockAccess, i, j, k, blockAccess.getBlockMetadata(i, j, k));
   }

   public int setConnectionFlagsForBlock(IBlockAccess blockAccess, int i, int j, int k, int iMetadata) {
      int iOrientation = this.getOrientation(iMetadata);
      int iFacing = 1;
      if (iOrientation == 1) {
         iFacing = 5;
      } else if (iOrientation == 2) {
         iFacing = 3;
      }

      this.tempPosNarrow = true;
      this.tempNegNarrow = true;
      BlockPos targetPos = new BlockPos(i, j, k);
      targetPos.addFacingAsOffset(iFacing);
      int iTargetBlockID = blockAccess.getBlockId(targetPos.x, targetPos.y, targetPos.z);
      Block targetBlock = Block.blocksList[iTargetBlockID];
      if (this.doesTargetBlockConnectToFacing(iOrientation, blockAccess, targetPos.x, targetPos.y, targetPos.z, Block.getOppositeFacing(iFacing))) {
         this.tempPosNarrow = false;
      }

      targetPos.set(i, j, k);
      targetPos.addFacingAsOffset(Block.getOppositeFacing(iFacing));
      iTargetBlockID = blockAccess.getBlockId(targetPos.x, targetPos.y, targetPos.z);
      targetBlock = Block.blocksList[iTargetBlockID];
      if (getIsStump(iMetadata) || this.doesTargetBlockConnectToFacing(iOrientation, blockAccess, targetPos.x, targetPos.y, targetPos.z, iFacing)) {
         this.tempNegNarrow = false;
      }

      if (!this.tempPosNarrow && this.tempNegNarrow) {
         iFacing = Block.getOppositeFacing(iFacing);
         this.tempPosNarrow = true;
         this.tempNegNarrow = false;
      }

      return iFacing;
   }

   public boolean doesTargetBlockConnectToFacing(int iMyOrientation, IBlockAccess blockAccess, int i, int j, int k, int iFacing) {
      int targetBlockID = blockAccess.getBlockId(i, j, k);
      Block targetBlock = Block.blocksList[targetBlockID];
      if (targetBlock == null) {
         return false;
      } else if (!(targetBlock instanceof ChewedLogBlock)) {
         return targetBlock instanceof LogSpikeBlock
            ? BTWBlocks.oakLogSpike.getFacing(blockAccess, i, j, k) == Block.getOppositeFacing(iFacing)
            : targetBlock.isLog(blockAccess, i, j, k);
      } else {
         return this.getDamageLevel(blockAccess, i, j, k) == 0 && iMyOrientation == this.getOrientation(blockAccess, i, j, k);
      }
   }

   public boolean isItemEffectiveConversionTool(ItemStack stack, World world, int i, int j, int k) {
      if (stack != null) {
         Item item = stack.getItem();
         if (item instanceof ChiselItem || item instanceof AxeItem || item == BTWItems.battleaxe) {
            return true;
         }
      }

      return false;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void registerIcons(IconRegister register) {
      this.blockIcon = register.registerIcon(this.sideTexture);
      this.iconSide = register.registerIcon(this.topTexture);
      this.iconStumpTop = register.registerIcon(this.stumpTopTexture);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public Icon getIcon(int iSide, int iMetadata) {
      int iOrientation = this.getOrientation(iMetadata);
      Icon icon = this.blockIcon;
      if (iOrientation == 0) {
         if (iSide >= 2) {
            icon = this.iconSide;
         } else if (iSide == 1 && getIsStump(iMetadata)) {
            icon = this.iconStumpTop;
         }
      } else if (iOrientation == 1) {
         if (iSide != 4 && iSide != 5) {
            icon = this.iconSide;
         }
      } else if (iSide != 2 && iSide != 3) {
         icon = this.iconSide;
      }

      return icon;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean shouldSideBeRendered(IBlockAccess blockAccess, int iNeighborI, int iNeighborJ, int iNeighborK, int iSide) {
      return this.currentBlockRenderer.shouldSideBeRenderedBasedOnCurrentBounds(iNeighborI, iNeighborJ, iNeighborK, iSide);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean renderBlock(RenderBlocks renderBlocks, int x, int y, int z) {
      int facing = this.setCurrentModelForBlock(renderBlocks.blockAccess, x, y, z);
      BlockModel transformedModel = this.currentModel.makeTemporaryCopy();
      transformedModel.tiltToFacingAlongY(facing);
      int metadata = renderBlocks.blockAccess.getBlockMetadata(x, y, z);
      if (metadata == 4 || metadata == 5 || metadata == 6 || metadata == 7) {
         renderBlocks.setUVRotateTop(1);
         renderBlocks.setUVRotateBottom(1);
         renderBlocks.setUVRotateWest(1);
         renderBlocks.setUVRotateEast(1);
      } else if (metadata == 8 || metadata == 9 || metadata == 10 || metadata == 11) {
         renderBlocks.setUVRotateNorth(1);
         renderBlocks.setUVRotateSouth(1);
      }

      transformedModel.renderAsBlock(renderBlocks, this, x, y, z);
      renderBlocks.clearUVRotation();
      return true;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void renderBlockAsItem(RenderBlocks renderBlocks, int iItemDamage, float fBrightness) {
      this.blockModelsNarrowOneSide[iItemDamage].renderAsItemBlock(renderBlocks, this, iItemDamage);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void getSubBlocks(int iBlockID, CreativeTabs creativeTabs, List list) {
      for (int iTempIndex = 0; iTempIndex < 4; iTempIndex++) {
         list.add(new ItemStack(iBlockID, 1, iTempIndex));
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int i, int j, int k) {
      int iFacing = this.setConnectionFlagsForBlock(world, i, j, k);
      int iDamageLevel = this.getDamageLevel(world, i, j, k);
      AxisAlignedBB tempSelectionBox = this.boxSelectionArray[iDamageLevel].makeTemporaryCopy();
      tempSelectionBox.tiltToFacingAlongY(iFacing);
      return tempSelectionBox.offset(i, j, k);
   }
}
