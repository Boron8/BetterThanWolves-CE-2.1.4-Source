package com.prupe.mcpatcher.mal.biome;

import com.prupe.mcpatcher.Config;
import com.prupe.mcpatcher.MCLogger;
import com.prupe.mcpatcher.MCPatcherUtils;
import com.prupe.mcpatcher.mal.resource.FakeResourceLocation;
import com.prupe.mcpatcher.mal.resource.PropertiesFile;
import com.prupe.mcpatcher.mal.resource.ResourceList;
import com.prupe.mcpatcher.mal.resource.TexturePackAPI;
import com.prupe.mcpatcher.mal.util.WeightedIndex;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;
import java.util.Map.Entry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.BiomeGenBase;
import net.minecraft.src.IBlockAccess;

@Environment(EnvType.CLIENT)
public abstract class ColorMap implements IColorMap {
   private static final MCLogger logger = MCLogger.getLogger("Custom Colors");
   public static final boolean useSwampColors = Config.getBoolean("Custom Colors", "swamp", true);
   private static final int FIXED = 0;
   private static final int TEMPERATURE_HUMIDITY = 1;
   private static final int BIOME_HEIGHT = 2;
   private static final int COLORMAP_WIDTH = 256;
   private static final int COLORMAP_HEIGHT = 256;
   public static final String BLOCK_COLORMAP_DIR = "/colormap/blocks";
   public static final List<FakeResourceLocation> unusedPNGs = new ArrayList<>();
   private static final String VANILLA_TYPE = "_vanillaType";
   private static final String ALT_SOURCE = "_altSource";
   private static int defaultColorMapFormat;
   private static boolean defaultFlipY;
   private static float defaultYVariance;
   protected final FakeResourceLocation resource;
   protected final int[] map;
   protected final int width;
   protected final int height;
   protected final float maxX;
   protected final float maxY;
   private final float[] xy = new float[2];
   private final float[] lastColor = new float[3];

   public static IColorMap loadVanillaColorMap(FakeResourceLocation vanillaImage, FakeResourceLocation swampImage) {
      Properties properties = new Properties();
      properties.setProperty("format", "1");
      properties.setProperty("source", vanillaImage.toString());
      if (!TexturePackAPI.hasCustomResource(vanillaImage)) {
         if (vanillaImage.getPath().contains("grass")) {
            properties.setProperty("_vanillaType", "grass");
         } else if (vanillaImage.getPath().contains("foliage")) {
            properties.setProperty("_vanillaType", "foliage");
         }
      }

      if (swampImage != null) {
         properties.setProperty("_altSource", swampImage.toString());
      }

      return loadColorMap(true, vanillaImage, properties);
   }

   public static IColorMap loadFixedColorMap(boolean useCustom, FakeResourceLocation resource) {
      return loadColorMap(useCustom, resource, null);
   }

   public static IColorMap loadColorMap(boolean useCustom, FakeResourceLocation resource, Properties properties) {
      IColorMap map = loadColorMap1(useCustom, resource, properties);
      if (map != null) {
         map.claimResources(unusedPNGs);
      }

      return map;
   }

