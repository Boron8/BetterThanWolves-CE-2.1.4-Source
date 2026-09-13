package btw.entity;

import btw.block.BTWBlocks;
import btw.block.blocks.FireBlock;
import btw.block.blocks.LogBlock;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityWeatherEffect;
import net.minecraft.src.MathHelper;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.Vec3;
import net.minecraft.src.World;

public class LightningBoltEntity extends EntityWeatherEffect {
   private static final int MAX_TRUNK_DETECTION_PENETRATION = 6;
   private static final int MAX_TRUNK_PENETRATION = 6;
   private int lightningState;
   private int durationCountdown;
   public long renderSeed = 0L;

   public LightningBoltEntity(World world, double dPosX, double dPosY, double dPosZ) {
      super(world);
      this.b(dPosX, dPosY, dPosZ, 0.0F, 0.0F);
      this.lightningState = 2;
      this.durationCountdown = this.rand.nextInt(3) + 1;
      this.renderSeed = this.rand.nextLong();
      if (!world.isRemote
         && !this.isStrikingLightningRod()
         && world.doChunksNearChunkExist(MathHelper.floor_double(dPosX), MathHelper.floor_double(dPosY), MathHelper.floor_double(dPosZ), 10)) {
         int iStrikeI = MathHelper.floor_double(dPosX);
         int iStrikeJ = (int)dPosY;
         int iStrikeK = MathHelper.floor_double(dPosZ);
         this.onStrikeBlock(world, iStrikeI, iStrikeJ, iStrikeK);
         if (!this.isStrikingLightningRod()) {
            for (int iTempCount = 0; iTempCount < 4; iTempCount++) {
               int iTempI = iStrikeI + this.rand.nextInt(3) - 1;
               int iTempJ = iStrikeJ + this.rand.nextInt(3) - 1;
               int iTempK = iStrikeK + this.rand.nextInt(3) - 1;
               if (FireBlock.canFireReplaceBlock(world, iTempI, iTempJ, iTempK)
                  && Block.fire.canPlaceBlockAt(world, iTempI, iTempJ, iTempK)
                  && world.getGameRules().getGameRuleBooleanValue("doFireTick")
                  && world.getDifficulty().shouldLightningStartFires()) {
                  world.setBlock(iTempI, iTempJ, iTempK, Block.fire.blockID);
               }
            }
         }
      }
   }

   @Override
   public void onUpdate() {
      super.l_();
      this.lightningState--;
      if (this.lightningState == 1) {
         this.worldObj.func_82739_e(2280, MathHelper.floor_double(this.posX), (int)this.posY - 1, MathHelper.floor_double(this.posZ), 0);
         if (!this.isStrikingLightningRod()) {
            double dRange = 3.0;

            for (Entity tempEntity : this.worldObj
               .getEntitiesWithinAABBExcludingEntity(
                  this,
                  AxisAlignedBB.getAABBPool()
                     .getAABB(this.posX - dRange, this.posY, this.posZ - dRange, this.posX + dRange, this.posY + 6.0 + dRange, this.posZ + dRange)
               )) {
               tempEntity.onStruckByLightning(this);
            }
         }
      } else if (this.lightningState < 0) {
         if (this.durationCountdown == 0) {
            this.w();
         } else if (this.lightningState < -this.rand.nextInt(10)) {
            this.durationCountdown--;
            this.lightningState = 1;
            this.renderSeed = this.rand.nextLong();
            if (!this.worldObj.isRemote
               && !this.isStrikingLightningRod()
               && this.worldObj
                  .doChunksNearChunkExist(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ), 10)) {
               int iTempI = MathHelper.floor_double(this.posX);
               int iTempJ = (int)this.posY;
               int iTempK = MathHelper.floor_double(this.posZ);
               if (FireBlock.canFireReplaceBlock(this.worldObj, iTempI, iTempJ, iTempK)
                  && Block.fire.canPlaceBlockAt(this.worldObj, iTempI, iTempJ, iTempK)
                  && this.worldObj.getGameRules().getGameRuleBooleanValue("doFireTick")
                  && this.worldObj.getDifficulty().shouldLightningStartFires()) {
                  this.worldObj.setBlock(iTempI, iTempJ, iTempK, Block.fire.blockID);
               }
            }
         }
      }

