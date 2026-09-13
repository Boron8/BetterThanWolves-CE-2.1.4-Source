package net.minecraft.src;

import argo.jdom.JdomParser;
import argo.jdom.JsonNode;
import argo.jdom.JsonNodeFactories;
import argo.jdom.JsonRootNode;
import argo.jdom.JsonStringNode;
import argo.saj.InvalidSyntaxException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;

@Environment(EnvType.CLIENT)
public class ThreadDownloadResources extends Thread {
   private static JdomParser parser = new JdomParser();
   public File resourcesFolder;
   private Minecraft mc;
   private boolean closing = false;

   public ThreadDownloadResources(File file, Minecraft mc) {
      this.mc = mc;
      this.setName("Resource download thread");
      this.setDaemon(true);
      this.resourcesFolder = new File(file, "resources/");
      if (!this.resourcesFolder.exists() && !this.resourcesFolder.mkdirs()) {
         throw new RuntimeException("The working directory could not be created: " + this.resourcesFolder);
      }
   }

   public JsonRootNode fetchJson(URL url) throws IOException {
      HttpURLConnection connection = null;
      connection = (HttpURLConnection)url.openConnection();
      connection.setDoInput(true);
      connection.setDoOutput(false);
      connection.connect();
      JsonRootNode json = null;
      if (connection.getResponseCode() / 100 == 4) {
         return null;
      } else {
         try {
            json = parser.parse(new InputStreamReader(connection.getInputStream()));
         } catch (InvalidSyntaxException var8) {
            var8.printStackTrace();
         } finally {
            connection.disconnect();
         }

         return json;
      }
   }

   @Override
   public void run() {
      try {
         URL versionManifestURL = new URL("https://launchermeta.mojang.com/mc/game/version_manifest.json");
         URL versionURL = null;
         URL assetIndexURL = null;
         URL assetURL = new URL("https://resources.download.minecraft.net/");
         JsonRootNode versionManifest = this.fetchJson(versionManifestURL);
         List<JsonNode> versions = versionManifest.e(new Object[]{"versions"});

         for (int index = 0; index < versions.size(); index++) {
            JsonNode versionMeta = versions.get(index);
            if (versionMeta.getStringValue("id").equalsIgnoreCase("1.5.2")) {
               versionURL = new URL(versionMeta.getStringValue("url"));
            }
         }

         JsonNode version = this.fetchJson(versionURL);
         assetIndexURL = new URL(version.getStringValue("assetIndex", "url"));
         JsonRootNode assetIndex = this.fetchJson(assetIndexURL);
         JsonNode objects = (JsonNode)assetIndex.d().get(JsonNodeFactories.aJsonString("objects"));
         Iterator<JsonStringNode> iterator = objects.getFields().keySet().iterator();

         while (iterator.hasNext()) {
            String path = iterator.next().getText();
            String hash = objects.getStringValue(path, "hash");
            long size = Integer.parseInt(objects.getNumberValue(path, "size"));
            if (size > 0L) {
               this.downloadAndInstallResource(assetURL, path, hash, size);
               if (this.closing) {
                  return;
               }
            }
         }
      } catch (Exception var15) {
         this.loadResource(this.resourcesFolder, "");
         var15.printStackTrace();
      }
   }

   public void reloadResources() {
      this.loadResource(this.resourcesFolder, "");
   }

   private void loadResource(File file, String path) {
      File[] files = file.listFiles();

      for (int fileIndex = 0; fileIndex < files.length; fileIndex++) {
         if (files[fileIndex].isDirectory()) {
            this.loadResource(files[fileIndex], path + files[fileIndex].getName() + "/");
         } else {
            try {
               this.mc.installResource(path + files[fileIndex].getName(), files[fileIndex]);
            } catch (Exception var6) {
               this.mc.getLogAgent().logWarning("Failed to add " + path + files[fileIndex].getName() + " in resources");
            }
         }
      }
   }

   private void downloadAndInstallResource(URL url, String path, String hash, long fileSize) {
      try {
         File file = new File(this.resourcesFolder, path);
         if (!file.exists() || file.length() != fileSize) {
            file.getParentFile().mkdirs();
            String assetHash = hash.substring(0, 2) + "/" + hash;
            this.downloadResource(new URL(url, assetHash), file);
            if (this.closing) {
               return;
            }
         }

         if (path.indexOf("/") >= 0) {
            this.mc.installResource(path, file);
         }
      } catch (Exception var8) {
         var8.printStackTrace();
      }
   }

   private void downloadResource(URL url, File file) throws IOException {
      byte[] buffer = new byte[4096];
      DataInputStream inputStream = new DataInputStream(url.openStream());
      DataOutputStream outputStream = new DataOutputStream(new FileOutputStream(file));

      int data;
      while ((data = inputStream.read(buffer)) >= 0) {
         outputStream.write(buffer, 0, data);
         if (this.closing) {
            return;
         }
      }

      inputStream.close();
      outputStream.close();
   }

   public void closeMinecraft() {
      this.closing = true;
   }
}
