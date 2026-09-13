package btw.entity;

import java.util.ArrayList;
import net.minecraft.src.Block;
import net.minecraft.src.DamageSource;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityFallingSand;
import net.minecraft.src.ItemStack;
import net.minecraft.src.MathHelper;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.World;

public class FallingBlockEntity extends EntityFallingSand {
   protected boolean hasBlockBrokenOnLand = false;
   protected boolean hurtsEntities = false;
   protected float hurtsAmount = 2.0F;
   protected int hurtsMaxDamage = 40;

   public FallingBlockEntity(World world) {
      super(world);
   }

   public FallingBlockEntity(World world, double dPosX, double dPosY, double dPosZ, int iBlockID, int iBlockMetadata) {
      super(world, dPosX, dPosY, dPosZ, iBlockID, iBlockMetadata);
   }

   @Override
   protected void readEntityFromNBT(NBTTagCompound tag) {
      super.readEntityFromNBT(tag);
      if (tag.hasKey("HurtEntities")) {
         this.hurtsEntities = tag.getBoolean("HurtEntities");
         this.hurtsAmount = tag.getFloat("FallHurtAmount");
         this.hurtsMaxDamage = tag.getInteger("FallHurtMax");
      } else if (this.blockID == Block.anvil.blockID) {
         this.hurtsEntities = true;
      }
   }

   @Override
   public void onUpdate() {
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
         int i = MathHelper.floor_double(this.posX);
         int j = MathHelper.floor_double(this.posY);
         int k = MathHelper.floor_double(this.posZ);
         if (this.fallTime == 1) {
            if (this.worldObj.getBlockId(i, j, k) != this.blockID) {
               this.w();
               return;
            }

            this.worldObj.setBlockToAir(i, j, k);
         }

         if (this.onGround) {
            this.motionX *= 0.7F;
            this.motionZ *= 0.7F;
            this.motionY *= -0.5;
            int iBlockBelowID = this.worldObj.getBlockId(i, j - 1, k);
            Block blockBelow = Block.blocksList[iBlockBelowID];
            if (blockBelow != null && blockBelow.canBeCrushedByFallingEntity(this.worldObj, i, j - 1, k, this)) {
               blockBelow.onCrushedByFallingEntity(this.worldObj, i, j - 1, k, this);
               this.worldObj.setBlockToAir(i, j - 1, k);
            } else if (this.worldObj.getBlockId(i, j, k) != Block.pistonMoving.blockID) {
               this.w();
               if (this.attemptToReplaceBlockAtPosition(i, j, k)) {
                  Block.blocksList[this.blockID].a_(this.worldObj, i, j, k, this.metadata);
               } else if (this.shouldDropItem && !this.hasBlockBrokenOnLand) {
                  int iDestinationBlockID = this.worldObj.getBlockId(i, j, k);
                  if (iDestinationBlockID != 0 && Block.blocksList[iDestinationBlockID].attemptToCombineWithFallingEntity(this.worldObj, i, j, k, this)) {
                     return;
                  }

                  Block.blocksList[this.blockID].onBlockDestroyedLandingFromFall(this.worldObj, i, j, k, this.metadata);
               }
            }
         } else if (this.fallTime > 100 && (j < 1 || j > 256) || this.fallTime > 600) {
            if (this.shouldDropItem) {
               this.a(new ItemStack(this.blockID, 1, Block.blocksList[this.blockID].damageDropped(this.metadata)), 0.0F);
            }

            this.w();
         }
      }

      if (this.R()) {
         Block.blocksList[this.blockID].onFallingUpdate(this);
      }
   }

   @Override
   protected void fall(float fFallDistance) {
      if (this.hurtsEntities) {
         int iFallDamage = MathHelper.ceiling_float_int(fFallDistance - 1.0F);
         if (iFallDamage > 0) {
            for (Entity tempEntity : new ArrayList(this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox))) {
               tempEntity.attackEntityFrom(DamageSource.fallingBlock, Math.min(MathHelper.floor_float(iFallDamage * this.hurtsAmount), this.hurtsMaxDamage));
            }
         }
      }

      Block block = Block.blocksList[this.blockID];
      if (block != null && !block.onFinishedFalling(this, fFallDistance)) {
         this.hasBlockBrokenOnLand = true;
      }
   }

   @Override
   public void setIsAnvil(boolean bIsAnvil) {
      super.setIsAnvil(bIsAnvil);
      this.hurtsEntities = bIsAnvil;
   }

   private boolean attemptToReplaceBlockAtPosition(int i, int j, int k) {
      if (!this.hasBlockBrokenOnLand
         && this.canReplaceBlockAtPosition(i, j, k)
         && !Block.blocksList[this.blockID].canFallIntoBlockAtPos(this.worldObj, i, j - 1, k)) {
         Block destBlock = Block.blocksList[this.worldObj.getBlockId(i, j, k)];
         if (destBlock != null) {
            destBlock.onCrushedByFallingEntity(this.worldObj, i, j, k, this);
         }

         return this.worldObj.setBlock(i, j, k, this.blockID, this.metadata, 3);
      } else {
         return false;
      }
   }

   private boolean canReplaceBlockAtPosition(int i, int j, int k) {
      if (this.worldObj.canPlaceEntityOnSide(this.blockID, i, j, k, true, 1, (Entity)null, (ItemStack)null)) {
         return true;
      } else {
         Block destBlock = Block.blocksList[this.worldObj.getBlockId(i, j, k)];
         return destBlock != null && destBlock.canBeCrushedByFallingEntity(this.worldObj, i, j, k, this);
      }
   }
}
