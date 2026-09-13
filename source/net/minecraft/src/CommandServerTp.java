package net.minecraft.src;

import java.util.List;
import net.minecraft.server.MinecraftServer;

public class CommandServerTp extends CommandBase {
   @Override
   public String getCommandName() {
      return "tp";
   }

   @Override
   public int getRequiredPermissionLevel() {
      return 2;
   }

   @Override
   public String getCommandUsage(ICommandSender var1) {
      return var1.translateString("commands.tp.usage");
   }

   @Override
   public void processCommand(ICommandSender var1, String[] var2) {
      if (var2.length < 1) {
         throw new WrongUsageException("commands.tp.usage");
      } else {
         EntityPlayerMP var3;
         if (var2.length != 2 && var2.length != 4) {
            var3 = c(var1);
         } else {
            var3 = c(var1, var2[0]);
            if (var3 == null) {
               throw new PlayerNotFoundException();
            }
         }

         if (var2.length != 3 && var2.length != 4) {
            if (var2.length == 1 || var2.length == 2) {
               EntityPlayerMP var14 = c(var1, var2[var2.length - 1]);
               if (var14 == null) {
                  throw new PlayerNotFoundException();
               }

               if (var14.worldObj != var3.worldObj) {
                  a(var1, "commands.tp.notSameDimension", new Object[0]);
                  return;
               }

               var3.mountEntity(null);
               var3.playerNetServerHandler.setPlayerLocation(var14.posX, var14.posY, var14.posZ, var14.rotationYaw, var14.rotationPitch);
               a(var1, "commands.tp.success", new Object[]{var3.am(), var14.am()});
            }
         } else if (var3.worldObj != null) {
            int var4 = var2.length - 3;
            double var5 = this.func_82368_a(var1, var3.posX, var2[var4++]);
            double var7 = this.func_82367_a(var1, var3.posY, var2[var4++], 0, 0);
            double var9 = this.func_82368_a(var1, var3.posZ, var2[var4++]);
            var3.mountEntity(null);
            var3.setPositionAndUpdate(var5, var7, var9);
            a(var1, "commands.tp.success.coordinates", new Object[]{var3.am(), var5, var7, var9});
         }
      }
   }

   private double func_82368_a(ICommandSender var1, double var2, String var4) {
      return this.func_82367_a(var1, var2, var4, -30000000, 30000000);
   }

   private double func_82367_a(ICommandSender var1, double var2, String var4, int var5, int var6) {
      boolean var7 = var4.startsWith("~");
      double var8 = var7 ? var2 : 0.0;
      if (!var7 || var4.length() > 1) {
         boolean var10 = var4.contains(".");
         if (var7) {
            var4 = var4.substring(1);
         }

         var8 += b(var1, var4);
         if (!var10 && !var7) {
            var8 += 0.5;
         }
      }

      if (var5 != 0 || var6 != 0) {
         if (var8 < var5) {
            throw new NumberInvalidException("commands.generic.double.tooSmall", var8, var5);
         }

         if (var8 > var6) {
            throw new NumberInvalidException("commands.generic.double.tooBig", var8, var6);
         }
      }

      return var8;
   }

   @Override
   public List addTabCompletionOptions(ICommandSender var1, String[] var2) {
      return var2.length != 1 && var2.length != 2 ? null : a(var2, MinecraftServer.getServer().getAllUsernames());
   }

   @Override
   public boolean isUsernameIndex(String[] var1, int var2) {
      return var2 == 0;
   }
}
