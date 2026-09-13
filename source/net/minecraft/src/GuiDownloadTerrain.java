package net.minecraft.src;

public class GuiDownloadTerrain extends GuiScreen {
   private NetClientHandler netHandler;
   private int updateCounter = 0;

   public GuiDownloadTerrain(NetClientHandler var1) {
      this.netHandler = var1;
   }

   @Override
   protected void keyTyped(char var1, int var2) {
   }

   @Override
   public void initGui() {
      this.buttonList.clear();
   }

   @Override
   public void updateScreen() {
      this.updateCounter++;
      if (this.updateCounter % 20 == 0) {
         this.netHandler.addToSendQueue(new Packet0KeepAlive());
      }

      if (this.netHandler != null) {
         this.netHandler.processReadPackets();
      }
   }

   @Override
   public void drawScreen(int var1, int var2, float var3) {
      this.c(0);
      StringTranslate var4 = StringTranslate.getInstance();
      this.a(this.fontRenderer, var4.translateKey("multiplayer.downloadingTerrain"), this.width / 2, this.height / 2 - 50, 16777215);
      super.drawScreen(var1, var2, var3);
   }
}
