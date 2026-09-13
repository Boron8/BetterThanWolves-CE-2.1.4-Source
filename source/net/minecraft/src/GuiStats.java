package net.minecraft.src;

import org.lwjgl.opengl.GL11;

public class GuiStats extends GuiScreen {
   private static RenderItem renderItem = new RenderItem();
   protected GuiScreen parentGui;
   protected String statsTitle = "Select world";
   private GuiSlotStatsGeneral slotGeneral;
   private GuiSlotStatsItem slotItem;
   private GuiSlotStatsBlock slotBlock;
   private StatFileWriter statFileWriter;
   private GuiSlot selectedSlot = null;

   public GuiStats(GuiScreen var1, StatFileWriter var2) {
      this.parentGui = var1;
      this.statFileWriter = var2;
   }

   @Override
   public void initGui() {
      this.statsTitle = StatCollector.translateToLocal("gui.stats");
      this.slotGeneral = new GuiSlotStatsGeneral(this);
      this.slotGeneral.a(this.buttonList, 1, 1);
      this.slotItem = new GuiSlotStatsItem(this);
      this.slotItem.a(this.buttonList, 1, 1);
      this.slotBlock = new GuiSlotStatsBlock(this);
      this.slotBlock.a(this.buttonList, 1, 1);
      this.selectedSlot = this.slotGeneral;
      this.addHeaderButtons();
   }

   public void addHeaderButtons() {
      StringTranslate var1 = StringTranslate.getInstance();
      this.buttonList.add(new GuiButton(0, this.width / 2 + 4, this.height - 28, 150, 20, var1.translateKey("gui.done")));
      this.buttonList.add(new GuiButton(1, this.width / 2 - 154, this.height - 52, 100, 20, var1.translateKey("stat.generalButton")));
      GuiButton var2;
      this.buttonList.add(var2 = new GuiButton(2, this.width / 2 - 46, this.height - 52, 100, 20, var1.translateKey("stat.blocksButton")));
      GuiButton var3;
      this.buttonList.add(var3 = new GuiButton(3, this.width / 2 + 62, this.height - 52, 100, 20, var1.translateKey("stat.itemsButton")));
      if (this.slotBlock.a() == 0) {
         var2.enabled = false;
      }

      if (this.slotItem.a() == 0) {
         var3.enabled = false;
      }
   }

   @Override
   protected void actionPerformed(GuiButton var1) {
      if (var1.enabled) {
         if (var1.id == 0) {
            this.mc.displayGuiScreen(this.parentGui);
         } else if (var1.id == 1) {
            this.selectedSlot = this.slotGeneral;
         } else if (var1.id == 3) {
            this.selectedSlot = this.slotItem;
         } else if (var1.id == 2) {
            this.selectedSlot = this.slotBlock;
         } else {
            this.selectedSlot.actionPerformed(var1);
         }
      }
   }

   @Override
   public void drawScreen(int var1, int var2, float var3) {
      this.selectedSlot.drawScreen(var1, var2, var3);
      this.a(this.fontRenderer, this.statsTitle, this.width / 2, 20, 16777215);
      super.drawScreen(var1, var2, var3);
   }

   private void drawItemSprite(int var1, int var2, int var3) {
      this.drawButtonBackground(var1 + 1, var2 + 1);
      GL11.glEnable(32826);
      RenderHelper.enableGUIStandardItemLighting();
      renderItem.renderItemIntoGUI(this.fontRenderer, this.mc.renderEngine, new ItemStack(var3, 1, 0), var1 + 2, var2 + 2);
      RenderHelper.disableStandardItemLighting();
      GL11.glDisable(32826);
   }

   private void drawButtonBackground(int var1, int var2) {
      this.drawSprite(var1, var2, 0, 0);
   }

   private void drawSprite(int var1, int var2, int var3, int var4) {
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      this.mc.renderEngine.bindTexture("/gui/slot.png");
      Tessellator var9 = Tessellator.instance;
      var9.startDrawingQuads();
      var9.addVertexWithUV(var1 + 0, var2 + 18, this.zLevel, (var3 + 0) * 0.0078125F, (var4 + 18) * 0.0078125F);
      var9.addVertexWithUV(var1 + 18, var2 + 18, this.zLevel, (var3 + 18) * 0.0078125F, (var4 + 18) * 0.0078125F);
      var9.addVertexWithUV(var1 + 18, var2 + 0, this.zLevel, (var3 + 18) * 0.0078125F, (var4 + 0) * 0.0078125F);
      var9.addVertexWithUV(var1 + 0, var2 + 0, this.zLevel, (var3 + 0) * 0.0078125F, (var4 + 0) * 0.0078125F);
      var9.draw();
   }
}
