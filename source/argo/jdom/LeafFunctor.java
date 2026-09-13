package argo.jdom;

abstract class LeafFunctor implements Functor {
   @Override
   public final Object applyTo(Object var1) {
      if (!this.a(var1)) {
         throw JsonNodeDoesNotMatchChainedJsonNodeSelectorException.func_74701_a(this);
      } else {
         return this.typeSafeApplyTo(var1);
      }
   }

   protected abstract Object typeSafeApplyTo(Object var1);
}
