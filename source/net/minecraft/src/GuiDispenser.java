package net.minecraft.src;

import org.lwjgl.opengl.GL11;

public class GuiDispenser extends GuiContainer {
   public TileEntityDispenser field_94078_r;

   public GuiDispenser(InventoryPlayer var1, TileEntityDispenser var2) {
      super(new ContainerDispenser(var1, var2));
      this.field_94078_r = var2;
   }

   @Override
   protected void drawGuiContainerForegroundLayer(int var1, int var2) {
      String var3 = this.field_94078_r.isInvNameLocalized() ? this.field_94078_r.getInvName() : StatCollector.translateToLocal(this.field_94078_r.getInvName());
      this.fontRenderer.drawString(var3, this.xSize / 2 - this.fontRenderer.getStringWidth(var3) / 2, 6, 4210752);
      this.fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 8, this.ySize - 96 + 2, 4210752);
   }

   @Override
   protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3) {
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      this.mc.renderEngine.bindTexture("/gui/trap.png");
      int var4 = (this.width - this.xSize) / 2;
      int var5 = (this.height - this.ySize) / 2;
      this.b(var4, var5, 0, 0, this.xSize, this.ySize);
   }
}
