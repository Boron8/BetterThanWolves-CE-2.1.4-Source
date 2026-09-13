package net.minecraft.src;

import org.lwjgl.opengl.GL11;

public class GuiCrafting extends GuiContainer {
   public GuiCrafting(InventoryPlayer var1, World var2, int var3, int var4, int var5) {
      super(new ContainerWorkbench(var1, var2, var3, var4, var5));
   }

   @Override
   protected void drawGuiContainerForegroundLayer(int var1, int var2) {
      this.fontRenderer.drawString(StatCollector.translateToLocal("container.crafting"), 28, 6, 4210752);
      this.fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 8, this.ySize - 96 + 2, 4210752);
   }

   @Override
   protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3) {
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      this.mc.renderEngine.bindTexture("/gui/crafting.png");
      int var4 = (this.width - this.xSize) / 2;
      int var5 = (this.height - this.ySize) / 2;
      this.b(var4, var5, 0, 0, this.xSize, this.ySize);
   }
}
