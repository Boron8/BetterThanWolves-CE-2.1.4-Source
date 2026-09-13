package btw.item.items;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.PotionHelper;

public class SoulFluxItem extends Item {
   public SoulFluxItem(int iItemID) {
      super(iItemID);
      this.setBuoyant();
      this.setBellowsBlowDistance(3);
      this.setFilterableProperties(8);
      this.c(PotionHelper.glowstoneEffect);
      this.b("fcItemSoulFlux");
      this.a(CreativeTabs.tabMaterials);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean hasEffect(ItemStack itemStack) {
      return true;
   }
}
