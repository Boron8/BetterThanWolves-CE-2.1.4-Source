package btw.item.items;

import net.minecraft.src.EnumArmorMaterial;

public class ArmorItemDiamond extends ArmorItem {
   private static final int RENDER_INDEX = 3;

   public ArmorItemDiamond(int iItemID, int iArmorType, int iWeight) {
      super(iItemID, EnumArmorMaterial.DIAMOND, 3, iArmorType, iWeight);
      this.setInfernalMaxEnchantmentCost(30);
      this.setInfernalMaxNumEnchants(2);
   }
}
