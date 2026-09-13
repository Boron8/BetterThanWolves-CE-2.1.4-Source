package btw.item.items;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.IconRegister;
import net.minecraft.src.ItemFood;

public class CookedMysteryMeatItem extends ItemFood {
   public CookedMysteryMeatItem(int iItemID) {
      super(iItemID, 5, 0.25F, true, false);
      this.b("fcItemMysteryMeatCooked");
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void registerIcons(IconRegister register) {
      this.itemIcon = register.registerIcon("beefCooked");
   }
}
