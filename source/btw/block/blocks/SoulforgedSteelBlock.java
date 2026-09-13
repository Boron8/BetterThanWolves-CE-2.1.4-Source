package btw.block.blocks;

import btw.block.BTWBlocks;
import java.util.Random;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.World;

public class SoulforgedSteelBlock extends Block {
   private static final float BLOCK_HARDNESS = 100.0F;
   private static final float BLOCK_EXPLOSION_RESISTANCE = 2000.0F;
   private static final int TICK_RATE = 4;
   private static final float STRONGHOLD_ACTIVATION_DISTANCE = 64.0F;
   private static final float STRONGHOLD_ACTIVATION_DISTANCE_SQ = 4096.0F;

   public SoulforgedSteelBlock(int iBlockID) {
      super(iBlockID, BTWBlocks.soulforgedSteelMaterial);
      this.c(100.0F);
      this.b(2000.0F);
      this.a(k);
      this.c("fcBlockSoulforgedSteel");
      this.a(CreativeTabs.tabBlock);
   }

   @Override
   public void onBlockAdded(World world, int i, int j, int k) {
      super.onBlockAdded(world, i, j, k);
      world.scheduleBlockUpdate(i, j, k, this.blockID, this.tickRate(world));
   }

   @Override
   public int getMobilityFlag() {
      return 2;
   }

   @Override
   public int tickRate(World world) {
      return 4;
   }

   @Override
   public void onNeighborBlockChange(World world, int i, int j, int k, int iChangedBlockID) {
      if ((
            this.isRedstonePowerFlagOn(world, i, j, k) != this.isReceivingRedstonePower(world, i, j, k)
               || this.isActivatedByWaterFlagOn(world, i, j, k) != this.isBlockNeighboringOnWater(world, i, j, k)
         )
         && !world.isUpdatePendingThisTickForBlock(i, j, k, this.blockID)) {
         world.scheduleBlockUpdate(i, j, k, this.blockID, this.tickRate(world));
      }
   }

   @Override
   public void updateTick(World world, int i, int j, int k, Random random) {
      boolean bPowered = this.isReceivingRedstonePower(world, i, j, k);
      if (this.isRedstonePowerFlagOn(world, i, j, k) != bPowered) {
         this.setRedstonePowerFlag(world, i, j, k, bPowered);
         if (bPowered) {
            world.playAuxSFX(2225, i, j, k, 1);
         }
      }

      boolean bIsNeighboringOnWater = this.isBlockNeighboringOnWater(world, i, j, k);
      if (this.isActivatedByWaterFlagOn(world, i, j, k) != bIsNeighboringOnWater) {
         this.setActivatedByWaterFlag(world, i, j, k, bIsNeighboringOnWater);
         if (bIsNeighboringOnWater) {
            world.playSoundEffect(i + 0.5, j + 0.5, k + 0.5, "random.drink", 0.25F + world.rand.nextFloat() * 0.25F, world.rand.nextFloat() * 0.75F + 0.25F);
            if (this.getStrongholdIndexWithinActivationRange(world, i, j, k) == 0) {
               world.playAuxSFX(2228, i, j, k, 0);
            }
         }
      }
   }

   public boolean isRedstonePowerFlagOn(IBlockAccess blockAccess, int i, int j, int k) {
      int iMetadata = blockAccess.getBlockMetadata(i, j, k);
      return (iMetadata & 1) > 0;
   }

   private void setRedstonePowerFlag(World world, int i, int j, int k, boolean bPowerFlag) {
      int iMetadata = world.getBlockMetadata(i, j, k) & -2;
      if (bPowerFlag) {
         iMetadata |= 1;
      }

      world.setBlockMetadataWithNotify(i, j, k, iMetadata);
   }

   private boolean isReceivingRedstonePower(IBlockAccess blockAccess, int i, int j, int k) {
      int iBlockAboveID = blockAccess.getBlockId(i, j + 1, k);
      return iBlockAboveID == Block.redstoneWire.blockID ? blockAccess.getBlockMetadata(i, j + 1, k) > 0 : false;
   }

   private void emitPoweredParticles(World world, int i, int j, int k, Random random) {
      for (int counter = 0; counter < 10; counter++) {
         float smokeX = i + random.nextFloat();
         float smokeY = j + random.nextFloat() * 0.5F + 1.0F;
         float smokeZ = k + random.nextFloat();
         world.spawnParticle("largesmoke", smokeX, smokeY, smokeZ, 0.0, 0.0, 0.0);
      }
   }

   public boolean isActivatedByWaterFlagOn(IBlockAccess blockAccess, int i, int j, int k) {
      int iMetadata = blockAccess.getBlockMetadata(i, j, k);
      return (iMetadata & 2) > 0;
   }

   private void setActivatedByWaterFlag(World world, int i, int j, int k, boolean bActivatedByWaterFlag) {
      int iMetadata = world.getBlockMetadata(i, j, k) & -3;
      if (bActivatedByWaterFlag) {
         iMetadata |= 2;
      }

      world.setBlockMetadataWithNotify(i, j, k, iMetadata);
   }

   public boolean isRecentlyActivatedFlagOn(IBlockAccess blockAccess, int i, int j, int k) {
      int iMetadata = blockAccess.getBlockMetadata(i, j, k);
      return (iMetadata & 4) > 0;
   }

   private void setRecentlyActivatedFlag(World world, int i, int j, int k, boolean bActivatedByWaterFlag) {
      int iMetadata = world.getBlockMetadata(i, j, k) & -5;
      if (bActivatedByWaterFlag) {
         iMetadata |= 4;
      }

      world.setBlockMetadataWithNotify(i, j, k, iMetadata);
   }

   private boolean isBlockNeighboringOnWater(World world, int i, int j, int k) {
      return this.isWaterBlock(world, i + 1, j, k)
         || this.isWaterBlock(world, i - 1, j, k)
         || this.isWaterBlock(world, i, j + 1, k)
         || this.isWaterBlock(world, i, j - 1, k)
         || this.isWaterBlock(world, i, j, k + 1)
         || this.isWaterBlock(world, i, j, k - 1);
   }

   private boolean isWaterBlock(World world, int i, int j, int k) {
      int iBlockID = world.getBlockId(i, j, k);
      return iBlockID == Block.waterMoving.blockID || iBlockID == Block.waterStill.blockID;
   }

   public int getStrongholdIndexWithinActivationRange(World world, int i, int j, int k) {
      return -1;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void randomDisplayTick(World world, int i, int j, int k, Random random) {
      if (this.isRedstonePowerFlagOn(world, i, j, k)) {
         this.emitPoweredParticles(world, i, j, k, random);
      }
   }
}
