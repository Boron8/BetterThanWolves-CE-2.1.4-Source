package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Collections;
import java.util.List;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

public class GuiScreenOnlineServers extends GuiScreen {
   private GuiScreen field_96188_a;
   private GuiSlotOnlineServerList field_96186_b;
   private static int field_96187_c = 0;
   private static final Object field_96185_d = new Object();
   private int field_96189_n = -1;
   private GuiButton field_96190_o;
   private GuiButton field_96198_p;
   private GuiButtonLink field_96197_q;
   private GuiButton field_96196_r;
   private String field_96195_s = null;
   private McoServerList field_96194_t;
   private boolean field_96193_u;
   private List field_96192_v = Collections.emptyList();
   private volatile int field_96199_x;
   private Long field_102019_y;
   private int field_104044_y = 0;

   public GuiScreenOnlineServers(GuiScreen var1) {
      this.field_96188_a = var1;
   }

   @Override
   public void initGui() {
      Keyboard.enableRepeatEvents(true);
      this.buttonList.clear();
      this.field_96194_t = new McoServerList(this.mc.session);
      if (!this.field_96193_u) {
         this.field_96193_u = true;
         this.field_96186_b = new GuiSlotOnlineServerList(this);
      } else {
         this.field_96186_b.a(this.width, this.height, 32, this.height - 64);
      }

      new ThreadOnlineScreen(this).start();
      this.func_96178_g();
   }

   public void func_96178_g() {
      StringTranslate var1 = StringTranslate.getInstance();
      this.buttonList.add(this.field_96196_r = new GuiButton(1, this.width / 2 - 154, this.height - 52, 100, 20, var1.translateKey("mco.selectServer.select")));
      this.buttonList.add(this.field_96198_p = new GuiButton(2, this.width / 2 - 48, this.height - 52, 100, 20, var1.translateKey("mco.selectServer.create")));
      this.buttonList
         .add(this.field_96190_o = new GuiButton(3, this.width / 2 + 58, this.height - 52, 100, 20, var1.translateKey("mco.selectServer.configure")));
      this.buttonList
         .add(this.field_96197_q = new GuiButtonLink(4, this.width / 2 - 154, this.height - 28, 154, 20, var1.translateKey("mco.selectServer.moreinfo")));
      this.buttonList.add(new GuiButton(0, this.width / 2 + 6, this.height - 28, 153, 20, var1.translateKey("gui.cancel")));
      boolean var2 = this.field_96189_n >= 0 && this.field_96189_n < this.field_96186_b.getSize();
      this.field_96196_r.enabled = var2
         && ((McoServer)this.field_96192_v.get(this.field_96189_n)).field_96404_d.equals("OPEN")
         && !((McoServer)this.field_96192_v.get(this.field_96189_n)).field_98166_h;
      this.field_96190_o.enabled = var2 && this.mc.session.username.equals(((McoServer)this.field_96192_v.get(this.field_96189_n)).field_96405_e);
      this.field_96198_p.enabled = this.field_96199_x > 0;
   }

   @Override
   public void updateScreen() {
      super.updateScreen();
      this.field_104044_y++;
      if (this.field_96194_t.func_98251_a()) {
         List var1 = this.field_96194_t.func_98252_c();

         for (McoServer var3 : var1) {
            for (McoServer var5 : this.field_96192_v) {
               if (var3.field_96408_a == var5.field_96408_a) {
                  var3.func_96401_a(var5);
                  if (this.field_102019_y != null && this.field_102019_y == var3.field_96408_a) {
                     this.field_102019_y = null;
                     var3.field_96411_l = false;
                  }
                  break;
               }
            }
         }

         this.field_96192_v = var1;
         this.field_96194_t.func_98250_b();
      }

      this.field_96198_p.enabled = this.field_96199_x > 0;
   }

   @Override
   public void onGuiClosed() {
      Keyboard.enableRepeatEvents(false);
   }

