package com.prupe.mcpatcher.ctm;

import com.prupe.mcpatcher.mal.resource.PropertiesFile;
import com.prupe.mcpatcher.mal.tile.TileLoader;
import com.prupe.mcpatcher.mal.util.WeightedIndex;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.Icon;

@Environment(EnvType.CLIENT)
class TileOverrideImpl {
   @Environment(EnvType.CLIENT)
   static final class CTM extends TileOverride {
      private static final int[] neighborMap = new int[]{
         0,
         3,
         0,
         3,
         12,
         5,
         12,
         15,
         0,
         3,
         0,
         3,
         12,
         5,
         12,
         15,
         1,
         2,
         1,
         2,
         4,
         7,
         4,
         29,
         1,
         2,
         1,
         2,
         13,
         31,
         13,
         14,
         0,
         3,
         0,
         3,
         12,
         5,
         12,
         15,
         0,
         3,
         0,
         3,
         12,
         5,
         12,
         15,
         1,
         2,
         1,
         2,
         4,
         7,
         4,
         29,
         1,
         2,
         1,
         2,
         13,
         31,
         13,
         14,
         36,
         17,
         36,
         17,
         24,
         19,
         24,
         43,
         36,
         17,
         36,
         17,
         24,
         19,
         24,
         43,
         16,
         18,
         16,
         18,
         6,
         46,
         6,
         21,
         16,
         18,
         16,
         18,
         28,
         9,
         28,
         22,
         36,
         17,
         36,
         17,
         24,
         19,
         24,
         43,
         36,
         17,
         36,
         17,
         24,
         19,
         24,
         43,
         37,
         40,
         37,
         40,
         30,
         8,
         30,
         34,
         37,
         40,
         37,
         40,
         25,
         23,
         25,
         45,
         0,
         3,
         0,
         3,
         12,
         5,
         12,
         15,
         0,
         3,
         0,
         3,
         12,
         5,
         12,
         15,
         1,
         2,
         1,
         2,
         4,
         7,
         4,
         29,
         1,
         2,
         1,
         2,
         13,
         31,
         13,
         14,
         0,
         3,
         0,
         3,
         12,
         5,
         12,
         15,
         0,
         3,
         0,
         3,
         12,
         5,
         12,
         15,
         1,
         2,
         1,
         2,
         4,
         7,
         4,
         29,
         1,
         2,
         1,
         2,
         13,
         31,
         13,
         14,
         36,
         39,
         36,
         39,
         24,
         41,
         24,
         27,
         36,
         39,
         36,
         39,
         24,
         41,
         24,
         27,
         16,
         42,
         16,
         42,
         6,
         20,
         6,
         10,
         16,
         42,
         16,
         42,
         28,
         35,
         28,
         44,
         36,
         39,
         36,
         39,
         24,
         41,
         24,
         27,
         36,
         39,
         36,
         39,
         24,
         41,
         24,
         27,
         37,
         38,
         37,
         38,
         30,
         11,
         30,
         32,
         37,
         38,
         37,
         38,
         25,
         33,
         25,
         26
      };

      CTM(PropertiesFile properties, TileLoader tileLoader) {
         super(properties, tileLoader);
      }

      @Override
      String getMethod() {
         return "ctm";
      }

      @Override
      String checkTileMap() {
         return this.getNumberOfTiles() >= 47 ? null : "requires at least 47 tiles";
      }

      @Override
      boolean requiresFace() {
         return true;
      }

      @Override
      Icon getTileWorld_Impl(RenderBlockState renderBlockState, Icon origIcon) {
         int neighborBits = 0;

         for (int bit = 0; bit < 8; bit++) {
            if (this.shouldConnect(renderBlockState, origIcon, bit)) {
               neighborBits |= 1 << bit;
            }
         }

         return this.icons[neighborMap[neighborBits]];
      }

      @Override
      Icon getTileHeld_Impl(RenderBlockState renderBlockState, Icon origIcon) {
         return this.icons[0];
      }
   }

