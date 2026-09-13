package argo.jdom;

final class ChainedFunctor implements Functor {
   private final JsonNodeSelector parentJsonNodeSelector;
   private final JsonNodeSelector childJsonNodeSelector;

   ChainedFunctor(JsonNodeSelector var1, JsonNodeSelector var2) {
      this.parentJsonNodeSelector = var1;
      this.childJsonNodeSelector = var2;
   }

   @Override
   public boolean matchesNode(Object var1) {
      return this.parentJsonNodeSelector.matches(var1) && this.childJsonNodeSelector.matches(this.parentJsonNodeSelector.getValue(var1));
   }

   @Override
   public Object applyTo(Object var1) {
      Object var2;
      try {
         var2 = this.parentJsonNodeSelector.getValue(var1);
      } catch (JsonNodeDoesNotMatchChainedJsonNodeSelectorException var6) {
         throw JsonNodeDoesNotMatchChainedJsonNodeSelectorException.func_74698_b(var6, this.parentJsonNodeSelector);
      }

      try {
         return this.childJsonNodeSelector.getValue(var2);
      } catch (JsonNodeDoesNotMatchChainedJsonNodeSelectorException var5) {
         throw JsonNodeDoesNotMatchChainedJsonNodeSelectorException.func_74699_a(var5, this.parentJsonNodeSelector);
      }
   }

   @Override
   public String shortForm() {
      return this.childJsonNodeSelector.shortForm();
   }

   @Override
   public String toString() {
      return this.parentJsonNodeSelector.toString() + ", with " + this.childJsonNodeSelector.toString();
   }
}
