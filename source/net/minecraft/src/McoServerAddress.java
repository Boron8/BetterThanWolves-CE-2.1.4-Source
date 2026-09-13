package net.minecraft.src;

import argo.jdom.JdomParser;
import argo.jdom.JsonRootNode;
import argo.saj.InvalidSyntaxException;

public class McoServerAddress extends ValueObject {
   public String field_96417_a;

   public static McoServerAddress func_98162_a(String var0) {
      McoServerAddress var1 = new McoServerAddress();

      try {
         JsonRootNode var2 = new JdomParser().parse(var0);
         var1.field_96417_a = var2.b(new Object[]{"address"});
      } catch (InvalidSyntaxException var3) {
      } catch (IllegalArgumentException var4) {
      }

      return var1;
   }
}
