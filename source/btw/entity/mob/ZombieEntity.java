package btw.entity.mob;

import btw.entity.mob.behavior.SimpleWanderBehavior;
import btw.entity.mob.behavior.ZombieBreakBarricadeBehavior;
import btw.entity.mob.behavior.ZombieSecondaryAttackBehavior;
import btw.entity.mob.villager.VillagerEntity;
import btw.entity.util.ZombieSecondaryTargetFilter;
import btw.item.BTWItems;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.Enchantment;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityAIAttackOnCollide;
import net.minecraft.src.EntityAIBreakDoor;
import net.minecraft.src.EntityAINearestAttackableTarget;
import net.minecraft.src.EntityAIWander;
import net.minecraft.src.EntityAnimal;
import net.minecraft.src.EntityCreature;
import net.minecraft.src.EntityItem;
import net.minecraft.src.EntityList;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EntityZombie;
import net.minecraft.src.IEntitySelector;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.MathHelper;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.Potion;
import net.minecraft.src.PotionEffect;
import net.minecraft.src.World;

public class ZombieEntity extends EntityZombie {
   public int villagerClass = -1;
   private IEntitySelector targetEntitySelector;

   public ZombieEntity(World world) {
      super(world);
      this.aC().setBreakDoors(false);
      this.tasks.removeAllTasksOfClass(EntityAIBreakDoor.class);
      this.tasks.removeAllTasksOfClass(EntityAIAttackOnCollide.class);
      this.tasks.removeAllTasksOfClass(EntityAIWander.class);
      this.tasks.addTask(1, new ZombieBreakBarricadeBehavior(this));
      this.tasks.addTask(2, new EntityAIAttackOnCollide(this, EntityPlayer.class, this.moveSpeed, false));
      this.tasks.addTask(3, new ZombieSecondaryAttackBehavior(this));
      this.tasks.addTask(6, new SimpleWanderBehavior(this, this.moveSpeed));
      this.targetTasks.removeAllTasksOfClass(EntityAINearestAttackableTarget.class);
      this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 16.0F, 0, true));
      this.targetEntitySelector = new ZombieSecondaryTargetFilter(this);
      this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityCreature.class, 16.0F, 0, false, false, this.targetEntitySelector));
   }

   @Override
   protected int func_96121_ay() {
      return 16;
   }

   @Override
   public float getSpeedModifier() {
      return this.m() ? 1.5F : super.getSpeedModifier();
   }

   @Override
   public void setVillager(boolean bIsVillager) {
      super.setVillager(bIsVillager);
      this.aC().setBreakDoors(bIsVillager);
   }

   @Override
   public void onLivingUpdate() {
      if (!this.m()) {
         this.checkForCatchFireInSun();
      }

      this.entityMobOnLivingUpdate();
   }

   @Override
   public boolean attackEntityAsMob(Entity attackedEntity) {
      return this.meleeAttack(attackedEntity);
   }

   @Override
   protected void dropRareDrop(int iBonusDrop) {
   }

   @Override
   protected void addRandomArmor() {
      this.entityLivingAddRandomArmor();
      if (this.rand.nextFloat() < 0.05F) {
         int iHeldType = this.rand.nextInt(3);
         if (iHeldType == 0) {
            this.c(0, new ItemStack(Item.swordIron));
         } else {
            this.c(0, new ItemStack(Item.shovelIron));
         }

         this.equipmentDropChances[0] = 0.99F;
      }
   }

   @Override
   public void writeEntityToNBT(NBTTagCompound tag) {
      super.writeEntityToNBT(tag);
      tag.setInteger("fcVillagerClass", this.villagerClass);
   }

   @Override
   public void readEntityFromNBT(NBTTagCompound tag) {
      super.readEntityFromNBT(tag);
      if (tag.hasKey("fcVillagerClass")) {
         this.villagerClass = tag.getInteger("fcVillagerClass");
      }
   }

   @Override
   public void onKillEntity(EntityLiving entityKilled) {
      if (this.rand.nextInt(4) != 0 && entityKilled instanceof VillagerEntity) {
         ZombieEntity newZombie = (ZombieEntity)EntityList.createEntityOfType(ZombieEntity.class, this.worldObj);
         newZombie.k(entityKilled);
         this.worldObj.removeEntity(entityKilled);
         newZombie.setPersistent(true);
         newZombie.setVillager(true);
         newZombie.villagerClass = ((VillagerEntity)entityKilled).m();
         if (entityKilled.isChild()) {
            newZombie.a(true);
         }

         this.worldObj.spawnEntityInWorld(newZombie);
         this.worldObj.playAuxSFXAtEntity(null, 1016, (int)this.posX, (int)this.posY, (int)this.posZ, 0);
      }
   }

   @Override
   public void initCreature() {
      this.h(this.rand.nextFloat() < 0.15F);
      this.addRandomArmor();
      this.bI();
   }

   @Override
   public boolean interact(EntityPlayer player) {
      return false;
   }

   @Override
   protected void convertToVillager() {
      VillagerEntity newVillager = VillagerEntity.createVillagerFromProfession(this.worldObj, this.villagerClass);
      newVillager.k(this);
      newVillager.initCreature();
      if (this.villagerClass == 0) {
         newVillager.setDirtyPeasant(1);
      }

      newVillager.q();
      if (this.h_()) {
         newVillager.a(-newVillager.getTicksForChildToGrow());
      }

      this.worldObj.removeEntity(this);
      this.worldObj.spawnEntityInWorld(newVillager);
      newVillager.d(new PotionEffect(Potion.confusion.id, 200, 0));
      this.worldObj.playAuxSFXAtEntity((EntityPlayer)null, 1017, (int)this.posX, (int)this.posY, (int)this.posZ, 0);
   }

   @Override
   protected int getConversionTimeBoost() {
      return 1;
   }

   @Override
   protected void modSpecificOnLivingUpdate() {
      super.modSpecificOnLivingUpdate();
      this.checkForLooseFood();
      if (!this.worldObj.isRemote && this.m() && !this.isDead && this.villagerClass < 0) {
         this.setVillager(false);
      }
   }

   @Override
   public void checkForScrollDrop() {
      if (this.rand.nextInt(1000) == 0) {
         ItemStack itemstack = new ItemStack(BTWItems.arcaneScroll, 1, Enchantment.smite.effectId);
         this.a(itemstack, 0.0F);
      }
   }

   @Override
   protected void attackEntity(Entity attackedEntity, float fDistanceToTarget) {
      if (attackedEntity instanceof EntityAnimal) {
         if (this.attackTime <= 0 && fDistanceToTarget < 4.0F) {
            this.attackTime = 20;
            this.attackEntityAsMob(attackedEntity);
         }
      } else {
         super.a(attackedEntity, fDistanceToTarget);
      }
   }

   @Override
   protected void dropHead() {
      this.a(new ItemStack(Item.skull.itemID, 1, 2), 0.0F);
   }

   @Override
   public void spawnerInitCreature() {
      this.h(this.rand.nextFloat() < 0.15F);
      this.bI();
   }

   @Override
   protected float getSoundPitch() {
      return !this.h_() && this.m() ? (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 0.7F : super.aY();
   }

   @Override
   public boolean getCanBeHeadCrabbed(boolean bSquidInWater) {
      return !bSquidInWater && this.riddenByEntity == null && this.R() && !this.h_();
   }

   @Override
   public void onHeadCrabbedBySquid(SquidEntity squid) {
      this.a(this.bd(), this.ba(), this.getSoundPitch());
   }

   @Override
   public double getMountedYOffset() {
      return this.height;
   }

   @Override
   protected boolean isWeightedByHeadCrab() {
      return false;
   }

   @Override
   public Entity getHeadCrabSharedAttackTarget() {
      return this.aJ();
   }

   @Override
   public boolean isImmuneToHeadCrabDamage() {
      return true;
   }

   private void checkForLooseFood() {
      if (!this.worldObj.isRemote && !this.isLivingDead) {
         boolean bAte = false;

         for (EntityItem itemEntity : this.worldObj.getEntitiesWithinAABB(EntityItem.class, this.boundingBox.expand(2.5, 1.0, 2.5))) {
            if (itemEntity.delayBeforeCanPickup == 0 && !itemEntity.isDead) {
               ItemStack itemStack = itemEntity.getEntityItem();
               Item item = itemStack.getItem();
               if (item.doZombiesConsume()) {
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

   public boolean attemptToStartCure() {
      if (!this.isLivingDead && this.m() && !this.o()) {
         this.a(this.rand.nextInt(2401) + 3600);
         return true;
      } else {
         return false;
      }
   }

   @Override
   public boolean canSoulAffectEntity(Entity soulEntity) {
      return this.attemptToStartCure();
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void handleHealthUpdate(byte var1) {
      super.handleHealthUpdate(var1);
      if (var1 == 16) {
         this.worldObj
            .playSound(
               this.posX + 0.5, this.posY + 0.5, this.posZ + 0.5, "mob.zombie.say", 1.0F + this.rand.nextFloat(), this.rand.nextFloat() * 0.2F + 0.5F, false
            );
      }
   }
}
