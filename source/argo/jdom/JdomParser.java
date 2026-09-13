package argo.jdom;

import argo.saj.SajParser;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

public final class JdomParser {
   public JsonRootNode parse(Reader var1) {
      JsonListenerToJdomAdapter var2 = new JsonListenerToJdomAdapter();
      new SajParser().parse(var1, var2);
      return var2.getDocument();
   }

   public JsonRootNode parse(String var1) {
      try {
         return this.parse(new StringReader(var1));
      } catch (IOException var4) {
         throw new RuntimeException("Coding failure in Argo:  StringWriter gave an IOException", var4);
      }
   }
}
