package net.minecraft.src;

import btw.entity.mob.CowEntity;
import btw.entity.model.CowUdderModel;
import com.prupe.mcpatcher.mal.resource.FakeResourceLocation;
import com.prupe.mcpatcher.mob.MobOverlay;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.lwjgl.opengl.GL11;

@Environment(EnvType.CLIENT)
public class RenderMooshroom extends RenderLiving {
   CowUdderModel modelUdder = new CowUdderModel();

   public RenderMooshroom(ModelBase par1ModelBase, float par2) {
      super(par1ModelBase, par2);
      this.a(this.modelUdder);
   }

   public void renderLivingMooshroom(EntityMooshroom par1EntityMooshroom, double par2, double par4, double par6, float par8, float par9) {
      super.doRenderLiving(par1EntityMooshroom, par2, par4, par6, par8, par9);
   }

   protected void renderMooshroomEquippedItems(EntityMooshroom par1EntityMooshroom, float par2) {
      super.renderEquippedItems(par1EntityMooshroom, par2);
      if (par1EntityMooshroom.h_()) {
         MobOverlay.finishMooshroom();
      } else if (!par1EntityMooshroom.getWearingBreedingHarness() && par1EntityMooshroom.isFullyFed()) {
         this.a(FakeResourceLocation.unwrap(MobOverlay.setupMooshroom(par1EntityMooshroom, FakeResourceLocation.wrap("/terrain.png"))));
         GL11.glEnable(2884);
         GL11.glPushMatrix();
         GL11.glScalef(1.0F, -1.0F, 1.0F);
         GL11.glTranslatef(0.2F, 0.4F, 0.5F);
         GL11.glRotatef(42.0F, 0.0F, 1.0F, 0.0F);
         if (!MobOverlay.renderMooshroomOverlay(0.0)) {
            this.renderBlocks.renderBlockAsItem(Block.mushroomRed, 0, 1.0F);
         }

         GL11.glTranslatef(0.1F, 0.0F, -0.6F);
         GL11.glRotatef(42.0F, 0.0F, 1.0F, 0.0F);
         if (!par1EntityMooshroom.isStarving() && !MobOverlay.renderMooshroomOverlay(0.0)) {
            this.renderBlocks.renderBlockAsItem(Block.mushroomRed, 0, 1.0F);
         }

         GL11.glPopMatrix();
         GL11.glPushMatrix();
         ((ModelQuadruped)this.mainModel).head.postRender(0.0625F);
         GL11.glScalef(1.0F, -1.0F, 1.0F);
         GL11.glTranslatef(0.0F, 0.75F, -0.2F);
         GL11.glRotatef(12.0F, 0.0F, 1.0F, 0.0F);
         if (par1EntityMooshroom.isFullyFed() && !MobOverlay.renderMooshroomOverlay(0.0)) {
            this.renderBlocks.renderBlockAsItem(Block.mushroomRed, 0, 1.0F);
         }

         GL11.glPopMatrix();
         GL11.glDisable(2884);
         MobOverlay.finishMooshroom();
      }
   }

   @Override
   protected void renderEquippedItems(EntityLiving par1EntityLiving, float par2) {
      this.renderMooshroomEquippedItems((EntityMooshroom)par1EntityLiving, par2);
   }

   @Override
   public void doRenderLiving(EntityLiving par1EntityLiving, double par2, double par4, double par6, float par8, float par9) {
      this.renderLivingMooshroom((EntityMooshroom)par1EntityLiving, par2, par4, par6, par8, par9);
   }

   @Override
   public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9) {
      this.renderLivingMooshroom((EntityMooshroom)par1Entity, par2, par4, par6, par8, par9);
   }

   @Override
   protected int shouldRenderPass(EntityLiving par1EntityLiving, int par2, float par3) {
      if (par2 == 0 && ((CowEntity)par1EntityLiving).gotMilk()) {
         this.a("/btwmodtex/cow_udder.png");
         return 1;
      } else {
         return -1;
      }
   }
}
