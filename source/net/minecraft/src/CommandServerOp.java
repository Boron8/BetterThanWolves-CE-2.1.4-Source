package net.minecraft.src;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.server.MinecraftServer;

public class CommandServerOp extends CommandBase {
   @Override
   public String getCommandName() {
      return "op";
   }

   @Override
   public int getRequiredPermissionLevel() {
      return 3;
   }

   @Override
   public String getCommandUsage(ICommandSender var1) {
      return var1.translateString("commands.op.usage");
   }

   @Override
   public void processCommand(ICommandSender var1, String[] var2) {
      if (var2.length == 1 && var2[0].length() > 0) {
         MinecraftServer.getServer().getConfigurationManager().addOp(var2[0]);
         a(var1, "commands.op.success", new Object[]{var2[0]});
      } else {
         throw new WrongUsageException("commands.op.usage");
      }
   }

   @Override
   public List addTabCompletionOptions(ICommandSender var1, String[] var2) {
      if (var2.length == 1) {
         String var3 = var2[var2.length - 1];
         ArrayList var4 = new ArrayList();

         for (String var8 : MinecraftServer.getServer().getAllUsernames()) {
            if (!MinecraftServer.getServer().getConfigurationManager().areCommandsAllowed(var8) && a(var3, var8)) {
               var4.add(var8);
            }
         }

         return var4;
      } else {
         return null;
      }
   }
}
