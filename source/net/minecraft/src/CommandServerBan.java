package net.minecraft.src;

import java.util.List;
import net.minecraft.server.MinecraftServer;

public class CommandServerBan extends CommandBase {
   @Override
   public String getCommandName() {
      return "ban";
   }

   @Override
   public int getRequiredPermissionLevel() {
      return 3;
   }

   @Override
   public String getCommandUsage(ICommandSender var1) {
      return var1.translateString("commands.ban.usage");
   }

   @Override
   public boolean canCommandSenderUseCommand(ICommandSender var1) {
      return MinecraftServer.getServer().getConfigurationManager().getBannedPlayers().isListActive() && super.canCommandSenderUseCommand(var1);
   }

   @Override
   public void processCommand(ICommandSender var1, String[] var2) {
      if (var2.length >= 1 && var2[0].length() > 0) {
         EntityPlayerMP var3 = MinecraftServer.getServer().getConfigurationManager().getPlayerForUsername(var2[0]);
         BanEntry var4 = new BanEntry(var2[0]);
         var4.setBannedBy(var1.getCommandSenderName());
         if (var2.length >= 2) {
            var4.setBanReason(a(var1, var2, 1));
         }

         MinecraftServer.getServer().getConfigurationManager().getBannedPlayers().put(var4);
         if (var3 != null) {
            var3.playerNetServerHandler.kickPlayerFromServer("You are banned from this server.");
         }

         a(var1, "commands.ban.success", new Object[]{var2[0]});
      } else {
         throw new WrongUsageException("commands.ban.usage");
      }
   }

   @Override
   public List addTabCompletionOptions(ICommandSender var1, String[] var2) {
      return var2.length >= 1 ? a(var2, MinecraftServer.getServer().getAllUsernames()) : null;
   }
}
