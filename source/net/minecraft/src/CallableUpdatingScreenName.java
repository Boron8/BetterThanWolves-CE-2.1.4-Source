package net.minecraft.src;

import java.util.concurrent.Callable;
import net.minecraft.client.Minecraft;

public class CallableUpdatingScreenName implements Callable {
   public CallableUpdatingScreenName(Minecraft var1) {
      this.theMinecraft = var1;
   }

   public String callUpdatingScreenName() {
      return this.theMinecraft.currentScreen.getClass().getCanonicalName();
   }
}
