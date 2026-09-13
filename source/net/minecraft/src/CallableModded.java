package net.minecraft.src;

import java.util.concurrent.Callable;
import net.minecraft.client.ClientBrandRetriever;
import net.minecraft.client.Minecraft;

public class CallableModded implements Callable {
   public CallableModded(Minecraft var1) {
      this.mc = var1;
   }

   public String getClientProfilerEnabled() {
      String var1 = ClientBrandRetriever.getClientModName();
      if (!var1.equals("vanilla")) {
         return "Definitely; Client brand changed to '" + var1 + "'";
      } else {
         return Minecraft.class.getSigners() == null
            ? "Very likely; Jar signature invalidated"
            : "Probably not. Jar signature remains and client brand is untouched.";
      }
   }
}
