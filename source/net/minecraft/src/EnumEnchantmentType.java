package net.minecraft.src;

public enum EnumEnchantmentType {
   all,
   armor,
   armor_feet,
   armor_legs,
   armor_torso,
   armor_head,
   weapon,
   digger,
   bow;

   public boolean canEnchantItem(Item var1) {
      if (this == all) {
         return true;
      } else if (var1 instanceof ItemArmor) {
         if (this == armor) {
            return true;
         } else {
            ItemArmor var2 = (ItemArmor)var1;
            if (var2.armorType == 0) {
               return this == armor_head;
            } else if (var2.armorType == 2) {
               return this == armor_legs;
            } else if (var2.armorType == 1) {
               return this == armor_torso;
            } else {
               return var2.armorType == 3 ? this == armor_feet : false;
            }
         }
      } else if (var1 instanceof ItemSword) {
         return this == weapon;
      } else if (var1 instanceof ItemTool) {
         return this == digger;
      } else {
         return var1 instanceof ItemBow ? this == bow : false;
      }
   }
}
