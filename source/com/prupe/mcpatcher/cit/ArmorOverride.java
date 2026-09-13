package com.prupe.mcpatcher.cit;

import com.prupe.mcpatcher.mal.resource.FakeResourceLocation;
import com.prupe.mcpatcher.mal.resource.PropertiesFile;
import com.prupe.mcpatcher.mal.resource.TexturePackAPI;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
final class ArmorOverride extends OverrideBase {
   private final Map<FakeResourceLocation, FakeResourceLocation> armorMap;

   ArmorOverride(PropertiesFile properties) {
      super(properties);
      if (this.items == null) {
         properties.error("no matching items specified");
      }

      if (this.textureName == null && this.alternateTextures == null) {
         properties.error("no replacement textures specified");
      }

      if (this.alternateTextures == null) {
         this.armorMap = null;
      } else {
         this.armorMap = new HashMap<>();

         for (Entry<String, FakeResourceLocation> entry : this.alternateTextures.entrySet()) {
            String key = entry.getKey();
            FakeResourceLocation value = entry.getValue();
            this.armorMap.put(TexturePackAPI.parseResourceLocation(CITUtils.FIXED_ARMOR_RESOURCE, key), value);
         }
      }
   }

   @Override
   String getType() {
      return "armor";
   }

   FakeResourceLocation getReplacementTexture(FakeResourceLocation origResource) {
      if (this.armorMap != null) {
         FakeResourceLocation newResource = this.armorMap.get(origResource);
         if (newResource != null) {
            return newResource;
         }
      }

      return this.textureName;
   }

   @Override
   String preprocessAltTextureKey(String name) {
      if (!name.endsWith(".png")) {
         name = name + ".png";
      }

      if (!name.contains("/")) {
         name = "./" + name;
      }

      return TexturePackAPI.parseResourceLocation(CITUtils.FIXED_ARMOR_RESOURCE, name).toString();
   }
}
