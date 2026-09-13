package net.minecraft.src;

public enum EnumToolMaterial {
   WOOD(0, 10, 1.01F, 0, 0, 20, 2),
   STONE(1, 50, 1.01F, 1, 5, 10, 1),
   IRON(2, 500, 6.0F, 2, 14, 25, 2),
   EMERALD(3, 1561, 8.0F, 3, 14, 30, 2),
   GOLD(0, 32, 12.0F, 0, 22, 30, 3),
   SOULFORGED_STEEL(4, 2250, 12.0F, 4, 0, 30, 4);

   private final int harvestLevel;
   private final int maxUses;
   private final float efficiencyOnProperMaterial;
   private final int damageVsEntity;
   private final int enchantability;
   private final int infernalMaxEnchantmentCost;
   private final int infernalMaxNumEnchants;

   private EnumToolMaterial(
      int iHarvestLevel, int iMaxUses, float fEffeciency, int iWeaponDamage, int iEnchantability, int iInfernalMaxEnchantmentCost, int iInfernalMaxNumEnchants
   ) {
      this.harvestLevel = iHarvestLevel;
      this.maxUses = iMaxUses;
      this.efficiencyOnProperMaterial = fEffeciency;
      this.damageVsEntity = iWeaponDamage;
      this.enchantability = iEnchantability;
      this.infernalMaxEnchantmentCost = iInfernalMaxEnchantmentCost;
      this.infernalMaxNumEnchants = iInfernalMaxNumEnchants;
   }

   public int getMaxUses() {
      return this.maxUses;
   }

   public float getEfficiencyOnProperMaterial() {
      return this.efficiencyOnProperMaterial;
   }

   public int getDamageVsEntity() {
      return this.damageVsEntity;
   }

   public int getHarvestLevel() {
      return this.harvestLevel;
   }

   public int getEnchantability() {
      return this.enchantability;
   }

   public int getToolCraftingMaterial() {
      return this == WOOD
         ? Block.planks.blockID
         : (
            this == STONE
               ? Block.cobblestone.blockID
               : (this == GOLD ? Item.ingotGold.itemID : (this == IRON ? Item.ingotIron.itemID : (this == EMERALD ? Item.diamond.itemID : 0)))
         );
   }

   public int getInfernalMaxEnchantmentCost() {
      return this.infernalMaxEnchantmentCost;
   }

   public int getInfernalMaxNumEnchants() {
      return this.infernalMaxNumEnchants;
   }
}
