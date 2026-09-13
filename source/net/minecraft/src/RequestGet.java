package net.minecraft.src;

public class RequestGet extends Request {
   public RequestGet(String var1, int var2, int var3) {
      super(var1, var2, var3);
   }

   public RequestGet func_96371_f() {
      try {
         this.field_96367_a.setDoInput(true);
         this.field_96367_a.setDoOutput(true);
         this.field_96367_a.setUseCaches(false);
         this.field_96367_a.setRequestMethod("GET");
         return this;
      } catch (Exception var2) {
         throw new ExceptionMcoHttp("Failed URL: " + this.field_96365_b, var2);
      }
   }
}
