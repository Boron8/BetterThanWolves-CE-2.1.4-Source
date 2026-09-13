package argo.jdom;

import java.util.HashMap;

class JsonObjectNodeBuilder_List extends HashMap {
   JsonObjectNodeBuilder_List(JsonObjectNodeBuilder var1) {
      this.nodeBuilder = var1;

      for (JsonFieldBuilder var3 : JsonObjectNodeBuilder.func_74607_a(this.nodeBuilder)) {
         this.put(var3.func_74724_b(), var3.buildValue());
      }
   }
}
