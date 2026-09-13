package org.bouncycastle.util;

public final class Strings {
   public static String toLowerCase(String var0) {
      boolean var1 = false;
      char[] var2 = var0.toCharArray();

      for (int var3 = 0; var3 != var2.length; var3++) {
         char var4 = var2[var3];
         if ('A' <= var4 && 'Z' >= var4) {
            var1 = true;
            var2[var3] = (char)(var4 - 'A' + 97);
         }
      }

      return var1 ? new String(var2) : var0;
   }
}
