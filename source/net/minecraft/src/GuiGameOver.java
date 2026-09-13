package net.minecraft.src;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.lwjgl.opengl.GL11;

@Environment(EnvType.CLIENT)
public class GuiGameOver extends GuiScreen {
   private int cooldownTimer;

   @Override
   public void initGui() {
      this.buttonList.clear();
      if (this.mc.theWorld.M().isHardcoreModeEnabled()) {
         if (this.mc.isIntegratedServerRunning()) {
            this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 96, StatCollector.translateToLocal("deathScreen.deleteWorld")));
         } else {
            this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 96, StatCollector.translateToLocal("deathScreen.leaveServer")));
         }
      } else {
         this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 72, StatCollector.translateToLocal("deathScreen.respawn")));
         this.buttonList.add(new GuiButton(2, this.width / 2 - 100, this.height / 4 + 96, StatCollector.translateToLocal("deathScreen.titleScreen")));
         if (this.mc.session == null) {
            ((GuiButton)this.buttonList.get(1)).enabled = false;
         } else {
            long timeOfLastSpawnAssignment = this.mc.thePlayer.getTimeOfLastSpawnAssignment();
            GuiButton respawnButton = (GuiButton)this.buttonList.get(0);
            if (this.mc.theWorld.I() - timeOfLastSpawnAssignment < 10800L && timeOfLastSpawnAssignment != 0L) {
               respawnButton.displayString = StatCollector.translateToLocal("deathScreen.respawnNearby");
            } else {
               respawnButton.displayString = StatCollector.translateToLocal("deathScreen.respawn");
            }
         }
      }

      for (GuiButton var2 : this.buttonList) {
         var2.enabled = false;
      }
   }

   @Override
   protected void keyTyped(char par1, int par2) {
   }

   @Override
   protected void actionPerformed(GuiButton par1GuiButton) {
      switch (par1GuiButton.id) {
         case 1:
         case 3:
            this.mc.thePlayer.respawnPlayer();
            this.mc.displayGuiScreen((GuiScreen)null);
            break;
         case 2:
            this.mc.theWorld.sendQuittingDisconnectingPacket();
            this.mc.loadWorld((WorldClient)null);
            this.mc.displayGuiScreen(new GuiMainMenu());
      }
   }

   @Override
   public void drawScreen(int par1, int par2, float par3) {
      this.a(0, 0, this.width, this.height, 1615855616, -1602211792);
      GL11.glPushMatrix();
      GL11.glScalef(2.0F, 2.0F, 2.0F);
      boolean var4 = this.mc.theWorld.M().isHardcoreModeEnabled();
      String var5 = var4 ? StatCollector.translateToLocal("deathScreen.title.hardcore") : StatCollector.translateToLocal("deathScreen.title");
      this.a(this.fontRenderer, var5, this.width / 2 / 2, 30, 16777215);
      GL11.glPopMatrix();
      if (var4) {
         this.a(this.fontRenderer, StatCollector.translateToLocal("deathScreen.hardcoreInfo"), this.width / 2, 144, 16777215);
      }

      this.a(
         this.fontRenderer,
         StatCollector.translateToLocal("deathScreen.score") + ": " + EnumChatFormatting.YELLOW + this.mc.thePlayer.cb(),
         this.width / 2,
         100,
         16777215
      );
      super.drawScreen(par1, par2, par3);
   }

   @Override
   public boolean doesGuiPauseGame() {
      return false;
   }

   @Override
   public void updateScreen() {
      super.updateScreen();
      this.cooldownTimer++;
      if (this.cooldownTimer == 20) {
         for (GuiButton var2 : this.buttonList) {
            var2.enabled = true;
         }
      }
   }
}