   private static IColorMap loadColorMap1(boolean useCustom, FakeResourceLocation resource, Properties properties) {
      if (useCustom && resource != null) {
         FakeResourceLocation propertiesResource;
         FakeResourceLocation imageResource;
         if (resource.toString().endsWith(".png")) {
            propertiesResource = TexturePackAPI.transformResourceLocation(resource, ".png", ".properties");
            imageResource = resource;
         } else {
            if (!resource.toString().endsWith(".properties")) {
               return null;
            }

            propertiesResource = resource;
            imageResource = TexturePackAPI.transformResourceLocation(resource, ".properties", ".png");
         }

         if (properties == null) {
            properties = TexturePackAPI.getProperties(propertiesResource);
            if (properties == null) {
               properties = new Properties();
            }
         }

         int format = parseFormat(MCPatcherUtils.getStringProperty(properties, "format", ""));
         if (format == 0) {
            int color = MCPatcherUtils.getHexProperty(properties, "color", 16777215);
            return new ColorMap.Fixed(color);
         } else {
            String path = MCPatcherUtils.getStringProperty(properties, "source", "");
            if (!MCPatcherUtils.isNullOrEmpty(path)) {
               imageResource = TexturePackAPI.parseResourceLocation(resource, path);
            }

            BufferedImage image = TexturePackAPI.getImage(imageResource);
            if (image == null) {
               return null;
            } else {
               switch (format) {
                  case 1:
                     String vanillaSource = MCPatcherUtils.getStringProperty(properties, "_vanillaType", "");
                     IColorMap defaultMap;
                     if ("grass".equals(vanillaSource)) {
                        defaultMap = new ColorMap.Grass(image);
                     } else if ("foliage".equals(vanillaSource)) {
                        defaultMap = new ColorMap.Foliage(image);
                     } else {
                        defaultMap = new ColorMap.TempHumidity(imageResource, properties, image);
                     }

                     path = MCPatcherUtils.getStringProperty(properties, "_altSource", "");
                     if (useSwampColors && !MCPatcherUtils.isNullOrEmpty(path)) {
                        FakeResourceLocation swampResource = TexturePackAPI.parseResourceLocation(resource, path);
                        image = TexturePackAPI.getImage(swampResource);
                        if (image != null) {
                           IColorMap swampMap = new ColorMap.TempHumidity(swampResource, properties, image);
                           return new ColorMap.Swamp(defaultMap, swampMap);
                        }
                     }

                     return defaultMap;
                  case 2:
                     ColorMap.Grid grid = new ColorMap.Grid(imageResource, properties, image);
                     if (grid.isInteger()) {
                        return new ColorMap.IntegerGrid(grid);
                     }

                     return grid;
                  default:
                     logger.error("%s: unknown format %d", resource, format);
                     return null;
               }
            }
         }
      } else {
         return null;
      }
   }

   public static void reset() {
      unusedPNGs.clear();
      defaultColorMapFormat = 1;
      defaultFlipY = false;
      defaultYVariance = Config.getInt("Custom Colors", "yVariance", 0);
   }

   public static void reloadColorMapSettings(PropertiesFile properties) {
      unusedPNGs.addAll(ResourceList.getInstance().listResources("/colormap/blocks", ".png", false));
      defaultColorMapFormat = parseFormat(properties.getString("palette.format", ""));
      defaultFlipY = properties.getBoolean("palette.flipY", false);
      defaultYVariance = properties.getFloat("palette.yVariance", 0.0F);
   }

   private static int parseFormat(String value) {
      if (MCPatcherUtils.isNullOrEmpty(value)) {
         return defaultColorMapFormat;
      } else {
         value = value.toLowerCase();
         if (value.matches("^\\d+$")) {
            try {
               return Integer.parseInt(value);
            } catch (NumberFormatException var2) {
               var2.printStackTrace();
            }
         } else {
            if (value.equals("fixed")) {
               return 0;
            }

            if (value.equals("temperature+humidity") || value.equals("t+h") || value.equals("vanilla")) {
               return 1;
            }

            if (value.equals("biome+height") || value.equals("b+h") || value.equals("grid")) {
               return 2;
            }
         }

         return defaultColorMapFormat;
      }
   }

   ColorMap(FakeResourceLocation resource, Properties properties, BufferedImage image) {
      this(resource, MCPatcherUtils.getImageRGB(image), image.getWidth(), image.getHeight());
   }

   ColorMap(FakeResourceLocation resource, int[] map, int width, int height) {
      this.resource = resource;
      this.map = map;
      this.width = width;
      this.height = height;

      for (int i = 0; i < map.length; i++) {
         map[i] &= 16777215;
      }

      this.maxX = width - 1.0F;
      this.maxY = height - 1.0F;
   }

   protected abstract void computeXY(BiomeGenBase var1, int var2, int var3, int var4, float[] var5);

   @Override
   public String toString() {
      return this.getClass().getSimpleName() + "{" + this.resource + "}";
   }

   @Override
   public final int getColorMultiplier(IBlockAccess blockAccess, int i, int j, int k) {
      this.computeXY(BiomeAPI.getBiomeGenAt(blockAccess, i, j, k), i, j, k, this.xy);
      return this.getRGB(this.xy[0], this.xy[1]);
   }

