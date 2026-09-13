package net.minecraft.src;

class GuiSnooperList extends GuiSlot {
   public GuiSnooperList(GuiSnooper var1) {
      super(var1.mc, var1.width, var1.height, 80, var1.height - 40, var1.fontRenderer.FONT_HEIGHT + 1);
      this.snooperGui = var1;
   }

   @Override
   protected int getSize() {
      return GuiSnooper.func_74095_a(this.snooperGui).size();
   }

   @Override
   protected void elementClicked(int var1, boolean var2) {
   }

   @Override
   protected boolean isSelected(int var1) {
      return false;
   }

   @Override
   protected void drawBackground() {
   }

   @Override
   protected void drawSlot(int var1, int var2, int var3, int var4, Tessellator var5) {
      this.snooperGui.fontRenderer.drawString((String)GuiSnooper.func_74095_a(this.snooperGui).get(var1), 10, var3, 16777215);
      this.snooperGui.fontRenderer.drawString((String)GuiSnooper.func_74094_b(this.snooperGui).get(var1), 230, var3, 16777215);
   }

   @Override
   protected int getScrollBarX() {
      return this.snooperGui.width - 10;
   }
}
