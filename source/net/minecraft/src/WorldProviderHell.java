package net.minecraft.src;

import com.prupe.mcpatcher.cc.ColorizeWorld;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

public class WorldProviderHell extends WorldProvider {
   @Override
   public void registerWorldChunkManager() {
      this.worldChunkMgr = new WorldChunkManagerHell(BiomeGenBase.hell, 1.0F, 0.0F);
      this.isHellWorld = true;
      this.hasNoSky = true;
      this.dimensionId = -1;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public Vec3 getFogColor(float par1, float par2) {
      return this.worldObj.getWorldVec3Pool().getVecFromPool(ColorizeWorld.netherFogColor[0], ColorizeWorld.netherFogColor[1], ColorizeWorld.netherFogColor[2]);
   }

   @Override
   protected void generateLightBrightnessTable() {
      float var1 = 0.1F;

      for (int var2 = 0; var2 <= 15; var2++) {
         float var3 = 1.0F - var2 / 15.0F;
         this.lightBrightnessTable[var2] = (1.0F - var3) / (var3 * 3.0F + 1.0F) * (1.0F - var1) + var1;
      }
   }

   @Override
   public IChunkProvider createChunkGenerator() {
      return new ChunkProviderHell(this.worldObj, this.worldObj.getSeed());
   }

   @Override
   public boolean isSurfaceWorld() {
      return false;
   }

   @Override
   public boolean canCoordinateBeSpawn(int par1, int par2) {
      return false;
   }

   @Override
   public float calculateCelestialAngle(long par1, float par3) {
      return 0.5F;
   }

   @Override
   public boolean canRespawnHere() {
      return false;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean doesXZShowFog(int par1, int par2) {
      return true;
   }

   @Override
   public String getDimensionName() {
      return "Nether";
   }
}