   @Override
   public final float[] getColorMultiplierF(IBlockAccess blockAccess, int i, int j, int k) {
      int rgb = this.getColorMultiplier(blockAccess, i, j, k);
      ColorUtils.intToFloat3(rgb, this.lastColor);
      return this.lastColor;
   }

   @Override
   public void claimResources(Collection<FakeResourceLocation> resources) {
      resources.remove(this.resource);
   }

   protected int getRGB(float x, float y) {
      x = clamp(x, 0.0F, this.maxX);
      y = clamp(y, 0.0F, this.maxY);
      int x0 = (int)x;
      int dx = (int)(256.0F * (x - x0));
      int x1 = x0 + 1;
      int y0 = (int)y;
      int dy = (int)(256.0F * (y - y0));
      int y1 = y0 + 1;
      if (dx == 0 && dy == 0) {
         return this.getRGB(x0, y0);
      } else if (dx == 0) {
         return this.interpolate(x0, y0, x0, y1, dy);
      } else {
         return dy == 0 ? this.interpolate(x0, y0, x1, y0, dx) : interpolate(this.interpolate(x0, y0, x1, y0, dx), this.interpolate(x0, y1, x1, y1, dx), dy);
      }
   }

   private int getRGB(int x, int y) {
      return this.map[x + this.width * y];
   }

   private int interpolate(int x1, int y1, int x2, int y2, int a2) {
      return interpolate(this.getRGB(x1, y1), this.getRGB(x2, y2), a2);
   }

   private static int interpolate(int rgb1, int rgb2, int a2) {
      int a1 = 256 - a2;
      int r1 = rgb1 >> 16 & 0xFF;
      int g1 = rgb1 >> 8 & 0xFF;
      int b1 = rgb1 & 0xFF;
      int r2 = rgb2 >> 16 & 0xFF;
      int g2 = rgb2 >> 8 & 0xFF;
      int b2 = rgb2 & 0xFF;
      int r = a1 * r1 + a2 * r2 >> 8;
      int g = a1 * g1 + a2 * g2 >> 8;
      int b = a1 * b1 + a2 * b2 >> 8;
      return r << 16 | g << 8 | b;
   }

   protected static float noise0to1(int i, int j, int k, int l) {
      int hash = (int)WeightedIndex.hash128To64(i, j, k, l) & 2147483647;
      return (float)(hash / 2.147483647E9);
   }

   protected static float noiseMinus1to1(int i, int j, int k, int l) {
      int hash = (int)WeightedIndex.hash128To64(i, j, k, l);
      return (float)(hash / -2.1474836E9F);
   }

   protected static float clamp(float i, float min, float max) {
      if (i < min) {
         return min;
      } else {
         return i > max ? max : i;
      }
   }

   protected static int clamp(int i, int min, int max) {
      if (i < min) {
         return min;
      } else {
         return i > max ? max : i;
      }
   }

   @Environment(EnvType.CLIENT)
   public static final class Fixed implements IColorMap {
      private final int colorI;
      private final float[] colorF = new float[3];

      public Fixed(int color) {
         this.colorI = color;
         ColorUtils.intToFloat3(this.colorI, this.colorF);
      }

      @Override
      public String toString() {
         return String.format("Fixed{%06x}", this.colorI);
      }

      @Override
      public boolean isHeightDependent() {
         return false;
      }

      @Override
      public int getColorMultiplier() {
         return this.colorI;
      }

      @Override
      public int getColorMultiplier(IBlockAccess blockAccess, int i, int j, int k) {
         return this.colorI;
      }

      @Override
      public float[] getColorMultiplierF(IBlockAccess blockAccess, int i, int j, int k) {
         return this.colorF;
      }

      @Override
      public void claimResources(Collection<FakeResourceLocation> resources) {
      }

      @Override
      public IColorMap copy() {
         return this;
      }
   }

   @Environment(EnvType.CLIENT)
   public static final class Foliage extends ColorMap.Vanilla {
      Foliage(BufferedImage image) {
         super(image);
      }

      Foliage(int defaultColor) {
         super(defaultColor);
      }

