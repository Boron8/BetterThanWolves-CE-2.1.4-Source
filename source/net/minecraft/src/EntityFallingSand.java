package net.minecraft.src;

import java.util.ArrayList;

public class EntityFallingSand extends Entity {
   public int blockID;
   public int metadata;
   public int fallTime = 0;
   public boolean shouldDropItem = true;
   private boolean isBreakingAnvil = false;
   private boolean isAnvil = false;
   private int fallHurtMax = 40;
   private float fallHurtAmount = 2.0F;
   public NBTTagCompound fallingBlockTileEntityData = null;

   public EntityFallingSand(World var1) {
      super(var1);
   }

   public EntityFallingSand(World var1, double var2, double var4, double var6, int var8) {
      this(var1, var2, var4, var6, var8, 0);
   }

   public EntityFallingSand(World var1, double var2, double var4, double var6, int var8, int var9) {
      super(var1);
      this.blockID = var8;
      this.metadata = var9;
      this.preventEntitySpawning = true;
      this.a(0.98F, 0.98F);
      this.yOffset = this.height / 2.0F;
      this.b(var2, var4, var6);
      this.motionX = 0.0;
      this.motionY = 0.0;
      this.motionZ = 0.0;
      this.prevPosX = var2;
      this.prevPosY = var4;
      this.prevPosZ = var6;
   }

   @Override
   protected boolean canTriggerWalking() {
      return false;
   }

   @Override
   protected void entityInit() {
   }

   @Override
   public boolean canBeCollidedWith() {
      return !this.isDead;
   }

   @Override
   public void onUpdate() {
      if (this.blockID == 0) {
         this.w();
      } else {
         this.prevPosX = this.posX;
         this.prevPosY = this.posY;
         this.prevPosZ = this.posZ;
         this.fallTime++;
         this.motionY -= 0.04F;
         this.d(this.motionX, this.motionY, this.motionZ);
         this.motionX *= 0.98F;
         this.motionY *= 0.98F;
         this.motionZ *= 0.98F;
         if (!this.worldObj.isRemote) {
            int var1 = MathHelper.floor_double(this.posX);
            int var2 = MathHelper.floor_double(this.posY);
            int var3 = MathHelper.floor_double(this.posZ);
            if (this.fallTime == 1) {
               if (this.worldObj.getBlockId(var1, var2, var3) != this.blockID) {
                  this.w();
                  return;
               }

               this.worldObj.setBlockToAir(var1, var2, var3);
            }

            if (this.onGround) {
               this.motionX *= 0.7F;
               this.motionZ *= 0.7F;
               this.motionY *= -0.5;
               if (this.worldObj.getBlockId(var1, var2, var3) != Block.pistonMoving.blockID) {
                  this.w();
                  if (!this.isBreakingAnvil
                     && this.worldObj.canPlaceEntityOnSide(this.blockID, var1, var2, var3, true, 1, null, null)
                     && !BlockSand.canFallBelow(this.worldObj, var1, var2 - 1, var3)
                     && this.worldObj.setBlock(var1, var2, var3, this.blockID, this.metadata, 3)) {
                     if (Block.blocksList[this.blockID] instanceof BlockSand) {
                        ((BlockSand)Block.blocksList[this.blockID]).onFinishFalling(this.worldObj, var1, var2, var3, this.metadata);
                     }

                     if (this.fallingBlockTileEntityData != null && Block.blocksList[this.blockID] instanceof ITileEntityProvider) {
                        TileEntity var4 = this.worldObj.getBlockTileEntity(var1, var2, var3);
                        if (var4 != null) {
                           NBTTagCompound var5 = new NBTTagCompound();
                           var4.writeToNBT(var5);

                           for (NBTBase var7 : this.fallingBlockTileEntityData.getTags()) {
                              if (!var7.getName().equals("x") && !var7.getName().equals("y") && !var7.getName().equals("z")) {
                                 var5.setTag(var7.getName(), var7.copy());
                              }
                           }

                           var4.readFromNBT(var5);
                           var4.onInventoryChanged();
                        }
                     }
                  } else if (this.shouldDropItem && !this.isBreakingAnvil) {
                     this.a(new ItemStack(this.blockID, 1, Block.blocksList[this.blockID].damageDropped(this.metadata)), 0.0F);
                  }
               }
            } else if (this.fallTime > 100 && !this.worldObj.isRemote && (var2 < 1 || var2 > 256) || this.fallTime > 600) {
               if (this.shouldDropItem) {
                  this.a(new ItemStack(this.blockID, 1, Block.blocksList[this.blockID].damageDropped(this.metadata)), 0.0F);
               }

               this.w();
            }
         }
      }
   }

