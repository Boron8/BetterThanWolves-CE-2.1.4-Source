package btw.block.blocks;

import btw.block.BTWBlocks;
import btw.block.util.Flammability;
import java.util.Random;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Icon;
import net.minecraft.src.IconRegister;
import net.minecraft.src.Material;
import net.minecraft.src.MathHelper;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.Tessellator;
import net.minecraft.src.World;

public class WeedsBlock extends PlantsBlock {
   public static final double WEEDS_BOUNDS_WIDTH = 0.75;
   public static final double WEEDS_BOUNDS_HALF_WIDTH = 0.375;
   @Environment(EnvType.CLIENT)
   private Icon[] weedIconArray = null;

   public WeedsBlock(int iBlockID) {
      super(iBlockID, Material.plants);
      this.c(0.0F);
      this.setBuoyant();
      this.setFireProperties(Flammability.CROPS);
      this.initBlockBounds(-0.25, 0.0, -0.25, 1.25, 0.5, 1.25);
      this.a(i);
      this.b(true);
      this.D();
   }

   @Override
   public int idDropped(int iMetadata, Random rand, int iFortuneModifier) {
      return -1;
   }

   @Override
   public void breakBlock(World world, int i, int j, int k, int iBlockID, int iMetadata) {
   }

   @Override
   public void onNeighborBlockChange(World world, int i, int j, int k, int iNeighborBlockID) {
      super.a(world, i, j, k, iNeighborBlockID);
      if (!this.f(world, i, j, k)) {
         world.setBlockToAir(i, j, k);
      }
   }

   @Override
   public AxisAlignedBB getBlockBoundsFromPoolBasedOnState(IBlockAccess blockAccess, int i, int j, int k) {
      float dVerticalOffset = 0.0F;
      Block blockBelow = Block.blocksList[blockAccess.getBlockId(i, j - 1, k)];
      if (blockBelow != null) {
         dVerticalOffset = blockBelow.groundCoverRestingOnVisualOffset(blockAccess, i, j - 1, k);
      }

      int iGrowthLevel = this.getWeedsGrowthLevel(blockAccess, i, j, k);
      double dBoundsHeight = getWeedsBoundsHeight(iGrowthLevel);
      return AxisAlignedBB.getAABBPool().getAABB(0.125, 0.0F + dVerticalOffset, 0.125, 0.875, dBoundsHeight + dVerticalOffset, 0.875);
   }

   @Override
   public void removeWeeds(World world, int i, int j, int k) {
      Block blockBelow = Block.blocksList[world.getBlockId(i, j - 1, k)];
      if (blockBelow != null) {
         blockBelow.removeWeeds(world, i, j - 1, k);
      }

      world.setBlockToAir(i, j, k);
   }

   @Override
   public boolean canWeedsGrowInBlock(IBlockAccess blockAccess, int i, int j, int k) {
      return true;
   }

   @Override
   protected boolean canGrowOnBlock(World world, int i, int j, int k) {
      int iBlockOnID = world.getBlockId(i, j, k);
      return world.getBlockId(i, j, k) == BTWBlocks.farmland.blockID || world.getBlockId(i, j, k) == BTWBlocks.fertilizedFarmland.blockID;
   }

   public static double getWeedsBoundsHeight(int iGrowthLevel) {
      return ((iGrowthLevel >> 1) + 1) / 8.0;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void registerIcons(IconRegister register) {
      this.weedIconArray = new Icon[8];
      this.weedIconArray[0] = this.weedIconArray[1] = register.registerIcon("fcBlockWeeds_0");
      this.weedIconArray[2] = this.weedIconArray[3] = register.registerIcon("fcBlockWeeds_1");
      this.weedIconArray[4] = this.weedIconArray[5] = register.registerIcon("fcBlockWeeds_2");
      this.weedIconArray[6] = this.weedIconArray[7] = register.registerIcon("fcBlockWeeds_3");
      this.blockIcon = this.weedIconArray[7];
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean renderBlock(RenderBlocks renderer, int i, int j, int k) {
      this.renderWeeds(this, renderer, i, j, k);
      return true;
   }

   @Environment(EnvType.CLIENT)
   public void renderWeeds(Block block, RenderBlocks renderer, int i, int j, int k) {
      int iGrowthLevel = block.getWeedsGrowthLevel(renderer.blockAccess, i, j, k);
      if (iGrowthLevel > 0) {
         double dVerticalOffset = 0.0;
         Block blockBelow = Block.blocksList[renderer.blockAccess.getBlockId(i, j - 1, k)];
         if (blockBelow != null) {
            dVerticalOffset = blockBelow.groundCoverRestingOnVisualOffset(renderer.blockAccess, i, j - 1, k);
         }

         Tessellator tessellator = Tessellator.instance;
         tessellator.setBrightness(block.getMixedBrightnessForBlock(renderer.blockAccess, i, j, k));
         tessellator.setColorOpaque_F(1.0F, 1.0F, 1.0F);
         iGrowthLevel = MathHelper.clamp_int(iGrowthLevel, 0, 7);
         block.renderCrossHatch(renderer, i, j, k, this.weedIconArray[iGrowthLevel], 0.125, dVerticalOffset);
      }
   }
}
