package argo.jdom;

import java.util.Map;

final class JsonNodeSelectors_Object extends LeafFunctor {
   public boolean func_74640_a(JsonNode var1) {
      return JsonNodeType.OBJECT == var1.getType();
   }

   @Override
   public String shortForm() {
      return "A short form object";
   }

   public Map func_74639_b(JsonNode var1) {
      return var1.getFields();
   }

   @Override
   public String toString() {
      return "an object";
   }
}
