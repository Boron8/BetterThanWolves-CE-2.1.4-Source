package net.minecraft.src;

import btw.item.BTWItems;

public class EntitySilverfish extends EntityMob {
   private int allySummonCooldown;

   public EntitySilverfish(World par1World) {
      super(par1World);
      this.texture = "/mob/silverfish.png";
      this.a(0.3F, 0.7F);
      this.moveSpeed = 0.6F;
   }

   @Override
   public int getMaxHealth() {
      return 8;
   }

   @Override
   protected boolean canTriggerWalking() {
      return false;
   }

   @Override
   protected Entity findPlayerToAttack() {
      double var1 = 8.0;
      return this.worldObj.getClosestVulnerablePlayerToEntity(this, var1);
   }

   @Override
   protected String getLivingSound() {
      return "mob.silverfish.say";
   }

   @Override
   protected String getHurtSound() {
      return "mob.silverfish.hit";
   }

   @Override
   protected String getDeathSound() {
      return "mob.silverfish.kill";
   }

   @Override
   public boolean attackEntityFrom(DamageSource par1DamageSource, int par2) {
      if (this.aq()) {
         return false;
      } else {
         if (this.allySummonCooldown <= 0 && (par1DamageSource instanceof EntityDamageSource || par1DamageSource == DamageSource.magic)) {
            this.allySummonCooldown = 20;
         }

         return super.attackEntityFrom(par1DamageSource, par2);
      }
   }

   @Override
   protected void attackEntity(Entity par1Entity, float par2) {
      if (this.attackTime <= 0 && par2 < 1.2F && par1Entity.boundingBox.maxY > this.boundingBox.minY && par1Entity.boundingBox.minY < this.boundingBox.maxY) {
         this.attackTime = 20;
         this.m(par1Entity);
      }
   }

   @Override
   protected void playStepSound(int par1, int par2, int par3, int par4) {
      this.a("mob.silverfish.step", 0.15F, 1.0F);
   }

   @Override
   protected int getDropItemId() {
      return 0;
   }

   @Override
   public void onUpdate() {
      this.renderYawOffset = this.rotationYaw;
      super.onUpdate();
   }

   @Override
   protected void updateEntityActionState() {
      super.bq();
      if (!this.worldObj.isRemote) {
         if (this.allySummonCooldown > 0) {
            this.allySummonCooldown--;
            if (this.allySummonCooldown == 0) {
               int var1 = MathHelper.floor_double(this.posX);
               int var2 = MathHelper.floor_double(this.posY);
               int var3 = MathHelper.floor_double(this.posZ);
               boolean var4 = false;

               for (int var5 = 0; !var4 && var5 <= 5 && var5 >= -5; var5 = var5 <= 0 ? 1 - var5 : 0 - var5) {
                  for (int var6 = 0; !var4 && var6 <= 10 && var6 >= -10; var6 = var6 <= 0 ? 1 - var6 : 0 - var6) {
                     for (int var7 = 0; !var4 && var7 <= 10 && var7 >= -10; var7 = var7 <= 0 ? 1 - var7 : 0 - var7) {
                        int var8 = this.worldObj.getBlockId(var1 + var6, var2 + var5, var3 + var7);
                        if (var8 != 0 && Block.blocksList[var8].isBlockInfestedBy(this)) {
                           this.worldObj.destroyBlock(var1 + var6, var2 + var5, var3 + var7, true);
                           Block.blocksList[var8].onBlockDestroyedByPlayer(this.worldObj, var1 + var6, var2 + var5, var3 + var7, 0);
                           if (this.rand.nextBoolean()) {
                              var4 = true;
                              break;
                           }
                        }
                     }
                  }
               }
            }
         }

         if (this.entityToAttack == null && !this.k()) {
            int var1 = MathHelper.floor_double(this.posX);
            int var2 = MathHelper.floor_double(this.posY + 0.5);
            int var3 = MathHelper.floor_double(this.posZ);
            int var9 = this.rand.nextInt(6);
            int var5 = this.worldObj.getBlockId(var1 + Facing.offsetsXForSide[var9], var2 + Facing.offsetsYForSide[var9], var3 + Facing.offsetsZForSide[var9]);
            if (var5 != 0) {
               int infestmetadata = this.worldObj
                  .getBlockMetadata(var1 + Facing.offsetsXForSide[var9], var2 + Facing.offsetsYForSide[var9], var3 + Facing.offsetsZForSide[var9]);
               if (Block.blocksList[var5].isBlockInfestable(this, infestmetadata)) {
                  Block.blocksList[var5]
                     .onInfested(
                        this.worldObj,
                        this,
                        var1 + Facing.offsetsXForSide[var9],
                        var2 + Facing.offsetsYForSide[var9],
                        var3 + Facing.offsetsZForSide[var9],
                        infestmetadata
                     );
               } else {
                  this.i();
               }
            }
         } else if (this.entityToAttack != null && !this.k()) {
            this.entityToAttack = null;
         }
      }
   }

   @Override
   public float getBlockPathWeight(int par1, int par2, int par3) {
      return this.worldObj.getBlockId(par1, par2 - 1, par3) == Block.stone.blockID ? 10.0F : super.getBlockPathWeight(par1, par2, par3);
   }

   @Override
   protected boolean isValidLightLevel() {
      return true;
   }

   @Override
   public boolean getCanSpawnHere() {
      if (super.getCanSpawnHere()) {
         EntityPlayer var1 = this.worldObj.getClosestPlayerToEntity(this, 5.0);
         return var1 == null;
      } else {
         return false;
      }
   }

   @Override
   public int getAttackStrength(Entity par1Entity) {
      return 1;
   }

   @Override
   public EnumCreatureAttribute getCreatureAttribute() {
      return EnumCreatureAttribute.ARTHROPOD;
   }

   @Override
   protected void dropFewItems(boolean bKilledByPlayer, int iLootingModifier) {
      if (this.worldObj.provider.dimensionId == 1) {
         int iDropChance = this.rand.nextInt(5) - 3 + iLootingModifier;
         if (iDropChance > 0) {
            this.b(Item.clay.itemID, 1);
         }
      }
   }

   @Override
   public void checkForScrollDrop() {
      if (this.worldObj.provider.dimensionId == 1 && this.rand.nextInt(1000) == 0) {
         ItemStack itemstack = new ItemStack(BTWItems.arcaneScroll, 1, Enchantment.efficiency.effectId);
         this.a(itemstack, 0.0F);
      }
   }
}
