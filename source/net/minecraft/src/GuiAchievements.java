package net.minecraft.src;

import java.util.Random;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class GuiAchievements extends GuiScreen {
   private static final int guiMapTop = AchievementList.minDisplayColumn * 24 - 112;
   private static final int guiMapLeft = AchievementList.minDisplayRow * 24 - 112;
   private static final int guiMapBottom = AchievementList.maxDisplayColumn * 24 - 77;
   private static final int guiMapRight = AchievementList.maxDisplayRow * 24 - 77;
   protected int achievementsPaneWidth = 256;
   protected int achievementsPaneHeight = 202;
   protected int mouseX = 0;
   protected int mouseY = 0;
   protected double field_74117_m;
   protected double field_74115_n;
   protected double guiMapX;
   protected double guiMapY;
   protected double field_74124_q;
   protected double field_74123_r;
   private int isMouseButtonDown = 0;
   private StatFileWriter statFileWriter;

   public GuiAchievements(StatFileWriter var1) {
      this.statFileWriter = var1;
      short var2 = 141;
      short var3 = 141;
      this.field_74117_m = this.guiMapX = this.field_74124_q = AchievementList.openInventory.displayColumn * 24 - var2 / 2 - 12;
      this.field_74115_n = this.guiMapY = this.field_74123_r = AchievementList.openInventory.displayRow * 24 - var3 / 2;
   }

   @Override
   public void initGui() {
      this.buttonList.clear();
      this.buttonList.add(new GuiSmallButton(1, this.width / 2 + 24, this.height / 2 + 74, 80, 20, StatCollector.translateToLocal("gui.done")));
   }

   @Override
   protected void actionPerformed(GuiButton var1) {
      if (var1.id == 1) {
         this.mc.displayGuiScreen(null);
         this.mc.setIngameFocus();
      }

      super.actionPerformed(var1);
   }

   @Override
   protected void keyTyped(char var1, int var2) {
      if (var2 == this.mc.gameSettings.keyBindInventory.keyCode) {
         this.mc.displayGuiScreen(null);
         this.mc.setIngameFocus();
      } else {
         super.keyTyped(var1, var2);
      }
   }

   @Override
   public void drawScreen(int var1, int var2, float var3) {
      if (Mouse.isButtonDown(0)) {
         int var4 = (this.width - this.achievementsPaneWidth) / 2;
         int var5 = (this.height - this.achievementsPaneHeight) / 2;
         int var6 = var4 + 8;
         int var7 = var5 + 17;
         if ((this.isMouseButtonDown == 0 || this.isMouseButtonDown == 1) && var1 >= var6 && var1 < var6 + 224 && var2 >= var7 && var2 < var7 + 155) {
            if (this.isMouseButtonDown == 0) {
               this.isMouseButtonDown = 1;
            } else {
               this.guiMapX = this.guiMapX - (var1 - this.mouseX);
               this.guiMapY = this.guiMapY - (var2 - this.mouseY);
               this.field_74124_q = this.field_74117_m = this.guiMapX;
               this.field_74123_r = this.field_74115_n = this.guiMapY;
            }

            this.mouseX = var1;
            this.mouseY = var2;
         }

         if (this.field_74124_q < guiMapTop) {
            this.field_74124_q = guiMapTop;
         }

         if (this.field_74123_r < guiMapLeft) {
            this.field_74123_r = guiMapLeft;
         }

         if (this.field_74124_q >= guiMapBottom) {
            this.field_74124_q = guiMapBottom - 1;
         }

         if (this.field_74123_r >= guiMapRight) {
            this.field_74123_r = guiMapRight - 1;
         }
      } else {
         this.isMouseButtonDown = 0;
      }

      this.e();
      this.genAchievementBackground(var1, var2, var3);
      GL11.glDisable(2896);
      GL11.glDisable(2929);
      this.drawTitle();
      GL11.glEnable(2896);
      GL11.glEnable(2929);
   }

   @Override
   public void updateScreen() {
      this.field_74117_m = this.guiMapX;
      this.field_74115_n = this.guiMapY;
      double var1 = this.field_74124_q - this.guiMapX;
      double var3 = this.field_74123_r - this.guiMapY;
      if (var1 * var1 + var3 * var3 < 4.0) {
         this.guiMapX += var1;
         this.guiMapY += var3;
      } else {
         this.guiMapX += var1 * 0.85;
         this.guiMapY += var3 * 0.85;
      }
   }

   protected void drawTitle() {
      int var1 = (this.width - this.achievementsPaneWidth) / 2;
      int var2 = (this.height - this.achievementsPaneHeight) / 2;
      this.fontRenderer.drawString("Achievements", var1 + 15, var2 + 5, 4210752);
   }

   protected void genAchievementBackground(int var1, int var2, float var3) {
      int var4 = MathHelper.floor_double(this.field_74117_m + (this.guiMapX - this.field_74117_m) * var3);
      int var5 = MathHelper.floor_double(this.field_74115_n + (this.guiMapY - this.field_74115_n) * var3);
      if (var4 < guiMapTop) {
         var4 = guiMapTop;
      }

      if (var5 < guiMapLeft) {
         var5 = guiMapLeft;
      }

      if (var4 >= guiMapBottom) {
         var4 = guiMapBottom - 1;
      }

      if (var5 >= guiMapRight) {
         var5 = guiMapRight - 1;
      }

      int var6 = (this.width - this.achievementsPaneWidth) / 2;
      int var7 = (this.height - this.achievementsPaneHeight) / 2;
      int var8 = var6 + 16;
      int var9 = var7 + 17;
      this.zLevel = 0.0F;
      GL11.glDepthFunc(518);
      GL11.glPushMatrix();
      GL11.glTranslatef(0.0F, 0.0F, -200.0F);
      GL11.glEnable(3553);
      GL11.glDisable(2896);
      GL11.glEnable(32826);
      GL11.glEnable(2903);
      this.mc.renderEngine.bindTexture("/terrain.png");
      int var10 = var4 + 288 >> 4;
      int var11 = var5 + 288 >> 4;
      int var12 = (var4 + 288) % 16;
      int var13 = (var5 + 288) % 16;
      Random var19 = new Random();

      for (int var20 = 0; var20 * 16 - var13 < 155; var20++) {
         float var21 = 0.6F - (var11 + var20) / 25.0F * 0.3F;
         GL11.glColor4f(var21, var21, var21, 1.0F);

         for (int var22 = 0; var22 * 16 - var12 < 224; var22++) {
            var19.setSeed(1234 + var10 + var22);
            var19.nextInt();
            int var23 = var19.nextInt(1 + var11 + var20) + (var11 + var20) / 2;
            Icon var24 = Block.sand.getIcon(0, 0);
            if (var23 > 37 || var11 + var20 == 35) {
               var24 = Block.bedrock.getIcon(0, 0);
            } else if (var23 == 22) {
               if (var19.nextInt(2) == 0) {
                  var24 = Block.oreDiamond.getIcon(0, 0);
               } else {
                  var24 = Block.oreRedstone.getIcon(0, 0);
               }
            } else if (var23 == 10) {
               var24 = Block.oreIron.getIcon(0, 0);
            } else if (var23 == 8) {
               var24 = Block.oreCoal.getIcon(0, 0);
            } else if (var23 > 4) {
               var24 = Block.stone.getIcon(0, 0);
            } else if (var23 > 0) {
               var24 = Block.dirt.getIcon(0, 0);
            }

            this.a(var8 + var22 * 16 - var12, var9 + var20 * 16 - var13, var24, 16, 16);
         }
      }

      GL11.glEnable(2929);
      GL11.glDepthFunc(515);
      GL11.glDisable(3553);

      for (int var30 = 0; var30 < AchievementList.achievementList.size(); var30++) {
         Achievement var32 = (Achievement)AchievementList.achievementList.get(var30);
         if (var32.parentAchievement != null) {
            int var34 = var32.displayColumn * 24 - var4 + 11 + var8;
            int var37 = var32.displayRow * 24 - var5 + 11 + var9;
            int var40 = var32.parentAchievement.displayColumn * 24 - var4 + 11 + var8;
            int var25 = var32.parentAchievement.displayRow * 24 - var5 + 11 + var9;
            boolean var26 = this.statFileWriter.hasAchievementUnlocked(var32);
            boolean var27 = this.statFileWriter.canUnlockAchievement(var32);
            int var28 = Math.sin(Minecraft.getSystemTime() % 600L / 600.0 * Math.PI * 2.0) > 0.6 ? 255 : 130;
            int var29 = -16777216;
            if (var26) {
               var29 = -9408400;
            } else if (var27) {
               var29 = 65280 + (var28 << 24);
            }

            this.a(var34, var40, var37, var29);
            this.b(var40, var37, var25, var29);
         }
      }

      Achievement var31 = null;
      RenderItem var33 = new RenderItem();
      RenderHelper.enableGUIStandardItemLighting();
      GL11.glDisable(2896);
      GL11.glEnable(32826);
      GL11.glEnable(2903);

      for (int var35 = 0; var35 < AchievementList.achievementList.size(); var35++) {
         Achievement var38 = (Achievement)AchievementList.achievementList.get(var35);
         int var41 = var38.displayColumn * 24 - var4;
         int var43 = var38.displayRow * 24 - var5;
         if (var41 >= -24 && var43 >= -24 && var41 <= 224 && var43 <= 155) {
            if (this.statFileWriter.hasAchievementUnlocked(var38)) {
               float var45 = 1.0F;
               GL11.glColor4f(var45, var45, var45, 1.0F);
            } else if (this.statFileWriter.canUnlockAchievement(var38)) {
               float var46 = Math.sin(Minecraft.getSystemTime() % 600L / 600.0 * Math.PI * 2.0) < 0.6 ? 0.6F : 0.8F;
               GL11.glColor4f(var46, var46, var46, 1.0F);
            } else {
               float var47 = 0.3F;
               GL11.glColor4f(var47, var47, var47, 1.0F);
            }

            this.mc.renderEngine.bindTexture("/achievement/bg.png");
            int var48 = var8 + var41;
            int var51 = var9 + var43;
            if (var38.getSpecial()) {
               this.b(var48 - 2, var51 - 2, 26, 202, 26, 26);
            } else {
               this.b(var48 - 2, var51 - 2, 0, 202, 26, 26);
            }

            if (!this.statFileWriter.canUnlockAchievement(var38)) {
               float var54 = 0.1F;
               GL11.glColor4f(var54, var54, var54, 1.0F);
               var33.renderWithColor = false;
            }

            GL11.glEnable(2896);
            GL11.glEnable(2884);
            var33.renderItemAndEffectIntoGUI(this.mc.fontRenderer, this.mc.renderEngine, var38.theItemStack, var48 + 3, var51 + 3);
            GL11.glDisable(2896);
            if (!this.statFileWriter.canUnlockAchievement(var38)) {
               var33.renderWithColor = true;
            }

            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            if (var1 >= var8
               && var2 >= var9
               && var1 < var8 + 224
               && var2 < var9 + 155
               && var1 >= var48
               && var1 <= var48 + 22
               && var2 >= var51
               && var2 <= var51 + 22) {
               var31 = var38;
            }
         }
      }

      GL11.glDisable(2929);
      GL11.glEnable(3042);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      this.mc.renderEngine.bindTexture("/achievement/bg.png");
      this.b(var6, var7, 0, 0, this.achievementsPaneWidth, this.achievementsPaneHeight);
      GL11.glPopMatrix();
      this.zLevel = 0.0F;
      GL11.glDepthFunc(515);
      GL11.glDisable(2929);
      GL11.glEnable(3553);
      super.drawScreen(var1, var2, var3);
      if (var31 != null) {
         String var36 = StatCollector.translateToLocal(var31.i());
         String var39 = var31.getDescription();
         int var42 = var1 + 12;
         int var44 = var2 - 4;
         if (this.statFileWriter.canUnlockAchievement(var31)) {
            int var49 = Math.max(this.fontRenderer.getStringWidth(var36), 120);
            int var52 = this.fontRenderer.splitStringWidth(var39, var49);
            if (this.statFileWriter.hasAchievementUnlocked(var31)) {
               var52 += 12;
            }

            this.a(var42 - 3, var44 - 3, var42 + var49 + 3, var44 + var52 + 3 + 12, -1073741824, -1073741824);
            this.fontRenderer.drawSplitString(var39, var42, var44 + 12, var49, -6250336);
            if (this.statFileWriter.hasAchievementUnlocked(var31)) {
               this.fontRenderer.drawStringWithShadow(StatCollector.translateToLocal("achievement.taken"), var42, var44 + var52 + 4, -7302913);
            }
         } else {
            int var50 = Math.max(this.fontRenderer.getStringWidth(var36), 120);
            String var53 = StatCollector.translateToLocalFormatted("achievement.requires", StatCollector.translateToLocal(var31.parentAchievement.i()));
            int var55 = this.fontRenderer.splitStringWidth(var53, var50);
            this.a(var42 - 3, var44 - 3, var42 + var50 + 3, var44 + var55 + 12 + 3, -1073741824, -1073741824);
            this.fontRenderer.drawSplitString(var53, var42, var44 + 12, var50, -9416624);
         }

         this.fontRenderer
            .drawStringWithShadow(
               var36,
               var42,
               var44,
               this.statFileWriter.canUnlockAchievement(var31) ? (var31.getSpecial() ? -128 : -1) : (var31.getSpecial() ? -8355776 : -8355712)
            );
      }

      GL11.glEnable(2929);
      GL11.glEnable(2896);
      RenderHelper.disableStandardItemLighting();
   }

   @Override
   public boolean doesGuiPauseGame() {
      return true;
   }
}
