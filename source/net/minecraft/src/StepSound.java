package net.minecraft.src;

public class StepSound {
   public final String stepSoundName;
   public final float stepSoundVolume;
   public final float stepSoundPitch;

   public StepSound(String par1Str, float par2, float par3) {
      this.stepSoundName = par1Str;
      this.stepSoundVolume = par2;
      this.stepSoundPitch = par3;
   }

   public float getVolume() {
      return this.stepSoundVolume;
   }

   public float getStepVolume() {
      return this.getVolume();
   }

   public float getPlaceVolume() {
      return this.getVolume();
   }

   public float getBreakVolume() {
      return this.getVolume();
   }

   public float getPitch() {
      return this.stepSoundPitch;
   }

   public float getStepPitch() {
      return this.getPitch();
   }

   public float getPlacePitch() {
      return this.getPitch();
   }

   public float getBreakPitch() {
      return this.getPitch();
   }

   public String getBreakSound() {
      return "dig." + this.stepSoundName;
   }

   public String getStepSound() {
      return "step." + this.stepSoundName;
   }

   public String getPlaceSound() {
      return this.getBreakSound();
   }
}
