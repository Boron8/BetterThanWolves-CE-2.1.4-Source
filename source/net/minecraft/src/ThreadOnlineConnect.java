package net.minecraft.src;

import java.net.ConnectException;
import java.net.UnknownHostException;

class ThreadOnlineConnect extends Thread {
   ThreadOnlineConnect(TaskOnlineConnect var1, String var2, int var3) {
      this.field_96594_c = var1;
      this.field_96595_a = var2;
      this.field_96593_b = var3;
   }

   @Override
   public void run() {
      try {
         TaskOnlineConnect.func_96583_a(
            this.field_96594_c,
            new NetClientHandler(this.field_96594_c.b(), this.field_96595_a, this.field_96593_b, TaskOnlineConnect.func_98172_a(this.field_96594_c))
         );
         if (this.field_96594_c.c()) {
            return;
         }

         this.field_96594_c.b(StringTranslate.getInstance().translateKey("mco.connect.authorizing"));
         TaskOnlineConnect.func_96580_a(this.field_96594_c)
            .addToSendQueue(new Packet2ClientProtocol(61, this.field_96594_c.b().session.username, this.field_96595_a, this.field_96593_b));
      } catch (UnknownHostException var2) {
         if (this.field_96594_c.c()) {
            return;
         }

         this.field_96594_c
            .b()
            .displayGuiScreen(
               new GuiScreenDisconnectedOnline(
                  TaskOnlineConnect.func_98172_a(this.field_96594_c), "connect.failed", "disconnect.genericReason", "Unknown host '" + this.field_96595_a + "'"
               )
            );
      } catch (ConnectException var3) {
         if (this.field_96594_c.c()) {
            return;
         }

         this.field_96594_c
            .b()
            .displayGuiScreen(
               new GuiScreenDisconnectedOnline(
                  TaskOnlineConnect.func_98172_a(this.field_96594_c), "connect.failed", "disconnect.genericReason", var3.getMessage()
               )
            );
      } catch (Exception var4) {
         if (this.field_96594_c.c()) {
            return;
         }

         var4.printStackTrace();
         this.field_96594_c
            .b()
            .displayGuiScreen(
               new GuiScreenDisconnectedOnline(
                  TaskOnlineConnect.func_98172_a(this.field_96594_c), "connect.failed", "disconnect.genericReason", var4.toString()
               )
            );
      }
   }
}
