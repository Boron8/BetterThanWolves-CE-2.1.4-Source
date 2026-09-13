package net.minecraft.src;

import btw.entity.mob.BatEntity;
import btw.entity.mob.ChickenEntity;
import btw.entity.mob.CowEntity;
import btw.entity.mob.CreeperEntity;
import btw.entity.mob.EndermanEntity;
import btw.entity.mob.PigEntity;
import btw.entity.mob.SheepEntity;
import btw.entity.mob.SkeletonEntity;
import btw.entity.mob.SlimeEntity;
import btw.entity.mob.SpiderEntity;
import btw.entity.mob.SquidEntity;
import btw.entity.mob.ZombieEntity;
import btw.world.biome.DesertBiome;
import btw.world.biome.EndBiome;
import btw.world.biome.ForestBiome;
import btw.world.biome.HillsBiome;
import btw.world.biome.JungleBiome;
import btw.world.biome.NetherBiome;
import btw.world.biome.SnowBiome;
import btw.world.biome.TaigaBiome;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

public abstract class BiomeGenBase {
   public static final BiomeGenBase[] biomeList = new BiomeGenBase[256];
   public static final BiomeGenBase ocean = new BiomeGenOcean(0).b(112).setBiomeName("Ocean").setMinMaxHeight(-1.0F, 0.4F);
   public static final BiomeGenBase plains = new BiomeGenPlains(1).b(9286496).setBiomeName("Plains").setTemperatureRainfall(0.8F, 0.4F);
   public static final BiomeGenBase desert = new DesertBiome(2)
      .b(16421912)
      .setBiomeName("Desert")
      .setDisableRain()
      .setTemperatureRainfall(2.0F, 0.0F)
      .setMinMaxHeight(0.1F, 0.2F);
   public static final BiomeGenBase extremeHills = new HillsBiome(3)
      .b(6316128)
      .setBiomeName("Extreme Hills")
      .setMinMaxHeight(0.3F, 1.5F)
      .setTemperatureRainfall(0.2F, 0.3F);
   public static final BiomeGenBase forest = new ForestBiome(4).b(353825).setBiomeName("Forest").func_76733_a(5159473).setTemperatureRainfall(0.7F, 0.8F);
   public static final BiomeGenBase taiga = new TaigaBiome(5)
      .b(747097)
      .setBiomeName("Taiga")
      .func_76733_a(5159473)
      .setEnableSnow()
      .setTemperatureRainfall(0.05F, 0.8F)
      .setMinMaxHeight(0.1F, 0.4F);
   public static final BiomeGenBase swampland = new BiomeGenSwamp(6)
      .b(522674)
      .setBiomeName("Swampland")
      .func_76733_a(9154376)
      .setMinMaxHeight(-0.2F, 0.1F)
      .setTemperatureRainfall(0.8F, 0.9F);
   public static final BiomeGenBase river = new BiomeGenRiver(7).b(255).setBiomeName("River").setMinMaxHeight(-0.5F, 0.0F);
   public static final BiomeGenBase hell = new NetherBiome(8).b(16711680).setBiomeName("Hell").setDisableRain().setTemperatureRainfall(2.0F, 0.0F);
   public static final BiomeGenBase sky = new EndBiome(9).b(8421631).setBiomeName("Sky").setDisableRain();
   public static final BiomeGenBase frozenOcean = new BiomeGenOcean(10)
      .b(9474208)
      .setBiomeName("FrozenOcean")
      .setEnableSnow()
      .setMinMaxHeight(-1.0F, 0.5F)
      .setTemperatureRainfall(0.0F, 0.5F);
   public static final BiomeGenBase frozenRiver = new BiomeGenRiver(11)
      .b(10526975)
      .setBiomeName("FrozenRiver")
      .setEnableSnow()
      .setMinMaxHeight(-0.5F, 0.0F)
      .setTemperatureRainfall(0.0F, 0.5F);
   public static final BiomeGenBase icePlains = new SnowBiome(12).b(16777215).setBiomeName("Ice Plains").setEnableSnow().setTemperatureRainfall(0.0F, 0.5F);
   public static final BiomeGenBase iceMountains = new SnowBiome(13)
      .b(10526880)
      .setBiomeName("Ice Mountains")
      .setEnableSnow()
      .setMinMaxHeight(0.3F, 1.3F)
      .setTemperatureRainfall(0.0F, 0.5F);
   public static final BiomeGenBase mushroomIsland = new BiomeGenMushroomIsland(14)
      .b(16711935)
      .setBiomeName("MushroomIsland")
      .setTemperatureRainfall(0.9F, 1.0F)
      .setMinMaxHeight(0.2F, 1.0F);
   public static final BiomeGenBase mushroomIslandShore = new BiomeGenMushroomIsland(15)
      .b(10486015)
      .setBiomeName("MushroomIslandShore")
      .setTemperatureRainfall(0.9F, 1.0F)
      .setMinMaxHeight(-1.0F, 0.1F);
   public static final BiomeGenBase beach = new BiomeGenBeach(16)
      .b(16440917)
      .setBiomeName("Beach")
      .setTemperatureRainfall(0.8F, 0.4F)
      .setMinMaxHeight(0.0F, 0.1F);
   public static final BiomeGenBase desertHills = new DesertBiome(17)
      .b(13786898)
      .setBiomeName("DesertHills")
      .setDisableRain()
      .setTemperatureRainfall(2.0F, 0.0F)
      .setMinMaxHeight(0.3F, 0.8F);
   public static final BiomeGenBase forestHills = new ForestBiome(18)
      .b(2250012)
      .setBiomeName("ForestHills")
      .func_76733_a(5159473)
      .setTemperatureRainfall(0.7F, 0.8F)
      .setMinMaxHeight(0.3F, 0.7F);
   public static final BiomeGenBase taigaHills = new TaigaBiome(19)
      .b(1456435)
      .setBiomeName("TaigaHills")
      .setEnableSnow()
      .func_76733_a(5159473)
      .setTemperatureRainfall(0.05F, 0.8F)
      .setMinMaxHeight(0.3F, 0.8F);
   public static final BiomeGenBase extremeHillsEdge = new HillsBiome(20)
      .b(7501978)
      .setBiomeName("Extreme Hills Edge")
      .setMinMaxHeight(0.2F, 0.8F)
      .setTemperatureRainfall(0.2F, 0.3F);
   public static final BiomeGenBase jungle = new JungleBiome(21)
      .b(5470985)
      .setBiomeName("Jungle")
      .func_76733_a(5470985)
      .setTemperatureRainfall(1.2F, 0.9F)
      .setMinMaxHeight(0.2F, 0.4F);
   public static final BiomeGenBase jungleHills = new JungleBiome(22)
      .b(2900485)
      .setBiomeName("JungleHills")
      .func_76733_a(5470985)
      .setTemperatureRainfall(1.2F, 0.9F)
      .setMinMaxHeight(1.8F, 0.5F);
   public String biomeName;
   public int color;
   public byte topBlock = (byte)Block.grass.blockID;
   public byte fillerBlock = (byte)Block.dirt.blockID;
   public int field_76754_C = 5169201;
   public float minHeight = 0.1F;
   public float maxHeight = 0.3F;
   public float temperature = 0.5F;
   public float rainfall = 0.5F;
   public int waterColorMultiplier = 16777215;
   public BiomeDecorator theBiomeDecorator;
   protected List spawnableMonsterList = new ArrayList();
   protected List spawnableCreatureList = new ArrayList();
   protected List spawnableWaterCreatureList = new ArrayList();
   protected List spawnableCaveCreatureList = new ArrayList();
   private boolean enableSnow;
   private boolean enableRain = true;
   public final int biomeID;
   protected WorldGenTrees worldGeneratorTrees = new WorldGenTrees(false);
   protected WorldGenBigTree worldGeneratorBigTree = new WorldGenBigTree(false);
   protected WorldGenForest worldGeneratorForest = new WorldGenForest(false);
   protected WorldGenSwamp worldGeneratorSwamp = new WorldGenSwamp();

