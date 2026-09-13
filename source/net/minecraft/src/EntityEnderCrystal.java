package net.minecraft.src;

public class EntityEnderCrystal extends Entity {
   public int innerRotation = 0;
   public int health;

   public EntityEnderCrystal(World var1) {
      super(var1);
      this.preventEntitySpawning = true;
      this.a(2.0F, 2.0F);
      this.yOffset = this.height / 2.0F;
      this.health = 5;
      this.innerRotation = this.rand.nextInt(100000);
   }

   public EntityEnderCrystal(World var1, double var2, double var4, double var6) {
      this(var1);
      this.b(var2, var4, var6);
   }

   @Override
   protected boolean canTriggerWalking() {
      return false;
   }

   @Override
   protected void entityInit() {
      this.dataWatcher.addObject(8, this.health);
   }

   @Override
   public void onUpdate() {
      this.prevPosX = this.posX;
      this.prevPosY = this.posY;
      this.prevPosZ = this.posZ;
      this.innerRotation++;
      this.dataWatcher.updateObject(8, this.health);
      int var1 = MathHelper.floor_double(this.posX);
      int var2 = MathHelper.floor_double(this.posY);
      int var3 = MathHelper.floor_double(this.posZ);
      if (this.worldObj.getBlockId(var1, var2, var3) != Block.fire.blockID) {
         this.worldObj.setBlock(var1, var2, var3, Block.fire.blockID);
      }
   }

   @Override
   protected void writeEntityToNBT(NBTTagCompound var1) {
   }

   @Override
   protected void readEntityFromNBT(NBTTagCompound var1) {
   }

   @Override
   public float getShadowSize() {
      return 0.0F;
   }

   @Override
   public boolean canBeCollidedWith() {
      return true;
   }

   @Override
   public boolean attackEntityFrom(DamageSource var1, int var2) {
      if (this.aq()) {
         return false;
      } else {
         if (!this.isDead && !this.worldObj.isRemote) {
            this.health = 0;
            if (this.health <= 0) {
               this.w();
               if (!this.worldObj.isRemote) {
                  this.worldObj.createExplosion(null, this.posX, this.posY, this.posZ, 6.0F, true);
               }
            }
         }

         return true;
      }
   }
}
