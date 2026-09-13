package btw.block.blocks.legacy;

import net.minecraft.src.BlockCarrot;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;

public class LegacyCarrotBlock extends BlockCarrot {
   public LegacyCarrotBlock(int iBlockID) {
      super(iBlockID);
   }

   @Override
   public float getBaseGrowthChance(World world, int i, int j, int k) {
      return 0.1F;
   }

   @Override
   protected void incrementGrowthLevel(World world, int i, int j, int k) {
      int iGrowthLevel = this.getGrowthLevel(world, i, j, k) + 1;
      if (iGrowthLevel != 7 && (iGrowthLevel & 1) != 0) {
         this.setGrowthLevelNoNotify(world, i, j, k, iGrowthLevel);
      } else {
         super.incrementGrowthLevel(world, i, j, k);
      }
   }

   @Override
   public ItemStack getStackRetrievedByBlockDispenser(World world, int i, int j, int k) {
      return world.getBlockMetadata(i, j, k) >= 7 ? super.getStackRetrievedByBlockDispenser(world, i, j, k) : null;
   }
}
