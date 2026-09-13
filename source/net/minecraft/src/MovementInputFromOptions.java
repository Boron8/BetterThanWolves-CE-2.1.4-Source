package net.minecraft.src;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class MovementInputFromOptions extends MovementInput {
   private GameSettings gameSettings;

   public MovementInputFromOptions(GameSettings par1GameSettings) {
      this.gameSettings = par1GameSettings;
   }

   @Override
   public void updatePlayerMoveState() {
      this.moveStrafe = 0.0F;
      this.moveForward = 0.0F;
      if (this.gameSettings.keyBindForward.pressed) {
         this.moveForward++;
      }

      if (this.gameSettings.keyBindBack.pressed) {
         this.moveForward--;
      }

      if (this.gameSettings.keyBindLeft.pressed) {
         this.moveStrafe++;
      }

      if (this.gameSettings.keyBindRight.pressed) {
         this.moveStrafe--;
      }

      this.jump = this.gameSettings.keyBindJump.pressed;
      this.sneak = this.gameSettings.keyBindSneak.pressed;
      this.special = this.gameSettings.keyBindSpecial.pressed;
      if (this.sneak) {
         this.moveStrafe = (float)(this.moveStrafe * 0.3);
         this.moveForward = (float)(this.moveForward * 0.3);
      }
   }
}
