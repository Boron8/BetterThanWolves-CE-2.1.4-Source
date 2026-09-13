package net.minecraft.src;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

class ThreadPollServers extends Thread {
   ThreadPollServers(GuiSlotServer var1, ServerData var2) {
      this.serverSlotContainer = var1;
      this.pollServersServerData = var2;
   }

   @Override
   public void run() {
      try {
         this.pollServersServerData.serverMOTD = EnumChatFormatting.DARK_GRAY + "Polling..";
         long var1 = System.nanoTime();
         GuiMultiplayer.func_82291_a(this.pollServersServerData);
         long var3 = System.nanoTime();
         this.pollServersServerData.pingToServer = (var3 - var1) / 1000000L;
      } catch (UnknownHostException var28) {
         this.pollServersServerData.pingToServer = -1L;
         this.pollServersServerData.serverMOTD = EnumChatFormatting.DARK_RED + "Can't resolve hostname";
      } catch (SocketTimeoutException var29) {
         this.pollServersServerData.pingToServer = -1L;
         this.pollServersServerData.serverMOTD = EnumChatFormatting.DARK_RED + "Can't reach server";
      } catch (ConnectException var30) {
         this.pollServersServerData.pingToServer = -1L;
         this.pollServersServerData.serverMOTD = EnumChatFormatting.DARK_RED + "Can't reach server";
      } catch (IOException var31) {
         this.pollServersServerData.pingToServer = -1L;
         this.pollServersServerData.serverMOTD = EnumChatFormatting.DARK_RED + "Communication error";
      } catch (Exception var32) {
         this.pollServersServerData.pingToServer = -1L;
         this.pollServersServerData.serverMOTD = "ERROR: " + var32.getClass();
      } finally {
         synchronized (GuiMultiplayer.getLock()) {
            GuiMultiplayer.decreaseThreadsPending();
         }
      }
   }
}
