package btw.entity.mob.villager;

import btw.block.BTWBlocks;
import btw.item.BTWItems;
import btw.util.ColorUtils;
import net.minecraft.src.Block;
import net.minecraft.src.Enchantment;
import net.minecraft.src.Item;

public class TradeList {
   private static final int FARMER = 0;
   private static final int LIBRARIAN = 1;
   private static final int PRIEST = 2;
   private static final int BLACKSMITH = 3;
   private static final int BUTCHER = 4;

   public static void addVillagerTrades() {
      addFarmerTrades();
      addLibrarianTrades();
      addPriestTrades();
      addBlacksmithTrades();
      addButcherTrades();
   }

   private static void addFarmerTrades() {
      VillagerEntity.addTradeToBuyMultipleItems(0, BTWBlocks.looseDirt.blockID, 48, 64, 1.0F, 1).setDefault(0);
      VillagerEntity.addTradeToBuyMultipleItems(0, Block.wood.blockID, 0, 32, 48, 0.15F, 1);
      VillagerEntity.addTradeToBuyMultipleItems(0, Block.wood.blockID, 1, 32, 48, 0.15F, 1);
      VillagerEntity.addTradeToBuyMultipleItems(0, Block.wood.blockID, 2, 32, 48, 0.15F, 1);
      VillagerEntity.addTradeToBuyMultipleItems(0, Block.wood.blockID, 3, 32, 48, 0.15F, 1);
      VillagerEntity.addTradeToBuyMultipleItems(0, BTWItems.wool.itemID, ColorUtils.BROWN.colorID, 16, 24, 1.0F, 1);
      VillagerEntity.addTradeToBuyMultipleItems(0, Item.dyePowder.itemID, ColorUtils.WHITE.colorID, 32, 48, 1.0F, 1);
      VillagerEntity.addLevelUpTradeToBuySingleItem(0, Item.hoeIron.itemID, 1, 1, 1);
      VillagerEntity.addTradeToBuyMultipleItems(0, BTWItems.flour.itemID, 24, 32, 1.0F, 2);
      VillagerEntity.addTradeToBuyMultipleItems(0, Item.sugar.itemID, 10, 20, 1.0F, 2);
      VillagerEntity.addTradeToBuyMultipleItems(0, BTWItems.cocoaBeans.itemID, 10, 16, 1.0F, 2);
      VillagerEntity.addTradeToBuyMultipleItems(0, BTWItems.brownMushroom.itemID, 10, 16, 1.0F, 2);
      VillagerEntity.addTradeToBuyMultipleItems(0, BTWItems.hempSeeds.itemID, 24, 32, 1.0F, 2);
      VillagerEntity.addTradeToBuyMultipleItems(0, Item.egg.itemID, 12, 12, 1.0F, 2);
      VillagerEntity.addTradeToBuyMultipleItems(0, Block.thinGlass.blockID, 16, 32, 1.0F, 2);
      VillagerEntity.addTradeToBuySingleItem(0, Item.bucketMilk.itemID, 1, 2, 0.25F, 2);
      VillagerEntity.addTradeToSellMultipleItems(0, BTWItems.wheat.itemID, 8, 16, 1.0F, 2);
      VillagerEntity.addTradeToSellMultipleItems(0, Item.appleRed.itemID, 2, 4, 0.5F, 2);
      VillagerEntity.addLevelUpTradeToBuySingleItem(0, BTWBlocks.millstone.blockID, 2, 2, 2);
      VillagerEntity.addTradeToSellSingleItem(0, BTWItems.sugarCaneRoots.itemID, 2, 3, 1.0F, 2).setMandatory();
      VillagerEntity.addTradeToBuyMultipleItems(0, Block.melon.blockID, 8, 10, 1.0F, 3);
      VillagerEntity.addTradeToBuyMultipleItems(0, BTWBlocks.freshPumpkin.blockID, 10, 16, 1.0F, 3);
      VillagerEntity.addTradeToBuyMultipleItems(0, BTWItems.stumpRemover.itemID, 8, 12, 1.0F, 3);
      VillagerEntity.addTradeToBuyMultipleItems(0, BTWItems.chocolate.itemID, 1, 2, 1.0F, 3);
      VillagerEntity.addTradeToBuySingleItem(0, Item.shears.itemID, 1, 1, 0.5F, 3);
      VillagerEntity.addTradeToBuySingleItem(0, Item.flintAndSteel.itemID, 1, 1, 0.5F, 3);
      VillagerEntity.addComplexTrade(0, BTWItems.stake.itemID, 0, 1, 1, Item.silk.itemID, 0, 16, 32, Item.emerald.itemID, 0, 1, 1, 0.5F, 3);
      VillagerEntity.addTradeToBuySingleItem(0, BTWItems.soap.itemID, 1, 2, 1.0F, 3).registerEffectForTrade(0, villager -> {
         villager.worldObj.playSoundAtEntity(villager, "mob.slime.attack", 1.0F, (villager.rand.nextFloat() - villager.rand.nextFloat()) * 0.2F + 1.0F);
         villager.setDirtyPeasant(0);
      }).setConditional(villager -> villager.getDirtyPeasant() > 0);
      VillagerEntity.addTradeToSellMultipleItems(0, Item.bread.itemID, 4, 6, 1.0F, 3);
      VillagerEntity.addTradeToSellMultipleItems(0, BTWItems.cookedMushroomOmelet.itemID, 8, 12, 0.5F, 3);
      VillagerEntity.addTradeToSellMultipleItems(0, BTWItems.cookedScrambledEggs.itemID, 8, 12, 0.5F, 3);
      VillagerEntity.addLevelUpTradeToBuySingleItem(0, BTWItems.waterWheel.itemID, 3, 3, 3);
      VillagerEntity.addTradeToBuySingleItem(0, BTWItems.cementBucket.itemID, 2, 4, 1.0F, 4);
      VillagerEntity.addTradeToBuyMultipleItems(0, BTWBlocks.lightBlockOff.blockID, 2, 4, 1.0F, 4);
      VillagerEntity.addTradeToSellMultipleItems(0, Item.cookie.itemID, 8, 16, 1.0F, 4);
      VillagerEntity.addTradeToSellMultipleItems(0, Item.pumpkinPie.itemID, 1, 2, 1.0F, 4);
      VillagerEntity.addTradeToSellSingleItem(0, Item.cake.itemID, 2, 4, 1.0F, 4);
      VillagerEntity.addLevelUpTradeToBuy(0, BTWBlocks.planterWithSoil.blockID, 0, 8, 12, 4, 4, 4);
      VillagerEntity.addTradeToSellSingleItem(0, Block.mycelium.blockID, 10, 20, 1.0F, 5);
      VillagerEntity.addArcaneScrollTrade(0, Enchantment.looting.effectId, 16, 32, 1.0F, 5);
   }

