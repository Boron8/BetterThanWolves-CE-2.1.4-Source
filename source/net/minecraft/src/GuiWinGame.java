package net.minecraft.src;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.lwjgl.opengl.GL11;

public class GuiWinGame extends GuiScreen {
   private int updateCounter = 0;
   private List lines;
   private int field_73989_c = 0;
   private float field_73987_d = 0.5F;

   @Override
   public void updateScreen() {
      this.updateCounter++;
      float var1 = (this.field_73989_c + this.height + this.height + 24) / this.field_73987_d;
      if (this.updateCounter > var1) {
         this.respawnPlayer();
      }
   }

   @Override
   protected void keyTyped(char var1, int var2) {
      if (var2 == 1) {
         this.respawnPlayer();
      }
   }

   private void respawnPlayer() {
      this.mc.thePlayer.sendQueue.addToSendQueue(new Packet205ClientCommand(1));
      this.mc.displayGuiScreen(null);
   }

   @Override
   public boolean doesGuiPauseGame() {
      return true;
   }

   @Override
   public void initGui() {
      if (this.lines == null) {
         this.lines = new ArrayList();

         try {
            String var1 = "";
            String var2 = "" + EnumChatFormatting.WHITE + EnumChatFormatting.OBFUSCATED + EnumChatFormatting.GREEN + EnumChatFormatting.AQUA;
            short var3 = 274;
            BufferedReader var4 = new BufferedReader(new InputStreamReader(GuiWinGame.class.getResourceAsStream("/title/win.txt"), Charset.forName("UTF-8")));
            Random var5 = new Random(8124371L);

            while ((var1 = var4.readLine()) != null) {
               var1 = var1.replaceAll("PLAYERNAME", this.mc.session.username);

               while (var1.contains(var2)) {
                  int var6 = var1.indexOf(var2);
                  Object var7 = var1.substring(0, var6);
                  String var8 = var1.substring(var6 + var2.length());
                  var1 = var7 + EnumChatFormatting.WHITE + EnumChatFormatting.OBFUSCATED + "XXXXXXXX".substring(0, var5.nextInt(4) + 3) + var8;
               }

               this.lines.addAll(this.mc.fontRenderer.listFormattedStringToWidth(var1, var3));
               this.lines.add("");
            }

            for (int var16 = 0; var16 < 8; var16++) {
               this.lines.add("");
            }

            var4 = new BufferedReader(new InputStreamReader(GuiWinGame.class.getResourceAsStream("/title/credits.txt"), Charset.forName("UTF-8")));

            while ((var1 = var4.readLine()) != null) {
               var1 = var1.replaceAll("PLAYERNAME", this.mc.session.username);
               var1 = var1.replaceAll("\t", "    ");
               this.lines.addAll(this.mc.fontRenderer.listFormattedStringToWidth(var1, var3));
               this.lines.add("");
            }

            this.field_73989_c = this.lines.size() * 12;
         } catch (Exception var9) {
            var9.printStackTrace();
         }
      }
   }

   private void func_73986_b(int var1, int var2, float var3) {
      Tessellator var4 = Tessellator.instance;
      this.mc.renderEngine.bindTexture("%blur%/gui/background.png");
      var4.startDrawingQuads();
      var4.setColorRGBA_F(1.0F, 1.0F, 1.0F, 1.0F);
      int var5 = this.width;
      float var6 = 0.0F - (this.updateCounter + var3) * 0.5F * this.field_73987_d;
      float var7 = this.height - (this.updateCounter + var3) * 0.5F * this.field_73987_d;
      float var8 = 0.015625F;
      float var9 = (this.updateCounter + var3 - 0.0F) * 0.02F;
      float var10 = (this.field_73989_c + this.height + this.height + 24) / this.field_73987_d;
      float var11 = (var10 - 20.0F - (this.updateCounter + var3)) * 0.005F;
      if (var11 < var9) {
         var9 = var11;
      }

      if (var9 > 1.0F) {
         var9 = 1.0F;
      }

      var9 *= var9;
      var9 = var9 * 96.0F / 255.0F;
      var4.setColorOpaque_F(var9, var9, var9);
      var4.addVertexWithUV(0.0, this.height, this.zLevel, 0.0, var6 * var8);
      var4.addVertexWithUV(var5, this.height, this.zLevel, var5 * var8, var6 * var8);
      var4.addVertexWithUV(var5, 0.0, this.zLevel, var5 * var8, var7 * var8);
      var4.addVertexWithUV(0.0, 0.0, this.zLevel, 0.0, var7 * var8);
      var4.draw();
   }

   @Override
   public void drawScreen(int var1, int var2, float var3) {
      this.func_73986_b(var1, var2, var3);
      Tessellator var4 = Tessellator.instance;
      short var5 = 274;
      int var6 = this.width / 2 - var5 / 2;
      int var7 = this.height + 50;
      float var8 = -(this.updateCounter + var3) * this.field_73987_d;
      GL11.glPushMatrix();
      GL11.glTranslatef(0.0F, var8, 0.0F);
      this.mc.renderEngine.bindTexture("/title/mclogo.png");
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      this.b(var6, var7, 0, 0, 155, 44);
      this.b(var6 + 155, var7, 0, 45, 155, 44);
      var4.setColorOpaque_I(16777215);
      int var9 = var7 + 200;

      for (int var10 = 0; var10 < this.lines.size(); var10++) {
         if (var10 == this.lines.size() - 1) {
            float var11 = var9 + var8 - (this.height / 2 - 6);
            if (var11 < 0.0F) {
               GL11.glTranslatef(0.0F, -var11, 0.0F);
            }
         }

         if (var9 + var8 + 12.0F + 8.0F > 0.0F && var9 + var8 < this.height) {
            String var13 = (String)this.lines.get(var10);
            if (var13.startsWith("[C]")) {
               this.fontRenderer
                  .drawStringWithShadow(var13.substring(3), var6 + (var5 - this.fontRenderer.getStringWidth(var13.substring(3))) / 2, var9, 16777215);
            } else {
               this.fontRenderer.fontRandom.setSeed(var10 * 4238972211L + this.updateCounter / 4);
               this.fontRenderer.drawStringWithShadow(var13, var6, var9, 16777215);
            }
         }

         var9 += 12;
      }

      GL11.glPopMatrix();
      this.mc.renderEngine.bindTexture("%blur%/misc/vignette.png");
      GL11.glEnable(3042);
      GL11.glBlendFunc(0, 769);
      var4.startDrawingQuads();
      var4.setColorRGBA_F(1.0F, 1.0F, 1.0F, 1.0F);
      int var12 = this.width;
      int var14 = this.height;
      var4.addVertexWithUV(0.0, var14, this.zLevel, 0.0, 1.0);
      var4.addVertexWithUV(var12, var14, this.zLevel, 1.0, 1.0);
      var4.addVertexWithUV(var12, 0.0, this.zLevel, 1.0, 0.0);
      var4.addVertexWithUV(0.0, 0.0, this.zLevel, 0.0, 0.0);
      var4.draw();
      GL11.glDisable(3042);
      super.drawScreen(var1, var2, var3);
   }
}