   @Environment(EnvType.CLIENT)
   static final class Fixed extends TileOverride {
      Fixed(PropertiesFile properties, TileLoader tileLoader) {
         super(properties, tileLoader);
      }

      @Override
      String getMethod() {
         return "fixed";
      }

      @Override
      String checkTileMap() {
         return this.getNumberOfTiles() == 1 ? null : "requires exactly 1 tile";
      }

      @Override
      Icon getTileWorld_Impl(RenderBlockState renderBlockState, Icon origIcon) {
         return this.icons[0];
      }

      @Override
      Icon getTileHeld_Impl(RenderBlockState renderBlockState, Icon origIcon) {
         return this.icons[0];
      }
   }

   @Environment(EnvType.CLIENT)
   static class Horizontal extends TileOverride {
      private static final int[] neighborMap = new int[]{3, 2, 0, 1};

      Horizontal(PropertiesFile properties, TileLoader tileLoader) {
         super(properties, tileLoader);
      }

      @Override
      String getMethod() {
         return "horizontal";
      }

      @Override
      String checkTileMap() {
         return this.getNumberOfTiles() == 4 ? null : "requires exactly 4 tiles";
      }

      @Override
      Icon getTileWorld_Impl(RenderBlockState renderBlockState, Icon origIcon) {
         int face = renderBlockState.getFaceForHV();
         if (face < 0) {
            return null;
         } else {
            int neighborBits = 0;
            if (this.shouldConnect(renderBlockState, origIcon, 0)) {
               neighborBits |= 1;
            }

            if (this.shouldConnect(renderBlockState, origIcon, 4)) {
               neighborBits |= 2;
            }

            return this.icons[neighborMap[neighborBits]];
         }
      }

      @Override
      Icon getTileHeld_Impl(RenderBlockState renderBlockState, Icon origIcon) {
         return this.icons[3];
      }
   }

   @Environment(EnvType.CLIENT)
   static final class HorizontalVertical extends TileOverrideImpl.Horizontal {
      private static final int[] neighborMap = new int[]{
         3,
         3,
         6,
         3,
         3,
         3,
         3,
         3,
         3,
         3,
         6,
         3,
         3,
         3,
         3,
         3,
         4,
         4,
         5,
         4,
         4,
         4,
         4,
         4,
         3,
         3,
         6,
         3,
         3,
         3,
         3,
         3,
         3,
         3,
         6,
         3,
         3,
         3,
         3,
         3,
         3,
         3,
         6,
         3,
         3,
         3,
         3,
         3,
         3,
         3,
         6,
         3,
         3,
         3,
         3,
         3,
         3,
         3,
         6,
         3,
         3,
         3,
         3,
         3
      };

      HorizontalVertical(PropertiesFile properties, TileLoader tileLoader) {
         super(properties, tileLoader);
      }

      @Override
      String getMethod() {
         return "horizontal+vertical";
      }

      @Override
      String checkTileMap() {
         return this.getNumberOfTiles() == 7 ? null : "requires exactly 7 tiles";
      }

      @Override
      Icon getTileWorld_Impl(RenderBlockState renderBlockState, Icon origIcon) {
         Icon icon = super.getTileWorld_Impl(renderBlockState, origIcon);
         if (icon != this.icons[3]) {
            return icon;
         } else {
            int neighborBits = 0;
            if (this.shouldConnect(renderBlockState, origIcon, 1)) {
               neighborBits |= 1;
            }

            if (this.shouldConnect(renderBlockState, origIcon, 2)) {
               neighborBits |= 2;
            }

            if (this.shouldConnect(renderBlockState, origIcon, 3)) {
               neighborBits |= 4;
            }

            if (this.shouldConnect(renderBlockState, origIcon, 5)) {
               neighborBits |= 8;
            }

            if (this.shouldConnect(renderBlockState, origIcon, 6)) {
               neighborBits |= 16;
            }

            if (this.shouldConnect(renderBlockState, origIcon, 7)) {
               neighborBits |= 32;
            }

            return this.icons[neighborMap[neighborBits]];
         }
      }
   }

