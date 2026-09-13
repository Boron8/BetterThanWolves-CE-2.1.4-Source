package net.minecraft.src;

public class RConConsoleSource implements ICommandSender {
   public static final RConConsoleSource consoleBuffer = new RConConsoleSource();
   private StringBuffer buffer = new StringBuffer();

   public void resetLog() {
      this.buffer.setLength(0);
   }

   public String getChatBuffer() {
      return this.buffer.toString();
   }

   @Override
   public String getCommandSenderName() {
      return "Rcon";
   }

   @Override
   public void sendChatToPlayer(String var1) {
      this.buffer.append(var1);
   }

   @Override
   public boolean canCommandSenderUseCommand(int var1, String var2) {
      return true;
   }

   @Override
   public String translateString(String var1, Object... var2) {
      return StringTranslate.getInstance().translateKeyFormat(var1, var2);
   }

   @Override
   public ChunkCoordinates getPlayerCoordinates() {
      return new ChunkCoordinates(0, 0, 0);
   }
}
