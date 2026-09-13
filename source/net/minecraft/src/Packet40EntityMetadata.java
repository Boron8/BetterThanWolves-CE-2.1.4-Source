package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.List;

public class Packet40EntityMetadata extends Packet {
   public int entityId;
   private List metadata;

   public Packet40EntityMetadata() {
   }

   public Packet40EntityMetadata(int var1, DataWatcher var2, boolean var3) {
      this.entityId = var1;
      if (var3) {
         this.metadata = var2.getAllWatched();
      } else {
         this.metadata = var2.unwatchAndReturnAllWatched();
      }
   }

   @Override
   public void readPacketData(DataInputStream var1) {
      this.entityId = var1.readInt();
      this.metadata = DataWatcher.readWatchableObjects(var1);
   }

   @Override
   public void writePacketData(DataOutputStream var1) {
      var1.writeInt(this.entityId);
      DataWatcher.writeObjectsInListToStream(this.metadata, var1);
   }

   @Override
   public void processPacket(NetHandler var1) {
      var1.handleEntityMetadata(this);
   }

   @Override
   public int getPacketSize() {
      return 5;
   }

   public List getMetadata() {
      return this.metadata;
   }
}
