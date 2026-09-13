package btw.network.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import net.minecraft.src.NetHandler;
import net.minecraft.src.Packet;

public class StartBlockHarvestPacket extends Packet {
   public int posX;
   public int posY;
   public int posZ;
   public int face;
   private int miningSpeedModifier;

   public StartBlockHarvestPacket() {
   }

   public StartBlockHarvestPacket(int i, int j, int k, int iHitFace, float fMiningSpeedModifier) {
      this.posX = i;
      this.posY = j;
      this.posZ = k;
      this.face = iHitFace;
      this.miningSpeedModifier = (int)(fMiningSpeedModifier * 8000.0F);
   }

   @Override
   public void readPacketData(DataInputStream stream) throws IOException {
      this.posX = stream.readInt();
      this.posY = stream.read();
      this.posZ = stream.readInt();
      this.face = stream.read();
      this.miningSpeedModifier = stream.readShort();
   }

   @Override
   public void writePacketData(DataOutputStream stream) throws IOException {
      stream.writeInt(this.posX);
      stream.write(this.posY);
      stream.writeInt(this.posZ);
      stream.write(this.face);
      stream.writeShort(this.miningSpeedModifier);
   }

   @Override
   public void processPacket(NetHandler par1NetHandler) {
      par1NetHandler.handleStartBlockHarvest(this);
   }

   @Override
   public int getPacketSize() {
      return 12;
   }

   public float getMiningSpeedModifier() {
      return this.miningSpeedModifier / 8000.0F;
   }
}
