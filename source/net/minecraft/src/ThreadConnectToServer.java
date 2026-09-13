package net.minecraft.src;

import java.net.ConnectException;
import java.net.UnknownHostException;

class ThreadConnectToServer extends Thread {
   ThreadConnectToServer(GuiConnecting var1, String var2, int var3) {
      this.connectingGui = var1;
      this.ip = var2;
      this.port = var3;
   }

   @Override
   public void run() {
      try {
         GuiConnecting.setNetClientHandler(this.connectingGui, new NetClientHandler(GuiConnecting.func_74256_a(this.connectingGui), this.ip, this.port));
         if (GuiConnecting.isCancelled(this.connectingGui)) {
            return;
         }

         GuiConnecting.getNetClientHandler(this.connectingGui)
            .addToSendQueue(new Packet2ClientProtocol(61, GuiConnecting.func_74254_c(this.connectingGui).session.username, this.ip, this.port));
      } catch (UnknownHostException var2) {
         if (GuiConnecting.isCancelled(this.connectingGui)) {
            return;
         }

         GuiConnecting.func_74250_f(this.connectingGui)
            .displayGuiScreen(
               new GuiDisconnected(
                  GuiConnecting.func_98097_e(this.connectingGui), "connect.failed", "disconnect.genericReason", "Unknown host '" + this.ip + "'"
               )
            );
      } catch (ConnectException var3) {
         if (GuiConnecting.isCancelled(this.connectingGui)) {
            return;
         }

         GuiConnecting.func_74251_g(this.connectingGui)
            .displayGuiScreen(
               new GuiDisconnected(GuiConnecting.func_98097_e(this.connectingGui), "connect.failed", "disconnect.genericReason", var3.getMessage())
            );
      } catch (Exception var4) {
         if (GuiConnecting.isCancelled(this.connectingGui)) {
            return;
         }

         var4.printStackTrace();
         GuiConnecting.func_98096_h(this.connectingGui)
            .displayGuiScreen(
               new GuiDisconnected(GuiConnecting.func_98097_e(this.connectingGui), "connect.failed", "disconnect.genericReason", var4.toString())
            );
      }
   }
}
