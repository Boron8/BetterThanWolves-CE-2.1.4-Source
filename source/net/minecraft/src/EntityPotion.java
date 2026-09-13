package net.minecraft.src;

import java.util.List;

public class EntityPotion extends EntityThrowable {
   private ItemStack potionDamage;

   public EntityPotion(World var1) {
      super(var1);
   }

   public EntityPotion(World var1, EntityLiving var2, int var3) {
      this(var1, var2, new ItemStack(Item.potion, 1, var3));
   }

   public EntityPotion(World var1, EntityLiving var2, ItemStack var3) {
      super(var1, var2);
      this.potionDamage = var3;
   }

   public EntityPotion(World var1, double var2, double var4, double var6, int var8) {
      this(var1, var2, var4, var6, new ItemStack(Item.potion, 1, var8));
   }

   public EntityPotion(World var1, double var2, double var4, double var6, ItemStack var8) {
      super(var1, var2, var4, var6);
      this.potionDamage = var8;
   }

   @Override
   protected float getGravityVelocity() {
      return 0.05F;
   }

   @Override
   protected float func_70182_d() {
      return 0.5F;
   }

   @Override
   protected float func_70183_g() {
      return -20.0F;
   }

   public void setPotionDamage(int var1) {
      if (this.potionDamage == null) {
         this.potionDamage = new ItemStack(Item.potion, 1, 0);
      }

      this.potionDamage.setItemDamage(var1);
   }

   public int getPotionDamage() {
      if (this.potionDamage == null) {
         this.potionDamage = new ItemStack(Item.potion, 1, 0);
      }

      return this.potionDamage.getItemDamage();
   }

   @Override
   protected void onImpact(MovingObjectPosition var1) {
      if (!this.worldObj.isRemote) {
         List var2 = Item.potion.getEffects(this.potionDamage);
         if (var2 != null && !var2.isEmpty()) {
            AxisAlignedBB var3 = this.boundingBox.expand(4.0, 2.0, 4.0);
            List var4 = this.worldObj.getEntitiesWithinAABB(EntityLiving.class, var3);
            if (var4 != null && !var4.isEmpty()) {
               for (EntityLiving var6 : var4) {
                  double var7 = this.e(var6);
                  if (var7 < 16.0) {
                     double var9 = 1.0 - Math.sqrt(var7) / 4.0;
                     if (var6 == var1.entityHit) {
                        var9 = 1.0;
                     }

                     for (PotionEffect var12 : var2) {
                        int var13 = var12.getPotionID();
                        if (Potion.potionTypes[var13].isInstant()) {
                           Potion.potionTypes[var13].affectEntity(this.h(), var6, var12.getAmplifier(), var9);
                        } else {
                           int var14 = (int)(var9 * var12.getDuration() + 0.5);
                           if (var14 > 20) {
                              var6.addPotionEffect(new PotionEffect(var13, var14, var12.getAmplifier()));
                           }
                        }
                     }
                  }
               }
            }
         }

         this.worldObj.playAuxSFX(2002, (int)Math.round(this.posX), (int)Math.round(this.posY), (int)Math.round(this.posZ), this.getPotionDamage());
         this.w();
      }
   }

   @Override
   public void readEntityFromNBT(NBTTagCompound var1) {
      super.readEntityFromNBT(var1);
      if (var1.hasKey("Potion")) {
         this.potionDamage = ItemStack.loadItemStackFromNBT(var1.getCompoundTag("Potion"));
      } else {
         this.setPotionDamage(var1.getInteger("potionValue"));
      }

      if (this.potionDamage == null) {
         this.w();
      }
   }

   @Override
   public void writeEntityToNBT(NBTTagCompound var1) {
      super.writeEntityToNBT(var1);
      if (this.potionDamage != null) {
         var1.setCompoundTag("Potion", this.potionDamage.writeToNBT(new NBTTagCompound()));
      }
   }
}
