package net.minecraft.src;

import java.util.concurrent.Callable;
import net.minecraft.client.Minecraft;
import org.lwjgl.Sys;

public class CallableLWJGLVersion implements Callable {
   public CallableLWJGLVersion(Minecraft var1) {
      this.mc = var1;
   }

   public String getType() {
      return Sys.getVersion();
   }
}
