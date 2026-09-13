package btw.client.gui;

import btw.client.render.util.RenderUtils;
import btw.inventory.container.WorkbenchContainer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.CraftingManager;
import net.minecraft.src.GuiContainer;
import net.minecraft.src.IRecipe;
import net.minecraft.src.InventoryPlayer;
import net.minecraft.src.Slot;
import net.minecraft.src.StatCollector;
import net.minecraft.src.World;
import org.lwjgl.opengl.GL11;

@Environment(EnvType.CLIENT)
public class CraftingGuiWorkbench extends GuiContainer {
   private WorkbenchContainer container = (WorkbenchContainer)this.inventorySlots;

   public CraftingGuiWorkbench(InventoryPlayer inventory, World world, int i, int j, int k) {
      super(new WorkbenchContainer(inventory, world, i, j, k));
   }

   @Override
   protected void drawGuiContainerForegroundLayer(int par1, int par2) {
      this.fontRenderer.drawString(StatCollector.translateToLocal("container.crafting"), 28, 6, 4210752);
      this.fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 8, this.ySize - 96 + 2, 4210752);
      this.drawSecondaryOutputIndicator();
   }

   @Override
   protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      this.mc.renderEngine.bindTexture("/gui/crafting.png");
      int var4 = (this.width - this.xSize) / 2;
      int var5 = (this.height - this.ySize) / 2;
      this.b(var4, var5, 0, 0, this.xSize, this.ySize);
   }

   private void drawSecondaryOutputIndicator() {
      IRecipe recipe = CraftingManager.getInstance().findMatchingIRecipe(this.container.craftMatrix, this.mc.theWorld);
      if (recipe != null && recipe.hasSecondaryOutput()) {
         Slot outputSlot = (Slot)this.container.inventorySlots.get(0);
         int iDisplayX = outputSlot.xDisplayPosition + 24;
         int iDisplayY = outputSlot.yDisplayPosition + 5;
         RenderUtils.drawSecondaryCraftingOutputIndicator(this.mc, iDisplayX, iDisplayY);
      }
   }
}
