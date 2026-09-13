package net.minecraft.src;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import net.minecraft.server.MinecraftServer;

public abstract class CommandBase implements ICommand {
   private static IAdminCommand theAdmin = null;

   public int getRequiredPermissionLevel() {
      return 4;
   }

   @Override
   public String getCommandUsage(ICommandSender var1) {
      return "/" + this.c();
   }

   @Override
   public List getCommandAliases() {
      return null;
   }

   @Override
   public boolean canCommandSenderUseCommand(ICommandSender var1) {
      return var1.canCommandSenderUseCommand(this.getRequiredPermissionLevel(), this.c());
   }

   @Override
   public List addTabCompletionOptions(ICommandSender var1, String[] var2) {
      return null;
   }

   public static int parseInt(ICommandSender var0, String var1) {
      try {
         return Integer.parseInt(var1);
      } catch (NumberFormatException var3) {
         throw new NumberInvalidException("commands.generic.num.invalid", var1);
      }
   }

   public static int parseIntWithMin(ICommandSender var0, String var1, int var2) {
      return parseIntBounded(var0, var1, var2, Integer.MAX_VALUE);
   }

   public static int parseIntBounded(ICommandSender var0, String var1, int var2, int var3) {
      int var4 = parseInt(var0, var1);
      if (var4 < var2) {
         throw new NumberInvalidException("commands.generic.num.tooSmall", var4, var2);
      } else if (var4 > var3) {
         throw new NumberInvalidException("commands.generic.num.tooBig", var4, var3);
      } else {
         return var4;
      }
   }

   public static double parseDouble(ICommandSender var0, String var1) {
      try {
         return Double.parseDouble(var1);
      } catch (NumberFormatException var3) {
         throw new NumberInvalidException("commands.generic.double.invalid", var1);
      }
   }

   public static EntityPlayerMP getCommandSenderAsPlayer(ICommandSender var0) {
      if (var0 instanceof EntityPlayerMP) {
         return (EntityPlayerMP)var0;
      } else {
         throw new PlayerNotFoundException("You must specify which player you wish to perform this action on.");
      }
   }

   public static EntityPlayerMP func_82359_c(ICommandSender var0, String var1) {
      EntityPlayerMP var2 = PlayerSelector.matchOnePlayer(var0, var1);
      if (var2 != null) {
         return var2;
      } else {
         var2 = MinecraftServer.getServer().getConfigurationManager().getPlayerForUsername(var1);
         if (var2 == null) {
            throw new PlayerNotFoundException();
         } else {
            return var2;
         }
      }
   }

   public static String func_96332_d(ICommandSender var0, String var1) {
      EntityPlayerMP var2 = PlayerSelector.matchOnePlayer(var0, var1);
      if (var2 != null) {
         return var2.am();
      } else if (PlayerSelector.hasArguments(var1)) {
         throw new PlayerNotFoundException();
      } else {
         return var1;
      }
   }

   public static String func_82360_a(ICommandSender var0, String[] var1, int var2) {
      return func_82361_a(var0, var1, var2, false);
   }

   public static String func_82361_a(ICommandSender var0, String[] var1, int var2, boolean var3) {
      StringBuilder var4 = new StringBuilder();

      for (int var5 = var2; var5 < var1.length; var5++) {
         if (var5 > var2) {
            var4.append(" ");
         }

         String var6 = var1[var5];
         if (var3) {
            String var7 = PlayerSelector.matchPlayersAsString(var0, var6);
            if (var7 != null) {
               var6 = var7;
            } else if (PlayerSelector.hasArguments(var6)) {
               throw new PlayerNotFoundException();
            }
         }

         var4.append(var6);
      }

      return var4.toString();
   }

   public static String joinNiceString(Object[] var0) {
      StringBuilder var1 = new StringBuilder();

      for (int var2 = 0; var2 < var0.length; var2++) {
         String var3 = var0[var2].toString();
         if (var2 > 0) {
            if (var2 == var0.length - 1) {
               var1.append(" and ");
            } else {
               var1.append(", ");
            }
         }

         var1.append(var3);
      }

      return var1.toString();
   }

   public static String func_96333_a(Collection var0) {
      return joinNiceString(var0.toArray(new String[0]));
   }

   public static boolean doesStringStartWith(String var0, String var1) {
      return var1.regionMatches(true, 0, var0, 0, var0.length());
   }

   public static List getListOfStringsMatchingLastWord(String[] var0, String... var1) {
      String var2 = var0[var0.length - 1];
      ArrayList var3 = new ArrayList();

      for (String var7 : var1) {
         if (doesStringStartWith(var2, var7)) {
            var3.add(var7);
         }
      }

      return var3;
   }

   public static List getListOfStringsFromIterableMatchingLastWord(String[] var0, Iterable var1) {
      String var2 = var0[var0.length - 1];
      ArrayList var3 = new ArrayList();

      for (String var5 : var1) {
         if (doesStringStartWith(var2, var5)) {
            var3.add(var5);
         }
      }

      return var3;
   }

   @Override
   public boolean isUsernameIndex(String[] var1, int var2) {
      return false;
   }

   public static void notifyAdmins(ICommandSender var0, String var1, Object... var2) {
      notifyAdmins(var0, 0, var1, var2);
   }

   public static void notifyAdmins(ICommandSender var0, int var1, String var2, Object... var3) {
      if (theAdmin != null) {
         theAdmin.notifyAdmins(var0, var1, var2, var3);
      }
   }

   public static void setAdminCommander(IAdminCommand var0) {
      theAdmin = var0;
   }

   public int compareTo(ICommand var1) {
      return this.c().compareTo(var1.getCommandName());
   }
}
