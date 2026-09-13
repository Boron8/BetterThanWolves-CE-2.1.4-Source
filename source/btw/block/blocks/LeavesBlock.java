package btw.block.blocks;

import btw.block.BTWBlocks;
import btw.block.util.Flammability;
import java.util.Random;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.Block;
import net.minecraft.src.BlockLeaves;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;

public class LeavesBlock extends BlockLeaves {
   protected static final int ADJACENT_TREE_BLOCK_ARRAY_WIDTH = 32;
   protected static final int ARRAY_WIDTH_HALF = 16;
   protected static final int ADJACENT_TREE_BLOCK_SEARCH_DIST = 4;
   protected static final int ADJACENT_TREE_BLOCK_CHUNK_CHECK_DIST = 5;
   protected int[][][] adjacentTreeBlocks3D = new int[32][32][32];

   public LeavesBlock(int iBlockID) {
      super(iBlockID);
      this.c(0.2F);
      this.setAxesEffectiveOn(true);
      this.setBuoyant();
      this.k(1);
      this.setFireProperties(Flammability.LEAVES);
      this.a(i);
      this.c("leaves");
   }

   @Override
   public float getMovementModifier(World world, int i, int j, int k) {
      return 0.5F;
   }

   @Override
   public boolean canGroundCoverRestOnBlock(World world, int i, int j, int k) {
      return true;
   }

   @Override
   public void dropBlockAsItemWithChance(World world, int i, int j, int k, int iMetadata, float fChance, int iFortuneModifier) {
      if (!world.isRemote) {
         int iChanceOfSaplingDrop = 20;
         if (world.rand.nextInt(iChanceOfSaplingDrop) == 0) {
            int iIdDropped = this.idDropped(iMetadata, world.rand, iFortuneModifier);
            this.b(world, i, j, k, new ItemStack(iIdDropped, 1, this.damageDropped(iMetadata)));
         }
      }
   }

   @Override
   public int idDropped(int metadata, Random rand, int fortuneModifier) {
      int type = metadata & 3;
      switch (type) {
         case 1:
            return BTWBlocks.spruceSapling.blockID;
         case 2:
            return BTWBlocks.birchSapling.blockID;
         case 3:
            return BTWBlocks.jungleSapling.blockID;
         default:
            return BTWBlocks.oakSapling.blockID;
      }
   }

   @Override
   public int damageDropped(int metadata) {
      return 0;
   }

   @Override
   public void updateTick(World world, int i, int j, int k, Random rand) {
      if (!world.isRemote) {
         int iMetadata = world.getBlockMetadata(i, j, k);
         if ((iMetadata & 8) != 0 && (iMetadata & 4) == 0 && world.checkChunksExist(i - 5, j - 5, k - 5, i + 5, j + 5, k + 5)) {
            this.updateAdjacentTreeBlockArray(world, i, j, k);
            int var12 = this.adjacentTreeBlocks3D[16][16][16];
            if (var12 >= 0) {
               int iNewMetadata = iMetadata & 7;
               world.SetBlockMetadataWithNotify(i, j, k, iNewMetadata, 4);
            } else {
               this.c(world, i, j, k, world.getBlockMetadata(i, j, k), 0);
               world.setBlockToAir(i, j, k);
            }
         }
      }
   }

   @Override
   public boolean hasLargeCenterHardPointToFacing(IBlockAccess blockAccess, int i, int j, int k, int iFacing, boolean bIgnoreTransparency) {
      return bIgnoreTransparency;
   }

   @Override
   public void onDestroyedByFire(World world, int i, int j, int k, int iFireAge, boolean bForcedFireSpread) {
      super.onDestroyedByFire(world, i, j, k, iFireAge, bForcedFireSpread);
      this.generateAshOnBurn(world, i, j, k);
   }

   @Override
   public ItemStack getStackRetrievedByBlockDispenser(World world, int i, int j, int k) {
      return this.c_(world.getBlockMetadata(i, j, k));
   }

