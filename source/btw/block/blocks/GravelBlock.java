package btw.block.blocks;

import btw.item.BTWItems;
import java.util.Random;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Item;
import net.minecraft.src.Material;
import net.minecraft.src.World;

public class GravelBlock extends FallingFullBlock {
   public GravelBlock(int iBlockID) {
      super(iBlockID, Material.sand);
      this.c(0.6F);
      this.setShovelsEffectiveOn();
      this.setFilterableProperties(8);
      this.a(h);
      this.c("gravel");
      this.a(CreativeTabs.tabBlock);
   }

   @Override
   public int idDropped(int iMetadata, Random rand, int iFortuneModifier) {
      return iFortuneModifier <= 0 && rand.nextInt(10) == 0 ? Item.flint.itemID : this.blockID;
   }

   @Override
   public void onBlockDestroyedWithImproperTool(World world, EntityPlayer player, int i, int j, int k, int iMetadata) {
      if (world.rand.nextInt(10) == 0) {
         this.dropItemsIndividually(world, i, j, k, Item.flint.itemID, 1, 0, 1.0F);
      } else {
         super.onBlockDestroyedWithImproperTool(world, player, i, j, k, iMetadata);
      }
   }

   @Override
   public boolean dropComponentItemsOnBadBreak(World world, int i, int j, int k, int iMetadata, float fChanceOfDrop) {
      this.dropItemsIndividually(world, i, j, k, BTWItems.gravelPile.itemID, 6, 0, fChanceOfDrop);
      return true;
   }

   @Override
   public boolean canBePistonShoveled(World world, int i, int j, int k) {
      return true;
   }
}
