package net.minecraft.src;

import java.util.concurrent.Callable;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;

public class CallableGLInfo implements Callable {
   public CallableGLInfo(Minecraft var1) {
      this.mc = var1;
   }

   public String getTexturePack() {
      return GL11.glGetString(7937) + " GL version " + GL11.glGetString(7938) + ", " + GL11.glGetString(7936);
   }
}
