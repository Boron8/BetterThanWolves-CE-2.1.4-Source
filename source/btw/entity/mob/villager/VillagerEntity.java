package btw.entity.mob.villager;

import btw.entity.mob.WitchEntity;
import btw.entity.mob.WolfEntity;
import btw.entity.mob.ZombieEntity;
import btw.entity.mob.behavior.VillagerBreedBehavior;
import btw.item.BTWItems;
import btw.util.RandomSelector;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.function.ToDoubleFunction;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.ChunkCoordinates;
import net.minecraft.src.EnchantmentHelper;
import net.minecraft.src.EntityAIAvoidEntity;
import net.minecraft.src.EntityAITempt;
import net.minecraft.src.EntityAIVillagerMate;
import net.minecraft.src.EntityAgeable;
import net.minecraft.src.EntityItem;
import net.minecraft.src.EntityList;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EntityVillager;
import net.minecraft.src.EntityZombie;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.MathHelper;
import net.minecraft.src.MerchantRecipe;
import net.minecraft.src.MerchantRecipeList;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.Potion;
import net.minecraft.src.PotionEffect;
import net.minecraft.src.World;

public abstract class VillagerEntity extends EntityVillager {
   protected static final int IN_LOVE_DATA_WATCHER_ID = 22;
   protected static final int TRADE_LEVEL_DATA_WATCHER_ID = 23;
   protected static final int TRADE_EXPERIENCE_DATA_WATCHER_ID = 25;
   protected static final int DIRTY_PEASANT_DATA_WATCHER_ID = 26;
   protected int aiFullTickCountdown;
   protected int updateTradesCountdown;
   public static final int PROFESSION_ID_FARMER = 0;
   public static final int PROFESSION_ID_LIBRARIAN = 1;
   public static final int PROFESSION_ID_PRIEST = 2;
   public static final int PROFESSION_ID_BLACKSMITH = 3;
   public static final int PROFESSION_ID_BUTCHER = 4;
   public static final int CASTE_ID_PEASANT = 0;
   public static final int CASTE_ID_MANUFACTURER = 1;
   public static final int CASTE_ID_ERUDITE = 2;
   public static Map<Integer, Class> professionMap = new HashMap<>();
   public static Map<Integer, ArrayList<Integer>> casteMap = new HashMap<>();
   public static Map<Integer, Set<VillagerEntity.WeightedMerchantEntry>> tradeByProfessionList = new HashMap<>();
   public static Map<Integer, Map<Integer, VillagerEntity.WeightedMerchantEntry>> levelUpTradeByProfessionList = new HashMap<>();
   public static Map<Integer, VillagerEntity.WeightedMerchantEntry> defaultTradeByProfessionList = new HashMap<>();
   public static Map<Integer, Map<VillagerEntity.WeightedMerchantEntry, VillagerEntity.TradeEffect>> tradeEffectRegistry = new HashMap<>();
   public static Map<Integer, Map<Integer, VillagerEntity.TradeEffect>> levelUpEffectRegistry = new HashMap<>();
   public static VillagerEntity.TradeEffect defaultLevelUpEffect = new VillagerEntity.TradeEffect() {
      @Override
      public void playEffect(VillagerEntity villager) {
         villager.worldObj.playSoundAtEntity(villager, "random.levelup", 0.5F + villager.rand.nextFloat() * 0.25F, 1.5F);
      }
   };
   public static int[] xpPerLevel = new int[]{0, 5, 7, 10, 15, 20};

   public VillagerEntity(World world) {
      this(world, 0);
   }

   public VillagerEntity(World world, int iProfession) {
      super(world, iProfession);
      this.tasks.removeAllTasksOfClass(EntityAIAvoidEntity.class);
      this.tasks.addTask(1, new EntityAIAvoidEntity(this, ZombieEntity.class, 8.0F, 0.3F, 0.35F));
      this.tasks.addTask(1, new EntityAIAvoidEntity(this, WolfEntity.class, 8.0F, 0.3F, 0.35F));
      this.tasks.removeAllTasksOfClass(EntityAIVillagerMate.class);
      this.tasks.addTask(1, new VillagerBreedBehavior(this));
      this.tasks.addTask(2, new EntityAITempt(this, 0.3F, Item.diamond.itemID, false));
      this.experienceValue = 50;
      this.updateTradesCountdown = 0;
      this.aiFullTickCountdown = 0;
   }

