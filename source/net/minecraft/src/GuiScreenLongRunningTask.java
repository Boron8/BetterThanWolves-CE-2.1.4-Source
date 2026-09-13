package net.minecraft.src;

import java.util.ArrayList;
import java.util.Collections;
import net.minecraft.client.Minecraft;

public class GuiScreenLongRunningTask extends GuiScreen {
   private final int field_96213_b = 666;
   private final GuiScreen field_96215_c;
   private final Thread field_98118_d;
   private volatile String field_96212_d = "";
   private volatile boolean field_96219_n = false;
   private volatile String field_96220_o;
   private volatile boolean field_96218_p = false;
   private int field_96216_q = 0;
   private TaskLongRunning field_96214_r;
   public static final String[] field_96217_a = new String[]{
      "\u2583 \u2584 \u2585 \u2586 \u2587 \u2588 \u2587 \u2586 \u2585 \u2584 \u2583",
      "_ \u2583 \u2584 \u2585 \u2586 \u2587 \u2588 \u2587 \u2586 \u2585 \u2584",
      "_ _ \u2583 \u2584 \u2585 \u2586 \u2587 \u2588 \u2587 \u2586 \u2585",
      "_ _ _ \u2583 \u2584 \u2585 \u2586 \u2587 \u2588 \u2587 \u2586",
      "_ _ _ _ \u2583 \u2584 \u2585 \u2586 \u2587 \u2588 \u2587",
      "_ _ _ _ _ \u2583 \u2584 \u2585 \u2586 \u2587 \u2588",
      "_ _ _ _ \u2583 \u2584 \u2585 \u2586 \u2587 \u2588 \u2587",
      "_ _ _ \u2583 \u2584 \u2585 \u2586 \u2587 \u2588 \u2587 \u2586",
      "_ _ \u2583 \u2584 \u2585 \u2586 \u2587 \u2588 \u2587 \u2586 \u2585",
      "_ \u2583 \u2584 \u2585 \u2586 \u2587 \u2588 \u2587 \u2586 \u2585 \u2584",
      "\u2583 \u2584 \u2585 \u2586 \u2587 \u2588 \u2587 \u2586 \u2585 \u2584 \u2583",
      "\u2584 \u2585 \u2586 \u2587 \u2588 \u2587 \u2586 \u2585 \u2584 \u2583 _",
      "\u2585 \u2586 \u2587 \u2588 \u2587 \u2586 \u2585 \u2584 \u2583 _ _",
      "\u2586 \u2587 \u2588 \u2587 \u2586 \u2585 \u2584 \u2583 _ _ _",
      "\u2587 \u2588 \u2587 \u2586 \u2585 \u2584 \u2583 _ _ _ _",
      "\u2588 \u2587 \u2586 \u2585 \u2584 \u2583 _ _ _ _ _",
      "\u2587 \u2588 \u2587 \u2586 \u2585 \u2584 \u2583 _ _ _ _",
      "\u2586 \u2587 \u2588 \u2587 \u2586 \u2585 \u2584 \u2583 _ _ _",
      "\u2585 \u2586 \u2587 \u2588 \u2587 \u2586 \u2585 \u2584 \u2583 _ _",
      "\u2584 \u2585 \u2586 \u2587 \u2588 \u2587 \u2586 \u2585 \u2584 \u2583 _"
   };

   public GuiScreenLongRunningTask(Minecraft var1, GuiScreen var2, TaskLongRunning var3) {
      super.buttonList = Collections.synchronizedList(new ArrayList());
      this.mc = var1;
      this.field_96215_c = var2;
      this.field_96214_r = var3;
      var3.func_96574_a(this);
      this.field_98118_d = new Thread(var3);
   }

   public void func_98117_g() {
      this.field_98118_d.start();
   }

   @Override
   public void updateScreen() {
      super.updateScreen();
      this.field_96216_q++;
      this.field_96214_r.func_96573_a();
   }

   @Override
   protected void keyTyped(char var1, int var2) {
   }

   @Override
   public void initGui() {
      this.field_96214_r.func_96571_d();
   }

   @Override
   protected void actionPerformed(GuiButton var1) {
      if (var1.id == 666) {
         this.field_96218_p = true;
         this.mc.displayGuiScreen(this.field_96215_c);
      }

      this.field_96214_r.func_96572_a(var1);
   }

   @Override
   public void drawScreen(int var1, int var2, float var3) {
      this.e();
      this.a(this.fontRenderer, this.field_96212_d, this.width / 2, this.height / 2 - 50, 16777215);
      this.a(this.fontRenderer, "", this.width / 2, this.height / 2 - 10, 16777215);
      if (!this.field_96219_n) {
         this.a(this.fontRenderer, field_96217_a[this.field_96216_q % field_96217_a.length], this.width / 2, this.height / 2 + 15, 8421504);
      }

      if (this.field_96219_n) {
         this.a(this.fontRenderer, this.field_96220_o, this.width / 2, this.height / 2 + 15, 16711680);
      }

      super.drawScreen(var1, var2, var3);
   }

   public void func_96209_a(String var1) {
      this.field_96219_n = true;
      this.field_96220_o = var1;
      this.buttonList.clear();
      this.buttonList.add(new GuiButton(666, this.width / 2 - 100, this.height / 4 + 120 + 12, "Back"));
   }

   public Minecraft func_96208_g() {
      return this.mc;
   }

   public void func_96210_b(String var1) {
      this.field_96212_d = var1;
   }

   public boolean func_96207_h() {
      return this.field_96218_p;
   }
}
