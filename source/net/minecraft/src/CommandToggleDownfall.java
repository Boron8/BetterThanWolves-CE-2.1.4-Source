package net.minecraft.src;

import net.minecraft.server.MinecraftServer;

public class CommandToggleDownfall extends CommandBase {
   @Override
   public String getCommandName() {
      return "toggledownfall";
   }

   @Override
   public int getRequiredPermissionLevel() {
      return 2;
   }

   @Override
   public void processCommand(ICommandSender var1, String[] var2) {
      this.toggleDownfall();
      a(var1, "commands.downfall.success", new Object[0]);
   }

   protected void toggleDownfall() {
      MinecraftServer.getServer().worldServers[0].A();
      MinecraftServer.getServer().worldServers[0].M().setThundering(true);
   }
}
