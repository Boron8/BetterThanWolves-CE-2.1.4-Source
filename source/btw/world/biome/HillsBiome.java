package btw.world.biome;

import btw.block.BTWBlocks;
import java.util.Random;
import net.minecraft.src.BiomeGenBase;
import net.minecraft.src.Block;
import net.minecraft.src.World;
import net.minecraft.src.WorldGenMinable;
import net.minecraft.src.WorldGenerator;

public class HillsBiome extends BiomeGenBase {
   private static final int NUM_SILVERFISH_CLUSTERS_PER_CHUNK = 7;
   private static final int NUM_SILVERFISH_BLOCKS_PER_CLUSTER = 8;
   protected WorldGenerator generatorSilverfish;
   protected WorldGenerator generatorSilverfishSecondStrata;
   protected WorldGenerator generatorSilverfishThirdStrata;
   public boolean hasSilverfishBeenInit = false;

   public HillsBiome(int iBiomeID) {
      super(iBiomeID);
   }

   @Override
   public void decorate(World world, Random rand, int iChunkX, int iChunkZ) {
      super.decorate(world, rand, iChunkX, iChunkZ);
      if (!this.hasSilverfishBeenInit) {
         this.initSilverfish();
      }

      this.addEmeralds(world, rand, iChunkX, iChunkZ);
      this.addSilverfishBlocks(world, rand, iChunkX, iChunkZ);
   }

   public void addEmeralds(World world, Random rand, int iChunkX, int iChunkZ) {
      int iNumEmeralds = 3 + rand.nextInt(6);

      for (int iTempCount = 0; iTempCount < iNumEmeralds; iTempCount++) {
         int iTempI = iChunkX + rand.nextInt(16);
         int iTempJ = rand.nextInt(28) + 4;
         int iTempK = iChunkZ + rand.nextInt(16);
         if (world.getBlockId(iTempI, iTempJ, iTempK) == Block.stone.blockID) {
            int iMetadata = 0;
            if (iTempJ <= 48 + world.rand.nextInt(2)) {
               int iStrataLevel = 1;
               if (iTempJ <= 24 + world.rand.nextInt(2)) {
                  iStrataLevel = 2;
               }

               iMetadata = Block.oreEmerald.getMetadataConversionForStrataLevel(iStrataLevel, 0);
            }

            world.setBlock(iTempI, iTempJ, iTempK, Block.oreEmerald.blockID, iMetadata, 2);
         }
      }
   }

   public void addSilverfishBlocks(World world, Random rand, int iChunkX, int iChunkZ) {
      for (int iTempCount = 0; iTempCount < 7; iTempCount++) {
         int iTempI = iChunkX + rand.nextInt(16);
         int iTempJ = rand.nextInt(64);
         int iTempK = iChunkZ + rand.nextInt(16);
         if (iTempJ <= 48 + world.rand.nextInt(2)) {
            if (iTempJ <= 24 + world.rand.nextInt(2)) {
               this.generatorSilverfishThirdStrata.generate(world, rand, iTempI, iTempJ, iTempK);
            }

            this.generatorSilverfishSecondStrata.generate(world, rand, iTempI, iTempJ, iTempK);
         }

         this.generatorSilverfish.generate(world, rand, iTempI, iTempJ, iTempK);
      }
   }

   public void initSilverfish() {
      this.generatorSilverfish = new WorldGenMinable(BTWBlocks.infestedStone.blockID, 8);
      this.generatorSilverfishSecondStrata = new WorldGenMinable(BTWBlocks.infestedMidStrataStone.blockID, 8);
      this.generatorSilverfishThirdStrata = new WorldGenMinable(BTWBlocks.infestedDeepStrataStone.blockID, 8);
      this.hasSilverfishBeenInit = true;
   }
}
