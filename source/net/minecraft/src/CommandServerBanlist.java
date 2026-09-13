package net.minecraft.src;

import java.util.List;
import net.minecraft.server.MinecraftServer;

public class CommandServerBanlist extends CommandBase {
   @Override
   public String getCommandName() {
      return "banlist";
   }

   @Override
   public int getRequiredPermissionLevel() {
      return 3;
   }

   @Override
   public boolean canCommandSenderUseCommand(ICommandSender var1) {
      return (
            MinecraftServer.getServer().getConfigurationManager().getBannedIPs().isListActive()
               || MinecraftServer.getServer().getConfigurationManager().getBannedPlayers().isListActive()
         )
         && super.canCommandSenderUseCommand(var1);
   }

   @Override
   public String getCommandUsage(ICommandSender var1) {
      return var1.translateString("commands.banlist.usage");
   }

   @Override
   public void processCommand(ICommandSender var1, String[] var2) {
      if (var2.length >= 1 && var2[0].equalsIgnoreCase("ips")) {
         var1.sendChatToPlayer(
            var1.translateString("commands.banlist.ips", MinecraftServer.getServer().getConfigurationManager().getBannedIPs().getBannedList().size())
         );
         var1.sendChatToPlayer(a(MinecraftServer.getServer().getConfigurationManager().getBannedIPs().getBannedList().keySet().toArray()));
      } else {
         var1.sendChatToPlayer(
            var1.translateString("commands.banlist.players", MinecraftServer.getServer().getConfigurationManager().getBannedPlayers().getBannedList().size())
         );
         var1.sendChatToPlayer(a(MinecraftServer.getServer().getConfigurationManager().getBannedPlayers().getBannedList().keySet().toArray()));
      }
   }

   @Override
   public List addTabCompletionOptions(ICommandSender var1, String[] var2) {
      return var2.length == 1 ? a(var2, new String[]{"players", "ips"}) : null;
   }
}
