package org.bouncycastle.asn1;

public abstract class ASN1Object implements ASN1Encodable {
   @Override
   public int hashCode() {
      return this.toASN1Primitive().hashCode();
   }

   @Override
   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (!(var1 instanceof ASN1Encodable)) {
         return false;
      } else {
         ASN1Encodable var2 = (ASN1Encodable)var1;
         return this.toASN1Primitive().equals(var2.toASN1Primitive());
      }
   }

   @Override
   public abstract ASN1Primitive toASN1Primitive();
}
