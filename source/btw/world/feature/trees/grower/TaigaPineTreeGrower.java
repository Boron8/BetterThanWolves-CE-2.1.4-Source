package btw.world.feature.trees.grower;

import java.util.Random;
import net.minecraft.src.Block;
import net.minecraft.src.World;

public class TaigaPineTreeGrower extends AbstractTreeGrower {
   public TaigaPineTreeGrower(String name, int minTreeHeight, int maxTreeHeight, TreeGrowers.TreeWoodType woodType) {
      super(name, minTreeHeight, maxTreeHeight, woodType);
   }

   @Override
   public boolean growTree(World world, Random rand, int x, int y, int z, boolean isWorldGen) {
      int treeHeight = rand.nextInt(this.maxTreeHeight - this.minTreeHeight + 1) + this.minTreeHeight;
      int var7 = treeHeight - rand.nextInt(2) - 3;
      int var8 = treeHeight - var7;
      int var9 = 1 + rand.nextInt(var8 + 1);
      boolean var10 = true;
      if (y >= 1 && y + treeHeight + 1 <= 128) {
         for (int var11 = y; var11 <= y + 1 + treeHeight && var10; var11++) {
            int var18;
            if (var11 - y < var7) {
               var18 = 0;
            } else {
               var18 = var9;
            }

            for (int var13 = x - var18; var13 <= x + var18 && var10; var13++) {
               for (int var14 = z - var18; var14 <= z + var18 && var10; var14++) {
                  if (var11 >= 0 && var11 < 128) {
                     int var15 = world.getBlockId(var13, var11, var14);
                     Block block = Block.blocksList[var15];
                     if (block != null && !block.isLeafBlock(world, var13, var11, var14) && !block.blockMaterial.isReplaceable()) {
                        var10 = false;
                     }
                  } else {
                     var10 = false;
                  }
               }
            }
         }

         if (!var10) {
            return false;
         } else {
            int var22 = world.getBlockId(x, y - 1, z);
            Block blockBelow = Block.blocksList[var22];
            if (blockBelow.canSaplingsGrowOnBlock(world, x, y - 1, z) && y < 128 - treeHeight - 1) {
               if (var22 == Block.grass.blockID || var22 == Block.dirt.blockID) {
                  this.setBlock(world, x, y - 1, z, Block.dirt.blockID, isWorldGen);
               }

               int var18 = 0;

               for (int var13 = y + treeHeight; var13 >= y + var7; var13--) {
                  for (int var14x = x - var18; var14x <= x + var18; var14x++) {
                     int var15 = var14x - x;

                     for (int var16 = z - var18; var16 <= z + var18; var16++) {
                        int var17 = var16 - z;
                        if (Math.abs(var15) != var18 || Math.abs(var17) != var18 || var18 <= 0) {
                           int blockID = world.getBlockId(var14x, var13, var16);
                           Block block = Block.blocksList[blockID];
                           if (block == null || block.isLeafBlock(world, var14x, var13, var16) || block.blockMaterial.isReplaceable()) {
                              this.setBlockAndMetadata(world, var14x, var13, var16, this.woodType.leavesBlockID, this.woodType.leavesMetadata, isWorldGen);
                           }
                        }
                     }
                  }

                  if (var18 >= 1 && var13 == y + var7 + 1) {
                     var18--;
                  } else if (var18 < var9) {
                     var18++;
                  }
               }

               for (int var24 = 0; var24 < treeHeight - 1; var24++) {
                  int var14x = world.getBlockId(x, y + var24, z);
                  Block block = Block.blocksList[var14x];
                  if (block == null || block.isLeafBlock(world, x, y + var24, z)) {
                     if (var24 == 0) {
                        this.setBlockAndMetadata(world, x, y + var24, z, this.woodType.stumpBlockID, this.woodType.stumpMetadata, isWorldGen);
                     } else {
                        this.setBlockAndMetadata(world, x, y + var24, z, this.woodType.woodBlockID, this.woodType.woodMetadata, isWorldGen);
                     }
                  }
               }

               return true;
            } else {
               return false;
            }
         }
      } else {
         return false;
      }
   }
}
