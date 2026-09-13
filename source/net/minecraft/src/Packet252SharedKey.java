package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.security.PrivateKey;
import java.security.PublicKey;
import javax.crypto.SecretKey;

public class Packet252SharedKey extends Packet {
   private byte[] sharedSecret = new byte[0];
   private byte[] verifyToken = new byte[0];
   private SecretKey sharedKey;

   public Packet252SharedKey() {
   }

   public Packet252SharedKey(SecretKey var1, PublicKey var2, byte[] var3) {
      this.sharedKey = var1;
      this.sharedSecret = CryptManager.encryptData(var2, var1.getEncoded());
      this.verifyToken = CryptManager.encryptData(var2, var3);
   }

   @Override
   public void readPacketData(DataInputStream var1) {
      this.sharedSecret = b(var1);
      this.verifyToken = b(var1);
   }

   @Override
   public void writePacketData(DataOutputStream var1) {
      a(var1, this.sharedSecret);
      a(var1, this.verifyToken);
   }

   @Override
   public void processPacket(NetHandler var1) {
      var1.handleSharedKey(this);
   }

   @Override
   public int getPacketSize() {
      return 2 + this.sharedSecret.length + 2 + this.verifyToken.length;
   }

   public SecretKey getSharedKey(PrivateKey var1) {
      return var1 == null ? this.sharedKey : (this.sharedKey = CryptManager.decryptSharedKey(var1, this.sharedSecret));
   }

   public SecretKey getSharedKey() {
      return this.getSharedKey(null);
   }

   public byte[] getVerifyToken(PrivateKey var1) {
      return var1 == null ? this.verifyToken : CryptManager.decryptData(var1, this.verifyToken);
   }
}
