package net.minecraft.src;

import btw.client.fx.particles.CindersFX;
import btw.client.fx.particles.SmallFlameFX;
import btw.client.fx.particles.WhiteCloudFX;
import btw.client.fx.particles.WhiteSmokeFX;
import btw.entity.LightningBoltEntity;
import btw.entity.mob.villager.VillagerEntity;
import btw.util.ReflectionUtils;
import com.prupe.mcpatcher.cc.ColorizeItem;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import net.minecraft.server.MinecraftServer;

public class EntityList {
   private static Map stringToClassMapping = new HashMap();
   private static Map classToStringMapping = new HashMap();
   private static Map IDtoClassMapping = new HashMap();
   private static Map classToIDMapping = new HashMap();
   private static Map stringToIDMapping = new HashMap();
   public static HashMap entityEggs = new LinkedHashMap();

   public static void addMapping(Class par0Class, String par1Str, int par2) {
      stringToClassMapping.put(par1Str, par0Class);
      classToStringMapping.put(par0Class, par1Str);
      IDtoClassMapping.put(par2, par0Class);
      classToIDMapping.put(par0Class, par2);
      stringToIDMapping.put(par1Str, par2);
   }

   public static void addMapping(Class par0Class, String par1Str, int par2, int par3, int par4) {
      if (!MinecraftServer.getIsServer()) {
         ColorizeItem.setupSpawnerEgg(par1Str, par2, par3, par4);
      }

      addMapping(par0Class, par1Str, par2);
      entityEggs.put(par2, new EntityEggInfo(par2, par3, par4));
   }

   public static Entity createEntityByName(String par0Str, World par1World) {
      Entity var2 = null;

      try {
         Class var3 = (Class)stringToClassMapping.get(par0Str);
         if (var3 != null) {
            if (VillagerEntity.class.equals(var3)) {
               var2 = VillagerEntity.createVillager(par1World);
            } else {
               var3 = getRegisteredReplacement(var3);
               var2 = (Entity)var3.getConstructor(World.class).newInstance(par1World);
            }
         }
      } catch (Exception var41) {
         var41.printStackTrace();
      }

      return var2;
   }

   public static Entity createEntityFromNBT(NBTTagCompound par0NBTTagCompound, World par1World) {
      Entity var2 = null;
      if ("Minecart".equals(par0NBTTagCompound.getString("id"))) {
         switch (par0NBTTagCompound.getInteger("Type")) {
            case 0:
               par0NBTTagCompound.setString("id", "MinecartRideable");
               break;
            case 1:
               par0NBTTagCompound.setString("id", "MinecartChest");
               break;
            case 2:
               par0NBTTagCompound.setString("id", "MinecartFurnace");
         }

         par0NBTTagCompound.removeTag("Type");
      }

      try {
         Class var3 = (Class)stringToClassMapping.get(par0NBTTagCompound.getString("id"));
         if (var3 != null) {
            if (VillagerEntity.class.equals(var3)) {
               int profession = par0NBTTagCompound.getInteger("Profession");
               var2 = VillagerEntity.createVillagerFromProfession(par1World, profession);
            } else {
               if (EntityPlayerMP.class.equals(var3)) {
                  return null;
               }

               var3 = getRegisteredReplacement(var3);
               var2 = (Entity)var3.getConstructor(World.class).newInstance(par1World);
            }
         }
      } catch (Exception var5) {
         var5.printStackTrace();
      }

      if (var2 != null) {
         var2.readFromNBT(par0NBTTagCompound);
      } else {
         par1World.getWorldLogAgent().logWarning("Skipping Entity with id " + par0NBTTagCompound.getString("id"));
      }

      return var2;
   }

   public static Entity createEntityByID(int par0, World par1World) {
      Entity var2 = null;

      try {
         Class var3 = getClassFromID(par0);
         if (var3 != null) {
            if (VillagerEntity.class.equals(var3)) {
               var2 = VillagerEntity.createVillager(par1World);
            } else {
               var3 = getRegisteredReplacement(var3);
               var2 = (Entity)var3.getConstructor(World.class).newInstance(par1World);
            }
         }
      } catch (Exception var41) {
         var41.printStackTrace();
      }

      if (var2 == null) {
         par1World.getWorldLogAgent().logWarning("Skipping Entity with id " + par0);
      }

      return var2;
   }

