package net.minecraft.src;

import org.lwjgl.opengl.GL11;

public class GuiFurnace extends GuiContainer {
   private TileEntityFurnace furnaceInventory;

   public GuiFurnace(InventoryPlayer var1, TileEntityFurnace var2) {
      super(new ContainerFurnace(var1, var2));
      this.furnaceInventory = var2;
   }

   @Override
   protected void drawGuiContainerForegroundLayer(int var1, int var2) {
      String var3 = this.furnaceInventory.isInvNameLocalized()
         ? this.furnaceInventory.getInvName()
         : StatCollector.translateToLocal(this.furnaceInventory.getInvName());
      this.fontRenderer.drawString(var3, this.xSize / 2 - this.fontRenderer.getStringWidth(var3) / 2, 6, 4210752);
      this.fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 8, this.ySize - 96 + 2, 4210752);
   }

   @Override
   protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3) {
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      this.mc.renderEngine.bindTexture("/gui/furnace.png");
      int var4 = (this.width - this.xSize) / 2;
      int var5 = (this.height - this.ySize) / 2;
      this.b(var4, var5, 0, 0, this.xSize, this.ySize);
      if (this.furnaceInventory.isBurning()) {
         int var6 = this.furnaceInventory.getBurnTimeRemainingScaled(12);
         this.b(var4 + 56, var5 + 36 + 12 - var6, 176, 12 - var6, 14, var6 + 2);
      }

      int var7 = this.furnaceInventory.getCookProgressScaled(24);
      this.b(var4 + 79, var5 + 34, 176, 14, var7 + 1, 16);
   }
}
