package net.minecraft.src;

import com.prupe.mcpatcher.mal.resource.FakeResourceLocation;
import com.prupe.mcpatcher.mob.MobRandomizer;
import java.util.Random;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.lwjgl.opengl.GL11;

@Environment(EnvType.CLIENT)
public class RenderEnderman extends RenderLiving {
   private ModelEnderman endermanModel;
   private Random rnd = new Random();

   public RenderEnderman() {
      super(new ModelEnderman(), 0.5F);
      this.endermanModel = (ModelEnderman)super.mainModel;
      this.a(this.endermanModel);
   }

   public void renderEnderman(EntityEnderman par1EntityEnderman, double par2, double par4, double par6, float par8, float par9) {
      this.endermanModel.isCarrying = par1EntityEnderman.getCarried() > 0;
      this.endermanModel.isAttacking = par1EntityEnderman.isScreaming();
      if (par1EntityEnderman.isScreaming()) {
         double var10 = 0.02;
         par2 += this.rnd.nextGaussian() * var10;
         par6 += this.rnd.nextGaussian() * var10;
      }

      super.doRenderLiving(par1EntityEnderman, par2, par4, par6, par8, par9);
   }

   protected void renderCarrying(EntityEnderman par1EntityEnderman, float par2) {
      super.renderEquippedItems(par1EntityEnderman, par2);
      if (par1EntityEnderman.getCarried() > 0) {
         GL11.glEnable(32826);
         GL11.glPushMatrix();
         float var3 = 0.5F;
         GL11.glTranslatef(0.0F, 0.6875F, -0.75F);
         var3 *= 1.0F;
         GL11.glRotatef(20.0F, 1.0F, 0.0F, 0.0F);
         GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
         GL11.glScalef(-var3, -var3, var3);
         int var4 = par1EntityEnderman.b(par2);
         int var5 = var4 % 65536;
         int var6 = var4 / 65536;
         OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, var5 / 1.0F, var6 / 1.0F);
         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
         this.a("/terrain.png");
         this.renderBlocks.renderBlockAsItem(Block.blocksList[par1EntityEnderman.getCarried()], par1EntityEnderman.getCarryingData(), 1.0F);
         GL11.glPopMatrix();
         GL11.glDisable(32826);
      }
   }

   protected int renderEyes(EntityEnderman par1EntityEnderman, int par2, float par3) {
      if (par2 != 0) {
         return -1;
      } else {
         this.a(FakeResourceLocation.unwrap(MobRandomizer.randomTexture((Entity)par1EntityEnderman, FakeResourceLocation.wrap("/mob/enderman_eyes.png"))));
         float var4 = 1.0F;
         GL11.glEnable(3042);
         GL11.glDisable(3008);
         GL11.glBlendFunc(1, 1);
         GL11.glDisable(2896);
         if (par1EntityEnderman.ai()) {
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
         GL11.glColor4f(1.0F, 1.0F, 1.0F, var4);
         return 1;
      }
   }

   @Override
   protected int shouldRenderPass(EntityLiving par1EntityLiving, int par2, float par3) {
      return this.renderEyes((EntityEnderman)par1EntityLiving, par2, par3);
   }

   @Override
   protected void renderEquippedItems(EntityLiving par1EntityLiving, float par2) {
      this.renderCarrying((EntityEnderman)par1EntityLiving, par2);
   }

   @Override
   public void doRenderLiving(EntityLiving par1EntityLiving, double par2, double par4, double par6, float par8, float par9) {
      this.renderEnderman((EntityEnderman)par1EntityLiving, par2, par4, par6, par8, par9);
   }

   @Override
   public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9) {
      this.renderEnderman((EntityEnderman)par1Entity, par2, par4, par6, par8, par9);
   }
}
