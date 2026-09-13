package net.minecraft.src;

import java.util.List;
import net.minecraft.server.MinecraftServer;

public class CommandServerSay extends CommandBase {
   @Override
   public String getCommandName() {
      return "say";
   }

   @Override
   public int getRequiredPermissionLevel() {
      return 1;
   }

   @Override
   public String getCommandUsage(ICommandSender var1) {
      return var1.translateString("commands.say.usage");
   }

   @Override
   public void processCommand(ICommandSender var1, String[] var2) {
      if (var2.length > 0 && var2[0].length() > 0) {
         String var3 = a(var1, var2, 0, true);
         MinecraftServer.getServer().getConfigurationManager().sendChatMsg(String.format("[%s] %s", var1.getCommandSenderName(), var3));
      } else {
         throw new WrongUsageException("commands.say.usage");
      }
   }

   @Override
   public List addTabCompletionOptions(ICommandSender var1, String[] var2) {
      return var2.length >= 1 ? a(var2, MinecraftServer.getServer().getAllUsernames()) : null;
   }
}
