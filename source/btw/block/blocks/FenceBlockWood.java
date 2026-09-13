package btw.block.blocks;

import btw.block.BTWBlocks;
import btw.block.util.Flammability;
import btw.item.BTWItems;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.World;

public class FenceBlockWood extends FenceBlock {
   public FenceBlockWood(int iBlockID) {
      super(iBlockID, "wood", BTWBlocks.plankMaterial);
      this.c(1.5F);
      this.b(5.0F);
      this.setAxesEffectiveOn();
      this.setBuoyant();
      this.setFireProperties(Flammability.PLANKS);
      this.a(g);
      this.c("fence");
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
}
