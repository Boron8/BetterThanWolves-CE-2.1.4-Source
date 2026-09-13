package net.minecraft.src;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.server.MinecraftServer;

public class CommandServerWhitelist extends CommandBase {
   @Override
   public String getCommandName() {
      return "whitelist";
   }

   @Override
   public int getRequiredPermissionLevel() {
      return 3;
   }

   @Override
   public String getCommandUsage(ICommandSender var1) {
      return var1.translateString("commands.whitelist.usage");
   }

   @Override
   public void processCommand(ICommandSender var1, String[] var2) {
      if (var2.length >= 1) {
         if (var2[0].equals("on")) {
            MinecraftServer.getServer().getConfigurationManager().setWhiteListEnabled(true);
            a(var1, "commands.whitelist.enabled", new Object[0]);
            return;
         }

         if (var2[0].equals("off")) {
            MinecraftServer.getServer().getConfigurationManager().setWhiteListEnabled(false);
            a(var1, "commands.whitelist.disabled", new Object[0]);
            return;
         }

         if (var2[0].equals("list")) {
            var1.sendChatToPlayer(
               var1.translateString(
                  "commands.whitelist.list",
                  MinecraftServer.getServer().getConfigurationManager().getWhiteListedPlayers().size(),
                  MinecraftServer.getServer().getConfigurationManager().getAvailablePlayerDat().length
               )
            );
            var1.sendChatToPlayer(a(MinecraftServer.getServer().getConfigurationManager().getWhiteListedPlayers().toArray(new String[0])));
            return;
         }

         if (var2[0].equals("add")) {
            if (var2.length < 2) {
               throw new WrongUsageException("commands.whitelist.add.usage");
            }

            MinecraftServer.getServer().getConfigurationManager().addToWhiteList(var2[1]);
            a(var1, "commands.whitelist.add.success", new Object[]{var2[1]});
            return;
         }

         if (var2[0].equals("remove")) {
            if (var2.length < 2) {
               throw new WrongUsageException("commands.whitelist.remove.usage");
            }

            MinecraftServer.getServer().getConfigurationManager().removeFromWhitelist(var2[1]);
            a(var1, "commands.whitelist.remove.success", new Object[]{var2[1]});
            return;
         }

         if (var2[0].equals("reload")) {
            MinecraftServer.getServer().getConfigurationManager().loadWhiteList();
            a(var1, "commands.whitelist.reloaded", new Object[0]);
            return;
         }
      }

      throw new WrongUsageException("commands.whitelist.usage");
   }

   @Override
   public List addTabCompletionOptions(ICommandSender var1, String[] var2) {
      if (var2.length == 1) {
         return a(var2, new String[]{"on", "off", "list", "add", "remove", "reload"});
      } else {
         if (var2.length == 2) {
            if (var2[0].equals("add")) {
               String[] var3 = MinecraftServer.getServer().getConfigurationManager().getAvailablePlayerDat();
               ArrayList var4 = new ArrayList();
               String var5 = var2[var2.length - 1];

               for (String var9 : var3) {
                  if (a(var5, var9) && !MinecraftServer.getServer().getConfigurationManager().getWhiteListedPlayers().contains(var9)) {
                     var4.add(var9);
                  }
               }

               return var4;
            }

            if (var2[0].equals("remove")) {
               return a(var2, MinecraftServer.getServer().getConfigurationManager().getWhiteListedPlayers());
            }
         }

         return null;
      }
   }
}
