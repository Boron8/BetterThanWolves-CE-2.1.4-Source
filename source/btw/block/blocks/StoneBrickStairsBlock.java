package btw.block.blocks;

import btw.block.BTWBlocks;
import java.util.Random;
import net.minecraft.src.Block;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.World;

public class StoneBrickStairsBlock extends StairsBlock {
   private int strata;

   public StoneBrickStairsBlock(int iBlockID, int strata) {
      super(iBlockID, Block.stoneBrick, strata << 2);
      this.strata = strata;
      this.setPicksEffectiveOn();
      this.c("stairsStoneBrickSmooth");
   }

   @Override
   public int idDropped(int iMetaData, Random rand, int iFortuneModifier) {
      int blockID = BTWBlocks.looseStoneBrickStairs.blockID;
      if (this.strata != 0) {
         if (this.strata == 1) {
            blockID = BTWBlocks.looseMidStrataStoneBrickStairs.blockID;
         } else {
            blockID = BTWBlocks.looseDeepStrataStoneBrickStairs.blockID;
         }
      }

      return blockID;
   }

   @Override
   public void onBlockDestroyedWithImproperTool(World world, EntityPlayer player, int i, int j, int k, int iMetadata) {
      this.c(world, i, j, k, iMetadata, 0);
   }

   @Override
   public boolean hasMortar(IBlockAccess blockAccess, int i, int j, int k) {
      return true;
   }

   public int getStrata() {
      return this.strata;
   }
}
