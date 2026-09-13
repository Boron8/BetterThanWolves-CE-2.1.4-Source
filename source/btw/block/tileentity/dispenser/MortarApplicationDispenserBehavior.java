package btw.block.tileentity.dispenser;

import net.minecraft.src.BehaviorDefaultDispenseItem;
import net.minecraft.src.Block;
import net.minecraft.src.BlockDispenser;
import net.minecraft.src.EnumFacing;
import net.minecraft.src.IBlockSource;
import net.minecraft.src.ItemStack;

public class MortarApplicationDispenserBehavior extends BehaviorDefaultDispenseItem {
   @Override
   public ItemStack dispenseStack(IBlockSource blockSource, ItemStack stack) {
      EnumFacing facing = BlockDispenser.getDispenserFacing(blockSource.getBlockMetadata());
      int x = blockSource.getXInt() + facing.getFrontOffsetX();
      int y = blockSource.getYInt();
      int z = blockSource.getZInt() + facing.getFrontOffsetZ();
      Block blockToFront = Block.blocksList[blockSource.k().getBlockId(x, y, z)];
      if (blockToFront != null && blockToFront.onMortarApplied(blockSource.k(), x, y, z)) {
         blockSource.k().playAuxSFX(2274, x, y, z, 0);
         stack.stackSize--;
         return stack;
      } else {
         return super.dispenseStack(blockSource, stack);
      }
   }
}
