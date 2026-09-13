package btw.client.gui;

import btw.block.tileentity.LoomTileEntity;
import btw.inventory.container.LoomContainer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.GuiContainer;
import net.minecraft.src.InventoryPlayer;
import org.lwjgl.opengl.GL11;

@Environment(EnvType.CLIENT)
public class LoomGui extends GuiContainer {
   static final int HOPPER_GUI_HEIGHT = 193;
   static final int HOPPER_MACHINE_ICON_HEIGHT = 14;
   private final LoomTileEntity loomEntity;

   public LoomGui(InventoryPlayer inventoryplayer, LoomTileEntity loomEntity) {
      super(new LoomContainer(inventoryplayer, loomEntity));
      this.ySize = 193;
      this.loomEntity = loomEntity;
   }

   @Override
   protected void drawGuiContainerForegroundLayer(int i, int j) {
      this.fontRenderer.drawString("Loom", 70, 6, 4210752);
      this.fontRenderer.drawString("Inventory", 8, this.ySize - 96 + 2, 4210752);
   }

   @Override
   protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      this.mc.renderEngine.bindTexture("/btwmodtex/fcLoom.png");
      int xPos = (this.width - this.xSize) / 2;
      int yPos = (this.height - this.ySize) / 2;
      this.b(xPos, yPos, 0, 0, this.xSize, this.ySize);
   }
}
