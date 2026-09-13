package btw.block.blocks;

import btw.block.BTWBlocks;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EntityFallingSand;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.World;

public class LadderBlock extends LadderBlockBase {
   public LadderBlock(int iBlockID) {
      super(iBlockID);
      this.c("fcBlockLadder");
      this.a(CreativeTabs.tabDecorations);
   }

   @Override
   public boolean getCanBeSetOnFireDirectly(IBlockAccess blockAccess, int i, int j, int k) {
      return true;
   }

   @Override
   public boolean getCanBeSetOnFireDirectlyByItem(IBlockAccess blockAccess, int i, int j, int k) {
      return false;
   }

   @Override
   public boolean setOnFireDirectly(World world, int i, int j, int k) {
      int iNewMetadata = BTWBlocks.flamingLadder.setFacing(0, this.getFacing(world, i, j, k));
      world.setBlockAndMetadataWithNotify(i, j, k, BTWBlocks.flamingLadder.blockID, iNewMetadata);
      return true;
   }

   @Override
   public int getChanceOfFireSpreadingDirectlyTo(IBlockAccess blockAccess, int i, int j, int k) {
      return 60;
   }

   @Override
   public boolean canBeCrushedByFallingEntity(World world, int i, int j, int k, EntityFallingSand entity) {
      return true;
   }

   @Override
   public void onCrushedByFallingEntity(World world, int i, int j, int k, EntityFallingSand entity) {
      if (!world.isRemote) {
         this.c(world, i, j, k, world.getBlockMetadata(i, j, k), 0);
      }
   }
}
