package com.prupe.mcpatcher.mal.resource;

import com.prupe.mcpatcher.MCLogger;
import com.prupe.mcpatcher.MCPatcherUtils;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.WeakHashMap;
import java.util.logging.Level;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.ITexturePack;
import net.minecraft.src.TexturePackCustom;
import net.minecraft.src.TexturePackDefault;
import net.minecraft.src.TexturePackImplementation;

@Environment(EnvType.CLIENT)
public class ResourceList {
   private static final MCLogger logger = MCLogger.getLogger("Texture Pack");
   private static ResourceList instance;
   private static final Map<ITexturePack, Integer> resourcePackOrder = new WeakHashMap<>();
   private final ITexturePack resourcePack;
   private final Set<ResourceLocationWithSource> allResources = new TreeSet<>(new ResourceLocationWithSource.Comparator1());

   public static ResourceList getInstance() {
      if (instance == null) {
         List<ITexturePack> resourcePacks = TexturePackAPI.getResourcePacks(null);
         int order = resourcePacks.size();
         resourcePackOrder.clear();

         for (ITexturePack resourcePack : resourcePacks) {
            resourcePackOrder.put(resourcePack, order);
            order--;
         }

         instance = new ResourceList();
      }

      return instance;
   }

   public static void clearInstance() {
      instance = null;
   }

   public static int getResourcePackOrder(ITexturePack resourcePack) {
      Integer i = resourcePackOrder.get(resourcePack);
      return i == null ? Integer.MAX_VALUE : i;
   }

   private ResourceList() {
      this.resourcePack = null;

      for (ITexturePack resourcePack : TexturePackAPI.getResourcePacks(null)) {
         ResourceList sublist;
         if (resourcePack instanceof TexturePackCustom) {
            sublist = new ResourceList((TexturePackCustom)resourcePack);
         } else if (resourcePack instanceof TexturePackDefault) {
            sublist = new ResourceList((TexturePackDefault)resourcePack);
         } else {
            if (!(resourcePack instanceof TexturePackImplementation)) {
               continue;
            }

            sublist = new ResourceList((TexturePackImplementation)resourcePack);
         }

         this.allResources.removeAll(sublist.allResources);
         this.allResources.addAll(sublist.allResources);
      }

      logger.fine("new %s", this);
      if (logger.isLoggable(Level.FINEST)) {
         for (ResourceLocationWithSource resource : this.allResources) {
            logger.finest("%s -> %s", resource, resource.getSource().getTexturePackFileName());
         }
      }
   }

   private ResourceList(TexturePackCustom resourcePack) {
      this.resourcePack = resourcePack;
      this.scanZipFile(resourcePack.texturePackZipFile);
      logger.fine("new %s", this);
   }

   private ResourceList(TexturePackDefault resourcePack) {
      this.resourcePack = resourcePack;
      String version = MCPatcherUtils.getMinecraftVersion();
      File jar = MCPatcherUtils.getMinecraftPath("versions", version, version + ".jar");
      if (jar.isFile()) {
         ZipFile zipFile = null;

         try {
            zipFile = new ZipFile(jar);
            this.scanZipFile(zipFile);
         } catch (Throwable var9) {
            var9.printStackTrace();
         } finally {
            MCPatcherUtils.close(zipFile);
         }
      }

      if (!this.allResources.isEmpty()) {
         logger.fine("new %s", this);
      }
   }

   private ResourceList(TexturePackImplementation resourcePack) {
      this.resourcePack = resourcePack;
      File directory = resourcePack.texturePackFile;
      if (directory != null && directory.isDirectory()) {
         Set<String> allFiles = new HashSet<>();
         listAllFiles(directory, "", allFiles);

         for (String path : allFiles) {
            FakeResourceLocation resource = TexturePackAPI.parsePath(path);
            if (resource != null) {
               File file = new File(directory, path);
               this.addResource(resource, file.isFile(), file.isDirectory());
            }
         }

         logger.fine("new %s", this);
      }
   }

   private void scanZipFile(ZipFile zipFile) {
      if (zipFile != null) {
         for (ZipEntry entry : Collections.list(zipFile.entries())) {
            String path = entry.getName();
            FakeResourceLocation resource = TexturePackAPI.parsePath(path);
            if (resource != null) {
               this.addResource(resource, !entry.isDirectory(), entry.isDirectory());
            }
         }
      }
   }

   private static void listAllFiles(File base, String subdir, Set<String> files) {
      File[] entries = new File(base, subdir).listFiles();
      if (entries != null) {
         for (File file : entries) {
            String newPath = subdir + file.getName();
            if (files.add(newPath) && file.isDirectory()) {
               listAllFiles(base, subdir + file.getName() + '/', files);
            }
         }
      }
   }

   private void addResource(FakeResourceLocation resource, boolean isFile, boolean isDirectory) {
      if (isFile) {
         this.allResources.add(new ResourceLocationWithSource(this.resourcePack, resource));
      } else if (isDirectory) {
         if (!resource.getPath().endsWith("/")) {
            resource = new FakeResourceLocation(resource.getNamespace(), resource.getPath() + '/');
         }

         this.allResources.add(new ResourceLocationWithSource(this.resourcePack, resource));
      }
   }

   public List<FakeResourceLocation> listResources(String directory, String suffix, boolean sortByFilename) {
      return this.listResources(directory, suffix, true, false, sortByFilename);
   }

   public List<FakeResourceLocation> listResources(String directory, String suffix, boolean recursive, boolean directories, boolean sortByFilename) {
      return this.listResources(null, directory, suffix, recursive, directories, sortByFilename);
   }

   public List<FakeResourceLocation> listResources(
      String namespace, String directory, String suffix, boolean recursive, boolean directories, boolean sortByFilename
   ) {
      if (suffix == null) {
         suffix = "";
      }

      if (MCPatcherUtils.isNullOrEmpty(directory)) {
         directory = "";
      } else if (!directory.endsWith("/")) {
         directory = directory + '/';
      }

      Set<ResourceLocationWithSource> tmpList = new TreeSet<>(new ResourceLocationWithSource.Comparator1(true, sortByFilename ? suffix : null));
      boolean allNamespaces = MCPatcherUtils.isNullOrEmpty(namespace);

      for (ResourceLocationWithSource resource : this.allResources) {
         if (directories == resource.isDirectory() && (allNamespaces || namespace.equals(resource.getNamespace()))) {
            String path = resource.getPath();
            if (path.endsWith(suffix) && path.startsWith(directory)) {
               if (!recursive) {
                  String subpath = path.substring(directory.length());
                  if (subpath.contains("/")) {
                     continue;
                  }
               }

               tmpList.add(resource);
            }
         }
      }

      return new ArrayList<>(tmpList);
   }

   @Override
   public String toString() {
      StringBuilder sb = new StringBuilder("ResourceList: ");
      if (this.resourcePack == null) {
         sb.append("(combined) ");
      } else {
         sb.append(this.resourcePack.getTexturePackFileName()).append(' ');
      }

      int fileCount = 0;
      int directoryCount = 0;
      Set<String> namespaces = new HashSet<>();

      for (ResourceLocationWithSource resource : this.allResources) {
         if (resource.isDirectory()) {
            directoryCount++;
         } else {
            fileCount++;
         }

         namespaces.add(resource.getNamespace());
      }

      sb.append(fileCount).append(" files, ");
      sb.append(directoryCount).append(" directories in ");
      sb.append(namespaces.size()).append(" namespaces");
      return sb.toString();
   }
}
