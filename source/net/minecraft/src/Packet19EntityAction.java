package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

public class Packet19EntityAction extends Packet {
   public int entityId;
   public int state;

   public Packet19EntityAction() {
   }

   @Environment(EnvType.CLIENT)
   public Packet19EntityAction(Entity par1Entity, int par2) {
      this.entityId = par1Entity.entityId;
      this.state = par2;
   }

   @Override
   public void readPacketData(DataInputStream par1DataInputStream) throws IOException {
      this.entityId = par1DataInputStream.readInt();
      this.state = par1DataInputStream.readInt();
   }

   @Override
   public void writePacketData(DataOutputStream par1DataOutputStream) throws IOException {
      par1DataOutputStream.writeInt(this.entityId);
      par1DataOutputStream.writeInt(this.state);
   }

   @Override
   public void processPacket(NetHandler par1NetHandler) {
      par1NetHandler.handleEntityAction(this);
   }

   @Override
   public int getPacketSize() {
      return 8;
   }
}
