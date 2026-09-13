package net.minecraft.src;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

class ThreadConnectToOnlineServer extends Thread {
   ThreadConnectToOnlineServer(GuiSlotOnlineServerList var1, McoServer var2) {
      this.field_96596_b = var1;
      this.field_96597_a = var2;
   }

   @Override
   public void run() {
      try {
         if (!this.field_96597_a.field_96411_l) {
            this.field_96597_a.field_96411_l = true;
            this.field_96597_a.field_96412_m = -2L;
            this.field_96597_a.field_96414_k = "";
            GuiScreenOnlineServers.func_101014_j();
            long var1 = System.nanoTime();
            GuiScreenOnlineServers.func_101002_a(this.field_96596_b.field_96294_a, this.field_96597_a);
            long var3 = System.nanoTime();
            this.field_96597_a.field_96412_m = (var3 - var1) / 1000000L;
         } else if (this.field_96597_a.field_102022_m) {
            this.field_96597_a.field_102022_m = false;
            GuiScreenOnlineServers.func_101002_a(this.field_96596_b.field_96294_a, this.field_96597_a);
         }
      } catch (UnknownHostException var28) {
         this.field_96597_a.field_96412_m = -1L;
      } catch (SocketTimeoutException var29) {
         this.field_96597_a.field_96412_m = -1L;
      } catch (ConnectException var30) {
         this.field_96597_a.field_96412_m = -1L;
      } catch (IOException var31) {
         this.field_96597_a.field_96412_m = -1L;
      } catch (Exception var32) {
         this.field_96597_a.field_96412_m = -1L;
      } finally {
         synchronized (GuiScreenOnlineServers.func_101007_h()) {
            GuiScreenOnlineServers.func_101013_k();
         }
      }
   }
}
