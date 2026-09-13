package argo.jdom;

import java.util.Arrays;

public final class JsonNodeSelectors {
   public static JsonNodeSelector func_74682_a(Object... var0) {
      return chainOn(var0, new JsonNodeSelector(new JsonNodeSelectors_String()));
   }

   public static JsonNodeSelector func_98316_b(Object... var0) {
      return chainOn(var0, new JsonNodeSelector(new JsonNodeSelectors_Number()));
   }

   public static JsonNodeSelector func_98315_c(Object... var0) {
      return chainOn(var0, new JsonNodeSelector(new JsonNodeSelectors_Boolean()));
   }

   public static JsonNodeSelector func_74683_b(Object... var0) {
      return chainOn(var0, new JsonNodeSelector(new JsonNodeSelectors_Array()));
   }

   public static JsonNodeSelector func_74681_c(Object... var0) {
      return chainOn(var0, new JsonNodeSelector(new JsonNodeSelectors_Object()));
   }

   public static JsonNodeSelector func_74675_a(String var0) {
      return func_74680_a(JsonNodeFactories.aJsonString(var0));
   }

   public static JsonNodeSelector func_74680_a(JsonStringNode var0) {
      return new JsonNodeSelector(new JsonNodeSelectors_Field(var0));
   }

   public static JsonNodeSelector func_74684_b(String var0) {
      return func_74681_c().with(func_74675_a(var0));
   }

   public static JsonNodeSelector func_74678_a(int var0) {
      return new JsonNodeSelector(new JsonNodeSelectors_Element(var0));
   }

   public static JsonNodeSelector func_74679_b(int var0) {
      return func_74683_b().with(func_74678_a(var0));
   }

   private static JsonNodeSelector chainOn(Object[] var0, JsonNodeSelector var1) {
      JsonNodeSelector var2 = var1;

      for (int var3 = var0.length - 1; var3 >= 0; var3--) {
         if (var0[var3] instanceof Integer) {
            var2 = chainedJsonNodeSelector(func_74679_b((Integer)var0[var3]), var2);
         } else {
            if (!(var0[var3] instanceof String)) {
               throw new IllegalArgumentException(
                  "Element ["
                     + var0[var3]
                     + "] of path elements"
                     + " ["
                     + Arrays.toString(var0)
                     + "] was of illegal type ["
                     + var0[var3].getClass().getCanonicalName()
                     + "]; only Integer and String are valid."
               );
            }

            var2 = chainedJsonNodeSelector(func_74684_b((String)var0[var3]), var2);
         }
      }

      return var2;
   }

   private static JsonNodeSelector chainedJsonNodeSelector(JsonNodeSelector var0, JsonNodeSelector var1) {
      return new JsonNodeSelector(new ChainedFunctor(var0, var1));
   }
}
