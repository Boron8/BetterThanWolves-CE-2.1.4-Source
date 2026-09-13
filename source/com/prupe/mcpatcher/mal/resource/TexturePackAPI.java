package com.prupe.mcpatcher.mal.resource;

import com.prupe.mcpatcher.MCLogger;
import com.prupe.mcpatcher.MCPatcherUtils;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.src.ITexturePack;
import net.minecraft.src.RenderEngine;
import net.minecraft.src.TexturePackDefault;
import net.minecraft.src.TexturePackList;

@Environment(EnvType.CLIENT)
public class TexturePackAPI {
   private static final MCLogger logger = MCLogger.getLogger("Texture Pack");
   public static final String DEFAULT_NAMESPACE = "minecraft";
   private static final TexturePackAPI instance = new TexturePackAPI();
   public static final String MCPATCHER_SUBDIR = "/";
   public static final FakeResourceLocation ITEMS_PNG = new FakeResourceLocation("/gui/items.png");
   public static final FakeResourceLocation BLOCKS_PNG = new FakeResourceLocation("terrain.png");
   private final List<Field> textureMapFields = new ArrayList<>();

   public static boolean isInitialized() {
      return instance != null && Minecraft.getMinecraft().texturePackList != null;
   }

   public static void scheduleTexturePackRefresh() {
      Minecraft.getMinecraft().scheduleTexturePackRefresh();
   }

   public static List<ITexturePack> getResourcePacks(String namespace) {
      List<ITexturePack> resourcePacks = new ArrayList<>();
      ITexturePack resourcePack = instance.getTexturePack();
      if (resourcePack != null) {
         resourcePacks.add(resourcePack);
      }

      return resourcePacks;
   }

   public static Set<String> getNamespaces() {
      Set<String> set = new HashSet<>();
      set.add("minecraft");
      return set;
   }

   public static boolean isDefaultTexturePack() {
      ITexturePack texturePack = instance.getTexturePack();
      return texturePack == null || texturePack instanceof TexturePackDefault;
   }

   public static InputStream getInputStream(FakeResourceLocation resource) {
      if (resource == null) {
         return null;
      } else {
         ITexturePack resourcePack;
         if (resource instanceof ResourceLocationWithSource) {
            resourcePack = ((ResourceLocationWithSource)resource).getSource();
         } else {
            resourcePack = instance.getTexturePack();
         }

         try {
            return resourcePack == null ? null : resourcePack.getResourceAsStream(resource.getPath());
         } catch (IOException var3) {
            return null;
         }
      }
   }

   public static boolean hasResource(FakeResourceLocation resource) {
      if (resource == null) {
         return false;
      } else if (resource.getPath().endsWith(".png")) {
         return getImage(resource) != null;
      } else if (resource.getPath().endsWith(".properties")) {
         return getProperties(resource) != null;
      } else {
         InputStream is = getInputStream(resource);
         MCPatcherUtils.close(is);
         return is != null;
      }
   }

   public static boolean hasCustomResource(FakeResourceLocation resource) {
      InputStream jar = null;
      InputStream pack = null;

      try {
         String path = resource.getPath();
         pack = getInputStream(resource);
         if (pack == null) {
            return false;
         } else {
            jar = Minecraft.class.getResourceAsStream(path);
            if (jar == null) {
               return true;
            } else {
               byte[] buffer1 = new byte[4096];
               byte[] buffer2 = new byte[4096];

               int read1;
               while ((read1 = pack.read(buffer1)) > 0) {
                  int read2 = jar.read(buffer2);
                  if (read1 != read2) {
                     return true;
                  }

                  for (int i = 0; i < read1; i++) {
                     if (buffer1[i] != buffer2[i]) {
                        return true;
                     }
                  }
               }

               return false;
            }
         }
      } catch (IOException var13) {
         var13.printStackTrace();
         return false;
      } finally {
         MCPatcherUtils.close(jar);
         MCPatcherUtils.close(pack);
      }
   }

   public static BufferedImage getImage(FakeResourceLocation resource) {
      if (resource == null) {
         return null;
      } else {
         InputStream input = getInputStream(resource);
         BufferedImage image = null;
         if (input != null) {
            try {
               image = ImageIO.read(input);
            } catch (IOException var7) {
               logger.error("could not read %s", resource);
               var7.printStackTrace();
            } finally {
               MCPatcherUtils.close(input);
            }
         }

         return image;
      }
   }

