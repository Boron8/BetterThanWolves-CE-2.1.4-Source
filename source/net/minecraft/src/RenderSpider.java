package net.minecraft.src;

import com.prupe.mcpatcher.mal.resource.FakeResourceLocation;
import com.prupe.mcpatcher.mob.MobRandomizer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.lwjgl.opengl.GL11;

@Environment(EnvType.CLIENT)
public class RenderSpider extends RenderLiving {
   public RenderSpider() {
      super(new ModelSpider(), 1.0F);
      this.a(new ModelSpider());
   }

   protected float setSpiderDeathMaxRotation(EntitySpider par1EntitySpider) {
      return 180.0F;
   }

   protected int setSpiderEyeBrightness(EntitySpider par1EntitySpider, int par2, float par3) {
      if (par2 != 0) {
         return -1;
      } else {
         this.a(FakeResourceLocation.unwrap(MobRandomizer.randomTexture((Entity)par1EntitySpider, FakeResourceLocation.wrap("/mob/spider_eyes.png"))));
         float var4 = 1.0F;
         GL11.glEnable(3042);
         GL11.glDisable(3008);
         GL11.glBlendFunc(1, 1);
         if (par1EntitySpider.ai()) {
            GL11.glDepthMask(false);
         } else {
            GL11.glDepthMask(true);
         }

         char var5 = '\uf0f0';
         int var6 = var5 % 65536;
         int var7 = var5 / 65536;
         OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, var6 / 1.0F, var7 / 1.0F);
         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
         GL11.glColor4f(1.0F, 1.0F, 1.0F, var4);
         return 1;
      }
   }

   protected void scaleSpider(EntitySpider par1EntitySpider, float par2) {
      float var3 = par1EntitySpider.spiderScaleAmount();
      GL11.glScalef(var3, var3, var3);
   }

   @Override
   protected void preRenderCallback(EntityLiving par1EntityLiving, float par2) {
      this.scaleSpider((EntitySpider)par1EntityLiving, par2);
   }

   @Override
   protected float getDeathMaxRotation(EntityLiving par1EntityLiving) {
      return this.setSpiderDeathMaxRotation((EntitySpider)par1EntityLiving);
   }

   @Override
   protected int shouldRenderPass(EntityLiving par1EntityLiving, int par2, float par3) {
      return this.setSpiderEyeBrightness((EntitySpider)par1EntityLiving, par2, par3);
   }
}
