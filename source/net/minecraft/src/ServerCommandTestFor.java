package net.minecraft.src;

public class ServerCommandTestFor extends CommandBase {
   @Override
   public String getCommandName() {
      return "testfor";
   }

   @Override
   public int getRequiredPermissionLevel() {
      return 2;
   }

   @Override
   public void processCommand(ICommandSender var1, String[] var2) {
      if (var2.length != 1) {
         throw new WrongUsageException("commands.testfor.usage");
      } else if (!(var1 instanceof TileEntityCommandBlock)) {
         throw new CommandException("commands.testfor.failed");
      } else {
         c(var1, var2[0]);
      }
   }

   @Override
   public boolean isUsernameIndex(String[] var1, int var2) {
      return var2 == 0;
   }
}
