package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public class Packet103SetSlot extends Packet {
   public int windowId;
   public int itemSlot;
   public ItemStack myItemStack;

   public Packet103SetSlot() {
   }

   public Packet103SetSlot(int var1, int var2, ItemStack var3) {
      this.windowId = var1;
      this.itemSlot = var2;
      this.myItemStack = var3 == null ? var3 : var3.copy();
   }

   @Override
   public void processPacket(NetHandler var1) {
      var1.handleSetSlot(this);
   }

   @Override
   public void readPacketData(DataInputStream var1) {
      this.windowId = var1.readByte();
      this.itemSlot = var1.readShort();
      this.myItemStack = c(var1);
   }

   @Override
   public void writePacketData(DataOutputStream var1) {
      var1.writeByte(this.windowId);
      var1.writeShort(this.itemSlot);
      a(this.myItemStack, var1);
   }

   @Override
   public int getPacketSize() {
      return 8;
   }
}
