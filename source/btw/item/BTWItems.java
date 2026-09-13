package btw.item;

import btw.BTWMod;
import btw.block.BTWBlocks;
import btw.block.blocks.PlanksBlock;
import btw.crafting.util.FurnaceBurnTime;
import btw.item.blockitems.AestheticEarthBlockItem;
import btw.item.blockitems.AestheticNonOpaqueBlockItem;
import btw.item.blockitems.AestheticOpaqueBlockItem;
import btw.item.blockitems.AestheticVegetationBlockItem;
import btw.item.blockitems.AnvilBlockItem;
import btw.item.blockitems.BloodWoodLogBlockItem;
import btw.item.blockitems.CompanionCubeBlockItem;
import btw.item.blockitems.DamageToMetadataBlockItem;
import btw.item.blockitems.DirtSlabBlockItem;
import btw.item.blockitems.DormantSoulforgeBlockItem;
import btw.item.blockitems.FiniteBurningTorchBlockItem;
import btw.item.blockitems.FiniteUnlitTorchBlockItem;
import btw.item.blockitems.FreshPumpkinBlockItem;
import btw.item.blockitems.GrassSlabBlockITem;
import btw.item.blockitems.IceBlockItem;
import btw.item.blockitems.InfiniteBurningTorchBlockItem;
import btw.item.blockitems.InfinteUnlitTorchBlockItem;
import btw.item.blockitems.LeavesBlockItem;
import btw.item.blockitems.LeverBlockItem;
import btw.item.blockitems.LogBlockItem;
import btw.item.blockitems.LooseDirtSlabBlockItem;
import btw.item.blockitems.LooseSnowSlabBlockItem;
import btw.item.blockitems.MillstoneBlockItem;
import btw.item.blockitems.MiningChargeBlockItem;
import btw.item.blockitems.MouldingAndDecorativeBlockItem;
import btw.item.blockitems.MouldingBlockItem;
import btw.item.blockitems.MyceliumSlabBlockItem;
import btw.item.blockitems.PlanterBlockItem;
import btw.item.blockitems.SandAndGravelSlabBlockItem;
import btw.item.blockitems.SaplingBlockItem;
import btw.item.blockitems.SidingAndCornerBlockItem;
import btw.item.blockitems.SlabBlockItem;
import btw.item.blockitems.SlabWithMetadataBlockItem;
import btw.item.blockitems.SnowBlockItem;
import btw.item.blockitems.UnfiredPotteryBlockItem;
import btw.item.blockitems.VaseBlockItem;
import btw.item.blockitems.WhiteStoneStairsBlockItem;
import btw.item.blockitems.WoodCornerStubBlockItem;
import btw.item.blockitems.WoodMouldingDecorativeStubBlockItem;
import btw.item.blockitems.WoodMouldingStubBlockItem;
import btw.item.blockitems.WoodSidingDecorativeStubBlockItem;
import btw.item.blockitems.WoodSidingStubBlockItem;
import btw.item.blockitems.WoolSlabBlockItem;
import btw.item.blockitems.legacy.LegacyCornerBlockItem;
import btw.item.blockitems.legacy.LegacySidingBlockItem;
import btw.item.blockitems.legacy.LegacySubstitutionBlockItem;
import btw.item.blockitems.legacy.LegacyTorchBlockItem;
import btw.item.items.AncientProphecyItem;
import btw.item.items.ArcaneScrollItem;
import btw.item.items.ArmorItemGimpSuit;
import btw.item.items.ArmorItemPadded;
import btw.item.items.ArmorItemSpecial;
import btw.item.items.ArmorItemSteel;
import btw.item.items.ArmorItemTanned;
import btw.item.items.ArmorItemWood;
import btw.item.items.AxeItemSteel;
import btw.item.items.BarkItem;
import btw.item.items.BattleAxeItem;
import btw.item.items.BedItem;
import btw.item.items.BoneCarvingItem;
import btw.item.items.BreedingHarnessItem;
import btw.item.items.BroadheadArrowItem;
import btw.item.items.BucketItemCement;
import btw.item.items.BucketItemMilkChocolate;
import btw.item.items.CandleItem;
import btw.item.items.CanvasItem;
import btw.item.items.ChiselItemDiamond;
import btw.item.items.ChiselItemIron;
import btw.item.items.ChiselItemStone;
import btw.item.items.ChiselItemWood;
import btw.item.items.ClubItem;
import btw.item.items.ClubItemWood;
import btw.item.items.CocoaBeanItem;
import btw.item.items.CompositeBowItem;
import btw.item.items.CookedMysteryMeatItem;
import btw.item.items.CookedWolfChopItem;
import btw.item.items.CorpseEyeItem;
import btw.item.items.CreeperOysterItem;
import btw.item.items.CuredFoodItem;
import btw.item.items.DirtPileItem;
import btw.item.items.DungItem;
import btw.item.items.DynamiteItem;
import btw.item.items.FireStarterItemPrimitive;
import btw.item.items.FishingRodItemBaited;
import btw.item.items.FoodItem;
import btw.item.items.FoulFoodItem;
import btw.item.items.GravelPileItem;
import btw.item.items.HardBoiledEggItem;
import btw.item.items.HighResolutionFoodItem;
import btw.item.items.HoeItemSteel;
import btw.item.items.KnittingItem;
import btw.item.items.KnittingNeedlesItem;
import btw.item.items.MattockItem;
import btw.item.items.MouldItem;
import btw.item.items.MushroomItem;
import btw.item.items.MysteriousGlandItem;
import btw.item.items.NameTagItem;
import btw.item.items.NetherBrickItem;
import btw.item.items.NetherGrothSporesItem;
import btw.item.items.NetherSludgeItem;
import btw.item.items.OreChunkItemGold;
import btw.item.items.OreChunkItemIron;
import btw.item.items.PickaxeItemSteel;
import btw.item.items.PlaceAsBlockItem;
import btw.item.items.RawMysteryMeatItem;
import btw.item.items.RawWolfChopItem;
import btw.item.items.RopeItem;
import btw.item.items.RottenArrowItem;
import btw.item.items.SandPileItem;
import btw.item.items.SeedFoodItem;
import btw.item.items.SeedItem;
import btw.item.items.ShearsItemDiamond;
import btw.item.items.ShovelItemSteel;
import btw.item.items.SinewExtractingItem;
import btw.item.items.SoapItem;
import btw.item.items.SoulFluxItem;
import btw.item.items.SoulSandPileItem;
import btw.item.items.SoulUrnItem;
import btw.item.items.SoupItem;
import btw.item.items.StoneBrickItem;
import btw.item.items.StoneItem;
import btw.item.items.StrawItem;
import btw.item.items.StumpRemoverItem;
import btw.item.items.SwordItemSteel;
import btw.item.items.TuningForkItem;
import btw.item.items.UnfiredBrickItem;
import btw.item.items.UnfiredNetherBrickItem;
import btw.item.items.VerticalWindMillItem;
import btw.item.items.WaterWheelItem;
import btw.item.items.WebUntanglingItem;
import btw.item.items.WheatItem;
import btw.item.items.WheatSeedsItem;
import btw.item.items.WickerPieceItem;
import btw.item.items.WickerWeavingItem;
import btw.item.items.WindMillItem;
import btw.item.items.WoolItem;
import btw.item.items.WoolKnitItem;
import btw.item.items.legacy.LegacyCandleItem;
import btw.item.items.legacy.LegacyGrateItem;
import btw.item.items.legacy.LegacySlatsItem;
import btw.item.items.legacy.LegacyWickerPaneItem;
import net.minecraft.src.Block;
import net.minecraft.src.BlockLog;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.Item;
import net.minecraft.src.ItemBlock;
import net.minecraft.src.ItemBlockWithMetadata;
import net.minecraft.src.ItemLilyPad;
import net.minecraft.src.ItemMultiTextureTile;
import net.minecraft.src.ItemReed;
import net.minecraft.src.ItemSlab;
import net.minecraft.src.Potion;
import net.minecraft.src.PotionHelper;

