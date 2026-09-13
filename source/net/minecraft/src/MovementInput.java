package net.minecraft.src;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class MovementInput {
   public float moveStrafe = 0.0F;
   public float moveForward = 0.0F;
   public boolean jump = false;
   public boolean sneak = false;
   public boolean special = false;

   public void updatePlayerMoveState() {
   }
}