   private static void addLibrarianTrades() {
      VillagerEntity.addTradeToBuyMultipleItems(1, Item.paper.itemID, 24, 32, 1.0F, 1);
      VillagerEntity.addTradeToBuyMultipleItems(1, Item.dyePowder.itemID, ColorUtils.BLACK.colorID, 24, 32, 1.0F, 1);
      VillagerEntity.addTradeToBuyMultipleItems(1, Item.feather.itemID, 16, 24, 1.0F, 1);
      VillagerEntity.addLevelUpTradeToBuySingleItem(1, Item.enchantedBook.itemID, 2, 2, 1);
      VillagerEntity.addTradeToBuyMultipleItems(1, Item.book.itemID, 1, 3, 1.0F, 2);
      VillagerEntity.addTradeToBuySingleItem(1, Item.writableBook.itemID, 1, 1, 1.0F, 2);
      VillagerEntity.addTradeToBuySingleItem(1, Block.bookShelf.blockID, 1, 1, 1.0F, 2);
      VillagerEntity.addTradeToBuyMultipleItems(1, Item.netherStalkSeeds.itemID, 16, 24, 1.0F, 2);
      VillagerEntity.addTradeToBuyMultipleItems(1, Item.lightStoneDust.itemID, 24, 32, 1.0F, 2);
      VillagerEntity.addTradeToBuyMultipleItems(1, BTWItems.nitre.itemID, 32, 48, 1.0F, 2);
      VillagerEntity.addTradeToBuyMultipleItems(1, BTWItems.batWing.itemID, 8, 12, 1.0F, 2);
      VillagerEntity.addTradeToBuyMultipleItems(1, Item.spiderEye.itemID, 4, 8, 1.0F, 2);
      VillagerEntity.addTradeToBuyMultipleItems(1, Item.redstone.itemID, 32, 48, 1.0F, 2);
      VillagerEntity.addLevelUpTradeToBuySingleItem(1, Item.brewingStand.itemID, 2, 2, 2);
      VillagerEntity.addTradeToBuyMultipleItems(1, BTWItems.witchWart.itemID, 6, 10, 1.0F, 3);
      VillagerEntity.addTradeToBuyMultipleItems(1, BTWItems.mysteriousGland.itemID, 14, 16, 1.0F, 3);
      VillagerEntity.addTradeToBuyMultipleItems(1, Item.fermentedSpiderEye.itemID, 4, 8, 1.0F, 3);
      VillagerEntity.addTradeToBuyMultipleItems(1, Item.ghastTear.itemID, 4, 6, 1.0F, 3);
      VillagerEntity.addTradeToBuyMultipleItems(1, Item.magmaCream.itemID, 8, 12, 1.0F, 3);
      VillagerEntity.addTradeToBuyMultipleItems(1, Item.blazePowder.itemID, 4, 6, 1.0F, 3);
      VillagerEntity.addLevelUpTradeToBuySingleItem(1, BTWBlocks.blockDispenser.blockID, 4, 4, 3);
      VillagerEntity.addTradeToBuyMultipleItems(1, BTWBlocks.detectorBlock.blockID, 2, 3, 1.0F, 4);
      VillagerEntity.addTradeToBuyMultipleItems(1, BTWBlocks.buddyBlock.blockID, 2, 3, 1.0F, 4);
      VillagerEntity.addTradeToBuyMultipleItems(1, BTWBlocks.blockDispenser.blockID, 2, 3, 1.0F, 4);
      VillagerEntity.addTradeToBuySingleItem(1, BTWBlocks.lens.blockID, 2, 3, 1.0F, 4);
      VillagerEntity.addLevelUpTradeToBuySingleItem(1, BTWItems.enderSpectacles.itemID, 3, 3, 4);
      VillagerEntity.addTradeToBuyMultipleItems(1, BTWItems.brimstone.itemID, 16, 32, 1.0F, 5);
      VillagerEntity.addTradeToBuyMultipleItems(1, BTWBlocks.aestheticVegetation.blockID, 2, 8, 16, 1.0F, 5);
      VillagerEntity.addTradeToBuySingleItem(1, BTWItems.netherGrothSpores.itemID, 2, 3, 1.0F, 5);
      VillagerEntity.addArcaneScrollTrade(1, Enchantment.power.effectId, 32, 48, 1.0F, 5);
      VillagerEntity.addItemConversionTrade(1, Item.enderPearl.itemID, 6, 8, Item.eyeOfEnder.itemID, 1.0F, 5).setMandatory();
   }

