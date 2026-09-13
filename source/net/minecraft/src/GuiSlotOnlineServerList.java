package net.minecraft.src;

import org.lwjgl.opengl.GL11;

class GuiSlotOnlineServerList extends GuiScreenSelectLocation {
   public GuiSlotOnlineServerList(GuiScreenOnlineServers var1) {
      super(GuiScreenOnlineServers.func_98075_b(var1), var1.width, var1.height, 32, var1.height - 64, 36);
      this.field_96294_a = var1;
   }

   @Override
   protected int getSize() {
      return GuiScreenOnlineServers.func_98094_c(this.field_96294_a).size() + 1;
   }

   @Override
   protected void elementClicked(int var1, boolean var2) {
      if (var1 < GuiScreenOnlineServers.func_98094_c(this.field_96294_a).size()) {
         GuiScreenOnlineServers.func_98089_b(this.field_96294_a, var1);
         McoServer var3 = (McoServer)GuiScreenOnlineServers.func_98094_c(this.field_96294_a).get(GuiScreenOnlineServers.func_98072_d(this.field_96294_a));
         GuiScreenOnlineServers.func_96161_f(this.field_96294_a).enabled = GuiScreenOnlineServers.func_98076_f(this.field_96294_a)
            .session
            .username
            .equals(var3.field_96405_e);
         GuiScreenOnlineServers.func_98092_g(this.field_96294_a).enabled = var3.field_96404_d.equals("OPEN") && !var3.field_98166_h;
         if (var2 && GuiScreenOnlineServers.func_98092_g(this.field_96294_a).enabled) {
            GuiScreenOnlineServers.func_98078_c(this.field_96294_a, GuiScreenOnlineServers.func_98072_d(this.field_96294_a));
         }
      }
   }

   @Override
   protected boolean isSelected(int var1) {
      return var1 == GuiScreenOnlineServers.func_98072_d(this.field_96294_a);
   }

   @Override
   protected boolean func_104086_b(int var1) {
      return var1 < GuiScreenOnlineServers.func_98094_c(this.field_96294_a).size()
         && ((McoServer)GuiScreenOnlineServers.func_98094_c(this.field_96294_a).get(var1))
            .field_96405_e
            .toLowerCase()
            .equals(GuiScreenOnlineServers.func_98091_h(this.field_96294_a).session.username);
   }

   @Override
   protected int getContentHeight() {
      return this.getSize() * 36;
   }

   @Override
   protected void drawBackground() {
      this.field_96294_a.e();
   }

   @Override
   protected void drawSlot(int var1, int var2, int var3, int var4, Tessellator var5) {
      if (var1 < GuiScreenOnlineServers.func_98094_c(this.field_96294_a).size()) {
         this.func_96292_b(var1, var2, var3, var4, var5);
      }
   }

   private void func_96292_b(int var1, int var2, int var3, int var4, Tessellator var5) {
      McoServer var6 = (McoServer)GuiScreenOnlineServers.func_98094_c(this.field_96294_a).get(var1);
      this.field_96294_a.b(GuiScreenOnlineServers.func_104038_i(this.field_96294_a), var6.func_96398_b(), var2 + 2, var3 + 1, 16777215);
      short var7 = 207;
      byte var8 = 1;
      if (var6.field_98166_h) {
         GuiScreenOnlineServers.func_101012_b(this.field_96294_a, var2 + var7, var3 + var8, this.field_104094_d, this.field_104095_e);
      } else if (var6.field_96404_d.equals("CLOSED")) {
         GuiScreenOnlineServers.func_101009_c(this.field_96294_a, var2 + var7, var3 + var8, this.field_104094_d, this.field_104095_e);
      } else if (var6.field_96405_e.equals(GuiScreenOnlineServers.func_104032_j(this.field_96294_a).session.username) && var6.field_104063_i < 7) {
         this.func_96293_a(var1, var2 - 14, var3, var6);
         GuiScreenOnlineServers.func_104030_a(this.field_96294_a, var2 + var7, var3 + var8, this.field_104094_d, this.field_104095_e, var6.field_104063_i);
      } else if (var6.field_96404_d.equals("OPEN")) {
         GuiScreenOnlineServers.func_104031_c(this.field_96294_a, var2 + var7, var3 + var8, this.field_104094_d, this.field_104095_e);
         this.func_96293_a(var1, var2 - 14, var3, var6);
      }

      this.field_96294_a.b(GuiScreenOnlineServers.func_98084_i(this.field_96294_a), var6.func_96397_a(), var2 + 2, var3 + 12, 7105644);
      this.field_96294_a.b(GuiScreenOnlineServers.func_101005_j(this.field_96294_a), var6.field_96405_e, var2 + 2, var3 + 12 + 11, 5000268);
   }

   private void func_96293_a(int var1, int var2, int var3, McoServer var4) {
      if (var4.field_96403_g != null) {
         synchronized (GuiScreenOnlineServers.func_101007_h()) {
            if (GuiScreenOnlineServers.func_101010_i() < 5 && (!var4.field_96411_l || var4.field_102022_m)) {
               new ThreadConnectToOnlineServer(this, var4).start();
            }
         }

         boolean var5 = var4.field_96415_h > 61;
         boolean var6 = var4.field_96415_h < 61;
         boolean var7 = var5 || var6;
         if (var4.field_96414_k != null) {
            this.field_96294_a
               .b(
                  GuiScreenOnlineServers.func_98079_k(this.field_96294_a),
                  var4.field_96414_k,
                  var2 + 215 - GuiScreenOnlineServers.func_98087_l(this.field_96294_a).getStringWidth(var4.field_96414_k),
                  var3 + 1,
                  8421504
               );
         }

         if (var7) {
            String var8 = EnumChatFormatting.DARK_RED + var4.field_96413_j;
            this.field_96294_a
               .b(
                  GuiScreenOnlineServers.func_98074_m(this.field_96294_a),
                  var8,
                  var2 + 200 - GuiScreenOnlineServers.func_101000_n(this.field_96294_a).getStringWidth(var8),
                  var3 + 1,
                  8421504
               );
         }

         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
         GuiScreenOnlineServers.func_101004_o(this.field_96294_a).renderEngine.bindTexture("/gui/icons.png");
         byte var13 = 0;
         byte var9 = 0;
         String var10 = null;
         if (var7) {
            var10 = var5 ? "Client out of date!" : "Server out of date!";
            var9 = 5;
            this.field_96294_a.b(var2 + 205, var3, var13 * 10, 176 + var9 * 8, 10, 8);
         }

         byte var11 = 4;
         if (this.field_104094_d >= var2 + 205 - var11
            && this.field_104095_e >= var3 - var11
            && this.field_104094_d <= var2 + 205 + 10 + var11
            && this.field_104095_e <= var3 + 8 + var11) {
            GuiScreenOnlineServers.func_101011_a(this.field_96294_a, var10);
         }
      }
   }
}
