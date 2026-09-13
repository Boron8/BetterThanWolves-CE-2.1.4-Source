package btw.block.blocks;

import btw.block.BTWBlocks;
import btw.block.util.Flammability;
import btw.item.BTWItems;
import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.World;

public class WickerBlock extends Block {
   public WickerBlock(int iBlockID) {
      super(iBlockID, BTWBlocks.wickerMaterial);
      this.c(0.5F);
      this.setAxesEffectiveOn();
      this.setBuoyant();
      this.setFireProperties(Flammability.WICKER);
      this.a(i);
      this.c("fcBlockWicker");
      this.a(CreativeTabs.tabBlock);
   }

   @Override
   public boolean doesBlockBreakSaw(World world, int i, int j, int k) {
      return false;
   }

   @Override
   public boolean dropComponentItemsOnBadBreak(World world, int i, int j, int k, int iMetadata, float fChanceOfDrop) {
      this.dropItemsIndividually(world, i, j, k, BTWItems.wickerPane.itemID, 3, 0, fChanceOfDrop);
      this.dropItemsIndividually(world, i, j, k, BTWItems.sawDust.itemID, 6, 0, fChanceOfDrop);
      return true;
   }

   @Override
   public boolean canToolsStickInBlock(IBlockAccess blockAccess, int i, int j, int k) {
      return false;
   }
}
