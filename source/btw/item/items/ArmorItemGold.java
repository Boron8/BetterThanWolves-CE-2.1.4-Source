package btw.item.items;

import net.minecraft.src.EnumArmorMaterial;

public class ArmorItemGold extends ArmorItem {
   private static final int RENDER_INDEX = 4;

   public ArmorItemGold(int iItemID, int iArmorType, int iWeight) {
      super(iItemID, EnumArmorMaterial.GOLD, 4, iArmorType, iWeight);
      this.setInfernalMaxEnchantmentCost(30);
      this.setInfernalMaxNumEnchants(3);
   }
}
