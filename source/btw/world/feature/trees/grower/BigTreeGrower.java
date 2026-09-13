package btw.world.feature.trees.grower;

import java.util.Random;
import net.minecraft.src.Block;
import net.minecraft.src.MathHelper;
import net.minecraft.src.World;

public class BigTreeGrower extends AbstractTreeGrower {
   public static final byte[] otherCoordPairs = new byte[]{2, 0, 0, 1, 2, 1};
   public Random rand = new Random();
   public World worldObj;
   public int[] basePos = new int[]{0, 0, 0};
   public int heightLimit = 0;
   public int height;
   public double heightAttenuation = 0.618;
   public double branchSlope = 0.381;
   public double scaleWidth = 1.0;
   public double leafDensity = 1.0;
   public int trunkSize = 1;
   public int heightLimitLimit = 12;
   public int leafDistanceLimit = 4;
   public int[][] leafNodes;

   public BigTreeGrower(String name, int minTreeHeight, int maxTreeHeight, TreeGrowers.TreeWoodType woodType) {
      super(name, minTreeHeight, maxTreeHeight, woodType);
   }

   @Override
   public boolean growTree(World world, Random rand, int x, int y, int z, boolean isWorldGen) {
      this.worldObj = world;
      long var6x = rand.nextLong();
      this.rand.setSeed(var6x);
      this.basePos[0] = x;
      this.basePos[1] = y;
      this.basePos[2] = z;
      this.heightLimit = this.minTreeHeight + this.rand.nextInt(this.maxTreeHeight - this.minTreeHeight + 1);
      if (!this.canTreeGrowHere()) {
         return false;
      } else {
         this.generateLeafNodeList();
         this.generateLeaves(isWorldGen);
         this.generateTrunk(isWorldGen);
         this.generateLeafNodeBases(isWorldGen);
         int trunkBlockId = world.getBlockId(x, y, z);
         if (trunkBlockId == this.woodType.woodBlockID) {
            this.setBlockAndMetadata(world, x, y, z, this.woodType.stumpBlockID, this.woodType.stumpMetadata, isWorldGen);
         }

         return true;
      }
   }

   protected boolean canTreeGrowHere() {
      int[] var1 = new int[]{this.basePos[0], this.basePos[1], this.basePos[2]};
      int[] var2 = new int[]{this.basePos[0], this.basePos[1] + this.heightLimit - 1, this.basePos[2]};
      int var3 = this.worldObj.getBlockId(this.basePos[0], this.basePos[1] - 1, this.basePos[2]);
      if (var3 != 2 && var3 != 3) {
         return false;
      } else {
         int var4 = this.checkBlockLine(var1, var2);
         if (var4 == -1) {
            return true;
         } else if (var4 < 6) {
            return false;
         } else {
            this.heightLimit = var4;
            return true;
         }
      }
   }

   public void generateLeafNodeList() {
      this.height = (int)(this.heightLimit * this.heightAttenuation);
      if (this.height >= this.heightLimit) {
         this.height = this.heightLimit - 1;
      }

      int var1 = (int)(1.382 + Math.pow(this.leafDensity * this.heightLimit / 13.0, 2.0));
      if (var1 < 1) {
         var1 = 1;
      }

      int[][] var2 = new int[var1 * this.heightLimit][4];
      int var3 = this.basePos[1] + this.heightLimit - this.leafDistanceLimit;
      int var4 = 1;
      int var5 = this.basePos[1] + this.height;
      int var6 = var3 - this.basePos[1];
      var2[0][0] = this.basePos[0];
      var2[0][1] = var3;
      var2[0][2] = this.basePos[2];
      var2[0][3] = var5;
      var3--;

      while (var6 >= 0) {
         int var7 = 0;
         float var8 = this.layerSize(var6);
         if (!(var8 < 0.0F)) {
            for (double var9 = 0.5; var7 < var1; var7++) {
               double var11 = this.scaleWidth * var8 * (this.rand.nextFloat() + 0.328);
               double var13 = this.rand.nextFloat() * 2.0 * Math.PI;
               int var15 = MathHelper.floor_double(var11 * Math.sin(var13) + this.basePos[0] + var9);
               int var16 = MathHelper.floor_double(var11 * Math.cos(var13) + this.basePos[2] + var9);
               int[] var17 = new int[]{var15, var3, var16};
               int[] var18 = new int[]{var15, var3 + this.leafDistanceLimit, var16};
               if (this.checkBlockLine(var17, var18) == -1) {
                  int[] var19 = new int[]{this.basePos[0], this.basePos[1], this.basePos[2]};
                  double var20 = Math.sqrt(Math.pow(Math.abs(this.basePos[0] - var17[0]), 2.0) + Math.pow(Math.abs(this.basePos[2] - var17[2]), 2.0));
                  double var22 = var20 * this.branchSlope;
                  if (var17[1] - var22 > var5) {
                     var19[1] = var5;
                  } else {
                     var19[1] = (int)(var17[1] - var22);
                  }

                  if (this.checkBlockLine(var19, var17) == -1) {
                     var2[var4][0] = var15;
                     var2[var4][1] = var3;
                     var2[var4][2] = var16;
                     var2[var4][3] = var19[1];
                     var4++;
                  }
               }
            }
         }

         var3--;
         var6--;
      }

      this.leafNodes = new int[var4][4];
      System.arraycopy(var2, 0, this.leafNodes, 0, var4);
   }

