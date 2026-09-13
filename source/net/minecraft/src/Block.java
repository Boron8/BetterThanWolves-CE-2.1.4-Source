package net.minecraft.src;

import btw.AddonHandler;
import btw.BTWAddon;
import btw.block.BTWBlocks;
import btw.block.blocks.AnvilBlock;
import btw.block.blocks.BeaconBlock;
import btw.block.blocks.BedBlock;
import btw.block.blocks.BedrockBlock;
import btw.block.blocks.BlackStoneBlock;
import btw.block.blocks.BlackStoneStairsBlock;
import btw.block.blocks.BookshelfBlock;
import btw.block.blocks.BrewingStandBlock;
import btw.block.blocks.BrickBlock;
import btw.block.blocks.BrickStairsBlock;
import btw.block.blocks.ButtonBlockStone;
import btw.block.blocks.ButtonBlockWood;
import btw.block.blocks.CactusBlock;
import btw.block.blocks.CakeBlock;
import btw.block.blocks.CarvedPumpkinBlock;
import btw.block.blocks.ChestBlock;
import btw.block.blocks.CisternBlock;
import btw.block.blocks.ClayBlock;
import btw.block.blocks.CoalOreBlock;
import btw.block.blocks.CobblestoneBlock;
import btw.block.blocks.CobblestoneStairsBlock;
import btw.block.blocks.CocoaPlantBlock;
import btw.block.blocks.ComparatorBlock;
import btw.block.blocks.DaylightDetectorBlock;
import btw.block.blocks.DeadBushBlock;
import btw.block.blocks.DetectorRailBlock;
import btw.block.blocks.DiamondOreBlock;
import btw.block.blocks.DirtBlock;
import btw.block.blocks.DispenserBlock;
import btw.block.blocks.DoorBlock;
import btw.block.blocks.DoorBlockWood;
import btw.block.blocks.DragonEggBlock;
import btw.block.blocks.EmeraldOreBlock;
import btw.block.blocks.EnchantingTableBlock;
import btw.block.blocks.EndPortalBlock;
import btw.block.blocks.EndPortalFrameBlock;
import btw.block.blocks.EndStoneBlock;
import btw.block.blocks.EnderChestBlock;
import btw.block.blocks.FenceBlock;
import btw.block.blocks.FenceBlockWood;
import btw.block.blocks.FenceGateBlock;
import btw.block.blocks.FireBlock;
import btw.block.blocks.FlowerBlock;
import btw.block.blocks.FlowerPotBlock;
import btw.block.blocks.FurnaceBlock;
import btw.block.blocks.GlassBlock;
import btw.block.blocks.GlowStoneBlock;
import btw.block.blocks.GoldOreBlock;
import btw.block.blocks.GrassBlock;
import btw.block.blocks.GravelBlock;
import btw.block.blocks.IceBlock;
import btw.block.blocks.InfiniteBurningTorchBlock;
import btw.block.blocks.IronBarsBlock;
import btw.block.blocks.IronOreBlock;
import btw.block.blocks.JackOLanternBlock;
import btw.block.blocks.JukeboxBlock;
import btw.block.blocks.LapisOreBlock;
import btw.block.blocks.LavaBlockFlowing;
import btw.block.blocks.LavaBlockStationary;
import btw.block.blocks.LeavesBlock;
import btw.block.blocks.LeverBlock;
import btw.block.blocks.LilyPadBlock;
import btw.block.blocks.LogBlock;
import btw.block.blocks.MelonBlock;
import btw.block.blocks.MobSpawnerBlock;
import btw.block.blocks.MossyCobblestoneBlock;
import btw.block.blocks.MushroomBlock;
import btw.block.blocks.MushroomBlockBrown;
import btw.block.blocks.MyceliumBlock;
import btw.block.blocks.NetherBrickBlock;
import btw.block.blocks.NetherBrickStairsBlock;
import btw.block.blocks.NetherQuartzOreBlock;
import btw.block.blocks.NetherWartBlock;
import btw.block.blocks.NetherrackBlock;
import btw.block.blocks.NoteBlock;
import btw.block.blocks.ObsidianBlock;
import btw.block.blocks.OreStorageBlock;
import btw.block.blocks.PaneBlock;
import btw.block.blocks.PistonBlockBase;
import btw.block.blocks.PistonBlockMoving;
import btw.block.blocks.PistonExtensionBlock;
import btw.block.blocks.PlanksBlock;
import btw.block.blocks.PortalBlock;
import btw.block.blocks.PotatoBlock;
import btw.block.blocks.PowderKegBlock;
import btw.block.blocks.PressurePlateBlockStone;
import btw.block.blocks.PressurePlateBlockWood;
import btw.block.blocks.RailBlock;
import btw.block.blocks.RedstoneLampBlock;
import btw.block.blocks.RedstoneOreBlock;
import btw.block.blocks.RedstoneRepeaterBlock;
import btw.block.blocks.RedstoneWireBlock;
import btw.block.blocks.SandBlock;
import btw.block.blocks.SandstoneBlock;
import btw.block.blocks.SandstoneStairsBlock;
import btw.block.blocks.SignBlock;
import btw.block.blocks.SignBlockWall;
import btw.block.blocks.SkullBlock;
import btw.block.blocks.SnowCoverBlock;
import btw.block.blocks.SoulSandBlock;
import btw.block.blocks.StemBlock;
import btw.block.blocks.StoneBlock;
import btw.block.blocks.StoneBrickBlock;
import btw.block.blocks.StoneBrickStairsBlock;
import btw.block.blocks.StubBlock;
import btw.block.blocks.TallGrassBlock;
import btw.block.blocks.TrapDoorBlock;
import btw.block.blocks.TripWireBlock;
import btw.block.blocks.TripWireHookBlock;
import btw.block.blocks.VanillaHopperBlock;
import btw.block.blocks.VanillaSlabBlock;
import btw.block.blocks.VineBlock;
import btw.block.blocks.WallBlock;
import btw.block.blocks.WaterBlockFlowing;
import btw.block.blocks.WaterBlockStationary;
import btw.block.blocks.WebBlock;
import btw.block.blocks.WoodSlabBlock;
import btw.block.blocks.WoodStairsBlock;
import btw.block.blocks.WoolBlock;
import btw.block.blocks.WorkbenchBlock;
import btw.block.blocks.legacy.LegacyCarrotBlock;
import btw.block.blocks.legacy.LegacyFarmlandBlockUnfertilized;
import btw.block.blocks.legacy.LegacyLadderBlock;
import btw.block.blocks.legacy.LegacyMushroomCapBlock;
import btw.block.blocks.legacy.LegacySaplingBlock;
import btw.block.blocks.legacy.LegacySilverfishBlock;
import btw.block.blocks.legacy.LegacySnowBlock;
import btw.block.blocks.legacy.LegacySugarCaneBlock;
import btw.block.blocks.legacy.LegacyWheatBlock;
import btw.block.util.Flammability;
import btw.crafting.manager.KilnCraftingManager;
import btw.crafting.manager.SawCraftingManager;
import btw.crafting.manager.TurntableCraftingManager;
import btw.crafting.recipe.types.KilnRecipe;
import btw.crafting.recipe.types.SawRecipe;
import btw.crafting.recipe.types.TurntableRecipe;
import btw.crafting.util.FurnaceBurnTime;
import btw.entity.FallingBlockEntity;
import btw.item.blockitems.legacy.LegacySaplingBlockItem;
import btw.item.util.ItemUtils;
import btw.util.ReflectionUtils;
import btw.world.util.BlockPos;
import btw.world.util.WorldUtils;
import com.prupe.mcpatcher.cc.ColorizeBlock;
import java.lang.reflect.Constructor;
import java.util.List;
import java.util.Random;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

