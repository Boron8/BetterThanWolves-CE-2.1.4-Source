package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public class Packet3Chat extends Packet {
   public static int maxChatLength = 119;
   public String message;
   private boolean isServer = true;

   public Packet3Chat() {
   }

   public Packet3Chat(String var1) {
      this(var1, true);
   }

   public Packet3Chat(String var1, boolean var2) {
      if (var1.length() > maxChatLength) {
         var1 = var1.substring(0, maxChatLength);
      }

      this.message = var1;
      this.isServer = var2;
   }

   @Override
   public void readPacketData(DataInputStream var1) {
      this.message = a(var1, maxChatLength);
   }

   @Override
   public void writePacketData(DataOutputStream var1) {
      a(this.message, var1);
   }

   @Override
   public void processPacket(NetHandler var1) {
      var1.handleChat(this);
   }

   @Override
   public int getPacketSize() {
      return 2 + this.message.length() * 2;
   }

   public boolean getIsServer() {
      return this.isServer;
   }

   @Override
   public boolean canProcessAsync() {
      return !this.message.startsWith("/");
   }
}
