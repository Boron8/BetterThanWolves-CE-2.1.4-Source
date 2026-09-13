package net.minecraft.src;

import org.lwjgl.input.Keyboard;

public class GuiScreenResetWorld extends GuiScreen {
   private GuiScreen field_96152_a;
   private McoServer field_96150_b;
   private GuiTextField field_96151_c;
   private final int field_96149_d = 1;
   private final int field_96153_n = 2;
   private GuiButton field_96154_o;

   public GuiScreenResetWorld(GuiScreen var1, McoServer var2) {
      this.field_96152_a = var1;
      this.field_96150_b = var2;
   }

   @Override
   public void updateScreen() {
      this.field_96151_c.updateCursorCounter();
   }

   @Override
   public void initGui() {
      StringTranslate var1 = StringTranslate.getInstance();
      Keyboard.enableRepeatEvents(true);
      this.buttonList.clear();
      this.buttonList
         .add(this.field_96154_o = new GuiButton(1, this.width / 2 - 100, this.height / 4 + 96 + 12, var1.translateKey("mco.configure.world.buttons.reset")));
      this.buttonList.add(new GuiButton(2, this.width / 2 - 100, this.height / 4 + 120 + 12, var1.translateKey("gui.cancel")));
      this.field_96151_c = new GuiTextField(this.fontRenderer, this.width / 2 - 100, 109, 200, 20);
      this.field_96151_c.setFocused(true);
      this.field_96151_c.setMaxStringLength(32);
      this.field_96151_c.setText("");
   }

   @Override
   public void onGuiClosed() {
      Keyboard.enableRepeatEvents(false);
   }

   @Override
   protected void keyTyped(char var1, int var2) {
      this.field_96151_c.textboxKeyTyped(var1, var2);
      if (var1 == '\r') {
         this.actionPerformed(this.field_96154_o);
      }
   }

   @Override
   protected void actionPerformed(GuiButton var1) {
      if (var1.enabled) {
         if (var1.id == 2) {
            this.mc.displayGuiScreen(this.field_96152_a);
         } else if (var1.id == 1) {
            TaskResetWorld var2 = new TaskResetWorld(this, this.field_96150_b.field_96408_a, this.field_96151_c.getText());
            GuiScreenLongRunningTask var3 = new GuiScreenLongRunningTask(this.mc, this.field_96152_a, var2);
            var3.func_98117_g();
            this.mc.displayGuiScreen(var3);
         }
      }
   }

   @Override
   protected void mouseClicked(int var1, int var2, int var3) {
      super.mouseClicked(var1, var2, var3);
      this.field_96151_c.mouseClicked(var1, var2, var3);
   }

   @Override
   public void drawScreen(int var1, int var2, float var3) {
      StringTranslate var4 = StringTranslate.getInstance();
      this.e();
      this.a(this.fontRenderer, var4.translateKey("mco.reset.world.title"), this.width / 2, 17, 16777215);
      this.a(this.fontRenderer, var4.translateKey("mco.reset.world.warning"), this.width / 2, 66, 16711680);
      this.b(this.fontRenderer, var4.translateKey("mco.reset.world.seed"), this.width / 2 - 100, 96, 10526880);
      this.field_96151_c.drawTextBox();
      super.drawScreen(var1, var2, var3);
   }
}
