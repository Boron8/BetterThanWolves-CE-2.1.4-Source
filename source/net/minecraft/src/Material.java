package net.minecraft.src;

public class Material {
   public static final Material air = new MaterialTransparent(MapColor.airColor);
   public static final Material grass = new Material(MapColor.grassColor).setRequiresTool();
   public static final Material ground = new Material(MapColor.dirtColor).setRequiresTool();
   public static final Material wood = new Material(MapColor.woodColor).setBurning().setMobsCantSpawnOn().setAxesEfficientOn().setDoesNotBreakSaw();
   public static final Material rock = new Material(MapColor.stoneColor).setRequiresTool();
   public static final Material iron = new Material(MapColor.ironColor).setRequiresTool();
   public static final Material anvil = new Material(MapColor.ironColor).setRequiresTool().setImmovableMobility();
   public static final Material water = new MaterialLiquid(MapColor.waterColor).n();
   public static final Material lava = new MaterialLiquid(MapColor.tntColor).n();
   public static final Material leaves = new Material(MapColor.foliageColor)
      .setBurning()
      .setTranslucent()
      .setNoPushMobility()
      .setAxesEfficientOn()
      .setAxesTreatAsVegetation()
      .setDoesNotBreakSaw();
   public static final Material plants = new MaterialLogic(MapColor.foliageColor).n().setAxesEfficientOn().setAxesTreatAsVegetation().setDoesNotBreakSaw();
   public static final Material vine = new MaterialLogic(MapColor.foliageColor)
      .g()
      .setNoPushMobility()
      .setReplaceable()
      .setAxesEfficientOn()
      .setAxesTreatAsVegetation()
      .setDoesNotBreakSaw();
   public static final Material sponge = new Material(MapColor.clothColor);
   public static final Material cloth = new Material(MapColor.clothColor).setBurning().setAxesEfficientOn().setDoesNotBreakSaw().setMobsCantSpawnOn();
   public static final Material fire = new MaterialTransparent(MapColor.airColor).n();
   public static final Material sand = new Material(MapColor.sandColor).setRequiresTool();
   public static final Material circuits = new MaterialLogic(MapColor.airColor).n();
   public static final Material glass = new Material(MapColor.airColor).setTranslucent().setAlwaysHarvested();
   public static final Material redstoneLight = new Material(MapColor.airColor).setAlwaysHarvested();
   public static final Material tnt = new Material(MapColor.tntColor).setBurning().setTranslucent();
   public static final Material coral = new Material(MapColor.foliageColor).setNoPushMobility();
   public static final Material ice = new Material(MapColor.iceColor).setTranslucent().setAlwaysHarvested();
   public static final Material snow = new MaterialLogic(MapColor.snowColor).i().setTranslucent().setRequiresTool().setNoPushMobility().setDoesNotBreakSaw();
   public static final Material craftedSnow = new Material(MapColor.snowColor).setRequiresTool().setDoesNotBreakSaw();
   public static final Material cactus = new Material(MapColor.foliageColor).setTranslucent().setNoPushMobility().setMobsCantSpawnOn().setDoesNotBreakSaw();
   public static final Material clay = new Material(MapColor.clayColor);
   public static final Material pumpkin = new Material(MapColor.foliageColor).setNoPushMobility().setAxesEfficientOn().setDoesNotBreakSaw();
   public static final Material dragonEgg = new Material(MapColor.foliageColor).setNoPushMobility();
   public static final Material portal = new MaterialPortal(MapColor.airColor).o();
   public static final Material cake = new Material(MapColor.airColor).setNoPushMobility();
   public static final Material web = new MaterialWeb(MapColor.clothColor).f().setNoPushMobility();
   public static final Material piston = new Material(MapColor.stoneColor).setImmovableMobility();
   private boolean canBurn;
   private boolean replaceable;
   private boolean isTranslucent;
   public final MapColor materialMapColor;
   private boolean requiresNoTool = true;
   private int mobilityFlag;
   private boolean field_85159_M;
   private boolean canMobsSpawnOn = true;
   private boolean canNetherMobsSpawnOn = false;
   private boolean axesEfficientOn = false;
   private boolean axesTreatAsVegetation = false;
   private boolean breaksSaw = true;

   public Material(MapColor par1MapColor) {
      this.materialMapColor = par1MapColor;
   }

   public boolean isLiquid() {
      return false;
   }

   public boolean isSolid() {
      return true;
   }

   public boolean getCanBlockGrass() {
      return true;
   }

   public boolean blocksMovement() {
      return true;
   }

   public Material setTranslucent() {
      this.isTranslucent = true;
      return this;
   }

   public Material setRequiresTool() {
      this.requiresNoTool = false;
      return this;
   }

   public Material setBurning() {
      this.canBurn = true;
      return this;
   }

   public boolean getCanBurn() {
      return this.canBurn;
   }

   public Material setReplaceable() {
      this.replaceable = true;
      return this;
   }

   public boolean isReplaceable() {
      return this.replaceable;
   }

   public boolean isOpaque() {
      return this.isTranslucent ? false : this.blocksMovement();
   }

   public boolean isToolNotRequired() {
      return this.requiresNoTool;
   }

   public int getMaterialMobility() {
      return this.mobilityFlag;
   }

   public Material setNoPushMobility() {
      this.mobilityFlag = 1;
      return this;
   }

   protected Material setImmovableMobility() {
      this.mobilityFlag = 2;
      return this;
   }

   protected Material setAlwaysHarvested() {
      this.field_85159_M = true;
      return this;
   }

   public boolean isAlwaysHarvested() {
      return this.field_85159_M;
   }

   public boolean getMobsCanSpawnOn(int iDimension) {
      return iDimension == -1 ? this.canNetherMobsSpawnOn : this.canMobsSpawnOn;
   }

   public Material setMobsCantSpawnOn() {
      this.canMobsSpawnOn = false;
      return this;
   }

   public Material setNetherMobsCanSpawnOn() {
      this.canNetherMobsSpawnOn = true;
      return this;
   }

   public boolean getAxesEfficientOn() {
      return this.axesEfficientOn;
   }

   public Material setAxesEfficientOn() {
      this.axesEfficientOn = true;
      return this;
   }

   public boolean getAxesTreatAsVegetation() {
      return this.axesTreatAsVegetation;
   }

   public Material setAxesTreatAsVegetation() {
      this.axesTreatAsVegetation = true;
      return this;
   }

   public boolean breaksSaw() {
      return this.breaksSaw;
   }

   public Material setDoesNotBreakSaw() {
      this.breaksSaw = false;
      return this;
   }
}
