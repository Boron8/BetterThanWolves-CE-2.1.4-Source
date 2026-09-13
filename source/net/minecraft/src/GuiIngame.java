package net.minecraft.src;

import btw.BTWMod;
import btw.util.status.BTWStatusCategory;
import btw.util.status.StatusEffect;
import btw.world.util.WorldUtils;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import org.lwjgl.opengl.GL11;

@Environment(EnvType.CLIENT)
public class GuiIngame extends Gui {
   private static final RenderItem itemRenderer = new RenderItem();
   private final Random rand = new Random();
   private final Minecraft mc;
   private final GuiNewChat persistantChatGUI;
   private int updateCounter = 0;
   private String recordPlaying = "";
   private int recordPlayingUpFor = 0;
   private boolean recordIsPlaying = false;
   public float prevVignetteBrightness = 1.0F;
   private int remainingHighlightTicks;
   private ItemStack highlightingItemStack;
   static final int TRUE_SIGHT_RANGE = 10;
   private int foodLevelOnLastGUIUpdate = 0;
   private float fatOnLastGUIUpdate = 0.0F;
   private int foodOverlayShakeCounter = 0;

   public GuiIngame(Minecraft par1Minecraft) {
      this.mc = par1Minecraft;
      this.persistantChatGUI = new GuiNewChat(par1Minecraft);
   }

