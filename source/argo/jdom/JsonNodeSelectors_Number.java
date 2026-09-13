package argo.jdom;

final class JsonNodeSelectors_Number extends LeafFunctor {
   public boolean func_98314_a(JsonNode var1) {
      return JsonNodeType.NUMBER == var1.getType();
   }

   @Override
   public String shortForm() {
      return "A short form nullable number";
   }

   public String func_98313_b(JsonNode var1) {
      return var1.getText();
   }

   @Override
   public String toString() {
      return "a value that is a number";
   }
}
