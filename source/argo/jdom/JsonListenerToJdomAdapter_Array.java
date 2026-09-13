package argo.jdom;

class JsonListenerToJdomAdapter_Array implements JsonListenerToJdomAdapter_NodeContainer {
   JsonListenerToJdomAdapter_Array(JsonListenerToJdomAdapter var1, JsonArrayNodeBuilder var2) {
      this.listenerToJdomAdapter = var1;
      this.nodeBuilder = var2;
   }

   @Override
   public void addNode(JsonNodeBuilder var1) {
      this.nodeBuilder.withElement(var1);
   }

   @Override
   public void addField(JsonFieldBuilder var1) {
      throw new RuntimeException("Coding failure in Argo:  Attempt to add a field to an array.");
   }
}
