package com.prupe.mcpatcher.cit;

import com.prupe.mcpatcher.MCLogger;
import com.prupe.mcpatcher.mal.resource.FakeResourceLocation;
import com.prupe.mcpatcher.mal.resource.PropertiesFile;
import com.prupe.mcpatcher.mal.resource.TexturePackAPI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.Potion;
import net.minecraft.src.PotionHelper;

@Environment(EnvType.CLIENT)
class PotionReplacer {
   private static final MCLogger logger = MCLogger.getLogger("Custom Item Textures", "CIT");
   private static final String ITEM_ID_POTION = "minecraft:potion";
   private static final String ITEM_ID_GLASS_BOTTLE = "minecraft:glass_bottle";
   private static final String LAYER_POTION_CONTENTS = "potion_contents";
   private static final String LAYER_POTION_DRINKABLE = "potion";
   private static final String LAYER_POTION_SPLASH = "potion_splash";
   private static final String LAYER_EMPTY_BOTTLE = "potion";
   private static final int SPLASH_BIT = 16384;
   private static final int EFFECT_BITS = 16399;
   private static final int MUNDANE_BITS = 16447;
   private static final int WATER_BITS = 65535;
   private static final int[] POTION_EFFECTS = new int[]{-1, 2, 10, -1, -1, 9, 5, 12, -1, -1, 1, -1, 3, -1, 14, -1, 6, -1, 8, 4, -1};
   private static final Map<String, Integer> mundanePotionMap = new HashMap<>();
   private static int weight = -2;
   final List<ItemOverride> overrides = new ArrayList<>();

   PotionReplacer() {
      FakeResourceLocation path = getPotionPath("water", false);
      if (TexturePackAPI.hasResource(path)) {
         weight++;
         this.registerVanillaPotion(path, 0, 65535, false);
         weight--;
      }

      path = getPotionPath("empty", false);
      if (TexturePackAPI.hasResource(path)) {
         this.registerEmptyBottle(path);
      }

      this.registerPotionsByEffect(false);
      this.registerPotionsByEffect(true);
      this.registerMundanePotions(false);
      this.registerMundanePotions(true);
      this.registerOtherPotions(false);
      this.registerOtherPotions(true);
   }

   private static FakeResourceLocation getPotionPath(String name, boolean splash) {
      String path = "cit/potion/" + (splash ? "splash/" : "normal/") + name + ".png";
      return TexturePackAPI.newMCPatcherResourceLocation(path);
   }

   private static Properties newProperties(FakeResourceLocation path, String itemID, String layer) {
      Properties properties = new Properties();
      properties.setProperty("type", "item");
      properties.setProperty("items", itemID);
      properties.setProperty("texture." + layer, path.toString());
      properties.setProperty("texture.potion_contents", "blank");
      properties.setProperty("weight", String.valueOf(weight));
      return properties;
   }

   private static Properties newProperties(FakeResourceLocation path, String itemID, boolean splash) {
      String layer = splash ? "potion_splash" : "potion";
      return newProperties(path, itemID, layer);
   }

   private void registerPotionsByEffect(boolean splash) {
      for (int effect = 0; effect < Potion.potionTypes.length; effect++) {
         if (Potion.potionTypes[effect] != null) {
            FakeResourceLocation path = getPotionPath(Potion.potionTypes[effect].getName().replaceFirst("^potion\\.", ""), splash);
            if (TexturePackAPI.hasResource(path)) {
               if (effect < POTION_EFFECTS.length && POTION_EFFECTS[effect] >= 0) {
                  int damage = POTION_EFFECTS[effect];
                  if (splash) {
                     damage |= 16384;
                  }

                  this.registerVanillaPotion(path, damage, 16399, splash);
               }

               if (!splash) {
                  this.registerCustomPotion(path, effect, splash);
               }
            }
         }
      }
   }

   private void registerMundanePotions(boolean splash) {
      for (Entry<String, Integer> entry : mundanePotionMap.entrySet()) {
         int damage = entry.getValue();
         if (splash) {
            damage |= 16384;
         }

         this.registerMundanePotion(entry.getKey(), damage, splash);
      }
   }

   private void registerMundanePotion(String name, int damage, boolean splash) {
      FakeResourceLocation path = getPotionPath(name, splash);
      if (TexturePackAPI.hasResource(path)) {
         this.registerVanillaPotion(path, damage, 16447, splash);
      }
   }

   private void registerOtherPotions(boolean splash) {
      FakeResourceLocation path = getPotionPath("other", splash);
      if (TexturePackAPI.hasResource(path)) {
         Properties properties = newProperties(path, "minecraft:potion", splash);
         StringBuilder sb = new StringBuilder();

         for (int i : mundanePotionMap.values()) {
            if (splash) {
               i |= 16384;
            }

            sb.append(' ').append(i);
         }

         properties.setProperty("damage", sb.toString().trim());
         properties.setProperty("damageMask", String.valueOf(16447));
         this.addOverride(path, properties);
      }
   }

   private void registerVanillaPotion(FakeResourceLocation path, int damage, int mask, boolean splash) {
      Properties properties = newProperties(path, "minecraft:potion", splash);
      properties.setProperty("damage", String.valueOf(damage));
      properties.setProperty("damageMask", String.valueOf(mask));
      this.addOverride(path, properties);
   }

   private void registerCustomPotion(FakeResourceLocation path, int effect, boolean splash) {
      Properties properties = newProperties(path, "minecraft:potion", splash);
      properties.setProperty("nbt.CustomPotionEffects.0.Id", String.valueOf(effect));
      this.addOverride(path, properties);
   }

   private void registerEmptyBottle(FakeResourceLocation path) {
      Properties properties = newProperties(path, "minecraft:glass_bottle", "potion");
      this.addOverride(path, properties);
   }

   private void addOverride(FakeResourceLocation path, Properties properties) {
      FakeResourceLocation propertiesName = TexturePackAPI.transformResourceLocation(path, ".png", ".properties");
      ItemOverride override = new ItemOverride(new PropertiesFile(logger, propertiesName, properties));
      if (override.properties.valid()) {
         this.overrides.add(override);
      }
   }

   static {
      try {
         for (int i : new int[]{0, 7, 11, 13, 15, 16, 23, 27, 29, 31, 32, 39, 43, 45, 47, 48, 55, 59, 61, 63}) {
            String name = PotionHelper.func_77905_c(i).replaceFirst("^potion\\.prefix\\.", "");
            mundanePotionMap.put(name, i);
            logger.fine("%s potion -> damage value %d", name, i);
         }

         for (int i = 0; i < Potion.potionTypes.length; i++) {
            Potion potion = Potion.potionTypes[i];
            if (potion != null) {
               logger.fine("%s potion -> effect %d", potion.getName().replaceFirst("^potion\\.", ""), i);
            }
         }
      } catch (Throwable var5) {
         var5.printStackTrace();
      }
   }
}
