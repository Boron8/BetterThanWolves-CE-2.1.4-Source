package net.minecraft.src;

import java.util.concurrent.Callable;
import net.minecraft.client.Minecraft;

public class CallableTexturePack implements Callable {
   public CallableTexturePack(Minecraft var1) {
      this.theMinecraft = var1;
   }

   public String callTexturePack() {
      return this.theMinecraft.gameSettings.skin;
   }
}
