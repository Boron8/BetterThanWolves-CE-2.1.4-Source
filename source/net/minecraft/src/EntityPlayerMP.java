package net.minecraft.src;

import btw.BTWMod;
import btw.block.tileentity.beacon.MagneticPoint;
import btw.entity.LightningBoltEntity;
import btw.inventory.container.WorkbenchContainer;
import btw.item.BTWItems;
import btw.network.packet.HardcoreSpawnPacket;
import btw.util.CustomDamageSource;
import btw.util.status.BTWStatusCategory;
import btw.util.status.StatusEffect;
import btw.world.util.WorldUtils;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import net.minecraft.server.MinecraftServer;

public class EntityPlayerMP extends EntityPlayer implements ICrafting {
   private StringTranslate translator = new StringTranslate("en_US");
   public NetServerHandler playerNetServerHandler;
   public MinecraftServer mcServer;
   public ItemInWorldManager theItemInWorldManager;
   public double managedPosX;
   public double managedPosZ;
   public final List loadedChunks = new LinkedList();
   public final List destroyedItemsNetCache = new LinkedList();
   private int lastHealth = -99999999;
   private int lastFoodLevel = -99999999;
   private boolean wasHungry = true;
   private int lastExperience = -99999999;
   private int initialInvulnerability = 60;
   private int renderDistance = 0;
   private int chatVisibility = 0;
   private boolean chatColours = true;
   private int currentWindowId = 0;
   public boolean playerInventoryBeingManipulated;
   public int ping;
   public boolean playerConqueredTheEnd = false;
   public LinkedList<ChunkCoordIntPair> chunksToBeSentToClient = new LinkedList<>();
   private int lastFoodSaturation = -99999999;
   private int exhaustionWithTimeCounter = 0;
   private static final int exhaustionWithTimePeriod = 600;
   private static final float exhaustionWithTimeAmount = 0.5F;
   private static final float minimumGloomBiteChance = 0.01F;
   private static final float maximumGloomBiteChance = 0.05F;
   private static final int delayBetweenZeroDamageAttackSounds = 20;
   private long timeOfLastZeroDamageAttackSound = 0L;

   public EntityPlayerMP(MinecraftServer par1MinecraftServer, World par2World, String par3Str, ItemInWorldManager par4ItemInWorldManager) {
      super(par2World);
      par4ItemInWorldManager.thisPlayerMP = this;
      this.theItemInWorldManager = par4ItemInWorldManager;
      this.renderDistance = par1MinecraftServer.getConfigurationManager().getViewDistance();
      ChunkCoordinates var5 = par2World.getSpawnPoint();
      int var6 = var5.posX;
      int var7 = var5.posZ;
      int var8 = var5.posY;
      if (!par2World.provider.hasNoSky && par2World.getWorldInfo().getGameType() != EnumGameType.ADVENTURE) {
         int var9 = Math.max(5, par1MinecraftServer.getSpawnProtectionSize() - 6);
         var6 += this.rand.nextInt(var9 * 2) - var9;
         var7 += this.rand.nextInt(var9 * 2) - var9;
         var8 = par2World.getTopSolidOrLiquidBlock(var6, var7);
      }

      this.mcServer = par1MinecraftServer;
      this.stepHeight = 0.0F;
      this.username = par3Str;
      this.yOffset = 0.0F;
      this.b(var6 + 0.5, var8, var7 + 0.5, 0.0F, 0.0F);

      while (!par2World.getCollidingBoundingBoxes(this, this.boundingBox).isEmpty()) {
         this.b(this.posX, this.posY + 1.0, this.posZ);
      }
   }

