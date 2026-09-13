package net.minecraft.src;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

public class EntityTNTPrimed extends Entity {
   public int fuse = 0;
   private EntityLiving tntPlacedBy;

   public EntityTNTPrimed(World par1World) {
      super(par1World);
      this.preventEntitySpawning = true;
      this.a(0.98F, 0.98F);
      this.yOffset = this.height / 2.0F;
   }

   public EntityTNTPrimed(World par1World, double par2, double par4, double par6, EntityLiving par8EntityLiving) {
      this(par1World);
      this.b(par2, par4, par6);
      float var9 = (float)(Math.random() * Math.PI * 2.0);
      this.motionX = -((float)Math.sin(var9)) * 0.02F;
      this.motionY = 0.2F;
      this.motionZ = -((float)Math.cos(var9)) * 0.02F;
      this.fuse = 80;
      this.prevPosX = par2;
      this.prevPosY = par4;
      this.prevPosZ = par6;
      this.tntPlacedBy = par8EntityLiving;
   }

   public EntityTNTPrimed(World par1World, double par2, double par4, double par6) {
      this(par1World, par2, par4, par6, null);
   }

   @Override
   protected void entityInit() {
   }

   @Override
   protected boolean canTriggerWalking() {
      return false;
   }

   @Override
   public boolean canBeCollidedWith() {
      return !this.isDead;
   }

   @Override
   public void onUpdate() {
      this.prevPosX = this.posX;
      this.prevPosY = this.posY;
      this.prevPosZ = this.posZ;
      this.motionY -= 0.04F;
      this.d(this.motionX, this.motionY, this.motionZ);
      this.motionX *= 0.98F;
      this.motionY *= 0.98F;
      this.motionZ *= 0.98F;
      if (this.onGround) {
         this.motionX *= 0.7F;
         this.motionZ *= 0.7F;
         this.motionY *= -0.5;
      }

      if (this.fuse-- <= 0) {
         this.w();
         if (!this.worldObj.isRemote) {
            this.explode();
         }
      } else {
         this.worldObj.spawnParticle("smoke", this.posX, this.posY + 0.5, this.posZ, 0.0, 0.0, 0.0);
      }
   }

   private void explode() {
      float var1 = 4.0F;
      this.worldObj.createExplosion(this, this.posX, this.posY, this.posZ, var1, true);
   }

   @Override
   protected void writeEntityToNBT(NBTTagCompound par1NBTTagCompound) {
      par1NBTTagCompound.setByte("Fuse", (byte)this.fuse);
   }

   @Override
   protected void readEntityFromNBT(NBTTagCompound par1NBTTagCompound) {
      this.fuse = par1NBTTagCompound.getByte("Fuse");
   }

   @Environment(EnvType.CLIENT)
   @Override
   public float getShadowSize() {
      return 0.0F;
   }

   public EntityLiving getTntPlacedBy() {
      return this.tntPlacedBy;
   }
}
