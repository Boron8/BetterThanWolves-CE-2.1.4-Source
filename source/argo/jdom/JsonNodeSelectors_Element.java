package argo.jdom;

import java.util.List;

final class JsonNodeSelectors_Element extends LeafFunctor {
   JsonNodeSelectors_Element(int var1) {
      this.index = var1;
   }

   public boolean matchesNode_(List var1) {
      return var1.size() > this.index;
   }

   @Override
   public String shortForm() {
      return Integer.toString(this.index);
   }

   public JsonNode typeSafeApplyTo_(List var1) {
      return (JsonNode)var1.get(this.index);
   }

   @Override
   public String toString() {
      return "an element at index [" + this.index + "]";
   }
}