public class BTWItems {
   public static Item cementBucket;
   public static Item rawWolfChop;
   public static Item cookedWolfChop;
   public static Item nethercoal;
   public static Item hempSeeds;
   public static Item hemp;
   public static Item gear;
   public static Item flour;
   public static Item hempFibers;
   public static Item scouredLeather;
   public static Item donut;
   public static Item rope;
   public static Item legacySlats;
   public static Item dung;
   public static Item waterWheel;
   public static Item windMillBlade;
   public static Item windMill;
   public static Item fabric;
   public static Item legacyGrate;
   public static Item legacyWickerPane;
   public static Item tannedLeather;
   public static Item leatherStrap;
   public static Item belt;
   public static Item foulFood;
   public static Item woodenBlade;
   public static Item glue;
   public static Item tallow;
   public static Item haft;
   public static Item soulforgedSteelIngot;
   public static Item steelPickaxe;
   public static Item steelShovel;
   public static Item steelHoe;
   public static Item battleaxe;
   public static Item steelSword;
   public static Item groundNetherrack;
   public static Item hellfireDust;
   public static Item concentratedHellfire;
   public static Item steelArmorPlate;
   public static Item plateHelmet;
   public static Item plateBreastplate;
   public static Item plateLeggings;
   public static Item plateBoots;
   public static Item compositeBow;
   public static Item broadheadArrowHead;
   public static Item broadheadArrow;
   public static Item coalDust;
   public static Item padding;
   public static Item filament;
   public static Item redstoneEye;
   public static Item urn;
   public static Item soulUrn;
   public static Item hardBoiledEgg;
   public static Item potash;
   public static Item soap;
   public static Item sawDust;
   public static Item gimpHelmet;
   public static Item gimpChest;
   public static Item gimpLeggings;
   public static Item gimpBoots;
   public static Item dynamite;
   public static Item breedingHarness;
   public static Item soulDust;
   public static Item mattock;
   public static Item steelAxe;
   public static Item netherSludge;
   public static Item netherBrick;
   public static Item tuningFork;
   public static Item arcaneScroll;
   public static Item legacyCandle;
   public static Item netherGrothSpores;
   public static Item mould;
   public static Item canvas;
   public static Item kibble;
   public static Item rawEgg;
   public static Item friedEgg;
   public static Item screw;
   public static Item rottenArrow;
   public static Item ocularOfEnder;
   public static Item enderSpectacles;
   public static Item stake;
   public static Item brimstone;
   public static Item nitre;
   public static Item element;
   public static Item fuse;
   public static Item blastingOil;
   public static Item verticalWindMill;
   public static Item boiledPotato;
   public static Item rawMutton;
   public static Item cookedMutton;
   public static Item witchWart;
   public static Item cookedCarrot;
   public static Item tastySandwich;
   public static Item steakAndPotatoes;
   public static Item hamAndEggs;
   public static Item steakDinner;
   public static Item porkDinner;
   public static Item wolfDinner;
   public static Item rawKebab;
   public static Item cookedKebab;
   public static Item chickenSoup;
   public static Item chowder;
   public static Item heartyStew;
   public static Item redMushroom;
   public static Item brownMushroom;
   public static Item ironNugget;
   public static Item mail;
   public static Item rawMysteryMeat;
   public static Item cookedMysteryMeat;
   public static Item rawMushroomOmelet;
   public static Item cookedMushroomOmelet;
   public static Item rawScrambledEggs;
   public static Item cookedScrambledEggs;
   public static Item creeperOysters;
   public static Item woolHelmet;
   public static Item woolChest;
   public static Item woolLeggings;
   public static Item woolBoots;
   public static Item paddedHelmet;
   public static Item paddedChest;
   public static Item paddedLeggings;
   public static Item paddedBoots;
   public static Item tannedLeatherHelmet;
   public static Item tannedLeatherChest;
   public static Item tannedLeatherLeggings;
   public static Item tannedLeatherBoots;
   public static Item diamondIngot;
   public static Item cutLeather;
   public static Item cutTannedLeather;
   public static Item cutScouredLeather;
   public static Item baitedFishingRod;
   public static Item dirtPile;
   public static Item sandPile;
   public static Item gravelPile;
   public static Item batWing;
   public static Item goldenDung;
   public static Item bark;
   public static Item soulSandPile;
   public static Item redstoneLatch;
   public static Item steelNugget;
   public static Item wool;
   public static Item cocoaBeans;
   public static Item chocolate;
   public static Item milkChocolateBucket;
   public static Item soulFlux;
   public static Item enderSlag;
   public static Item unbakedCake;
   public static Item unbakedCookies;
   public static Item unbakedPumpkinPie;
   public static Item mysteriousGland;
   public static Item rawLiver;
   public static Item cookedLiver;
   public static Item ancientProphecy;
   public static Item stumpRemover;
   public static Item pointyStick;
   public static Item stone;
   public static Item sharpStone;
   public static Item woodenClub;
   public static Item firePlough;
   public static Item bowDrill;
   public static Item ironOreChunk;
   public static Item ironOrePile;
   public static Item ironChisel;
   public static Item diamondChisel;
   public static Item goldOreChunk;
   public static Item goldOrePile;
   public static Item wickerPane;
   public static Item knittingNeedles;
   public static Item knitting;
   public static Item woolKnit;
   public static Item boneClub;
   public static Item curedMeat;
   public static Item metalFragment;
   public static Item clayPile;
   public static Item burnedMeat;
   public static Item chickenFeed;
   public static Item boneFishHook;
   public static Item boneCarving;
   public static Item stoneBrick;
   public static Item wickerWeaving;
   public static Item wheat;
   public static Item wheatSeeds;
   public static Item breadDough;
   public static Item straw;
   public static Item unfiredBrick;
   public static Item unfiredNetherBrick;
   public static Item nameTag;
   public static Item diamondShears;
   public static Item sugarCaneRoots;
   public static Item carrot;
   public static Item carrotSeeds;
   public static Item diamondArmorPlate;
   public static Item mashedMelon;
   public static Item bedroll;
   public static Item candle;
   public static Item corpseEye;
   public static Item tangledWeb;
   public static Item webUntangling;
   public static Item sinew;
   public static Item sinewExtractingBeef;
   public static Item sinewExtractingWolf;
   private static int fcBlockDirtSlabID = 206;
   public static int woodSidingStubID;
   public static int woodMouldingStubID;
   public static int woodCornerStubID;
   public static int woodMouldingDecorativeStubID;
   public static int woodSidingDecorativeStubID;

