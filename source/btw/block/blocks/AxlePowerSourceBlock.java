package btw.block.blocks;

import btw.block.BTWBlocks;
import java.util.Random;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.World;

public class AxlePowerSourceBlock extends AxleBlock {
   public AxlePowerSourceBlock(int iBlockID) {
      super(iBlockID);
      this.c("fcBlockAxlePowerSource");
      this.a(CreativeTabs.tabRedstone);
   }

   @Override
   public void updateTick(World world, int i, int j, int k, Random random) {
   }

   @Override
   public void onNeighborBlockChange(World world, int i, int j, int k, int iBlockID) {
   }

   @Override
   public int idDropped(int iMetadata, Random rand, int iFortuneModifier) {
      return BTWBlocks.axle.blockID;
   }

   @Override
   public int getMechanicalPowerLevelProvidedToAxleAtFacing(World world, int i, int j, int k, int iFacing) {
      int iAlignment = this.getAxisAlignment(world, i, j, k);
      return iFacing >> 1 == iAlignment ? 4 : 0;
   }

   @Override
   protected void validatePowerLevel(World world, int i, int j, int k) {
   }

   @Override
   public int getPowerLevel(IBlockAccess iBlockAccess, int i, int j, int k) {
      return 4;
   }

   @Override
   public int getPowerLevelFromMetadata(int iMetadata) {
      return 4;
   }

   @Override
   public void setPowerLevel(World world, int i, int j, int k, int iPowerLevel) {
   }

   @Override
   public int setPowerLevelInMetadata(int iMetadata, int iPowerLevel) {
      return iMetadata;
   }

   @Override
   public void setPowerLevelWithoutNotify(World world, int i, int j, int k, int iPowerLevel) {
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean clientCheckIfPowered(IBlockAccess blockAccess, int i, int j, int k) {
      return true;
   }
}
