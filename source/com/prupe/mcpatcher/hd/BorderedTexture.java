package com.prupe.mcpatcher.hd;

import com.prupe.mcpatcher.MCLogger;
import com.prupe.mcpatcher.MCPatcherUtils;
import com.prupe.mcpatcher.mal.tile.IconAPI;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.Texture;
import net.minecraft.src.TextureStitched;

@Environment(EnvType.CLIENT)
public class BorderedTexture extends TextureStitched {
   private static final MCLogger logger = MCLogger.getLogger("Mipmap");
   private float minU;
   private float maxU;
   private float minV;
   private float maxV;
   private float scaledWidth;
   private float scaledHeight;
   private int tilesheetWidth;
   private int tilesheetHeight;
   private int x0;
   private int y0;
   private String tilesheet;
   int border;

   public static TextureStitched create(String tilesheet, String name) {
      return (TextureStitched)(AAHelper.useAAForTexture(tilesheet) ? new BorderedTexture(tilesheet, name) : new TextureStitched(name));
   }

   private BorderedTexture(String tilesheet, String name) {
      super(name);
      this.tilesheet = tilesheet;
   }

   @Override
   public void init(Texture texture, List animations, int x0, int y0, int width, int height, boolean flipped) {
      super.init(texture, animations, x0, y0, width, height, flipped);
      this.tilesheetWidth = texture.getWidth();
      this.tilesheetHeight = texture.getHeight();
      this.x0 = x0;
      this.y0 = y0;
      this.border = MCPatcherUtils.isNullOrEmpty(animations) ? 0 : ((Texture)animations.get(0)).border;
      this.setBorderWidth(width, height, this.border);
   }

   @Override
   public float getMinU() {
      return this.minU;
   }

   @Override
   public float getMaxU() {
      return this.maxU;
   }

   @Override
   public float getInterpolatedU(double u) {
      return this.border > 0 ? this.minU + (float)u * this.scaledWidth : super.getInterpolatedU(u);
   }

   @Override
   public float getMinV() {
      return this.minV;
   }

   @Override
   public float getMaxV() {
      return this.maxV;
   }

   @Override
   public float getInterpolatedV(double v) {
      return this.border > 0 ? this.minV + (float)v * this.scaledHeight : super.getInterpolatedV(v);
   }

   @Override
   public void copyFrom(TextureStitched stitched) {
      if (stitched instanceof BorderedTexture) {
         BorderedTexture bordered = (BorderedTexture)stitched;
         this.tilesheetWidth = bordered.tilesheetWidth;
         this.tilesheetHeight = bordered.tilesheetHeight;
         this.x0 = bordered.x0;
         this.y0 = bordered.y0;
         this.tilesheet = bordered.tilesheet;
         this.border = bordered.border;
      }
   }

   void setBorderWidth(int width, int height, int border) {
      this.border = border;
      if (width > 0 && height > 0) {
         logger.finer(
            "setBorderWidth(%s, %s, %d): %dx%d -> %dx%d",
            this.tilesheet,
            IconAPI.getIconName(this),
            border,
            width - 2 * border,
            height - 2 * border,
            width,
            height
         );
         if (border > 0) {
            this.x0 += border;
            this.y0 += border;
            width -= 2 * border;
            height -= 2 * border;
            this.minU = (float)this.x0 / this.tilesheetWidth;
            this.maxU = (float)(this.x0 + width) / this.tilesheetWidth;
            this.minV = (float)this.y0 / this.tilesheetHeight;
            this.maxV = (float)(this.y0 + height) / this.tilesheetHeight;
         } else {
            this.minU = super.getMinU();
            this.maxU = super.getMaxU();
            this.minV = super.getMinV();
            this.maxV = super.getMaxV();
         }

         this.scaledWidth = (this.maxU - this.minU) / 16.0F;
         this.scaledHeight = (this.maxV - this.minV) / 16.0F;
      } else {
         this.x0 = this.y0 = 0;
         this.minU = this.maxU = this.minV = this.maxV = 0.0F;
         this.scaledWidth = this.scaledHeight = 0.0F;
      }
   }
}
