package net.minecraft.src;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.Map.Entry;

public class GuiSnooper extends GuiScreen {
   private final GuiScreen snooperGuiScreen;
   private final GameSettings snooperGameSettings;
   private final List field_74098_c = new ArrayList();
   private final List field_74096_d = new ArrayList();
   private String snooperTitle;
   private String[] field_74101_n;
   private GuiSnooperList snooperList;
   private GuiButton buttonAllowSnooping;

   public GuiSnooper(GuiScreen var1, GameSettings var2) {
      this.snooperGuiScreen = var1;
      this.snooperGameSettings = var2;
   }

   @Override
   public void initGui() {
      this.snooperTitle = StatCollector.translateToLocal("options.snooper.title");
      String var1 = StatCollector.translateToLocal("options.snooper.desc");
      ArrayList var2 = new ArrayList();

      for (String var4 : this.fontRenderer.listFormattedStringToWidth(var1, this.width - 30)) {
         var2.add(var4);
      }

      this.field_74101_n = var2.toArray(new String[0]);
      this.field_74098_c.clear();
      this.field_74096_d.clear();
      this.buttonList
         .add(
            this.buttonAllowSnooping = new GuiButton(
               1, this.width / 2 - 152, this.height - 30, 150, 20, this.snooperGameSettings.getKeyBinding(EnumOptions.SNOOPER_ENABLED)
            )
         );
      this.buttonList.add(new GuiButton(2, this.width / 2 + 2, this.height - 30, 150, 20, StatCollector.translateToLocal("gui.done")));
      boolean var6 = this.mc.getIntegratedServer() != null && this.mc.getIntegratedServer().aj() != null;

      for (Entry var5 : new TreeMap(this.mc.getPlayerUsageSnooper().getCurrentStats()).entrySet()) {
         this.field_74098_c.add((var6 ? "C " : "") + (String)var5.getKey());
         this.field_74096_d.add(this.fontRenderer.trimStringToWidth((String)var5.getValue(), this.width - 220));
      }

      if (var6) {
         for (Entry var9 : new TreeMap(this.mc.getIntegratedServer().aj().getCurrentStats()).entrySet()) {
            this.field_74098_c.add("S " + (String)var9.getKey());
            this.field_74096_d.add(this.fontRenderer.trimStringToWidth((String)var9.getValue(), this.width - 220));
         }
      }

      this.snooperList = new GuiSnooperList(this);
   }

   @Override
   protected void actionPerformed(GuiButton var1) {
      if (var1.enabled) {
         if (var1.id == 2) {
            this.snooperGameSettings.saveOptions();
            this.snooperGameSettings.saveOptions();
            this.mc.displayGuiScreen(this.snooperGuiScreen);
         }

         if (var1.id == 1) {
            this.snooperGameSettings.setOptionValue(EnumOptions.SNOOPER_ENABLED, 1);
            this.buttonAllowSnooping.displayString = this.snooperGameSettings.getKeyBinding(EnumOptions.SNOOPER_ENABLED);
         }
      }
   }

   @Override
   public void drawScreen(int var1, int var2, float var3) {
      this.e();
      this.snooperList.a(var1, var2, var3);
      this.a(this.fontRenderer, this.snooperTitle, this.width / 2, 8, 16777215);
      int var4 = 22;

      for (String var8 : this.field_74101_n) {
         this.a(this.fontRenderer, var8, this.width / 2, var4, 8421504);
         var4 += this.fontRenderer.FONT_HEIGHT;
      }

      super.drawScreen(var1, var2, var3);
   }
}
