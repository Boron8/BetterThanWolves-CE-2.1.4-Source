package net.minecraft.src;

public class EntitySmallFireball extends EntityFireball {
   public EntitySmallFireball(World var1) {
      super(var1);
      this.a(0.3125F, 0.3125F);
   }

   public EntitySmallFireball(World var1, EntityLiving var2, double var3, double var5, double var7) {
      super(var1, var2, var3, var5, var7);
      this.a(0.3125F, 0.3125F);
   }

   public EntitySmallFireball(World var1, double var2, double var4, double var6, double var8, double var10, double var12) {
      super(var1, var2, var4, var6, var8, var10, var12);
      this.a(0.3125F, 0.3125F);
   }

   @Override
   protected void onImpact(MovingObjectPosition var1) {
      if (!this.worldObj.isRemote) {
         if (var1.entityHit != null) {
            if (!var1.entityHit.isImmuneToFire() && var1.entityHit.attackEntityFrom(DamageSource.causeFireballDamage(this, this.shootingEntity), 5)) {
               var1.entityHit.setFire(5);
            }
         } else {
            int var2 = var1.blockX;
            int var3 = var1.blockY;
            int var4 = var1.blockZ;
            switch (var1.sideHit) {
               case 0:
                  var3--;
                  break;
               case 1:
                  var3++;
                  break;
               case 2:
                  var4--;
                  break;
               case 3:
                  var4++;
                  break;
               case 4:
                  var2--;
                  break;
               case 5:
                  var2++;
            }

            if (this.worldObj.isAirBlock(var2, var3, var4)) {
               this.worldObj.setBlock(var2, var3, var4, Block.fire.blockID);
            }
         }

         this.w();
      }
   }

   @Override
   public boolean canBeCollidedWith() {
      return false;
   }

   @Override
   public boolean attackEntityFrom(DamageSource var1, int var2) {
      return false;
   }
}
