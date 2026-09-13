package net.minecraft.src;

import net.minecraft.client.Minecraft;

public class ThreadClientSleep extends Thread {
   public ThreadClientSleep(Minecraft var1, String var2) {
      super(var2);
      this.mc = var1;
   }

   @Override
   public void run() {
      while (this.mc.running) {
         try {
            Thread.sleep(2147483647L);
         } catch (InterruptedException var2) {
         }
      }
   }
}
