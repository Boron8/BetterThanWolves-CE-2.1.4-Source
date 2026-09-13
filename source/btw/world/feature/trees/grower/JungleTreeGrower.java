package btw.world.feature.trees.grower;

import java.util.Random;
import net.minecraft.src.Block;
import net.minecraft.src.MathHelper;
import net.minecraft.src.World;

public class JungleTreeGrower extends AbstractTreeGrower {
   public JungleTreeGrower(String name, int minTreeHeight, int maxTreeHeight, TreeGrowers.TreeWoodType woodType) {
      super(name, minTreeHeight, maxTreeHeight, woodType);
   }

   @Override
   public boolean growTree(World world, Random rand, int x, int y, int z, boolean isWorldGen) {
      int treeHeight = rand.nextInt(this.maxTreeHeight - this.minTreeHeight) + this.minTreeHeight;
      boolean canGrow = true;
      if (y >= 1 && y + treeHeight + 1 <= 256) {
         for (int var8 = y; var8 <= y + 1 + treeHeight; var8++) {
            byte var9 = 2;
            if (var8 == y) {
               var9 = 1;
            }

            if (var8 >= y + 1 + treeHeight - 2) {
               var9 = 2;
            }

            for (int var10 = x - var9; var10 <= x + var9 && canGrow; var10++) {
               for (int var11 = z - var9; var11 <= z + var9 && canGrow; var11++) {
                  if (var8 >= 0 && var8 < 256) {
                     int var12 = world.getBlockId(var10, var8, var11);
                     Block block = Block.blocksList[var12];
                     if (block != null
                        && !block.isLog(world, var10, var8, var11)
                        && !block.isLeafBlock(world, var10, var8, var11)
                        && block.blockID != Block.grass.blockID
                        && block.blockID != Block.dirt.blockID
                        && !block.blockMaterial.isReplaceable()) {
                        canGrow = false;
                     }
                  } else {
                     canGrow = false;
                  }
               }
            }
         }

         if (!canGrow) {
            return false;
         } else {
            int var17 = world.getBlockId(x, y - 1, z);
            Block blockBelow = Block.blocksList[var17];
            if (blockBelow != null && blockBelow.canSaplingsGrowOnBlock(world, x, y - 1, z) && y < 256 - treeHeight - 1) {
               if (var17 == Block.grass.blockID || var17 == Block.dirt.blockID) {
                  world.setBlock(x, y - 1, z, Block.dirt.blockID, 0, 2);
                  world.setBlock(x + 1, y - 1, z, Block.dirt.blockID, 0, 2);
                  world.setBlock(x, y - 1, z + 1, Block.dirt.blockID, 0, 2);
                  world.setBlock(x + 1, y - 1, z + 1, Block.dirt.blockID, 0, 2);
               }

               this.growLeaves(world, x, z, y + treeHeight, 2, rand, isWorldGen);

               for (int var14 = y + treeHeight - 2 - rand.nextInt(4); var14 > y + treeHeight / 2; var14 -= 2 + rand.nextInt(4)) {
                  float var15 = rand.nextFloat() * (float) Math.PI * 2.0F;
                  int var11x = x + (int)(0.5F + MathHelper.cos(var15) * 4.0F);
                  int var12 = z + (int)(0.5F + MathHelper.sin(var15) * 4.0F);
                  this.growLeaves(world, var11x, var12, var14, 0, rand, isWorldGen);

                  for (int var13 = 0; var13 < 5; var13++) {
                     var11x = x + (int)(1.5F + MathHelper.cos(var15) * var13);
                     var12 = z + (int)(1.5F + MathHelper.sin(var15) * var13);
                     this.setBlockAndMetadata(world, var11x, var14 - 3 + var13 / 2, var12, this.woodType.woodBlockID, this.woodType.woodMetadata, isWorldGen);
                  }
               }

               for (int var10 = 0; var10 < treeHeight; var10++) {
                  int var11x = world.getBlockId(x, y + var10, z);
                  Block block = Block.blocksList[var11x];
                  if (block == null || block.isLeafBlock(world, x, y + var10, z)) {
                     this.setBlockAndMetadata(world, x, y + var10, z, this.woodType.woodBlockID, this.woodType.woodMetadata, isWorldGen);
                     if (var10 > 0) {
                        if (rand.nextInt(3) > 0 && world.isAirBlock(x - 1, y + var10, z)) {
                           this.setBlockAndMetadata(world, x - 1, y + var10, z, Block.vine.blockID, 8, isWorldGen);
                        }

                        if (rand.nextInt(3) > 0 && world.isAirBlock(x, y + var10, z - 1)) {
                           this.setBlockAndMetadata(world, x, y + var10, z - 1, Block.vine.blockID, 1, isWorldGen);
                        }
                     }
                  }

                  if (var10 < treeHeight - 1) {
                     var11x = world.getBlockId(x + 1, y + var10, z);
                     block = Block.blocksList[var11x];
                     if (block == null || block.isLeafBlock(world, x + 1, y + var10, z)) {
                        this.setBlockAndMetadata(world, x + 1, y + var10, z, this.woodType.woodBlockID, this.woodType.woodMetadata, isWorldGen);
                        if (var10 > 0) {
                           if (rand.nextInt(3) > 0 && world.isAirBlock(x + 2, y + var10, z)) {
                              this.setBlockAndMetadata(world, x + 2, y + var10, z, Block.vine.blockID, 2, isWorldGen);
                           }

                           if (rand.nextInt(3) > 0 && world.isAirBlock(x + 1, y + var10, z - 1)) {
                              this.setBlockAndMetadata(world, x + 1, y + var10, z - 1, Block.vine.blockID, 1, isWorldGen);
                           }
                        }
                     }

                     var11x = world.getBlockId(x + 1, y + var10, z + 1);
                     block = Block.blocksList[var11x];
                     if (block == null || block.isLeafBlock(world, x + 1, y + var10, z + 1)) {
                        this.setBlockAndMetadata(world, x + 1, y + var10, z + 1, this.woodType.woodBlockID, this.woodType.woodMetadata, isWorldGen);
                        if (var10 > 0) {
                           if (rand.nextInt(3) > 0 && world.isAirBlock(x + 2, y + var10, z + 1)) {
                              this.setBlockAndMetadata(world, x + 2, y + var10, z + 1, Block.vine.blockID, 2, isWorldGen);
                           }

                           if (rand.nextInt(3) > 0 && world.isAirBlock(x + 1, y + var10, z + 2)) {
                              this.setBlockAndMetadata(world, x + 1, y + var10, z + 2, Block.vine.blockID, 4, isWorldGen);
                           }
                        }
                     }

                     var11x = world.getBlockId(x, y + var10, z + 1);
                     block = Block.blocksList[var11x];
                     if (block == null || block.isLeafBlock(world, x, y + var10, z + 1)) {
                        this.setBlockAndMetadata(world, x, y + var10, z + 1, this.woodType.woodBlockID, this.woodType.woodMetadata, isWorldGen);
                        if (var10 > 0) {
                           if (rand.nextInt(3) > 0 && world.isAirBlock(x - 1, y + var10, z + 1)) {
                              this.setBlockAndMetadata(world, x - 1, y + var10, z + 1, Block.vine.blockID, 8, isWorldGen);
                           }

                           if (rand.nextInt(3) > 0 && world.isAirBlock(x, y + var10, z + 2)) {
                              this.setBlockAndMetadata(world, x, y + var10, z + 2, Block.vine.blockID, 4, isWorldGen);
                           }
                        }
                     }
                  }
               }

               this.attemptToPlaceStump(world, x, y, z, isWorldGen);
               this.attemptToPlaceStump(world, x, y, z + 1, isWorldGen);
               this.attemptToPlaceStump(world, x + 1, y, z, isWorldGen);
               this.attemptToPlaceStump(world, x + 1, y, z + 1, isWorldGen);
               return true;
            } else {
               return false;
            }
         }
      } else {
         return false;
      }
   }

