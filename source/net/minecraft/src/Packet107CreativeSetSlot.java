package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public class Packet107CreativeSetSlot extends Packet {
   public int slot;
   public ItemStack itemStack;

   public Packet107CreativeSetSlot() {
   }

   public Packet107CreativeSetSlot(int var1, ItemStack var2) {
      this.slot = var1;
      this.itemStack = var2 != null ? var2.copy() : null;
   }

   @Override
   public void processPacket(NetHandler var1) {
      var1.handleCreativeSetSlot(this);
   }

   @Override
   public void readPacketData(DataInputStream var1) {
      this.slot = var1.readShort();
      this.itemStack = c(var1);
   }

   @Override
   public void writePacketData(DataOutputStream var1) {
      var1.writeShort(this.slot);
      a(this.itemStack, var1);
   }

   @Override
   public int getPacketSize() {
      return 8;
   }
}
