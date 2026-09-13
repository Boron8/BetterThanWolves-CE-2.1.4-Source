package net.minecraft.src;

import btw.block.blocks.FireBlock;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Explosion {
   public boolean isFlaming = false;
   public boolean isSmoking = true;
   private int field_77289_h = 16;
   private Random explosionRNG = new Random();
   private World worldObj;
   public double explosionX;
   public double explosionY;
   public double explosionZ;
   public Entity exploder;
   public float explosionSize;
   public List affectedBlockPositions = new ArrayList();
   private Map field_77288_k = new HashMap();
   public boolean suppressFX = false;
   public List secondaryExplosionList = null;

   public Explosion(World par1World, Entity par2Entity, double par3, double par5, double par7, float par9) {
      this.worldObj = par1World;
      this.exploder = par2Entity;
      this.explosionSize = par9;
      this.explosionX = par3;
      this.explosionY = par5;
      this.explosionZ = par7;
   }

   public void doExplosionA() {
      float var1 = this.explosionSize;
      HashSet var2 = new HashSet();

      for (int var3 = 0; var3 < this.field_77289_h; var3++) {
         for (int var4 = 0; var4 < this.field_77289_h; var4++) {
            for (int var5 = 0; var5 < this.field_77289_h; var5++) {
               if (var3 == 0 || var3 == this.field_77289_h - 1 || var4 == 0 || var4 == this.field_77289_h - 1 || var5 == 0 || var5 == this.field_77289_h - 1) {
                  double var6 = var3 / (this.field_77289_h - 1.0F) * 2.0F - 1.0F;
                  double var8 = var4 / (this.field_77289_h - 1.0F) * 2.0F - 1.0F;
                  double var10 = var5 / (this.field_77289_h - 1.0F) * 2.0F - 1.0F;
                  double var12 = Math.sqrt(var6 * var6 + var8 * var8 + var10 * var10);
                  var6 /= var12;
                  var8 /= var12;
                  var10 /= var12;
                  float var14 = this.explosionSize * (0.7F + this.worldObj.rand.nextFloat() * 0.6F);
                  double var15 = this.explosionX;
                  double var17 = this.explosionY;
                  double var19 = this.explosionZ;

                  for (float var21 = 0.3F; var14 > 0.0F; var14 -= var21 * 0.75F) {
                     int var22 = MathHelper.floor_double(var15);
                     int var23 = MathHelper.floor_double(var17);
                     int var24 = MathHelper.floor_double(var19);
                     int var25 = this.worldObj.getBlockId(var22, var23, var24);
                     if (var25 > 0) {
                        Block var26 = Block.blocksList[var25];
                        float var27 = this.exploder != null
                           ? this.exploder.func_82146_a(this, this.worldObj, var22, var23, var24, var26)
                           : var26.getExplosionResistance(this.exploder, this.worldObj, var22, var23, var24);
                        var14 -= (var27 + 0.3F) * var21;
                     }

                     if (var14 > 0.0F && (this.exploder == null || this.exploder.func_96091_a(this, this.worldObj, var22, var23, var24, var25, var14))) {
                        var2.add(new ChunkPosition(var22, var23, var24));
                     }

                     var15 += var6 * var21;
                     var17 += var8 * var21;
                     var19 += var10 * var21;
                  }
               }
            }
         }
      }

      this.affectedBlockPositions.addAll(var2);
      this.explosionSize *= 2.0F;
      int var291 = MathHelper.floor_double(this.explosionX - this.explosionSize - 1.0);
      int var4 = MathHelper.floor_double(this.explosionX + this.explosionSize + 1.0);
      int var5x = MathHelper.floor_double(this.explosionY - this.explosionSize - 1.0);
      int var29x = MathHelper.floor_double(this.explosionY + this.explosionSize + 1.0);
      int var7 = MathHelper.floor_double(this.explosionZ - this.explosionSize - 1.0);
      int var30 = MathHelper.floor_double(this.explosionZ + this.explosionSize + 1.0);
      List var9 = this.worldObj
         .getEntitiesWithinAABBExcludingEntity(this.exploder, AxisAlignedBB.getAABBPool().getAABB(var291, var5x, var7, var4, var29x, var30));
      Vec3 var31 = this.worldObj.getWorldVec3Pool().getVecFromPool(this.explosionX, this.explosionY, this.explosionZ);

      for (int var11 = 0; var11 < var9.size(); var11++) {
         Entity var32 = (Entity)var9.get(var11);
         double var13 = var32.getDistance(this.explosionX, this.explosionY, this.explosionZ) / this.explosionSize;
         if (var13 <= 1.0) {
            double var15 = var32.posX - this.explosionX;
            double var17 = var32.posY + var32.getEyeHeight() - this.explosionY;
            double var19 = var32.posZ - this.explosionZ;
            double var34 = MathHelper.sqrt_double(var15 * var15 + var17 * var17 + var19 * var19);
            if (var34 != 0.0) {
               var15 /= var34;
               var17 /= var34;
               var19 /= var34;
               double var33 = this.worldObj.getBlockDensity(var31, var32.boundingBox);
               double var35 = (1.0 - var13) * var33;
               var32.attackEntityFrom(DamageSource.setExplosionSource(this), (int)((var35 * var35 + var35) / 2.0 * 8.0 * this.explosionSize + 1.0));
               double var36 = EnchantmentProtection.func_92092_a(var32, var35);
               var32.motionX += var15 * var36;
               var32.motionY += var17 * var36;
               var32.motionZ += var19 * var36;
               if (var32 instanceof EntityPlayer) {
                  this.field_77288_k.put((EntityPlayer)var32, this.worldObj.getWorldVec3Pool().getVecFromPool(var15 * var35, var17 * var35, var19 * var35));
               }
            }
         }
      }

      this.explosionSize = var1;
   }

   public void doExplosionB(boolean par1) {
      if (!this.suppressFX) {
         this.worldObj
            .playSoundEffect(
               this.explosionX,
               this.explosionY,
               this.explosionZ,
               "random.explode",
               4.0F,
               (1.0F + (this.worldObj.rand.nextFloat() - this.worldObj.rand.nextFloat()) * 0.2F) * 0.7F
            );
         if (this.explosionSize >= 2.0F && this.isSmoking) {
            this.worldObj.spawnParticle("hugeexplosion", this.explosionX, this.explosionY, this.explosionZ, 1.0, 0.0, 0.0);
         } else {
            this.worldObj.spawnParticle("largeexplode", this.explosionX, this.explosionY, this.explosionZ, 1.0, 0.0, 0.0);
         }
      }

      if (this.isSmoking) {
         for (ChunkPosition var3 : this.affectedBlockPositions) {
            int var4 = var3.x;
            int var5 = var3.y;
            int var6 = var3.z;
            int var7 = this.worldObj.getBlockId(var4, var5, var6);
            if (par1) {
               double var8 = var4 + this.worldObj.rand.nextFloat();
               double var10 = var5 + this.worldObj.rand.nextFloat();
               double var12 = var6 + this.worldObj.rand.nextFloat();
               double var14 = var8 - this.explosionX;
               double var16 = var10 - this.explosionY;
               double var18 = var12 - this.explosionZ;
               double var20 = MathHelper.sqrt_double(var14 * var14 + var16 * var16 + var18 * var18);
               var14 /= var20;
               var16 /= var20;
               var18 /= var20;
               double var22 = 0.5 / (var20 / this.explosionSize + 0.1);
               var22 *= this.worldObj.rand.nextFloat() * this.worldObj.rand.nextFloat() + 0.3F;
               var14 *= var22;
               var16 *= var22;
               var18 *= var22;
               this.worldObj
                  .spawnParticle(
                     "explode",
                     (var8 + this.explosionX * 1.0) / 2.0,
                     (var10 + this.explosionY * 1.0) / 2.0,
                     (var12 + this.explosionZ * 1.0) / 2.0,
                     var14,
                     var16,
                     var18
                  );
               this.worldObj.spawnParticle("smoke", var8, var10, var12, var14, var16, var18);
            }

            if (var7 > 0) {
               Block var25 = Block.blocksList[var7];
               var25.dropItemsOnDestroyedByExplosion(this.worldObj, var4, var5, var6, this);
               var25.onBlockDestroyedByExplosion(this.worldObj, var4, var5, var6, this);
               this.worldObj.setBlock(var4, var5, var6, 0, 0, 3);
               var25.postBlockDestroyedByExplosion(this.worldObj, var4, var5, var6, this);
            }
         }
      }

      if (this.isFlaming) {
         for (ChunkPosition var3 : this.affectedBlockPositions) {
            if (this.explosionRNG.nextInt(3) == 0
               && FireBlock.canFireReplaceBlock(this.worldObj, var3.x, var3.y, var3.z)
               && Block.fire.canPlaceBlockAt(this.worldObj, var3.x, var3.y, var3.z)) {
               this.worldObj.setBlock(var3.x, var3.y, var3.z, Block.fire.blockID);
            }
         }
      }

      this.performSecondaryExplosions();
   }

   public Map func_77277_b() {
      return this.field_77288_k;
   }

   public EntityLiving func_94613_c() {
      return this.exploder == null
         ? null
         : (
            this.exploder instanceof EntityTNTPrimed
               ? ((EntityTNTPrimed)this.exploder).getTntPlacedBy()
               : (this.exploder instanceof EntityLiving ? (EntityLiving)this.exploder : null)
         );
   }

   public void addSecondaryExplosionNoFX(double dPosX, double dPosY, double dPosZ, float fExplosionSize, boolean bCreatesFlames, boolean bDestroysBlocks) {
      if (this.secondaryExplosionList == null) {
         this.secondaryExplosionList = new ArrayList();
      }

      Explosion explosion = new Explosion(this.worldObj, null, dPosX, dPosY, dPosZ, fExplosionSize);
      explosion.isFlaming = bCreatesFlames;
      explosion.isSmoking = bDestroysBlocks;
      explosion.suppressFX = true;
      this.secondaryExplosionList.add(explosion);
   }

   private void performSecondaryExplosions() {
      if (this.secondaryExplosionList != null) {
         for (Explosion tempExplosion : this.secondaryExplosionList) {
            tempExplosion.doExplosionA();
            tempExplosion.doExplosionB(false);
         }
      }
   }
}
