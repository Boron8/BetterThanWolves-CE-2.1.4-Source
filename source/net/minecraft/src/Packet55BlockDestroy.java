package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public class Packet55BlockDestroy extends Packet {
   private int entityId;
   private int posX;
   private int posY;
   private int posZ;
   private int destroyedStage;

   public Packet55BlockDestroy() {
   }

   public Packet55BlockDestroy(int var1, int var2, int var3, int var4, int var5) {
      this.entityId = var1;
      this.posX = var2;
      this.posY = var3;
      this.posZ = var4;
      this.destroyedStage = var5;
   }

   @Override
   public void readPacketData(DataInputStream var1) {
      this.entityId = var1.readInt();
      this.posX = var1.readInt();
      this.posY = var1.readInt();
      this.posZ = var1.readInt();
      this.destroyedStage = var1.read();
   }

   @Override
   public void writePacketData(DataOutputStream var1) {
      var1.writeInt(this.entityId);
      var1.writeInt(this.posX);
      var1.writeInt(this.posY);
      var1.writeInt(this.posZ);
      var1.write(this.destroyedStage);
   }

   @Override
   public void processPacket(NetHandler var1) {
      var1.handleBlockDestroy(this);
   }

   @Override
   public int getPacketSize() {
      return 13;
   }

   public int getEntityId() {
      return this.entityId;
   }

   public int getPosX() {
      return this.posX;
   }

   public int getPosY() {
      return this.posY;
   }

   public int getPosZ() {
      return this.posZ;
   }

   public int getDestroyedStage() {
      return this.destroyedStage;
   }

   @Override
   public boolean isRealPacket() {
      return true;
   }

   @Override
   public boolean containsSameEntityIDAs(Packet var1) {
      Packet55BlockDestroy var2 = (Packet55BlockDestroy)var1;
      return var2.entityId == this.entityId;
   }
}
