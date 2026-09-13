package btw.item.items;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.IconRegister;
import net.minecraft.src.ItemFood;
import net.minecraft.src.Potion;

public class FoodItem extends ItemFood {
   public static final int FOOD_POISIONING_STANDARD_DURATION = 60;
   public static final float FOOD_POISONING_STANDARD_CHANCE = 0.3F;
   public static final int FOOD_POISIONING_INCREASED_DURATION = 60;
   public static final float FOOD_POISONING_INCREASED_CHANCE = 0.8F;
   public static final int DONUT_HUNGER_HEALED = 1;
   public static final float DONUT_SATURATION_MODIFIER = 0.5F;
   public static final String DONUT_ITEM_NAME = "fcItemDonut";
   public static final int DOG_FOOD_HUNGER_HEALED = 3;
   public static final float DOG_FOOD_SATURATION_MODIFIER = 0.0F;
   public static final String DOG_FOOD_ITEM_NAME = "fcItemKibble";
   public static final int RAW_EGG_HUNGER_HEALED = 2;
   public static final float RAW_EGG_SATURATION_MODIFIER = 0.25F;
   public static final String RAW_EGG_ITEM_NAME = "fcItemEggRaw";
   public static final int FRIED_EGG_HUNGER_HEALED = 3;
   public static final float FRIED_EGG_SATURATION_MODIFIER = 0.25F;
   public static final String FRIED_EGG_ITEM_NAME = "fcItemEggFried";
   public static final int BOILED_POTATO_HUNGER_HEALED = 2;
   public static final float BOILED_POTATO_SATURATION_MODIFIER = 0.0F;
   public static final String BOILED_POTATO_ITEM_NAME = "fcItemPotatoBoiled";
   public static final int COOKED_CARROT_HUNGER_HEALED = 2;
   public static final float COOKED_CARROT_SATURATION_MODIFIER = 0.0F;
   public static final String COOKED_CARROT_ITEM_NAME = "fcItemCarrotCooked";
   public static final int TASTY_SANDWICH_HUNGER_HEALED = 5;
   public static final float TASTY_SANDWICH_SATURATION_MODIFIER = 0.25F;
   public static final String TASTY_SANDWICH_ITEM_NAME = "fcItemSandwichTasty";
   public static final int STEAK_AND_POTATOES_HUNGER_HEALED = 6;
   public static final float STEAK_AND_POTATOES_SATURATION_MODIFIER = 0.25F;
   public static final String STEAK_AND_POTATOES_ITEM_NAME = "fcItemSteakAndPotatoes";
   public static final int HAM_AND_EGGS_HUNGER_HEALED = 6;
   public static final float HAM_AND_EGGS_SATURATION_MODIFIER = 0.25F;
   public static final String HAM_AND_EGGS_ITEM_NAME = "fcItemHamAndEggs";
   public static final int STEAK_DINNER_HUNGER_HEALED = 8;
   public static final float STEAK_DINNER_SATURATION_MODIFIER = 0.25F;
   public static final String STEAK_DINNER_ITEM_NAME = "fcItemDinnerSteak";
   public static final int PORK_DINNER_HUNGER_HEALED = 8;
   public static final float PORK_DINNER_SATURATION_MODIFIER = 0.25F;
   public static final String PORK_DINNER_ITEM_NAME = "fcItemDinnerPork";
   public static final int WOLF_DINNER_HUNGER_HEALED = 8;
   public static final float WOLF_DINNER_SATURATION_MODIFIER = 0.25F;
   public static final String WOLF_DINNER_ITEM_NAME = "fcItemDinnerWolf";
   public static final int RAW_KEBAB_HUNGER_HEALED = 6;
   public static final float RAW_KEBAB_SATURATION_MODIFIER = 0.25F;
   public static final String RAW_KEBAB_ITEM_NAME = "fcItemKebabRaw";
   public static final int COOKED_KEBAB_HUNGER_HEALED = 8;
   public static final float COOKED_KEBAB_SATURATION_MODIFIER = 0.25F;
   public static final String COOKED_KEBAB_ITEM_NAME = "fcItemKebabCooked";
   public static final int CHICKEN_SOUP_HUNGER_HEALED = 8;
   public static final float CHICKEN_SOUP_SATURATION_MODIFIER = 0.25F;
   public static final String CHICKEN_SOUP_ITEM_NAME = "fcItemSoupChicken";
   public static final int FISH_SOUP_HUNGER_HEALED = 5;
   public static final float FISH_SOUP_SATURATION_MODIFIER = 0.25F;
   public static final String FISH_SOUP_ITEM_NAME = "fcItemChowder";
   public static final int HEARTY_STEW_HUNGER_HEALED = 10;
   public static final float HEARTY_STEW_SATURATION_MODIFIER = 0.25F;
   public static final String HEARTY_STEW_ITEM_NAME = "fcItemStewHearty";
   public static final int RAW_MUSHROOM_OMELET_HUNGER_HEALED = 3;
   public static final float RAW_MUSHROOM_OMELET_SATURATION_MODIFIER = 0.25F;
   public static final String RAW_MUSHROOM_OMELET_ITEM_NAME = "fcItemMushroomOmletRaw";
   public static final int COOKED_MUSHROOM_OMELET_HUNGER_HEALED = 4;
   public static final float COOKED_MUSHROOM_OMELET_SATURATION_MODIFIER = 0.25F;
   public static final String COOKED_MUSHROOM_OMELET_ITEM_NAME = "fcItemMushroomOmletCooked";
   public static final int RAW_SCRAMBLED_EGGS_HUNGER_HEALED = 3;
   public static final float RAW_SCRAMBLED_EGGS_SATURATION_MODIFIER = 0.25F;
   public static final String RAW_SCRAMBLED_EGGS_ITEM_NAME = "fcItemEggScrambledRaw";
   public static final int COOKED_SCRAMBLED_EGGS_HUNGER_HEALED = 4;
   public static final float COOKED_SCRAMBLED_EGGS_SATURATION_MODIFIER = 0.25F;
   public static final String COOKED_SCRAMBLED_EGGS_ITEM_NAME = "fcItemEggScrambledCooked";
   public static final int CREEPER_OYSTERS_HUNGER_HEALED = 2;
   public static final float CREEPER_OYSTERS_SATURATION_MODIFIER = 0.8F;
   public static final String CREEPER_OYSTERS_ITEM_NAME = "fcItemCreeperOysters";
   public static final int BAT_WING_HUNGER_HEALED = 1;
   public static final float BAT_WING_SATURATION_MODIFIER = 0.8F;
   public static final String BAT_WING_ITEM_NAME = "fcItemBatWing";
   public static final int CHOCOLATE_HUNGER_HEALED = 2;
   public static final float CHOCOLATE_SATURATION_MODIFIER = 0.5F;
   public static final String CHOCOLATE_ITEM_NAME = "fcItemChocolate";
   public static final int MUTTON_RAW_HUNGER_HEALED = 3;
   public static final int MUTTON_COOKED_HUNGER_HEALED = 4;
   public static final float MUTTON_SATURATION_MODIFIER = 0.25F;
   public static final int BEAST_LIVER_RAW_HUNGER_HEALED = 5;
   public static final int BEAST_LIVER_COOKED_HUNGER_HEALED = 6;
   public static final float BEAST_LIVER_SATURATION_MODIFIER = 0.5F;
   public static final int CHICKEN_RAW_HUNGER_HEALED = 3;
   public static final int CHICKEN_COOKED_HUNGER_HEALED = 4;
   public static final float CHICKEN_SATURATION_MODIFIER = 0.25F;
   public static final int BEEF_RAW_HUNGER_HEALED = 4;
   public static final int BEEF_COOKED_HUNGER_HEALED = 5;
   public static final float BEEF_SATURATION_MODIFIER = 0.25F;
   public static final int FISH_RAW_HUNGER_HEALED = 3;
   public static final int FISH_COOKED_HUNGER_HEALED = 4;
   public static final float FISH_SATURATION_MODIFIER = 0.25F;
   public static final int PORK_CHOP_RAW_HUNGER_HEALED = 4;
   public static final int PORK_CHOP_COOKED_HUNGER_HEALED = 5;
   public static final float PORK_CHOP_SATURATION_MODIFIER = 0.25F;
   public static final int MEAT_CURED_HUNGER_HEALED = 3;
   public static final float MEAT_CURED_SATURATION_MODIFIER = 0.25F;
   public static final int MEAT_BURNED_HUNGER_HEALED = 2;
   public static final float MEAT_BURNED_SATURATION_MODIFIER = 0.25F;
   private String iconNameOverride = null;

   public FoodItem(int iItemID, int iHungerHealed, float fSaturationModifier, boolean bWolfMeat, String sItemName) {
      super(iItemID, iHungerHealed, fSaturationModifier, bWolfMeat);
      this.b(sItemName);
   }

   public FoodItem(int iItemID, int iHungerHealed, float fSaturationModifier, boolean bWolfMeat, String sItemName, boolean bZombiesConsume) {
      super(iItemID, iHungerHealed, fSaturationModifier, bWolfMeat, bZombiesConsume);
      this.b(sItemName);
   }

   public FoodItem setStandardFoodPoisoningEffect() {
      this.a(Potion.hunger.id, 60, 0, 0.3F);
      return this;
   }

   public FoodItem setIncreasedFoodPoisoningEffect() {
      this.a(Potion.hunger.id, 60, 0, 0.8F);
      return this;
   }

   public FoodItem setIconName(String sName) {
      this.iconNameOverride = sName;
      return this;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void registerIcons(IconRegister register) {
      if (this.iconNameOverride != null) {
         this.itemIcon = register.registerIcon(this.iconNameOverride);
      } else {
         super.a(register);
      }
   }
}
