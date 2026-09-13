package net.minecraft.src;

import java.util.List;
import net.minecraft.server.MinecraftServer;

public class CommandServerKick extends CommandBase {
   @Override
   public String getCommandName() {
      return "kick";
   }

   @Override
   public int getRequiredPermissionLevel() {
      return 3;
   }

   @Override
   public String getCommandUsage(ICommandSender var1) {
      return var1.translateString("commands.kick.usage");
   }

   @Override
   public void processCommand(ICommandSender var1, String[] var2) {
      if (var2.length > 0 && var2[0].length() > 1) {
         EntityPlayerMP var3 = MinecraftServer.getServer().getConfigurationManager().getPlayerForUsername(var2[0]);
         String var4 = "Kicked by an operator.";
         boolean var5 = false;
         if (var3 == null) {
            throw new PlayerNotFoundException();
         } else {
            if (var2.length >= 2) {
               var4 = a(var1, var2, 1);
               var5 = true;
            }

            var3.playerNetServerHandler.kickPlayerFromServer(var4);
            if (var5) {
               a(var1, "commands.kick.success.reason", new Object[]{var3.am(), var4});
            } else {
               a(var1, "commands.kick.success", new Object[]{var3.am()});
            }
         }
      } else {
         throw new WrongUsageException("commands.kick.usage");
      }
   }

   @Override
   public List addTabCompletionOptions(ICommandSender var1, String[] var2) {
      return var2.length >= 1 ? a(var2, MinecraftServer.getServer().getAllUsernames()) : null;
   }
}
