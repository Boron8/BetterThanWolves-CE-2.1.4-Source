package btw.util;

public class MathUtils {
   public static double clampDouble(double dValue, double dBottom, double dTop) {
      if (dValue < dBottom) {
         return dBottom;
      } else {
         return dValue > dTop ? dTop : dValue;
      }
   }

   public static double clampDoubleTop(double dValue, double dTop) {
      return dValue > dTop ? dTop : dValue;
   }

   public static double clampDoubleBottom(double dValue, double dBottom) {
      return dValue < dBottom ? dBottom : dValue;
   }

   public static double absDouble(double dValue) {
      return dValue >= 0.0 ? dValue : -dValue;
   }
}
