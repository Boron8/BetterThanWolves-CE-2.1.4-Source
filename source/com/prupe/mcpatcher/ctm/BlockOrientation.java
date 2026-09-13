package com.prupe.mcpatcher.ctm;

import com.prupe.mcpatcher.mal.block.BlockAPI;
import com.prupe.mcpatcher.mal.block.BlockStateMatcher;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.Block;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Icon;

@Environment(EnvType.CLIENT)
final class BlockOrientation extends RenderBlockState {
   private static final int[][][] NEIGHBOR_OFFSET = new int[][][]{
      makeNeighborOffset(4, 3, 5, 2),
      makeNeighborOffset(4, 3, 5, 2),
      makeNeighborOffset(5, 0, 4, 1),
      makeNeighborOffset(4, 0, 5, 1),
      makeNeighborOffset(2, 0, 3, 1),
      makeNeighborOffset(3, 0, 2, 1)
   };
   private static final int[][] ROTATE_UV_MAP = new int[][]{
      {4, 5, 2, 3, 1, 0, 2, -2, 2, -2, 0, 0},
      {2, 3, 1, 0, 4, 5, 0, 0, 0, 0, -2, 2},
      {4, 5, 2, 3, 1, 0, 2, -2, -2, -2, 0, 0},
      {2, 3, 1, 0, 4, 5, 0, 0, 0, 0, -2, -2}
   };
   private int i;
   private int j;
   private int k;
   private int metadata;
   private int altMetadata;
   private int metadataBits;
   private int renderType;
   private int blockFace;
   private int textureFace;
   private int textureFaceOrig;
   private int rotateUV;

   @Override
   public void clear() {
      super.clear();
      this.i = this.j = this.k = 0;
      this.renderType = -1;
      this.metadata = 0;
      this.blockFace = this.textureFace = 0;
      this.rotateUV = 0;
      this.offsetsComputed = false;
      this.haveOffsets = false;
      this.di = this.dj = this.dk = 0;
   }

   @Override
   public int getI() {
      return this.i;
   }

   @Override
   public int getJ() {
      return this.j;
   }

   @Override
   public int getK() {
      return this.k;
   }

   @Override
   public int getBlockFace() {
      return this.blockFace;
   }

   @Override
   public int getTextureFace() {
      return this.textureFace;
   }

   @Override
   public int getTextureFaceOrig() {
      return this.textureFaceOrig;
   }

   @Override
   public String getTextureFaceName() {
      throw new UnsupportedOperationException("getTextureName");
   }

   @Override
   public int getFaceForHV() {
      return this.blockFace;
   }

   @Override
   public boolean match(BlockStateMatcher matcher) {
      return this.isInWorld() ? matcher.match(this.blockAccess, this.i, this.j, this.k) : matcher.match(this.block, this.metadata);
   }

   @Override
   public int[] getOffset(int blockFace, int relativeDirection) {
      return NEIGHBOR_OFFSET[blockFace][this.rotateUV(relativeDirection)];
   }

   @Override
   public boolean setCoordOffsetsForRenderType() {
      if (this.offsetsComputed) {
         return this.haveOffsets;
      } else {
         this.offsetsComputed = true;
         this.haveOffsets = false;
         this.di = this.dj = this.dk = 0;
         switch (this.renderType) {
            case 1:
               while (this.j + this.dj > 0 && this.block == BlockAPI.getBlockAt(this.blockAccess, this.i, this.j + this.dj - 1, this.k)) {
                  this.dj--;
                  this.haveOffsets = true;
               }
               break;
            case 7:
            case 40:
               if ((this.metadata & 8) != 0 && this.block == BlockAPI.getBlockAt(this.blockAccess, this.i, this.j - 1, this.k)) {
                  this.dj--;
                  this.haveOffsets = true;
               }
               break;
            case 14:
               this.metadata = BlockAPI.getMetadataAt(this.blockAccess, this.i, this.j, this.k);
               switch (this.metadata) {
                  case 0:
                  case 4:
                     this.dk = 1;
                     break;
                  case 1:
                  case 5:
                     this.di = -1;
                     break;
                  case 2:
                  case 6:
                     this.dk = -1;
                     break;
                  case 3:
                  case 7:
                     this.di = 1;
                     break;
                  default:
                     return false;
               }

               this.haveOffsets = this.block == BlockAPI.getBlockAt(this.blockAccess, this.i + this.di, this.j, this.k + this.dk);
         }

         return this.haveOffsets;
      }
   }