   private void attemptToPlaceStump(World world, int x, int y, int z, boolean isWorldGen) {
      int iTrunkBlockId = world.getBlockId(x, y, z);
      if (iTrunkBlockId == this.woodType.woodBlockID) {
         this.setBlockAndMetadata(world, x, y, z, this.woodType.stumpBlockID, this.woodType.stumpMetadata, isWorldGen);
      }
   }

   private void growLeaves(World world, int x, int y, int z, int par5, Random rand, boolean isWorldGen) {
      byte var7x = 2;

      for (int k = z - var7x; k <= z; k++) {
         int zDist = k - z;
         int var10 = par5 + 1 - zDist;

         for (int i = x - var10; i <= x + var10 + 1; i++) {
            int xDist = i - x;

            for (int j = y - var10; j <= y + var10 + 1; j++) {
               int yDist = j - y;
               if ((xDist >= 0 || yDist >= 0 || xDist * xDist + yDist * yDist <= var10 * var10)
                  && (xDist <= 0 && yDist <= 0 || xDist * xDist + yDist * yDist <= (var10 + 1) * (var10 + 1))
                  && (rand.nextInt(4) != 0 || xDist * xDist + yDist * yDist <= (var10 - 1) * (var10 - 1))) {
                  int var15 = world.getBlockId(i, k, j);
                  Block block = Block.blocksList[var15];
                  if (block == null || block.isLeafBlock(world, i, k, j)) {
                     this.setBlockAndMetadata(world, i, k, j, this.woodType.leavesBlockID, this.woodType.leavesMetadata, isWorldGen);
                  }
               }
            }
         }
      }
   }
}
