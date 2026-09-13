package net.minecraft.src;

import java.util.concurrent.Callable;
import net.minecraft.client.ClientBrandRetriever;
import net.minecraft.client.Minecraft;

class CallableIsModded implements Callable {
   CallableIsModded(IntegratedServer var1) {
      this.theIntegratedServer = var1;
   }

   public String getMinecraftIsModded() {
      String var1 = ClientBrandRetriever.getClientModName();
      if (!var1.equals("vanilla")) {
         return "Definitely; Client brand changed to '" + var1 + "'";
      } else {
         var1 = this.theIntegratedServer.getServerModName();
         if (!var1.equals("vanilla")) {
            return "Definitely; Server brand changed to '" + var1 + "'";
         } else {
            return Minecraft.class.getSigners() == null
               ? "Very likely; Jar signature invalidated"
               : "Probably not. Jar signature remains and both client + server brands are untouched.";
         }
      }
   }
}
