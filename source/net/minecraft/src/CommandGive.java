package net.minecraft.src;

import java.util.List;
import net.minecraft.server.MinecraftServer;

public class CommandGive extends CommandBase {
   @Override
   public String getCommandName() {
      return "give";
   }

   @Override
   public int getRequiredPermissionLevel() {
      return 2;
   }

   @Override
   public String getCommandUsage(ICommandSender par1ICommandSender) {
      return par1ICommandSender.translateString("commands.give.usage");
   }

   @Override
   public void processCommand(ICommandSender par1ICommandSender, String[] par2ArrayOfStr) {
      if (par2ArrayOfStr.length >= 2) {
         EntityPlayerMP var3 = c(par1ICommandSender, par2ArrayOfStr[0]);
         int var4 = a(par1ICommandSender, par2ArrayOfStr[1], 1);
         int var5 = 1;
         int var6 = 0;
         if (Item.itemsList[var4] == null) {
            throw new NumberInvalidException("commands.give.notFound", var4);
         } else {
            if (par2ArrayOfStr.length >= 3) {
               var5 = a(par1ICommandSender, par2ArrayOfStr[2], 1, 64);
            }

            if (par2ArrayOfStr.length >= 4) {
               var6 = a(par1ICommandSender, par2ArrayOfStr[3]);
            }

            ItemStack var7 = new ItemStack(var4, var5, var6);
            var7.getItem().initializeStackOnGiveCommand(var3.worldObj.rand, var7);
            EntityItem var8 = var3.c(var7);
            var8.delayBeforeCanPickup = 0;
            a(par1ICommandSender, "commands.give.success", new Object[]{Item.itemsList[var4].func_77653_i(var7), var4, var5, var3.am()});
         }
      } else {
         throw new WrongUsageException("commands.give.usage");
      }
   }

   @Override
   public List addTabCompletionOptions(ICommandSender par1ICommandSender, String[] par2ArrayOfStr) {
      return par2ArrayOfStr.length == 1 ? a(par2ArrayOfStr, this.getPlayers()) : null;
   }

   protected String[] getPlayers() {
      return MinecraftServer.getServer().getAllUsernames();
   }

   @Override
   public boolean isUsernameIndex(String[] par1ArrayOfStr, int par2) {
      return par2 == 0;
   }
}
