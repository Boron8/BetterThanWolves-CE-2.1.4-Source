package net.minecraft.src;

import net.minecraft.server.MinecraftServer;

public class CommandServerSaveAll extends CommandBase {
   @Override
   public String getCommandName() {
      return "save-all";
   }

   @Override
   public int getRequiredPermissionLevel() {
      return 4;
   }

   @Override
   public void processCommand(ICommandSender var1, String[] var2) {
      MinecraftServer var3 = MinecraftServer.getServer();
      var1.sendChatToPlayer(var1.translateString("commands.save.start"));
      if (var3.getConfigurationManager() != null) {
         var3.getConfigurationManager().saveAllPlayerData();
      }

      try {
         for (int var4 = 0; var4 < var3.worldServers.length; var4++) {
            if (var3.worldServers[var4] != null) {
               WorldServer var5 = var3.worldServers[var4];
               boolean var6 = var5.canNotSave;
               var5.canNotSave = false;
               var5.saveAllChunks(true, null);
               var5.canNotSave = var6;
            }
         }

         if (var2.length > 0 && "flush".equals(var2[0])) {
            var1.sendChatToPlayer(var1.translateString("commands.save.flushStart"));

            for (int var8 = 0; var8 < var3.worldServers.length; var8++) {
               if (var3.worldServers[var8] != null) {
                  WorldServer var9 = var3.worldServers[var8];
                  boolean var10 = var9.canNotSave;
                  var9.canNotSave = false;
                  var9.func_104140_m();
                  var9.canNotSave = var10;
               }
            }

            var1.sendChatToPlayer(var1.translateString("commands.save.flushEnd"));
         }
      } catch (MinecraftException var7) {
         a(var1, "commands.save.failed", new Object[]{var7.getMessage()});
         return;
      }

      a(var1, "commands.save.success", new Object[0]);
   }
}
