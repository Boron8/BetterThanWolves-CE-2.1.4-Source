package btw.block.blocks;

import btw.block.BTWBlocks;
import btw.util.MiscUtils;
import java.util.Random;
import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Item;
import net.minecraft.src.Material;
import net.minecraft.src.World;

public class SolidSnowBlock extends Block {
   public static final float HARDNESS = 0.5F;
   public static final int CHANCE_OF_MELTING = 1;

   public SolidSnowBlock(int iBlockID) {
      super(iBlockID, Material.craftedSnow);
      this.c(0.5F);
      this.setShovelsEffectiveOn();
      this.setBuoyant();
      this.a(o);
      this.c("fcBlockSnowSolid");
      this.b(true);
      this.k(4);
      Block.useNeighborBrightness[iBlockID] = true;
      this.a(CreativeTabs.tabBlock);
   }

   @Override
   public int idDropped(int iMetadata, Random rand, int iFortuneModifier) {
      return Item.snowball.itemID;
   }

   @Override
   public int quantityDropped(Random rand) {
      return 8;
   }

   @Override
   public void onBlockDestroyedWithImproperTool(World world, EntityPlayer player, int i, int j, int k, int iMetadata) {
      this.c(world, i, j, k, iMetadata, 0);
   }

   @Override
   public void onBlockAdded(World world, int i, int j, int k) {
      if (world.provider.isHellWorld) {
         world.setBlockToAir(i, j, k);
         world.playSoundEffect(i + 0.5, j + 0.5, k + 0.5, "random.fizz", 0.5F, 2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F);

         for (int iTempCount = 0; iTempCount < 8; iTempCount++) {
            world.spawnParticle("largesmoke", i + Math.random(), j + Math.random(), k + Math.random(), 0.0, 0.0, 0.0);
         }
      }
   }

   @Override
   public void randomUpdateTick(World world, int i, int j, int k, Random rand) {
      if (!MiscUtils.isIKInColdBiome(world, i, k) && rand.nextInt(1) == 0) {
         this.convertToLooseSnow(world, i, j, k);
      }
   }

   @Override
   public boolean isStickyToSnow(IBlockAccess blockAccess, int i, int j, int k) {
      return true;
   }

   @Override
   public float getMovementModifier(World world, int i, int j, int k) {
      return 1.0F;
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

   @Override
   public boolean canBePistonShoveled(World world, int i, int j, int k) {
      return true;
   }

   private void convertToLooseSnow(World world, int i, int j, int k) {
      int iNewMetadata = BTWBlocks.looseSnow.setHardeningLevel(0, 7);
      world.setBlockAndMetadataWithNotify(i, j, k, BTWBlocks.looseSnow.blockID, iNewMetadata);
      BTWBlocks.looseSnow.scheduleCheckForFall(world, i, j, k);
   }

   private void melt(World world, int i, int j, int k) {
      MiscUtils.placeNonPersistentWaterMinorSpread(world, i, j, k);
   }
}
