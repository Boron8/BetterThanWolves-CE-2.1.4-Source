package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public class Packet206SetObjective extends Packet {
   public String objectiveName;
   public String objectiveDisplayName;
   public int change;

   public Packet206SetObjective() {
   }

   public Packet206SetObjective(ScoreObjective var1, int var2) {
      this.objectiveName = var1.getName();
      this.objectiveDisplayName = var1.getDisplayName();
      this.change = var2;
   }

   @Override
   public void readPacketData(DataInputStream var1) {
      this.objectiveName = a(var1, 16);
      this.objectiveDisplayName = a(var1, 32);
      this.change = var1.readByte();
   }

   @Override
   public void writePacketData(DataOutputStream var1) {
      a(this.objectiveName, var1);
      a(this.objectiveDisplayName, var1);
      var1.writeByte(this.change);
   }

   @Override
   public void processPacket(NetHandler var1) {
      var1.handleSetObjective(this);
   }

   @Override
   public int getPacketSize() {
      return 2 + this.objectiveName.length() + 2 + this.objectiveDisplayName.length() + 1;
   }
}
