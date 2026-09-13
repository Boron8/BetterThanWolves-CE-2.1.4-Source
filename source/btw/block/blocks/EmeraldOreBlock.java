package btw.block.blocks;

import java.util.Random;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Item;

public class EmeraldOreBlock extends OreBlockStaged {
   public EmeraldOreBlock(int iBlockID) {
      super(iBlockID);
   }

   @Override
   public int idDropped(int iMetadata, Random rand, int iFortuneModifier) {
      return Item.emerald.itemID;
   }

   @Override
   public int idDroppedOnConversion(int iMetadata) {
      return Item.emerald.itemID;
   }

   @Override
   public int getRequiredToolLevelForOre(IBlockAccess blockAccess, int i, int j, int k) {
      return 2;
   }
}
