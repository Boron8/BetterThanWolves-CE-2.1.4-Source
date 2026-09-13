package btw.block.blocks;

import btw.block.BTWBlocks;
import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.Material;
import net.minecraft.src.World;

public class SpiderEyeBlock extends Block {
   public SpiderEyeBlock(int blockID) {
      super(blockID, Material.ground);
      this.c(0.6F);
      this.setShovelsEffectiveOn(true);
      this.setBuoyancy(1.0F);
      this.a(BTWBlocks.stepSoundSquish);
      this.a(CreativeTabs.tabBlock);
      this.c("fcBlockSpiderEye");
   }

   @Override
   public boolean canBePistonShoveled(World world, int x, int y, int z) {
      return true;
   }
}