   @Environment(EnvType.CLIENT)
   static final class Random1 extends TileOverride {
      private final int symmetry;
      private final boolean linked;
      private final WeightedIndex chooser;

      Random1(PropertiesFile properties, TileLoader tileLoader) {
         super(properties, tileLoader);
         String sym = properties.getString("symmetry", "none");
         if (sym.equals("all")) {
            this.symmetry = 6;
         } else if (sym.equals("opposite")) {
            this.symmetry = 2;
         } else {
            this.symmetry = 1;
         }

         this.linked = properties.getBoolean("linked", false);
         this.chooser = WeightedIndex.create(this.getNumberOfTiles(), properties.getString("weights", ""));
         if (this.chooser == null) {
            properties.error("invalid weights");
         }
      }

      @Override
      String getMethod() {
         return "random";
      }

      @Override
      Icon getTileWorld_Impl(RenderBlockState renderBlockState, Icon origIcon) {
         int face = renderBlockState.getBlockFace();
         if (face < 0) {
            face = 0;
         }

         int i = renderBlockState.getI();
         int j = renderBlockState.getJ();
         int k = renderBlockState.getK();
         if (this.linked && renderBlockState.setCoordOffsetsForRenderType()) {
            i += renderBlockState.getDI();
            j += renderBlockState.getDJ();
            k += renderBlockState.getDK();
         }

         long hash = WeightedIndex.hash128To64(i, j, k, face / this.symmetry);
         int index = this.chooser.choose(hash);
         return this.icons[index];
      }

      @Override
      Icon getTileHeld_Impl(RenderBlockState renderBlockState, Icon origIcon) {
         return this.icons[0];
      }
   }

   @Environment(EnvType.CLIENT)
   static final class Repeat extends TileOverride {
      private final int width;
      private final int height;
      private final int symmetry;

      Repeat(PropertiesFile properties, TileLoader tileLoader) {
         super(properties, tileLoader);
         this.width = properties.getInt("width", 0);
         this.height = properties.getInt("height", 0);
         if (this.width <= 0 || this.height <= 0) {
            properties.error("invalid width and height (%dx%d)", this.width, this.height);
         }

         String sym = properties.getString("symmetry", "none");
         if (sym.equals("opposite")) {
            this.symmetry = -2;
         } else {
            this.symmetry = -1;
         }
      }

      @Override
      String getMethod() {
         return "repeat";
      }

      @Override
      String checkTileMap() {
         return this.getNumberOfTiles() == this.width * this.height ? null : String.format("requires exactly %dx%d tiles", this.width, this.height);
      }

      @Override
      Icon getTileWorld_Impl(RenderBlockState renderBlockState, Icon origIcon) {
         int face = renderBlockState.getBlockFace();
         if (face < 0) {
            face = 0;
         }

         face &= this.symmetry;
         int i = renderBlockState.getI();
         int j = renderBlockState.getJ();
         int k = renderBlockState.getK();
         int[] xOffset = renderBlockState.getOffset(face, 4);
         int[] yOffset = renderBlockState.getOffset(face, 2);
         int x = i * xOffset[0] + j * xOffset[1] + k * xOffset[2];
         int y = i * yOffset[0] + j * yOffset[1] + k * yOffset[2];
         if (face == 2 || face == 5) {
            x--;
         }

         x %= this.width;
         if (x < 0) {
            x += this.width;
         }

         y %= this.height;
         if (y < 0) {
            y += this.height;
         }

         return this.icons[this.width * y + x];
      }

      @Override
      Icon getTileHeld_Impl(RenderBlockState renderBlockState, Icon origIcon) {
         return this.icons[0];
      }
   }

   @Environment(EnvType.CLIENT)
   static final class Top extends TileOverride {
      Top(PropertiesFile properties, TileLoader tileLoader) {
         super(properties, tileLoader);
      }

      @Override
      String getMethod() {
         return "top";
      }

