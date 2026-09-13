package com.prupe.mcpatcher.cit;

import com.prupe.mcpatcher.Config;
import com.prupe.mcpatcher.MCLogger;
import com.prupe.mcpatcher.mal.item.ItemAPI;
import com.prupe.mcpatcher.mal.resource.FakeResourceLocation;
import com.prupe.mcpatcher.mal.resource.PropertiesFile;
import com.prupe.mcpatcher.mal.resource.ResourceList;
import com.prupe.mcpatcher.mal.resource.TexturePackAPI;
import com.prupe.mcpatcher.mal.resource.TexturePackChangeHandler;
import com.prupe.mcpatcher.mal.tessellator.TessellatorAPI;
import com.prupe.mcpatcher.mal.tile.IconAPI;
import com.prupe.mcpatcher.mal.tile.TileLoader;
import java.awt.image.BufferedImage;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityPotion;
import net.minecraft.src.Icon;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTBase;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.NBTTagList;

@Environment(EnvType.CLIENT)
public class CITUtils {
   private static final MCLogger logger = MCLogger.getLogger("Custom Item Textures", "CIT");
   static final String CIT_PROPERTIES = "cit.properties";
   private static final FakeResourceLocation CIT_PROPERTIES1 = TexturePackAPI.newMCPatcherResourceLocation("cit.properties");
   private static final FakeResourceLocation CIT_PROPERTIES2 = TexturePackAPI.newMCPatcherResourceLocation("cit/cit.properties");
   static final FakeResourceLocation FIXED_ARMOR_RESOURCE = new FakeResourceLocation("textures/models/armor/iron_layer_1.png");
   static final int MAX_ENCHANTMENTS = 256;
   private static Item itemEnchantedBook;
   static Item itemCompass;
   static Item itemClock;
   static final boolean enableItems = Config.getBoolean("Custom Item Textures", "items", true);
   static final boolean enableEnchantments = Config.getBoolean("Custom Item Textures", "enchantments", true);
   static final boolean enableArmor = Config.getBoolean("Custom Item Textures", "armor", true);
   private static TileLoader tileLoader;
   private static final Map<Item, List<ItemOverride>> items = new IdentityHashMap<>();
   private static final Map<Item, List<Enchantment>> enchantments = new IdentityHashMap<>();
   private static final List<Enchantment> allItemEnchantments = new ArrayList<>();
   private static final Map<Item, List<ArmorOverride>> armors = new IdentityHashMap<>();
   static boolean useGlint;
   private static EnchantmentList armorMatches;
   private static int armorMatchIndex;
   private static ItemStack lastItemStack;
   private static int lastRenderPass;
   static Icon lastOrigIcon;
   private static Icon lastIcon;
   private static Field potionItemStackField;

   public static void init() {
   }

   public static Icon getIcon(Icon icon, ItemStack itemStack, int renderPass) {
      if (icon == lastIcon && itemStack == lastItemStack && renderPass == lastRenderPass) {
         return icon;
      } else {
         lastIcon = icon;
         lastOrigIcon = icon;
         lastItemStack = itemStack;
         lastRenderPass = renderPass;
         if (enableItems) {
            ItemOverride override = findItemOverride(itemStack);
            if (override != null) {
               Icon newIcon = override.getReplacementIcon(icon);
               if (newIcon != null) {
                  lastIcon = newIcon;
               }
            }
         }

         return lastIcon;
      }
   }

   public static Icon getEntityIcon(Icon icon, Entity entity) {
      if (entity instanceof EntityPotion && potionItemStackField != null) {
         try {
            return getIcon(icon, (ItemStack)potionItemStackField.get(entity), 1);
         } catch (IllegalAccessException var3) {
            var3.printStackTrace();
            potionItemStackField = null;
         }
      }

      return icon;
   }

   public static FakeResourceLocation getArmorTexture(FakeResourceLocation texture, EntityLiving entity, ItemStack itemStack) {
      if (enableArmor) {
         ArmorOverride override = findArmorOverride(itemStack);
         if (override != null) {
            FakeResourceLocation newTexture = override.getReplacementTexture(texture);
            if (newTexture != null) {
               return newTexture;
            }
         }
      }

      return texture;
   }

   private static <T extends OverrideBase> T findMatch(Map<Item, List<T>> overrides, ItemStack itemStack) {
      Item item = itemStack.getItem();
      List<T> list = overrides.get(item);
      if (list != null) {
         int[] enchantmentLevels = getEnchantmentLevels(item, itemStack.getTagCompound());
         boolean hasEffect = itemStack.hasEffect();

         for (T override : list) {
            if (override.match(itemStack, enchantmentLevels, hasEffect)) {
               return override;
            }
         }
      }

      return null;
   }