   public void renderGameOverlay(float par1, boolean par2, int par3, int par4) {
      ScaledResolution var5 = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
      int var6 = var5.getScaledWidth();
      int var7 = var5.getScaledHeight();
      FontRenderer var8 = this.mc.fontRenderer;
      this.mc.entityRenderer.setupOverlayRendering();
      GL11.glEnable(3042);
      if (Minecraft.isFancyGraphicsEnabled()) {
         this.renderVignette(this.mc.thePlayer.c(par1), var6, var7);
      } else {
         GL11.glBlendFunc(770, 771);
      }

      ItemStack var9 = this.mc.thePlayer.inventory.armorItemInSlot(3);
      if (this.mc.gameSettings.thirdPersonView == 0 && var9 != null && var9.itemID == Block.pumpkin.blockID) {
         this.renderPumpkinBlur(var6, var7);
      }

      this.renderModSpecificPlayerSightEffects();
      if (!this.mc.thePlayer.a(Potion.confusion)) {
         float var10 = this.mc.thePlayer.prevTimeInPortal + (this.mc.thePlayer.timeInPortal - this.mc.thePlayer.prevTimeInPortal) * par1;
         if (var10 > 0.0F) {
            this.renderPortalOverlay(var10, var6, var7);
         }
      }

      if (!this.mc.playerController.enableEverythingIsScrewedUpMode()) {
         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
         this.mc.renderEngine.bindTexture("/gui/gui.png");
         InventoryPlayer var31 = this.mc.thePlayer.inventory;
         this.zLevel = -90.0F;
         this.b(var6 / 2 - 91, var7 - 22, 0, 0, 182, 22);
         this.b(var6 / 2 - 91 - 1 + var31.currentItem * 20, var7 - 22 - 1, 0, 22, 24, 22);
         this.mc.renderEngine.bindTexture("/gui/icons.png");
         GL11.glEnable(3042);
         GL11.glBlendFunc(775, 769);
         this.b(var6 / 2 - 7, var7 / 2 - 7, 0, 0, 16, 16);
         GL11.glDisable(3042);
         boolean var11 = this.mc.thePlayer.hurtResistantTime / 3 % 2 == 1;
         if (this.mc.thePlayer.hurtResistantTime < 10) {
            var11 = false;
         }

         int var12 = this.mc.thePlayer.aX();
         int var13 = this.mc.thePlayer.prevHealth;
         this.rand.setSeed(this.updateCounter * 312871);
         boolean var14 = false;
         FoodStats var15 = this.mc.thePlayer.cn();
         int var16 = var15.getFoodLevel();
         int var17 = var15.getPrevFoodLevel();
         this.mc.mcProfiler.startSection("bossHealth");
         this.renderBossHealth();
         this.mc.mcProfiler.endSection();
         if (this.mc.playerController.shouldDrawHUD()) {
            int var18 = var6 / 2 - 91;
            int var19 = var6 / 2 + 91;
            this.mc.mcProfiler.startSection("expBar");
            int var20 = this.mc.thePlayer.cm();
            if (var20 > 0) {
               short var21 = 182;
               int var22 = (int)(this.mc.thePlayer.experience * (var21 + 1));
               int var23 = var7 - 32 + 3;
               this.b(var18, var23, 0, 64, var21, 5);
               if (var22 > 0) {
                  this.b(var18, var23, 0, 69, var22, 5);
               }
            }

            int var47 = var7 - 39;
            int var22 = var47 - 10;
            int var23 = this.mc.thePlayer.aZ();
            int var24 = -1;
            if (this.mc.thePlayer.a(Potion.regeneration)) {
               var24 = this.updateCounter % 25;
            }

            this.mc.mcProfiler.endStartSection("healthArmor");

            for (int var25 = 0; var25 < 10; var25++) {
               if (var23 > 0) {
                  int var26 = var18 + var25 * 8;
                  if (var25 * 2 + 1 < var23) {
                     this.b(var26, var22, 34, 9, 9, 9);
                  }

                  if (var25 * 2 + 1 == var23) {
                     this.b(var26, var22, 25, 9, 9, 9);
                  }

                  if (var25 * 2 + 1 > var23) {
                     this.b(var26, var22, 16, 9, 9, 9);
                  }
               }

               int var26x = 16;
               if (this.mc.thePlayer.a(Potion.poison)) {
                  var26x += 36;
               } else if (this.mc.thePlayer.a(Potion.wither)) {
                  var26x += 72;
               }

               byte var27 = 0;
               if (var11) {
                  var27 = 1;
               }

               int var28 = var18 + var25 * 8;
               int var29 = var47;
               if (var12 <= 4) {
                  var29 = var47 + this.rand.nextInt(2);
               }

               if (var25 == var24) {
                  var29 -= 2;
               }

               byte var30 = 0;
               if (this.mc.theWorld.M().isHardcoreModeEnabled() || BTWMod.useHardcoreHearts) {
                  var30 = 5;
               }

               this.b(var28, var29, 16 + var27 * 9, 9 * var30, 9, 9);
               if (var11) {
                  if (var25 * 2 + 1 < var13) {
                     this.b(var28, var29, var26x + 54, 9 * var30, 9, 9);
                  }

                  if (var25 * 2 + 1 == var13) {
                     this.b(var28, var29, var26x + 63, 9 * var30, 9, 9);
                  }
               }

               if (var25 * 2 + 1 < var12) {
                  this.b(var28, var29, var26x + 36, 9 * var30, 9, 9);
               }

               if (var25 * 2 + 1 == var12) {
                  this.b(var28, var29, var26x + 45, 9 * var30, 9, 9);
               }
            }

            this.mc.mcProfiler.endStartSection("food");
            this.drawFoodOverlay(var19, var47);
            int iSightlessTextOffset = -8;
            this.mc.mcProfiler.endStartSection("air");
            if (this.mc.thePlayer.a(Material.water) || this.mc.thePlayer.ak() < 300) {
               int var75 = this.mc.thePlayer.ak();
               int var26xx = MathHelper.ceiling_double_int((var75 - 2) * 10.0 / 300.0);
               int var50 = MathHelper.ceiling_double_int(var75 * 10.0 / 300.0) - var26xx;

               for (int var28x = 0; var28x < var26xx + var50; var28x++) {
                  if (var28x < var26xx) {
                     this.b(var19 - var28x * 8 - 9, var22, 16, 18, 9, 9);
                  } else {
                     this.b(var19 - var28x * 8 - 9, var22, 25, 18, 9, 9);
                  }
               }
            }

            this.drawPenaltyText(var19, var22);
            this.mc.mcProfiler.endSection();
         }

         GL11.glDisable(3042);
         this.mc.mcProfiler.startSection("actionBar");
         GL11.glEnable(32826);
         RenderHelper.enableGUIStandardItemLighting();

         for (int var18x = 0; var18x < 9; var18x++) {
            int var19x = var6 / 2 - 90 + var18x * 20 + 2;
            int var20x = var7 - 16 - 3;
            this.renderInventorySlot(var18x, var19x, var20x, par1);
         }

         RenderHelper.disableStandardItemLighting();
         GL11.glDisable(32826);
         this.mc.mcProfiler.endSection();
      }

      if (this.mc.thePlayer.cj() > 0) {
         this.mc.mcProfiler.startSection("sleep");
         GL11.glDisable(2929);
         GL11.glDisable(3008);
         int var32 = this.mc.thePlayer.cj();
         float var33 = var32 / 100.0F;
         if (var33 > 1.0F) {
            var33 = 1.0F - (var32 - 100) / 10.0F;
         }

         int var12 = (int)(220.0F * var33) << 24 | 1052704;
         a(0, 0, var6, var7, var12);
         GL11.glEnable(3008);
         GL11.glEnable(2929);
         this.mc.mcProfiler.endSection();
      }

      if (this.mc.playerController.func_78763_f() && this.mc.thePlayer.experienceLevel > 0) {
         this.mc.mcProfiler.startSection("expLevel");
         boolean var11x = false;
         int var12 = var11x ? 16777215 : 8453920;
         String var34 = "" + this.mc.thePlayer.experienceLevel;
         int var38 = (var6 - var8.getStringWidth(var34)) / 2;
         int var37 = var7 - 31 - 4;
         var8.drawString(var34, var38 + 1, var37, 0);
         var8.drawString(var34, var38 - 1, var37, 0);
         var8.drawString(var34, var38, var37 + 1, 0);
         var8.drawString(var34, var38, var37 - 1, 0);
         var8.drawString(var34, var38, var37, var12);
         this.mc.mcProfiler.endSection();
      }

      if (this.mc.gameSettings.heldItemTooltips) {
         this.mc.mcProfiler.startSection("toolHighlight");
         if (this.remainingHighlightTicks > 0 && this.highlightingItemStack != null) {
            String var35 = this.highlightingItemStack.getDisplayName();
            int var12 = (var6 - var8.getStringWidth(var35)) / 2;
            int var13 = var7 - 59;
            if (!this.mc.playerController.shouldDrawHUD()) {
               var13 += 14;
            }

            int var38 = (int)(this.remainingHighlightTicks * 256.0F / 10.0F);
            if (var38 > 255) {
               var38 = 255;
            }

            if (var38 > 0) {
               GL11.glPushMatrix();
               GL11.glEnable(3042);
               GL11.glBlendFunc(770, 771);
               var8.drawStringWithShadow(var35, var12, var13, 16777215 + (var38 << 24));
               GL11.glDisable(3042);
               GL11.glPopMatrix();
            }
         }

         this.mc.mcProfiler.endSection();
      }

      if (this.mc.isDemo()) {
         this.mc.mcProfiler.startSection("demo");
         String var35x = "";
         if (this.mc.theWorld.H() >= 120500L) {
            var35x = StatCollector.translateToLocal("demo.demoExpired");
         } else {
            var35x = String.format(StatCollector.translateToLocal("demo.remainingTime"), StringUtils.ticksToElapsedTime((int)(120500L - this.mc.theWorld.H())));
         }

         int var12x = var8.getStringWidth(var35x);
         var8.drawStringWithShadow(var35x, var6 - var12x - 10, 5, 16777215);
         this.mc.mcProfiler.endSection();
      }

      if (this.mc.gameSettings.showDebugInfo) {
         this.mc.mcProfiler.startSection("debug");
         GL11.glPushMatrix();
         var8.drawStringWithShadow("Minecraft 1.5.2 (" + this.mc.debug + ")", 2, 2, 16777215);
         var8.drawStringWithShadow(this.mc.debugInfoRenders(), 2, 12, 16777215);
         var8.drawStringWithShadow(this.mc.getEntityDebug(), 2, 22, 16777215);
         var8.drawStringWithShadow(this.mc.debugInfoEntities(), 2, 32, 16777215);
         var8.drawStringWithShadow(this.mc.getWorldProviderName(), 2, 42, 16777215);
         long var36 = Runtime.getRuntime().maxMemory();
         long var40 = Runtime.getRuntime().totalMemory();
         long var43 = Runtime.getRuntime().freeMemory();
         long var44 = var40 - var43;
         String var46 = "Used memory: " + var44 * 100L / var36 + "% (" + var44 / 1024L / 1024L + "MB) of " + var36 / 1024L / 1024L + "MB";
         this.b(var8, var46, var6 - var8.getStringWidth(var46) - 2, 2, 14737632);
         var46 = "Allocated memory: " + var40 * 100L / var36 + "% (" + var40 / 1024L / 1024L + "MB)";
         this.b(var8, var46, var6 - var8.getStringWidth(var46) - 2, 12, 14737632);
         this.renderModDebugOverlay();
         GL11.glPopMatrix();
         this.mc.mcProfiler.endSection();
      }

      if (this.recordPlayingUpFor > 0) {
         this.mc.mcProfiler.startSection("overlayMessage");
         float var33 = this.recordPlayingUpFor - par1;
         int var12x = (int)(var33 * 256.0F / 20.0F);
         if (var12x > 255) {
            var12x = 255;
         }

         if (var12x > 0) {
            GL11.glPushMatrix();
            GL11.glTranslatef(var6 / 2, var7 - 48, 0.0F);
            GL11.glEnable(3042);
            GL11.glBlendFunc(770, 771);
            int var13x = 16777215;
            if (this.recordIsPlaying) {
               var13x = Color.HSBtoRGB(var33 / 50.0F, 0.7F, 0.6F) & 16777215;
            }

            var8.drawString(this.recordPlaying, -var8.getStringWidth(this.recordPlaying) / 2, -4, var13x + (var12x << 24));
            GL11.glDisable(3042);
            GL11.glPopMatrix();
         }

         this.mc.mcProfiler.endSection();
      }

      ScoreObjective var42 = this.mc.theWorld.W().func_96539_a(1);
      if (var42 != null) {
         this.func_96136_a(var42, var7, var6, var8);
      }

      GL11.glEnable(3042);
      GL11.glBlendFunc(770, 771);
      GL11.glDisable(3008);
      GL11.glPushMatrix();
      GL11.glTranslatef(0.0F, var7 - 48, 0.0F);
      this.mc.mcProfiler.startSection("chat");
      this.persistantChatGUI.drawChat(this.updateCounter);
      this.mc.mcProfiler.endSection();
      GL11.glPopMatrix();
      var42 = this.mc.theWorld.W().func_96539_a(0);
      if (this.mc.gameSettings.keyBindPlayerList.pressed
         && (!this.mc.isIntegratedServerRunning() || this.mc.thePlayer.sendQueue.playerInfoList.size() > 1 || var42 != null)) {
         this.mc.mcProfiler.startSection("playerList");
         NetClientHandler var39 = this.mc.thePlayer.sendQueue;
         List var41 = var39.playerInfoList;
         int var38x = var39.currentServerMaxPlayers;
         int var37 = var38x;

         int var16;
         for (var16 = 1; var37 > 20; var37 = (var38x + var16 - 1) / var16) {
            var16++;
         }

         int var17 = 300 / var16;
         if (var17 > 150) {
            var17 = 150;
         }

         int var18x = (var6 - var16 * var17) / 2;
         byte var45 = 10;
         a(var18x - 1, var45 - 1, var18x + var17 * var16, var45 + 9 * var37, Integer.MIN_VALUE);

         for (int var20x = 0; var20x < var38x; var20x++) {
            int var47x = var18x + var20x % var16 * var17;
            int var22x = var45 + var20x / var16 * 9;
            a(var47x, var22x, var47x + var17 - 1, var22x + 8, 553648127);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glEnable(3008);
            if (var20x < var41.size()) {
               GuiPlayerInfo var49 = (GuiPlayerInfo)var41.get(var20x);
               ScorePlayerTeam var48 = this.mc.theWorld.W().getPlayersTeam(var49.name);
               String var53 = ScorePlayerTeam.func_96667_a(var48, var49.name);
               var8.drawStringWithShadow(var53, var47x, var22x, 16777215);
               if (var42 != null) {
                  int var26xx = var47x + var8.getStringWidth(var53) + 5;
                  int var50 = var47x + var17 - 12 - 5;
                  if (var50 - var26xx > 5) {
                     Score var56 = var42.getScoreboard().func_96529_a(var49.name, var42);
                     String var57 = EnumChatFormatting.YELLOW + "" + var56.func_96652_c();
                     var8.drawStringWithShadow(var57, var50 - var8.getStringWidth(var57), var22x, 16777215);
                  }
               }

               GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
               this.mc.renderEngine.bindTexture("/gui/icons.png");
               byte var55 = 0;
               boolean var54 = false;
               byte var27x;
               if (var49.responseTime < 0) {
                  var27x = 5;
               } else if (var49.responseTime < 150) {
                  var27x = 0;
               } else if (var49.responseTime < 300) {
                  var27x = 1;
               } else if (var49.responseTime < 600) {
                  var27x = 2;
               } else if (var49.responseTime < 1000) {
                  var27x = 3;
               } else {
                  var27x = 4;
               }

               this.zLevel += 100.0F;
               this.b(var47x + var17 - 12, var22x, 0 + var55 * 10, 176 + var27x * 8, 10, 8);
               this.zLevel -= 100.0F;
            }
         }
      }

      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      GL11.glDisable(2896);
      GL11.glEnable(3008);
   }

