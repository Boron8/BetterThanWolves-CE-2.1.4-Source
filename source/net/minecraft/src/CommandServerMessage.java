package net.minecraft.src;

import java.util.Arrays;
import java.util.List;
import net.minecraft.server.MinecraftServer;

public class CommandServerMessage extends CommandBase {
   @Override
   public List getCommandAliases() {
      return Arrays.asList("w", "msg");
   }

   @Override
   public String getCommandName() {
      return "tell";
   }

   @Override
   public int getRequiredPermissionLevel() {
      return 0;
   }

   @Override
   public void processCommand(ICommandSender var1, String[] var2) {
      if (var2.length < 2) {
         throw new WrongUsageException("commands.message.usage");
      } else {
         EntityPlayerMP var3 = c(var1, var2[0]);
         if (var3 == null) {
            throw new PlayerNotFoundException();
         } else if (var3 == var1) {
            throw new PlayerNotFoundException("commands.message.sameTarget");
         } else {
            String var4 = a(var1, var2, 1, !(var1 instanceof EntityPlayer));
            var3.a(
               EnumChatFormatting.GRAY
                  + ""
                  + EnumChatFormatting.ITALIC
                  + var3.translateString("commands.message.display.incoming", var1.getCommandSenderName(), var4)
            );
            var1.sendChatToPlayer(
               EnumChatFormatting.GRAY
                  + ""
                  + EnumChatFormatting.ITALIC
                  + var1.translateString("commands.message.display.outgoing", var3.getCommandSenderName(), var4)
            );
         }
      }
   }

   @Override
   public List addTabCompletionOptions(ICommandSender var1, String[] var2) {
      return a(var2, MinecraftServer.getServer().getAllUsernames());
   }

   @Override
   public boolean isUsernameIndex(String[] var1, int var2) {
      return var2 == 0;
   }
}
