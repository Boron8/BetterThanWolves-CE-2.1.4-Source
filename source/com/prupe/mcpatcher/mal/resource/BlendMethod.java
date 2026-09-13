package com.prupe.mcpatcher.mal.resource;

import java.util.HashSet;
import java.util.Set;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.lwjgl.opengl.GL11;

@Environment(EnvType.CLIENT)
public class BlendMethod {
   private static final Set<FakeResourceLocation> blankResources = new HashSet<>();
   public static final BlendMethod ALPHA = new BlendMethod("alpha", 770, 771, true, false, true, 0);
   public static final BlendMethod ADD = new BlendMethod("add", 770, 1, true, false, true, 0);
   public static final BlendMethod SUBTRACT = new BlendMethod("subtract", 775, 0, true, true, false, 0);
   public static final BlendMethod MULTIPLY = new BlendMethod("multiply", 774, 771, true, true, true, -1);
   public static final BlendMethod DODGE = new BlendMethod("dodge", 1, 1, true, true, false, 0);
   public static final BlendMethod BURN = new BlendMethod("burn", 0, 769, true, true, false, null);
   public static final BlendMethod SCREEN = new BlendMethod("screen", 1, 769, true, true, false, -1);
   public static final BlendMethod OVERLAY = new BlendMethod("overlay", 774, 768, true, true, false, -2139062144);
   public static final BlendMethod REPLACE = new BlendMethod("replace", 0, 0, false, false, true, null);
   private final int srcBlend;
   private final int dstBlend;
   private final String name;
   private final boolean blend;
   private final boolean fadeRGB;
   private final boolean fadeAlpha;
   private final FakeResourceLocation blankResource;

   public static BlendMethod parse(String text) {
      text = text.toLowerCase().trim();
      if (text.equals("alpha")) {
         return ALPHA;
      } else if (text.equals("add")) {
         return ADD;
      } else if (text.equals("subtract")) {
         return SUBTRACT;
      } else if (text.equals("multiply")) {
         return MULTIPLY;
      } else if (text.equals("dodge")) {
         return DODGE;
      } else if (text.equals("burn")) {
         return BURN;
      } else if (text.equals("screen")) {
         return SCREEN;
      } else if (text.equals("overlay") || text.equals("color")) {
         return OVERLAY;
      } else if (!text.equals("replace") && !text.equals("none")) {
         String[] tokens = text.split("\\s+");
         if (tokens.length >= 2) {
            try {
               int srcBlend = Integer.parseInt(tokens[0]);
               int dstBlend = Integer.parseInt(tokens[1]);
               return new BlendMethod("custom(" + srcBlend + "," + dstBlend + ")", srcBlend, dstBlend, true, true, false, 0);
            } catch (NumberFormatException var4) {
            }
         }

         return null;
      } else {
         return REPLACE;
      }
   }

   public static Set<FakeResourceLocation> getAllBlankResources() {
      return blankResources;
   }

   private BlendMethod(String name, int srcBlend, int dstBlend, boolean blend, boolean fadeRGB, boolean fadeAlpha, Integer neutralRGB) {
      this.name = name;
      this.srcBlend = srcBlend;
      this.dstBlend = dstBlend;
      this.blend = blend;
      this.fadeRGB = fadeRGB;
      this.fadeAlpha = fadeAlpha;
      if (neutralRGB == null) {
         this.blankResource = null;
      } else {
         String filename = String.format("blank_%08x.png", neutralRGB);
         this.blankResource = TexturePackAPI.newMCPatcherResourceLocation(filename);
      }

      if (this.blankResource != null) {
         blankResources.add(this.blankResource);
      }
   }

   @Override
   public String toString() {
      return this.name;
   }

   public void applyFade(float fade) {
      if (this.fadeRGB && this.fadeAlpha) {
         GLAPI.glColor4f(fade, fade, fade, fade);
      } else if (this.fadeRGB) {
         GLAPI.glColor4f(fade, fade, fade, 1.0F);
      } else if (this.fadeAlpha) {
         GLAPI.glColor4f(1.0F, 1.0F, 1.0F, fade);
      }
   }

   public void applyAlphaTest() {
      if (this.blend) {
         GL11.glDisable(3008);
      } else {
         GL11.glEnable(3008);
         GLAPI.glAlphaFunc(516, 0.01F);
      }
   }

   public void applyDepthFunc() {
      if (this.blend) {
         GLAPI.glDepthFunc(514);
      } else {
         GLAPI.glDepthFunc(515);
         GLAPI.glDepthMask(true);
      }
   }

   public void applyBlending() {
      if (this.blend) {
         GL11.glEnable(3042);
         GLAPI.glBlendFuncSeparate(this.srcBlend, this.dstBlend, 1, 771);
      } else {
         GL11.glDisable(3042);
      }
   }

   public boolean isColorBased() {
      return this.fadeRGB;
   }

   public boolean canFade() {
      return this.blend && (this.fadeAlpha || this.fadeRGB);
   }

   public FakeResourceLocation getBlankResource() {
      return this.blankResource;
   }
}
