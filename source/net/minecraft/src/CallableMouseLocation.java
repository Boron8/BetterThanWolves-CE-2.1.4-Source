package net.minecraft.src;

import java.util.concurrent.Callable;
import org.lwjgl.input.Mouse;

class CallableMouseLocation implements Callable {
   CallableMouseLocation(EntityRenderer var1, int var2, int var3) {
      this.theEntityRenderer = var1;
      this.field_90026_a = var2;
      this.field_90024_b = var3;
   }

   public String callMouseLocation() {
      return String.format("Scaled: (%d, %d). Absolute: (%d, %d)", this.field_90026_a, this.field_90024_b, Mouse.getX(), Mouse.getY());
   }
}
