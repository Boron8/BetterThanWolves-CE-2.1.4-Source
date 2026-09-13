package btw.client.fx;

import btw.block.BTWBlocks;
import btw.block.blocks.CompanionCubeBlock;
import btw.item.BTWItems;
import btw.world.util.BlockPos;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.Block;
import net.minecraft.src.EntityDiggingFX;
import net.minecraft.src.EntityList;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Material;

@Environment(EnvType.CLIENT)
public class BTWEffectManager {
   public static final int ANIMAL_BIRTHING_EFFECT_ID = 2222;
   public static final int SAW_DAMAGE_EFFECT_ID = 2223;
   public static final int NETHER_GROTH_SPORES_EFFECT_ID = 2224;
   public static final int GHAST_SCREAM_EFFECT_ID = 2225;
   public static final int BURP_SOUND_EFFECT_ID = 2226;
   public static final int FIRE_FIZZ_EFFECT_ID = 2227;
   public static final int GHAST_MOAN_EFFECT_ID = 2228;
   public static final int MINING_CHARGE_EXPLOSION_EFFECT_ID = 2229;
   public static final int HOPPER_EJECT_XP_EFFECT_ID = 2230;
   public static final int ITEM_COLLECTION_POP_EFFECT_ID = 2231;
   public static final int XP_EJECT_POP_EFFECT_ID = 2232;
   public static final int HOPPER_CLOSE_EFFECT_ID = 2233;
   public static final int REDSTONE_CLICK_EFFECT_ID = 2234;
   public static final int MECHANICAL_DEVICE_EXPLODE_EFFECT_ID = 2235;
   public static final int BLOCK_PLACE_EFFECT_ID = 2236;
   public static final int DYNAMITE_FUSE_EFFECT_ID = 2237;
   public static final int LOW_PITCH_CLICK_EFFECT_ID = 2238;
   public static final int WOLF_HURT_EFFECT_ID = 2239;
   public static final int CHICKEN_HURT_EFFECT_ID = 2240;
   public static final int BLOCK_DISPENSER_SMOKE_EFFECT_ID = 2241;
   public static final int COMPANION_CUBE_DEATH_EFFECT_ID = 2242;
   public static final int POSSESSED_CHICKEN_EXPLOSION_EFFECT_ID = 2243;
   public static final int ENDERMAN_COLLECT_BLOCK_EFFECT_ID = 2244;
   public static final int ENDERMAN_CONVERT_BLOCK_EFFECT_ID = 2245;
   public static final int ENDERMAN_PLACE_BLOCK_EFFECT_ID = 2246;
   public static final int ENDERMAN_CHANGE_DIMENSION_EFFECT_ID = 2247;
   public static final int SOUL_URN_SHATTER_EFFECT_ID = 2248;
   public static final int MELON_EXPLODE_EFFECT_ID = 2249;
   public static final int PUMPKIN_EXPLODE_EFFECT_ID = 2250;
   public static final int GOURD_IMPACT_SOUND_EFFECT_ID = 2251;
   public static final int DESTROY_BLOCK_RESPECT_PARTICLE_SETTINGS_EFFECT_ID = 2252;
   public static final int COW_REGEN_MILK_EFFECT_ID = 2253;
   public static final int COW_MILKING_EFFECT_ID = 2254;
   public static final int COW_CONVERSION_TO_MOOSHROOM_EFFECT_ID = 2255;
   public static final int WOLF_HOWL_EFFECT_ID = 2256;
   public static final int WOLF_CONVERSION_TO_DIRE_WOLF_EFFECT_ID = 2257;
   public static final int CREEPER_SNIP_EFFECT_ID = 2258;
   public static final int POSSESSED_PIG_TRANSFORMATION_EFFECT_ID = 2259;
   public static final int POSSESSED_VILLAGER_TRANSFORMATION_EFFECT_ID = 2260;
   public static final int SHEEP_REGROW_WOOL_EFFECT_ID = 2261;
   public static final int SQUID_TENTACLE_FLING_EFFECT_ID = 2262;
   public static final int CREATE_SNOW_GOLEM_EFFECT_ID = 2263;
   public static final int CREATE_IRON_GOLEM_EFFECT_ID = 2264;
   public static final int TOSS_THE_MILK_EFFECT_ID = 2265;
   public static final int APPLY_DUNG_TO_WOLF_EFFECT_ID = 2266;
   public static final int REMOVE_STUMP_EFFECT_ID = 2267;
   public static final int SHAFT_RIPPED_OFF_EFFECT_ID = 2268;
   public static final int STONE_RIPPED_OFF_EFFECT_ID = 2269;
   public static final int GRAVEL_RIPPED_OFF_EFFECT_ID = 2270;
   public static final int WOOD_BLOCK_DESTROYED_EFFECT_ID = 2271;
   public static final int BLOCK_DESTROYED_WITH_IMPROPER_TOOL_EFFECT_ID = 2272;
   public static final int POSSESSED_SQUID_TRANSFORMATION_EFFECT_ID = 2273;
   public static final int APPLY_MORTAR_EFFECT_ID = 2274;
   public static final int LOOSE_BLOCK_STUCK_TO_MORTAR_EFFECT_ID = 2275;
   public static final int SMOLDERING_LOG_FALL_EFFECT_ID = 2276;
   public static final int SMOLDERING_LOG_EXPLOSION_EFFECT_ID = 2277;
   public static final int WATER_EVAPORATION_EFFECT_ID = 2278;
   public static final int CREATE_WITHER_EFFECT_ID = 2279;
   public static final int LIGHTNING_STRIKE_EFFECT_ID = 2280;
   public static final int FLAMING_NETHERRACK_FALL_EFFECT_ID = 2281;
   public static final int CACTUS_EXPLOSION_EFFECT_ID = 2282;
   public static final int ANIMAL_EATING_EFFECT_ID = 2283;
   public static final int WOLF_EATING_EFFECT_ID = 2284;
   public static final int FAILED_EATING_EFFECT_ID = 2285;

