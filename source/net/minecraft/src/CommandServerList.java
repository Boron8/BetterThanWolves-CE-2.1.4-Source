package net.minecraft.src;

import net.minecraft.server.MinecraftServer;

public class CommandServerList extends CommandBase {
   @Override
   public String getCommandName() {
      return "list";
   }

   @Override
   public int getRequiredPermissionLevel() {
      return 0;
   }

   @Override
   public void processCommand(ICommandSender var1, String[] var2) {
      var1.sendChatToPlayer(
         var1.translateString("commands.players.list", MinecraftServer.getServer().getCurrentPlayerCount(), MinecraftServer.getServer().getMaxPlayers())
      );
      var1.sendChatToPlayer(MinecraftServer.getServer().getConfigurationManager().getPlayerListAsString());
   }
}
