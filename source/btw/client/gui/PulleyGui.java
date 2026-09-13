package btw.client.gui;

import btw.block.tileentity.PulleyTileEntity;
import btw.inventory.container.PulleyContainer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.GuiContainer;
import net.minecraft.src.InventoryPlayer;
import org.lwjgl.opengl.GL11;

@Environment(EnvType.CLIENT)
public class PulleyGui extends GuiContainer {
   static final int PULLEY_GUI_HEIGHT = 174;
   static final int PULLEY_MACHINE_ICON_WIDTH = 14;
   static final int PULLEY_MACHINE_ICON_HEIGHT = 14;
   private PulleyTileEntity associatedTileEntityPulley;

   public PulleyGui(InventoryPlayer inventoryplayer, PulleyTileEntity tileEntityPulley) {
      super(new PulleyContainer(inventoryplayer, tileEntityPulley));
      this.ySize = 174;
      this.associatedTileEntityPulley = tileEntityPulley;
   }

   @Override
   protected void drawGuiContainerForegroundLayer(int i, int j) {
      this.fontRenderer.drawString("Pulley", 75, 6, 4210752);
      this.fontRenderer.drawString("Inventory", 8, this.ySize - 96 + 2, 4210752);
   }

   @Override
   protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      this.mc.renderEngine.bindTexture("/btwmodtex/fcguipulley.png");
      int xPos = (this.width - this.xSize) / 2;
      int yPos = (this.height - this.ySize) / 2;
      this.b(xPos, yPos, 0, 0, this.xSize, this.ySize);
      if (this.associatedTileEntityPulley.mechanicalPowerIndicator > 0) {
         this.b(xPos + 80, yPos + 18, 176, 0, 14, 14);
      }
   }
}
