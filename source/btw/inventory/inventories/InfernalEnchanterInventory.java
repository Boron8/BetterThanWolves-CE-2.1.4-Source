package btw.inventory.inventories;

import btw.inventory.container.InfernalEnchanterContainer;
import net.minecraft.src.InventoryBasic;

public class InfernalEnchanterInventory extends InventoryBasic {
   final InfernalEnchanterContainer container;

   public InfernalEnchanterInventory(InfernalEnchanterContainer container, String name, int iNumSlots) {
      super(name, true, iNumSlots);
      this.container = container;
   }

   @Override
   public void onInventoryChanged() {
      super.onInventoryChanged();
      this.container.onCraftMatrixChanged(this);
   }
}