   private static void addPriestTrades() {
      VillagerEntity.addTradeToBuyMultipleItems(2, BTWItems.hemp.itemID, 18, 22, 1.0F, 1).setDefault(2);
      VillagerEntity.addTradeToBuyMultipleItems(2, BTWItems.redMushroom.itemID, 10, 16, 1.0F, 1);
      VillagerEntity.addTradeToBuyMultipleItems(2, Block.cactus.blockID, 32, 64, 1.0F, 1);
      VillagerEntity.addTradeToBuySingleItem(2, Item.painting.itemID, 2, 3, 0.5F, 1);
      VillagerEntity.addTradeToBuySingleItem(2, Item.flintAndSteel.itemID, 1, 1, 1.0F, 1);
      VillagerEntity.addLevelUpTradeToBuySingleItem(2, Block.enchantmentTable.blockID, 2, 2, 1);
      VillagerEntity.addEnchantmentTrade(2, Item.swordIron.itemID, 2, 4, 0.25F, 2);
      VillagerEntity.addEnchantmentTrade(2, Item.axeIron.itemID, 2, 4, 0.25F, 2);
      VillagerEntity.addEnchantmentTrade(2, Item.pickaxeIron.itemID, 2, 4, 0.25F, 2);
      VillagerEntity.addEnchantmentTrade(2, Item.helmetIron.itemID, 2, 4, 0.25F, 2);
      VillagerEntity.addEnchantmentTrade(2, Item.plateIron.itemID, 2, 4, 0.25F, 2);
      VillagerEntity.addEnchantmentTrade(2, Item.legsIron.itemID, 2, 4, 0.25F, 2);
      VillagerEntity.addEnchantmentTrade(2, Item.bootsIron.itemID, 2, 4, 0.25F, 2);
      VillagerEntity.addEnchantmentTrade(2, Item.swordDiamond.itemID, 2, 4, 0.25F, 2);
      VillagerEntity.addEnchantmentTrade(2, Item.axeDiamond.itemID, 2, 4, 0.25F, 2);
      VillagerEntity.addEnchantmentTrade(2, Item.pickaxeDiamond.itemID, 2, 4, 0.25F, 2);
      VillagerEntity.addEnchantmentTrade(2, Item.helmetDiamond.itemID, 2, 4, 0.25F, 2);
      VillagerEntity.addEnchantmentTrade(2, Item.plateDiamond.itemID, 2, 4, 0.25F, 2);
      VillagerEntity.addEnchantmentTrade(2, Item.legsDiamond.itemID, 2, 4, 0.25F, 2);
      VillagerEntity.addEnchantmentTrade(2, Item.bootsDiamond.itemID, 2, 4, 0.25F, 2);
      VillagerEntity.addLevelUpTradeToBuySingleItem(2, BTWBlocks.arcaneVessel.blockID, 2, 2, 2);
      ((VillagerEntity.WeightedMerchantRecipeEntry)VillagerEntity.addTradeToBuyMultipleItems(2, BTWItems.candle.itemID, 4, 8, 1.0F, 3))
         .setRandomMetas(new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15}, 0);
      VillagerEntity.addTradeToBuyMultipleItems(2, Item.skull.itemID, 0, 2, 4, 1.0F, 3);
      VillagerEntity.addTradeToBuyMultipleItems(2, Item.skull.itemID, 2, 2, 4, 1.0F, 3);
      VillagerEntity.addTradeToBuyMultipleItems(2, Item.skull.itemID, 4, 2, 4, 1.0F, 3);
      VillagerEntity.addTradeToBuyMultipleItems(2, BTWBlocks.aestheticOpaque.blockID, 15, 2, 3, 1.0F, 3);
      VillagerEntity.addLevelUpTradeToBuySingleItem(2, Item.skull.itemID, 1, 3, 3, 3);
      VillagerEntity.addTradeToBuyMultipleItems(2, BTWItems.soulUrn.itemID, 2, 3, 2.0F, 4);
      VillagerEntity.addTradeToBuySingleItem(2, BTWItems.canvas.itemID, 2, 3, 1.0F, 5);
      VillagerEntity.addLevelUpTradeToBuySingleItem(2, BTWBlocks.infernalEnchanter.blockID, 4, 4, 4);
      VillagerEntity.addSkullconversionTrade(2, 1, 6, 8, 5, 1.0F, 4).setMandatory();
      VillagerEntity.addComplexTrade(
            2, BTWBlocks.dormandSoulforge.blockID, 0, 1, 1, Item.netherStar.itemID, 0, 1, 1, BTWBlocks.soulforge.blockID, 0, 1, 1, 1.0F, 4
         )
         .setMandatory()
         .registerEffectForTrade(2, villager -> {
            villager.worldObj.playSoundAtEntity(villager, "random.anvil_land", 0.3F, villager.rand.nextFloat() * 0.1F + 0.9F);
            villager.worldObj.playSoundAtEntity(villager, "ambient.cave.cave4", 0.5F, villager.rand.nextFloat() * 0.05F + 0.5F);
         });
      VillagerEntity.addArcaneScrollTrade(2, Enchantment.fortune.effectId, 48, 64, 1.0F, 5);
   }

   private static void addBlacksmithTrades() {
      VillagerEntity.addTradeToBuyMultipleItems(3, Item.coal.itemID, 16, 24, 1.0F, 1).setDefault(3);
      VillagerEntity.addTradeToBuyMultipleItems(3, Block.wood.blockID, 2, 32, 48, 1.0F, 1);
      VillagerEntity.addTradeToBuyMultipleItems(3, BTWItems.ironNugget.itemID, 18, 27, 1.0F, 1);
      VillagerEntity.addTradeToBuySingleItem(3, BTWBlocks.idleOven.blockID, 1, 1, 1.0F, 1);
      VillagerEntity.addTradeToSellSingleItem(3, Item.swordIron.itemID, 4, 6, 1.0F, 1);
      VillagerEntity.addTradeToSellSingleItem(3, Item.axeIron.itemID, 4, 6, 1.0F, 1);
      VillagerEntity.addTradeToSellSingleItem(3, Item.pickaxeIron.itemID, 6, 9, 1.0F, 1);
      VillagerEntity.addTradeToSellSingleItem(3, Item.shovelIron.itemID, 2, 3, 1.0F, 1);
      VillagerEntity.addTradeToSellSingleItem(3, Item.hoeIron.itemID, 2, 3, 1.0F, 1);
      VillagerEntity.addLevelUpTradeToBuySingleItem(3, BTWBlocks.hibachi.blockID, 1, 1, 1);
      VillagerEntity.addTradeToBuyMultipleItems(3, BTWItems.nethercoal.itemID, 12, 20, 1.0F, 2);
      VillagerEntity.addTradeToBuyMultipleItems(3, BTWBlocks.hibachi.blockID, 2, 3, 1.0F, 2);
      VillagerEntity.addTradeToBuyMultipleItems(3, BTWItems.creeperOysters.itemID, 14, 16, 1.0F, 2);
      VillagerEntity.addTradeToBuyMultipleItems(3, Item.goldNugget.itemID, 18, 27, 1.0F, 2);
      VillagerEntity.addTradeToBuySingleItem(3, Item.diamond.itemID, 2, 3, 1.0F, 2);
      VillagerEntity.addTradeToSellSingleItem(3, Item.bootsIron.itemID, 4, 6, 1.0F, 2);
      VillagerEntity.addTradeToSellSingleItem(3, Item.helmetIron.itemID, 10, 15, 1.0F, 2);
      VillagerEntity.addTradeToSellSingleItem(3, Item.plateIron.itemID, 16, 24, 1.0F, 2);
      VillagerEntity.addTradeToSellSingleItem(3, Item.legsIron.itemID, 14, 21, 1.0F, 2);
      VillagerEntity.addLevelUpTradeToBuySingleItem(3, BTWBlocks.bellows.blockID, 2, 2, 2);
      VillagerEntity.addTradeToSellSingleItem(3, Item.swordDiamond.itemID, 8, 12, 1.0F, 3);
      VillagerEntity.addTradeToSellSingleItem(3, Item.axeDiamond.itemID, 8, 12, 1.0F, 3);
      VillagerEntity.addTradeToSellSingleItem(3, Item.pickaxeDiamond.itemID, 12, 18, 1.0F, 3);
      VillagerEntity.addTradeToSellSingleItem(3, Item.shovelDiamond.itemID, 4, 6, 1.0F, 3);
      VillagerEntity.addTradeToSellSingleItem(3, Item.hoeDiamond.itemID, 4, 6, 1.0F, 3);
      VillagerEntity.addLevelUpTradeToBuySingleItem(3, BTWBlocks.crucible.blockID, 3, 3, 3);
      VillagerEntity.addTradeToBuyMultipleItems(3, BTWItems.soulUrn.itemID, 2, 3, 1.0F, 4);
      VillagerEntity.addTradeToBuyMultipleItems(3, BTWItems.haft.itemID, 6, 8, 1.0F, 4);
      VillagerEntity.addTradeToBuyMultipleItems(3, BTWBlocks.miningCharge.blockID, 4, 6, 1.0F, 4);
      VillagerEntity.addTradeToSellSingleItem(3, Item.bootsDiamond.itemID, 8, 12, 1.0F, 4);
      VillagerEntity.addTradeToSellSingleItem(3, Item.helmetDiamond.itemID, 20, 30, 1.0F, 4);
      VillagerEntity.addTradeToSellSingleItem(3, Item.plateDiamond.itemID, 32, 48, 1.0F, 4);
      VillagerEntity.addTradeToSellSingleItem(3, Item.legsDiamond.itemID, 28, 42, 1.0F, 4);
      VillagerEntity.addLevelUpTradeToBuy(3, BTWItems.soulforgedSteelIngot.itemID, 0, 8, 8, 4, 4, 4);
      VillagerEntity.addTradeToBuyMultipleItems(3, BTWItems.soulFlux.itemID, 16, 24, 1.0F, 5);
      VillagerEntity.addTradeToSellSingleItem(3, Item.bootsChain.itemID, 4, 6, 1.0F, 5);
      VillagerEntity.addTradeToSellSingleItem(3, Item.helmetChain.itemID, 10, 15, 1.0F, 5);
      VillagerEntity.addTradeToSellSingleItem(3, Item.plateChain.itemID, 16, 24, 1.0F, 5);
      VillagerEntity.addTradeToSellSingleItem(3, Item.legsChain.itemID, 14, 21, 1.0F, 5);
      VillagerEntity.addTradeToSellSingleItem(3, BTWItems.steelSword.itemID, 16, 24, 1.0F, 5);
      VillagerEntity.addTradeToSellSingleItem(3, BTWItems.steelAxe.itemID, 16, 24, 1.0F, 5);
      VillagerEntity.addTradeToSellSingleItem(3, BTWItems.steelPickaxe.itemID, 24, 36, 1.0F, 5);
      VillagerEntity.addTradeToSellSingleItem(3, BTWItems.steelShovel.itemID, 8, 16, 1.0F, 5);
      VillagerEntity.addTradeToSellSingleItem(3, BTWItems.steelHoe.itemID, 16, 24, 1.0F, 5);
      VillagerEntity.addArcaneScrollTrade(3, Enchantment.unbreaking.effectId, 32, 48, 1.0F, 5);
   }

   private static void addButcherTrades() {
      VillagerEntity.addTradeToBuyMultipleItems(4, Item.arrow.itemID, 24, 32, 1.0F, 1);
      VillagerEntity.addTradeToBuySingleItem(4, Item.shears.itemID, 1, 1, 0.5F, 1);
      VillagerEntity.addTradeToBuySingleItem(4, Item.fishingRod.itemID, 1, 1, 0.5F, 1);
      VillagerEntity.addTradeToSellMultipleItems(4, Item.beefRaw.itemID, 8, 10, 1.0F, 1).setDefault(4);
      VillagerEntity.addTradeToSellMultipleItems(4, Item.porkRaw.itemID, 8, 10, 1.0F, 1);
      VillagerEntity.addTradeToSellMultipleItems(4, Item.chickenRaw.itemID, 10, 12, 1.0F, 1);
      VillagerEntity.addTradeToSellMultipleItems(4, Item.fishRaw.itemID, 10, 12, 1.0F, 1);
      VillagerEntity.addTradeToSellMultipleItems(4, BTWItems.rawMutton.itemID, 10, 12, 1.0F, 1);
      VillagerEntity.addTradeToSellMultipleItems(4, Item.leather.itemID, 7, 9, 1.0F, 1);
      VillagerEntity.addLevelUpTradeToBuySingleItem(4, BTWBlocks.cauldron.blockID, 1, 1, 1);
      VillagerEntity.addTradeToBuyMultipleItems(4, BTWItems.dung.itemID, 10, 16, 1.0F, 2);
      VillagerEntity.addTradeToBuyMultipleItems(4, BTWItems.rawWolfChop.itemID, 6, 8, 1.0F, 2);
      VillagerEntity.addTradeToBuyMultipleItems(4, BTWItems.bark.itemID, 1, 48, 64, 1.0F, 2);
      VillagerEntity.addTradeToSellMultipleItems(4, BTWItems.steakAndPotatoes.itemID, 4, 8, 1.0F, 2);
      VillagerEntity.addTradeToSellMultipleItems(4, BTWItems.hamAndEggs.itemID, 4, 8, 1.0F, 2);
      VillagerEntity.addTradeToSellMultipleItems(4, BTWItems.tastySandwich.itemID, 4, 8, 1.0F, 2);
      VillagerEntity.addTradeToSellMultipleItems(4, BTWItems.chowder.itemID, 10, 12, 1.0F, 2);
      VillagerEntity.addTradeToSellMultipleItems(4, BTWItems.cookedKebab.itemID, 4, 8, 1.0F, 2);
      VillagerEntity.addLevelUpTradeToBuySingleItem(4, BTWBlocks.saw.blockID, 2, 2, 2);
      VillagerEntity.addTradeToBuyMultipleItems(4, BTWItems.carrot.itemID, 10, 16, 1.0F, 3);
      VillagerEntity.addTradeToBuyMultipleItems(4, Item.potato.itemID, 10, 16, 1.0F, 3);
      VillagerEntity.addTradeToBuySingleItem(4, BTWItems.rawLiver.itemID, 1, 2, 1.0F, 3);
      VillagerEntity.addTradeToSellMultipleItems(4, BTWItems.tannedLeather.itemID, 4, 8, 1.0F, 3);
      VillagerEntity.addTradeToSellMultipleItems(4, BTWItems.porkDinner.itemID, 4, 6, 1.0F, 3);
      VillagerEntity.addTradeToSellMultipleItems(4, BTWItems.steakDinner.itemID, 4, 6, 1.0F, 3);
      VillagerEntity.addTradeToSellMultipleItems(4, BTWItems.wolfDinner.itemID, 4, 6, 1.0F, 3);
      VillagerEntity.addTradeToSellMultipleItems(4, BTWItems.chickenSoup.itemID, 4, 6, 1.0F, 3);
      VillagerEntity.addTradeToSellSingleItem(4, Item.saddle.itemID, 2, 3, 1.0F, 3);
      VillagerEntity.addLevelUpTradeToBuySingleItem(4, BTWItems.breedingHarness.itemID, 3, 3, 3);
      VillagerEntity.addTradeToBuyMultipleItems(4, BTWItems.rawMysteryMeat.itemID, 2, 3, 1.0F, 4);
      VillagerEntity.addTradeToBuySingleItem(4, BTWItems.screw.itemID, 2, 3, 1.0F, 4);
      VillagerEntity.addTradeToBuySingleItem(4, BTWItems.compositeBow.itemID, 2, 3, 1.0F, 4);
      VillagerEntity.addTradeToSellMultipleItems(4, BTWItems.heartyStew.itemID, 3, 4, 1.0F, 4);
      VillagerEntity.addTradeToSellSingleItem(4, BTWItems.tannedLeatherBoots.itemID, 2, 3, 0.5F, 4);
      VillagerEntity.addTradeToSellSingleItem(4, BTWItems.tannedLeatherChest.itemID, 6, 8, 0.5F, 4);
      VillagerEntity.addTradeToSellSingleItem(4, BTWItems.tannedLeatherHelmet.itemID, 3, 4, 0.5F, 4);
      VillagerEntity.addTradeToSellSingleItem(4, BTWItems.tannedLeatherLeggings.itemID, 4, 6, 0.5F, 4);
      VillagerEntity.addLevelUpTradeToBuySingleItem(4, BTWBlocks.aestheticOpaque.blockID, 12, 4, 4, 4);
      VillagerEntity.addSkullconversionTrade(4, 0, 6, 8, 1, 1.0F, 4).setMandatory();
      VillagerEntity.addTradeToBuyMultipleItems(4, BTWItems.dynamite.itemID, 4, 6, 1.0F, 5);
      VillagerEntity.addTradeToBuySingleItem(4, BTWItems.battleaxe.itemID, 4, 5, 1.0F, 5);
      VillagerEntity.addTradeToBuySingleItem(4, BTWBlocks.companionCube.blockID, 1, 2, 1.0F, 5)
         .registerEffectForTrade(
            4,
            villager -> villager.worldObj
               .playSoundAtEntity(villager, "mob.wolf.hurt", 5.0F, (villager.rand.nextFloat() - villager.rand.nextFloat()) * 0.2F + 1.0F)
         );
      VillagerEntity.addTradeToBuyMultipleItems(4, BTWItems.broadheadArrow.itemID, 6, 12, 1.0F, 5);
      VillagerEntity.addComplexTrade(4, BTWBlocks.lightningRod.blockID, 0, 1, 1, BTWItems.soap.itemID, 0, 1, 1, Item.emerald.itemID, 0, 3, 5, 0.5F, 5)
         .registerEffectForTrade(4, villager -> villager.worldObj.playSoundAtEntity(villager, "random.classic_hurt", 1.0F, villager.getSoundPitch() * 2.0F));
      VillagerEntity.addArcaneScrollTrade(4, Enchantment.sharpness.effectId, 32, 48, 1.0F, 5);
   }
}
