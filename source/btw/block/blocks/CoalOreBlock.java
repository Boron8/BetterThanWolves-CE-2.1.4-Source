package btw.block.blocks;

import btw.item.BTWItems;
import java.util.Random;
import net.minecraft.src.Item;

public class CoalOreBlock extends OreBlockStaged {
   public CoalOreBlock(int iBlockID) {
      super(iBlockID);
   }

   @Override
   public int idDropped(int iMetadata, Random random, int iFortuneModifier) {
      return Item.coal.itemID;
   }

   @Override
   public int idDroppedOnConversion(int iMetadata) {
      return BTWItems.coalDust.itemID;
   }
}
