package net.minecraft.src;

import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

public class EntityDragon extends EntityLiving implements IBossDisplayData, IEntityMultiPart {
   public double targetX;
   public double targetY;
   public double targetZ;
   public double[][] ringBuffer = new double[64][3];
   public int ringBufferIndex = -1;
   public EntityDragonPart[] dragonPartArray;
   public EntityDragonPart dragonPartHead;
   public EntityDragonPart dragonPartBody;
   public EntityDragonPart dragonPartTail1;
   public EntityDragonPart dragonPartTail2;
   public EntityDragonPart dragonPartTail3;
   public EntityDragonPart dragonPartWing1;
   public EntityDragonPart dragonPartWing2;
   public float prevAnimTime = 0.0F;
   public float animTime = 0.0F;
   public boolean forceNewTarget = false;
   public boolean slowed = false;
   private Entity target;
   public int deathTicks = 0;
   public EntityEnderCrystal healingEnderCrystal = null;
   private static final long PLAYER_SWITCH_DIMENSIONS_GRACE_PERIOD = 600L;

   public EntityDragon(World par1World) {
      super(par1World);
      this.dragonPartArray = new EntityDragonPart[]{
         this.dragonPartHead = (EntityDragonPart)EntityList.createEntityOfType(EntityDragonPart.class, this, "head", 6.0F, 6.0F),
         this.dragonPartBody = (EntityDragonPart)EntityList.createEntityOfType(EntityDragonPart.class, this, "body", 8.0F, 8.0F),
         this.dragonPartTail1 = (EntityDragonPart)EntityList.createEntityOfType(EntityDragonPart.class, this, "tail", 4.0F, 4.0F),
         this.dragonPartTail2 = (EntityDragonPart)EntityList.createEntityOfType(EntityDragonPart.class, this, "tail", 4.0F, 4.0F),
         this.dragonPartTail3 = (EntityDragonPart)EntityList.createEntityOfType(EntityDragonPart.class, this, "tail", 4.0F, 4.0F),
         this.dragonPartWing1 = (EntityDragonPart)EntityList.createEntityOfType(EntityDragonPart.class, this, "wing", 4.0F, 4.0F),
         this.dragonPartWing2 = (EntityDragonPart)EntityList.createEntityOfType(EntityDragonPart.class, this, "wing", 4.0F, 4.0F)
      };
      this.b(this.getMaxHealth());
      this.texture = "/mob/enderdragon/ender.png";
      this.a(16.0F, 8.0F);
      this.noClip = true;
      this.isImmuneToFire = true;
      this.targetY = 100.0;
      this.ignoreFrustumCheck = true;
   }

   @Override
   public int getMaxHealth() {
      return 200;
   }

   @Override
   protected void entityInit() {
      super.entityInit();
      this.dataWatcher.addObject(16, new Integer(this.getMaxHealth()));
   }

   public double[] getMovementOffsets(int par1, float par2) {
      if (this.health <= 0) {
         par2 = 0.0F;
      }

      par2 = 1.0F - par2;
      int var3 = this.ringBufferIndex - par1 * 1 & 63;
      int var4 = this.ringBufferIndex - par1 * 1 - 1 & 63;
      double[] var5 = new double[3];
      double var6 = this.ringBuffer[var3][0];
      double var8 = MathHelper.wrapAngleTo180_double(this.ringBuffer[var4][0] - var6);
      var5[0] = var6 + var8 * par2;
      var6 = this.ringBuffer[var3][1];
      var8 = this.ringBuffer[var4][1] - var6;
      var5[1] = var6 + var8 * par2;
      var5[2] = this.ringBuffer[var3][2] + (this.ringBuffer[var4][2] - this.ringBuffer[var3][2]) * par2;
      return var5;
   }