   private void func_96136_a(ScoreObjective par1ScoreObjective, int par2, int par3, FontRenderer par4FontRenderer) {
      Scoreboard var5 = par1ScoreObjective.getScoreboard();
      Collection var6 = var5.func_96534_i(par1ScoreObjective);
      if (var6.size() <= 15) {
         int var7 = par4FontRenderer.getStringWidth(par1ScoreObjective.getDisplayName());

         for (Score var9 : var6) {
            ScorePlayerTeam var10 = var5.getPlayersTeam(var9.func_96653_e());
            String var11 = ScorePlayerTeam.func_96667_a(var10, var9.func_96653_e()) + ": " + EnumChatFormatting.RED + var9.func_96652_c();
            var7 = Math.max(var7, par4FontRenderer.getStringWidth(var11));
         }

         int var22 = var6.size() * par4FontRenderer.FONT_HEIGHT;
         int var23 = par2 / 2 + var22 / 3;
         byte var25 = 3;
         int var24 = par3 - var7 - var25;
         int var12 = 0;

         for (Score var14 : var6) {
            var12++;
            ScorePlayerTeam var15 = var5.getPlayersTeam(var14.func_96653_e());
            String var16 = ScorePlayerTeam.func_96667_a(var15, var14.func_96653_e());
            String var17 = EnumChatFormatting.RED + "" + var14.func_96652_c();
            int var19 = var23 - var12 * par4FontRenderer.FONT_HEIGHT;
            int var20 = par3 - var25 + 2;
            a(var24 - 2, var19, var20, var19 + par4FontRenderer.FONT_HEIGHT, 1342177280);
            par4FontRenderer.drawString(var16, var24, var19, 553648127);
            par4FontRenderer.drawString(var17, var20 - par4FontRenderer.getStringWidth(var17), var19, 553648127);
            if (var12 == var6.size()) {
               String var21 = par1ScoreObjective.getDisplayName();
               a(var24 - 2, var19 - par4FontRenderer.FONT_HEIGHT - 1, var20, var19 - 1, 1610612736);
               a(var24 - 2, var19 - 1, var20, var19, 1342177280);
               par4FontRenderer.drawString(
                  var21, var24 + var7 / 2 - par4FontRenderer.getStringWidth(var21) / 2, var19 - par4FontRenderer.FONT_HEIGHT, 553648127
               );
            }
         }
      }
   }

