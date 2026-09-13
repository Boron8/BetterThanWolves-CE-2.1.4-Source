package net.minecraft.src;

import java.util.concurrent.Callable;

class CallableScreenSize implements Callable {
   CallableScreenSize(EntityRenderer var1, ScaledResolution var2) {
      this.theEntityRenderer = var1;
      this.theScaledResolution = var2;
   }

   public String callScreenSize() {
      return String.format(
         "Scaled: (%d, %d). Absolute: (%d, %d). Scale factor of %d",
         this.theScaledResolution.getScaledWidth(),
         this.theScaledResolution.getScaledHeight(),
         EntityRenderer.getRendererMinecraft(this.theEntityRenderer).displayWidth,
         EntityRenderer.getRendererMinecraft(this.theEntityRenderer).displayHeight,
         this.theScaledResolution.getScaleFactor()
      );
   }
}
