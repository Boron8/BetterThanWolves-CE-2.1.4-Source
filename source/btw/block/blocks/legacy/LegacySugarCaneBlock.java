package btw.block.blocks.legacy;

import btw.item.BTWItems;
import java.util.Random;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.Block;
import net.minecraft.src.BlockReed;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityAnimal;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Material;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.World;

public class LegacySugarCaneBlock extends BlockReed {
   private static final double WIDTH = 0.75;
   private static final double HALF_WIDTH = 0.375;

   public LegacySugarCaneBlock(int iBlockID) {
      super(iBlockID);
      this.initBlockBounds(0.125, 0.0, 0.125, 0.875, 1.0, 0.875);
   }

   @Override
   public void updateTick(World world, int i, int j, int k, Random rand) {
      if (world.provider.dimensionId != 1 && rand.nextInt(2) == 0 && world.isAirBlock(i, j + 1, k)) {
         int iReedHeight = 1;

         while (world.getBlockId(i, j - iReedHeight, k) == this.blockID) {
            iReedHeight++;
         }

         if (iReedHeight < 3) {
            int iMetadata = world.getBlockMetadata(i, j, k);
            if (iMetadata == 15) {
               world.setBlock(i, j + 1, k, this.blockID);
               world.SetBlockMetadataWithNotify(i, j, k, 0, 4);
            } else {
               world.SetBlockMetadataWithNotify(i, j, k, iMetadata + 1, 4);
            }
         }
      }
   }

   @Override
   public void dropBlockAsItemWithChance(World world, int i, int j, int k, int iMetadata, float fChance, int iFortuneModifier) {
      int idBelow = world.getBlockId(i, j - 1, k);
      if (idBelow == Block.reed.blockID || idBelow == 0) {
         super.a(world, i, j, k, iMetadata, fChance, iFortuneModifier);
      } else if (!world.isRemote) {
         this.dropItemsIndividually(world, i, j, k, BTWItems.sugarCaneRoots.itemID, 1, 0, 1.0F);
      }
   }

   @Override
   public boolean canPlaceBlockAt(World world, int i, int j, int k) {
      if (world.getBlockMaterial(i, j, k) == Material.water) {
         return false;
      } else {
         int blockBelowID = world.getBlockId(i, j - 1, k);
         Block blockBelow = Block.blocksList[blockBelowID];
         return blockBelowID == this.blockID
            || blockBelow != null
               && blockBelow.canReedsGrowOnBlock(world, i, j - 1, k)
               && blockBelow.isConsideredNeighbouringWaterForReedGrowthOn(world, i, j - 1, k);
      }
   }

   @Override
   public void onEntityCollidedWithBlock(World world, int i, int j, int k, Entity entity) {
      if (entity.isAffectedByMovementModifiers() && entity.onGround) {
         entity.motionX *= 0.8;
         entity.motionZ *= 0.8;
      }
   }

   @Override
   public boolean doesBlockDropAsItemOnSaw(World world, int i, int j, int k) {
      return true;
   }

   @Override
   public boolean getPreventsFluidFlow(World world, int i, int j, int k, Block fluidBlock) {
      return false;
   }

   @Override
   public boolean canBeGrazedOn(IBlockAccess blockAccess, int i, int j, int k, EntityAnimal animal) {
      return animal.canGrazeOnRoughVegetation();
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean renderBlock(RenderBlocks renderer, int i, int j, int k) {
      renderer.setRenderBounds(this.getBlockBoundsFromPoolBasedOnState(renderer.blockAccess, i, j, k));
      return renderer.renderCrossedSquares(this, i, j, k);
   }
}
