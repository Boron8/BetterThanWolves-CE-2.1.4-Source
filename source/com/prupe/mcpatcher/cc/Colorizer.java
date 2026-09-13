package com.prupe.mcpatcher.cc;

import com.prupe.mcpatcher.Config;
import com.prupe.mcpatcher.MCLogger;
import com.prupe.mcpatcher.mal.biome.ColorMap;
import com.prupe.mcpatcher.mal.biome.ColorUtils;
import com.prupe.mcpatcher.mal.resource.FakeResourceLocation;
import com.prupe.mcpatcher.mal.resource.PropertiesFile;
import com.prupe.mcpatcher.mal.resource.TexturePackAPI;
import com.prupe.mcpatcher.mal.resource.TexturePackChangeHandler;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.Potion;

@Environment(EnvType.CLIENT)
public class Colorizer {
   private static final MCLogger logger = MCLogger.getLogger("Custom Colors");
   static final FakeResourceLocation COLOR_PROPERTIES = TexturePackAPI.newMCPatcherResourceLocation("color.properties");
   private static PropertiesFile properties;
   static final boolean usePotionColors = Config.getBoolean("Custom Colors", "potion", true);
   static final boolean useParticleColors = Config.getBoolean("Custom Colors", "particle", true);
   static final boolean useFogColors = Config.getBoolean("Custom Colors", "fog", true);
   static final boolean useCloudType = Config.getBoolean("Custom Colors", "clouds", true);
   static final boolean useMapColors = Config.getBoolean("Custom Colors", "map", true);
   static final boolean useDyeColors = Config.getBoolean("Custom Colors", "dye", true);
   static final boolean useTextColors = Config.getBoolean("Custom Colors", "text", true);
   static final boolean useXPOrbColors = Config.getBoolean("Custom Colors", "xporb", true);
   static final boolean useEggColors = Config.getBoolean("Custom Colors", "egg", true);
   public static final float[] setColor = new float[3];

   public static void setColorF(int color) {
      ColorUtils.intToFloat3(color, setColor);
   }

   static void setColorF(float[] color) {
      setColor[0] = color[0];
      setColor[1] = color[1];
      setColor[2] = color[2];
   }

   static void init() {
   }

   private static void reset() {
      properties = new PropertiesFile(logger, COLOR_PROPERTIES);
      ColorMap.reset();

      try {
         ColorizeBlock.reset();
      } catch (NoClassDefFoundError var1) {
      }

      Lightmap.reset();
      ColorizeItem.reset();
      ColorizeWorld.reset();
      ColorizeEntity.reset();
   }

   private static void reloadColorProperties() {
      properties = PropertiesFile.getNonNull(logger, COLOR_PROPERTIES);
      logger.finer("reloading %s", properties);
   }

   static String getStringKey(String[] keys, int index) {
      return keys != null && index >= 0 && index < keys.length && keys[index] != null ? keys[index] : "" + index;
   }

   static void loadIntColor(String key, Potion potion) {
      potion.liquidColor = loadIntColor(key, potion.liquidColor);
   }

   static boolean loadIntColor(String key, int[] color, int index) {
      logger.config("%s=%06x", key, color[index]);
      String value = properties.getString(key, "");
      if (!value.equals("")) {
         try {
            color[index] = Integer.parseInt(value, 16);
            return true;
         } catch (NumberFormatException var5) {
         }
      }

      return false;
   }

   static int loadIntColor(String key, int color) {
      logger.config("%s=%06x", key, color);
      return properties.getHex(key, color);
   }

   static void loadFloatColor(String key, float[] color) {
      int intColor = ColorUtils.float3ToInt(color);
      ColorUtils.intToFloat3(loadIntColor(key, intColor), color);
   }

   static Integer loadIntegerColor(String key) {
      int[] tmp = new int[1];
      return loadIntColor(key, tmp, 0) ? tmp[0] : null;
   }

   static float[] loadFloatColor(String key) {
      Integer color = loadIntegerColor(key);
      if (color == null) {
         return null;
      } else {
         float[] rgb = new float[3];
         ColorUtils.intToFloat3(color, rgb);
         return rgb;
      }
   }

   static {
      try {
         reset();
      } catch (Throwable var1) {
         var1.printStackTrace();
      }

      TexturePackChangeHandler.register(new TexturePackChangeHandler("Custom Colors", 2) {
         @Override
         public void beforeChange() {
            Colorizer.reset();
         }

         @Override
         public void afterChange() {
            Colorizer.reloadColorProperties();
            ColorMap.reloadColorMapSettings(Colorizer.properties);
            if (Colorizer.useParticleColors) {
               ColorizeEntity.reloadParticleColors(Colorizer.properties);
            }

            try {
               ColorizeBlock.reloadAll(Colorizer.properties);
            } catch (NoClassDefFoundError var2) {
            }

            if (Colorizer.useFogColors) {
               ColorizeWorld.reloadFogColors(Colorizer.properties);
            }

            if (Colorizer.usePotionColors) {
               ColorizeItem.reloadPotionColors(Colorizer.properties);
            }

            if (Colorizer.useCloudType) {
               ColorizeWorld.reloadCloudType(Colorizer.properties);
            }

            if (Colorizer.useMapColors) {
               ColorizeItem.reloadMapColors(Colorizer.properties);
            }

            if (Colorizer.useDyeColors) {
               ColorizeEntity.reloadDyeColors(Colorizer.properties);
            }

            if (Colorizer.useTextColors) {
               ColorizeWorld.reloadTextColors(Colorizer.properties);
            }

            if (Colorizer.useXPOrbColors) {
               ColorizeEntity.reloadXPOrbColors(Colorizer.properties);
            }
         }
      });
   }
}
