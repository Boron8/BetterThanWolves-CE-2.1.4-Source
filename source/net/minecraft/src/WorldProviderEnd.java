package net.minecraft.src;

import com.prupe.mcpatcher.cc.ColorizeWorld;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

public class WorldProviderEnd extends WorldProvider {
   @Override
   public void registerWorldChunkManager() {
      this.worldChunkMgr = new WorldChunkManagerHell(BiomeGenBase.sky, 0.5F, 0.0F);
      this.dimensionId = 1;
      this.hasNoSky = true;
   }

   @Override
   public IChunkProvider createChunkGenerator() {
      return new ChunkProviderEnd(this.worldObj, this.worldObj.getSeed());
   }

   @Override
   public float calculateCelestialAngle(long par1, float par3) {
      return 0.0F;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public float[] calcSunriseSunsetColors(float par1, float par2) {
      return null;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public Vec3 getFogColor(float par1, float par2) {
      int var3 = 10518688;
      float var4 = MathHelper.cos(par1 * (float) Math.PI * 2.0F) * 2.0F + 0.5F;
      if (var4 < 0.0F) {
         var4 = 0.0F;
      }

      if (var4 > 1.0F) {
         var4 = 1.0F;
      }

      float var5 = (var3 >> 16 & 0xFF) / 255.0F;
      float var6 = (var3 >> 8 & 0xFF) / 255.0F;
      float var7 = (var3 & 0xFF) / 255.0F;
      float var10000 = var5 * (var4 * 0.0F + 0.15F);
      var10000 = var6 * (var4 * 0.0F + 0.15F);
      var10000 = var7 * (var4 * 0.0F + 0.15F);
      return this.worldObj.getWorldVec3Pool().getVecFromPool(ColorizeWorld.endFogColor[0], ColorizeWorld.endFogColor[1], ColorizeWorld.endFogColor[2]);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean isSkyColored() {
      return false;
   }

   @Override
   public boolean canRespawnHere() {
      return false;
   }

   @Override
   public boolean isSurfaceWorld() {
      return false;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public float getCloudHeight() {
      return 8.0F;
   }

   @Override
   public boolean canCoordinateBeSpawn(int par1, int par2) {
      int var3 = this.worldObj.getFirstUncoveredBlock(par1, par2);
      return var3 == 0 ? false : Block.blocksList[var3].blockMaterial.blocksMovement();
   }

   @Override
   public ChunkCoordinates getEntrancePortalLocation() {
      return new ChunkCoordinates(100, 50, 0);
   }

   @Override
   public int getAverageGroundLevel() {
      return 50;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean doesXZShowFog(int par1, int par2) {
      return true;
   }

   @Override
   public String getDimensionName() {
      return "The End";
   }
}
