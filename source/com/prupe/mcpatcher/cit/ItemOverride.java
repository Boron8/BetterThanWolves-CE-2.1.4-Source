package com.prupe.mcpatcher.cit;

import com.prupe.mcpatcher.mal.item.ItemAPI;
import com.prupe.mcpatcher.mal.resource.FakeResourceLocation;
import com.prupe.mcpatcher.mal.resource.PropertiesFile;
import com.prupe.mcpatcher.mal.tile.TileLoader;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.Icon;

@Environment(EnvType.CLIENT)
final class ItemOverride extends OverrideBase {
   private Icon icon;
   private final Map<Icon, Icon> iconMap;

   ItemOverride(PropertiesFile properties) {
      super(properties);
      if (this.items == null) {
         properties.error("no matching items specified");
      }

      this.iconMap = this.alternateTextures == null ? null : new HashMap<>();
   }

   @Override
   String getType() {
      return "item";
   }

   Icon getReplacementIcon(Icon origIcon) {
      if (this.iconMap != null) {
         Icon newIcon = this.iconMap.get(origIcon);
         if (newIcon != null) {
            return newIcon;
         }
      }

      return this.icon;
   }

   void preload(TileLoader tileLoader) {
      String special = null;
      if (this.items != null) {
         if (this.items.contains(CITUtils.itemCompass)) {
            special = "compass";
         } else if (this.items.contains(CITUtils.itemClock)) {
            special = "clock";
         }
      }

      if (this.textureName != null) {
         tileLoader.preloadTile(this.textureName, false, special);
      }

      if (this.alternateTextures != null) {
         for (Entry<String, FakeResourceLocation> entry : this.alternateTextures.entrySet()) {
            tileLoader.preloadTile(entry.getValue(), false, special);
         }
      }
   }

   void registerIcon(TileLoader tileLoader) {
      if (this.textureName != null) {
         this.icon = tileLoader.getIcon(this.textureName);
      }

      if (this.alternateTextures != null) {
         for (Entry<String, FakeResourceLocation> entry : this.alternateTextures.entrySet()) {
            Icon from = tileLoader.getIcon(entry.getKey());
            Icon to = tileLoader.getIcon(entry.getValue());
            if (from != null && to != null) {
               this.iconMap.put(from, to);
            }
         }
      }
   }

   @Override
   String preprocessAltTextureKey(String name) {
      if (name.startsWith("textures/items/")) {
         name = name.substring(15);
         if (name.endsWith(".png")) {
            name = name.substring(0, name.length() - 4);
         }
      }

      return ItemAPI.expandTileName(name);
   }
}