   private void renderBossHealth() {
      if (BossStatus.bossName != null && BossStatus.statusBarLength > 0) {
         BossStatus.statusBarLength--;
         FontRenderer var1 = this.mc.fontRenderer;
         ScaledResolution var2 = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
         int var3 = var2.getScaledWidth();
         short var4 = 182;
         int var5 = var3 / 2 - var4 / 2;
         int var6 = (int)(BossStatus.healthScale * (var4 + 1));
         byte var7 = 12;
         this.b(var5, var7, 0, 74, var4, 5);
         this.b(var5, var7, 0, 74, var4, 5);
         if (var6 > 0) {
            this.b(var5, var7, 0, 79, var6, 5);
         }

         String var8 = BossStatus.bossName;
         var1.drawStringWithShadow(var8, var3 / 2 - var1.getStringWidth(var8) / 2, var7 - 10, 16777215);
         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
         this.mc.renderEngine.bindTexture("/gui/icons.png");
      }
   }

   private void renderPumpkinBlur(int par1, int par2) {
      GL11.glDisable(2929);
      GL11.glDepthMask(false);
      GL11.glBlendFunc(770, 771);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      GL11.glDisable(3008);
      this.mc.renderEngine.bindTexture("%blur%/misc/pumpkinblur.png");
      Tessellator var3 = Tessellator.instance;
      var3.startDrawingQuads();
      var3.addVertexWithUV(0.0, par2, -90.0, 0.0, 1.0);
      var3.addVertexWithUV(par1, par2, -90.0, 1.0, 1.0);
      var3.addVertexWithUV(par1, 0.0, -90.0, 1.0, 0.0);
      var3.addVertexWithUV(0.0, 0.0, -90.0, 0.0, 0.0);
      var3.draw();
      GL11.glDepthMask(true);
      GL11.glEnable(2929);
      GL11.glEnable(3008);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
   }

   private void renderVignette(float par1, int par2, int par3) {
      par1 = 1.0F - par1;
      if (par1 < 0.0F) {
         par1 = 0.0F;
      }

      if (par1 > 1.0F) {
         par1 = 1.0F;
      }

      this.prevVignetteBrightness = (float)(this.prevVignetteBrightness + (par1 - this.prevVignetteBrightness) * 0.01);
      GL11.glDisable(2929);
      GL11.glDepthMask(false);
      GL11.glBlendFunc(0, 769);
      GL11.glColor4f(this.prevVignetteBrightness, this.prevVignetteBrightness, this.prevVignetteBrightness, 1.0F);
      this.mc.renderEngine.bindTexture("%blur%/misc/vignette.png");
      Tessellator var4 = Tessellator.instance;
      var4.startDrawingQuads();
      var4.addVertexWithUV(0.0, par3, -90.0, 0.0, 1.0);
      var4.addVertexWithUV(par2, par3, -90.0, 1.0, 1.0);
      var4.addVertexWithUV(par2, 0.0, -90.0, 1.0, 0.0);
      var4.addVertexWithUV(0.0, 0.0, -90.0, 0.0, 0.0);
      var4.draw();
      GL11.glDepthMask(true);
      GL11.glEnable(2929);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      GL11.glBlendFunc(770, 771);
   }

