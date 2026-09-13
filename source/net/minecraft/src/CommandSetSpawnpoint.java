package net.minecraft.src;

import java.util.List;
import net.minecraft.server.MinecraftServer;

public class CommandSetSpawnpoint extends CommandBase {
   @Override
   public String getCommandName() {
      return "spawnpoint";
   }

   @Override
   public int getRequiredPermissionLevel() {
      return 2;
   }

   @Override
   public String getCommandUsage(ICommandSender var1) {
      return var1.translateString("commands.spawnpoint.usage");
   }

   @Override
   public void processCommand(ICommandSender var1, String[] var2) {
      EntityPlayerMP var3 = var2.length == 0 ? c(var1) : c(var1, var2[0]);
      if (var2.length == 4) {
         if (var3.worldObj != null) {
            int var4 = 1;
            int var5 = 30000000;
            int var6 = a(var1, var2[var4++], -var5, var5);
            int var7 = a(var1, var2[var4++], 0, 256);
            int var8 = a(var1, var2[var4++], -var5, var5);
            var3.a(new ChunkCoordinates(var6, var7, var8), true);
            a(var1, "commands.spawnpoint.success", new Object[]{var3.am(), var6, var7, var8});
         }
      } else {
         if (var2.length > 1) {
            throw new WrongUsageException("commands.spawnpoint.usage");
         }

         ChunkCoordinates var12 = var3.getPlayerCoordinates();
         var3.a(var12, true);
         a(var1, "commands.spawnpoint.success", new Object[]{var3.am(), var12.posX, var12.posY, var12.posZ});
      }
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
