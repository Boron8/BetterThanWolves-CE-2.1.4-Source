package btw.client.gui;

import btw.block.tileentity.HopperTileEntity;
import btw.inventory.container.HopperContainer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.GuiContainer;
import net.minecraft.src.InventoryPlayer;
import org.lwjgl.opengl.GL11;

@Environment(EnvType.CLIENT)
public class HopperGui extends GuiContainer {
   static final int HOPPER_GUI_HEIGHT = 193;
   static final int HOPPER_MACHINE_ICON_HEIGHT = 14;
   private HopperTileEntity associatedTileEntityHopper;

   public HopperGui(InventoryPlayer inventoryplayer, HopperTileEntity tileentityHopper) {
      super(new HopperContainer(inventoryplayer, tileentityHopper));
      this.ySize = 193;
      this.associatedTileEntityHopper = tileentityHopper;
   }

   @Override
   protected void drawGuiContainerForegroundLayer(int i, int j) {
      this.fontRenderer.drawString("Hopper", 70, 6, 4210752);
      this.fontRenderer.drawString("Inventory", 8, this.ySize - 96 + 2, 4210752);
   }

   @Override
   protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      this.mc.renderEngine.bindTexture("/btwmodtex/fcHopper.png");
      int xPos = (this.width - this.xSize) / 2;
      int yPos = (this.height - this.ySize) / 2;
      this.b(xPos, yPos, 0, 0, this.xSize, this.ySize);
      if (this.associatedTileEntityHopper.mechanicalPowerIndicator > 0) {
         this.b(xPos + 80, yPos + 18, 176, 0, 14, 14);
      }
   }
}
