package btw.item.items.legacy;

import btw.block.BTWBlocks;
import btw.item.items.PlaceAsBlockItem;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.ItemStack;
import net.minecraft.src.MathHelper;
import net.minecraft.src.StringTranslate;

public class LegacyCandleItem extends PlaceAsBlockItem {
   public static final int[] candleColors = new int[]{
      1052688, 11743532, 3887386, 5320730, 2437522, 8073150, 2651799, 8618883, 4408131, 14188952, 4312372, 14602026, 6719955, 12801229, 15435844, 16777215
   };
   public static final String[] candleColorNames = new String[]{
      "Black", "Red", "Green", "Brown", "Blue", "Purple", "Cyan", "Light Gray", "Gray", "Pink", "Lime", "Yellow", "Light Blue", "Magenta", "Orange", "White"
   };

   public LegacyCandleItem(int iItemID) {
      super(iItemID, BTWBlocks.legacyCandle.blockID, 0, "fcItemCandle");
      this.e(0);
      this.a(true);
      this.setBuoyant();
   }

   @Override
   public int getMetadata(int iItemDamage) {
      return iItemDamage;
   }

   @Override
   public String getItemDisplayName(ItemStack stack) {
      int iColor = MathHelper.clamp_int(stack.getItemDamage(), 0, 15);
      return ("Old " + candleColorNames[iColor] + " " + StringTranslate.getInstance().translateNamedKey(this.i(stack))).trim();
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
   public int getColorFromItemStack(ItemStack itemStack, int iLayer) {
      return candleColors[itemStack.getItemDamage()];
   }
}
