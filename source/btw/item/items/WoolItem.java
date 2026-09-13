package btw.item.items;

import btw.crafting.util.FurnaceBurnTime;
import btw.item.BTWItems;
import btw.util.ColorUtils;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.InventoryCrafting;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.MathHelper;
import net.minecraft.src.StringTranslate;

public class WoolItem extends Item {
   public static final int[] woolColors = new int[]{
      1052688, 11743532, 3887386, 5320730, 2437522, 8073150, 2651799, 8618883, 4408131, 14188952, 4312372, 14602026, 6719955, 12801229, 15435844, 16777215
   };
   private static List<List<Integer>> colorConversionArray = null;

   public WoolItem(int iItemID) {
      super(iItemID);
      this.e(0);
      this.a(true);
      this.setBuoyant();
      this.setBellowsBlowDistance(1);
      this.setfurnaceburntime(FurnaceBurnTime.KINDLING);
      this.setFilterableProperties(18);
      this.b("fcItemWool");
      this.a(CreativeTabs.tabMaterials);
   }

   @Override
   public String getItemDisplayName(ItemStack stack) {
      int itemDamage = stack.getItemDamage();
      return ("" + StringTranslate.getInstance().translateNamedKey("wool." + ColorUtils.colorOrder[itemDamage])).trim();
   }

   public static int averageWoolColorsInGrid(InventoryCrafting inventory) {
      int iAverageColor = 0;
      int iSumRed = 0;
      int iSumGreen = 0;
      int iSumBlue = 0;
      int iWoolCount = 0;

      for (int iTempSlot = 0; iTempSlot < inventory.getSizeInventory(); iTempSlot++) {
         ItemStack tempStack = inventory.getStackInSlot(iTempSlot);
         if (tempStack != null && (tempStack.itemID == BTWItems.wool.itemID || tempStack.itemID == BTWItems.woolKnit.itemID)) {
            int iWoolColorIndex = MathHelper.clamp_int(tempStack.getItemDamage(), 0, 15);
            int iWoolColor = woolColors[iWoolColorIndex];
            iWoolCount++;
            iSumRed += iWoolColor >> 16 & 0xFF;
            iSumGreen += iWoolColor >> 8 & 0xFF;
            iSumBlue += iWoolColor & 0xFF;
         }
      }

      if (iWoolCount > 0) {
         int iAverageRed = iSumRed / iWoolCount;
         int iAverageGreen = iSumGreen / iWoolCount;
         int iAverageBlue = iSumBlue / iWoolCount;
         iAverageColor = iAverageRed << 16 | iAverageGreen << 8 | iAverageBlue;
      }

      return iAverageColor;
   }

   private static void initColorConversionArray() {
      colorConversionArray = new ArrayList<>(16);

      for (int iTempIndex = 0; iTempIndex < 16; iTempIndex++) {
         List<Integer> tempColorList = new LinkedList<>();
         colorConversionArray.add(iTempIndex, tempColorList);
         tempColorList.add(woolColors[iTempIndex]);
      }

      setHardColorConversionPoint(8, 0, 15);
      setHardColorConversionPoint(9, 1, 15);
      setHardColorConversionPoint(14, 1, 11);
      setHardColorConversionPoint(9, 2, 10);
      setHardColorConversionPoint(5, 4, 1);
      setHardColorConversionPoint(6, 4, 2);
      setHardColorConversionPoint(12, 4, 15);
      setHardColorConversionPoint(13, 5, 9);
      setHardColorConversionPoint(7, 8, 15);
   }

   private static void setHardColorConversionPoint(int iToColorIndex, int iFromColorIndex1, int iFromColorIndex2) {
      int iFromColor1 = woolColors[iFromColorIndex1];
      int iFromColor2 = woolColors[iFromColorIndex2];
      int iBlendedRed = ((iFromColor1 >> 16 & 0xFF) + (iFromColor2 >> 16 & 0xFF)) / 2;
      int iBlendedGreen = ((iFromColor1 >> 8 & 0xFF) + (iFromColor2 >> 8 & 0xFF)) / 2;
      int iBlendedBlue = ((iFromColor1 & 0xFF) + (iFromColor2 & 0xFF)) / 2;
      int iBlendedColor = iBlendedRed << 16 | iBlendedGreen << 8 | iBlendedBlue;
      colorConversionArray.get(iToColorIndex).add(iBlendedColor);
   }

   public static int getClosestColorIndex(int iColor) {
      int iClosestIndex = -1;
      int iClosestColorDistanceSq = 0;
      int iColorRed = iColor >> 16 & 0xFF;
      int iColorGreen = iColor >> 8 & 0xFF;
      int iColorBlue = iColor & 0xFF;
      if (colorConversionArray == null) {
         initColorConversionArray();
      }

      if (MathHelper.abs_int(iColorRed - iColorGreen) > 5 || MathHelper.abs_int(iColorRed - iColorBlue) > 5) {
         for (int iTempIndex = 0; iTempIndex < 16; iTempIndex++) {
            for (int iTempColor : colorConversionArray.get(iTempIndex)) {
               int iTempColorRed = iTempColor >> 16 & 0xFF;
               int iTempColorGreen = iTempColor >> 8 & 0xFF;
               int iTempColorBlue = iTempColor & 0xFF;
               int iTempRedDelta = iTempColorRed - iColorRed;
               int iTempGreenDelta = iTempColorGreen - iColorGreen;
               int iTempBlueDelta = iTempColorBlue - iColorBlue;
               int iTempColorDistanceSq = 2 * iTempRedDelta * iTempRedDelta + 4 * iTempGreenDelta * iTempGreenDelta + 3 * iTempBlueDelta * iTempBlueDelta;
               if (iClosestIndex == -1 || iTempColorDistanceSq < iClosestColorDistanceSq) {
                  iClosestIndex = iTempIndex;
                  iClosestColorDistanceSq = iTempColorDistanceSq;
               }
            }
         }
      }

      if (iClosestIndex == -1 || iClosestColorDistanceSq > 15000) {
         int iColorTotal = iColorRed + iColorGreen + iColorBlue;
         if (iColorTotal < 125) {
            iClosestIndex = 0;
         } else if (iColorTotal < 297) {
            iClosestIndex = 8;
         } else if (iColorTotal < 579) {
            iClosestIndex = 7;
         } else {
            iClosestIndex = 15;
         }
      }

      return iClosestIndex;
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
      return woolColors[itemStack.getItemDamage()];
   }
}
