package btw.client.gui;

import btw.block.tileentity.dispenser.BlockDispenserTileEntity;
import btw.inventory.container.BlockDispenserContainer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.GuiContainer;
import net.minecraft.src.InventoryPlayer;
import org.lwjgl.opengl.GL11;

@Environment(EnvType.CLIENT)
public class BlockDispenserGui extends GuiContainer {
   static final int SELECTION_ICON_HEIGHT = 20;
   static final int GUI_HEIGHT = 182;
   private BlockDispenserTileEntity associatedTileEntityBlockDispenser;

   public BlockDispenserGui(InventoryPlayer inventoryplayer, BlockDispenserTileEntity tileentitydispenser) {
      super(new BlockDispenserContainer(inventoryplayer, tileentitydispenser));
      this.associatedTileEntityBlockDispenser = tileentitydispenser;
      this.ySize = 182;
   }

   @Override
   protected void drawGuiContainerForegroundLayer(int i, int j) {
      this.fontRenderer.drawString("Block Dispenser", 48, 6, 4210752);
      this.fontRenderer.drawString("Inventory", 8, this.ySize - 94 + 2, 4210752);
   }

   @Override
   protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      this.mc.renderEngine.bindTexture("/btwmodtex/fcguiblockdisp.png");
      int xPos = (this.width - this.xSize) / 2;
      int yPos = (this.height - this.ySize) / 2;
      this.b(xPos, yPos, 0, 0, this.xSize, this.ySize);
      int iXOffset = this.associatedTileEntityBlockDispenser.nextSlotIndexToDispense % 4 * 18;
      int iYOffset = this.associatedTileEntityBlockDispenser.nextSlotIndexToDispense / 4 * 18;
      this.b(xPos + 51 + iXOffset, yPos + 15 + iYOffset, 176, 0, 20, 20);
   }
}
