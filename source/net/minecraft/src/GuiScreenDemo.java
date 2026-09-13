package net.minecraft.src;

import java.net.URI;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

public class GuiScreenDemo extends GuiScreen {
   @Override
   public void initGui() {
      this.buttonList.clear();
      byte var1 = -16;
      this.buttonList.add(new GuiButton(1, this.width / 2 - 116, this.height / 2 + 62 + var1, 114, 20, StatCollector.translateToLocal("demo.help.buy")));
      this.buttonList.add(new GuiButton(2, this.width / 2 + 2, this.height / 2 + 62 + var1, 114, 20, StatCollector.translateToLocal("demo.help.later")));
   }

   @Override
   protected void actionPerformed(GuiButton var1) {
      switch (var1.id) {
         case 1:
            var1.enabled = false;

            try {
               Class var2 = Class.forName("java.awt.Desktop");
               Object var3 = var2.getMethod("getDesktop").invoke(null);
               var2.getMethod("browse", URI.class).invoke(var3, new URI("http://www.minecraft.net/store?source=demo"));
            } catch (Throwable var4) {
               var4.printStackTrace();
            }
            break;
         case 2:
            this.mc.displayGuiScreen(null);
            this.mc.setIngameFocus();
      }
   }

   @Override
   public void updateScreen() {
      super.updateScreen();
   }

   @Override
   public void drawDefaultBackground() {
      super.drawDefaultBackground();
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      this.mc.renderEngine.bindTexture("/gui/demo_bg.png");
      int var1 = (this.width - 248) / 2;
      int var2 = (this.height - 166) / 2;
      this.b(var1, var2, 0, 0, 248, 166);
   }

   @Override
   public void drawScreen(int var1, int var2, float var3) {
      this.drawDefaultBackground();
      int var4 = (this.width - 248) / 2 + 10;
      int var5 = (this.height - 166) / 2 + 8;
      this.fontRenderer.drawString(StatCollector.translateToLocal("demo.help.title"), var4, var5, 2039583);
      var5 += 12;
      GameSettings var7 = this.mc.gameSettings;
      String var6 = StatCollector.translateToLocal("demo.help.movementShort");
      var6 = String.format(
         var6,
         Keyboard.getKeyName(var7.keyBindForward.keyCode),
         Keyboard.getKeyName(var7.keyBindLeft.keyCode),
         Keyboard.getKeyName(var7.keyBindBack.keyCode),
         Keyboard.getKeyName(var7.keyBindRight.keyCode)
      );
      this.fontRenderer.drawString(var6, var4, var5, 5197647);
      var6 = StatCollector.translateToLocal("demo.help.movementMouse");
      this.fontRenderer.drawString(var6, var4, var5 + 12, 5197647);
      var6 = StatCollector.translateToLocal("demo.help.jump");
      var6 = String.format(var6, Keyboard.getKeyName(var7.keyBindJump.keyCode));
      this.fontRenderer.drawString(var6, var4, var5 + 24, 5197647);
      var6 = StatCollector.translateToLocal("demo.help.inventory");
      var6 = String.format(var6, Keyboard.getKeyName(var7.keyBindInventory.keyCode));
      this.fontRenderer.drawString(var6, var4, var5 + 36, 5197647);
      this.fontRenderer.drawSplitString(StatCollector.translateToLocal("demo.help.fullWrapped"), var4, var5 + 68, 218, 2039583);
      super.drawScreen(var1, var2, var3);
   }
}
