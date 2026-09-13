package net.minecraft.src;

import argo.jdom.JdomParser;
import argo.jdom.JsonNode;
import argo.jdom.JsonRootNode;
import argo.saj.InvalidSyntaxException;
import java.util.ArrayList;
import java.util.List;

public class ValueObjectList extends ValueObject {
   public List field_96430_d;

   public static ValueObjectList func_98161_a(String var0) {
      ValueObjectList var1 = new ValueObjectList();
      var1.field_96430_d = new ArrayList();

      try {
         JsonRootNode var2 = new JdomParser().parse(var0);
         if (var2.d(new Object[]{"servers"})) {
            for (JsonNode var4 : var2.e(new Object[]{"servers"})) {
               var1.field_96430_d.add(McoServer.func_98163_a(var4));
            }
         }
      } catch (InvalidSyntaxException var5) {
      } catch (IllegalArgumentException var6) {
      }

      return var1;
   }
}