   @Override
   public boolean shouldConnectByBlock(Block neighbor, int neighborI, int neighborJ, int neighborK) {
      return this.block == neighbor && (this.metadataBits & 1 << BlockAPI.getMetadataAt(this.blockAccess, neighborI, neighborJ, neighborK)) != 0;
   }

   @Override
   public boolean shouldConnectByTile(Block neighbor, Icon origIcon, int neighborI, int neighborJ, int neighborK) {
      return origIcon == BlockAPI.getBlockIcon(neighbor, this.blockAccess, neighborI, neighborJ, neighborK, this.getTextureFaceOrig());
   }

   void setBlock(Block block, IBlockAccess blockAccess, int i, int j, int k) {
      this.block = block;
      this.blockAccess = blockAccess;
      this.inWorld = true;
      this.i = i;
      this.j = j;
      this.k = k;
      this.renderType = block.getRenderType();
      this.metadata = this.altMetadata = BlockAPI.getMetadataAt(blockAccess, i, j, k);
      this.offsetsComputed = false;
   }

   void setFace(int face) {
      this.blockFace = this.getBlockFaceByRenderType(face);
      this.textureFaceOrig = face;
      this.rotateUV = 0;
      this.textureFace = this.blockFaceToTextureFace(this.blockFace);
      this.metadataBits = 1 << this.metadata | 1 << this.altMetadata;
   }

   void setBlockMetadata(Block block, int metadata, int face) {
      this.block = block;
      this.blockAccess = null;
      this.inWorld = false;
      this.i = this.j = this.k = 0;
      this.renderType = block.getRenderType();
      this.blockFace = this.textureFace = this.textureFaceOrig = face;
      this.metadata = metadata;
      this.metadataBits = 1 << metadata;
      this.di = this.dj = this.dk = 0;
      this.rotateUV = 0;
   }

   private int getBlockFaceByRenderType(int face) {
      switch (this.renderType) {
         case 1:
            return 2;
         case 8:
            switch (this.metadata) {
               case 2:
               case 3:
               case 4:
               case 5:
                  return this.metadata;
               default:
                  return face;
            }
         case 20:
            switch (this.metadata) {
               case 1:
                  return 2;
               case 2:
                  return 5;
               case 3:
               case 5:
               case 6:
               case 7:
               default:
                  break;
               case 4:
                  return 3;
               case 8:
                  return 4;
            }
      }

      return face;
   }

   private int blockFaceToTextureFace(int face) {
      switch (this.renderType) {
         case 31:
            switch (this.metadata & 12) {
               case 4:
                  this.altMetadata &= -13;
                  this.rotateUV = ROTATE_UV_MAP[0][face + 6];
                  return ROTATE_UV_MAP[0][face];
               case 8:
                  this.altMetadata &= -13;
                  this.rotateUV = ROTATE_UV_MAP[1][face + 6];
                  return ROTATE_UV_MAP[1][face];
               default:
                  return face;
            }
         case 39:
            switch (this.metadata) {
               case 3:
                  this.altMetadata = 2;
                  this.rotateUV = ROTATE_UV_MAP[2][face + 6];
                  return ROTATE_UV_MAP[2][face];
               case 4:
                  this.altMetadata = 2;
                  this.rotateUV = ROTATE_UV_MAP[3][face + 6];
                  return ROTATE_UV_MAP[3][face];
            }
      }

      return face;
   }

   private int rotateUV(int neighbor) {
      return neighbor + this.rotateUV & 7;
   }

   boolean logIt() {
      return false;
   }
}
