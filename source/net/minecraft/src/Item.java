package net.minecraft.src;

import btw.AddonHandler;
import btw.BTWAddon;
import btw.block.BTWBlocks;
import btw.crafting.util.FurnaceBurnTime;
import btw.item.items.ArmorItemChain;
import btw.item.items.ArmorItemDiamond;
import btw.item.items.ArmorItemGold;
import btw.item.items.ArmorItemIron;
import btw.item.items.ArmorItemLeather;
import btw.item.items.ArrowItem;
import btw.item.items.AxeItem;
import btw.item.items.BedItem;
import btw.item.items.BoatItem;
import btw.item.items.BoneItem;
import btw.item.items.BookItem;
import btw.item.items.BowItem;
import btw.item.items.BrickItem;
import btw.item.items.BucketItemEmpty;
import btw.item.items.BucketItemLava;
import btw.item.items.BucketItemMilk;
import btw.item.items.BucketItemWater;
import btw.item.items.CarrotOnAStickItem;
import btw.item.items.ClayItem;
import btw.item.items.DoorItemWood;
import btw.item.items.DyeItem;
import btw.item.items.EggItem;
import btw.item.items.EmptyMapItem;
import btw.item.items.EnchantedBookItem;
import btw.item.items.FireChargeItem;
import btw.item.items.FishingRodItem;
import btw.item.items.FlintAndSteelItem;
import btw.item.items.FlintItem;
import btw.item.items.FoodItem;
import btw.item.items.GlassBottleItem;
import btw.item.items.HighResolutionFoodItem;
import btw.item.items.HoeItem;
import btw.item.items.MapItem;
import btw.item.items.MinecartItem;
import btw.item.items.MushroomSoupItem;
import btw.item.items.NetherQuartzItem;
import btw.item.items.NetherStarItem;
import btw.item.items.PickaxeItem;
import btw.item.items.PlaceAsBlockItem;
import btw.item.items.PotionItem;
import btw.item.items.RedstoneItem;
import btw.item.items.RedstoneRepeaterItem;
import btw.item.items.RottenFleshItem;
import btw.item.items.SeedFoodItem;
import btw.item.items.SeedItem;
import btw.item.items.ShaftItem;
import btw.item.items.ShearsItem;
import btw.item.items.ShovelItem;
import btw.item.items.ShovelItemStone;
import btw.item.items.SignItem;
import btw.item.items.SlimeballItem;
import btw.item.items.SnowballItem;
import btw.item.items.StubItem;
import btw.item.items.SwordItem;
import btw.item.items.legacy.LegacyWheatItem;
import btw.util.ReflectionUtils;
import com.prupe.mcpatcher.cit.CITUtils;
import java.lang.reflect.Constructor;
import java.util.List;
import java.util.Random;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

