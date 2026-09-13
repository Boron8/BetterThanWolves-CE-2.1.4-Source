package net.minecraft.src;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import net.minecraft.server.MinecraftServer;

public class CommandHelp extends CommandBase {
   @Override
   public String getCommandName() {
      return "help";
   }

   @Override
   public int getRequiredPermissionLevel() {
      return 0;
   }

   @Override
   public String getCommandUsage(ICommandSender var1) {
      return var1.translateString("commands.help.usage");
   }

   @Override
   public List getCommandAliases() {
      return Arrays.asList("?");
   }

   @Override
   public void processCommand(ICommandSender var1, String[] var2) {
      List var3 = this.getSortedPossibleCommands(var1);
      byte var4 = 7;
      int var5 = (var3.size() - 1) / var4;
      int var6 = 0;

      try {
         var6 = var2.length == 0 ? 0 : a(var1, var2[0], 1, var5 + 1) - 1;
      } catch (NumberInvalidException var10) {
         Map var8 = this.getCommands();
         ICommand var9 = (ICommand)var8.get(var2[0]);
         if (var9 != null) {
            throw new WrongUsageException(var9.getCommandUsage(var1));
         }

         throw new CommandNotFoundException();
      }

      int var7 = Math.min((var6 + 1) * var4, var3.size());
      var1.sendChatToPlayer(EnumChatFormatting.DARK_GREEN + var1.translateString("commands.help.header", var6 + 1, var5 + 1));

      for (int var12 = var6 * var4; var12 < var7; var12++) {
         ICommand var13 = (ICommand)var3.get(var12);
         var1.sendChatToPlayer(var13.getCommandUsage(var1));
      }

      if (var6 == 0 && var1 instanceof EntityPlayer) {
         var1.sendChatToPlayer(EnumChatFormatting.GREEN + var1.translateString("commands.help.footer"));
      }
   }

   protected List getSortedPossibleCommands(ICommandSender var1) {
      List var2 = MinecraftServer.getServer().getCommandManager().getPossibleCommands(var1);
      Collections.sort(var2);
      return var2;
   }

   protected Map getCommands() {
      return MinecraftServer.getServer().getCommandManager().getCommands();
   }
}
