package com.prupe.mcpatcher.cit;

import com.prupe.mcpatcher.mal.resource.BlendMethod;
import com.prupe.mcpatcher.mal.resource.FakeResourceLocation;
import com.prupe.mcpatcher.mal.resource.GLAPI;
import com.prupe.mcpatcher.mal.resource.PropertiesFile;
import com.prupe.mcpatcher.mal.resource.TexturePackAPI;
import com.prupe.mcpatcher.mal.tessellator.TessellatorAPI;
import com.prupe.mcpatcher.mal.tile.IconAPI;
import java.awt.image.BufferedImage;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.Icon;
import net.minecraft.src.ItemRenderer;
import net.minecraft.src.Tessellator;
import org.lwjgl.opengl.GL11;

@Environment(EnvType.CLIENT)
final class Enchantment extends OverrideBase {
   private static final float ITEM_2D_THICKNESS = 0.0625F;
   static float baseArmorWidth;
   static float baseArmorHeight;
   private static boolean lightingWasEnabled;
   final int layer;
   final BlendMethod blendMethod;
   private final float rotation;
   private final double speed;
   final float duration;
   private boolean armorScaleSet;
   private float armorScaleX;
   private float armorScaleY;

   static void beginOuter2D() {
      GL11.glEnable(3008);
      GLAPI.glAlphaFunc(516, 0.01F);
      GL11.glEnable(3042);
      GLAPI.glDepthFunc(514);
      GLAPI.glDepthMask(false);
      GL11.glDisable(2896);
      GL11.glMatrixMode(5890);
   }

   static void endOuter2D() {
      GLAPI.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      GL11.glDisable(3042);
      GLAPI.glDepthFunc(515);
      GLAPI.glDepthMask(true);
      GL11.glEnable(2896);
      GL11.glMatrixMode(5888);
   }

   static void beginOuter3D() {
      GL11.glEnable(3008);
      GLAPI.glAlphaFunc(516, 0.01F);
      GL11.glEnable(3042);
      GLAPI.glDepthFunc(514);
      lightingWasEnabled = GL11.glGetBoolean(2896);
      GL11.glDisable(2896);
      GL11.glMatrixMode(5890);
   }

   static void endOuter3D() {
      GLAPI.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      GL11.glDisable(3042);
      GLAPI.glDepthFunc(515);
      if (lightingWasEnabled) {
         GL11.glEnable(2896);
      }

      GL11.glMatrixMode(5888);
   }

   Enchantment(PropertiesFile properties) {
      super(properties);
      if (properties.valid() && this.textureName == null && this.alternateTextures == null) {
         properties.error("no source texture specified");
      }

      this.layer = properties.getInt("layer", 0);
      String value = properties.getString("blend", "add");
      this.blendMethod = BlendMethod.parse(value);
      if (this.blendMethod == null) {
         properties.error("unknown blend type %s", value);
      }

      this.rotation = properties.getFloat("rotation", 0.0F);
      this.speed = properties.getDouble("speed", 0.0);
      this.duration = properties.getFloat("duration", 1.0F);
      String valueX = properties.getString("armorScaleX", "");
      String valueY = properties.getString("armorScaleY", "");
      if (!valueX.isEmpty() && !valueY.isEmpty()) {
         try {
            this.armorScaleX = Float.parseFloat(valueX);
            this.armorScaleY = Float.parseFloat(valueY);
            this.armorScaleSet = true;
         } catch (NumberFormatException var6) {
            var6.printStackTrace();
         }
      }
   }

   @Override
   String getType() {
      return "enchantment";
   }

   void render2D(Tessellator tessellator, float intensity, float x0, float y0, float x1, float y1, float z) {
      if (!(intensity <= 0.0F)) {
         if (intensity > 1.0F) {
            intensity = 1.0F;
         }

         if (this.bindTexture(CITUtils.lastOrigIcon)) {
            this.begin(intensity);
            TessellatorAPI.startDrawingQuads(tessellator);
            TessellatorAPI.addVertexWithUV(tessellator, x0, y0, z, 0.0, 0.0);
            TessellatorAPI.addVertexWithUV(tessellator, x0, y1, z, 0.0, 1.0);
            TessellatorAPI.addVertexWithUV(tessellator, x1, y1, z, 1.0, 1.0);
            TessellatorAPI.addVertexWithUV(tessellator, x1, y0, z, 1.0, 0.0);
            TessellatorAPI.draw(tessellator);
            this.end();
         }
      }
   }

   void render3D(Tessellator tessellator, float intensity, int width, int height) {
      if (!(intensity <= 0.0F)) {
         if (intensity > 1.0F) {
            intensity = 1.0F;
         }

         if (this.bindTexture(CITUtils.lastOrigIcon)) {
            this.begin(intensity);
            ItemRenderer.renderItemIn2D(tessellator, 1.0F, 0.0F, 0.0F, 1.0F, width, height, 0.0625F);
            this.end();
         }
      }
   }

   boolean bindTexture(Icon icon) {
      FakeResourceLocation texture;
      if (this.alternateTextures != null && icon != null) {
         texture = this.alternateTextures.get(IconAPI.getIconName(icon));
         if (texture == null) {
            texture = this.textureName;
         }
      } else {
         texture = this.textureName;
      }

      if (texture == null) {
         return false;
      } else {
         TexturePackAPI.bindTexture(texture);
         return true;
      }
   }

   void beginArmor(float intensity) {
      GL11.glEnable(3042);
      GLAPI.glDepthFunc(514);
      GLAPI.glDepthMask(false);
      GL11.glDisable(2896);
      GL11.glMatrixMode(5890);
      this.begin(intensity);
      if (!this.armorScaleSet) {
         this.setArmorScale();
      }

      GL11.glScalef(this.armorScaleX, this.armorScaleY, 1.0F);
      GL11.glMatrixMode(5888);
   }

   void endArmor() {
      GLAPI.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      GL11.glDisable(3042);
      GLAPI.glDepthFunc(515);
      GLAPI.glDepthMask(true);
      GL11.glEnable(2896);
      GL11.glMatrixMode(5890);
      this.end();
      GL11.glLoadIdentity();
      GL11.glMatrixMode(5888);
   }

   void begin(float intensity) {
      this.blendMethod.applyBlending();
      this.blendMethod.applyDepthFunc();
      this.blendMethod.applyFade(intensity);
      GL11.glPushMatrix();
      if (this.speed != 0.0) {
         double offset = System.currentTimeMillis() * this.speed / 3000.0;
         offset -= Math.floor(offset);
         GL11.glTranslatef((float)offset * 8.0F, 0.0F, 0.0F);
      }

      GL11.glRotatef(this.rotation, 0.0F, 0.0F, 1.0F);
   }

   void end() {
      GL11.glPopMatrix();
   }

   private void setArmorScale() {
      this.armorScaleSet = true;
      this.armorScaleX = 1.0F;
      this.armorScaleY = 0.5F;
      BufferedImage overlayImage = TexturePackAPI.getImage(this.textureName);
      if (overlayImage != null) {
         if (overlayImage.getWidth() < baseArmorWidth) {
            this.armorScaleX = this.armorScaleX * (baseArmorWidth / overlayImage.getWidth());
         }

         if (overlayImage.getHeight() < baseArmorHeight) {
            this.armorScaleY = this.armorScaleY * (baseArmorHeight / overlayImage.getHeight());
         }
      }

      logger.finer("%s: scaling by %.3fx%.3f for armor model", this, this.armorScaleX, this.armorScaleY);
   }
}
