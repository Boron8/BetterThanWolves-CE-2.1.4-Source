package net.minecraft.src;

import java.util.List;
import net.minecraft.server.MinecraftServer;

public class CommandServerPardon extends CommandBase {
   @Override
   public String getCommandName() {
      return "pardon";
   }

   @Override
   public int getRequiredPermissionLevel() {
      return 3;
   }

   @Override
   public String getCommandUsage(ICommandSender var1) {
      return var1.translateString("commands.unban.usage");
   }

   @Override
   public boolean canCommandSenderUseCommand(ICommandSender var1) {
      return MinecraftServer.getServer().getConfigurationManager().getBannedPlayers().isListActive() && super.canCommandSenderUseCommand(var1);
   }

   @Override
   public void processCommand(ICommandSender var1, String[] var2) {
      if (var2.length == 1 && var2[0].length() > 0) {
         MinecraftServer.getServer().getConfigurationManager().getBannedPlayers().remove(var2[0]);
         a(var1, "commands.unban.success", new Object[]{var2[0]});
      } else {
         throw new WrongUsageException("commands.unban.usage");
      }
   }

   @Override
   public List addTabCompletionOptions(ICommandSender var1, String[] var2) {
      return var2.length == 1 ? a(var2, MinecraftServer.getServer().getConfigurationManager().getBannedPlayers().getBannedList().keySet()) : null;
   }
}
