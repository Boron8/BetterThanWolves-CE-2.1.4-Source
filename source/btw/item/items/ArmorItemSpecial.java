package btw.item.items;

import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EnumArmorMaterial;

public class ArmorItemSpecial extends ArmorItemMod {
   static final int RENDER_INDEX = 1;
   static final int ARMOR_LEVEL = 1;
   static final int MAX_DAMAGE = 12;

   public ArmorItemSpecial(int iItemID, int iArmorType) {
      super(iItemID, EnumArmorMaterial.IRON, 1, iArmorType, 0);
      this.e(12);
      this.damageReduceAmount = EnumArmorMaterial.CLOTH.getDamageReductionAmount(iArmorType);
      this.a(CreativeTabs.tabCombat);
   }

   @Override
   public String getWornTexturePrefix() {
      return "special";
   }
}