   public static Properties getProperties(FakeResourceLocation resource) {
      Properties properties = new Properties();
      return getProperties(resource, properties) ? properties : null;
   }

   public static boolean getProperties(FakeResourceLocation resource, Properties properties) {
      if (properties != null) {
         InputStream input = getInputStream(resource);

         boolean e;
         try {
            if (input == null) {
               return false;
            }

            properties.load(input);
            e = true;
         } catch (IOException var7) {
            logger.error("could not read %s", resource);
            var7.printStackTrace();
            return false;
         } finally {
            MCPatcherUtils.close(input);
         }

         return e;
      } else {
         return false;
      }
   }

   public static FakeResourceLocation transformResourceLocation(FakeResourceLocation resource, String oldExt, String newExt) {
      return new FakeResourceLocation(resource.getNamespace(), resource.getPath().replaceFirst(Pattern.quote(oldExt) + "$", newExt));
   }

   public static FakeResourceLocation parsePath(String path) {
      return MCPatcherUtils.isNullOrEmpty(path) ? null : new FakeResourceLocation(path.replace(File.separatorChar, '/'));
   }

   public static FakeResourceLocation parseResourceLocation(String path) {
      return parseResourceLocation(new FakeResourceLocation("minecraft", "a"), path);
   }

   public static FakeResourceLocation parseResourceLocation(FakeResourceLocation baseResource, String path) {
      if (MCPatcherUtils.isNullOrEmpty(path)) {
         return null;
      } else {
         if (path.startsWith("~/")) {
            path = path.substring(1);
         }

         if (path.startsWith("./")) {
            return new FakeResourceLocation(baseResource.getNamespace(), baseResource.getPath().replaceFirst("[^/]+$", "") + path.substring(2));
         } else {
            return path.startsWith("/")
               ? new FakeResourceLocation(path)
               : new FakeResourceLocation(baseResource.getNamespace(), baseResource.getPath().replaceFirst("[^/]+$", "") + path);
         }
      }
   }

   public static FakeResourceLocation newMCPatcherResourceLocation(String path) {
      return new FakeResourceLocation("/" + path.replaceFirst("^/+", ""));
   }

   public static int getTextureIfLoaded(FakeResourceLocation resource) {
      if (resource == null) {
         return -1;
      } else {
         RenderEngine renderEngine = Minecraft.getMinecraft().renderEngine;
         String path = resource.getPath();
         if (!path.equals("/terrain.png") && !path.equals("/gui/items.png")) {
            for (Field field : instance.textureMapFields) {
               try {
                  HashMap map = (HashMap)field.get(renderEngine);
                  if (map != null) {
                     Object value = map.get(resource.toString());
                     if (value instanceof Integer) {
                        return (Integer)value;
                     }
                  }
               } catch (IllegalAccessException var7) {
               }
            }

            return -1;
         } else {
            return renderEngine.getTexture(path);
         }
      }
   }

   public static boolean isTextureLoaded(FakeResourceLocation resource) {
      return getTextureIfLoaded(resource) >= 0;
   }

   public static void bindTexture(FakeResourceLocation resource) {
      if (resource != null) {
         Minecraft.getMinecraft().renderEngine.bindTexture(resource.toString());
      }
   }

   public static void unloadTexture(FakeResourceLocation resource) {
      if (resource != null) {
         int texture = getTextureIfLoaded(resource);
         if (texture >= 0) {
            logger.finest("unloading texture %s", resource);
            RenderEngine renderEngine = Minecraft.getMinecraft().renderEngine;
            renderEngine.deleteTexture(texture);

            for (Field field : instance.textureMapFields) {
               try {
                  HashMap map = (HashMap)field.get(renderEngine);
                  if (map != null) {
                     map.remove(resource.toString());
                  }
               } catch (IllegalAccessException var6) {
               }
            }
         }
      }
   }

   public static void flushUnusedTextures() {
   }

   private TexturePackAPI() {
      try {
         for (Field field : RenderEngine.class.getDeclaredFields()) {
            if (HashMap.class.isAssignableFrom(field.getType())) {
               field.setAccessible(true);
               this.textureMapFields.add(field);
            }
         }
      } catch (Throwable var5) {
         var5.printStackTrace();
      }
   }

   private ITexturePack getTexturePack() {
      Minecraft minecraft = Minecraft.getMinecraft();
      if (minecraft == null) {
         return null;
      } else {
         TexturePackList texturePackList = minecraft.texturePackList;
         return texturePackList == null ? null : texturePackList.getSelectedTexturePack();
      }
   }
}