   @Override
   protected void updateAITick() {
      this.aiFullTickCountdown--;
      if (this.aiFullTickCountdown <= 0) {
         this.aiFullTickCountdown = 70 + this.rand.nextInt(50);
         this.worldObj
            .villageCollectionObj
            .addVillagerPosition(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ));
         this.villageObj = this.worldObj
            .villageCollectionObj
            .findNearestVillage(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ), 32);
         if (this.villageObj == null) {
            this.aO();
         } else {
            ChunkCoordinates var1 = this.villageObj.getCenter();
            this.b(var1.posX, var1.posY, var1.posZ, (int)(this.villageObj.getVillageRadius() * 0.6F));
         }
      }

      if (!this.p()) {
         if (this.getCurrentTradeLevel() == 0) {
            this.setTradeLevel(1);
            this.buyingList = null;
            this.updateTradesCountdown = 0;
            this.checkForNewTrades(1);
         } else if (this.updateTradesCountdown > 0) {
            if (this.buyingList == null) {
               this.buyingList = new MerchantRecipeList();
            }

            this.updateTradesCountdown--;
            if (this.updateTradesCountdown <= 0) {
               Iterator tradeListIterator = this.buyingList.iterator();

               while (tradeListIterator.hasNext()) {
                  MerchantRecipe tempRecipe = (MerchantRecipe)tradeListIterator.next();
                  if (tempRecipe.func_82784_g()) {
                     tradeListIterator.remove();
                  }
               }

               int desiredNumTrades = this.getCurrentMaxNumTrades();
               if (this.buyingList.size() < desiredNumTrades) {
                  this.checkForNewTrades(desiredNumTrades - this.buyingList.size());
                  this.worldObj.setEntityState(this, (byte)14);
                  this.d(new PotionEffect(Potion.regeneration.id, 200, 0));
               }
            }
         } else {
            this.updateTradesCountdown = 600 + this.rand.nextInt(600);
         }
      }
   }

   @Override
   public boolean interact(EntityPlayer player) {
      if (this.customInteract(player)) {
         return true;
      } else {
         return this.getInLove() > 0 ? this.entityAgeableInteract(player) : super.interact(player);
      }
   }

   @Override
   protected void entityInit() {
      super.entityInit();
      this.dataWatcher.addObject(22, new Integer(0));
      this.dataWatcher.addObject(23, new Integer(0));
      this.dataWatcher.addObject(25, new Integer(0));
      this.dataWatcher.addObject(26, new Integer(0));
   }

   @Override
   public void writeEntityToNBT(NBTTagCompound tag) {
      super.writeEntityToNBT(tag);
      tag.setInteger("FCInLove", this.getInLove());
      tag.setInteger("FCTradeLevel", this.getCurrentTradeLevel());
      tag.setInteger("FCTradeXP", this.getCurrentTradeXP());
      tag.setInteger("FCDirty", this.getDirtyPeasant());
   }

   @Override
   public void readEntityFromNBT(NBTTagCompound tag) {
      super.readEntityFromNBT(tag);
      if (tag.hasKey("FCInLove")) {
         this.setInLove(tag.getInteger("FCInLove"));
      }

      if (tag.hasKey("FCTradeLevel")) {
         this.setTradeLevel(tag.getInteger("FCTradeLevel"));
      }

      if (tag.hasKey("FCTradeXP")) {
         this.setTradeExperience(tag.getInteger("FCTradeXP"));
      }

      if (tag.hasKey("FCDirty")) {
         this.setDirtyPeasant(tag.getInteger("FCDirty"));
      }

      this.checkForInvalidTrades();
   }

   @Override
   public void setRevengeTarget(EntityLiving attackingEntity) {
      this.randomTickDivider = attackingEntity;
      if (attackingEntity != null) {
         this.isMating = 100;
         if (this.villageObj != null) {
            this.villageObj.addOrRenewAgressor(attackingEntity);
         }

         if (this.R()) {
            this.worldObj.setEntityState(this, (byte)13);
         }
      } else {
         this.isMating = 0;
      }
   }

   @Override
   public void useRecipe(MerchantRecipe recipe) {
      recipe.incrementToolUses();
      this.updateTradesCountdown = 10;
      this.playEffectsForTrade(recipe);
      if (recipe.tradeLevel < 0) {
         int tradeLevel = this.getCurrentTradeLevel();
         if (tradeLevel < 5 && this.getCurrentTradeXP() == this.getCurrentTradeMaxXP() && this.getCurrentTradeLevel() == -recipe.tradeLevel) {
            this.setTradeLevel(++tradeLevel);
            this.setTradeExperience(0);
            this.playEffectsForLevelUp(recipe);
         }
      } else if (recipe.tradeLevel >= this.getCurrentTradeLevel() && !recipe.isMandatory()) {
         int currentXP = this.getCurrentTradeXP() + 1;
         int maxXP = this.getCurrentTradeMaxXP();
         if (currentXP > maxXP) {
            currentXP = maxXP;
         }

         this.setTradeExperience(currentXP);
      }
   }

   @Override
   public MerchantRecipeList getRecipes(EntityPlayer player) {
      if (this.buyingList == null) {
         this.checkForNewTrades(1);
      }

      return this.buyingList;
   }

   @Override
   public void initCreature() {
      this.s(this.getProfessionFromClass());
   }

   @Override
   public void onLivingUpdate() {
      super.c();
      if (!this.worldObj.isRemote) {
         if (this.R()) {
            this.checkForLooseMilk();
         }
      } else {
         this.updateStatusParticles();
      }
   }

   @Override
   protected void dropFewItems(boolean bKilledByPlayer, int iLootingModifier) {
      if (!this.hasHeadCrabbedSquid()) {
         int iDropItemID = BTWItems.rawMysteryMeat.itemID;
         if (this.ae()) {
            if (this.worldObj.getDifficulty().shouldBurningMobsDropCookedMeat()) {
               iDropItemID = BTWItems.cookedMysteryMeat.itemID;
            } else {
               iDropItemID = BTWItems.burnedMeat.itemID;
            }
         }

         int iNumDropped = this.rand.nextInt(3) + 1 + this.rand.nextInt(1 + iLootingModifier);

         for (int iTempCount = 0; iTempCount < iNumDropped; iTempCount++) {
            this.b(iDropItemID, 1);
         }
      }
   }

   @Override
   public float getSoundPitch() {
      float fPitch = super.aY();
      if (this.isPossessed() || this.m() == 2 && this.getCurrentTradeLevel() == 5) {
         fPitch *= 0.6F;
      }

      return fPitch;
   }

   @Override
   public boolean getCanCreatureTypeBePossessed() {
      return true;
   }

   @Override
   public void onFullPossession() {
      this.worldObj.playAuxSFX(2260, MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ), 0);
      this.w();
      WitchEntity entityWitch = (WitchEntity)EntityList.createEntityOfType(WitchEntity.class, this.worldObj);
      entityWitch.b(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
      entityWitch.renderYawOffset = this.renderYawOffset;
      entityWitch.setPersistent(true);
      this.worldObj.spawnEntityInWorld(entityWitch);
   }

   @Override
   public boolean isValidZombieSecondaryTarget(EntityZombie zombie) {
      return true;
   }

   @Override
   public boolean isSecondaryTargetForSquid() {
      return true;
   }

   @Override
   public double getMountedYOffset() {
      return this.height;
   }

   public VillagerEntity func_90012_b(EntityAgeable otherParent) {
      VillagerEntity child = createVillager(this.worldObj);
      child.initCreature();
      return child;
   }

   public static VillagerEntity createVillager(World world) {
      return createVillagerFromProfession(world, 0);
   }

   public static VillagerEntity createVillagerFromProfession(World world, int profession) {
      Class villagerClass = professionMap.get(profession);

      try {
         villagerClass = EntityList.getRegisteredReplacement(villagerClass);
         VillagerEntity villager = (VillagerEntity)villagerClass.getConstructor(World.class).newInstance(world);
         villager.s(profession);
         return villager;
      } catch (InstantiationException var4) {
         var4.printStackTrace();
      } catch (IllegalAccessException var5) {
         var5.printStackTrace();
      } catch (IllegalArgumentException var6) {
         var6.printStackTrace();
      } catch (InvocationTargetException var7) {
         var7.printStackTrace();
      } catch (NoSuchMethodException var8) {
         var8.printStackTrace();
      } catch (SecurityException var9) {
         var9.printStackTrace();
      }

      return null;
   }

   public VillagerEntity spawnBabyVillagerWithProfession(EntityAgeable otherParent, int profession) {
      VillagerEntity child = createVillagerFromProfession(this.worldObj, profession);
      child.initCreature();
      return child;
   }

   public int getProfessionFromClass() {
      for (int id : professionMap.keySet()) {
         if (this.getClass().isAssignableFrom(professionMap.get(id))) {
            return id;
         }
      }

      return 0;
   }

   public static int getCasteFromProfession(int professionID) {
      for (int caste : casteMap.keySet()) {
         if (casteMap.get(caste).contains(professionID)) {
            return caste;
         }
      }

      return -1;
   }

   protected void checkForNewTrades(int availableTrades) {
      if (availableTrades > 0) {
         if (this.getCurrentTradeMaxXP() == this.getCurrentTradeXP() && this.checkForLevelUpTrade()) {
            if (--availableTrades <= 0) {
               return;
            }
         }

         MerchantRecipeList recipeList = new MerchantRecipeList();
         availableTrades = this.checkForProfessionMandatoryTrades(recipeList, availableTrades, this.getCurrentTradeLevel());
         if (availableTrades > 0) {
            this.checkForProfessionTrades(recipeList, availableTrades);
         }

         if (recipeList.isEmpty()) {
            recipeList.add(this.getProfessionDefaultTrade());
         } else {
            Collections.shuffle(recipeList);
         }

         if (this.buyingList == null) {
            this.buyingList = new MerchantRecipeList();
         }

         for (int i = 0; i < recipeList.size(); i++) {
            this.buyingList.addToListWithCheck((MerchantRecipe)recipeList.get(i));
         }
      }
   }

   protected void checkForProfessionTrades(MerchantRecipeList recipeList, int availableTrades) {
      Set<VillagerEntity.WeightedMerchantEntry> tradeList = new HashSet<>();

      for (VillagerEntity.WeightedMerchantEntry entry : tradeByProfessionList.get(this.getProfessionFromClass())) {
         if (entry.level <= this.getCurrentTradeLevel() && !entry.isMandatory() && entry.canBeAdded(this)) {
            tradeList.add(entry);
         }
      }

      int currentAttempts = 0;

      for (int maxAttempts = 50; availableTrades > 0 && currentAttempts < maxAttempts; currentAttempts++) {
         MerchantRecipe recipe = this.getRandomTradeFromAdjustedWeight(tradeList);
         if (!this.doesRecipeListAlreadyContainRecipe(recipe)) {
            recipeList.add(recipe);
            availableTrades--;
         }
      }
   }

   protected int checkForProfessionMandatoryTrades(MerchantRecipeList recipeList, int availableTrades, int level) {
      Set<VillagerEntity.WeightedMerchantEntry> entries = tradeByProfessionList.get(this.getProfessionFromClass());
      if (entries != null) {
         for (VillagerEntity.WeightedMerchantEntry entry : entries) {
            if (entry.level <= level && availableTrades > 0 && entry.isMandatory()) {
               MerchantRecipe recipe = entry.generateRecipe(this.rand);
               if (!this.doesRecipeListAlreadyContainRecipe(recipe)) {
                  recipeList.add(recipe);
                  availableTrades--;
               }
            }
         }
      }

      return availableTrades;
   }

   private boolean checkForLevelUpTrade() {
      if (this.getCurrentTradeLevel() >= 5) {
         return false;
      } else {
         MerchantRecipe recipe = this.getProfessionLevelUpTrade(this.getCurrentTradeLevel());
         if (recipe != null && !this.doesRecipeListAlreadyContainRecipe(recipe)) {
            this.buyingList.add(recipe);
            return true;
         } else {
            return false;
         }
      }
   }

   protected MerchantRecipe getProfessionLevelUpTrade(int level) {
      VillagerEntity.WeightedMerchantEntry entry = levelUpTradeByProfessionList.get(this.getProfessionFromClass()).get(level);
      if (entry == null) {
         throw new RuntimeException(
            "Level up entry for profession " + this.getProfessionFromClass() + " on level " + this.getCurrentTradeLevel() + " was missing!"
         );
      } else {
         return entry.generateRecipe(this.rand);
      }
   }

   protected boolean doesRecipeListAlreadyContainRecipe(MerchantRecipe recipe) {
      if (this.buyingList != null) {
         for (int i = 0; i < this.buyingList.size(); i++) {
            MerchantRecipe recipeForCompare = (MerchantRecipe)this.buyingList.get(i);
            if (recipe.hasSameIDsAs(recipeForCompare)) {
               return true;
            }
         }
      }

      return false;
   }

   protected MerchantRecipe getProfessionDefaultTrade() {
      return defaultTradeByProfessionList.get(this.getProfessionFromClass()).generateRecipe(this.rand);
   }

   protected MerchantRecipe getRandomTradeFromAdjustedWeight(Set<VillagerEntity.WeightedMerchantEntry> tradeList) {
      final int villagerTradeLevel = this.getCurrentTradeLevel();
      ToDoubleFunction<VillagerEntity.WeightedMerchantEntry> weighter = new ToDoubleFunction<VillagerEntity.WeightedMerchantEntry>() {
         public double applyAsDouble(VillagerEntity.WeightedMerchantEntry entry) {
            return entry.weight * entry.level / villagerTradeLevel;
         }
      };
      RandomSelector<VillagerEntity.WeightedMerchantEntry> selector = RandomSelector.weighted(tradeList, weighter);
      return selector.next(this.rand).generateRecipe(this.rand);
   }

   public int getCurrentMaxNumTrades() {
      int numMandatoryTrades = 0;

      for (VillagerEntity.WeightedMerchantEntry entry : tradeByProfessionList.get(this.getProfessionFromClass())) {
         if (entry.level <= this.getCurrentTradeLevel() && entry.isMandatory()) {
            numMandatoryTrades++;
         }
      }

      return this.getCurrentTradeLevel() + numMandatoryTrades;
   }

   private void checkForInvalidTrades() {
      if (this.buyingList != null) {
         Iterator iterator = this.buyingList.iterator();

         while (iterator.hasNext()) {
            MerchantRecipe trade = (MerchantRecipe)iterator.next();
            if (this.isInvalidProfessionTrade(trade)) {
               iterator.remove();
            }
         }
      }
   }

   protected boolean isInvalidProfessionTrade(MerchantRecipe trade) {
      try {
         for (VillagerEntity.WeightedMerchantEntry entry : tradeByProfessionList.get(this.getProfessionFromClass())) {
            if (entry.matchesMerchantRecipe(trade)) {
               if (trade.getItemToBuy().stackSize <= trade.getItemToBuy().getMaxStackSize()
                  && (!trade.hasSecondItemToBuy() || trade.getSecondItemToBuy().stackSize <= trade.getSecondItemToBuy().getMaxStackSize())
                  && trade.getItemToSell().stackSize <= trade.getItemToSell().getMaxStackSize()) {
                  return false;
               }

               return true;
            }
         }

         for (int i = 1; i < 5; i++) {
            VillagerEntity.WeightedMerchantEntry entryx = levelUpTradeByProfessionList.get(this.getProfessionFromClass()).get(i);
            if (entryx.matchesMerchantRecipe(trade)) {
               if (trade.getItemToBuy().stackSize <= trade.getItemToBuy().getMaxStackSize()
                  && (!trade.hasSecondItemToBuy() || trade.getSecondItemToBuy().stackSize <= trade.getSecondItemToBuy().getMaxStackSize())
                  && trade.getItemToSell().stackSize <= trade.getItemToSell().getMaxStackSize()) {
                  return false;
               }

               return true;
            }
         }

         return true;
      } catch (NullPointerException var4) {
         var4.printStackTrace();
         return false;
      }
   }

   public static VillagerEntity.WeightedMerchantEntry addTradeToBuySingleItem(
      int profession, int itemID, int minEmeraldCount, int maxEmeraldCount, float weight, int tradeLevel
   ) {
      return addTradeToBuySingleItem(profession, itemID, 0, minEmeraldCount, maxEmeraldCount, weight, tradeLevel);
   }

   public static VillagerEntity.WeightedMerchantEntry addTradeToBuySingleItem(
      int profession, int itemID, int itemMetadata, int minEmeraldCount, int maxEmeraldCount, float weight, int tradeLevel
   ) {
      return addTradeToBuy(profession, itemID, itemMetadata, 1, 1, minEmeraldCount, maxEmeraldCount, weight, tradeLevel);
   }

   public static VillagerEntity.WeightedMerchantEntry addTradeToBuyMultipleItems(
      int profession, int itemID, int minItemCount, int maxItemCount, float weight, int tradeLevel
   ) {
      return addTradeToBuyMultipleItems(profession, itemID, 0, minItemCount, maxItemCount, weight, tradeLevel);
   }

   public static VillagerEntity.WeightedMerchantEntry addTradeToBuyMultipleItems(
      int profession, int itemID, int itemMetadata, int minItemCount, int maxItemCount, float weight, int tradeLevel
   ) {
      return addTradeToBuy(profession, itemID, itemMetadata, minItemCount, maxItemCount, 1, 1, weight, tradeLevel);
   }

   public static VillagerEntity.WeightedMerchantEntry addTradeToBuy(
      int profession, int itemID, int itemMetadata, int minItemCount, int maxItemCount, int minEmeraldCount, int maxEmeraldCount, float weight, int tradeLevel
   ) {
      VillagerEntity.WeightedMerchantRecipeEntry entry = new VillagerEntity.WeightedMerchantRecipeEntry(
         itemID, itemMetadata, minItemCount, maxItemCount, Item.emerald.itemID, 0, minEmeraldCount, maxEmeraldCount, weight, tradeLevel
      );
      return addCustomTrade(profession, entry);
   }

   public static VillagerEntity.WeightedMerchantEntry addTradeToSellSingleItem(
      int profession, int itemID, int minEmeraldCount, int maxEmeraldCount, float weight, int tradeLevel
   ) {
      return addTradeToSellSingleItem(profession, itemID, 0, minEmeraldCount, maxEmeraldCount, weight, tradeLevel);
   }

   public static VillagerEntity.WeightedMerchantEntry addTradeToSellSingleItem(
      int profession, int itemID, int itemMetadata, int minEmeraldCount, int maxEmeraldCount, float weight, int tradeLevel
   ) {
      return addTradeToSell(profession, itemID, itemMetadata, 1, 1, minEmeraldCount, maxEmeraldCount, weight, tradeLevel);
   }

   public static VillagerEntity.WeightedMerchantEntry addTradeToSellMultipleItems(
      int profession, int itemID, int minItemCount, int maxItemCount, float weight, int tradeLevel
   ) {
      return addTradeToSellMultipleItems(profession, itemID, 0, minItemCount, maxItemCount, weight, tradeLevel);
   }

   public static VillagerEntity.WeightedMerchantEntry addTradeToSellMultipleItems(
      int profession, int itemID, int itemMetadata, int minItemCount, int maxItemCount, float weight, int tradeLevel
   ) {
      return addTradeToSell(profession, itemID, itemMetadata, minItemCount, maxItemCount, 1, 1, weight, tradeLevel);
   }

   public static VillagerEntity.WeightedMerchantEntry addTradeToSell(
      int profession, int itemID, int itemMetadata, int minItemCount, int maxItemCount, int minEmeraldCount, int maxEmeraldCount, float weight, int tradeLevel
   ) {
      VillagerEntity.WeightedMerchantRecipeEntry entry = new VillagerEntity.WeightedMerchantRecipeEntry(
         Item.emerald.itemID, 0, minEmeraldCount, maxEmeraldCount, itemID, itemMetadata, minItemCount, maxItemCount, weight, tradeLevel
      );
      return addCustomTrade(profession, entry);
   }

   public static VillagerEntity.WeightedMerchantEntry addArcaneScrollTrade(
      int profession, int enchantmentID, int minEmeraldCount, int maxEmeraldCount, float weight, int tradeLevel
   ) {
      return addItemConversionTrade(
         profession, Item.paper.itemID, 0, minEmeraldCount, maxEmeraldCount, BTWItems.arcaneScroll.itemID, enchantmentID, weight, tradeLevel
      );
   }

   public static VillagerEntity.WeightedMerchantEntry addSkullconversionTrade(
      int profession, int inputSkullType, int minEmeralds, int maxEmeralds, int resultSkullType, float weight, int tradeLevel
   ) {
      return addItemConversionTrade(
         profession, Item.skull.itemID, inputSkullType, minEmeralds, maxEmeralds, Item.skull.itemID, resultSkullType, weight, tradeLevel
      );
   }

   public static VillagerEntity.WeightedMerchantEntry addItemConversionTrade(
      int profession, int itemID, int minEmeralds, int maxEmeralds, int resultID, float weight, int tradeLevel
   ) {
      return addItemConversionTrade(profession, itemID, 0, minEmeralds, maxEmeralds, resultID, 0, weight, tradeLevel);
   }

   public static VillagerEntity.WeightedMerchantEntry addItemConversionTrade(
      int profession, int itemID, int itemMetadata, int minEmeraldCount, int maxEmeraldCount, int resultID, int resultMetadata, float weight, int tradeLevel
   ) {
      VillagerEntity.WeightedMerchantRecipeEntry entry = new VillagerEntity.WeightedMerchantRecipeEntry(
         itemID, itemMetadata, 1, 1, Item.emerald.itemID, 0, minEmeraldCount, maxEmeraldCount, resultID, resultMetadata, 1, 1, weight, tradeLevel
      );
      return addCustomTrade(profession, entry);
   }

   public static VillagerEntity.WeightedMerchantEntry addEnchantmentTrade(
      int profession, int itemID, int minEmeraldCount, int maxEmeraldCount, float weight, int tradeLevel
   ) {
      VillagerEntity.WeightMerchantEnchantmentEntry entry = new VillagerEntity.WeightMerchantEnchantmentEntry(
         itemID, minEmeraldCount, maxEmeraldCount, weight, tradeLevel
      );
      return addCustomTrade(profession, entry);
   }

   public static VillagerEntity.WeightedMerchantEntry addComplexTrade(
      int profession,
      int input1ID,
      int input1Metadata,
      int input1MinCount,
      int input1MaxCount,
      int input2ID,
      int input2Metadata,
      int input2MinCount,
      int input2MaxCount,
      int resultID,
      int resultMetadata,
      int resultMinCount,
      int resultMaxCount,
      float weight,
      int tradeLevel
   ) {
      VillagerEntity.WeightedMerchantRecipeEntry entry = new VillagerEntity.WeightedMerchantRecipeEntry(
         input1ID,
         input1Metadata,
         input1MinCount,
         input1MaxCount,
         input2ID,
         input2Metadata,
         input2MinCount,
         input2MaxCount,
         resultID,
         resultMetadata,
         resultMinCount,
         resultMaxCount,
         weight,
         tradeLevel
      );
      return addCustomTrade(profession, entry);
   }

   public static VillagerEntity.WeightedMerchantEntry addCustomTrade(int profession, VillagerEntity.WeightedMerchantEntry entry) {
      Map<Integer, Set<VillagerEntity.WeightedMerchantEntry>> tradeList = tradeByProfessionList;
      Set<VillagerEntity.WeightedMerchantEntry> tradeEntryList = tradeList.get(profession);
      if (tradeEntryList == null) {
         tradeEntryList = new HashSet<>();
         tradeList.put(profession, tradeEntryList);
      }

      tradeEntryList.add(entry);
      return entry;
   }

   public static void addLevelUpTradeToBuySingleItem(int profession, int itemID, int minEmeraldCount, int maxEmeraldCount, int tradeLevel) {
      addLevelUpTradeToBuySingleItem(profession, itemID, 0, minEmeraldCount, maxEmeraldCount, tradeLevel);
   }

   public static void addLevelUpTradeToBuySingleItem(int profession, int itemID, int itemMetadata, int minEmeraldCount, int maxEmeraldCount, int tradeLevel) {
      addLevelUpTradeToBuy(profession, itemID, itemMetadata, 1, 1, minEmeraldCount, maxEmeraldCount, tradeLevel);
   }

   public static void addLevelUpTradeToBuyMultipleItems(int profession, int itemID, int minItemCount, int maxItemCount, int tradeLevel) {
      addLevelUpTradeToBuyMultipleItems(profession, itemID, 0, minItemCount, maxItemCount, tradeLevel);
   }

   public static void addLevelUpTradeToBuyMultipleItems(int profession, int itemID, int itemMetadata, int minItemCount, int maxItemCount, int tradeLevel) {
      addLevelUpTradeToBuy(profession, itemID, itemMetadata, minItemCount, maxItemCount, 1, 1, tradeLevel);
   }

   public static void addLevelUpTradeToBuy(
      int profession, int itemID, int itemMetadata, int minItemCount, int maxItemCount, int minEmeraldCount, int maxEmeraldCount, int tradeLevel
   ) {
      VillagerEntity.WeightedMerchantRecipeEntry entry = new VillagerEntity.WeightedMerchantRecipeEntry(
         itemID, itemMetadata, minItemCount, maxItemCount, Item.emerald.itemID, 0, minEmeraldCount, maxEmeraldCount, 1.0F, tradeLevel
      );
      addCustomLevelUpTrade(profession, entry);
   }

   public static void addCustomLevelUpTrade(int profession, VillagerEntity.WeightedMerchantEntry entry) {
      Map<Integer, VillagerEntity.WeightedMerchantEntry> tradeEntryList = levelUpTradeByProfessionList.get(profession);
      if (tradeEntryList == null) {
         tradeEntryList = new HashMap<>();
         levelUpTradeByProfessionList.put(profession, tradeEntryList);
      }

      int tradeLevel = entry.level;
      entry.level *= -1;
      if (tradeEntryList.containsKey(tradeLevel)) {
         throw new RuntimeException("Profession id " + profession + "already has a level up trade assigned for level " + tradeLevel);
      } else {
         tradeEntryList.put(tradeLevel, entry);
      }
   }

   public static boolean removeLevelUpTrade(int profession, int level) {
      Map<Integer, VillagerEntity.WeightedMerchantEntry> tradeEntryList = levelUpTradeByProfessionList.get(profession);
      if (tradeEntryList != null) {
         VillagerEntity.WeightedMerchantEntry entry = tradeEntryList.get(level);
         if (entry != null) {
            tradeEntryList.remove(level);
         }
      }

      return false;
   }

   public static boolean removeTradeToBuy(int profession, int itemID, int itemMetadata) {
      return removeComplexTrade(profession, itemID, itemMetadata, 0, 0, Item.emerald.itemID, 0);
   }

   public static boolean removeTradeToSell(int profession, int itemID, int itemMetadata) {
      return removeComplexTrade(profession, Item.emerald.itemID, 0, 0, 0, itemID, itemMetadata);
   }

   public static boolean removeComplexTrade(int profession, int item1ID, int item1Metadata, int item2ID, int item2Metadata, int resultID, int resultMetadata) {
      VillagerEntity.WeightedMerchantRecipeEntry entryToRemove = new VillagerEntity.WeightedMerchantRecipeEntry(
         item1ID, item1Metadata, 1, 1, item2ID, item2Metadata, 1, 1, resultID, resultMetadata, 1, 1, 1.0F, 1
      );
      return removeCustomTrade(profession, entryToRemove);
   }

   public static boolean removeEnchantmentTrade(int profession, int itemID) {
      VillagerEntity.WeightMerchantEnchantmentEntry entryToRemove = new VillagerEntity.WeightMerchantEnchantmentEntry(itemID, 1, 1, 1.0F, 1);
      return removeCustomTrade(profession, entryToRemove);
   }

   public static boolean removeCustomTrade(int profession, VillagerEntity.WeightedMerchantEntry entryToRemove) {
      for (VillagerEntity.WeightedMerchantEntry entry : tradeByProfessionList.get(profession)) {
         if (entry.equals(entryToRemove)) {
            tradeByProfessionList.get(profession).remove(entry);
            return true;
         }
      }

      for (int i = 1; i < 5; i++) {
         VillagerEntity.WeightedMerchantEntry entryx = levelUpTradeByProfessionList.get(profession).get(i);
         if (entryx.equals(entryToRemove)) {
            levelUpTradeByProfessionList.get(profession).put(i, null);
            return true;
         }
      }

      return false;
   }

   public void playEffectsForTrade(MerchantRecipe trade) {
      Map<VillagerEntity.WeightedMerchantEntry, VillagerEntity.TradeEffect> effectRegistry = tradeEffectRegistry.get(this.getProfessionFromClass());
      if (effectRegistry != null) {
         for (VillagerEntity.WeightedMerchantEntry entry : effectRegistry.keySet()) {
            if (entry.matchesMerchantRecipe(trade)) {
               effectRegistry.get(entry).playEffect(this);
            }
         }
      }
   }

   public static void registerEffectForTrade(int profession, VillagerEntity.WeightedMerchantEntry entry, VillagerEntity.TradeEffect effect) {
      Map<VillagerEntity.WeightedMerchantEntry, VillagerEntity.TradeEffect> effectRegistry = tradeEffectRegistry.get(profession);
      if (effectRegistry == null) {
         effectRegistry = new HashMap<>();
         tradeEffectRegistry.put(profession, effectRegistry);
      }

      effectRegistry.put(entry, effect);
   }

   public static boolean removeEffectForTrade(int profession, VillagerEntity.WeightedMerchantEntry entry, VillagerEntity.TradeEffect effect) {
      Map<VillagerEntity.WeightedMerchantEntry, VillagerEntity.TradeEffect> effectRegistry = tradeEffectRegistry.get(profession);
      if (effectRegistry != null && effectRegistry.containsKey(entry)) {
         effectRegistry.remove(entry);
         return true;
      } else {
         return false;
      }
   }

   public void playEffectsForLevelUp(MerchantRecipe trade) {
      Map<Integer, VillagerEntity.TradeEffect> effectRegistry = levelUpEffectRegistry.get(this.getProfessionFromClass());
      if (effectRegistry != null) {
         VillagerEntity.TradeEffect effect = effectRegistry.get(-trade.tradeLevel);
         if (effect != null) {
            effect.playEffect(this);
            return;
         }
      }

      defaultLevelUpEffect.playEffect(this);
   }

   public static void registerEffectForLevelUp(int profession, int level, VillagerEntity.TradeEffect effect) {
      Map<Integer, VillagerEntity.TradeEffect> effectRegistry = levelUpEffectRegistry.get(profession);
      if (effectRegistry == null) {
         effectRegistry = new HashMap<>();
         levelUpEffectRegistry.put(profession, effectRegistry);
      }

      effectRegistry.put(level, effect);
   }

   public static boolean removeEffectForLevelUp(int profession, int level) {
      Map<Integer, VillagerEntity.TradeEffect> effectRegistry = levelUpEffectRegistry.get(profession);
      if (effectRegistry != null && effectRegistry.containsKey(level)) {
         effectRegistry.remove(level);
         return true;
      } else {
         return false;
      }
   }

   protected boolean customInteract(EntityPlayer player) {
      ItemStack heldStack = player.inventory.getCurrentItem();
      if (heldStack != null && heldStack.getItem().itemID == Item.diamond.itemID && this.b() == 0 && this.getInLove() == 0 && !this.isPossessed()) {
         if (!player.capabilities.isCreativeMode) {
            heldStack.stackSize--;
            if (heldStack.stackSize <= 0) {
               player.inventory.setInventorySlotContents(player.inventory.currentItem, (ItemStack)null);
            }
         }

         this.worldObj.playSoundAtEntity(this, "random.classic_hurt", 1.0F, this.getSoundPitch() * 2.0F);
         this.setInLove(1);
         this.entityToAttack = null;
         return true;
      } else {
         if (heldStack != null && heldStack.itemID == BTWItems.nameTag.itemID) {
            BTWItems.nameTag.itemInteractionForEntity(heldStack, this);
         }

         return false;
      }
   }

   protected void updateStatusParticles() {
      this.spawnCustomParticles();
      if (this.getInLove() > 0) {
         this.generateRandomParticles("heart");
      }
   }

   protected void spawnCustomParticles() {
   }

   @Override
   protected void generateRandomParticles(String sParticle) {
      for (int iTempCount = 0; iTempCount < 5; iTempCount++) {
         double dVelX = this.rand.nextGaussian() * 0.02;
         double dVelY = this.rand.nextGaussian() * 0.02;
         double dVelZ = this.rand.nextGaussian() * 0.02;
         this.worldObj
            .spawnParticle(
               sParticle,
               this.posX + this.rand.nextFloat() * this.width * 2.0F - this.width,
               this.posY + 1.0 + this.rand.nextFloat() * this.height,
               this.posZ + this.rand.nextFloat() * this.width * 2.0F - this.width,
               dVelX,
               dVelY,
               dVelZ
            );
      }
   }

   public void checkForLooseMilk() {
      List collisionList = this.worldObj
         .getEntitiesWithinAABB(
            EntityItem.class,
            AxisAlignedBB.getAABBPool().getAABB(this.posX - 1.0, this.posY - 1.0, this.posZ - 1.0, this.posX + 1.0, this.posY + 1.0, this.posZ + 1.0)
         );
      if (!collisionList.isEmpty()) {
         for (int listIndex = 0; listIndex < collisionList.size(); listIndex++) {
            EntityItem entityItem = (EntityItem)collisionList.get(listIndex);
            if (entityItem.delayBeforeCanPickup <= 0 && !entityItem.isDead) {
               int iTempItemID = entityItem.getEntityItem().itemID;
               Item tempItem = Item.itemsList[iTempItemID];
               if (tempItem.itemID == Item.bucketMilk.itemID) {
                  entityItem.w();
                  entityItem = (EntityItem)EntityList.createEntityOfType(
                     EntityItem.class, this.worldObj, this.posX, this.posY - 0.3F + this.e(), this.posZ, new ItemStack(Item.bucketMilk, 1, 0)
                  );
                  float f1 = 0.2F;
                  entityItem.motionX = -MathHelper.sin(this.rotationYaw / 180.0F * 3.141593F) * MathHelper.cos(this.rotationPitch / 180.0F * 3.141593F) * f1;
                  entityItem.motionZ = MathHelper.cos(this.rotationYaw / 180.0F * 3.141593F) * MathHelper.cos(this.rotationPitch / 180.0F * 3.141593F) * f1;
                  entityItem.motionY = -MathHelper.sin(this.rotationPitch / 180.0F * 3.141593F) * f1 + 0.2F;
                  f1 = 0.02F;
                  float f3 = this.rand.nextFloat() * 3.141593F * 2.0F;
                  f1 *= this.rand.nextFloat();
                  entityItem.motionX = entityItem.motionX + Math.cos(f3) * f1;
                  entityItem.motionY += 0.25;
                  entityItem.motionZ = entityItem.motionZ + Math.sin(f3) * f1;
                  entityItem.delayBeforeCanPickup = 10;
                  this.worldObj.spawnEntityInWorld(entityItem);
                  int iFXI = MathHelper.floor_double(entityItem.posX);
                  int iFXJ = MathHelper.floor_double(entityItem.posY);
                  int iFXK = MathHelper.floor_double(entityItem.posZ);
                  int iExtraData = 0;
                  if (this.isPossessed() || this.m() == 2 && this.getCurrentTradeLevel() == 5) {
                     iExtraData = 1;
                  }

                  this.worldObj.playAuxSFX(2265, iFXI, iFXJ, iFXK, iExtraData);
               }
            }
         }
      }
   }

   public int getInLove() {
      return this.dataWatcher.getWatchableObjectInt(22);
   }

   public void setInLove(int iInLove) {
      this.dataWatcher.updateObject(22, iInLove);
   }

   @Override
   public int getCurrentTradeLevel() {
      return this.dataWatcher.getWatchableObjectInt(23);
   }

   public void setTradeLevel(int iTradeLevel) {
      this.dataWatcher.updateObject(23, iTradeLevel);
   }

   @Override
   public int getCurrentTradeXP() {
      return this.dataWatcher.getWatchableObjectInt(25);
   }

   public void setTradeExperience(int iTradeExperience) {
      this.dataWatcher.updateObject(25, iTradeExperience);
   }

   public int getDirtyPeasant() {
      return 0;
   }

   @Override
   public int getCurrentTradeMaxXP() {
      return xpPerLevel[this.getCurrentTradeLevel()];
   }

   public void setDirtyPeasant(int iDirtyPeasant) {
   }

   protected void scheduleImmediateTradelistRefresh() {
      this.updateTradesCountdown = 1;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void handleHealthUpdate(byte bUpdateType) {
      super.handleHealthUpdate(bUpdateType);
      if (bUpdateType == 14) {
         this.worldObj.playSound(this.posX, this.posY, this.posZ, "random.pop", 0.25F, ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
      }
   }

   static {
      professionMap.put(0, FarmerVillagerEntity.class);
      professionMap.put(1, LibrarianVillagerEntity.class);
      professionMap.put(2, PriestVillagerEntity.class);
      professionMap.put(3, BlacksmithVillagerEntity.class);
      professionMap.put(4, ButcherVillagerEntity.class);
      casteMap.put(0, new ArrayList<>());
      casteMap.put(1, new ArrayList<>());
      casteMap.put(2, new ArrayList<>());
      casteMap.get(0).add(0);
      casteMap.get(1).add(3);
      casteMap.get(1).add(4);
      casteMap.get(2).add(1);
      casteMap.get(2).add(2);
   }

   public interface TradeConditional {
      boolean shouldAddTrade(VillagerEntity var1);
   }

   public interface TradeEffect {
      void playEffect(VillagerEntity var1);
   }

   public static class WeightMerchantEnchantmentEntry extends VillagerEntity.WeightedMerchantEntry {
      public final int itemID;
      public final int minEmeraldCount;
      public final int maxEmeraldCount;

      public WeightMerchantEnchantmentEntry(int itemID, int minEmeraldCount, int maxEmeraldCount, float weight, int level) {
         super(weight, level);
         this.itemID = itemID;
         this.minEmeraldCount = minEmeraldCount;
         this.maxEmeraldCount = maxEmeraldCount;
      }

      @Override
      public boolean equals(VillagerEntity.WeightedMerchantEntry entry) {
         return entry instanceof VillagerEntity.WeightMerchantEnchantmentEntry && this.itemID == ((VillagerEntity.WeightMerchantEnchantmentEntry)entry).itemID;
      }

      @Override
      public MerchantRecipe generateRecipe(Random rand) {
         int cost = MathHelper.getRandomIntegerInRange(rand, this.minEmeraldCount, this.maxEmeraldCount);
         ItemStack input = new ItemStack(Item.itemsList[this.itemID]);
         ItemStack emeralds = new ItemStack(Item.emerald, cost);
         ItemStack result = EnchantmentHelper.addRandomEnchantment(rand, input.copy(), 5 + rand.nextInt(15));
         MerchantRecipe trade = new MerchantRecipe(input, emeralds, result, this.level);
         if (this.isMandatory()) {
            trade.setMandatory();
         }

         return trade;
      }

      @Override
      public boolean matchesMerchantRecipe(MerchantRecipe trade) {
         return trade.getItemToBuy().itemID == this.itemID
            && (!trade.hasSecondItemToBuy() || trade.getSecondItemToBuy().itemID == Item.emerald.itemID)
            && trade.getItemToSell().itemID == this.itemID;
      }
   }

   public abstract static class WeightedMerchantEntry {
      public final float weight;
      public int level;
      private boolean isMandatory;
      private VillagerEntity.TradeConditional conditional;

      public WeightedMerchantEntry(float weight, int level) {
         this.weight = weight;
         this.level = level;
         this.isMandatory = false;
      }

      public abstract boolean equals(VillagerEntity.WeightedMerchantEntry var1);

      public abstract MerchantRecipe generateRecipe(Random var1);

      public abstract boolean matchesMerchantRecipe(MerchantRecipe var1);

      public void setDefault(int profession) {
         VillagerEntity.defaultTradeByProfessionList.put(profession, this);
      }

      public VillagerEntity.WeightedMerchantEntry registerEffectForTrade(int profession, VillagerEntity.TradeEffect effect) {
         VillagerEntity.registerEffectForTrade(profession, this, effect);
         return this;
      }

      public boolean isMandatory() {
         return this.isMandatory;
      }

      public VillagerEntity.WeightedMerchantEntry setMandatory() {
         this.isMandatory = true;
         return this;
      }

      public VillagerEntity.WeightedMerchantEntry setConditional(VillagerEntity.TradeConditional conditional) {
         this.conditional = conditional;
         return this;
      }

      public boolean canBeAdded(VillagerEntity villager) {
         return this.conditional == null || this.conditional.shouldAddTrade(villager);
      }
   }

   public static class WeightedMerchantRecipeEntry extends VillagerEntity.WeightedMerchantEntry {
      public final int input1ID;
      public final int input1Metadata;
      public final int input1MinCount;
      public final int input1MaxCount;
      public final int input2ID;
      public final int input2Metadata;
      public final int input2MinCount;
      public final int input2MaxCount;
      public final int resultID;
      public final int resultMetadata;
      public final int resultMinCount;
      public final int resultMaxCount;
      private boolean randomizeMeta1 = false;
      private int[] randomMetas1;
      private boolean randomizeMeta2 = false;
      private int[] randomMetas2;
      private boolean randomizeMetaResult = false;
      private int[] randomMetasResult;

      public WeightedMerchantRecipeEntry(
         int input1ID,
         int input1Metadata,
         int input1MinCount,
         int input1MaxCount,
         int input2ID,
         int input2Metadata,
         int input2MinCount,
         int input2MaxCount,
         int resultID,
         int resultMetadata,
         int resultMinCount,
         int resultMaxCount,
         float weight,
         int level
      ) {
         super(weight, level);
         this.input1ID = input1ID;
         this.input1Metadata = input1Metadata;
         this.input1MinCount = input1MinCount;
         this.input1MaxCount = input1MaxCount;
         this.input2ID = input2ID;
         this.input2Metadata = input2Metadata;
         this.input2MinCount = input2MinCount;
         this.input2MaxCount = input2MaxCount;
         this.resultID = resultID;
         this.resultMetadata = resultMetadata;
         this.resultMinCount = resultMinCount;
         this.resultMaxCount = resultMaxCount;
      }

      public WeightedMerchantRecipeEntry(
         int inputID,
         int inputMetadata,
         int inputMinCount,
         int inputMaxCount,
         int resultID,
         int resultMetadata,
         int resultMinCount,
         int resultMaxCount,
         float weight,
         int level
      ) {
         super(weight, level);
         this.input1ID = inputID;
         this.input1Metadata = inputMetadata;
         this.input1MinCount = inputMinCount;
         this.input1MaxCount = inputMaxCount;
         this.input2ID = 0;
         this.input2Metadata = 0;
         this.input2MinCount = 0;
         this.input2MaxCount = 0;
         this.resultID = resultID;
         this.resultMetadata = resultMetadata;
         this.resultMinCount = resultMinCount;
         this.resultMaxCount = resultMaxCount;
      }

      @Override
      public boolean equals(VillagerEntity.WeightedMerchantEntry entry) {
         if (!(entry instanceof VillagerEntity.WeightedMerchantRecipeEntry)) {
            return false;
         } else {
            VillagerEntity.WeightedMerchantRecipeEntry recipeEntry = (VillagerEntity.WeightedMerchantRecipeEntry)entry;
            return this.input1ID == recipeEntry.input1ID
               && this.input1Metadata == recipeEntry.input1Metadata
               && this.input2ID == recipeEntry.input2ID
               && this.input2Metadata == recipeEntry.input2Metadata
               && this.resultID == recipeEntry.resultID
               && this.resultMetadata == recipeEntry.resultMetadata;
         }
      }

      @Override
      public MerchantRecipe generateRecipe(Random rand) {
         int count1 = MathHelper.getRandomIntegerInRange(rand, this.input1MinCount, this.input1MaxCount);
         int count2 = MathHelper.getRandomIntegerInRange(rand, this.input2MinCount, this.input2MaxCount);
         int countResult = MathHelper.getRandomIntegerInRange(rand, this.resultMinCount, this.resultMaxCount);
         int meta1 = this.input1Metadata;
         int meta2 = this.input2Metadata;
         int metaResult = this.resultMetadata;
         if (this.randomizeMeta1) {
            meta1 = this.randomMetas1[rand.nextInt(this.randomMetas1.length)];
         }

         if (this.randomizeMeta2) {
            meta2 = this.randomMetas2[rand.nextInt(this.randomMetas2.length)];
         }

         if (this.randomizeMetaResult) {
            metaResult = this.randomMetasResult[rand.nextInt(this.randomMetasResult.length)];
         }

         ItemStack input1 = new ItemStack(Item.itemsList[this.input1ID], count1, meta1);
         ItemStack input2 = null;
         if (this.input2ID != 0) {
            input2 = new ItemStack(Item.itemsList[this.input2ID], count2, meta2);
         }

         ItemStack result = new ItemStack(Item.itemsList[this.resultID], countResult, metaResult);
         MerchantRecipe trade = new MerchantRecipe(input1, input2, result, this.level);
         if (this.isMandatory()) {
            trade.setMandatory();
         }

         return trade;
      }

      public VillagerEntity.WeightedMerchantEntry setRandomMetas(int[] randomMetas, int slot) {
         switch (slot) {
            case 0:
               this.randomizeMeta1 = true;
               this.randomMetas1 = randomMetas;
               break;
            case 1:
               this.randomizeMeta2 = true;
               this.randomMetas2 = randomMetas;
               break;
            case 2:
               this.randomizeMetaResult = true;
               this.randomMetasResult = randomMetas;
         }

         return this;
      }

      @Override
      public boolean matchesMerchantRecipe(MerchantRecipe trade) {
         return trade.getItemToBuy().itemID == this.input1ID
            && (trade.getItemToBuy().getItemDamage() == this.input1Metadata || this.randomizeMeta1)
            && (
               !trade.hasSecondItemToBuy()
                  || trade.getSecondItemToBuy().itemID == this.input2ID
                     && (trade.getSecondItemToBuy().getItemDamage() == this.input2Metadata || this.randomizeMeta2)
            )
            && trade.getItemToSell().itemID == this.resultID
            && (trade.getItemToSell().getItemDamage() == this.resultMetadata || this.randomizeMetaResult);
      }
   }
}