   public void genTreeLayer(int par1, int par2, int par3, float par4, byte par5, int blockID, int metadata, boolean isWorldGen) {
      int var7x = (int)(par4 + 0.618);
      byte var8x = otherCoordPairs[par5];
      byte var9 = otherCoordPairs[par5 + 3];
      int[] var10 = new int[]{par1, par2, par3};
      int[] var11 = new int[]{0, 0, 0};
      int var12 = -var7x;
      int var13 = -var7x;

      for (var11[par5] = var10[par5]; var12 <= var7x; var12++) {
         var11[var8x] = var10[var8x] + var12;
         var13 = -var7x;

         while (var13 <= var7x) {
            double var15 = Math.pow(Math.abs(var12) + 0.5, 2.0) + Math.pow(Math.abs(var13) + 0.5, 2.0);
            if (var15 > par4 * par4) {
               var13++;
            } else {
               var11[var9] = var10[var9] + var13;
               int blockIDToCheck = this.worldObj.getBlockId(var11[0], var11[1], var11[2]);
               Block blockToCheck = Block.blocksList[blockIDToCheck];
               if (blockToCheck == null || blockToCheck.isLeafBlock(this.worldObj, var11[0], var11[1], var11[2])) {
                  this.setBlockAndMetadata(this.worldObj, var11[0], var11[1], var11[2], blockID, metadata, isWorldGen);
               }

               var13++;
            }
         }
      }
   }

   public float layerSize(int par1) {
      if (par1 < this.heightLimit * 0.3) {
         return -1.618F;
      } else {
         float var2 = this.heightLimit / 2.0F;
         float var3 = this.heightLimit / 2.0F - par1;
         float var4;
         if (var3 == 0.0F) {
            var4 = var2;
         } else if (Math.abs(var3) >= var2) {
            var4 = 0.0F;
         } else {
            var4 = (float)Math.sqrt(Math.pow(Math.abs(var2), 2.0) - Math.pow(Math.abs(var3), 2.0));
         }

         return var4 * 0.5F;
      }
   }

   public float leafSize(int par1) {
      return par1 >= 0 && par1 < this.leafDistanceLimit ? (par1 != 0 && par1 != this.leafDistanceLimit - 1 ? 3.0F : 2.0F) : -1.0F;
   }

   public void generateLeafNode(int par1, int par2, int par3, boolean isWorldGen) {
      int var4x = par2;

      for (int var5 = par2 + this.leafDistanceLimit; var4x < var5; var4x++) {
         float var6 = this.leafSize(var4x - par2);
         this.genTreeLayer(par1, var4x, par3, var6, (byte)1, this.woodType.leavesBlockID, this.woodType.leavesMetadata, isWorldGen);
      }
   }

   public void placeBlockLine(int[] par1ArrayOfInteger, int[] par2ArrayOfInteger, int blockID, int metadata, boolean isWorldGen) {
      int[] var4x = new int[]{0, 0, 0};
      byte var5x = 0;

      byte var6;
      for (var6 = 0; var5x < 3; var5x++) {
         var4x[var5x] = par2ArrayOfInteger[var5x] - par1ArrayOfInteger[var5x];
         if (Math.abs(var4x[var5x]) > Math.abs(var4x[var6])) {
            var6 = var5x;
         }
      }

      if (var4x[var6] != 0) {
         byte var7 = otherCoordPairs[var6];
         byte var8 = otherCoordPairs[var6 + 3];
         byte var9;
         if (var4x[var6] > 0) {
            var9 = 1;
         } else {
            var9 = -1;
         }

         double var10 = (double)var4x[var7] / var4x[var6];
         double var12 = (double)var4x[var8] / var4x[var6];
         int[] var14 = new int[]{0, 0, 0};
         int var15 = 0;

         for (int var16 = var4x[var6] + var9; var15 != var16; var15 += var9) {
            var14[var6] = MathHelper.floor_double(par1ArrayOfInteger[var6] + var15 + 0.5);
            var14[var7] = MathHelper.floor_double(par1ArrayOfInteger[var7] + var15 * var10 + 0.5);
            var14[var8] = MathHelper.floor_double(par1ArrayOfInteger[var8] + var15 * var12 + 0.5);
            byte rotation = 0;
            int var18 = Math.abs(var14[0] - par1ArrayOfInteger[0]);
            int var19 = Math.abs(var14[2] - par1ArrayOfInteger[2]);
            int var20 = Math.max(var18, var19);
            if (var20 > 0) {
               if (var18 == var20) {
                  rotation = 4;
               } else if (var19 == var20) {
                  rotation = 8;
               }
            }

            this.setBlockAndMetadata(this.worldObj, var14[0], var14[1], var14[2], blockID, metadata | rotation, isWorldGen);
         }
      }
   }

