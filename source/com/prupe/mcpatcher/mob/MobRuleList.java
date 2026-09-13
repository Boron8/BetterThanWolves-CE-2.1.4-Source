package com.prupe.mcpatcher.mob;

import com.prupe.mcpatcher.MCLogger;
import com.prupe.mcpatcher.MCPatcherUtils;
import com.prupe.mcpatcher.mal.biome.BiomeAPI;
import com.prupe.mcpatcher.mal.resource.FakeResourceLocation;
import com.prupe.mcpatcher.mal.resource.PropertiesFile;
import com.prupe.mcpatcher.mal.resource.TexturePackAPI;
import com.prupe.mcpatcher.mal.util.WeightedIndex;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
class MobRuleList {
   private static final MCLogger logger = MCLogger.getLogger("Random Mobs");
   public static final String ALTERNATIVES_REGEX = "_(eyes|overlay|tame|angry|collar|fur|invulnerable|shooting)\\.properties$";
   private static final Map<FakeResourceLocation, MobRuleList> allRules = new HashMap<>();
   private final FakeResourceLocation baseSkin;
   private final List<FakeResourceLocation> allSkins;
   private final int skinCount;
   private final List<MobRuleList.MobRuleEntry> entries;

   private MobRuleList(FakeResourceLocation baseSkin) {
      this.baseSkin = baseSkin;
      String newPath = baseSkin.getPath().replaceFirst("^textures/entity/", "/mob/");
      FakeResourceLocation newSkin = new FakeResourceLocation(baseSkin.getNamespace(), newPath);
      this.allSkins = new ArrayList<>();
      this.allSkins.add(baseSkin);
      int i = 2;

      while (true) {
         FakeResourceLocation skin = TexturePackAPI.transformResourceLocation(newSkin, ".png", i + ".png");
         if (!TexturePackAPI.hasResource(skin)) {
            this.skinCount = this.allSkins.size();
            if (this.skinCount <= 1) {
               this.entries = null;
               return;
            } else {
               logger.fine("found %d variations for %s", this.skinCount, baseSkin);
               FakeResourceLocation filename = TexturePackAPI.transformResourceLocation(newSkin, ".png", ".properties");
               skin = new FakeResourceLocation(
                  newSkin.getNamespace(),
                  filename.getPath().replaceFirst("_(eyes|overlay|tame|angry|collar|fur|invulnerable|shooting)\\.properties$", ".properties")
               );
               PropertiesFile properties = PropertiesFile.get(logger, filename);
               if (properties == null && !filename.equals(skin)) {
                  properties = PropertiesFile.get(logger, skin);
                  if (properties != null) {
                     logger.fine("using %s for %s", skin, baseSkin);
                  }
               }

               ArrayList<MobRuleList.MobRuleEntry> tmpEntries = new ArrayList<>();
               if (properties != null) {
                  int ix = 0;

                  while (true) {
                     MobRuleList.MobRuleEntry entry = MobRuleList.MobRuleEntry.load(properties, ix, this.skinCount);
                     if (entry == null) {
                        if (ix > 0) {
                           break;
                        }
                     } else {
                        logger.fine("  %s", entry.toString());
                        tmpEntries.add(entry);
                     }

                     ix++;
                  }
               }

               this.entries = tmpEntries.isEmpty() ? null : tmpEntries;
               return;
            }
         }

         this.allSkins.add(skin);
         i++;
      }
   }

   FakeResourceLocation getSkin(long key, int i, int j, int k, Integer biome) {
      if (this.entries == null) {
         int index = (int)(key % this.skinCount);
         if (index < 0) {
            index += this.skinCount;
         }

         return this.allSkins.get(index);
      } else {
         if (j < 0) {
            j = 0;
         }

         for (MobRuleList.MobRuleEntry entry : this.entries) {
            if (entry.match(i, j, k, biome)) {
               int index = entry.weightedIndex.choose(key);
               return this.allSkins.get(entry.skins[index]);
            }
         }

         return this.baseSkin;
      }
   }

   static MobRuleList get(FakeResourceLocation texture) {
      MobRuleList list = allRules.get(texture);
      if (list == null) {
         list = new MobRuleList(texture);
         allRules.put(texture, list);
      }

      return list;
   }

   static void clear() {
      allRules.clear();
   }

   @Environment(EnvType.CLIENT)
   private static class MobRuleEntry {
      final int[] skins;
      final WeightedIndex weightedIndex;
      private final BitSet biomes;
      private final BitSet height;

      static MobRuleList.MobRuleEntry load(PropertiesFile properties, int index, int limit) {
         String skinList = properties.getString("skins." + index, "").toLowerCase();
         int[] skins;
         if (!skinList.equals("*") && !skinList.equals("all") && !skinList.equals("any")) {
            skins = MCPatcherUtils.parseIntegerList(skinList, 1, limit);
            if (skins.length <= 0) {
               return null;
            }

            for (int i = 0; i < skins.length; i++) {
               skins[i]--;
            }
         } else {
            skins = new int[limit];
            int i = 0;

            while (i < skins.length) {
               skins[i] = i++;
            }
         }

         WeightedIndex chooser = WeightedIndex.create(skins.length, properties.getString("weights." + index, ""));
         if (chooser == null) {
            return null;
         } else {
            String biomeList = properties.getString("biomes." + index, "");
            BitSet biomes;
            if (biomeList.isEmpty()) {
               biomes = null;
            } else {
               biomes = new BitSet();
               BiomeAPI.parseBiomeList(biomeList, biomes);
            }

            BitSet height = BiomeAPI.getHeightListProperty(properties, "." + index);
            return new MobRuleList.MobRuleEntry(skins, chooser, biomes, height);
         }
      }

      MobRuleEntry(int[] skins, WeightedIndex weightedIndex, BitSet biomes, BitSet height) {
         this.skins = skins;
         this.weightedIndex = weightedIndex;
         this.biomes = biomes;
         this.height = height;
      }

      boolean match(int i, int j, int k, Integer biome) {
         return this.biomes == null || biome != null && this.biomes.get(biome) ? this.height == null || this.height.get(j) : false;
      }

      @Override
      public String toString() {
         StringBuilder sb = new StringBuilder();
         sb.append("skins:");

         for (int i : this.skins) {
            sb.append(' ').append(i + 1);
         }

         if (this.biomes != null) {
            sb.append(", biomes:");

            for (int i = this.biomes.nextSetBit(0); i >= 0; i = this.biomes.nextSetBit(i + 1)) {
               sb.append(' ').append(i);
            }
         }

         if (this.height != null) {
            sb.append(", height:");

            for (int i = this.height.nextSetBit(0); i >= 0; i = this.height.nextSetBit(i + 1)) {
               sb.append(' ').append(i);
            }
         }

         sb.append(", weights: ").append(this.weightedIndex.toString());
         return sb.toString();
      }
   }
}
