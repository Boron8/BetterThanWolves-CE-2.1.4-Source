package net.minecraft.src;

import net.minecraft.server.MinecraftServer;

public class TileEntityCommandBlock extends TileEntity implements ICommandSender {
   private int succesCount = 0;
   private String command = "";
   private String commandSenderName = "@";

   public void setCommand(String var1) {
      this.command = var1;
      this.k_();
   }

   public String getCommand() {
      return this.command;
   }

   public int executeCommandOnPowered(World var1) {
      if (var1.isRemote) {
         return 0;
      } else {
         MinecraftServer var2 = MinecraftServer.getServer();
         if (var2 != null && var2.isCommandBlockEnabled()) {
            ICommandManager var3 = var2.getCommandManager();
            return var3.executeCommand(this, this.command);
         } else {
            return 0;
         }
      }
   }

   @Override
   public String getCommandSenderName() {
      return this.commandSenderName;
   }

   public void setCommandSenderName(String var1) {
      this.commandSenderName = var1;
   }

   @Override
   public void sendChatToPlayer(String var1) {
   }

   @Override
   public boolean canCommandSenderUseCommand(int var1, String var2) {
      return var1 <= 2;
   }

   @Override
   public String translateString(String var1, Object... var2) {
      return var1;
   }

   @Override
   public void writeToNBT(NBTTagCompound var1) {
      super.writeToNBT(var1);
      var1.setString("Command", this.command);
      var1.setInteger("SuccessCount", this.succesCount);
      var1.setString("CustomName", this.commandSenderName);
   }

   @Override
   public void readFromNBT(NBTTagCompound var1) {
      super.readFromNBT(var1);
      this.command = var1.getString("Command");
      this.succesCount = var1.getInteger("SuccessCount");
      if (var1.hasKey("CustomName")) {
         this.commandSenderName = var1.getString("CustomName");
      }
   }

   @Override
   public ChunkCoordinates getPlayerCoordinates() {
      return new ChunkCoordinates(this.xCoord, this.yCoord, this.zCoord);
   }

   @Override
   public Packet getDescriptionPacket() {
      NBTTagCompound var1 = new NBTTagCompound();
      this.writeToNBT(var1);
      return new Packet132TileEntityData(this.xCoord, this.yCoord, this.zCoord, 2, var1);
   }

   public int func_96103_d() {
      return this.succesCount;
   }

   public void func_96102_a(int var1) {
      this.succesCount = var1;
   }
}
