package net.minecraft.src;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class GuiSleepMP extends GuiChat {
   @Override
   public void initGui() {
      super.initGui();
      StringTranslate var1 = StringTranslate.getInstance();
      this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height - 70, var1.translateKey("multiplayer.stopSleeping")));
   }

   @Override
   protected void keyTyped(char par1, int par2) {
      if (par2 == 1) {
         this.wakeEntity();
      } else if (par2 == 28) {
         String var3 = this.inputField.getText().trim();
         if (var3.length() > 0) {
            this.mc.thePlayer.sendChatMessage(var3);
         }

         this.inputField.setText("");
         this.mc.ingameGUI.getChatGUI().resetScroll();
      } else {
         super.keyTyped(par1, par2);
      }
   }

   @Override
   protected void actionPerformed(GuiButton par1GuiButton) {
      if (par1GuiButton.id == 1) {
         this.wakeEntity();
      } else {
         super.a(par1GuiButton);
      }
   }

   private void wakeEntity() {
      NetClientHandler var1 = this.mc.thePlayer.sendQueue;
      var1.addToSendQueue(new Packet19EntityAction(this.mc.thePlayer, 3));
   }
}
