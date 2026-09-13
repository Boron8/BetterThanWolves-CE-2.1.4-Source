package argo.jdom;

public final class JsonStringNodeBuilder implements JsonNodeBuilder {
   private final String builtStringNode;

   JsonStringNodeBuilder(String var1) {
      this.builtStringNode = var1;
   }

   public JsonStringNode func_74600_a() {
      return JsonNodeFactories.aJsonString(this.builtStringNode);
   }
}
