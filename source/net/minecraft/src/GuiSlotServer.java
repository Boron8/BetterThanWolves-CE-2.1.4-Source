package net.minecraft.src;

import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;

class GuiSlotServer extends GuiSlot {
   public GuiSlotServer(GuiMultiplayer var1) {
      super(var1.mc, var1.width, var1.height, 32, var1.height - 64, 36);
      this.parentGui = var1;
   }

   @Override
   protected int getSize() {
      return GuiMultiplayer.getInternetServerList(this.parentGui).countServers() + GuiMultiplayer.getListOfLanServers(this.parentGui).size() + 1;
   }

   @Override
   protected void elementClicked(int var1, boolean var2) {
      if (var1 < GuiMultiplayer.getInternetServerList(this.parentGui).countServers() + GuiMultiplayer.getListOfLanServers(this.parentGui).size()) {
         int var3 = GuiMultiplayer.getSelectedServer(this.parentGui);
         GuiMultiplayer.getAndSetSelectedServer(this.parentGui, var1);
         ServerData var4 = GuiMultiplayer.getInternetServerList(this.parentGui).countServers() > var1
            ? GuiMultiplayer.getInternetServerList(this.parentGui).getServerData(var1)
            : null;
         boolean var5 = GuiMultiplayer.getSelectedServer(this.parentGui) >= 0
            && GuiMultiplayer.getSelectedServer(this.parentGui) < this.getSize()
            && (var4 == null || var4.field_82821_f == 61);
         boolean var6 = GuiMultiplayer.getSelectedServer(this.parentGui) < GuiMultiplayer.getInternetServerList(this.parentGui).countServers();
         GuiMultiplayer.getButtonSelect(this.parentGui).enabled = var5;
         GuiMultiplayer.getButtonEdit(this.parentGui).enabled = var6;
         GuiMultiplayer.getButtonDelete(this.parentGui).enabled = var6;
         if (var2 && var5) {
            GuiMultiplayer.func_74008_b(this.parentGui, var1);
         } else if (var6 && GuiScreen.isShiftKeyDown() && var3 >= 0 && var3 < GuiMultiplayer.getInternetServerList(this.parentGui).countServers()) {
            GuiMultiplayer.getInternetServerList(this.parentGui).swapServers(var3, GuiMultiplayer.getSelectedServer(this.parentGui));
         }
      }
   }

   @Override
   protected boolean isSelected(int var1) {
      return var1 == GuiMultiplayer.getSelectedServer(this.parentGui);
   }

   @Override
   protected int getContentHeight() {
      return this.getSize() * 36;
   }

   @Override
   protected void drawBackground() {
      this.parentGui.e();
   }

   @Override
   protected void drawSlot(int var1, int var2, int var3, int var4, Tessellator var5) {
      if (var1 < GuiMultiplayer.getInternetServerList(this.parentGui).countServers()) {
         this.func_77247_d(var1, var2, var3, var4, var5);
      } else if (var1 < GuiMultiplayer.getInternetServerList(this.parentGui).countServers() + GuiMultiplayer.getListOfLanServers(this.parentGui).size()) {
         this.func_77248_b(var1, var2, var3, var4, var5);
      } else {
         this.func_77249_c(var1, var2, var3, var4, var5);
      }
   }

   private void func_77248_b(int var1, int var2, int var3, int var4, Tessellator var5) {
      LanServer var6 = (LanServer)GuiMultiplayer.getListOfLanServers(this.parentGui)
         .get(var1 - GuiMultiplayer.getInternetServerList(this.parentGui).countServers());
      this.parentGui.b(this.parentGui.fontRenderer, StatCollector.translateToLocal("lanServer.title"), var2 + 2, var3 + 1, 16777215);
      this.parentGui.b(this.parentGui.fontRenderer, var6.getServerMotd(), var2 + 2, var3 + 12, 8421504);
      if (this.parentGui.mc.gameSettings.hideServerAddress) {
         this.parentGui.b(this.parentGui.fontRenderer, StatCollector.translateToLocal("selectServer.hiddenAddress"), var2 + 2, var3 + 12 + 11, 3158064);
      } else {
         this.parentGui.b(this.parentGui.fontRenderer, var6.getServerIpPort(), var2 + 2, var3 + 12 + 11, 3158064);
      }
   }

