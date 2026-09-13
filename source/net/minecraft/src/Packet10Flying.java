package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public class Packet10Flying extends Packet {
   public double xPosition;
   public double yPosition;
   public double zPosition;
   public double stance;
   public float yaw;
   public float pitch;
   public boolean onGround;
   public boolean moving;
   public boolean rotating;

   public Packet10Flying() {
   }

   public Packet10Flying(boolean var1) {
      this.onGround = var1;
   }

   @Override
   public void processPacket(NetHandler var1) {
      var1.handleFlying(this);
   }

   @Override
   public void readPacketData(DataInputStream var1) {
      this.onGround = var1.read() != 0;
   }

   @Override
   public void writePacketData(DataOutputStream var1) {
      var1.write(this.onGround ? 1 : 0);
   }

   @Override
   public int getPacketSize() {
      return 1;
   }

   @Override
   public boolean isRealPacket() {
      return true;
   }

   @Override
   public boolean containsSameEntityIDAs(Packet var1) {
      return true;
   }
}
