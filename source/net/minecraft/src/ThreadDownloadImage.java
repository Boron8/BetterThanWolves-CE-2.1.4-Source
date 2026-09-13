package net.minecraft.src;

import argo.jdom.JdomParser;
import argo.jdom.JsonRootNode;
import java.io.File;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
class ThreadDownloadImage extends Thread {
   static final Map<String, String> uuids = new HashMap<>();
   final String location;
   final IImageBuffer buffer;
   final ThreadDownloadImageData imageData;

   ThreadDownloadImage(ThreadDownloadImageData par1, String par2Str, IImageBuffer par3IImageBuffer) {
      this.imageData = par1;
      this.location = par2Str;
      this.buffer = par3IImageBuffer;
   }

   private String fetchUuid(String userName) {
      if (uuids.containsKey(userName)) {
         return uuids.get(userName);
      } else {
         HttpURLConnection profileConn = null;
         String id = null;

         Object json;
         try {
            URL profileUrl = new URL("https://api.mojang.com/users/profiles/minecraft/" + userName);
            profileConn = (HttpURLConnection)profileUrl.openConnection();
            profileConn.setDoInput(true);
            profileConn.setDoOutput(false);
            profileConn.connect();
            if (profileConn.getResponseCode() / 100 != 4) {
               JsonRootNode jsonx = new JdomParser().parse(new InputStreamReader(profileConn.getInputStream()));
               String name = jsonx.b(new Object[]{"name"});
               id = jsonx.b(new Object[]{"id"});
               if (userName.equals(name)) {
                  uuids.put(userName, id);
               } else {
                  id = null;
               }

               return id;
            }

            json = null;
         } catch (Exception var10) {
            var10.printStackTrace();
            return id;
         } finally {
            if (profileConn != null) {
               profileConn.disconnect();
            }
         }

         return (String)json;
      }
   }

   @Override
   public void run() {
      HttpURLConnection var1 = null;
      String urlLocation = this.location;

      try {
         if (this.location.startsWith("http://skins.minecraft.net/")) {
            String userName = new File(this.location, "").getName().replaceFirst("[.][^.]+$", "");
            String uuid = this.fetchUuid(userName);
            if (this.location.startsWith("http://skins.minecraft.net/MinecraftSkins")) {
               urlLocation = "http://crafatar.com/skins/" + uuid;
            }

            if (this.location.startsWith("http://skins.minecraft.net/MinecraftCloaks")) {
               urlLocation = "http://crafatar.com/capes/" + uuid;
            }
         }

         URL var2 = new URL(urlLocation);
         var1 = (HttpURLConnection)var2.openConnection();
         var1.setDoInput(true);
         var1.setDoOutput(false);
         var1.connect();
         if (var1.getResponseCode() / 100 != 4) {
            if (this.buffer == null) {
               this.imageData.image = ImageIO.read(var1.getInputStream());
            } else {
               this.imageData.image = this.buffer.parseUserSkin(ImageIO.read(var1.getInputStream()));
            }

            return;
         }
      } catch (Exception var8) {
         var8.printStackTrace();
         return;
      } finally {
         var1.disconnect();
      }
   }
}