      @Override
      public IColorMap copy() {
         return new ColorMap.Foliage(this.defaultColor);
      }

      @Override
      int getColorMultiplier(BiomeGenBase biome, int i, int j, int k) {
         return BiomeAPI.getFoliageColor(biome, i, j, k);
      }
   }

   @Environment(EnvType.CLIENT)
   public static final class Grass extends ColorMap.Vanilla {
      Grass(BufferedImage image) {
         super(image);
      }

      Grass(int defaultColor) {
         super(defaultColor);
      }

      @Override
      public IColorMap copy() {
         return new ColorMap.Grass(this.defaultColor);
      }

      @Override
      int getColorMultiplier(BiomeGenBase biome, int i, int j, int k) {
         return BiomeAPI.getGrassColor(biome, i, j, k);
      }
   }

   @Environment(EnvType.CLIENT)
   public static final class Grid extends ColorMap {
      private final float[] biomeX = new float[BiomeGenBase.biomeList.length];
      private final float yVariance;
      private final float yOffset;
      private final int defaultColor;

      private Grid(FakeResourceLocation resource, Properties properties, BufferedImage image) {
         super(resource, properties, image);
         if (MCPatcherUtils.getBooleanProperty(properties, "flipY", ColorMap.defaultFlipY)) {
            int[] temp = new int[this.width];

            for (int i = 0; i < this.map.length / 2; i += this.width) {
               int j = this.map.length - this.width - i;
               System.arraycopy(this.map, i, temp, 0, this.width);
               System.arraycopy(this.map, j, this.map, i, this.width);
               System.arraycopy(temp, 0, this.map, j, this.width);
            }
         }

         this.yVariance = Math.max(MCPatcherUtils.getFloatProperty(properties, "yVariance", ColorMap.defaultYVariance), 0.0F);
         this.yOffset = MCPatcherUtils.getFloatProperty(properties, "yOffset", 0.0F);

         for (int i = 0; i < this.biomeX.length; i++) {
            this.biomeX[i] = i % this.width;
         }

         for (Entry<Object, Object> entry : properties.entrySet()) {
            String key = (String)entry.getKey();
            String value = (String)entry.getValue();
            if (key.endsWith(".x") && !MCPatcherUtils.isNullOrEmpty(value)) {
               key = key.substring(0, key.length() - 2);
               BiomeGenBase biome = BiomeAPI.findBiomeByName(key);
               if (biome != null && biome.biomeID >= 0 && biome.biomeID < BiomeGenBase.biomeList.length) {
                  try {
                     this.biomeX[biome.biomeID] = Float.parseFloat(value);
                  } catch (NumberFormatException var10) {
                     var10.printStackTrace();
                  }
               }
            }
         }

         this.defaultColor = MCPatcherUtils.getHexProperty(properties, "color", this.getRGB(this.biomeX[1], this.getY(64)));
      }

      private Grid(FakeResourceLocation resource, int[] map, int width, int height, float[] biomeX, float yVariance, float yOffset, int defaultColor) {
         super(resource, map, width, height);
         System.arraycopy(biomeX, 0, this.biomeX, 0, biomeX.length);
         this.yVariance = yVariance;
         this.yOffset = yOffset;
         this.defaultColor = defaultColor;
      }

      boolean isInteger() {
         if (this.yVariance == 0.0F && Math.floor(this.yOffset) == this.yOffset) {
            for (int i = 0; i < this.biomeX.length; i++) {
               if (this.biomeX[i] != i % this.width) {
                  return false;
               }
            }

            return true;
         } else {
            return false;
         }
      }

      @Override
      public boolean isHeightDependent() {
         return true;
      }

      @Override
      public int getColorMultiplier() {
         return this.defaultColor;
      }

      @Override
      public IColorMap copy() {
         return new ColorMap.Grid(this.resource, this.map, this.width, this.height, this.biomeX, this.yVariance, this.yOffset, this.defaultColor);
      }

      @Override
      protected void computeXY(BiomeGenBase biome, int i, int j, int k, float[] f) {
         f[0] = this.getX(biome, i, j, k);
         f[1] = this.getY(biome, i, j, k);
      }

