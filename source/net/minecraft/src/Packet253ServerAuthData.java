package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.security.PublicKey;

public class Packet253ServerAuthData extends Packet {
   private String serverId;
   private PublicKey publicKey;
   private byte[] verifyToken = new byte[0];

   public Packet253ServerAuthData() {
   }

   public Packet253ServerAuthData(String var1, PublicKey var2, byte[] var3) {
      this.serverId = var1;
      this.publicKey = var2;
      this.verifyToken = var3;
   }

   @Override
   public void readPacketData(DataInputStream var1) {
      this.serverId = a(var1, 20);
      this.publicKey = CryptManager.decodePublicKey(b(var1));
      this.verifyToken = b(var1);
   }

   @Override
   public void writePacketData(DataOutputStream var1) {
      a(this.serverId, var1);
      a(var1, this.publicKey.getEncoded());
      a(var1, this.verifyToken);
   }

   @Override
   public void processPacket(NetHandler var1) {
      var1.handleServerAuthData(this);
   }

   @Override
   public int getPacketSize() {
      return 2 + this.serverId.length() * 2 + 2 + this.publicKey.getEncoded().length + 2 + this.verifyToken.length;
   }

   public String getServerId() {
      return this.serverId;
   }

   public PublicKey getPublicKey() {
      return this.publicKey;
   }

   public byte[] getVerifyToken() {
      return this.verifyToken;
   }
}