   static ItemOverride findItemOverride(ItemStack itemStack) {
      return findMatch(items, itemStack);
   }

   static ArmorOverride findArmorOverride(ItemStack itemStack) {
      return findMatch(armors, itemStack);
   }

   static EnchantmentList findEnchantments(ItemStack itemStack) {
      return new EnchantmentList(enchantments, allItemEnchantments, itemStack);
   }

   public static boolean renderEnchantmentHeld(ItemStack itemStack, int renderPass) {
      if (itemStack == null || renderPass != 0) {
         return true;
      } else if (!enableEnchantments) {
         return false;
      } else {
         EnchantmentList matches = findEnchantments(itemStack);
         if (matches.isEmpty()) {
            return !useGlint;
         } else {
            int width;
            int height;
            if (lastIcon == null) {
               height = 256;
               width = 256;
            } else {
               width = IconAPI.getIconWidth(lastIcon);
               height = IconAPI.getIconHeight(lastIcon);
            }

            Enchantment.beginOuter3D();

            for (int i = 0; i < matches.size(); i++) {
               matches.getEnchantment(i).render3D(TessellatorAPI.getTessellator(), matches.getIntensity(i), width, height);
            }

            Enchantment.endOuter3D();
            return !useGlint;
         }
      }
   }

   public static boolean renderEnchantmentDropped(ItemStack itemStack) {
      return renderEnchantmentHeld(itemStack, lastRenderPass);
   }

   public static boolean renderEnchantmentGUI(ItemStack itemStack, int x, int y, float z) {
      if (enableEnchantments && itemStack != null) {
         EnchantmentList matches = findEnchantments(itemStack);
         if (matches.isEmpty()) {
            return !useGlint;
         } else {
            Enchantment.beginOuter2D();

            for (int i = 0; i < matches.size(); i++) {
               matches.getEnchantment(i).render2D(TessellatorAPI.getTessellator(), matches.getIntensity(i), x, y, x + 16, y + 16, z);
            }

            Enchantment.endOuter2D();
            return !useGlint;
         }
      } else {
         return false;
      }
   }

   public static boolean setupArmorEnchantments(EntityLiving entity, int pass) {
      return setupArmorEnchantments(entity.getCurrentItemOrArmor(4 - pass));
   }

   public static boolean setupArmorEnchantments(ItemStack itemStack) {
      armorMatches = null;
      armorMatchIndex = 0;
      if (enableEnchantments && itemStack != null) {
         EnchantmentList tmpList = findEnchantments(itemStack);
         if (!tmpList.isEmpty()) {
            armorMatches = tmpList;
         }
      }

      return isArmorEnchantmentActive() || !useGlint;
   }

   public static boolean preRenderArmorEnchantment() {
      if (isArmorEnchantmentActive()) {
         Enchantment enchantment = armorMatches.getEnchantment(armorMatchIndex);
         if (enchantment.bindTexture(lastOrigIcon)) {
            enchantment.beginArmor(armorMatches.getIntensity(armorMatchIndex));
            return true;
         } else {
            return false;
         }
      } else {
         armorMatches = null;
         armorMatchIndex = 0;
         return false;
      }
   }

   public static boolean isArmorEnchantmentActive() {
      return armorMatches != null && armorMatchIndex < armorMatches.size();
   }

   public static void postRenderArmorEnchantment() {
      armorMatches.getEnchantment(armorMatchIndex).endArmor();
      armorMatchIndex++;
   }

   static int[] getEnchantmentLevels(Item item, NBTTagCompound nbt) {
      int[] levels = null;
      if (nbt != null) {
         NBTBase base;
         if (item == itemEnchantedBook) {
            base = nbt.getTag("StoredEnchantments");
         } else {
            base = nbt.getTag("ench");
         }

         if (base instanceof NBTTagList) {
            NBTTagList list = (NBTTagList)base;

            for (int i = 0; i < list.tagCount(); i++) {
               base = list.tagAt(i);
               if (base instanceof NBTTagCompound) {
                  short id = ((NBTTagCompound)base).getShort("id");
                  short level = ((NBTTagCompound)base).getShort("lvl");
                  if (id >= 0 && id < 256 && level > 0) {
                     if (levels == null) {
                        levels = new int[256];
                     }

                     levels[id] += level;
                  }
               }
            }
         }
      }

      return levels;
   }