public class Item {
   private CreativeTabs tabToDisplayOn = null;
   protected static Random itemRand = new Random();
   public static Item[] itemsList = new Item[32000];
   public static final int FILTERABLE_NO_PROPERTIES = 0;
   public static final int FILTERABLE_SOLID_BLOCK = 1;
   public static final int FILTERABLE_SMALL = 2;
   public static final int FILTERABLE_NARROW = 4;
   public static final int FILTERABLE_FINE = 8;
   public static final int FILTERABLE_THIN = 16;
   public static Item shovelIron = new ShovelItem(0, EnumToolMaterial.IRON).b("shovelIron");
   public static Item pickaxeIron = new PickaxeItem(1, EnumToolMaterial.IRON).b("pickaxeIron");
   public static Item axeIron = new AxeItem(2, EnumToolMaterial.IRON).b("hatchetIron");
   public static Item flintAndSteel = new FlintAndSteelItem(3).b("flintAndSteel");
   public static Item appleRed = new ItemFood(4, 1, 0.0F, false).setFilterableProperties(2).setUnlocalizedName("apple");
   public static ItemBow bow = new BowItem(5);
   public static Item arrow = new ArrowItem(6);
   public static Item coal = new ItemCoal(7)
      .setIncineratedInCrucible()
      .setfurnaceburntime(FurnaceBurnTime.COAL)
      .setFilterableProperties(2)
      .setUnlocalizedName("coal");
   public static Item diamond = new Item(8).setFilterableProperties(2).setUnlocalizedName("diamond").setCreativeTab(CreativeTabs.tabMaterials);
   public static Item ingotIron = new Item(9).setUnlocalizedName("ingotIron").setCreativeTab(CreativeTabs.tabMaterials);
   public static Item ingotGold = new Item(10).setUnlocalizedName("ingotGold").setCreativeTab(CreativeTabs.tabMaterials);
   public static Item swordIron = new SwordItem(11, EnumToolMaterial.IRON).b("swordIron");
   public static Item swordWood = new SwordItem(12, EnumToolMaterial.WOOD).b("swordWood");
   public static Item shovelWood = new ShovelItem(13, EnumToolMaterial.WOOD).setDamageVsEntity(2).b("shovelWood");
   public static Item pickaxeWood = new PickaxeItem(14, EnumToolMaterial.WOOD, 1).b("pickaxeWood");
   public static Item axeWood = new AxeItem(15, EnumToolMaterial.WOOD).b("hatchetWood");
   public static Item swordStone = new SwordItem(16, EnumToolMaterial.STONE).b("swordStone");
   public static Item shovelStone = new ShovelItemStone(17);
   public static Item pickaxeStone = new PickaxeItem(18, EnumToolMaterial.STONE).b("pickaxeStone");
   public static Item axeStone = new AxeItem(19, EnumToolMaterial.STONE).b("hatchetStone");
   public static Item swordDiamond = new SwordItem(20, EnumToolMaterial.EMERALD).b("swordDiamond");
   public static Item shovelDiamond = new ShovelItem(21, EnumToolMaterial.EMERALD).b("shovelDiamond");
   public static Item pickaxeDiamond = new PickaxeItem(22, EnumToolMaterial.EMERALD).b("pickaxeDiamond");
   public static Item axeDiamond = new AxeItem(23, EnumToolMaterial.EMERALD).b("hatchetDiamond");
   public static Item stick = new ShaftItem(24);
   public static Item bowlEmpty = new Item(25).setBuoyant().setIncineratedInCrucible().setUnlocalizedName("bowl").setCreativeTab(CreativeTabs.tabMaterials);
   public static Item bowlSoup = new MushroomSoupItem(26, 3).b("mushroomStew");
   public static Item swordGold = new SwordItem(27, EnumToolMaterial.GOLD).b("swordGold");
   public static Item shovelGold = new ShovelItem(28, EnumToolMaterial.GOLD).b("shovelGold");
   public static Item pickaxeGold = new PickaxeItem(29, EnumToolMaterial.GOLD).b("pickaxeGold");
   public static Item axeGold = new AxeItem(30, EnumToolMaterial.GOLD).b("hatchetGold");
   public static Item silk = new Item(31)
      .setBuoyant()
      .setBellowsBlowDistance(2)
      .setIncineratedInCrucible()
      .setFilterableProperties(18)
      .setUnlocalizedName("string")
      .setCreativeTab(CreativeTabs.tabMaterials);
   public static Item feather = new Item(32)
      .setBuoyant()
      .setfurnaceburntime(FurnaceBurnTime.KINDLING)
      .setIncineratedInCrucible()
      .setBellowsBlowDistance(3)
      .setFilterableProperties(18)
      .setUnlocalizedName("feather")
      .setCreativeTab(CreativeTabs.tabMaterials);
   public static Item gunpowder = new Item(33)
      .setBellowsBlowDistance(3)
      .setFilterableProperties(8)
      .setUnlocalizedName("sulphur")
      .setPotionEffect(PotionHelper.gunpowderEffect)
      .setCreativeTab(CreativeTabs.tabMaterials);
   public static Item hoeWood = new HoeItem(34, EnumToolMaterial.WOOD).b("hoeWood");
   public static Item hoeStone = new HoeItem(35, EnumToolMaterial.STONE).b("hoeStone");
   public static Item hoeIron = new HoeItem(36, EnumToolMaterial.IRON).b("hoeIron");
   public static Item hoeDiamond = new HoeItem(37, EnumToolMaterial.EMERALD).b("hoeDiamond");
   public static Item hoeGold = new HoeItem(38, EnumToolMaterial.GOLD).b("hoeGold");
   public static Item seeds = new SeedItem(39, Block.crops.blockID).setAsBasicChickenFood().setUnlocalizedName("seeds").setCreativeTab(null);
   public static Item wheat = new LegacyWheatItem(40);
   public static Item bread = new ItemFood(41, 3, 0.25F, false).b("bread");
   public static ItemArmor helmetLeather = (ItemArmor)new ArmorItemLeather(42, 0).b("helmetCloth");
   public static ItemArmor plateLeather = (ItemArmor)new ArmorItemLeather(43, 1).b("chestplateCloth");
   public static ItemArmor legsLeather = (ItemArmor)new ArmorItemLeather(44, 2).b("leggingsCloth");
   public static ItemArmor bootsLeather = (ItemArmor)new ArmorItemLeather(45, 3).b("bootsCloth");
   public static ItemArmor helmetChain = (ItemArmor)new ArmorItemChain(46, 0, 3).b("helmetChain");
   public static ItemArmor plateChain = (ItemArmor)new ArmorItemChain(47, 1, 4).b("chestplateChain");
   public static ItemArmor legsChain = (ItemArmor)new ArmorItemChain(48, 2, 4).b("leggingsChain");
   public static ItemArmor bootsChain = (ItemArmor)new ArmorItemChain(49, 3, 2).b("bootsChain");
   public static ItemArmor helmetIron = (ItemArmor)new ArmorItemIron(50, 0, 5).b("helmetIron");
   public static ItemArmor plateIron = (ItemArmor)new ArmorItemIron(51, 1, 8).b("chestplateIron");
   public static ItemArmor legsIron = (ItemArmor)new ArmorItemIron(52, 2, 7).b("leggingsIron");
   public static ItemArmor bootsIron = (ItemArmor)new ArmorItemIron(53, 3, 4).b("bootsIron");
   public static ItemArmor helmetDiamond = (ItemArmor)new ArmorItemDiamond(54, 0, 5).b("helmetDiamond");
   public static ItemArmor plateDiamond = (ItemArmor)new ArmorItemDiamond(55, 1, 8).b("chestplateDiamond");
   public static ItemArmor legsDiamond = (ItemArmor)new ArmorItemDiamond(56, 2, 7).b("leggingsDiamond");
   public static ItemArmor bootsDiamond = (ItemArmor)new ArmorItemDiamond(57, 3, 4).b("bootsDiamond");
   public static ItemArmor helmetGold = (ItemArmor)new ArmorItemGold(58, 0, 5).b("helmetGold");
   public static ItemArmor plateGold = (ItemArmor)new ArmorItemGold(59, 1, 8).b("chestplateGold");
   public static ItemArmor legsGold = (ItemArmor)new ArmorItemGold(60, 2, 7).b("leggingsGold");
   public static ItemArmor bootsGold = (ItemArmor)new ArmorItemGold(61, 3, 4).b("bootsGold");
   public static Item flint = new FlintItem(62);
   public static Item porkRaw = new FoodItem(63, 4, 0.25F, true, "porkchopRaw", true).setStandardFoodPoisoningEffect();
   public static Item porkCooked = new ItemFood(64, 5, 0.25F, true).b("porkchopCooked");
   public static Item painting = new ItemHangingEntity(65, EntityPainting.class).setBuoyant().setIncineratedInCrucible().setUnlocalizedName("painting");
   public static Item appleGold = new ItemAppleGold(66, 1, 0.0F, false)
      .j()
      .setPotionEffect(Potion.regeneration.id, 5, 0, 1.0F)
      .setNonBuoyant()
      .setNotIncineratedInCrucible()
      .setFilterableProperties(2)
      .setUnlocalizedName("appleGold");
   public static Item sign = new SignItem(67);
   public static Item doorWood = new DoorItemWood(68);
   public static Item bucketEmpty = new BucketItemEmpty(69);
   public static Item bucketWater = new BucketItemWater(70);
   public static Item bucketLava = new BucketItemLava(71);
   public static Item minecartEmpty = new MinecartItem(72, 0).b("minecart");
   public static Item saddle = new ItemSaddle(73).setBuoyant().setIncineratedInCrucible().setUnlocalizedName("saddle");
   public static Item doorIron = new ItemDoor(74, Material.iron).b("doorIron");
   public static Item redstone = new RedstoneItem(75);
   public static Item snowball = new SnowballItem(76);
   public static Item boat = new BoatItem(77);
   public static Item leather = new Item(78)
      .setBuoyant()
      .setIncineratedInCrucible()
      .setFilterableProperties(16)
      .setUnlocalizedName("leather")
      .setCreativeTab(CreativeTabs.tabMaterials);
   public static Item bucketMilk = new BucketItemMilk(79);
   public static Item brick = new BrickItem(80);
   public static Item clay = new ClayItem(81);
   public static Item reed = new ItemReed(82, Block.reed)
      .setBuoyant()
      .setfurnaceburntime(FurnaceBurnTime.KINDLING)
      .setIncineratedInCrucible()
      .setFilterableProperties(4)
      .setUnlocalizedName("reeds")
      .setCreativeTab(CreativeTabs.tabMaterials);
   public static Item paper = new Item(83)
      .setBuoyant()
      .setBellowsBlowDistance(3)
      .setfurnaceburntime(FurnaceBurnTime.KINDLING)
      .setIncineratedInCrucible()
      .setFilterableProperties(18)
      .setUnlocalizedName("paper")
      .setCreativeTab(CreativeTabs.tabMisc);
   public static Item book = new BookItem(84);
   public static Item slimeBall = new SlimeballItem(85);
   public static Item minecartCrate = new MinecartItem(86, 1).b("minecartChest");
   public static Item minecartPowered = new MinecartItem(87, 2).b("minecartFurnace");
   public static Item egg = new EggItem(88);
   public static Item compass = new Item(89).setUnlocalizedName("compass").setCreativeTab(CreativeTabs.tabTools);
   public static ItemFishingRod fishingRod = new FishingRodItem(90);
   public static Item pocketSundial = new Item(91).setUnlocalizedName("clock").setCreativeTab(CreativeTabs.tabTools);
   public static Item lightStoneDust = new Item(92)
      .setBellowsBlowDistance(3)
      .setFilterableProperties(8)
      .setUnlocalizedName("yellowDust")
      .setCreativeTab(CreativeTabs.tabMaterials);
   public static Item fishRaw = new FoodItem(93, 3, 0.25F, false, "fishRaw").setStandardFoodPoisoningEffect();
   public static Item fishCooked = new ItemFood(94, 4, 0.25F, false).b("fishCooked");
   public static Item dyePowder = new DyeItem(95);
   public static Item bone = new BoneItem(96);
   public static Item sugar = new Item(97)
      .setBuoyant()
      .setBellowsBlowDistance(3)
      .setIncineratedInCrucible()
      .setFilterableProperties(8)
      .setUnlocalizedName("sugar")
      .setCreativeTab(CreativeTabs.tabMaterials);
   public static Item cake = new ItemReed(98, Block.cake)
      .setBuoyant()
      .setIncineratedInCrucible()
      .setMaxStackSize(1)
      .setUnlocalizedName("cake")
      .setCreativeTab(CreativeTabs.tabFood);
   public static Item bed = new BedItem(99, Block.bed.blockID).setBuoyant().setIncineratedInCrucible().setMaxStackSize(1).setUnlocalizedName("bed");
   public static Item redstoneRepeater = new RedstoneRepeaterItem(100);
   public static Item cookie = new ItemFood(101, 1, 1.0F, false).setAlwaysEdible().setFilterableProperties(2).setUnlocalizedName("cookie");
   public static ItemMap map = new MapItem(102);
   public static ItemShears shears = (ItemShears)new ShearsItem(103).b("shears");
   public static Item melon = new HighResolutionFoodItem(104, 2, 0.0F, false, "melon");
   public static Item pumpkinSeeds = new SeedFoodItem(105, 1, 0.0F, Block.pumpkinStem.blockID)
      .setAsBasicChickenFood()
      .setBellowsBlowDistance(2)
      .setFilterableProperties(8)
      .setUnlocalizedName("seeds_pumpkin");
   public static Item melonSeeds = new SeedItem(106, Block.melonStem.blockID).setAsBasicChickenFood().setUnlocalizedName("seeds_melon");
   public static Item beefRaw = new FoodItem(107, 4, 0.25F, true, "beefRaw", true).setStandardFoodPoisoningEffect();
   public static Item beefCooked = new ItemFood(108, 5, 0.25F, true).b("beefCooked");
   public static Item chickenRaw = new FoodItem(109, 3, 0.25F, true, "chickenRaw").setStandardFoodPoisoningEffect();
   public static Item chickenCooked = new ItemFood(110, 4, 0.25F, true).b("chickenCooked");
   public static Item rottenFlesh = new RottenFleshItem(111);
   public static Item enderPearl = new ItemEnderPearl(112).setFilterableProperties(2).setUnlocalizedName("enderPearl");
   public static Item blazeRod = new Item(113)
      .setfurnaceburntime(FurnaceBurnTime.BLAZE_ROD)
      .setFilterableProperties(4)
      .setUnlocalizedName("blazeRod")
      .setCreativeTab(CreativeTabs.tabMaterials);
   public static Item ghastTear = new Item(114)
      .setFilterableProperties(2)
      .setUnlocalizedName("ghastTear")
      .setPotionEffect("+0-1-2-3&4-4+13")
      .setCreativeTab(CreativeTabs.tabBrewing);
   public static Item goldNugget = new Item(115).setFilterableProperties(2).setUnlocalizedName("goldNugget").setCreativeTab(CreativeTabs.tabMaterials);
   public static Item netherStalkSeeds = new SeedItem(116, Block.netherStalk.blockID)
      .setBellowsBlowDistance(1)
      .setUnlocalizedName("netherStalkSeeds")
      .setPotionEffect("+4");
   public static ItemPotion potion = new PotionItem(117);
   public static Item glassBottle = new GlassBottleItem(118).setBuoyant().setUnlocalizedName("glassBottle");
   public static Item spiderEye = new ItemFood(119, 2, 0.8F, false)
      .setPotionEffect(Potion.poison.id, 5, 0, 1.0F)
      .setNeutralBuoyant()
      .setFilterableProperties(2)
      .setPotionEffect(PotionHelper.goldenCarrotEffect)
      .setUnlocalizedName("spiderEye");
   public static Item fermentedSpiderEye = new Item(120)
      .setNeutralBuoyant()
      .setIncineratedInCrucible()
      .setFilterableProperties(2)
      .setUnlocalizedName("fermentedSpiderEye")
      .setPotionEffect(PotionHelper.fermentedSpiderEyeEffect)
      .setCreativeTab(CreativeTabs.tabBrewing);
   public static Item blazePowder = new Item(121)
      .setBellowsBlowDistance(3)
      .setFilterableProperties(8)
      .setUnlocalizedName("blazePowder")
      .setPotionEffect(PotionHelper.blazePowderEffect)
      .setCreativeTab(CreativeTabs.tabBrewing);
   public static Item magmaCream = new Item(122)
      .setNeutralBuoyant()
      .setUnlocalizedName("magmaCream")
      .setPotionEffect(PotionHelper.magmaCreamEffect)
      .setCreativeTab(CreativeTabs.tabBrewing);
   public static Item brewingStand = new ItemReed(123, Block.brewingStand).b("brewingStand").setCreativeTab(CreativeTabs.tabBrewing);
   public static Item cauldron = new ItemReed(124, Block.cauldron).b("cauldron").setCreativeTab(CreativeTabs.tabBrewing);
   public static Item eyeOfEnder = new ItemEnderEye(125).setFilterableProperties(2).setUnlocalizedName("eyeOfEnder");
   public static Item speckledMelon = new Item(126).setUnlocalizedName("speckledMelon").setCreativeTab(CreativeTabs.tabFood);
   public static Item monsterPlacer = new ItemMonsterPlacer(127).b("monsterPlacer");
   public static Item expBottle = new ItemExpBottle(128).b("expBottle");
   public static Item fireballCharge = new FireChargeItem(129);
   public static Item writableBook = new ItemWritableBook(130)
      .setBuoyant()
      .setIncineratedInCrucible()
      .setUnlocalizedName("writingBook")
      .setCreativeTab(CreativeTabs.tabMisc);
   public static Item writtenBook = new ItemEditableBook(131).setBuoyant().setIncineratedInCrucible().setUnlocalizedName("writtenBook");
   public static Item emerald = new Item(132).setUnlocalizedName("emerald").setCreativeTab(CreativeTabs.tabMaterials);
   public static Item itemFrame = new ItemHangingEntity(133, EntityItemFrame.class)
      .setBuoyant()
      .setIncineratedInCrucible()
      .setFilterableProperties(1)
      .setUnlocalizedName("frame");
   public static Item flowerPot = new ItemReed(134, Block.flowerPot)
      .setBuoyant()
      .setFilterableProperties(1)
      .setUnlocalizedName("flowerPot")
      .setCreativeTab(CreativeTabs.tabDecorations);
   public static Item carrot = new SeedFoodItem(135, 3, 0.0F, Block.carrot.blockID)
      .setFilterableProperties(2)
      .setAsBasicPigFood()
      .setUnlocalizedName("carrots")
      .setCreativeTab(null);
   public static Item potato = new SeedFoodItem(136, 3, 0.0F, Block.potato.blockID).setFilterableProperties(2).setAsBasicPigFood().setUnlocalizedName("potato");
   public static Item bakedPotato = new ItemFood(137, 2, 0.0F, false).setFilterableProperties(2).setAsBasicPigFood().setUnlocalizedName("potatoBaked");
   public static Item poisonousPotato = new ItemFood(138, 1, 0.0F, false)
      .setPotionEffect(Potion.poison.id, 5, 0, 0.6F)
      .setFilterableProperties(2)
      .setUnlocalizedName("potatoPoisonous");
   public static ItemEmptyMap emptyMap = new EmptyMapItem(139);
   public static Item goldenCarrot = new ItemFood(140, 1, 0.0F, false).setNonBuoyant().setFilterableProperties(2).setUnlocalizedName("carrotGolden");
   public static Item skull = new ItemSkull(141).setBuoyant().setIncineratedInCrucible().setFilterableProperties(1).setUnlocalizedName("skull");
   public static Item carrotOnAStick = new CarrotOnAStickItem(142);
   public static Item netherStar = new NetherStarItem(143);
   public static Item pumpkinPie = new ItemFood(144, 2, 2.5F, false).setAlwaysEdible().b("pumpkinPie").setCreativeTab(CreativeTabs.tabFood);
   public static Item firework = new ItemFirework(145).b("fireworks");
   public static Item fireworkCharge = new ItemFireworkCharge(146).b("fireworksCharge").setCreativeTab(CreativeTabs.tabMisc);
   public static ItemEnchantedBook enchantedBook = (ItemEnchantedBook)new EnchantedBookItem(147).d(1).setUnlocalizedName("enchantedBook");
   public static Item comparator = new PlaceAsBlockItem(148, Block.redstoneComparatorIdle.blockID).b("comparator").setCreativeTab(CreativeTabs.tabRedstone);
   public static Item netherrackBrick = new Item(149).setUnlocalizedName("netherbrick");
   public static Item netherQuartz = new NetherQuartzItem(150).b("netherquartz").setCreativeTab(CreativeTabs.tabMaterials);
   public static Item minecartTnt = new StubItem(151).b("minecartTnt");
   public static Item minecartHopper = new StubItem(152).b("minecartHopper");
   @Environment(EnvType.CLIENT)
   public static Item tntMinecart = minecartTnt;
   @Environment(EnvType.CLIENT)
   public static Item hopperMinecart = minecartHopper;
   public static Item record13 = new ItemRecord(2000, "13").b("record");
   public static Item recordCat = new ItemRecord(2001, "cat").b("record").setCreativeTab(null);
   public static Item recordBlocks = new ItemRecord(2002, "blocks").b("record").setCreativeTab(null);
   public static Item recordChirp = new ItemRecord(2003, "chirp").b("record").setCreativeTab(null);
   public static Item recordFar = new ItemRecord(2004, "far").b("record").setCreativeTab(null);
   public static Item recordMall = new ItemRecord(2005, "mall").b("record").setCreativeTab(null);
   public static Item recordMellohi = new ItemRecord(2006, "mellohi").b("record").setCreativeTab(null);
   public static Item recordStal = new ItemRecord(2007, "stal").b("record").setCreativeTab(null);
   public static Item recordStrad = new ItemRecord(2008, "strad").b("record").setCreativeTab(null);
   public static Item recordWard = new ItemRecord(2009, "ward").b("record").setCreativeTab(null);
   public static Item record11 = new ItemRecord(2010, "11").b("record").setCreativeTab(null);
   public static Item recordWait = new ItemRecord(2011, "wait").b("record").setCreativeTab((CreativeTabs)null);
   public final int itemID;
   protected int maxStackSize = 64;
   private int maxDamage = 0;
   protected boolean full3D = false;
   protected boolean hasSubtypes = false;
   private Item containerItem = null;
   private String potionEffect = null;
   private String unlocalizedName;
   @Environment(EnvType.CLIENT)
   public Icon itemIcon;
   public static final boolean[] itemReplaced = new boolean[32000];
   public static final String[] itemReplacedBy = new String[32000];
   private Class entityClass = EntityItem.class;
   public static boolean suppressConflictWarnings = false;
   public static final int BASE_HERBIVORE_ITEM_FOOD_VALUE = 6400;
   public static final int BASE_PIG_ITEM_FOOD_VALUE = 6400;
   public static final int BASE_CHICKEN_ITEM_FOOD_VALUE = 6400;
   private int herbivoreFoodValue = 0;
   private int birdFoodValue = 0;
   private int pigFoodValue = 0;
   private float buoyancy = -1.0F;
   private int bellowsBlowDistance = 0;
   private int infernalMaxNumEnchants = 0;
   private int infernalMaxEnchantmentCost = 0;
   protected int defaultFurnaceBurnTime = 0;
   protected boolean isInceratedInCrucible = false;
   protected int filterablePropertiesBitfield = 0;

