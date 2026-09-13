package net.minecraft.src;

import java.util.List;
import net.minecraft.server.MinecraftServer;

public class CommandGameRule extends CommandBase {
   @Override
   public String getCommandName() {
      return "gamerule";
   }

   @Override
   public int getRequiredPermissionLevel() {
      return 2;
   }

   @Override
   public String getCommandUsage(ICommandSender var1) {
      return var1.translateString("commands.gamerule.usage");
   }

   @Override
   public void processCommand(ICommandSender var1, String[] var2) {
      if (var2.length == 2) {
         String var7 = var2[0];
         String var8 = var2[1];
         GameRules var9 = this.getGameRules();
         if (var9.hasRule(var7)) {
            var9.setOrCreateGameRule(var7, var8);
            a(var1, "commands.gamerule.success", new Object[0]);
         } else {
            a(var1, "commands.gamerule.norule", new Object[]{var7});
         }
      } else if (var2.length == 1) {
         String var6 = var2[0];
         GameRules var4 = this.getGameRules();
         if (var4.hasRule(var6)) {
            String var5 = var4.getGameRuleStringValue(var6);
            var1.sendChatToPlayer(var6 + " = " + var5);
         } else {
            a(var1, "commands.gamerule.norule", new Object[]{var6});
         }
      } else if (var2.length == 0) {
         GameRules var3 = this.getGameRules();
         var1.sendChatToPlayer(a(var3.getRules()));
      } else {
         throw new WrongUsageException("commands.gamerule.usage");
      }
   }

   @Override
   public List addTabCompletionOptions(ICommandSender var1, String[] var2) {
      if (var2.length == 1) {
         return a(var2, this.getGameRules().getRules());
      } else {
         return var2.length == 2 ? a(var2, new String[]{"true", "false"}) : null;
      }
   }

   private GameRules getGameRules() {
      return MinecraftServer.getServer().worldServerForDimension(0).N();
   }
}
