package com.prupe.mcpatcher.cc;

import com.prupe.mcpatcher.MCLogger;
import com.prupe.mcpatcher.mal.resource.PropertiesFile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.MapColor;
import net.minecraft.src.Potion;
import net.minecraft.src.PotionHelper;

@Environment(EnvType.CLIENT)
public class ColorizeItem {
   private static final MCLogger logger = MCLogger.getLogger("Custom Colors");
   private static final Map<Integer, String> entityNamesByID = new HashMap<>();
   private static final Map<Integer, Integer> spawnerEggShellColors = new HashMap<>();
   private static final Map<Integer, Integer> spawnerEggSpotColors = new HashMap<>();
   private static int waterBottleColor;
   private static final List<Potion> potions = new ArrayList<>();
   private static boolean potionsInitialized;
   private static final String[] MAP_MATERIALS = new String[]{
      "air",
      "grass",
      "sand",
      "cloth",
      "tnt",
      "ice",
      "iron",
      "foliage",
      "snow",
      "clay",
      "dirt",
      "stone",
      "water",
      "wood",
      "quartz",
      "adobe",
      "magenta",
      "lightBlue",
      "yellow",
      "lime",
      "pink",
      "gray",
      "silver",
      "cyan",
      "purple",
      "blue",
      "brown",
      "green",
      "red",
      "black",
      "gold",
      "diamond",
      "lapis",
      "emerald",
      "obsidian",
      "netherrack"
   };

   static void reset() {
      spawnerEggShellColors.clear();
      spawnerEggSpotColors.clear();
      if (potionsInitialized && PotionHelper.getPotionColorCache() != null) {
         PotionHelper.getPotionColorCache().clear();
      }

      potionsInitialized = true;
      waterBottleColor = 3694022;

      for (Potion potion : potions) {
         potion.liquidColor = potion.origColor;
      }

      for (MapColor mapColor : MapColor.mapColorArray) {
         if (mapColor != null) {
            mapColor.colorValue = mapColor.origColorValue;
         }
      }
   }

   static void reloadPotionColors(PropertiesFile properties) {
      for (Potion potion : potions) {
         Colorizer.loadIntColor(potion.name, potion);
      }

      int[] temp = new int[]{waterBottleColor};
      Colorizer.loadIntColor("potion.water", temp, 0);
      waterBottleColor = temp[0];
   }

   static void reloadMapColors(PropertiesFile properties) {
      for (int i = 0; i < MapColor.mapColorArray.length; i++) {
         if (MapColor.mapColorArray[i] != null) {
            int[] rgb = new int[]{MapColor.mapColorArray[i].origColorValue};
            Colorizer.loadIntColor("map." + Colorizer.getStringKey(MAP_MATERIALS, i), rgb, 0);
            MapColor.mapColorArray[i].colorValue = rgb[0];
         }
      }
   }

   public static void setupSpawnerEgg(String entityName, int entityID, int defaultShellColor, int defaultSpotColor) {
      logger.config("egg.shell.%s=%06x", entityName, defaultShellColor);
      logger.config("egg.spots.%s=%06x", entityName, defaultSpotColor);
      entityNamesByID.put(entityID, entityName);
   }

   public static void setupPotion(Potion potion) {
      potion.origColor = potion.liquidColor;
      potions.add(potion);
   }

   public static int colorizeSpawnerEgg(int defaultColor, int entityID, int spots) {
      if (!Colorizer.useEggColors) {
         return defaultColor;
      } else {
         Integer value = null;
         Map<Integer, Integer> eggMap = spots == 0 ? spawnerEggShellColors : spawnerEggSpotColors;
         if (eggMap.containsKey(entityID)) {
            value = eggMap.get(entityID);
         } else if (entityNamesByID.containsKey(entityID)) {
            String name = entityNamesByID.get(entityID);
            if (name != null) {
               int[] tmp = new int[]{defaultColor};
               Colorizer.loadIntColor((spots == 0 ? "egg.shell." : "egg.spots.") + name, tmp, 0);
               eggMap.put(entityID, tmp[0]);
               value = tmp[0];
            }
         }

         return value == null ? defaultColor : value;
      }
   }

   public static int getWaterBottleColor() {
      return waterBottleColor;
   }

   static {
      try {
         reset();
      } catch (Throwable var1) {
         var1.printStackTrace();
      }
   }
}