      if (this.lightningState >= 0 && this.worldObj.isRemote) {
         this.worldObj.lastLightningBolt = 2;
      }
   }

   @Override
   protected void entityInit() {
   }

   @Override
   protected void readEntityFromNBT(NBTTagCompound par1NBTTagCompound) {
   }

   @Override
   protected void writeEntityToNBT(NBTTagCompound par1NBTTagCompound) {
   }

   private boolean isStrikingLightningRod() {
      int i = MathHelper.floor_double(this.posX);
      int j = MathHelper.floor_double(this.posY);
      int k = MathHelper.floor_double(this.posZ);
      return this.worldObj.getBlockId(i, j - 1, k) == BTWBlocks.lightningRod.blockID;
   }

   private void onStrikeBlock(World world, int i, int j, int k) {
      if (world.getGameRules().getGameRuleBooleanValue("doFireTick") && world.getDifficulty().shouldLightningStartFires()) {
         if (this.hasHitTreeTrunk(world, i, j, k)) {
            this.burnTreeTrunk(world, i, j, k);
         } else if (world.getBlockId(i, j - 1, k) == Block.leaves.blockID) {
            for (int iTempCount = 0; iTempCount < 5; iTempCount++) {
               int iTempI = i + world.rand.nextInt(5) - 2;
               int iTempK = k + world.rand.nextInt(5) - 2;
               if (FireBlock.canFireReplaceBlock(world, iTempI, j, iTempK) && this.hasHitTreeTrunk(world, iTempI, j, iTempK)) {
                  this.burnTreeTrunk(world, iTempI, j, iTempK);
                  break;
               }
            }
         }

         Block blockBelow = Block.blocksList[world.getBlockId(i, j - 1, k)];
         if (blockBelow != null) {
            blockBelow.onStruckByLightning(world, i, j - 1, k);
         }

         if (FireBlock.canFireReplaceBlock(world, i, j, k) && Block.fire.canPlaceBlockAt(world, i, j, k)) {
            world.setBlock(i, j, k, Block.fire.blockID);
         }
      }
   }

   private boolean hasHitTreeTrunk(World world, int i, int j, int k) {
      int m_iMinJ = j - 6;
      j--;

      while (j >= m_iMinJ) {
         int iTempBlockID = world.getBlockId(i, j, k);
         if (iTempBlockID == Block.wood.blockID) {
            return (world.getBlockMetadata(i, j, k) & 3) != 3;
         }

         if (iTempBlockID != Block.leaves.blockID) {
            return false;
         }

         j--;
      }

      return false;
   }

   private boolean burnTreeTrunk(World world, int i, int j, int k) {
      int m_iMinJ = j - 6;
      LogBlock logBlock = (LogBlock)Block.wood;
      j--;

      for (; j >= m_iMinJ; j--) {
         int iTempBlockID = world.getBlockId(i, j, k);
         if (iTempBlockID == Block.wood.blockID) {
            if ((world.getBlockMetadata(i, j, k) & 3) == 3) {
               break;
            }

            logBlock.convertToSmouldering(world, i, j, k);
         } else {
            if (iTempBlockID != Block.leaves.blockID) {
               break;
            }

            if (Block.fire.canPlaceBlockAt(world, i, j, k)) {
               world.setBlock(i, j, k, Block.fire.blockID);
            } else {
               world.setBlockToAir(i, j, k);
            }
         }
      }

      return false;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean isInRangeToRenderVec3D(Vec3 vec) {
      return this.lightningState >= 0;
   }
}
