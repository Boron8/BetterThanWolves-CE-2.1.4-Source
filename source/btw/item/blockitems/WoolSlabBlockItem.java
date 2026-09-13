package btw.item.blockitems;

import btw.block.BTWBlocks;
import net.minecraft.src.World;

public class WoolSlabBlockItem extends SlabBlockItem {
   public WoolSlabBlockItem(int i) {
      super(i);
      this.a(true);
      this.b("fcBlockWoolSlab");
   }

   @Override
   public int getMetadata(int i) {
      return i;
   }

   @Override
   public int getBlockIDToPlace(int iItemDamage, int iFacing, float fClickX, float fClickY, float fClickZ) {
      return iFacing != 0 && (iFacing == 1 || !(fClickY > 0.5)) ? BTWBlocks.woolSlab.blockID : BTWBlocks.woolSlabTop.blockID;
   }

   @Override
   public boolean canCombineWithBlock(World world, int i, int j, int k, int iItemDamage) {
      int iBlockID = world.getBlockId(i, j, k);
      int iBlockMetadata = world.getBlockMetadata(i, j, k);
      return (iBlockID == BTWBlocks.woolSlab.blockID || iBlockID == BTWBlocks.woolSlabTop.blockID) && iBlockMetadata == iItemDamage;
   }
}
