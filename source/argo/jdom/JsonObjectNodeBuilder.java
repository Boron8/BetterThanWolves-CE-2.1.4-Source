package argo.jdom;

import java.util.LinkedList;
import java.util.List;

public final class JsonObjectNodeBuilder implements JsonNodeBuilder {
   private final List fieldBuilders = new LinkedList();

   JsonObjectNodeBuilder() {
   }

   public JsonObjectNodeBuilder withFieldBuilder(JsonFieldBuilder var1) {
      this.fieldBuilders.add(var1);
      return this;
   }

   public JsonRootNode func_74606_a() {
      return JsonNodeFactories.aJsonObject(new JsonObjectNodeBuilder_List(this));
   }
}
