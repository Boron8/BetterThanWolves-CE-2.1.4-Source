package net.minecraft.src;

import com.prupe.mcpatcher.hd.FancyDial;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;

@Environment(EnvType.CLIENT)
public class TextureCompass extends TextureStitched {
   public static TextureCompass compassTexture;
   public double currentAngle;
   public double angleDelta;
   private boolean directionUpdated = false;

   public TextureCompass() {
      super("compass");
      compassTexture = this;
      FancyDial.setup(this);
   }

   @Override
   public void updateAnimation() {
      this.updateInert();
      this.directionUpdated = false;
   }

   private void updateCompass(World par1World, double par2, double par4, double par6, boolean par8, boolean par9, EntityPlayer player) {
      double var10x = 0.0;
      if (par1World != null && !par8) {
         var10x = this.computeCompassAngle(par2, par4, par6, par9, player);
      }

      if (par9) {
         this.currentAngle = var10x;
      } else if (!this.directionUpdated) {
         double var17 = var10x - this.currentAngle;

         while (var17 < -Math.PI) {
            var17 += Math.PI * 2;
         }

         while (var17 >= Math.PI) {
            var17 -= Math.PI * 2;
         }

         if (var17 < -1.0) {
            var17 = -1.0;
         }

         if (var17 > 1.0) {
            var17 = 1.0;
         }

         this.angleDelta += var17 * 0.1;
         this.angleDelta *= 0.8;
         this.currentAngle = this.currentAngle + this.angleDelta;
         this.directionUpdated = true;
      }

      if (!FancyDial.update(this, par9)) {
         int var18 = (int)((this.currentAngle / (Math.PI * 2) + 1.0) * this.textureList.size()) % this.textureList.size();

         while (var18 < 0) {
            var18 = (var18 + this.textureList.size()) % this.textureList.size();
         }

         if (var18 != this.frameCounter) {
            this.frameCounter = var18;
            this.textureSheet.copyFrom(this.originX, this.originY, (Texture)this.textureList.get(this.frameCounter), this.rotated);
         }
      }
   }

   public void updateActive() {
      Minecraft mc = Minecraft.getMinecraft();
      if (mc.theWorld != null && mc.thePlayer != null) {
         this.updateCompass(mc.theWorld, mc.thePlayer.posX, mc.thePlayer.posZ, mc.thePlayer.rotationYaw, false, false, mc.thePlayer);
      } else {
         this.updateCompass(null, 0.0, 0.0, 0.0, true, false, null);
      }
   }

   public void updateInert() {
      this.frameCounter = this.textureList.size() / 2;
      this.textureSheet.copyFrom(this.originX, this.originY, (Texture)this.textureList.get(this.frameCounter), this.rotated);
   }

   private double computeCompassAngle(double dSourceX, double dSourceZ, double dSourceYaw, boolean bIsInFrame, EntityPlayer player) {
      double angle = Math.PI;
      if (!bIsInFrame && player != null) {
         if (!player.hasValidMagneticPointForLocation()) {
            angle = Math.random() * Math.PI * 2.0;
         } else {
            int iTargetI = player.getStongestMagneticPointForLocationI();
            int iTargetK = player.getStongestMagneticPointForLocationK();
            double dDeltaX = iTargetI + 0.5 - dSourceX;
            double dDeltaZ = iTargetK + 0.5 - dSourceZ;
            dSourceYaw %= 360.0;
            angle = -((dSourceYaw - 90.0) * Math.PI / 180.0 - Math.atan2(dDeltaZ, dDeltaX));
         }
      }

      return angle;
   }
}
