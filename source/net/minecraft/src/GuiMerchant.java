package net.minecraft.src;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.lwjgl.opengl.GL11;

@Environment(EnvType.CLIENT)
public class GuiMerchant extends GuiContainer {
   private IMerchant theIMerchant;
   private GuiButtonMerchant nextRecipeButtonIndex;
   private GuiButtonMerchant previousRecipeButtonIndex;
   private int currentRecipeIndex = 0;
   private String field_94082_v;
   private int currentNumValidRecipes = 0;
   private int validRecipesScrollOffset = 0;

   public GuiMerchant(InventoryPlayer par1, IMerchant par2, World par3World, String par4) {
      super(new ContainerMerchant(par1, par2, par3World));
      this.theIMerchant = par2;
      this.field_94082_v = par4 != null && par4.length() >= 1 ? par4 : StatCollector.translateToLocal("entity.Villager.name");
      this.ySize = 239;
   }

   @Override
   public void initGui() {
      super.initGui();
      int var1 = (this.width - this.xSize) / 2;
      int var2 = (this.height - this.ySize) / 2;
      this.buttonList.add(this.nextRecipeButtonIndex = new GuiButtonMerchant(1, var1 + 144, var2 + 118, true));
      this.buttonList.add(this.previousRecipeButtonIndex = new GuiButtonMerchant(2, var1 + 34 - 14, var2 + 118, false));
      this.nextRecipeButtonIndex.drawButton = false;
      this.previousRecipeButtonIndex.drawButton = false;
      this.nextRecipeButtonIndex.enabled = false;
      this.previousRecipeButtonIndex.enabled = false;
   }

