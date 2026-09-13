package argo.jdom;

import argo.saj.JsonListener;
import java.util.Stack;

final class JsonListenerToJdomAdapter implements JsonListener {
   private final Stack stack = new Stack();
   private JsonNodeBuilder root;

   JsonRootNode getDocument() {
      return (JsonRootNode)this.root.buildNode();
   }

   @Override
   public void startDocument() {
   }

   @Override
   public void endDocument() {
   }

   @Override
   public void startArray() {
      JsonArrayNodeBuilder var1 = JsonNodeBuilders.anArrayBuilder();
      this.addRootNode(var1);
      this.stack.push(new JsonListenerToJdomAdapter_Array(this, var1));
   }

   @Override
   public void endArray() {
      this.stack.pop();
   }

   @Override
   public void startObject() {
      JsonObjectNodeBuilder var1 = JsonNodeBuilders.anObjectBuilder();
      this.addRootNode(var1);
      this.stack.push(new JsonListenerToJdomAdapter_Object(this, var1));
   }

   @Override
   public void endObject() {
      this.stack.pop();
   }

   @Override
   public void startField(String var1) {
      JsonFieldBuilder var2 = JsonFieldBuilder.aJsonFieldBuilder().withKey(JsonNodeBuilders.func_74710_b(var1));
      ((JsonListenerToJdomAdapter_NodeContainer)this.stack.peek()).addField(var2);
      this.stack.push(new JsonListenerToJdomAdapter_Field(this, var2));
   }

   @Override
   public void endField() {
      this.stack.pop();
   }

   @Override
   public void numberValue(String var1) {
      this.addValue(JsonNodeBuilders.func_74712_a(var1));
   }

   @Override
   public void trueValue() {
      this.addValue(JsonNodeBuilders.func_74713_b());
   }

   @Override
   public void stringValue(String var1) {
      this.addValue(JsonNodeBuilders.func_74710_b(var1));
   }

   @Override
   public void falseValue() {
      this.addValue(JsonNodeBuilders.func_74709_c());
   }

   @Override
   public void nullValue() {
      this.addValue(JsonNodeBuilders.func_74714_a());
   }

   private void addRootNode(JsonNodeBuilder var1) {
      if (this.root == null) {
         this.root = var1;
      } else {
         this.addValue(var1);
      }
   }

   private void addValue(JsonNodeBuilder var1) {
      ((JsonListenerToJdomAdapter_NodeContainer)this.stack.peek()).addNode(var1);
   }
}
