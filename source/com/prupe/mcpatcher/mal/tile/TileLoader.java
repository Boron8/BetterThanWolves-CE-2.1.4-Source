package com.prupe.mcpatcher.mal.tile;

import com.prupe.mcpatcher.Config;
import com.prupe.mcpatcher.MCLogger;
import com.prupe.mcpatcher.MCPatcherUtils;
import com.prupe.mcpatcher.mal.resource.BlendMethod;
import com.prupe.mcpatcher.mal.resource.FakeResourceLocation;
import com.prupe.mcpatcher.mal.resource.TexturePackAPI;
import com.prupe.mcpatcher.mal.resource.TexturePackChangeHandler;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.src.Icon;
import net.minecraft.src.TextureMap;
import net.minecraft.src.TextureStitched;

@Environment(EnvType.CLIENT)
public class TileLoader {
   private static final MCLogger logger = MCLogger.getLogger("Tilesheet");
   private static final List<TileLoader> loaders = new ArrayList<>();
   private static final boolean debugTextures = Config.getBoolean("Connected Textures", "debugTextures", false);
   private static final Map<String, String> specialTextures = new HashMap<>();
   private static final TexturePackChangeHandler changeHandler;
   private static boolean changeHandlerCalled;
   private static boolean useFullPath;
   private static final long MAX_TILESHEET_SIZE;
   protected final String mapName;
   protected final MCLogger subLogger;
   private TextureMap baseTextureMap;
   private final Map<String, TextureStitched> baseTexturesByName = new HashMap<>();
   private final Set<FakeResourceLocation> tilesToRegister = new HashSet<>();
   private final Map<FakeResourceLocation, BufferedImage> tileImages = new HashMap<>();
   private final Map<String, Icon> iconMap = new HashMap<>();

   public static void registerIcons(TextureMap textureMap, String mapName, Map<String, TextureStitched> map) {
      mapName = mapName.replaceFirst("/$", "");
      logger.fine("before registerIcons(%s) %d icons", mapName, map.size());
      if (!changeHandlerCalled) {
         logger.severe("beforeChange was not called, invoking directly");
         changeHandler.beforeChange();
      }

      for (TileLoader loader : loaders) {
         if (loader.isForThisMap(mapName)) {
            if (loader.baseTextureMap == null) {
               loader.baseTextureMap = textureMap;
               loader.baseTexturesByName.putAll(map);
            }

            if (!loader.tilesToRegister.isEmpty()) {
               loader.subLogger.fine("adding icons to %s (%d remaining)", mapName, loader.tilesToRegister.size(), mapName);

               while (!loader.tilesToRegister.isEmpty() && loader.registerOneIcon(textureMap, mapName, map)) {
               }

               loader.subLogger.fine("done adding icons to %s (%d remaining)", mapName, loader.tilesToRegister.size(), mapName);
            }
         }
      }

      logger.fine("after registerIcons(%s) %d icons", mapName, map.size());
   }

   public static String getOverridePath(String prefix, String basePath, String name, String ext) {
      String path;
      if (name.endsWith(".png")) {
         path = name.replaceFirst("^/", "").replaceFirst("\\.[^.]+$", "") + ext;
         useFullPath = true;
      } else {
         path = basePath;
         if (!basePath.endsWith("/")) {
            path = basePath + "/";
         }

         path = path + name;
         path = path + ext;
         useFullPath = false;
      }

      path = prefix + path;
      logger.finer("getOverridePath(%s, %s, %s, %s) -> %s", prefix, basePath, name, ext, path);
      return path;
   }

   public static String getOverrideBasename(Object o, String path) {
      if (useFullPath) {
         useFullPath = false;
         return "/" + path;
      } else {
         File file = new File(path);
         return file.getName().substring(0, file.getName().lastIndexOf(46));
      }
   }

   public static boolean isSpecialTexture(TextureMap map, String texture, String special) {
      return special.equals(texture) || special.equals(specialTextures.get(texture));
   }

   public static BufferedImage generateDebugTexture(String text, int width, int height, boolean alternate) {
      BufferedImage image = new BufferedImage(width, height, 2);
      Graphics graphics = image.getGraphics();
      graphics.setColor(alternate ? new Color(0, 255, 255, 128) : Color.WHITE);
      graphics.fillRect(0, 0, width, height);
      graphics.setColor(alternate ? Color.RED : Color.BLACK);
      int ypos = 10;
      if (alternate) {
         ypos += height / 2;
      }

      int charsPerRow = width / 8;
      if (charsPerRow <= 0) {
         return image;
      } else {
         while (text.length() % charsPerRow != 0) {
            text = text + " ";
         }

         while (ypos < height && !text.equals("")) {
            graphics.drawString(text.substring(0, charsPerRow), 1, ypos);
            ypos += graphics.getFont().getSize();
            text = text.substring(charsPerRow);
         }

         return image;
      }
   }

   static void init() {
   }

   public TileLoader(String mapName, MCLogger logger) {
      this.mapName = mapName;
      this.subLogger = logger;
      loaders.add(this);
   }

   private static long getTextureSize(TextureStitched texture) {
      return texture == null ? 0L : 4 * IconAPI.getIconWidth(texture) * IconAPI.getIconHeight(texture);
   }

   private static long getTextureSize(Collection<TextureStitched> textures) {
      long size = 0L;

      for (TextureStitched texture : textures) {
         size += getTextureSize(texture);
      }

      return size;
   }