   public Item(int par1) {
      this.itemID = 256 + par1;
      if (!suppressConflictWarnings && itemsList[256 + par1] != null) {
         System.out.println("CONFLICT @ " + par1);
      }

      itemsList[256 + par1] = this;
   }

   public Item setMaxStackSize(int par1) {
      this.maxStackSize = par1;
      return this;
   }

   @Environment(EnvType.CLIENT)
   public int getSpriteNumber() {
      return 1;
   }

   @Environment(EnvType.CLIENT)
   public Icon getIconFromDamage(int par1) {
      return this.itemIcon;
   }

   @Environment(EnvType.CLIENT)
   public final Icon getIconIndex(ItemStack par1ItemStack) {
      return CITUtils.getIcon(this.getIconFromDamage(par1ItemStack.getItemDamage()), par1ItemStack, 0);
   }

   public boolean onItemUse(
      ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10
   ) {
      return false;
   }

   public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
      return par1ItemStack;
   }

   public ItemStack onEaten(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
      return par1ItemStack;
   }

   public int getItemStackLimit() {
      return this.maxStackSize;
   }

   public int getMetadata(int par1) {
      return 0;
   }

   public boolean getHasSubtypes() {
      return this.hasSubtypes;
   }

   protected Item setHasSubtypes(boolean par1) {
      this.hasSubtypes = par1;
      return this;
   }

   public int getMaxDamage() {
      return this.maxDamage;
   }

   protected Item setMaxDamage(int par1) {
      this.maxDamage = par1;
      return this;
   }

   public boolean isDamageable() {
      return this.maxDamage > 0 && !this.hasSubtypes;
   }

   public boolean hitEntity(ItemStack par1ItemStack, EntityLiving par2EntityLiving, EntityLiving par3EntityLiving) {
      return false;
   }

   public boolean onBlockDestroyed(ItemStack par1ItemStack, World par2World, int par3, int par4, int par5, int par6, EntityLiving par7EntityLiving) {
      return false;
   }

   public int getDamageVsEntity(Entity par1Entity) {
      return 1;
   }

   public boolean itemInteractionForEntity(ItemStack par1ItemStack, EntityLiving par2EntityLiving) {
      return false;
   }

   public Item setFull3D() {
      this.full3D = true;
      return this;
   }

   @Environment(EnvType.CLIENT)
   public boolean isFull3D() {
      return this.full3D;
   }

   @Environment(EnvType.CLIENT)
   public boolean shouldRotateAroundWhenRendering() {
      return false;
   }

   public Item setUnlocalizedName(String par1Str) {
      this.unlocalizedName = par1Str;
      return this;
   }

   public String getLocalizedName(ItemStack par1ItemStack) {
      String var2 = this.getUnlocalizedName(par1ItemStack);
      return var2 == null ? "" : StatCollector.translateToLocal(var2);
   }

   public String getUnlocalizedName() {
      return "item." + this.unlocalizedName;
   }

   public String getUnlocalizedName(ItemStack par1ItemStack) {
      return "item." + this.unlocalizedName;
   }

   public Item setContainerItem(Item par1Item) {
      this.containerItem = par1Item;
      return this;
   }

   public boolean doesContainerItemLeaveCraftingGrid(ItemStack par1ItemStack) {
      return true;
   }

   public boolean getShareTag() {
      return true;
   }

   public Item getContainerItem() {
      return this.containerItem;
   }

   public boolean hasContainerItem() {
      return this.containerItem != null;
   }

   public String getStatName() {
      return StatCollector.translateToLocal(this.getUnlocalizedName() + ".name");
   }

   public String func_77653_i(ItemStack par1ItemStack) {
      return StatCollector.translateToLocal(this.getUnlocalizedName(par1ItemStack) + ".name");
   }

   @Environment(EnvType.CLIENT)
   public int getColorFromItemStack(ItemStack par1ItemStack, int par2) {
      return 16777215;
   }

   public void onUpdate(ItemStack stack, World world, EntityPlayer entity, int iInventorySlot, boolean bIsHandHeldItem) {
   }

   public void onCreated(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
   }

   public boolean isMap() {
      return false;
   }

   public EnumAction getItemUseAction(ItemStack par1ItemStack) {
      return EnumAction.none;
   }

   public int getMaxItemUseDuration(ItemStack par1ItemStack) {
      return 0;
   }

   public void onPlayerStoppedUsing(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer, int par4) {
   }

   public Item setPotionEffect(String par1Str) {
      this.potionEffect = par1Str;
      return this;
   }

   public String getPotionEffect() {
      return this.potionEffect;
   }

   public boolean isPotionIngredient() {
      return this.potionEffect != null;
   }

   @Environment(EnvType.CLIENT)
   public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
   }

   public String getItemDisplayName(ItemStack par1ItemStack) {
      return ("" + StringTranslate.getInstance().translateNamedKey(this.getLocalizedName(par1ItemStack))).trim();
   }

   @Environment(EnvType.CLIENT)
   public boolean hasEffect(ItemStack par1ItemStack) {
      return par1ItemStack.isItemEnchanted();
   }

   @Environment(EnvType.CLIENT)
   public EnumRarity getRarity(ItemStack par1ItemStack) {
      return par1ItemStack.isItemEnchanted() ? EnumRarity.rare : EnumRarity.common;
   }

   public boolean isItemTool(ItemStack par1ItemStack) {
      return this.getItemStackLimit() == 1 && this.isDamageable();
   }

   protected MovingObjectPosition getMovingObjectPositionFromPlayer(World par1World, EntityPlayer par2EntityPlayer, boolean par3) {
      float var4 = 1.0F;
      float var5 = par2EntityPlayer.prevRotationPitch + (par2EntityPlayer.rotationPitch - par2EntityPlayer.prevRotationPitch) * var4;
      float var6 = par2EntityPlayer.prevRotationYaw + (par2EntityPlayer.rotationYaw - par2EntityPlayer.prevRotationYaw) * var4;
      double var7 = par2EntityPlayer.prevPosX + (par2EntityPlayer.posX - par2EntityPlayer.prevPosX) * var4;
      double var9 = par2EntityPlayer.prevPosY + (par2EntityPlayer.posY - par2EntityPlayer.prevPosY) * var4 + 1.62 - par2EntityPlayer.yOffset;
      double var11 = par2EntityPlayer.prevPosZ + (par2EntityPlayer.posZ - par2EntityPlayer.prevPosZ) * var4;
      Vec3 var13 = par1World.getWorldVec3Pool().getVecFromPool(var7, var9, var11);
      float var14 = MathHelper.cos(-var6 * (float) (Math.PI / 180.0) - (float) Math.PI);
      float var15 = MathHelper.sin(-var6 * (float) (Math.PI / 180.0) - (float) Math.PI);
      float var16 = -MathHelper.cos(-var5 * (float) (Math.PI / 180.0));
      float var17 = MathHelper.sin(-var5 * (float) (Math.PI / 180.0));
      float var18 = var15 * var16;
      float var20 = var14 * var16;
      double var21 = 5.0;
      Vec3 var23 = var13.addVector(var18 * var21, var17 * var21, var20 * var21);
      return par1World.rayTraceBlocks_do_do(var13, var23, par3, !par3);
   }

   public int getItemEnchantability() {
      return 0;
   }

   @Environment(EnvType.CLIENT)
   public boolean requiresMultipleRenderPasses() {
      return false;
   }

   @Environment(EnvType.CLIENT)
   public Icon getIconFromDamageForRenderPass(int par1, int par2) {
      return this.getIconFromDamage(par1);
   }

   @Environment(EnvType.CLIENT)
   public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List) {
      par3List.add(new ItemStack(par1, 1, 0));
   }

   @Environment(EnvType.CLIENT)
   public CreativeTabs getCreativeTab() {
      return this.tabToDisplayOn;
   }

   public Item setCreativeTab(CreativeTabs par1CreativeTabs) {
      this.tabToDisplayOn = par1CreativeTabs;
      return this;
   }

   public boolean func_82788_x() {
      return true;
   }

   public boolean getIsRepairable(ItemStack par1ItemStack, ItemStack par2ItemStack) {
      return false;
   }

   @Environment(EnvType.CLIENT)
   public void registerIcons(IconRegister par1IconRegister) {
      this.itemIcon = par1IconRegister.registerIcon(this.unlocalizedName);
   }

   public boolean canItemBeUsedByPlayer(World world, int i, int j, int k, int iFacing, EntityPlayer player, ItemStack stack) {
      return true;
   }

   public boolean doZombiesConsume() {
      return false;
   }

   public boolean isEfficientVsBlock(ItemStack stack, World world, Block block, int i, int j, int k) {
      return false;
   }

   public boolean canHarvestBlock(ItemStack stack, World world, Block block, int i, int j, int k) {
      return false;
   }

   public float getStrVsBlock(ItemStack stack, World world, Block block, int i, int j, int k) {
      return 1.0F;
   }

   public boolean isMultiUsePerClick() {
      return true;
   }

   public float getExhaustionOnUsedToHarvestBlock(int iBlockID, World world, int i, int j, int k, int iBlockMetadata) {
      return 0.025F;
   }

   public void initializeStackOnGiveCommand(Random rand, ItemStack stack) {
   }

   public void updateUsingItem(ItemStack stack, World world, EntityPlayer player) {
   }

   public int getItemUseWarmupDuration() {
      return 7;
   }

   public boolean ignoreDamageWhenComparingDuringUse() {
      return false;
   }

   @Deprecated
   public static Item replaceItem(int id, Class newClass, BTWAddon addonPerformingReplacement, Object... parameters) {
      return replaceItem(id, newClass, new String[0], addonPerformingReplacement, parameters);
   }

   @Deprecated
   public static Item replaceItem(int id, Class newClass, String[] validAddonNamesForOverwrite, BTWAddon addonPerformingReplacement, Object... parameters) {
      if (itemReplaced[id]) {
         String replacedBy = itemReplacedBy[id];
         boolean isValidOverwrite = false;

         for (String addonName : validAddonNamesForOverwrite) {
            if (replacedBy.equals(addonName)) {
               isValidOverwrite = true;
            }
         }

         if (!isValidOverwrite) {
            throw new RuntimeException("Multiple addons attempting to replace item " + itemsList[id]);
         }
      }

      suppressConflictWarnings = true;
      Item newItem = null;
      Class[] parameterTypes = new Class[parameters.length + 1];
      Object[] parameterValues = new Object[parameters.length + 1];
      parameterTypes[0] = int.class;
      parameterValues[0] = id - 256;
      Item original = itemsList[id];
      itemsList[id] = null;

      for (int i = 0; i < parameters.length; i++) {
         Class<?> type = parameters[i].getClass();
         Class<?> primitiveType = ReflectionUtils.getPrimitiveFromBoxedClass(type);
         if (primitiveType != null) {
            type = primitiveType;
         }

         parameterTypes[i + 1] = type;
         parameterValues[i + 1] = parameters[i];
      }

      Constructor constructorToUse = ReflectionUtils.findMatchingConstructor(newClass, parameterTypes);
      if (constructorToUse != null) {
         try {
            constructorToUse.setAccessible(true);
            newItem = (Item)constructorToUse.newInstance(parameterValues);
         } catch (InstantiationException var15) {
            throw new RuntimeException("A problem has occured attempting to instantiate replacement for " + itemsList[id]);
         } catch (IllegalArgumentException var16) {
            throw new RuntimeException("Incompatible types passed to specified constructor for " + itemsList[id]);
         } catch (Exception var17) {
            var17.printStackTrace();
         }

         itemReplaced[id] = true;
         if (addonPerformingReplacement != null) {
            itemReplacedBy[id] = addonPerformingReplacement.getName();
         } else {
            AddonHandler.logWarning(
               "Deprecated item replacement call for item "
                  + itemsList[id]
                  + ". Please attach addon when handling replacement for mutual replacement handling."
            );
            itemReplacedBy[id] = "";
         }

         newItem.setFilterableProperties(original.filterablePropertiesBitfield).setBuoyancy(original.getBuoyancy(0)).setCreativeTab(original.getCreativeTab());
         if (original.isIncineratedInCrucible()) {
            newItem.setIncineratedInCrucible();
         } else {
            newItem.setNotIncineratedInCrucible();
         }

         itemsList[id] = newItem;
         suppressConflictWarnings = false;
         return newItem;
      } else {
         String message = "No appropriate constructor found for " + itemsList[id] + ": ";

         for (Class<?> paramType : parameterTypes) {
            message = message + paramType.getSimpleName() + ", ";
         }

         throw new RuntimeException(message);
      }
   }

   public int getHerbivoreFoodValue(int iItemDamage) {
      return this.herbivoreFoodValue;
   }

   public Item setHerbivoreFoodValue(int iFoodValue) {
      this.herbivoreFoodValue = iFoodValue;
      return this;
   }

   public Item setAsBasicHerbivoreFood() {
      return this.setHerbivoreFoodValue(6400);
   }

   public int getChickenFoodValue(int iItemDamage) {
      return this.birdFoodValue;
   }

   public Item setChickenFoodValue(int iFoodValue) {
      this.birdFoodValue = iFoodValue;
      return this;
   }

   public Item setAsBasicChickenFood() {
      return this.setChickenFoodValue(6400);
   }

   public int getPigFoodValue(int iItemDamage) {
      return this.pigFoodValue;
   }

   public Item setPigFoodValue(int iFoodValue) {
      this.pigFoodValue = iFoodValue;
      return this;
   }

   public Item setAsBasicPigFood() {
      return this.setPigFoodValue(6400);
   }

   public boolean isWolfFood() {
      return false;
   }

   public int getWolfHealAmount() {
      return 0;
   }

   public Item setBuoyancy(float fBuoyancy) {
      this.buoyancy = fBuoyancy;
      return this;
   }

   public Item setBuoyant() {
      return this.setBuoyancy(1.0F);
   }

   public Item setNonBuoyant() {
      return this.setBuoyancy(-1.0F);
   }

   public Item setNeutralBuoyant() {
      return this.setBuoyancy(0.0F);
   }

   public float getBuoyancy(int iItemDamage) {
      return this.buoyancy;
   }

   public int getWeightWhenWorn() {
      return 0;
   }

   public Item setBellowsBlowDistance(int iDistance) {
      this.bellowsBlowDistance = iDistance;
      return this;
   }

   public int getBellowsBlowDistance(int iItemDamage) {
      return this.bellowsBlowDistance;
   }

   public Item setInfernalMaxNumEnchants(int iMaxNumEnchants) {
      this.infernalMaxNumEnchants = iMaxNumEnchants;
      return this;
   }

   public int getInfernalMaxNumEnchants() {
      return this.infernalMaxNumEnchants;
   }

   public Item setInfernalMaxEnchantmentCost(int iMaxEnchantmentCost) {
      this.infernalMaxEnchantmentCost = iMaxEnchantmentCost;
      return this;
   }

   public int getInfernalMaxEnchantmentCost() {
      return this.infernalMaxEnchantmentCost;
   }

   public boolean isEnchantmentApplicable(Enchantment enchantment) {
      return enchantment.type == EnumEnchantmentType.all;
   }

   public boolean isConsumedInCrafting() {
      return true;
   }

   public boolean isDamagedInCrafting() {
      return false;
   }

   public void onUsedInCrafting(int iItemDamage, EntityPlayer player, ItemStack outputStack) {
      this.onUsedInCrafting(player, outputStack);
   }

   public void onUsedInCrafting(EntityPlayer player, ItemStack outputStack) {
   }

   public void onDamagedInCrafting(EntityPlayer player) {
   }

   public void onBrokenInCrafting(EntityPlayer player) {
   }

   public int getFurnaceBurnTime(int iItemDamage) {
      return this.defaultFurnaceBurnTime;
   }

   public Item setfurnaceburntime(int iBurnTime) {
      this.defaultFurnaceBurnTime = iBurnTime;
      return this;
   }

   public Item setfurnaceburntime(FurnaceBurnTime burnTime) {
      this.setfurnaceburntime(burnTime.burnTime);
      return this;
   }

   public int getCampfireBurnTime(int iItemDamage) {
      return this.getFurnaceBurnTime(iItemDamage);
   }

   public boolean getCanItemStartFireOnUse(int iItemDamage) {
      return false;
   }

   public boolean getCanItemBeSetOnFireOnUse(int iItemDamage) {
      return false;
   }

   public boolean getCanBeFedDirectlyIntoCampfire(int iItemDamage) {
      return !this.getCanItemBeSetOnFireOnUse(iItemDamage) && !this.getCanItemStartFireOnUse(iItemDamage) && this.getCampfireBurnTime(iItemDamage) > 0;
   }

   public boolean getCanBeFedDirectlyIntoBrickOven(int iItemDamage) {
      return !this.getCanItemBeSetOnFireOnUse(iItemDamage) && !this.getCanItemStartFireOnUse(iItemDamage) && this.getFurnaceBurnTime(iItemDamage) > 0;
   }

   public boolean isIncineratedInCrucible() {
      return this.isInceratedInCrucible;
   }

   public Item setIncineratedInCrucible() {
      this.isInceratedInCrucible = true;
      return this;
   }

   public Item setNotIncineratedInCrucible() {
      this.isInceratedInCrucible = false;
      return this;
   }

   public boolean doesConsumeContainerItemWhenCrafted(Item containerItem) {
      return false;
   }

   public boolean canItemPassIfFilter(ItemStack filteredItem) {
      return true;
   }

   public int getFilterableProperties(ItemStack stack) {
      return this.filterablePropertiesBitfield;
   }

   public Item setFilterableProperties(int iProperties) {
      this.filterablePropertiesBitfield = iProperties;
      return this;
   }

   public static void setAllPicksToBeEffectiveVsBlock(Block block) {
      block.setPicksEffectiveOn(true);
   }

   public static void setAllAxesToBeEffectiveVsBlock(Block block) {
      block.setAxesEffectiveOn(true);
   }

   public static void setAllShovelsToBeEffectiveVsBlock(Block block) {
      block.setShovelsEffectiveOn(true);
   }

   public boolean onItemUsedByBlockDispenser(ItemStack stack, World world, int i, int j, int k, int iFacing) {
      BTWBlocks.blockDispenser.spitOutItem(world, i, j, k, stack);
      world.playAuxSFX(1000, i, j, k, 0);
      return true;
   }

   public boolean hasCustomItemEntity() {
      return this.entityClass != EntityItem.class;
   }

   public Class getCustomItemEntity() {
      return this.entityClass;
   }

   public void setCustomItemEntity(Class entityClass) {
      this.entityClass = entityClass;
   }

   public EntityItem createItemAsEntityInWorld(World world, double x, double y, double z, ItemStack stack) {
      return null;
   }

   @Environment(EnvType.CLIENT)
   public Icon getHopperFilterIcon() {
      return null;
   }

   public Icon getAnimationIcon(EntityPlayer player) {
      return null;
   }

   static {
      StatList.initStats();
   }
}
