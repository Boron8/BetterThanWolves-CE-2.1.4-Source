package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public class Packet5PlayerInventory extends Packet {
   public int entityID;
   public int slot;
   private ItemStack itemSlot;

   public Packet5PlayerInventory() {
   }

   public Packet5PlayerInventory(int var1, int var2, ItemStack var3) {
      this.entityID = var1;
      this.slot = var2;
      this.itemSlot = var3 == null ? null : var3.copy();
   }

   @Override
   public void readPacketData(DataInputStream var1) {
      this.entityID = var1.readInt();
      this.slot = var1.readShort();
      this.itemSlot = c(var1);
   }

   @Override
   public void writePacketData(DataOutputStream var1) {
      var1.writeInt(this.entityID);
      var1.writeShort(this.slot);
      a(this.itemSlot, var1);
   }

   @Override
   public void processPacket(NetHandler var1) {
      var1.handlePlayerInventory(this);
   }

   @Override
   public int getPacketSize() {
      return 8;
   }

   public ItemStack getItemSlot() {
      return this.itemSlot;
   }

   @Override
   public boolean isRealPacket() {
      return true;
   }

   @Override
   public boolean containsSameEntityIDAs(Packet var1) {
      Packet5PlayerInventory var2 = (Packet5PlayerInventory)var1;
      return var2.entityID == this.entityID && var2.slot == this.slot;
   }
}