   private void renderPortalOverlay(float par1, int par2, int par3) {
      if (par1 < 1.0F) {
         par1 *= par1;
         par1 *= par1;
         par1 = par1 * 0.8F + 0.2F;
      }

      GL11.glDisable(3008);
      GL11.glDisable(2929);
      GL11.glDepthMask(false);
      GL11.glBlendFunc(770, 771);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, par1);
      this.mc.renderEngine.bindTexture("/terrain.png");
      Icon var4 = Block.portal.m(1);
      float var5 = var4.getMinU();
      float var6 = var4.getMinV();
      float var7 = var4.getMaxU();
      float var8 = var4.getMaxV();
      Tessellator var9 = Tessellator.instance;
      var9.startDrawingQuads();
      var9.addVertexWithUV(0.0, par3, -90.0, var5, var8);
      var9.addVertexWithUV(par2, par3, -90.0, var7, var8);
      var9.addVertexWithUV(par2, 0.0, -90.0, var7, var6);
      var9.addVertexWithUV(0.0, 0.0, -90.0, var5, var6);
      var9.draw();
      GL11.glDepthMask(true);
      GL11.glEnable(2929);
      GL11.glEnable(3008);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
   }

   private void renderInventorySlot(int par1, int par2, int par3, float par4) {
      ItemStack var5 = this.mc.thePlayer.inventory.mainInventory[par1];
      if (var5 != null) {
         float var6 = var5.animationsToGo - par4;
         if (var6 > 0.0F) {
            GL11.glPushMatrix();
            float var7 = 1.0F + var6 / 5.0F;
            GL11.glTranslatef(par2 + 8, par3 + 12, 0.0F);
            GL11.glScalef(1.0F / var7, (var7 + 1.0F) / 2.0F, 1.0F);
            GL11.glTranslatef(-(par2 + 8), -(par3 + 12), 0.0F);
         }

         int iItemID = var5.getItem().itemID;
         if (iItemID == Item.compass.itemID) {
            TextureCompass.compassTexture.updateActive();
         }

         itemRenderer.renderItemAndEffectIntoGUI(this.mc.fontRenderer, this.mc.renderEngine, var5, par2, par3);
         if (iItemID == Item.compass.itemID) {
            TextureCompass.compassTexture.updateInert();
         }

         if (var6 > 0.0F) {
            GL11.glPopMatrix();
         }

         itemRenderer.renderItemOverlayIntoGUI(this.mc.fontRenderer, this.mc.renderEngine, var5, par2, par3);
      }
   }

   public void updateTick() {
      if (this.recordPlayingUpFor > 0) {
         this.recordPlayingUpFor--;
      }

      this.updateCounter++;
      if (this.mc.thePlayer != null) {
         ItemStack var1 = this.mc.thePlayer.inventory.getCurrentItem();
         if (var1 == null) {
            this.remainingHighlightTicks = 0;
         } else if (this.highlightingItemStack != null
            && var1.itemID == this.highlightingItemStack.itemID
            && ItemStack.areItemStackTagsEqual(var1, this.highlightingItemStack)
            && (var1.isItemStackDamageable() || var1.getItemDamage() == this.highlightingItemStack.getItemDamage())) {
            if (this.remainingHighlightTicks > 0) {
               this.remainingHighlightTicks--;
            }
         } else {
            this.remainingHighlightTicks = 40;
         }

         this.highlightingItemStack = var1;
      }
   }

   public void setRecordPlayingMessage(String par1Str) {
      this.recordPlaying = "Now playing: " + par1Str;
      this.recordPlayingUpFor = 60;
      this.recordIsPlaying = true;
   }

   public GuiNewChat getChatGUI() {
      return this.persistantChatGUI;
   }

   public int getUpdateCounter() {
      return this.updateCounter;
   }

   public void renderModDebugOverlay() {
      this.addChunkBoundaryDisplay(64);
      this.addLoadedChunksOnServerDisplay(84);
   }

   private void addChunkBoundaryDisplay(int iYPos) {
      FontRenderer fontRenderer = this.mc.fontRenderer;
      int chunkX = MathHelper.floor_double(this.mc.thePlayer.posX) % 16;
      if (chunkX < 0) {
         chunkX += 16;
      }

      int iDistToChunkBndryX = chunkX;
      if (15 - chunkX < chunkX) {
         iDistToChunkBndryX = 15 - chunkX;
      }

      int chunkZ = MathHelper.floor_double(this.mc.thePlayer.posZ) % 16;
      if (chunkZ < 0) {
         chunkZ += 16;
      }

      int iDistToChunkBndryZ = chunkZ;
      if (15 - chunkZ < chunkZ) {
         iDistToChunkBndryZ = 15 - chunkZ;
      }

      int iDistToChunkBndry = iDistToChunkBndryX;
      if (iDistToChunkBndryZ < iDistToChunkBndryX) {
         iDistToChunkBndry = iDistToChunkBndryZ;
      }

      this.b(fontRenderer, String.format("Dist To Chnk Bndry: %d", iDistToChunkBndry), 2, iYPos, 14737632);
   }

   private void addCurrentBiomeDisplay(int iYPos) {
      FontRenderer fontRenderer = this.mc.fontRenderer;
      EntityPlayer player = this.mc.thePlayer;
      BiomeGenBase biomeGen = player.worldObj.getBiomeGenForCoords(MathHelper.floor_double(player.posX), MathHelper.floor_double(player.posZ));
      String sBiomeDescriptor;
      if (biomeGen != null) {
         sBiomeDescriptor = biomeGen.getClass().getName();
      } else {
         sBiomeDescriptor = "unknown";
      }

      this.b(fontRenderer, "Biome: " + sBiomeDescriptor, 2, iYPos, 14737632);
   }

   private void addMovementSpeedDisplay(int iYPos) {
      FontRenderer fontRenderer = this.mc.fontRenderer;
      double playerSpeed = Math.sqrt(this.mc.thePlayer.motionX * this.mc.thePlayer.motionX + this.mc.thePlayer.motionZ * this.mc.thePlayer.motionZ);
      String sPlayerSpeedString = String.format("Player Speed: %.5f", playerSpeed);
      this.b(fontRenderer, sPlayerSpeedString, 2, iYPos, 14737632);
      double riddenSpeed = 0.0;
      if (this.mc.thePlayer.ridingEntity != null) {
         riddenSpeed = Math.sqrt(
            this.mc.thePlayer.ridingEntity.motionX * this.mc.thePlayer.ridingEntity.motionX
               + this.mc.thePlayer.ridingEntity.motionZ * this.mc.thePlayer.ridingEntity.motionZ
         );
      }

      String sRiddenSpeedString = String.format("Ridden Speed: %.5f", riddenSpeed);
      new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
      int iXPos = 12 + fontRenderer.getStringWidth(sPlayerSpeedString);
      this.b(fontRenderer, String.format("Ridden Speed: %.5f", riddenSpeed), iXPos, iYPos, 14737632);
   }

   private void addLoadedChunksOnServerDisplay(int iYPos) {
      if (MinecraftServer.getServer() != null) {
         FontRenderer fontrenderer = this.mc.fontRenderer;
         if (MinecraftServer.getServer().worldServers[0] != null) {
            IChunkProvider provider = MinecraftServer.getServer().worldServers[0].K();
            this.b(fontrenderer, "Overworld " + provider.makeString(), 2, 84, 14737632);
         }

         if (MinecraftServer.getServer().worldServers[1] != null) {
            IChunkProvider provider = MinecraftServer.getServer().worldServers[1].K();
            this.b(fontrenderer, "Nether " + provider.makeString(), 2, 94, 14737632);
         }

         if (MinecraftServer.getServer().worldServers[2] != null) {
            IChunkProvider provider = MinecraftServer.getServer().worldServers[2].K();
            this.b(fontrenderer, "End " + provider.makeString(), 2, 104, 14737632);
         }
      }
   }

   private void renderEnderSpectaclesBlur() {
      ScaledResolution resolution = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
      int iScreenWidth = resolution.getScaledWidth();
      int iScreenHeight = resolution.getScaledHeight();
      GL11.glDisable(2929);
      GL11.glDepthMask(false);
      GL11.glBlendFunc(770, 771);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      GL11.glDisable(3008);
      this.mc.renderEngine.bindTexture("%blur%/btwmodtex/spectaclesblur.png");
      Tessellator tessellator = Tessellator.instance;
      tessellator.startDrawingQuads();
      tessellator.addVertexWithUV(0.0, iScreenHeight, -90.0, 0.0, 1.0);
      tessellator.addVertexWithUV(iScreenWidth, iScreenHeight, -90.0, 1.0, 1.0);
      tessellator.addVertexWithUV(iScreenWidth, 0.0, -90.0, 1.0, 0.0);
      tessellator.addVertexWithUV(0.0, 0.0, -90.0, 0.0, 0.0);
      tessellator.draw();
      GL11.glDepthMask(true);
      GL11.glEnable(2929);
      GL11.glEnable(3008);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
   }

   public void renderGameOverlayWithGuiDisabled(float fSmoothCameraPartialTicks, boolean bScreenActive, int iMouseX, int iMouseY) {
      ScaledResolution resolution = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
      int iScreenWidth = resolution.getScaledWidth();
      int iScreenHeight = resolution.getScaledHeight();
      FontRenderer fontRenderer = this.mc.fontRenderer;
      this.mc.entityRenderer.setupOverlayRendering();
      GL11.glEnable(3042);
      if (Minecraft.isFancyGraphicsEnabled()) {
         this.renderVignette(this.mc.thePlayer.c(fSmoothCameraPartialTicks), iScreenWidth, iScreenHeight);
      } else {
         GL11.glBlendFunc(770, 771);
      }

      ItemStack var9 = this.mc.thePlayer.inventory.armorItemInSlot(3);
      if (this.mc.gameSettings.thirdPersonView == 0 && var9 != null && var9.itemID == Block.pumpkin.blockID) {
         this.renderPumpkinBlur(iScreenWidth, iScreenHeight);
      }

      this.renderModSpecificPlayerSightEffects();
      if (!this.mc.thePlayer.a(Potion.confusion)) {
         float var10 = this.mc.thePlayer.prevTimeInPortal + (this.mc.thePlayer.timeInPortal - this.mc.thePlayer.prevTimeInPortal) * fSmoothCameraPartialTicks;
         if (var10 > 0.0F) {
            this.renderPortalOverlay(var10, iScreenWidth, iScreenHeight);
         }
      }

      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      GL11.glDisable(2896);
      GL11.glEnable(3008);
   }

   private void renderModSpecificPlayerSightEffects() {
      if (this.mc.gameSettings.thirdPersonView == 0) {
         if (this.mc.thePlayer.isWearingEnderSpectacles()) {
            this.renderEnderSpectaclesBlur();
            this.addTrueSightParticles();
         } else if (this.mc.thePlayer.a(BTWMod.potionTrueSight)) {
            this.addTrueSightParticles();
            this.addSpawnChunksParticles();
         }
      }
   }

   private void addTrueSightParticles() {
      if (!this.mc.isGamePaused) {
         EntityPlayer player = this.mc.thePlayer;
         World world = this.mc.theWorld;
         int iParticleSetting = this.mc.gameSettings.particleSetting;
         int iPlayerI = MathHelper.floor_double(player.posX);
         int iPlayerJ = MathHelper.floor_double(player.posY);
         int iPlayerK = MathHelper.floor_double(player.posZ);

         for (int iTempI = iPlayerI - 10; iTempI <= iPlayerI + 10; iTempI++) {
            for (int iTempJ = iPlayerJ - 10; iTempJ <= iPlayerJ + 10; iTempJ++) {
               for (int iTempK = iPlayerK - 10; iTempK <= iPlayerK + 10; iTempK++) {
                  if (WorldUtils.canMobsSpawnHere(world, iTempI, iTempJ, iTempK)) {
                     double dVerticalOffset = 0.0;
                     Block blockBelow = Block.blocksList[world.getBlockId(iTempI, iTempJ - 1, iTempK)];
                     if (blockBelow != null) {
                        dVerticalOffset = blockBelow.mobSpawnOnVerticalOffset(world, iTempI, iTempJ - 1, iTempK);
                     }

                     if (this.rand.nextInt(12) <= 2 - iParticleSetting) {
                        double particleX = iTempI + this.rand.nextDouble();
                        double particleY = iTempJ + dVerticalOffset + this.rand.nextDouble() * 0.25;
                        double particleZ = iTempK + this.rand.nextDouble();
                        this.spawnTrueSightParticle(world, particleX, particleY, particleZ);
                     }
                  }
               }
            }
         }
      }
   }

   private void spawnTrueSightParticle(World world, double dXPos, double dYPos, double dZPos) {
      EntityFX particleEntity = (EntityFX)EntityList.createEntityOfType(EntitySpellParticleFX.class, world, dXPos, dYPos, dZPos, 0.0, 0.0, 0.0);
      particleEntity.setRBGColorF(0.0F, 0.0F, 0.0F);
      this.mc.effectRenderer.addEffect(particleEntity);
   }

   private void addSpawnChunksParticles() {
      World world = this.mc.theWorld;
      EntityPlayer player = this.mc.thePlayer;
      if (!this.mc.isGamePaused && player.getSpawnChunksVisualizationLocationJ() != 0 && world.provider.dimensionId == 0) {
         int iViewDistanceChunks = world.getActiveChunkRangeInChunks();
         int iFirstPassRange = (iViewDistanceChunks + 2) * 16;
         int iPlayerX = MathHelper.floor_double(player.posX);
         int iDeltaPosX = iPlayerX - player.getSpawnChunksVisualizationLocationI();
         if (iDeltaPosX >= -iFirstPassRange && iDeltaPosX <= iFirstPassRange) {
            int iPlayerZ = MathHelper.floor_double(player.posZ);
            int iDeltaPosZ = iPlayerZ - player.getSpawnChunksVisualizationLocationK();
            if (iDeltaPosZ >= -iFirstPassRange && iDeltaPosZ <= iFirstPassRange) {
               int iParticleSetting = this.mc.gameSettings.particleSetting;
               int iNumParticles = 200 - iParticleSetting * 100;

               for (int iTempCount = 0; iTempCount < iNumParticles; iTempCount++) {
                  double particleY = player.posY - 10.0 + this.rand.nextDouble() * 10.0 * 2.0;
                  if (particleY > 0.0 && particleY <= 256.0) {
                     double particleX = player.posX - 10.0 + this.rand.nextDouble() * 10.0 * 2.0;
                     double particleZ = player.posZ - 10.0 + this.rand.nextDouble() * 10.0 * 2.0;
                     int iSpawnChunkX = player.getSpawnChunksVisualizationLocationI() >> 4;
                     int iSpawnChunkZ = player.getSpawnChunksVisualizationLocationK() >> 4;
                     if (this.isPosInSpawnChunkZone(particleX, particleY, particleZ, iSpawnChunkX, iSpawnChunkZ, iViewDistanceChunks)) {
                        if (this.isPosInSpawnChunkZone(particleX, particleY, particleZ, iSpawnChunkX, iSpawnChunkZ, iViewDistanceChunks - 2)) {
                           if (this.isPosInSpawnBlock(player, particleX, particleY, particleZ)) {
                              this.spawnSpawnPointParticle(world, particleX, particleY, particleZ);
                           } else {
                              this.spawnSpawnChunkInnerParticle(world, particleX, particleY, particleZ);
                           }
                        } else {
                           this.spawnSpawnChunkOuterParticle(world, particleX, particleY, particleZ);
                        }
                     }
                  }
               }
            }
         }
      }
   }

   private void spawnSpawnChunkOuterParticle(World world, double dXPos, double dYPos, double dZPos) {
      EntityFX particleEntity = (EntityFX)EntityList.createEntityOfType(EntityCritFX.class, world, dXPos, dYPos, dZPos, 0.0, 0.0, 0.0);
      particleEntity.setRBGColorF(0.0F, 0.0F, 0.5F);
      particleEntity.setAlphaF(0.5F);
      this.mc.effectRenderer.addEffect(particleEntity);
   }

   private void spawnSpawnChunkInnerParticle(World world, double dXPos, double dYPos, double dZPos) {
      EntityFX particleEntity = (EntityFX)EntityList.createEntityOfType(EntityCritFX.class, world, dXPos, dYPos, dZPos, 0.0, 0.0, 0.0);
      particleEntity.setRBGColorF(0.5F, 0.0F, 0.5F);
      particleEntity.setAlphaF(0.25F);
      this.mc.effectRenderer.addEffect(particleEntity);
   }

   private void spawnSpawnPointParticle(World world, double dXPos, double dYPos, double dZPos) {
      EntityFX particleEntity = (EntityFX)EntityList.createEntityOfType(EntityEnchantmentTableParticleFX.class, world, dXPos, dYPos, dZPos, 0.0, 0.0, 0.0);
      particleEntity.setRBGColorF(0.75F, 0.0F, 0.0F);
      particleEntity.setAlphaF(0.5F);
      this.mc.effectRenderer.addEffect(particleEntity);
   }

   public boolean isPosInSpawnChunkZone(double posX, double posY, double posZ, int iSpawnChunkX, int iSpawnChunkZ, int iChunkRange) {
      int iPosChunkX = MathHelper.floor_double(posX / 16.0);
      int iDeltaX = iPosChunkX - iSpawnChunkX;
      if (iDeltaX >= -iChunkRange && iDeltaX <= iChunkRange) {
         int iPosChunkZ = MathHelper.floor_double(posZ / 16.0);
         int iDeltaZ = iPosChunkZ - iSpawnChunkZ;
         if (iDeltaZ >= -iChunkRange && iDeltaZ <= iChunkRange) {
            return true;
         }
      }

      return false;
   }

   public boolean isPosInSpawnBlock(EntityPlayer player, double posX, double posY, double posZ) {
      int iDeltaX = MathHelper.floor_double(posX) - player.getSpawnChunksVisualizationLocationI();
      if (iDeltaX >= -1 && iDeltaX <= 1) {
         int iDeltaZ = MathHelper.floor_double(posZ) - player.getSpawnChunksVisualizationLocationK();
         if (iDeltaZ >= -1 && iDeltaZ <= 1) {
            return true;
         }
      }

      return false;
   }

   private void drawFoodOverlay(int iScreenX, int iScreenY) {
      FoodStats stats = this.mc.thePlayer.cn();
      int iHungerPenalty = this.mc.thePlayer.getStatusForCategory(BTWStatusCategory.HUNGER).map(StatusEffect::getLevel).orElse(0);
      int iFoodLevel = stats.getFoodLevel();
      float fSaturationLevel = stats.getSaturationLevel();
      int iSaturationPips = (int)((stats.getSaturationLevel() + 0.124F) * 4.0F);
      int iFullHungerPips = iFoodLevel / 6;
      if (this.mc.thePlayer.exhaustionAddedSinceLastGuiUpdate) {
         this.foodOverlayShakeCounter = 20;
         this.mc.thePlayer.exhaustionAddedSinceLastGuiUpdate = false;
      } else if (this.foodOverlayShakeCounter > 0) {
         this.foodOverlayShakeCounter--;
      }

      for (int iTempCount = 0; iTempCount < 10; iTempCount++) {
         int iShankScreenY = iScreenY;
         int iShankTextureOffsetX = 16;
         byte iBackgroundTextureOffsetX = 0;
         if (this.mc.thePlayer.a(Potion.hunger)) {
            iShankTextureOffsetX += 36;
            iBackgroundTextureOffsetX = 13;
         } else if (iTempCount < iSaturationPips >> 3) {
            iBackgroundTextureOffsetX = 1;
         }

         if (iHungerPenalty > 0 && this.updateCounter % (iFoodLevel * 5 + 1) == 0) {
            iShankScreenY = iScreenY + (this.rand.nextInt(3) - 1);
         } else if (this.foodOverlayShakeCounter > 0) {
            int iShakeAmount = 1;
            if (this.rand.nextInt(2) == 0) {
               iShakeAmount = -iShakeAmount;
            }

            iShankScreenY = iScreenY + iShakeAmount;
         }

         int iShankScreenX = iScreenX - iTempCount * 8 - 9;
         this.b(iShankScreenX, iShankScreenY, 16 + iBackgroundTextureOffsetX * 9, 27, 9, 9);
         if (iTempCount == iSaturationPips >> 3 && !this.mc.thePlayer.a(Potion.hunger)) {
            int iPartialPips = iSaturationPips % 8;
            if (iPartialPips != 0) {
               this.b(iShankScreenX + 8 - iPartialPips, iShankScreenY, 33 - iPartialPips, 27, 1 + iPartialPips, 9);
            }
         }

         if (iTempCount < iFullHungerPips) {
            this.b(iShankScreenX, iShankScreenY, iShankTextureOffsetX + 36, 27, 9, 9);
         } else if (iTempCount == iFullHungerPips) {
            int iPartialPips = iFoodLevel % 6;
            if (iPartialPips != 0) {
               this.b(iShankScreenX + 7 - iPartialPips, iShankScreenY, iShankTextureOffsetX + 36 + 7 - iPartialPips, 27, 3 + iPartialPips, 9);
            }
         }
      }
   }

   private void drawPenaltyText(int screenX, int screenY) {
      if (!this.mc.thePlayer.isDead) {
         ArrayList<StatusEffect> activeStatuses = this.mc.thePlayer.getAllActiveStatusEffects();
         FontRenderer fontRenderer = this.mc.fontRenderer;

         for (int i = 0; i < activeStatuses.size(); i++) {
            String status = StringTranslate.getInstance().translateKey(activeStatuses.get(i).getUnlocalizedName());
            int stringWidth = fontRenderer.getStringWidth(status);
            int offset = i * 10;
            fontRenderer.drawStringWithShadow(status, screenX - stringWidth, screenY - offset, 16777215);
         }
      }
   }

   public static boolean installationIntegrityTest() {
      return true;
   }
}
