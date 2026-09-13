package com.prupe.mcpatcher.ctm;

import com.prupe.mcpatcher.Config;
import com.prupe.mcpatcher.mal.block.BlockStateMatcher;
import com.prupe.mcpatcher.mal.tile.IconAPI;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.Block;
import net.minecraft.src.Icon;

@Environment(EnvType.CLIENT)
public abstract class TileOverrideIterator implements Iterator<ITileOverride> {
   private static final int MAX_RECURSION = Config.getInt("Connected Textures", "maxRecursion", 4);
   private final Map<Block, List<BlockStateMatcher>> allBlockOverrides;
   private final Map<String, List<ITileOverride>> allTileOverrides;
   protected Icon currentIcon;
   private List<BlockStateMatcher> blockOverrides;
   private List<ITileOverride> tileOverrides;
   private final Set<ITileOverride> skipOverrides = new HashSet<>();
   private RenderBlockState renderBlockState;
   private int blockPos;
   private int iconPos;
   private boolean foundNext;
   private ITileOverride nextOverride;
   private ITileOverride lastMatchedOverride;

   protected TileOverrideIterator(Map<Block, List<BlockStateMatcher>> allBlockOverrides, Map<String, List<ITileOverride>> allTileOverrides) {
      this.allBlockOverrides = allBlockOverrides;
      this.allTileOverrides = allTileOverrides;
   }

   void clear() {
      this.currentIcon = null;
      this.blockOverrides = null;
      this.tileOverrides = null;
      this.nextOverride = null;
      this.lastMatchedOverride = null;
      this.skipOverrides.clear();
   }

   private void resetForNextPass() {
      this.blockOverrides = null;
      this.tileOverrides = this.allTileOverrides.get(IconAPI.getIconName(this.currentIcon));
      this.blockPos = 0;
      this.iconPos = 0;
      this.foundNext = false;
   }

   @Override
   public boolean hasNext() {
      if (this.foundNext) {
         return true;
      } else {
         if (this.tileOverrides != null) {
            while (this.iconPos < this.tileOverrides.size()) {
               if (this.checkOverride(this.tileOverrides.get(this.iconPos++))) {
                  this.renderBlockState.setFilter(null);
                  return true;
               }
            }
         }

         if (this.blockOverrides != null) {
            while (this.blockPos < this.blockOverrides.size()) {
               BlockStateMatcher matcher = this.blockOverrides.get(this.blockPos++);
               if (this.renderBlockState.match(matcher) && this.checkOverride((ITileOverride)matcher.getData())) {
                  this.renderBlockState.setFilter(matcher);
                  return true;
               }
            }
         }

         return false;
      }
   }

   public ITileOverride next() {
      if (!this.foundNext) {
         throw new IllegalStateException("next called before hasNext() == true");
      } else {
         this.foundNext = false;
         return this.nextOverride;
      }
   }

   @Override
   public void remove() {
      throw new UnsupportedOperationException("remove not supported");
   }

   private boolean checkOverride(ITileOverride override) {
      if (override != null && !override.isDisabled() && !this.skipOverrides.contains(override)) {
         this.foundNext = true;
         this.nextOverride = override;
         return true;
      } else {
         return false;
      }
   }

   public ITileOverride go(RenderBlockState renderBlockState, Icon origIcon) {
      this.renderBlockState = renderBlockState;
      renderBlockState.setFilter(null);
      this.currentIcon = origIcon;
      this.blockOverrides = this.allBlockOverrides.get(renderBlockState.getBlock());
      this.tileOverrides = this.allTileOverrides.get(IconAPI.getIconName(origIcon));
      this.blockPos = 0;
      this.iconPos = 0;
      this.foundNext = false;
      this.nextOverride = null;
      this.lastMatchedOverride = null;
      this.skipOverrides.clear();

      for (int pass = 0; pass < MAX_RECURSION; pass++) {
         while (this.hasNext()) {
            ITileOverride override = this.next();
            Icon newIcon = this.getTile(override, renderBlockState, origIcon);
            if (newIcon != null) {
               this.lastMatchedOverride = override;
               this.skipOverrides.add(override);
               this.currentIcon = newIcon;
               this.resetForNextPass();
               break;
            }
         }
         break;
      }

      return this.lastMatchedOverride;
   }

   public Icon getIcon() {
      return this.currentIcon;
   }

   protected abstract Icon getTile(ITileOverride var1, RenderBlockState var2, Icon var3);

   @Environment(EnvType.CLIENT)
   public static final class IJK extends TileOverrideIterator {
      IJK(Map<Block, List<BlockStateMatcher>> blockOverrides, Map<String, List<ITileOverride>> tileOverrides) {
         super(blockOverrides, tileOverrides);
      }

      @Override
      protected Icon getTile(ITileOverride override, RenderBlockState renderBlockState, Icon origIcon) {
         return override.getTileWorld(renderBlockState, origIcon);
      }
   }

   @Environment(EnvType.CLIENT)
   public static final class Metadata extends TileOverrideIterator {
      Metadata(Map<Block, List<BlockStateMatcher>> blockOverrides, Map<String, List<ITileOverride>> tileOverrides) {
         super(blockOverrides, tileOverrides);
      }

      @Override
      protected Icon getTile(ITileOverride override, RenderBlockState renderBlockState, Icon origIcon) {
         return override.getTileHeld(renderBlockState, origIcon);
      }
   }
}
