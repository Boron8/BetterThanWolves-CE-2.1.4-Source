package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public class Packet61DoorChange extends Packet {
   public int sfxID;
   public int auxData;
   public int posX;
   public int posY;
   public int posZ;
   private boolean disableRelativeVolume;

   public Packet61DoorChange() {
   }

   public Packet61DoorChange(int var1, int var2, int var3, int var4, int var5, boolean var6) {
      this.sfxID = var1;
      this.posX = var2;
      this.posY = var3;
      this.posZ = var4;
      this.auxData = var5;
      this.disableRelativeVolume = var6;
   }

   @Override
   public void readPacketData(DataInputStream var1) {
      this.sfxID = var1.readInt();
      this.posX = var1.readInt();
      this.posY = var1.readByte() & 255;
      this.posZ = var1.readInt();
      this.auxData = var1.readInt();
      this.disableRelativeVolume = var1.readBoolean();
   }

   @Override
   public void writePacketData(DataOutputStream var1) {
      var1.writeInt(this.sfxID);
      var1.writeInt(this.posX);
      var1.writeByte(this.posY & 0xFF);
      var1.writeInt(this.posZ);
      var1.writeInt(this.auxData);
      var1.writeBoolean(this.disableRelativeVolume);
   }

   @Override
   public void processPacket(NetHandler var1) {
      var1.handleDoorChange(this);
   }

   @Override
   public int getPacketSize() {
      return 21;
   }

   public boolean getRelativeVolumeDisabled() {
      return this.disableRelativeVolume;
   }
}