   @Override
   protected void fall(float var1) {
      if (this.isAnvil) {
         int var2 = MathHelper.ceiling_float_int(var1 - 1.0F);
         if (var2 > 0) {
            ArrayList var3 = new ArrayList(this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox));
            DamageSource var4 = this.blockID == Block.anvil.blockID ? DamageSource.anvil : DamageSource.fallingBlock;

            for (Entity var6 : var3) {
               var6.attackEntityFrom(var4, Math.min(MathHelper.floor_float(var2 * this.fallHurtAmount), this.fallHurtMax));
            }

            if (this.blockID == Block.anvil.blockID && this.rand.nextFloat() < 0.05F + var2 * 0.05) {
               int var7 = this.metadata >> 2;
               int var9 = this.metadata & 3;
               if (++var7 > 2) {
                  this.isBreakingAnvil = true;
               } else {
                  this.metadata = var9 | var7 << 2;
               }
            }
         }
      }
   }

   @Override
   protected void writeEntityToNBT(NBTTagCompound var1) {
      var1.setByte("Tile", (byte)this.blockID);
      var1.setInteger("TileID", this.blockID);
      var1.setByte("Data", (byte)this.metadata);
      var1.setByte("Time", (byte)this.fallTime);
      var1.setBoolean("DropItem", this.shouldDropItem);
      var1.setBoolean("HurtEntities", this.isAnvil);
      var1.setFloat("FallHurtAmount", this.fallHurtAmount);
      var1.setInteger("FallHurtMax", this.fallHurtMax);
      if (this.fallingBlockTileEntityData != null) {
         var1.setCompoundTag("TileEntityData", this.fallingBlockTileEntityData);
      }
   }

   @Override
   protected void readEntityFromNBT(NBTTagCompound var1) {
      if (var1.hasKey("TileID")) {
         this.blockID = var1.getInteger("TileID");
      } else {
         this.blockID = var1.getByte("Tile") & 255;
      }

      this.metadata = var1.getByte("Data") & 255;
      this.fallTime = var1.getByte("Time") & 255;
      if (var1.hasKey("HurtEntities")) {
         this.isAnvil = var1.getBoolean("HurtEntities");
         this.fallHurtAmount = var1.getFloat("FallHurtAmount");
         this.fallHurtMax = var1.getInteger("FallHurtMax");
      } else if (this.blockID == Block.anvil.blockID) {
         this.isAnvil = true;
      }

      if (var1.hasKey("DropItem")) {
         this.shouldDropItem = var1.getBoolean("DropItem");
      }

      if (var1.hasKey("TileEntityData")) {
         this.fallingBlockTileEntityData = var1.getCompoundTag("TileEntityData");
      }

      if (this.blockID == 0) {
         this.blockID = Block.sand.blockID;
      }
   }

   @Override
   public float getShadowSize() {
      return 0.0F;
   }

   public World getWorld() {
      return this.worldObj;
   }

   public void setIsAnvil(boolean var1) {
      this.isAnvil = var1;
   }

   @Override
   public boolean canRenderOnFire() {
      return false;
   }

   @Override
   public void func_85029_a(CrashReportCategory var1) {
      super.func_85029_a(var1);
      var1.addCrashSection("Immitating block ID", this.blockID);
      var1.addCrashSection("Immitating block data", this.metadata);
   }
}
