package btw.world.feature.trees;

import net.minecraft.src.MathHelper;
import net.minecraft.src.WorldGenBigTree;

public class BigTreeGenerator extends WorldGenBigTree {
   public BigTreeGenerator(boolean bDoBlockNotify) {
      super(bDoBlockNotify);
   }

   @Override
   public void genTreeLayer(int par1, int par2, int par3, float par4, byte par5, int par6) {
      int i = (int)(par4 + 0.618);
      byte byte0 = doBlockNotify[par5];
      byte byte1 = doBlockNotify[par5 + 3];
      int[] ai = new int[]{par1, par2, par3};
      int[] ai1 = new int[]{0, 0, 0};
      int j = -i;
      int k = -i;

      for (ai1[par5] = ai[par5]; j <= i; j++) {
         ai1[byte0] = ai[byte0] + j;
         int l = -i;

         while (l <= i) {
            double d = Math.sqrt(Math.pow(Math.abs(j) + 0.5, 2.0) + Math.pow(Math.abs(l) + 0.5, 2.0));
            if (d > par4) {
               l++;
            } else {
               ai1[byte1] = ai[byte1] + l;
               int i1 = this.worldObj.getBlockId(ai1[0], ai1[1], ai1[2]);
               if (!this.worldObj.isAirBlock(ai1[0], ai1[1], ai1[2]) && i1 != 18) {
                  l++;
               } else {
                  this.a(this.worldObj, ai1[0], ai1[1], ai1[2], par6, 0);
                  l++;
               }
            }
         }
      }
   }

   @Override
   public int checkBlockLine(int[] par1ArrayOfInteger, int[] par2ArrayOfInteger) {
      int[] ai = new int[]{0, 0, 0};
      byte byte0 = 0;

      int i;
      for (i = 0; byte0 < 3; byte0++) {
         ai[byte0] = par2ArrayOfInteger[byte0] - par1ArrayOfInteger[byte0];
         if (Math.abs(ai[byte0]) > Math.abs(ai[i])) {
            i = byte0;
         }
      }

      if (ai[i] == 0) {
         return -1;
      } else {
         byte byte1 = doBlockNotify[i];
         byte byte2 = doBlockNotify[i + 3];
         byte byte3;
         if (ai[i] > 0) {
            byte3 = 1;
         } else {
            byte3 = -1;
         }

         double d = (double)ai[byte1] / ai[i];
         double d1 = (double)ai[byte2] / ai[i];
         int[] ai1 = new int[]{0, 0, 0};
         int j = 0;

         int k;
         for (k = ai[i] + byte3; j != k; j += byte3) {
            ai1[i] = par1ArrayOfInteger[i] + j;
            ai1[byte1] = MathHelper.floor_double(par1ArrayOfInteger[byte1] + j * d);
            ai1[byte2] = MathHelper.floor_double(par1ArrayOfInteger[byte2] + j * d1);
            int l = this.worldObj.getBlockId(ai1[0], ai1[1], ai1[2]);
            if (!this.worldObj.isAirBlock(ai1[0], ai1[1], ai1[2]) && l != 18) {
               break;
            }
         }

         return j == k ? -1 : Math.abs(j);
      }
   }

   @Override
   public boolean validTreeLocation() {
      int[] ai = new int[]{this.basePos[0], this.basePos[1], this.basePos[2]};
      int[] ai1 = new int[]{this.basePos[0], this.basePos[1] + this.heightLimit - 1, this.basePos[2]};
      int i = this.worldObj.getBlockId(this.basePos[0], this.basePos[1] - 1, this.basePos[2]);
      if (!TreeUtils.canSaplingGrowOnBlock(this.worldObj, this.basePos[0], this.basePos[1] - 1, this.basePos[2])) {
         return false;
      } else {
         int j = this.checkBlockLine(ai, ai1);
         if (j == -1) {
            return true;
         } else if (j < 6) {
            return false;
         } else {
            this.heightLimit = j;
            return true;
         }
      }
   }
}
