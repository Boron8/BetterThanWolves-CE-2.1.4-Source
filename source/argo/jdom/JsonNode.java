package argo.jdom;

import java.util.List;
import java.util.Map;

public abstract class JsonNode {
   JsonNode() {
   }

   public abstract JsonNodeType getType();

   public abstract String getText();

   public abstract Map getFields();

   public abstract List getElements();

   public final Boolean getBooleanValue(Object... var1) {
      return (Boolean)this.wrapExceptionsFor(JsonNodeSelectors.func_98315_c(var1), this, var1);
   }

   public final String getStringValue(Object... var1) {
      return (String)this.wrapExceptionsFor(JsonNodeSelectors.func_74682_a(var1), this, var1);
   }

   public final String getNumberValue(Object... var1) {
      return (String)this.wrapExceptionsFor(JsonNodeSelectors.func_98316_b(var1), this, var1);
   }

   public final boolean isArrayNode(Object... var1) {
      return JsonNodeSelectors.func_74683_b(var1).matches(this);
   }

   public final List getArrayNode(Object... var1) {
      return (List)this.wrapExceptionsFor(JsonNodeSelectors.func_74683_b(var1), this, var1);
   }

   private Object wrapExceptionsFor(JsonNodeSelector var1, JsonNode var2, Object[] var3) {
      try {
         return var1.getValue(var2);
      } catch (JsonNodeDoesNotMatchChainedJsonNodeSelectorException var5) {
         throw JsonNodeDoesNotMatchPathElementsException.jsonNodeDoesNotMatchPathElementsException(var5, var3, JsonNodeFactories.aJsonArray(var2));
      }
   }
}
