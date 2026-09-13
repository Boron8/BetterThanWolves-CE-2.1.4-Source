package btw.block.blocks;

import btw.block.BTWBlocks;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.Material;
import net.minecraft.src.World;

public class RottenFleshSlabBlock extends SlabBlock {
   public RottenFleshSlabBlock(int iBlockID) {
      super(iBlockID, Material.ground);
      this.c(0.6F);
      this.setShovelsEffectiveOn(true);
      this.setBuoyancy(1.0F);
      this.a(BTWBlocks.stepSoundSquish);
      this.c("fcBlockRottenFleshSlab");
      this.a(CreativeTabs.tabBlock);
   }

   @Override
   public boolean doesBlockBreakSaw(World world, int i, int j, int k) {
      return false;
   }

   @Override
   public int getCombinedBlockID(int iMetadata) {
      return BTWBlocks.rottenFleshBlock.blockID;
   }

   @Override
   public boolean canBePistonShoveled(World world, int i, int j, int k) {
      return true;
   }
}
