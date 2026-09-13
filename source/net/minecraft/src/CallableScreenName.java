package net.minecraft.src;

import java.util.concurrent.Callable;

class CallableScreenName implements Callable {
   CallableScreenName(EntityRenderer var1) {
      this.entityRender = var1;
   }

   public String callScreenName() {
      return EntityRenderer.getRendererMinecraft(this.entityRender).currentScreen.getClass().getCanonicalName();
   }
}
