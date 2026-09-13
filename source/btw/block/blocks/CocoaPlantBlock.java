package btw.block.blocks;

import btw.item.BTWItems;
import java.util.Random;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.BlockCocoa;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.ItemStack;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.World;

public class CocoaPlantBlock extends BlockCocoa {
   public CocoaPlantBlock(int iBlockID) {
      super(iBlockID);
      this.setAxesEffectiveOn(true);
   }

   @Override
   public void updateTick(World world, int i, int j, int k, Random rand) {
      if (!this.f(world, i, j, k)) {
         this.c(world, i, j, k, world.getBlockMetadata(i, j, k), 0);
         world.setBlockWithNotify(i, j, k, 0);
      } else if (world.provider.dimensionId != 1 && world.rand.nextInt(20) == 0) {
         int iMetadata = world.getBlockMetadata(i, j, k);
         int iGrowthLevel = c(iMetadata);
         if (iGrowthLevel < 2) {
            world.setBlockMetadataWithNotify(i, j, k, ++iGrowthLevel << 2 | j(iMetadata));
         }
      }
   }

   @Override
   public void dropBlockAsItemWithChance(World world, int i, int j, int k, int iMetadata, float fChance, int iFortuneModifier) {
      int iGrowthState = c(iMetadata);
      int iNumDropped = 0;
      if (iGrowthState >= 2) {
         iNumDropped = 1;
         if (world.rand.nextInt(4) - iFortuneModifier <= 0) {
            iNumDropped = 2;
         }
      }

      for (int iTempCount = 0; iTempCount < iNumDropped; iTempCount++) {
         this.b(world, i, j, k, new ItemStack(BTWItems.cocoaBeans, 1, 0));
      }
   }

   @Override
   public int getDamageValue(World par1World, int par2, int par3, int par4) {
      return 0;
   }

   @Override
   public ItemStack getStackRetrievedByBlockDispenser(World world, int i, int j, int k) {
      return new ItemStack(BTWItems.cocoaBeans.itemID, 1, 0);
   }

   @Override
   public void setBlockBoundsBasedOnState(IBlockAccess blockAccess, int i, int j, int k) {
   }

   @Override
   public AxisAlignedBB getBlockBoundsFromPoolBasedOnState(IBlockAccess blockAccess, int i, int j, int k) {
      int iMetadata = blockAccess.getBlockMetadata(i, j, k);
      int var6 = j(iMetadata);
      int var7 = c(iMetadata);
      int var8 = 4 + var7 * 2;
      int var9 = 5 + var7 * 2;
      float var10 = var8 / 2.0F;
      switch (var6) {
         case 0:
            return AxisAlignedBB.getAABBPool()
               .getAABB((8.0F - var10) / 16.0F, (12.0F - var9) / 16.0F, (15.0F - var8) / 16.0F, (8.0F + var10) / 16.0F, 0.75, 0.9375);
         case 1:
            return AxisAlignedBB.getAABBPool()
               .getAABB(0.0625, (12.0F - var9) / 16.0F, (8.0F - var10) / 16.0F, (1.0F + var8) / 16.0F, 0.75, (8.0F + var10) / 16.0F);
         case 2:
            return AxisAlignedBB.getAABBPool()
               .getAABB((8.0F - var10) / 16.0F, (12.0F - var9) / 16.0F, 0.0625, (8.0F + var10) / 16.0F, 0.75, (1.0F + var8) / 16.0F);
         default:
            return AxisAlignedBB.getAABBPool()
               .getAABB((15.0F - var8) / 16.0F, (12.0F - var9) / 16.0F, (8.0F - var10) / 16.0F, 0.9375, 0.75, (8.0F + var10) / 16.0F);
      }
   }

   @Override
   public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k) {
      return this.getBlockBoundsFromPoolBasedOnState(world, i, j, k).offset(i, j, k);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean renderBlock(RenderBlocks renderer, int i, int j, int k) {
      renderer.setRenderBounds(this.getBlockBoundsFromPoolBasedOnState(renderer.blockAccess, i, j, k));
      return renderer.renderBlockCocoa(this, i, j, k);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean shouldSideBeRendered(IBlockAccess blockAccess, int iNeighborI, int iNeighborJ, int iNeighborK, int iSide) {
      return this.currentBlockRenderer.shouldSideBeRenderedBasedOnCurrentBounds(iNeighborI, iNeighborJ, iNeighborK, iSide);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public int idPicked(World world, int i, int j, int k) {
      return BTWItems.cocoaBeans.itemID;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int i, int j, int k) {
      return this.getBlockBoundsFromPoolBasedOnState(world, i, j, k).offset(i, j, k);
   }
}
