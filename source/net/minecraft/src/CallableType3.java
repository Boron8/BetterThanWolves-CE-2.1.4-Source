package net.minecraft.src;

import java.util.concurrent.Callable;

class CallableType3 implements Callable {
   CallableType3(IntegratedServer var1) {
      this.theIntegratedServer = var1;
   }

   public String getType() {
      return "Integrated Server (map_client.txt)";
   }
}
