package net.minecraft.src;

public class GuiScreenConfirmation extends GuiYesNo {
   private String field_96288_n;

   public GuiScreenConfirmation(GuiScreen var1, String var2, String var3, String var4, int var5) {
      super(var1, var2, var3, var5);
      this.field_96288_n = var4;
   }

   @Override
   public void initGui() {
      this.buttonList.add(new GuiSmallButton(0, this.width / 2 - 155, this.height / 6 + 112, this.buttonText1));
      this.buttonList.add(new GuiSmallButton(1, this.width / 2 - 155 + 160, this.height / 6 + 112, this.buttonText2));
   }

   @Override
   public void drawScreen(int var1, int var2, float var3) {
      super.drawScreen(var1, var2, var3);
      this.a(this.fontRenderer, this.field_96288_n, this.width / 2, 110, 16777215);
   }
}