   public static void instantiateModItems() {
      cementBucket = new BucketItemCement(BTWMod.instance.parseID("fcBucketCementID"));
      rawWolfChop = new RawWolfChopItem(BTWMod.instance.parseID("fcWolfRawID"));
      cookedWolfChop = new CookedWolfChopItem(BTWMod.instance.parseID("fcWolfCookedID"));
      nethercoal = new Item(BTWMod.instance.parseID("fcNethercoalID"))
         .setfurnaceburntime(2 * FurnaceBurnTime.COAL.burnTime)
         .setFilterableProperties(2)
         .setUnlocalizedName("fcItemNethercoal")
         .setCreativeTab(CreativeTabs.tabMaterials);
      hempSeeds = new SeedItem(BTWMod.instance.parseID("fcHempSeedsID"), BTWBlocks.hempCrop.blockID)
         .setAsBasicChickenFood()
         .setUnlocalizedName("fcItemSeedsHemp");
      hemp = new Item(BTWMod.instance.parseID("fcHempID"))
         .setBuoyant()
         .setBellowsBlowDistance(2)
         .setIncineratedInCrucible()
         .setFilterableProperties(16)
         .setUnlocalizedName("fcItemHemp")
         .setCreativeTab(CreativeTabs.tabMaterials);
      gear = new Item(BTWMod.instance.parseID("fcGearID"))
         .setBuoyant()
         .setIncineratedInCrucible()
         .setUnlocalizedName("fcItemGear")
         .setCreativeTab(CreativeTabs.tabMaterials);
      flour = new Item(BTWMod.instance.parseID("fcFlourID"))
         .setBuoyant()
         .setBellowsBlowDistance(3)
         .setIncineratedInCrucible()
         .setFilterableProperties(8)
         .setUnlocalizedName("fcItemFlour")
         .setCreativeTab(CreativeTabs.tabMaterials);
      hempFibers = new Item(BTWMod.instance.parseID("fcHempFibersID"))
         .setBuoyant()
         .setBellowsBlowDistance(2)
         .setIncineratedInCrucible()
         .setFilterableProperties(18)
         .setUnlocalizedName("fcItemFibersHemp")
         .setCreativeTab(CreativeTabs.tabMaterials);
      scouredLeather = new Item(BTWMod.instance.parseID("fcScouredLeatherID"))
         .setBuoyant()
         .setIncineratedInCrucible()
         .setFilterableProperties(16)
         .setUnlocalizedName("fcItemLeatherScoured")
         .setCreativeTab(CreativeTabs.tabMaterials);
      donut = new FoodItem(BTWMod.instance.parseID("fcDonutID"), 1, 0.5F, false, "fcItemDonut").j().setFilterableProperties(2);
      rope = new RopeItem(BTWMod.instance.parseID("fcRopeItemID"))
         .setIncineratedInCrucible()
         .setFilterableProperties(4)
         .setCreativeTab(CreativeTabs.tabTransport);
      legacySlats = new LegacySlatsItem(BTWMod.instance.parseID("fcRollersItemID"));
      dung = new DungItem(BTWMod.instance.parseID("fcDungID"));
      waterWheel = new WaterWheelItem(BTWMod.instance.parseID("fcWaterWheelItemID")).setIncineratedInCrucible();
      windMillBlade = new Item(BTWMod.instance.parseID("fcWindMillBladeItemID"))
         .setBuoyant()
         .setIncineratedInCrucible()
         .setMaxStackSize(1)
         .setFull3D()
         .setUnlocalizedName("fcItemBladeWindMill")
         .setCreativeTab(CreativeTabs.tabMaterials);
      windMill = new WindMillItem(BTWMod.instance.parseID("fcWindMillItemID")).setIncineratedInCrucible();
      fabric = new Item(BTWMod.instance.parseID("fcHempClothID"))
         .setBuoyant()
         .setBellowsBlowDistance(1)
         .setIncineratedInCrucible()
         .setFilterableProperties(16)
         .setUnlocalizedName("fcItempFabric")
         .setCreativeTab(CreativeTabs.tabMaterials);
      legacyGrate = new LegacyGrateItem(BTWMod.instance.parseID("fcGrateID"));
      legacyWickerPane = new LegacyWickerPaneItem(BTWMod.instance.parseID("fcWickerID"));
      tannedLeather = new Item(BTWMod.instance.parseID("fcTannedLeatherID"))
         .setBuoyant()
         .setIncineratedInCrucible()
         .setFilterableProperties(16)
         .setUnlocalizedName("fcItemLeatherTanned")
         .setCreativeTab(CreativeTabs.tabMaterials);
      leatherStrap = new Item(BTWMod.instance.parseID("fcStrapID"))
         .setBuoyant()
         .setBellowsBlowDistance(1)
         .setIncineratedInCrucible()
         .setFilterableProperties(20)
         .setUnlocalizedName("fcItemStrap")
         .setCreativeTab(CreativeTabs.tabMaterials);
      belt = new Item(BTWMod.instance.parseID("fcBeltID"))
         .setBuoyant()
         .setIncineratedInCrucible()
         .setFilterableProperties(4)
         .setUnlocalizedName("fcItemBelt")
         .setCreativeTab(CreativeTabs.tabMaterials);
      foulFood = new FoulFoodItem(BTWMod.instance.parseID("fcFoulFoodID"));
      woodenBlade = new Item(BTWMod.instance.parseID("fcWoodBladeID"))
         .setBuoyant()
         .setIncineratedInCrucible()
         .setMaxStackSize(1)
         .setUnlocalizedName("fcItemBladeWood")
         .setCreativeTab(CreativeTabs.tabMaterials);
      glue = new Item(BTWMod.instance.parseID("fcGlueID"))
         .setNeutralBuoyant()
         .setIncineratedInCrucible()
         .setUnlocalizedName("fcItemGlue")
         .setCreativeTab(CreativeTabs.tabMaterials);
      tallow = new Item(BTWMod.instance.parseID("fcTallowID"))
         .setBuoyant()
         .setIncineratedInCrucible()
         .setUnlocalizedName("fcItemTallow")
         .setCreativeTab(CreativeTabs.tabMaterials);
      haft = new Item(BTWMod.instance.parseID("fcHaftID"))
         .setBuoyant()
         .setIncineratedInCrucible()
         .setFilterableProperties(4)
         .setUnlocalizedName("fcItemHaft")
         .setCreativeTab(CreativeTabs.tabMaterials);
      soulforgedSteelIngot = new Item(BTWMod.instance.parseID("fcSteelID")).setUnlocalizedName("fcItemIngotSteel").setCreativeTab(CreativeTabs.tabMaterials);
      steelPickaxe = new PickaxeItemSteel(BTWMod.instance.parseID("fcRefinedPickAxeID"));
      steelShovel = new ShovelItemSteel(BTWMod.instance.parseID("fcRefinedShovelID"));
      steelHoe = new HoeItemSteel(BTWMod.instance.parseID("fcRefinedHoeID"));
      battleaxe = new BattleAxeItem(BTWMod.instance.parseID("fcBattleAxeID"));
      steelSword = new SwordItemSteel(BTWMod.instance.parseID("fcRefinedSwordID"));
      groundNetherrack = new Item(BTWMod.instance.parseID("fcGroundNetherrackID"))
         .setBellowsBlowDistance(2)
         .setUnlocalizedName("fcItemNetherrackGround")
         .setFilterableProperties(8)
         .setCreativeTab(CreativeTabs.tabMaterials);
      hellfireDust = new Item(BTWMod.instance.parseID("fcHellfireDustID"))
         .setBellowsBlowDistance(3)
         .setUnlocalizedName("fcItemDustHellfire")
         .setFilterableProperties(8)
         .setCreativeTab(CreativeTabs.tabMaterials);
      concentratedHellfire = new Item(BTWMod.instance.parseID("fcConcentratedHellfireID"))
         .setUnlocalizedName("fcItemConcentratedHellfire")
         .setCreativeTab(CreativeTabs.tabMaterials);
      steelArmorPlate = new Item(BTWMod.instance.parseID("fcArmorPlateID")).setUnlocalizedName("fcItemArmorPlate").setCreativeTab(CreativeTabs.tabMaterials);
      diamondArmorPlate = new Item(BTWMod.instance.parseID("fcItemDiamondPlateID"))
         .setUnlocalizedName("fcItemDiamondPlate")
         .setCreativeTab(CreativeTabs.tabMaterials);
      plateHelmet = new ArmorItemSteel(BTWMod.instance.parseID("fcPlateHelmID"), 0, 10).b("fcItemHelmetPlate");
      plateBreastplate = new ArmorItemSteel(BTWMod.instance.parseID("fcPlateBreastPlateID"), 1, 14).b("fcItemChestplatePlate");
      plateLeggings = new ArmorItemSteel(BTWMod.instance.parseID("fcPlateLeggingsID"), 2, 12).b("fcItemLeggingsPlate");
      plateBoots = new ArmorItemSteel(BTWMod.instance.parseID("fcPlateBootsID"), 3, 8).b("fcItemBootsPlate");
      compositeBow = new CompositeBowItem(BTWMod.instance.parseID("fcCompositeBowID"));
      broadheadArrowHead = new Item(BTWMod.instance.parseID("fcBroadheadArrowheadID"))
         .setFilterableProperties(2)
         .setUnlocalizedName("fcItemArrowheadBroadhead")
         .setCreativeTab(CreativeTabs.tabMaterials);
      broadheadArrow = new BroadheadArrowItem(BTWMod.instance.parseID("fcBroadheadArrowID"));
      coalDust = new Item(BTWMod.instance.parseID("fcCoalDustID"))
         .setBellowsBlowDistance(3)
         .setfurnaceburntime(FurnaceBurnTime.COAL.burnTime / 2)
         .setFilterableProperties(8)
         .setUnlocalizedName("fcItemDustCoal")
         .setCreativeTab(CreativeTabs.tabMaterials);
      padding = new Item(BTWMod.instance.parseID("fcPaddingID"))
         .setBuoyant()
         .setIncineratedInCrucible()
         .setUnlocalizedName("fcItemPadding")
         .setCreativeTab(CreativeTabs.tabMaterials);
      filament = new Item(BTWMod.instance.parseID("fcFilamentID"))
         .setBellowsBlowDistance(1)
         .setFilterableProperties(2)
         .setUnlocalizedName("fcItemFilament")
         .setCreativeTab(CreativeTabs.tabMaterials);
      redstoneEye = new Item(BTWMod.instance.parseID("fcPolishedLapisID"))
         .setFilterableProperties(2)
         .setUnlocalizedName("fcItemRedstoneEye")
         .setCreativeTab(CreativeTabs.tabMaterials);
      urn = new PlaceAsBlockItem(BTWMod.instance.parseID("fcUrnID"), BTWBlocks.aestheticNonOpaque.blockID, 0, "fcItemUrn")
         .setBuoyant()
         .setCreativeTab(CreativeTabs.tabMaterials);
      soulUrn = new SoulUrnItem(BTWMod.instance.parseID("fcSoulUrnID"));
      hardBoiledEgg = new HardBoiledEggItem(BTWMod.instance.parseID("fcHardBoiledEggID"));
      potash = new Item(BTWMod.instance.parseID("fcPotashID"))
         .setBellowsBlowDistance(3)
         .setFilterableProperties(8)
         .setUnlocalizedName("fcItemPotash")
         .setCreativeTab(CreativeTabs.tabMaterials);
      soap = new SoapItem(BTWMod.instance.parseID("fcSoapID")).setIncineratedInCrucible().setFilterableProperties(2).setCreativeTab(CreativeTabs.tabMaterials);
      sawDust = new Item(BTWMod.instance.parseID("fcSawDustID"))
         .setBuoyant()
         .setBellowsBlowDistance(3)
         .setfurnaceburntime(FurnaceBurnTime.KINDLING)
         .setIncineratedInCrucible()
         .setFilterableProperties(8)
         .setUnlocalizedName("fcItemDustSaw")
         .setCreativeTab(CreativeTabs.tabMaterials);
      gimpHelmet = new ArmorItemGimpSuit(BTWMod.instance.parseID("fcTannedLeatherHelmID"), 0).b("fcItemGimpHelm");
      gimpChest = new ArmorItemGimpSuit(BTWMod.instance.parseID("fcTannedLeatherChestID"), 1).b("fcItemGimpChest");
      gimpLeggings = new ArmorItemGimpSuit(BTWMod.instance.parseID("fcTannedLeatherLeggingsID"), 2).b("fcItemGimpLeggings");
      gimpBoots = new ArmorItemGimpSuit(BTWMod.instance.parseID("fcTannedLeatherBootsID"), 3).b("fcItemGimpBoots");
      dynamite = new DynamiteItem(BTWMod.instance.parseID("fcDynamiteID"));
      breedingHarness = new BreedingHarnessItem(BTWMod.instance.parseID("fcBreedingHarnessID"));
      soulDust = new Item(BTWMod.instance.parseID("fcSoulDustID"))
         .setBuoyant()
         .setBellowsBlowDistance(3)
         .setfurnaceburntime(FurnaceBurnTime.KINDLING)
         .setIncineratedInCrucible()
         .setFilterableProperties(8)
         .setUnlocalizedName("fcItemDustSoul")
         .setCreativeTab(CreativeTabs.tabMaterials);
      mattock = new MattockItem(BTWMod.instance.parseID("fcMattockID"));
      steelAxe = new AxeItemSteel(BTWMod.instance.parseID("fcRefinedAxeID"));
      netherSludge = new NetherSludgeItem(BTWMod.instance.parseID("fcNetherSludgeID"));
      netherBrick = new NetherBrickItem(BTWMod.instance.parseID("fcNetherBrickID"));
      tuningFork = new TuningForkItem(BTWMod.instance.parseID("fcItemTuningForkID"));
      arcaneScroll = new ArcaneScrollItem(BTWMod.instance.parseID("fcItemArcaneScrollID"));
      legacyCandle = new LegacyCandleItem(BTWMod.instance.parseID("fcItemCandleID"));
      netherGrothSpores = new NetherGrothSporesItem(BTWMod.instance.parseID("fcItemNetherGrowthSporesID"));
      mould = new MouldItem(BTWMod.instance.parseID("fcItemMouldID")).b("fcItemMould");
      canvas = new CanvasItem(BTWMod.instance.parseID("fcItemCanvasID"));
      kibble = new FoodItem(BTWMod.instance.parseID("fcItemDogFoodID"), 3, 0.0F, true, "fcItemKibble")
         .setStandardFoodPoisoningEffect()
         .setFilterableProperties(2);
      rawEgg = new FoodItem(BTWMod.instance.parseID("fcItemRawEggID"), 2, 0.25F, false, "fcItemEggRaw")
         .setStandardFoodPoisoningEffect()
         .setFilterableProperties(2);
      friedEgg = new FoodItem(BTWMod.instance.parseID("fcItemFriedEggID"), 3, 0.25F, false, "fcItemEggFried").setFilterableProperties(2);
      screw = new Item(BTWMod.instance.parseID("fcItemScrewID")).setUnlocalizedName("fcItemScrew").setCreativeTab(CreativeTabs.tabMaterials);
      rottenArrow = new RottenArrowItem(BTWMod.instance.parseID("fcItemRottenArrowID"));
      ocularOfEnder = new Item(BTWMod.instance.parseID("fcItemOcularOfEnderID"))
         .setUnlocalizedName("fcItemOcularOfEnder")
         .setCreativeTab(CreativeTabs.tabMaterials);
      enderSpectacles = new ArmorItemSpecial(BTWMod.instance.parseID("fcItemEnderSpectaclesID"), 0).b("fcItemEnderSpectacles");
      stake = new ItemReed(BTWMod.instance.parseID("fcItemStakeID"), BTWBlocks.stake)
         .setBuoyant()
         .setFilterableProperties(1)
         .setUnlocalizedName("fcItemStake")
         .setCreativeTab(CreativeTabs.tabTools);
      brimstone = new Item(BTWMod.instance.parseID("fcItemBrimstoneID"))
         .setBellowsBlowDistance(3)
         .setFilterableProperties(8)
         .setUnlocalizedName("fcItemBrimstone")
         .setCreativeTab(CreativeTabs.tabMaterials);
      nitre = new Item(BTWMod.instance.parseID("fcItemNitreID"))
         .setBellowsBlowDistance(3)
         .setFilterableProperties(8)
         .setUnlocalizedName("fcItemNitre")
         .setCreativeTab(CreativeTabs.tabMaterials);
      element = new Item(BTWMod.instance.parseID("fcItemElementID"))
         .setBellowsBlowDistance(1)
         .setUnlocalizedName("fcItemElement")
         .setFilterableProperties(2)
         .setCreativeTab(CreativeTabs.tabMaterials);
      fuse = new Item(BTWMod.instance.parseID("fcItemFuseID"))
         .setBuoyant()
         .setBellowsBlowDistance(2)
         .setFilterableProperties(18)
         .setUnlocalizedName("fcItemFuse")
         .setCreativeTab(CreativeTabs.tabMaterials);
      blastingOil = new Item(BTWMod.instance.parseID("fcItemBlastingOilID")).setUnlocalizedName("fcItemBlastingOil").setCreativeTab(CreativeTabs.tabMaterials);
      verticalWindMill = new VerticalWindMillItem(BTWMod.instance.parseID("fcItemWindMillVerticalID"));
      boiledPotato = new FoodItem(BTWMod.instance.parseID("fcItemBoiledPotatoID"), 2, 0.0F, false, "fcItemPotatoBoiled")
         .setAsBasicPigFood()
         .setFilterableProperties(2);
      rawMutton = new FoodItem(BTWMod.instance.parseID("fcItemMuttonRawID"), 3, 0.25F, true, "fcItemMuttonRaw", true).setStandardFoodPoisoningEffect();
      cookedMutton = new FoodItem(BTWMod.instance.parseID("fcItemMuttonCookedID"), 4, 0.25F, true, "fcItemMuttonCooked");
      witchWart = new Item(BTWMod.instance.parseID("fcItemWitchWartID"))
         .setBuoyant()
         .setBellowsBlowDistance(1)
         .setFilterableProperties(2)
         .setPotionEffect(PotionHelper.redstoneEffect)
         .setUnlocalizedName("fcItemWitchWart")
         .setCreativeTab(CreativeTabs.tabMaterials);
      cookedCarrot = new FoodItem(BTWMod.instance.parseID("fcItemCookedCarrotID"), 2, 0.0F, false, "fcItemCarrotCooked").setAsBasicPigFood();
      tastySandwich = new FoodItem(BTWMod.instance.parseID("fcItemTastySandwichID"), 5, 0.25F, true, "fcItemSandwichTasty");
      steakAndPotatoes = new FoodItem(BTWMod.instance.parseID("fcItemSteakAndPotatoesID"), 6, 0.25F, true, "fcItemSteakAndPotatoes");
      hamAndEggs = new FoodItem(BTWMod.instance.parseID("fcItemHamAndEggsID"), 6, 0.25F, true, "fcItemHamAndEggs");
      steakDinner = new FoodItem(BTWMod.instance.parseID("fcItemSteakDinnerID"), 8, 0.25F, true, "fcItemDinnerSteak");
      porkDinner = new FoodItem(BTWMod.instance.parseID("fcItemPorkDinnerID"), 8, 0.25F, true, "fcItemDinnerPork");
      wolfDinner = new FoodItem(BTWMod.instance.parseID("fcItemWolfDinnerID"), 8, 0.25F, true, "fcItemDinnerWolf");
      rawKebab = new FoodItem(BTWMod.instance.parseID("fcItemRawKebabID"), 6, 0.25F, true, "fcItemKebabRaw").setStandardFoodPoisoningEffect();
      cookedKebab = new FoodItem(BTWMod.instance.parseID("fcItemCookedKebabID"), 8, 0.25F, true, "fcItemKebabCooked");
      chickenSoup = new SoupItem(BTWMod.instance.parseID("fcItemChickenSoupID"), 8, 0.25F, true, "fcItemSoupChicken");
      chowder = new SoupItem(BTWMod.instance.parseID("fcItemFishSoupID"), 5, 0.25F, false, "fcItemChowder");
      heartyStew = new SoupItem(BTWMod.instance.parseID("fcItemHeartyStewID"), 10, 0.25F, true, "fcItemStewHearty");
      redMushroom = new MushroomItem(BTWMod.instance.parseID("fcItemMushroomRedID"), 1, 0.0F, "fcItemMushroomRed", Block.mushroomRed.blockID)
         .a(Potion.poison.id, 5, 0, 1.0F)
         .setFilterableProperties(2)
         .setPotionEffect(PotionHelper.spiderEyeEffect);
      brownMushroom = new MushroomItem(BTWMod.instance.parseID("fcItemMushroomBrownID"), 1, 0.0F, "fcItemMushroomBrown", Block.mushroomBrown.blockID)
         .setFilterableProperties(2);
      ironNugget = new Item(BTWMod.instance.parseID("fcItemNuggetIronID"))
         .setFilterableProperties(2)
         .setUnlocalizedName("fcItemNuggetIron")
         .setCreativeTab(CreativeTabs.tabMaterials);
      mail = new Item(BTWMod.instance.parseID("fcItemMailID")).setUnlocalizedName("fcItemMail");
      rawMysteryMeat = new RawMysteryMeatItem(BTWMod.instance.parseID("fcItemRawMysteryMeatID"));
      cookedMysteryMeat = new CookedMysteryMeatItem(BTWMod.instance.parseID("fcItemCookedMysteryMeatID"));
      rawMushroomOmelet = new FoodItem(BTWMod.instance.parseID("fcItemRawMushroomOmeletID"), 3, 0.25F, false, "fcItemMushroomOmletRaw")
         .setStandardFoodPoisoningEffect();
      cookedMushroomOmelet = new FoodItem(BTWMod.instance.parseID("fcItemCookedMushroomOmeletID"), 4, 0.25F, false, "fcItemMushroomOmletCooked");
      rawScrambledEggs = new FoodItem(BTWMod.instance.parseID("fcItemRawScrambledEggsID"), 3, 0.25F, false, "fcItemEggScrambledRaw")
         .setStandardFoodPoisoningEffect();
      cookedScrambledEggs = new FoodItem(BTWMod.instance.parseID("fcItemCookedScrambledEggsID"), 4, 0.25F, false, "fcItemEggScrambledCooked");
      creeperOysters = new CreeperOysterItem(BTWMod.instance.parseID("fcItemCreeperOystersID"));
      woolHelmet = new ArmorItemWood(BTWMod.instance.parseID("fcItemArmorWoolHelmID"), 0).b("fcItemWoolHelm");
      woolChest = new ArmorItemWood(BTWMod.instance.parseID("fcItemArmorWoolChestID"), 1).b("fcItemWoolChest");
      woolLeggings = new ArmorItemWood(BTWMod.instance.parseID("fcItemArmorWoolLeggingsID"), 2).b("fcItemWoolLeggings");
      woolBoots = new ArmorItemWood(BTWMod.instance.parseID("fcItemArmorWoolBootsID"), 3).b("fcItemWoolBoots");
      paddedHelmet = new ArmorItemPadded(BTWMod.instance.parseID("fcItemArmorPaddedHelmID"), 0).b("fcItemPaddedHelm");
      paddedChest = new ArmorItemPadded(BTWMod.instance.parseID("fcItemArmorPaddedChestID"), 1).b("fcItemPaddedChest");
      paddedLeggings = new ArmorItemPadded(BTWMod.instance.parseID("fcItemArmorPaddedLeggingsID"), 2).b("fcItemPaddedLeggings");
      paddedBoots = new ArmorItemPadded(BTWMod.instance.parseID("fcItemArmorPaddedBootsID"), 3).b("fcItemPaddedBoots");
      tannedLeatherHelmet = new ArmorItemTanned(BTWMod.instance.parseID("fcItemArmorTannedHelmID"), 0).b("fcItemTannedHelm");
      tannedLeatherChest = new ArmorItemTanned(BTWMod.instance.parseID("fcItemArmorTannedChestID"), 1).b("fcItemTannedChest");
      tannedLeatherLeggings = new ArmorItemTanned(BTWMod.instance.parseID("fcItemArmorTannedLeggingsID"), 2).b("fcItemTannedLeggings");
      tannedLeatherBoots = new ArmorItemTanned(BTWMod.instance.parseID("fcItemArmorTannedBootsID"), 3).b("fcItemTannedBoots");
      diamondIngot = new Item(BTWMod.instance.parseID("fcItemIngotDiamondID"))
         .setCreativeTab(CreativeTabs.tabMaterials)
         .setUnlocalizedName("fcItemIngotDiamond");
      cutLeather = new Item(BTWMod.instance.parseID("fcItemLeatherCutID")).setBuoyant().setFilterableProperties(16).setUnlocalizedName("fcItemLeatherCut");
      cutTannedLeather = new Item(BTWMod.instance.parseID("fcItemTannedLeatherCutID"))
         .setBuoyant()
         .setFilterableProperties(16)
         .setUnlocalizedName("fcItemLeatherTannedCut");
      cutScouredLeather = new Item(BTWMod.instance.parseID("fcItemScouredLeatherCutID"))
         .setBuoyant()
         .setFilterableProperties(16)
         .setUnlocalizedName("fcItemLeatherScouredCut");
      baitedFishingRod = new FishingRodItemBaited(BTWMod.instance.parseID("fcItemFishingRodBaitedID"));
      dirtPile = new DirtPileItem(BTWMod.instance.parseID("fcItemPileDirtID"));
      sandPile = new SandPileItem(BTWMod.instance.parseID("fcItemPileSandID"));
      gravelPile = new GravelPileItem(BTWMod.instance.parseID("fcItemPileGravelID"));
      batWing = new FoodItem(BTWMod.instance.parseID("fcItemBatWingID"), 1, 0.8F, false, "fcItemBatWing")
         .setStandardFoodPoisoningEffect()
         .setBellowsBlowDistance(3)
         .setFilterableProperties(18);
      goldenDung = new Item(BTWMod.instance.parseID("fcItemGoldenDungID")).setUnlocalizedName("fcItemDungGolden");
      bark = new BarkItem(BTWMod.instance.parseID("fcItemBarkID"));
      soulSandPile = new SoulSandPileItem(BTWMod.instance.parseID("fcItemPileSoulSandID"));
      redstoneLatch = new Item(BTWMod.instance.parseID("fcItemRedstoneLatchID")).setUnlocalizedName("fcItemRedstoneLatch");
      steelNugget = new Item(BTWMod.instance.parseID("fcItemNuggetSteelID")).setUnlocalizedName("fcItemNuggetSteel");
      wool = new WoolItem(BTWMod.instance.parseID("fcItemWoolID"));
      cocoaBeans = new CocoaBeanItem(BTWMod.instance.parseID("fcItemCocoaBeansID"));
      chocolate = new FoodItem(BTWMod.instance.parseID("fcItemChocolateID"), 2, 0.5F, true, "fcItemChocolate").j().setFilterableProperties(2);
      milkChocolateBucket = new BucketItemMilkChocolate(BTWMod.instance.parseID("fcItemBucketChocolateMilkID"));
      soulFlux = new SoulFluxItem(BTWMod.instance.parseID("fcItemSoulFluxID"));
      enderSlag = new Item(BTWMod.instance.parseID("fcItemEnderSlagID"))
         .setBellowsBlowDistance(1)
         .setFilterableProperties(8)
         .setUnlocalizedName("fcItemEnderSlag");
      unbakedCake = new PlaceAsBlockItem(BTWMod.instance.parseID("fcItemPastryUncookedCakeID"), BTWBlocks.unfiredPottery.blockID, 9, "fcItemPastryUncookedCake")
         .setBuoyant()
         .setCreativeTab(CreativeTabs.tabFood);
      unbakedCookies = new PlaceAsBlockItem(
            BTWMod.instance.parseID("fcItemPastryUncookedCookiesID"), BTWBlocks.unfiredPottery.blockID, 10, "fcItemPastryUncookedCookies"
         )
         .setBuoyant()
         .setCreativeTab(CreativeTabs.tabFood);
      unbakedPumpkinPie = new PlaceAsBlockItem(
            BTWMod.instance.parseID("fcItemPastryUncookedPumpkinPieID"), BTWBlocks.unfiredPottery.blockID, 12, "fcItemPastryUncookedPumpkinPie"
         )
         .setBuoyant()
         .setCreativeTab(CreativeTabs.tabFood);
      mysteriousGland = new MysteriousGlandItem(BTWMod.instance.parseID("fcItemMysteriousGlandID")).a(CreativeTabs.tabMaterials);
      rawLiver = new FoodItem(BTWMod.instance.parseID("fcItemBeastLiverRawID"), 5, 0.5F, true, "fcItemBeastLiverRaw").setStandardFoodPoisoningEffect();
      cookedLiver = new FoodItem(BTWMod.instance.parseID("fcItemBeastLiverCookedID"), 6, 0.5F, true, "fcItemBeastLiverCooked");
      ancientProphecy = new AncientProphecyItem(BTWMod.instance.parseID("fcItemAncientProphecyID"));
      stumpRemover = new StumpRemoverItem(BTWMod.instance.parseID("fcItemStumpRemoverID"));
      pointyStick = new ChiselItemWood(BTWMod.instance.parseID("fcItemChiselWoodID"));
      stone = new StoneItem(BTWMod.instance.parseID("fcItemStoneID")).b("fcItemStone");
      sharpStone = new ChiselItemStone(BTWMod.instance.parseID("fcItemChiselStoneID"));
      woodenClub = new ClubItemWood(BTWMod.instance.parseID("fcItemClubID"));
      firePlough = new FireStarterItemPrimitive(BTWMod.instance.parseID("fcItemFireStarterSticksID"), 250, 0.05F, -0.1F, 0.1F, 0.001F)
         .setFilterableProperties(4)
         .setUnlocalizedName("fcItemFireStarterSticks");
      bowDrill = new FireStarterItemPrimitive(BTWMod.instance.parseID("fcItemFireStarterBowID"), 250, 0.025F, -0.1F, 0.1F, 0.004F).b("fcItemFireStarterBow");
      ironOreChunk = new OreChunkItemIron(BTWMod.instance.parseID("fcItemChunkIronOreID"));
      ironOrePile = new Item(BTWMod.instance.parseID("fcItemPileIronOreID"))
         .setFilterableProperties(8)
         .setUnlocalizedName("fcItemPileIronOre")
         .setCreativeTab(CreativeTabs.tabMaterials);
      ironChisel = new ChiselItemIron(BTWMod.instance.parseID("fcItemChiselIronID"));
      diamondChisel = new ChiselItemDiamond(BTWMod.instance.parseID("fcItemChiselDiamondID"));
      goldOreChunk = new OreChunkItemGold(BTWMod.instance.parseID("fcItemChunkGoldOreID"));
      goldOrePile = new Item(BTWMod.instance.parseID("fcItemPileGoldOreID"))
         .setFilterableProperties(8)
         .setUnlocalizedName("fcItemPileGoldOre")
         .setCreativeTab(CreativeTabs.tabMaterials);
      wickerPane = new WickerPieceItem(BTWMod.instance.parseID("fcItemWickerPieceID"));
      knittingNeedles = new KnittingNeedlesItem(BTWMod.instance.parseID("fcItemKnittingNeedlesID"));
      knitting = new KnittingItem(BTWMod.instance.parseID("fcItemKnittingID"));
      woolKnit = new WoolKnitItem(BTWMod.instance.parseID("fcItemWoolKnitID"));
      boneClub = new ClubItem(BTWMod.instance.parseID("fcItemClubBoneID"), 20, 4, "fcItemClubBone");
      curedMeat = new CuredFoodItem(BTWMod.instance.parseID("fcItemMeatCuredID"), 3, 0.25F, "fcItemMeatCured");
      metalFragment = new Item(BTWMod.instance.parseID("fcItemMetalFragmentID"))
         .setFilterableProperties(2)
         .setUnlocalizedName("fcItemMetalFragment")
         .setCreativeTab(CreativeTabs.tabMisc);
      clayPile = new Item(BTWMod.instance.parseID("fcItemPileClayID"))
         .setFilterableProperties(2)
         .setUnlocalizedName("fcItemPileClay")
         .setCreativeTab(CreativeTabs.tabMaterials);
      burnedMeat = new FoodItem(BTWMod.instance.parseID("fcItemMeatBurnedID"), 2, 0.25F, true, "fcItemMeatBurned");
      chickenFeed = new Item(BTWMod.instance.parseID("fcItemChickenFeedID"))
         .setAsBasicChickenFood()
         .setBellowsBlowDistance(2)
         .setFilterableProperties(8)
         .setUnlocalizedName("fcItemChickenFeed")
         .setCreativeTab(CreativeTabs.tabFood);
      boneFishHook = new Item(BTWMod.instance.parseID("fcItemFishHookBoneID"))
         .setBellowsBlowDistance(2)
         .setFilterableProperties(2)
         .setUnlocalizedName("fcItemFishHookBone")
         .setCreativeTab(CreativeTabs.tabMisc);
      boneCarving = new BoneCarvingItem(BTWMod.instance.parseID("fcItemCarvingBoneID"));
      stoneBrick = new StoneBrickItem(BTWMod.instance.parseID("fcItemBrickStoneID")).b("fcItemBrickStone");
      wickerWeaving = new WickerWeavingItem(BTWMod.instance.parseID("fcItemWickerWeavingID"));
      wheat = new WheatItem(BTWMod.instance.parseID("fcItemWheatID"));
      wheatSeeds = new WheatSeedsItem(BTWMod.instance.parseID("fcItemWheatSeedsID"));
      breadDough = new PlaceAsBlockItem(BTWMod.instance.parseID("fcItemBreadDoughID"), BTWBlocks.unfiredPottery.blockID, 13, "fcItemBreadDough")
         .setBuoyant()
         .setIncineratedInCrucible()
         .setCreativeTab(CreativeTabs.tabFood);
      straw = new StrawItem(BTWMod.instance.parseID("fcItemStrawID"));
      unfiredBrick = new UnfiredBrickItem(BTWMod.instance.parseID("fcItemBrickUnfiredID"));
      unfiredNetherBrick = new UnfiredNetherBrickItem(BTWMod.instance.parseID("fcItemNetherBrickUnfiredID"));
      nameTag = new NameTagItem(BTWMod.instance.parseID("fcItemNameTagID"));
      diamondShears = new ShearsItemDiamond(BTWMod.instance.parseID("fcItemShearsDiamondID"));
      sugarCaneRoots = new PlaceAsBlockItem(BTWMod.instance.parseID("fcItemReedRootsID"), BTWBlocks.sugarCaneRoots.blockID)
         .setBuoyant()
         .setfurnaceburntime(FurnaceBurnTime.KINDLING)
         .setIncineratedInCrucible()
         .setFilterableProperties(4)
         .setUnlocalizedName("fcItemReedRoots")
         .setCreativeTab(CreativeTabs.tabMaterials);
      ((PlaceAsBlockItem)Item.reed).setAssociatedBlockID(BTWBlocks.sugarCane.blockID);
      carrot = new SeedFoodItem(BTWMod.instance.parseID("fcItemCarrotID"), 3, 0.0F, BTWBlocks.floweringCarrotCrop.blockID)
         .setFilterableProperties(2)
         .setAsBasicPigFood()
         .setUnlocalizedName("fcItemCarrot");
      carrotSeeds = new SeedItem(BTWMod.instance.parseID("fcItemCarrotSeedsID"), BTWBlocks.carrotCrop.blockID)
         .setAsBasicChickenFood()
         .setUnlocalizedName("fcItemCarrotSeeds");
      mashedMelon = new HighResolutionFoodItem(BTWMod.instance.parseID("fcItemMelonMashedID"), 2, 0.0F, false, "fcItemMelonMashed").setFilterableProperties(8);
      bedroll = new BedItem(BTWMod.instance.parseID("fcItemBedrollID"), BTWBlocks.bedroll.blockID)
         .setBuoyant()
         .setIncineratedInCrucible()
         .setMaxStackSize(1)
         .setUnlocalizedName("fcItemBedroll");
      candle = new CandleItem(BTWMod.instance.parseID("fcItemCandleNewID"));
      corpseEye = new CorpseEyeItem(BTWMod.instance.parseID("fcItemCorpseEyeID"));
      tangledWeb = new Item(BTWMod.instance.parseID("fcItemTangledWebID"))
         .setBuoyant()
         .setIncineratedInCrucible()
         .setUnlocalizedName("fcItemTangledWeb")
         .setCreativeTab(CreativeTabs.tabMaterials);
      webUntangling = new WebUntanglingItem(BTWMod.instance.parseID("fcItemWebUntanglingID"));
      sinew = new Item(BTWMod.instance.parseID("fcItemSinewID"))
         .setBuoyant()
         .setBellowsBlowDistance(2)
         .setIncineratedInCrucible()
         .setFilterableProperties(18)
         .setUnlocalizedName("fcItemSinew")
         .setCreativeTab(CreativeTabs.tabMaterials);
      sinewExtractingBeef = new SinewExtractingItem(BTWMod.instance.parseID("fcItemSinewExtractingBeefID"), "fcItemSinewExtractingBeef");
      sinewExtractingWolf = new SinewExtractingItem(BTWMod.instance.parseID("fcItemSinewExtractingWolfID"), "fcItemSinewExtractingWolf");
      createAssociatedItemsForModBlocks();
   }

