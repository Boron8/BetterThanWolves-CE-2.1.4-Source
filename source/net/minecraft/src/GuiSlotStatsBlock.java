package net.minecraft.src;

import java.util.ArrayList;

class GuiSlotStatsBlock extends GuiSlotStats {
   public GuiSlotStatsBlock(GuiStats var1) {
      super(var1);
      this.theStats = var1;
      this.field_77266_h = new ArrayList();

      for (StatCrafting var3 : StatList.objectMineStats) {
         boolean var4 = false;
         int var5 = var3.getItemID();
         if (GuiStats.getStatsFileWriter(var1).writeStat(var3) > 0) {
            var4 = true;
         } else if (StatList.objectUseStats[var5] != null && GuiStats.getStatsFileWriter(var1).writeStat(StatList.objectUseStats[var5]) > 0) {
            var4 = true;
         } else if (StatList.objectCraftStats[var5] != null && GuiStats.getStatsFileWriter(var1).writeStat(StatList.objectCraftStats[var5]) > 0) {
            var4 = true;
         }

         if (var4) {
            this.field_77266_h.add(var3);
         }
      }

      this.field_77267_i = new SorterStatsBlock(this, var1);
   }

   @Override
   protected void func_77222_a(int var1, int var2, Tessellator var3) {
      super.func_77222_a(var1, var2, var3);
      if (this.field_77262_g == 0) {
         GuiStats.drawSprite(this.theStats, var1 + 115 - 18 + 1, var2 + 1 + 1, 18, 18);
      } else {
         GuiStats.drawSprite(this.theStats, var1 + 115 - 18, var2 + 1, 18, 18);
      }

      if (this.field_77262_g == 1) {
         GuiStats.drawSprite(this.theStats, var1 + 165 - 18 + 1, var2 + 1 + 1, 36, 18);
      } else {
         GuiStats.drawSprite(this.theStats, var1 + 165 - 18, var2 + 1, 36, 18);
      }

      if (this.field_77262_g == 2) {
         GuiStats.drawSprite(this.theStats, var1 + 215 - 18 + 1, var2 + 1 + 1, 54, 18);
      } else {
         GuiStats.drawSprite(this.theStats, var1 + 215 - 18, var2 + 1, 54, 18);
      }
   }

   @Override
   protected void drawSlot(int var1, int var2, int var3, int var4, Tessellator var5) {
      StatCrafting var6 = this.d(var1);
      int var7 = var6.getItemID();
      GuiStats.drawItemSprite(this.theStats, var2 + 40, var3, var7);
      this.a((StatCrafting)StatList.objectCraftStats[var7], var2 + 115, var3, var1 % 2 == 0);
      this.a((StatCrafting)StatList.objectUseStats[var7], var2 + 165, var3, var1 % 2 == 0);
      this.a(var6, var2 + 215, var3, var1 % 2 == 0);
   }

   @Override
   protected String func_77258_c(int var1) {
      if (var1 == 0) {
         return "stat.crafted";
      } else {
         return var1 == 1 ? "stat.used" : "stat.mined";
      }
   }
}
