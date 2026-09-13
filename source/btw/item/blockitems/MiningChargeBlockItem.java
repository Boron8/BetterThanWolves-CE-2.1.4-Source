package btw.item.blockitems;

import btw.block.BTWBlocks;
import btw.block.blocks.MiningChargeBlock;
import btw.world.util.BlockPos;
import net.minecraft.src.ItemBlock;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;

public class MiningChargeBlockItem extends ItemBlock {
   public MiningChargeBlockItem(int iItemID) {
      super(iItemID);
   }

   @Override
   public boolean onItemUsedByBlockDispenser(ItemStack stack, World world, int i, int j, int k, int iFacing) {
      BlockPos targetPos = new BlockPos(i, j, k, iFacing);
      MiningChargeBlock.createPrimedEntity(world, targetPos.x, targetPos.y, targetPos.z, iFacing);
      world.playAuxSFX(2236, i, j, k, BTWBlocks.miningCharge.blockID);
      return true;
   }
}
