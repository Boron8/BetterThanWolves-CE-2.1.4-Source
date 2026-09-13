package argo.jdom;

final class JsonNodeSelectors_Boolean extends LeafFunctor {
   public boolean func_98312_a(JsonNode var1) {
      return JsonNodeType.TRUE == var1.getType() || JsonNodeType.FALSE == var1.getType();
   }

   @Override
   public String shortForm() {
      return "A short form boolean";
   }

   public Boolean func_98311_b(JsonNode var1) {
      return JsonNodeType.TRUE == var1.getType();
   }

   @Override
   public String toString() {
      return "a true or false";
   }
}