   private void func_77249_c(int var1, int var2, int var3, int var4, Tessellator var5) {
      this.parentGui.a(this.parentGui.fontRenderer, StatCollector.translateToLocal("lanServer.scanning"), this.parentGui.width / 2, var3 + 1, 16777215);
      String var6;
      switch (GuiMultiplayer.getTicksOpened(this.parentGui) / 3 % 4) {
         case 0:
         default:
            var6 = "O o o";
            break;
         case 1:
         case 3:
            var6 = "o O o";
            break;
         case 2:
            var6 = "o o O";
      }

      this.parentGui.a(this.parentGui.fontRenderer, var6, this.parentGui.width / 2, var3 + 12, 8421504);
   }

   private void func_77247_d(int var1, int var2, int var3, int var4, Tessellator var5) {
      ServerData var6 = GuiMultiplayer.getInternetServerList(this.parentGui).getServerData(var1);
      synchronized (GuiMultiplayer.getLock()) {
         if (GuiMultiplayer.getThreadsPending() < 5 && !var6.field_78841_f) {
            var6.field_78841_f = true;
            var6.pingToServer = -2L;
            var6.serverMOTD = "";
            var6.populationInfo = "";
            GuiMultiplayer.increaseThreadsPending();
            new ThreadPollServers(this, var6).start();
         }
      }

      boolean var7 = var6.field_82821_f > 61;
      boolean var8 = var6.field_82821_f < 61;
      boolean var9 = var7 || var8;
      this.parentGui.b(this.parentGui.fontRenderer, var6.serverName, var2 + 2, var3 + 1, 16777215);
      this.parentGui.b(this.parentGui.fontRenderer, var6.serverMOTD, var2 + 2, var3 + 12, 8421504);
      this.parentGui
         .b(this.parentGui.fontRenderer, var6.populationInfo, var2 + 215 - this.parentGui.fontRenderer.getStringWidth(var6.populationInfo), var3 + 12, 8421504);
      if (var9) {
         String var10 = EnumChatFormatting.DARK_RED + var6.gameVersion;
         this.parentGui.b(this.parentGui.fontRenderer, var10, var2 + 200 - this.parentGui.fontRenderer.getStringWidth(var10), var3 + 1, 8421504);
      }

      if (!this.parentGui.mc.gameSettings.hideServerAddress && !var6.isHidingAddress()) {
         this.parentGui.b(this.parentGui.fontRenderer, var6.serverIP, var2 + 2, var3 + 12 + 11, 3158064);
      } else {
         this.parentGui.b(this.parentGui.fontRenderer, StatCollector.translateToLocal("selectServer.hiddenAddress"), var2 + 2, var3 + 12 + 11, 3158064);
      }

      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      this.parentGui.mc.renderEngine.bindTexture("/gui/icons.png");
      byte var15 = 0;
      int var11 = 0;
      String var12 = "";
      if (var9) {
         var12 = var7 ? "Client out of date!" : "Server out of date!";
         var11 = 5;
      } else if (var6.field_78841_f && var6.pingToServer != -2L) {
         if (var6.pingToServer < 0L) {
            var11 = 5;
         } else if (var6.pingToServer < 150L) {
            var11 = 0;
         } else if (var6.pingToServer < 300L) {
            var11 = 1;
         } else if (var6.pingToServer < 600L) {
            var11 = 2;
         } else if (var6.pingToServer < 1000L) {
            var11 = 3;
         } else {
            var11 = 4;
         }

         if (var6.pingToServer < 0L) {
            var12 = "(no connection)";
         } else {
            var12 = var6.pingToServer + "ms";
         }
      } else {
         var15 = 1;
         var11 = (int)(Minecraft.getSystemTime() / 100L + var1 * 2 & 7L);
         if (var11 > 4) {
            var11 = 8 - var11;
         }

         var12 = "Polling..";
      }

      this.parentGui.b(var2 + 205, var3, 0 + var15 * 10, 176 + var11 * 8, 10, 8);
      byte var13 = 4;
      if (this.mouseX >= var2 + 205 - var13 && this.mouseY >= var3 - var13 && this.mouseX <= var2 + 205 + 10 + var13 && this.mouseY <= var3 + 8 + var13) {
         GuiMultiplayer.getAndSetLagTooltip(this.parentGui, var12);
      }
   }
}
