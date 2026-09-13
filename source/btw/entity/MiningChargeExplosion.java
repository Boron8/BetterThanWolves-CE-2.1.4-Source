package btw.entity;

import btw.world.util.BlockPos;
import java.util.List;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.DamageSource;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityItem;
import net.minecraft.src.Item;
import net.minecraft.src.MathHelper;
import net.minecraft.src.Vec3;
import net.minecraft.src.World;

public class MiningChargeExplosion {
   public static final float EXPLOSION_STRENGTH = 20.0F;
   private World worldObj;
   public double explosionX;
   public double explosionY;
   public double explosionZ;
   public int facing;

   public MiningChargeExplosion(World world, double dXPos, double dYPos, double dZPos, int iFacing) {
      this.worldObj = world;
      this.explosionX = dXPos;
      this.explosionY = dYPos;
      this.explosionZ = dZPos;
      this.facing = iFacing;
   }

   public void doExplosion() {
      this.damageEntities();
      this.destroyBlocks();
      this.worldObj
         .playAuxSFX(2229, MathHelper.floor_double(this.explosionX), MathHelper.floor_double(this.explosionY), MathHelper.floor_double(this.explosionZ), 0);
   }

   private void destroyBlocks() {
      int iISource = MathHelper.floor_double(this.explosionX);
      int iJSource = MathHelper.floor_double(this.explosionY);
      int iKSource = MathHelper.floor_double(this.explosionZ);
      int iSourceBlockID = this.worldObj.getBlockId(iISource, iJSource, iKSource);
      if (iSourceBlockID <= 0 || !(Block.blocksList[iSourceBlockID].getExplosionResistance(null, this.worldObj, iISource, iJSource, iKSource) >= 20.0F)) {
         BlockPos targetPos = new BlockPos(iISource, iJSource, iKSource);
         targetPos.addFacingAsOffset(this.facing);
         int iTargetBlockID = this.worldObj.getBlockId(targetPos.x, targetPos.y, targetPos.z);
         if (iTargetBlockID > 0 && Block.blocksList[iTargetBlockID].getExplosionResistance(null, this.worldObj, targetPos.x, targetPos.y, targetPos.z) >= 20.0F
            )
          {
            targetPos = new BlockPos(iISource, iJSource, iKSource);
         }

         this.destroyCentralBlocks(targetPos.x, targetPos.y, targetPos.z);
         targetPos.addFacingAsOffset(this.facing);
         iTargetBlockID = this.worldObj.getBlockId(targetPos.x, targetPos.y, targetPos.z);
         if (iTargetBlockID <= 0
            || !(Block.blocksList[iTargetBlockID].getExplosionResistance(null, this.worldObj, targetPos.x, targetPos.y, targetPos.z) >= 20.0F)) {
            targetPos.addFacingAsOffset(this.facing);
            iTargetBlockID = this.worldObj.getBlockId(targetPos.x, targetPos.y, targetPos.z);
            if (iTargetBlockID > 0
               && Block.blocksList[iTargetBlockID].getExplosionResistance(null, this.worldObj, targetPos.x, targetPos.y, targetPos.z) < 20.0F) {
               this.destroyBlock(targetPos.x, targetPos.y, targetPos.z);
            }
         }
      }
   }

   private void destroyCentralBlocks(int iICenter, int iJCenter, int iKCenter) {
      for (int iTempI = iICenter - 1; iTempI <= iICenter + 1; iTempI++) {
         for (int iTempJ = iJCenter - 1; iTempJ <= iJCenter + 1; iTempJ++) {
            for (int iTempK = iKCenter - 1; iTempK <= iKCenter + 1; iTempK++) {
               int iTempBlockID = this.worldObj.getBlockId(iTempI, iTempJ, iTempK);
               if (iTempBlockID > 0 && Block.blocksList[iTempBlockID].getExplosionResistance(null, this.worldObj, iTempI, iTempJ, iTempK) < 20.0F) {
                  this.destroyBlock(iTempI, iTempJ, iTempK);
               }
            }
         }
      }
   }

   private void damageEntities() {
      float explosionSize = 6.0F;
      int k = MathHelper.floor_double(this.explosionX - explosionSize - 1.0);
      int i1 = MathHelper.floor_double(this.explosionX + explosionSize + 1.0);
      int k1 = MathHelper.floor_double(this.explosionY - explosionSize - 1.0);
      int l1 = MathHelper.floor_double(this.explosionY + explosionSize + 1.0);
      int i2 = MathHelper.floor_double(this.explosionZ - explosionSize - 1.0);
      int j2 = MathHelper.floor_double(this.explosionZ + explosionSize + 1.0);
      List list = this.worldObj.getEntitiesWithinAABBExcludingEntity(null, AxisAlignedBB.getAABBPool().getAABB(k, k1, i2, i1, l1, j2));
      Vec3 vec3d = Vec3.createVectorHelper(this.explosionX, this.explosionY, this.explosionZ);

      for (int k2 = 0; k2 < list.size(); k2++) {
         Entity entity = (Entity)list.get(k2);
         double d4 = entity.getDistance(this.explosionX, this.explosionY, this.explosionZ) / explosionSize;
         if (d4 <= 1.0) {
            double d6 = entity.posX - this.explosionX;
            double d8 = entity.posY - this.explosionY;
            double d10 = entity.posZ - this.explosionZ;
            double d11 = MathHelper.sqrt_double(d6 * d6 + d8 * d8 + d10 * d10);
            d6 /= d11;
            d8 /= d11;
            d10 /= d11;
            double d12 = this.worldObj.getBlockDensity(vec3d, entity.boundingBox);
            double d13 = (1.0 - d4) * d12;
            if (entity instanceof EntityItem) {
               EntityItem entityItem = (EntityItem)entity;
               int iItemID = entityItem.getEntityItem().itemID;
               if (iItemID != Item.coal.itemID
                  && iItemID != Item.redstone.itemID
                  && iItemID != Item.dyePowder.itemID
                  && iItemID != Item.flint.itemID
                  && iItemID != Block.oreGold.blockID
                  && iItemID != Block.oreIron.blockID) {
                  int iItemDamage = (int)((d13 * d13 + d13) / 2.0 * 8.0 * explosionSize + 1.0);
                  if (iItemDamage > 2) {
                     iItemDamage = 3;
                  }

                  entity.attackEntityFrom(DamageSource.setExplosionSource(null), iItemDamage);
               }
            } else {
               entity.attackEntityFrom(DamageSource.setExplosionSource(null), (int)((d13 * d13 + d13) / 2.0 * 8.0 * explosionSize + 1.0));
            }

            if (!(entity instanceof MiningChargeEntity)) {
               entity.motionX += d6 * d13;
               entity.motionY += d8 * d13;
               entity.motionZ += d10 * d13;
            }
         }
      }
   }

   private void destroyBlock(int x, int y, int z) {
      int blockID = this.worldObj.getBlockId(x, y, z);
      int meta = this.worldObj.getBlockMetadata(x, y, z);
      if (blockID > 0) {
         Block destroyedBlock = Block.blocksList[blockID];
         destroyedBlock.dropItemsOnDestroyedByMiningCharge(this.worldObj, x, y, z, meta);
         destroyedBlock.onBlockDestroyedByMiningCharge(this.worldObj, x, y, z);
         this.worldObj.setBlockWithNotify(x, y, z, 0);
         destroyedBlock.postBlockDestroyedByMiningCharge(this.worldObj, x, y, z);
      }
   }
}
