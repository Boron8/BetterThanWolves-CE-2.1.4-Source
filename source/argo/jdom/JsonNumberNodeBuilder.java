package argo.jdom;

final class JsonNumberNodeBuilder implements JsonNodeBuilder {
   private final JsonNode builtNode;

   JsonNumberNodeBuilder(String var1) {
      this.builtNode = JsonNodeFactories.aJsonNumber(var1);
   }

   @Override
   public JsonNode buildNode() {
      return this.builtNode;
   }
}
