package net.minecraft.src;

import java.util.Collections;
import java.util.List;

public class EntityAINearestAttackableTarget extends EntityAITarget {
   EntityLiving targetEntity;
   Class targetClass;
   int targetChance;
   private final IEntitySelector field_82643_g;
   private EntityAINearestAttackableTargetSorter theNearestAttackableTargetSorter;

   public EntityAINearestAttackableTarget(EntityLiving var1, Class var2, float var3, int var4, boolean var5) {
      this(var1, var2, var3, var4, var5, false);
   }

   public EntityAINearestAttackableTarget(EntityLiving var1, Class var2, float var3, int var4, boolean var5, boolean var6) {
      this(var1, var2, var3, var4, var5, var6, null);
   }

   public EntityAINearestAttackableTarget(EntityLiving var1, Class var2, float var3, int var4, boolean var5, boolean var6, IEntitySelector var7) {
      super(var1, var3, var5, var6);
      this.targetClass = var2;
      this.targetDistance = var3;
      this.targetChance = var4;
      this.theNearestAttackableTargetSorter = new EntityAINearestAttackableTargetSorter(this, var1);
      this.field_82643_g = var7;
      this.a(1);
   }

   @Override
   public boolean shouldExecute() {
      if (this.targetChance > 0 && this.taskOwner.getRNG().nextInt(this.targetChance) != 0) {
         return false;
      } else {
         if (this.targetClass == EntityPlayer.class) {
            EntityPlayer var1 = this.taskOwner.worldObj.getClosestVulnerablePlayerToEntity(this.taskOwner, this.targetDistance);
            if (this.a(var1, false)) {
               this.targetEntity = var1;
               return true;
            }
         } else {
            List var5 = this.taskOwner
               .worldObj
               .selectEntitiesWithinAABB(this.targetClass, this.taskOwner.boundingBox.expand(this.targetDistance, 4.0, this.targetDistance), this.field_82643_g);
            Collections.sort(var5, this.theNearestAttackableTargetSorter);

            for (Entity var3 : var5) {
               EntityLiving var4 = (EntityLiving)var3;
               if (this.a(var4, false)) {
                  this.targetEntity = var4;
                  return true;
               }
            }
         }

         return false;
      }
   }

   @Override
   public void startExecuting() {
      this.taskOwner.setAttackTarget(this.targetEntity);
      super.startExecuting();
   }
}
