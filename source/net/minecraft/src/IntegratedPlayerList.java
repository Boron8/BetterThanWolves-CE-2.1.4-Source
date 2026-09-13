package net.minecraft.src;

import java.net.SocketAddress;

public class IntegratedPlayerList extends ServerConfigurationManager {
   private NBTTagCompound hostPlayerData = null;

   public IntegratedPlayerList(IntegratedServer var1) {
      super(var1);
      this.viewDistance = 10;
   }

   @Override
   protected void writePlayerData(EntityPlayerMP var1) {
      if (var1.c_().equals(this.getIntegratedServer().H())) {
         this.hostPlayerData = new NBTTagCompound();
         var1.e(this.hostPlayerData);
      }

      super.writePlayerData(var1);
   }

   @Override
   public String allowUserToConnect(SocketAddress var1, String var2) {
      return var2.equalsIgnoreCase(this.getIntegratedServer().H()) ? "That name is already taken." : super.allowUserToConnect(var1, var2);
   }

   public IntegratedServer getIntegratedServer() {
      return (IntegratedServer)super.getServerInstance();
   }

   @Override
   public NBTTagCompound getHostPlayerData() {
      return this.hostPlayerData;
   }
}
