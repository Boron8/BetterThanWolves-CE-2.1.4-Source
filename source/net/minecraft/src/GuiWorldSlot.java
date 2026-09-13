package net.minecraft.src;

import java.util.Date;

class GuiWorldSlot extends GuiSlot {
   public GuiWorldSlot(GuiSelectWorld var1) {
      super(var1.mc, var1.width, var1.height, 32, var1.height - 64, 36);
      this.parentWorldGui = var1;
   }

   @Override
   protected int getSize() {
      return GuiSelectWorld.getSize(this.parentWorldGui).size();
   }

   @Override
   protected void elementClicked(int var1, boolean var2) {
      GuiSelectWorld.onElementSelected(this.parentWorldGui, var1);
      boolean var3 = GuiSelectWorld.getSelectedWorld(this.parentWorldGui) >= 0 && GuiSelectWorld.getSelectedWorld(this.parentWorldGui) < this.getSize();
      GuiSelectWorld.getSelectButton(this.parentWorldGui).enabled = var3;
      GuiSelectWorld.getRenameButton(this.parentWorldGui).enabled = var3;
      GuiSelectWorld.getDeleteButton(this.parentWorldGui).enabled = var3;
      GuiSelectWorld.func_82312_f(this.parentWorldGui).enabled = var3;
      if (var2 && var3) {
         this.parentWorldGui.selectWorld(var1);
      }
   }

   @Override
   protected boolean isSelected(int var1) {
      return var1 == GuiSelectWorld.getSelectedWorld(this.parentWorldGui);
   }

   @Override
   protected int getContentHeight() {
      return GuiSelectWorld.getSize(this.parentWorldGui).size() * 36;
   }

   @Override
   protected void drawBackground() {
      this.parentWorldGui.e();
   }

   @Override
   protected void drawSlot(int var1, int var2, int var3, int var4, Tessellator var5) {
      SaveFormatComparator var6 = (SaveFormatComparator)GuiSelectWorld.getSize(this.parentWorldGui).get(var1);
      String var7 = var6.getDisplayName();
      if (var7 == null || MathHelper.stringNullOrLengthZero(var7)) {
         var7 = GuiSelectWorld.func_82313_g(this.parentWorldGui) + " " + (var1 + 1);
      }

      String var8 = var6.getFileName();
      var8 = var8 + " (" + GuiSelectWorld.func_82315_h(this.parentWorldGui).format(new Date(var6.getLastTimePlayed()));
      var8 = var8 + ")";
      String var9 = "";
      if (var6.requiresConversion()) {
         var9 = GuiSelectWorld.func_82311_i(this.parentWorldGui) + " " + var9;
      } else {
         var9 = GuiSelectWorld.func_82314_j(this.parentWorldGui)[var6.getEnumGameType().getID()];
         if (var6.isHardcoreModeEnabled()) {
            var9 = EnumChatFormatting.DARK_RED + StatCollector.translateToLocal("gameMode.hardcore") + EnumChatFormatting.RESET;
         }

         if (var6.getCheatsEnabled()) {
            var9 = var9 + ", " + StatCollector.translateToLocal("selectWorld.cheats");
         }
      }

      this.parentWorldGui.b(this.parentWorldGui.fontRenderer, var7, var2 + 2, var3 + 1, 16777215);
      this.parentWorldGui.b(this.parentWorldGui.fontRenderer, var8, var2 + 2, var3 + 12, 8421504);
      this.parentWorldGui.b(this.parentWorldGui.fontRenderer, var9, var2 + 2, var3 + 12 + 10, 8421504);
   }
}
