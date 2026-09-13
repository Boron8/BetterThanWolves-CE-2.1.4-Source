package com.prupe.mcpatcher.renderpass;

import com.prupe.mcpatcher.MCLogger;
import com.prupe.mcpatcher.ctm.CTMUtils;
import com.prupe.mcpatcher.mal.block.BlockAPI;
import com.prupe.mcpatcher.mal.block.RenderPassAPI;
import com.prupe.mcpatcher.mal.resource.BlendMethod;
import com.prupe.mcpatcher.mal.resource.FakeResourceLocation;
import com.prupe.mcpatcher.mal.resource.GLAPI;
import com.prupe.mcpatcher.mal.resource.PropertiesFile;
import com.prupe.mcpatcher.mal.resource.TexturePackAPI;
import com.prupe.mcpatcher.mal.resource.TexturePackChangeHandler;
import java.util.Arrays;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.Block;
import net.minecraft.src.EntityRenderer;
import net.minecraft.src.IBlockAccess;
import org.lwjgl.opengl.GL11;

@Environment(EnvType.CLIENT)
public class RenderPass {
   private static final MCLogger logger = MCLogger.getLogger("Better Glass");
   private static final FakeResourceLocation RENDERPASS_PROPERTIES = TexturePackAPI.newMCPatcherResourceLocation("renderpass.properties");
   private static final Map<Block, Integer> baseRenderPass = new IdentityHashMap<>();
   private static final Map<Block, Integer> extraRenderPass = new IdentityHashMap<>();
   private static final Map<Block, Integer> renderPassBits = new IdentityHashMap<>();
   private static final Set<Block> customRenderPassBlocks = new HashSet<>();
   private static BlendMethod blendMethod;
   private static FakeResourceLocation blendBlankResource;
   private static boolean enableLightmap;
   private static boolean enableColormap;
   private static final boolean[] backfaceCulling = new boolean[RenderPassAPI.NUM_RENDER_PASSES];
   private static int currentRenderPass = -1;
   private static int maxRenderPass = 1;
   private static boolean canRenderInThisPass;
   private static boolean hasCustomRenderPasses;
   private static boolean ambientOcclusion;
   private static final int COLOR_POS_0 = 3;
   private static final int COLOR_POS_1 = 10;
   private static final int COLOR_POS_2 = 17;
   private static final int COLOR_POS_3 = 24;
   private static int saveColor0;
   private static int saveColor1;
   private static int saveColor2;
   private static int saveColor3;

   public static void start(int pass) {
      currentRenderPass = RenderPassMap.vanillaToMCPatcher(pass);
      CTMUtils.setBlankResource();
   }

   public static void finish() {
      currentRenderPass = -1;
      CTMUtils.setBlankResource();
   }

   public static boolean skipAllRenderPasses(boolean[] skipRenderPass) {
      return skipRenderPass[0] && skipRenderPass[1] && skipRenderPass[2] && skipRenderPass[3];
   }

   public static boolean checkRenderPasses(Block block, boolean moreRenderPasses) {
      int bits = renderPassBits.get(block) >>> currentRenderPass;
      canRenderInThisPass = (bits & 1) != 0;
      hasCustomRenderPasses = customRenderPassBlocks.contains(block);
      return moreRenderPasses || bits >>> 1 != 0;
   }

   public static boolean canRenderInThisPass(boolean canRender) {
      return hasCustomRenderPasses ? canRenderInThisPass : canRender;
   }

   public static boolean shouldSideBeRendered(Block block, IBlockAccess blockAccess, int i, int j, int k, int face) {
      if (BlockAPI.shouldSideBeRendered(block, blockAccess, i, j, k, face)) {
         return true;
      } else if (!extraRenderPass.containsKey(block)) {
         Block neighbor = BlockAPI.getBlockAt(blockAccess, i, j, k);
         return extraRenderPass.containsKey(neighbor);
      } else {
         return false;
      }
   }

   public static boolean setAmbientOcclusion(boolean ambientOcclusion) {
      RenderPass.ambientOcclusion = ambientOcclusion;
      return ambientOcclusion;
   }

