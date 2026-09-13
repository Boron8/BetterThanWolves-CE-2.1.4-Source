package org.bouncycastle.asn1;

public abstract class ASN1Primitive extends ASN1Object {
   ASN1Primitive() {
   }

   @Override
   public final boolean equals(Object var1) {
      return this == var1 ? true : var1 instanceof ASN1Encodable && this.asn1Equals(((ASN1Encodable)var1).toASN1Primitive());
   }

   @Override
   public ASN1Primitive toASN1Primitive() {
      return this;
   }

   @Override
   public abstract int hashCode();

   abstract boolean asn1Equals(ASN1Primitive var1);
}
