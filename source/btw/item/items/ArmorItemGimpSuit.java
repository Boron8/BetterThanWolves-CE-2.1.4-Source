package btw.item.items;

import net.minecraft.src.EnumArmorMaterial;

public class ArmorItemGimpSuit extends ArmorItemMod {
   static final int RENDER_INDEX = 1;
   static final int WORN_WEIGHT = 0;

   public ArmorItemGimpSuit(int iItemID, int iArmorType) {
      super(iItemID, EnumArmorMaterial.CLOTH, 1, iArmorType, 0);
      this.e(this.n() << 1);
      this.setInfernalMaxEnchantmentCost(10);
      this.setInfernalMaxNumEnchants(2);
      this.setBuoyant();
   }

   @Override
   public String getWornTexturePrefix() {
      return "fcGimp";
   }
}
