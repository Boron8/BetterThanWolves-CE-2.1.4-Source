package btw.item.items;

import btw.crafting.util.FurnaceBurnTime;
import btw.util.ColorUtils;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.StringTranslate;

public class WoolKnitItem extends Item {
   public WoolKnitItem(int iItemID) {
      super(iItemID);
      this.e(0);
      this.a(true);
      this.setBuoyant();
      this.setBellowsBlowDistance(1);
      this.setfurnaceburntime(FurnaceBurnTime.WOOL_KNIT);
      this.setFilterableProperties(16);
      this.b("fcItemWoolKnit");
      this.a(CreativeTabs.tabMaterials);
   }

   @Override
   public String getItemDisplayName(ItemStack stack) {
      int itemDamage = stack.getItemDamage();
      return ("" + StringTranslate.getInstance().translateNamedKey("woolKnit." + ColorUtils.colorOrder[itemDamage])).trim();
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void getSubItems(int iItemID, CreativeTabs creativeTabs, List list) {
      for (int iColor = 0; iColor < 16; iColor++) {
         list.add(new ItemStack(iItemID, 1, iColor));
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public int getColorFromItemStack(ItemStack stack, int iRenderPass) {
      return WoolItem.woolColors[stack.getItemDamage()];
   }
}
