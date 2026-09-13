package net.minecraft.src;

import java.io.IOException;

class ThreadTitleScreen extends Thread {
   ThreadTitleScreen(GuiMainMenu var1, StringTranslate var2, int var3, int var4) {
      this.field_98132_d = var1;
      this.field_98135_a = var2;
      this.field_98133_b = var3;
      this.field_98134_c = var4;
   }

   @Override
   public void run() {
      McoClient var1 = new McoClient(GuiMainMenu.func_98058_a(this.field_98132_d).session);
      boolean var2 = false;

      for (int var3 = 0; var3 < 3; var3++) {
         try {
            Boolean var4 = var1.func_96375_b();
            if (var4) {
               GuiMainMenu.func_98061_a(this.field_98132_d, this.field_98135_a, this.field_98133_b, this.field_98134_c);
            }

            GuiMainMenu.func_98059_a(var4);
         } catch (ExceptionRetryCall var6) {
            var2 = true;
         } catch (ExceptionMcoService var7) {
         } catch (IOException var8) {
         }

         if (!var2) {
            break;
         }

         try {
            Thread.sleep(10000L);
         } catch (InterruptedException var5) {
            Thread.currentThread().interrupt();
         }
      }
   }
}
