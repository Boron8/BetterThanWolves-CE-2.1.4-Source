package net.minecraft.src;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

public class EntityVillager extends EntityAgeable implements INpc, IMerchant {
   private int randomTickDivider = 0;
   private boolean isMating = false;
   private boolean isPlaying = false;
   public Village villageObj = null;
   private EntityPlayer buyingPlayer;
   protected MerchantRecipeList buyingList;
   private int timeUntilReset;
   private boolean needsInitilization;
   private int wealth;
   private String lastBuyingPlayer;
   private boolean field_82190_bM;
   private float field_82191_bN;
   private static final Map villagerStockList = new HashMap();
   private static final Map blacksmithSellingList = new HashMap();

   public EntityVillager(World par1World) {
      this(par1World, 0);
   }

   public EntityVillager(World par1World, int par2) {
      super(par1World);
      this.setProfession(par2);
      this.texture = "/mob/villager/villager.png";
      this.moveSpeed = 0.5F;
      this.a(0.6F, 1.8F);
      this.aC().setBreakDoors(true);
      this.aC().setAvoidsWater(true);
      this.tasks.addTask(0, new EntityAISwimming(this));
      this.tasks.addTask(1, new EntityAIAvoidEntity(this, EntityZombie.class, 8.0F, 0.3F, 0.35F));
      this.tasks.addTask(1, new EntityAITradePlayer(this));
      this.tasks.addTask(1, new EntityAILookAtTradePlayer(this));
      this.tasks.addTask(2, new EntityAIMoveIndoors(this));
      this.tasks.addTask(3, new EntityAIRestrictOpenDoor(this));
      this.tasks.addTask(4, new EntityAIOpenDoor(this, true));
      this.tasks.addTask(5, new EntityAIMoveTwardsRestriction(this, 0.3F));
      this.tasks.addTask(6, new EntityAIVillagerMate(this));
      this.tasks.addTask(7, new EntityAIFollowGolem(this));
      this.tasks.addTask(8, new EntityAIPlay(this, 0.32F));
      this.tasks.addTask(9, new EntityAIWatchClosest2(this, EntityPlayer.class, 3.0F, 1.0F));
      this.tasks.addTask(9, new EntityAIWatchClosest2(this, EntityVillager.class, 5.0F, 0.02F));
      this.tasks.addTask(9, new EntityAIWander(this, 0.3F));
      this.tasks.addTask(10, new EntityAIWatchClosest(this, EntityLiving.class, 8.0F));
   }

   @Override
   public boolean isAIEnabled() {
      return true;
   }

