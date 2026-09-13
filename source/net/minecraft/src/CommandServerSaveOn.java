package net.minecraft.src;

import net.minecraft.server.MinecraftServer;

public class CommandServerSaveOn extends CommandBase {
   @Override
   public String getCommandName() {
      return "save-on";
   }

   @Override
   public int getRequiredPermissionLevel() {
      return 4;
   }

   @Override
   public void processCommand(ICommandSender var1, String[] var2) {
      MinecraftServer var3 = MinecraftServer.getServer();

      for (int var4 = 0; var4 < var3.worldServers.length; var4++) {
         if (var3.worldServers[var4] != null) {
            WorldServer var5 = var3.worldServers[var4];
            var5.canNotSave = false;
         }
      }

      a(var1, "commands.save.enabled", new Object[0]);
   }
}
