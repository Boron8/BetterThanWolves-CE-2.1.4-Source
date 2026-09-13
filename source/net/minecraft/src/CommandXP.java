package net.minecraft.src;

import java.util.List;
import net.minecraft.server.MinecraftServer;

public class CommandXP extends CommandBase {
   @Override
   public String getCommandName() {
      return "xp";
   }

   @Override
   public int getRequiredPermissionLevel() {
      return 2;
   }

   @Override
   public String getCommandUsage(ICommandSender var1) {
      return var1.translateString("commands.xp.usage");
   }

   @Override
   public void processCommand(ICommandSender var1, String[] var2) {
      if (var2.length <= 0) {
         throw new WrongUsageException("commands.xp.usage");
      } else {
         String var4 = var2[0];
         boolean var5 = var4.endsWith("l") || var4.endsWith("L");
         if (var5 && var4.length() > 1) {
            var4 = var4.substring(0, var4.length() - 1);
         }

         int var6 = a(var1, var4);
         boolean var7 = var6 < 0;
         if (var7) {
            var6 *= -1;
         }

         EntityPlayerMP var3;
         if (var2.length > 1) {
            var3 = c(var1, var2[1]);
         } else {
            var3 = c(var1);
         }

         if (var5) {
            if (var7) {
               var3.addExperienceLevel(-var6);
               a(var1, "commands.xp.success.negative.levels", new Object[]{var6, var3.getEntityName()});
            } else {
               var3.addExperienceLevel(var6);
               a(var1, "commands.xp.success.levels", new Object[]{var6, var3.getEntityName()});
            }
         } else {
            if (var7) {
               throw new WrongUsageException("commands.xp.failure.widthdrawXp");
            }

            var3.addExperience(var6);
            a(var1, "commands.xp.success", new Object[]{var6, var3.getEntityName()});
         }
      }
   }

   @Override
   public List addTabCompletionOptions(ICommandSender var1, String[] var2) {
      return var2.length == 2 ? a(var2, this.getAllUsernames()) : null;
   }

   protected String[] getAllUsernames() {
      return MinecraftServer.getServer().getAllUsernames();
   }

   @Override
   public boolean isUsernameIndex(String[] var1, int var2) {
      return var2 == 1;
   }
}
