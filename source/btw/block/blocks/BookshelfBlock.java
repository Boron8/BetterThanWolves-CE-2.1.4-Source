package btw.block.blocks;

import btw.block.BTWBlocks;
import btw.block.util.Flammability;
import btw.crafting.util.FurnaceBurnTime;
import btw.item.BTWItems;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Icon;
import net.minecraft.src.Item;
import net.minecraft.src.World;

public class BookshelfBlock extends Block {
   public BookshelfBlock(int iBlockID) {
      super(iBlockID, BTWBlocks.plankMaterial);
      this.c(1.5F);
      this.setAxesEffectiveOn();
      this.setBuoyant();
      this.setFurnaceBurnTime(FurnaceBurnTime.WOOD_BASED_BLOCK);
      this.setFireProperties(Flammability.BOOKSHELVES);
      this.a(g);
      this.c("bookshelf");
      this.a(CreativeTabs.tabBlock);
   }

   @Override
   public int getHarvestToolLevel(IBlockAccess blockAccess, int i, int j, int k) {
      return 2;
   }

   @Override
   public boolean dropComponentItemsOnBadBreak(World world, int i, int j, int k, int iMetadata, float fChanceOfDrop) {
      this.dropItemsIndividually(world, i, j, k, BTWItems.sawDust.itemID, 2, 0, fChanceOfDrop);
      this.dropItemsIndividually(world, i, j, k, Item.book.itemID, 3, 0, fChanceOfDrop);
      return true;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public Icon getIcon(int iSide, int iMetadata) {
      return iSide < 2 ? Block.planks.getBlockTextureFromSide(iSide) : super.getIcon(iSide, iMetadata);
   }
}
