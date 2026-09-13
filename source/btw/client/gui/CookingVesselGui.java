package btw.client.gui;

import btw.block.tileentity.CookingVesselTileEntity;
import btw.inventory.BTWContainers;
import btw.inventory.container.CookingVesselContainer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.GuiContainer;
import net.minecraft.src.InventoryPlayer;
import org.lwjgl.opengl.GL11;

@Environment(EnvType.CLIENT)
public class CookingVesselGui extends GuiContainer {
   static final int GUI_HEIGHT = 193;
   static final int FIRE_ICON_HEIGHT = 12;
   private CookingVesselTileEntity associatedTileEntity;
   private int containerID;

   public CookingVesselGui(InventoryPlayer inventoryplayer, CookingVesselTileEntity tileEntity, int iContainerID) {
      super(new CookingVesselContainer(inventoryplayer, tileEntity));
      this.ySize = 193;
      this.associatedTileEntity = tileEntity;
      this.containerID = iContainerID;
   }

   @Override
   protected void drawGuiContainerForegroundLayer(int i, int j) {
      if (this.containerID == BTWContainers.crucibleContainerID) {
         this.fontRenderer.drawString("Crucible", 66, 6, 4210752);
      } else {
         this.fontRenderer.drawString("Cauldron", 66, 6, 4210752);
      }

      this.fontRenderer.drawString("Inventory", 8, this.ySize - 96 + 2, 4210752);
   }

   @Override
   protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      this.mc.renderEngine.bindTexture("/btwmodtex/fccauldron.png");
      int xPos = (this.width - this.xSize) / 2;
      int yPos = (this.height - this.ySize) / 2;
      this.b(xPos, yPos, 0, 0, this.xSize, this.ySize);
      if (this.associatedTileEntity.isCooking()) {
         int scaledIconHeight = this.associatedTileEntity.getCookProgressScaled(12);
         this.b(xPos + 81, yPos + 19 + 12 - scaledIconHeight, 176, 12 - scaledIconHeight, 14, scaledIconHeight + 2);
      }
   }
}
