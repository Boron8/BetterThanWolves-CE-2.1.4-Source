package com.prupe.mcpatcher.cc;

import com.prupe.mcpatcher.Config;
import com.prupe.mcpatcher.mal.biome.BiomeAPI;
import com.prupe.mcpatcher.mal.biome.ColorMap;
import com.prupe.mcpatcher.mal.biome.ColorMapBase;
import com.prupe.mcpatcher.mal.biome.ColorUtils;
import com.prupe.mcpatcher.mal.biome.IColorMap;
import com.prupe.mcpatcher.mal.resource.FakeResourceLocation;
import com.prupe.mcpatcher.mal.resource.PropertiesFile;
import com.prupe.mcpatcher.mal.resource.TexturePackAPI;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.Entity;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.World;
import net.minecraft.src.WorldProvider;

@Environment(EnvType.CLIENT)
public class ColorizeWorld {
   private static final int fogBlendRadius = Config.getInt("Custom Colors", "fogBlendRadius", 7);
   private static final FakeResourceLocation UNDERWATERCOLOR = TexturePackAPI.newMCPatcherResourceLocation("/misc/underwatercolor.png");
   private static final FakeResourceLocation UNDERLAVACOLOR = TexturePackAPI.newMCPatcherResourceLocation("/misc/underlavacolor.png");
   private static final FakeResourceLocation FOGCOLOR0 = TexturePackAPI.newMCPatcherResourceLocation("/misc/fogcolor0.png");
   private static final FakeResourceLocation SKYCOLOR0 = TexturePackAPI.newMCPatcherResourceLocation("/misc/skycolor0.png");
   private static final String TEXT_KEY = "text.";
   private static final String TEXT_CODE_KEY = "text.code.";
   private static final int CLOUDS_DEFAULT = -1;
   private static final int CLOUDS_NONE = 0;
   private static final int CLOUDS_FAST = 1;
   private static final int CLOUDS_FANCY = 2;
   private static int cloudType = -1;
   private static Entity fogCamera;
   private static final Map<Integer, Integer> textColorMap = new HashMap<>();
   private static final int[] textCodeColors = new int[32];
   private static final boolean[] textCodeColorSet = new boolean[32];
   private static int signTextColor;
   static IColorMap underwaterColor;
   private static IColorMap underlavaColor;
   private static IColorMap fogColorMap;
   private static IColorMap skyColorMap;
   public static float[] netherFogColor;
   public static float[] endFogColor;
   public static int endSkyColor;

   static void reset() {
      underwaterColor = null;
      underlavaColor = null;
      fogColorMap = null;
      skyColorMap = null;
      netherFogColor = new float[]{0.2F, 0.03F, 0.03F};
      endFogColor = new float[]{0.075F, 0.075F, 0.094F};
      endSkyColor = 1579032;
      cloudType = -1;
      textColorMap.clear();

      for (int i = 0; i < textCodeColorSet.length; i++) {
         textCodeColorSet[i] = false;
      }

      signTextColor = 0;
   }

   static void reloadFogColors(PropertiesFile properties) {
      underwaterColor = wrapFogMap(ColorMap.loadFixedColorMap(Colorizer.useFogColors, UNDERWATERCOLOR));
      underlavaColor = wrapFogMap(ColorMap.loadFixedColorMap(Colorizer.useFogColors, UNDERLAVACOLOR));
      fogColorMap = wrapFogMap(ColorMap.loadFixedColorMap(Colorizer.useFogColors, FOGCOLOR0));
      skyColorMap = wrapFogMap(ColorMap.loadFixedColorMap(Colorizer.useFogColors, SKYCOLOR0));
      Colorizer.loadFloatColor("fog.nether", netherFogColor);
      Colorizer.loadFloatColor("fog.end", endFogColor);
      endSkyColor = Colorizer.loadIntColor("sky.end", endSkyColor);
   }

   static IColorMap wrapFogMap(IColorMap map) {
      if (map == null) {
         return null;
      } else {
         if (fogBlendRadius > 0) {
            map = new ColorMapBase.Blended(map, fogBlendRadius);
         }

         map = new ColorMapBase.Cached(map);
         IColorMap var2 = new ColorMapBase.Smoothed(map, 3000.0F);
         return new ColorMapBase.Outer(var2);
      }
   }

