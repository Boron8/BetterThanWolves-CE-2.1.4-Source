package com.prupe.mcpatcher.sky;

import com.prupe.mcpatcher.Config;
import com.prupe.mcpatcher.MCLogger;
import com.prupe.mcpatcher.mal.resource.BlendMethod;
import com.prupe.mcpatcher.mal.resource.FakeResourceLocation;
import com.prupe.mcpatcher.mal.resource.GLAPI;
import com.prupe.mcpatcher.mal.resource.PropertiesFile;
import com.prupe.mcpatcher.mal.resource.TexturePackAPI;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.EntityFX;
import net.minecraft.src.EntityFireworkOverlayFX;
import net.minecraft.src.EntityFireworkSparkFX;

@Environment(EnvType.CLIENT)
public class FireworksHelper {
   private static final int LIT_LAYER = 3;
   private static final int EXTRA_LAYER = 4;
   private static final FakeResourceLocation PARTICLE_PROPERTIES = TexturePackAPI.newMCPatcherResourceLocation("particle.properties");
   private static final MCLogger logger = MCLogger.getLogger("Better Skies");
   private static final boolean enable = Config.getBoolean("Better Skies", "brightenFireworks", true);
   private static BlendMethod blendMethod;

   public static int getFXLayer(EntityFX entity) {
      return !enable || !(entity instanceof EntityFireworkSparkFX) && !(entity instanceof EntityFireworkOverlayFX) ? entity.getFXLayer() : 4;
   }

   public static boolean skipThisLayer(boolean skip, int layer) {
      return skip || layer == 3 || !enable && layer > 3;
   }

   public static void setParticleBlendMethod(int layer, int pass, boolean setDefault) {
      if (enable && layer == 4 && blendMethod != null) {
         blendMethod.applyBlending();
      } else if (setDefault) {
         GLAPI.glBlendFunc(770, 771);
      }
   }

   static void reload() {
      PropertiesFile properties = PropertiesFile.getNonNull(logger, PARTICLE_PROPERTIES);
      String blend = properties.getString("blend.4", "add");
      blendMethod = BlendMethod.parse(blend);
      if (blendMethod == null) {
         properties.error("%s: unknown blend method %s", PARTICLE_PROPERTIES, blend);
      } else if (enable) {
         properties.config("using %s blending for fireworks particles", blendMethod);
      } else {
         properties.config("using default blending for fireworks particles");
      }
   }
}
