package btw.block.blocks;

import btw.block.BTWBlocks;
import btw.item.BTWItems;
import net.minecraft.src.EnumMobType;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.World;

public class PressurePlateBlockWood extends PressurePlateBlock {
   public PressurePlateBlockWood(int iBlockID) {
      super(iBlockID, "wood", BTWBlocks.plankMaterial, EnumMobType.everything);
      this.c(0.5F);
      this.setAxesEffectiveOn();
      this.setBuoyant();
      this.a(g);
      this.c("pressurePlate");
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
