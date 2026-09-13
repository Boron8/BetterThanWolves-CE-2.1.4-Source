package com.prupe.mcpatcher.mal.biome;

import com.prupe.mcpatcher.MCLogger;
import com.prupe.mcpatcher.mal.resource.FakeResourceLocation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.IBlockAccess;

@Environment(EnvType.CLIENT)
public abstract class ColorMapBase {
   private static final MCLogger logger = MCLogger.getLogger("Custom Colors");
   public static final int DEFAULT_HEIGHT = 64;

   @Environment(EnvType.CLIENT)
   public static final class Blended implements IColorMap {
      private final IColorMap parent;
      private final int blendRadius;
      private final int[][] offset;
      private final float[] weight;
      private final float[] lastColor = new float[3];

      public Blended(IColorMap parent, int blendRadius) {
         this.parent = parent;
         this.blendRadius = blendRadius;
         List<int[]> blendOffset = new ArrayList<>();
         List<Float> blendWeight = new ArrayList<>();
         float blendScale = 0.0F;

         for (int r = 0; r <= blendRadius; r++) {
            if (r == 0) {
               blendScale += addSample(blendOffset, blendWeight, 0, 0);
            } else {
               switch (r % 8) {
                  case 1:
                     blendScale += addSamples(blendOffset, blendWeight, r, 0, 1);
                     break;
                  case 2:
                     blendScale += addSamples(blendOffset, blendWeight, r, 1, 1);
                     break;
                  case 3:
                  case 4:
                     blendScale += addSamples(blendOffset, blendWeight, r, 1, 2);
                     break;
                  case 5:
                  case 6:
                     blendScale += addSamples(blendOffset, blendWeight, r, 1, 3);
                     break;
                  case 7:
                  default:
                     blendScale += addSamples(blendOffset, blendWeight, r, 2, 3);
               }
            }
         }

         this.offset = blendOffset.toArray(new int[blendOffset.size()][]);
         this.weight = new float[blendWeight.size()];

         for (int i = 0; i < blendWeight.size(); i++) {
            this.weight[i] = blendWeight.get(i) / blendScale;
         }
      }

      private static float addSample(List<int[]> blendOffset, List<Float> blendWeight, int di, int dk) {
         float weight = (float)Math.pow(1.0F + Math.max(Math.abs(di), Math.abs(dk)), -0.5);
         blendOffset.add(new int[]{di, dk});
         blendWeight.add(weight);
         return weight;
      }

      private static float addSamples(List<int[]> blendOffset, List<Float> blendWeight, int r, int num, int denom) {
         int s = num * r / denom;
         float sum = 0.0F;
         if (r % 2 == 0) {
            r ^= s;
            s ^= r;
            r ^= s;
         }

         sum += addSample(blendOffset, blendWeight, r, s);
         sum += addSample(blendOffset, blendWeight, -s, r);
         sum += addSample(blendOffset, blendWeight, -r, -s);
         return sum + addSample(blendOffset, blendWeight, s, -r);
      }

      @Override
      public String toString() {
         return this.parent.toString();
      }

      @Override
      public boolean isHeightDependent() {
         return this.parent.isHeightDependent();
      }

      @Override
      public int getColorMultiplier() {
         return this.parent.getColorMultiplier();
      }

      @Override
      public int getColorMultiplier(IBlockAccess blockAccess, int i, int j, int k) {
         return ColorUtils.float3ToInt(this.getColorMultiplierF(blockAccess, i, j, k));
      }

      @Override
      public float[] getColorMultiplierF(IBlockAccess blockAccess, int i, int j, int k) {
         this.lastColor[0] = 0.0F;
         this.lastColor[1] = 0.0F;
         this.lastColor[2] = 0.0F;

         for (int n = 0; n < this.weight.length; n++) {
            int[] offset = this.offset[n];
            float weight = this.weight[n];
            float[] tmpColor = this.parent.getColorMultiplierF(blockAccess, i + offset[0], j, k + offset[1]);
            this.lastColor[0] = this.lastColor[0] + tmpColor[0] * weight;
            this.lastColor[1] = this.lastColor[1] + tmpColor[1] * weight;
            this.lastColor[2] = this.lastColor[2] + tmpColor[2] * weight;
         }

         return this.lastColor;
      }

      @Override
      public void claimResources(Collection<FakeResourceLocation> resources) {
         this.parent.claimResources(resources);
      }

      @Override
      public IColorMap copy() {
         return new ColorMapBase.Blended(this.parent.copy(), this.blendRadius);
      }
   }

