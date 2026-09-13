package btw.item.items;

import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EnumArmorMaterial;

public class ArmorItemChain extends ArmorItemMod {
   static final int RENDER_INDEX = 1;

   public ArmorItemChain(int iItemID, int iArmorType, int iWeight) {
      super(iItemID, EnumArmorMaterial.CHAIN, 1, iArmorType, iWeight);
      this.setInfernalMaxEnchantmentCost(30);
      this.setInfernalMaxNumEnchants(2);
      this.a(CreativeTabs.tabCombat);
   }

   @Override
   public String getWornTexturePrefix() {
      return "chain";
   }
}
