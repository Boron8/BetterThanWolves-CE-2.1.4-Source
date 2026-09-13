package net.minecraft.src;

import org.lwjgl.opengl.GL11;

class GuiFlatPresetsListSlot extends GuiSlot {
   public int field_82459_a;

   public GuiFlatPresetsListSlot(GuiFlatPresets var1) {
      super(var1.mc, var1.width, var1.height, 80, var1.height - 37, 24);
      this.flatPresetsGui = var1;
      this.field_82459_a = -1;
   }

   private void func_82457_a(int var1, int var2, int var3) {
      this.func_82456_d(var1 + 1, var2 + 1);
      GL11.glEnable(32826);
      RenderHelper.enableGUIStandardItemLighting();
      GuiFlatPresets.getPresetIconRenderer()
         .renderItemIntoGUI(this.flatPresetsGui.fontRenderer, this.flatPresetsGui.mc.renderEngine, new ItemStack(var3, 1, 0), var1 + 2, var2 + 2);
      RenderHelper.disableStandardItemLighting();
      GL11.glDisable(32826);
   }

   private void func_82456_d(int var1, int var2) {
      this.func_82455_b(var1, var2, 0, 0);
   }

   private void func_82455_b(int var1, int var2, int var3, int var4) {
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      this.flatPresetsGui.mc.renderEngine.bindTexture("/gui/slot.png");
      Tessellator var9 = Tessellator.instance;
      var9.startDrawingQuads();
      var9.addVertexWithUV(var1 + 0, var2 + 18, this.flatPresetsGui.zLevel, (var3 + 0) * 0.0078125F, (var4 + 18) * 0.0078125F);
      var9.addVertexWithUV(var1 + 18, var2 + 18, this.flatPresetsGui.zLevel, (var3 + 18) * 0.0078125F, (var4 + 18) * 0.0078125F);
      var9.addVertexWithUV(var1 + 18, var2 + 0, this.flatPresetsGui.zLevel, (var3 + 18) * 0.0078125F, (var4 + 0) * 0.0078125F);
      var9.addVertexWithUV(var1 + 0, var2 + 0, this.flatPresetsGui.zLevel, (var3 + 0) * 0.0078125F, (var4 + 0) * 0.0078125F);
      var9.draw();
   }

   @Override
   protected int getSize() {
      return GuiFlatPresets.getPresets().size();
   }

   @Override
   protected void elementClicked(int var1, boolean var2) {
      this.field_82459_a = var1;
      this.flatPresetsGui.func_82296_g();
      GuiFlatPresets.func_82298_b(this.flatPresetsGui)
         .setText(((GuiFlatPresetsItem)GuiFlatPresets.getPresets().get(GuiFlatPresets.func_82292_a(this.flatPresetsGui).field_82459_a)).presetData);
   }

   @Override
   protected boolean isSelected(int var1) {
      return var1 == this.field_82459_a;
   }

   @Override
   protected void drawBackground() {
   }

   @Override
   protected void drawSlot(int var1, int var2, int var3, int var4, Tessellator var5) {
      GuiFlatPresetsItem var6 = (GuiFlatPresetsItem)GuiFlatPresets.getPresets().get(var1);
      this.func_82457_a(var2, var3, var6.iconId);
      this.flatPresetsGui.fontRenderer.drawString(var6.presetName, var2 + 18 + 5, var3 + 6, 16777215);
   }
}
