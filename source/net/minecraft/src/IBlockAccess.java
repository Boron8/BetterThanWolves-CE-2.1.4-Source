package net.minecraft.src;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

public interface IBlockAccess {
   int getBlockId(int var1, int var2, int var3);

   TileEntity getBlockTileEntity(int var1, int var2, int var3);

   @Environment(EnvType.CLIENT)
   int getLightBrightnessForSkyBlocks(int var1, int var2, int var3, int var4);

   @Environment(EnvType.CLIENT)
   float getBrightness(int var1, int var2, int var3, int var4);

   @Environment(EnvType.CLIENT)
   float getLightBrightness(int var1, int var2, int var3);

   int getBlockMetadata(int var1, int var2, int var3);

   Material getBlockMaterial(int var1, int var2, int var3);

   @Environment(EnvType.CLIENT)
   boolean isBlockOpaqueCube(int var1, int var2, int var3);

   boolean isBlockNormalCube(int var1, int var2, int var3);

   @Environment(EnvType.CLIENT)
   boolean isAirBlock(int var1, int var2, int var3);

   BiomeGenBase getBiomeGenForCoords(int var1, int var2);

   @Environment(EnvType.CLIENT)
   int getHeight();

   @Environment(EnvType.CLIENT)
   boolean extendedLevelsInChunkCache();

   @Environment(EnvType.CLIENT)
   boolean doesBlockHaveSolidTopSurface(int var1, int var2, int var3);

   Vec3Pool getWorldVec3Pool();

   int isBlockProvidingPowerTo(int var1, int var2, int var3, int var4);

   default void editClass() {
   }
}
