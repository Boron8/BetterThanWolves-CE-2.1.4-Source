package net.minecraft.src;

import java.io.IOException;
import org.lwjgl.input.Keyboard;

public class GuiScreenConfigureWorld extends GuiScreen {
   private final GuiScreen field_96285_a;
   private McoServer field_96280_b;
   private SelectionListInvited field_96282_c;
   private int field_96277_d;
   private int field_96286_n;
   private int field_96287_o;
   private int field_96284_p = -1;
   private String field_96283_q;
   private GuiButton field_96281_r;
   private GuiButton field_96279_s;
   private GuiButton field_96278_t;
   private GuiButton field_96276_u;
   private GuiButton field_98128_v;
   private GuiButton field_98127_w;
   private GuiButton field_98129_x;
   private boolean field_102020_y;

   public GuiScreenConfigureWorld(GuiScreen var1, McoServer var2) {
      this.field_96285_a = var1;
      this.field_96280_b = var2;
   }

   @Override
   public void updateScreen() {
   }

   @Override
   public void initGui() {
      StringTranslate var1 = StringTranslate.getInstance();
      this.field_96277_d = this.width / 2 - 200;
      this.field_96286_n = 180;
      this.field_96287_o = this.width / 2;
      Keyboard.enableRepeatEvents(true);
      this.buttonList.clear();
      if (this.field_96280_b.field_96404_d.equals("CLOSED")) {
         this.buttonList
            .add(
               this.field_96281_r = new GuiButton(
                  0, this.field_96277_d, this.func_96264_a(12), this.field_96286_n / 2 - 2, 20, var1.translateKey("mco.configure.world.buttons.open")
               )
            );
         this.field_96281_r.enabled = !this.field_96280_b.field_98166_h;
      } else {
         this.buttonList
            .add(
               this.field_96279_s = new GuiButton(
                  1, this.field_96277_d, this.func_96264_a(12), this.field_96286_n / 2 - 2, 20, var1.translateKey("mco.configure.world.buttons.close")
               )
            );
         this.field_96279_s.enabled = !this.field_96280_b.field_98166_h;
      }

      this.buttonList
         .add(
            this.field_98129_x = new GuiButton(
               7,
               this.field_96277_d + this.field_96286_n / 2 + 2,
               this.func_96264_a(12),
               this.field_96286_n / 2 - 2,
               20,
               var1.translateKey("mco.configure.world.buttons.subscription")
            )
         );
      this.buttonList
         .add(
            this.field_96278_t = new GuiButton(
               5, this.field_96277_d, this.func_96264_a(10), this.field_96286_n / 2 - 2, 20, var1.translateKey("mco.configure.world.buttons.edit")
            )
         );
      this.buttonList
         .add(
            this.field_96276_u = new GuiButton(
               6,
               this.field_96277_d + this.field_96286_n / 2 + 2,
               this.func_96264_a(10),
               this.field_96286_n / 2 - 2,
               20,
               var1.translateKey("mco.configure.world.buttons.reset")
            )
         );
      this.buttonList
         .add(
            this.field_98128_v = new GuiButton(
               4, this.field_96287_o, this.func_96264_a(10), this.field_96286_n / 2 - 2, 20, var1.translateKey("mco.configure.world.buttons.invite")
            )
         );
      this.buttonList
         .add(
            this.field_98127_w = new GuiButton(
               3,
               this.field_96287_o + this.field_96286_n / 2 + 2,
               this.func_96264_a(10),
               this.field_96286_n / 2 - 2,
               20,
               var1.translateKey("mco.configure.world.buttons.uninvite")
            )
         );
      this.buttonList.add(new GuiButton(10, this.field_96287_o, this.func_96264_a(12), this.field_96286_n, 20, var1.translateKey("gui.back")));
      this.field_96282_c = new SelectionListInvited(this);
      this.field_96278_t.enabled = !this.field_96280_b.field_98166_h;
      this.field_96276_u.enabled = !this.field_96280_b.field_98166_h;
      this.field_98128_v.enabled = !this.field_96280_b.field_98166_h;
      this.field_98127_w.enabled = !this.field_96280_b.field_98166_h;
   }

   private int func_96264_a(int var1) {
      return 40 + var1 * 13;
   }

   @Override
   public void onGuiClosed() {
      Keyboard.enableRepeatEvents(false);
   }

