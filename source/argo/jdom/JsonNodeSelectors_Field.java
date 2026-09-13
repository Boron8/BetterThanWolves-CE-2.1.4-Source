package argo.jdom;

import java.util.Map;

final class JsonNodeSelectors_Field extends LeafFunctor {
   JsonNodeSelectors_Field(JsonStringNode var1) {
      this.theFieldName = var1;
   }

   public boolean func_74641_a(Map var1) {
      return var1.containsKey(this.theFieldName);
   }

   @Override
   public String shortForm() {
      return "\"" + this.theFieldName.getText() + "\"";
   }

   public JsonNode typeSafeApplyTo(Map var1) {
      return (JsonNode)var1.get(this.theFieldName);
   }

   @Override
   public String toString() {
      return "a field called [\"" + this.theFieldName.getText() + "\"]";
   }
}
