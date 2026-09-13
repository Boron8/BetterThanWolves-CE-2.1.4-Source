package btw.item.items;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.IconRegister;
import net.minecraft.src.Item;

public class StubItem extends Item {
   public StubItem(int iItemID) {
      super(iItemID);
      this.a(null);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void registerIcons(IconRegister register) {
      this.itemIcon = register.registerIcon("fcItemDung");
   }
}
