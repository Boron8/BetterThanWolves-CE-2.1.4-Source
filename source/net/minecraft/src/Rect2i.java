package net.minecraft.src;

public class Rect2i {
   private int rectX;
   private int rectY;
   private int rectWidth;
   private int rectHeight;

   public Rect2i(int var1, int var2, int var3, int var4) {
      this.rectX = var1;
      this.rectY = var2;
      this.rectWidth = var3;
      this.rectHeight = var4;
   }

   public Rect2i intersection(Rect2i var1) {
      int var2 = this.rectX;
      int var3 = this.rectY;
      int var4 = this.rectX + this.rectWidth;
      int var5 = this.rectY + this.rectHeight;
      int var6 = var1.getRectX();
      int var7 = var1.getRectY();
      int var8 = var6 + var1.getRectWidth();
      int var9 = var7 + var1.getRectHeight();
      this.rectX = Math.max(var2, var6);
      this.rectY = Math.max(var3, var7);
      this.rectWidth = Math.max(0, Math.min(var4, var8) - this.rectX);
      this.rectHeight = Math.max(0, Math.min(var5, var9) - this.rectY);
      return this;
   }

   public int getRectX() {
      return this.rectX;
   }

   public int getRectY() {
      return this.rectY;
   }

   public int getRectWidth() {
      return this.rectWidth;
   }

   public int getRectHeight() {
      return this.rectHeight;
   }
}
