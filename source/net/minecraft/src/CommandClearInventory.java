package net.minecraft.src;

import java.util.List;
import net.minecraft.server.MinecraftServer;

public class CommandClearInventory extends CommandBase {
   @Override
   public String getCommandName() {
      return "clear";
   }

   @Override
   public String getCommandUsage(ICommandSender var1) {
      return var1.translateString("commands.clear.usage");
   }

   @Override
   public int getRequiredPermissionLevel() {
      return 2;
   }

   @Override
   public void processCommand(ICommandSender var1, String[] var2) {
      EntityPlayerMP var3 = var2.length == 0 ? c(var1) : c(var1, var2[0]);
      int var4 = var2.length >= 2 ? a(var1, var2[1], 1) : -1;
      int var5 = var2.length >= 3 ? a(var1, var2[2], 0) : -1;
      int var6 = var3.inventory.clearInventory(var4, var5);
      var3.inventoryContainer.detectAndSendChanges();
      if (var6 == 0) {
         throw new CommandException("commands.clear.failure", var3.am());
      } else {
         a(var1, "commands.clear.success", new Object[]{var3.am(), var6});
      }
   }

   @Override
   public List addTabCompletionOptions(ICommandSender var1, String[] var2) {
      return var2.length == 1 ? a(var2, this.getAllOnlineUsernames()) : null;
   }

   protected String[] getAllOnlineUsernames() {
      return MinecraftServer.getServer().getAllUsernames();
   }

   @Override
   public boolean isUsernameIndex(String[] var1, int var2) {
      return var2 == 0;
   }
}
