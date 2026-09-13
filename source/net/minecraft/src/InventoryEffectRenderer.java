package net.minecraft.src;

import java.util.Collection;
import org.lwjgl.opengl.GL11;

public abstract class InventoryEffectRenderer extends GuiContainer {
   private boolean field_74222_o;

   public InventoryEffectRenderer(Container var1) {
      super(var1);
   }

   @Override
   public void initGui() {
      super.initGui();
      if (!this.mc.thePlayer.bC().isEmpty()) {
         this.guiLeft = 160 + (this.width - this.xSize - 200) / 2;
         this.field_74222_o = true;
      }
   }

   @Override
   public void drawScreen(int var1, int var2, float var3) {
      super.drawScreen(var1, var2, var3);
      if (this.field_74222_o) {
         this.displayDebuffEffects();
      }
   }

   private void displayDebuffEffects() {
      int var1 = this.guiLeft - 124;
      int var2 = this.guiTop;
      Collection var4 = this.mc.thePlayer.bC();
      if (!var4.isEmpty()) {
         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
         GL11.glDisable(2896);
         int var5 = 33;
         if (var4.size() > 5) {
            var5 = 132 / (var4.size() - 1);
         }

         for (PotionEffect var7 : this.mc.thePlayer.bC()) {
            Potion var8 = Potion.potionTypes[var7.getPotionID()];
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            this.mc.renderEngine.bindTexture("/gui/inventory.png");
            this.b(var1, var2, 0, 166, 140, 32);
            if (var8.hasStatusIcon()) {
               int var9 = var8.getStatusIconIndex();
               this.b(var1 + 6, var2 + 7, 0 + var9 % 8 * 18, 198 + var9 / 8 * 18, 18, 18);
            }

            String var11 = StatCollector.translateToLocal(var8.getName());
            if (var7.getAmplifier() == 1) {
               var11 = var11 + " II";
            } else if (var7.getAmplifier() == 2) {
               var11 = var11 + " III";
            } else if (var7.getAmplifier() == 3) {
               var11 = var11 + " IV";
            }

            this.fontRenderer.drawStringWithShadow(var11, var1 + 10 + 18, var2 + 6, 16777215);
            String var10 = Potion.getDurationString(var7);
            this.fontRenderer.drawStringWithShadow(var10, var1 + 10 + 18, var2 + 6 + 10, 8355711);
            var2 += var5;
         }
      }
   }
}
