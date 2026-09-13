package net.minecraft.src;

import btw.entity.mob.CowEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class ModelCow extends ModelQuadruped {
   private float headRotation;

   public ModelCow() {
      super(12, 0.0F);
      this.head = new ModelRenderer(this, 0, 0);
      this.head.addBox(-4.0F, -4.0F, -6.0F, 8, 8, 6, 0.0F);
      this.head.setRotationPoint(0.0F, 4.0F, -8.0F);
      this.head.setTextureOffset(22, 0).addBox(-5.0F, -5.0F, -4.0F, 1, 3, 1, 0.0F);
      this.head.setTextureOffset(22, 0).addBox(4.0F, -5.0F, -4.0F, 1, 3, 1, 0.0F);
      this.body = new ModelRenderer(this, 18, 4);
      this.body.addBox(-6.0F, -10.0F, -7.0F, 12, 18, 10, 0.0F);
      this.body.setRotationPoint(0.0F, 5.0F, 2.0F);
      this.body.setTextureOffset(52, 0).addBox(-2.0F, 2.0F, -8.0F, 4, 6, 1);
      this.leg1.rotationPointX--;
      this.leg2.rotationPointX++;
      this.leg1.rotationPointZ += 0.0F;
      this.leg2.rotationPointZ += 0.0F;
      this.leg3.rotationPointX--;
      this.leg4.rotationPointX++;
      this.leg3.rotationPointZ--;
      this.leg4.rotationPointZ--;
      this.field_78151_h += 2.0F;
   }

   @Override
   public void setLivingAnimations(EntityLiving entity, float par2, float par3, float fPartialTick) {
      super.a(entity, par2, par3, fPartialTick);
      CowEntity cow = (CowEntity)entity;
      this.head.rotationPointY = 4.0F + cow.getGrazeHeadVerticalOffset(fPartialTick) * 4.0F;
      this.headRotation = cow.getGrazeHeadRotation(fPartialTick);
   }

   @Override
   public void setRotationAngles(float par1, float par2, float par3, float par4, float par5, float par6, Entity entity) {
      super.setRotationAngles(par1, par2, par3, par4, par5, par6, entity);
      this.adjustRotationAnglesForKickAttack(par1, par2, par3, par4, par5, par6, entity);
      this.head.rotateAngleX = this.headRotation;
   }

   private void adjustRotationAnglesForKickAttack(float par1, float par2, float par3, float par4, float par5, float par6, Entity entity) {
      CowEntity cow = (CowEntity)entity;
      if (cow != null) {
         int iAttackProgressCounter = cow.kickAttackInProgressCounter;
         if (iAttackProgressCounter > 0) {
            float fRotationFactor = 2.0F;
            if (cow.kickAttackLegUsed == 0) {
               this.leg1.rotateAngleX = MathHelper.cos((float) Math.PI * fRotationFactor) * 1.4F;
            } else {
               this.leg2.rotateAngleX = MathHelper.cos((float) Math.PI * fRotationFactor) * 1.4F;
            }
         }
      }
   }
}
