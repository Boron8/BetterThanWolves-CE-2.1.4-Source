package net.minecraft.src;

import java.util.concurrent.Callable;
import net.minecraft.client.Minecraft;

public class CallableTickingScreenName implements Callable {
   public CallableTickingScreenName(Minecraft var1) {
      this.mc = var1;
   }

   public String getLWJGLVersion() {
      return this.mc.currentScreen.getClass().getCanonicalName();
   }
}
