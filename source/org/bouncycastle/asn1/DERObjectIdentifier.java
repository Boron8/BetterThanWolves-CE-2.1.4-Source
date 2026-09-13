package org.bouncycastle.asn1;

public class DERObjectIdentifier extends ASN1Primitive {
   String identifier;
   private static ASN1ObjectIdentifier[][] cache = new ASN1ObjectIdentifier[255][];

   public DERObjectIdentifier(String var1) {
      if (!isValidIdentifier(var1)) {
         throw new IllegalArgumentException("string " + var1 + " not an OID");
      } else {
         this.identifier = var1;
      }
   }

   public String getId() {
      return this.identifier;
   }

   @Override
   public int hashCode() {
      return this.identifier.hashCode();
   }

   @Override
   boolean asn1Equals(ASN1Primitive var1) {
      return !(var1 instanceof DERObjectIdentifier) ? false : this.identifier.equals(((DERObjectIdentifier)var1).identifier);
   }

   @Override
   public String toString() {
      return this.getId();
   }

   private static boolean isValidIdentifier(String var0) {
      if (var0.length() >= 3 && var0.charAt(1) == '.') {
         char var1 = var0.charAt(0);
         if (var1 >= '0' && var1 <= '2') {
            boolean var2 = false;

            for (int var3 = var0.length() - 1; var3 >= 2; var3--) {
               char var4 = var0.charAt(var3);
               if ('0' <= var4 && var4 <= '9') {
                  var2 = true;
               } else {
                  if (var4 != '.') {
                     return false;
                  }

                  if (!var2) {
                     return false;
                  }

                  var2 = false;
               }
            }

            return var2;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }
}
