package btw.block.blocks;

import btw.block.BTWBlocks;
import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.Material;
import net.minecraft.src.World;

public class CreeperOysterBlock extends Block {
   public static final float HARDNESS = 0.6F;

   public CreeperOysterBlock(int iBlockID) {
      super(iBlockID, Material.ground);
      this.c(0.6F);
      this.setShovelsEffectiveOn(true);
      this.setBuoyancy(1.0F);
      this.a(BTWBlocks.stepSoundSquish);
      this.a(CreativeTabs.tabBlock);
      this.c("fcBlockCreeperOysters");
   }

   @Override
   public boolean doesBlockBreakSaw(World world, int i, int j, int k) {
      return false;
   }

   @Override
   public boolean canBePistonShoveled(World world, int i, int j, int k) {
      return true;
   }
}
