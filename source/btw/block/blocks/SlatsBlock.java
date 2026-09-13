package btw.block.blocks;

import btw.block.util.Flammability;
import btw.item.BTWItems;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.Block;
import net.minecraft.src.Icon;
import net.minecraft.src.IconRegister;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.World;

public class SlatsBlock extends PaneBlock {
   @Environment(EnvType.CLIENT)
   private Icon filterIcon;

   public SlatsBlock(int iBlockID) {
      super(iBlockID, "fcBlockSlats", "fcBlockSlats_side", Material.wood, false);
      this.c(0.5F);
      this.setAxesEffectiveOn();
      this.setBuoyant();
      this.setFireProperties(Flammability.PLANKS);
      this.k(4);
      Block.useNeighborBrightness[iBlockID] = true;
      this.a(g);
      this.c("fcBlockSlats");
   }

   @Override
   public boolean doesBlockBreakSaw(World world, int i, int j, int k) {
      return false;
   }

   @Override
   public boolean dropComponentItemsOnBadBreak(World world, int i, int j, int k, int iMetadata, float fChanceOfDrop) {
      this.dropItemsIndividually(world, i, j, k, Item.stick.itemID, 2, 0, fChanceOfDrop);
      this.dropItemsIndividually(world, i, j, k, BTWItems.sawDust.itemID, 2, 0, fChanceOfDrop);
      return true;
   }

   @Override
   public boolean canItemPassIfFilter(ItemStack filteredItem) {
      int iFilterableProperties = filteredItem.getItem().getFilterableProperties(filteredItem);
      return (iFilterableProperties & 24) != 0;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void registerIcons(IconRegister register) {
      super.a(register);
      this.filterIcon = register.registerIcon("fcBlockHopper_slats");
   }

   @Environment(EnvType.CLIENT)
   @Override
   public Icon getHopperFilterIcon() {
      return this.filterIcon;
   }
}
