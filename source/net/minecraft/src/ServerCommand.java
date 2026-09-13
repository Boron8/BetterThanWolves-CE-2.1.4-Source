package net.minecraft.src;

public class ServerCommand {
   public final String command;
   public final ICommandSender sender;

   public ServerCommand(String var1, ICommandSender var2) {
      this.command = var1;
      this.sender = var2;
   }
}
