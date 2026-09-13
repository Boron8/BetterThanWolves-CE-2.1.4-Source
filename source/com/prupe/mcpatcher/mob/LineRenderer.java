package com.prupe.mcpatcher.mob;

import com.prupe.mcpatcher.Config;
import com.prupe.mcpatcher.MCLogger;
import com.prupe.mcpatcher.mal.resource.FakeResourceLocation;
import com.prupe.mcpatcher.mal.resource.GLAPI;
import com.prupe.mcpatcher.mal.resource.PropertiesFile;
import com.prupe.mcpatcher.mal.resource.TexturePackAPI;
import com.prupe.mcpatcher.mal.tessellator.TessellatorAPI;
import com.prupe.mcpatcher.mal.util.InputHandler;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.Tessellator;
import org.lwjgl.opengl.GL11;

@Environment(EnvType.CLIENT)
public class LineRenderer {
   private static final MCLogger logger = MCLogger.getLogger("Random Mobs");
   private static final double D_WIDTH = 9.765625E-4;
   private static final double D_POS = 0.00390625;
   private static final boolean enable = Config.getBoolean("Random Mobs", "leashLine", true);
   private static final LineRenderer[] renderers = new LineRenderer[2];
   private final FakeResourceLocation texture;
   private final double width;
   private final double a;
   private final double b;
   private final double sx;
   private final double sy;
   private final double sz;
   private final int segments;
   private final double tileFactor;
   private final boolean active;
   private final InputHandler keyboard;
   private double plusWidth;
   private double plusTile;
   private double plusSX;
   private double plusSY;
   private double plusSZ;

   public static boolean renderLine(int type, double x, double y, double z, double dx, double dy, double dz) {
      LineRenderer renderer = renderers[type];
      return renderer != null && renderer.render(x, y, z, dx, dy, dz);
   }

   static void reset() {
      if (enable) {
         setup(0, "fishingline", 0.0075, 0.0, 0.25, 16);
         setup(1, "lead", 0.025, 1.3333333333333333, 0.125, 24);
      }
   }

   private static void setup(int type, String name, double defaultWidth, double a, double b, int segments) {
      LineRenderer renderer = new LineRenderer(name, defaultWidth, a, b, segments);
      if (renderer.active) {
         logger.fine("using %s", renderer);
         renderers[type] = renderer;
      } else {
         logger.fine("%s not found", renderer);
         renderers[type] = null;
      }
   }

   private LineRenderer(String name, double width, double a, double b, int segments) {
      this.texture = TexturePackAPI.newMCPatcherResourceLocation("/misc/" + name + ".png");
      this.active = TexturePackAPI.hasResource(this.texture);
      PropertiesFile properties = PropertiesFile.getNonNull(logger, TexturePackAPI.transformResourceLocation(this.texture, ".png", ".properties"));
      this.width = properties.getDouble("width", width);
      this.a = properties.getDouble("a", a);
      this.b = properties.getDouble("b", b);
      this.sx = properties.getDouble("sx", 0.0);
      this.sy = properties.getDouble("sy", 0.0);
      this.sz = properties.getDouble("sz", 0.0);
      this.segments = properties.getInt("segments", segments);
      this.tileFactor = properties.getDouble("tileFactor", 24.0);
      this.keyboard = new InputHandler(name, properties.getBoolean("debug", false));
   }

