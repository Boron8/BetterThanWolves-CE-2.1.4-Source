package btw.block.blocks;

import btw.block.BTWBlocks;
import btw.block.util.Flammability;
import btw.item.BTWItems;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.IconRegister;
import net.minecraft.src.World;

public class WickerSlabBlock extends SlabBlock {
   public WickerSlabBlock(int iBlockID) {
      super(iBlockID, BTWBlocks.wickerMaterial);
      this.c(0.5F);
      this.setAxesEffectiveOn();
      this.setBuoyant();
      this.setFireProperties(Flammability.WICKER);
      this.a(i);
      this.c("fcBlockWickerSlab");
      this.a(CreativeTabs.tabBlock);
   }

   @Override
   public boolean doesBlockBreakSaw(World world, int i, int j, int k) {
      return false;
   }

   @Override
   public boolean dropComponentItemsOnBadBreak(World world, int i, int j, int k, int iMetadata, float fChanceOfDrop) {
      this.dropItemsIndividually(world, i, j, k, BTWItems.wickerPane.itemID, 1, 0, fChanceOfDrop);
      this.dropItemsIndividually(world, i, j, k, BTWItems.sawDust.itemID, 3, 0, fChanceOfDrop);
      return true;
   }

   @Override
   public int getCombinedBlockID(int iMetadata) {
      return BTWBlocks.wickerBlock.blockID;
   }

   @Override
   public boolean canToolsStickInBlock(IBlockAccess blockAccess, int i, int j, int k) {
      return false;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void registerIcons(IconRegister register) {
      this.blockIcon = register.registerIcon("fcBlockWicker");
   }
}
