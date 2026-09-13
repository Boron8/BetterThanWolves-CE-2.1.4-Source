package net.minecraft.src;

import java.util.concurrent.Callable;

class CallableJavaInfo implements Callable {
   CallableJavaInfo(CrashReport var1) {
      this.theCrashReport = var1;
   }

   public String getJavaInfoAsString() {
      return System.getProperty("java.version") + ", " + System.getProperty("java.vendor");
   }
}
