package net.minecraft.src;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.List;
import java.util.concurrent.Callable;

class CallableJVMFlags implements Callable {
   CallableJVMFlags(CrashReport var1) {
      this.theCrashReport = var1;
   }

   public String getJVMFlagsAsString() {
      RuntimeMXBean var1 = ManagementFactory.getRuntimeMXBean();
      List var2 = var1.getInputArguments();
      int var3 = 0;
      StringBuilder var4 = new StringBuilder();

      for (String var6 : var2) {
         if (var6.startsWith("-X")) {
            if (var3++ > 0) {
               var4.append(" ");
            }

            var4.append(var6);
         }
      }

      return String.format("%d total; %s", var3, var4.toString());
   }
}
