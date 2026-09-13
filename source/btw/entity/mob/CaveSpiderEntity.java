package btw.entity.mob;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.Potion;
import net.minecraft.src.PotionEffect;
import net.minecraft.src.World;

public class CaveSpiderEntity extends SpiderEntity {
   public CaveSpiderEntity(World world) {
      super(world);
      this.texture = "/mob/cavespider.png";
      this.a(0.7F, 0.5F);
   }

   @Override
   public int getMaxHealth() {
      return 12;
   }

   @Override
   public boolean attackEntityAsMob(Entity target) {
      if (super.m(target)) {
         if (target instanceof EntityLiving) {
            ((EntityLiving)target).addPotionEffect(new PotionEffect(Potion.poison.id, 140, 0));
         }

         return true;
      } else {
         return false;
      }
   }

   @Override
   public void initCreature() {
   }

   @Override
   public boolean doesLightAffectAggessiveness() {
      return false;
   }

   @Override
   protected boolean dropsSpiderEyes() {
      return false;
   }

   @Override
   protected void checkForSpiderSkeletonMounting() {
   }

   @Environment(EnvType.CLIENT)
   @Override
   public float spiderScaleAmount() {
      return 0.7F;
   }
}