   public void generateLeaves(boolean isWorldGen) {
      int var1x = 0;

      for (int var2 = this.leafNodes.length; var1x < var2; var1x++) {
         int var3 = this.leafNodes[var1x][0];
         int var4 = this.leafNodes[var1x][1];
         int var5 = this.leafNodes[var1x][2];
         this.generateLeafNode(var3, var4, var5, isWorldGen);
      }
   }

   public boolean leafNodeNeedsBase(int par1) {
      return par1 >= this.heightLimit * 0.2;
   }

   public void generateTrunk(boolean isWorldGen) {
      int var1x = this.basePos[0];
      int var2 = this.basePos[1];
      int var3 = this.basePos[1] + this.height;
      int var4 = this.basePos[2];
      int[] var5 = new int[]{var1x, var2, var4};
      int[] var6 = new int[]{var1x, var3, var4};
      this.placeBlockLine(var5, var6, this.woodType.woodBlockID, this.woodType.woodMetadata, isWorldGen);
      if (this.trunkSize == 2) {
         var5[0]++;
         var6[0]++;
         this.placeBlockLine(var5, var6, this.woodType.woodBlockID, this.woodType.woodMetadata, isWorldGen);
         var5[2]++;
         var6[2]++;
         this.placeBlockLine(var5, var6, this.woodType.woodBlockID, this.woodType.woodMetadata, isWorldGen);
         var5[0] += -1;
         var6[0] += -1;
         this.placeBlockLine(var5, var6, this.woodType.woodBlockID, this.woodType.woodMetadata, isWorldGen);
      }
   }

   public void generateLeafNodeBases(boolean isWorldGen) {
      int var1x = 0;
      int var2 = this.leafNodes.length;

      for (int[] var3 = new int[]{this.basePos[0], this.basePos[1], this.basePos[2]}; var1x < var2; var1x++) {
         int[] var4 = this.leafNodes[var1x];
         int[] var5 = new int[]{var4[0], var4[1], var4[2]};
         var3[1] = var4[3];
         int var6 = var3[1] - this.basePos[1];
         if (this.leafNodeNeedsBase(var6)) {
            this.placeBlockLine(var3, var5, this.woodType.woodBlockID, this.woodType.woodMetadata, isWorldGen);
         }
      }
   }

   public int checkBlockLine(int[] par1ArrayOfInteger, int[] par2ArrayOfInteger) {
      int[] var3 = new int[]{0, 0, 0};
      byte var4 = 0;

      byte var5;
      for (var5 = 0; var4 < 3; var4++) {
         var3[var4] = par2ArrayOfInteger[var4] - par1ArrayOfInteger[var4];
         if (Math.abs(var3[var4]) > Math.abs(var3[var5])) {
            var5 = var4;
         }
      }

      if (var3[var5] == 0) {
         return -1;
      } else {
         byte var6 = otherCoordPairs[var5];
         byte var7 = otherCoordPairs[var5 + 3];
         byte var8;
         if (var3[var5] > 0) {
            var8 = 1;
         } else {
            var8 = -1;
         }

         double var9 = (double)var3[var6] / var3[var5];
         double var11 = (double)var3[var7] / var3[var5];
         int[] var13 = new int[]{0, 0, 0};
         int var14 = 0;

         int var15;
         for (var15 = var3[var5] + var8; var14 != var15; var14 += var8) {
            var13[var5] = par1ArrayOfInteger[var5] + var14;
            var13[var6] = MathHelper.floor_double(par1ArrayOfInteger[var6] + var14 * var9);
            var13[var7] = MathHelper.floor_double(par1ArrayOfInteger[var7] + var14 * var11);
            int blockID = this.worldObj.getBlockId(var13[0], var13[1], var13[2]);
            Block block = Block.blocksList[blockID];
            if (block != null && !block.isLeafBlock(this.worldObj, var13[0], var13[1], var13[2])) {
               break;
            }
         }

         return var14 == var15 ? -1 : Math.abs(var14);
      }
   }

   public void setScale(double par1, double par3, double par5) {
      this.heightLimitLimit = (int)(par1 * 12.0);
      if (par1 > 0.5) {
         this.leafDistanceLimit = 5;
      }

      this.scaleWidth = par3;
      this.leafDensity = par5;
   }
}