   @Override
   public void onLivingUpdate() {
      if (!this.worldObj.isRemote) {
         this.dataWatcher.updateObject(16, this.health);
      } else {
         float var1 = MathHelper.cos(this.animTime * (float) Math.PI * 2.0F);
         float var2 = MathHelper.cos(this.prevAnimTime * (float) Math.PI * 2.0F);
         if (var2 <= -0.3F && var1 >= -0.3F) {
            this.worldObj.playSound(this.posX, this.posY, this.posZ, "mob.enderdragon.wings", 5.0F, 0.8F + this.rand.nextFloat() * 0.3F, false);
         }
      }

      this.prevAnimTime = this.animTime;
      if (this.health <= 0) {
         float var1x = (this.rand.nextFloat() - 0.5F) * 8.0F;
         float var2x = (this.rand.nextFloat() - 0.5F) * 4.0F;
         float var3 = (this.rand.nextFloat() - 0.5F) * 8.0F;
         this.worldObj.spawnParticle("largeexplode", this.posX + var1x, this.posY + 2.0 + var2x, this.posZ + var3, 0.0, 0.0, 0.0);
      } else {
         this.updateDragonEnderCrystal();
         float var1x = 0.2F / (MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ) * 10.0F + 1.0F);
         var1x *= (float)Math.pow(2.0, this.motionY);
         if (this.slowed) {
            this.animTime += var1x * 0.5F;
         } else {
            this.animTime += var1x;
         }

         this.rotationYaw = MathHelper.wrapAngleTo180_float(this.rotationYaw);
         if (this.ringBufferIndex < 0) {
            for (int var25 = 0; var25 < this.ringBuffer.length; var25++) {
               this.ringBuffer[var25][0] = this.rotationYaw;
               this.ringBuffer[var25][1] = this.posY;
            }
         }

         if (++this.ringBufferIndex == this.ringBuffer.length) {
            this.ringBufferIndex = 0;
         }

         this.ringBuffer[this.ringBufferIndex][0] = this.rotationYaw;
         this.ringBuffer[this.ringBufferIndex][1] = this.posY;
         if (this.worldObj.isRemote) {
            if (this.newPosRotationIncrements > 0) {
               double var26 = this.posX + (this.newPosX - this.posX) / this.newPosRotationIncrements;
               double var4 = this.posY + (this.newPosY - this.posY) / this.newPosRotationIncrements;
               double var6 = this.posZ + (this.newPosZ - this.posZ) / this.newPosRotationIncrements;
               double var8 = MathHelper.wrapAngleTo180_double(this.newRotationYaw - this.rotationYaw);
               this.rotationYaw = (float)(this.rotationYaw + var8 / this.newPosRotationIncrements);
               this.rotationPitch = (float)(this.rotationPitch + (this.newRotationPitch - this.rotationPitch) / this.newPosRotationIncrements);
               this.newPosRotationIncrements--;
               this.b(var26, var4, var6);
               this.b(this.rotationYaw, this.rotationPitch);
            }
         } else {
            double var26 = this.targetX - this.posX;
            double var4 = this.targetY - this.posY;
            double var6 = this.targetZ - this.posZ;
            double var8 = var26 * var26 + var4 * var4 + var6 * var6;
            if (this.target != null) {
               this.targetX = this.target.posX;
               this.targetZ = this.target.posZ;
               double var10 = this.targetX - this.posX;
               double var12 = this.targetZ - this.posZ;
               double var14 = Math.sqrt(var10 * var10 + var12 * var12);
               double var16 = 0.4F + var14 / 80.0 - 1.0;
               if (var16 > 10.0) {
                  var16 = 10.0;
               }

               this.targetY = this.target.boundingBox.minY + var16;
            } else {
               this.targetX = this.targetX + this.rand.nextGaussian() * 2.0;
               this.targetZ = this.targetZ + this.rand.nextGaussian() * 2.0;
            }

            if (this.forceNewTarget || var8 < 100.0 || var8 > 22500.0 || this.isCollidedHorizontally || this.isCollidedVertically) {
               this.setNewTarget();
            }

            var4 /= MathHelper.sqrt_double(var26 * var26 + var6 * var6);
            float var33 = 0.6F;
            if (var4 < -var33) {
               var4 = -var33;
            }

            if (var4 > var33) {
               var4 = var33;
            }

            this.motionY += var4 * 0.1F;
            this.rotationYaw = MathHelper.wrapAngleTo180_float(this.rotationYaw);
            double var11 = 180.0 - Math.atan2(var26, var6) * 180.0 / Math.PI;
            double var13 = MathHelper.wrapAngleTo180_double(var11 - this.rotationYaw);
            if (var13 > 50.0) {
               var13 = 50.0;
            }

            if (var13 < -50.0) {
               var13 = -50.0;
            }

            Vec3 var15 = this.worldObj
               .getWorldVec3Pool()
               .getVecFromPool(this.targetX - this.posX, this.targetY - this.posY, this.targetZ - this.posZ)
               .normalize();
            Vec3 var40 = this.worldObj
               .getWorldVec3Pool()
               .getVecFromPool(
                  MathHelper.sin(this.rotationYaw * (float) Math.PI / 180.0F), this.motionY, -MathHelper.cos(this.rotationYaw * (float) Math.PI / 180.0F)
               )
               .normalize();
            float var17 = (float)(var40.dotProduct(var15) + 0.5) / 1.5F;
            if (var17 < 0.0F) {
               var17 = 0.0F;
            }

            this.randomYawVelocity *= 0.8F;
            float var18 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ) * 1.0F + 1.0F;
            double var19 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ) * 1.0 + 1.0;
            if (var19 > 40.0) {
               var19 = 40.0;
            }

