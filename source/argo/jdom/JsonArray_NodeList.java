package argo.jdom;

import java.util.ArrayList;

final class JsonArray_NodeList extends ArrayList {
   JsonArray_NodeList(Iterable var1) {
      this.elements = var1;

      for (JsonNode var3 : this.elements) {
         this.add(var3);
      }
   }
}
