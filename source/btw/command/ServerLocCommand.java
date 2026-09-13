package btw.command;

import net.minecraft.src.CommandBase;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ICommandSender;
import net.minecraft.src.MathHelper;

public class ServerLocCommand extends CommandBase {
   @Override
   public String getCommandName() {
      return "loc";
   }

   @Override
   public String getCommandUsage(ICommandSender par1ICommandSender) {
      return "/loc";
   }

   @Override
   public void processCommand(ICommandSender par1ICommandSender, String[] par2ArrayOfStr) {
      if (par1ICommandSender instanceof EntityPlayer) {
         EntityPlayer player = (EntityPlayer)par1ICommandSender;
         par1ICommandSender.sendChatToPlayer(
            "\u00a7e"
               + "Current Location: "
               + MathHelper.floor_double(player.posX)
               + ", "
               + MathHelper.floor_double(player.posY)
               + ", "
               + MathHelper.floor_double(player.posZ)
         );
      }
   }
}
