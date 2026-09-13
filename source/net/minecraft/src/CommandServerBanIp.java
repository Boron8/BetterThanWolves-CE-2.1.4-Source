package net.minecraft.src;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraft.server.MinecraftServer;

public class CommandServerBanIp extends CommandBase {
   public static final Pattern IPv4Pattern = Pattern.compile(
      "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])$"
   );

   @Override
   public String getCommandName() {
      return "ban-ip";
   }

   @Override
   public int getRequiredPermissionLevel() {
      return 3;
   }

   @Override
   public boolean canCommandSenderUseCommand(ICommandSender var1) {
      return MinecraftServer.getServer().getConfigurationManager().getBannedIPs().isListActive() && super.canCommandSenderUseCommand(var1);
   }

   @Override
   public String getCommandUsage(ICommandSender var1) {
      return var1.translateString("commands.banip.usage");
   }

   @Override
   public void processCommand(ICommandSender var1, String[] var2) {
      if (var2.length >= 1 && var2[0].length() > 1) {
         Matcher var3 = IPv4Pattern.matcher(var2[0]);
         String var4 = null;
         if (var2.length >= 2) {
            var4 = a(var1, var2, 1);
         }

         if (var3.matches()) {
            this.banIP(var1, var2[0], var4);
         } else {
            EntityPlayerMP var5 = MinecraftServer.getServer().getConfigurationManager().getPlayerForUsername(var2[0]);
            if (var5 == null) {
               throw new PlayerNotFoundException("commands.banip.invalid");
            }

            this.banIP(var1, var5.getPlayerIP(), var4);
         }
      } else {
         throw new WrongUsageException("commands.banip.usage");
      }
   }

   @Override
   public List addTabCompletionOptions(ICommandSender var1, String[] var2) {
      return var2.length == 1 ? a(var2, MinecraftServer.getServer().getAllUsernames()) : null;
   }

   protected void banIP(ICommandSender var1, String var2, String var3) {
      BanEntry var4 = new BanEntry(var2);
      var4.setBannedBy(var1.getCommandSenderName());
      if (var3 != null) {
         var4.setBanReason(var3);
      }

      MinecraftServer.getServer().getConfigurationManager().getBannedIPs().put(var4);
      List var5 = MinecraftServer.getServer().getConfigurationManager().getPlayerList(var2);
      String[] var6 = new String[var5.size()];
      int var7 = 0;

      for (EntityPlayerMP var9 : var5) {
         var9.playerNetServerHandler.kickPlayerFromServer("You have been IP banned.");
         var6[var7++] = var9.am();
      }

      if (var5.isEmpty()) {
         a(var1, "commands.banip.success", new Object[]{var2});
      } else {
         a(var1, "commands.banip.success.players", new Object[]{var2, a(var6)});
      }
   }
}
