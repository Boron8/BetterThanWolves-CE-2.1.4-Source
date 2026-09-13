package btw.item.items;

import net.minecraft.src.EnumArmorMaterial;

public class ArmorItemPadded extends ArmorItemMod {
   static final int RENDER_INDEX = 1;
   private final int enchantability = 0;

   public ArmorItemPadded(int iItemID, int iArmorType) {
      super(iItemID, EnumArmorMaterial.CLOTH, 1, iArmorType, 0);
      this.e(this.n() >> 1);
      this.setBuoyant();
      this.setIncineratedInCrucible();
   }

   @Override
   public boolean hasCustomColors() {
      return true;
   }

   @Override
   public int getDefaultColor() {
      return 10063743;
   }

   @Override
   public String getWornTexturePrefix() {
      return "fcPadded";
   }

   @Override
   public int getItemEnchantability() {
      return 0;
   }
}
