package net.minecraft.src;

import net.minecraft.server.MinecraftServer;

public class CommandServerSaveOff extends CommandBase {
   @Override
   public String getCommandName() {
      return "save-off";
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
            var5.canNotSave = true;
         }
      }

      a(var1, "commands.save.disabled", new Object[0]);
   }
}
