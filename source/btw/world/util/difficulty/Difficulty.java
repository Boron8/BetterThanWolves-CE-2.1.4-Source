package btw.world.util.difficulty;

import net.minecraft.src.StringTranslate;

public class Difficulty {
   public final String NAME;
   public final int ID;

   public Difficulty(String name) {
      this.NAME = name;
      this.ID = Difficulties.DIFFICULTY_LIST.size();
      Difficulties.DIFFICULTY_LIST.add(this);
   }

   public String getLocalizedName() {
      return StringTranslate.getInstance().translateKey("difficulty." + this.NAME + ".name");
   }

   public float getHungerIntensiveActionCostMultiplier() {
      return 1.0F;
   }

   public boolean shouldNetherCoalTorchesStartFires() {
      return true;
   }

   public float getNoToolBlockHardnessMultiplier() {
      return 1.0F;
   }

   public boolean shouldBurningMobsDropCookedMeat() {
      return false;
   }

   public float getCowKickStrengthMultiplier() {
      return 1.0F;
   }

   public boolean canMilkingStartleCows() {
      return true;
   }

   public boolean canAnimalsStarve() {
      return true;
   }

   public boolean shouldBlocksStartleAnimals() {
      return true;
   }

   public boolean areJungleSpidersHostile() {
      return true;
   }

   public boolean shouldReduceJungleSpiderFoodPoisoning() {
      return false;
   }

   public boolean shouldSquidsAttackDryPlayers() {
      return true;
   }

   public boolean shouldGhastsAngerPigmen() {
      return true;
   }

   public int getDeathCountBeforeItemDestruction() {
      return 1;
   }

   public float getHealthRegenDelayMultiplier() {
      return 1.0F;
   }

   public int getStatusEffectOffset() {
      return 0;
   }

   public int getStatusEffectStageGap() {
      return 1;
   }

   public boolean allowsPlacingBlocksInAir() {
      return false;
   }

   public boolean canWeedsKillPlants() {
      return true;
   }

   public boolean shouldLightningStartFires() {
      return true;
   }

   public float getAbandonmentRangeMultiplier() {
      return 1.0F;
   }

   public boolean shouldHCSRangeIncrease() {
      return true;
   }
}