   public static FakeResourceLocation getDefaultAddress(FakeResourceLocation propertiesAddress) {
      return TexturePackAPI.transformResourceLocation(propertiesAddress, ".properties", ".png");
   }

   public static FakeResourceLocation parseTileAddress(FakeResourceLocation propertiesAddress, String value) {
      return parseTileAddress(propertiesAddress, value, BlendMethod.ALPHA.getBlankResource());
   }

   public static FakeResourceLocation parseTileAddress(FakeResourceLocation propertiesAddress, String value, FakeResourceLocation blankResource) {
      if (value == null) {
         return null;
      } else if (value.equals("blank")) {
         return blankResource;
      } else if (!value.equals("null") && !value.equals("none") && !value.equals("default") && !value.equals("")) {
         if (!value.endsWith(".png")) {
            value = value + ".png";
         }

         return TexturePackAPI.parseResourceLocation(propertiesAddress, value);
      } else {
         return null;
      }
   }

   public boolean preloadTile(FakeResourceLocation resource, boolean alternate, String special) {
      if (this.tileImages.containsKey(resource)) {
         return true;
      } else {
         BufferedImage image = null;
         if (!debugTextures) {
            image = TexturePackAPI.getImage(resource);
            if (image == null) {
               this.subLogger.warning("missing %s", resource);
            }
         }

         if (image == null) {
            image = generateDebugTexture(resource.getPath(), 64, 64, alternate);
         }

         this.tilesToRegister.add(resource);
         this.tileImages.put(resource, image);
         if (special != null) {
            specialTextures.put(resource.toString(), special);
         }

         return true;
      }
   }

   public boolean preloadTile(FakeResourceLocation resource, boolean alternate) {
      return this.preloadTile(resource, alternate, null);
   }

   protected boolean isForThisMap(String mapName) {
      return mapName.equals("textures") || mapName.startsWith(this.mapName);
   }

   private boolean registerDefaultIcon(String name) {
      if (name.startsWith(this.mapName) && name.endsWith(".png") && this.baseTextureMap != null) {
         String defaultName = name.substring(this.mapName.length()).replaceFirst("\\.png$", "");
         TextureStitched texture = this.baseTexturesByName.get(defaultName);
         if (texture != null) {
            this.subLogger.finer("%s -> existing icon %s", name, defaultName);
            this.iconMap.put(name, texture);
            return true;
         }
      }

      return false;
   }

   private boolean registerOneIcon(TextureMap textureMap, String mapName, Map<String, TextureStitched> map) {
      FakeResourceLocation resource = this.tilesToRegister.iterator().next();
      String name = resource.toString();
      if (this.registerDefaultIcon(name)) {
         this.tilesToRegister.remove(resource);
         return true;
      } else {
         BufferedImage image = this.tileImages.get(resource);
         if (image == null) {
            this.subLogger.error("tile for %s unexpectedly missing", resource);
            this.tilesToRegister.remove(resource);
            return true;
         } else {
            int width = image.getWidth();
            int height = image.getHeight();
            Icon icon = textureMap.registerIcon(name);
            map.put(name, (TextureStitched)icon);
            this.iconMap.put(name, icon);
            String extra = width == height ? "" : ", " + height / width + " frames";
            this.subLogger.finer("%s -> %s icon %dx%d%s", name, mapName, width, width, extra);
            this.tilesToRegister.remove(resource);
            return true;
         }
      }
   }

   public void finish() {
      this.tilesToRegister.clear();
      this.tileImages.clear();
   }

   public Icon getIcon(String name) {
      if (MCPatcherUtils.isNullOrEmpty(name)) {
         return null;
      } else {
         Icon icon = this.iconMap.get(name);
         if (icon == null) {
            icon = this.baseTexturesByName.get(name);
         }

         return icon;
      }
   }

   public Icon getIcon(FakeResourceLocation resource) {
      return resource == null ? null : this.getIcon(resource.toString());
   }

   static {
      long maxSize = 4096L;

      try {
         maxSize = Minecraft.getGLMaximumTextureSize();
      } catch (NoSuchMethodError var3) {
      } catch (Throwable var4) {
         var4.printStackTrace();
      }

      MAX_TILESHEET_SIZE = maxSize * maxSize * 4L * 7L / 8L;
      logger.config("max texture size is %dx%d (%.1fMB)", maxSize, maxSize, (float)MAX_TILESHEET_SIZE / 1048576.0F);
      changeHandler = new TexturePackChangeHandler("Tilesheet API", 2) {
         @Override
         public void initialize() {
         }

         @Override
         public void beforeChange() {
            TileLoader.changeHandlerCalled = true;
            TileLoader.loaders.clear();
            TileLoader.specialTextures.clear();
         }

         @Override
         public void afterChange() {
            for (TileLoader loader : TileLoader.loaders) {
               if (!loader.tilesToRegister.isEmpty()) {
                  loader.subLogger.warning("could not load all %s tiles (%d remaining)", loader.mapName, loader.tilesToRegister.size());
                  loader.tilesToRegister.clear();
               }
            }

            TileLoader.changeHandlerCalled = false;
         }

         @Override
         public void afterChange2() {
            for (TileLoader loader : TileLoader.loaders) {
               loader.finish();
            }
         }
      };
      TexturePackChangeHandler.register(changeHandler);
   }
}
