package btw.block.blocks.legacy;

import btw.block.BTWBlocks;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.Block;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.IconRegister;
import net.minecraft.src.World;

public class LegacyFarmlandBlockFertilized extends LegacyFarmlandBlockBase {
   private static final int DEFAULT_TEXTURE = 2;
   private static final int TOP_WET_TEXTURE = 136;
   public static final int TOP_DRY_TEXTURE = 137;

   public LegacyFarmlandBlockFertilized(int iBlockID) {
      super(iBlockID);
      this.c("FCBlockFarmlandFertilized");
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
      world.setBlockAndMetadataWithNotify(i, j, k, Block.tilledField.blockID, iMetadata);
   }

   @Override
   protected boolean isFertilized(IBlockAccess blockAccess, int i, int j, int k) {
      return true;
   }

   @Override
   protected void convertToNewSoil(World world, int i, int j, int k) {
      int iNewMetadata = 0;
      if (this.isHydrated(world, i, j, k)) {
         iNewMetadata = BTWBlocks.fertilizedFarmland.setFullyHydrated(iNewMetadata);
      }

      world.setBlockAndMetadataWithNotify(i, j, k, BTWBlocks.fertilizedFarmland.blockID, iNewMetadata);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void registerIcons(IconRegister register) {
      this.blockIcon = register.registerIcon("dirt");
      this.iconTopWet = register.registerIcon("FCBlockFarmlandFertilized_wet");
      this.iconTopDry = register.registerIcon("FCBlockFarmlandFertilized_dry");
   }
}
