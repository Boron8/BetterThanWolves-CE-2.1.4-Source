package net.minecraft.src;

import java.util.concurrent.Callable;

class CallableIntCache implements Callable {
   CallableIntCache(CrashReport var1) {
      this.theCrashReport = var1;
   }

   public String func_85083_a() {
      return IntCache.func_85144_b();
   }
}
