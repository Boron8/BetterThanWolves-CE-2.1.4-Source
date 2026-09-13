package argo.jdom;

import java.util.List;
import java.util.Map;

public final class JsonStringNode extends JsonNode implements Comparable {
   private final String value;

   JsonStringNode(String var1) {
      if (var1 == null) {
         throw new NullPointerException("Attempt to construct a JsonString with a null value.");
      } else {
         this.value = var1;
      }
   }

   @Override
   public JsonNodeType getType() {
      return JsonNodeType.STRING;
   }

   @Override
   public String getText() {
      return this.value;
   }

   @Override
   public Map getFields() {
      throw new IllegalStateException("Attempt to get fields on a JsonNode without fields.");
   }

   @Override
   public List getElements() {
      throw new IllegalStateException("Attempt to get elements on a JsonNode without elements.");
   }

   @Override
   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (var1 != null && this.getClass() == var1.getClass()) {
         JsonStringNode var2 = (JsonStringNode)var1;
         return this.value.equals(var2.value);
      } else {
         return false;
      }
   }

   @Override
   public int hashCode() {
      return this.value.hashCode();
   }

   @Override
   public String toString() {
      return "JsonStringNode value:[" + this.value + "]";
   }

   public int func_74626_a(JsonStringNode var1) {
      return this.value.compareTo(var1.value);
   }
}
