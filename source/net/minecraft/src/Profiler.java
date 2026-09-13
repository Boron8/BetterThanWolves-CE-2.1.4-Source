package net.minecraft.src;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Profiler {
   private final List sectionList = new ArrayList();
   private final List timestampList = new ArrayList();
   public boolean profilingEnabled = false;
   private String profilingSection = "";
   private final Map profilingMap = new HashMap();

   public void clearProfiling() {
      this.profilingMap.clear();
      this.profilingSection = "";
      this.sectionList.clear();
   }

   public void startSection(String var1) {
      if (this.profilingEnabled) {
         if (this.profilingSection.length() > 0) {
            this.profilingSection = this.profilingSection + ".";
         }

         this.profilingSection = this.profilingSection + var1;
         this.sectionList.add(this.profilingSection);
         this.timestampList.add(System.nanoTime());
      }
   }

   public void endSection() {
      if (this.profilingEnabled) {
         long var1 = System.nanoTime();
         long var3 = (Long)this.timestampList.remove(this.timestampList.size() - 1);
         this.sectionList.remove(this.sectionList.size() - 1);
         long var5 = var1 - var3;
         if (this.profilingMap.containsKey(this.profilingSection)) {
            this.profilingMap.put(this.profilingSection, (Long)this.profilingMap.get(this.profilingSection) + var5);
         } else {
            this.profilingMap.put(this.profilingSection, var5);
         }

         if (var5 > 100000000L) {
            System.out.println("Something's taking too long! '" + this.profilingSection + "' took aprox " + var5 / 1000000.0 + " ms");
         }

         this.profilingSection = !this.sectionList.isEmpty() ? (String)this.sectionList.get(this.sectionList.size() - 1) : "";
      }
   }

   public List getProfilingData(String var1) {
      if (!this.profilingEnabled) {
         return null;
      } else {
         String var2 = var1;
         long var3 = this.profilingMap.containsKey("root") ? (Long)this.profilingMap.get("root") : 0L;
         long var5 = this.profilingMap.containsKey(var1) ? (Long)this.profilingMap.get(var1) : -1L;
         ArrayList var7 = new ArrayList();
         if (var1.length() > 0) {
            var1 = var1 + ".";
         }

         long var8 = 0L;

         for (String var11 : this.profilingMap.keySet()) {
            if (var11.length() > var1.length() && var11.startsWith(var1) && var11.indexOf(".", var1.length() + 1) < 0) {
               var8 += this.profilingMap.get(var11);
            }
         }

         float var20 = (float)var8;
         if (var8 < var5) {
            var8 = var5;
         }

         if (var3 < var8) {
            var3 = var8;
         }

         for (String var12 : this.profilingMap.keySet()) {
            if (var12.length() > var1.length() && var12.startsWith(var1) && var12.indexOf(".", var1.length() + 1) < 0) {
               long var13 = (Long)this.profilingMap.get(var12);
               double var15 = var13 * 100.0 / var8;
               double var17 = var13 * 100.0 / var3;
               String var19 = var12.substring(var1.length());
               var7.add(new ProfilerResult(var19, var15, var17));
            }
         }

         for (String var23 : this.profilingMap.keySet()) {
            this.profilingMap.put(var23, (Long)this.profilingMap.get(var23) * 999L / 1000L);
         }

         if ((float)var8 > var20) {
            var7.add(new ProfilerResult("unspecified", ((float)var8 - var20) * 100.0 / var8, ((float)var8 - var20) * 100.0 / var3));
         }

         Collections.sort(var7);
         var7.add(0, new ProfilerResult(var2, 100.0, var8 * 100.0 / var3));
         return var7;
      }
   }

   public void endStartSection(String var1) {
      this.endSection();
      this.startSection(var1);
   }

   public String getNameOfLastSection() {
      return this.sectionList.size() == 0 ? "[UNKNOWN]" : (String)this.sectionList.get(this.sectionList.size() - 1);
   }
}
