package net.minecraft.src;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;

public class PlayerUsageSnooper {
   private Map dataMap = new HashMap();
   private final String uniqueID = UUID.randomUUID().toString();
   private final URL serverUrl;
   private final IPlayerUsage playerStatsCollector;
   private final java.util.Timer threadTrigger = new java.util.Timer("Snooper Timer", true);
   private final Object syncLock = new Object();
   private final long field_98224_g = System.currentTimeMillis();
   private boolean isRunning = false;
   private int selfCounter = 0;

   public PlayerUsageSnooper(String var1, IPlayerUsage var2) {
      try {
         this.serverUrl = new URL("http://snoop.minecraft.net/" + var1 + "?version=" + 1);
      } catch (MalformedURLException var4) {
         throw new IllegalArgumentException();
      }

      this.playerStatsCollector = var2;
   }

   public void startSnooper() {
      if (!this.isRunning) {
         this.isRunning = true;
         this.addBaseDataToSnooper();
         this.threadTrigger.schedule(new PlayerUsageSnooperThread(this), 0L, 900000L);
      }
   }

   private void addBaseDataToSnooper() {
      this.addJvmArgsToSnooper();
      this.addData("snooper_token", this.uniqueID);
      this.addData("os_name", System.getProperty("os.name"));
      this.addData("os_version", System.getProperty("os.version"));
      this.addData("os_architecture", System.getProperty("os.arch"));
      this.addData("java_version", System.getProperty("java.version"));
      this.addData("version", "1.5.2");
      this.playerStatsCollector.addServerTypeToSnooper(this);
   }

   private void addJvmArgsToSnooper() {
      RuntimeMXBean var1 = ManagementFactory.getRuntimeMXBean();
      List var2 = var1.getInputArguments();
      int var3 = 0;

      for (String var5 : var2) {
         if (var5.startsWith("-X")) {
            this.addData("jvm_arg[" + var3++ + "]", var5);
         }
      }

      this.addData("jvm_args", var3);
   }

   public void addMemoryStatsToSnooper() {
      this.addData("memory_total", Runtime.getRuntime().totalMemory());
      this.addData("memory_max", Runtime.getRuntime().maxMemory());
      this.addData("memory_free", Runtime.getRuntime().freeMemory());
      this.addData("cpu_cores", Runtime.getRuntime().availableProcessors());
      this.addData("run_time", (System.currentTimeMillis() - this.field_98224_g) / 60L * 1000L);
      this.playerStatsCollector.addServerStatsToSnooper(this);
   }

   public void addData(String var1, Object var2) {
      synchronized (this.syncLock) {
         this.dataMap.put(var1, var2);
      }
   }

   public Map getCurrentStats() {
      LinkedHashMap var1 = new LinkedHashMap();
      synchronized (this.syncLock) {
         this.addMemoryStatsToSnooper();

         for (Entry var4 : this.dataMap.entrySet()) {
            var1.put(var4.getKey(), var4.getValue().toString());
         }

         return var1;
      }
   }

   public boolean isSnooperRunning() {
      return this.isRunning;
   }

   public void stopSnooper() {
      this.threadTrigger.cancel();
   }

   public String getUniqueID() {
      return this.uniqueID;
   }
}