   @Override
   protected void drawGuiContainerForegroundLayer(int par1, int par2) {
      this.fontRenderer.drawString(this.field_94082_v, this.xSize / 2 - this.fontRenderer.getStringWidth(this.field_94082_v) / 2, 6, 4210752);
      this.fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 8, this.ySize - 96 + 2, 4210752);
   }

   @Override
   protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      this.mc.renderEngine.bindTexture("/btwmodtex/fcguitrading.png");
      int var4 = (this.width - this.xSize) / 2;
      int var5 = (this.height - this.ySize) / 2;
      this.b(var4, var5, 0, 0, this.xSize, this.ySize);
      MerchantRecipeList var6 = this.theIMerchant.getRecipes(this.mc.thePlayer);
      if (var6 != null && !var6.isEmpty()) {
         int var7 = this.currentRecipeIndex;
         MerchantRecipe var8 = (MerchantRecipe)var6.get(var7);
         if (var8.func_82784_g()) {
            this.mc.renderEngine.bindTexture("/btwmodtex/fcguitrading.png");
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glDisable(2896);
            this.b(this.guiLeft + 83, this.guiTop + 21, 212, 0, 28, 21);
            this.b(this.guiLeft + 83, this.guiTop + 51, 212, 0, 28, 21);
         }
      }

      this.drawXPDisplay();
   }

   public IMerchant getIMerchant() {
      return this.theIMerchant;
   }

   @Override
   public void drawScreen(int iMouseX, int iMouseY, float fMysteryVariable) {
      super.drawScreen(iMouseX, iMouseY, fMysteryVariable);
      MerchantRecipeList recipeList = this.theIMerchant.getRecipes(this.mc.thePlayer);
      ContainerMerchant associatedContainer = this.getAssociatedContainerMerchant();
      if (recipeList != null && !recipeList.isEmpty() && associatedContainer != null) {
         int iGuiX = (this.width - this.xSize) / 2;
         int iGuiY = (this.height - this.ySize) / 2;
         GL11.glPushMatrix();
         RenderHelper.enableGUIStandardItemLighting();
         GL11.glDisable(2896);
         GL11.glEnable(32826);
         GL11.glEnable(2903);
         int iNumRecipes = recipeList.size();
         if (iNumRecipes > 8) {
            iNumRecipes = 8;
         }

         ItemStack tooltipStack = null;
         int iOffsetY = 18;

         for (int iTempRecipeIndex = 0; iTempRecipeIndex < iNumRecipes; iTempRecipeIndex++) {
            int iOffsetX = 8;
            if (iTempRecipeIndex % 2 == 1) {
               iOffsetX = 98;
            }

            MerchantRecipe tempRecipe = (MerchantRecipe)recipeList.get(iTempRecipeIndex);
            GL11.glEnable(2896);
            ItemStack inputStack1 = tempRecipe.getItemToBuy();
            ItemStack inputStack2 = tempRecipe.getSecondItemToBuy();
            ItemStack outputStack = tempRecipe.getItemToSell();
            selectedButton.zLevel = 100.0F;
            selectedButton.renderItemAndEffectIntoGUI(this.fontRenderer, this.mc.renderEngine, inputStack1, iGuiX + iOffsetX, iGuiY + iOffsetY);
            selectedButton.renderItemOverlayIntoGUI(this.fontRenderer, this.mc.renderEngine, inputStack1, iGuiX + iOffsetX, iGuiY + iOffsetY);
            if (inputStack2 != null) {
               selectedButton.renderItemAndEffectIntoGUI(this.fontRenderer, this.mc.renderEngine, inputStack2, iGuiX + iOffsetX + 18, iGuiY + iOffsetY);
               selectedButton.renderItemOverlayIntoGUI(this.fontRenderer, this.mc.renderEngine, inputStack2, iGuiX + iOffsetX + 18, iGuiY + iOffsetY);
            }

            selectedButton.renderItemAndEffectIntoGUI(this.fontRenderer, this.mc.renderEngine, outputStack, iGuiX + iOffsetX + 54, iGuiY + iOffsetY);
            selectedButton.renderItemOverlayIntoGUI(this.fontRenderer, this.mc.renderEngine, outputStack, iGuiX + iOffsetX + 54, iGuiY + iOffsetY);
            selectedButton.zLevel = 0.0F;
            GL11.glDisable(2896);
            this.mc.renderEngine.bindTexture("/btwmodtex/fcguitrading.png");
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            if (!tempRecipe.func_82784_g()) {
               this.b(iGuiX + iOffsetX + 36, iGuiY + iOffsetY, 176, 38, 16, 16);
               String sXPString = null;
               if (tempRecipe.tradeLevel < 0) {
                  if (-tempRecipe.tradeLevel == associatedContainer.associatedVillagerTradeLevel) {
                     sXPString = "++";
                  }
               } else if (tempRecipe.tradeLevel == associatedContainer.associatedVillagerTradeLevel && tempRecipe.tradeLevel < 5 && !tempRecipe.isMandatory()) {
                  sXPString = "+";
               }

               if (sXPString != null) {
                  int iLevelScreenX = iGuiX + iOffsetX + 45 - this.mc.fontRenderer.getStringWidth(sXPString) / 2;
                  int iLevelScreenY = iGuiY + iOffsetY + 5;
                  this.mc.fontRenderer.drawString(sXPString, iLevelScreenX + 1, iLevelScreenY, 0);
                  this.mc.fontRenderer.drawString(sXPString, iLevelScreenX - 1, iLevelScreenY, 0);
                  this.mc.fontRenderer.drawString(sXPString, iLevelScreenX, iLevelScreenY + 1, 0);
                  this.mc.fontRenderer.drawString(sXPString, iLevelScreenX, iLevelScreenY - 1, 0);
                  this.mc.fontRenderer.drawString(sXPString, iLevelScreenX, iLevelScreenY, 8453920);
               }
            } else {
               this.b(iGuiX + iOffsetX + 36, iGuiY + iOffsetY, 191, 38, 16, 16);
            }

            if (this.c(iOffsetX, iOffsetY, 16, 16, iMouseX, iMouseY)) {
               tooltipStack = inputStack1;
            } else if (inputStack2 != null && this.c(iOffsetX + 18, iOffsetY, 16, 16, iMouseX, iMouseY)) {
               tooltipStack = inputStack2;
            } else if (this.c(iOffsetX + 53, iOffsetY, 16, 16, iMouseX, iMouseY)) {
               tooltipStack = outputStack;
            }

            if (iTempRecipeIndex % 2 == 1) {
               iOffsetY += 18;
            }
         }

         if (tooltipStack != null) {
            this.a(tooltipStack, iMouseX, iMouseY);
         }

         GL11.glPopMatrix();
         GL11.glEnable(2896);
         GL11.glEnable(2929);
         RenderHelper.enableStandardItemLighting();
      }
   }

   ContainerMerchant getAssociatedContainerMerchant() {
      return this.inventorySlots != null && this.inventorySlots instanceof ContainerMerchant ? (ContainerMerchant)this.inventorySlots : null;
   }

   private void drawXPDisplay() {
      ContainerMerchant associatedContainer = this.getAssociatedContainerMerchant();
      if (associatedContainer != null && associatedContainer.associatedVillagerTradeLevel > 0) {
         this.mc.renderEngine.bindTexture("/btwmodtex/fcguitrading.png");
         int xPos = (this.width - this.xSize) / 2;
         int yPos = (this.height - this.ySize) / 2;
         if (associatedContainer.associatedVillagerTradeMaxXP > 0) {
            int iXPBarIconWidth = 151;
            float fXPBarScale = (float)associatedContainer.associatedVillagerTradeXP / associatedContainer.associatedVillagerTradeMaxXP;
            if (associatedContainer.associatedVillagerTradeLevel >= 5) {
               fXPBarScale = 1.0F;
            }

            int iScaledIconWidth = (int)(fXPBarScale * iXPBarIconWidth);
            if (iScaledIconWidth > 0) {
               this.b(xPos + 12, yPos + 99, 0, 251, iScaledIconWidth, 5);
            }
         }

         String sLevelString = "" + associatedContainer.associatedVillagerTradeLevel;
         if (associatedContainer.associatedVillagerTradeLevel >= 5) {
            sLevelString = "Max";
         }

         int iLevelScreenX = xPos + 88 - this.mc.fontRenderer.getStringWidth(sLevelString) / 2;
         int iLevelScreenY = yPos + 93;
         this.mc.fontRenderer.drawString(sLevelString, iLevelScreenX + 1, iLevelScreenY, 0);
         this.mc.fontRenderer.drawString(sLevelString, iLevelScreenX - 1, iLevelScreenY, 0);
         this.mc.fontRenderer.drawString(sLevelString, iLevelScreenX, iLevelScreenY + 1, 0);
         this.mc.fontRenderer.drawString(sLevelString, iLevelScreenX, iLevelScreenY - 1, 0);
         this.mc.fontRenderer.drawString(sLevelString, iLevelScreenX, iLevelScreenY, 8453920);
      }
   }

   private int isEmeraldOnlyBuyTrade(MerchantRecipe recipe) {
      if (recipe.getSecondItemToBuy() == null) {
         ItemStack firstItem = recipe.getItemToBuy();
         if (firstItem != null && firstItem.itemID == Item.emerald.itemID) {
            return firstItem.stackSize;
         }
      }

      return 0;
   }

   @Override
   public void updateScreen() {
      super.updateScreen();
      int iOldCurrentRecipe = this.currentRecipeIndex;
      MerchantRecipeList recipeList = this.theIMerchant.getRecipes(this.mc.thePlayer);
      ContainerMerchant associatedContainer = this.getAssociatedContainerMerchant();
      this.currentNumValidRecipes = 0;
      if (recipeList != null && !recipeList.isEmpty() && associatedContainer != null) {
         InventoryMerchant merchantInventory = associatedContainer.getMerchantInventory();
         ItemStack playerStack1 = merchantInventory.getStackInSlot(0);
         ItemStack playerStack2 = merchantInventory.getStackInSlot(1);
         if (playerStack1 != null || playerStack2 != null) {
            int iNumRecipes = recipeList.size();

            for (int iTempRecipeIndex = 0; iTempRecipeIndex < iNumRecipes; iTempRecipeIndex++) {
               if (playerStack1 != null && recipeList.canRecipeBeUsed(playerStack1, playerStack2, iTempRecipeIndex) != null
                  || playerStack2 != null && recipeList.canRecipeBeUsed(playerStack2, playerStack1, iTempRecipeIndex) != null) {
                  this.currentNumValidRecipes++;
                  MerchantRecipe tempRecipe = (MerchantRecipe)recipeList.get(iTempRecipeIndex);
                  int iTempEmeraldTrade = this.isEmeraldOnlyBuyTrade(tempRecipe);
                  if (iTempEmeraldTrade <= 0) {
                     this.validRecipesScrollOffset = 0;
                     this.currentRecipeIndex = iTempRecipeIndex;
                     break;
                  }

                  if (this.currentNumValidRecipes <= this.validRecipesScrollOffset + 1) {
                     this.currentRecipeIndex = iTempRecipeIndex;
                  }
               }
            }

            if (iOldCurrentRecipe != this.currentRecipeIndex) {
               associatedContainer.setCurrentRecipeIndex(this.currentRecipeIndex);
               ByteArrayOutputStream var3 = new ByteArrayOutputStream();
               DataOutputStream var4 = new DataOutputStream(var3);

               try {
                  var4.writeInt(this.currentRecipeIndex);
                  this.mc.getNetHandler().addToSendQueue(new Packet250CustomPayload("MC|TrSel", var3.toByteArray()));
               } catch (Exception var11) {
                  var11.printStackTrace();
               }
            }
         }
      }

      if (this.currentNumValidRecipes <= 0 || this.validRecipesScrollOffset >= this.currentNumValidRecipes) {
         this.validRecipesScrollOffset = 0;
      }

      if (this.currentNumValidRecipes > 1) {
         this.nextRecipeButtonIndex.drawButton = true;
         this.previousRecipeButtonIndex.drawButton = true;
         this.nextRecipeButtonIndex.enabled = this.validRecipesScrollOffset < this.currentNumValidRecipes - 1;
         this.previousRecipeButtonIndex.enabled = this.validRecipesScrollOffset > 0;
      } else {
         this.nextRecipeButtonIndex.drawButton = false;
         this.previousRecipeButtonIndex.drawButton = false;
         this.nextRecipeButtonIndex.enabled = false;
         this.previousRecipeButtonIndex.enabled = false;
      }
   }

   @Override
   protected void actionPerformed(GuiButton button) {
      if (button == this.nextRecipeButtonIndex) {
         if (this.validRecipesScrollOffset < this.currentNumValidRecipes - 1) {
            this.validRecipesScrollOffset++;
         }
      } else if (button == this.previousRecipeButtonIndex && this.validRecipesScrollOffset > 0) {
         this.validRecipesScrollOffset--;
      }
   }
}
