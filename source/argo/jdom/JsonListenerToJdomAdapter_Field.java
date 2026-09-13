package argo.jdom;

class JsonListenerToJdomAdapter_Field implements JsonListenerToJdomAdapter_NodeContainer {
   JsonListenerToJdomAdapter_Field(JsonListenerToJdomAdapter var1, JsonFieldBuilder var2) {
      this.listenerToJdomAdapter = var1;
      this.fieldBuilder = var2;
   }

   @Override
   public void addNode(JsonNodeBuilder var1) {
      this.fieldBuilder.withValue(var1);
   }

   @Override
   public void addField(JsonFieldBuilder var1) {
      throw new RuntimeException("Coding failure in Argo:  Attempt to add a field to a field.");
   }
}
