package net.minecraft.src;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
class RunnableTitleScreen implements Runnable {
   final GuiMainMenu field_104058_d;

   RunnableTitleScreen(GuiMainMenu par1GuiMainMenu) {
      this.field_104058_d = par1GuiMainMenu;
   }

   @Override
   public void run() {
   }
}
