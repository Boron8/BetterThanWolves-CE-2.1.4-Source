package btw.entity.model;

import btw.entity.mob.PigEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.ModelPig;

@Environment(EnvType.CLIENT)
public class PigModel extends ModelPig {
   private float headRotation;

   public PigModel() {
      this(0.0F);
   }

   public PigModel(float fScaleFactor) {
      super(fScaleFactor);
   }

   @Override
   public void setLivingAnimations(EntityLiving entity, float par2, float par3, float fPartialTick) {
      super.a(entity, par2, par3, fPartialTick);
      PigEntity pig = (PigEntity)entity;
      if (!pig.h_()) {
         this.head.rotationPointY = 12.0F + pig.getGrazeHeadVerticalOffset(fPartialTick) * 4.0F;
      } else {
         this.head.rotationPointY = 12.0F + pig.getGrazeHeadVerticalOffset(fPartialTick) * 2.0F;
      }

      this.headRotation = pig.getGrazeHeadRotation(fPartialTick);
   }

   @Override
   public void setRotationAngles(float par1, float par2, float par3, float par4, float par5, float par6, Entity entity) {
      super.a(par1, par2, par3, par4, par5, par6, entity);
      this.head.rotateAngleX = this.headRotation;
   }
}
