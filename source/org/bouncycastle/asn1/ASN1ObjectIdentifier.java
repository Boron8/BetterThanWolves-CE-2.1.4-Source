package org.bouncycastle.asn1;

public class ASN1ObjectIdentifier extends DERObjectIdentifier {
   public ASN1ObjectIdentifier(String var1) {
      super(var1);
   }

   public ASN1ObjectIdentifier branch(String var1) {
      return new ASN1ObjectIdentifier(this.b() + "." + var1);
   }
}
