package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Collections;
import java.util.List;
import org.lwjgl.input.Keyboard;

public class GuiMultiplayer extends GuiScreen {
   private static int threadsPending = 0;
   private static Object lock = new Object();
   private GuiScreen parentScreen;
   private GuiSlotServer serverSlotContainer;
   private ServerList internetServerList;
   private int selectedServer = -1;
   private GuiButton field_96289_p;
   private GuiButton buttonSelect;
   private GuiButton buttonDelete;
   private boolean deleteClicked = false;
   private boolean addClicked = false;
   private boolean editClicked = false;
   private boolean directClicked = false;
   private String lagTooltip = null;
   private ServerData theServerData = null;
   private LanServerList localNetworkServerList;
   private ThreadLanServerFind localServerFindThread;
   private int ticksOpened;
   private boolean field_74024_A;
   private List listofLanServers = Collections.emptyList();

   public GuiMultiplayer(GuiScreen var1) {
      this.parentScreen = var1;
   }

   @Override
   public void initGui() {
      Keyboard.enableRepeatEvents(true);
      this.buttonList.clear();
      if (!this.field_74024_A) {
         this.field_74024_A = true;
         this.internetServerList = new ServerList(this.mc);
         this.internetServerList.loadServerList();
         this.localNetworkServerList = new LanServerList();

         try {
            this.localServerFindThread = new ThreadLanServerFind(this.localNetworkServerList);
            this.localServerFindThread.start();
         } catch (Exception var2) {
            this.mc.getLogAgent().logWarning("Unable to start LAN server detection: " + var2.getMessage());
         }

         this.serverSlotContainer = new GuiSlotServer(this);
      } else {
         this.serverSlotContainer.a(this.width, this.height, 32, this.height - 64);
      }

      this.initGuiControls();
   }

   public void initGuiControls() {
      StringTranslate var1 = StringTranslate.getInstance();
      this.buttonList.add(this.field_96289_p = new GuiButton(7, this.width / 2 - 154, this.height - 28, 70, 20, var1.translateKey("selectServer.edit")));
      this.buttonList.add(this.buttonDelete = new GuiButton(2, this.width / 2 - 74, this.height - 28, 70, 20, var1.translateKey("selectServer.delete")));
      this.buttonList.add(this.buttonSelect = new GuiButton(1, this.width / 2 - 154, this.height - 52, 100, 20, var1.translateKey("selectServer.select")));
      this.buttonList.add(new GuiButton(4, this.width / 2 - 50, this.height - 52, 100, 20, var1.translateKey("selectServer.direct")));
      this.buttonList.add(new GuiButton(3, this.width / 2 + 4 + 50, this.height - 52, 100, 20, var1.translateKey("selectServer.add")));
      this.buttonList.add(new GuiButton(8, this.width / 2 + 4, this.height - 28, 70, 20, var1.translateKey("selectServer.refresh")));
      this.buttonList.add(new GuiButton(0, this.width / 2 + 4 + 76, this.height - 28, 75, 20, var1.translateKey("gui.cancel")));
      boolean var2 = this.selectedServer >= 0 && this.selectedServer < this.serverSlotContainer.getSize();
      this.buttonSelect.enabled = var2;
      this.field_96289_p.enabled = var2;
      this.buttonDelete.enabled = var2;
   }

   @Override
   public void updateScreen() {
      super.updateScreen();
      this.ticksOpened++;
      if (this.localNetworkServerList.getWasUpdated()) {
         this.listofLanServers = this.localNetworkServerList.getLanServers();
         this.localNetworkServerList.setWasNotUpdated();
      }
   }

   @Override
   public void onGuiClosed() {
      Keyboard.enableRepeatEvents(false);
      if (this.localServerFindThread != null) {
         this.localServerFindThread.interrupt();
         this.localServerFindThread = null;
      }
   }