public class Block {
   private CreativeTabs displayOnCreativeTab;
   public static final StepSound soundPowderFootstep = new StepSound("stone", 1.0F, 1.0F);
   public static final StepSound soundWoodFootstep = new StepSound("wood", 1.0F, 1.0F);
   public static final StepSound soundGravelFootstep = new StepSound("gravel", 1.0F, 1.0F);
   public static final StepSound soundGrassFootstep = new StepSound("grass", 1.0F, 1.0F);
   public static final StepSound soundStoneFootstep = new StepSound("stone", 1.0F, 1.0F);
   public static final StepSound soundMetalFootstep = new StepSound("stone", 1.0F, 1.5F);
   public static final StepSound soundGlassFootstep = new StepSoundStone("stone", 1.0F, 1.0F);
   public static final StepSound soundClothFootstep = new StepSound("cloth", 1.0F, 1.0F);
   public static final StepSound soundSandFootstep = new StepSound("sand", 1.0F, 1.0F);
   public static final StepSound soundSnowFootstep = new StepSound("snow", 1.0F, 1.0F);
   public static final StepSound soundLadderFootstep = new StepSoundSand("ladder", 1.0F, 1.0F);
   public static final StepSound soundAnvilFootstep = new StepSoundAnvil("anvil", 0.3F, 1.0F);
   public static final Block[] blocksList = new Block[4096];
   public static final boolean[] opaqueCubeLookup = new boolean[4096];
   public static final int[] lightOpacity = new int[4096];
   public static final boolean[] hasKilnRecipe = new boolean[4096];
   public static final boolean[] canBlockGrass = new boolean[4096];
   public static final int[] lightValue = new int[4096];
   public static boolean[] useNeighborBrightness = new boolean[4096];
   public static Block stone = new StoneBlock(1);
   public static BlockGrass grass = new GrassBlock(2);
   public static Block dirt = new DirtBlock(3);
   public static Block cobblestone = new CobblestoneBlock(4)
      .c(2.0F)
      .setResistance(10.0F)
      .setStepSound(soundStoneFootstep)
      .setUnlocalizedName("stonebrick")
      .setCreativeTab(CreativeTabs.tabBlock);
   public static Block planks = new PlanksBlock(5);
   public static Block sapling = new LegacySaplingBlock(6).c(0.0F).setBuoyant().setStepSound(soundGrassFootstep).setUnlocalizedName("sapling");
   public static Block bedrock = new BedrockBlock(7);
   public static BlockFluid waterMoving = (BlockFluid)new WaterBlockFlowing(8, Material.water)
      .c(100.0F)
      .setLightOpacity(3)
      .setUnlocalizedName("water")
      .setCreativeTab(CreativeTabs.tabBlock)
      .disableStats();
   public static Block waterStill = new WaterBlockStationary(9, Material.water).c(100.0F).setLightOpacity(3).setUnlocalizedName("water").disableStats();
   public static BlockFluid lavaMoving = (BlockFluid)new LavaBlockFlowing(10, Material.lava)
      .c(0.0F)
      .setLightValue(1.0F)
      .setUnlocalizedName("lava")
      .setCreativeTab(CreativeTabs.tabBlock)
      .disableStats();
   public static Block lavaStill = new LavaBlockStationary(11, Material.lava).c(100.0F).setLightValue(1.0F).setUnlocalizedName("lava").disableStats();
   public static Block sand = new SandBlock(12);
   public static Block gravel = new GravelBlock(13);
   public static Block oreGold = new GoldOreBlock(14).c(3.0F).setResistance(5.0F).setStepSound(soundStoneFootstep).setUnlocalizedName("oreGold");
   public static Block oreIron = new IronOreBlock(15).c(3.0F).setResistance(5.0F).setStepSound(soundStoneFootstep).setUnlocalizedName("oreIron");
   public static Block oreCoal = new CoalOreBlock(16).c(3.0F).setResistance(5.0F).setStepSound(soundStoneFootstep).setUnlocalizedName("oreCoal");
   public static Block wood = new LogBlock(17);
   public static BlockLeaves leaves = new LeavesBlock(18);
   public static Block sponge = new BlockSponge(19).c(0.6F).setStepSound(soundGrassFootstep).setUnlocalizedName("sponge");
   public static Block glass = new GlassBlock(20, Material.glass, false).c(0.3F).setStepSound(soundGlassFootstep).setUnlocalizedName("glass");
   public static Block oreLapis = new LapisOreBlock(21).c(3.0F).setResistance(5.0F).setStepSound(soundStoneFootstep).setUnlocalizedName("oreLapis");
   public static Block blockLapis = new Block(22, Material.rock)
      .setPicksEffectiveOn()
      .setHardness(3.0F)
      .setResistance(5.0F)
      .setStepSound(soundStoneFootstep)
      .setUnlocalizedName("blockLapis")
      .setCreativeTab(CreativeTabs.tabBlock);
   public static Block dispenser = new DispenserBlock(23);
   public static Block sandStone = new SandstoneBlock(24);
   public static Block music = new NoteBlock(25);
   public static Block bed = new BedBlock(26).c(1.0F).setBuoyant().setUnlocalizedName("bed").disableStats();
   public static Block railPowered = new BlockRailPowered(27)
      .setPicksEffectiveOn()
      .setHardness(0.7F)
      .setStepSound(soundMetalFootstep)
      .setUnlocalizedName("goldenRail");
   public static Block railDetector = new DetectorRailBlock(28).c(0.7F).setStepSound(soundMetalFootstep).setUnlocalizedName("detectorRail");
   public static BlockPistonBase pistonStickyBase = (BlockPistonBase)new PistonBlockBase(29, true).c("pistonStickyBase");
   public static Block web = new WebBlock(30);
   public static BlockTallGrass tallGrass = new TallGrassBlock(31);
   public static BlockDeadBush deadBush = new DeadBushBlock(32);
   public static BlockPistonBase pistonBase = (BlockPistonBase)new PistonBlockBase(33, false).c("pistonBase");
   public static BlockPistonExtension pistonExtension = new PistonExtensionBlock(34);
   public static Block cloth = new WoolBlock();
   public static BlockPistonMoving pistonMoving = new PistonBlockMoving(36);
   public static BlockFlower plantYellow = (BlockFlower)new FlowerBlock(37).c("flower");
   public static BlockFlower plantRed = (BlockFlower)new FlowerBlock(38).c("rose");
   public static BlockFlower mushroomBrown = (BlockFlower)new MushroomBlockBrown(39, "mushroom_brown")
      .c(0.0F)
      .setBuoyant()
      .setStepSound(soundGrassFootstep)
      .setUnlocalizedName("mushroom");
   public static BlockFlower mushroomRed = (BlockFlower)new MushroomBlock(40, "mushroom_red")
      .c(0.0F)
      .setBuoyant()
      .setStepSound(soundGrassFootstep)
      .setUnlocalizedName("mushroom");
   public static Block blockGold = new OreStorageBlock(41).c(3.0F).setResistance(10.0F).setStepSound(soundMetalFootstep).setUnlocalizedName("blockGold");
   public static Block blockIron = new OreStorageBlock(42).c(5.0F).setResistance(10.0F).setStepSound(soundMetalFootstep).setUnlocalizedName("blockIron");
   public static BlockHalfSlab stoneDoubleSlab = (BlockHalfSlab)new VanillaSlabBlock(43, true)
      .setPicksEffectiveOn()
      .setHardness(2.0F)
      .setResistance(10.0F)
      .setStepSound(soundStoneFootstep)
      .setUnlocalizedName("stoneSlab");
   public static BlockHalfSlab stoneSingleSlab = (BlockHalfSlab)new VanillaSlabBlock(44, false)
      .setPicksEffectiveOn()
      .setHardness(2.0F)
      .setResistance(10.0F)
      .setStepSound(soundStoneFootstep)
      .setUnlocalizedName("stoneSlab");
   public static Block brick = new BrickBlock(45)
      .c(2.0F)
      .setResistance(10.0F)
      .setStepSound(soundStoneFootstep)
      .setUnlocalizedName("brick")
      .setCreativeTab(CreativeTabs.tabBlock);
   public static Block tnt = new PowderKegBlock(46);
   public static Block bookShelf = new BookshelfBlock(47);
   public static Block cobblestoneMossy = new MossyCobblestoneBlock(48);
   public static Block obsidian = new ObsidianBlock(49);
   public static Block torchWood = new InfiniteBurningTorchBlock(50, false);
   public static BlockFire fire = (BlockFire)new FireBlock(51)
      .c(0.0F)
      .setLightValue(1.0F)
      .setStepSound(soundWoodFootstep)
      .setUnlocalizedName("fire")
      .disableStats();
   public static Block mobSpawner = new MobSpawnerBlock(52);
   public static Block stairsWoodOak = new WoodStairsBlock(53, planks, 0).c("stairsWood");
   public static BlockChest chest = (BlockChest)new ChestBlock(54).a(null);
   public static BlockRedstoneWire redstoneWire = (BlockRedstoneWire)new RedstoneWireBlock(55)
      .c(0.0F)
      .setStepSound(soundPowderFootstep)
      .setUnlocalizedName("redstoneDust")
      .disableStats();
   public static Block oreDiamond = new DiamondOreBlock(56).c(3.0F).setResistance(5.0F).setStepSound(soundStoneFootstep).setUnlocalizedName("oreDiamond");
   public static Block blockDiamond = new OreStorageBlock(57).c(5.0F).setResistance(10.0F).setStepSound(soundMetalFootstep).setUnlocalizedName("blockDiamond");
   public static Block workbench = new WorkbenchBlock(58);
   public static Block crops = new LegacyWheatBlock(59).setBuoyant().setUnlocalizedName("crops");
   public static Block tilledField = new LegacyFarmlandBlockUnfertilized(60);
   public static Block furnaceIdle = new FurnaceBlock(61, false);
   public static Block furnaceBurning = new FurnaceBlock(62, true);
   public static Block signPost = new SignBlock(63, true);
   public static Block doorWood = new DoorBlockWood(64);
   public static Block ladder = new LegacyLadderBlock(65);
   public static Block rail = new RailBlock(66);
   public static Block stairsCobblestone = new CobblestoneStairsBlock(67, 0);
   public static Block signWall = new SignBlockWall(68);
   public static Block lever = new LeverBlock(69).c(0.5F).setStepSound(soundWoodFootstep).setUnlocalizedName("lever");
   public static Block pressurePlateStone = new PressurePlateBlockStone(70);
   public static Block doorIron = new DoorBlock(71, Material.iron).c(5.0F).setStepSound(soundMetalFootstep).setUnlocalizedName("doorIron").disableStats();
   public static Block pressurePlatePlanks = new PressurePlateBlockWood(72);
   public static Block oreRedstone = new RedstoneOreBlock(73, false)
      .c(3.0F)
      .setResistance(5.0F)
      .setStepSound(soundStoneFootstep)
      .setUnlocalizedName("oreRedstone")
      .setCreativeTab(CreativeTabs.tabBlock);
   public static Block oreRedstoneGlowing = new RedstoneOreBlock(74, true)
      .a(0.625F)
      .setHardness(3.0F)
      .setResistance(5.0F)
      .setStepSound(soundStoneFootstep)
      .setUnlocalizedName("oreRedstone")
      .setCreativeTab(null);
   public static Block torchRedstoneIdle = new BlockRedstoneTorch(75, false).c("notGate");
   public static Block torchRedstoneActive = new BlockRedstoneTorch(76, true).c("notGate").setCreativeTab(CreativeTabs.tabRedstone);
   public static Block stoneButton = new ButtonBlockStone(77).c(0.5F).setStepSound(soundStoneFootstep).setUnlocalizedName("button");
   public static Block snow = new SnowCoverBlock(78);
   public static Block ice = new IceBlock(79).c(0.5F).setBuoyant().setLightOpacity(3).setStepSound(soundGlassFootstep).setUnlocalizedName("ice");
   public static Block blockSnow = new LegacySnowBlock(80);
   public static Block cactus = new CactusBlock(81);
   public static Block blockClay = new ClayBlock(82).c(0.6F).setUnlocalizedName("fcBlockClay");
   public static Block reed = new LegacySugarCaneBlock(83).c(0.0F).setBuoyant().setStepSound(soundGrassFootstep).setUnlocalizedName("reeds").disableStats();
   public static Block jukebox = new JukeboxBlock(84);
   public static Block fence = new FenceBlockWood(85);
   public static Block pumpkin = new CarvedPumpkinBlock(86);
   public static Block netherrack = new NetherrackBlock(87);
   public static Block slowSand = new SoulSandBlock(88).c(0.5F).setStepSound(soundSandFootstep).setUnlocalizedName("hellsand");
   public static Block glowStone = new GlowStoneBlock(89);
   public static BlockPortal portal = new PortalBlock(90);
   public static Block pumpkinLantern = new JackOLanternBlock(91);
   public static Block cake = new CakeBlock(92).c(0.5F).setStepSound(soundClothFootstep).setUnlocalizedName("cake").disableStats();
   public static BlockRedstoneRepeater redstoneRepeaterIdle = new RedstoneRepeaterBlock(93, false);
   public static BlockRedstoneRepeater redstoneRepeaterActive = new RedstoneRepeaterBlock(94, true);
   public static Block lockedChest = new BlockLockedChest(95)
      .c(0.0F)
      .setLightValue(1.0F)
      .setStepSound(soundWoodFootstep)
      .setUnlocalizedName("lockedchest")
      .setTickRandomly(true);
   public static Block trapdoor = new TrapDoorBlock(96);
   public static Block silverfish = new LegacySilverfishBlock(97);
   public static Block stoneBrick = new StoneBrickBlock(98);
   public static Block mushroomCapBrown = new LegacyMushroomCapBlock(99, 0);
   public static Block mushroomCapRed = new LegacyMushroomCapBlock(100, 1);
   public static Block fenceIron = new IronBarsBlock(101);
   public static Block thinGlass = new PaneBlock(102, "glass", "thinglass_top", Material.glass, false)
      .c(0.3F)
      .setPicksEffectiveOn()
      .setStepSound(soundGlassFootstep)
      .setUnlocalizedName("thinGlass");
   public static Block melon = new MelonBlock(103).c(1.0F).setStepSound(soundWoodFootstep).setUnlocalizedName("melon");
   public static Block pumpkinStem = new StemBlock(104, pumpkin);
   public static Block melonStem = new StemBlock(105, melon);
   public static Block vine = new VineBlock(106);
   public static Block fenceGate = new FenceGateBlock(107);
   public static Block stairsBrick = new BrickStairsBlock(108).c("stairsBrick");
   public static Block stairsStoneBrick = new StoneBrickStairsBlock(109, 0);
   public static BlockMycelium mycelium = new MyceliumBlock(110);
   public static Block waterlily = new LilyPadBlock(111).c(0.0F).setStepSound(soundGrassFootstep).setUnlocalizedName("waterlily");
   public static Block netherBrick = new NetherBrickBlock(112);
   public static Block netherFence = new FenceBlock(113, "netherBrick", BTWBlocks.netherRockMaterial)
      .c(2.0F)
      .setResistance(10.0F)
      .setStepSound(soundStoneFootstep)
      .setUnlocalizedName("netherFence");
   public static Block stairsNetherBrick = new NetherBrickStairsBlock(114);
   public static Block netherStalk = new NetherWartBlock(115).c("netherStalk");
   public static Block enchantmentTable = new EnchantingTableBlock(116).c(5.0F).setResistance(2000.0F).setUnlocalizedName("enchantmentTable");
   public static Block brewingStand = new BrewingStandBlock(117).c(0.5F).setLightValue(0.125F).setUnlocalizedName("brewingStand");
   public static BlockCauldron cauldron = (BlockCauldron)new CisternBlock(118).c(2.0F).setUnlocalizedName("cauldron");
   public static Block endPortal = new EndPortalBlock(119, Material.portal).c(-1.0F).setResistance(6000000.0F);
   public static Block endPortalFrame = new EndPortalFrameBlock(120)
      .a(soundGlassFootstep)
      .setLightValue(0.125F)
      .setHardness(-1.0F)
      .setUnlocalizedName("endPortalFrame")
      .setResistance(6000000.0F)
      .setCreativeTab(CreativeTabs.tabDecorations);
   public static Block whiteStone = new EndStoneBlock(121, Material.rock)
      .c(3.0F)
      .setResistance(15.0F)
      .setStepSound(soundStoneFootstep)
      .setUnlocalizedName("whiteStone")
      .setCreativeTab(CreativeTabs.tabBlock);
   public static Block dragonEgg = new DragonEggBlock(122)
      .c(3.0F)
      .setResistance(15.0F)
      .setStepSound(soundStoneFootstep)
      .setLightValue(0.125F)
      .setUnlocalizedName("dragonEgg");
   public static Block redstoneLampIdle = new RedstoneLampBlock(123, false)
      .c(0.3F)
      .setStepSound(soundGlassFootstep)
      .setUnlocalizedName("redstoneLight")
      .setCreativeTab(CreativeTabs.tabRedstone);
   public static Block redstoneLampActive = new RedstoneLampBlock(124, true).c(0.3F).setStepSound(soundGlassFootstep).setUnlocalizedName("redstoneLight");
   public static BlockHalfSlab woodDoubleSlab = new WoodSlabBlock(125, true);
   public static BlockHalfSlab woodSingleSlab = new WoodSlabBlock(126, false);
   public static Block cocoaPlant = new CocoaPlantBlock(127)
      .c(0.2F)
      .setResistance(5.0F)
      .setBuoyant()
      .setStepSound(soundWoodFootstep)
      .setUnlocalizedName("cocoa");
   public static Block stairsSandStone = new SandstoneStairsBlock(128).c("stairsSandStone");
   public static Block oreEmerald = new EmeraldOreBlock(129).c(3.0F).setResistance(5.0F).setStepSound(soundStoneFootstep).setUnlocalizedName("oreEmerald");
   public static Block enderChest = new EnderChestBlock(130)
      .c(22.5F)
      .setResistance(1000.0F)
      .setStepSound(soundStoneFootstep)
      .setUnlocalizedName("enderChest")
      .setLightValue(0.5F);
   public static BlockTripWireSource tripWireSource = new TripWireHookBlock(131);
   public static Block tripWire = new TripWireBlock(132).c("tripWire");
   public static Block blockEmerald = new OreStorageBlock(133).c(5.0F).setResistance(10.0F).setStepSound(soundMetalFootstep).setUnlocalizedName("blockEmerald");
   public static Block stairsWoodSpruce = new WoodStairsBlock(134, planks, 1).c("stairsWoodSpruce");
   public static Block stairsWoodBirch = new WoodStairsBlock(135, planks, 2).c("stairsWoodBirch");
   public static Block stairsWoodJungle = new WoodStairsBlock(136, planks, 3).c("stairsWoodJungle");
   public static Block commandBlock = new BlockCommandBlock(137).c("commandBlock");
   public static BlockBeacon beacon = (BlockBeacon)new BeaconBlock(138).c("beacon").setLightValue(1.0F);
   public static Block cobblestoneWall = new WallBlock(139, cobblestone).c("cobbleWall");
   public static Block flowerPot = new FlowerPotBlock(140).c(0.0F).setStepSound(soundPowderFootstep).setUnlocalizedName("flowerPot");
   public static Block carrot = new LegacyCarrotBlock(141).c("carrots");
   public static Block potato = new PotatoBlock(142).c("potatoes");
   public static Block woodenButton = new ButtonBlockWood(143).c(0.5F).setStepSound(soundWoodFootstep).setUnlocalizedName("button");
   public static Block skull = new SkullBlock(144);
   public static Block anvil = new AnvilBlock(145);
   public static Block chestTrapped = new StubBlock(146).c("chestTrap");
   public static Block pressurePlateGold = new StubBlock(147).c("weightedPlate_light");
   public static Block pressurePlateIron = new StubBlock(148).c("weightedPlate_heavy");
   public static BlockComparator redstoneComparatorIdle = (BlockComparator)new ComparatorBlock(149, false)
      .c(0.0F)
      .setStepSound(soundWoodFootstep)
      .setUnlocalizedName("comparator")
      .disableStats();
   public static BlockComparator redstoneComparatorActive = (BlockComparator)new ComparatorBlock(150, true)
      .c(0.0F)
      .setStepSound(soundWoodFootstep)
      .setUnlocalizedName("comparator")
      .disableStats();
   public static BlockDaylightDetector daylightSensor = (BlockDaylightDetector)new DaylightDetectorBlock(151)
      .c(0.2F)
      .setStepSound(soundWoodFootstep)
      .setUnlocalizedName("daylightDetector");
   public static Block blockRedstone = new BlockPoweredOre(152)
      .c(5.0F)
      .setResistance(10.0F)
      .setStepSound(soundMetalFootstep)
      .setUnlocalizedName("blockRedstone")
      .setLightValue(0.75F);
   public static Block oreNetherQuartz = new NetherQuartzOreBlock(153);
   public static BlockHopper hopperBlock = new VanillaHopperBlock(154);
   public static Block blockNetherQuartz = new BlackStoneBlock(155);
   public static Block stairsNetherQuartz = new BlackStoneStairsBlock(156);
   public static Block railActivator = new StubBlock(157).c("activatorRail");
   public static Block dropper = new StubBlock(158).c("dropper");
   public final int blockID;
   public float blockHardness;
   public float blockResistance;
   protected boolean blockConstructorCalled = true;
   protected boolean enableStats = true;
   protected boolean needsRandomTick;
   protected boolean isBlockContainer;
   protected double minX = 0.0;
   protected double minY = 0.0;
   protected double minZ = 0.0;
   protected double maxX = 1.0;
   protected double maxY = 1.0;
   protected double maxZ = 1.0;
   public StepSound stepSound;
   public float blockParticleGravity;
   public Material blockMaterial;
   public float slipperiness;
   private String unlocalizedName;
   public Icon blockIcon;
   public static final boolean[] blockReplaced = new boolean[4096];
   public static final String[] blockReplacedBy = new String[4096];
   private int idDroppedOnStonecut = -1;
   private int countDroppedOnStonecut = 0;
   private int metaDroppedOnStonecut = 0;
   private MapColor[] mapColorsForMetadata;
   private static final int[] rotatedFacingsAroundJClockwise;
   private static final int[] rotatedFacingsAroundJCounterclockwise;
   private static final int[] cycledFacings;
   private static final int[] cycledFacingsReversed;
   private int defaultFurnaceBurnTime = 0;
   private boolean shovelsEffectiveOn = false;
   private boolean picksEffectiveOn = false;
   private boolean axesEffectiveOn = false;
   private boolean hoesEffectiveOn = false;
   private boolean chiselsEffectiveOn = false;
   private boolean chiselsCanHarvest = false;
   private float buoyancy = -1.0F;
   protected int filterablePropertiesBitfield = 0;
   private static final int loadedRangeToCheckFalling = 32;
   private AxisAlignedBB fixedBlockBounds = new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 1.0, 1.0);
   private boolean fixedBlockBoundsSet = false;
   private int herbivoreItemFoodValue = 0;
   private int birdItemFoodValue = 0;
   private int pigItemFoodValue = 0;
   private boolean alwaysStartlesAnimals;
   @Environment(EnvType.CLIENT)
   public RenderBlocks currentBlockRenderer = null;

   protected Block(int par1, Material par2Material) {
      this.stepSound = soundPowderFootstep;
      this.blockParticleGravity = 1.0F;
      this.slipperiness = 0.6F;
      if (blocksList[par1] != null) {
         throw new IllegalArgumentException("Slot " + par1 + " is already occupied by " + blocksList[par1] + " when adding " + this);
      } else {
         this.blockMaterial = par2Material;
         blocksList[par1] = this;
         this.blockID = par1;
         this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
         opaqueCubeLookup[par1] = this.isOpaqueCube();
         lightOpacity[par1] = this.isOpaqueCube() ? 255 : 0;
         canBlockGrass[par1] = !par2Material.getCanBlockGrass();
         useNeighborBrightness[par1] = false;
         this.setFilterableProperties(1);
      }
   }

   protected void initializeBlock() {
   }

   public Block setStepSound(StepSound par1StepSound) {
      this.stepSound = par1StepSound;
      return this;
   }

   protected Block setLightOpacity(int par1) {
      lightOpacity[this.blockID] = par1;
      return this;
   }

   public Block setLightValue(float par1) {
      lightValue[this.blockID] = (int)(15.0F * par1);
      return this;
   }

   public Block setResistance(float par1) {
      this.blockResistance = par1 * 3.0F;
      return this;
   }

   public static boolean isNormalCube(int par0) {
      Block var1 = blocksList[par0];
      return var1 == null ? false : var1.blockMaterial.isOpaque() && var1.renderAsNormalBlock();
   }

   public boolean renderAsNormalBlock() {
      return true;
   }

   public boolean getBlocksMovement(IBlockAccess par1IBlockAccess, int par2, int par3, int par4) {
      return !this.blockMaterial.blocksMovement();
   }

   public int getRenderType() {
      return 0;
   }

   public Block setHardness(float par1) {
      this.blockHardness = par1;
      if (this.blockResistance < par1 * 5.0F) {
         this.blockResistance = par1 * 5.0F;
      }

      return this;
   }

   protected Block setBlockUnbreakable() {
      this.setHardness(-1.0F);
      return this;
   }

   public float getBlockHardness(World par1World, int par2, int par3, int par4) {
      return this.blockHardness;
   }

   protected Block setTickRandomly(boolean par1) {
      this.needsRandomTick = par1;
      return this;
   }

   public boolean getTickRandomly() {
      return this.needsRandomTick;
   }

   public boolean hasTileEntity() {
      return this.isBlockContainer;
   }

   @Deprecated
   protected final void setBlockBounds(float par1, float par2, float par3, float par4, float par5, float par6) {
   }

   @Environment(EnvType.CLIENT)
   public float getBlockBrightness(IBlockAccess par1IBlockAccess, int par2, int par3, int par4) {
      return par1IBlockAccess.getBrightness(
         par2, par3, par4, getLightValueForBlock(par1IBlockAccess, par2, par3, par4, blocksList[par1IBlockAccess.getBlockId(par2, par3, par4)])
      );
   }

   @Environment(EnvType.CLIENT)
   public int getMixedBrightnessForBlock(IBlockAccess par1IBlockAccess, int par2, int par3, int par4) {
      return par1IBlockAccess.getLightBrightnessForSkyBlocks(
         par2, par3, par4, getLightValueForBlock(par1IBlockAccess, par2, par3, par4, blocksList[par1IBlockAccess.getBlockId(par2, par3, par4)])
      );
   }

   public boolean isBlockSolid(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5) {
      return par1IBlockAccess.getBlockMaterial(par2, par3, par4).isSolid();
   }

   @Environment(EnvType.CLIENT)
   public Icon getBlockTexture(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5) {
      return this.getIcon(par5, par1IBlockAccess.getBlockMetadata(par2, par3, par4));
   }

   @Environment(EnvType.CLIENT)
   public Icon getIcon(int par1, int par2) {
      return this.blockIcon;
   }

   @Environment(EnvType.CLIENT)
   public final Icon getBlockTextureFromSide(int par1) {
      return this.getIcon(par1, 0);
   }

   public void addCollisionBoxesToList(World par1World, int par2, int par3, int par4, AxisAlignedBB par5AxisAlignedBB, List par6List, Entity par7Entity) {
      AxisAlignedBB var8 = this.getCollisionBoundingBoxFromPool(par1World, par2, par3, par4);
      if (var8 != null && par5AxisAlignedBB.intersectsWith(var8)) {
         par6List.add(var8);
      }
   }

   public boolean isOpaqueCube() {
      return true;
   }

   public boolean canCollideCheck(int par1, boolean par2) {
      return this.isCollidable();
   }

   public boolean isCollidable() {
      return true;
   }

   public void updateTick(World par1World, int par2, int par3, int par4, Random par5Random) {
   }

   @Environment(EnvType.CLIENT)
   public void randomDisplayTick(World par1World, int par2, int par3, int par4, Random par5Random) {
   }

   public void onBlockDestroyedByPlayer(World par1World, int par2, int par3, int par4, int par5) {
   }

   public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, int par5) {
   }

   public int tickRate(World par1World) {
      return 10;
   }

   public void onBlockAdded(World par1World, int par2, int par3, int par4) {
   }

   public void breakBlock(World par1World, int par2, int par3, int par4, int par5, int par6) {
   }

   public int quantityDropped(Random par1Random) {
      return 1;
   }

   public int idDropped(int par1, Random par2Random, int par3) {
      return this.blockID;
   }

   public final void dropBlockAsItem(World par1World, int par2, int par3, int par4, int par5, int par6) {
      this.dropBlockAsItemWithChance(par1World, par2, par3, par4, par5, 1.0F, par6);
   }

   public void dropBlockAsItemWithChance(World par1World, int par2, int par3, int par4, int par5, float par6, int par7) {
      if (!par1World.isRemote) {
         int var8 = this.quantityDroppedWithBonus(par7, par1World.rand);

         for (int var9 = 0; var9 < var8; var9++) {
            if (par1World.rand.nextFloat() <= par6) {
               int var10 = this.idDropped(par5, par1World.rand, par7);
               if (var10 > 0) {
                  this.dropBlockAsItem_do(par1World, par2, par3, par4, new ItemStack(var10, 1, this.damageDropped(par5)));
               }
            }
         }
      }
   }

   protected void dropBlockAsItem_do(World par1World, int par2, int par3, int par4, ItemStack par5ItemStack) {
      if (!par1World.isRemote && par1World.getGameRules().getGameRuleBooleanValue("doTileDrops")) {
         float var6 = 0.7F;
         double var7 = par1World.rand.nextFloat() * var6 + (1.0F - var6) * 0.5;
         double var9 = par1World.rand.nextFloat() * var6 + (1.0F - var6) * 0.5;
         double var11 = par1World.rand.nextFloat() * var6 + (1.0F - var6) * 0.5;
         EntityItem var13 = (EntityItem)EntityList.createEntityOfType(EntityItem.class, par1World, par2 + var7, par3 + var9, par4 + var11, par5ItemStack);
         var13.delayBeforeCanPickup = 10;
         par1World.spawnEntityInWorld(var13);
      }
   }

   protected void dropXpOnBlockBreak(World par1World, int par2, int par3, int par4, int par5) {
   }

   public int damageDropped(int par1) {
      return 0;
   }

   public float getExplosionResistance(Entity par1Entity) {
      return this.blockResistance / 5.0F;
   }

   public void onBlockDestroyedByExplosion(World par1World, int par2, int par3, int par4, Explosion par5Explosion) {
   }

   public void postBlockDestroyedByExplosion(World par1World, int par2, int par3, int par4, Explosion par5Explosion) {
   }

   public void onBlockDestroyedByMiningCharge(World world, int x, int y, int z) {
      this.onBlockDestroyedByExplosion(world, x, y, z, null);
   }

   public void postBlockDestroyedByMiningCharge(World world, int x, int y, int z) {
      this.postBlockDestroyedByExplosion(world, x, y, z, null);
   }

   @Environment(EnvType.CLIENT)
   public int getRenderBlockPass() {
      return 0;
   }

   public boolean canPlaceBlockOnSide(World par1World, int par2, int par3, int par4, int par5, ItemStack par6ItemStack) {
      return this.canPlaceBlockOnSide(par1World, par2, par3, par4, par5);
   }

   public boolean canPlaceBlockOnSide(World par1World, int par2, int par3, int par4, int par5) {
      return this.canPlaceBlockAt(par1World, par2, par3, par4);
   }

   public boolean canPlaceBlockAt(World par1World, int par2, int par3, int par4) {
      int var5 = par1World.getBlockId(par2, par3, par4);
      return var5 == 0 || blocksList[var5].blockMaterial.isReplaceable();
   }

   public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9) {
      return false;
   }

   public void onEntityWalking(World par1World, int par2, int par3, int par4, Entity par5Entity) {
   }

   public void onEntityStepsIn(World world, int x, int y, int z, Entity entity) {
   }

   public int onBlockPlaced(World par1World, int par2, int par3, int par4, int par5, float par6, float par7, float par8, int par9) {
      return par9;
   }

   public void onBlockClicked(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer) {
   }

   public void velocityToAddToEntity(World par1World, int par2, int par3, int par4, Entity par5Entity, Vec3 par6Vec3) {
   }

   public void setBlockBoundsBasedOnState(IBlockAccess par1IBlockAccess, int par2, int par3, int par4) {
   }

   public final double getBlockBoundsMinX() {
      return this.minX;
   }

   public final double getBlockBoundsMaxX() {
      return this.maxX;
   }

   public final double getBlockBoundsMinY() {
      return this.minY;
   }

   public final double getBlockBoundsMaxY() {
      return this.maxY;
   }

   public final double getBlockBoundsMinZ() {
      return this.minZ;
   }

   public final double getBlockBoundsMaxZ() {
      return this.maxZ;
   }

   @Environment(EnvType.CLIENT)
   public int getBlockColor() {
      return ColorizeBlock.colorizeBlock(this) ? ColorizeBlock.blockColor : 16777215;
   }

   @Environment(EnvType.CLIENT)
   public int getRenderColor(int par1) {
      return ColorizeBlock.colorizeBlock(this, par1) ? ColorizeBlock.blockColor : 16777215;
   }

   @Environment(EnvType.CLIENT)
   public int colorMultiplier(IBlockAccess par1IBlockAccess, int par2, int par3, int par4) {
      return ColorizeBlock.colorizeBlock(this, par1IBlockAccess, par2, par3, par4) ? ColorizeBlock.blockColor : 16777215;
   }

   public int isProvidingWeakPower(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5) {
      return 0;
   }

   public boolean canProvidePower() {
      return false;
   }

   public void onEntityCollidedWithBlock(World par1World, int par2, int par3, int par4, Entity par5Entity) {
   }

   public int isProvidingStrongPower(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5) {
      return 0;
   }

   public void setBlockBoundsForItemRender() {
   }

   public void harvestBlock(World par1World, EntityPlayer par2EntityPlayer, int par3, int par4, int par5, int par6) {
      par2EntityPlayer.addStat(StatList.mineBlockStatArray[this.blockID], 1);
      par2EntityPlayer.addHarvestBlockExhaustion(this.blockID, par3, par4, par5, par6);
      if (this.canSilkHarvest(par6) && EnchantmentHelper.getSilkTouchModifier(par2EntityPlayer)) {
         ItemStack var8 = this.createStackedBlock(par6);
         if (var8 != null) {
            this.dropBlockAsItem_do(par1World, par3, par4, par5, var8);
         }
      } else {
         int var7 = EnchantmentHelper.getFortuneModifier(par2EntityPlayer);
         this.dropBlockAsItem(par1World, par3, par4, par5, par6, var7);
      }
   }

   protected boolean canSilkHarvest() {
      return this.renderAsNormalBlock() && !this.isBlockContainer;
   }

   protected ItemStack createStackedBlock(int par1) {
      int var2 = 0;
      if (this.blockID >= 0 && this.blockID < Item.itemsList.length && Item.itemsList[this.blockID].getHasSubtypes()) {
         var2 = par1;
      }

      return new ItemStack(this.blockID, 1, var2);
   }

   public int quantityDroppedWithBonus(int par1, Random par2Random) {
      return this.quantityDropped(par2Random);
   }

   public boolean canBlockStay(World par1World, int par2, int par3, int par4) {
      return true;
   }

   public void onBlockPlacedBy(World par1World, int par2, int par3, int par4, EntityLiving par5EntityLiving, ItemStack par6ItemStack) {
   }

   public void onPostBlockPlaced(World par1World, int par2, int par3, int par4, int par5) {
   }

   public Block setUnlocalizedName(String par1Str) {
      this.unlocalizedName = par1Str;
      return this;
   }

   public String getLocalizedName() {
      return StatCollector.translateToLocal(this.getUnlocalizedName() + ".name");
   }

   public String getUnlocalizedName() {
      return "tile." + this.unlocalizedName;
   }

   @Environment(EnvType.CLIENT)
   public String getUnlocalizedName2() {
      return this.unlocalizedName;
   }

   public boolean onBlockEventReceived(World par1World, int par2, int par3, int par4, int par5, int par6) {
      return false;
   }

   public boolean getEnableStats() {
      return this.enableStats;
   }

   public Block disableStats() {
      this.enableStats = false;
      return this;
   }

   public int getMobilityFlag() {
      return this.blockMaterial.getMaterialMobility();
   }

   @Environment(EnvType.CLIENT)
   public float getAmbientOcclusionLightValue(IBlockAccess par1IBlockAccess, int par2, int par3, int par4) {
      return par1IBlockAccess.isBlockNormalCube(par2, par3, par4) ? 0.2F : 1.0F;
   }

   public void onFallenUpon(World par1World, int par2, int par3, int par4, Entity par5Entity, float par6) {
   }

   @Environment(EnvType.CLIENT)
   public int idPicked(World par1World, int par2, int par3, int par4) {
      return this.blockID;
   }

   public int getDamageValue(World par1World, int par2, int par3, int par4) {
      return this.damageDropped(par1World.getBlockMetadata(par2, par3, par4));
   }

   @Environment(EnvType.CLIENT)
   public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3List) {
      par3List.add(new ItemStack(par1, 1, 0));
   }

   public CreativeTabs getCreativeTabToDisplayOn() {
      return this.displayOnCreativeTab;
   }

   public Block setCreativeTab(CreativeTabs par1CreativeTabs) {
      this.displayOnCreativeTab = par1CreativeTabs;
      return this;
   }

   public void onBlockHarvested(World par1World, int par2, int par3, int par4, int par5, EntityPlayer par6EntityPlayer) {
   }

   public void onSetBlockIDWithMetaData(World par1World, int par2, int par3, int par4, int par5) {
   }

   public void fillWithRain(World par1World, int par2, int par3, int par4) {
   }

   @Environment(EnvType.CLIENT)
   public boolean isFlowerPot() {
      return false;
   }

   public boolean func_82506_l() {
      return true;
   }

   public boolean canDropFromExplosion(Explosion par1Explosion) {
      return true;
   }

   public boolean isAssociatedBlockID(int par1) {
      return this.blockID == par1;
   }

   public static boolean isAssociatedBlockID(int par0, int par1) {
      return par0 == par1
         ? true
         : (par0 != 0 && par1 != 0 && blocksList[par0] != null && blocksList[par1] != null ? blocksList[par0].isAssociatedBlockID(par1) : false);
   }

   public boolean hasComparatorInputOverride() {
      return false;
   }

   public int getComparatorInputOverride(World par1World, int par2, int par3, int par4, int par5) {
      return 0;
   }

   @Environment(EnvType.CLIENT)
   public void registerIcons(IconRegister par1IconRegister) {
      this.blockIcon = par1IconRegister.registerIcon(this.unlocalizedName);
   }

   @Environment(EnvType.CLIENT)
   public String getItemIconName() {
      return null;
   }

   public boolean isNormalCube(IBlockAccess blockAccess, int i, int j, int k) {
      return this.blockMaterial.isOpaque() && this.renderAsNormalBlock();
   }

   public int preBlockPlacedBy(World world, int i, int j, int k, int iMetadata, EntityLiving entityBy) {
      return iMetadata;
   }

   public void setBlockMaterial(Material material) {
      this.blockMaterial = material;
      canBlockGrass[this.blockID] = !material.getCanBlockGrass();
   }

   public void randomUpdateTick(World world, int i, int j, int k, Random rand) {
      this.updateTick(world, i, j, k, rand);
   }

   public void clientNotificationOfMetadataChange(World world, int i, int j, int k, int iOldMetadata, int iNewMetadata) {
   }

   public void onArrowImpact(World world, int i, int j, int k, EntityArrow arrow) {
   }

   public void onArrowCollide(World world, int i, int j, int k, EntityArrow arrow) {
   }

   public float getMovementModifier(World world, int i, int j, int k) {
      float fModifier = 1.0F;
      if (this.blockMaterial != Material.ground && this.blockMaterial != Material.grass) {
         fModifier *= 1.2F;
      }

      return fModifier;
   }

   public void onPlayerWalksOnBlock(World world, int i, int j, int k, EntityPlayer player) {
   }

   public boolean doesBlockHopperEject(World world, int i, int j, int k) {
      return this.blockMaterial.isSolid();
   }

   public boolean doesBlockHopperInsert(World world, int i, int j, int k) {
      return false;
   }

   public boolean getIsBlockWarm(IBlockAccess blockAccess, int i, int j, int k) {
      return false;
   }

   public StepSound getStepSound(World world, int i, int j, int k) {
      return this.stepSound;
   }

   public void clientBreakBlock(World world, int i, int j, int k, int iBlockID, int iMetadata) {
   }

   public void clientBlockAdded(World world, int i, int j, int k) {
   }

   public boolean hasStrata() {
      return false;
   }

   public int getMetadataConversionForStrataLevel(int iLevel, int iMetadata) {
      return iMetadata;
   }

   public float getExplosionResistance(Entity entity, World world, int i, int j, int k) {
      return this.getExplosionResistance(entity);
   }

   public boolean canBlockStayDuringGenerate(World world, int i, int j, int k) {
      return this.canBlockStay(world, i, j, k);
   }

   public boolean isStairBlock() {
      return false;
   }

   public boolean shouldDeleteTileEntityOnBlockChange(int iNewBlockID) {
      return true;
   }

   public boolean isNaturalStone(IBlockAccess blockAccess, int i, int j, int k) {
      return false;
   }

   public static AxisAlignedBB getFullBlockBoundingBoxFromPool(World world, int i, int j, int k) {
      return AxisAlignedBB.getAABBPool().getAABB(i, j, k, i + 1.0F, j + 1.0F, k + 1.0F);
   }

   public boolean canSpitWebReplaceBlock(World world, int i, int j, int k) {
      return this.isGroundCover() || this.isAirBlock();
   }

   public boolean isAirBlock() {
      return false;
   }

   public boolean isReplaceableVegetation(World world, int i, int j, int k) {
      return false;
   }

   public boolean hasWaterToSidesOrTop(World world, int i, int j, int k) {
      for (int iFacing = 1; iFacing <= 5; iFacing++) {
         BlockPos tempPos = new BlockPos(i, j, k, iFacing);
         int iTempBlockID = world.getBlockId(tempPos.x, tempPos.y, tempPos.z);
         Block tempBlock = blocksList[iTempBlockID];
         if (tempBlock != null && tempBlock.blockMaterial == Material.water) {
            return true;
         }
      }

      return false;
   }

   public boolean getPreventsFluidFlow(World world, int i, int j, int k, Block fluidBlock) {
      return this.blockMaterial == Material.portal ? true : this.blockMaterial.blocksMovement();
   }

   public void onFluidFlowIntoBlock(World world, int i, int j, int k, BlockFluid fluidBlock) {
      this.dropBlockAsItem(world, i, j, k, world.getBlockMetadata(i, j, k), 0);
   }

   public boolean isBlockClimbable(World world, int i, int j, int k) {
      return false;
   }

   public boolean triggersBuddy() {
      return true;
   }

   @Deprecated
   public static Block replaceBlock(int id, Class newClass, BTWAddon addonPerformingReplacement, Object... parameters) {
      return replaceBlock(id, newClass, new String[0], addonPerformingReplacement, parameters);
   }

   @Deprecated
   public static Block replaceBlock(int id, Class newClass, String[] validAddonNamesForOverwrite, BTWAddon addonPerformingReplacement, Object... parameters) {
      if (blockReplaced[id]) {
         String replacedBy = blockReplacedBy[id];
         boolean isValidOverwrite = false;

         for (String addonName : validAddonNamesForOverwrite) {
            if (replacedBy.equals(addonName)) {
               isValidOverwrite = true;
            }
         }

         if (!isValidOverwrite) {
            throw new RuntimeException("Multiple addons attempting to replace block " + blocksList[id]);
         }
      }

      Block newBlock = null;
      Class[] parameterTypes = new Class[parameters.length + 1];
      Object[] parameterValues = new Object[parameters.length + 1];
      parameterTypes[0] = int.class;
      parameterValues[0] = id;
      Block original = blocksList[id];
      blocksList[id] = null;

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
            newBlock = (Block)constructorToUse.newInstance(parameterValues);
         } catch (InstantiationException var15) {
            throw new RuntimeException("A problem has occured attempting to instantiate replacement for " + blocksList[id]);
         } catch (IllegalArgumentException var16) {
            throw new RuntimeException("Incompatible types passed to specified constructor for " + blocksList[id]);
         } catch (Exception var17) {
            var17.printStackTrace();
         }

         blockReplaced[id] = true;
         if (addonPerformingReplacement != null) {
            blockReplacedBy[id] = addonPerformingReplacement.getName();
         } else {
            AddonHandler.logWarning(
               "Deprecated block replacement call for block "
                  + blocksList[id]
                  + ". Please attach addon when handling replacement for mutual replacement handling."
            );
            blockReplacedBy[id] = "";
         }

         newBlock.setHardness(original.blockHardness)
            .setResistance(original.blockResistance)
            .setStepSound(original.stepSound)
            .setUnlocalizedName(original.unlocalizedName)
            .setCreativeTab(original.getCreativeTabToDisplayOn());
         if (!original.enableStats) {
            newBlock.disableStats();
         }

         blocksList[id] = newBlock;
         return newBlock;
      } else {
         String message = "No appropriate constructor found for " + blocksList[id] + ": ";

         for (Class<?> paramType : parameterTypes) {
            message = message + paramType.getSimpleName() + ", ";
         }

         throw new RuntimeException(message);
      }
   }

   protected boolean canSilkHarvest(int iMetadata) {
      return this.canSilkHarvest();
   }

   public void onBlockDestroyedWithImproperTool(World world, EntityPlayer player, int i, int j, int k, int iMetadata) {
      world.playAuxSFX(2272, i, j, k, this.blockID + (iMetadata << 12));
      this.dropComponentItemsOnBadBreak(world, i, j, k, iMetadata, 1.0F);
   }

   protected void dropItemsIndividually(World world, int i, int j, int k, int iIDDropped, int iPileCount, int iDamageDropped, float fChanceOfPileDrop) {
      for (int iTempCount = 0; iTempCount < iPileCount; iTempCount++) {
         if (world.rand.nextFloat() <= fChanceOfPileDrop) {
            ItemStack stack = new ItemStack(iIDDropped, 1, iDamageDropped);
            this.dropBlockAsItem_do(world, i, j, k, stack);
         }
      }
   }

   public boolean dropComponentItemsOnBadBreak(World world, int i, int j, int k, int iMetadata, float fChanceOfDrop) {
      return false;
   }

   public void dropItemsOnDestroyedByExplosion(World world, int i, int j, int k, Explosion explosion) {
      if (!world.isRemote && this.canDropFromExplosion(explosion)) {
         float fChance = 1.0F;
         if (explosion != null) {
            fChance /= explosion.explosionSize;
         }

         int iMetadata = world.getBlockMetadata(i, j, k);
         if (!this.dropComponentItemsOnBadBreak(world, i, j, k, iMetadata, fChance)) {
            this.dropBlockAsItemWithChance(world, i, j, k, iMetadata, fChance, 0);
         }
      }
   }

   public void dropItemsOnDestroyedByMiningCharge(World world, int x, int y, int z, int metadata) {
      if (!world.isRemote) {
         this.dropBlockAsItem(world, x, y, z, metadata, 0);
      }
   }

   protected void onDirtDugWithImproperTool(World world, int i, int j, int k) {
      for (int iTempFacing = 0; iTempFacing < 6; iTempFacing++) {
         this.notifyNeighborDirtDugWithImproperTool(world, i, j, k, iTempFacing);
      }
   }

   protected void onDirtSlabDugWithImproperTool(World world, int i, int j, int k, boolean bUpsideDown) {
      for (int iTempFacing = 0; iTempFacing < 6; iTempFacing++) {
         if ((!bUpsideDown || iTempFacing != 0) && (bUpsideDown || iTempFacing != 1)) {
            this.notifyNeighborDirtDugWithImproperTool(world, i, j, k, iTempFacing);
         }
      }
   }

   protected void notifyNeighborDirtDugWithImproperTool(World world, int i, int j, int k, int iToFacing) {
      BlockPos neighborPos = new BlockPos(i, j, k, iToFacing);
      int iTargetBlockID = world.getBlockId(neighborPos.x, neighborPos.y, neighborPos.z);
      Block targetBlock = blocksList[iTargetBlockID];
      if (targetBlock != null) {
         targetBlock.onNeighborDirtDugWithImproperTool(world, neighborPos.x, neighborPos.y, neighborPos.z, getOppositeFacing(iToFacing));
      }
   }

   protected void onNeighborDirtDugWithImproperTool(World world, int i, int j, int k, int iToFacing) {
   }

   public boolean canBlocksBePlacedAgainstThisBlock(World world, int x, int y, int z) {
      return true;
   }

   public boolean hasSmallCenterHardPointToFacing(IBlockAccess blockAccess, int i, int j, int k, int iFacing, boolean bIgnoreTransparency) {
      return this.hasCenterHardPointToFacing(blockAccess, i, j, k, iFacing, bIgnoreTransparency);
   }

   public boolean hasSmallCenterHardPointToFacing(IBlockAccess blockAccess, int i, int j, int k, int iFacing) {
      return this.hasSmallCenterHardPointToFacing(blockAccess, i, j, k, iFacing, false);
   }

   public boolean hasCenterHardPointToFacing(IBlockAccess blockAccess, int i, int j, int k, int iFacing, boolean bIgnoreTransparency) {
      return this.hasLargeCenterHardPointToFacing(blockAccess, i, j, k, iFacing, bIgnoreTransparency);
   }

   public boolean hasCenterHardPointToFacing(IBlockAccess blockAccess, int i, int j, int k, int iFacing) {
      return this.hasCenterHardPointToFacing(blockAccess, i, j, k, iFacing, false);
   }

   public boolean hasLargeCenterHardPointToFacing(IBlockAccess blockAccess, int i, int j, int k, int iFacing, boolean bIgnoreTransparency) {
      return blockAccess.isBlockNormalCube(i, j, k);
   }

   public boolean hasLargeCenterHardPointToFacing(IBlockAccess blockAccess, int i, int j, int k, int iFacing) {
      return this.hasLargeCenterHardPointToFacing(blockAccess, i, j, k, iFacing, false);
   }

   public boolean isBlockRestingOnThatBelow(IBlockAccess blockAccess, int i, int j, int k) {
      return false;
   }

   public boolean isBlockAttachedToFacing(IBlockAccess blockAccess, int i, int j, int k, int iFacing) {
      return false;
   }

   public void attachToFacing(World world, int i, int j, int k, int iFacing) {
   }

   public boolean hasContactPointToFullFace(IBlockAccess blockAccess, int i, int j, int k, int iFacing) {
      return blockAccess.isBlockNormalCube(i, j, k);
   }

   public boolean hasContactPointToSlabSideFace(IBlockAccess blockAccess, int i, int j, int k, int iFacing, boolean bIsSlabUpsideDown) {
      return this.hasContactPointToFullFace(blockAccess, i, j, k, iFacing);
   }

   public boolean hasContactPointToStairShapedFace(IBlockAccess blockAccess, int i, int j, int k, int iFacing) {
      return this.hasContactPointToFullFace(blockAccess, i, j, k, iFacing);
   }

   public boolean hasContactPointToStairNarrowVerticalFace(IBlockAccess blockAccess, int i, int j, int k, int iFacing, int iStairFacing) {
      return this.hasContactPointToFullFace(blockAccess, i, j, k, iFacing);
   }

   public boolean onMortarApplied(World world, int i, int j, int k) {
      return false;
   }

   public boolean hasMortar(IBlockAccess blockAccess, int i, int j, int k) {
      return false;
   }

   public boolean hasNeighborWithMortarInContact(World world, int i, int j, int k) {
      for (int iTempFacing = 0; iTempFacing < 6; iTempFacing++) {
         if (WorldUtils.hasNeighborWithMortarInFullFaceContactToFacing(world, i, j, k, iTempFacing)) {
            return true;
         }
      }

      return false;
   }

   public boolean isStickyToSnow(IBlockAccess blockAccess, int i, int j, int k) {
      return false;
   }

   public boolean hasStickySnowNeighborInContact(World world, int i, int j, int k) {
      for (int iTempFacing = 0; iTempFacing < 6; iTempFacing++) {
         if (WorldUtils.hasStickySnowNeighborInFullFaceContactToFacing(world, i, j, k, iTempFacing)) {
            return true;
         }
      }

      return false;
   }

   public int getFurnaceBurnTime(int iItemDamage) {
      return this.defaultFurnaceBurnTime;
   }

   public void setFurnaceBurnTime(int iBurnTime) {
      this.defaultFurnaceBurnTime = iBurnTime;
   }

   public void setFurnaceBurnTime(FurnaceBurnTime burnTime) {
      this.setFurnaceBurnTime(burnTime.burnTime);
   }

   public boolean doesInfiniteBurnToFacing(IBlockAccess blockAccess, int i, int j, int k, int iFacing) {
      return false;
   }

   public boolean doesExtinguishFireAbove(World world, int i, int j, int k) {
      return false;
   }

   public void onDestroyedByFire(World world, int i, int j, int k, int iFireAge, boolean bForcedFireSpread) {
      if (bForcedFireSpread || world.rand.nextInt(iFireAge + 10) < 5 && !world.isRainingAtPos(i, j, k)) {
         int iNewFireMetadata = iFireAge + world.rand.nextInt(5) / 4;
         if (iNewFireMetadata > 15) {
            iNewFireMetadata = 15;
         }

         world.setBlockAndMetadataWithNotify(i, j, k, fire.blockID, iNewFireMetadata);
      } else {
         world.setBlockWithNotify(i, j, k, 0);
      }
   }

   public Block setFireProperties(int iChanceToEncourageFire, int iAbilityToCatchFire) {
      BlockFire.chanceToEncourageFire[this.blockID] = iChanceToEncourageFire;
      BlockFire.abilityToCatchFire[this.blockID] = iAbilityToCatchFire;
      return this;
   }

   public Block setFireProperties(Flammability flammability) {
      return this.setFireProperties(flammability.chanceToEncourageFire, flammability.abilityToCatchFire);
   }

   public boolean getCanBeSetOnFireDirectly(IBlockAccess blockAccess, int i, int j, int k) {
      return false;
   }

   public boolean getCanBeSetOnFireDirectlyByItem(IBlockAccess blockAccess, int i, int j, int k) {
      return this.getCanBeSetOnFireDirectly(blockAccess, i, j, k);
   }

   public boolean setOnFireDirectly(World world, int i, int j, int k) {
      return false;
   }

   public int getChanceOfFireSpreadingDirectlyTo(IBlockAccess blockAccess, int i, int j, int k) {
      return 0;
   }

   public boolean getCanBlockLightItemOnFire(IBlockAccess blockAccess, int i, int j, int k) {
      return false;
   }

   public boolean getDoesFireDamageToEntities(World world, int i, int j, int k, Entity entity) {
      return this.getDoesFireDamageToEntities(world, i, j, k);
   }

   public boolean getDoesFireDamageToEntities(World world, int i, int j, int k) {
      return false;
   }

   public boolean getCanBlockBeIncinerated(World world, int i, int j, int k) {
      return fire.canBlockCatchFire(world, i, j, k) || !this.blockMaterial.blocksMovement();
   }

   public boolean getCanBlockBeReplacedByFire(World world, int i, int j, int k) {
      return this.isAirBlock();
   }

   public boolean isIncineratedInCrucible() {
      return FireBlock.canBlockBeDestroyedByFire(this.blockID);
   }

   public boolean canPathThroughBlock(IBlockAccess blockAccess, int i, int j, int k, Entity entity, PathFinder pathFinder) {
      return this.getBlocksMovement(blockAccess, i, j, k);
   }

   public boolean shouldOffsetPositionIfPathingOutOf(IBlockAccess blockAccess, int i, int j, int k, Entity entity, PathFinder pathFinder) {
      return !this.canPathThroughBlock(blockAccess, i, j, k, entity, pathFinder);
   }

   public int getWeightOnPathBlocked(IBlockAccess blockAccess, int i, int j, int k) {
      return 0;
   }

   public int adjustPathWeightOnNotBlocked(int iPreviousWeight) {
      return iPreviousWeight;
   }

   public boolean isBreakableBarricade(IBlockAccess blockAccess, int i, int j, int k) {
      return false;
   }

   public boolean isBreakableBarricadeOpen(IBlockAccess blockAccess, int i, int j, int k) {
      return false;
   }

   public final boolean getCanBeCookedByKiLn(IBlockAccess blockAccess, int i, int j, int k) {
      int metadata = blockAccess.getBlockMetadata(i, j, k);
      return KilnCraftingManager.instance.getRecipeResult(this, metadata) != null;
   }

   public final int getCookTimeMultiplierInKiLn(IBlockAccess blockAccess, int i, int j, int k) {
      int metadata = blockAccess.getBlockMetadata(i, j, k);
      KilnRecipe recipe = KilnCraftingManager.instance.getRecipe(this, metadata);
      return recipe != null ? recipe.getCookTimeMultiplier() : 1;
   }

   public final ItemStack[] getOutputsWhenCookedByKiln(IBlockAccess blockAccess, int i, int j, int k) {
      int metadata = blockAccess.getBlockMetadata(i, j, k);
      return KilnCraftingManager.instance.getRecipeResult(this, metadata);
   }

   public void onCookedByKiLn(World world, int i, int j, int k) {
      ItemStack[] outputs = this.getOutputsWhenCookedByKiln(world, i, j, k);
      if (outputs != null) {
         world.setBlockToAir(i, j, k);

         for (ItemStack stack : outputs) {
            ItemUtils.ejectStackWithRandomOffset(world, i, j, k, stack.copy());
         }
      }
   }

   public boolean doesBlockBreakSaw(World world, int x, int y, int z) {
      int metadata = world.getBlockMetadata(x, y, z);
      SawRecipe recipe = SawCraftingManager.instance.getRecipe(this, metadata);
      return this.blockMaterial.isSolid() && this.blockMaterial.breaksSaw() && recipe == null;
   }

   public boolean onBlockSawed(World world, int i, int j, int k, int iSawPosI, int iSawPosJ, int iSawPosK) {
      return this.onBlockSawed(world, i, j, k);
   }

   public boolean onBlockSawed(World world, int i, int j, int k) {
      int metadata = world.getBlockMetadata(i, j, k);
      SawRecipe recipe = SawCraftingManager.instance.getRecipe(this, metadata);
      if (recipe != null) {
         for (ItemStack stack : recipe.getOutput()) {
            ItemUtils.ejectStackWithRandomOffset(world, i, j, k, stack.copy());
         }
      } else {
         if (!this.doesBlockDropAsItemOnSaw(world, i, j, k)) {
            return false;
         }

         this.dropBlockAsItem(world, i, j, k, world.getBlockMetadata(i, j, k), 0);
      }

      world.setBlockToAir(i, j, k);
      return true;
   }

   public boolean doesBlockDropAsItemOnSaw(World world, int i, int j, int k) {
      return this.blockMaterial.isSolid();
   }

   public int getBlockIDOnInfest(EntityLiving entity, int metadata) {
      return BTWBlocks.infestedStone.blockID;
   }

   public void onInfested(World world, EntityLiving entity, int x, int y, int z, int metadata) {
      this.infestBlock(world, entity, x, y, z, metadata);
      entity.spawnExplosionParticle();
      entity.w();
   }

   public void infestBlock(World world, EntityLiving entity, int x, int y, int z, int metadata) {
      world.setBlock(x, y, z, this.getBlockIDOnInfest(entity, metadata), 0, 3);
   }

   public boolean isBlockInfestedBy(EntityLiving entity) {
      return false;
   }

   public boolean isBlockInfestable(EntityLiving entity, int metadata) {
      return false;
   }

   public int getMechanicalPowerLevelProvidedToAxleAtFacing(World world, int i, int j, int k, int iFacing) {
      return 0;
   }

   public boolean areShovelsEffectiveOn() {
      return this.shovelsEffectiveOn;
   }

   public boolean arePicksEffectiveOn() {
      return this.picksEffectiveOn;
   }

   public boolean areAxesEffectiveOn() {
      return this.axesEffectiveOn;
   }

   public boolean areHoesEffectiveOn() {
      return this.hoesEffectiveOn;
   }

   public boolean arechiselseffectiveon() {
      return this.chiselsEffectiveOn;
   }

   public boolean arechiselseffectiveon(World world, int i, int j, int k) {
      return this.arechiselseffectiveon();
   }

   public boolean canChiselsHarvest() {
      return this.chiselsCanHarvest;
   }

   public Block setShovelsEffectiveOn() {
      return this.setShovelsEffectiveOn(true);
   }

   public Block setShovelsEffectiveOn(boolean bEffective) {
      this.shovelsEffectiveOn = bEffective;
      return this;
   }

   public Block setPicksEffectiveOn() {
      return this.setPicksEffectiveOn(true);
   }

   public Block setPicksEffectiveOn(boolean bEffective) {
      this.picksEffectiveOn = bEffective;
      return this;
   }

   public Block setAxesEffectiveOn() {
      return this.setAxesEffectiveOn(true);
   }

   public Block setAxesEffectiveOn(boolean bEffective) {
      this.axesEffectiveOn = bEffective;
      return this;
   }

   public Block setHoesEffectiveOn() {
      return this.setHoesEffectiveOn(true);
   }

   public Block setHoesEffectiveOn(boolean bEffective) {
      this.hoesEffectiveOn = bEffective;
      return this;
   }

   public Block setChiselsEffectiveOn() {
      return this.setChiselsEffectiveOn(true);
   }

   public Block setChiselsEffectiveOn(boolean bEffective) {
      this.chiselsEffectiveOn = bEffective;
      return this;
   }

   public Block setChiselsCanHarvest() {
      return this.setChiselsCanHarvest(true);
   }

   public Block setChiselsCanHarvest(boolean bCanHarvest) {
      this.chiselsCanHarvest = bCanHarvest;
      return this;
   }

   public float getPlayerRelativeBlockHardness(EntityPlayer player, World world, int i, int j, int k) {
      float fBlockHardness = this.getBlockHardness(world, i, j, k);
      if (fBlockHardness >= 0.0F) {
         float fRelativeHardness = player.getCurrentPlayerStrVsBlock(this, i, j, k) / fBlockHardness;
         return player.isCurrentToolEffectiveOnBlock(this, i, j, k)
            ? fRelativeHardness / 30.0F
            : fRelativeHardness / (200.0F * world.getDifficulty().getNoToolBlockHardnessMultiplier());
      } else {
         return 0.0F;
      }
   }

   public boolean canConvertBlock(ItemStack stack, World world, int i, int j, int k) {
      return false;
   }

   public boolean convertBlock(ItemStack stack, World world, int i, int j, int k, int iFromSide) {
      return false;
   }

   public int getEfficientToolLevel(IBlockAccess blockAccess, int i, int j, int k) {
      return 0;
   }

   public int getHarvestToolLevel(IBlockAccess blockAccess, int i, int j, int k) {
      return this.getEfficientToolLevel(blockAccess, i, j, k);
   }

   public boolean getIsProblemToRemove(ItemStack toolStack, IBlockAccess blockAccess, int i, int j, int k) {
      return false;
   }

   public boolean getDoesStumpRemoverWorkOnBlock(IBlockAccess blockAccess, int i, int j, int k) {
      return false;
   }

   public boolean canToolsStickInBlock(IBlockAccess blockAccess, int i, int j, int k) {
      return true;
   }

   public boolean canToolStickInBlockSpecialCase(World world, int x, int y, int z, Item toolOrSword) {
      return false;
   }

   public Block setBuoyancy(float fBuoyancy) {
      this.buoyancy = fBuoyancy;
      return this;
   }

   public Block setBuoyant() {
      return this.setBuoyancy(1.0F);
   }

   public Block setNonBuoyant() {
      return this.setBuoyancy(-1.0F);
   }

   public Block setNeutralBuoyant() {
      return this.setBuoyancy(0.0F);
   }

   public float getBuoyancy(int iMetadata) {
      return this.buoyancy;
   }

   public boolean canGroundCoverRestOnBlock(World world, int i, int j, int k) {
      return world.doesBlockHaveSolidTopSurface(i, j, k);
   }

   public float groundCoverRestingOnVisualOffset(IBlockAccess blockAccess, int i, int j, int k) {
      return 0.0F;
   }

   public boolean isGroundCover() {
      return false;
   }

   public boolean getCanGrassSpreadToBlock(World world, int i, int j, int k) {
      return false;
   }

   public boolean spreadGrassToBlock(World world, int i, int j, int k) {
      return false;
   }

   public boolean getCanGrassGrowUnderBlock(World world, int i, int j, int k, boolean bGrassOnHalfSlab) {
      return !bGrassOnHalfSlab ? !this.hasLargeCenterHardPointToFacing(world, i, j, k, 0) : true;
   }

   public boolean getCanMyceliumSpreadToBlock(World world, int i, int j, int k) {
      return false;
   }

   public boolean spreadMyceliumToBlock(World world, int i, int j, int k) {
      return false;
   }

   public boolean getCanBlightSpreadToBlock(World world, int i, int j, int k, int iBlightLevel) {
      return false;
   }

   public boolean isSnowCoveringTopSurface(IBlockAccess blockAccess, int i, int j, int k) {
      int iBlockAboveID = blockAccess.getBlockId(i, j + 1, k);
      if (iBlockAboveID != 0) {
         Block blockAbove = blocksList[iBlockAboveID];
         Material aboveMaterial = blockAbove.blockMaterial;
         if (aboveMaterial == Material.snow || aboveMaterial == Material.craftedSnow && blockAbove.hasCenterHardPointToFacing(blockAccess, i, j + 1, k, 0)) {
            return true;
         }

         if (blockAbove.groundCoverRestingOnVisualOffset(blockAccess, i, j + 1, k) < -0.99F && blockAccess.getBlockId(i, j + 2, k) == snow.blockID) {
            return true;
         }
      }

      return false;
   }

   public int onPreBlockPlacedByPiston(World world, int i, int j, int k, int iMetadata, int iDirectionMoved) {
      return iMetadata;
   }

   public boolean canBlockBePulledByPiston(World world, int i, int j, int k, int iToFacing) {
      return this.getMobilityFlag() != 1 ? this.canBlockBePushedByPiston(world, i, j, k, iToFacing) : false;
   }

   public boolean canBlockBePushedByPiston(World world, int i, int j, int k, int iToFacing) {
      int iMobility = this.getMobilityFlag();
      return iMobility != 2;
   }

   public boolean canBePistonShoveled(World world, int i, int j, int k) {
      return this.areShovelsEffectiveOn();
   }

   public int getPistonShovelEjectDirection(World world, int i, int j, int k, int iToFacing) {
      return -1;
   }

   public AxisAlignedBB getAsPistonMovingBoundingBox(World world, int i, int j, int k) {
      return this.getCollisionBoundingBoxFromPool(world, i, j, k);
   }

   public int adjustMetadataForPistonMove(int iMetadata) {
      return iMetadata;
   }

   public boolean canContainPistonPackingToFacing(World world, int i, int j, int k, int iFacing) {
      return this.hasLargeCenterHardPointToFacing(world, i, j, k, iFacing, true);
   }

   public void onBrokenByPistonPush(World world, int i, int j, int k, int iMetadata) {
      this.dropBlockAsItem(world, i, j, k, iMetadata, 0);
   }

   public boolean canItemPassIfFilter(ItemStack filteredItem) {
      return true;
   }

   public int getFilterableProperties(ItemStack stack) {
      return this.filterablePropertiesBitfield;
   }

   public void setFilterableProperties(int iProperties) {
      this.filterablePropertiesBitfield = iProperties;
   }

   public boolean isFallingBlock() {
      return false;
   }

   protected boolean checkForFall(World world, int i, int j, int k) {
      if (this.canFallIntoBlockAtPos(world, i, j - 1, k) && j >= 0) {
         if (!BlockSand.fallInstantly && world.checkChunksExist(i - 32, j - 32, k - 32, i + 32, j + 32, k + 32)) {
            if (!world.isRemote) {
               FallingBlockEntity fallingEntity = (FallingBlockEntity)EntityList.createEntityOfType(
                  FallingBlockEntity.class, world, i + 0.5, j + 0.5, k + 0.5, this.blockID, world.getBlockMetadata(i, j, k)
               );
               this.a(fallingEntity);
               world.spawnEntityInWorld(fallingEntity);
            }

            return true;
         } else {
            world.setBlockToAir(i, j, k);

            while (this.canFallIntoBlockAtPos(world, i, j - 1, k) && j > 0) {
               j--;
            }

            if (j > 0) {
               world.setBlock(i, j, k, this.blockID);
            }

            return true;
         }
      } else {
         return false;
      }
   }

   protected void a(EntityFallingSand entity) {
   }

   public void a_(World world, int i, int j, int k, int iMetadata) {
      this.notifyNearbyAnimalsFinishedFalling(world, i, j, k);
   }

   public void onFallingUpdate(FallingBlockEntity entity) {
   }

   public void notifyNearbyAnimalsFinishedFalling(World world, int i, int j, int k) {
      if (!world.isRemote) {
         EntityPlayer entityPlayer = world.getClosestPlayer(i + 0.5F, j + 0.5F, k + 0.5F, 64.0);
         if (entityPlayer != null) {
            world.notifyNearbyAnimalsOfPlayerBlockAddOrRemove(entityPlayer, this, i, j, k);
         }
      }
   }

   public boolean onFinishedFalling(EntityFallingSand entity, float fFallDistance) {
      return true;
   }

   public boolean attemptToCombineWithFallingEntity(World world, int i, int j, int k, EntityFallingSand entity) {
      return false;
   }

   public boolean canBeCrushedByFallingEntity(World world, int i, int j, int k, EntityFallingSand entity) {
      return false;
   }

   public void onCrushedByFallingEntity(World world, int i, int j, int k, EntityFallingSand entity) {
   }

   public boolean canFallIntoBlockAtPos(World world, int i, int j, int k) {
      Block targetBlock = blocksList[world.getBlockId(i, j, k)];
      return targetBlock == null || !targetBlock.canSupportFallingBlocks(world, i, j, k);
   }

   public boolean canSupportFallingBlocks(IBlockAccess blockAccess, int i, int j, int k) {
      return this.hasCenterHardPointToFacing(blockAccess, i, j, k, 1, true);
   }

   protected void checkForUnstableGround(World world, int i, int j, int k) {
      for (int iJOffset = 1; iJOffset <= 16; iJOffset++) {
         int iTempJ = j - iJOffset;
         if (iTempJ <= 0) {
            break;
         }

         if (world.isAirBlock(i, iTempJ, k)) {
            world.notifyBlockOfNeighborChange(i, iTempJ + 1, k, 0);
            break;
         }

         int iTempBlockID = world.getBlockId(i, iTempJ, k);
         if (iTempBlockID == fire.blockID) {
            world.notifyBlockOfNeighborChange(i, iTempJ + 1, k, 0);
            break;
         }

         Block tempBlock = blocksList[iTempBlockID];
         if (tempBlock.blockMaterial == Material.water || tempBlock.blockMaterial == Material.lava) {
            world.notifyBlockOfNeighborChange(i, iTempJ + 1, k, 0);
            break;
         }

         if (!tempBlock.isFallingBlock()) {
            break;
         }
      }
   }

   public void scheduleCheckForFall(World world, int i, int j, int k) {
      world.scheduleBlockUpdate(i, j, k, this.blockID, this.tickRate(world));
   }

   public void onBlockDestroyedLandingFromFall(World world, int i, int j, int k, int iMetadata) {
      this.onBlockDestroyedWithImproperTool(world, null, i, j, k, iMetadata);
   }

   public boolean hasFallingBlockRestingOn(IBlockAccess blockAccess, int i, int j, int k) {
      Block blockAbove = blocksList[blockAccess.getBlockId(i, j + 1, k)];
      return blockAbove != null && blockAbove.isFallingBlock() && blockAbove.hasCenterHardPointToFacing(blockAccess, i, j + 1, k, 0);
   }

   public int getFacing(IBlockAccess blockAccess, int i, int j, int k) {
      return this.getFacing(blockAccess.getBlockMetadata(i, j, k));
   }

   public int getFacing(int iMetadata) {
      return 0;
   }

   public void setFacing(World world, int i, int j, int k, int iFacing) {
      int iMetadata = world.getBlockMetadata(i, j, k);
      int iNewMetadata = this.setFacing(iMetadata, iFacing);
      if (iNewMetadata != iMetadata) {
         world.setBlockMetadataWithNotify(i, j, k, iNewMetadata);
      }
   }

   public int setFacing(int iMetadata, int iFacing) {
      return iMetadata;
   }

   public boolean toggleFacing(World world, int i, int j, int k, boolean bReverse) {
      return this.rotateAroundJAxis(world, i, j, k, bReverse);
   }

   public int convertFacingToTopTextureRotation(int iFacing) {
      if (iFacing >= 2) {
         if (iFacing > 3) {
            if (iFacing == 4) {
               return 2;
            }

            return 1;
         }

         if (iFacing == 3) {
            return 3;
         }
      }

      return 0;
   }

   public int convertFacingToBottomTextureRotation(int iFacing) {
      if (iFacing >= 2) {
         if (iFacing > 3) {
            if (iFacing == 4) {
               return 1;
            }

            return 2;
         }

         if (iFacing == 3) {
            return 3;
         }
      }

      return 0;
   }

   public static int getOppositeFacing(int iFacing) {
      return iFacing ^ 1;
   }

   public static int rotateFacingAroundY(int iFacing, boolean bReverse) {
      return bReverse ? rotatedFacingsAroundJCounterclockwise[iFacing] : rotatedFacingsAroundJClockwise[iFacing];
   }

   public static int cycleFacing(int iFacing, boolean bReverse) {
      return bReverse ? cycledFacingsReversed[iFacing] : cycledFacings[iFacing];
   }

   public boolean canRotateOnTurntable(IBlockAccess blockAccess, int i, int j, int k) {
      return blockAccess.isBlockNormalCube(i, j, k);
   }

   public boolean canTransmitRotationHorizontallyOnTurntable(IBlockAccess blockAccess, int i, int j, int k) {
      return blockAccess.isBlockNormalCube(i, j, k);
   }

   public boolean canTransmitRotationVerticallyOnTurntable(IBlockAccess blockAccess, int i, int j, int k) {
      return blockAccess.isBlockNormalCube(i, j, k);
   }

   public int rotateOnTurntable(World world, int x, int y, int z, boolean reverse, int craftingCounter) {
      this.onRotatedOnTurntable(world, x, y, z);
      if (!this.rotateAroundJAxis(world, x, y, z, reverse)) {
         world.notifyBlocksOfNeighborChange(x, y, z, this.blockID);
      }

      int metadata = world.getBlockMetadata(x, y, z);
      TurntableRecipe recipe = TurntableCraftingManager.instance.getRecipe(this, metadata);
      if (recipe != null) {
         craftingCounter = this.turntableCraftingRotation(world, x, y, z, reverse, craftingCounter);
      }

      return craftingCounter;
   }

   protected void onRotatedOnTurntable(World world, int x, int y, int z) {
   }

   protected int turntableCraftingRotation(World world, int x, int y, int z, boolean reverse, int craftingCounter) {
      craftingCounter++;
      int metadata = world.getBlockMetadata(x, y, z);
      TurntableRecipe recipe = TurntableCraftingManager.instance.getRecipe(this, metadata);
      if (recipe != null) {
         if (craftingCounter >= recipe.getRotationsToCraft()) {
            this.onCraftedOnTurntable(world, x, y, z);
            Block output = recipe.getOutputBlock();
            int outputBlockID;
            if (output != null) {
               outputBlockID = output.blockID;
            } else {
               outputBlockID = 0;
            }

            world.setBlockAndMetadataWithNotify(x, y, z, outputBlockID, recipe.getOutputMetadata());
            recipe.playCompletionEffect(world, x, y, z);
            if (recipe.getItemsEjected() != null) {
               for (ItemStack stack : recipe.getItemsEjected()) {
                  for (int i = 0; i < stack.stackSize; i++) {
                     ItemUtils.ejectSingleItemWithRandomOffset(world, x, y, z, stack.itemID, stack.getItemDamage());
                  }
               }
            }

            craftingCounter = 0;
         } else {
            recipe.playEffect(world, x, y, z);
         }
      }

      return craftingCounter;
   }

   public void onCraftedOnTurntable(World world, int x, int y, int z) {
      world.playAuxSFX(2252, x, y, z, world.getBlockId(x, y, z) + (world.getBlockMetadata(x, y, z) << 12));
   }

   public boolean rotateAroundJAxis(World world, int i, int j, int k, boolean bReverse) {
      int iMetadata = world.getBlockMetadata(i, j, k);
      int iNewMetadata = this.rotateMetadataAroundJAxis(iMetadata, bReverse);
      if (iNewMetadata != iMetadata) {
         world.setBlockMetadataWithNotify(i, j, k, iNewMetadata);
         return true;
      } else {
         return false;
      }
   }

   public int rotateMetadataAroundJAxis(int iMetadata, boolean bReverse) {
      int iFacing = this.getFacing(iMetadata);
      int iNewFacing = rotateFacingAroundY(iFacing, bReverse);
      return this.setFacing(iMetadata, iNewFacing);
   }

   public boolean canRotateAroundBlockOnTurntableToFacing(World world, int i, int j, int k, int iFacing) {
      return false;
   }

   public boolean onRotatedAroundBlockOnTurntableToFacing(World world, int i, int j, int k, int iFacing) {
      return true;
   }

   public int getNewMetadataRotatedAroundBlockOnTurntableToFacing(World world, int i, int j, int k, int iInitialFacing, int iRotatedFacing) {
      return 0;
   }

   public ItemStack getStackRetrievedByBlockDispenser(World world, int i, int j, int k) {
      int iMetadata = world.getBlockMetadata(i, j, k);
      if (this.canSilkHarvest(iMetadata)) {
         return this.createStackedBlock(iMetadata);
      } else {
         int iIdDropped = this.idDropped(iMetadata, world.rand, 0);
         return iIdDropped > 0 ? new ItemStack(iIdDropped, 1, this.damageDropped(iMetadata)) : null;
      }
   }

   public boolean isBlockDestroyedByBlockDispenser(int iMetadata) {
      return false;
   }

   public void onRemovedByBlockDispenser(World world, int i, int j, int k) {
      world.playAuxSFX(2252, i, j, k, this.blockID + (world.getBlockMetadata(i, j, k) << 12));
      world.setBlockWithNotify(i, j, k, 0);
   }

   public void onStruckByLightning(World world, int i, int j, int k) {
   }

   public boolean canMobsSpawnOn(World world, int i, int j, int k) {
      return this.blockMaterial.getMobsCanSpawnOn(world.provider.dimensionId) && this.getCollisionBoundingBoxFromPool(world, i, j, k) != null;
   }

   public float mobSpawnOnVerticalOffset(World world, int i, int j, int k) {
      return 0.0F;
   }

   protected void initBlockBounds(double dMinX, double dMinY, double dMinZ, double dMaxX, double dMaxY, double dMaxZ) {
      if (!this.fixedBlockBoundsSet) {
         this.fixedBlockBounds.setBounds(dMinX, dMinY, dMinZ, dMaxX, dMaxY, dMaxZ);
      }
   }

   protected void initBlockBounds(AxisAlignedBB bounds) {
      if (!this.fixedBlockBoundsSet) {
         this.fixedBlockBounds.setBB(bounds);
      }
   }

   protected AxisAlignedBB getFixedBlockBoundsFromPool() {
      this.fixedBlockBoundsSet = true;
      return this.fixedBlockBounds.makeTemporaryCopy();
   }

   public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k) {
      return this.getBlockBoundsFromPoolBasedOnState(world, i, j, k).offset(i, j, k);
   }

   public AxisAlignedBB getBlockBoundsFromPoolBasedOnState(IBlockAccess blockAccess, int i, int j, int k) {
      return this.getFixedBlockBoundsFromPool();
   }

   public MovingObjectPosition collisionRayTrace(World world, int i, int j, int k, Vec3 startRay, Vec3 endRay) {
      return this.collisionRayTraceVsBlockBounds(world, i, j, k, startRay, endRay);
   }

   public MovingObjectPosition mouseOverRayTrace(World world, int i, int j, int k, Vec3 startRay, Vec3 endRay) {
      return this.collisionRayTrace(world, i, j, k, startRay, endRay);
   }

   public MovingObjectPosition collisionRayTraceVsBlockBounds(World world, int i, int j, int k, Vec3 startRay, Vec3 endRay) {
      AxisAlignedBB collisionBox = this.getBlockBoundsFromPoolBasedOnState(world, i, j, k).offset(i, j, k);
      MovingObjectPosition collisionPoint = collisionBox.calculateIntercept(startRay, endRay);
      if (collisionPoint != null) {
         collisionPoint.blockX = i;
         collisionPoint.blockY = j;
         collisionPoint.blockZ = k;
      }

      return collisionPoint;
   }

   public boolean canBeGrazedOn(IBlockAccess blockAccess, int i, int j, int k, EntityAnimal byAnimal) {
      return false;
   }

   public void onGrazed(World world, int i, int j, int k, EntityAnimal animal) {
      world.setBlockToAir(i, j, k);
      Block blockBelow = blocksList[world.getBlockId(i, j - 1, k)];
      if (blockBelow != null) {
         blockBelow.onVegetationAboveGrazed(world, i, j - 1, k, animal);
      }
   }

   public void onVegetationAboveGrazed(World world, int i, int j, int k, EntityAnimal animal) {
   }

   public void notifyNeighborsBlockDisrupted(World world, int i, int j, int k) {
      BlockPos pos = new BlockPos(i, j, k);
      BlockPos tempPos = new BlockPos();

      for (int iTempFacing = 0; iTempFacing <= 5; iTempFacing++) {
         tempPos.set(pos);
         tempPos.addFacingAsOffset(iTempFacing);
         Block tempBlock = blocksList[world.getBlockId(tempPos.x, tempPos.y, tempPos.z)];
         if (tempBlock != null) {
            tempBlock.onNeighborDisrupted(world, tempPos.x, tempPos.y, tempPos.z, getOppositeFacing(iTempFacing));
         }
      }
   }

   public void onNeighborDisrupted(World world, int i, int j, int k, int iToFacing) {
   }

   public int getHerbivoreItemFoodValue(int iItemDamage) {
      return this.herbivoreItemFoodValue;
   }

   public void setHerbivoreItemFoodValue(int iFoodValue) {
      this.herbivoreItemFoodValue = iFoodValue;
   }

   public int getChickenItemFoodValue(int iItemDamage) {
      return this.birdItemFoodValue;
   }

   public void setChickenItemFoodValue(int iFoodValue) {
      this.birdItemFoodValue = iFoodValue;
   }

   public int getPigItemFoodValue(int iItemDamage) {
      return this.pigItemFoodValue;
   }

   public void setPigItemFoodValue(int iFoodValue) {
      this.pigItemFoodValue = iFoodValue;
   }

   public Block setAlwaysStartlesAnimals() {
      this.alwaysStartlesAnimals = true;
      return this;
   }

   public boolean startlesAnimalsWhenPlaced(World world, int x, int y, int z) {
      return this.alwaysStartlesAnimals || this.blockMaterial.blocksMovement();
   }

   public boolean canDomesticatedCropsGrowOnBlock(World world, int i, int j, int k) {
      return false;
   }

   public boolean canReedsGrowOnBlock(World world, int i, int j, int k) {
      return false;
   }

   public boolean canSaplingsGrowOnBlock(World world, int i, int j, int k) {
      return false;
   }

   public boolean canWildVegetationGrowOnBlock(World world, int i, int j, int k) {
      return false;
   }

   public boolean canNetherWartGrowOnBlock(World world, int i, int j, int k) {
      return false;
   }

   public boolean canCactusGrowOnBlock(World world, int i, int j, int k) {
      return false;
   }

   public boolean isBlockHydratedForPlantGrowthOn(World world, int i, int j, int k) {
      return false;
   }

   public boolean isConsideredNeighbouringWaterForReedGrowthOn(World world, int i, int j, int k) {
      for (int iTempI = i - 1; iTempI <= i + 1; iTempI++) {
         for (int iTempK = k - 1; iTempK <= k + 1; iTempK++) {
            if (world.getBlockMaterial(iTempI, j, iTempK) == Material.water) {
               return true;
            }
         }
      }

      return false;
   }

   public float getPlantGrowthOnMultiplier(World world, int i, int j, int k, Block plantBlock) {
      return 1.0F;
   }

   public boolean getIsFertilizedForPlantGrowth(World world, int i, int j, int k) {
      return false;
   }

   public void notifyOfFullStagePlantGrowthOn(World world, int i, int j, int k, Block plantBlock) {
   }

   public void notifyOfPlantAboveRemoved(World world, int i, int j, int k, Block plantBlock) {
   }

   public boolean canWeedsGrowInBlock(IBlockAccess blockAccess, int i, int j, int k) {
      return false;
   }

   public int getWeedsGrowthLevel(IBlockAccess blockAccess, int i, int j, int k) {
      return 0;
   }

   public void removeWeeds(World world, int i, int j, int k) {
   }

   public boolean attemptToApplyFertilizerTo(World world, int i, int j, int k) {
      return false;
   }

   public boolean getConvertsLegacySoil(IBlockAccess blockAccess, int i, int j, int k) {
      return false;
   }

   public MapColor getMapColor(int meta) {
      if (this.mapColorsForMetadata == null) {
         return this.blockMaterial.materialMapColor;
      } else {
         try {
            return this.mapColorsForMetadata[meta];
         } catch (Exception var3) {
            AddonHandler.logMessage("Map color not found for metadata " + meta + " of block " + this);
            return this.blockMaterial.materialMapColor;
         }
      }
   }

   public Block setMapColorsForMetadata(MapColor[] mapColors) {
      this.mapColorsForMetadata = mapColors;
      return this;
   }

   public static int getLightValueForBlock(IBlockAccess blockAccess, int x, int y, int z, Block block) {
      return block != null ? block.getLightValue(blockAccess, x, y, z) : 0;
   }

   public int getLightValue(IBlockAccess blockAccess, int x, int y, int z) {
      return lightValue[this.blockID];
   }

   public boolean canBeConvertedByMobSpawner(World world, int x, int y, int z) {
      return false;
   }

   public void convertBlockFromMobSpawner(World world, int x, int y, int z) {
   }

   public boolean shouldWallConnectToThisBlockToFacing(IBlockAccess blockAccess, int x, int y, int z, int facing) {
      return this.isNormalCube(blockAccess, x, y, z)
         || this.isWall(blockAccess.getBlockMetadata(x, y, z))
         || this.hasLargeCenterHardPointToFacing(blockAccess, x, y, z, facing, true);
   }

   public boolean shouldFenceConnectToThisBlockToFacing(IBlockAccess blockAccess, int x, int y, int z, int facing) {
      return this.isNormalCube(blockAccess, x, y, z)
         || this.isFence(blockAccess.getBlockMetadata(x, y, z))
         || this.hasLargeCenterHardPointToFacing(blockAccess, x, y, z, facing, true);
   }

   public boolean shouldPaneConnectToThisBlockToFacing(IBlockAccess blockAccess, int x, int y, int z, int facing) {
      return this.isNormalCube(blockAccess, x, y, z) || this.hasLargeCenterHardPointToFacing(blockAccess, x, y, z, facing, true);
   }

   public boolean isWall(int metadata) {
      return false;
   }

   public boolean isFence(int metadata) {
      return false;
   }

   public boolean isBenchOrTable(int metadata) {
      return false;
   }

   public boolean shouldWallFormPostBelowThisBlock(IBlockAccess blockAccess, int x, int y, int z) {
      return this.hasLargeCenterHardPointToFacing(blockAccess, x, y, z, 0)
         || this.isBlockRestingOnThatBelow(blockAccess, x, y, z)
         || this.isFence(blockAccess.getBlockMetadata(x, y, z))
         || this.isWall(blockAccess.getBlockMetadata(x, y, z));
   }

   public boolean isLog(IBlockAccess blockAccess, int x, int y, int z) {
      return false;
   }

   public boolean canSupportLeaves(IBlockAccess blockAccess, int x, int y, int z) {
      return this.isLog(blockAccess, x, y, z);
   }

   public boolean isLeafBlock(IBlockAccess blockAccess, int x, int y, int z) {
      return false;
   }

   public static boolean installationIntegrityTest() {
      return true;
   }

   public boolean attemptToAffectBlockWithSoul(World world, int x, int y, int z) {
      return false;
   }

   public boolean shouldSideBeRendered(IBlockAccess blockAccess, int iNeighborI, int iNeighborJ, int iNeighborK, int iSide) {
      Block neighborBlock = blocksList[blockAccess.getBlockId(iNeighborI, iNeighborJ, iNeighborK)];
      return neighborBlock != null ? neighborBlock.shouldRenderNeighborFullFaceSide(blockAccess, iNeighborI, iNeighborJ, iNeighborK, iSide) : true;
   }

   @Environment(EnvType.CLIENT)
   public boolean shouldRenderNeighborHalfSlabSide(IBlockAccess blockAccess, int i, int j, int k, int iNeighborSlabSide, boolean bNeighborUpsideDown) {
      return !this.isOpaqueCube();
   }

   @Environment(EnvType.CLIENT)
   public boolean shouldRenderNeighborFullFaceSide(IBlockAccess blockAccess, int i, int j, int k, int iNeighborSide) {
      return !this.isOpaqueCube();
   }

   @Environment(EnvType.CLIENT)
   public boolean renderBlock(RenderBlocks renderer, int i, int j, int k) {
      renderer.setRenderBounds(this.getBlockBoundsFromPoolBasedOnState(renderer.blockAccess, i, j, k));
      return renderer.renderStandardBlock(this, i, j, k);
   }

   @Environment(EnvType.CLIENT)
   public void renderBlockSecondPass(RenderBlocks renderBlocks, int i, int j, int k, boolean bFirstPassResult) {
   }

   @Environment(EnvType.CLIENT)
   public boolean renderBlockWithTexture(RenderBlocks renderBlocks, int i, int j, int k, Icon texture) {
      renderBlocks.setOverrideBlockTexture(texture);
      boolean bReturnValue;
      if (!this.renderAsNormalBlock()) {
         bReturnValue = this.renderBlock(renderBlocks, i, j, k);
      } else {
         renderBlocks.setRenderBounds(this.getBlockBoundsFromPoolBasedOnState(renderBlocks.blockAccess, i, j, k));
         bReturnValue = renderBlocks.renderStandardBlock(this, i, j, k);
      }

      renderBlocks.clearOverrideBlockTexture();
      return bReturnValue;
   }

   @Environment(EnvType.CLIENT)
   public AxisAlignedBB getBlockBoundsFromPoolForItemRender(int iItemDamage) {
      return this.getFixedBlockBoundsFromPool();
   }

   @Environment(EnvType.CLIENT)
   public void renderBlockAsItem(RenderBlocks renderBlocks, int iItemDamage, float fBrightness) {
      renderBlocks.renderBlockAsItemVanilla(this, iItemDamage, fBrightness);
   }

   @Environment(EnvType.CLIENT)
   public boolean doesItemRenderAsBlock(int iItemDamage) {
      return RenderBlocks.doesRenderIDRenderItemIn3D(this.getRenderType());
   }

   @Environment(EnvType.CLIENT)
   public void renderCookingByKiLnOverlay(RenderBlocks renderBlocks, int i, int j, int k, boolean bFirstPassResult) {
      if (bFirstPassResult && hasKilnRecipe[this.blockID]) {
         IBlockAccess blockAccess = renderBlocks.blockAccess;
         if (!renderBlocks.hasOverrideBlockTexture() && this.getCanBeCookedByKiLn(blockAccess, i, j, k)) {
            int iBlockBelowID = blockAccess.getBlockId(i, j - 1, k);
            if (iBlockBelowID == BTWBlocks.kiln.blockID) {
               Icon overlayTexture = BTWBlocks.kiln.getCookTextureForCurrentState(blockAccess, i, j - 1, k);
               if (overlayTexture != null) {
                  this.renderBlockWithTexture(renderBlocks, i, j, k, overlayTexture);
               }
            }
         }
      }
   }

   @Environment(EnvType.CLIENT)
   public boolean shouldRenderWhileFalling(World world, EntityFallingSand entity) {
      int iCurrentBlockI = MathHelper.floor_double(entity.posX);
      int iCurrentBlockJ = MathHelper.floor_double(entity.posY);
      int iCurrentBlockK = MathHelper.floor_double(entity.posZ);
      int iBlockIDAtLocation = world.getBlockId(iCurrentBlockI, iCurrentBlockJ, iCurrentBlockK);
      return iBlockIDAtLocation != entity.blockID;
   }

   @Environment(EnvType.CLIENT)
   public void renderFallingBlock(RenderBlocks renderBlocks, int i, int j, int k, int iMetadata) {
      renderBlocks.setRenderBounds(this.getFixedBlockBoundsFromPool());
      renderBlocks.renderStandardFallingBlock(this, i, j, k, iMetadata);
   }

   @Environment(EnvType.CLIENT)
   public boolean shouldSideBeRenderedOnFallingBlock(int iSide, int iMetadata) {
      return true;
   }

   @Environment(EnvType.CLIENT)
   public void renderBlockMovedByPiston(RenderBlocks renderBlocks, int i, int j, int k) {
      renderBlocks.renderBlockAllFaces(this, i, j, k);
   }

   @Environment(EnvType.CLIENT)
   public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int i, int j, int k) {
      return this.getBlockBoundsFromPoolBasedOnState(world, i, j, k).offset(i, j, k);
   }

   @Environment(EnvType.CLIENT)
   public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, MovingObjectPosition rayTraceHit) {
      return this.getSelectedBoundingBoxFromPool(world, rayTraceHit.blockX, rayTraceHit.blockY, rayTraceHit.blockZ);
   }

   @Environment(EnvType.CLIENT)
   public Icon getIconByIndex(int iIndex) {
      return this.blockIcon;
   }

   @Environment(EnvType.CLIENT)
   public Icon getHopperFilterIcon() {
      return null;
   }

   @Environment(EnvType.CLIENT)
   public void renderCrossHatch(RenderBlocks renderer, int i, int j, int k, Icon icon, double dBorderWidth, double dVerticalOffset) {
      Tessellator tessellator = Tessellator.instance;
      double dX = i;
      double dY = j + dVerticalOffset;
      double dZ = k;
      double dMinU = icon.getMinU();
      double dMinV = icon.getMinV();
      double dMaxU = icon.getMaxU();
      double dMaxV = icon.getMaxV();
      double dX1 = dX + 1.0 - dBorderWidth;
      double dX2 = dX + dBorderWidth;
      double dZ2 = dZ + 1.0;
      tessellator.addVertexWithUV(dX1, dY + 1.0, dZ, dMinU, dMinV);
      tessellator.addVertexWithUV(dX1, dY + 0.0, dZ, dMinU, dMaxV);
      tessellator.addVertexWithUV(dX1, dY + 0.0, dZ2, dMaxU, dMaxV);
      tessellator.addVertexWithUV(dX1, dY + 1.0, dZ2, dMaxU, dMinV);
      tessellator.addVertexWithUV(dX1, dY + 1.0, dZ2, dMinU, dMinV);
      tessellator.addVertexWithUV(dX1, dY + 0.0, dZ2, dMinU, dMaxV);
      tessellator.addVertexWithUV(dX1, dY + 0.0, dZ, dMaxU, dMaxV);
      tessellator.addVertexWithUV(dX1, dY + 1.0, dZ, dMaxU, dMinV);
      tessellator.addVertexWithUV(dX2, dY + 1.0, dZ2, dMinU, dMinV);
      tessellator.addVertexWithUV(dX2, dY + 0.0, dZ2, dMinU, dMaxV);
      tessellator.addVertexWithUV(dX2, dY + 0.0, dZ, dMaxU, dMaxV);
      tessellator.addVertexWithUV(dX2, dY + 1.0, dZ, dMaxU, dMinV);
      tessellator.addVertexWithUV(dX2, dY + 1.0, dZ, dMinU, dMinV);
      tessellator.addVertexWithUV(dX2, dY + 0.0, dZ, dMinU, dMaxV);
      tessellator.addVertexWithUV(dX2, dY + 0.0, dZ2, dMaxU, dMaxV);
      tessellator.addVertexWithUV(dX2, dY + 1.0, dZ2, dMaxU, dMinV);
      dX2 = dX + 1.0;
      double dZ1 = dZ + dBorderWidth;
      dZ2 = dZ + 1.0 - dBorderWidth;
      tessellator.addVertexWithUV(dX, dY + 1.0, dZ1, dMinU, dMinV);
      tessellator.addVertexWithUV(dX, dY + 0.0, dZ1, dMinU, dMaxV);
      tessellator.addVertexWithUV(dX2, dY + 0.0, dZ1, dMaxU, dMaxV);
      tessellator.addVertexWithUV(dX2, dY + 1.0, dZ1, dMaxU, dMinV);
      tessellator.addVertexWithUV(dX2, dY + 1.0, dZ1, dMinU, dMinV);
      tessellator.addVertexWithUV(dX2, dY + 0.0, dZ1, dMinU, dMaxV);
      tessellator.addVertexWithUV(dX, dY + 0.0, dZ1, dMaxU, dMaxV);
      tessellator.addVertexWithUV(dX, dY + 1.0, dZ1, dMaxU, dMinV);
      tessellator.addVertexWithUV(dX2, dY + 1.0, dZ2, dMinU, dMinV);
      tessellator.addVertexWithUV(dX2, dY + 0.0, dZ2, dMinU, dMaxV);
      tessellator.addVertexWithUV(dX, dY + 0.0, dZ2, dMaxU, dMaxV);
      tessellator.addVertexWithUV(dX, dY + 1.0, dZ2, dMaxU, dMinV);
      tessellator.addVertexWithUV(dX, dY + 1.0, dZ2, dMinU, dMinV);
      tessellator.addVertexWithUV(dX, dY + 0.0, dZ2, dMinU, dMaxV);
      tessellator.addVertexWithUV(dX2, dY + 0.0, dZ2, dMaxU, dMaxV);
      tessellator.addVertexWithUV(dX2, dY + 1.0, dZ2, dMaxU, dMinV);
   }

   static {
      Item.itemsList[cloth.blockID] = new ItemCloth(cloth.blockID - 256).b("cloth");
      Item.itemsList[wood.blockID] = new ItemMultiTextureTile(wood.blockID - 256, wood, BlockLog.woodType).b("log");
      Item.itemsList[planks.blockID] = new ItemMultiTextureTile(planks.blockID - 256, planks, BlockWood.woodType).b("wood");
      Item.itemsList[silverfish.blockID] = new ItemMultiTextureTile(silverfish.blockID - 256, silverfish, BlockSilverfish.silverfishStoneTypes)
         .b("monsterStoneEgg");
      Item.itemsList[stoneBrick.blockID] = new ItemMultiTextureTile(stoneBrick.blockID - 256, stoneBrick, StoneBrickBlock.stoneBrickTypesStratified)
         .b("stonebricksmooth");
      Item.itemsList[sandStone.blockID] = new ItemMultiTextureTile(sandStone.blockID - 256, sandStone, BlockSandStone.SAND_STONE_TYPES).b("sandStone");
      Item.itemsList[blockNetherQuartz.blockID] = new ItemMultiTextureTile(blockNetherQuartz.blockID - 256, blockNetherQuartz, BlockQuartz.quartzBlockTypes)
         .b("quartzBlock");
      Item.itemsList[stoneSingleSlab.blockID] = new ItemSlab(stoneSingleSlab.blockID - 256, stoneSingleSlab, stoneDoubleSlab, false).b("stoneSlab");
      Item.itemsList[stoneDoubleSlab.blockID] = new ItemSlab(stoneDoubleSlab.blockID - 256, stoneSingleSlab, stoneDoubleSlab, true).b("stoneSlab");
      Item.itemsList[woodSingleSlab.blockID] = new ItemSlab(woodSingleSlab.blockID - 256, woodSingleSlab, woodDoubleSlab, false).b("woodSlab");
      Item.itemsList[woodDoubleSlab.blockID] = new ItemSlab(woodDoubleSlab.blockID - 256, woodSingleSlab, woodDoubleSlab, true).b("woodSlab");
      Item.itemsList[sapling.blockID] = new LegacySaplingBlockItem(sapling.blockID - 256).b("sapling");
      Item.itemsList[leaves.blockID] = new ItemLeaves(leaves.blockID - 256).b("leaves");
      Item.itemsList[vine.blockID] = new ItemColored(vine.blockID - 256, false);
      Item.itemsList[tallGrass.blockID] = new ItemColored(tallGrass.blockID - 256, true).setBlockNames(new String[]{"shrub", "grass", "fern"});
      Item.itemsList[snow.blockID] = new ItemSnow(snow.blockID - 256, snow);
      Item.itemsList[waterlily.blockID] = new ItemLilyPad(waterlily.blockID - 256);
      Item.itemsList[pistonBase.blockID] = new ItemPiston(pistonBase.blockID - 256);
      Item.itemsList[pistonStickyBase.blockID] = new ItemPiston(pistonStickyBase.blockID - 256);
      Item.itemsList[cobblestoneWall.blockID] = new ItemMultiTextureTile(cobblestoneWall.blockID - 256, cobblestoneWall, BlockWall.types).b("cobbleWall");
      Item.itemsList[anvil.blockID] = new ItemAnvilBlock(anvil).b("anvil");

      for (int var0 = 0; var0 < 256; var0++) {
         if (blocksList[var0] != null) {
            if (Item.itemsList[var0] == null) {
               Item.itemsList[var0] = new ItemBlock(var0 - 256);
               blocksList[var0].initializeBlock();
            }

            if (canBlockGrass[var0] || lightOpacity[var0] == 0) {
               useNeighborBrightness[var0] = true;
            }
         }
      }

      canBlockGrass[0] = true;
      StatList.initBreakableStats();
      rotatedFacingsAroundJClockwise = new int[]{0, 1, 4, 5, 3, 2};
      rotatedFacingsAroundJCounterclockwise = new int[]{0, 1, 5, 4, 2, 3};
      cycledFacings = new int[]{4, 0, 1, 5, 3, 2};
      cycledFacingsReversed = new int[]{1, 2, 5, 4, 0, 3};
   }
}
