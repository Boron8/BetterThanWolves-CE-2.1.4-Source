package btw.block.blocks;

import btw.block.BTWBlocks;
import java.util.Random;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.World;

public class NetherBrickStairsBlock extends StairsBlock {
   public NetherBrickStairsBlock(int iBlockID) {
      super(iBlockID, bE, 0);
      this.c("stairsNetherBrick");
   }

   @Override
   public int idDropped(int iMetaData, Random rand, int iFortuneModifier) {
      return BTWBlocks.looseNEtherBrickStairs.blockID;
   }

   @Override
   public void onBlockDestroyedWithImproperTool(World world, EntityPlayer player, int i, int j, int k, int iMetadata) {
      this.c(world, i, j, k, iMetadata, 0);
   }

   @Override
   public boolean hasMortar(IBlockAccess blockAccess, int i, int j, int k) {
      return true;
   }
}
