package argo.jdom;

final class JsonFieldBuilder {
   private JsonNodeBuilder key;
   private JsonNodeBuilder valueBuilder;

   private JsonFieldBuilder() {
   }

   static JsonFieldBuilder aJsonFieldBuilder() {
      return new JsonFieldBuilder();
   }

   JsonFieldBuilder withKey(JsonNodeBuilder var1) {
      this.key = var1;
      return this;
   }

   JsonFieldBuilder withValue(JsonNodeBuilder var1) {
      this.valueBuilder = var1;
      return this;
   }

   JsonStringNode func_74724_b() {
      return (JsonStringNode)this.key.buildNode();
   }

   JsonNode buildValue() {
      return this.valueBuilder.buildNode();
   }
}