   @Override
   protected void actionPerformed(GuiButton var1) {
      if (var1.enabled) {
         if (var1.id == 10) {
            if (this.field_102020_y) {
               ((GuiScreenOnlineServers)this.field_96285_a).func_102018_a(this.field_96280_b.field_96408_a);
            }

            this.mc.displayGuiScreen(this.field_96285_a);
         } else if (var1.id == 5) {
            this.mc.displayGuiScreen(new GuiScreenEditOnlineWorld(this, this.field_96285_a, this.field_96280_b));
         } else if (var1.id == 1) {
            StringTranslate var2 = StringTranslate.getInstance();
            String var3 = var2.translateKey("mco.configure.world.close.question.line1");
            String var4 = var2.translateKey("mco.configure.world.close.question.line2");
            this.mc.displayGuiScreen(new GuiScreenConfirmation(this, "Warning!", var3, var4, 1));
         } else if (var1.id == 0) {
            this.func_96268_g();
         } else if (var1.id == 4) {
            this.mc.displayGuiScreen(new GuiScreenInvite(this.field_96285_a, this, this.field_96280_b));
         } else if (var1.id == 3) {
            this.func_96272_i();
         } else if (var1.id == 6) {
            this.mc.displayGuiScreen(new GuiScreenResetWorld(this, this.field_96280_b));
         } else if (var1.id == 7) {
            this.mc.displayGuiScreen(new GuiScreenSubscription(this, this.field_96280_b));
         }
      }
   }

   private void func_96268_g() {
      McoClient var1 = new McoClient(this.mc.session);

      try {
         Boolean var2 = var1.func_96383_b(this.field_96280_b.field_96408_a);
         if (var2) {
            this.field_102020_y = true;
            this.field_96280_b.field_96404_d = "OPEN";
            this.initGui();
         }
      } catch (ExceptionMcoService var3) {
      } catch (IOException var4) {
      }
   }

   private void func_96275_h() {
      McoClient var1 = new McoClient(this.mc.session);

      try {
         boolean var2 = var1.func_96378_c(this.field_96280_b.field_96408_a);
         if (var2) {
            this.field_102020_y = true;
            this.field_96280_b.field_96404_d = "CLOSED";
            this.initGui();
         }
      } catch (ExceptionMcoService var3) {
      } catch (IOException var4) {
      }
   }

   private void func_96272_i() {
      if (this.field_96284_p >= 0 && this.field_96284_p < this.field_96280_b.field_96402_f.size()) {
         this.field_96283_q = (String)this.field_96280_b.field_96402_f.get(this.field_96284_p);
         StringTranslate var1 = StringTranslate.getInstance();
         GuiYesNo var2 = new GuiYesNo(this, "Warning!", var1.translateKey("mco.configure.world.uninvite.question") + " '" + this.field_96283_q + "'", 3);
         this.mc.displayGuiScreen(var2);
      }
   }

   @Override
   public void confirmClicked(boolean var1, int var2) {
      if (var2 == 3) {
         if (var1) {
            McoClient var3 = new McoClient(this.mc.session);

            try {
               var3.func_96381_a(this.field_96280_b.field_96408_a, this.field_96283_q);
            } catch (ExceptionMcoService var5) {
               System.err.println("Could not uninvite the selected user");
            }

            this.func_96267_d(this.field_96284_p);
         }

         this.mc.displayGuiScreen(new GuiScreenConfigureWorld(this.field_96285_a, this.field_96280_b));
      }

      if (var2 == 1) {
         if (var1) {
            this.func_96275_h();
         }

         this.mc.displayGuiScreen(this);
      }
   }

   private void func_96267_d(int var1) {
      this.field_96280_b.field_96402_f.remove(var1);
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
      this.field_96282_c.a(var1, var2, var3);
      this.a(this.fontRenderer, var4.translateKey("mco.configure.world.title"), this.width / 2, 17, 16777215);
      this.b(this.fontRenderer, var4.translateKey("mco.configure.world.name"), this.field_96277_d, this.func_96264_a(1), 10526880);
      this.b(this.fontRenderer, this.field_96280_b.func_96398_b(), this.field_96277_d, this.func_96264_a(2), 16777215);
      this.b(this.fontRenderer, var4.translateKey("mco.configure.world.description"), this.field_96277_d, this.func_96264_a(4), 10526880);
      this.b(this.fontRenderer, this.field_96280_b.func_96397_a(), this.field_96277_d, this.func_96264_a(5), 16777215);
      this.b(this.fontRenderer, var4.translateKey("mco.configure.world.status"), this.field_96277_d, this.func_96264_a(7), 10526880);
      this.b(this.fontRenderer, this.func_104045_j(), this.field_96277_d, this.func_96264_a(8), 16777215);
      this.b(this.fontRenderer, var4.translateKey("mco.configure.world.invited"), this.field_96287_o, this.func_96264_a(1), 10526880);
      super.drawScreen(var1, var2, var3);
   }

   private String func_104045_j() {
      if (this.field_96280_b.field_98166_h) {
         return "Expired";
      } else {
         String var1 = this.field_96280_b.field_96404_d.toLowerCase();
         return Character.toUpperCase(var1.charAt(0)) + var1.substring(1);
      }
   }
}
