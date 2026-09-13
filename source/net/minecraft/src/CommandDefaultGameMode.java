package net.minecraft.src;

import net.minecraft.server.MinecraftServer;

public class CommandDefaultGameMode extends CommandGameMode {
   @Override
   public String getCommandName() {
      return "defaultgamemode";
   }

   @Override
   public String getCommandUsage(ICommandSender var1) {
      return var1.translateString("commands.defaultgamemode.usage");
   }

   @Override
   public void processCommand(ICommandSender var1, String[] var2) {
      if (var2.length > 0) {
         EnumGameType var3 = this.e(var1, var2[0]);
         this.setGameType(var3);
         String var4 = StatCollector.translateToLocal("gameMode." + var3.getName());
         a(var1, "commands.defaultgamemode.success", new Object[]{var4});
      } else {
         throw new WrongUsageException("commands.defaultgamemode.usage");
      }
   }

   protected void setGameType(EnumGameType var1) {
      MinecraftServer.getServer().setGameType(var1);
   }
}
