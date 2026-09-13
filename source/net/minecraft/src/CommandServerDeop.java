package net.minecraft.src;

import java.util.List;
import net.minecraft.server.MinecraftServer;

public class CommandServerDeop extends CommandBase {
   @Override
   public String getCommandName() {
      return "deop";
   }

   @Override
   public int getRequiredPermissionLevel() {
      return 3;
   }

   @Override
   public String getCommandUsage(ICommandSender var1) {
      return var1.translateString("commands.deop.usage");
   }

   @Override
   public void processCommand(ICommandSender var1, String[] var2) {
      if (var2.length == 1 && var2[0].length() > 0) {
         MinecraftServer.getServer().getConfigurationManager().removeOp(var2[0]);
         a(var1, "commands.deop.success", new Object[]{var2[0]});
      } else {
         throw new WrongUsageException("commands.deop.usage");
      }
   }

   @Override
   public List addTabCompletionOptions(ICommandSender var1, String[] var2) {
      return var2.length == 1 ? a(var2, MinecraftServer.getServer().getConfigurationManager().getOps()) : null;
   }
}
