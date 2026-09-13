package net.minecraft.src;

final class StepSoundAnvil extends StepSound {
   StepSoundAnvil(String var1, float var2, float var3) {
      super(var1, var2, var3);
   }

   @Override
   public String getBreakSound() {
      return "dig.stone";
   }

   @Override
   public String getPlaceSound() {
      return "random.anvil_land";
   }
}
