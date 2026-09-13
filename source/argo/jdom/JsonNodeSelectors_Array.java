package argo.jdom;

import java.util.List;

final class JsonNodeSelectors_Array extends LeafFunctor {
   public boolean matchesNode_(JsonNode var1) {
      return JsonNodeType.ARRAY == var1.getType();
   }

   @Override
   public String shortForm() {
      return "A short form array";
   }

   public List typeSafeApplyTo(JsonNode var1) {
      return var1.getElements();
   }

   @Override
   public String toString() {
      return "an array";
   }
}