   @Override
   protected void actionPerformed(GuiButton var1) {
      if (var1.enabled) {
         if (var1.id == 2) {
            String var2 = this.internetServerList.getServerData(this.selectedServer).serverName;
            if (var2 != null) {
               this.deleteClicked = true;
               StringTranslate var3 = StringTranslate.getInstance();
               String var4 = var3.translateKey("selectServer.deleteQuestion");
               String var5 = "'" + var2 + "' " + var3.translateKey("selectServer.deleteWarning");
               String var6 = var3.translateKey("selectServer.deleteButton");
               String var7 = var3.translateKey("gui.cancel");
               GuiYesNo var8 = new GuiYesNo(this, var4, var5, var6, var7, this.selectedServer);
               this.mc.displayGuiScreen(var8);
            }
         } else if (var1.id == 1) {
            this.joinServer(this.selectedServer);
         } else if (var1.id == 4) {
            this.directClicked = true;
            this.mc
               .displayGuiScreen(
                  new GuiScreenServerList(this, this.theServerData = new ServerData(StatCollector.translateToLocal("selectServer.defaultName"), ""))
               );
         } else if (var1.id == 3) {
            this.addClicked = true;
            this.mc
               .displayGuiScreen(
                  new GuiScreenAddServer(this, this.theServerData = new ServerData(StatCollector.translateToLocal("selectServer.defaultName"), ""))
               );
         } else if (var1.id == 7) {
            this.editClicked = true;
            ServerData var9 = this.internetServerList.getServerData(this.selectedServer);
            this.theServerData = new ServerData(var9.serverName, var9.serverIP);
            this.theServerData.setHideAddress(var9.isHidingAddress());
            this.mc.displayGuiScreen(new GuiScreenAddServer(this, this.theServerData));
         } else if (var1.id == 0) {
            this.mc.displayGuiScreen(this.parentScreen);
         } else if (var1.id == 8) {
            this.mc.displayGuiScreen(new GuiMultiplayer(this.parentScreen));
         } else {
            this.serverSlotContainer.a(var1);
         }
      }
   }

   @Override
   public void confirmClicked(boolean var1, int var2) {
      if (this.deleteClicked) {
         this.deleteClicked = false;
         if (var1) {
            this.internetServerList.removeServerData(var2);
            this.internetServerList.saveServerList();
            this.selectedServer = -1;
         }

         this.mc.displayGuiScreen(this);
      } else if (this.directClicked) {
         this.directClicked = false;
         if (var1) {
            this.connectToServer(this.theServerData);
         } else {
            this.mc.displayGuiScreen(this);
         }
      } else if (this.addClicked) {
         this.addClicked = false;
         if (var1) {
            this.internetServerList.addServerData(this.theServerData);
            this.internetServerList.saveServerList();
            this.selectedServer = -1;
         }

         this.mc.displayGuiScreen(this);
      } else if (this.editClicked) {
         this.editClicked = false;
         if (var1) {
            ServerData var3 = this.internetServerList.getServerData(this.selectedServer);
            var3.serverName = this.theServerData.serverName;
            var3.serverIP = this.theServerData.serverIP;
            var3.setHideAddress(this.theServerData.isHidingAddress());
            this.internetServerList.saveServerList();
         }

         this.mc.displayGuiScreen(this);
      }
   }

   @Override
   protected void keyTyped(char var1, int var2) {
      int var3 = this.selectedServer;
      if (var2 == 59) {
         this.mc.gameSettings.hideServerAddress = !this.mc.gameSettings.hideServerAddress;
         this.mc.gameSettings.saveOptions();
      } else {
         if (p() && var2 == 200) {
            if (var3 > 0 && var3 < this.internetServerList.countServers()) {
               this.internetServerList.swapServers(var3, var3 - 1);
               this.selectedServer--;
               if (var3 < this.internetServerList.countServers() - 1) {
                  this.serverSlotContainer.b(-this.serverSlotContainer.slotHeight);
               }
            }
         } else if (p() && var2 == 208) {
            if (var3 < this.internetServerList.countServers() - 1) {
               this.internetServerList.swapServers(var3, var3 + 1);
               this.selectedServer++;
               if (var3 > 0) {
                  this.serverSlotContainer.b(this.serverSlotContainer.slotHeight);
               }
            }
         } else if (var1 == '\r') {
            this.actionPerformed((GuiButton)this.buttonList.get(2));
         } else {
            super.keyTyped(var1, var2);
         }
      }
   }

   @Override
   public void drawScreen(int var1, int var2, float var3) {
      this.lagTooltip = null;
      StringTranslate var4 = StringTranslate.getInstance();
      this.e();
      this.serverSlotContainer.a(var1, var2, var3);
      this.a(this.fontRenderer, var4.translateKey("multiplayer.title"), this.width / 2, 20, 16777215);
      super.drawScreen(var1, var2, var3);
      if (this.lagTooltip != null) {
         this.func_74007_a(this.lagTooltip, var1, var2);
      }
   }

