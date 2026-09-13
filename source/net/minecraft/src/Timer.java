package net.minecraft.src;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;

@Environment(EnvType.CLIENT)
public class Timer {
   float ticksPerSecond;
   private double lastHRTime;
   public int elapsedTicks;
   public float renderPartialTicks;
   public float timerSpeed = 1.0F;
   public float elapsedPartialTicks = 0.0F;
   private long lastSyncSysClock;
   private long lastSyncHRClock;
   private long field_74285_i;
   private double timeSyncAdjustment = 1.0;
   private float timerSpeedTarget = 1.0F;

   public Timer(float par1) {
      this.ticksPerSecond = par1;
      this.lastSyncSysClock = Minecraft.getSystemTime();
      this.lastSyncHRClock = System.nanoTime() / 1000000L;
   }

   public void updateTimer() {
      long var1 = Minecraft.getSystemTime();
      long var3 = var1 - this.lastSyncSysClock;
      long var5 = System.nanoTime() / 1000000L;
      double var7 = var5 / 1000.0;
      if (var3 <= 1000L && var3 >= 0L) {
         this.field_74285_i += var3;
         if (this.field_74285_i > 1000L) {
            long var9 = var5 - this.lastSyncHRClock;
            double var11 = (double)this.field_74285_i / var9;
            this.timeSyncAdjustment = this.timeSyncAdjustment + (var11 - this.timeSyncAdjustment) * 0.2F;
            this.lastSyncHRClock = var5;
            this.field_74285_i = 0L;
         }

         if (this.field_74285_i < 0L) {
            this.lastSyncHRClock = var5;
         }
      } else {
         this.lastHRTime = var7;
      }

      this.timerSpeed = (float)(this.timerSpeed + (this.timerSpeedTarget - this.timerSpeed) * Math.min(var3 / 3000.0, 1.0));
      this.lastSyncSysClock = var1;
      double var13 = (var7 - this.lastHRTime) * this.timeSyncAdjustment;
      this.lastHRTime = var7;
      if (var13 < 0.0) {
         var13 = 0.0;
      }

      if (var13 > 1.0) {
         var13 = 1.0;
      }

      this.elapsedPartialTicks = (float)(this.elapsedPartialTicks + var13 * this.timerSpeed * this.ticksPerSecond);
      this.elapsedTicks = (int)this.elapsedPartialTicks;
      this.elapsedPartialTicks = this.elapsedPartialTicks - this.elapsedTicks;
      if (this.elapsedTicks > 10) {
         this.elapsedTicks = 10;
      }

      this.renderPartialTicks = this.elapsedPartialTicks;
   }

   public void resetTimerSpeed() {
      this.timerSpeed = 1.0F;
      this.timerSpeedTarget = 1.0F;
   }

   public float getTimerSpeed() {
      return this.timerSpeed;
   }

   public void setTimerSpeedTarget(float timerSpeed) {
      this.timerSpeedTarget = timerSpeed;
   }
}
