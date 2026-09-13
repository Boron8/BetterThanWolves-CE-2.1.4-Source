package btw.item.items;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.IconRegister;
import net.minecraft.src.ItemFood;

public class CookedWolfChopItem extends ItemFood {
   public CookedWolfChopItem(int iItemID) {
      super(iItemID, 5, 0.25F, false, false);
      this.b("fcItemWolfChopCooked");
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void registerIcons(IconRegister register) {
      this.itemIcon = register.registerIcon("porkchopCooked");
   }
}
