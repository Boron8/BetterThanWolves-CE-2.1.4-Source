package net.minecraft.src;

import java.util.concurrent.Callable;
import net.minecraft.client.Minecraft;

public class CallableType2 implements Callable {
   public CallableType2(Minecraft var1) {
      this.mc = var1;
   }

   public String func_82886_a() {
      return "Client (map_client.txt)";
   }
}
