package net.minecraft.src;

import java.awt.Canvas;
import net.minecraft.client.MinecraftApplet;

public class CanvasMinecraftApplet extends Canvas {
   public CanvasMinecraftApplet(MinecraftApplet var1) {
      this.mcApplet = var1;
   }

   @Override
   public synchronized void addNotify() {
      super.addNotify();
      this.mcApplet.startMainThread();
   }

   @Override
   public synchronized void removeNotify() {
      this.mcApplet.shutdown();
      super.removeNotify();
   }
}