   static void reloadCloudType(PropertiesFile properties) {
      String value = properties.getString("clouds", "").toLowerCase();
      if (value.equals("fast")) {
         cloudType = 1;
      } else if (value.equals("fancy")) {
         cloudType = 2;
      } else if (value.equals("none")) {
         cloudType = 0;
      }
   }

   static void reloadTextColors(PropertiesFile properties) {
      for (int i = 0; i < textCodeColors.length; i++) {
         textCodeColorSet[i] = Colorizer.loadIntColor("text.code." + i, textCodeColors, i);
         if (textCodeColorSet[i] && i + 16 < textCodeColors.length) {
            textCodeColors[i + 16] = (textCodeColors[i] & 16579836) >> 2;
            textCodeColorSet[i + 16] = true;
         }
      }

      for (Entry<String, String> entry : properties.entrySet()) {
         String key = entry.getKey();
         String value = entry.getValue();
         if (key.startsWith("text.") && !key.startsWith("text.code.")) {
            key = key.substring("text.".length()).trim();

            try {
               int oldColor;
               if (key.equals("xpbar")) {
                  oldColor = 8453920;
               } else if (key.equals("boss")) {
                  oldColor = 16711935;
               } else {
                  oldColor = Integer.parseInt(key, 16);
               }

               int newColor = Integer.parseInt(value, 16);
               textColorMap.put(oldColor, newColor);
            } catch (NumberFormatException var7) {
            }
         }
      }

      signTextColor = Colorizer.loadIntColor("text.sign", 0);
   }

   public static void setupForFog(Entity entity) {
      fogCamera = entity;
   }

   private static boolean computeFogColor(IBlockAccess blockAccess, IColorMap colorMap) {
      if (colorMap != null && fogCamera != null) {
         int i = (int)fogCamera.posX;
         int j = (int)fogCamera.posY;
         int k = (int)fogCamera.posZ;
         Colorizer.setColorF(colorMap.getColorMultiplierF(blockAccess, i, j, k));
         return true;
      } else {
         return false;
      }
   }

   public static boolean computeFogColor(WorldProvider worldProvider, float f) {
      return worldProvider.getWorldType() == 0 && computeFogColor(worldProvider.worldObj, fogColorMap);
   }

   public static boolean computeSkyColor(World world, float f) {
      if (world.provider.getWorldType() == 0 && computeFogColor(world, skyColorMap)) {
         computeLightningFlash(world, f);
         return true;
      } else {
         return false;
      }
   }

   public static boolean computeUnderwaterColor() {
      return computeFogColor(BiomeAPI.getWorld(), underwaterColor);
   }

   public static boolean computeUnderlavaColor() {
      return computeFogColor(BiomeAPI.getWorld(), underlavaColor);
   }

   private static void computeLightningFlash(World world, float f) {
      if (world.lastLightningBolt > 0) {
         f = 0.45F * ColorUtils.clamp(world.lastLightningBolt - f);
         Colorizer.setColor[0] = Colorizer.setColor[0] * (1.0F - f) + 0.8F * f;
         Colorizer.setColor[1] = Colorizer.setColor[1] * (1.0F - f) + 0.8F * f;
         Colorizer.setColor[2] = Colorizer.setColor[2] * (1.0F - f) + 0.8F * f;
      }
   }

   public static boolean drawFancyClouds(boolean fancyGraphics) {
      switch (cloudType) {
         case 0:
         case 1:
            return false;
         case 2:
            return true;
         default:
            return fancyGraphics;
      }
   }

   public static int drawFancyClouds(int fancyGraphics) {
      switch (cloudType) {
         case 0:
         case 1:
         case 2:
            return cloudType;
         default:
            return fancyGraphics;
      }
   }

   public static int colorizeText(int defaultColor) {
      int high = defaultColor & 0xFF000000;
      defaultColor &= 16777215;
      Integer newColor = textColorMap.get(defaultColor);
      return newColor == null ? high | defaultColor : high | newColor;
   }

   public static int colorizeText(int defaultColor, int index) {
      return index >= 0 && index < textCodeColors.length && textCodeColorSet[index] ? defaultColor & 0xFF000000 | textCodeColors[index] : defaultColor;
   }

   public static int colorizeSignText() {
      return signTextColor;
   }

   static {
      try {
         reset();
      } catch (Throwable var1) {
         var1.printStackTrace();
      }
   }
}
