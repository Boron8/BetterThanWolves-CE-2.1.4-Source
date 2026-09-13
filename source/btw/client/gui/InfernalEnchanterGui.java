package btw.client.gui;

import btw.inventory.container.InfernalEnchanterContainer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.EnchantmentNameParts;
import net.minecraft.src.FontRenderer;
import net.minecraft.src.GuiContainer;
import net.minecraft.src.InventoryPlayer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.RenderHelper;
import net.minecraft.src.StatCollector;
import net.minecraft.src.World;
import org.lwjgl.opengl.GL11;

@Environment(EnvType.CLIENT)
public class InfernalEnchanterGui extends GuiContainer {
   private static final int GUI_HEIGHT = 210;
   private static final int SCROLL_ICON_SCREEN_POS_X = 17;
   private static final int SCROLL_ICON_SCREEN_POS_Y = 37;
   private static final int SCROLL_ICON_BITMAP_POS_X = 176;
   private static final int SCROLL_ICON_BITMAP_POS_Y = 0;
   private static final int ITEM_ICON_SCREEN_POS_X = 17;
   private static final int ITEM_ICON_SCREEN_POS_Y = 75;
   private static final int ITEM_ICON_BITMAP_POS_X = 192;
   private static final int ITEM_ICON_BITMAP_POS_Y = 0;
   private static final int ENCHANTMENT_BUTTONS_POS_X = 60;
   private static final int ENCHANTMENT_BUTTONS_POS_Y = 17;
   private static final int ENCHANTMENT_BUTTONS_HEIGHT = 19;
   private static final int ENCHANTMENT_BUTTONS_WIDTH = 108;
   private static final int ENCHANTMENT_BUTTON_NORMAL_POS_X = 0;
   private static final int ENCHANTMENT_BUTTON_NORMAL_POS_Y = 211;
   private static final int ENCHANTMENT_BUTTON_INACTIVE_POS_X = 0;
   private static final int ENCHANTMENT_BUTTON_INACTIVE_POS_Y = 230;
   private static final int ENCHANTMENT_BUTTON_HIGHLIGHTED_POS_X = 108;
   private static final int ENCHANTMENT_BUTTON_HIGHLIGHTED_POS_Y = 211;
   private InfernalEnchanterContainer container;

   public InfernalEnchanterGui(InventoryPlayer playerInventory, World world, int i, int j, int k) {
      super(new InfernalEnchanterContainer(playerInventory, world, i, j, k));
      this.ySize = 210;
      this.container = (InfernalEnchanterContainer)this.inventorySlots;
   }

   @Override
   public void onGuiClosed() {
      super.onGuiClosed();
   }

   @Override
   public void updateScreen() {
      super.updateScreen();
   }

   @Override
   protected void mouseClicked(int iXClick, int iYClick, int par3) {
      super.mouseClicked(iXClick, iYClick, par3);
      int iBitmapXOffset = (this.width - this.xSize) / 2;
      int iBitmapYOffset = (this.height - this.ySize) / 2;

      for (int iButtonIndex = 0; iButtonIndex < 5; iButtonIndex++) {
         int iRelativeXClick = iXClick - (iBitmapXOffset + 60);
         int iRelativeYClick = iYClick - (iBitmapYOffset + 17 + 19 * iButtonIndex);
         if (iRelativeXClick >= 0 && iRelativeYClick >= 0 && iRelativeXClick < 108 && iRelativeYClick < 19) {
            this.mc.playerController.sendEnchantPacket(this.container.windowId, iButtonIndex);
         }
      }
   }

   @Override
   protected void drawGuiContainerForegroundLayer(int i, int j) {
      this.fontRenderer.drawString("Infernal Enchanter", 40, 5, 4210752);
      this.fontRenderer.drawString("Inventory", 8, this.ySize - 96 + 2, 4210752);

      for (int iTemp = 0; iTemp < 5; iTemp++) {
         String levelString = StatCollector.translateToLocal("enchantment.level." + (iTemp + 1));
         this.fontRenderer.drawString(levelString, 45, 17 + iTemp * 19 + 6, 4210752);
      }
   }

