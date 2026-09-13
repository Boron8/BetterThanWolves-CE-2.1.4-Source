package net.minecraft.src;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

public class EntityCaveSpider extends EntitySpider {
   public EntityCaveSpider(World par1World) {
      super(par1World);
      this.texture = "/mob/cavespider.png";
      this.a(0.7F, 0.5F);
   }

   @Override
   public int getMaxHealth() {
      return 12;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public float spiderScaleAmount() {
      return 0.7F;
   }

   @Override
   public boolean attackEntityAsMob(Entity par1Entity) {
      if (super.m(par1Entity)) {
         if (par1Entity instanceof EntityLiving) {
            byte var2 = 0;
            if (this.worldObj.difficultySetting > 1) {
               if (this.worldObj.difficultySetting == 2) {
                  var2 = 7;
               } else if (this.worldObj.difficultySetting == 3) {
                  var2 = 15;
               }
            }

            if (var2 > 0) {
               ((EntityLiving)par1Entity).addPotionEffect(new PotionEffect(Potion.poison.id, var2 * 20, 0));
            }
         }

         return true;
      } else {
         return false;
      }
   }

   @Override
   public void initCreature() {
   }
}
