package btw.item.items;

import net.minecraft.src.Enchantment;
import net.minecraft.src.ItemEditableBook;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.NBTTagList;
import net.minecraft.src.NBTTagString;

public class AncientProphecyItem extends ItemEditableBook {
   public static String[] prophecyTexts = new String[]{
      "Prophecy Text 1",
      "Prophecy Text 2",
      "Prophecy Text 3",
      "Prophecy Text 4",
      "Prophecy Text 5",
      "Prophecy Text 6",
      "Prophecy Text 7",
      "Prophecy Text 8",
      "Prophecy Text 9",
      "Prophecy Text 10",
      "Prophecy Text 11",
      "Prophecy Text 12",
      "Prophecy Text 13",
      "Prophecy Text 14",
      "Prophecy Text 15",
      "Prophecy Text 16"
   };
   public static String cheatProphecyText = "...and on the third day of the fourth month, the prophet descended from on high and spoke onto his flock: \n\n'Woe be onto the cheater and his kin, for verily...he does suck'...";

   public AncientProphecyItem(int iItemID) {
      super(iItemID);
      this.a(true);
      this.e(0);
      this.setBuoyant();
      this.b("fcItemAncientProphecy");
   }

   public void initializeProphecyDataFromEnchantmentID(ItemStack stack, int iEnchantmentID) {
      if (iEnchantmentID < 0) {
         this.initializeProphecyData(stack, cheatProphecyText);
      } else {
         int iEnchantmentNum = this.convertEnchantmentIDToEnchantmentNumber(iEnchantmentID);
         int iProphecyIndex = iEnchantmentNum % prophecyTexts.length;
         this.initializeProphecyData(stack, prophecyTexts[iProphecyIndex]);
      }
   }

   private int convertEnchantmentIDToEnchantmentNumber(int iEnchantmentID) {
      int iEnchantmentNum = 0;

      for (int iTempNum = 0; iTempNum < Enchantment.field_92090_c.length; iTempNum++) {
         if (Enchantment.field_92090_c[iTempNum].effectId == iEnchantmentID) {
            iEnchantmentNum = iTempNum;
            break;
         }
      }

      return iEnchantmentNum;
   }

   private void initializeProphecyData(ItemStack stack, String sProphecyText) {
      NBTTagCompound tagCompound = new NBTTagCompound();
      tagCompound.setString("title", "Prophecy Fragment");
      tagCompound.setString("author", "Unknown");
      NBTTagList pageList = new NBTTagList("pages");
      pageList.appendTag(new NBTTagString("1", sProphecyText));
      tagCompound.setTag("pages", pageList);
      stack.setTagCompound(tagCompound);
   }
}
