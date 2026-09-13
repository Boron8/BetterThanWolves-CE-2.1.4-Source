package btw.block;

import net.minecraft.src.World;

public interface MechanicalBlock {
   boolean canOutputMechanicalPower();

   boolean canInputMechanicalPower();

   boolean isInputtingMechanicalPower(World var1, int var2, int var3, int var4);

   boolean isOutputtingMechanicalPower(World var1, int var2, int var3, int var4);

   boolean canInputAxlePowerToFacing(World var1, int var2, int var3, int var4, int var5);

   void overpower(World var1, int var2, int var3, int var4);
}
