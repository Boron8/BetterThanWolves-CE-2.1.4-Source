package btw.entity.mob;

import btw.entity.mob.behavior.SimpleWanderBehavior;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityAITargetNonTamed;
import net.minecraft.src.EntityAIWander;
import net.minecraft.src.EntityAgeable;
import net.minecraft.src.EntityAnimal;
import net.minecraft.src.EntityItem;
import net.minecraft.src.EntityList;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityOcelot;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.MathHelper;
import net.minecraft.src.World;

public class OcelotEntity extends EntityOcelot {
   private static final int TABBY_SKIN_ID = 22;

   public OcelotEntity(World world) {
      super(world);
      this.tasks.removeAllTasksOfClass(EntityAIWander.class);
      this.targetTasks.removeAllTasksOfClass(EntityAITargetNonTamed.class);
      this.tasks.addTask(10, new SimpleWanderBehavior(this, 0.23F));
      this.targetTasks.addTask(1, new EntityAITargetNonTamed(this, ChickenEntity.class, 14.0F, 750, false));
   }

   @Override
   public boolean interact(EntityPlayer player) {
      boolean bWasTamed = this.m();
      boolean bReturnValue = super.interact(player);
      if (!this.worldObj.isRemote && !bWasTamed && this.m() && this.worldObj.rand.nextInt(4) == 0) {
         this.s(22);
      }

      return bReturnValue;
   }

   @Override
   public void checkForLooseFood() {
      if (!this.worldObj.isRemote && this.R()) {
         boolean bAte = false;

         for (EntityItem itemEntity : this.worldObj.getEntitiesWithinAABB(EntityItem.class, this.boundingBox.expand(2.5, 1.0, 2.5))) {
            if (itemEntity.delayBeforeCanPickup == 0 && !itemEntity.isDead) {
               ItemStack itemStack = itemEntity.getEntityItem();
               Item item = itemStack.getItem();
               if (item.itemID == Item.chickenRaw.itemID || item.itemID == Item.fishRaw.itemID) {
                  itemEntity.w();
                  bAte = true;
               }
            }
         }

         if (bAte) {
            this.worldObj.playAuxSFX(2226, MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ), 0);
         }
      }
   }

   @Override
   public void onNearbyAnimalAttacked(EntityAnimal attackedAnimal, EntityLiving attackSource) {
   }

   @Override
   public void onNearbyPlayerStartles(EntityPlayer player) {
   }

   @Override
   public boolean isAffectedByMovementModifiers() {
      return false;
   }

   @Override
   public void initCreature() {
      if (this.worldObj.rand.nextInt(7) == 0) {
         for (int iTempCount = 0; iTempCount < 2; iTempCount++) {
            OcelotEntity kitten = (OcelotEntity)EntityList.createEntityOfType(OcelotEntity.class, this.worldObj);
            kitten.b(this.posX, this.posY, this.posZ, this.rotationYaw, 0.0F);
            kitten.a(-kitten.getTicksForChildToGrow());
            this.worldObj.spawnEntityInWorld(kitten);
         }
      }
   }

   public OcelotEntity spawnBabyAnimal(EntityAgeable otherParent) {
      OcelotEntity kitten = (OcelotEntity)EntityList.createEntityOfType(OcelotEntity.class, this.worldObj);
      if (this.m()) {
         kitten.a(this.o());
         kitten.j(true);
         kitten.s(this.t());
      }

      return kitten;
   }

   @Override
   public int getItemFoodValue(ItemStack stack) {
      return 0;
   }

   @Override
   public boolean isTooHungryToHeal() {
      return true;
   }

   @Override
   public int getMeleeAttackStrength(Entity target) {
      return 3;
   }

   @Override
   public boolean attackEntityAsMob(Entity target) {
      return this.meleeAttack(target);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public String getTexture() {
      return this.t() == 22 ? "/btwmodtex/cat_tabby.png" : super.getTexture();
   }
}
