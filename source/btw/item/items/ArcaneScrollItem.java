package btw.item.items;

import btw.item.BTWItems;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.Enchantment;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.MathHelper;
import net.minecraft.src.StatCollector;

public class ArcaneScrollItem extends Item {
   public ArcaneScrollItem(int iItemID) {
      super(iItemID);
      this.e(0);
      this.a(true);
      this.setBuoyant();
      this.setBellowsBlowDistance(3);
      this.setFilterableProperties(18);
      this.b("fcItemScrollArcane");
      this.a(CreativeTabs.tabBrewing);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean hasEffect(ItemStack itemStack) {
      return true;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void addInformation(ItemStack itemStack, EntityPlayer player, List infoList, boolean bAdvamcedToolTips) {
      int iIndex = MathHelper.clamp_int(itemStack.getItemDamage(), 0, Enchantment.enchantmentsList.length - 1);
      Enchantment enchantment = Enchantment.enchantmentsList[iIndex];
      if (enchantment != null) {
         infoList.add(StatCollector.translateToLocal(enchantment.getName()));
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void getSubItems(int iItemID, CreativeTabs creativeTabs, List list) {
      for (int iTempIndex = 0; iTempIndex < Enchantment.enchantmentsList.length; iTempIndex++) {
         if (Enchantment.enchantmentsList[iTempIndex] != null) {
            list.add(new ItemStack(BTWItems.arcaneScroll, 1, Enchantment.enchantmentsList[iTempIndex].effectId));
         }
      }
   }
}
