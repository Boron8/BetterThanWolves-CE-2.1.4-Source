package net.minecraft.src;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

class DedicatedServerCommandThread extends Thread {
   DedicatedServerCommandThread(DedicatedServer var1) {
      this.server = var1;
   }

   @Override
   public void run() {
      BufferedReader var1 = new BufferedReader(new InputStreamReader(System.in));

      String var2;
      try {
         while (!this.server.ac() && this.server.m() && (var2 = var1.readLine()) != null) {
            this.server.addPendingCommand(var2, this.server);
         }
      } catch (IOException var4) {
         var4.printStackTrace();
      }
   }
}
