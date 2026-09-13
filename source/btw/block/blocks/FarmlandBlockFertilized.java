package btw.block.blocks;

import btw.block.BTWBlocks;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.Block;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.IconRegister;
import net.minecraft.src.World;

public class FarmlandBlockFertilized extends FarmlandBlock {
   public FarmlandBlockFertilized(int iBlockID) {
      super(iBlockID);
   }

   @Override
   public float getPlantGrowthOnMultiplier(World world, int i, int j, int k, Block plantBlock) {
      return 2.0F;
   }

   @Override
   public boolean getIsFertilizedForPlantGrowth(World world, int i, int j, int k) {
      return true;
   }

   @Override
   public void notifyOfFullStagePlantGrowthOn(World world, int i, int j, int k, Block plantBlock) {
      int iMetadata = world.getBlockMetadata(i, j, k);
      world.setBlockAndMetadataWithNotify(i, j, k, BTWBlocks.farmland.blockID, iMetadata);
   }

   @Override
   protected boolean isFertilized(IBlockAccess blockAccess, int i, int j, int k) {
      return true;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void registerIcons(IconRegister register) {
      this.blockIcon = register.registerIcon("dirt");
      this.iconTopWet = register.registerIcon("FCBlockFarmlandFertilized_wet");
      this.iconTopDry = register.registerIcon("FCBlockFarmlandFertilized_dry");
   }
}
