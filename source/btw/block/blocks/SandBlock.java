package btw.block.blocks;

import btw.item.BTWItems;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.Material;
import net.minecraft.src.World;

public class SandBlock extends FallingFullBlock {
   public SandBlock(int iBlockID) {
      super(iBlockID, Material.sand);
      this.c(0.5F);
      this.setShovelsEffectiveOn();
      this.setFilterableProperties(8);
      this.a(n);
      this.c("sand");
      this.a(CreativeTabs.tabBlock);
   }

   @Override
   public float getMovementModifier(World world, int i, int j, int k) {
      return 0.8F;
   }

   @Override
   public boolean dropComponentItemsOnBadBreak(World world, int i, int j, int k, int iMetadata, float fChanceOfDrop) {
      this.dropItemsIndividually(world, i, j, k, BTWItems.sandPile.itemID, 6, 0, fChanceOfDrop);
      return true;
   }

   @Override
   public boolean canBePistonShoveled(World world, int i, int j, int k) {
      return true;
   }

   @Override
   public boolean canReedsGrowOnBlock(World world, int i, int j, int k) {
      return true;
   }

   @Override
   public boolean canCactusGrowOnBlock(World world, int i, int j, int k) {
      return true;
   }
}
