package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet26EntityExpOrb extends Packet {
   public boolean notPlayerOwned;
   public int entityId;
   public int posX;
   public int posY;
   public int posZ;
   public int xpValue;

   public Packet26EntityExpOrb() {
   }

   public Packet26EntityExpOrb(EntityXPOrb par1EntityXPOrb) {
      this.entityId = par1EntityXPOrb.entityId;
      this.posX = MathHelper.floor_double(par1EntityXPOrb.posX * 32.0);
      this.posY = MathHelper.floor_double(par1EntityXPOrb.posY * 32.0);
      this.posZ = MathHelper.floor_double(par1EntityXPOrb.posZ * 32.0);
      this.xpValue = par1EntityXPOrb.getXpValue();
      this.notPlayerOwned = par1EntityXPOrb.notPlayerOwned;
   }

   @Override
   public void readPacketData(DataInputStream par1DataInputStream) throws IOException {
      this.entityId = par1DataInputStream.readInt();
      this.posX = par1DataInputStream.readInt();
      this.posY = par1DataInputStream.readInt();
      this.posZ = par1DataInputStream.readInt();
      this.xpValue = par1DataInputStream.readShort();
      this.notPlayerOwned = par1DataInputStream.readBoolean();
   }

   @Override
   public void writePacketData(DataOutputStream par1DataOutputStream) throws IOException {
      par1DataOutputStream.writeInt(this.entityId);
      par1DataOutputStream.writeInt(this.posX);
      par1DataOutputStream.writeInt(this.posY);
      par1DataOutputStream.writeInt(this.posZ);
      par1DataOutputStream.writeShort(this.xpValue);
      par1DataOutputStream.writeBoolean(this.notPlayerOwned);
   }

   @Override
   public void processPacket(NetHandler par1NetHandler) {
      par1NetHandler.handleEntityExpOrb(this);
   }

   @Override
   public int getPacketSize() {
      return 19;
   }
}
