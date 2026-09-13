package btw.entity.model;

import btw.entity.mob.ChickenEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.ModelChicken;

@Environment(EnvType.CLIENT)
public class ChickenModel extends ModelChicken {
   private float headRotation;

   @Override
   public void setLivingAnimations(EntityLiving entity, float par2, float par3, float fPartialTick) {
      super.a(entity, par2, par3, fPartialTick);
      ChickenEntity chicken = (ChickenEntity)entity;
      if (!chicken.h_()) {
         this.head.rotationPointY = 15.0F + chicken.getGrazeHeadVerticalOffset(fPartialTick) * 3.0F;
      } else {
         this.head.rotationPointY = 15.0F + chicken.getGrazeHeadVerticalOffset(fPartialTick) * 1.5F;
      }

      this.bill.rotationPointY = this.chin.rotationPointY = this.head.rotationPointY;
      this.headRotation = chicken.getGrazeHeadRotation(fPartialTick);
   }

   @Override
   public void setRotationAngles(float par1, float par2, float par3, float par4, float par5, float par6, Entity entity) {
      super.setRotationAngles(par1, par2, par3, par4, par5, par6, entity);
      this.head.rotateAngleX = this.bill.rotateAngleX = this.chin.rotateAngleX = this.headRotation;
   }
}
