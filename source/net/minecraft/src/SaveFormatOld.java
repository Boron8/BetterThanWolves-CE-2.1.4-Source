package net.minecraft.src;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class SaveFormatOld implements ISaveFormat {
   protected final File savesDirectory;

   public SaveFormatOld(File var1) {
      if (!var1.exists()) {
         var1.mkdirs();
      }

      this.savesDirectory = var1;
   }

   @Override
   public List getSaveList() {
      ArrayList var1 = new ArrayList();

      for (int var2 = 0; var2 < 5; var2++) {
         String var3 = "World" + (var2 + 1);
         WorldInfo var4 = this.getWorldInfo(var3);
         if (var4 != null) {
            var1.add(
               new SaveFormatComparator(
                  var3, "", var4.getLastTimePlayed(), var4.getSizeOnDisk(), var4.getGameType(), false, var4.isHardcoreModeEnabled(), var4.areCommandsAllowed()
               )
            );
         }
      }

      return var1;
   }

   @Override
   public void flushCache() {
   }

   @Override
   public WorldInfo getWorldInfo(String var1) {
      File var2 = new File(this.savesDirectory, var1);
      if (!var2.exists()) {
         return null;
      } else {
         File var3 = new File(var2, "level.dat");
         if (var3.exists()) {
            try {
               NBTTagCompound var9 = CompressedStreamTools.readCompressed(new FileInputStream(var3));
               NBTTagCompound var10 = var9.getCompoundTag("Data");
               return new WorldInfo(var10);
            } catch (Exception var7) {
               var7.printStackTrace();
            }
         }

         var3 = new File(var2, "level.dat_old");
         if (var3.exists()) {
            try {
               NBTTagCompound var4 = CompressedStreamTools.readCompressed(new FileInputStream(var3));
               NBTTagCompound var5 = var4.getCompoundTag("Data");
               return new WorldInfo(var5);
            } catch (Exception var6) {
               var6.printStackTrace();
            }
         }

         return null;
      }
   }

   @Override
   public void renameWorld(String var1, String var2) {
      File var3 = new File(this.savesDirectory, var1);
      if (var3.exists()) {
         File var4 = new File(var3, "level.dat");
         if (var4.exists()) {
            try {
               NBTTagCompound var5 = CompressedStreamTools.readCompressed(new FileInputStream(var4));
               NBTTagCompound var6 = var5.getCompoundTag("Data");
               var6.setString("LevelName", var2);
               CompressedStreamTools.writeCompressed(var5, new FileOutputStream(var4));
            } catch (Exception var7) {
               var7.printStackTrace();
            }
         }
      }
   }

   @Override
   public boolean deleteWorldDirectory(String var1) {
      File var2 = new File(this.savesDirectory, var1);
      if (!var2.exists()) {
         return true;
      } else {
         System.out.println("Deleting level " + var1);

         for (int var3 = 1; var3 <= 5; var3++) {
            System.out.println("Attempt " + var3 + "...");
            if (deleteFiles(var2.listFiles())) {
               break;
            }

            System.out.println("Unsuccessful in deleting contents.");
            if (var3 < 5) {
               try {
                  Thread.sleep(500L);
               } catch (InterruptedException var5) {
               }
            }
         }

         return var2.delete();
      }
   }

   protected static boolean deleteFiles(File[] var0) {
      for (int var1 = 0; var1 < var0.length; var1++) {
         File var2 = var0[var1];
         System.out.println("Deleting " + var2);
         if (var2.isDirectory() && !deleteFiles(var2.listFiles())) {
            System.out.println("Couldn't delete directory " + var2);
            return false;
         }

         if (!var2.delete()) {
            System.out.println("Couldn't delete file " + var2);
            return false;
         }
      }

      return true;
   }

   @Override
   public ISaveHandler getSaveLoader(String var1, boolean var2) {
      return new SaveHandler(this.savesDirectory, var1, var2);
   }

   @Override
   public boolean isOldMapFormat(String var1) {
      return false;
   }

   @Override
   public boolean convertMapFormat(String var1, IProgressUpdate var2) {
      return false;
   }

   @Override
   public boolean canLoadWorld(String var1) {
      File var2 = new File(this.savesDirectory, var1);
      return var2.isDirectory();
   }
}
