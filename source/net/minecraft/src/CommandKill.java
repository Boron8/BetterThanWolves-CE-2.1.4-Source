package net.minecraft.src;

public class CommandKill extends CommandBase {
   @Override
   public String getCommandName() {
      return "kill";
   }

   @Override
   public int getRequiredPermissionLevel() {
      return 0;
   }

   @Override
   public void processCommand(ICommandSender var1, String[] var2) {
      EntityPlayerMP var3 = c(var1);
      var3.attackEntityFrom(DamageSource.outOfWorld, 1000);
      var1.sendChatToPlayer("Ouch. That looks like it hurt.");
   }
}
