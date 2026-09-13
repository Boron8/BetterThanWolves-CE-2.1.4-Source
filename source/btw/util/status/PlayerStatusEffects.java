package btw.util.status;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.src.Potion;

public class PlayerStatusEffects {
   public static Map<StatusCategory, Map<Integer, StatusEffect>> STATUS_EFFECT_LIST = new HashMap<>();
   public static final StatusEffect GLOOM = createGloomEffect(1, "gloom").build();
   public static final StatusEffect DREAD = createGloomEffect(2, "dread").build();
   public static final StatusEffect TERROR = createGloomEffect(3, "terror").build();
   public static final int HUNGER_SCALAR = 6;
   public static final int HUNGER_EFFECT_START = 24;
   public static final StatusEffect PECKISH = createHungerEffect(1, 1.0F, "peckish").build();
   public static final StatusEffect HUNGRY = createHungerEffect(2, 0.75F, "hungry").build();
   public static final StatusEffect FAMISHED = createHungerEffect(3, 0.5F, "famished").setPreventsJumping().build();
   public static final StatusEffect EMACIATED = createHungerEffect(4, 0.25F, "emaciated").setPreventsJumping().build();
   public static final StatusEffect STARVING = createHungerEffect(5, 0.25F, "starving")
      .setPreventsJumping()
      .setPotionEffect(Potion.confusion.getId(), 0)
      .build();
   public static final int HEALTH_SCALAR = 2;
   public static final int HEALTH_EFFECT_START = 10;
   public static final StatusEffect HURT = createHealthEffect(1, 1.0F, "hurt").build();
   public static final StatusEffect INJURED = createHealthEffect(2, 0.75F, "injured").build();
   public static final StatusEffect WOUNDED = createHealthEffect(3, 0.5F, "wounded").build();
   public static final StatusEffect CRIPPLED = createHealthEffect(4, 0.25F, "crippled").setPreventsJumping().build();
   public static final StatusEffect DYING = createHealthEffect(5, 0.25F, "dying").setPreventsJumping().setPotionEffect(Potion.blindness.getId(), 0).build();
   public static final int FAT_SCALAR = 2;
   public static final int FAT_EFFECT_START = 12;
   public static final StatusEffect PLUMP = createFatEffect(1, 1.0F, "plump").build();
   public static final StatusEffect CHUBBY = createFatEffect(2, 0.75F, "chubby").build();
   public static final StatusEffect FAT = createFatEffect(3, 0.5F, "fat").build();
   public static final StatusEffect OBESE = createFatEffect(4, 0.25F, "obese").setPreventsJumping().build();

   private static StatusEffectBuilder createGloomEffect(int level, String name) {
      return new StatusEffectBuilder(level, BTWStatusCategory.GLOOM)
         .setEffectivenessMultiplier(0.5F)
         .setAffectsMiningSpeed()
         .setAffectsMovement()
         .setEffectsMultiplicative()
         .setUnlocalizedName(BTWStatusCategory.GLOOM.getName(), name)
         .setEvaluator(player -> player.getGloomLevel() == level);
   }

   private static StatusEffectBuilder createHungerEffect(int level, float effectivenessMultiplier, String name) {
      return createExhaustionEffect(level, BTWStatusCategory.HUNGER, effectivenessMultiplier, name).setEvaluator(player -> {
         if (player.capabilities.isCreativeMode) {
            return false;
         } else {
            int hungerStart = 24 - player.worldObj.getDifficulty().getStatusEffectOffset();
            int maxHunger = hungerStart - player.worldObj.getDifficulty().getStatusEffectStageGap() * 6 * (level - 1);
            int minHunger = hungerStart - player.worldObj.getDifficulty().getStatusEffectStageGap() * 6 * level;
            int hungerLevel = player.foodStats.getFoodLevel();
            return maxHunger >= hungerLevel && minHunger < hungerLevel;
         }
      });
   }

   private static StatusEffectBuilder createHealthEffect(int level, float effectivenessMultiplier, String name) {
      return createExhaustionEffect(level, BTWStatusCategory.HEALTH, effectivenessMultiplier, name).setEvaluator(player -> {
         if (player.capabilities.isCreativeMode) {
            return false;
         } else {
            int healthStart = 10 - player.worldObj.getDifficulty().getStatusEffectOffset();
            int maxHealth = healthStart - player.worldObj.getDifficulty().getStatusEffectStageGap() * 2 * (level - 1);
            int minHealth = healthStart - player.worldObj.getDifficulty().getStatusEffectStageGap() * 2 * level;
            int healthLevel = player.aX();
            return maxHealth >= healthLevel && minHealth < healthLevel;
         }
      });
   }

   private static StatusEffectBuilder createFatEffect(int level, float effectivenessMultiplier, String name) {
      return createExhaustionEffect(level, BTWStatusCategory.FAT, effectivenessMultiplier, name).setEvaluator(player -> {
         if (player.capabilities.isCreativeMode) {
            return false;
         } else {
            float fatStart = 12 + player.worldObj.getDifficulty().getStatusEffectOffset();
            float maxFat = fatStart + player.worldObj.getDifficulty().getStatusEffectStageGap() * 2 * (level - 1);
            float minFat = fatStart + player.worldObj.getDifficulty().getStatusEffectStageGap() * 2 * level;
            float fatLevel = player.foodStats.getSaturationLevel();
            return maxFat >= fatLevel && minFat < fatLevel;
         }
      });
   }

   private static StatusEffectBuilder createExhaustionEffect(int level, StatusCategory category, float effectivenessMultiplier, String name) {
      return new StatusEffectBuilder(level, category)
         .setEffectivenessMultiplier(effectivenessMultiplier)
         .setAffectsAttackDamage()
         .setAffectsMiningSpeed()
         .setAffectsMovement()
         .setPreventsSprinting()
         .setUnlocalizedName(category.getName(), name);
   }
}
