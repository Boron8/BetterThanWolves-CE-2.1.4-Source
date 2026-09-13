package argo.format;

import argo.jdom.JsonNode;
import argo.jdom.JsonRootNode;
import argo.jdom.JsonStringNode;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.TreeSet;

public final class CompactJsonFormatter implements JsonFormatter {
   @Override
   public String format(JsonRootNode var1) {
      StringWriter var2 = new StringWriter();

      try {
         this.format(var1, var2);
      } catch (IOException var4) {
         throw new RuntimeException("Coding failure in Argo:  StringWriter gave an IOException", var4);
      }

      return var2.toString();
   }

   public void format(JsonRootNode var1, Writer var2) {
      this.formatJsonNode(var1, var2);
   }

   private void formatJsonNode(JsonNode var1, Writer var2) {
      boolean var3 = true;
      switch (var1.getType()) {
         case ARRAY:
            var2.append('[');

            for (JsonNode var7 : var1.getElements()) {
               if (!var3) {
                  var2.append(',');
               }

               var3 = false;
               this.formatJsonNode(var7, var2);
            }

            var2.append(']');
            break;
         case OBJECT:
            var2.append('{');

            for (JsonStringNode var5 : new TreeSet(var1.getFields().keySet())) {
               if (!var3) {
                  var2.append(',');
               }

               var3 = false;
               this.formatJsonNode(var5, var2);
               var2.append(':');
               this.formatJsonNode((JsonNode)var1.getFields().get(var5), var2);
            }

            var2.append('}');
            break;
         case STRING:
            var2.append('"').append(new JsonEscapedString(var1.getText()).toString()).append('"');
            break;
         case NUMBER:
            var2.append(var1.getText());
            break;
         case FALSE:
            var2.append("false");
            break;
         case TRUE:
            var2.append("true");
            break;
         case NULL:
            var2.append("null");
            break;
         default:
            throw new RuntimeException("Coding failure in Argo:  Attempt to format a JsonNode of unknown type [" + var1.getType() + "];");
      }
   }
}
