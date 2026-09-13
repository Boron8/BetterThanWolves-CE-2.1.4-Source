package btw.block.blocks;

import btw.block.BTWBlocks;
import btw.world.util.WorldUtils;
import java.util.Random;
import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Item;
import net.minecraft.src.Material;
import net.minecraft.src.World;

public class UnfiredClayBlock extends Block {
   public UnfiredClayBlock(int iBlockID) {
      super(iBlockID, Material.clay);
      this.c(0.6F);
      this.setShovelsEffectiveOn();
      this.a(BTWBlocks.stepSoundSquish);
      this.c("fcBlockUnfiredClay");
      this.a(CreativeTabs.tabBlock);
   }

   @Override
   public int idDropped(int iMetaData, Random rand, int iFortuneModifier) {
      return Item.clay.itemID;
   }

   @Override
   public int quantityDropped(Random rand) {
      return 9;
   }

   @Override
   public boolean canPlaceBlockAt(World world, int i, int j, int k) {
      return WorldUtils.doesBlockHaveLargeCenterHardpointToFacing(world, i, j - 1, k, 1, true);
   }

   @Override
   public void onBlockAdded(World world, int i, int j, int k) {
      if (!WorldUtils.doesBlockHaveLargeCenterHardpointToFacing(world, i, j - 1, k, 1, true)) {
         this.c(world, i, j, k, world.getBlockMetadata(i, j, k), 0);
         world.setBlockWithNotify(i, j, k, 0);
      }
   }

   @Override
   public void onNeighborBlockChange(World world, int i, int j, int k, int iBlockID) {
      if (!WorldUtils.doesBlockHaveLargeCenterHardpointToFacing(world, i, j - 1, k, 1, true)) {
         this.c(world, i, j, k, world.getBlockMetadata(i, j, k), 0);
         world.setBlockWithNotify(i, j, k, 0);
      }
   }

   @Override
   public boolean canTransmitRotationVerticallyOnTurntable(IBlockAccess blockAccess, int i, int j, int k) {
      return false;
   }

   @Override
   public boolean canBePistonShoveled(World world, int i, int j, int k) {
      return true;
   }
}
