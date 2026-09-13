package net.minecraft.src;

import java.util.concurrent.Callable;

class CallableType implements Callable {
   CallableType(DedicatedServer var1) {
      this.theDecitatedServer = var1;
   }

   public String getType() {
      String var1 = this.theDecitatedServer.getServerModName();
      return !var1.equals("vanilla") ? "Definitely; Server brand changed to '" + var1 + "'" : "Unknown (can't tell)";
   }
}
