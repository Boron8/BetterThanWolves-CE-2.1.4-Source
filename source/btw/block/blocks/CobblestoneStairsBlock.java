package btw.block.blocks;

import btw.block.BTWBlocks;
import java.util.Random;
import net.minecraft.src.Block;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.World;

public class CobblestoneStairsBlock extends StairsBlock {
   private int strata;

   public CobblestoneStairsBlock(int iBlockID, int strata) {
      super(iBlockID, Block.cobblestone, strata);
      this.strata = strata;
      this.setPicksEffectiveOn();
      this.c("stairsStone");
   }

   @Override
   public int idDropped(int iMetaData, Random rand, int iFortuneModifier) {
      int blockID = BTWBlocks.looseCobblestoneStairs.blockID;
      if (this.strata != 0) {
         if (this.strata == 1) {
            blockID = BTWBlocks.looseMidStrataCobblestoneStairs.blockID;
         } else {
            blockID = BTWBlocks.looseDeepStrataCobblestoneStairs.blockID;
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
