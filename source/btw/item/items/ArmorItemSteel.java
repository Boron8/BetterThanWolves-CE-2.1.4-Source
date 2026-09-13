package btw.item.items;

import net.minecraft.src.EnumArmorMaterial;

public class ArmorItemSteel extends ArmorItemMod {
   static final int RENDER_INDEX = 1;
   private final int enchantability = 0;
   static final int MAX_DAMAGE = 576;

   public ArmorItemSteel(int iItemID, int iArmorType, int iWeight) {
      super(iItemID, EnumArmorMaterial.DIAMOND, 1, iArmorType, iWeight);
      this.e(576);
      this.setInfernalMaxEnchantmentCost(30);
      this.setInfernalMaxNumEnchants(4);
   }

   @Override
   public int getItemEnchantability() {
      return 0;
   }

   @Override
   public String getWornTexturePrefix() {
      return "plate";
   }
}
