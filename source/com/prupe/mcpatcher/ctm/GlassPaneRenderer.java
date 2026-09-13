package com.prupe.mcpatcher.ctm;

import com.prupe.mcpatcher.Config;
import com.prupe.mcpatcher.mal.block.BlockAPI;
import com.prupe.mcpatcher.mal.tessellator.TessellatorAPI;
import java.util.Arrays;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.Block;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Icon;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.Tessellator;

@Environment(EnvType.CLIENT)
public class GlassPaneRenderer {
   private static final boolean enable = Config.getBoolean("Connected Textures", "glassPane", true);
   public static boolean skipPaneRendering;
   public static boolean skipTopEdgeRendering;
   public static boolean skipBottomEdgeRendering;
   private static final Icon[] icons = new Icon[6];
   private static double u0;
   private static double u1;
   private static double u2;
   private static double u3;
   private static double v0;
   private static double v1;
   private static double u1Scaled;
   private static double u2Scaled;

   public static void renderThin(
      RenderBlocks renderBlocks,
      Block blockPane,
      Icon origIcon,
      int i,
      int j,
      int k,
      boolean connectNorth,
      boolean connectSouth,
      boolean connectWest,
      boolean connectEast
   ) {
      if (setupIcons(renderBlocks, blockPane, origIcon, i, j, k)) {
         render(i, j, k, connectNorth, connectSouth, connectWest, connectEast, 0.0, 0.0, 0.0, false);
      }
   }

   public static void renderThick(
      RenderBlocks renderBlocks,
      Block blockPane,
      Icon origIcon,
      int i,
      int j,
      int k,
      boolean connectNorth,
      boolean connectSouth,
      boolean connectWest,
      boolean connectEast
   ) {
      if (setupIcons(renderBlocks, blockPane, origIcon, i, j, k)) {
         setupPaneEdges(renderBlocks, blockPane, i, j, k);
         render(i, j, k, connectNorth, connectSouth, connectWest, connectEast, 0.0625, 1.0, 0.001, true);
      }
   }

   private static boolean setupIcons(RenderBlocks renderBlocks, Block blockPane, Icon origIcon, int i, int j, int k) {
      skipTopEdgeRendering = false;
      skipBottomEdgeRendering = false;
      skipPaneRendering = false;
      if (!enable) {
         return false;
      } else {
         for (int face = 2; face <= 5; face++) {
            icons[face] = CTMUtils.getBlockIcon(origIcon, renderBlocks, blockPane, renderBlocks.blockAccess, i, j, k, face);
            if (icons[face] == null) {
               skipPaneRendering = false;
               return false;
            }

            if (icons[face] != origIcon) {
               skipPaneRendering = true;
            }
         }

         return skipPaneRendering;
      }
   }

   private static void setupPaneEdges(RenderBlocks renderBlocks, Block blockPane, int i, int j, int k) {
      IBlockAccess blockAccess = renderBlocks.blockAccess;
      int metadata = BlockAPI.getMetadataAt(blockAccess, i, j, k);
      skipBottomEdgeRendering = BlockAPI.getBlockAt(blockAccess, i, j - 1, k) == blockPane && BlockAPI.getMetadataAt(blockAccess, i, j - 1, k) == metadata;
      skipTopEdgeRendering = BlockAPI.getBlockAt(blockAccess, i, j + 1, k) == blockPane && BlockAPI.getMetadataAt(blockAccess, i, j + 1, k) == metadata;
   }

