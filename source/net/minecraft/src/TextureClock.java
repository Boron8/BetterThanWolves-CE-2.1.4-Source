package net.minecraft.src;

import com.prupe.mcpatcher.hd.FancyDial;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;

@Environment(EnvType.CLIENT)
public class TextureClock extends TextureStitched {
   public double field_94239_h;
   private double field_94240_i;
   public static TextureClock clockTexture;

   public TextureClock() {
      super("compass");
      FancyDial.setup(this);
      clockTexture = this;
   }

   @Override
   public void updateAnimation() {
      Minecraft var1 = Minecraft.getMinecraft();
      double var2 = 0.0;
      if (var1.theWorld != null && var1.thePlayer != null) {
         float var4 = var1.theWorld.c(1.0F);
         var2 = var4;
         if (!var1.theWorld.provider.isSurfaceWorld()) {
            var2 = Math.random();
         }
      }

      double var7 = var2 - this.field_94239_h;

      while (var7 < -0.5) {
         var7++;
      }

      while (var7 >= 0.5) {
         var7--;
      }

      if (var7 < -1.0) {
         var7 = -1.0;
      }

      if (var7 > 1.0) {
         var7 = 1.0;
      }

      this.field_94240_i += var7 * 0.1;
      this.field_94240_i *= 0.8;
      this.field_94239_h = this.field_94239_h + this.field_94240_i;
      if (!FancyDial.update(this, false)) {
         int var6 = (int)((this.field_94239_h + 1.0) * this.textureList.size()) % this.textureList.size();

         while (var6 < 0) {
            var6 = (var6 + this.textureList.size()) % this.textureList.size();
         }

         if (var6 != this.frameCounter) {
            this.frameCounter = var6;
            this.textureSheet.copyFrom(this.originX, this.originY, (Texture)this.textureList.get(this.frameCounter), this.rotated);
         }
      }
   }

   public void updateInert() {
      this.frameCounter = 0;
      this.textureSheet.copyFrom(this.originX, this.originY, (Texture)this.textureList.get(this.frameCounter), this.rotated);
   }
}
