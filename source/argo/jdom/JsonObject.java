package argo.jdom;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

final class JsonObject extends JsonRootNode {
   private final Map fields;

   JsonObject(Map var1) {
      this.fields = new HashMap(var1);
   }

   @Override
   public Map getFields() {
      return new HashMap(this.fields);
   }

   @Override
   public JsonNodeType getType() {
      return JsonNodeType.OBJECT;
   }

   @Override
   public String getText() {
      throw new IllegalStateException("Attempt to get text on a JsonNode without text.");
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
         JsonObject var2 = (JsonObject)var1;
         return this.fields.equals(var2.fields);
      } else {
         return false;
      }
   }

   @Override
   public int hashCode() {
      return this.fields.hashCode();
   }

   @Override
   public String toString() {
      return "JsonObject fields:[" + this.fields + "]";
   }
}
