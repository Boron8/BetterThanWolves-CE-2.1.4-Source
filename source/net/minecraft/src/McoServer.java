package net.minecraft.src;

import argo.jdom.JdomParser;
import argo.jdom.JsonNode;
import argo.saj.InvalidSyntaxException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

public class McoServer extends ValueObject {
   public long field_96408_a;
   public String field_96406_b;
   public String field_96407_c;
   public String field_96404_d;
   public String field_96405_e;
   public List field_96402_f;
   public String field_96403_g;
   public boolean field_98166_h;
   public int field_104063_i;
   public int field_96415_h;
   public String field_96413_j = "";
   public String field_96414_k = "";
   public boolean field_96411_l;
   public boolean field_102022_m = false;
   public long field_96412_m;
   private String field_96409_n = null;
   private String field_96410_o = null;

   public String func_96397_a() {
      if (this.field_96409_n == null) {
         try {
            this.field_96409_n = URLDecoder.decode(this.field_96407_c, "UTF-8");
         } catch (UnsupportedEncodingException var2) {
            this.field_96409_n = this.field_96407_c;
         }
      }

      return this.field_96409_n;
   }

   public String func_96398_b() {
      if (this.field_96410_o == null) {
         try {
            this.field_96410_o = URLDecoder.decode(this.field_96406_b, "UTF-8");
         } catch (UnsupportedEncodingException var2) {
            this.field_96410_o = this.field_96406_b;
         }
      }

      return this.field_96410_o;
   }

   public void func_96399_a(String var1) {
      this.field_96406_b = var1;
      this.field_96410_o = null;
   }

   public void func_96400_b(String var1) {
      this.field_96407_c = var1;
      this.field_96409_n = null;
   }

   public void func_96401_a(McoServer var1) {
      this.field_96414_k = var1.field_96414_k;
      this.field_96413_j = var1.field_96413_j;
      this.field_96412_m = var1.field_96412_m;
      this.field_96411_l = var1.field_96411_l;
      this.field_96415_h = var1.field_96415_h;
      this.field_102022_m = true;
   }

   public static McoServer func_98163_a(JsonNode var0) {
      McoServer var1 = new McoServer();

      try {
         var1.field_96408_a = Long.parseLong(var0.getNumberValue("id"));
         var1.field_96406_b = var0.getStringValue("name");
         var1.field_96407_c = var0.getStringValue("motd");
         var1.field_96404_d = var0.getStringValue("state");
         var1.field_96405_e = var0.getStringValue("owner");
         if (var0.isArrayNode("invited")) {
            var1.field_96402_f = func_98164_a(var0.getArrayNode("invited"));
         } else {
            var1.field_96402_f = new ArrayList();
         }

         var1.field_104063_i = Integer.parseInt(var0.getNumberValue("daysLeft"));
         var1.field_96403_g = var0.getStringValue("ip");
         var1.field_98166_h = var0.getBooleanValue("expired");
      } catch (IllegalArgumentException var3) {
      }

      return var1;
   }

   private static List func_98164_a(List var0) {
      ArrayList var1 = new ArrayList();

      for (JsonNode var3 : var0) {
         var1.add(var3.getStringValue());
      }

      return var1;
   }

   public static McoServer func_98165_c(String var0) {
      McoServer var1 = new McoServer();

      try {
         var1 = func_98163_a(new JdomParser().parse(var0));
      } catch (InvalidSyntaxException var3) {
      }

      return var1;
   }
}
