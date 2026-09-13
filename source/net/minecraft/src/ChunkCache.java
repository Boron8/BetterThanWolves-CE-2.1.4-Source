package net.minecraft.src;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

public class ChunkCache implements IBlockAccess {
   private int chunkX;
   private int chunkZ;
   private Chunk[][] chunkArray;
   private boolean hasExtendedLevels;
   public World worldObj;

   public ChunkCache(World par1World, int par2, int par3, int par4, int par5, int par6, int par7, int par8) {
      this.worldObj = par1World;
      this.chunkX = par2 - par8 >> 4;
      this.chunkZ = par4 - par8 >> 4;
      int var9 = par5 + par8 >> 4;
      int var10 = par7 + par8 >> 4;
      this.chunkArray = new Chunk[var9 - this.chunkX + 1][var10 - this.chunkZ + 1];
      this.hasExtendedLevels = true;

      for (int var11 = this.chunkX; var11 <= var9; var11++) {
         for (int var12 = this.chunkZ; var12 <= var10; var12++) {
            Chunk var13 = par1World.getChunkFromChunkCoords(var11, var12);
            if (var13 != null) {
               this.chunkArray[var11 - this.chunkX][var12 - this.chunkZ] = var13;
            }
         }
      }

      for (int var14 = par2 >> 4; var14 <= par5 >> 4; var14++) {
         for (int var12x = par4 >> 4; var12x <= par7 >> 4; var12x++) {
            Chunk var13 = this.chunkArray[var14 - this.chunkX][var12x - this.chunkZ];
            if (var13 != null && !var13.getAreLevelsEmpty(par3, par6)) {
               this.hasExtendedLevels = false;
            }
         }
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean extendedLevelsInChunkCache() {
      return this.hasExtendedLevels;
   }

   @Override
   public int getBlockId(int par1, int par2, int par3) {
      if (par2 < 0) {
         return 0;
      } else if (par2 >= 256) {
         return 0;
      } else {
         int var4 = (par1 >> 4) - this.chunkX;
         int var5 = (par3 >> 4) - this.chunkZ;
         if (var4 >= 0 && var4 < this.chunkArray.length && var5 >= 0 && var5 < this.chunkArray[var4].length) {
            Chunk var6 = this.chunkArray[var4][var5];
            return var6 == null ? 0 : var6.getBlockID(par1 & 15, par2, par3 & 15);
         } else {
            return 0;
         }
      }
   }

   @Override
   public TileEntity getBlockTileEntity(int par1, int par2, int par3) {
      int var4 = (par1 >> 4) - this.chunkX;
      int var5 = (par3 >> 4) - this.chunkZ;
      return this.chunkArray[var4][var5].getChunkBlockTileEntity(par1 & 15, par2, par3 & 15);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public float getBrightness(int par1, int par2, int par3, int par4) {
      int var5 = this.getLightValue(par1, par2, par3);
      if (var5 < par4) {
         var5 = par4;
      }

      return this.worldObj.provider.lightBrightnessTable[var5];
   }

   @Environment(EnvType.CLIENT)
   @Override
   public int getLightBrightnessForSkyBlocks(int par1, int par2, int par3, int par4) {
      int var5 = this.getSkyBlockTypeBrightness(EnumSkyBlock.Sky, par1, par2, par3);
      int var6 = this.getSkyBlockTypeBrightness(EnumSkyBlock.Block, par1, par2, par3);
      if (var6 < par4) {
         var6 = par4;
      }

      return var5 << 20 | var6 << 4;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public float getLightBrightness(int par1, int par2, int par3) {
      return this.worldObj.provider.lightBrightnessTable[this.getLightValue(par1, par2, par3)];
   }

   @Environment(EnvType.CLIENT)
   public int getLightValue(int par1, int par2, int par3) {
      return this.getLightValueExt(par1, par2, par3, true);
   }

   @Environment(EnvType.CLIENT)
   public int getLightValueExt(int par1, int par2, int par3, boolean par4) {
      if (par1 >= -30000000 && par3 >= -30000000 && par1 < 30000000 && par3 <= 30000000) {
         if (par4) {
            int var5 = this.getBlockId(par1, par2, par3);
            if (Block.useNeighborBrightness[var5]) {
               int var6 = this.getLightValueExt(par1, par2 + 1, par3, false);
               int var7 = this.getLightValueExt(par1 + 1, par2, par3, false);
               int var8 = this.getLightValueExt(par1 - 1, par2, par3, false);
               int var9 = this.getLightValueExt(par1, par2, par3 + 1, false);
               int var10 = this.getLightValueExt(par1, par2, par3 - 1, false);
               if (var7 > var6) {
                  var6 = var7;
               }

               if (var8 > var6) {
                  var6 = var8;
               }

               if (var9 > var6) {
                  var6 = var9;
               }

               if (var10 > var6) {
                  var6 = var10;
               }

               return var6;
            }
         }

         if (par2 < 0) {
            return 0;
         } else if (par2 >= 256) {
            int var5 = 15 - this.worldObj.skylightSubtracted;
            if (var5 < 0) {
               var5 = 0;
            }

            return var5;
         } else {
            int var5 = (par1 >> 4) - this.chunkX;
            int var6x = (par3 >> 4) - this.chunkZ;
            return this.chunkArray[var5][var6x].getBlockLightValue(par1 & 15, par2, par3 & 15, this.worldObj.skylightSubtracted);
         }
      } else {
         return 15;
      }
   }

   @Override
   public int getBlockMetadata(int par1, int par2, int par3) {
      if (par2 < 0) {
         return 0;
      } else if (par2 >= 256) {
         return 0;
      } else {
         int var4 = (par1 >> 4) - this.chunkX;
         int var5 = (par3 >> 4) - this.chunkZ;
         return this.chunkArray[var4][var5].getBlockMetadata(par1 & 15, par2, par3 & 15);
      }
   }

   @Override
   public Material getBlockMaterial(int par1, int par2, int par3) {
      int var4 = this.getBlockId(par1, par2, par3);
      return var4 == 0 ? Material.air : Block.blocksList[var4].blockMaterial;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public BiomeGenBase getBiomeGenForCoords(int par1, int par2) {
      return this.worldObj.getBiomeGenForCoords(par1, par2);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean isBlockOpaqueCube(int par1, int par2, int par3) {
      Block var4 = Block.blocksList[this.getBlockId(par1, par2, par3)];
      return var4 == null ? false : var4.isOpaqueCube();
   }

   @Override
   public boolean isBlockNormalCube(int par1, int par2, int par3) {
      Block var4 = Block.blocksList[this.getBlockId(par1, par2, par3)];
      return var4 == null ? false : var4.blockMaterial.blocksMovement() && var4.renderAsNormalBlock();
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean doesBlockHaveSolidTopSurface(int i, int j, int k) {
      Block block = Block.blocksList[this.getBlockId(i, j, k)];
      return block != null && block.hasLargeCenterHardPointToFacing(this, i, j, k, 1);
   }

   @Override
   public Vec3Pool getWorldVec3Pool() {
      return this.worldObj.getWorldVec3Pool();
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean isAirBlock(int par1, int par2, int par3) {
      Block var4 = Block.blocksList[this.getBlockId(par1, par2, par3)];
      return var4 == null;
   }

   @Environment(EnvType.CLIENT)
   public int getSkyBlockTypeBrightness(EnumSkyBlock par1EnumSkyBlock, int par2, int par3, int par4) {
      if (par3 < 0) {
         par3 = 0;
      }

      if (par3 >= 256) {
         par3 = 255;
      }

      if (par3 < 0 || par3 >= 256 || par2 < -30000000 || par4 < -30000000 || par2 >= 30000000 || par4 > 30000000) {
         return par1EnumSkyBlock.defaultLightValue;
      } else if (par1EnumSkyBlock == EnumSkyBlock.Sky && this.worldObj.provider.hasNoSky) {
         return 0;
      } else if (Block.useNeighborBrightness[this.getBlockId(par2, par3, par4)]) {
         int var5 = this.getSpecialBlockBrightness(par1EnumSkyBlock, par2, par3 + 1, par4);
         int var6 = this.getSpecialBlockBrightness(par1EnumSkyBlock, par2 + 1, par3, par4);
         int var7 = this.getSpecialBlockBrightness(par1EnumSkyBlock, par2 - 1, par3, par4);
         int var8 = this.getSpecialBlockBrightness(par1EnumSkyBlock, par2, par3, par4 + 1);
         int var9 = this.getSpecialBlockBrightness(par1EnumSkyBlock, par2, par3, par4 - 1);
         if (var6 > var5) {
            var5 = var6;
         }

         if (var7 > var5) {
            var5 = var7;
         }

         if (var8 > var5) {
            var5 = var8;
         }

         if (var9 > var5) {
            var5 = var9;
         }

         return var5;
      } else {
         int var5x = (par2 >> 4) - this.chunkX;
         int var6x = (par4 >> 4) - this.chunkZ;
         return this.chunkArray[var5x][var6x].getSavedLightValue(par1EnumSkyBlock, par2 & 15, par3, par4 & 15);
      }
   }

   @Environment(EnvType.CLIENT)
   public int getSpecialBlockBrightness(EnumSkyBlock par1EnumSkyBlock, int par2, int par3, int par4) {
      if (par3 < 0) {
         par3 = 0;
      }

      if (par3 >= 256) {
         par3 = 255;
      }

      if (par3 >= 0 && par3 < 256 && par2 >= -30000000 && par4 >= -30000000 && par2 < 30000000 && par4 <= 30000000) {
         int var5 = (par2 >> 4) - this.chunkX;
         int var6 = (par4 >> 4) - this.chunkZ;
         return this.chunkArray[var5][var6].getSavedLightValue(par1EnumSkyBlock, par2 & 15, par3, par4 & 15);
      } else {
         return par1EnumSkyBlock.defaultLightValue;
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public int getHeight() {
      return 256;
   }

   @Override
   public int isBlockProvidingPowerTo(int par1, int par2, int par3, int par4) {
      int var5 = this.getBlockId(par1, par2, par3);
      return var5 == 0 ? 0 : Block.blocksList[var5].isProvidingStrongPower(this, par1, par2, par3, par4);
   }
}
