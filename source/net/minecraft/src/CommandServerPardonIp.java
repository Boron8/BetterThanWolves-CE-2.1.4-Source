package net.minecraft.src;

import java.util.List;
import java.util.regex.Matcher;
import net.minecraft.server.MinecraftServer;

public class CommandServerPardonIp extends CommandBase {
   @Override
   public String getCommandName() {
      return "pardon-ip";
   }

   @Override
   public int getRequiredPermissionLevel() {
      return 3;
   }

   @Override
   public boolean canCommandSenderUseCommand(ICommandSender var1) {
      return MinecraftServer.getServer().getConfigurationManager().getBannedIPs().isListActive() && super.canCommandSenderUseCommand(var1);
   }

   @Override
   public String getCommandUsage(ICommandSender var1) {
      return var1.translateString("commands.unbanip.usage");
   }

   @Override
   public void processCommand(ICommandSender var1, String[] var2) {
      if (var2.length == 1 && var2[0].length() > 1) {
         Matcher var3 = CommandServerBanIp.IPv4Pattern.matcher(var2[0]);
         if (var3.matches()) {
            MinecraftServer.getServer().getConfigurationManager().getBannedIPs().remove(var2[0]);
            a(var1, "commands.unbanip.success", new Object[]{var2[0]});
         } else {
            throw new SyntaxErrorException("commands.unbanip.invalid");
         }
      } else {
         throw new WrongUsageException("commands.unbanip.usage");
      }
   }

   @Override
   public List addTabCompletionOptions(ICommandSender var1, String[] var2) {
      return var2.length == 1 ? a(var2, MinecraftServer.getServer().getConfigurationManager().getBannedIPs().getBannedList().keySet()) : null;
   }
}
