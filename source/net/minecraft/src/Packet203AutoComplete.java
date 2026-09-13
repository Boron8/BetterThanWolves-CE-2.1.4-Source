package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public class Packet203AutoComplete extends Packet {
   private String text;

   public Packet203AutoComplete() {
   }

   public Packet203AutoComplete(String var1) {
      this.text = var1;
   }

   @Override
   public void readPacketData(DataInputStream var1) {
      this.text = a(var1, Packet3Chat.maxChatLength);
   }

   @Override
   public void writePacketData(DataOutputStream var1) {
      a(this.text, var1);
   }

   @Override
   public void processPacket(NetHandler var1) {
      var1.handleAutoComplete(this);
   }

   @Override
   public int getPacketSize() {
      return 2 + this.text.length() * 2;
   }

   public String getText() {
      return this.text;
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