      @Override
      String checkTileMap() {
         return this.getNumberOfTiles() == 1 ? null : "requires exactly 1 tile";
      }

      @Override
      Icon getTileWorld_Impl(RenderBlockState renderBlockState, Icon origIcon) {
         int face = renderBlockState.getBlockFace();
         if (face < 0) {
            face = 2;
         } else if (face <= 1) {
            return null;
         }

         return this.shouldConnect(renderBlockState, origIcon, face, 6) ? this.icons[0] : null;
      }

      @Override
      Icon getTileHeld_Impl(RenderBlockState renderBlockState, Icon origIcon) {
         return null;
      }
   }

   @Environment(EnvType.CLIENT)
   static class Vertical extends TileOverride {
      private static final int[] neighborMap = new int[]{3, 2, 0, 1};

      Vertical(PropertiesFile properties, TileLoader tileLoader) {
         super(properties, tileLoader);
      }

      @Override
      String getMethod() {
         return "vertical";
      }

      @Override
      String checkTileMap() {
         return this.getNumberOfTiles() == 4 ? null : "requires exactly 4 tiles";
      }

      @Override
      Icon getTileWorld_Impl(RenderBlockState renderBlockState, Icon origIcon) {
         int face = renderBlockState.getFaceForHV();
         if (face < 0) {
            return null;
         } else {
            int neighborBits = 0;
            if (this.shouldConnect(renderBlockState, origIcon, 2)) {
               neighborBits |= 1;
            }

            if (this.shouldConnect(renderBlockState, origIcon, 6)) {
               neighborBits |= 2;
            }

            return this.icons[neighborMap[neighborBits]];
         }
      }

      @Override
      Icon getTileHeld_Impl(RenderBlockState renderBlockState, Icon origIcon) {
         return this.icons[3];
      }
   }

   @Environment(EnvType.CLIENT)
   static final class VerticalHorizontal extends TileOverrideImpl.Vertical {
      private static final int[] neighborMap = new int[]{
         3,
         6,
         3,
         3,
         3,
         6,
         3,
         3,
         4,
         5,
         4,
         4,
         3,
         6,
         3,
         3,
         3,
         6,
         3,
         3,
         3,
         6,
         3,
         3,
         3,
         6,
         3,
         3,
         3,
         6,
         3,
         3,
         3,
         3,
         3,
         3,
         3,
         3,
         3,
         3,
         4,
         4,
         4,
         4,
         3,
         3,
         3,
         3,
         3,
         3,
         3,
         3,
         3,
         3,
         3,
         3,
         3,
         3,
         3,
         3,
         3,
         3,
         3,
         3
      };

      VerticalHorizontal(PropertiesFile properties, TileLoader tileLoader) {
         super(properties, tileLoader);
      }

      @Override
      String getMethod() {
         return "vertical+horizontal";
      }

      @Override
      String checkTileMap() {
         return this.getNumberOfTiles() == 7 ? null : "requires exactly 7 tiles";
      }

      @Override
      Icon getTileWorld_Impl(RenderBlockState renderBlockState, Icon origIcon) {
         Icon icon = super.getTileWorld_Impl(renderBlockState, origIcon);
         if (icon != this.icons[3]) {
            return icon;
         } else {
            int neighborBits = 0;
            if (this.shouldConnect(renderBlockState, origIcon, 0)) {
               neighborBits |= 1;
            }

            if (this.shouldConnect(renderBlockState, origIcon, 1)) {
               neighborBits |= 2;
            }

            if (this.shouldConnect(renderBlockState, origIcon, 3)) {
               neighborBits |= 4;
            }

            if (this.shouldConnect(renderBlockState, origIcon, 4)) {
               neighborBits |= 8;
            }

            if (this.shouldConnect(renderBlockState, origIcon, 5)) {
               neighborBits |= 16;
            }

            if (this.shouldConnect(renderBlockState, origIcon, 7)) {
               neighborBits |= 32;
            }

            return this.icons[neighborMap[neighborBits]];
         }
      }
   }
}
