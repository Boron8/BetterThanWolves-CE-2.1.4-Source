package com.prupe.mcpatcher.mal.block;

import com.prupe.mcpatcher.mal.resource.BlendMethod;
import com.prupe.mcpatcher.mal.resource.FakeResourceLocation;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.Block;

@Environment(EnvType.CLIENT)
public class RenderPassAPI {
   public static RenderPassAPI instance = new RenderPassAPI();
   private static final String[] NAMES = new String[]{"solid", "cutout_mipped", "cutout", "translucent", "backface", "overlay"};
   public static final int SOLID_RENDER_PASS = 0;
   public static final int CUTOUT_MIPPED_RENDER_PASS = 1;
   public static final int CUTOUT_RENDER_PASS = 2;
   public static final int TRANSLUCENT_RENDER_PASS = 3;
   public static final int BACKFACE_RENDER_PASS = 4;
   public static final int OVERLAY_RENDER_PASS = 5;
   public static final int MAX_BASE_RENDER_PASS = 4;
   public static final int MAX_EXTRA_RENDER_PASS = NAMES.length - 1;
   public static final int NUM_RENDER_PASSES = NAMES.length;

   public int parseRenderPass(String value) {
      int pass = value.matches("\\d+") ? Integer.parseInt(value) : -1;
      if (value.equalsIgnoreCase("solid") || pass == 0) {
         return 0;
      } else if (value.equalsIgnoreCase("cutout_mipped")) {
         return 1;
      } else if (value.equalsIgnoreCase("cutout")) {
         return 2;
      } else if (value.equalsIgnoreCase("translucent") || pass == 1) {
         return 3;
      } else if (value.equalsIgnoreCase("backface") || pass == 2) {
         return 4;
      } else {
         return !value.equalsIgnoreCase("overlay") && pass != 3 ? pass : 5;
      }
   }

   public String getRenderPassName(int pass) {
      if (pass < 0) {
         return "(default)";
      } else {
         return pass < NAMES.length ? NAMES[pass] : "(unknown pass " + pass + ")";
      }
   }

   public boolean skipDefaultRendering(Block block) {
      return false;
   }

   public boolean skipThisRenderPass(Block block, int pass) {
      return pass > 4;
   }

   public boolean useColorMultiplierThisPass(Block block) {
      return true;
   }

   public boolean useLightmapThisPass() {
      return true;
   }

   public void clear() {
   }

   public void refreshBlendingOptions() {
   }

   public void setRenderPassForBlock(Block block, int pass) {
   }

   public FakeResourceLocation getBlankResource(int pass) {
      return BlendMethod.ALPHA.getBlankResource();
   }

   public FakeResourceLocation getBlankResource() {
      return BlendMethod.ALPHA.getBlankResource();
   }
}
