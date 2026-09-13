package btw.item.items;

import net.minecraft.src.EnumArmorMaterial;

public class ArmorItemLeather extends ArmorItem {
   private static final int RENDER_INDEX = 0;
   private static final int WORN_WEIGHT = 0;

   public ArmorItemLeather(int iItemID, int iArmorType) {
      super(iItemID, EnumArmorMaterial.CLOTH, 0, iArmorType, 0);
      this.setInfernalMaxEnchantmentCost(10);
      this.setInfernalMaxNumEnchants(2);
      this.setBuoyant();
      this.setIncineratedInCrucible();
   }
}
