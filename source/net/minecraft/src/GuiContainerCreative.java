package net.minecraft.src;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class GuiContainerCreative extends InventoryEffectRenderer {
   private static InventoryBasic inventory = new InventoryBasic("tmp", true, 45);
   private static int selectedTabIndex = CreativeTabs.tabBlock.getTabIndex();
   private float currentScroll = 0.0F;
   private boolean isScrolling = false;
   private boolean wasClicking;
   private GuiTextField searchField;
   private List backupContainerSlots;
   private Slot field_74235_v = null;
   private boolean field_74234_w = false;
   private CreativeCrafting field_82324_x;

   public GuiContainerCreative(EntityPlayer var1) {
      super(new ContainerCreative(var1));
      var1.openContainer = this.inventorySlots;
      this.allowUserInput = true;
      var1.addStat(AchievementList.openInventory, 1);
      this.ySize = 136;
      this.xSize = 195;
   }

   @Override
   public void updateScreen() {
      if (!this.mc.playerController.isInCreativeMode()) {
         this.mc.displayGuiScreen(new GuiInventory(this.mc.thePlayer));
      }
   }

   @Override
   protected void handleMouseClick(Slot var1, int var2, int var3, int var4) {
      this.field_74234_w = true;
      boolean var5 = var4 == 1;
      var4 = var2 == -999 && var4 == 0 ? 4 : var4;
      if (var1 == null && selectedTabIndex != CreativeTabs.tabInventory.getTabIndex() && var4 != 5) {
         InventoryPlayer var15 = this.mc.thePlayer.inventory;
         if (var15.getItemStack() != null) {
            if (var3 == 0) {
               this.mc.thePlayer.c(var15.getItemStack());
               this.mc.playerController.func_78752_a(var15.getItemStack());
               var15.setItemStack(null);
            }

            if (var3 == 1) {
               ItemStack var17 = var15.getItemStack().splitStack(1);
               this.mc.thePlayer.c(var17);
               this.mc.playerController.func_78752_a(var17);
               if (var15.getItemStack().stackSize == 0) {
                  var15.setItemStack(null);
               }
            }
         }
      } else if (var1 == this.field_74235_v && var5) {
         for (int var14 = 0; var14 < this.mc.thePlayer.inventoryContainer.getInventory().size(); var14++) {
            this.mc.playerController.sendSlotPacket(null, var14);
         }
      } else if (selectedTabIndex == CreativeTabs.tabInventory.getTabIndex()) {
         if (var1 == this.field_74235_v) {
            this.mc.thePlayer.inventory.setItemStack(null);
         } else if (var4 == 4 && var1 != null && var1.getHasStack()) {
            ItemStack var6 = var1.decrStackSize(var3 == 0 ? 1 : var1.getStack().getMaxStackSize());
            this.mc.thePlayer.c(var6);
            this.mc.playerController.func_78752_a(var6);
         } else if (var4 == 4 && this.mc.thePlayer.inventory.getItemStack() != null) {
            this.mc.thePlayer.c(this.mc.thePlayer.inventory.getItemStack());
            this.mc.playerController.func_78752_a(this.mc.thePlayer.inventory.getItemStack());
            this.mc.thePlayer.inventory.setItemStack(null);
         } else {
            this.mc
               .thePlayer
               .inventoryContainer
               .slotClick(var1 == null ? var2 : SlotCreativeInventory.func_75240_a((SlotCreativeInventory)var1).slotNumber, var3, var4, this.mc.thePlayer);
            this.mc.thePlayer.inventoryContainer.detectAndSendChanges();
         }
      } else if (var4 != 5 && var1.inventory == inventory) {
         InventoryPlayer var13 = this.mc.thePlayer.inventory;
         ItemStack var7 = var13.getItemStack();
         ItemStack var8 = var1.getStack();
         if (var4 == 2) {
            if (var8 != null && var3 >= 0 && var3 < 9) {
               ItemStack var19 = var8.copy();
               var19.stackSize = var19.getMaxStackSize();
               this.mc.thePlayer.inventory.setInventorySlotContents(var3, var19);
               this.mc.thePlayer.inventoryContainer.detectAndSendChanges();
            }

            return;
         }

         if (var4 == 3) {
            if (var13.getItemStack() == null && var1.getHasStack()) {
               ItemStack var18 = var1.getStack().copy();
               var18.stackSize = var18.getMaxStackSize();
               var13.setItemStack(var18);
            }

            return;
         }

         if (var4 == 4) {
            if (var8 != null) {
               ItemStack var9 = var8.copy();
               var9.stackSize = var3 == 0 ? 1 : var9.getMaxStackSize();
               this.mc.thePlayer.c(var9);
               this.mc.playerController.func_78752_a(var9);
            }

            return;
         }

         if (var7 != null && var8 != null && var7.isItemEqual(var8)) {
            if (var3 == 0) {
               if (var5) {
                  var7.stackSize = var7.getMaxStackSize();
               } else if (var7.stackSize < var7.getMaxStackSize()) {
                  var7.stackSize++;
               }
            } else if (var7.stackSize <= 1) {
               var13.setItemStack(null);
            } else {
               var7.stackSize--;
            }
         } else if (var8 != null && var7 == null) {
            var13.setItemStack(ItemStack.copyItemStack(var8));
            var7 = var13.getItemStack();
            if (var5) {
               var7.stackSize = var7.getMaxStackSize();
            }
         } else {
            var13.setItemStack(null);
         }
      } else {
         this.inventorySlots.slotClick(var1 == null ? var2 : var1.slotNumber, var3, var4, this.mc.thePlayer);
         if (Container.func_94532_c(var3) == 2) {
            for (int var11 = 0; var11 < 9; var11++) {
               this.mc.playerController.sendSlotPacket(this.inventorySlots.getSlot(45 + var11).getStack(), 36 + var11);
            }
         } else if (var1 != null) {
            ItemStack var12 = this.inventorySlots.getSlot(var1.slotNumber).getStack();
            this.mc.playerController.sendSlotPacket(var12, var1.slotNumber - this.inventorySlots.inventorySlots.size() + 9 + 36);
         }
      }
   }

   @Override
   public void initGui() {
      if (this.mc.playerController.isInCreativeMode()) {
         super.initGui();
         this.buttonList.clear();
         Keyboard.enableRepeatEvents(true);
         this.searchField = new GuiTextField(this.fontRenderer, this.guiLeft + 82, this.guiTop + 6, 89, this.fontRenderer.FONT_HEIGHT);
         this.searchField.setMaxStringLength(15);
         this.searchField.setEnableBackgroundDrawing(false);
         this.searchField.setVisible(false);
         this.searchField.setTextColor(16777215);
         int var1 = selectedTabIndex;
         selectedTabIndex = -1;
         this.setCurrentCreativeTab(CreativeTabs.creativeTabArray[var1]);
         this.field_82324_x = new CreativeCrafting(this.mc);
         this.mc.thePlayer.inventoryContainer.addCraftingToCrafters(this.field_82324_x);
      } else {
         this.mc.displayGuiScreen(new GuiInventory(this.mc.thePlayer));
      }
   }

   @Override
   public void onGuiClosed() {
      super.b();
      if (this.mc.thePlayer != null && this.mc.thePlayer.inventory != null) {
         this.mc.thePlayer.inventoryContainer.removeCraftingFromCrafters(this.field_82324_x);
      }

      Keyboard.enableRepeatEvents(false);
   }

   @Override
   protected void keyTyped(char var1, int var2) {
      if (selectedTabIndex != CreativeTabs.tabAllSearch.getTabIndex()) {
         if (GameSettings.isKeyDown(this.mc.gameSettings.keyBindChat)) {
            this.setCurrentCreativeTab(CreativeTabs.tabAllSearch);
         } else {
            super.a(var1, var2);
         }
      } else {
         if (this.field_74234_w) {
            this.field_74234_w = false;
            this.searchField.setText("");
         }

         if (!this.a(var2)) {
            if (this.searchField.textboxKeyTyped(var1, var2)) {
               this.updateCreativeSearch();
            } else {
               super.a(var1, var2);
            }
         }
      }
   }

   private void updateCreativeSearch() {
      ContainerCreative var1 = (ContainerCreative)this.inventorySlots;
      var1.itemList.clear();

      for (Item var5 : Item.itemsList) {
         if (var5 != null && var5.getCreativeTab() != null) {
            var5.getSubItems(var5.itemID, null, var1.itemList);
         }
      }

      for (Enchantment var14 : Enchantment.enchantmentsList) {
         if (var14 != null && var14.type != null) {
            Item.enchantedBook.func_92113_a(var14, var1.itemList);
         }
      }

      Iterator var9 = var1.itemList.iterator();
      String var11 = this.searchField.getText().toLowerCase();

      while (var9.hasNext()) {
         ItemStack var13 = (ItemStack)var9.next();
         boolean var15 = false;

         for (String var7 : var13.getTooltip(this.mc.thePlayer, this.mc.gameSettings.advancedItemTooltips)) {
            if (var7.toLowerCase().contains(var11)) {
               var15 = true;
               break;
            }
         }

         if (!var15) {
            var9.remove();
         }
      }

      this.currentScroll = 0.0F;
      var1.scrollTo(0.0F);
   }

   @Override
   protected void drawGuiContainerForegroundLayer(int var1, int var2) {
      CreativeTabs var3 = CreativeTabs.creativeTabArray[selectedTabIndex];
      if (var3.drawInForegroundOfTab()) {
         this.fontRenderer.drawString(var3.getTranslatedTabLabel(), 8, 6, 4210752);
      }
   }

   @Override
   protected void mouseClicked(int var1, int var2, int var3) {
      if (var3 == 0) {
         int var4 = var1 - this.guiLeft;
         int var5 = var2 - this.guiTop;

         for (CreativeTabs var9 : CreativeTabs.creativeTabArray) {
            if (this.func_74232_a(var9, var4, var5)) {
               return;
            }
         }
      }

      super.a(var1, var2, var3);
   }

   @Override
   protected void mouseMovedOrUp(int var1, int var2, int var3) {
      if (var3 == 0) {
         int var4 = var1 - this.guiLeft;
         int var5 = var2 - this.guiTop;

         for (CreativeTabs var9 : CreativeTabs.creativeTabArray) {
            if (this.func_74232_a(var9, var4, var5)) {
               this.setCurrentCreativeTab(var9);
               return;
            }
         }
      }

      super.b(var1, var2, var3);
   }

   private boolean needsScrollBars() {
      return selectedTabIndex != CreativeTabs.tabInventory.getTabIndex()
         && CreativeTabs.creativeTabArray[selectedTabIndex].shouldHidePlayerInventory()
         && ((ContainerCreative)this.inventorySlots).hasMoreThan1PageOfItemsInList();
   }

   private void setCurrentCreativeTab(CreativeTabs var1) {
      int var2 = selectedTabIndex;
      selectedTabIndex = var1.getTabIndex();
      ContainerCreative var3 = (ContainerCreative)this.inventorySlots;
      this.field_94077_p.clear();
      var3.itemList.clear();
      var1.displayAllReleventItems(var3.itemList);
      if (var1 == CreativeTabs.tabInventory) {
         Container var4 = this.mc.thePlayer.inventoryContainer;
         if (this.backupContainerSlots == null) {
            this.backupContainerSlots = var3.inventorySlots;
         }

         var3.inventorySlots = new ArrayList();

         for (int var5 = 0; var5 < var4.inventorySlots.size(); var5++) {
            SlotCreativeInventory var6 = new SlotCreativeInventory(this, (Slot)var4.inventorySlots.get(var5), var5);
            var3.inventorySlots.add(var6);
            if (var5 >= 5 && var5 < 9) {
               int var10 = var5 - 5;
               int var11 = var10 / 2;
               int var12 = var10 % 2;
               var6.xDisplayPosition = 9 + var11 * 54;
               var6.yDisplayPosition = 6 + var12 * 27;
            } else if (var5 >= 0 && var5 < 5) {
               var6.yDisplayPosition = -2000;
               var6.xDisplayPosition = -2000;
            } else if (var5 < var4.inventorySlots.size()) {
               int var7 = var5 - 9;
               int var8 = var7 % 9;
               int var9 = var7 / 9;
               var6.xDisplayPosition = 9 + var8 * 18;
               if (var5 >= 36) {
                  var6.yDisplayPosition = 112;
               } else {
                  var6.yDisplayPosition = 54 + var9 * 18;
               }
            }
         }

         this.field_74235_v = new Slot(inventory, 0, 173, 112);
         var3.inventorySlots.add(this.field_74235_v);
      } else if (var2 == CreativeTabs.tabInventory.getTabIndex()) {
         var3.inventorySlots = this.backupContainerSlots;
         this.backupContainerSlots = null;
      }

      if (this.searchField != null) {
         if (var1 == CreativeTabs.tabAllSearch) {
            this.searchField.setVisible(true);
            this.searchField.setCanLoseFocus(false);
            this.searchField.setFocused(true);
            this.searchField.setText("");
            this.updateCreativeSearch();
         } else {
            this.searchField.setVisible(false);
            this.searchField.setCanLoseFocus(true);
            this.searchField.setFocused(false);
         }
      }

      this.currentScroll = 0.0F;
      var3.scrollTo(0.0F);
   }

   @Override
   public void handleMouseInput() {
      super.d();
      int var1 = Mouse.getEventDWheel();
      if (var1 != 0 && this.needsScrollBars()) {
         int var2 = ((ContainerCreative)this.inventorySlots).itemList.size() / 9 - 5 + 1;
         if (var1 > 0) {
            var1 = 1;
         }

         if (var1 < 0) {
            var1 = -1;
         }

         this.currentScroll = (float)(this.currentScroll - (double)var1 / var2);
         if (this.currentScroll < 0.0F) {
            this.currentScroll = 0.0F;
         }

         if (this.currentScroll > 1.0F) {
            this.currentScroll = 1.0F;
         }

         ((ContainerCreative)this.inventorySlots).scrollTo(this.currentScroll);
      }
   }

   @Override
   public void drawScreen(int var1, int var2, float var3) {
      boolean var4 = Mouse.isButtonDown(0);
      int var5 = this.guiLeft;
      int var6 = this.guiTop;
      int var7 = var5 + 175;
      int var8 = var6 + 18;
      int var9 = var7 + 14;
      int var10 = var8 + 112;
      if (!this.wasClicking && var4 && var1 >= var7 && var2 >= var8 && var1 < var9 && var2 < var10) {
         this.isScrolling = this.needsScrollBars();
      }

      if (!var4) {
         this.isScrolling = false;
      }

      this.wasClicking = var4;
      if (this.isScrolling) {
         this.currentScroll = (var2 - var8 - 7.5F) / (var10 - var8 - 15.0F);
         if (this.currentScroll < 0.0F) {
            this.currentScroll = 0.0F;
         }

         if (this.currentScroll > 1.0F) {
            this.currentScroll = 1.0F;
         }

         ((ContainerCreative)this.inventorySlots).scrollTo(this.currentScroll);
      }

      super.drawScreen(var1, var2, var3);

      for (CreativeTabs var14 : CreativeTabs.creativeTabArray) {
         if (this.renderCreativeInventoryHoveringText(var14, var1, var2)) {
            break;
         }
      }

      if (this.field_74235_v != null
         && selectedTabIndex == CreativeTabs.tabInventory.getTabIndex()
         && this.c(this.field_74235_v.xDisplayPosition, this.field_74235_v.yDisplayPosition, 16, 16, var1, var2)) {
         this.a(StringTranslate.getInstance().translateKey("inventory.binSlot"), var1, var2);
      }

      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      GL11.glDisable(2896);
   }

   @Override
   protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3) {
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      RenderHelper.enableGUIStandardItemLighting();
      CreativeTabs var4 = CreativeTabs.creativeTabArray[selectedTabIndex];

      for (CreativeTabs var8 : CreativeTabs.creativeTabArray) {
         this.mc.renderEngine.bindTexture("/gui/allitems.png");
         if (var8.getTabIndex() != selectedTabIndex) {
            this.renderCreativeTab(var8);
         }
      }

      this.mc.renderEngine.bindTexture("/gui/creative_inv/" + var4.getBackgroundImageName());
      this.b(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
      this.searchField.drawTextBox();
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      int var9 = this.guiLeft + 175;
      int var10 = this.guiTop + 18;
      int var11 = var10 + 112;
      this.mc.renderEngine.bindTexture("/gui/allitems.png");
      if (var4.shouldHidePlayerInventory()) {
         this.b(var9, var10 + (int)((var11 - var10 - 17) * this.currentScroll), 232 + (this.needsScrollBars() ? 0 : 12), 0, 12, 15);
      }

      this.renderCreativeTab(var4);
      if (var4 == CreativeTabs.tabInventory) {
         GuiInventory.drawPlayerOnGui(this.mc, this.guiLeft + 43, this.guiTop + 45, 20, this.guiLeft + 43 - var2, this.guiTop + 45 - 30 - var3);
      }
   }

   protected boolean func_74232_a(CreativeTabs var1, int var2, int var3) {
      int var4 = var1.getTabColumn();
      int var5 = 28 * var4;
      int var6 = 0;
      if (var4 == 5) {
         var5 = this.xSize - 28 + 2;
      } else if (var4 > 0) {
         var5 += var4;
      }

      if (var1.isTabInFirstRow()) {
         var6 -= 32;
      } else {
         var6 += this.ySize;
      }

      return var2 >= var5 && var2 <= var5 + 28 && var3 >= var6 && var3 <= var6 + 32;
   }

   protected boolean renderCreativeInventoryHoveringText(CreativeTabs var1, int var2, int var3) {
      int var4 = var1.getTabColumn();
      int var5 = 28 * var4;
      int var6 = 0;
      if (var4 == 5) {
         var5 = this.xSize - 28 + 2;
      } else if (var4 > 0) {
         var5 += var4;
      }

      if (var1.isTabInFirstRow()) {
         var6 -= 32;
      } else {
         var6 += this.ySize;
      }

      if (this.c(var5 + 3, var6 + 3, 23, 27, var2, var3)) {
         this.a(var1.getTranslatedTabLabel(), var2, var3);
         return true;
      } else {
         return false;
      }
   }

   protected void renderCreativeTab(CreativeTabs var1) {
      boolean var2 = var1.getTabIndex() == selectedTabIndex;
      boolean var3 = var1.isTabInFirstRow();
      int var4 = var1.getTabColumn();
      int var5 = var4 * 28;
      byte var6 = 0;
      int var7 = this.guiLeft + 28 * var4;
      int var8 = this.guiTop;
      byte var9 = 32;
      if (var2) {
         var6 += 32;
      }

      if (var4 == 5) {
         var7 = this.guiLeft + this.xSize - 28;
      } else if (var4 > 0) {
         var7 += var4;
      }

      if (var3) {
         var8 -= 28;
      } else {
         var6 += 64;
         var8 += this.ySize - 4;
      }

      GL11.glDisable(2896);
      this.b(var7, var8, var5, var6, 28, var9);
      this.zLevel = 100.0F;
      selectedButton.zLevel = 100.0F;
      var7 += 6;
      var8 += 8 + (var3 ? 1 : -1);
      GL11.glEnable(2896);
      GL11.glEnable(32826);
      ItemStack var10 = new ItemStack(var1.getTabIconItem());
      selectedButton.renderItemAndEffectIntoGUI(this.fontRenderer, this.mc.renderEngine, var10, var7, var8);
      selectedButton.renderItemOverlayIntoGUI(this.fontRenderer, this.mc.renderEngine, var10, var7, var8);
      GL11.glDisable(2896);
      selectedButton.zLevel = 0.0F;
      this.zLevel = 0.0F;
   }

   @Override
   protected void actionPerformed(GuiButton var1) {
      if (var1.id == 0) {
         this.mc.displayGuiScreen(new GuiAchievements(this.mc.statFileWriter));
      }

      if (var1.id == 1) {
         this.mc.displayGuiScreen(new GuiStats(this, this.mc.statFileWriter));
      }
   }

   public int func_74230_h() {
      return selectedTabIndex;
   }
}