   @Environment(EnvType.CLIENT)
   public static final class Cached implements IColorMap {
      private final IColorMap parent;
      private int lastI = Integer.MIN_VALUE;
      private int lastJ = Integer.MIN_VALUE;
      private int lastK = Integer.MIN_VALUE;
      private int lastColorI;
      private float[] lastColorF;

      public Cached(IColorMap parent) {
         this.parent = parent;
      }

      @Override
      public String toString() {
         return this.parent.toString();
      }

      @Override
      public boolean isHeightDependent() {
         return this.parent.isHeightDependent();
      }

      @Override
      public int getColorMultiplier() {
         return this.parent.getColorMultiplier();
      }

      @Override
      public int getColorMultiplier(IBlockAccess blockAccess, int i, int j, int k) {
         if (i != this.lastI || j != this.lastJ || k != this.lastK) {
            this.lastColorI = this.parent.getColorMultiplier(blockAccess, i, j, k);
            this.lastI = i;
            this.lastJ = j;
            this.lastK = k;
         }

         return this.lastColorI;
      }

      @Override
      public float[] getColorMultiplierF(IBlockAccess blockAccess, int i, int j, int k) {
         if (i != this.lastI || j != this.lastJ || k != this.lastK) {
            this.lastColorF = this.parent.getColorMultiplierF(blockAccess, i, j, k);
            this.lastI = i;
            this.lastJ = j;
            this.lastK = k;
         }

         return this.lastColorF;
      }

      @Override
      public void claimResources(Collection<FakeResourceLocation> resources) {
         this.parent.claimResources(resources);
      }

      @Override
      public IColorMap copy() {
         return new ColorMapBase.Cached(this.parent.copy());
      }
   }

   @Environment(EnvType.CLIENT)
   public static final class Chunked implements IColorMap {
      private static final int I_MASK = -16;
      private static final int K_MASK = -16;
      private static final int I_SIZE = 17;
      private static final int J_SIZE = 2;
      private static final int K_SIZE = 17;
      private static final int IK_SIZE = 289;
      private static final int IJK_SIZE = 578;
      private final IColorMap parent;
      private int baseI = Integer.MIN_VALUE;
      private int baseJ = Integer.MIN_VALUE;
      private int baseK = Integer.MIN_VALUE;
      private int baseOffset;
      private final float[][] data = new float[578][];
      private final float[][] data1 = new float[578][3];
      private final boolean logEnabled = ColorMapBase.logger.isLoggable(Level.FINEST);
      private long lastLogTime = System.currentTimeMillis();
      private int calls;
      private int missChunk;
      private int missSheet;
      private int missBlock;

      public Chunked(IColorMap parent) {
         this.parent = parent;
      }

      @Override
      public String toString() {
         return this.parent.toString();
      }

      @Override
      public boolean isHeightDependent() {
         return this.parent.isHeightDependent();
      }

      @Override
      public int getColorMultiplier() {
         return this.parent.getColorMultiplier();
      }

      @Override
      public int getColorMultiplier(IBlockAccess blockAccess, int i, int j, int k) {
         return ColorUtils.float3ToInt(this.getColorMultiplierF(blockAccess, i, j, k));
      }

      @Override
      public float[] getColorMultiplierF(IBlockAccess blockAccess, int i, int j, int k) {
         int offset = this.getChunkOffset(i, j, k);
         this.calls++;
         if (offset < 0) {
            this.setChunkBase(i, j, k);
            offset = this.getChunkOffset(i, j, k);
            this.missChunk++;
         }

         float[] color = this.data[offset];
         if (color == null) {
            color = this.data[offset] = this.data1[offset];
            copy3f(color, this.parent.getColorMultiplierF(blockAccess, i, j, k));
            this.missBlock++;
         }

         if (this.logEnabled) {
            long now = System.currentTimeMillis();
            if (now - this.lastLogTime > 5000L) {
               float mult = 100.0F / this.calls;
               ColorMapBase.logger
                  .finest(
                     "%s: calls: %d, miss chunk: %.2f%%, miss sheet: %.2f%%, miss block: %.2f%%",
                     this,
                     this.calls,
                     this.missChunk * mult,
                     this.missSheet * mult,
                     this.missBlock * mult
                  );
               this.lastLogTime = now;
            }
         }

         return color;
      }

      @Override
      public void claimResources(Collection<FakeResourceLocation> resources) {
         this.parent.claimResources(resources);
      }

