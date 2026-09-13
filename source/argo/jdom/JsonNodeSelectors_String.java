package argo.jdom;

final class JsonNodeSelectors_String extends LeafFunctor {
   public boolean func_74645_a(JsonNode var1) {
      return JsonNodeType.STRING == var1.getType();
   }

   @Override
   public String shortForm() {
      return "A short form string";
   }

   public String func_74644_b(JsonNode var1) {
      return var1.getText();
   }

   @Override
   public String toString() {
      return "a value that is a string";
   }
}
