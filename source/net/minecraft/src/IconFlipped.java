package net.minecraft.src;

public class IconFlipped implements Icon {
   private final Icon baseIcon;
   private final boolean flipU;
   private final boolean flipV;

   public IconFlipped(Icon var1, boolean var2, boolean var3) {
      this.baseIcon = var1;
      this.flipU = var2;
      this.flipV = var3;
   }

   @Override
   public int getOriginX() {
      return this.baseIcon.getOriginX();
   }

   @Override
   public int getOriginY() {
      return this.baseIcon.getOriginY();
   }

   @Override
   public float getMinU() {
      return this.flipU ? this.baseIcon.getMaxU() : this.baseIcon.getMinU();
   }

   @Override
   public float getMaxU() {
      return this.flipU ? this.baseIcon.getMinU() : this.baseIcon.getMaxU();
   }

   @Override
   public float getInterpolatedU(double var1) {
      float var3 = this.getMaxU() - this.getMinU();
      return this.getMinU() + var3 * ((float)var1 / 16.0F);
   }

   @Override
   public float getMinV() {
      return this.flipV ? this.baseIcon.getMinV() : this.baseIcon.getMinV();
   }

   @Override
   public float getMaxV() {
      return this.flipV ? this.baseIcon.getMinV() : this.baseIcon.getMaxV();
   }

   @Override
   public float getInterpolatedV(double var1) {
      float var3 = this.getMaxV() - this.getMinV();
      return this.getMinV() + var3 * ((float)var1 / 16.0F);
   }

   @Override
   public String getIconName() {
      return this.baseIcon.getIconName();
   }

   @Override
   public int getSheetWidth() {
      return this.baseIcon.getSheetWidth();
   }

   @Override
   public int getSheetHeight() {
      return this.baseIcon.getSheetHeight();
   }
}