   public static int getEntityID(Entity par0Entity) {
      Class var1 = par0Entity.getClass();
      if (classToIDMapping.containsKey(var1)) {
         return (Integer)classToIDMapping.get(var1);
      } else {
         throw new IllegalStateException("Entity class missing ID! Class: " + var1);
      }
   }

   public static Class getClassFromID(int par0) {
      return (Class)IDtoClassMapping.get(par0);
   }

   public static String getEntityString(Entity par0Entity) {
      return (String)classToStringMapping.get(par0Entity.getClass());
   }

   public static String getStringFromID(int par0) {
      Class var1 = getClassFromID(par0);
      return var1 != null ? (String)classToStringMapping.get(var1) : null;
   }

   public static boolean removeMapping(String sName, boolean bRemoveEgg) {
      Integer iID = (Integer)stringToIDMapping.get(sName);
      if (iID != null) {
         Class mappedClass = (Class)IDtoClassMapping.get(iID);
         if (mappedClass != null) {
            stringToClassMapping.remove(sName);
            classToStringMapping.remove(mappedClass);
            IDtoClassMapping.remove(iID);
            classToIDMapping.remove(mappedClass);
            stringToIDMapping.remove(sName);
            if (bRemoveEgg) {
               entityEggs.remove(iID);
            }

            return true;
         }
      }

      return false;
   }

   @Deprecated
   public static boolean replaceExistingMapping(Class newClass, String sName) {
      Integer iID = (Integer)stringToIDMapping.get(sName);
      if (iID != null && removeMapping(sName, false)) {
         addMapping(newClass, sName, iID);
      }

      return false;
   }

   @Deprecated
   public static boolean replaceExistingMappingSafe(Class newClass, String entityName) {
      int id = (Integer)stringToIDMapping.get(entityName);
      if (removeMappingSafe(entityName, false)) {
         addMapping(newClass, entityName, id);
      }

      return false;
   }

   public static boolean removeMappingSafe(String var0, boolean var1) {
      Integer var2 = (Integer)stringToIDMapping.get(var0);
      if (var2 != null) {
         Class var3 = (Class)IDtoClassMapping.get(var2);
         if (var3 != null) {
            stringToClassMapping.remove(var0);
            IDtoClassMapping.remove(var2);
            if (var1) {
               entityEggs.remove(var2);
            }

            return true;
         }
      }

      return false;
   }

   public static Entity createEntityOfType(Class<? extends Entity> entityClass, Object... parameters) {
      Entity entity = null;
      Class[] parameterTypes = new Class[parameters.length];
      Object[] parameterValues = new Object[parameters.length];

      for (int i = 0; i < parameters.length; i++) {
         Class<?> type = parameters[i].getClass();
         Class<?> primitiveType = ReflectionUtils.getPrimitiveFromBoxedClass(type);
         if (primitiveType != null) {
            type = primitiveType;
         }

         parameterTypes[i] = type;
         parameterValues[i] = parameters[i];
      }

      entityClass = getRegisteredReplacement(entityClass);
      if (EntityItem.class.isAssignableFrom(entityClass)) {
         for (Object o : parameters) {
            if (o instanceof ItemStack) {
               Item item = ((ItemStack)o).getItem();
               if (item.hasCustomItemEntity()) {
                  entityClass = item.getCustomItemEntity();
               }
               break;
            }
         }
      }

      Constructor constructorToUse = ReflectionUtils.findMatchingConstructor(entityClass, parameterTypes);
      if (constructorToUse != null) {
         try {
            constructorToUse.setAccessible(true);
            entity = (Entity)constructorToUse.newInstance(parameterValues);
         } catch (InstantiationException var11) {
            throw new RuntimeException("A problem has occured attempting to instantiate replacement for " + classToStringMapping.get(entityClass));
         } catch (IllegalArgumentException var12) {
            var12.printStackTrace();
            throw new RuntimeException("Incompatible types passed to specified constructor for " + classToStringMapping.get(entityClass));
         } catch (InvocationTargetException var13) {
            var13.printStackTrace();
            System.out.println("Caused by:");
            var13.getCause().printStackTrace();
         } catch (Exception var14) {
            var14.printStackTrace();
         }

         return entity;
      } else {
         String message = "No appropriate constructor found for " + classToStringMapping.get(entityClass) + ": ";

         for (Class<?> paramType : parameterTypes) {
            message = message + paramType.getSimpleName() + ", ";
         }

         throw new RuntimeException(message);
      }
   }

