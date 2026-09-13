package btw.block.blocks;

import btw.block.BTWBlocks;
import btw.block.model.BlockModel;
import java.util.Random;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.Block;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.ItemStack;
import net.minecraft.src.MovingObjectPosition;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.Vec3;
import net.minecraft.src.World;

public class CharredStumpBlock extends Block {
   public static final float HARDNESS = 3.0F;
   private BlockModel[] blockModelsNarrowOneSide;
   private static final float RIM_WIDTH = 0.0625F;
   private static final float LAYER_HEIGHT = 0.125F;
   private static final float FIRST_LAYER_HEIGHT = 0.1875F;
   private static final float LAYER_WIDTH_GAP = 0.0625F;
   private BlockModel tempCurrentModel;

   public CharredStumpBlock(int iBlockID) {
      super(iBlockID, BTWBlocks.logMaterial);
      this.c(3.0F);
      this.setAxesEffectiveOn();
      this.setChiselsEffectiveOn();
      this.setBuoyant();
      this.initModels();
      Block.useNeighborBrightness[iBlockID] = true;
      this.k(8);
      this.a(h);
      this.c("fcBlockStumpCharred");
   }

   @Override
   public int idDropped(int iMetadata, Random rand, int iFortuneModifier) {
      return 0;
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
   public MovingObjectPosition collisionRayTrace(World world, int i, int j, int k, Vec3 startRay, Vec3 endRay) {
      return this.getCurrentModelForBlock(world, i, j, k).collisionRayTrace(world, i, j, k, startRay, endRay);
   }

   @Override
   public boolean hasCenterHardPointToFacing(IBlockAccess blockAccess, int i, int j, int k, int iFacing, boolean bIgnoreTransparency) {
      return iFacing <= 1;
   }

   @Override
   public boolean canConvertBlock(ItemStack stack, World world, int i, int j, int k) {
      return true;
   }

   @Override
   public boolean convertBlock(ItemStack stack, World world, int i, int j, int k, int iFromSide) {
      int iOldMetadata = world.getBlockMetadata(i, j, k);
      int iDamageLevel = this.getDamageLevel(iOldMetadata);
      if (iDamageLevel < 3) {
         this.setDamageLevel(world, i, j, k, ++iDamageLevel);
         return true;
      } else {
         return false;
      }
   }

   @Override
   public boolean getIsProblemToRemove(ItemStack toolStack, IBlockAccess blockAccess, int i, int j, int k) {
      return true;
   }

   @Override
   public boolean getDoesStumpRemoverWorkOnBlock(IBlockAccess blockAccess, int i, int j, int k) {
      return true;
   }

   @Override
   public boolean getCanBlockBeIncinerated(World world, int i, int j, int k) {
      return false;
   }

   @Override
   public int getHarvestToolLevel(IBlockAccess blockAccess, int i, int j, int k) {
      return 1000;
   }

   protected void initModels() {
      this.blockModelsNarrowOneSide = new BlockModel[4];

      for (int iTempIndex = 0; iTempIndex < 4; iTempIndex++) {
         BlockModel tempNarrowOneSide = this.blockModelsNarrowOneSide[iTempIndex] = new BlockModel();
         float fCenterColumnWidthGap = 0.0625F + 0.0625F * iTempIndex;
         float fCenterColumnHeightGap = 0.0F;
         if (iTempIndex > 0) {
            fCenterColumnHeightGap = 0.1875F + 0.125F * (iTempIndex - 1);
         }

         tempNarrowOneSide.addBox(
            fCenterColumnWidthGap, fCenterColumnHeightGap, fCenterColumnWidthGap, 1.0F - fCenterColumnWidthGap, 1.0, 1.0F - fCenterColumnWidthGap
         );
      }

      for (int iTempIndex = 1; iTempIndex < 4; iTempIndex++) {
         this.blockModelsNarrowOneSide[iTempIndex].addBox(0.0625, 0.0, 0.0625, 0.9375, 0.1875, 0.9375);
      }

      float fWidthGap = 0.125F;
      float fHeightGap = 0.1875F;

      for (int iTempIndex = 2; iTempIndex < 4; iTempIndex++) {
         this.blockModelsNarrowOneSide[iTempIndex].addBox(fWidthGap, fHeightGap, fWidthGap, 1.0F - fWidthGap, fHeightGap + 0.125F, 1.0F - fWidthGap);
      }

      fWidthGap = 0.1875F;
      fHeightGap = 0.3125F;
      this.blockModelsNarrowOneSide[3].addBox(fWidthGap, fHeightGap, fWidthGap, 1.0F - fWidthGap, fHeightGap + 0.125F, 1.0F - fWidthGap);
   }

   public void setDamageLevel(World world, int i, int j, int k, int iDamageLevel) {
      int iMetadata = this.setDamageLevel(world.getBlockMetadata(i, j, k), iDamageLevel);
      world.setBlockMetadataWithNotify(i, j, k, iMetadata);
   }

   public int setDamageLevel(int iMetadata, int iDamageLevel) {
      iMetadata &= -4;
      return iMetadata | iDamageLevel;
   }

   public int getDamageLevel(IBlockAccess blockAccess, int i, int j, int k) {
      return this.getDamageLevel(blockAccess.getBlockMetadata(i, j, k));
   }

   public int getDamageLevel(int iMetadata) {
      return iMetadata & 3;
   }

   public BlockModel getCurrentModelForBlock(IBlockAccess blockAccess, int i, int j, int k) {
      int iDamageLevel = this.getDamageLevel(blockAccess, i, j, k);
      return this.blockModelsNarrowOneSide[iDamageLevel];
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean renderBlock(RenderBlocks renderBlocks, int i, int j, int k) {
      return this.getCurrentModelForBlock(renderBlocks.blockAccess, i, j, k).renderAsBlock(renderBlocks, this, i, j, k);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean shouldSideBeRendered(IBlockAccess blockAccess, int iNeighborI, int iNeighborJ, int iNeighborK, int iSide) {
      return this.currentBlockRenderer.shouldSideBeRenderedBasedOnCurrentBounds(iNeighborI, iNeighborJ, iNeighborK, iSide);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void renderBlockAsItem(RenderBlocks renderBlocks, int iItemDamage, float fBrightness) {
      this.blockModelsNarrowOneSide[iItemDamage].renderAsItemBlock(renderBlocks, this, iItemDamage);
   }
}