   public static float getAOBaseMultiplier(float multiplier) {
      return RenderPassAPI.instance.useLightmapThisPass() ? multiplier : 1.0F;
   }

   public static boolean useBlockShading() {
      return RenderPassAPI.instance.useLightmapThisPass();
   }

   public static void unshadeBuffer(int[] b) {
      if (!useBlockShading()) {
         saveColor0 = b[3];
         saveColor1 = b[10];
         saveColor2 = b[17];
         saveColor3 = b[24];
         b[3] = b[10] = b[17] = b[24] = -1;
      }
   }

   public static void reshadeBuffer(int[] b) {
      if (!useBlockShading()) {
         b[3] = saveColor0;
         b[10] = saveColor1;
         b[17] = saveColor2;
         b[24] = saveColor3;
      }
   }

   public static boolean preRenderPass(int pass) {
      currentRenderPass = pass;
      if (pass > maxRenderPass) {
         return false;
      } else {
         switch (pass) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
               if (!backfaceCulling[pass]) {
                  GL11.glDisable(2884);
               }
               break;
            case 5:
               GLAPI.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
               GL11.glPolygonOffset(-2.0F, -2.0F);
               GL11.glEnable(32823);
               if (backfaceCulling[pass]) {
                  GL11.glEnable(2884);
               } else {
                  GL11.glDisable(2884);
               }

               if (ambientOcclusion) {
                  GL11.glShadeModel(7425);
               }

               blendMethod.applyBlending();
         }

         return true;
      }
   }

   public static int postRenderPass(int value) {
      switch (currentRenderPass) {
         case 0:
         case 1:
         case 2:
         case 3:
         case 4:
            if (!backfaceCulling[currentRenderPass]) {
               GL11.glEnable(2884);
            }
            break;
         case 5:
            GL11.glPolygonOffset(0.0F, 0.0F);
            GL11.glDisable(32823);
            if (!backfaceCulling[currentRenderPass]) {
               GL11.glEnable(2884);
            }

            GL11.glDisable(3042);
            GL11.glShadeModel(7424);
      }

      currentRenderPass = -1;
      return value;
   }

   public static void enableDisableLightmap(EntityRenderer renderer, double partialTick) {
      if (RenderPassAPI.instance.useLightmapThisPass()) {
         renderer.enableLightmap(partialTick);
      } else {
         renderer.disableLightmap(partialTick);
      }
   }

   static {
      RenderPassAPI.instance = new RenderPassAPI() {
         @Override
         public boolean skipDefaultRendering(Block block) {
            return RenderPass.currentRenderPass > 4;
         }

         @Override
         public boolean skipThisRenderPass(Block block, int pass) {
            if (RenderPass.currentRenderPass < 0) {
               return pass > 4;
            } else {
               if (pass < 0) {
                  pass = RenderPassMap.getDefaultRenderPass(block);
               }

               return pass != RenderPass.currentRenderPass;
            }
         }

         @Override
         public boolean useColorMultiplierThisPass(Block block) {
            return RenderPass.currentRenderPass != 5 || RenderPass.enableColormap;
         }

         @Override
         public boolean useLightmapThisPass() {
            return RenderPass.currentRenderPass != 5 || RenderPass.enableLightmap;
         }

         @Override
         public void clear() {
            RenderPass.canRenderInThisPass = false;
            RenderPass.maxRenderPass = 3;
            RenderPass.baseRenderPass.clear();
            RenderPass.extraRenderPass.clear();
            RenderPass.renderPassBits.clear();
            RenderPass.customRenderPassBlocks.clear();
            RenderPass.blendMethod = BlendMethod.ALPHA;
            RenderPass.blendBlankResource = RenderPass.blendMethod.getBlankResource();
            if (RenderPass.blendBlankResource == null) {
               RenderPass.blendBlankResource = BlendMethod.ALPHA.getBlankResource();
            }

            RenderPass.enableLightmap = true;
            RenderPass.enableColormap = false;
            Arrays.fill(RenderPass.backfaceCulling, true);
            RenderPass.backfaceCulling[4] = false;

            for (Block block : BlockAPI.getAllBlocks()) {
               RenderPass.baseRenderPass.put(block, RenderPassMap.getDefaultRenderPass(block));
            }
         }

         @Override
         public void refreshBlendingOptions() {
            PropertiesFile properties = PropertiesFile.get(RenderPass.logger, RenderPass.RENDERPASS_PROPERTIES);
            if (properties != null) {
               this.remapProperties(properties);
               String method = properties.getString("blend.overlay", "alpha").trim().toLowerCase();
               RenderPass.blendMethod = BlendMethod.parse(method);
               if (RenderPass.blendMethod == null) {
                  RenderPass.logger.error("%s: unknown blend method '%s'", RenderPass.RENDERPASS_PROPERTIES, method);
                  RenderPass.blendMethod = BlendMethod.ALPHA;
               }

               RenderPass.blendBlankResource = RenderPass.blendMethod.getBlankResource();
               if (RenderPass.blendBlankResource == null) {
                  RenderPass.blendBlankResource = BlendMethod.ALPHA.getBlankResource();
               }

               RenderPass.enableLightmap = properties.getBoolean("enableLightmap.overlay", !RenderPass.blendMethod.isColorBased());
               RenderPass.enableColormap = properties.getBoolean("enableColormap.overlay", false);
               RenderPass.backfaceCulling[5] = properties.getBoolean("backfaceCulling.overlay", true);
               RenderPass.backfaceCulling[2] = RenderPass.backfaceCulling[RenderPassMap.getCutoutRenderPass()] = properties.getBoolean(
                  "backfaceCulling.cutout", true
               );
               RenderPass.backfaceCulling[1] = properties.getBoolean("backfaceCulling.cutout_mipped", RenderPass.backfaceCulling[2]);
               RenderPass.backfaceCulling[3] = properties.getBoolean("backfaceCulling.translucent", true);
            }
         }

         private void remapProperties(PropertiesFile properties) {
            for (Entry<String, String> entry : properties.entrySet()) {
               String key = entry.getKey();
               key = key.replaceFirst("\\.3$", ".overlay");
               key = key.replaceFirst("\\.2$", ".backface");
               if (!key.equals(entry.getKey())) {
                  properties.warning("%s is deprecated in 1.8.  Use %s instead", entry.getKey(), key);
               }

               properties.setProperty(key, entry.getValue());
            }
         }

         @Override
         public void setRenderPassForBlock(Block block, int pass) {
            if (block != null && pass >= 0) {
               String name;
               if (pass <= 4) {
                  RenderPass.baseRenderPass.put(block, pass);
                  name = "base";
               } else {
                  RenderPass.extraRenderPass.put(block, pass);
                  name = "extra";
               }

               RenderPass.logger.fine("%s %s render pass -> %s", BlockAPI.getBlockName(block), name, RenderPassAPI.instance.getRenderPassName(pass));
               RenderPass.customRenderPassBlocks.add(block);
               RenderPass.maxRenderPass = Math.max(RenderPass.maxRenderPass, pass);
            }
         }

         @Override
         public FakeResourceLocation getBlankResource(int pass) {
            return pass == 5 ? RenderPass.blendBlankResource : super.getBlankResource(pass);
         }

         @Override
         public FakeResourceLocation getBlankResource() {
            return this.getBlankResource(RenderPass.currentRenderPass);
         }
      };
      TexturePackChangeHandler.register(new TexturePackChangeHandler("Better Glass", 4) {
         @Override
         public void beforeChange() {
         }

         @Override
         public void afterChange() {
            for (Block block : BlockAPI.getAllBlocks()) {
               int bits = 0;
               Integer i = RenderPass.baseRenderPass.get(block);
               if (i != null && i >= 0) {
                  bits |= 1 << i;
               }

               i = RenderPass.extraRenderPass.get(block);
               if (i != null && i >= 0) {
                  bits |= 1 << i;
               }

               RenderPass.renderPassBits.put(block, bits);
            }
         }
      });
   }
}