   @Override
   protected void actionPerformed(GuiButton var1) {
      if (var1.enabled) {
         if (var1.id == 1) {
            this.func_96159_a(this.field_96189_n);
         } else if (var1.id == 3) {
            List var2 = this.field_96194_t.func_98252_c();
            if (this.field_96189_n < var2.size()) {
               McoServer var3 = (McoServer)var2.get(this.field_96189_n);
               McoServer var4 = this.func_98086_a(var3.field_96408_a);
               if (var4 != null) {
                  this.field_96194_t.func_98248_d();
                  this.mc.displayGuiScreen(new GuiScreenConfigureWorld(this, var4));
               }
            }
         } else if (var1.id == 0) {
            this.field_96194_t.func_98248_d();
            this.mc.displayGuiScreen(this.field_96188_a);
         } else if (var1.id == 2) {
            this.field_96194_t.func_98248_d();
            this.mc.displayGuiScreen(new GuiScreenCreateOnlineWorld(this));
         } else if (var1.id == 4) {
            this.field_96197_q.func_96135_a("http://realms.minecraft.net/");
         } else {
            this.field_96186_b.a(var1);
         }
      }
   }

   public void func_102018_a(long var1) {
      this.field_96189_n = -1;
      this.field_102019_y = var1;
   }

   private McoServer func_98086_a(long var1) {
      McoClient var3 = new McoClient(this.mc.session);

      try {
         return var3.func_98176_a(var1);
      } catch (ExceptionMcoService var5) {
      } catch (IOException var6) {
      }

      return null;
   }

   @Override
   protected void keyTyped(char var1, int var2) {
      if (var2 == 59) {
         this.mc.gameSettings.hideServerAddress = !this.mc.gameSettings.hideServerAddress;
         this.mc.gameSettings.saveOptions();
      } else {
         if (var1 == '\r') {
            this.actionPerformed((GuiButton)this.buttonList.get(2));
         } else {
            super.keyTyped(var1, var2);
         }
      }
   }

   @Override
   public void drawScreen(int var1, int var2, float var3) {
      this.field_96195_s = null;
      StringTranslate var4 = StringTranslate.getInstance();
      this.e();
      this.field_96186_b.a(var1, var2, var3);
      this.a(this.fontRenderer, var4.translateKey("mco.title"), this.width / 2, 20, 16777215);
      super.drawScreen(var1, var2, var3);
      if (this.field_96195_s != null) {
         this.func_96165_a(this.field_96195_s, var1, var2);
      }
   }

   private void func_96159_a(int var1) {
      if (var1 >= 0 && var1 < this.field_96192_v.size()) {
         McoServer var2 = (McoServer)this.field_96192_v.get(var1);
         this.field_96194_t.func_98248_d();
         GuiScreenLongRunningTask var3 = new GuiScreenLongRunningTask(this.mc, this, new TaskOnlineConnect(this, var2));
         var3.func_98117_g();
         this.mc.displayGuiScreen(var3);
      }
   }

   private void func_101008_c(int var1, int var2, int var3, int var4) {
      this.mc.renderEngine.bindTexture("/gui/gui.png");
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      GL11.glPushMatrix();
      GL11.glScalef(0.5F, 0.5F, 0.5F);
      this.b(var1 * 2, var2 * 2, 191, 0, 16, 15);
      GL11.glPopMatrix();
      if (var3 >= var1 && var3 <= var1 + 9 && var4 >= var2 && var4 <= var2 + 9) {
         this.field_96195_s = "Expired World";
      }
   }

   private void func_104039_b(int var1, int var2, int var3, int var4, int var5) {
      if (this.field_104044_y % 20 < 10) {
         this.mc.renderEngine.bindTexture("/gui/gui.png");
         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
         GL11.glPushMatrix();
         GL11.glScalef(0.5F, 0.5F, 0.5F);
         this.b(var1 * 2, var2 * 2, 207, 0, 16, 15);
         GL11.glPopMatrix();
      }

      if (var3 >= var1 && var3 <= var1 + 9 && var4 >= var2 && var4 <= var2 + 9) {
         if (var5 == 0) {
            this.field_96195_s = "Expires in < a day";
         } else {
            this.field_96195_s = "Expires in " + var5 + (var5 > 1 ? " days" : " day");
         }
      }
   }

   private void func_101006_d(int var1, int var2, int var3, int var4) {
      this.mc.renderEngine.bindTexture("/gui/gui.png");
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      GL11.glPushMatrix();
      GL11.glScalef(0.5F, 0.5F, 0.5F);
      this.b(var1 * 2, var2 * 2, 207, 0, 16, 15);
      GL11.glPopMatrix();
      if (var3 >= var1 && var3 <= var1 + 9 && var4 >= var2 && var4 <= var2 + 9) {
         this.field_96195_s = "Open World";
      }
   }

