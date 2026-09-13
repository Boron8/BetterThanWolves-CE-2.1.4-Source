package btw.item.items;

import net.minecraft.src.EnumArmorMaterial;

public class ArmorItemIron extends ArmorItem {
   private static final int RENDER_INDEX = 2;

   public ArmorItemIron(int iItemID, int iArmorType, int iWeight) {
      super(iItemID, EnumArmorMaterial.IRON, 2, iArmorType, iWeight);
      this.setInfernalMaxEnchantmentCost(25);
      this.setInfernalMaxNumEnchants(2);
   }
}
