package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public class Packet208SetDisplayObjective extends Packet {
   public int scoreboardPosition;
   public String scoreName;

   public Packet208SetDisplayObjective() {
   }

   public Packet208SetDisplayObjective(int var1, ScoreObjective var2) {
      this.scoreboardPosition = var1;
      if (var2 == null) {
         this.scoreName = "";
      } else {
         this.scoreName = var2.getName();
      }
   }

   @Override
   public void readPacketData(DataInputStream var1) {
      this.scoreboardPosition = var1.readByte();
      this.scoreName = a(var1, 16);
   }

   @Override
   public void writePacketData(DataOutputStream var1) {
      var1.writeByte(this.scoreboardPosition);
      a(this.scoreName, var1);
   }

   @Override
   public void processPacket(NetHandler var1) {
      var1.handleSetDisplayObjective(this);
   }

   @Override
   public int getPacketSize() {
      return 3 + this.scoreName.length();
   }
}