   protected BiomeGenBase(int par1) {
      this.biomeID = par1;
      biomeList[par1] = this;
      this.theBiomeDecorator = this.createBiomeDecorator();
      this.spawnableCreatureList.add(new SpawnListEntry(SheepEntity.class, 12, 4, 4));
      this.spawnableCreatureList.add(new SpawnListEntry(PigEntity.class, 10, 4, 4));
      this.spawnableCreatureList.add(new SpawnListEntry(ChickenEntity.class, 10, 4, 4));
      this.spawnableCreatureList.add(new SpawnListEntry(CowEntity.class, 8, 4, 4));
      this.spawnableMonsterList.add(new SpawnListEntry(SpiderEntity.class, 10, 4, 4));
      this.spawnableMonsterList.add(new SpawnListEntry(ZombieEntity.class, 10, 4, 4));
      this.spawnableMonsterList.add(new SpawnListEntry(SkeletonEntity.class, 10, 4, 4));
      this.spawnableMonsterList.add(new SpawnListEntry(CreeperEntity.class, 10, 4, 4));
      this.spawnableMonsterList.add(new SpawnListEntry(SlimeEntity.class, 10, 4, 4));
      this.spawnableMonsterList.add(new SpawnListEntry(EndermanEntity.class, 1, 1, 4));
      this.spawnableWaterCreatureList.add(new SpawnListEntry(SquidEntity.class, 10, 4, 4));
      this.spawnableCaveCreatureList.add(new SpawnListEntry(BatEntity.class, 10, 8, 8));
   }

   protected BiomeDecorator createBiomeDecorator() {
      return new BiomeDecorator(this);
   }