            this.randomYawVelocity = (float)(this.randomYawVelocity + var13 * (0.7F / var19 / var18));
            this.rotationYaw = this.rotationYaw + this.randomYawVelocity * 0.1F;
            float var21 = (float)(2.0 / (var19 + 1.0));
            float var22 = 0.06F;
            this.a(0.0F, -1.0F, var22 * (var17 * var21 + (1.0F - var21)));
            if (this.slowed) {
               this.d(this.motionX * 0.8F, this.motionY * 0.8F, this.motionZ * 0.8F);
            } else {
               this.d(this.motionX, this.motionY, this.motionZ);
            }

            Vec3 var23 = this.worldObj.getWorldVec3Pool().getVecFromPool(this.motionX, this.motionY, this.motionZ).normalize();
            float var24 = (float)(var23.dotProduct(var40) + 1.0) / 2.0F;
            var24 = 0.8F + 0.15F * var24;
            this.motionX *= var24;
            this.motionZ *= var24;
            this.motionY *= 0.91F;
         }

         this.renderYawOffset = this.rotationYaw;
         this.dragonPartHead.width = this.dragonPartHead.height = 3.0F;
         this.dragonPartTail1.width = this.dragonPartTail1.height = 2.0F;
         this.dragonPartTail2.width = this.dragonPartTail2.height = 2.0F;
         this.dragonPartTail3.width = this.dragonPartTail3.height = 2.0F;
         this.dragonPartBody.height = 3.0F;
         this.dragonPartBody.width = 5.0F;
         this.dragonPartWing1.height = 2.0F;
         this.dragonPartWing1.width = 4.0F;
         this.dragonPartWing2.height = 3.0F;
         this.dragonPartWing2.width = 4.0F;
         float var2x = (float)(this.getMovementOffsets(5, 1.0F)[1] - this.getMovementOffsets(10, 1.0F)[1]) * 10.0F / 180.0F * (float) Math.PI;
         float var3 = MathHelper.cos(var2x);
         float var28 = -MathHelper.sin(var2x);
         float var5 = this.rotationYaw * (float) Math.PI / 180.0F;
         float var27 = MathHelper.sin(var5);
         float var7 = MathHelper.cos(var5);
         this.dragonPartBody.l_();
         this.dragonPartBody.b(this.posX + var27 * 0.5F, this.posY, this.posZ - var7 * 0.5F, 0.0F, 0.0F);
         this.dragonPartWing1.l_();
         this.dragonPartWing1.b(this.posX + var7 * 4.5F, this.posY + 2.0, this.posZ + var27 * 4.5F, 0.0F, 0.0F);
         this.dragonPartWing2.l_();
         this.dragonPartWing2.b(this.posX - var7 * 4.5F, this.posY + 2.0, this.posZ - var27 * 4.5F, 0.0F, 0.0F);
         if (!this.worldObj.isRemote && this.hurtTime == 0) {
            this.collideWithEntities(
               this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.dragonPartWing1.boundingBox.expand(4.0, 2.0, 4.0).offset(0.0, -2.0, 0.0))
            );
            this.collideWithEntities(
               this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.dragonPartWing2.boundingBox.expand(4.0, 2.0, 4.0).offset(0.0, -2.0, 0.0))
            );
            this.attackEntitiesInList(this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.dragonPartHead.boundingBox.expand(1.0, 1.0, 1.0)));
         }

         double[] var29 = this.getMovementOffsets(5, 1.0F);
         double[] var9 = this.getMovementOffsets(0, 1.0F);
         float var33x = MathHelper.sin(this.rotationYaw * (float) Math.PI / 180.0F - this.randomYawVelocity * 0.01F);
         float var32 = MathHelper.cos(this.rotationYaw * (float) Math.PI / 180.0F - this.randomYawVelocity * 0.01F);
         this.dragonPartHead.l_();
         this.dragonPartHead
            .b(this.posX + var33x * 5.5F * var3, this.posY + (var9[1] - var29[1]) * 1.0 + var28 * 5.5F, this.posZ - var32 * 5.5F * var3, 0.0F, 0.0F);

         for (int var30 = 0; var30 < 3; var30++) {
            EntityDragonPart var31 = null;
            if (var30 == 0) {
               var31 = this.dragonPartTail1;
            }

            if (var30 == 1) {
               var31 = this.dragonPartTail2;
            }

            if (var30 == 2) {
               var31 = this.dragonPartTail3;
            }

            double[] var35 = this.getMovementOffsets(12 + var30 * 2, 1.0F);
            float var34 = this.rotationYaw * (float) Math.PI / 180.0F + this.simplifyAngle(var35[0] - var29[0]) * (float) Math.PI / 180.0F * 1.0F;
            float var38 = MathHelper.sin(var34);
            float var37 = MathHelper.cos(var34);
            float var36 = 1.5F;
            float var39 = (var30 + 1) * 2.0F;
            var31.l_();
            var31.b(
               this.posX - (var27 * var36 + var38 * var39) * var3,
               this.posY + (var35[1] - var29[1]) * 1.0 - (var39 + var36) * var28 + 1.5,
               this.posZ + (var7 * var36 + var37 * var39) * var3,
               0.0F,
               0.0F
            );
         }

         if (!this.worldObj.isRemote) {
            this.slowed = this.destroyBlocksInAABB(this.dragonPartHead.boundingBox) | this.destroyBlocksInAABB(this.dragonPartBody.boundingBox);
         }
      }
   }

   private void updateDragonEnderCrystal() {
      if (this.healingEnderCrystal != null) {
         if (this.healingEnderCrystal.isDead) {
            if (!this.worldObj.isRemote) {
               this.attackEntityFromPart(this.dragonPartHead, DamageSource.setExplosionSource((Explosion)null), 10);
            }

            this.healingEnderCrystal = null;
         } else if (this.ticksExisted % 10 == 0 && this.aX() < this.getMaxHealth()) {
            this.b(this.aX() + 1);
         }
      }

      if (this.rand.nextInt(10) == 0) {
         float var1 = 32.0F;
         List var2 = this.worldObj.getEntitiesWithinAABB(EntityEnderCrystal.class, this.boundingBox.expand(var1, var1, var1));
         EntityEnderCrystal var3 = null;
         double var4 = Double.MAX_VALUE;

         for (EntityEnderCrystal var7 : var2) {
            double var8 = var7.e(this);
            if (var8 < var4) {
               var4 = var8;
               var3 = var7;
            }
         }

         this.healingEnderCrystal = var3;
      }
   }

   private void collideWithEntities(List par1List) {
      double var2 = (this.dragonPartBody.boundingBox.minX + this.dragonPartBody.boundingBox.maxX) / 2.0;
      double var4 = (this.dragonPartBody.boundingBox.minZ + this.dragonPartBody.boundingBox.maxZ) / 2.0;

      for (Entity var7 : par1List) {
         if (var7 instanceof EntityLiving) {
            double var8 = var7.posX - var2;
            double var10 = var7.posZ - var4;
            double var12 = var8 * var8 + var10 * var10;
            var7.addVelocity(var8 / var12 * 4.0, 0.2F, var10 / var12 * 4.0);
         }
      }
   }

   private void attackEntitiesInList(List par1List) {
      for (int var2 = 0; var2 < par1List.size(); var2++) {
         Entity var3 = (Entity)par1List.get(var2);
         if (var3 instanceof EntityLiving) {
            var3.attackEntityFrom(DamageSource.causeMobDamage(this), 10);
         }
      }
   }

   private void setNewTarget() {
      this.forceNewTarget = false;
      boolean bTargetSelected = false;
      if (this.rand.nextInt(2) == 0 && !this.worldObj.playerEntities.isEmpty()) {
         this.target = (Entity)this.worldObj.playerEntities.get(this.rand.nextInt(this.worldObj.playerEntities.size()));
         long lTargetChangedDimensionTime = ((EntityPlayer)this.target).timeOfLastDimensionSwitch;
         long lWorldTime = this.worldObj.getWorldTime();
         if (lWorldTime < lTargetChangedDimensionTime || lWorldTime - lTargetChangedDimensionTime > 600L) {
            bTargetSelected = true;
         }
      }

      if (!bTargetSelected) {
         boolean var1 = false;

         do {
            this.targetX = 0.0;
            this.targetY = 70.0F + this.rand.nextFloat() * 50.0F;
            this.targetZ = 0.0;
            this.targetX = this.targetX + (this.rand.nextFloat() * 120.0F - 60.0F);
            this.targetZ = this.targetZ + (this.rand.nextFloat() * 120.0F - 60.0F);
            double var2 = this.posX - this.targetX;
            double var4 = this.posY - this.targetY;
            double var6 = this.posZ - this.targetZ;
            var1 = var2 * var2 + var4 * var4 + var6 * var6 > 100.0;
         } while (!var1);

         this.target = null;
      }
   }

   private float simplifyAngle(double par1) {
      return (float)MathHelper.wrapAngleTo180_double(par1);
   }

   private boolean destroyBlocksInAABB(AxisAlignedBB par1AxisAlignedBB) {
      int var2 = MathHelper.floor_double(par1AxisAlignedBB.minX);
      int var3 = MathHelper.floor_double(par1AxisAlignedBB.minY);
      int var4 = MathHelper.floor_double(par1AxisAlignedBB.minZ);
      int var5 = MathHelper.floor_double(par1AxisAlignedBB.maxX);
      int var6 = MathHelper.floor_double(par1AxisAlignedBB.maxY);
      int var7 = MathHelper.floor_double(par1AxisAlignedBB.maxZ);
      boolean var8 = false;
      boolean var9 = false;

      for (int var10 = var2; var10 <= var5; var10++) {
         for (int var11 = var3; var11 <= var6; var11++) {
            for (int var12 = var4; var12 <= var7; var12++) {
               int var13 = this.worldObj.getBlockId(var10, var11, var12);
               if (var13 != 0) {
                  if (var13 != Block.obsidian.blockID
                     && var13 != Block.whiteStone.blockID
                     && var13 != Block.bedrock.blockID
                     && this.worldObj.getGameRules().getGameRuleBooleanValue("mobGriefing")) {
                     var9 = this.worldObj.setBlockToAir(var10, var11, var12) || var9;
                  } else {
                     var8 = true;
                  }
               }
            }
         }
      }

      if (var9) {
         double var16 = par1AxisAlignedBB.minX + (par1AxisAlignedBB.maxX - par1AxisAlignedBB.minX) * this.rand.nextFloat();
         double var17 = par1AxisAlignedBB.minY + (par1AxisAlignedBB.maxY - par1AxisAlignedBB.minY) * this.rand.nextFloat();
         double var14 = par1AxisAlignedBB.minZ + (par1AxisAlignedBB.maxZ - par1AxisAlignedBB.minZ) * this.rand.nextFloat();
         this.worldObj.spawnParticle("largeexplode", var16, var17, var14, 0.0, 0.0, 0.0);
      }

      return var8;
   }

   @Override
   public boolean attackEntityFromPart(EntityDragonPart par1EntityDragonPart, DamageSource par2DamageSource, int par3) {
      if (par1EntityDragonPart != this.dragonPartHead) {
         par3 = par3 / 4 + 1;
      }

      float var4 = this.rotationYaw * (float) Math.PI / 180.0F;
      float var5 = MathHelper.sin(var4);
      float var6 = MathHelper.cos(var4);
      this.targetX = this.posX + var5 * 5.0F + (this.rand.nextFloat() - 0.5F) * 2.0F;
      this.targetY = this.posY + this.rand.nextFloat() * 3.0F + 1.0;
      this.targetZ = this.posZ - var6 * 5.0F + (this.rand.nextFloat() - 0.5F) * 2.0F;
      this.target = null;
      if (par2DamageSource.getEntity() instanceof EntityPlayer || par2DamageSource.isExplosion()) {
         this.func_82195_e(par2DamageSource, par3);
      }

      return true;
   }

   @Override
   public boolean attackEntityFrom(DamageSource par1DamageSource, int par2) {
      return false;
   }

   protected boolean func_82195_e(DamageSource par1DamageSource, int par2) {
      return super.attackEntityFrom(par1DamageSource, par2);
   }

   @Override
   protected void onDeathUpdate() {
      this.deathTicks++;
      if (this.deathTicks >= 180 && this.deathTicks <= 200) {
         float var1 = (this.rand.nextFloat() - 0.5F) * 8.0F;
         float var2 = (this.rand.nextFloat() - 0.5F) * 4.0F;
         float var3 = (this.rand.nextFloat() - 0.5F) * 8.0F;
         this.worldObj.spawnParticle("hugeexplosion", this.posX + var1, this.posY + 2.0 + var2, this.posZ + var3, 0.0, 0.0, 0.0);
      }

      if (!this.worldObj.isRemote) {
         if (this.deathTicks > 150 && this.deathTicks % 5 == 0) {
            int var4 = 1000;

            while (var4 > 0) {
               int var5 = EntityXPOrb.getXPSplit(var4);
               var4 -= var5;
               this.worldObj.spawnEntityInWorld(EntityList.createEntityOfType(EntityXPOrb.class, this.worldObj, this.posX, this.posY, this.posZ, var5));
            }
         }

         if (this.deathTicks == 1) {
            this.worldObj.func_82739_e(1018, (int)this.posX, (int)this.posY, (int)this.posZ, 0);
         }
      }

      this.d(0.0, 0.1F, 0.0);
      this.renderYawOffset = this.rotationYaw += 20.0F;
      if (this.deathTicks == 200 && !this.worldObj.isRemote) {
         int var4 = 2000;

         while (var4 > 0) {
            int var5 = EntityXPOrb.getXPSplit(var4);
            var4 -= var5;
            this.worldObj.spawnEntityInWorld(EntityList.createEntityOfType(EntityXPOrb.class, this.worldObj, this.posX, this.posY, this.posZ, var5));
         }

         this.createEnderPortal(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posZ));
         this.w();
      }
   }

   private void createEnderPortal(int par1, int par2) {
      byte var3 = 64;
      BlockEndPortal.bossDefeated = true;
      byte var4 = 4;

      for (int var5 = var3 - 1; var5 <= var3 + 32; var5++) {
         for (int var6 = par1 - var4; var6 <= par1 + var4; var6++) {
            for (int var7 = par2 - var4; var7 <= par2 + var4; var7++) {
               double var8 = var6 - par1;
               double var10 = var7 - par2;
               double var12 = var8 * var8 + var10 * var10;
               if (var12 <= (var4 - 0.5) * (var4 - 0.5)) {
                  if (var5 < var3) {
                     if (var12 <= (var4 - 1 - 0.5) * (var4 - 1 - 0.5)) {
                        this.worldObj.setBlock(var6, var5, var7, Block.bedrock.blockID);
                     }
                  } else if (var5 > var3) {
                     this.worldObj.setBlock(var6, var5, var7, 0);
                  } else if (var12 > (var4 - 1 - 0.5) * (var4 - 1 - 0.5)) {
                     this.worldObj.setBlock(var6, var5, var7, Block.bedrock.blockID);
                  } else {
                     this.worldObj.setBlock(var6, var5, var7, Block.endPortal.blockID);
                  }
               }
            }
         }
      }

      this.worldObj.setBlock(par1, var3 + 0, par2, Block.bedrock.blockID);
      this.worldObj.setBlock(par1, var3 + 1, par2, Block.bedrock.blockID);
      this.worldObj.setBlock(par1, var3 + 2, par2, Block.bedrock.blockID);
      this.worldObj.setBlock(par1 - 1, var3 + 2, par2, Block.torchWood.blockID);
      this.worldObj.setBlock(par1 + 1, var3 + 2, par2, Block.torchWood.blockID);
      this.worldObj.setBlock(par1, var3 + 2, par2 - 1, Block.torchWood.blockID);
      this.worldObj.setBlock(par1, var3 + 2, par2 + 1, Block.torchWood.blockID);
      this.worldObj.setBlock(par1, var3 + 3, par2, Block.bedrock.blockID);
      this.worldObj.setBlock(par1, var3 + 4, par2, Block.dragonEgg.blockID);
      BlockEndPortal.bossDefeated = false;
   }

   @Override
   protected void despawnEntity() {
   }

   @Override
   public Entity[] getParts() {
      return this.dragonPartArray;
   }

   @Override
   public boolean canBeCollidedWith() {
      return false;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public int getBossHealth() {
      return this.dataWatcher.getWatchableObjectInt(16);
   }

   @Override
   public World func_82194_d() {
      return this.worldObj;
   }

   @Override
   protected String getLivingSound() {
      return "mob.enderdragon.growl";
   }

   @Override
   protected String getHurtSound() {
      return "mob.enderdragon.hit";
   }

   @Override
   protected float getSoundVolume() {
      return 5.0F;
   }
}
