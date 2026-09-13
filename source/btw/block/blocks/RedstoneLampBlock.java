package btw.block.blocks;

import java.util.Random;
import net.minecraft.src.Block;
import net.minecraft.src.BlockRedstoneLight;
import net.minecraft.src.World;

public class RedstoneLampBlock extends BlockRedstoneLight {
   private final boolean powered;

   public RedstoneLampBlock(int iBlockID, boolean bIsLit) {
      super(iBlockID, bIsLit);
      this.setPicksEffectiveOn(true);
      this.powered = bIsLit;
   }

   @Override
   protected boolean canSilkHarvest() {
      return false;
   }

   @Override
   public void onBlockAdded(World world, int i, int j, int k) {
      if (!world.isRemote) {
         if (this.powered && !world.isBlockIndirectlyGettingPowered(i, j, k)) {
            world.scheduleBlockUpdate(i, j, k, this.blockID, 4);
         } else if (!this.powered && world.isBlockIndirectlyGettingPowered(i, j, k)) {
            world.setBlock(i, j, k, Block.redstoneLampActive.blockID);
         }
      }
   }

   @Override
   public void onNeighborBlockChange(World world, int i, int j, int k, int iNeighborBlockID) {
      if (this.powered) {
         if (!world.isBlockIndirectlyGettingPowered(i, j, k) && !world.isUpdatePendingThisTickForBlock(i, j, k, this.blockID)) {
            world.scheduleBlockUpdate(i, j, k, this.blockID, 4);
         }
      } else if (world.isBlockIndirectlyGettingPowered(i, j, k)) {
         world.setBlockWithNotify(i, j, k, Block.redstoneLampActive.blockID);
      }
   }

   @Override
   public void updateTick(World world, int i, int j, int k, Random rand) {
      if (!world.isRemote && this.powered && !world.isBlockIndirectlyGettingPowered(i, j, k)) {
         world.setBlock(i, j, k, Block.redstoneLampIdle.blockID);
      }
   }
}
