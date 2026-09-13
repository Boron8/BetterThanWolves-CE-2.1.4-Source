package btw.client.gui;

import btw.inventory.container.HamperContainer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.GuiContainer;
import net.minecraft.src.IInventory;
import net.minecraft.src.InventoryPlayer;
import net.minecraft.src.StatCollector;
import org.lwjgl.opengl.GL11;

@Environment(EnvType.CLIENT)
public class HamperGui extends GuiContainer {
   private static final int HAMPER_GUI_HEIGHT = 149;
   private IInventory hamperInventory;

   public HamperGui(InventoryPlayer playerInventory, IInventory hamperInventory) {
      super(new HamperContainer(playerInventory, hamperInventory));
      this.ySize = 149;
      this.hamperInventory = hamperInventory;
   }

   @Override
   protected void drawGuiContainerForegroundLayer(int i, int j) {
      String windowName = StatCollector.translateToLocal(this.hamperInventory.getInvName());
      this.fontRenderer.drawString(windowName, this.xSize / 2 - this.fontRenderer.getStringWidth(windowName) / 2, 6, 4210752);
      this.fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 8, this.ySize - 96 + 2, 4210752);
   }

   @Override
   protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      this.mc.renderEngine.bindTexture("/btwmodtex/fcGuiInv4.png");
      int xPos = (this.width - this.xSize) / 2;
      int yPos = (this.height - this.ySize) / 2;
      this.b(xPos, yPos, 0, 0, this.xSize, this.ySize);
   }
}
