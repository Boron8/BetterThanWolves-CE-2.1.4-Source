package btw.client.gui;

import btw.client.render.util.RenderUtils;
import btw.inventory.container.SoulforgeContainer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.CraftingManager;
import net.minecraft.src.GuiContainer;
import net.minecraft.src.IRecipe;
import net.minecraft.src.InventoryPlayer;
import net.minecraft.src.Slot;
import net.minecraft.src.World;
import org.lwjgl.opengl.GL11;

@Environment(EnvType.CLIENT)
public class CraftingGuiSoulforge extends GuiContainer {
   private SoulforgeContainer container = (SoulforgeContainer)this.inventorySlots;
   static final int M_I_GUI_HEIGHT = 184;

   public CraftingGuiSoulforge(InventoryPlayer inventoryplayer, World world, int i, int j, int k) {
      super(new SoulforgeContainer(inventoryplayer, world, i, j, k));
      this.ySize = 184;
   }

   @Override
   protected void drawGuiContainerForegroundLayer(int i, int j) {
      this.fontRenderer.drawString("Soulforge", 22, 6, 4210752);
      this.fontRenderer.drawString("Inventory", 8, this.ySize - 96 + 2, 4210752);
      this.drawSecondaryOutputIndicator();
   }

   @Override
   protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      this.mc.renderEngine.bindTexture("/btwmodtex/fcguianvil.png");
      int xPos = (this.width - this.xSize) / 2;
      int yPos = (this.height - this.ySize) / 2;
      this.b(xPos, yPos, 0, 0, this.xSize, this.ySize);
   }

   private void drawSecondaryOutputIndicator() {
      IRecipe recipe = CraftingManager.getInstance().findMatchingIRecipe(this.container.craftMatrix, this.mc.theWorld);
      if (recipe != null && recipe.hasSecondaryOutput()) {
         Slot outputSlot = (Slot)this.container.inventorySlots.get(0);
         int iDisplayX = outputSlot.xDisplayPosition + 26;
         int iDisplayY = outputSlot.yDisplayPosition + 5;
         RenderUtils.drawSecondaryCraftingOutputIndicator(this.mc, iDisplayX, iDisplayY);
      }
   }
}