      @Override
      public IColorMap copy() {
         return new ColorMapBase.Chunked(this.parent.copy());
      }

      private static void copy3f(float[] dst, float[] src) {
         dst[0] = src[0];
         dst[1] = src[1];
         dst[2] = src[2];
      }

      private void setChunkBase(int i, int j, int k) {
         this.baseI = i & -16;
         this.baseJ = j;
         this.baseK = k & -16;
         this.baseOffset = 0;
         Arrays.fill(this.data, null);
      }

      private int getChunkOffset(int i, int j, int k) {
         i -= this.baseI;
         j -= this.baseJ;
         k -= this.baseK;
         if (j >= 0 && j <= 2 && k >= 0 && k < 17 && i >= 0 && i < 17) {
            if (j == 2) {
               j--;
               this.baseJ++;
               this.missSheet++;
               Arrays.fill(this.data, this.baseOffset, 578, null);
               this.baseOffset ^= 289;
            }

            return (this.baseOffset + j * 289 + k * 17 + i) % 578;
         } else {
            return -1;
         }
      }
   }

   @Environment(EnvType.CLIENT)
   public static final class Outer implements IColorMap {
      private final IColorMap parent;
      private final boolean isHeightDependent;
      private final int mapDefault;

      public Outer(IColorMap parent) {
         this.parent = parent;
         this.isHeightDependent = parent.isHeightDependent();
         this.mapDefault = parent.getColorMultiplier();
      }

      @Override
      public String toString() {
         return this.parent.toString();
      }

      @Override
      public boolean isHeightDependent() {
         return this.parent.isHeightDependent();
      }

      @Override
      public int getColorMultiplier() {
         return this.mapDefault;
      }

      @Override
      public int getColorMultiplier(IBlockAccess blockAccess, int i, int j, int k) {
         if (!this.isHeightDependent) {
            j = 64;
         }

         return this.parent.getColorMultiplier(blockAccess, i, j, k);
      }

      @Override
      public float[] getColorMultiplierF(IBlockAccess blockAccess, int i, int j, int k) {
         if (!this.isHeightDependent) {
            j = 64;
         }

         return this.parent.getColorMultiplierF(blockAccess, i, j, k);
      }

      @Override
      public void claimResources(Collection<FakeResourceLocation> resources) {
         this.parent.claimResources(resources);
      }

      @Override
      public IColorMap copy() {
         return new ColorMapBase.Outer(this.parent.copy());
      }
   }

   @Environment(EnvType.CLIENT)
   public static final class Smoothed implements IColorMap {
      private final IColorMap parent;
      private final float smoothTime;
      private final float[] lastColor = new float[3];
      private long lastTime;

      public Smoothed(IColorMap parent, float smoothTime) {
         this.parent = parent;
         this.smoothTime = smoothTime;
      }

      @Override
      public String toString() {
         return this.parent.toString();
      }

      @Override
      public boolean isHeightDependent() {
         return this.parent.isHeightDependent();
      }

      @Override
      public int getColorMultiplier() {
         return this.parent.getColorMultiplier();
      }

      @Override
      public int getColorMultiplier(IBlockAccess blockAccess, int i, int j, int k) {
         return ColorUtils.float3ToInt(this.getColorMultiplierF(blockAccess, i, j, k));
      }

      @Override
      public float[] getColorMultiplierF(IBlockAccess blockAccess, int i, int j, int k) {
         float[] currentColor = this.parent.getColorMultiplierF(blockAccess, i, j, k);
         long now = System.currentTimeMillis();
         if (this.lastTime == 0L) {
            this.lastColor[0] = currentColor[0];
            this.lastColor[1] = currentColor[1];
            this.lastColor[2] = currentColor[2];
         } else {
            float r = ColorUtils.clamp((float)(now - this.lastTime) / this.smoothTime);
            float s = 1.0F - r;
            this.lastColor[0] = r * currentColor[0] + s * this.lastColor[0];
            this.lastColor[1] = r * currentColor[1] + s * this.lastColor[1];
            this.lastColor[2] = r * currentColor[2] + s * this.lastColor[2];
         }

         this.lastTime = now;
         return this.lastColor;
      }

      @Override
      public void claimResources(Collection<FakeResourceLocation> resources) {
         this.parent.claimResources(resources);
      }

      @Override
      public IColorMap copy() {
         return new ColorMapBase.Smoothed(this.parent.copy(), this.smoothTime);
      }
   }
}
