package net.minecraft.src;

class DedicatedServerSleepThread extends Thread {
   DedicatedServerSleepThread(DedicatedServer var1) {
      this.theDecitatedServer = var1;
      this.setDaemon(true);
      this.start();
   }

   @Override
   public void run() {
      while (true) {
         try {
            Thread.sleep(2147483647L);
         } catch (InterruptedException var2) {
         }
      }
   }
}