   private void func_101001_e(int var1, int var2, int var3, int var4) {
      this.mc.renderEngine.bindTexture("/gui/gui.png");
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      GL11.glPushMatrix();
      GL11.glScalef(0.5F, 0.5F, 0.5F);
      this.b(var1 * 2, var2 * 2, 223, 0, 16, 15);
      GL11.glPopMatrix();
      if (var3 >= var1 && var3 <= var1 + 9 && var4 >= var2 && var4 <= var2 + 9) {
         this.field_96195_s = "Closed World";
      }
   }

   protected void func_96165_a(String var1, int var2, int var3) {
      if (var1 != null) {
         int var4 = var2 + 12;
         int var5 = var3 - 12;
         int var6 = this.fontRenderer.getStringWidth(var1);
         this.a(var4 - 3, var5 - 3, var4 + var6 + 3, var5 + 8 + 3, -1073741824, -1073741824);
         this.fontRenderer.drawStringWithShadow(var1, var4, var5, -1);
      }
   }

   private void func_96174_a(McoServer var1) {
      if (var1.field_96414_k.equals("")) {
         var1.field_96414_k = EnumChatFormatting.GRAY + "" + 0;
      }

      var1.field_96415_h = 61;
      ServerAddress var2 = ServerAddress.func_78860_a(var1.field_96403_g);
      Socket var3 = null;
      DataInputStream var4 = null;
      DataOutputStream var5 = null;

      try {
         var3 = new Socket();
         var3.setSoTimeout(3000);
         var3.setTcpNoDelay(true);
         var3.setTrafficClass(18);
         var3.connect(new InetSocketAddress(var2.getIP(), var2.getPort()), 3000);
         var4 = new DataInputStream(var3.getInputStream());
         var5 = new DataOutputStream(var3.getOutputStream());
         var5.write(254);
         var5.write(1);
         if (var4.read() != 255) {
            throw new IOException("Bad message");
         }

         String var6 = Packet.readString(var4, 256);
         char[] var7 = var6.toCharArray();

         for (int var8 = 0; var8 < var7.length; var8++) {
            if (var7[var8] != 167 && var7[var8] != 0 && ChatAllowedCharacters.allowedCharacters.indexOf(var7[var8]) < 0) {
               var7[var8] = '?';
            }
         }

         var6 = new String(var7);
         if (var6.startsWith("\u00a7") && var6.length() > 1) {
            String[] var30 = var6.substring(1).split("\u0000");
            if (MathHelper.parseIntWithDefault(var30[0], 0) == 1) {
               var1.field_96415_h = MathHelper.parseIntWithDefault(var30[1], var1.field_96415_h);
               var1.field_96413_j = var30[2];
               int var31 = MathHelper.parseIntWithDefault(var30[4], 0);
               int var32 = MathHelper.parseIntWithDefault(var30[5], 0);
               if (var31 >= 0 && var32 >= 0) {
                  var1.field_96414_k = EnumChatFormatting.GRAY + "" + var31;
               } else {
                  var1.field_96414_k = "" + EnumChatFormatting.DARK_GRAY + "???";
               }
            } else {
               var1.field_96413_j = "???";
               var1.field_96415_h = 62;
               var1.field_96414_k = "" + EnumChatFormatting.DARK_GRAY + "???";
            }
         } else {
            String[] var29 = var6.split("\u00a7");
            Object var28 = var29[0];
            int var9 = -1;
            int var10 = -1;

            try {
               var9 = Integer.parseInt(var29[1]);
               var10 = Integer.parseInt(var29[2]);
            } catch (Exception var25) {
            }

            var1.field_96407_c = EnumChatFormatting.GRAY + var28;
            if (var9 >= 0 && var10 > 0) {
               var1.field_96414_k = EnumChatFormatting.GRAY + "" + var9;
            } else {
               var1.field_96414_k = "" + EnumChatFormatting.DARK_GRAY + "???";
            }

            var1.field_96413_j = "1.3";
            var1.field_96415_h = 60;
         }
      } finally {
         try {
            if (var4 != null) {
               var4.close();
            }
         } catch (Throwable var24) {
         }

         try {
            if (var5 != null) {
               var5.close();
            }
         } catch (Throwable var23) {
         }

         try {
            if (var3 != null) {
               var3.close();
            }
         } catch (Throwable var22) {
         }
      }
   }
}
