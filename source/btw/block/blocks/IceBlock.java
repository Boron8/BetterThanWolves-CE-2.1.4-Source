package btw.block.blocks;

import btw.util.MiscUtils;
import java.util.Random;
import net.minecraft.src.Block;
import net.minecraft.src.BlockIce;
import net.minecraft.src.EnchantmentHelper;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EnumSkyBlock;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.StatList;
import net.minecraft.src.World;

public class IceBlock extends BlockIce {
   public IceBlock(int iBlockID) {
      super(iBlockID);
      this.setPicksEffectiveOn();
   }

   @Override
   public void harvestBlock(World world, EntityPlayer player, int i, int j, int k, int iMetadata) {
      if (world.provider.isHellWorld || !this.isNonSourceIceFromMetadata(iMetadata) || this.r_() && EnchantmentHelper.getSilkTouchModifier(player)) {
         super.harvestBlock(world, player, i, j, k, iMetadata);
      } else {
         player.addExhaustion(0.025F);
         player.addStat(StatList.mineBlockStatArray[this.blockID], 1);
         MiscUtils.placeNonPersistentWaterMinorSpread(world, i, j, k);
      }
   }

   @Override
   public void updateTick(World world, int i, int j, int k, Random random) {
      if (world.getSavedLightValue(EnumSkyBlock.Block, i, j, k) > 11 - Block.lightOpacity[this.blockID]) {
         this.melt(world, i, j, k);
      }
   }

   @Override
   public void onBlockAdded(World world, int i, int j, int k) {
      if (world.provider.isHellWorld) {
         world.setBlockWithNotify(i, j, k, 0);
         world.playSoundEffect(i + 0.5, j + 0.5, k + 0.5, "random.fizz", 0.5F, 2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F);

         for (int l = 0; l < 8; l++) {
            world.spawnParticle("largesmoke", i + Math.random(), j + Math.random(), k + Math.random(), 0.0, 0.0, 0.0);
         }
      } else {
         int iBlockAboveID = world.getBlockId(i, j + 1, k);
         if (!MiscUtils.isIKInColdBiome(world, i, k) || iBlockAboveID != this.blockID && !world.canBlockSeeTheSky(i, j + 1, k)) {
            world.setBlockWithNotify(i, j, k, 0);
            MiscUtils.placeNonPersistentWaterMinorSpread(world, i, j, k);
         }
      }
   }

   @Override
   public float getMovementModifier(World world, int i, int j, int k) {
      return 1.0F;
   }

   @Override
   public int adjustMetadataForPistonMove(int iMetadata) {
      int var2;
      return var2 = iMetadata | 8;
   }

   @Override
   public boolean hasLargeCenterHardPointToFacing(IBlockAccess blockAccess, int i, int j, int k, int iFacing, boolean bIgnoreTransparency) {
      return bIgnoreTransparency;
   }

   @Override
   public boolean getCanBeSetOnFireDirectly(IBlockAccess blockAccess, int i, int j, int k) {
      return true;
   }

   @Override
   public boolean getCanBeSetOnFireDirectlyByItem(IBlockAccess blockAccess, int i, int j, int k) {
      return false;
   }

   @Override
   public boolean setOnFireDirectly(World world, int i, int j, int k) {
      this.melt(world, i, j, k);
      return true;
   }

   @Override
   public int getChanceOfFireSpreadingDirectlyTo(IBlockAccess blockAccess, int i, int j, int k) {
      return 60;
   }

   public boolean isNonSourceIce(IBlockAccess blockAccess, int i, int j, int k) {
      return this.isNonSourceIceFromMetadata(blockAccess.getBlockMetadata(i, j, k));
   }

   public void setIsNonSourceIce(World world, int i, int j, int k, boolean bNonSource) {
      int iMetadata = world.getBlockMetadata(i, j, k) & -9;
      if (bNonSource) {
         iMetadata |= 8;
      }

      world.setBlockMetadata(i, j, k, iMetadata);
   }

   public boolean isNonSourceIceFromMetadata(int iMetadata) {
      return (iMetadata & 8) > 0;
   }

   private void melt(World world, int i, int j, int k) {
      if (this.isNonSourceIce(world, i, j, k)) {
         MiscUtils.placeNonPersistentWaterMinorSpread(world, i, j, k);
      } else {
         world.setBlockWithNotify(i, j, k, Block.waterMoving.blockID);
      }
   }
}