   private static void render(
      int i,
      int j,
      int k,
      boolean connectNorth,
      boolean connectSouth,
      boolean connectWest,
      boolean connectEast,
      double thickness,
      double uOffset,
      double yOffset,
      boolean edges
   ) {
      double i0 = i;
      double i1 = i0 + 0.5 - thickness;
      double i2 = i0 + 0.5 + thickness;
      double i3 = i0 + 1.0;
      double j0 = j + yOffset;
      double j1 = j + 1.0 - yOffset;
      double k0 = k;
      double k1 = k0 + 0.5 - thickness;
      double k2 = k0 + 0.5 + thickness;
      double k3 = k0 + 1.0;
      u1Scaled = 8.0 - uOffset;
      u2Scaled = 8.0 + uOffset;
      if (!connectNorth && !connectSouth && !connectWest && !connectEast) {
         connectEast = true;
         connectWest = true;
         connectSouth = true;
         connectNorth = true;
         if (edges) {
            setupTileCoords(5);
            drawFace(i3, j1, k2, u1, v0, i3, j0, k1, u2, v1);
            setupTileCoords(4);
            drawFace(i0, j1, k1, u1, v0, i0, j0, k2, u2, v1);
            setupTileCoords(3);
            drawFace(i1, j1, k3, u1, v0, i2, j0, k3, u2, v1);
            setupTileCoords(2);
            drawFace(i2, j1, k0, u1, v0, i1, j0, k0, u2, v1);
         }
      }

      if (connectEast && connectWest) {
         setupTileCoords(3);
         drawFace(i0, j1, k2, u0, v0, i3, j0, k2, u3, v1);
         setupTileCoords(2);
         drawFace(i3, j1, k1, u0, v0, i0, j0, k1, u3, v1);
      } else if (connectWest) {
         setupTileCoords(3);
         if (connectSouth) {
            drawFace(i0, j1, k2, u2, v0, i1, j0, k2, u3, v1);
         } else {
            drawFace(i0, j1, k2, u1, v0, i2, j0, k2, u3, v1);
         }

         setupTileCoords(2);
         if (connectNorth) {
            drawFace(i1, j1, k1, u0, v0, i0, j0, k1, u1, v1);
         } else {
            drawFace(i2, j1, k1, u0, v0, i0, j0, k1, u2, v1);
         }

         if (edges && !connectNorth && !connectSouth) {
            setupTileCoords(5);
            drawFace(i2, j1, k2, u1, v0, i2, j0, k1, u2, v1);
         }
      } else if (connectEast) {
         setupTileCoords(3);
         if (connectSouth) {
            drawFace(i2, j1, k2, u0, v0, i3, j0, k2, u1, v1);
         } else {
            drawFace(i1, j1, k2, u0, v0, i3, j0, k2, u2, v1);
         }

         setupTileCoords(2);
         if (connectNorth) {
            drawFace(i3, j1, k1, u2, v0, i2, j0, k1, u3, v1);
         } else {
            drawFace(i3, j1, k1, u1, v0, i1, j0, k1, u3, v1);
         }

         if (edges && !connectNorth && !connectSouth) {
            setupTileCoords(4);
            drawFace(i1, j1, k1, u1, v0, i1, j0, k2, u2, v1);
         }
      }

      if (connectNorth && connectSouth) {
         setupTileCoords(4);
         drawFace(i1, j1, k0, u0, v0, i1, j0, k3, u3, v1);
         setupTileCoords(5);
         drawFace(i2, j1, k3, u0, v0, i2, j0, k0, u3, v1);
      } else if (connectNorth) {
         setupTileCoords(4);
         if (connectWest) {
            drawFace(i1, j1, k0, u2, v0, i1, j0, k1, u3, v1);
         } else {
            drawFace(i1, j1, k0, u1, v0, i1, j0, k2, u3, v1);
         }

         setupTileCoords(5);
         if (connectEast) {
            drawFace(i2, j1, k1, u0, v0, i2, j0, k0, u1, v1);
         } else {
            drawFace(i2, j1, k2, u0, v0, i2, j0, k0, u2, v1);
         }

         if (edges && !connectWest && !connectEast) {
            setupTileCoords(3);
            drawFace(i1, j1, k2, u1, v0, i2, j0, k2, u2, v1);
         }
      } else if (connectSouth) {
         setupTileCoords(4);
         if (connectWest) {
            drawFace(i1, j1, k2, u0, v0, i1, j0, k3, u1, v1);
         } else {
            drawFace(i1, j1, k1, u0, v0, i1, j0, k3, u2, v1);
         }

         setupTileCoords(5);
         if (connectEast) {
            drawFace(i2, j1, k3, u2, v0, i2, j0, k2, u3, v1);
         } else {
            drawFace(i2, j1, k3, u1, v0, i2, j0, k1, u3, v1);
         }

         if (edges && !connectWest && !connectEast) {
            setupTileCoords(2);
            drawFace(i2, j1, k1, u1, v0, i1, j0, k1, u2, v1);
         }
      }
   }

   private static void setupTileCoords(int face) {
      Icon icon = icons[face];
      u0 = icon.getMinU();
      u1 = icon.getInterpolatedU(u1Scaled);
      u2 = icon.getInterpolatedU(u2Scaled);
      u3 = icon.getMaxU();
      v0 = icon.getMinV();
      v1 = icon.getMaxV();
   }

   private static void drawFace(double x0, double y0, double z0, double u0, double v0, double x1, double y1, double z1, double u1, double v1) {
      Tessellator tessellator = TessellatorAPI.getTessellator();
      TessellatorAPI.addVertexWithUV(tessellator, x0, y0, z0, u0, v0);
      TessellatorAPI.addVertexWithUV(tessellator, x0, y1, z0, u0, v1);
      TessellatorAPI.addVertexWithUV(tessellator, x1, y1, z1, u1, v1);
      TessellatorAPI.addVertexWithUV(tessellator, x1, y0, z1, u1, v0);
   }

   static void clear() {
      Arrays.fill(icons, null);
      skipPaneRendering = false;
   }
}
