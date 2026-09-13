package net.minecraft.src;

import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;

public class GuiAchievement extends Gui {
   private Minecraft theGame;
   private int achievementWindowWidth;
   private int achievementWindowHeight;
   private String achievementGetLocalText;
   private String achievementStatName;
   private Achievement theAchievement;
   private long achievementTime;
   private RenderItem itemRender;
   private boolean haveAchiement;

   public GuiAchievement(Minecraft var1) {
      this.theGame = var1;
      this.itemRender = new RenderItem();
   }

   public void queueTakenAchievement(Achievement var1) {
      this.achievementGetLocalText = StatCollector.translateToLocal("achievement.get");
      this.achievementStatName = StatCollector.translateToLocal(var1.i());
      this.achievementTime = Minecraft.getSystemTime();
      this.theAchievement = var1;
      this.haveAchiement = false;
   }

   public void queueAchievementInformation(Achievement var1) {
      this.achievementGetLocalText = StatCollector.translateToLocal(var1.i());
      this.achievementStatName = var1.getDescription();
      this.achievementTime = Minecraft.getSystemTime() - 2500L;
      this.theAchievement = var1;
      this.haveAchiement = true;
   }

   private void updateAchievementWindowScale() {
      GL11.glViewport(0, 0, this.theGame.displayWidth, this.theGame.displayHeight);
      GL11.glMatrixMode(5889);
      GL11.glLoadIdentity();
      GL11.glMatrixMode(5888);
      GL11.glLoadIdentity();
      this.achievementWindowWidth = this.theGame.displayWidth;
      this.achievementWindowHeight = this.theGame.displayHeight;
      ScaledResolution var1 = new ScaledResolution(this.theGame.gameSettings, this.theGame.displayWidth, this.theGame.displayHeight);
      this.achievementWindowWidth = var1.getScaledWidth();
      this.achievementWindowHeight = var1.getScaledHeight();
      GL11.glClear(256);
      GL11.glMatrixMode(5889);
      GL11.glLoadIdentity();
      GL11.glOrtho(0.0, this.achievementWindowWidth, this.achievementWindowHeight, 0.0, 1000.0, 3000.0);
      GL11.glMatrixMode(5888);
      GL11.glLoadIdentity();
      GL11.glTranslatef(0.0F, 0.0F, -2000.0F);
   }

   public void updateAchievementWindow() {
      if (this.theAchievement != null && this.achievementTime != 0L) {
         double var1 = (Minecraft.getSystemTime() - this.achievementTime) / 3000.0;
         if (this.haveAchiement || !(var1 < 0.0) && !(var1 > 1.0)) {
            this.updateAchievementWindowScale();
            GL11.glDisable(2929);
            GL11.glDepthMask(false);
            double var3 = var1 * 2.0;
            if (var3 > 1.0) {
               var3 = 2.0 - var3;
            }

            var3 *= 4.0;
            var3 = 1.0 - var3;
            if (var3 < 0.0) {
               var3 = 0.0;
            }

            var3 *= var3;
            var3 *= var3;
            int var5 = this.achievementWindowWidth - 160;
            int var6 = 0 - (int)(var3 * 36.0);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glEnable(3553);
            this.theGame.renderEngine.bindTexture("/achievement/bg.png");
            GL11.glDisable(2896);
            this.b(var5, var6, 96, 202, 160, 32);
            if (this.haveAchiement) {
               this.theGame.fontRenderer.drawSplitString(this.achievementStatName, var5 + 30, var6 + 7, 120, -1);
            } else {
               this.theGame.fontRenderer.drawString(this.achievementGetLocalText, var5 + 30, var6 + 7, -256);
               this.theGame.fontRenderer.drawString(this.achievementStatName, var5 + 30, var6 + 18, -1);
            }

            RenderHelper.enableGUIStandardItemLighting();
            GL11.glDisable(2896);
            GL11.glEnable(32826);
            GL11.glEnable(2903);
            GL11.glEnable(2896);
            this.itemRender
               .renderItemAndEffectIntoGUI(this.theGame.fontRenderer, this.theGame.renderEngine, this.theAchievement.theItemStack, var5 + 8, var6 + 8);
            GL11.glDisable(2896);
            GL11.glDepthMask(true);
            GL11.glEnable(2929);
         } else {
            this.achievementTime = 0L;
         }
      }
   }
}
