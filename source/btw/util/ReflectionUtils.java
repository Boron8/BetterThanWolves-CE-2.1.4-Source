package btw.util;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

public class ReflectionUtils {
   private static boolean hasCheckedForObfuscation = false;
   private static boolean isObfuscated = false;
   public static Map<Class, Class> boxedToPrimitiveTypeMap = new HashMap<>();

   public static boolean isObfuscated() {
      if (hasCheckedForObfuscation) {
         return isObfuscated;
      } else {
         hasCheckedForObfuscation = true;
         return false;
      }
   }

   public static void setObfuscated(boolean isObfuscated) {
      ReflectionUtils.isObfuscated = isObfuscated;
      hasCheckedForObfuscation = true;
   }

   public static Class<?> getPrimitiveFromBoxedClass(Class<?> type) {
      return boxedToPrimitiveTypeMap.containsKey(type) ? boxedToPrimitiveTypeMap.get(type) : null;
   }

   public static Constructor findMatchingConstructor(Class<?> classForSearch, Class[] parameterTypes) {
      Constructor[] constructors = classForSearch.getDeclaredConstructors();
      Constructor constructorToUse = null;

      for (Constructor c : constructors) {
         boolean constructorMatches = true;
         Class[] cParamTypes = c.getParameterTypes();
         if (cParamTypes.length == parameterTypes.length) {
            for (int j = 0; j < cParamTypes.length; j++) {
               if (!doesParameterTypeSatisfyConstructorType(parameterTypes[j], cParamTypes[j])) {
                  constructorMatches = false;
                  break;
               }
            }
         } else {
            constructorMatches = false;
         }

         if (constructorMatches) {
            constructorToUse = c;
            break;
         }
      }

      return constructorToUse;
   }

   private static boolean doesParameterTypeSatisfyConstructorType(Class<?> parameterType, Class<?> constructorType) {
      if (parameterType.isPrimitive() && constructorType.isPrimitive()) {
         int pSize = getPrimitiveSize(parameterType);
         int cSize = getPrimitiveSize(constructorType);
         if (constructorType == boolean.class) {
            return parameterType.equals(boolean.class);
         } else {
            return !isFloatingPoint(constructorType) && isFloatingPoint(parameterType) ? false : pSize <= cSize;
         }
      } else {
         return constructorType.isAssignableFrom(parameterType);
      }
   }

   private static int getPrimitiveSize(Class<?> primitive) {
      if (!primitive.isPrimitive()) {
         throw new IllegalArgumentException("Expected a primitive but was passed " + primitive.getSimpleName());
      } else if (primitive == byte.class || primitive == char.class) {
         return 8;
      } else if (primitive == short.class) {
         return 16;
      } else {
         return primitive != int.class && primitive != float.class ? 64 : 32;
      }
   }

   private static boolean isFloatingPoint(Class<?> primitive) {
      return primitive == float.class || primitive == double.class;
   }

   public static Class getClassByName(String name) {
      Class c = null;

      try {
         if (isObfuscated()) {
            c = Class.forName("net.minecraft.src." + name);
         } else {
            c = Class.forName(name);
         }
      } catch (ClassNotFoundException var3) {
         var3.printStackTrace();
      }

      return c;
   }

   static {
      boxedToPrimitiveTypeMap.put(Byte.class, byte.class);
      boxedToPrimitiveTypeMap.put(Short.class, short.class);
      boxedToPrimitiveTypeMap.put(Integer.class, int.class);
      boxedToPrimitiveTypeMap.put(Long.class, long.class);
      boxedToPrimitiveTypeMap.put(Float.class, float.class);
      boxedToPrimitiveTypeMap.put(Double.class, double.class);
      boxedToPrimitiveTypeMap.put(Character.class, char.class);
      boxedToPrimitiveTypeMap.put(Boolean.class, boolean.class);
   }
}
