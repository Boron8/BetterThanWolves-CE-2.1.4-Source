package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public class Packet6SpawnPosition extends Packet {
   public int xPosition;
   public int yPosition;
   public int zPosition;

   public Packet6SpawnPosition() {
   }

   public Packet6SpawnPosition(int var1, int var2, int var3) {
      this.xPosition = var1;
      this.yPosition = var2;
      this.zPosition = var3;
   }

   @Override
   public void readPacketData(DataInputStream var1) {
      this.xPosition = var1.readInt();
      this.yPosition = var1.readInt();
      this.zPosition = var1.readInt();
   }

   @Override
   public void writePacketData(DataOutputStream var1) {
      var1.writeInt(this.xPosition);
      var1.writeInt(this.yPosition);
      var1.writeInt(this.zPosition);
   }

   @Override
   public void processPacket(NetHandler var1) {
      var1.handleSpawnPosition(this);
   }

   @Override
   public int getPacketSize() {
      return 12;
   }

   @Override
   public boolean isRealPacket() {
      return true;
   }

   @Override
   public boolean containsSameEntityIDAs(Packet var1) {
      return true;
   }

   @Override
   public boolean canProcessAsync() {
      return false;
   }
}