   private BiomeGenBase setTemperatureRainfall(float par1, float par2) {
      if (par1 > 0.1F && par1 < 0.2F) {
         throw new IllegalArgumentException("Please avoid temperatures in the range 0.1 - 0.2 because of snow");
      } else {
         this.temperature = par1;
         this.rainfall = par2;
         return this;
      }
   }

   private BiomeGenBase setMinMaxHeight(float par1, float par2) {
      this.minHeight = par1;
      this.maxHeight = par2;
      return this;
   }

   private BiomeGenBase setDisableRain() {
      this.enableRain = false;
      return this;
   }

   public WorldGenerator getRandomWorldGenForTrees(Random par1Random) {
      return (WorldGenerator)(par1Random.nextInt(10) == 0 ? this.worldGeneratorBigTree : this.worldGeneratorTrees);
   }

   public WorldGenerator getRandomWorldGenForGrass(Random par1Random) {
      return new WorldGenTallGrass(Block.tallGrass.blockID, 1);
   }

   protected BiomeGenBase setEnableSnow() {
      this.enableSnow = true;
      return this;
   }

   protected BiomeGenBase setBiomeName(String par1Str) {
      this.biomeName = par1Str;
      return this;
   }

   protected BiomeGenBase func_76733_a(int par1) {
      this.field_76754_C = par1;
      return this;
   }

   protected BiomeGenBase setColor(int par1) {
      this.color = par1;
      return this;
   }

   @Environment(EnvType.CLIENT)
   public int getSkyColorByTemp(float par1) {
      par1 /= 3.0F;
      if (par1 < -1.0F) {
         par1 = -1.0F;
      }

      if (par1 > 1.0F) {
         par1 = 1.0F;
      }

      return Color.getHSBColor(0.62222224F - par1 * 0.05F, 0.5F + par1 * 0.1F, 1.0F).getRGB();
   }

   public List getSpawnableList(EnumCreatureType par1EnumCreatureType) {
      return par1EnumCreatureType == EnumCreatureType.monster
         ? this.spawnableMonsterList
         : (
            par1EnumCreatureType == EnumCreatureType.creature
               ? this.spawnableCreatureList
               : (
                  par1EnumCreatureType == EnumCreatureType.waterCreature
                     ? this.spawnableWaterCreatureList
                     : (par1EnumCreatureType == EnumCreatureType.ambient ? this.spawnableCaveCreatureList : null)
               )
         );
   }

   public boolean getEnableSnow() {
      return this.enableSnow;
   }

   public boolean isHighHumidity() {
      return this.rainfall > 0.85F;
   }

   public float getSpawningChance() {
      return 0.1F;
   }

   public final int getIntRainfall() {
      return (int)(this.rainfall * 65536.0F);
   }

   public final int getIntTemperature() {
      return (int)(this.temperature * 65536.0F);
   }

   public final float getFloatRainfall() {
      return this.rainfall;
   }

   public final float getFloatTemperature() {
      return this.temperature;
   }

   public void decorate(World par1World, Random par2Random, int par3, int par4) {
      this.theBiomeDecorator.decorate(par1World, par2Random, par3, par4);
   }

   @Environment(EnvType.CLIENT)
   public int getBiomeGrassColor() {
      double var1 = MathHelper.clamp_float(this.getFloatTemperature(), 0.0F, 1.0F);
      double var3 = MathHelper.clamp_float(this.getFloatRainfall(), 0.0F, 1.0F);
      return ColorizerGrass.getGrassColor(var1, var3);
   }

   @Environment(EnvType.CLIENT)
   public int getBiomeFoliageColor() {
      double var1 = MathHelper.clamp_float(this.getFloatTemperature(), 0.0F, 1.0F);
      double var3 = MathHelper.clamp_float(this.getFloatRainfall(), 0.0F, 1.0F);
      return ColorizerFoliage.getFoliageColor(var1, var3);
   }

   public boolean canRainInBiome() {
      return this.enableSnow ? false : this.enableRain;
   }

   public boolean canLightningStrikeInBiome() {
      return this.canRainInBiome();
   }

   public boolean canSlimesSpawnOnSurface() {
      return false;
   }

   public boolean canSnowAt(World world, int x, int y, int z) {
      return this.getEnableSnow();
   }

   static {
      WorldGenReed.addBiomeToGenerator(swampland);
      WorldGenReed.addBiomeToGenerator(jungle);
      WorldGenReed.addBiomeToGenerator(jungleHills);
      WorldGenReed.addBiomeToGenerator(river);
      WorldGenPumpkin.addBiomeToGenerator(plains);
      ComponentVillageStartPiece.addDesertBiome(desert);
      StructureScatteredFeatureStart.addDesertBiome(desert);
      StructureScatteredFeatureStart.addDesertBiome(desertHills);
      StructureScatteredFeatureStart.addJungleBiome(jungle);
      StructureScatteredFeatureStart.addJungleBiome(jungleHills);
      StructureScatteredFeatureStart.addSwampBiome(swampland);
   }
}
