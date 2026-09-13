package net.minecraft.src;

class SelectionListInvited extends SelectionListBase {
   public SelectionListInvited(GuiScreenConfigureWorld var1) {
      super(
         GuiScreenConfigureWorld.func_96265_a(var1),
         GuiScreenConfigureWorld.func_96271_b(var1),
         GuiScreenConfigureWorld.func_96274_a(var1, 2),
         GuiScreenConfigureWorld.func_96269_c(var1),
         GuiScreenConfigureWorld.func_96274_a(var1, 9) - GuiScreenConfigureWorld.func_96274_a(var1, 2),
         12
      );
      this.field_98264_a = var1;
   }

   @Override
   protected int func_96608_a() {
      return GuiScreenConfigureWorld.func_96266_d(this.field_98264_a).field_96402_f.size() + 1;
   }

   @Override
   protected void func_96615_a(int var1, boolean var2) {
      if (var1 < GuiScreenConfigureWorld.func_96266_d(this.field_98264_a).field_96402_f.size()) {
         GuiScreenConfigureWorld.func_96270_b(this.field_98264_a, var1);
      }
   }

   @Override
   protected boolean func_96609_a(int var1) {
      return var1 == GuiScreenConfigureWorld.func_96263_e(this.field_98264_a);
   }

   @Override
   protected int func_96613_b() {
      return this.func_96608_a() * 12;
   }

   @Override
   protected void func_96611_c() {
   }

   @Override
   protected void func_96610_a(int var1, int var2, int var3, int var4, Tessellator var5) {
      if (var1 < GuiScreenConfigureWorld.func_96266_d(this.field_98264_a).field_96402_f.size()) {
         this.func_98263_b(var1, var2, var3, var4, var5);
      }
   }

   private void func_98263_b(int var1, int var2, int var3, int var4, Tessellator var5) {
      String var6 = (String)GuiScreenConfigureWorld.func_96266_d(this.field_98264_a).field_96402_f.get(var1);
      this.field_98264_a.b(GuiScreenConfigureWorld.func_96273_f(this.field_98264_a), var6, var2 + 2, var3 + 1, 16777215);
   }
}
