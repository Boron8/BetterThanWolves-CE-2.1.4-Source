package net.minecraft.src;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.lwjgl.input.Mouse;

abstract class GuiSlotStats extends GuiSlot {
   protected int field_77262_g;
   protected List field_77266_h;
   protected Comparator field_77267_i;
   protected int field_77264_j;
   protected int field_77265_k;

   protected GuiSlotStats(GuiStats var1) {
      super(GuiStats.getMinecraft1(var1), var1.width, var1.height, 32, var1.height - 64, 20);
      this.statsGui = var1;
      this.field_77262_g = -1;
      this.field_77264_j = -1;
      this.field_77265_k = 0;
      this.a(false);
      this.a(true, 20);
   }

   @Override
   protected void elementClicked(int var1, boolean var2) {
   }

   @Override
   protected boolean isSelected(int var1) {
      return false;
   }

   @Override
   protected void drawBackground() {
      this.statsGui.e();
   }

   @Override
   protected void func_77222_a(int var1, int var2, Tessellator var3) {
      if (!Mouse.isButtonDown(0)) {
         this.field_77262_g = -1;
      }

      if (this.field_77262_g == 0) {
         GuiStats.drawSprite(this.statsGui, var1 + 115 - 18, var2 + 1, 0, 0);
      } else {
         GuiStats.drawSprite(this.statsGui, var1 + 115 - 18, var2 + 1, 0, 18);
      }

      if (this.field_77262_g == 1) {
         GuiStats.drawSprite(this.statsGui, var1 + 165 - 18, var2 + 1, 0, 0);
      } else {
         GuiStats.drawSprite(this.statsGui, var1 + 165 - 18, var2 + 1, 0, 18);
      }

      if (this.field_77262_g == 2) {
         GuiStats.drawSprite(this.statsGui, var1 + 215 - 18, var2 + 1, 0, 0);
      } else {
         GuiStats.drawSprite(this.statsGui, var1 + 215 - 18, var2 + 1, 0, 18);
      }

      if (this.field_77264_j != -1) {
         short var4 = 79;
         byte var5 = 18;
         if (this.field_77264_j == 1) {
            var4 = 129;
         } else if (this.field_77264_j == 2) {
            var4 = 179;
         }

         if (this.field_77265_k == 1) {
            var5 = 36;
         }

         GuiStats.drawSprite(this.statsGui, var1 + var4, var2 + 1, var5, 0);
      }
   }

   @Override
   protected void func_77224_a(int var1, int var2) {
      this.field_77262_g = -1;
      if (var1 >= 79 && var1 < 115) {
         this.field_77262_g = 0;
      } else if (var1 >= 129 && var1 < 165) {
         this.field_77262_g = 1;
      } else if (var1 >= 179 && var1 < 215) {
         this.field_77262_g = 2;
      }

      if (this.field_77262_g >= 0) {
         this.func_77261_e(this.field_77262_g);
         GuiStats.getMinecraft2(this.statsGui).sndManager.playSoundFX("random.click", 1.0F, 1.0F);
      }
   }

   @Override
   protected final int getSize() {
      return this.field_77266_h.size();
   }

   protected final StatCrafting func_77257_d(int var1) {
      return (StatCrafting)this.field_77266_h.get(var1);
   }

   protected abstract String func_77258_c(int var1);

   protected void func_77260_a(StatCrafting var1, int var2, int var3, boolean var4) {
      if (var1 != null) {
         String var5 = var1.a(GuiStats.getStatsFileWriter(this.statsGui).writeStat(var1));
         this.statsGui
            .b(
               GuiStats.getFontRenderer4(this.statsGui),
               var5,
               var2 - GuiStats.getFontRenderer5(this.statsGui).getStringWidth(var5),
               var3 + 5,
               var4 ? 16777215 : 9474192
            );
      } else {
         String var6 = "-";
         this.statsGui
            .b(
               GuiStats.getFontRenderer6(this.statsGui),
               var6,
               var2 - GuiStats.getFontRenderer7(this.statsGui).getStringWidth(var6),
               var3 + 5,
               var4 ? 16777215 : 9474192
            );
      }
   }

   @Override
   protected void func_77215_b(int var1, int var2) {
      if (var2 >= this.top && var2 <= this.bottom) {
         int var3 = this.c(var1, var2);
         int var4 = this.statsGui.width / 2 - 92 - 16;
         if (var3 >= 0) {
            if (var1 < var4 + 40 || var1 > var4 + 40 + 20) {
               return;
            }

            StatCrafting var11 = this.func_77257_d(var3);
            this.func_77259_a(var11, var1, var2);
         } else {
            String var5 = "";
            if (var1 >= var4 + 115 - 18 && var1 <= var4 + 115) {
               var5 = this.func_77258_c(0);
            } else if (var1 >= var4 + 165 - 18 && var1 <= var4 + 165) {
               var5 = this.func_77258_c(1);
            } else {
               if (var1 < var4 + 215 - 18 || var1 > var4 + 215) {
                  return;
               }

               var5 = this.func_77258_c(2);
            }

            var5 = ("" + StringTranslate.getInstance().translateKey(var5)).trim();
            if (var5.length() > 0) {
               int var6 = var1 + 12;
               int var7 = var2 - 12;
               int var8 = GuiStats.getFontRenderer8(this.statsGui).getStringWidth(var5);
               GuiStats.drawGradientRect(this.statsGui, var6 - 3, var7 - 3, var6 + var8 + 3, var7 + 8 + 3, -1073741824, -1073741824);
               GuiStats.getFontRenderer9(this.statsGui).drawStringWithShadow(var5, var6, var7, -1);
            }
         }
      }
   }

   protected void func_77259_a(StatCrafting var1, int var2, int var3) {
      if (var1 != null) {
         Item var4 = Item.itemsList[var1.getItemID()];
         String var5 = ("" + StringTranslate.getInstance().translateNamedKey(var4.getUnlocalizedName())).trim();
         if (var5.length() > 0) {
            int var6 = var2 + 12;
            int var7 = var3 - 12;
            int var8 = GuiStats.getFontRenderer10(this.statsGui).getStringWidth(var5);
            GuiStats.drawGradientRect1(this.statsGui, var6 - 3, var7 - 3, var6 + var8 + 3, var7 + 8 + 3, -1073741824, -1073741824);
            GuiStats.getFontRenderer11(this.statsGui).drawStringWithShadow(var5, var6, var7, -1);
         }
      }
   }

   protected void func_77261_e(int var1) {
      if (var1 != this.field_77264_j) {
         this.field_77264_j = var1;
         this.field_77265_k = -1;
      } else if (this.field_77265_k == -1) {
         this.field_77265_k = 1;
      } else {
         this.field_77264_j = -1;
         this.field_77265_k = 0;
      }

      Collections.sort(this.field_77266_h, this.field_77267_i);
   }
}
