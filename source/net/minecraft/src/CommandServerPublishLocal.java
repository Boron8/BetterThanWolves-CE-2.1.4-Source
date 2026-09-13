package net.minecraft.src;

import net.minecraft.server.MinecraftServer;

public class CommandServerPublishLocal extends CommandBase {
   @Override
   public String getCommandName() {
      return "publish";
   }

   @Override
   public int getRequiredPermissionLevel() {
      return 4;
   }

   @Override
   public void processCommand(ICommandSender var1, String[] var2) {
      String var3 = MinecraftServer.getServer().shareToLAN(EnumGameType.SURVIVAL, false);
      if (var3 != null) {
         a(var1, "commands.publish.started", new Object[]{var3});
      } else {
         a(var1, "commands.publish.failed", new Object[0]);
      }
   }
}
