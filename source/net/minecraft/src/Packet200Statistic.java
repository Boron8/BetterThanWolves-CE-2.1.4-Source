package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public class Packet200Statistic extends Packet {
   public int statisticId;
   public int amount;

   public Packet200Statistic() {
   }

   public Packet200Statistic(int var1, int var2) {
      this.statisticId = var1;
      this.amount = var2;
   }

   @Override
   public void processPacket(NetHandler var1) {
      var1.handleStatistic(this);
   }

   @Override
   public void readPacketData(DataInputStream var1) {
      this.statisticId = var1.readInt();
      this.amount = var1.readByte();
   }

   @Override
   public void writePacketData(DataOutputStream var1) {
      var1.writeInt(this.statisticId);
      var1.writeByte(this.amount);
   }

   @Override
   public int getPacketSize() {
      return 6;
   }

   @Override
   public boolean canProcessAsync() {
      return true;
   }
}
