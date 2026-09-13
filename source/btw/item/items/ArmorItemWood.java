package btw.item.items;

import btw.crafting.util.FurnaceBurnTime;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.EnumArmorMaterial;
import net.minecraft.src.Icon;
import net.minecraft.src.IconRegister;

public class ArmorItemWood extends ArmorItemMod {
   static final int RENDER_INDEX = 1;
   private final int enchantability = 0;
   @Environment(EnvType.CLIENT)
   private Icon iconOverlay = null;

   public ArmorItemWood(int iItemID, int iArmorType) {
      super(iItemID, EnumArmorMaterial.CLOTH, 1, iArmorType, 0);
      this.e(this.n() >> 2);
      this.damageReduceAmount = 1;
      this.setBuoyant();
      this.setfurnaceburntime(this.getNumWoolKnitMadeOf() * FurnaceBurnTime.WOOL_KNIT.burnTime / 2);
   }

   @Override
   public boolean hasCustomColors() {
      return true;
   }

   @Override
   public boolean hasSecondRenderLayerWhenWorn() {
      return true;
   }

   @Override
   public int getDefaultColor() {
      return 8421504;
   }

   @Override
   public String getWornTexturePrefix() {
      return "fcWool";
   }

   @Override
   public int getItemEnchantability() {
      return 0;
   }

   private int getNumWoolKnitMadeOf() {
      switch (this.armorType) {
         case 0:
            return 2;
         case 1:
            return 4;
         case 2:
            return 3;
         default:
            return 2;
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void registerIcons(IconRegister register) {
      super.a(register);
      if (this.armorType == 0) {
         this.iconOverlay = register.registerIcon("fcItemWoolHelm_overlay");
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean requiresMultipleRenderPasses() {
      return this.iconOverlay != null;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public Icon getIconFromDamageForRenderPass(int iDamage, int iRenderPass) {
      return iRenderPass == 1 && this.iconOverlay != null ? this.iconOverlay : this.a_(iDamage);
   }
}