   private static void createAssociatedItemsForModBlocks() {
      registerCustomBlockItems();

      for (int iTempBlockID = 0; iTempBlockID < 4096; iTempBlockID++) {
         if (Block.blocksList[iTempBlockID] != null && Item.itemsList[iTempBlockID] == null) {
            Item.itemsList[iTempBlockID] = new ItemBlock(iTempBlockID - 256);
         }
      }
   }

   private static void registerCustomBlockItems() {
      Item.suppressConflictWarnings = true;
      Item.itemsList[BTWBlocks.sandAndGravelSlab.blockID] = new SandAndGravelSlabBlockItem(BTWBlocks.sandAndGravelSlab.blockID - 256);
      Item.itemsList[BTWBlocks.aestheticEarth.blockID] = new AestheticEarthBlockItem(BTWBlocks.aestheticEarth.blockID - 256);
      Item.itemsList[BTWBlocks.miningCharge.blockID] = new MiningChargeBlockItem(BTWBlocks.miningCharge.blockID - 256);
      Item.itemsList[BTWBlocks.woolSlab.blockID] = new WoolSlabBlockItem(BTWBlocks.woolSlab.blockID - 256);
      Item.itemsList[BTWBlocks.companionCube.blockID] = new CompanionCubeBlockItem(BTWBlocks.companionCube.blockID - 256);
      Item.itemsList[BTWBlocks.millstone.blockID] = new MillstoneBlockItem(BTWBlocks.millstone.blockID - 256);
      Item.itemsList[BTWBlocks.unfiredPottery.blockID] = new UnfiredPotteryBlockItem(BTWBlocks.unfiredPottery.blockID - 256);
      Item.itemsList[BTWBlocks.vase.blockID] = new VaseBlockItem(BTWBlocks.vase.blockID - 256);
      Item.itemsList[BTWBlocks.planter.blockID] = new PlanterBlockItem(BTWBlocks.planter.blockID - 256);
      Item.itemsList[BTWBlocks.aestheticNonOpaque.blockID] = new AestheticNonOpaqueBlockItem(BTWBlocks.aestheticNonOpaque.blockID - 256);
      Item.itemsList[BTWBlocks.aestheticOpaque.blockID] = new AestheticOpaqueBlockItem(BTWBlocks.aestheticOpaque.blockID - 256);
      Item.itemsList[BTWBlocks.aestheticVegetation.blockID] = new AestheticVegetationBlockItem(BTWBlocks.aestheticVegetation.blockID - 256);
      Item.itemsList[BTWBlocks.bloodWoodLog.blockID] = new BloodWoodLogBlockItem(BTWBlocks.bloodWoodLog.blockID - 256);
      Item.itemsList[BTWBlocks.bloodWoodLeaves.blockID] = new LeavesBlockItem(BTWBlocks.bloodWoodLeaves.blockID - 256);
      Item.itemsList[BTWBlocks.dirtSlab.blockID] = new DirtSlabBlockItem(BTWBlocks.dirtSlab.blockID - 256);
      Item.itemsList[BTWBlocks.whiteStoneStairs.blockID] = new WhiteStoneStairsBlockItem(BTWBlocks.whiteStoneStairs.blockID - 256);
      Item.itemsList[BTWBlocks.legacyStoneAndOakSiding.blockID] = new LegacySidingBlockItem(BTWBlocks.legacyStoneAndOakSiding.blockID - 256);
      Item.itemsList[BTWBlocks.legacyStoneAndOakCorner.blockID] = new LegacyCornerBlockItem(BTWBlocks.legacyStoneAndOakCorner.blockID - 256);
      Item.itemsList[BTWBlocks.stoneBrickSidingAndCorner.blockID] = new SidingAndCornerBlockItem(BTWBlocks.stoneBrickSidingAndCorner.blockID - 256);
      Item.itemsList[BTWBlocks.stoneBrickMouldingAndDecorative.blockID] = new MouldingAndDecorativeBlockItem(
         BTWBlocks.stoneBrickMouldingAndDecorative.blockID - 256
      );
      Item.itemsList[BTWBlocks.oakWoodSidingAndCorner.blockID] = new SidingAndCornerBlockItem(BTWBlocks.oakWoodSidingAndCorner.blockID - 256);
      Item.itemsList[BTWBlocks.oakWoodMouldingAndDecorative.blockID] = new MouldingBlockItem(BTWBlocks.oakWoodMouldingAndDecorative.blockID - 256);
      Item.itemsList[BTWBlocks.spruceWoodSidingAndCorner.blockID] = new WoodSidingStubBlockItem(BTWBlocks.spruceWoodSidingAndCorner.blockID - 256);
      Item.itemsList[BTWBlocks.spruceWoodMouldingAndDecorative.blockID] = new WoodMouldingStubBlockItem(BTWBlocks.spruceWoodMouldingAndDecorative.blockID - 256);
      Item.itemsList[BTWBlocks.birchWoodSidingAndCorner.blockID] = new WoodCornerStubBlockItem(BTWBlocks.birchWoodSidingAndCorner.blockID - 256);
      Item.itemsList[BTWBlocks.birchWoodMouldingAndDecorative.blockID] = new WoodMouldingDecorativeStubBlockItem(
         BTWBlocks.birchWoodMouldingAndDecorative.blockID - 256
      );
      Item.itemsList[BTWBlocks.jungleWoodSidingAndCorner.blockID] = new WoodSidingDecorativeStubBlockItem(BTWBlocks.jungleWoodSidingAndCorner.blockID - 256);
      Item.itemsList[BTWBlocks.jungleWoodMouldingAndDecorative.blockID] = new MouldingBlockItem(BTWBlocks.jungleWoodMouldingAndDecorative.blockID - 256);
      Item.itemsList[BTWBlocks.bloodWoodSidingAndCorner.blockID] = new SidingAndCornerBlockItem(BTWBlocks.bloodWoodSidingAndCorner.blockID - 256);
      Item.itemsList[BTWBlocks.bloodWoodMouldingAndDecorative.blockID] = new MouldingBlockItem(BTWBlocks.bloodWoodMouldingAndDecorative.blockID - 256);
      woodSidingStubID = BTWBlocks.spruceWoodSidingAndCorner.blockID;
      woodMouldingStubID = BTWBlocks.spruceWoodMouldingAndDecorative.blockID;
      woodCornerStubID = BTWBlocks.birchWoodSidingAndCorner.blockID;
      woodMouldingDecorativeStubID = BTWBlocks.birchWoodMouldingAndDecorative.blockID;
      woodSidingDecorativeStubID = BTWBlocks.jungleWoodSidingAndCorner.blockID;
      Item.itemsList[BTWBlocks.whiteStoneSidingAndCorner.blockID] = new SidingAndCornerBlockItem(BTWBlocks.whiteStoneSidingAndCorner.blockID - 256);
      Item.itemsList[BTWBlocks.whiteStoneMouldingAndDecroative.blockID] = new MouldingAndDecorativeBlockItem(
         BTWBlocks.whiteStoneMouldingAndDecroative.blockID - 256
      );
      Item.itemsList[BTWBlocks.netherBrickSidingAndCorner.blockID] = new SidingAndCornerBlockItem(BTWBlocks.netherBrickSidingAndCorner.blockID - 256);
      Item.itemsList[BTWBlocks.netherBrickMouldingAndDecorative.blockID] = new MouldingAndDecorativeBlockItem(
         BTWBlocks.netherBrickMouldingAndDecorative.blockID - 256
      );
      Item.itemsList[BTWBlocks.brickSidingAndCorner.blockID] = new SidingAndCornerBlockItem(BTWBlocks.brickSidingAndCorner.blockID - 256);
      Item.itemsList[BTWBlocks.brickMouldingAndDecorative.blockID] = new MouldingAndDecorativeBlockItem(BTWBlocks.brickMouldingAndDecorative.blockID - 256);
      Item.itemsList[BTWBlocks.stoneSidingAndCorner.blockID] = new SidingAndCornerBlockItem(BTWBlocks.stoneSidingAndCorner.blockID - 256);
      Item.itemsList[BTWBlocks.stoneMouldingAndDecorative.blockID] = new MouldingAndDecorativeBlockItem(BTWBlocks.stoneMouldingAndDecorative.blockID - 256);
      Item.itemsList[BTWBlocks.sandstoneSidingAndCorner.blockID] = new SidingAndCornerBlockItem(BTWBlocks.sandstoneSidingAndCorner.blockID - 256);
      Item.itemsList[BTWBlocks.sandstoneMouldingAndDecorative.blockID] = new MouldingAndDecorativeBlockItem(
         BTWBlocks.sandstoneMouldingAndDecorative.blockID - 256
      );
      Item.itemsList[BTWBlocks.blackStoneSidingAndCorner.blockID] = new SidingAndCornerBlockItem(BTWBlocks.blackStoneSidingAndCorner.blockID - 256);
      Item.itemsList[BTWBlocks.blackStoneMouldingAndDecorative.blockID] = new MouldingAndDecorativeBlockItem(
         BTWBlocks.blackStoneMouldingAndDecorative.blockID - 256
      );
      Item.itemsList[BTWBlocks.dormandSoulforge.blockID] = new DormantSoulforgeBlockItem(BTWBlocks.dormandSoulforge.blockID - 256);
      Item.itemsList[BTWBlocks.rottenFleshSlab.blockID] = new SlabBlockItem(BTWBlocks.rottenFleshSlab.blockID - 256);
      Item.itemsList[BTWBlocks.boneSlab.blockID] = new SlabBlockItem(BTWBlocks.boneSlab.blockID - 256);
      Item.itemsList[BTWBlocks.freshPumpkin.blockID] = new FreshPumpkinBlockItem(BTWBlocks.freshPumpkin.blockID - 256);
      Item.itemsList[BTWBlocks.oakChewedLog.blockID] = new DamageToMetadataBlockItem(BTWBlocks.oakChewedLog.blockID - 256);
      Item.itemsList[BTWBlocks.looseDirtSlab.blockID] = new LooseDirtSlabBlockItem(BTWBlocks.looseDirtSlab.blockID - 256);
      Item.itemsList[BTWBlocks.looseBrickSlab.blockID] = new SlabBlockItem(BTWBlocks.looseBrickSlab.blockID - 256);
      Item.itemsList[BTWBlocks.looseCobblestoneSlab.blockID] = new SlabWithMetadataBlockItem(BTWBlocks.looseCobblestoneSlab.blockID - 256);
      Item.itemsList[BTWBlocks.finiteUnlitTorch.blockID] = new FiniteUnlitTorchBlockItem(BTWBlocks.finiteUnlitTorch.blockID - 256);
      Item.itemsList[BTWBlocks.finiteBurningTorch.blockID] = new FiniteBurningTorchBlockItem(BTWBlocks.finiteBurningTorch.blockID - 256);
      Item.itemsList[BTWBlocks.infiniteUnlitTorch.blockID] = new InfinteUnlitTorchBlockItem(BTWBlocks.infiniteUnlitTorch.blockID - 256);
      Item.itemsList[BTWBlocks.myceliumSlab.blockID] = new MyceliumSlabBlockItem(BTWBlocks.myceliumSlab.blockID - 256);
      Item.itemsList[BTWBlocks.grassSlab.blockID] = new GrassSlabBlockITem(BTWBlocks.grassSlab.blockID - 256);
      Item.itemsList[BTWBlocks.charredStump.blockID] = new DamageToMetadataBlockItem(BTWBlocks.charredStump.blockID - 256);
      Item.itemsList[BTWBlocks.looseSnowSlab.blockID] = new LooseSnowSlabBlockItem(BTWBlocks.looseSnowSlab.blockID - 256);
      Item.itemsList[BTWBlocks.solidSnowSlab.blockID] = new SlabBlockItem(BTWBlocks.solidSnowSlab.blockID - 256);
      Item.itemsList[BTWBlocks.creeperOysterSlab.blockID] = new SlabBlockItem(BTWBlocks.creeperOysterSlab.blockID - 256);
      Item.itemsList[BTWBlocks.infiniteBurningTorch.blockID] = new InfiniteBurningTorchBlockItem(BTWBlocks.infiniteBurningTorch.blockID - 256);
      Item.itemsList[BTWBlocks.wickerSlab.blockID] = new SlabBlockItem(BTWBlocks.wickerSlab.blockID - 256);
      Item.itemsList[BTWBlocks.looseCobblestone.blockID] = new ItemBlockWithMetadata(BTWBlocks.looseCobblestone.blockID - 256, BTWBlocks.looseCobblestone)
         .b("fcBlockCobblestoneLoose");
      Item.itemsList[BTWBlocks.looseStoneBrick.blockID] = new ItemBlockWithMetadata(BTWBlocks.looseStoneBrick.blockID - 256, BTWBlocks.looseStoneBrick)
         .b("fcBlockStoneBrickLoose");
      Item.itemsList[BTWBlocks.looseStoneBrickSlab.blockID] = new SlabWithMetadataBlockItem(BTWBlocks.looseStoneBrickSlab.blockID - 256);
      Item.itemsList[BTWBlocks.cobblestoneSlab.blockID] = new ItemSlab(
            BTWBlocks.cobblestoneSlab.blockID - 256, BTWBlocks.cobblestoneSlab, BTWBlocks.cobblestoneDoubleSlab, false
         )
         .b("stoneSlab");
      Item.itemsList[BTWBlocks.cobblestoneDoubleSlab.blockID] = new ItemSlab(
            BTWBlocks.cobblestoneDoubleSlab.blockID - 256, BTWBlocks.cobblestoneSlab, BTWBlocks.cobblestoneDoubleSlab, true
         )
         .b("stoneSlab");
      Item.itemsList[BTWBlocks.stoneBrickSlab.blockID] = new ItemSlab(
            BTWBlocks.stoneBrickSlab.blockID - 256, BTWBlocks.stoneBrickSlab, BTWBlocks.stoneBrickDoubleSlab, false
         )
         .b("stoneSlab");
      Item.itemsList[BTWBlocks.stoneBrickDoubleSlab.blockID] = new ItemSlab(
            BTWBlocks.stoneBrickDoubleSlab.blockID - 256, BTWBlocks.stoneBrickSlab, BTWBlocks.stoneBrickDoubleSlab, true
         )
         .b("stoneSlab");
      Item.itemsList[BTWBlocks.stoneSlab.blockID] = new ItemSlab(BTWBlocks.stoneSlab.blockID - 256, BTWBlocks.stoneSlab, BTWBlocks.stoneDoubleSlab, false)
         .b("stoneSlab");
      Item.itemsList[BTWBlocks.stoneDoubleSlab.blockID] = new ItemSlab(
            BTWBlocks.stoneDoubleSlab.blockID - 256, BTWBlocks.stoneSlab, BTWBlocks.stoneDoubleSlab, true
         )
         .b("stoneSlab");
      Item.itemsList[BTWBlocks.midStrataStoneBrickSidingAndCorner.blockID] = new SidingAndCornerBlockItem(
         BTWBlocks.midStrataStoneBrickSidingAndCorner.blockID - 256
      );
      Item.itemsList[BTWBlocks.midStrataStoneBrickMouldingAndDecorative.blockID] = new MouldingAndDecorativeBlockItem(
         BTWBlocks.midStrataStoneBrickMouldingAndDecorative.blockID - 256
      );
      Item.itemsList[BTWBlocks.deepStrataStoneBrickSidingAndCorner.blockID] = new SidingAndCornerBlockItem(
         BTWBlocks.deepStrataStoneBrickSidingAndCorner.blockID - 256
      );
      Item.itemsList[BTWBlocks.deepStrataStoneBrickMouldingAndDecorative.blockID] = new MouldingAndDecorativeBlockItem(
         BTWBlocks.deepStrataStoneBrickMouldingAndDecorative.blockID - 256
      );
      Item.itemsList[BTWBlocks.midStrataStoneSidingAndCorner.blockID] = new SidingAndCornerBlockItem(BTWBlocks.midStrataStoneSidingAndCorner.blockID - 256);
      Item.itemsList[BTWBlocks.midStrataStoneMouldingAndDecorative.blockID] = new MouldingAndDecorativeBlockItem(
         BTWBlocks.midStrataStoneMouldingAndDecorative.blockID - 256
      );
      Item.itemsList[BTWBlocks.deepStrataStoneSidingAndCorner.blockID] = new SidingAndCornerBlockItem(BTWBlocks.deepStrataStoneSidingAndCorner.blockID - 256);
      Item.itemsList[BTWBlocks.deepStrataStoneMouldingAndDecorative.blockID] = new MouldingAndDecorativeBlockItem(
         BTWBlocks.deepStrataStoneMouldingAndDecorative.blockID - 256
      );
      Item.itemsList[BTWBlocks.spiderEyeSlab.blockID] = new SlabBlockItem(BTWBlocks.spiderEyeSlab.blockID - 256);
      Item.itemsList[BTWBlocks.oakSapling.blockID] = new SaplingBlockItem(BTWBlocks.oakSapling.blockID - 256, BTWBlocks.oakSapling);
      Item.itemsList[BTWBlocks.spruceSapling.blockID] = new SaplingBlockItem(BTWBlocks.spruceSapling.blockID - 256, BTWBlocks.spruceSapling);
      Item.itemsList[BTWBlocks.birchSapling.blockID] = new SaplingBlockItem(BTWBlocks.birchSapling.blockID - 256, BTWBlocks.birchSapling);
      Item.itemsList[BTWBlocks.jungleSapling.blockID] = new SaplingBlockItem(BTWBlocks.jungleSapling.blockID - 256, BTWBlocks.jungleSapling);
      Item.itemsList[Block.wood.blockID] = new LogBlockItem(Block.wood.blockID - 256, Block.wood, BlockLog.woodType).b("log");
      Item.itemsList[Block.planks.blockID] = new ItemMultiTextureTile(Block.planks.blockID - 256, Block.planks, PlanksBlock.woodTypes).b("wood");
      Item.itemsList[Block.lever.blockID] = new LeverBlockItem(Block.lever.blockID - 256).b("lever");
      Item.itemsList[Block.ice.blockID] = new IceBlockItem(Block.ice.blockID - 256).b("ice");
      Item.itemsList[Block.snow.blockID] = new SnowBlockItem(Block.snow.blockID - 256, Block.snow).b("snow");
      Item.itemsList[Block.stone.blockID] = new ItemBlockWithMetadata(Block.stone.blockID - 256, Block.stone).b("stone");
      Item.itemsList[Block.cobblestone.blockID] = new ItemBlockWithMetadata(Block.cobblestone.blockID - 256, Block.cobblestone).b("stonebrick");
      Item.itemsList[Block.oreCoal.blockID] = new ItemBlockWithMetadata(Block.oreCoal.blockID - 256, Block.oreCoal).b("oreCoal");
      Item.itemsList[Block.oreIron.blockID] = new ItemBlockWithMetadata(Block.oreIron.blockID - 256, Block.oreIron).b("oreIron");
      Item.itemsList[Block.oreGold.blockID] = new ItemBlockWithMetadata(Block.oreGold.blockID - 256, Block.oreGold).b("oreGold");
      Item.itemsList[Block.oreDiamond.blockID] = new ItemBlockWithMetadata(Block.oreDiamond.blockID - 256, Block.oreDiamond).b("oreDiamond");
      Item.itemsList[Block.oreEmerald.blockID] = new ItemBlockWithMetadata(Block.oreEmerald.blockID - 256, Block.oreEmerald).b("oreEmerald");
      Item.itemsList[Block.oreLapis.blockID] = new ItemBlockWithMetadata(Block.oreLapis.blockID - 256, Block.oreLapis).b("oreLapis");
      Item.itemsList[Block.oreRedstone.blockID] = new ItemBlockWithMetadata(Block.oreRedstone.blockID - 256, Block.oreRedstone).b("oreRedstone");
      Item.itemsList[Block.waterlily.blockID] = new ItemLilyPad(Block.waterlily.blockID - 256).b("waterlily");
      Item.itemsList[Block.anvil.blockID] = new AnvilBlockItem(Block.anvil.blockID - 256).b("anvil");
      Item.itemsList[Block.torchWood.blockID] = new LegacyTorchBlockItem(Block.torchWood.blockID - 256).b("torch");
      Item.itemsList[Block.cobblestoneMossy.blockID] = new ItemBlockWithMetadata(Block.cobblestoneMossy.blockID - 256, Block.cobblestoneMossy).b("stoneMoss");
      Item.itemsList[Block.melon.blockID].setMaxStackSize(16);
      Item.itemsList[Block.workbench.blockID] = new LegacySubstitutionBlockItem(Block.workbench.blockID - 256, BTWBlocks.workbench.blockID);
      Item.itemsList[Block.chest.blockID] = new LegacySubstitutionBlockItem(Block.chest.blockID - 256, BTWBlocks.chest.blockID);
      Item.itemsList[Block.doorWood.blockID] = new LegacySubstitutionBlockItem(Block.doorWood.blockID - 256, BTWBlocks.woodenDoor.blockID);
      Item.itemsList[Block.web.blockID] = new LegacySubstitutionBlockItem(Block.web.blockID - 256, BTWBlocks.web.blockID);
      Item.itemsList[Block.ladder.blockID] = new LegacySubstitutionBlockItem(Block.ladder.blockID - 256, BTWBlocks.ladder.blockID);
      Item.itemsList[Block.blockSnow.blockID] = new LegacySubstitutionBlockItem(Block.blockSnow.blockID - 256, BTWBlocks.solidSnow.blockID);
      Item.suppressConflictWarnings = false;
   }
}
