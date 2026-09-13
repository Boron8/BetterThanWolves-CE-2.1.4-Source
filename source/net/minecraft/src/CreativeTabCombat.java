package net.minecraft.src;

import java.util.List;

final class CreativeTabCombat extends CreativeTabs {
   CreativeTabCombat(int var1, String var2) {
      super(var1, var2);
   }

   @Override
   public int getTabIconItemIndex() {
      return Item.swordGold.itemID;
   }

   @Override
   public void displayAllReleventItems(List var1) {
      super.displayAllReleventItems(var1);
      this.a(
         var1,
         new EnumEnchantmentType[]{
            EnumEnchantmentType.armor,
            EnumEnchantmentType.armor_feet,
            EnumEnchantmentType.armor_head,
            EnumEnchantmentType.armor_legs,
            EnumEnchantmentType.armor_torso,
            EnumEnchantmentType.bow,
            EnumEnchantmentType.weapon
         }
      );
   }
}
