package btw.entity;

import btw.block.BTWBlocks;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.Block;
import net.minecraft.src.DamageSource;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityWitherSkull;
import net.minecraft.src.Explosion;
import net.minecraft.src.MathHelper;
import net.minecraft.src.MovingObjectPosition;
import net.minecraft.src.Potion;
import net.minecraft.src.PotionEffect;
import net.minecraft.src.World;

public class WitherSkullEntity extends EntityWitherSkull {
   private static final int BLIGHT_SPREAD_RANGE = 3;
   private static final double BLIGHT_SPREAD_RANGE_SQ = 9.0;

   public WitherSkullEntity(World world) {
      super(world);
   }

   public WitherSkullEntity(World world, EntityLiving shootingEntity, double dDirX, double dDirY, double dDirZ) {
      super(world, shootingEntity, dDirX, dDirY, dDirZ);
   }

   @Override
   public float func_82146_a(Explosion explosion, World par2World, int par3, int par4, int par5, Block par6Block) {
      float fResistance = par6Block.getExplosionResistance(this, par2World, par3, par4, par5);
      if (this.isHighExplosive()
         && par6Block != Block.bedrock
         && par6Block != Block.endPortal
         && par6Block != Block.endPortalFrame
         && par6Block != BTWBlocks.soulforgedSteelBlock) {
         fResistance = Math.min(0.8F, fResistance);
      }

      return fResistance;
   }

   @Override
   protected void onImpact(MovingObjectPosition pos) {
      if (!this.worldObj.isRemote) {
         if (pos.entityHit != null) {
            if (this.shootingEntity != null) {
               if (pos.entityHit.attackEntityFrom(DamageSource.causeMobDamage(this.shootingEntity), 8) && !pos.entityHit.isEntityAlive()) {
                  this.shootingEntity.heal(5);
               }
            } else {
               pos.entityHit.attackEntityFrom(DamageSource.magic, 5);
            }

            if (pos.entityHit instanceof EntityLiving) {
               int iDamage = 5;
               if (this.worldObj.difficultySetting == 2) {
                  iDamage = 10;
               } else if (this.worldObj.difficultySetting == 3) {
                  iDamage = 40;
               }

               ((EntityLiving)pos.entityHit).addPotionEffect(new PotionEffect(Potion.wither.id, 20 * iDamage, 1));
            }
         }

         this.worldObj.newExplosion(this, this.posX, this.posY, this.posZ, 1.0F, false, this.worldObj.getGameRules().getGameRuleBooleanValue("mobGriefing"));
         if (!this.isHighExplosive()) {
            this.spreadBlightInArea();
         }

         this.w();
      }
   }

   protected boolean isHighExplosive() {
      return this.d();
   }

   protected void spreadBlightInArea() {
      int iCenterI = MathHelper.floor_double(this.posX);
      int iCenterJ = MathHelper.floor_double(this.posY);
      int iCenterK = MathHelper.floor_double(this.posZ);

      for (int iTempI = iCenterI - 3; iTempI <= iCenterI + 3; iTempI++) {
         for (int iTempJ = iCenterJ - 3; iTempJ <= iCenterJ + 3; iTempJ++) {
            for (int iTempK = iCenterK - 3; iTempK <= iCenterK + 3; iTempK++) {
               double dDeltaI = iTempI - iCenterI;
               double dDeltaJ = iTempJ - iCenterJ;
               double dDeltaK = iTempK - iCenterK;
               double dDistSq = dDeltaI * dDeltaI + dDeltaJ * dDeltaJ + dDeltaK * dDeltaK;
               if (dDistSq <= 9.0) {
                  this.attemptSpreadBlightToBlock(iTempI, iTempJ, iTempK);
               }
            }
         }
      }
   }

   protected void attemptSpreadBlightToBlock(int i, int j, int k) {
      int iTargetBlockID = this.worldObj.getBlockId(i, j, k);
      if (iTargetBlockID == Block.grass.blockID) {
         int iAboveTargetBlockID = this.worldObj.getBlockId(i, j + 1, k);
         if (Block.lightOpacity[iAboveTargetBlockID] <= 2) {
            this.worldObj.setBlockAndMetadataWithNotify(i, j, k, BTWBlocks.aestheticEarth.blockID, 0);
         }
      }
   }

   @Environment(EnvType.CLIENT)
   public WitherSkullEntity(World world, double dPosX, double dPosY, double dPosZ, double dVelX, double dVelY, double dVelZ) {
      super(world, dPosX, dPosY, dPosZ, dVelX, dVelY, dVelZ);
   }
}
