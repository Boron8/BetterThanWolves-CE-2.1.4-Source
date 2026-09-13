package com.prupe.mcpatcher.cit;

import com.prupe.mcpatcher.MCLogger;
import com.prupe.mcpatcher.MCPatcherUtils;
import com.prupe.mcpatcher.mal.item.ItemAPI;
import com.prupe.mcpatcher.mal.nbt.NBTRule;
import com.prupe.mcpatcher.mal.resource.FakeResourceLocation;
import com.prupe.mcpatcher.mal.resource.PropertiesFile;
import com.prupe.mcpatcher.mal.resource.TexturePackAPI;
import com.prupe.mcpatcher.mal.tile.TileLoader;
import java.io.File;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;

@Environment(EnvType.CLIENT)
abstract class OverrideBase implements Comparable<OverrideBase> {
   static final MCLogger logger = MCLogger.getLogger("Custom Item Textures", "CIT");
   private static final int MAX_DAMAGE = 65535;
   private static final int MAX_STACK_SIZE = 65535;
   final PropertiesFile properties;
   final FakeResourceLocation textureName;
   final Map<String, FakeResourceLocation> alternateTextures;
   final int weight;
   final Set<Item> items;
   final BitSet damagePercent;
   final BitSet damage;
   final int damageMask;
   final BitSet stackSize;
   final BitSet enchantmentIDs;
   final BitSet enchantmentLevels;
   private final List<NBTRule> nbtRules = new ArrayList<>();
   int lastEnchantmentLevel;

   static OverrideBase create(FakeResourceLocation filename) {
      if (new File(filename.getPath()).getName().equals("cit.properties")) {
         return null;
      } else {
         PropertiesFile properties = PropertiesFile.get(logger, filename);
         if (properties == null) {
            return null;
         } else {
            String type = properties.getString("type", "item").toLowerCase();
            OverrideBase override;
            if (type.equals("item")) {
               if (!CITUtils.enableItems) {
                  return null;
               }

               override = new ItemOverride(properties);
            } else if (!type.equals("enchantment") && !type.equals("overlay")) {
               if (!type.equals("armor")) {
                  logger.error("%s: unknown type '%s'", filename, type);
                  return null;
               }

               if (!CITUtils.enableArmor) {
                  return null;
               }

               override = new ArmorOverride(properties);
            } else {
               if (!CITUtils.enableEnchantments) {
                  return null;
               }

               override = new Enchantment(properties);
            }

            return override.properties.valid() ? override : null;
         }
      }
   }

   OverrideBase(PropertiesFile properties) {
      this.properties = properties;
      this.alternateTextures = this.getAlternateTextures();
      String value = properties.getString("source", "");
      FakeResourceLocation resource = null;
      if (value.equals("")) {
         value = properties.getString("texture", "");
      }

      if (value.equals("")) {
         value = properties.getString("tile", "");
      }

      if (value.equals("")) {
         if (MCPatcherUtils.isNullOrEmpty(this.alternateTextures)) {
            resource = TileLoader.getDefaultAddress(properties.getResource());
            if (!TexturePackAPI.hasResource(resource)) {
               resource = null;
            }
         }
      } else {
         resource = TileLoader.parseTileAddress(properties.getResource(), value);
         if (!TexturePackAPI.hasResource(resource)) {
            properties.error("source texture %s not found", value);
            resource = null;
         }
      }

      this.textureName = resource;
      this.weight = properties.getInt("weight", 0);
      value = properties.getString("items", "");
      if (value.equals("")) {
         value = properties.getString("matchItems", "");
      }

      if (value.equals("")) {
         this.items = null;
      } else {
         this.items = new HashSet<>();

         for (String s : value.split("\\s+")) {
            Item item = ItemAPI.parseItemName(s);
            if (item != null) {
               this.items.add(item);
            }
         }
      }

      value = properties.getString("damage", "");
      if (value.equals("")) {
         this.damage = null;
         this.damagePercent = null;
      } else if (value.contains("%")) {
         this.damage = null;
         this.damagePercent = parseBitSet(value.replace("%", ""), 0, 100);
      } else {
         this.damage = parseBitSet(value, 0, 65535);
         this.damagePercent = null;
      }

      this.damageMask = properties.getInt("damageMask", 65535);
      this.stackSize = parseBitSet(properties, "stackSize", 0, 65535);
      this.enchantmentIDs = parseBitSet(properties, "enchantmentIDs", 0, 255);
      this.enchantmentLevels = parseBitSet(properties, "enchantmentLevels", 0, 255);

      for (Entry<String, String> entry : properties.entrySet()) {
         String name = entry.getKey();
         if (name.startsWith("nbt.")) {
            value = entry.getValue();
            NBTRule rule = NBTRule.create(name, value);
            if (rule == null) {
               properties.error("invalid nbt rule: %s", value);
            } else {
               this.nbtRules.add(rule);
            }
         }
      }
   }

