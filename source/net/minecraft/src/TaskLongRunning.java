package net.minecraft.src;

import net.minecraft.client.Minecraft;

public abstract class TaskLongRunning implements Runnable {
   protected GuiScreenLongRunningTask field_96579_b;

   public void func_96574_a(GuiScreenLongRunningTask var1) {
      this.field_96579_b = var1;
   }

   public void func_96575_a(String var1) {
      this.field_96579_b.func_96209_a(var1);
   }

   public void func_96576_b(String var1) {
      this.field_96579_b.func_96210_b(var1);
   }

   public Minecraft func_96578_b() {
      return this.field_96579_b.func_96208_g();
   }

   public boolean func_96577_c() {
      return this.field_96579_b.func_96207_h();
   }

   public void func_96573_a() {
   }

   public void func_96572_a(GuiButton var1) {
   }

   public void func_96571_d() {
   }
}
