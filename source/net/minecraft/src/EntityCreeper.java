package net.minecraft.src;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

public class EntityCreeper extends EntityMob {
   private int lastActiveTime;
   private int timeSinceIgnited;
   private int fuseTime = 30;
   private int explosionRadius = 3;

   public EntityCreeper(World par1World) {
      super(par1World);
      this.texture = "/mob/creeper.png";
      this.tasks.addTask(1, new EntityAISwimming(this));
      this.tasks.addTask(2, new EntityAICreeperSwell(this));
      this.tasks.addTask(3, new EntityAIAvoidEntity(this, EntityOcelot.class, 6.0F, 0.25F, 0.3F));
      this.tasks.addTask(4, new EntityAIAttackOnCollide(this, 0.25F, false));
      this.tasks.addTask(5, new EntityAIWander(this, 0.2F));
      this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
      this.tasks.addTask(6, new EntityAILookIdle(this));
      this.targetTasks.addTask(1, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 16.0F, 0, true));
      this.targetTasks.addTask(2, new EntityAIHurtByTarget(this, false));
   }

   @Override
   public boolean isAIEnabled() {
      return true;
   }

   @Override
   public int func_82143_as() {
      return this.aJ() == null ? 3 : 3 + (this.health - 1);
   }

   @Override
   protected void fall(float par1) {
      super.a(par1);
      this.timeSinceIgnited = (int)(this.timeSinceIgnited + par1 * 1.5F);
      if (this.timeSinceIgnited > this.fuseTime - 5) {
         this.timeSinceIgnited = this.fuseTime - 5;
      }
   }

   @Override
   public int getMaxHealth() {
      return 20;
   }

   @Override
   protected void entityInit() {
      super.a();
      this.dataWatcher.addObject(16, (byte)-1);
      this.dataWatcher.addObject(17, (byte)0);
   }

   @Override
   public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound) {
      super.b(par1NBTTagCompound);
      if (this.dataWatcher.getWatchableObjectByte(17) == 1) {
         par1NBTTagCompound.setBoolean("powered", true);
      }

      par1NBTTagCompound.setShort("Fuse", (short)this.fuseTime);
      par1NBTTagCompound.setByte("ExplosionRadius", (byte)this.explosionRadius);
   }

   @Override
   public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound) {
      super.a(par1NBTTagCompound);
      this.dataWatcher.updateObject(17, (byte)(par1NBTTagCompound.getBoolean("powered") ? 1 : 0));
      if (par1NBTTagCompound.hasKey("Fuse")) {
         this.fuseTime = par1NBTTagCompound.getShort("Fuse");
      }

      if (par1NBTTagCompound.hasKey("ExplosionRadius")) {
         this.explosionRadius = par1NBTTagCompound.getByte("ExplosionRadius");
      }
   }

   @Override
   public void onUpdate() {
      if (this.R()) {
         this.lastActiveTime = this.timeSinceIgnited;
         int var1 = this.getCreeperState();
         if (var1 > 0 && this.timeSinceIgnited == 0) {
            this.a("random.fuse", 1.0F, 0.5F);
         }

         this.timeSinceIgnited += var1;
         if (this.timeSinceIgnited < 0) {
            this.timeSinceIgnited = 0;
         }

         if (this.timeSinceIgnited >= this.fuseTime) {
            this.timeSinceIgnited = this.fuseTime;
            if (!this.worldObj.isRemote) {
               boolean var2 = this.worldObj.getGameRules().getGameRuleBooleanValue("mobGriefing");
               if (this.getPowered()) {
                  this.worldObj.createExplosion(this, this.posX, this.posY, this.posZ, this.explosionRadius * 2, var2);
               } else {
                  this.worldObj.createExplosion(this, this.posX, this.posY, this.posZ, this.explosionRadius, var2);
               }

               this.w();
            }
         }
      }

      super.onUpdate();
   }

   @Override
   protected String getHurtSound() {
      return "mob.creeper.say";
   }

   @Override
   protected String getDeathSound() {
      return "mob.creeper.death";
   }

   @Override
   public void onDeath(DamageSource par1DamageSource) {
      super.a(par1DamageSource);
      if (par1DamageSource.getEntity() instanceof EntitySkeleton) {
         int var2 = Item.record13.itemID + this.rand.nextInt(Item.recordWait.itemID - Item.record13.itemID + 1);
         this.b(var2, 1);
      }
   }

   @Override
   public boolean attackEntityAsMob(Entity par1Entity) {
      return true;
   }

   public boolean getPowered() {
      return this.dataWatcher.getWatchableObjectByte(17) == 1;
   }

   @Environment(EnvType.CLIENT)
   public float getCreeperFlashIntensity(float par1) {
      return (this.lastActiveTime + (this.timeSinceIgnited - this.lastActiveTime) * par1) / (this.fuseTime - 2);
   }

   @Override
   protected int getDropItemId() {
      return Item.gunpowder.itemID;
   }

   public int getCreeperState() {
      return this.dataWatcher.getWatchableObjectByte(16);
   }

   public void setCreeperState(int par1) {
      this.dataWatcher.updateObject(16, (byte)par1);
   }

   @Override
   public void onStruckByLightning(EntityLightningBolt par1EntityLightningBolt) {
      super.a(par1EntityLightningBolt);
      this.dataWatcher.updateObject(17, (byte)1);
   }
}
