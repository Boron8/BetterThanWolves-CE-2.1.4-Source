package btw.client.render.entity;

import btw.entity.mob.WolfEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.ModelBase;
import net.minecraft.src.OpenGlHelper;
import net.minecraft.src.RenderWolf;
import org.lwjgl.opengl.GL11;

@Environment(EnvType.CLIENT)
public class WolfRenderer extends RenderWolf {
   public WolfRenderer(ModelBase model, ModelBase modelOverlay, float fShadowSize) {
      super(model, modelOverlay, fShadowSize);
   }

   @Override
   protected int shouldRenderPass(EntityLiving entity, int iRenderPass, float par3) {
      return this.renderGlowingEyes((WolfEntity)entity, iRenderPass) ? 1 : super.shouldRenderPass(entity, iRenderPass, par3);
   }

   private boolean renderGlowingEyes(WolfEntity wolf, int iRenderPass) {
      if (iRenderPass == 2 && wolf.areEyesGlowing()) {
         this.a("/btwmodtex/fcWolfNothingToWorryAbout.png");
         GL11.glEnable(3042);
         GL11.glDisable(3008);
         GL11.glBlendFunc(1, 1);
         GL11.glDisable(2896);
         if (wolf.ai()) {
            GL11.glDepthMask(false);
         } else {
            GL11.glDepthMask(true);
         }

         char var5 = '\uf0f0';
         int var6 = var5 % 65536;
         int var7 = var5 / 65536;
         OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, var6 / 1.0F, var7 / 1.0F);
         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
         GL11.glEnable(2896);
         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
         return true;
      } else {
         return false;
      }
   }
}
