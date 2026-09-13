package btw.block.blocks;

import btw.block.util.Flammability;
import java.util.Random;
import net.minecraft.src.Block;
import net.minecraft.src.BlockTallGrass;
import net.minecraft.src.EntityAnimal;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;

public class TallGrassBlock extends BlockTallGrass {
   private static final double HALF_WIDTH = 0.4F;

   public TallGrassBlock(int iBlockID) {
      super(iBlockID);
      this.c(0.0F);
      this.setBuoyant();
      this.setFireProperties(Flammability.GRASS);
      this.initBlockBounds(0.099999994F, 0.0, 0.099999994F, 0.9000000059604645, 0.8, 0.9000000059604645);
      this.a(i);
      this.c("tallgrass");
   }

   @Override
   public int idDropped(int iMetadata, Random rand, int iFortuneModifier) {
      return -1;
   }

   @Override
   public void updateTick(World world, int i, int j, int k, Random rand) {
      int iBlockAboveMaxNaturalLight = world.getBlockNaturalLightValueMaximum(i, j + 1, k);
      int iBlockAboveCurrentNaturalLight = iBlockAboveMaxNaturalLight - world.skylightSubtracted;
      if (iBlockAboveCurrentNaturalLight >= 9 && world.provider.dimensionId != 1) {
         int iMetadata = world.getBlockMetadata(i, j, k);
         if (iMetadata == 1 && rand.nextInt(3) == 0) {
            int iTargetI = i + rand.nextInt(3) - 1;
            int iTargetJ = j - 1 + rand.nextInt(5) - 3;
            int iTargetK = k + rand.nextInt(3) - 1;
            int iTargetBlockID = world.getBlockId(iTargetI, iTargetJ + 1, iTargetK);
            if (world.getBlockId(iTargetI, iTargetJ, iTargetK) == Block.tilledField.blockID && world.isAirBlock(iTargetI, iTargetJ + 1, iTargetK)) {
               int iTargetBlockMaxNaturalLight = world.getBlockNaturalLightValueMaximum(iTargetI, iTargetJ + 1, iTargetK);
               if (iTargetBlockMaxNaturalLight >= 9) {
                  world.setBlockAndMetadataWithNotify(iTargetI, iTargetJ + 1, iTargetK, Block.tallGrass.blockID, 1);
               }
            }
         }
      }

      super.a(world, i, j, k, rand);
   }

   @Override
   public boolean canSpitWebReplaceBlock(World world, int i, int j, int k) {
      return true;
   }

   @Override
   public boolean isReplaceableVegetation(World world, int i, int j, int k) {
      return true;
   }

   @Override
   public ItemStack getStackRetrievedByBlockDispenser(World world, int i, int j, int k) {
      return this.c_(world.getBlockMetadata(i, j, k));
   }

   @Override
   public boolean canBeGrazedOn(IBlockAccess blockAccess, int i, int j, int k, EntityAnimal animal) {
      return blockAccess.getBlockMetadata(i, j, k) != 0 || animal.canGrazeOnRoughVegetation();
   }

   @Override
   public int getHerbivoreItemFoodValue(int iItemDamage) {
      return iItemDamage != 1 && iItemDamage != 2 ? super.getHerbivoreItemFoodValue(iItemDamage) : 1600;
   }
}