   private void joinServer(int var1) {
      if (var1 < this.internetServerList.countServers()) {
         this.connectToServer(this.internetServerList.getServerData(var1));
      } else {
         var1 -= this.internetServerList.countServers();
         if (var1 < this.listofLanServers.size()) {
            LanServer var2 = (LanServer)this.listofLanServers.get(var1);
            this.connectToServer(new ServerData(var2.getServerMotd(), var2.getServerIpPort()));
         }
      }
   }

   private void connectToServer(ServerData var1) {
      this.mc.displayGuiScreen(new GuiConnecting(this, this.mc, var1));
   }

   private static void func_74017_b(ServerData var0) {
      ServerAddress var1 = ServerAddress.func_78860_a(var0.serverIP);
      Socket var2 = null;
      DataInputStream var3 = null;
      DataOutputStream var4 = null;

      try {
         var2 = new Socket();
         var2.setSoTimeout(3000);
         var2.setTcpNoDelay(true);
         var2.setTrafficClass(18);
         var2.connect(new InetSocketAddress(var1.getIP(), var1.getPort()), 3000);
         var3 = new DataInputStream(var2.getInputStream());
         var4 = new DataOutputStream(var2.getOutputStream());
         var4.write(254);
         var4.write(1);
         if (var3.read() != 255) {
            throw new IOException("Bad message");
         }

         String var5 = Packet.readString(var3, 256);
         char[] var6 = var5.toCharArray();

         for (int var7 = 0; var7 < var6.length; var7++) {
            if (var6[var7] != 167 && var6[var7] != 0 && ChatAllowedCharacters.allowedCharacters.indexOf(var6[var7]) < 0) {
               var6[var7] = '?';
            }
         }

         var5 = new String(var6);
         if (var5.startsWith("\u00a7") && var5.length() > 1) {
            String[] var29 = var5.substring(1).split("\u0000");
            if (MathHelper.parseIntWithDefault(var29[0], 0) == 1) {
               var0.serverMOTD = var29[3];
               var0.field_82821_f = MathHelper.parseIntWithDefault(var29[1], var0.field_82821_f);
               var0.gameVersion = var29[2];
               int var30 = MathHelper.parseIntWithDefault(var29[4], 0);
               int var31 = MathHelper.parseIntWithDefault(var29[5], 0);
               if (var30 >= 0 && var31 >= 0) {
                  var0.populationInfo = EnumChatFormatting.GRAY + "" + var30 + "" + EnumChatFormatting.DARK_GRAY + "/" + EnumChatFormatting.GRAY + var31;
               } else {
                  var0.populationInfo = "" + EnumChatFormatting.DARK_GRAY + "???";
               }
            } else {
               var0.gameVersion = "???";
               var0.serverMOTD = "" + EnumChatFormatting.DARK_GRAY + "???";
               var0.field_82821_f = 62;
               var0.populationInfo = "" + EnumChatFormatting.DARK_GRAY + "???";
            }
         } else {
            String[] var28 = var5.split("\u00a7");
            Object var27 = var28[0];
            int var8 = -1;
            int var9 = -1;

            try {
               var8 = Integer.parseInt(var28[1]);
               var9 = Integer.parseInt(var28[2]);
            } catch (Exception var24) {
            }

            var0.serverMOTD = EnumChatFormatting.GRAY + var27;
            if (var8 >= 0 && var9 > 0) {
               var0.populationInfo = EnumChatFormatting.GRAY + "" + var8 + "" + EnumChatFormatting.DARK_GRAY + "/" + EnumChatFormatting.GRAY + var9;
            } else {
               var0.populationInfo = "" + EnumChatFormatting.DARK_GRAY + "???";
            }

            var0.gameVersion = "1.3";
            var0.field_82821_f = 60;
         }
      } finally {
         try {
            if (var3 != null) {
               var3.close();
            }
         } catch (Throwable var23) {
         }

         try {
            if (var4 != null) {
               var4.close();
            }
         } catch (Throwable var22) {
         }

         try {
            if (var2 != null) {
               var2.close();
            }
         } catch (Throwable var21) {
         }
      }
   }

   protected void func_74007_a(String var1, int var2, int var3) {
      if (var1 != null) {
         int var4 = var2 + 12;
         int var5 = var3 - 12;
         int var6 = this.fontRenderer.getStringWidth(var1);
         this.a(var4 - 3, var5 - 3, var4 + var6 + 3, var5 + 8 + 3, -1073741824, -1073741824);
         this.fontRenderer.drawStringWithShadow(var1, var4, var5, -1);
      }
   }
}
