package btw.item.items;

import net.minecraft.src.Block;
import net.minecraft.src.Enchantment;
import net.minecraft.src.EnumEnchantmentType;
import net.minecraft.src.EnumToolMaterial;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;

public class ShearsItemDiamond extends ShearsItem {
   public ShearsItemDiamond(int id) {
      super(id);
      this.e(500);
      this.setInfernalMaxEnchantmentCost(30);
      this.setInfernalMaxNumEnchants(4);
      this.b("fcItemShearsDiamond");
   }

   @Override
   public float getStrVsBlock(ItemStack var1, World var2, Block var3, int var4, int var5, int var6) {
      return super.getStrVsBlock(var1, var2, var3, var4, var5, var6) * 1.33F;
   }

   @Override
   public boolean isDamagedInCrafting() {
      return false;
   }

   @Override
   public boolean isConsumedInCrafting() {
      return false;
   }

   @Override
   public int getItemEnchantability() {
      return EnumToolMaterial.EMERALD.getEnchantability();
   }

   @Override
   public boolean isEnchantmentApplicable(Enchantment var1) {
      return var1.type == EnumEnchantmentType.digger ? true : super.isEnchantmentApplicable(var1);
   }
}
