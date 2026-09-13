package net.minecraft.src;

import btw.entity.mob.ZombieEntity;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

public class EntityPigZombie extends ZombieEntity {
   private int angerLevel = 0;
   private int randomSoundDelay = 0;

   public EntityPigZombie(World par1World) {
      super(par1World);
      this.texture = "/mob/pigzombie.png";
      this.moveSpeed = 0.5F;
      this.isImmuneToFire = true;
   }

   @Override
   protected boolean isAIEnabled() {
      return false;
   }

   @Override
   public void onUpdate() {
      this.moveSpeed = this.entityToAttack != null ? 0.95F : 0.5F;
      if (this.randomSoundDelay > 0 && --this.randomSoundDelay == 0) {
         this.a("mob.zombiepig.zpigangry", this.ba() * 2.0F, ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F) * 1.8F);
      }

      super.l_();
   }

   @Environment(EnvType.CLIENT)
   @Override
   public String getTexture() {
      return "/mob/pigzombie.png";
   }

   @Override
   public boolean getCanSpawnHere() {
      return this.worldObj.difficultySetting > 0
         && this.worldObj.checkNoEntityCollision(this.boundingBox)
         && this.worldObj.getCollidingBoundingBoxes(this, this.boundingBox).isEmpty()
         && !this.worldObj.isAnyLiquid(this.boundingBox);
   }

   @Override
   public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound) {
      super.writeEntityToNBT(par1NBTTagCompound);
      par1NBTTagCompound.setShort("Anger", (short)this.angerLevel);
   }

   @Override
   public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound) {
      super.readEntityFromNBT(par1NBTTagCompound);
      this.angerLevel = par1NBTTagCompound.getShort("Anger");
   }

   @Override
   protected Entity findPlayerToAttack() {
      return this.angerLevel == 0 ? null : super.j();
   }

   @Override
   public boolean attackEntityFrom(DamageSource par1DamageSource, int par2) {
      if (this.aq()) {
         return false;
      } else {
         Entity var3 = par1DamageSource.getEntity();
         if (var3 instanceof EntityPlayer) {
            List var4 = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.expand(32.0, 32.0, 32.0));

            for (int var5 = 0; var5 < var4.size(); var5++) {
               Entity var6 = (Entity)var4.get(var5);
               if (var6 instanceof EntityPigZombie) {
                  EntityPigZombie var7 = (EntityPigZombie)var6;
                  var7.becomeAngryAt(var3);
               }
            }

            this.becomeAngryAt(var3);
         }

         return super.a(par1DamageSource, par2);
      }
   }

   protected void becomeAngryAt(Entity par1Entity) {
      this.entityToAttack = par1Entity;
      this.angerLevel = 400 + this.rand.nextInt(400);
      this.randomSoundDelay = this.rand.nextInt(40);
   }

   @Override
   protected String getLivingSound() {
      return "mob.zombiepig.zpig";
   }

   @Override
   protected String getHurtSound() {
      return "mob.zombiepig.zpighurt";
   }

   @Override
   protected String getDeathSound() {
      return "mob.zombiepig.zpigdeath";
   }

   @Override
   protected void dropFewItems(boolean par1, int par2) {
      int var3 = this.rand.nextInt(2 + par2);

      for (int var4 = 0; var4 < var3; var4++) {
         this.b(Item.rottenFlesh.itemID, 1);
      }

      var3 = this.rand.nextInt(2 + par2);

      for (int var6 = 0; var6 < var3; var6++) {
         this.b(Item.goldNugget.itemID, 1);
      }
   }

   @Override
   public boolean interact(EntityPlayer par1EntityPlayer) {
      return false;
   }

   @Override
   protected void dropRareDrop(int par1) {
      this.b(Item.ingotGold.itemID, 1);
   }

   @Override
   protected int getDropItemId() {
      return Item.rottenFlesh.itemID;
   }

   @Override
   protected void addRandomArmor() {
      this.c(0, new ItemStack(Item.swordGold));
   }

   @Override
   public void initCreature() {
      super.initCreature();
      this.i(false);
   }

   @Override
   public int getAttackStrength(Entity par1Entity) {
      ItemStack var2 = this.bG();
      int var3 = 5;
      if (var2 != null) {
         var3 += var2.getDamageVsEntity(this);
      }

      return var3;
   }
}
