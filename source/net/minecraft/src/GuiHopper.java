package net.minecraft.src;

import org.lwjgl.opengl.GL11;

public class GuiHopper extends GuiContainer {
   private IInventory field_94081_r;
   private IInventory field_94080_s;

   public GuiHopper(InventoryPlayer var1, IInventory var2) {
      super(new ContainerHopper(var1, var2));
      this.field_94081_r = var1;
      this.field_94080_s = var2;
      this.allowUserInput = false;
      this.ySize = 133;
   }

   @Override
   protected void drawGuiContainerForegroundLayer(int var1, int var2) {
      this.fontRenderer
         .drawString(
            this.field_94080_s.isInvNameLocalized() ? this.field_94080_s.getInvName() : StatCollector.translateToLocal(this.field_94080_s.getInvName()),
            8,
            6,
            4210752
         );
      this.fontRenderer
         .drawString(
            this.field_94081_r.isInvNameLocalized() ? this.field_94081_r.getInvName() : StatCollector.translateToLocal(this.field_94081_r.getInvName()),
            8,
            this.ySize - 96 + 2,
            4210752
         );
   }

   @Override
   protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3) {
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      this.mc.renderEngine.bindTexture("/gui/hopper.png");
      int var4 = (this.width - this.xSize) / 2;
      int var5 = (this.height - this.ySize) / 2;
      this.b(var4, var5, 0, 0, this.xSize, this.ySize);
   }
}