   public static Class<? extends Entity> getRegisteredReplacement(Class<? extends Entity> entityClass) {
      int id = (Integer)classToIDMapping.get(entityClass);
      return (Class<? extends Entity>)IDtoClassMapping.get(id);
   }

   static {
      addMapping(EntityItem.class, "Item", 1);
      addMapping(EntityXPOrb.class, "XPOrb", 2);
      addMapping(EntityPainting.class, "Painting", 9);
      addMapping(EntityArrow.class, "Arrow", 10);
      addMapping(EntitySnowball.class, "Snowball", 11);
      addMapping(EntityLargeFireball.class, "Fireball", 12);
      addMapping(EntitySmallFireball.class, "SmallFireball", 13);
      addMapping(EntityEnderPearl.class, "ThrownEnderpearl", 14);
      addMapping(EntityEnderEye.class, "EyeOfEnderSignal", 15);
      addMapping(EntityPotion.class, "ThrownPotion", 16);
      addMapping(EntityExpBottle.class, "ThrownExpBottle", 17);
      addMapping(EntityItemFrame.class, "ItemFrame", 18);
      addMapping(EntityWitherSkull.class, "WitherSkull", 19);
      addMapping(EntityTNTPrimed.class, "PrimedTnt", 20);
      addMapping(EntityFallingSand.class, "FallingSand", 21);
      addMapping(EntityFireworkRocket.class, "FireworksRocketEntity", 22);
      addMapping(EntityBoat.class, "Boat", 41);
      addMapping(EntityMinecartEmpty.class, "MinecartRideable", 42);
      addMapping(EntityMinecartChest.class, "MinecartChest", 43);
      addMapping(EntityMinecartFurnace.class, "MinecartFurnace", 44);
      addMapping(EntityMinecartTNT.class, "MinecartTNT", 45);
      addMapping(EntityMinecartHopper.class, "MinecartHopper", 46);
      addMapping(EntityMinecartMobSpawner.class, "MinecartSpawner", 47);
      addMapping(EntityLiving.class, "Mob", 48);
      addMapping(EntityMob.class, "Monster", 49);
      addMapping(EntityCreeper.class, "Creeper", 50, 894731, 0);
      addMapping(EntitySkeleton.class, "Skeleton", 51, 12698049, 4802889);
      addMapping(EntitySpider.class, "Spider", 52, 3419431, 11013646);
      addMapping(EntityGiantZombie.class, "Giant", 53);
      addMapping(EntityZombie.class, "Zombie", 54, 44975, 7969893);
      addMapping(EntitySlime.class, "Slime", 55, 5349438, 8306542);
      addMapping(EntityGhast.class, "Ghast", 56, 16382457, 12369084);
      addMapping(EntityPigZombie.class, "PigZombie", 57, 15373203, 5009705);
      addMapping(EntityEnderman.class, "Enderman", 58, 1447446, 0);
      addMapping(EntityCaveSpider.class, "CaveSpider", 59, 803406, 11013646);
      addMapping(EntitySilverfish.class, "Silverfish", 60, 7237230, 3158064);
      addMapping(EntityBlaze.class, "Blaze", 61, 16167425, 16775294);
      addMapping(EntityMagmaCube.class, "LavaSlime", 62, 3407872, 16579584);
      addMapping(EntityDragon.class, "EnderDragon", 63);
      addMapping(EntityWither.class, "WitherBoss", 64);
      addMapping(EntityBat.class, "Bat", 65, 4996656, 986895);
      addMapping(EntityWitch.class, "Witch", 66, 3407872, 5349438);
      addMapping(EntityPig.class, "Pig", 90, 15771042, 14377823);
      addMapping(EntitySheep.class, "Sheep", 91, 15198183, 16758197);
      addMapping(EntityCow.class, "Cow", 92, 4470310, 10592673);
      addMapping(EntityChicken.class, "Chicken", 93, 10592673, 16711680);
      addMapping(EntitySquid.class, "Squid", 94, 2243405, 7375001);
      addMapping(EntityWolf.class, "Wolf", 95, 14144467, 13545366);
      addMapping(EntityMooshroom.class, "MushroomCow", 96, 10489616, 12040119);
      addMapping(EntitySnowman.class, "SnowMan", 97);
      addMapping(EntityOcelot.class, "Ozelot", 98, 15720061, 5653556);
      addMapping(EntityIronGolem.class, "VillagerGolem", 99);
      addMapping(EntityVillager.class, "Villager", 120, 5651507, 12422002);
      addMapping(EntityEnderCrystal.class, "EnderCrystal", 200);
      int id = -1;
      addMapping(EntityPlayerMP.class, "PlayerMP", id--);
      addMapping(LightningBoltEntity.class, "LightningBolt", id--);
      addMapping(EntityEgg.class, "Egg", id--);
      addMapping(EntitySnowball.class, "Snowball", id--);
      addMapping(EntityFishHook.class, "FishHook", id--);
      addMapping(EntityDragonPart.class, "DragonPart", id--);
      if (!MinecraftServer.getIsServer()) {
         addMapping(EntityClientPlayerMP.class, "ClientPlayerMP", id--);
         addMapping(EntityOtherPlayerMP.class, "OtherPlayerMP", id--);
         addMapping(EntityFireworkRocket.class, "FireworkRocket", id--);
         addMapping(EntityAuraFX.class, "AuraFX", id--);
         addMapping(EntityBreakingFX.class, "BreakingFX", id--);
         addMapping(EntityBubbleFX.class, "BubbleFX", id--);
         addMapping(CindersFX.class, "CindersFX", id--);
         addMapping(EntityCloudFX.class, "CloudFX", id--);
         addMapping(EntityCritFX.class, "CritFX", id--);
         addMapping(EntityCrit2FX.class, "Crit2FX", id--);
         addMapping(EntityDiggingFX.class, "DiggingFX", id--);
         addMapping(EntityDropParticleFX.class, "DropFX", id--);
         addMapping(EntityEnchantmentTableParticleFX.class, "EnchantmentTableFX", id--);
         addMapping(EntityExplodeFX.class, "ExplodeFX", id--);
         addMapping(EntityFlameFX.class, "FlameFX", id--);
         addMapping(EntityFireworkOverlayFX.class, "FireworkOverlayFX", id--);
         addMapping(EntityFireworkSparkFX.class, "FireworkSparkFX", id--);
         addMapping(EntityFireworkStarterFX.class, "FireworkStarterFX", id--);
         addMapping(EntityFootStepFX.class, "FootStepFX", id--);
         addMapping(EntityHeartFX.class, "HeartFX", id--);
         addMapping(EntityHugeExplodeFX.class, "HugeExplodeFX", id--);
         addMapping(EntityLargeExplodeFX.class, "LargeExplodeFX", id--);
         addMapping(EntityLavaFX.class, "LavaFX", id--);
         addMapping(EntityNoteFX.class, "NoteFX", id--);
         addMapping(EntityPickupFX.class, "PickupFX", id--);
         addMapping(EntityPortalFX.class, "PortalFX", id--);
         addMapping(EntityRainFX.class, "RainFX", id--);
         addMapping(EntityReddustFX.class, "RedDustFX", id--);
         addMapping(SmallFlameFX.class, "SmallFlameFX", id--);
         addMapping(EntitySmokeFX.class, "SmokeFX", id--);
         addMapping(EntitySnowShovelFX.class, "SnowShovelFX", id--);
         addMapping(EntitySpellParticleFX.class, "SpellFX", id--);
         addMapping(EntitySplashFX.class, "SplashFX", id--);
         addMapping(EntitySuspendFX.class, "SuspendFX", id--);
         addMapping(WhiteSmokeFX.class, "WhiteSmokeFX", id--);
         addMapping(WhiteCloudFX.class, "WhiteCloudFX", id--);
      }
   }
}