   public static void initEffects() {
      EffectHandler.effectMap.put(2222, (mcInstance, world, player, x, y, z, data) -> {
         world.playSound(x, y, z, "mob.slime.attack", 1.0F, (world.rand.nextFloat() - world.rand.nextFloat()) * 0.2F + 1.0F);

         for (int counter = 0; counter < 10; counter++) {
            double bloodX = x + world.rand.nextDouble();
            double bloodY = y + 1.0 + world.rand.nextDouble();
            double bloodZ = z + world.rand.nextDouble();
            world.spawnParticle("reddust", bloodX, bloodY, bloodZ, 0.0, 0.0, 0.0);
         }

         for (int i = 0; i < 10; i++) {
            double bloodX = x - 0.5 + world.rand.nextDouble();
            double bloodY = y + world.rand.nextDouble();
            double bloodZ = z - 0.5 + world.rand.nextDouble();
            world.spawnParticle("dripLava", bloodX, bloodY, bloodZ, 0.0, 0.0, 0.0);
         }
      });
      EffectHandler.effectMap.put(2223, (mcInstance, world, player, x, y, z, data) -> {
         world.playSound(x, y, z, "minecart.base", 1.0F + world.rand.nextFloat() * 0.1F, 2.0F + world.rand.nextFloat() * 0.1F);
         BlockPos targetPos = new BlockPos((int)x, (int)y, (int)z, data);

         for (int i = 0; i < 10; i++) {
            float smokeX = targetPos.x + world.rand.nextFloat();
            float smokeY = targetPos.y + world.rand.nextFloat();
            float smokeZ = targetPos.z + world.rand.nextFloat();
            world.spawnParticle("reddust", smokeX, smokeY, smokeZ, 0.0, 0.0, 0.0);
         }
      });
      EffectHandler.effectMap
         .put(
            2224,
            (mcInstance, world, player, x, y, z, data) -> {
               world.playSound(x, y, z, "random.fuse", 2.0F, world.rand.nextFloat() * 0.4F + 1.5F);

               for (int i = 0; i < 10; i++) {
                  world.spawnParticle(
                     "hugeexplosion",
                     x + world.rand.nextDouble() * 10.0 - 5.0,
                     y + world.rand.nextDouble() * 10.0 - 5.0,
                     z + world.rand.nextDouble() * 10.0 - 5.0,
                     0.0,
                     0.0,
                     0.0
                  );
               }
            }
         );
      EffectHandler.effectMap.put(2225, (mcInstance, world, player, x, y, z, data) -> {
         float screamPitch = world.rand.nextFloat() * 0.4F + 0.8F;
         if (data == 1) {
            screamPitch = world.rand.nextFloat() * 0.4F + 0.25F;
         }

         world.playSound(x, y, z, "mob.ghast.scream", 1.0F, screamPitch);
      });
      EffectHandler.effectMap
         .put(2226, (mcInstance, world, player, x, y, z, data) -> world.playSound(x, y, z, "random.burp", 1.0F, world.rand.nextFloat() * 0.4F + 0.7F));
      EffectHandler.effectMap.put(2227, (mcInstance, world, player, x, y, z, data) -> {
         float fizzVolume = 0.5F;
         float fizzPitch = 2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F;
         if (data == 1) {
            fizzVolume = 0.1F;
            fizzPitch = 1.0F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.2F;
         }

         world.playSound(x, y, z, "random.fizz", fizzVolume, fizzPitch);
      });
      EffectHandler.effectMap
         .put(
            2228,
            (mcInstance, world, player, x, y, z, data) -> world.playSound(
               x, y, z, "mob.ghast.moan", 0.5F, 2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F
            )
         );
      EffectHandler.effectMap.put(2229, (mcInstance, world, player, x, y, z, data) -> {
         world.playSound(x, y, z, "random.explode", 4.0F, (1.0F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.2F) * 0.7F);
         world.spawnParticle("hugeexplosion", x, y, z, 0.0, 0.0, 0.0);
      });
      EffectHandler.effectMap.put(2230, (mcInstance, world, player, x, y, z, data) -> {
         world.playSound(x, y, z, "liquid.lavapop", 0.5F + world.rand.nextFloat() * 0.25F, 0.5F + world.rand.nextFloat() * 0.25F);

         for (int i = 0; i < 4; i++) {
            world.spawnParticle("slime", x, y - 0.6, z, 0.0, 0.0, 0.0);
         }
      });
      EffectHandler.effectMap
         .put(
            2231,
            (mcInstance, world, player, x, y, z, data) -> world.playSound(
               x, y, z, "random.pop", 0.25F, ((world.rand.nextFloat() - world.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F
            )
         );
      EffectHandler.effectMap
         .put(
            2232,
            (mcInstance, world, player, x, y, z, data) -> world.playSound(
               x, y, z, "liquid.lavapop", 0.5F + world.rand.nextFloat() * 0.25F, 0.5F + world.rand.nextFloat() * 0.25F
            )
         );
      EffectHandler.effectMap.put(2233, (mcInstance, world, player, x, y, z, data) -> world.playSound(x, y, z, "mob.irongolem.walk", 1.0F, 1.25F));
      EffectHandler.effectMap.put(2234, (mcInstance, world, player, x, y, z, data) -> world.playSound(x, y, z, "random.click", 0.75F, 2.0F));
      EffectHandler.effectMap.put(2235, (mcInstance, world, player, x, y, z, data) -> {
         world.playSound(x, y, z, "mob.zombie.woodbreak", 0.5F, 0.6F + world.rand.nextFloat() * 0.25F);
         world.spawnParticle("explode", x, y, z, 0.0, 0.0, 0.0);

         for (int i = 0; i < 20; i++) {
            double smokeX = x + world.rand.nextDouble() - 0.5;
            double smokeY = y + world.rand.nextDouble() - 0.5;
            double smokeZ = z + world.rand.nextDouble() - 0.5;
            double smokeVelX = (smokeX - x) * 0.33;
            double smokeVelY = (smokeY - y) * 0.33;
            double smokeVelZ = (smokeZ - z) * 0.33;
            world.spawnParticle("smoke", smokeX, smokeY, smokeZ, smokeVelX, smokeVelY, smokeVelZ);
         }
      });
      EffectHandler.effectMap.put(2236, (mcInstance, world, player, x, y, z, data) -> {
         Block block = Block.blocksList[data];
         if (block != null) {
            world.playSound(x, y, z, block.stepSound.getPlaceSound(), (block.stepSound.getPlaceVolume() + 1.0F) / 2.0F, block.stepSound.getPlacePitch() * 0.8F);
         }
      });
      EffectHandler.effectMap.put(2237, (mcInstance, world, player, x, y, z, data) -> world.playSound(x, y, z, "random.fuse", 1.0F, 1.0F));
      EffectHandler.effectMap.put(2238, (mcInstance, world, player, x, y, z, data) -> world.playSound(x, y, z, "random.click", 0.1F, 0.5F));
      EffectHandler.effectMap
         .put(
            2239,
            (mcInstance, world, player, x, y, z, data) -> world.playSound(
               x, y, z, "mob.wolf.hurt", 0.4F, (world.rand.nextFloat() - world.rand.nextFloat()) * 0.2F + 1.0F
            )
         );
      EffectHandler.effectMap
         .put(
            2240,
            (mcInstance, world, player, x, y, z, data) -> world.playSound(
               x, y, z, "mob.chicken.hurt", 1.0F, (world.rand.nextFloat() - world.rand.nextFloat()) * 0.2F + 1.0F
            )
         );
      EffectHandler.effectMap.put(2241, (mcInstance, world, player, x, y, z, data) -> {
         BlockPos targetDeltaPos = new BlockPos(0, 0, 0, data);
         double ejectX = x + targetDeltaPos.x * 0.6;
         double ejectY = y + targetDeltaPos.y * 0.6;
         double ejectZ = z + targetDeltaPos.z * 0.6;

         for (int i = 0; i < 10; i++) {
            double d4 = world.rand.nextDouble() * 0.2 + 0.01;
            double smokeX = ejectX + targetDeltaPos.x * 0.01 + (world.rand.nextDouble() - 0.5) * targetDeltaPos.x * 0.5;
            double smokeY = ejectY + targetDeltaPos.y * 0.01 + (world.rand.nextDouble() - 0.5) * 0.5;
            double smokeZ = ejectZ + targetDeltaPos.z * 0.01 + (world.rand.nextDouble() - 0.5) * targetDeltaPos.z * 0.5;
            double smokeVelX = targetDeltaPos.x * d4 + world.rand.nextGaussian() * 0.01;
            double smokeVelY = targetDeltaPos.y * d4 - 0.03 + world.rand.nextGaussian() * 0.01;
            double smokeVelZ = targetDeltaPos.z * d4 + world.rand.nextGaussian() * 0.01;
            world.spawnParticle("smoke", smokeX, smokeY, smokeZ, smokeVelX, smokeVelY, smokeVelZ);
         }
      });
      EffectHandler.effectMap.put(2242, (mcInstance, world, player, x, y, z, data) -> {
         CompanionCubeBlock.spawnHearts(world, (int)x, (int)y, (int)z);
         world.playSound(x, y, z, "mob.wolf.whine", 0.5F, 2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F);
      });
      EffectHandler.effectMap.put(2243, (mcInstance, world, player, x, y, z, data) -> {
         world.playSound(x, y, z, "random.explode", 1.0F, (world.rand.nextFloat() - world.rand.nextFloat()) * 0.2F + 1.0F);
         world.playSound(x, y, z, "mob.chicken.hurt", 2.0F, world.rand.nextFloat() * 0.4F + 1.2F);

         for (int i = 0; i < 10; i++) {
            double bloodX = x + world.rand.nextDouble();
            double bloodY = y + 1.0 + world.rand.nextDouble();
            double bloodZ = z + world.rand.nextDouble();
            world.spawnParticle("reddust", bloodX, bloodY, bloodZ, 0.0, 0.0, 0.0);
         }

         for (int i = 0; i < 10; i++) {
            double bloodX = x - 0.5 + world.rand.nextDouble();
            double bloodY = y + world.rand.nextDouble() * 0.5;
            double bloodZ = z - 0.5 + world.rand.nextDouble();
            world.spawnParticle("dripLava", bloodX, bloodY, bloodZ, 0.0, 0.0, 0.0);
         }

         for (int i = 0; i < 300; i++) {
            double bloodX = x + world.rand.nextDouble() - 0.5;
            double bloodY = y - 1.0;
            double bloodZ = z + world.rand.nextDouble() - 0.5;
            double bloodVelX = (world.rand.nextDouble() - 0.5) * 0.5;
            double bloodVelY = 0.2 + world.rand.nextDouble() * 0.6;
            double bloodVelZ = (world.rand.nextDouble() - 0.5) * 0.5;
            world.spawnParticle("iconcrack_331", bloodX, bloodY, bloodZ, bloodVelX, bloodVelY, bloodVelZ);
         }

         for (int i = 0; i < 25; i++) {
            double boneX = x + world.rand.nextDouble() - 0.5;
            double boneY = y - 1.0;
            double boneZ = z + world.rand.nextDouble() - 0.5;
            double boneVelX = (world.rand.nextDouble() - 0.5) * 0.5;
            double boneVelY = 0.2 + world.rand.nextDouble() * 0.6;
            double boneVelZ = (world.rand.nextDouble() - 0.5) * 0.5;
            world.spawnParticle("iconcrack_352", boneX, boneY, boneZ, boneVelX, boneVelY, boneVelZ);
         }
      });
      EffectHandler.effectMap.put(2244, (mcInstance, world, player, x, y, z, data) -> {
         int blockID = data & 4095;
         int metadata = data >> 12 & 0xFF;
         Block block = Block.blocksList[blockID];
         if (block != null) {
            world.playSound(x, y, z, block.stepSound.getBreakSound(), (block.stepSound.getBreakVolume() + 1.0F) / 2.0F, block.stepSound.getBreakPitch() * 0.8F);
            mcInstance.effectRenderer.addBlockDestroyEffects((int)x, (int)y, (int)z, blockID, metadata);
         }
      });
      EffectHandler.effectMap.put(2245, (mcInstance, world, player, x, y, z, data) -> {
         int blockID = data & 4095;
         int metadata = data >> 12 & 0xFF;
         Block block = Block.blocksList[blockID];
         if (block != null) {
            world.playSound(x, y, z, block.stepSound.getBreakSound(), (block.stepSound.getBreakVolume() + 1.0F) / 2.0F, block.stepSound.getBreakPitch() * 0.8F);
            mcInstance.effectRenderer.addBlockDestroyEffects((int)x, (int)y, (int)z, blockID, metadata);
         }

         for (int i = 0; i < 25; i++) {
            double particleX = x + (world.rand.nextDouble() - 0.5) * 1.5;
            double particleY = y + (world.rand.nextDouble() - 0.5);
            double particleZ = z + (world.rand.nextDouble() - 0.5) * 1.5;
            world.spawnParticle("mobSpell", particleX, particleY, particleZ, 0.0, 0.0, 0.0);
         }

         world.playSound(x, y, z, "mob.endermen.portal", 1.0F, 1.0F);
      });
      EffectHandler.effectMap.put(2246, (mcInstance, world, player, x, y, z, data) -> {
         int blockID = data & 4095;
         Block block = Block.blocksList[blockID];
         if (block != null) {
            world.playSound(x, y, z, block.stepSound.getStepSound(), (block.stepSound.getStepVolume() + 1.0F) / 2.0F, block.stepSound.getStepPitch() * 0.8F);
         }

         world.playSound(x, y, z, "mob.endermen.hit", 1.0F, 1.0F);
      });
      EffectHandler.effectMap.put(2247, (mcInstance, world, player, x, y, z, data) -> {
         world.spawnParticle("largeexplode", x, y, z, 0.0, 0.0, 0.0);
         world.playSound(x, y, z, "ambient.weather.thunder", 3.0F, world.rand.nextFloat() * 0.4F + 0.8F);
      });
      EffectHandler.effectMap.put(2248, (mcInstance, world, player, x, y, z, data) -> {
         for (int i = 0; i < 8; i++) {
            world.spawnParticle("snowballpoof", x, y, z, 0.0, 0.0, 0.0);
         }

         world.playSound(x, y, z, "random.glass", 1.0F, 1.2F / (world.rand.nextFloat() * 0.2F + 0.9F));
         world.playSound(x, y, z, "mob.ghast.scream", 0.2F, world.rand.nextFloat() * 0.2F + 0.5F);

         for (int i = 0; i < 100; i++) {
            double particleX = x + world.rand.nextDouble() * 3.0 - 1.5;
            double particleY = y + world.rand.nextDouble() * 3.0 - 1.5;
            double particleZ = z + world.rand.nextDouble() * 3.0 - 1.5;
            world.spawnParticle("mobSpell", particleX, particleY, particleZ, 0.0, 0.0, 0.0);
         }
      });
      EffectHandler.effectMap.put(2249, (mcInstance, world, player, x, y, z, data) -> {
         String particle = "iconcrack_360";

         for (int i = 0; i < 150; i++) {
            double particleX = x + world.rand.nextDouble() - 0.5;
            double particleY = y - 0.45;
            double particleZ = z + world.rand.nextDouble() - 0.5;
            double particleVelX = (world.rand.nextDouble() - 0.5) * 0.5;
            double particleVelY = world.rand.nextDouble() * 0.7;
            double particleVelZ = (world.rand.nextDouble() - 0.5) * 0.5;
            world.spawnParticle(particle, particleX, particleY, particleZ, particleVelX, particleVelY, particleVelZ);
         }

         world.playSound(x, y, z, "mob.zombie.wood", 0.2F, 0.6F + world.rand.nextFloat() * 0.25F);
         world.playSound(x, y, z, "mob.slime.attack", 1.0F, (world.rand.nextFloat() - world.rand.nextFloat()) * 0.2F + 0.6F);
      });
      EffectHandler.effectMap.put(2250, (mcInstance, world, player, x, y, z, data) -> {
         String particle = "iconcrack_" + BTWItems.cookedCarrot.itemID;

         for (int i = 0; i < 150; i++) {
            double particleX = x + world.rand.nextDouble() - 0.5;
            double particleY = y - 0.45;
            double particleZ = z + world.rand.nextDouble() - 0.5;
            double particleVelX = (world.rand.nextDouble() - 0.5) * 0.5;
            double particleVelY = world.rand.nextDouble() * 0.7;
            double particleVelZ = (world.rand.nextDouble() - 0.5) * 0.5;
            world.spawnParticle(particle, particleX, particleY, particleZ, particleVelX, particleVelY, particleVelZ);
         }

         world.playSound(x, y, z, "mob.zombie.wood", 0.2F, 0.6F + world.rand.nextFloat() * 0.25F);
         world.playSound(x, y, z, "mob.slime.attack", 1.0F, (world.rand.nextFloat() - world.rand.nextFloat()) * 0.2F + 0.6F);
      });
      EffectHandler.effectMap
         .put(2251, (mcInstance, world, player, x, y, z, data) -> world.playSound(x, y, z, "mob.zombie.wood", 0.1F, 0.4F + world.rand.nextFloat() * 0.25F));
      EffectHandler.effectMap
         .put(
            2252,
            (mcInstance, world, player, x, y, z, data) -> {
               int blockID = data & 4095;
               int metadata = data >> 12 & 0xFF;
               Block block = Block.blocksList[blockID];
               if (block != null) {
                  world.playSound(
                     x, y, z, block.stepSound.getBreakSound(), (block.stepSound.getBreakVolume() + 1.0F) / 2.0F, block.stepSound.getBreakPitch() * 0.8F
                  );
                  if (mcInstance.gameSettings.particleSetting <= 1) {
                     for (int i = 0; i < 4; i++) {
                        for (int j = 0; j < 4; j++) {
                           for (int k = 0; k < 4; k++) {
                              if (mcInstance.gameSettings.particleSetting == 0 || world.rand.nextInt(3) == 0) {
                                 double particleX = i + (i + 0.5) / 4.0;
                                 double particleY = j + (j + 0.5) / 4.0;
                                 double particleZ = k + (k + 0.5) / 4.0;
                                 EntityDiggingFX digEffect = (EntityDiggingFX)EntityList.createEntityOfType(
                                    EntityDiggingFX.class,
                                    world,
                                    particleX,
                                    particleY,
                                    particleZ,
                                    particleX - i - 0.5,
                                    particleY - j - 0.5,
                                    particleZ - k - 0.5,
                                    block,
                                    0,
                                    metadata,
                                    mcInstance.renderEngine
                                 );
                                 digEffect.applyRenderColor(metadata);
                                 mcInstance.effectRenderer.addEffect(digEffect);
                              }
                           }
                        }
                     }
                  }
               }
            }
         );
      EffectHandler.effectMap
         .put(
            2253,
            (mcInstance, world, player, x, y, z, data) -> world.playSound(
               x, y, z, "mob.slime.attack", 1.0F, (world.rand.nextFloat() - world.rand.nextFloat()) * 0.2F + 0.6F
            )
         );
      EffectHandler.effectMap.put(2254, (mcInstance, world, player, x, y, z, data) -> {
         world.playSound(x, y, z, "mob.slime.attack", 1.0F, (world.rand.nextFloat() - world.rand.nextFloat()) * 0.2F + 0.6F);
         String milkParticle = "iconcrack_332";

         for (int i = 0; i < 50; i++) {
            double particleX = x + world.rand.nextDouble() - 0.5;
            double particleY = y - 0.45;
            double particleZ = z + world.rand.nextDouble() - 0.5;
            double particleVelX = (world.rand.nextDouble() - 0.5) * 0.5;
            double particleVelY = world.rand.nextDouble() * 0.25;
            double particleVelZ = (world.rand.nextDouble() - 0.5) * 0.5;
            world.spawnParticle(milkParticle, particleX, particleY, particleZ, particleVelX, particleVelY, particleVelZ);
         }
      });
      EffectHandler.effectMap.put(2255, (mcInstance, world, player, x, y, z, data) -> {
         world.spawnParticle("largeexplode", x, y, z, 0.0, 0.0, 0.0);
         world.playSound(x, y, z, "mob.slime.attack", 1.0F, (world.rand.nextFloat() - world.rand.nextFloat()) * 0.2F + 1.0F);
         float hurtPitch = (world.rand.nextFloat() - world.rand.nextFloat()) * 0.2F + 1.0F;
         if (data > 0) {
            hurtPitch += 0.5F;
         }

         world.playSound(x, y, z, "mob.cow.hurt", 1.0F, hurtPitch);
      });
      EffectHandler.effectMap.put(2256, (mcInstance, world, player, x, y, z, data) -> {
         float soundVolume;
         float soundPitch;
         if (data > 0) {
            soundVolume = 10.0F;
            soundPitch = (world.rand.nextFloat() - world.rand.nextFloat()) * 0.05F + 0.55F;
         } else {
            soundVolume = 8.5F;
            soundPitch = (world.rand.nextFloat() - world.rand.nextFloat()) * 0.2F + 1.0F;
         }

         EntityPlayer localPlayer = mcInstance.thePlayer;
         if (localPlayer != null && localPlayer.posY < 64.0) {
            float volumeMultiplier = (float)(localPlayer.posY / 64.0);
            soundVolume *= volumeMultiplier;
            if (soundVolume < 1.0F) {
               soundVolume = 1.0F;
            }
         }

         world.playSound(x, y, z, "mob.wolf.howl", soundVolume, soundPitch);
      });
      EffectHandler.effectMap.put(2257, (mcInstance, world, player, x, y, z, data) -> {
         world.spawnParticle("largeexplode", x, y, z, 0.0, 0.0, 0.0);
         world.playSound(x, y, z, "mob.slime.attack", 1.0F, (world.rand.nextFloat() - world.rand.nextFloat()) * 0.2F + 1.0F);
         world.playSound(x, y, z, "mob.wolf.growl", 8.5F, (world.rand.nextFloat() - world.rand.nextFloat()) * 0.05F + 0.55F);
      });
      EffectHandler.effectMap.put(2258, (mcInstance, world, player, x, y, z, data) -> {
         world.playSound(x, y, z, "mob.sheep.shear", 1.0F, 1.0F);
         world.playSound(x, y, z, "mob.slime.attack", 1.0F, (world.rand.nextFloat() - world.rand.nextFloat()) * 0.1F + 0.7F);
         String creeperSnipParticle = "iconcrack_332";

         for (int i = 0; i < 50; i++) {
            double particleX = x + world.rand.nextDouble() - 0.5;
            double particleY = y - 0.45;
            double particleZ = z + world.rand.nextDouble() - 0.5;
            double particleVelX = (world.rand.nextDouble() - 0.5) * 0.5;
            double particleVelY = world.rand.nextDouble() * 0.25;
            double particleVelZ = (world.rand.nextDouble() - 0.5) * 0.5;
            world.spawnParticle(creeperSnipParticle, particleX, particleY, particleZ, particleVelX, particleVelY, particleVelZ);
         }
      });
      EffectHandler.effectMap.put(2259, (mcInstance, world, player, x, y, z, data) -> {
         world.playSound(x, y, z, "mob.pig.death", 2.0F, world.rand.nextFloat() * 0.4F + 1.2F);
         world.playSound(x, y, z, "mob.zombiepig.zpigangry", 2.0F, ((world.rand.nextFloat() - world.rand.nextFloat()) * 0.2F + 1.0F) * 1.8F);
         world.playSound(x, y, z, "mob.slime.attack", 1.0F, (world.rand.nextFloat() - world.rand.nextFloat()) * 0.2F + 1.0F);
         world.spawnParticle("largeexplode", x, y, z, 0.0, 0.0, 0.0);

         for (int i = 0; i < 50; i++) {
            double particleX = x + world.rand.nextDouble() - 0.5;
            double particleY = y - 1.0;
            double particleZ = z + world.rand.nextDouble() - 0.5;
            double particleVelX = (world.rand.nextDouble() - 0.5) * 0.5;
            double particleVelY = 0.2 + world.rand.nextDouble() * 0.6;
            double particleVelZ = (world.rand.nextDouble() - 0.5) * 0.5;
            world.spawnParticle("iconcrack-319", particleX, particleY, particleZ, particleVelX, particleVelY, particleVelZ);
         }
      });
      EffectHandler.effectMap
         .put(
            2260,
            (mcInstance, world, player, x, y, z, data) -> {
               world.playSound(x, y, z, "ambient.weather.thunder", 3.0F, world.rand.nextFloat() * 0.4F + 0.8F);
               world.playSound(x, y, z, "mob.ghast.affectionate scream", 2.0F, 0.5F + world.rand.nextFloat() * 0.25F);
               world.spawnParticle("largeexplode", x, y, z, 0.0, 0.0, 0.0);

               for (int i = 0; i < world.rand.nextInt(35) + 10; i++) {
                  world.spawnParticle(
                     "witchMagic",
                     x + world.rand.nextGaussian() * 0.125,
                     y + 2.0 + world.rand.nextGaussian() * 0.125,
                     z + world.rand.nextGaussian() * 0.125,
                     0.0,
                     0.0,
                     0.0
                  );
               }
            }
         );
      EffectHandler.effectMap
         .put(2261, (mcInstance, world, player, x, y, z, data) -> world.playSound(x, y, z, "step.cloth", 1.0F, world.rand.nextFloat() * 0.1F + 0.9F));
      EffectHandler.effectMap
         .put(
            2262,
            (mcInstance, world, player, x, y, z, data) -> {
               world.playSound(x, y, z, "mob.slime.attack", 1.0F, (world.rand.nextFloat() - world.rand.nextFloat()) * 0.2F + 0.6F);
               if (mcInstance.gameSettings.particleSetting <= 1) {
                  int blockID = Block.waterStill.blockID;
                  int metadata = 0;
                  Block block = Block.blocksList[blockID];

                  for (int i = 0; i < 4; i++) {
                     for (int j = 0; j < 4; j++) {
                        for (int k = 0; k < 4; k++) {
                           if (mcInstance.gameSettings.particleSetting == 0 || world.rand.nextInt(3) == 0) {
                              double particleX = i + (i + 0.5) / 4.0;
                              double particleY = j + (j + 0.5) / 4.0;
                              double particleZ = k + (k + 0.5) / 4.0;
                              EntityDiggingFX digEffect = (EntityDiggingFX)EntityList.createEntityOfType(
                                 EntityDiggingFX.class,
                                 world,
                                 particleX,
                                 particleY,
                                 particleZ,
                                 particleX - i - 0.5,
                                 particleY - j - 0.5,
                                 particleZ - k - 0.5,
                                 block,
                                 0,
                                 metadata,
                                 mcInstance.renderEngine
                              );
                              digEffect.applyRenderColor(metadata);
                              mcInstance.effectRenderer.addEffect(digEffect);
                           }
                        }
                     }
                  }
               }
            }
         );
      EffectHandler.effectMap
         .put(
            2263,
            (mcInstance, world, player, x, y, z, data) -> {
               for (int i = 0; i < 120; i++) {
                  world.spawnParticle(
                     "snowshovel",
                     (int)x + world.rand.nextDouble(),
                     (int)y - 2 + world.rand.nextDouble() * 2.5,
                     (int)z + world.rand.nextDouble(),
                     0.0,
                     0.0,
                     0.0
                  );
               }

               for (int i = 0; i < 8; i++) {
                  world.spawnParticle("snowballpoof", x, y, z, 0.0, 0.0, 0.0);
               }

               world.playSound(x, y, z, "random.glass", 1.0F, 1.2F / (world.rand.nextFloat() * 0.2F + 0.9F));
               world.playSound(x, y, z, "mob.enderdragon.growl", 0.25F, world.rand.nextFloat() * 0.2F + 1.8F);

               for (int i = 0; i < 100; i++) {
                  double particleX = x + world.rand.nextDouble() * 3.0 - 1.5;
                  double particleY = y + world.rand.nextDouble() * 3.0 - 1.5;
                  double particleZ = z + world.rand.nextDouble() * 3.0 - 1.5;
                  world.spawnParticle("mobSpell", particleX, particleY, particleZ, 0.0, 0.0, 0.0);
               }
            }
         );
      EffectHandler.effectMap
         .put(
            2264,
            (mcInstance, world, player, x, y, z, data) -> {
               for (int i = 0; i < 120; i++) {
                  world.spawnParticle(
                     "snowballpoof",
                     (int)x + world.rand.nextDouble(),
                     (int)y - 2 + world.rand.nextDouble() * 2.5,
                     (int)z + world.rand.nextDouble(),
                     0.0,
                     0.0,
                     0.0
                  );
               }

               for (int i = 0; i < 8; i++) {
                  world.spawnParticle("snowballpoof", x, y, z, 0.0, 0.0, 0.0);
               }

               world.playSound(x, y, z, "random.glass", 1.0F, 1.2F / (world.rand.nextFloat() * 0.2F + 0.9F));
               world.playSound(x, y, z, "mob.irongolem.death", 1.0F, world.rand.nextFloat() * 0.2F + 0.5F);
               world.playSound(x, y, z, "mob.enderdragon.growl", 0.5F, world.rand.nextFloat() * 0.2F + 1.5F);

               for (int i = 0; i < 100; i++) {
                  double particleX = x + world.rand.nextDouble() * 3.0 - 1.5;
                  double particleY = y + world.rand.nextDouble() * 3.0 - 1.5;
                  double particleZ = z + world.rand.nextDouble() * 3.0 - 1.5;
                  world.spawnParticle("mobSpell", particleX, particleY, particleZ, 0.0, 0.0, 0.0);
               }
            }
         );
      EffectHandler.effectMap
         .put(
            2265,
            (mcInstance, world, player, x, y, z, data) -> {
               for (int i = 0; i < 120; i++) {
                  world.spawnParticle(
                     "snowballpoof",
                     (int)x + world.rand.nextDouble(),
                     (int)y - 2 + world.rand.nextDouble() * 2.5,
                     (int)z + world.rand.nextDouble(),
                     0.0,
                     0.0,
                     0.0
                  );
               }

               float soundPitch = 2.0F;
               if (data > 0) {
                  soundPitch = 1.2F;
               }

               world.playSound(x, y, z, "mob.slime.attack", 0.5F, (world.rand.nextFloat() - world.rand.nextFloat()) * 0.1F + 0.6F);
               world.playSound(x, y, z, "random.classic_hurt", 0.25F, soundPitch);
            }
         );
      EffectHandler.effectMap.put(2266, (mcInstance, world, player, x, y, z, data) -> {
         world.playSound(x, y, z, "mob.wolf.whine", 0.5F, 1.5F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.4F);

         for (int i = 0; i < 15; i++) {
            double particleX = x + world.rand.nextDouble() - 0.5;
            double particleY = y - 0.5 + world.rand.nextDouble() * 0.25;
            double particleZ = z + world.rand.nextDouble() - 0.5;
            double particleVelX = (world.rand.nextDouble() - 0.5) * 0.25;
            double particleVelY = 0.1 + world.rand.nextDouble() * 0.1;
            double particleVelZ = (world.rand.nextDouble() - 0.5) * 0.25;
            world.spawnParticle("iconcrack_491", particleX, particleY, particleZ, particleVelX, particleVelY, particleVelZ);
         }

         world.playSound(x, y, z, "mob.slime.attack", 0.5F, (world.rand.nextFloat() - world.rand.nextFloat()) * 0.1F + 0.8F);
      });
      EffectHandler.effectMap.put(2267, (mcInstance, world, player, x, y, z, data) -> {
         world.playSound(x, y, z, "mob.slime.attack", 1.0F, (world.rand.nextFloat() - world.rand.nextFloat()) * 0.2F + 0.6F);
         world.spawnParticle("largeexplode", x, y, z, 0.0, 0.0, 0.0);

         for (int i = 0; i < 20; i++) {
            double smokeX = x + world.rand.nextDouble() - 0.5;
            double smokeY = y + world.rand.nextDouble() - 0.5;
            double smokeZ = z + world.rand.nextDouble() - 0.5;
            double smokeVelX = (smokeX - x) * 0.33;
            double smokeVelY = (smokeY - y) * 0.33;
            double smokeVelZ = (smokeZ - z) * 0.33;
            world.spawnParticle("smoke", smokeX, smokeY, smokeZ, smokeVelX, smokeVelY, smokeVelZ);
         }
      });
      EffectHandler.effectMap
         .put(
            2268, (mcInstance, world, player, x, y, z, data) -> world.playSound(x, y, z, "mob.zombie.woodbreak", 0.25F, 1.0F + world.rand.nextFloat() * 0.25F)
         );
      EffectHandler.effectMap
         .put(2269, (mcInstance, world, player, x, y, z, data) -> world.playSound(x, y, z, "random.anvil_land", 0.5F, world.rand.nextFloat() * 0.25F + 1.75F));
      EffectHandler.effectMap.put(2270, (mcInstance, world, player, x, y, z, data) -> {
         world.playSound(x, y, z, "random.anvil_land", 0.25F, world.rand.nextFloat() * 0.25F + 1.5F);
         world.playSound(x, y, z, "step.gravel", 1.0F, world.rand.nextFloat() * 0.25F + 1.0F);
      });
      EffectHandler.effectMap
         .put(
            2271, (mcInstance, world, player, x, y, z, data) -> world.playSound(x, y, z, "mob.zombie.woodbreak", 0.25F, 1.0F + world.rand.nextFloat() * 0.25F)
         );
      EffectHandler.effectMap.put(2272, (mcInstance, world, player, x, y, z, data) -> {
         int blockID = data & 4095;
         Block block = Block.blocksList[blockID];
         if (block != null) {
            int metadata = data >> 12 & 0xFF;
            if (block.blockMaterial == BTWBlocks.plankMaterial || block.blockMaterial == BTWBlocks.logMaterial) {
               world.playSound(x, y, z, "mob.zombie.woodbreak", 0.25F, 1.0F + world.rand.nextFloat() * 0.25F);
            } else if (block.blockMaterial == Material.anvil) {
               world.playSound(x, y, z, "random.anvil_land", 1.0F, world.rand.nextFloat() * 0.25F + 0.75F);
            }
         }
      });
      EffectHandler.effectMap.put(2273, (mcInstance, world, player, x, y, z, data) -> {
         world.playSound(x, y, z, "mob.slime.attack", 1.0F, (world.rand.nextFloat() - world.rand.nextFloat()) * 0.2F + 1.0F);
         world.playSound(x, y, z, "mob.ghast.scream", 10.0F, (world.rand.nextFloat() - world.rand.nextFloat()) * 0.2F + 1.0F);

         for (int i = 0; i < 10; i++) {
            double explodeX = x + (world.rand.nextDouble() - 0.5) * 4.0;
            double explodeY = y + (world.rand.nextDouble() - 0.5) * 4.0;
            double explodeZ = z + (world.rand.nextDouble() - 0.5) * 4.0;
            world.spawnParticle("largeexplode", explodeX, explodeY, explodeZ, 0.0, 0.0, 0.0);
         }
      });
      EffectHandler.effectMap
         .put(
            2274,
            (mcInstance, world, player, x, y, z, data) -> world.playSound(
               x, y, z, "mob.slime.attack", 0.7F + world.rand.nextFloat() * 0.1F, 0.85F + world.rand.nextFloat() * 0.1F
            )
         );
      EffectHandler.effectMap
         .put(
            2275,
            (mcInstance, world, player, x, y, z, data) -> world.playSound(
               x, y, z, "mob.slime.attack", 0.15F + world.rand.nextFloat() * 0.1F, 0.6F + world.rand.nextFloat() * 0.1F
            )
         );
      EffectHandler.effectMap.put(2276, (mcInstance, world, player, x, y, z, data) -> {
         world.playSound(x, y, z, "mob.zombie.woodbreak", 1.25F, 0.5F + world.rand.nextFloat() * 0.1F);
         world.playSound(x, y, z, "mob.ghast.fireball", 1.0F, 0.5F + world.rand.nextFloat() * 0.1F);
      });
      EffectHandler.effectMap.put(2277, (mcInstance, world, player, x, y, z, data) -> {
         world.playSound(x, y, z, "mob.zombie.wood", 1.25F, 0.5F + world.rand.nextFloat() * 0.1F);
         world.spawnParticle("largeexplode", x, y, z, 0.0, 0.0, 0.0);

         for (int i = 0; i < 10; i++) {
            world.spawnParticle("fccinders", x, y, z, 0.0, 0.0, 0.0);
         }
      });
      EffectHandler.effectMap
         .put(
            2278,
            (mcInstance, world, player, x, y, z, data) -> {
               world.playSound(x, y, z, "random.fizz", 0.5F, 2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F);

               for (int i = 0; i < 8; i++) {
                  world.spawnParticle(
                     "largesmoke", x + world.rand.nextDouble() - 0.5, y + world.rand.nextDouble() - 0.5, z + world.rand.nextDouble() - 0.5, 0.0, 0.0, 0.0
                  );
               }
            }
         );
      EffectHandler.effectMap
         .put(
            2279,
            (mcInstance, world, player, x, y, z, data) -> {
               for (int i = 0; i < 120; i++) {
                  world.spawnParticle(
                     "snowballpoof",
                     (int)x + world.rand.nextDouble(),
                     (int)y - 2 + world.rand.nextDouble() * 2.5,
                     (int)z + world.rand.nextDouble(),
                     0.0,
                     0.0,
                     0.0
                  );
               }

               for (int i = 0; i < 8; i++) {
                  world.spawnParticle("snowballpoof", x, y, z, 0.0, 0.0, 0.0);
               }

               world.playSound(x, y, z, "random.glass", 1.0F, 1.2F / (world.rand.nextFloat() * 0.2F + 0.9F));
               world.playSound(x, y, z, "mob.wither.death", 1.0F, world.rand.nextFloat() * 0.2F + 0.5F);
               world.playSound(x, y, z, "mob.enderdragon.growl", 0.5F, world.rand.nextFloat() * 0.2F + 1.5F);

               for (int i = 0; i < 100; i++) {
                  double particleX = x + world.rand.nextDouble() * 3.0 - 1.5;
                  double particleY = y + world.rand.nextDouble() * 3.0 - 1.5;
                  double particleZ = z + world.rand.nextDouble() * 3.0 - 1.5;
                  world.spawnParticle("mobSpell", particleX, particleY, particleZ, 0.0, 0.0, 0.0);
               }
            }
         );
      EffectHandler.effectMap.put(2280, (mcInstance, world, player, x, y, z, data) -> {
         world.spawnParticle("largeexplode", x, y, z, 0.0, 0.0, 0.0);
         world.playSound(x, y, z, "random.explode", 4.0F, 0.5F + world.rand.nextFloat() * 0.2F);
         world.playSound(x, y, z, "ambient.weather.thunder", 10000.0F, 0.8F + world.rand.nextFloat() * 0.2F);
      });
      EffectHandler.effectMap
         .put(2281, (mcInstance, world, player, x, y, z, data) -> world.playSound(x, y, z, "mob.ghast.fireball", 0.1F, 0.75F + world.rand.nextFloat() * 0.1F));
      EffectHandler.effectMap.put(2282, (mcInstance, world, player, x, y, z, data) -> {
         for (int i = 0; i < 150; i++) {
            double particleX = x + world.rand.nextDouble() - 0.5;
            double particleY = y - 0.45;
            double particleZ = z + world.rand.nextDouble() - 0.5;
            double particleVelX = (world.rand.nextDouble() - 0.5) * 0.5;
            double particleVelY = world.rand.nextDouble() * 0.7;
            double particleVelZ = (world.rand.nextDouble() - 0.5) * 0.5;
            world.spawnParticle("iconcrack_338", particleX, particleY, particleZ, particleVelX, particleVelY, particleVelZ);
         }
      });
      EffectHandler.effectMap.put(2283, (mcInstance, world, player, x, y, z, data) -> {
         world.playSound(x, y, z, "random.eat", 0.75F, (world.rand.nextFloat() - world.rand.nextFloat()) * 0.2F + 0.6F);

         for (int i = 0; i < 25; i++) {
            double particleX = x + world.rand.nextDouble() - 0.5;
            double particleZ = z + world.rand.nextDouble() - 0.5;
            double particleVelX = (world.rand.nextDouble() - 0.5) * 0.25;
            double particleVelY = world.rand.nextDouble() * 0.35;
            double particleVelZ = (world.rand.nextDouble() - 0.5) * 0.25;
            world.spawnParticle("iconcrack_361", particleX, y, particleZ, particleVelX, particleVelY, particleVelZ);
         }
      });
      EffectHandler.effectMap.put(2284, (mcInstance, world, player, x, y, z, data) -> {
         world.playSound(x, y, z, "random.burp", 1.0F, world.rand.nextFloat() * 0.4F + 0.7F);

         for (int i = 0; i < 25; i++) {
            double particleX = x + world.rand.nextDouble() - 0.5;
            double particleZ = z + world.rand.nextDouble() - 0.5;
            double particleVelX = (world.rand.nextDouble() - 0.5) * 0.25;
            double particleVelY = world.rand.nextDouble() * 0.35;
            double particleVelZ = (world.rand.nextDouble() - 0.5) * 0.25;
            world.spawnParticle("iconcrack_281", particleX, y, particleZ, particleVelX, particleVelY, particleVelZ);
         }
      });
      EffectHandler.effectMap
         .put(2285, (mcInstance, world, player, x, y, z, data) -> world.playSound(x, y, z, "random.burp", 0.25F, world.rand.nextFloat() * 0.3F + 1.0F));
   }
}
