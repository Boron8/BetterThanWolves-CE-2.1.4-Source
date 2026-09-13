package net.minecraft.src;

import java.util.List;
import org.lwjgl.opengl.GL11;

class GuiTexturePackSlot extends GuiSlot {
   public GuiTexturePackSlot(GuiTexturePacks var1) {
      super(GuiTexturePacks.func_73950_a(var1), var1.width, var1.height, 32, var1.height - 55 + 4, 36);
      this.parentTexturePackGui = var1;
   }

   @Override
   protected int getSize() {
      return GuiTexturePacks.func_73955_b(this.parentTexturePackGui).texturePackList.availableTexturePacks().size();
   }

   @Override
   protected void elementClicked(int var1, boolean var2) {
      List var3 = GuiTexturePacks.func_73958_c(this.parentTexturePackGui).texturePackList.availableTexturePacks();

      try {
         GuiTexturePacks.func_73951_d(this.parentTexturePackGui).texturePackList.setTexturePack((ITexturePack)var3.get(var1));
         GuiTexturePacks.func_73952_e(this.parentTexturePackGui).renderEngine.refreshTextures();
         GuiTexturePacks.func_73962_f(this.parentTexturePackGui).renderGlobal.loadRenderers();
      } catch (Exception var5) {
         GuiTexturePacks.func_73959_g(this.parentTexturePackGui).texturePackList.setTexturePack((ITexturePack)var3.get(0));
         GuiTexturePacks.func_73957_h(this.parentTexturePackGui).renderEngine.refreshTextures();
         GuiTexturePacks.func_73956_i(this.parentTexturePackGui).renderGlobal.loadRenderers();
      }
   }

   @Override
   protected boolean isSelected(int var1) {
      List var2 = GuiTexturePacks.func_73953_j(this.parentTexturePackGui).texturePackList.availableTexturePacks();
      return GuiTexturePacks.func_73961_k(this.parentTexturePackGui).texturePackList.getSelectedTexturePack() == var2.get(var1);
   }

   @Override
   protected int getContentHeight() {
      return this.getSize() * 36;
   }

   @Override
   protected void drawBackground() {
      this.parentTexturePackGui.e();
   }

   @Override
   protected void drawSlot(int var1, int var2, int var3, int var4, Tessellator var5) {
      ITexturePack var6 = (ITexturePack)GuiTexturePacks.func_96143_l(this.parentTexturePackGui).texturePackList.availableTexturePacks().get(var1);
      var6.bindThumbnailTexture(GuiTexturePacks.func_96142_m(this.parentTexturePackGui).renderEngine);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      var5.startDrawingQuads();
      var5.setColorOpaque_I(16777215);
      var5.addVertexWithUV(var2, var3 + var4, 0.0, 0.0, 1.0);
      var5.addVertexWithUV(var2 + 32, var3 + var4, 0.0, 1.0, 1.0);
      var5.addVertexWithUV(var2 + 32, var3, 0.0, 1.0, 0.0);
      var5.addVertexWithUV(var2, var3, 0.0, 0.0, 0.0);
      var5.draw();
      String var7 = var6.getTexturePackFileName();
      if (!var6.isCompatible()) {
         var7 = EnumChatFormatting.DARK_RED + StatCollector.translateToLocal("texturePack.incompatible") + " - " + var7;
      }

      if (var7.length() > 32) {
         var7 = var7.substring(0, 32).trim() + "...";
      }

      this.parentTexturePackGui.b(GuiTexturePacks.func_73954_n(this.parentTexturePackGui), var7, var2 + 32 + 2, var3 + 1, 16777215);
      this.parentTexturePackGui.b(GuiTexturePacks.func_96145_o(this.parentTexturePackGui), var6.getFirstDescriptionLine(), var2 + 32 + 2, var3 + 12, 8421504);
      this.parentTexturePackGui
         .b(GuiTexturePacks.func_96144_p(this.parentTexturePackGui), var6.getSecondDescriptionLine(), var2 + 32 + 2, var3 + 12 + 10, 8421504);
   }
}