      private float getX(BiomeGenBase biome, int i, int j, int k) {
         return this.biomeX[biome.biomeID];
      }

      private float getY(int j) {
         return j - this.yOffset;
      }

      private float getY(BiomeGenBase biome, int i, int j, int k) {
         float y = this.getY(j);
         if (this.yVariance != 0.0F) {
            y += this.yVariance * noiseMinus1to1(k, -j, i, ~biome.biomeID);
         }

         return y;
      }
   }

   @Environment(EnvType.CLIENT)
   public static final class IntegerGrid implements IColorMap {
      private final FakeResourceLocation resource;
      private final int[] map;
      private final int width;
      private final int maxHeight;
      private final int yOffset;
      private final int defaultColor;
      private final float[] lastColor = new float[3];

      IntegerGrid(ColorMap.Grid grid) {
         this(grid.resource, grid.map, grid.width, grid.height - 1, (int)grid.yOffset, grid.defaultColor);
      }

      IntegerGrid(FakeResourceLocation resource, int[] map, int width, int maxHeight, int yOffset, int defaultColor) {
         this.resource = resource;
         this.map = map;
         this.width = width;
         this.maxHeight = maxHeight;
         this.yOffset = yOffset;
         this.defaultColor = defaultColor;
      }

      @Override
      public String toString() {
         return this.getClass().getSimpleName() + "{" + this.resource + "}";
      }

      @Override
      public boolean isHeightDependent() {
         return true;
      }

      @Override
      public int getColorMultiplier() {
         return this.defaultColor;
      }

      @Override
      public int getColorMultiplier(IBlockAccess blockAccess, int i, int j, int k) {
         return this.getRGB(BiomeAPI.getBiomeIDAt(blockAccess, i, j, k), j);
      }

      @Override
      public float[] getColorMultiplierF(IBlockAccess blockAccess, int i, int j, int k) {
         int rgb = this.getColorMultiplier(blockAccess, i, j, k);
         ColorUtils.intToFloat3(rgb, this.lastColor);
         return this.lastColor;
      }

      @Override
      public void claimResources(Collection<FakeResourceLocation> resources) {
         resources.remove(this.resource);
      }

      @Override
      public IColorMap copy() {
         return new ColorMap.IntegerGrid(this.resource, this.map, this.width, this.maxHeight, this.yOffset, this.defaultColor);
      }

      private int getRGB(int x, int y) {
         x = ColorMap.clamp(x, 0, 255) % this.width;
         y = ColorMap.clamp(y - this.yOffset, 0, this.maxHeight);
         return this.map[y * this.width + x];
      }
   }

   @Environment(EnvType.CLIENT)
   public static final class Swamp implements IColorMap {
      private final IColorMap defaultMap;
      private final IColorMap swampMap;
      private final BiomeGenBase swampBiome;

      Swamp(IColorMap defaultMap, IColorMap swampMap) {
         this.defaultMap = defaultMap;
         this.swampMap = swampMap;
         this.swampBiome = BiomeAPI.findBiomeByName("Swampland");
      }

      @Override
      public String toString() {
         return this.defaultMap.toString();
      }

      @Override
      public boolean isHeightDependent() {
         return this.defaultMap.isHeightDependent() || this.swampMap.isHeightDependent();
      }

      @Override
      public int getColorMultiplier() {
         return this.defaultMap.getColorMultiplier();
      }

      @Override
      public int getColorMultiplier(IBlockAccess blockAccess, int i, int j, int k) {
         IColorMap map = BiomeAPI.getBiomeGenAt(blockAccess, i, j, k) == this.swampBiome ? this.swampMap : this.defaultMap;
         return map.getColorMultiplier(blockAccess, i, j, k);
      }

      @Override
      public float[] getColorMultiplierF(IBlockAccess blockAccess, int i, int j, int k) {
         IColorMap map = BiomeAPI.getBiomeGenAt(blockAccess, i, j, k) == this.swampBiome ? this.swampMap : this.defaultMap;
         return map.getColorMultiplierF(blockAccess, i, j, k);
      }

