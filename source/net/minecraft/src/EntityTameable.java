package net.minecraft.src;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

public abstract class EntityTameable extends EntityAnimal {
   protected EntityAISit aiSit = new EntityAISit(this);

   public EntityTameable(World par1World) {
      super(par1World);
   }

   @Override
   protected void entityInit() {
      super.entityInit();
      this.dataWatcher.addObject(16, (byte)0);
      this.dataWatcher.addObject(17, "");
   }

   @Override
   public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound) {
      super.writeEntityToNBT(par1NBTTagCompound);
      if (this.getOwnerName() == null) {
         par1NBTTagCompound.setString("Owner", "");
      } else {
         par1NBTTagCompound.setString("Owner", this.getOwnerName());
      }

      par1NBTTagCompound.setBoolean("Sitting", this.isSitting());
   }

   @Override
   public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound) {
      super.readEntityFromNBT(par1NBTTagCompound);
      String var2 = par1NBTTagCompound.getString("Owner");
      if (var2.length() > 0) {
         this.setOwner(var2);
         this.setTamed(true);
      }

      this.aiSit.setSitting(par1NBTTagCompound.getBoolean("Sitting"));
      this.setSitting(par1NBTTagCompound.getBoolean("Sitting"));
   }

   protected void playTameEffect(boolean par1) {
      String var2 = "heart";
      if (!par1) {
         var2 = "smoke";
      }

      for (int var3 = 0; var3 < 7; var3++) {
         double var4 = this.rand.nextGaussian() * 0.02;
         double var6 = this.rand.nextGaussian() * 0.02;
         double var8 = this.rand.nextGaussian() * 0.02;
         this.worldObj
            .spawnParticle(
               var2,
               this.posX + this.rand.nextFloat() * this.width * 2.0F - this.width,
               this.posY + 0.5 + this.rand.nextFloat() * this.height,
               this.posZ + this.rand.nextFloat() * this.width * 2.0F - this.width,
               var4,
               var6,
               var8
            );
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void handleHealthUpdate(byte par1) {
      if (par1 == 7) {
         this.playTameEffect(true);
      } else if (par1 == 6) {
         this.playTameEffect(false);
      } else {
         super.handleHealthUpdate(par1);
      }
   }

   public boolean isTamed() {
      return (this.dataWatcher.getWatchableObjectByte(16) & 4) != 0;
   }

   public void setTamed(boolean par1) {
      byte var2 = this.dataWatcher.getWatchableObjectByte(16);
      if (par1) {
         this.dataWatcher.updateObject(16, (byte)(var2 | 4));
      } else {
         this.dataWatcher.updateObject(16, (byte)(var2 & -5));
      }
   }

   public boolean isSitting() {
      return (this.dataWatcher.getWatchableObjectByte(16) & 1) != 0;
   }

   public void setSitting(boolean par1) {
      byte var2 = this.dataWatcher.getWatchableObjectByte(16);
      if (par1) {
         this.dataWatcher.updateObject(16, (byte)(var2 | 1));
      } else {
         this.dataWatcher.updateObject(16, (byte)(var2 & -2));
      }
   }

   public String getOwnerName() {
      return this.dataWatcher.getWatchableObjectString(17);
   }

   public void setOwner(String par1Str) {
      this.dataWatcher.updateObject(17, par1Str);
   }

   public EntityLiving getOwner() {
      return this.worldObj.getPlayerEntityByName(this.getOwnerName());
   }

   public EntityAISit func_70907_r() {
      return this.aiSit;
   }

   @Override
   public boolean isSecondaryTargetForSquid() {
      return false;
   }

   public boolean isAITryingToSit() {
      return this.aiSit.isTryingToSit();
   }
}
