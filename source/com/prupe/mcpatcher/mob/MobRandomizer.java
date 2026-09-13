package com.prupe.mcpatcher.mob;

import com.prupe.mcpatcher.MCLogger;
import com.prupe.mcpatcher.mal.biome.BiomeAPI;
import com.prupe.mcpatcher.mal.resource.FakeResourceLocation;
import com.prupe.mcpatcher.mal.resource.TexturePackChangeHandler;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.NBTTagCompound;

@Environment(EnvType.CLIENT)
public class MobRandomizer {
   private static final MCLogger logger = MCLogger.getLogger("Random Mobs");
   private static final LinkedHashMap<String, FakeResourceLocation> cache = new LinkedHashMap<>();

   static void init() {
   }

   public static FakeResourceLocation randomTexture(EntityLiving entity, FakeResourceLocation texture) {
      if (texture != null && texture.getPath().endsWith(".png")) {
         String key = texture.toString() + ":" + entity.entityId;
         FakeResourceLocation newTexture = cache.get(key);
         if (newTexture == null) {
            MobRandomizer.ExtraInfo info = MobRandomizer.ExtraInfo.getInfo(entity);
            MobRuleList list = MobRuleList.get(texture);
            newTexture = list.getSkin(info.skin, info.origX, info.origY, info.origZ, info.origBiome);
            cache.put(key, newTexture);
            logger.finer("entity %s using %s (cache: %d)", entity, newTexture, cache.size());
            if (cache.size() > 250) {
               while (cache.size() > 200) {
                  cache.remove(cache.keySet().iterator().next());
               }
            }
         }

         return newTexture;
      } else {
         return texture;
      }
   }

   public static FakeResourceLocation randomTexture(Entity entity, FakeResourceLocation texture) {
      return entity instanceof EntityLiving ? randomTexture((EntityLiving)entity, texture) : texture;
   }

   static {
      TexturePackChangeHandler.register(new TexturePackChangeHandler("Random Mobs", 2) {
         @Override
         public void beforeChange() {
            MobRandomizer.cache.clear();
         }

         @Override
         public void afterChange() {
            MobRuleList.clear();
            MobOverlay.reset();
            LineRenderer.reset();
         }
      });
   }

   @Environment(EnvType.CLIENT)
   public static final class ExtraInfo {
      private static final String SKIN_TAG = "randomMobsSkin";
      private static final String ORIG_X_TAG = "origX";
      private static final String ORIG_Y_TAG = "origY";
      private static final String ORIG_Z_TAG = "origZ";
      private static final long MULTIPLIER = 25214903917L;
      private static final long ADDEND = 11L;
      private static final long MASK = 281474976710655L;
      private static final HashMap<Integer, MobRandomizer.ExtraInfo> allInfo = new HashMap<>();
      private static final HashMap<WeakReference<EntityLiving>, MobRandomizer.ExtraInfo> allRefs = new HashMap<>();
      private static final ReferenceQueue<EntityLiving> refQueue = new ReferenceQueue<>();
      private final int entityId;
      private final HashSet<WeakReference<EntityLiving>> references;
      private final long skin;
      private final int origX;
      private final int origY;
      private final int origZ;
      private Integer origBiome;

      ExtraInfo(EntityLiving entity) {
         this(entity, getSkinId(entity.entityId), (int)entity.posX, (int)entity.posY, (int)entity.posZ);
      }

      ExtraInfo(EntityLiving entity, long skin, int origX, int origY, int origZ) {
         this.entityId = entity.entityId;
         this.references = new HashSet<>();
         this.skin = skin;
         this.origX = origX;
         this.origY = origY;
         this.origZ = origZ;
      }

      private void setBiome() {
         if (this.origBiome == null) {
            this.origBiome = BiomeAPI.getBiomeIDAt(BiomeAPI.getWorld(), this.origX, this.origY, this.origZ);
         }
      }

      @Override
      public String toString() {
         return String.format(
            "%s{%d, %d, %d, %d, %d, %s}", this.getClass().getSimpleName(), this.entityId, this.skin, this.origX, this.origY, this.origZ, this.origBiome
         );
      }

      private static void clearUnusedReferences() {
         synchronized (allInfo) {
            Reference<? extends EntityLiving> ref;
            while ((ref = refQueue.poll()) != null) {
               MobRandomizer.ExtraInfo info = allRefs.get(ref);
               if (info != null) {
                  info.references.remove(ref);
                  if (info.references.isEmpty()) {
                     MobRandomizer.logger.finest("removing unused ref %d", info.entityId);
                     allInfo.remove(info.entityId);
                  }
               }

               allRefs.remove(ref);
            }
         }
      }

      static MobRandomizer.ExtraInfo getInfo(EntityLiving entity) {
         synchronized (allInfo) {
            clearUnusedReferences();
            MobRandomizer.ExtraInfo info = allInfo.get(entity.entityId);
            if (info == null) {
               info = new MobRandomizer.ExtraInfo(entity);
               putInfo(entity, info);
            }

            boolean found = false;

            for (WeakReference<EntityLiving> ref : info.references) {
               if (ref.get() == entity) {
                  found = true;
                  break;
               }
            }

            if (!found) {
               WeakReference<EntityLiving> reference = new WeakReference<>(entity, refQueue);
               info.references.add(reference);
               allRefs.put(reference, info);
               MobRandomizer.logger.finest("added ref #%d for %d (%d entities)", info.references.size(), entity.entityId, allInfo.size());
            }

            info.setBiome();
            return info;
         }
      }

      static void putInfo(EntityLiving entity, MobRandomizer.ExtraInfo info) {
         synchronized (allInfo) {
            allInfo.put(entity.entityId, info);
         }
      }

      static void clearInfo() {
         synchronized (allInfo) {
            allInfo.clear();
         }
      }

      private static long getSkinId(int entityId) {
         long n = entityId;
         n = n ^ n << 16 ^ n << 32 ^ n << 48;
         n = 25214903917L * n + 11L;
         n = 25214903917L * n + 11L;
         n &= 281474976710655L;
         return n >> 32 ^ n;
      }

      public static void readFromNBT(EntityLiving entity, NBTTagCompound nbt) {
         long skin = nbt.getLong("randomMobsSkin");
         if (skin != 0L) {
            int x = nbt.getInteger("origX");
            int y = nbt.getInteger("origY");
            int z = nbt.getInteger("origZ");
            putInfo(entity, new MobRandomizer.ExtraInfo(entity, skin, x, y, z));
         }
      }

      public static void writeToNBT(EntityLiving entity, NBTTagCompound nbt) {
         synchronized (allInfo) {
            MobRandomizer.ExtraInfo info = allInfo.get(entity.entityId);
            if (info != null) {
               nbt.setLong("randomMobsSkin", info.skin);
               nbt.setInteger("origX", info.origX);
               nbt.setInteger("origY", info.origY);
               nbt.setInteger("origZ", info.origZ);
            }
         }
      }
   }
}