      @Override
      public void claimResources(Collection<FakeResourceLocation> resources) {
         this.defaultMap.claimResources(resources);
         this.swampMap.claimResources(resources);
      }

      @Override
      public IColorMap copy() {
         return new ColorMap.Swamp(this.defaultMap.copy(), this.swampMap.copy());
      }
   }

   @Environment(EnvType.CLIENT)
   public static final class TempHumidity extends ColorMap {
      private final int defaultColor;

      private TempHumidity(FakeResourceLocation resource, Properties properties, BufferedImage image) {
         super(resource, properties, image);
         this.defaultColor = MCPatcherUtils.getHexProperty(properties, "color", this.getRGB(this.maxX * 0.5F, this.maxY * 0.5F));
      }

      private TempHumidity(FakeResourceLocation resource, int[] map, int width, int height, int defaultColor) {
         super(resource, map, width, height);
         this.defaultColor = defaultColor;
      }

      @Override
      public boolean isHeightDependent() {
         return false;
      }

      @Override
      public int getColorMultiplier() {
         return this.defaultColor;
      }

      @Override
      public IColorMap copy() {
         return new ColorMap.TempHumidity(this.resource, this.map, this.width, this.height, this.defaultColor);
      }

      @Override
      protected void computeXY(BiomeGenBase biome, int i, int j, int k, float[] f) {
         float temperature = ColorUtils.clamp(BiomeAPI.getTemperature(biome, i, j, k));
         float rainfall = ColorUtils.clamp(BiomeAPI.getRainfall(biome, i, j, k));
         f[0] = this.maxX * (1.0F - temperature);
         f[1] = this.maxY * (1.0F - temperature * rainfall);
      }
   }

   @Environment(EnvType.CLIENT)
   public abstract static class Vanilla implements IColorMap {
      protected final int defaultColor;
      protected final float[] lastColor = new float[3];

      Vanilla(BufferedImage image) {
         this(image.getRGB(127, 127));
      }

      Vanilla(int defaultColor) {
         this.defaultColor = defaultColor & 16777215;
      }

      @Override
      public String toString() {
         return String.format("%s{%06x}", this.getClass().getSimpleName(), this.defaultColor);
      }

      @Override
      public final boolean isHeightDependent() {
         return false;
      }

      @Override
      public final int getColorMultiplier() {
         return this.defaultColor;
      }

      @Override
      public final int getColorMultiplier(IBlockAccess blockAccess, int i, int j, int k) {
         return this.getColorMultiplier(BiomeAPI.getBiomeGenAt(blockAccess, i, j, k), i, j, k);
      }

      @Override
      public final float[] getColorMultiplierF(IBlockAccess blockAccess, int i, int j, int k) {
         ColorUtils.intToFloat3(this.getColorMultiplier(blockAccess, i, j, k), this.lastColor);
         return this.lastColor;
      }

      @Override
      public final void claimResources(Collection<FakeResourceLocation> resources) {
      }

      abstract int getColorMultiplier(BiomeGenBase var1, int var2, int var3, int var4);
   }

   @Environment(EnvType.CLIENT)
   public static final class Water implements IColorMap {
      private final float[] lastColor = new float[3];

      @Override
      public String toString() {
         return String.format("Water{%06x}", this.getColorMultiplier());
      }

      @Override
      public boolean isHeightDependent() {
         return false;
      }

      @Override
      public int getColorMultiplier() {
         return BiomeAPI.getWaterColorMultiplier(BiomeAPI.findBiomeByName("Ocean"));
      }

      @Override
      public int getColorMultiplier(IBlockAccess blockAccess, int i, int j, int k) {
         return BiomeAPI.getWaterColorMultiplier(BiomeAPI.getBiomeGenAt(blockAccess, i, j, k));
      }

      @Override
      public float[] getColorMultiplierF(IBlockAccess blockAccess, int i, int j, int k) {
         ColorUtils.intToFloat3(this.getColorMultiplier(blockAccess, i, j, k), this.lastColor);
         return this.lastColor;
      }

      @Override
      public void claimResources(Collection<FakeResourceLocation> resources) {
      }

      @Override
      public IColorMap copy() {
         return new ColorMap.Water();
      }
   }
}
