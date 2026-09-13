package argo.jdom;

import java.util.LinkedList;
import java.util.List;

public final class JsonArrayNodeBuilder implements JsonNodeBuilder {
   private final List elementBuilders = new LinkedList();

   JsonArrayNodeBuilder() {
   }

   public JsonArrayNodeBuilder withElement(JsonNodeBuilder var1) {
      this.elementBuilders.add(var1);
      return this;
   }

   public JsonRootNode build() {
      LinkedList var1 = new LinkedList();

      for (JsonNodeBuilder var3 : this.elementBuilders) {
         var1.add(var3.buildNode());
      }

      return JsonNodeFactories.aJsonArray(var1);
   }
}
