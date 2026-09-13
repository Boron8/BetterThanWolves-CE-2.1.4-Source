package org.bouncycastle.jce.provider;

import java.security.PrivilegedAction;

class BouncyCastleProviderAction implements PrivilegedAction {
   BouncyCastleProviderAction(BouncyCastleProvider var1) {
      this.theBouncyCastleProvider = var1;
   }

   @Override
   public Object run() {
      BouncyCastleProvider.doSetup(this.theBouncyCastleProvider);
      return null;
   }
}
