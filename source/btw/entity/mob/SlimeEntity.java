package btw.entity.mob;

import btw.item.BTWItems;
import net.minecraft.src.Block;
import net.minecraft.src.DamageSource;
import net.minecraft.src.Enchantment;
import net.minecraft.src.EntityList;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EntitySlime;
import net.minecraft.src.ItemStack;
import net.minecraft.src.MathHelper;
import net.minecraft.src.World;

public class SlimeEntity extends EntitySlime {
   private int jumpCountdown = 0;

   public SlimeEntity(World world) {
      super(world);
      this.jumpCountdown = this.j();
   }

   @Override
   protected void setSlimeSize(int iSize) {
      this.dataWatcher.updateObject(16, new Byte((byte)iSize));
      if (iSize == 1) {
         this.a(0.6F, 0.4F);
      } else {
         this.a(0.4F * iSize, 0.4F * iSize);
      }

      this.b(this.posX, this.posY, this.posZ);
      this.b(this.aW());
      this.experienceValue = iSize;
   }

   @Override
   protected boolean makesSoundOnLand() {
      if (super.makesSoundOnLand() && !this.inWater) {
         this.a(this.n(), this.getSoundVolume(), this.getSoundPitch() * ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F));
      }

      return false;
   }

   @Override
   protected void updateEntityActionState() {
      this.entityAge++;
      this.bn();
      EntityPlayer targetPlayer = this.worldObj.getClosestVulnerablePlayerToEntity(this, 16.0);
      if (targetPlayer != null) {
         this.a(targetPlayer, 10.0F, 20.0F);
      }

      this.isJumping = false;
      if (this.onGround) {
         this.jumpCountdown--;
         if (this.jumpCountdown <= 0) {
            this.playJumpSound();
            this.isJumping = true;
            this.jumpCountdown = this.j();
            this.moveForward = this.p();
            if (targetPlayer != null) {
               this.moveStrafing = 1.0F - this.rand.nextFloat() * 2.0F;
               this.jumpCountdown /= 6;
            } else {
               this.moveStrafing = 0.0F;
               if (this.rand.nextInt(4) == 0) {
                  this.rotationYaw = MathHelper.wrapAngleTo180_float(this.rand.nextInt(4) * 90.0F);
               }
            }
         } else {
            this.moveStrafing = this.moveForward = 0.0F;
         }
      }
   }

   @Override
   protected double minDistFromPlayerForDespawn() {
      return 64.0;
   }

   @Override
   public void onCollideWithPlayer(EntityPlayer player) {
      if (this.canDamagePlayer() && this.n(player) && player.attackEntityFrom(DamageSource.causeMobDamage(this), this.m())) {
         this.attackTime = 20;
         this.a("mob.slime.attack", 1.0F, this.getSoundPitch() * ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F));
      }
   }

   @Override
   protected boolean canDamagePlayer() {
      return this.R() && this.attackTime <= 0;
   }

   @Override
   public boolean getCanSpawnHere() {
      if (super.getCanSpawnHere()) {
         return this.posY < 40.0
            ? this.canSpawnOnBlockInSlimeChunk(
               MathHelper.floor_double(this.posX), MathHelper.floor_double(this.boundingBox.minY) - 1, MathHelper.floor_double(this.posZ)
            )
            : true;
      } else {
         return false;
      }
   }

   @Override
   protected float getSoundVolume() {
      return 0.1F * this.p();
   }

   @Override
   public boolean canBreatheUnderwater() {
      return true;
   }

   @Override
   public void checkForScrollDrop() {
      if (this.p() == 1 && this.rand.nextInt(1000) == 0) {
         ItemStack itemstack = new ItemStack(BTWItems.arcaneScroll, 1, Enchantment.protection.effectId);
         this.a(itemstack, 0.0F);
      }
   }

   @Override
   public boolean isAffectedByMovementModifiers() {
      return false;
   }

   @Override
   public void jump() {
      this.motionY = 0.5249999836087227;
      this.isAirBorne = true;
   }

   @Override
   public boolean canSwim() {
      return false;
   }

   @Override
   public float getDefaultSlipperinessOnGround() {
      return 0.819F;
   }

   @Override
   public float getSlipperinessRelativeToBlock(int iBlockID) {
      return this.getDefaultSlipperinessOnGround();
   }

   @Override
   protected void fall(float fFallDistance) {
   }

   @Override
   protected float getSoundPitch() {
      int iSize = this.p();
      if (iSize == 4) {
         return 0.75F;
      } else {
         return iSize == 2 ? 1.0F : 1.25F;
      }
   }

   @Override
   protected EntitySlime createInstance() {
      return (EntitySlime)EntityList.createEntityOfType(SlimeEntity.class, this.worldObj);
   }

   private boolean canSpawnOnBlockInSlimeChunk(int i, int j, int k) {
      int iBlockID = this.worldObj.getBlockId(i, j, k);
      return iBlockID == Block.dirt.blockID
         || iBlockID == Block.stone.blockID
         || iBlockID == Block.grass.blockID
         || iBlockID == Block.gravel.blockID
         || iBlockID == Block.sand.blockID;
   }

   private void playJumpSound() {
      if (this.q()) {
         if (!this.inWater) {
            this.a(this.n(), this.getSoundVolume(), this.getSoundPitch() * ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F));
         } else {
            this.a("liquid.swim", 0.25F, this.getSoundPitch() * (1.0F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.4F));
         }
      }
   }
}
