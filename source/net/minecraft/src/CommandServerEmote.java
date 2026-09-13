package net.minecraft.src;

import java.util.List;
import net.minecraft.server.MinecraftServer;

public class CommandServerEmote extends CommandBase {
   @Override
   public String getCommandName() {
      return "me";
   }

   @Override
   public int getRequiredPermissionLevel() {
      return 0;
   }

   @Override
   public String getCommandUsage(ICommandSender var1) {
      return var1.translateString("commands.me.usage");
   }

   @Override
   public void processCommand(ICommandSender var1, String[] var2) {
      if (var2.length > 0) {
         String var3 = a(var1, var2, 0, var1.canCommandSenderUseCommand(1, "me"));
         MinecraftServer.getServer().getConfigurationManager().sendPacketToAllPlayers(new Packet3Chat("* " + var1.getCommandSenderName() + " " + var3));
      } else {
         throw new WrongUsageException("commands.me.usage");
      }
   }

   @Override
   public List addTabCompletionOptions(ICommandSender var1, String[] var2) {
      return a(var2, MinecraftServer.getServer().getAllUsernames());
   }
}
