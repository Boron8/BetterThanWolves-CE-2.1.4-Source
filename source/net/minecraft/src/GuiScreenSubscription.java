package net.minecraft.src;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import org.lwjgl.input.Keyboard;

public class GuiScreenSubscription extends GuiScreen {
   private final GuiScreen field_98067_a;
   private final McoServer field_98065_b;
   private final int field_98066_c = 0;
   private final int field_98064_d = 1;
   private int field_98068_n;
   private String field_98069_o;

   public GuiScreenSubscription(GuiScreen var1, McoServer var2) {
      this.field_98067_a = var1;
      this.field_98065_b = var2;
   }

   @Override
   public void updateScreen() {
   }

   @Override
   public void initGui() {
      this.func_98063_a(this.field_98065_b.field_96408_a);
      StringTranslate var1 = StringTranslate.getInstance();
      Keyboard.enableRepeatEvents(true);
      this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 120 + 12, var1.translateKey("gui.cancel")));
   }

   private void func_98063_a(long var1) {
      McoClient var3 = new McoClient(this.mc.session);

      try {
         ValueObjectSubscription var4 = var3.func_98177_f(var1);
         this.field_98068_n = var4.field_98170_b;
         this.field_98069_o = this.func_98062_b(var4.field_98171_a);
      } catch (ExceptionMcoService var5) {
      } catch (IOException var6) {
      }
   }

   private String func_98062_b(long var1) {
      GregorianCalendar var3 = new GregorianCalendar(TimeZone.getDefault());
      var3.setTimeInMillis(var1);
      return SimpleDateFormat.getDateTimeInstance().format(var3.getTime());
   }

   @Override
   public void onGuiClosed() {
      Keyboard.enableRepeatEvents(false);
   }

   @Override
   protected void actionPerformed(GuiButton var1) {
      if (var1.enabled) {
         if (var1.id == 0) {
            this.mc.displayGuiScreen(this.field_98067_a);
         } else if (var1.id == 1) {
         }
      }
   }

   @Override
   protected void keyTyped(char var1, int var2) {
   }

   @Override
   protected void mouseClicked(int var1, int var2, int var3) {
      super.mouseClicked(var1, var2, var3);
   }

   @Override
   public void drawScreen(int var1, int var2, float var3) {
      StringTranslate var4 = StringTranslate.getInstance();
      this.e();
      this.a(this.fontRenderer, var4.translateKey("mco.configure.world.subscription.title"), this.width / 2, 17, 16777215);
      this.b(this.fontRenderer, var4.translateKey("mco.configure.world.subscription.start"), this.width / 2 - 100, 53, 10526880);
      this.b(this.fontRenderer, this.field_98069_o, this.width / 2 - 100, 66, 16777215);
      this.b(this.fontRenderer, var4.translateKey("mco.configure.world.subscription.daysleft"), this.width / 2 - 100, 85, 10526880);
      this.b(this.fontRenderer, String.valueOf(this.field_98068_n), this.width / 2 - 100, 98, 16777215);
      super.drawScreen(var1, var2, var3);
   }
}