   public int compareTo(OverrideBase o) {
      int result = o.weight - this.weight;
      return result != 0 ? result : this.properties.getResource().toString().compareTo(o.properties.getResource().toString());
   }

   boolean match(ItemStack itemStack, int[] itemEnchantmentLevels, boolean hasEffect) {
      return this.matchDamage(itemStack)
         && this.matchDamagePercent(itemStack)
         && this.matchStackSize(itemStack)
         && this.matchEnchantment(itemEnchantmentLevels, hasEffect)
         && this.matchNBT(itemStack);
   }

   String preprocessAltTextureKey(String name) {
      return name;
   }

   private Map<String, FakeResourceLocation> getAlternateTextures() {
      Map<String, FakeResourceLocation> tmpMap = new HashMap<>();

      for (Entry<String, String> entry : this.properties.entrySet()) {
         String key = entry.getKey();
         String value = entry.getValue();
         String name;
         if (key.startsWith("source.")) {
            name = key.substring(7);
         } else if (key.startsWith("texture.")) {
            name = key.substring(8);
         } else {
            if (!key.startsWith("tile.")) {
               continue;
            }

            name = key.substring(5);
         }

         name = this.preprocessAltTextureKey(name);
         if (!MCPatcherUtils.isNullOrEmpty(name)) {
            FakeResourceLocation resource = TileLoader.parseTileAddress(this.properties.getResource(), value);
            if (resource != null) {
               tmpMap.put(name, resource);
            }
         }
      }

      return tmpMap.isEmpty() ? null : tmpMap;
   }

   private boolean matchDamage(ItemStack itemStack) {
      return this.damage == null || this.damage.get(itemStack.getItemDamage() & this.damageMask);
   }

   private boolean matchDamagePercent(ItemStack itemStack) {
      if (this.damagePercent == null) {
         return true;
      } else {
         int maxDamage = itemStack.getMaxDamage();
         if (maxDamage == 0) {
            return false;
         } else {
            int percent = 100 * itemStack.getItemDamage() / maxDamage;
            if (percent < 0) {
               percent = 0;
            } else if (percent > 100) {
               percent = 100;
            }

            return this.damagePercent.get(percent);
         }
      }
   }

   private boolean matchStackSize(ItemStack itemStack) {
      return this.stackSize == null || this.stackSize.get(itemStack.stackSize);
   }

   private boolean matchEnchantment(int[] itemEnchantmentLevels, boolean hasEffect) {
      if (this.enchantmentLevels == null && this.enchantmentIDs == null) {
         return true;
      } else {
         return itemEnchantmentLevels == null
            ? (this.lastEnchantmentLevel = this.getEnchantmentLevelMatch(hasEffect)) >= 0
            : (this.lastEnchantmentLevel = this.getEnchantmentLevelMatch(itemEnchantmentLevels)) >= 0;
      }
   }

   private int getEnchantmentLevelMatch(boolean hasEffect) {
      return hasEffect && this.enchantmentIDs == null && this.enchantmentLevels.get(1) ? 1 : -1;
   }

   private int getEnchantmentLevelMatch(int[] itemEnchantmentLevels) {
      int matchLevel = -1;
      if (this.enchantmentIDs == null) {
         int sum = 0;

         for (int level : itemEnchantmentLevels) {
            sum += level;
         }

         if (this.enchantmentLevels.get(sum)) {
            return sum;
         }
      } else if (this.enchantmentLevels == null) {
         for (int id = this.enchantmentIDs.nextSetBit(0); id >= 0; id = this.enchantmentIDs.nextSetBit(id + 1)) {
            if (itemEnchantmentLevels[id] > 0) {
               matchLevel = Math.max(matchLevel, itemEnchantmentLevels[id]);
            }
         }
      } else {
         for (int idx = this.enchantmentIDs.nextSetBit(0); idx >= 0; idx = this.enchantmentIDs.nextSetBit(idx + 1)) {
            if (this.enchantmentLevels.get(itemEnchantmentLevels[idx])) {
               matchLevel = Math.max(matchLevel, itemEnchantmentLevels[idx]);
            }
         }
      }

      return matchLevel;
   }

   private boolean matchNBT(ItemStack itemStack) {
      for (NBTRule rule : this.nbtRules) {
         if (!rule.match(itemStack.getTagCompound())) {
            return false;
         }
      }

      return true;
   }

   abstract String getType();

   @Override
   public String toString() {
      return String.format("ItemOverride{%s, %s, %s}", this.getType(), this.properties, this.textureName);
   }

   private static BitSet parseBitSet(PropertiesFile properties, String tag, int min, int max) {
      String value = properties.getString(tag, "");
      return parseBitSet(value, min, max);
   }

   private static BitSet parseBitSet(String value, int min, int max) {
      if (value.equals("")) {
         return null;
      } else {
         BitSet bits = new BitSet();

         for (int i : MCPatcherUtils.parseIntegerList(value, min, max)) {
            bits.set(i);
         }

         return bits;
      }
   }
}
