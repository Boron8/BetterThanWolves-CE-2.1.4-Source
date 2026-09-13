package net.minecraft.src;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public abstract class ValueObject {
   @Override
   public String toString() {
      StringBuilder var1 = new StringBuilder("{");

      for (Field var5 : this.getClass().getFields()) {
         if (!func_96394_a(var5)) {
            try {
               var1.append(var5.getName()).append("=").append(var5.get(this)).append(" ");
            } catch (IllegalAccessException var7) {
            }
         }
      }

      var1.deleteCharAt(var1.length() - 1);
      var1.append('}');
      return var1.toString();
   }

   private static boolean func_96394_a(Field var0) {
      return Modifier.isStatic(var0.getModifiers());
   }
}
