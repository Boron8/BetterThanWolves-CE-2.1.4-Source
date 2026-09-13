package net.minecraft.src;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

class TaskWorldCreation extends TaskLongRunning {
   private final String field_96589_c;
   private final String field_96587_d;
   private final String field_96588_e;
   private final String field_104065_f;

   public TaskWorldCreation(GuiScreenCreateOnlineWorld var1, String var2, String var3, String var4, String var5) {
      this.field_96590_a = var1;
      this.field_96589_c = var2;
      this.field_96587_d = var3;
      this.field_96588_e = var4;
      this.field_104065_f = var5;
   }

   @Override
   public void run() {
      String var1 = StringTranslate.getInstance().translateKey("mco.create.world.wait");
      this.b(var1);
      McoClient var2 = new McoClient(GuiScreenCreateOnlineWorld.func_96248_a(this.field_96590_a).session);

      try {
         var2.func_96386_a(this.field_96589_c, this.field_96587_d, this.field_96588_e, this.field_104065_f);
         GuiScreenCreateOnlineWorld.func_96246_c(this.field_96590_a).displayGuiScreen(GuiScreenCreateOnlineWorld.func_96247_b(this.field_96590_a));
      } catch (ExceptionMcoService var4) {
         this.a(var4.field_96391_b);
      } catch (UnsupportedEncodingException var5) {
      } catch (IOException var6) {
      } catch (Exception var7) {
         this.a("Failed");
      }
   }
}