   @Override
   protected void updateAITick() {
      if (--this.randomTickDivider <= 0) {
         this.worldObj
            .villageCollectionObj
            .addVillagerPosition(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ));
         this.randomTickDivider = 70 + this.rand.nextInt(50);
         this.villageObj = this.worldObj
            .villageCollectionObj
            .findNearestVillage(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ), 32);
         if (this.villageObj == null) {
            this.aO();
         } else {
            ChunkCoordinates var1 = this.villageObj.getCenter();
            this.b(var1.posX, var1.posY, var1.posZ, (int)(this.villageObj.getVillageRadius() * 0.6F));
            if (this.field_82190_bM) {
               this.field_82190_bM = false;
               this.villageObj.func_82683_b(5);
            }
         }
      }

      if (!this.isTrading() && this.timeUntilReset > 0) {
         this.timeUntilReset--;
         if (this.timeUntilReset <= 0) {
            if (this.needsInitilization) {
               if (this.buyingList.size() > 1) {
                  for (MerchantRecipe var2 : this.buyingList) {
                     if (var2.func_82784_g()) {
                        var2.func_82783_a(this.rand.nextInt(6) + this.rand.nextInt(6) + 2);
                     }
                  }
               }

               this.addDefaultEquipmentAndRecipies(1);
               this.needsInitilization = false;
               if (this.villageObj != null && this.lastBuyingPlayer != null) {
                  this.worldObj.setEntityState(this, (byte)14);
                  this.villageObj.setReputationForPlayer(this.lastBuyingPlayer, 1);
               }
            }

            this.d(new PotionEffect(Potion.regeneration.id, 200, 0));
         }
      }

      super.bp();
   }

   @Override
   public boolean interact(EntityPlayer par1EntityPlayer) {
      ItemStack var2 = par1EntityPlayer.inventory.getCurrentItem();
      boolean var3 = var2 != null && var2.itemID == Item.monsterPlacer.itemID;
      if (!var3 && this.R() && !this.isTrading() && !this.h_()) {
         if (!this.worldObj.isRemote) {
            this.setCustomer(par1EntityPlayer);
            par1EntityPlayer.displayGUIMerchant(this, this.bP());
         }

         return true;
      } else {
         return super.interact(par1EntityPlayer);
      }
   }

   @Override
   protected void entityInit() {
      super.entityInit();
      this.dataWatcher.addObject(16, 0);
   }

   @Override
   public int getMaxHealth() {
      return 20;
   }

   @Override
   public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound) {
      super.writeEntityToNBT(par1NBTTagCompound);
      par1NBTTagCompound.setInteger("Profession", this.getProfession());
      par1NBTTagCompound.setInteger("Riches", this.wealth);
      if (this.buyingList != null) {
         par1NBTTagCompound.setCompoundTag("Offers", this.buyingList.getRecipiesAsTags());
      }
   }

   @Override
   public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound) {
      super.readEntityFromNBT(par1NBTTagCompound);
      this.setProfession(par1NBTTagCompound.getInteger("Profession"));
      this.wealth = par1NBTTagCompound.getInteger("Riches");
      if (par1NBTTagCompound.hasKey("Offers")) {
         NBTTagCompound var2 = par1NBTTagCompound.getCompoundTag("Offers");
         this.buyingList = new MerchantRecipeList(var2);
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public String getTexture() {
      switch (this.getProfession()) {
         case 0:
            return "/mob/villager/farmer.png";
         case 1:
            return "/mob/villager/librarian.png";
         case 2:
            return "/mob/villager/priest.png";
         case 3:
            return "/mob/villager/smith.png";
         case 4:
            return "/mob/villager/butcher.png";
         default:
            return super.N();
      }
   }

   @Override
   protected boolean canDespawn() {
      return false;
   }

   @Override
   protected String getLivingSound() {
      return "mob.villager.default";
   }

   @Override
   protected String getHurtSound() {
      return "mob.villager.defaulthurt";
   }

   @Override
   protected String getDeathSound() {
      return "mob.villager.defaultdeath";
   }

   public void setProfession(int par1) {
      this.dataWatcher.updateObject(16, par1);
   }

   public int getProfession() {
      return this.dataWatcher.getWatchableObjectInt(16);
   }

   public boolean isMating() {
      return this.isMating;
   }

   public void setMating(boolean par1) {
      this.isMating = par1;
   }

   public void setPlaying(boolean par1) {
      this.isPlaying = par1;
   }

   public boolean isPlaying() {
      return this.isPlaying;
   }

   @Override
   public void setRevengeTarget(EntityLiving par1EntityLiving) {
      super.c(par1EntityLiving);
      if (this.villageObj != null && par1EntityLiving != null) {
         this.villageObj.addOrRenewAgressor(par1EntityLiving);
         if (par1EntityLiving instanceof EntityPlayer) {
            byte var2 = -1;
            if (this.h_()) {
               var2 = -3;
            }

            this.villageObj.setReputationForPlayer(((EntityPlayer)par1EntityLiving).getCommandSenderName(), var2);
            if (this.R()) {
               this.worldObj.setEntityState(this, (byte)13);
            }
         }
      }
   }

   @Override
   public void onDeath(DamageSource par1DamageSource) {
      if (this.villageObj != null) {
         Entity var2 = par1DamageSource.getEntity();
         if (var2 != null) {
            if (var2 instanceof EntityPlayer) {
               this.villageObj.setReputationForPlayer(((EntityPlayer)var2).getCommandSenderName(), -2);
            } else if (var2 instanceof IMob) {
               this.villageObj.endMatingSeason();
            }
         } else if (var2 == null) {
            EntityPlayer var3 = this.worldObj.getClosestPlayerToEntity(this, 16.0);
            if (var3 != null) {
               this.villageObj.endMatingSeason();
            }
         }
      }

      super.a(par1DamageSource);
   }

   @Override
   public void setCustomer(EntityPlayer par1EntityPlayer) {
      this.buyingPlayer = par1EntityPlayer;
   }

   @Override
   public EntityPlayer getCustomer() {
      return this.buyingPlayer;
   }

   public boolean isTrading() {
      return this.buyingPlayer != null;
   }

   @Override
   public void useRecipe(MerchantRecipe par1MerchantRecipe) {
      par1MerchantRecipe.incrementToolUses();
      if (par1MerchantRecipe.hasSameIDsAs((MerchantRecipe)this.buyingList.get(this.buyingList.size() - 1))) {
         this.timeUntilReset = 40;
         this.needsInitilization = true;
         if (this.buyingPlayer != null) {
            this.lastBuyingPlayer = this.buyingPlayer.getCommandSenderName();
         } else {
            this.lastBuyingPlayer = null;
         }
      }

      if (par1MerchantRecipe.getItemToBuy().itemID == Item.emerald.itemID) {
         this.wealth = this.wealth + par1MerchantRecipe.getItemToBuy().stackSize;
      }
   }

   @Override
   public MerchantRecipeList getRecipes(EntityPlayer par1EntityPlayer) {
      if (this.buyingList == null) {
         this.addDefaultEquipmentAndRecipies(1);
      }

      return this.buyingList;
   }

   private float func_82188_j(float par1) {
      float var2 = par1 + this.field_82191_bN;
      return var2 > 0.9F ? 0.9F - (var2 - 0.9F) : var2;
   }

   private void addDefaultEquipmentAndRecipies(int par1) {
      if (this.buyingList != null) {
         this.field_82191_bN = MathHelper.sqrt_float(this.buyingList.size()) * 0.2F;
      } else {
         this.field_82191_bN = 0.0F;
      }

      MerchantRecipeList var2 = new MerchantRecipeList();
      switch (this.getProfession()) {
         case 0:
            addMerchantItem(var2, Item.wheat.itemID, this.rand, this.func_82188_j(0.9F));
            addMerchantItem(var2, Block.cloth.blockID, this.rand, this.func_82188_j(0.5F));
            addMerchantItem(var2, Item.chickenRaw.itemID, this.rand, this.func_82188_j(0.5F));
            addMerchantItem(var2, Item.fishCooked.itemID, this.rand, this.func_82188_j(0.4F));
            addBlacksmithItem(var2, Item.bread.itemID, this.rand, this.func_82188_j(0.9F));
            addBlacksmithItem(var2, Item.melon.itemID, this.rand, this.func_82188_j(0.3F));
            addBlacksmithItem(var2, Item.appleRed.itemID, this.rand, this.func_82188_j(0.3F));
            addBlacksmithItem(var2, Item.cookie.itemID, this.rand, this.func_82188_j(0.3F));
            addBlacksmithItem(var2, Item.shears.itemID, this.rand, this.func_82188_j(0.3F));
            addBlacksmithItem(var2, Item.flintAndSteel.itemID, this.rand, this.func_82188_j(0.3F));
            addBlacksmithItem(var2, Item.chickenCooked.itemID, this.rand, this.func_82188_j(0.3F));
            addBlacksmithItem(var2, Item.arrow.itemID, this.rand, this.func_82188_j(0.5F));
            if (this.rand.nextFloat() < this.func_82188_j(0.5F)) {
               var2.add(
                  new MerchantRecipe(
                     new ItemStack(Block.gravel, 10), new ItemStack(Item.emerald), new ItemStack(Item.flint.itemID, 4 + this.rand.nextInt(2), 0)
                  )
               );
            }
            break;
         case 1:
            addMerchantItem(var2, Item.paper.itemID, this.rand, this.func_82188_j(0.8F));
            addMerchantItem(var2, Item.book.itemID, this.rand, this.func_82188_j(0.8F));
            addMerchantItem(var2, Item.writtenBook.itemID, this.rand, this.func_82188_j(0.3F));
            addBlacksmithItem(var2, Block.bookShelf.blockID, this.rand, this.func_82188_j(0.8F));
            addBlacksmithItem(var2, Block.glass.blockID, this.rand, this.func_82188_j(0.2F));
            addBlacksmithItem(var2, Item.compass.itemID, this.rand, this.func_82188_j(0.2F));
            addBlacksmithItem(var2, Item.pocketSundial.itemID, this.rand, this.func_82188_j(0.2F));
            if (this.rand.nextFloat() < this.func_82188_j(0.07F)) {
               Enchantment var8 = Enchantment.field_92090_c[this.rand.nextInt(Enchantment.field_92090_c.length)];
               int var10 = MathHelper.getRandomIntegerInRange(this.rand, var8.getMinLevel(), var8.getMaxLevel());
               ItemStack var11 = Item.enchantedBook.func_92111_a(new EnchantmentData(var8, var10));
               int var6 = 2 + this.rand.nextInt(5 + var10 * 10) + 3 * var10;
               var2.add(new MerchantRecipe(new ItemStack(Item.book), new ItemStack(Item.emerald, var6), var11));
            }
            break;
         case 2:
            addBlacksmithItem(var2, Item.eyeOfEnder.itemID, this.rand, this.func_82188_j(0.3F));
            addBlacksmithItem(var2, Item.expBottle.itemID, this.rand, this.func_82188_j(0.2F));
            addBlacksmithItem(var2, Item.redstone.itemID, this.rand, this.func_82188_j(0.4F));
            addBlacksmithItem(var2, Block.glowStone.blockID, this.rand, this.func_82188_j(0.3F));
            int[] var3 = new int[]{
               Item.swordIron.itemID,
               Item.swordDiamond.itemID,
               Item.plateIron.itemID,
               Item.plateDiamond.itemID,
               Item.axeIron.itemID,
               Item.axeDiamond.itemID,
               Item.pickaxeIron.itemID,
               Item.pickaxeDiamond.itemID
            };

            for (int var7 : var3) {
               if (this.rand.nextFloat() < this.func_82188_j(0.05F)) {
                  var2.add(
                     new MerchantRecipe(
                        new ItemStack(var7, 1, 0),
                        new ItemStack(Item.emerald, 2 + this.rand.nextInt(3), 0),
                        EnchantmentHelper.addRandomEnchantment(this.rand, new ItemStack(var7, 1, 0), 5 + this.rand.nextInt(15))
                     )
                  );
               }
            }
            break;
         case 3:
            addMerchantItem(var2, Item.coal.itemID, this.rand, this.func_82188_j(0.7F));
            addMerchantItem(var2, Item.ingotIron.itemID, this.rand, this.func_82188_j(0.5F));
            addMerchantItem(var2, Item.ingotGold.itemID, this.rand, this.func_82188_j(0.5F));
            addMerchantItem(var2, Item.diamond.itemID, this.rand, this.func_82188_j(0.5F));
            addBlacksmithItem(var2, Item.swordIron.itemID, this.rand, this.func_82188_j(0.5F));
            addBlacksmithItem(var2, Item.swordDiamond.itemID, this.rand, this.func_82188_j(0.5F));
            addBlacksmithItem(var2, Item.axeIron.itemID, this.rand, this.func_82188_j(0.3F));
            addBlacksmithItem(var2, Item.axeDiamond.itemID, this.rand, this.func_82188_j(0.3F));
            addBlacksmithItem(var2, Item.pickaxeIron.itemID, this.rand, this.func_82188_j(0.5F));
            addBlacksmithItem(var2, Item.pickaxeDiamond.itemID, this.rand, this.func_82188_j(0.5F));
            addBlacksmithItem(var2, Item.shovelIron.itemID, this.rand, this.func_82188_j(0.2F));
            addBlacksmithItem(var2, Item.shovelDiamond.itemID, this.rand, this.func_82188_j(0.2F));
            addBlacksmithItem(var2, Item.hoeIron.itemID, this.rand, this.func_82188_j(0.2F));
            addBlacksmithItem(var2, Item.hoeDiamond.itemID, this.rand, this.func_82188_j(0.2F));
            addBlacksmithItem(var2, Item.bootsIron.itemID, this.rand, this.func_82188_j(0.2F));
            addBlacksmithItem(var2, Item.bootsDiamond.itemID, this.rand, this.func_82188_j(0.2F));
            addBlacksmithItem(var2, Item.helmetIron.itemID, this.rand, this.func_82188_j(0.2F));
            addBlacksmithItem(var2, Item.helmetDiamond.itemID, this.rand, this.func_82188_j(0.2F));
            addBlacksmithItem(var2, Item.plateIron.itemID, this.rand, this.func_82188_j(0.2F));
            addBlacksmithItem(var2, Item.plateDiamond.itemID, this.rand, this.func_82188_j(0.2F));
            addBlacksmithItem(var2, Item.legsIron.itemID, this.rand, this.func_82188_j(0.2F));
            addBlacksmithItem(var2, Item.legsDiamond.itemID, this.rand, this.func_82188_j(0.2F));
            addBlacksmithItem(var2, Item.bootsChain.itemID, this.rand, this.func_82188_j(0.1F));
            addBlacksmithItem(var2, Item.helmetChain.itemID, this.rand, this.func_82188_j(0.1F));
            addBlacksmithItem(var2, Item.plateChain.itemID, this.rand, this.func_82188_j(0.1F));
            addBlacksmithItem(var2, Item.legsChain.itemID, this.rand, this.func_82188_j(0.1F));
            break;
         case 4:
            addMerchantItem(var2, Item.coal.itemID, this.rand, this.func_82188_j(0.7F));
            addMerchantItem(var2, Item.porkRaw.itemID, this.rand, this.func_82188_j(0.5F));
            addMerchantItem(var2, Item.beefRaw.itemID, this.rand, this.func_82188_j(0.5F));
            addBlacksmithItem(var2, Item.saddle.itemID, this.rand, this.func_82188_j(0.1F));
            addBlacksmithItem(var2, Item.plateLeather.itemID, this.rand, this.func_82188_j(0.3F));
            addBlacksmithItem(var2, Item.bootsLeather.itemID, this.rand, this.func_82188_j(0.3F));
            addBlacksmithItem(var2, Item.helmetLeather.itemID, this.rand, this.func_82188_j(0.3F));
            addBlacksmithItem(var2, Item.legsLeather.itemID, this.rand, this.func_82188_j(0.3F));
            addBlacksmithItem(var2, Item.porkCooked.itemID, this.rand, this.func_82188_j(0.3F));
            addBlacksmithItem(var2, Item.beefCooked.itemID, this.rand, this.func_82188_j(0.3F));
      }

      if (var2.isEmpty()) {
         addMerchantItem(var2, Item.ingotGold.itemID, this.rand, 1.0F);
      }

      Collections.shuffle(var2);
      if (this.buyingList == null) {
         this.buyingList = new MerchantRecipeList();
      }

      for (int var9 = 0; var9 < par1 && var9 < var2.size(); var9++) {
         this.buyingList.addToListWithCheck((MerchantRecipe)var2.get(var9));
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void setRecipes(MerchantRecipeList par1MerchantRecipeList) {
   }

   private static void addMerchantItem(MerchantRecipeList par0MerchantRecipeList, int par1, Random par2Random, float par3) {
      if (par2Random.nextFloat() < par3) {
         par0MerchantRecipeList.add(new MerchantRecipe(getRandomSizedStack(par1, par2Random), Item.emerald));
      }
   }

   private static ItemStack getRandomSizedStack(int par0, Random par1Random) {
      return new ItemStack(par0, getRandomCountForItem(par0, par1Random), 0);
   }

   private static int getRandomCountForItem(int par0, Random par1Random) {
      Tuple var2 = (Tuple)villagerStockList.get(par0);
      return var2 == null
         ? 1
         : (
            var2.getFirst() >= var2.getSecond()
               ? (Integer)var2.getFirst()
               : (Integer)var2.getFirst() + par1Random.nextInt((Integer)var2.getSecond() - (Integer)var2.getFirst())
         );
   }

   private static void addBlacksmithItem(MerchantRecipeList par0MerchantRecipeList, int par1, Random par2Random, float par3) {
      if (par2Random.nextFloat() < par3) {
         int var4 = getRandomCountForBlacksmithItem(par1, par2Random);
         ItemStack var5;
         ItemStack var6;
         if (var4 < 0) {
            var5 = new ItemStack(Item.emerald.itemID, 1, 0);
            var6 = new ItemStack(par1, -var4, 0);
         } else {
            var5 = new ItemStack(Item.emerald.itemID, var4, 0);
            var6 = new ItemStack(par1, 1, 0);
         }

         par0MerchantRecipeList.add(new MerchantRecipe(var5, var6));
      }
   }

   private static int getRandomCountForBlacksmithItem(int par0, Random par1Random) {
      Tuple var2 = (Tuple)blacksmithSellingList.get(par0);
      return var2 == null
         ? 1
         : (
            var2.getFirst() >= var2.getSecond()
               ? (Integer)var2.getFirst()
               : (Integer)var2.getFirst() + par1Random.nextInt((Integer)var2.getSecond() - (Integer)var2.getFirst())
         );
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void handleHealthUpdate(byte par1) {
      if (par1 == 12) {
         this.generateRandomParticles("heart");
      } else if (par1 == 13) {
         this.generateRandomParticles("angryVillager");
      } else if (par1 == 14) {
         this.generateRandomParticles("happyVillager");
      } else {
         super.a(par1);
      }
   }

   @Environment(EnvType.CLIENT)
   private void generateRandomParticles(String par1Str) {
      for (int var2 = 0; var2 < 5; var2++) {
         double var3 = this.rand.nextGaussian() * 0.02;
         double var5 = this.rand.nextGaussian() * 0.02;
         double var7 = this.rand.nextGaussian() * 0.02;
         this.worldObj
            .spawnParticle(
               par1Str,
               this.posX + this.rand.nextFloat() * this.width * 2.0F - this.width,
               this.posY + 1.0 + this.rand.nextFloat() * this.height,
               this.posZ + this.rand.nextFloat() * this.width * 2.0F - this.width,
               var3,
               var5,
               var7
            );
      }
   }

   @Override
   public void initCreature() {
      this.setProfession(this.worldObj.rand.nextInt(5));
   }

   public void func_82187_q() {
      this.field_82190_bM = true;
   }

   public EntityVillager func_90012_b(EntityAgeable par1EntityAgeable) {
      EntityVillager var2 = (EntityVillager)EntityList.createEntityOfType(EntityVillager.class, this.worldObj);
      var2.initCreature();
      return var2;
   }

   @Override
   public EntityAgeable createChild(EntityAgeable par1EntityAgeable) {
      return this.func_90012_b(par1EntityAgeable);
   }

   @Override
   public int getCurrentTradeLevel() {
      return 0;
   }

   @Override
   public int getCurrentTradeXP() {
      return 0;
   }

   @Override
   public int getCurrentTradeMaxXP() {
      return 0;
   }

   static {
      villagerStockList.put(Item.coal.itemID, new Tuple(16, 24));
      villagerStockList.put(Item.ingotIron.itemID, new Tuple(8, 10));
      villagerStockList.put(Item.ingotGold.itemID, new Tuple(8, 10));
      villagerStockList.put(Item.diamond.itemID, new Tuple(4, 6));
      villagerStockList.put(Item.paper.itemID, new Tuple(24, 36));
      villagerStockList.put(Item.book.itemID, new Tuple(11, 13));
      villagerStockList.put(Item.writtenBook.itemID, new Tuple(1, 1));
      villagerStockList.put(Item.enderPearl.itemID, new Tuple(3, 4));
      villagerStockList.put(Item.eyeOfEnder.itemID, new Tuple(2, 3));
      villagerStockList.put(Item.porkRaw.itemID, new Tuple(14, 18));
      villagerStockList.put(Item.beefRaw.itemID, new Tuple(14, 18));
      villagerStockList.put(Item.chickenRaw.itemID, new Tuple(14, 18));
      villagerStockList.put(Item.fishCooked.itemID, new Tuple(9, 13));
      villagerStockList.put(Item.seeds.itemID, new Tuple(34, 48));
      villagerStockList.put(Item.melonSeeds.itemID, new Tuple(30, 38));
      villagerStockList.put(Item.pumpkinSeeds.itemID, new Tuple(30, 38));
      villagerStockList.put(Item.wheat.itemID, new Tuple(18, 22));
      villagerStockList.put(Block.cloth.blockID, new Tuple(14, 22));
      villagerStockList.put(Item.rottenFlesh.itemID, new Tuple(36, 64));
      blacksmithSellingList.put(Item.flintAndSteel.itemID, new Tuple(3, 4));
      blacksmithSellingList.put(Item.shears.itemID, new Tuple(3, 4));
      blacksmithSellingList.put(Item.swordIron.itemID, new Tuple(7, 11));
      blacksmithSellingList.put(Item.swordDiamond.itemID, new Tuple(12, 14));
      blacksmithSellingList.put(Item.axeIron.itemID, new Tuple(6, 8));
      blacksmithSellingList.put(Item.axeDiamond.itemID, new Tuple(9, 12));
      blacksmithSellingList.put(Item.pickaxeIron.itemID, new Tuple(7, 9));
      blacksmithSellingList.put(Item.pickaxeDiamond.itemID, new Tuple(10, 12));
      blacksmithSellingList.put(Item.shovelIron.itemID, new Tuple(4, 6));
      blacksmithSellingList.put(Item.shovelDiamond.itemID, new Tuple(7, 8));
      blacksmithSellingList.put(Item.hoeIron.itemID, new Tuple(4, 6));
      blacksmithSellingList.put(Item.hoeDiamond.itemID, new Tuple(7, 8));
      blacksmithSellingList.put(Item.bootsIron.itemID, new Tuple(4, 6));
      blacksmithSellingList.put(Item.bootsDiamond.itemID, new Tuple(7, 8));
      blacksmithSellingList.put(Item.helmetIron.itemID, new Tuple(4, 6));
      blacksmithSellingList.put(Item.helmetDiamond.itemID, new Tuple(7, 8));
      blacksmithSellingList.put(Item.plateIron.itemID, new Tuple(10, 14));
      blacksmithSellingList.put(Item.plateDiamond.itemID, new Tuple(16, 19));
      blacksmithSellingList.put(Item.legsIron.itemID, new Tuple(8, 10));
      blacksmithSellingList.put(Item.legsDiamond.itemID, new Tuple(11, 14));
      blacksmithSellingList.put(Item.bootsChain.itemID, new Tuple(5, 7));
      blacksmithSellingList.put(Item.helmetChain.itemID, new Tuple(5, 7));
      blacksmithSellingList.put(Item.plateChain.itemID, new Tuple(11, 15));
      blacksmithSellingList.put(Item.legsChain.itemID, new Tuple(9, 11));
      blacksmithSellingList.put(Item.bread.itemID, new Tuple(-4, -2));
      blacksmithSellingList.put(Item.melon.itemID, new Tuple(-8, -4));
      blacksmithSellingList.put(Item.appleRed.itemID, new Tuple(-8, -4));
      blacksmithSellingList.put(Item.cookie.itemID, new Tuple(-10, -7));
      blacksmithSellingList.put(Block.glass.blockID, new Tuple(-5, -3));
      blacksmithSellingList.put(Block.bookShelf.blockID, new Tuple(3, 4));
      blacksmithSellingList.put(Item.plateLeather.itemID, new Tuple(4, 5));
      blacksmithSellingList.put(Item.bootsLeather.itemID, new Tuple(2, 4));
      blacksmithSellingList.put(Item.helmetLeather.itemID, new Tuple(2, 4));
      blacksmithSellingList.put(Item.legsLeather.itemID, new Tuple(2, 4));
      blacksmithSellingList.put(Item.saddle.itemID, new Tuple(6, 8));
      blacksmithSellingList.put(Item.expBottle.itemID, new Tuple(-4, -1));
      blacksmithSellingList.put(Item.redstone.itemID, new Tuple(-4, -1));
      blacksmithSellingList.put(Item.compass.itemID, new Tuple(10, 12));
      blacksmithSellingList.put(Item.pocketSundial.itemID, new Tuple(10, 12));
      blacksmithSellingList.put(Block.glowStone.blockID, new Tuple(-3, -1));
      blacksmithSellingList.put(Item.porkCooked.itemID, new Tuple(-7, -5));
      blacksmithSellingList.put(Item.beefCooked.itemID, new Tuple(-7, -5));
      blacksmithSellingList.put(Item.chickenCooked.itemID, new Tuple(-8, -6));
      blacksmithSellingList.put(Item.eyeOfEnder.itemID, new Tuple(7, 11));
      blacksmithSellingList.put(Item.arrow.itemID, new Tuple(-12, -8));
   }
}
