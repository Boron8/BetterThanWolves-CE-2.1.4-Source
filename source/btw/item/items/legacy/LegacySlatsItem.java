package btw.item.items.legacy;

import btw.block.BTWBlocks;
import btw.item.items.PlaceAsBlockItem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.Icon;
import net.minecraft.src.ItemStack;

public class LegacySlatsItem extends PlaceAsBlockItem {
   public LegacySlatsItem(int iItemID) {
      super(iItemID, BTWBlocks.aestheticNonOpaque.blockID, 8, "fcItemSlats");
      this.setBuoyant();
      this.setIncineratedInCrucible();
   }

   @Override
   public boolean canItemPassIfFilter(ItemStack filteredItem) {
      int iFilterableProperties = filteredItem.getItem().getFilterableProperties(filteredItem);
      return (iFilterableProperties & 24) != 0;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public Icon getHopperFilterIcon() {
      return BTWBlocks.slatsPane.getHopperFilterIcon();
   }
}