   @Override
   public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound) {
      super.readEntityFromNBT(par1NBTTagCompound);
      if (par1NBTTagCompound.hasKey("playerGameType")) {
         if (MinecraftServer.getServer().func_104056_am()) {
            this.theItemInWorldManager.setGameType(MinecraftServer.getServer().getGameType());
         } else {
            this.theItemInWorldManager.setGameType(EnumGameType.getByID(par1NBTTagCompound.getInteger("playerGameType")));
         }
      }
   }

   @Override
   public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound) {
      super.writeEntityToNBT(par1NBTTagCompound);
      par1NBTTagCompound.setInteger("playerGameType", this.theItemInWorldManager.getGameType().getID());
   }

   @Override
   public void addExperienceLevel(int par1) {
      super.addExperienceLevel(par1);
      this.lastExperience = -1;
   }

   public void addSelfToInternalCraftingInventory() {
      this.openContainer.addCraftingToCrafters(this);
   }

   @Override
   protected void resetHeight() {
      this.yOffset = 0.0F;
   }

   @Override
   public float getEyeHeight() {
      return this.sleeping ? 0.18F : 1.62F;
   }

   @Override
   public void onUpdate() {
      this.theItemInWorldManager.updateBlockRemoving();
      this.initialInvulnerability--;
      this.openContainer.detectAndSendChanges();

      while (!this.destroyedItemsNetCache.isEmpty()) {
         int var1 = Math.min(this.destroyedItemsNetCache.size(), 127);
         int[] var2 = new int[var1];
         Iterator var3 = this.destroyedItemsNetCache.iterator();
         int var4 = 0;

         while (var3.hasNext() && var4 < var1) {
            var2[var4++] = (Integer)var3.next();
            var3.remove();
         }

         this.playerNetServerHandler.sendPacketToPlayer(new Packet29DestroyEntity(var2));
      }

      this.sendChunksToClient();
      this.modSpecificOnUpdate();
   }

   @Override
   public void setEntityHealth(int par1) {
      super.b(par1);

      for (ScoreObjective var4 : this.cr().func_96520_a(ScoreObjectiveCriteria.field_96638_f)) {
         this.cr().func_96529_a(this.am(), var4).func_96651_a(Arrays.asList(this));
      }
   }

   public void onUpdateEntity() {
      try {
         super.onUpdate();

         for (int var1 = 0; var1 < this.inventory.getSizeInventory(); var1++) {
            ItemStack var5 = this.inventory.getStackInSlot(var1);
            if (var5 != null && Item.itemsList[var5.itemID].isMap() && this.playerNetServerHandler.packetSize() <= 5) {
               Packet var6 = ((ItemMapBase)Item.itemsList[var5.itemID]).createMapDataPacket(var5, this.worldObj, this);
               if (var6 != null) {
                  this.playerNetServerHandler.sendPacketToPlayer(var6);
               }
            }
         }

         if (this.aX() != this.lastHealth
            || this.lastFoodLevel != this.foodStats.getFoodLevel()
            || this.foodStats.getSaturationLevel() == 0.0F != this.wasHungry
            || this.lastFoodSaturation != (int)(this.foodStats.getSaturationLevel() * 8.0F)) {
            this.playerNetServerHandler
               .sendPacketToPlayer(new Packet8UpdateHealth(this.aX(), this.foodStats.getFoodLevel(), this.foodStats.getSaturationLevel()));
            this.lastHealth = this.aX();
            this.lastFoodLevel = this.foodStats.getFoodLevel();
            this.wasHungry = this.foodStats.getSaturationLevel() == 0.0F;
            this.lastFoodSaturation = (int)(this.foodStats.getSaturationLevel() * 8.0F);
         }

         if (this.experienceTotal != this.lastExperience) {
            this.lastExperience = this.experienceTotal;
            this.playerNetServerHandler.sendPacketToPlayer(new Packet43Experience(this.experience, this.experienceTotal, this.experienceLevel));
         }
      } catch (Throwable var41) {
         CrashReport var2 = CrashReport.makeCrashReport(var41, "Ticking player");
         CrashReportCategory var3 = var2.makeCategory("Player being ticked");
         this.a(var3);
         throw new ReportedException(var2);
      }
   }

   @Override
   public void onDeath(DamageSource par1DamageSource) {
      this.entityLivingOnDeath(par1DamageSource);
      this.mcServer.getConfigurationManager().sendChatMsg(this.field_94063_bt.func_94546_b());
      if (!this.worldObj.getGameRules().getGameRuleBooleanValue("keepInventory")) {
         this.inventory.dropAllItems();
      }

      this.customModDrops(par1DamageSource);

      for (ScoreObjective var4 : this.worldObj.getScoreboard().func_96520_a(ScoreObjectiveCriteria.field_96642_c)) {
         Score var5 = this.cr().func_96529_a(this.am(), var4);
         var5.func_96648_a();
      }

      EntityLiving var6 = this.bN();
      if (var6 != null) {
         var6.c(this, this.scoreValue);
      }
   }

   @Override
   public boolean attackEntityFrom(DamageSource par1DamageSource, int par2) {
      if (this.aq()) {
         return false;
      } else {
         boolean var3 = this.mcServer.isDedicatedServer() && this.mcServer.isPVPEnabled() && "fall".equals(par1DamageSource.damageType);
         if (!var3 && this.initialInvulnerability > 0 && par1DamageSource != DamageSource.outOfWorld) {
            return false;
         } else {
            if (par1DamageSource instanceof EntityDamageSource) {
               Entity var4 = par1DamageSource.getEntity();
               if (var4 instanceof EntityPlayer && !this.func_96122_a((EntityPlayer)var4)) {
                  return false;
               }

               if (var4 instanceof EntityArrow) {
                  EntityArrow var5 = (EntityArrow)var4;
                  if (var5.shootingEntity instanceof EntityPlayer && !this.func_96122_a((EntityPlayer)var5.shootingEntity)) {
                     return false;
                  }
               }
            }

            return super.attackEntityFrom(par1DamageSource, par2);
         }
      }
   }

   @Override
   public boolean func_96122_a(EntityPlayer par1EntityPlayer) {
      return !this.mcServer.isPVPEnabled() ? false : super.func_96122_a(par1EntityPlayer);
   }

   @Override
   public void travelToDimension(int par1) {
      if (this.dimension == 1 && par1 == 1) {
         this.a(AchievementList.theEnd2);
         this.worldObj.removeEntity(this);
         this.playerConqueredTheEnd = true;
         this.playerNetServerHandler.sendPacketToPlayer(new Packet70GameEvent(4, 0));
      } else {
         if (this.dimension == 1 && par1 == 0) {
            this.a(AchievementList.theEnd);
            ChunkCoordinates var2 = this.mcServer.worldServerForDimension(par1).getEntrancePortalLocation();
            if (var2 != null) {
               this.playerNetServerHandler.setPlayerLocation(var2.posX, var2.posY, var2.posZ, 0.0F, 0.0F);
            }

            par1 = 1;
         } else {
            this.a(AchievementList.portal);
         }

         this.mcServer.getConfigurationManager().transferPlayerToDimension(this, par1);
         this.lastExperience = -1;
         this.lastHealth = -1;
         this.lastFoodLevel = -1;
      }
   }

   private void sendTileEntityToPlayer(TileEntity par1TileEntity) {
      if (par1TileEntity != null) {
         Packet var2 = par1TileEntity.getDescriptionPacket();
         if (var2 != null) {
            this.playerNetServerHandler.sendPacketToPlayer(var2);
         }
      }
   }

   @Override
   public void onItemPickup(Entity par1Entity, int par2) {
      super.a(par1Entity, par2);
      this.openContainer.detectAndSendChanges();
   }

   @Override
   public EnumStatus sleepInBedAt(int par1, int par2, int par3) {
      EnumStatus var4 = super.sleepInBedAt(par1, par2, par3);
      if (var4 == EnumStatus.OK) {
         Packet17Sleep var5 = new Packet17Sleep(this, 0, par1, par2, par3);
         this.getServerForPlayer().getEntityTracker().sendPacketToAllPlayersTrackingEntity(this, var5);
         this.playerNetServerHandler.setPlayerLocation(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
         this.playerNetServerHandler.sendPacketToPlayer(var5);
      }

      return var4;
   }

   @Override
   public void wakeUpPlayer(boolean par1, boolean par2, boolean par3) {
      if (this.bz()) {
         this.getServerForPlayer().getEntityTracker().sendPacketToAllAssociatedPlayers(this, new Packet18Animation(this, 3));
      }

      super.wakeUpPlayer(par1, par2, par3);
      if (this.playerNetServerHandler != null) {
         this.playerNetServerHandler.setPlayerLocation(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
      }
   }

   @Override
   public void mountEntity(Entity par1Entity) {
      super.mountEntity(par1Entity);
      this.playerNetServerHandler.sendPacketToPlayer(new Packet39AttachEntity(this, this.ridingEntity));
      this.playerNetServerHandler.setPlayerLocation(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
   }

   @Override
   protected void updateFallState(double par1, boolean par3) {
   }

   public void updateFlyingState(double par1, boolean par3) {
      super.a(par1, par3);
   }

   private void incrementWindowID() {
      this.currentWindowId = this.currentWindowId % 100 + 1;
   }

   @Override
   public void displayGUIWorkbench(int par1, int par2, int par3) {
      this.incrementWindowID();
      this.playerNetServerHandler.sendPacketToPlayer(new Packet100OpenWindow(this.currentWindowId, 1, "Crafting", 9, true));
      this.openContainer = new WorkbenchContainer(this.inventory, this.worldObj, par1, par2, par3);
      this.openContainer.windowId = this.currentWindowId;
      this.openContainer.addCraftingToCrafters(this);
   }

   @Override
   public void displayGUIEnchantment(int par1, int par2, int par3, String par4Str) {
      this.incrementWindowID();
      this.playerNetServerHandler.sendPacketToPlayer(new Packet100OpenWindow(this.currentWindowId, 4, par4Str == null ? "" : par4Str, 9, par4Str != null));
      this.openContainer = new ContainerEnchantment(this.inventory, this.worldObj, par1, par2, par3);
      this.openContainer.windowId = this.currentWindowId;
      this.openContainer.addCraftingToCrafters(this);
   }

   @Override
   public void displayGUIAnvil(int par1, int par2, int par3) {
      this.incrementWindowID();
      this.playerNetServerHandler.sendPacketToPlayer(new Packet100OpenWindow(this.currentWindowId, 8, "Repairing", 9, true));
      this.openContainer = new ContainerRepair(this.inventory, this.worldObj, par1, par2, par3, this);
      this.openContainer.windowId = this.currentWindowId;
      this.openContainer.addCraftingToCrafters(this);
   }

   @Override
   public void displayGUIChest(IInventory par1IInventory) {
      if (this.openContainer != this.inventoryContainer) {
         this.closeScreen();
      }

      this.incrementWindowID();
      this.playerNetServerHandler
         .sendPacketToPlayer(
            new Packet100OpenWindow(
               this.currentWindowId, 0, par1IInventory.getInvName(), par1IInventory.getSizeInventory(), par1IInventory.isInvNameLocalized()
            )
         );
      this.openContainer = new ContainerChest(this.inventory, par1IInventory);
      this.openContainer.windowId = this.currentWindowId;
      this.openContainer.addCraftingToCrafters(this);
   }

   @Override
   public void displayGUIHopper(TileEntityHopper par1TileEntityHopper) {
      this.incrementWindowID();
      this.playerNetServerHandler
         .sendPacketToPlayer(
            new Packet100OpenWindow(
               this.currentWindowId, 9, par1TileEntityHopper.getInvName(), par1TileEntityHopper.getSizeInventory(), par1TileEntityHopper.isInvNameLocalized()
            )
         );
      this.openContainer = new ContainerHopper(this.inventory, par1TileEntityHopper);
      this.openContainer.windowId = this.currentWindowId;
      this.openContainer.addCraftingToCrafters(this);
   }

   @Override
   public void displayGUIHopperMinecart(EntityMinecartHopper par1EntityMinecartHopper) {
      this.incrementWindowID();
      this.playerNetServerHandler
         .sendPacketToPlayer(
            new Packet100OpenWindow(
               this.currentWindowId, 9, par1EntityMinecartHopper.b(), par1EntityMinecartHopper.getSizeInventory(), par1EntityMinecartHopper.c()
            )
         );
      this.openContainer = new ContainerHopper(this.inventory, par1EntityMinecartHopper);
      this.openContainer.windowId = this.currentWindowId;
      this.openContainer.addCraftingToCrafters(this);
   }

   @Override
   public void displayGUIFurnace(TileEntityFurnace par1TileEntityFurnace) {
      this.incrementWindowID();
      this.playerNetServerHandler
         .sendPacketToPlayer(
            new Packet100OpenWindow(
               this.currentWindowId,
               2,
               par1TileEntityFurnace.getInvName(),
               par1TileEntityFurnace.getSizeInventory(),
               par1TileEntityFurnace.isInvNameLocalized()
            )
         );
      this.openContainer = new ContainerFurnace(this.inventory, par1TileEntityFurnace);
      this.openContainer.windowId = this.currentWindowId;
      this.openContainer.addCraftingToCrafters(this);
   }

   @Override
   public void displayGUIDispenser(TileEntityDispenser par1TileEntityDispenser) {
      this.incrementWindowID();
      this.playerNetServerHandler
         .sendPacketToPlayer(
            new Packet100OpenWindow(
               this.currentWindowId,
               par1TileEntityDispenser instanceof TileEntityDropper ? 10 : 3,
               par1TileEntityDispenser.getInvName(),
               par1TileEntityDispenser.getSizeInventory(),
               par1TileEntityDispenser.isInvNameLocalized()
            )
         );
      this.openContainer = new ContainerDispenser(this.inventory, par1TileEntityDispenser);
      this.openContainer.windowId = this.currentWindowId;
      this.openContainer.addCraftingToCrafters(this);
   }

   @Override
   public void displayGUIBrewingStand(TileEntityBrewingStand par1TileEntityBrewingStand) {
      this.incrementWindowID();
      this.playerNetServerHandler
         .sendPacketToPlayer(
            new Packet100OpenWindow(
               this.currentWindowId,
               5,
               par1TileEntityBrewingStand.getInvName(),
               par1TileEntityBrewingStand.getSizeInventory(),
               par1TileEntityBrewingStand.isInvNameLocalized()
            )
         );
      this.openContainer = new ContainerBrewingStand(this.inventory, par1TileEntityBrewingStand);
      this.openContainer.windowId = this.currentWindowId;
      this.openContainer.addCraftingToCrafters(this);
   }

   @Override
   public void displayGUIBeacon(TileEntityBeacon par1TileEntityBeacon) {
      this.incrementWindowID();
      this.playerNetServerHandler
         .sendPacketToPlayer(
            new Packet100OpenWindow(
               this.currentWindowId, 7, par1TileEntityBeacon.getInvName(), par1TileEntityBeacon.getSizeInventory(), par1TileEntityBeacon.isInvNameLocalized()
            )
         );
      this.openContainer = new ContainerBeacon(this.inventory, par1TileEntityBeacon);
      this.openContainer.windowId = this.currentWindowId;
      this.openContainer.addCraftingToCrafters(this);
   }

   @Override
   public void displayGUIMerchant(IMerchant par1IMerchant, String par2Str) {
      this.incrementWindowID();
      this.openContainer = new ContainerMerchant(this.inventory, par1IMerchant, this.worldObj);
      this.openContainer.windowId = this.currentWindowId;
      InventoryMerchant var3 = ((ContainerMerchant)this.openContainer).getMerchantInventory();
      this.playerNetServerHandler
         .sendPacketToPlayer(new Packet100OpenWindow(this.currentWindowId, 6, par2Str == null ? "" : par2Str, var3.getSizeInventory(), par2Str != null));
      this.openContainer.addCraftingToCrafters(this);
      MerchantRecipeList var4 = par1IMerchant.getRecipes(this);
      if (var4 != null) {
         try {
            ByteArrayOutputStream var5 = new ByteArrayOutputStream();
            DataOutputStream var6 = new DataOutputStream(var5);
            var6.writeInt(this.currentWindowId);
            var4.writeRecipiesToStream(var6);
            this.playerNetServerHandler.sendPacketToPlayer(new Packet250CustomPayload("MC|TrList", var5.toByteArray()));
         } catch (IOException var71) {
            var71.printStackTrace();
         }
      }
   }

   @Override
   public void sendSlotContents(Container par1Container, int par2, ItemStack par3ItemStack) {
      if (!(par1Container.getSlot(par2) instanceof SlotCrafting) && !this.playerInventoryBeingManipulated) {
         this.playerNetServerHandler.sendPacketToPlayer(new Packet103SetSlot(par1Container.windowId, par2, par3ItemStack));
      }
   }

   public void sendContainerToPlayer(Container par1Container) {
      this.sendContainerAndContentsToPlayer(par1Container, par1Container.getInventory());
   }

   @Override
   public void sendContainerAndContentsToPlayer(Container par1Container, List par2List) {
      this.playerNetServerHandler.sendPacketToPlayer(new Packet104WindowItems(par1Container.windowId, par2List));
      this.playerNetServerHandler.sendPacketToPlayer(new Packet103SetSlot(-1, -1, this.inventory.getItemStack()));
   }

   @Override
   public void sendProgressBarUpdate(Container par1Container, int par2, int par3) {
      this.playerNetServerHandler.sendPacketToPlayer(new Packet105UpdateProgressbar(par1Container.windowId, par2, par3));
   }

   @Override
   public void closeScreen() {
      this.playerNetServerHandler.sendPacketToPlayer(new Packet101CloseWindow(this.openContainer.windowId));
      this.closeInventory();
   }

   public void updateHeldItem() {
      if (!this.playerInventoryBeingManipulated) {
         this.playerNetServerHandler.sendPacketToPlayer(new Packet103SetSlot(-1, -1, this.inventory.getItemStack()));
      }
   }

   public void closeInventory() {
      this.openContainer.onCraftGuiClosed(this);
      this.openContainer = this.inventoryContainer;
   }

   @Override
   public void addStat(StatBase par1StatBase, int par2) {
      if (par1StatBase != null && !par1StatBase.isIndependent) {
         while (par2 > 100) {
            this.playerNetServerHandler.sendPacketToPlayer(new Packet200Statistic(par1StatBase.statId, 100));
            par2 -= 100;
         }

         this.playerNetServerHandler.sendPacketToPlayer(new Packet200Statistic(par1StatBase.statId, par2));
      }
   }

   public void mountEntityAndWakeUp() {
      if (this.riddenByEntity != null) {
         this.riddenByEntity.mountEntity(this);
      }

      if (this.sleeping) {
         this.wakeUpPlayer(true, false, false);
      }
   }

   public void setPlayerHealthUpdated() {
      this.lastHealth = -99999999;
   }

   @Override
   public void addChatMessage(String par1Str) {
      StringTranslate var2 = StringTranslate.getInstance();
      String var3 = var2.translateKey(par1Str);
      this.playerNetServerHandler.sendPacketToPlayer(new Packet3Chat(var3));
   }

   @Override
   protected void onItemUseFinish() {
      this.playerNetServerHandler.sendPacketToPlayer(new Packet38EntityStatus(this.entityId, (byte)9));
      super.onItemUseFinish();
   }

   @Override
   public void setItemInUse(ItemStack par1ItemStack, int par2) {
      super.setItemInUse(par1ItemStack, par2);
      if (par1ItemStack != null && par1ItemStack.getItem() != null && par1ItemStack.getItem().getItemUseAction(par1ItemStack) == EnumAction.eat) {
         this.getServerForPlayer().getEntityTracker().sendPacketToAllAssociatedPlayers(this, new Packet18Animation(this, 5));
      }
   }

   @Override
   public void clonePlayer(EntityPlayer par1EntityPlayer, boolean par2) {
      super.clonePlayer(par1EntityPlayer, par2);
      this.lastExperience = -1;
      this.lastHealth = -1;
      this.lastFoodLevel = -1;
      this.destroyedItemsNetCache.addAll(((EntityPlayerMP)par1EntityPlayer).destroyedItemsNetCache);
   }

   @Override
   protected void onNewPotionEffect(PotionEffect par1PotionEffect) {
      super.a(par1PotionEffect);
      this.playerNetServerHandler.sendPacketToPlayer(new Packet41EntityEffect(this.entityId, par1PotionEffect));
   }

   @Override
   protected void onChangedPotionEffect(PotionEffect par1PotionEffect) {
      super.b(par1PotionEffect);
      this.playerNetServerHandler.sendPacketToPlayer(new Packet41EntityEffect(this.entityId, par1PotionEffect));
   }

   @Override
   protected void onFinishedPotionEffect(PotionEffect par1PotionEffect) {
      super.c(par1PotionEffect);
      this.playerNetServerHandler.sendPacketToPlayer(new Packet42RemoveEntityEffect(this.entityId, par1PotionEffect));
   }

   @Override
   public void setPositionAndUpdate(double par1, double par3, double par5) {
      this.playerNetServerHandler.setPlayerLocation(par1, par3, par5, this.rotationYaw, this.rotationPitch);
   }

   @Override
   public void onCriticalHit(Entity par1Entity) {
      this.getServerForPlayer().getEntityTracker().sendPacketToAllAssociatedPlayers(this, new Packet18Animation(par1Entity, 6));
   }

   @Override
   public void onEnchantmentCritical(Entity par1Entity) {
      this.getServerForPlayer().getEntityTracker().sendPacketToAllAssociatedPlayers(this, new Packet18Animation(par1Entity, 7));
   }

   @Override
   public void sendPlayerAbilities() {
      if (this.playerNetServerHandler != null) {
         this.playerNetServerHandler.sendPacketToPlayer(new Packet202PlayerAbilities(this.capabilities));
      }
   }

   public WorldServer getServerForPlayer() {
      return (WorldServer)this.worldObj;
   }

   @Override
   public void setGameType(EnumGameType par1EnumGameType) {
      this.theItemInWorldManager.setGameType(par1EnumGameType);
      this.playerNetServerHandler.sendPacketToPlayer(new Packet70GameEvent(3, par1EnumGameType.getID()));
   }

   @Override
   public void sendChatToPlayer(String par1Str) {
      this.playerNetServerHandler.sendPacketToPlayer(new Packet3Chat(par1Str));
   }

   @Override
   public boolean canCommandSenderUseCommand(int par1, String par2Str) {
      return "seed".equals(par2Str) && !this.mcServer.isDedicatedServer()
         ? true
         : (
            !"tell".equals(par2Str) && !"help".equals(par2Str) && !"me".equals(par2Str)
               ? this.mcServer.getConfigurationManager().areCommandsAllowed(this.username)
               : true
         );
   }

   public String getPlayerIP() {
      String var1 = this.playerNetServerHandler.netManager.getSocketAddress().toString();
      var1 = var1.substring(var1.indexOf("/") + 1);
      return var1.substring(0, var1.indexOf(":"));
   }

   public void updateClientInfo(Packet204ClientInfo par1Packet204ClientInfo) {
      if (this.translator.getLanguageList().containsKey(par1Packet204ClientInfo.getLanguage())) {
         this.translator.setLanguage(par1Packet204ClientInfo.getLanguage(), false);
      }

      int var2 = 256 >> par1Packet204ClientInfo.getRenderDistance();
      if (var2 > 3 && var2 < 15) {
         this.renderDistance = var2;
      }

      this.chatVisibility = par1Packet204ClientInfo.getChatVisibility();
      this.chatColours = par1Packet204ClientInfo.getChatColours();
      if (this.mcServer.isSinglePlayer() && this.mcServer.getServerOwner().equals(this.username)) {
      }

      this.b(1, !par1Packet204ClientInfo.getShowCape());
   }

   @Override
   public StringTranslate getTranslator() {
      return this.translator;
   }

   public int getChatVisibility() {
      return this.chatVisibility;
   }

   public void requestTexturePackLoad(String par1Str, int par2) {
      String var3 = par1Str + "\u0000" + par2;
      this.playerNetServerHandler.sendPacketToPlayer(new Packet250CustomPayload("MC|TPack", var3.getBytes()));
   }

   @Override
   public ChunkCoordinates getPlayerCoordinates() {
      return new ChunkCoordinates(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY + 0.5), MathHelper.floor_double(this.posZ));
   }

   @Override
   protected void checkForHeadDrop(DamageSource source, int iLootingModifier) {
      Entity sourceEntity = source.getEntity();
      int iHeadChance = this.rand.nextInt(200);
      iHeadChance -= iLootingModifier;
      if (sourceEntity instanceof EntityPlayer) {
         if (((EntityPlayer)sourceEntity).getHeldItem() != null && ((EntityPlayer)sourceEntity).getHeldItem().getItem().itemID == BTWItems.battleaxe.itemID) {
            iHeadChance = 0;
         }
      } else if (source == CustomDamageSource.damageSourceChoppingBlock) {
         iHeadChance = 0;
      }

      if (iHeadChance < 5) {
         this.dropHead();
      }
   }

   private void customModDrops(DamageSource source) {
      Entity sourceEntity = source.getEntity();
      int iLootingModifier = this.getAmbientLootingModifier();
      if (sourceEntity instanceof EntityPlayer) {
         int iPlayerLootingModifier = EnchantmentHelper.getLootingModifier((EntityLiving)sourceEntity);
         if (iPlayerLootingModifier > iLootingModifier) {
            iLootingModifier = iPlayerLootingModifier;
         }
      }

      this.dropMysteryMeat(iLootingModifier);
      this.checkForHeadDrop(source, iLootingModifier);
   }

   private void dropMysteryMeat(int iLootingModifier) {
      if (!this.hasHeadCrabbedSquid()) {
         long lOverworldTime = MinecraftServer.getServer().worldServers[0].I();
         if (this.timeOfLastSpawnAssignment == 0L
            || this.timeOfLastSpawnAssignment > lOverworldTime
            || lOverworldTime - this.timeOfLastSpawnAssignment >= 10800L) {
            int iDropItemID = BTWItems.rawMysteryMeat.itemID;
            if (this.validateBoundRespawnBeacon(this.worldObj, this.dimension, this.spawnDimension).beaconStatus
               != EntityPlayer$BeaconRespawnValidationResult.BeaconStatus.MISSING) {
               iDropItemID = Item.rottenFlesh.itemID;
            } else if (this.ae()) {
               if (this.worldObj.getDifficulty().shouldBurningMobsDropCookedMeat()) {
                  iDropItemID = BTWItems.cookedMysteryMeat.itemID;
               } else {
                  iDropItemID = BTWItems.burnedMeat.itemID;
               }
            }

            int iFat = this.getStatusForCategory(BTWStatusCategory.FAT).map(StatusEffect::getLevel).orElse(0) / 2;
            int iNumDropped = 2 + iFat;

            for (int iTempCount = 0; iTempCount < iNumDropped; iTempCount++) {
               this.b(iDropItemID, 1);
            }
         }
      }
   }

   private void modSpecificOnUpdate() {
      this.updateExhaustionWithTime();
      this.updatePotionEffectsFromStatus();
      this.updateMagneticInfluences();
      this.updateSpawnChunksVisualization();
      this.notifyBlockWalkedOn();
   }

   private void updateMagneticInfluences() {
      if ((this.worldObj.getTotalWorldTime() + this.entityId) % 40L == 0L) {
         MagneticPoint strongestPoint = null;
         double dStrongestFieldStrength = 0.0;
         if (this.worldObj.provider.isSurfaceWorld()) {
            ChunkCoordinates spawnPos = this.worldObj.getSpawnPoint();
            strongestPoint = new MagneticPoint(spawnPos.posX, 0, spawnPos.posZ, 2);
            dStrongestFieldStrength = strongestPoint.getFieldStrengthRelativeToPosition(this.posX, this.posZ);

            for (MagneticPoint tempPoint : this.worldObj.getMagneticPointList().magneticPoints) {
               double dTempFieldStrength = tempPoint.getFieldStrengthRelativeToPosition(this.posX, this.posZ);
               if (dTempFieldStrength > dStrongestFieldStrength) {
                  strongestPoint = tempPoint;
                  dStrongestFieldStrength = dTempFieldStrength;
               }
            }
         } else {
            for (MagneticPoint tempPointx : this.worldObj.getMagneticPointList().magneticPoints) {
               double dTempFieldStrength = tempPointx.getFieldStrengthRelativeToPositionWithBackgroundNoise(this.posX, this.posZ);
               if (dTempFieldStrength > dStrongestFieldStrength) {
                  strongestPoint = tempPointx;
                  dStrongestFieldStrength = dTempFieldStrength;
               }
            }
         }

         if (strongestPoint != null) {
            this.setHasValidMagneticPointForLocation(true);
            this.setStrongestMagneticPointForLocationI(strongestPoint.posX);
            this.setStrongestMagneticPointForLocationK(strongestPoint.posZ);
         } else {
            this.setHasValidMagneticPointForLocation(false);
         }
      }
   }

   private void updateSpawnChunksVisualization() {
      if (this.worldObj.provider.dimensionId != 0 || !this.isWearingEnderSpectacles() && !this.a(BTWMod.potionTrueSight)) {
         this.setSpawnChunksVisualization(0, 0, 0);
      } else {
         this.setSpawnChunksVisualization(this.worldObj.worldInfo.getSpawnX(), this.worldObj.worldInfo.getSpawnY(), this.worldObj.worldInfo.getSpawnZ());
      }
   }

   private void updateExhaustionWithTime() {
      this.exhaustionWithTimeCounter++;
      if (this.exhaustionWithTimeCounter >= 600) {
         if (!this.capabilities.disableDamage) {
            this.foodStats.addExhaustion(0.5F);
         }

         this.exhaustionWithTimeCounter = 0;
      }
   }

   private void updatePotionEffectsFromStatus() {
      if (!this.isDead && (this.worldObj.getTotalWorldTime() + this.entityId) % 40L == 0L && !this.capabilities.isCreativeMode) {
         for (StatusEffect effect : this.getAllActiveStatusEffects()) {
            effect.getPotionEffect().ifPresent(this::addPotionEffect);
         }
      }
   }

   @Override
   protected void updateGloomState() {
      if (!this.isDead) {
         if (this.isInGloom() && !this.capabilities.isCreativeMode) {
            this.inGloomCounter++;
            if (this.getGloomLevel() == 0 || this.inGloomCounter > 1200 && this.getGloomLevel() < 3) {
               this.setGloomLevel(this.getGloomLevel() + 1);
               this.inGloomCounter = 0;
            }

            if (this.getGloomLevel() >= 3) {
               if ((this.worldObj.getTotalWorldTime() + this.entityId) % 80L == 0L) {
                  this.d(new PotionEffect(Potion.confusion.getId(), 180, 0, true));
               }

               float counterProgress = this.inGloomCounter / 1200.0F;
               if (counterProgress > 1.0F) {
                  counterProgress = 1.0F;
               }

               float gloomBiteChance = 0.01F + 0.04F * counterProgress;
               if (this.rand.nextFloat() < gloomBiteChance && this.attackEntityFrom(CustomDamageSource.damageSourceGloom, 1) && this.health <= 0) {
                  this.worldObj.playAuxSFX(2226, MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ), 0);
               }
            }
         } else {
            this.setGloomLevel(0);
            this.inGloomCounter = 0;
         }
      }
   }

   private boolean isInGloom() {
      if (!this.capabilities.disableDamage && !this.a(Potion.nightVision) && this.worldObj.provider.dimensionId == 0) {
         int i = MathHelper.floor_double(this.posX);
         int j = MathHelper.floor_double(this.posY - this.yOffset);
         int k = MathHelper.floor_double(this.posZ);
         int iOldSkylightSubtracted = this.worldObj.skylightSubtracted;
         float fSunBrightness = this.worldObj.computeOverworldSunBrightnessWithMoonPhases();
         if (fSunBrightness < 0.02) {
            this.worldObj.skylightSubtracted = 15;
         } else {
            this.worldObj.skylightSubtracted = (int)((1.0F - fSunBrightness) * 11.9F);
         }

         float fBlockInLightValue = this.worldObj.getLightBrightness(i, j, k);
         float fBlockAboveLightValue = this.worldObj.getLightBrightness(i, j + 1, k);
         if (fBlockAboveLightValue > fBlockInLightValue) {
            fBlockInLightValue = fBlockAboveLightValue;
         }

         this.worldObj.skylightSubtracted = iOldSkylightSubtracted;
         return fBlockInLightValue < 0.001F;
      } else {
         return false;
      }
   }

   @Override
   public void addRawChatMessage(String message) {
      this.playerNetServerHandler.sendPacket(new Packet3Chat(message));
   }

   @Override
   protected void onZeroDamageAttack() {
      long lCurrentTime = this.worldObj.getWorldTime();
      if (lCurrentTime > this.timeOfLastZeroDamageAttackSound + 20L) {
         this.worldObj.playSoundAtEntity(this, "random.classic_hurt", 1.0F + this.rand.nextFloat() * 0.25F, this.aY() * 1.2F + this.rand.nextFloat() * 0.1F);
         this.timeOfLastZeroDamageAttackSound = lCurrentTime;
      }
   }

   @Override
   public void onStruckByLightning(LightningBoltEntity boltEntity) {
      if (!this.capabilities.disableDamage) {
         this.e(12);
         this.d(8);
         this.flingAwayFromEntity(boltEntity, 2.0);
         this.worldObj.playSoundAtEntity(this, "random.classic_hurt", 1.0F + this.rand.nextFloat() * 0.25F, this.aY() * 1.2F + this.rand.nextFloat() * 0.1F);
         this.d(new PotionEffect(Potion.blindness.getId(), 90, 0, true));
         this.d(new PotionEffect(Potion.confusion.getId(), 180, 0, true));
      }
   }

   public int incrementAndGetWindowID() {
      this.currentWindowId = this.currentWindowId % 100 + 1;
      return this.currentWindowId;
   }

   private void notifyBlockWalkedOn() {
      if (this.onGround) {
         int iGroundI = MathHelper.floor_double(this.posX);
         int iGroundJ = MathHelper.floor_double(this.posY - 0.03 - this.yOffset);
         int iGroundK = MathHelper.floor_double(this.posZ);
         Block blockOn = Block.blocksList[this.worldObj.getBlockId(iGroundI, iGroundJ, iGroundK)];
         if (blockOn == null || blockOn.getCollisionBoundingBoxFromPool(this.worldObj, iGroundI, iGroundJ, iGroundK) == null) {
            float fHalfWidth = this.width / 2.0F;
            int iCenterGroundI = iGroundI;
            iGroundI = MathHelper.floor_double(this.posX + fHalfWidth);
            blockOn = Block.blocksList[this.worldObj.getBlockId(iGroundI, iGroundJ, iGroundK)];
            if (blockOn == null || blockOn.getCollisionBoundingBoxFromPool(this.worldObj, iGroundI, iGroundJ, iGroundK) == null) {
               iGroundI = MathHelper.floor_double(this.posX - fHalfWidth);
               blockOn = Block.blocksList[this.worldObj.getBlockId(iGroundI, iGroundJ, iGroundK)];
               if (blockOn == null || blockOn.getCollisionBoundingBoxFromPool(this.worldObj, iGroundI, iGroundJ, iGroundK) == null) {
                  iGroundI = iCenterGroundI;
                  iGroundK = MathHelper.floor_double(this.posZ + fHalfWidth);
                  blockOn = Block.blocksList[this.worldObj.getBlockId(iCenterGroundI, iGroundJ, iGroundK)];
                  if (blockOn == null || blockOn.getCollisionBoundingBoxFromPool(this.worldObj, iCenterGroundI, iGroundJ, iGroundK) == null) {
                     iGroundK = MathHelper.floor_double(this.posZ - fHalfWidth);
                     blockOn = Block.blocksList[this.worldObj.getBlockId(iCenterGroundI, iGroundJ, iGroundK)];
                  }
               }
            }
         }

         if (blockOn != null) {
            blockOn.onPlayerWalksOnBlock(this.worldObj, iGroundI, iGroundJ, iGroundK, this);
         }
      }
   }

   public void sendChunksToClient() {
      if (!this.chunksToBeSentToClient.isEmpty()) {
         Iterator<ChunkCoordIntPair> coordIterator = this.chunksToBeSentToClient.iterator();
         ArrayList<Chunk> chunksToSend = new ArrayList<>();
         ArrayList<TileEntity> tileEntitiesToSend = new ArrayList<>();

         while (coordIterator.hasNext() && chunksToSend.size() < 5) {
            ChunkCoordIntPair tempCoord = coordIterator.next();
            coordIterator.remove();
            if (tempCoord != null && this.worldObj.chunkExists(tempCoord.chunkXPos, tempCoord.chunkZPos)) {
               chunksToSend.add(this.worldObj.getChunkFromChunkCoords(tempCoord.chunkXPos, tempCoord.chunkZPos));
               tileEntitiesToSend.addAll(
                  this.getServerForPlayer()
                     .getAllTileEntityInBox(
                        tempCoord.chunkXPos * 16, 0, tempCoord.chunkZPos * 16, tempCoord.chunkXPos * 16 + 16, 256, tempCoord.chunkZPos * 16 + 16
                     )
               );
            }
         }

         if (!chunksToSend.isEmpty()) {
            WorldUtils.sendPacketToPlayer(this.playerNetServerHandler, new Packet56MapChunks(chunksToSend));

            for (TileEntity tempTile : tileEntitiesToSend) {
               this.sendTileEntityToPlayer(tempTile);
            }

            for (Chunk var10 : chunksToSend) {
               this.getServerForPlayer().getEntityTracker().func_85172_a(this, var10);
            }
         }
      }
   }

   @Override
   public void setTimeOfLastSpawnAssignment(long timeOfLastSpawnAssignment) {
      super.setTimeOfLastSpawnAssignment(timeOfLastSpawnAssignment);
      HardcoreSpawnPacket packet = new HardcoreSpawnPacket(timeOfLastSpawnAssignment);
      this.playerNetServerHandler.sendPacketToPlayer(packet);
   }
}
