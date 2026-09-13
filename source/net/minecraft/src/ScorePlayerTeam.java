package net.minecraft.src;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class ScorePlayerTeam {
   private final Scoreboard theScoreboard;
   private final String field_96675_b;
   private final Set membershipSet = new HashSet();
   private String field_96673_d;
   private String field_96674_e = "";
   private String field_96671_f = "";
   private boolean field_96672_g = true;
   private boolean field_98301_h = true;

   public ScorePlayerTeam(Scoreboard var1, String var2) {
      this.theScoreboard = var1;
      this.field_96675_b = var2;
      this.field_96673_d = var2;
   }

   public String func_96661_b() {
      return this.field_96675_b;
   }

   public String func_96669_c() {
      return this.field_96673_d;
   }

   public void func_96664_a(String var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("Name cannot be null");
      } else {
         this.field_96673_d = var1;
         this.theScoreboard.func_96538_b(this);
      }
   }

   public Collection getMembershipCollection() {
      return this.membershipSet;
   }

   public String func_96668_e() {
      return this.field_96674_e;
   }

   public void func_96666_b(String var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("Prefix cannot be null");
      } else {
         this.field_96674_e = var1;
         this.theScoreboard.func_96538_b(this);
      }
   }

   public String func_96663_f() {
      return this.field_96671_f;
   }

   public void func_96662_c(String var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("Suffix cannot be null");
      } else {
         this.field_96671_f = var1;
         this.theScoreboard.func_96538_b(this);
      }
   }

   public static String func_96667_a(ScorePlayerTeam var0, String var1) {
      return var0 == null ? var1 : var0.func_96668_e() + var1 + var0.func_96663_f();
   }

   public boolean func_96665_g() {
      return this.field_96672_g;
   }

   public void func_96660_a(boolean var1) {
      this.field_96672_g = var1;
      this.theScoreboard.func_96538_b(this);
   }

   public boolean func_98297_h() {
      return this.field_98301_h;
   }

   public void func_98300_b(boolean var1) {
      this.field_98301_h = var1;
      this.theScoreboard.func_96538_b(this);
   }

   public int func_98299_i() {
      int var1 = 0;
      int var2 = 0;
      if (this.func_96665_g()) {
         var1 |= 1 << var2++;
      }

      if (this.func_98297_h()) {
         var1 |= 1 << var2++;
      }

      return var1;
   }

   public void func_98298_a(int var1) {
      int var2 = 0;
      this.func_96660_a((var1 & 1 << var2++) > 0);
      this.func_98300_b((var1 & 1 << var2++) > 0);
   }
}
