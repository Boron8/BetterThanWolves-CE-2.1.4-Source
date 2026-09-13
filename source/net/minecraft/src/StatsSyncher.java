package net.minecraft.src;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Map;

public class StatsSyncher {
   private volatile boolean isBusy = false;
   private volatile Map field_77430_b = null;
   private volatile Map field_77431_c = null;
   private StatFileWriter statFileWriter;
   private File unsentDataFile;
   private File dataFile;
   private File unsentTempFile;
   private File tempFile;
   private File unsentOldFile;
   private File oldFile;
   private Session theSession;
   private int field_77433_l = 0;
   private int field_77434_m = 0;

   public StatsSyncher(Session var1, StatFileWriter var2, File var3) {
      this.unsentDataFile = new File(var3, "stats_" + var1.username.toLowerCase() + "_unsent.dat");
      this.dataFile = new File(var3, "stats_" + var1.username.toLowerCase() + ".dat");
      this.unsentOldFile = new File(var3, "stats_" + var1.username.toLowerCase() + "_unsent.old");
      this.oldFile = new File(var3, "stats_" + var1.username.toLowerCase() + ".old");
      this.unsentTempFile = new File(var3, "stats_" + var1.username.toLowerCase() + "_unsent.tmp");
      this.tempFile = new File(var3, "stats_" + var1.username.toLowerCase() + ".tmp");
      if (!var1.username.toLowerCase().equals(var1.username)) {
         this.func_77412_a(var3, "stats_" + var1.username + "_unsent.dat", this.unsentDataFile);
         this.func_77412_a(var3, "stats_" + var1.username + ".dat", this.dataFile);
         this.func_77412_a(var3, "stats_" + var1.username + "_unsent.old", this.unsentOldFile);
         this.func_77412_a(var3, "stats_" + var1.username + ".old", this.oldFile);
         this.func_77412_a(var3, "stats_" + var1.username + "_unsent.tmp", this.unsentTempFile);
         this.func_77412_a(var3, "stats_" + var1.username + ".tmp", this.tempFile);
      }

      this.statFileWriter = var2;
      this.theSession = var1;
      if (this.unsentDataFile.exists()) {
         var2.writeStats(this.func_77417_a(this.unsentDataFile, this.unsentTempFile, this.unsentOldFile));
      }

      this.beginReceiveStats();
   }

   private void func_77412_a(File var1, String var2, File var3) {
      File var4 = new File(var1, var2);
      if (var4.exists() && !var4.isDirectory() && !var3.exists()) {
         var4.renameTo(var3);
      }
   }

   private Map func_77417_a(File var1, File var2, File var3) {
      if (var1.exists()) {
         return this.func_77413_a(var1);
      } else if (var3.exists()) {
         return this.func_77413_a(var3);
      } else {
         return var2.exists() ? this.func_77413_a(var2) : null;
      }
   }

   private Map func_77413_a(File var1) {
      BufferedReader var2 = null;

      try {
         var2 = new BufferedReader(new FileReader(var1));
         String var3 = "";
         StringBuilder var4 = new StringBuilder();

         while ((var3 = var2.readLine()) != null) {
            var4.append(var3);
         }

         return StatFileWriter.func_77453_b(var4.toString());
      } catch (Exception var15) {
         var15.printStackTrace();
      } finally {
         if (var2 != null) {
            try {
               var2.close();
            } catch (Exception var14) {
               var14.printStackTrace();
            }
         }
      }

      return null;
   }

   private void func_77421_a(Map var1, File var2, File var3, File var4) {
      PrintWriter var5 = new PrintWriter(new FileWriter(var3, false));

      try {
         var5.print(StatFileWriter.func_77441_a(this.theSession.username, "local", var1));
      } finally {
         var5.close();
      }

      if (var4.exists()) {
         var4.delete();
      }

      if (var2.exists()) {
         var2.renameTo(var4);
      }

      var3.renameTo(var2);
   }

   public void beginReceiveStats() {
      if (this.isBusy) {
         throw new IllegalStateException("Can't get stats from server while StatsSyncher is busy!");
      } else {
         this.field_77433_l = 100;
         this.isBusy = true;
         new ThreadStatSyncherReceive(this).start();
      }
   }

   public void beginSendStats(Map var1) {
      if (this.isBusy) {
         throw new IllegalStateException("Can't save stats while StatsSyncher is busy!");
      } else {
         this.field_77433_l = 100;
         this.isBusy = true;
         new ThreadStatSyncherSend(this, var1).start();
      }
   }

   public void syncStatsFileWithMap(Map var1) {
      int var2 = 30;

      while (this.isBusy) {
         if (--var2 <= 0) {
            break;
         }

         try {
            Thread.sleep(100L);
         } catch (InterruptedException var10) {
            var10.printStackTrace();
         }
      }

      this.isBusy = true;

      try {
         this.func_77421_a(var1, this.unsentDataFile, this.unsentTempFile, this.unsentOldFile);
      } catch (Exception var8) {
         var8.printStackTrace();
      } finally {
         this.isBusy = false;
      }
   }

   public boolean func_77425_c() {
      return this.field_77433_l <= 0 && !this.isBusy && this.field_77431_c == null;
   }

   public void func_77422_e() {
      if (this.field_77433_l > 0) {
         this.field_77433_l--;
      }

      if (this.field_77434_m > 0) {
         this.field_77434_m--;
      }

      if (this.field_77431_c != null) {
         this.statFileWriter.func_77448_c(this.field_77431_c);
         this.field_77431_c = null;
      }

      if (this.field_77430_b != null) {
         this.statFileWriter.func_77452_b(this.field_77430_b);
         this.field_77430_b = null;
      }
   }
}
