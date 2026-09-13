package net.minecraft.src;

class GuiSlotStatsGeneral extends GuiSlot {
   public GuiSlotStatsGeneral(GuiStats var1) {
      super(GuiStats.getMinecraft(var1), var1.width, var1.height, 32, var1.height - 64, 10);
      this.statsGui = var1;
      this.a(false);
   }

   @Override
   protected int getSize() {
      return StatList.generalStats.size();
   }

   @Override
   protected void elementClicked(int var1, boolean var2) {
   }

   @Override
   protected boolean isSelected(int var1) {
      return false;
   }

   @Override
   protected int getContentHeight() {
      return this.getSize() * 10;
   }

   @Override
   protected void drawBackground() {
      this.statsGui.e();
   }

   @Override
   protected void drawSlot(int var1, int var2, int var3, int var4, Tessellator var5) {
      StatBase var6 = (StatBase)StatList.generalStats.get(var1);
      this.statsGui
         .b(GuiStats.getFontRenderer1(this.statsGui), StatCollector.translateToLocal(var6.getName()), var2 + 2, var3 + 1, var1 % 2 == 0 ? 16777215 : 9474192);
      String var7 = var6.func_75968_a(GuiStats.getStatsFileWriter(this.statsGui).writeStat(var6));
      this.statsGui
         .b(
            GuiStats.getFontRenderer2(this.statsGui),
            var7,
            var2 + 2 + 213 - GuiStats.getFontRenderer3(this.statsGui).getStringWidth(var7),
            var3 + 1,
            var1 % 2 == 0 ? 16777215 : 9474192
         );
   }
}