   @Override
   public boolean canMobsSpawnOn(World world, int i, int j, int k) {
      return world.provider.dimensionId != -1;
   }

   @Override
   public boolean isLeafBlock(IBlockAccess blockAccess, int x, int y, int z) {
      return true;
   }

   protected void generateAshOnBurn(World world, int i, int j, int k) {
      for (int iTempJ = j; iTempJ > 0; iTempJ--) {
         if (AshGroundCoverBlock.canAshReplaceBlock(world, i, iTempJ, k)) {
            int iBlockBelowID = world.getBlockId(i, iTempJ - 1, k);
            Block blockBelow = Block.blocksList[iBlockBelowID];
            if (blockBelow != null && blockBelow.canGroundCoverRestOnBlock(world, i, iTempJ - 1, k)) {
               world.setBlockWithNotify(i, iTempJ, k, BTWBlocks.ashCoverBlock.blockID);
               break;
            }
         } else if (world.getBlockId(i, iTempJ, k) != Block.fire.blockID) {
            break;
         }
      }
   }

   protected void updateAdjacentTreeBlockArray(World world, int x, int y, int z) {
      for (int i = -4; i <= 4; i++) {
         for (int j = -4; j <= 4; j++) {
            for (int k = -4; k <= 4; k++) {
               int blockID = world.getBlockId(x + i, y + j, z + k);
               Block block = Block.blocksList[blockID];
               if (block != null && block.canSupportLeaves(world, x, y, z)) {
                  this.adjacentTreeBlocks3D[i + 16][j + 16][k + 16] = 0;
               } else if (block != null && block.isLeafBlock(world, x, y, z)) {
                  this.adjacentTreeBlocks3D[i + 16][j + 16][k + 16] = -2;
               } else {
                  this.adjacentTreeBlocks3D[i + 16][j + 16][k + 16] = -1;
               }
            }
         }
      }

      for (int distance = 1; distance <= 4; distance++) {
         for (int i = -4; i <= 4; i++) {
            for (int j = -4; j <= 4; j++) {
               for (int kx = -4; kx <= 4; kx++) {
                  if (this.adjacentTreeBlocks3D[i + 16][j + 16][kx + 16] == distance - 1) {
                     if (this.adjacentTreeBlocks3D[i + 16 - 1][j + 16][kx + 16] == -2) {
                        this.adjacentTreeBlocks3D[i + 16 - 1][j + 16][kx + 16] = distance;
                     }

                     if (this.adjacentTreeBlocks3D[i + 16 + 1][j + 16][kx + 16] == -2) {
                        this.adjacentTreeBlocks3D[i + 16 + 1][j + 16][kx + 16] = distance;
                     }

                     if (this.adjacentTreeBlocks3D[i + 16][j + 16 - 1][kx + 16] == -2) {
                        this.adjacentTreeBlocks3D[i + 16][j + 16 - 1][kx + 16] = distance;
                     }

                     if (this.adjacentTreeBlocks3D[i + 16][j + 16 + 1][kx + 16] == -2) {
                        this.adjacentTreeBlocks3D[i + 16][j + 16 + 1][kx + 16] = distance;
                     }

                     if (this.adjacentTreeBlocks3D[i + 16][j + 16][kx + 16 - 1] == -2) {
                        this.adjacentTreeBlocks3D[i + 16][j + 16][kx + 16 - 1] = distance;
                     }

                     if (this.adjacentTreeBlocks3D[i + 16][j + 16][kx + 16 + 1] == -2) {
                        this.adjacentTreeBlocks3D[i + 16][j + 16][kx + 16 + 1] = distance;
                     }
                  }
               }
            }
         }
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void randomDisplayTick(World world, int i, int j, int k, Random rand) {
      if (world.isRainingAtPos(i, j + 1, k) && !world.doesBlockHaveSolidTopSurface(i, j - 1, k) && rand.nextInt(15) == 1) {
         world.spawnParticle("dripWater", i + rand.nextDouble(), j - 0.05, k + rand.nextDouble(), 0.0, 0.0, 0.0);
      }
   }
}
