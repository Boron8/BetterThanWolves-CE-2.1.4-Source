package net.minecraft.src;

import com.prupe.mcpatcher.cit.CITUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.lwjgl.opengl.GL11;

@Environment(EnvType.CLIENT)
public class RenderSnowball extends Render {
   private Item field_94151_a;
   private int field_94150_f;

   public RenderSnowball(Item par1, int par2) {
      this.field_94151_a = par1;
      this.field_94150_f = par2;
   }

   public RenderSnowball(Item par1) {
      this(par1, 0);
   }

   @Override
   public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9) {
      Icon var10 = CITUtils.getEntityIcon(this.field_94151_a.getIconFromDamage(this.field_94150_f), par1Entity);
      if (var10 != null) {
         GL11.glPushMatrix();
         GL11.glTranslatef((float)par2, (float)par4, (float)par6);
         GL11.glEnable(32826);
         GL11.glScalef(0.5F, 0.5F, 0.5F);
         this.a("/gui/items.png");
         Tessellator var11 = Tessellator.instance;
         if (var10 == ItemPotion.func_94589_d("potion_splash")) {
            int var12 = PotionHelper.func_77915_a(((EntityPotion)par1Entity).getPotionDamage(), false);
            float var13 = (var12 >> 16 & 0xFF) / 255.0F;
            float var14 = (var12 >> 8 & 0xFF) / 255.0F;
            float var15 = (var12 & 0xFF) / 255.0F;
            GL11.glColor3f(var13, var14, var15);
            GL11.glPushMatrix();
            this.func_77026_a(var11, ItemPotion.func_94589_d("potion_contents"));
            GL11.glPopMatrix();
            GL11.glColor3f(1.0F, 1.0F, 1.0F);
         }

         this.func_77026_a(var11, var10);
         GL11.glDisable(32826);
         GL11.glPopMatrix();
      }
   }

   private void func_77026_a(Tessellator par1Tessellator, Icon par2Icon) {
      float var3 = par2Icon.getMinU();
      float var4 = par2Icon.getMaxU();
      float var5 = par2Icon.getMinV();
      float var6 = par2Icon.getMaxV();
      float var7 = 1.0F;
      float var8 = 0.5F;
      float var9 = 0.25F;
      GL11.glRotatef(180.0F - this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
      GL11.glRotatef(-this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
      par1Tessellator.startDrawingQuads();
      par1Tessellator.setNormal(0.0F, 1.0F, 0.0F);
      par1Tessellator.addVertexWithUV(0.0F - var8, 0.0F - var9, 0.0, var3, var6);
      par1Tessellator.addVertexWithUV(var7 - var8, 0.0F - var9, 0.0, var4, var6);
      par1Tessellator.addVertexWithUV(var7 - var8, var7 - var9, 0.0, var4, var5);
      par1Tessellator.addVertexWithUV(0.0F - var8, var7 - var9, 0.0, var3, var5);
      par1Tessellator.draw();
   }
}
