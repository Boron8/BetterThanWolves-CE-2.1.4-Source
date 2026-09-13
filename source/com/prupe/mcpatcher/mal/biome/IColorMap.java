package com.prupe.mcpatcher.mal.biome;

import com.prupe.mcpatcher.mal.resource.FakeResourceLocation;
import java.util.Collection;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.IBlockAccess;

@Environment(EnvType.CLIENT)
public interface IColorMap {
   boolean isHeightDependent();

   int getColorMultiplier();

   int getColorMultiplier(IBlockAccess var1, int var2, int var3, int var4);

   float[] getColorMultiplierF(IBlockAccess var1, int var2, int var3, int var4);

   void claimResources(Collection<FakeResourceLocation> var1);

   IColorMap copy();
}