   @Override
   protected void drawGuiContainerBackgroundLayer(float par1, int iMouseX, int iMouseY) {
      RenderHelper.disableStandardItemLighting();
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      this.mc.renderEngine.bindTexture("/btwmodtex/fcguiinfernal.png");
      int xPos = (this.width - this.xSize) / 2;
      int yPos = (this.height - this.ySize) / 2;
      this.b(xPos, yPos, 0, 0, this.xSize, this.ySize);
      this.mc.renderEngine.bindTexture("/btwmodtex/fcguiinfernal.png");
      ItemStack scrollStack = this.container.tableInventory.getStackInSlot(0);
      if (scrollStack == null) {
         this.b(xPos + 17, yPos + 37, 176, 0, 16, 16);
      }

      ItemStack itemStack = this.container.tableInventory.getStackInSlot(1);
      if (itemStack == null) {
         this.b(xPos + 17, yPos + 75, 192, 0, 16, 16);
      }

      EnchantmentNameParts.instance.setRandSeed(this.container.nameSeed);

      for (int iTempButton = 0; iTempButton < 5; iTempButton++) {
         String enchantmentName = EnchantmentNameParts.instance.generateRandomEnchantName();
         int iButtonEnchantmentLevel = this.container.currentEnchantmentLevels[iTempButton];
         boolean bPlayerCapable = iButtonEnchantmentLevel <= this.mc.thePlayer.experienceLevel
            && iButtonEnchantmentLevel <= this.container.maxSurroundingBookshelfLevel;
         int iNameColor = 6839882;
         int iLevelNumberColor = 8453920;
         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
         this.mc.renderEngine.bindTexture("/btwmodtex/fcguiinfernal.png");
         if (iButtonEnchantmentLevel <= 0 || !bPlayerCapable) {
            this.b(xPos + 60, yPos + 17 + 19 * iTempButton, 0, 230, 108, 19);
            iNameColor = (iNameColor & 16711422) >> 1;
            iLevelNumberColor = 4226832;
         } else if (this.isMouseOverEnchantmentButton(iTempButton, iMouseX, iMouseY)) {
            this.b(xPos + 60, yPos + 17 + 19 * iTempButton, 108, 211, 108, 19);
            iNameColor = 16777088;
         }

         if (iButtonEnchantmentLevel > 0) {
            FontRenderer tempFontRenderer = this.mc.standardGalacticFontRenderer;
            tempFontRenderer.drawSplitString(enchantmentName, xPos + 60 + 2, yPos + 2 + 17 + 19 * iTempButton, 104, iNameColor);
            tempFontRenderer = this.mc.fontRenderer;
            String enchantmentLevelString = "" + iButtonEnchantmentLevel;
            tempFontRenderer.drawStringWithShadow(
               enchantmentLevelString,
               xPos + 60 + 108 - 2 - tempFontRenderer.getStringWidth(enchantmentLevelString),
               yPos + 9 + 17 + 19 * iTempButton,
               iLevelNumberColor
            );
         }
      }
   }

   private boolean isMouseOverEnchantmentButton(int iButtonIndex, int iMouseX, int iMouseY) {
      int iBackgroundXPos = (this.width - this.xSize) / 2;
      int iBackgroundYPos = (this.height - this.ySize) / 2;
      int iRelativeMouseX = iMouseX - iBackgroundXPos;
      int iRelativeMouseY = iMouseY - iBackgroundYPos;
      int iButtonYPos = 17 + 19 * iButtonIndex;
      return iRelativeMouseX >= 60 && iRelativeMouseX <= 168 && iRelativeMouseY >= iButtonYPos && iRelativeMouseY <= iButtonYPos + 19;
   }
}
