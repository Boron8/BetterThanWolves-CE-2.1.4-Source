package btw.block.blocks;

import btw.block.BTWBlocks;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.World;

public class NetherrackBlock extends FullBlock {
   public NetherrackBlock(int iBlockID) {
      super(iBlockID, BTWBlocks.netherRockMaterial);
      this.c(0.6F);
      this.b(0.6666667F);
      this.setPicksEffectiveOn();
      this.a(j);
      this.a(CreativeTabs.tabBlock);
      this.c("hellrock");
   }

   @Override
   public float getMovementModifier(World world, int i, int j, int k) {
      return 1.0F;
   }

   @Override
   public int getEfficientToolLevel(IBlockAccess blockAccess, int i, int j, int k) {
      return 2;
   }

   @Override
   public void onBlockAdded(World world, int i, int j, int k) {
      super.a(world, i, j, k);
      if (!world.provider.isHellWorld) {
         world.setBlock(i, j, k, BTWBlocks.fallingNetherrack.blockID, 0, 2);
      }
   }
}