   private boolean render(double x, double y, double z, double dx, double dy, double dz) {
      if (this.keyboard.isKeyDown(55)) {
         return false;
      } else {
         boolean changed = false;
         if (this.keyboard.isEnabled()) {
            if (this.keyboard.isKeyPressed(78)) {
               changed = true;
               this.plusWidth += 9.765625E-4;
            } else if (this.keyboard.isKeyPressed(74)) {
               changed = true;
               this.plusWidth -= 9.765625E-4;
            } else if (this.keyboard.isKeyPressed(181)) {
               changed = true;
               this.plusWidth = this.plusTile = this.plusSX = this.plusSY = this.plusSZ = 0.0;
            } else if (this.keyboard.isKeyPressed(81)) {
               changed = true;
               this.plusTile--;
            } else if (this.keyboard.isKeyPressed(73)) {
               changed = true;
               this.plusTile++;
            } else if (this.keyboard.isKeyDown(75)) {
               changed = true;
               this.plusSX -= 0.00390625;
            } else if (this.keyboard.isKeyDown(77)) {
               changed = true;
               this.plusSX += 0.00390625;
            } else if (this.keyboard.isKeyDown(79)) {
               changed = true;
               this.plusSY -= 0.00390625;
            } else if (this.keyboard.isKeyDown(71)) {
               changed = true;
               this.plusSY += 0.00390625;
            } else if (this.keyboard.isKeyDown(80)) {
               changed = true;
               this.plusSZ += 0.00390625;
            } else if (this.keyboard.isKeyDown(72)) {
               changed = true;
               this.plusSZ -= 0.00390625;
            }
         }

         TexturePackAPI.bindTexture(this.texture);
         Tessellator tessellator = TessellatorAPI.getTessellator();
         TessellatorAPI.startDrawingQuads(tessellator);
         GL11.glDisable(2884);
         GLAPI.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
         dx += this.sx + this.plusSX;
         dy += this.sy + this.plusSY;
         dz += this.sz + this.plusSZ;
         double x0 = x;
         double y0 = y + this.a + this.b;
         double z0 = z;
         double u0 = 0.0;
         double len = Math.sqrt(dx * dx + dy * dy + dz * dz);
         double t = this.tileFactor + this.plusTile;
         double w = this.width + this.plusWidth;
         if (changed) {
            logger.info("%s: dx=%f, dy=%f, dz=%f, len=%f(*%d=%f), slen=%f", this, dx, dy, dz, len, (int)t, len * t, len * t / this.segments);
            System.out.printf("width=%f\n", w);
            System.out.printf("tileFactor=%f\n", t);
            System.out.printf("sx=%f\n", this.sx + this.plusSX);
            System.out.printf("sy=%f\n", this.sy + this.plusSY);
            System.out.printf("sz=%f\n", this.sz + this.plusSZ);
         }

         len *= t / this.segments;

         for (int i = 1; i <= this.segments; i++) {
            double s = (double)i / this.segments;
            double x1 = x + s * dx;
            double y1 = y + (s * s + s) * 0.5 * dy + this.a * (1.0 - s) + this.b;
            double z1 = z + s * dz;
            double u1 = (this.segments - i) * len;
            TessellatorAPI.addVertexWithUV(tessellator, x0, y0, z0, u0, 1.0);
            TessellatorAPI.addVertexWithUV(tessellator, x1, y1, z1, u1, 1.0);
            TessellatorAPI.addVertexWithUV(tessellator, x1 + w, y1 + w, z1, u1, 0.0);
            TessellatorAPI.addVertexWithUV(tessellator, x0 + w, y0 + w, z0, u0, 0.0);
            TessellatorAPI.addVertexWithUV(tessellator, x0, y0 + w, z0, u0, 1.0);
            TessellatorAPI.addVertexWithUV(tessellator, x1, y1 + w, z1, u1, 1.0);
            TessellatorAPI.addVertexWithUV(tessellator, x1 + w, y1, z1 + w, u1, 0.0);
            TessellatorAPI.addVertexWithUV(tessellator, x0 + w, y0, z0 + w, u0, 0.0);
            x0 = x1;
            y0 = y1;
            z0 = z1;
            u0 = u1;
         }

         TessellatorAPI.draw(tessellator);
         GL11.glEnable(2884);
         return true;
      }
   }

   @Override
   public String toString() {
      return "LineRenderer{" + this.texture + ", " + (this.width + this.plusWidth) + "}";
   }
}
