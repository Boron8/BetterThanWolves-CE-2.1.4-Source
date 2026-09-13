package btw.item.items.legacy;

import btw.block.BTWBlocks;
import btw.item.items.PlaceAsBlockItem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.Icon;
import net.minecraft.src.ItemStack;

public class LegacyGrateItem extends PlaceAsBlockItem {
   public LegacyGrateItem(int iItemID) {
      super(iItemID, BTWBlocks.aestheticNonOpaque.blockID, 6, "fcItemGrate");
      this.setBuoyant();
      this.setIncineratedInCrucible();
   }

   @Override
   public boolean canItemPassIfFilter(ItemStack filteredItem) {
      int iFilterableProperties = filteredItem.getItem().getFilterableProperties(filteredItem);
      return (iFilterableProperties & 10) != 0;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public Icon getHopperFilterIcon() {
      return BTWBlocks.gratePane.getHopperFilterIcon();
   }
}
