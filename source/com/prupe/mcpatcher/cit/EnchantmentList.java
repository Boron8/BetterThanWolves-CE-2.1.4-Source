package com.prupe.mcpatcher.cit;

import com.prupe.mcpatcher.MCLogger;
import com.prupe.mcpatcher.mal.resource.PropertiesFile;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;

@Environment(EnvType.CLIENT)
final class EnchantmentList {
   private static final MCLogger logger = MCLogger.getLogger("Custom Item Textures", "CIT");
   private static final float PI = (float) Math.PI;
   private static EnchantmentList.LayerMethod applyMethod;
   private static int limit;
   private static float fade;
   private final List<EnchantmentList.Layer> layers = new ArrayList<>();

   static void setProperties(PropertiesFile properties) {
      applyMethod = new EnchantmentList.Average();
      limit = 99;
      fade = 0.5F;
      if (properties != null) {
         String value = properties.getString("method", "average").toLowerCase();
         if (value.equals("layered")) {
            applyMethod = new EnchantmentList.Layered();
         } else if (value.equals("cycle")) {
            applyMethod = new EnchantmentList.Cycle();
         } else if (!value.equals("average")) {
            logger.warning("%s: unknown enchantment layering method '%s'", "cit.properties", value);
         }

         limit = Math.max(properties.getInt("cap", limit), 0);
         fade = Math.max(properties.getFloat("fade", fade), 0.0F);
      }
   }

   EnchantmentList(Map<Item, List<Enchantment>> enchantments, List<Enchantment> allItemEnchantments, ItemStack itemStack) {
      BitSet layersPresent = new BitSet();
      Map<Integer, EnchantmentList.Layer> tmpLayers = new HashMap<>();
      Item item = itemStack.getItem();
      int[] enchantmentLevels = CITUtils.getEnchantmentLevels(item, itemStack.getTagCompound());
      boolean hasEffect = itemStack.hasEffect();
      List<Enchantment> list = enchantments.get(item);
      if (list == null) {
         list = allItemEnchantments;
      }

      for (Enchantment enchantment : list) {
         if (enchantment.match(itemStack, enchantmentLevels, hasEffect)) {
            int level = Math.max(enchantment.lastEnchantmentLevel, 1);
            int layer = enchantment.layer;
            if (!layersPresent.get(layer)) {
               EnchantmentList.Layer newLayer = new EnchantmentList.Layer(enchantment, level);
               tmpLayers.put(layer, newLayer);
               layersPresent.set(layer);
            }
         }
      }

      if (!layersPresent.isEmpty()) {
         while (layersPresent.cardinality() > limit) {
            int layer = layersPresent.nextSetBit(0);
            layersPresent.clear(layer);
            tmpLayers.remove(layer);
         }

         for (int i = layersPresent.nextSetBit(0); i >= 0; i = layersPresent.nextSetBit(i + 1)) {
            this.layers.add(tmpLayers.get(i));
         }

         applyMethod.computeIntensities(this);
      }
   }

   boolean isEmpty() {
      return this.layers.isEmpty();
   }

   int size() {
      return this.layers.size();
   }

   Enchantment getEnchantment(int index) {
      return this.layers.get(index).enchantment;
   }

   float getIntensity(int index) {
      return this.layers.get(index).intensity;
   }

   @Environment(EnvType.CLIENT)
   private static final class Average extends EnchantmentList.LayerMethod {
      private Average() {
      }

      @Override
      void computeIntensities(EnchantmentList enchantments) {
         int total = 0;

         for (EnchantmentList.Layer layer : enchantments.layers) {
            if (layer.enchantment.blendMethod.canFade()) {
               total += layer.level;
            }
         }

         this.scaleIntensities(enchantments, total);
      }
   }

   @Environment(EnvType.CLIENT)
   private static final class Cycle extends EnchantmentList.LayerMethod {
      private Cycle() {
      }

      @Override
      void computeIntensities(EnchantmentList enchantments) {
         float total = 0.0F;

         for (EnchantmentList.Layer layer : enchantments.layers) {
            if (layer.enchantment.blendMethod.canFade()) {
               total += layer.getEffectiveDuration();
            }
         }

         float timestamp = (float)(System.currentTimeMillis() / 1000.0 % total);

         for (EnchantmentList.Layer layerx : enchantments.layers) {
            if (!layerx.enchantment.blendMethod.canFade()) {
               layerx.intensity = layerx.level > 0 ? 1.0F : 0.0F;
            } else {
               if (timestamp <= 0.0F) {
                  break;
               }

               float duration = layerx.getEffectiveDuration();
               if (timestamp < duration) {
                  float denominator = (float)Math.sin((float) Math.PI * EnchantmentList.fade / duration);
                  layerx.intensity = (float)(Math.sin((float) Math.PI * timestamp / duration) / (denominator == 0.0F ? 1.0F : denominator));
               }

               timestamp -= duration;
            }
         }
      }
   }

   @Environment(EnvType.CLIENT)
   private static final class Layer {
      final Enchantment enchantment;
      final int level;
      float intensity;

      Layer(Enchantment enchantment, int level) {
         this.enchantment = enchantment;
         this.level = level;
      }

      float getEffectiveDuration() {
         return this.enchantment.duration + 2.0F * EnchantmentList.fade;
      }
   }

   @Environment(EnvType.CLIENT)
   private abstract static class LayerMethod {
      private LayerMethod() {
      }

      abstract void computeIntensities(EnchantmentList var1);

      protected void scaleIntensities(EnchantmentList enchantments, int denominator) {
         if (denominator > 0) {
            for (EnchantmentList.Layer layer : enchantments.layers) {
               if (layer.enchantment.blendMethod.canFade()) {
                  layer.intensity = (float)layer.level / denominator;
               } else {
                  layer.intensity = layer.level > 0 ? 1.0F : 0.0F;
               }
            }
         } else {
            for (EnchantmentList.Layer layerx : enchantments.layers) {
               layerx.intensity = layerx.level > 0 ? 1.0F : 0.0F;
            }
         }
      }
   }

   @Environment(EnvType.CLIENT)
   private static final class Layered extends EnchantmentList.LayerMethod {
      private Layered() {
      }

      @Override
      void computeIntensities(EnchantmentList enchantments) {
         int max = 0;

         for (EnchantmentList.Layer layer : enchantments.layers) {
            if (layer.enchantment.blendMethod.canFade()) {
               Math.max(max, layer.level);
            }
         }

         this.scaleIntensities(enchantments, max);
      }
   }
}
