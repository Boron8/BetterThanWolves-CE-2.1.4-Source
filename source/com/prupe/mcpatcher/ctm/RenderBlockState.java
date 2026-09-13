package com.prupe.mcpatcher.ctm;

import com.prupe.mcpatcher.mal.block.BlockStateMatcher;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.Block;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Icon;

@Environment(EnvType.CLIENT)
public abstract class RenderBlockState {
   public static final int BOTTOM_FACE = 0;
   public static final int TOP_FACE = 1;
   public static final int NORTH_FACE = 2;
   public static final int SOUTH_FACE = 3;
   public static final int WEST_FACE = 4;
   public static final int EAST_FACE = 5;
   public static final int[] GO_DOWN = new int[]{0, -1, 0};
   public static final int[] GO_UP = new int[]{0, 1, 0};
   public static final int[] GO_NORTH = new int[]{0, 0, -1};
   public static final int[] GO_SOUTH = new int[]{0, 0, 1};
   public static final int[] GO_WEST = new int[]{-1, 0, 0};
   public static final int[] GO_EAST = new int[]{1, 0, 0};
   public static final int[][] NORMALS = new int[][]{GO_DOWN, GO_UP, GO_NORTH, GO_SOUTH, GO_WEST, GO_EAST};
   public static final int REL_L = 0;
   public static final int REL_DL = 1;
   public static final int REL_D = 2;
   public static final int REL_DR = 3;
   public static final int REL_R = 4;
   public static final int REL_UR = 5;
   public static final int REL_U = 6;
   public static final int REL_UL = 7;
   public static final int CONNECT_BY_BLOCK = 0;
   public static final int CONNECT_BY_TILE = 1;
   public static final int CONNECT_BY_MATERIAL = 2;
   protected IBlockAccess blockAccess;
   protected Block block;
   protected boolean useAO;
   protected boolean inWorld;
   protected BlockStateMatcher matcher;
   protected boolean offsetsComputed;
   protected boolean haveOffsets;
   protected int di;
   protected int dj;
   protected int dk;

   protected static int[] add(int[] a, int[] b) {
      if (a.length != b.length) {
         throw new RuntimeException("arrays to add are not same length");
      } else {
         int[] c = new int[a.length];

         for (int i = 0; i < c.length; i++) {
            c[i] = a[i] + b[i];
         }

         return c;
      }
   }

   protected static int[][] makeNeighborOffset(int left, int down, int right, int up) {
      int[] l = NORMALS[left];
      int[] d = NORMALS[down];
      int[] r = NORMALS[right];
      int[] u = NORMALS[up];
      return new int[][]{l, add(l, d), d, add(d, r), r, add(r, u), u, add(u, l)};
   }

   public final IBlockAccess getBlockAccess() {
      return this.blockAccess;
   }

   public final Block getBlock() {
      return this.block;
   }

   public final boolean useAO() {
      return this.useAO;
   }

   public final boolean isInWorld() {
      return this.inWorld;
   }

   public final void setFilter(BlockStateMatcher matcher) {
      this.matcher = matcher;
   }

   public final BlockStateMatcher getFilter() {
      return this.matcher;
   }

   public void clear() {
      this.blockAccess = null;
      this.block = null;
      this.useAO = false;
      this.inWorld = false;
      this.offsetsComputed = false;
      this.haveOffsets = false;
      this.di = this.dj = this.dk = 0;
      this.setFilter(null);
   }

   public abstract int getI();

   public abstract int getJ();

   public abstract int getK();

   public abstract int getBlockFace();

   public abstract int getTextureFace();

   public abstract int getTextureFaceOrig();

   public abstract String getTextureFaceName();

   public abstract int getFaceForHV();

   public abstract boolean match(BlockStateMatcher var1);

   public abstract int[] getOffset(int var1, int var2);

   public abstract boolean setCoordOffsetsForRenderType();

   public final int getDI() {
      return this.di;
   }

   public final int getDJ() {
      return this.dj;
   }

   public final int getDK() {
      return this.dk;
   }

   public abstract boolean shouldConnectByBlock(Block var1, int var2, int var3, int var4);

   public abstract boolean shouldConnectByTile(Block var1, Icon var2, int var3, int var4, int var5);
}