   static {
      for (Field f : EntityPotion.class.getDeclaredFields()) {
         if (ItemStack.class.isAssignableFrom(f.getType())) {
            f.setAccessible(true);
            potionItemStackField = f;
            break;
         }
      }

      TexturePackChangeHandler.register(new TexturePackChangeHandler("Custom Item Textures", 3) {
         @Override
         public void beforeChange() {
            CITUtils.itemEnchantedBook = ItemAPI.getFixedItem("minecraft:enchanted_book");
            CITUtils.itemCompass = ItemAPI.getFixedItem("minecraft:compass");
            CITUtils.itemClock = ItemAPI.getFixedItem("minecraft:clock");
            CITUtils.tileLoader = new TileLoader("textures/items", CITUtils.logger);
            CITUtils.items.clear();
            CITUtils.enchantments.clear();
            CITUtils.allItemEnchantments.clear();
            CITUtils.armors.clear();
            CITUtils.lastOrigIcon = null;
            CITUtils.lastIcon = null;
            BufferedImage image = TexturePackAPI.getImage(CITUtils.FIXED_ARMOR_RESOURCE);
            if (image == null) {
               Enchantment.baseArmorWidth = 64.0F;
               Enchantment.baseArmorHeight = 32.0F;
            } else {
               Enchantment.baseArmorWidth = image.getWidth();
               Enchantment.baseArmorHeight = image.getHeight();
            }

            PropertiesFile properties = PropertiesFile.get(CITUtils.logger, CITUtils.CIT_PROPERTIES1);
            if (properties == null) {
               properties = PropertiesFile.getNonNull(CITUtils.logger, CITUtils.CIT_PROPERTIES2);
            }

            CITUtils.useGlint = properties.getBoolean("useGlint", true);
            EnchantmentList.setProperties(properties);
            if (CITUtils.enableItems || CITUtils.enableEnchantments || CITUtils.enableArmor) {
               for (FakeResourceLocation resource : ResourceList.getInstance().listResources("/cit", ".properties", true)) {
                  this.registerOverride(OverrideBase.create(resource));
               }

               if (CITUtils.enableItems) {
                  PotionReplacer replacer = new PotionReplacer();

                  for (ItemOverride override : replacer.overrides) {
                     this.registerOverride(override);
                  }
               }
            }
         }

         @Override
         public void afterChange() {
            for (List<ItemOverride> list : CITUtils.items.values()) {
               for (OverrideBase override : list) {
                  ((ItemOverride)override).registerIcon(CITUtils.tileLoader);
               }

               Collections.sort(list);
            }

            for (List<Enchantment> list : CITUtils.enchantments.values()) {
               list.addAll(CITUtils.allItemEnchantments);
               Collections.sort(list);
            }

            Collections.sort(CITUtils.allItemEnchantments);

            for (List<ArmorOverride> list : CITUtils.armors.values()) {
               Collections.sort(list);
            }
         }

         private void registerOverride(OverrideBase override) {
            if (override != null && override.properties.valid()) {
               Map map;
               if (override instanceof ItemOverride) {
                  ((ItemOverride)override).preload(CITUtils.tileLoader);
                  map = CITUtils.items;
               } else if (override instanceof Enchantment) {
                  map = CITUtils.enchantments;
               } else {
                  if (!(override instanceof ArmorOverride)) {
                     CITUtils.logger.severe("unknown ItemOverride type %d", override.getClass().getName());
                     return;
                  }

                  map = CITUtils.armors;
               }

               if (override.items == null) {
                  if (override instanceof Enchantment) {
                     CITUtils.logger.fine("registered %s to all items", override);
                     CITUtils.allItemEnchantments.add((Enchantment)override);
                  }
               } else {
                  int i = 0;

                  for (Item item : override.items) {
                     this.registerOverride(map, item, override);
                     if (i < 10) {
                        CITUtils.logger.fine("registered %s to item %s", override, ItemAPI.getItemName(item));
                     } else if (i == 10) {
                        CITUtils.logger.fine("... %d total", override.items.size());
                     }

                     i++;
                  }
               }
            }
         }

         private void registerOverride(Map<Item, List<OverrideBase>> map, Item item, OverrideBase override) {
            List<OverrideBase> list = map.get(item);
            if (list == null) {
               list = new ArrayList<>();
               map.put(item, list);
            }

            list.add(override);
         }
      });
   }
}
