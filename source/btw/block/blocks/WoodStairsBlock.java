package btw.block.blocks;

import btw.block.util.Flammability;
import btw.item.BTWItems;
import net.minecraft.src.Block;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.World;

public class WoodStairsBlock extends StairsBlock {
   public WoodStairsBlock(int iBlockID, Block referenceBlock, int iReferenceBlockMetadata) {
      super(iBlockID, referenceBlock, iReferenceBlockMetadata);
      this.setAxesEffectiveOn();
      this.setBuoyant();
      this.setFireProperties(Flammability.PLANKS);
   }

   @Override
   public int getHarvestToolLevel(IBlockAccess blockAccess, int i, int j, int k) {
      return 2;
   }

   @Override
   public boolean dropComponentItemsOnBadBreak(World world, int i, int j, int k, int iMetadata, float fChanceOfDrop) {
      this.dropItemsIndividually(world, i, j, k, BTWItems.sawDust.itemID, 2, 0, fChanceOfDrop);
      return true;
   }

   @Override
   public int getFurnaceBurnTime(int iItemDamage) {
      return this.referenceBlock.getFurnaceBurnTime(this.referenceBlockMetadata) * 3 / 4;
   }
}
