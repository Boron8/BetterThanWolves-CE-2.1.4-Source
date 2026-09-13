package net.minecraft.src;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertyManager {
   private final Properties properties = new Properties();
   private final ILogAgent logger;
   private final File associatedFile;

   public PropertyManager(File var1, ILogAgent var2) {
      this.associatedFile = var1;
      this.logger = var2;
      if (var1.exists()) {
         FileInputStream var3 = null;

         try {
            var3 = new FileInputStream(var1);
            this.properties.load(var3);
         } catch (Exception var13) {
            var2.logWarningException("Failed to load " + var1, var13);
            this.logMessageAndSave();
         } finally {
            if (var3 != null) {
               try {
                  var3.close();
               } catch (IOException var12) {
               }
            }
         }
      } else {
         var2.logWarning(var1 + " does not exist");
         this.logMessageAndSave();
      }
   }

   public void logMessageAndSave() {
      this.logger.logInfo("Generating new properties file");
      this.saveProperties();
   }

   public void saveProperties() {
      FileOutputStream var1 = null;

      try {
         var1 = new FileOutputStream(this.associatedFile);
         this.properties.store(var1, "Minecraft server properties");
      } catch (Exception var11) {
         this.logger.logWarningException("Failed to save " + this.associatedFile, var11);
         this.logMessageAndSave();
      } finally {
         if (var1 != null) {
            try {
               var1.close();
            } catch (IOException var10) {
            }
         }
      }
   }

   public File getPropertiesFile() {
      return this.associatedFile;
   }

   public String getProperty(String var1, String var2) {
      if (!this.properties.containsKey(var1)) {
         this.properties.setProperty(var1, var2);
         this.saveProperties();
      }

      return this.properties.getProperty(var1, var2);
   }

   public int getIntProperty(String var1, int var2) {
      try {
         return Integer.parseInt(this.getProperty(var1, "" + var2));
      } catch (Exception var4) {
         this.properties.setProperty(var1, "" + var2);
         return var2;
      }
   }

   public boolean getBooleanProperty(String var1, boolean var2) {
      try {
         return Boolean.parseBoolean(this.getProperty(var1, "" + var2));
      } catch (Exception var4) {
         this.properties.setProperty(var1, "" + var2);
         return var2;
      }
   }

   public void setProperty(String var1, Object var2) {
      this.properties.setProperty(var1, "" + var2);
   }
}
