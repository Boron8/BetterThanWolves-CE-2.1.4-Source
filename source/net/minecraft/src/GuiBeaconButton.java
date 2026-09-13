package net.minecraft.src;

import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;

class GuiBeaconButton extends GuiButton {
   private final String buttonTexture;
   private final int field_82257_l;
   private final int field_82258_m;
   private boolean field_82256_n;

   protected GuiBeaconButton(int var1, int var2, int var3, String var4, int var5, int var6) {
      super(var1, var2, var3, 22, 22, "");
      this.buttonTexture = var4;
      this.field_82257_l = var5;
      this.field_82258_m = var6;
   }

   @Override
   public void drawButton(Minecraft var1, int var2, int var3) {
      if (this.drawButton) {
         var1.renderEngine.bindTexture("/gui/beacon.png");
         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
         this.field_82253_i = var2 >= this.xPosition && var3 >= this.yPosition && var2 < this.xPosition + this.width && var3 < this.yPosition + this.height;
         short var4 = 219;
         int var5 = 0;
         if (!this.enabled) {
            var5 += this.width * 2;
         } else if (this.field_82256_n) {
            var5 += this.width * 1;
         } else if (this.field_82253_i) {
            var5 += this.width * 3;
         }

         this.b(this.xPosition, this.yPosition, var5, var4, this.width, this.height);
         if (!"/gui/beacon.png".equals(this.buttonTexture)) {
            var1.renderEngine.bindTexture(this.buttonTexture);
         }

         this.b(this.xPosition + 2, this.yPosition + 2, this.field_82257_l, this.field_82258_m, 18, 18);
      }
   }

   public boolean func_82255_b() {
      return this.field_82256_n;
   }

   public void func_82254_b(boolean var1) {
      this.field_82256_n = var1;
   }
}
