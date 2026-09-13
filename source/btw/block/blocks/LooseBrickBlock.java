package btw.block.blocks;

import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.Material;
import net.minecraft.src.World;

public class LooseBrickBlock extends MortarReceiverBlock {
   public LooseBrickBlock(int iBlockID) {
      super(iBlockID, Material.rock);
      this.c(1.0F);
      this.b(5.0F);
      this.setPicksEffectiveOn();
      this.setChiselsEffectiveOn();
      this.a(Block.soundStoneFootstep);
      this.c("fcBlockBrickLoose");
      this.a(CreativeTabs.tabBlock);
   }

   @Override
   public boolean onMortarApplied(World world, int i, int j, int k) {
      world.setBlockWithNotify(i, j, k, Block.brick.blockID);
      return true;
   }
}
