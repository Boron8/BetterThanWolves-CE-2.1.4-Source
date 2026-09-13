package btw.network.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import net.minecraft.src.NetHandler;
import net.minecraft.src.Packet;

public class TimerSpeedPacket extends Packet {
   public float timerSpeed;

   public TimerSpeedPacket() {
   }

   public TimerSpeedPacket(float timerSpeed) {
      this.timerSpeed = timerSpeed;
   }

   @Override
   public void readPacketData(DataInputStream var1) throws IOException {
      this.timerSpeed = var1.readFloat();
   }

   @Override
   public void writePacketData(DataOutputStream var1) throws IOException {
      var1.writeFloat(this.timerSpeed);
   }

   @Override
   public void processPacket(NetHandler var1) {
      var1.handleTimerSpeed(this);
   }

   @Override
   public int getPacketSize() {
      return 4;
   }
}
