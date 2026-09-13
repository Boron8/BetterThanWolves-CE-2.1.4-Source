package btw.entity.mob;

import btw.block.blocks.BlockDispenserBlock;
import btw.block.tileentity.dispenser.BlockDispenserTileEntity;
import btw.entity.mob.behavior.AnimalFleeBehavior;
import btw.entity.mob.behavior.GrazeBehavior;
import btw.entity.mob.behavior.MoveToGrazeBehavior;
import btw.entity.mob.behavior.MoveToLooseFoodBehavior;
import btw.entity.mob.behavior.MultiTemptBehavior;
import btw.entity.mob.behavior.SimpleWanderBehavior;
import btw.inventory.util.InventoryUtils;
import btw.item.BTWItems;
import btw.item.items.ShearsItem;
import btw.util.ColorUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.management.InstanceAlreadyExistsException;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.BlockCloth;
import net.minecraft.src.DamageSource;
import net.minecraft.src.EntityAIFollowParent;
import net.minecraft.src.EntityAILookIdle;
import net.minecraft.src.EntityAIMate;
import net.minecraft.src.EntityAISwimming;
import net.minecraft.src.EntityAIWatchClosest;
import net.minecraft.src.EntityAgeable;
import net.minecraft.src.EntityItem;
import net.minecraft.src.EntityList;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EntitySheep;
import net.minecraft.src.EntityZombie;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.MathHelper;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.World;

public class SheepEntity extends EntitySheep {
   private static final int FULL_WOOL_ACCUMULATION_COUNT = 24000;
   private int woolAccumulationCount = 0;
   private int originalWoolColorWatcherID = 20;
   private static ArrayList<SheepEntity.ColorBlendEntry> colorBlendList = new ArrayList<>();

   public SheepEntity(World world) {
      super(world);
      this.texture = "/btwmodtex/fcSheep.png";
      this.tasks.removeAllTasks();
      this.tasks.addTask(0, new EntityAISwimming(this));
      this.tasks.addTask(1, new AnimalFleeBehavior(this, 0.38F));
      this.tasks.addTask(2, new EntityAIMate(this, 0.23F));
      this.tasks.addTask(3, new MultiTemptBehavior(this, 0.25F));
      this.tasks.addTask(4, new GrazeBehavior(this));
      this.tasks.addTask(5, new MoveToLooseFoodBehavior(this, 0.23F));
      this.tasks.addTask(6, new MoveToGrazeBehavior(this, 0.23F));
      this.tasks.addTask(7, new EntityAIFollowParent(this, 0.25F));
      this.tasks.addTask(8, new SimpleWanderBehavior(this, 0.25F));
      this.tasks.addTask(9, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
      this.tasks.addTask(10, new EntityAILookIdle(this));
   }

   @Override
   public boolean isAIEnabled() {
      return !this.getWearingBreedingHarness();
   }

   @Override
   protected void dropFewItems(boolean bKilledByPlayer, int iLootingModifier) {
      if (!this.n() && this.isFullyFed()) {
         this.a(new ItemStack(BTWItems.wool.itemID, 1, BlockCloth.getDyeFromBlock(this.m())), 0.0F);
      }

      this.dropMutton(iLootingModifier);
   }

   @Override
   public boolean interact(EntityPlayer player) {
      ItemStack stack = player.inventory.getCurrentItem();
      if (stack != null && stack.getItem() instanceof ShearsItem && !this.n() && !this.h_()) {
         if (!this.worldObj.isRemote) {
            this.i(true);
            int iNumItems = 1 + this.rand.nextInt(3);

            for (int iTempCount = 0; iTempCount < iNumItems; iTempCount++) {
               EntityItem tempItem = this.a(new ItemStack(BTWItems.wool.itemID, 1, BlockCloth.getDyeFromBlock(this.m())), 1.0F);
               tempItem.motionY = tempItem.motionY + this.rand.nextFloat() * 0.05F;
               tempItem.motionX = tempItem.motionX + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.1F;
               tempItem.motionZ = tempItem.motionZ + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.1F;
            }
         }

         stack.damageItem(1, player);
         this.a("mob.sheep.shear", 1.0F, 1.0F);
         this.a(DamageSource.generic, 0);
         if (stack.stackSize <= 0) {
            player.inventory.mainInventory[player.inventory.currentItem] = null;
         }
      }

      return this.entityAnimalInteract(player);
   }

   @Override
   protected int getDropItemId() {
      return BTWItems.wool.itemID;
   }

   @Override
   public void writeEntityToNBT(NBTTagCompound tag) {
      super.writeEntityToNBT(tag);
      tag.setByte("fcOrgClr", (byte)this.getOriginalFleeceColor());
      tag.setInteger("fcWoolCount", this.woolAccumulationCount);
   }

   @Override
   public void readEntityFromNBT(NBTTagCompound tag) {
      super.readEntityFromNBT(tag);
      if (tag.hasKey("fcOrgClr")) {
         this.setOriginalFleeceColor(tag.getByte("fcOrgClr"));
      }

      if (tag.hasKey("fcWoolCount")) {
         this.woolAccumulationCount = tag.getInteger("fcWoolCount");
      }
   }

   @Override
   public boolean getCanCreatureTypeBePossessed() {
      return true;
   }

   @Override
   protected void modSpecificOnLivingUpdate() {
      super.modSpecificOnLivingUpdate();
      if (!this.isLivingDead && this.isFullyPossessed() && !this.n() && !this.G() && !this.I()) {
         if (this.posY < 125.0) {
            this.motionY += 0.08341;
         } else {
            this.motionY += 0.0725;
         }

         if (!this.onGround
            && !this.isCollidedHorizontally
            && this.worldObj.provider.dimensionId == 0
            && this.posY > 100.0
            && !this.checkForWolfBomb()
            && !this.n()
            && this.motionX > -0.012F) {
            this.motionX -= 0.005;
         }
      }
   }

   @Override
   protected void fall(float par1) {
      if (!this.isFullyPossessed() || this.n()) {
         super.a(par1);
      }
   }

   @Override
   public double getMountedYOffset() {
      return this.height;
   }

   @Override
   public boolean isBreedingItem(ItemStack stack) {
      return stack.itemID == Item.pumpkinPie.itemID;
   }

   @Override
   public boolean isValidZombieSecondaryTarget(EntityZombie zombie) {
      return true;
   }

   @Override
   public void initCreature() {
      this.initHungerWithVariance();
      int iFleeceColor = a(this.worldObj.rand);
      if (iFleeceColor == 0) {
         int iDiceRoll = this.worldObj.rand.nextInt(500);
         if (iDiceRoll == 0) {
            iFleeceColor = 3;
         } else if (iDiceRoll == 1) {
            iFleeceColor = 5;
         }
      }

      this.setFleeceColor(iFleeceColor);
   }

   @Override
   public void setFleeceColor(int iColor) {
      super.setFleeceColor(iColor);
      this.setOriginalFleeceColor(iColor);
   }

   @Override
   public EntityAgeable createChild(EntityAgeable parent) {
      return this.spawnHardcoreBaby(parent);
   }

   @Override
   public boolean isSubjectToHunger() {
      return true;
   }

   @Override
   public int getFoodValueMultiplier() {
      return 3;
   }

   @Override
   public void onBecomeFamished() {
      super.onBecomeFamished();
      if (!this.n()) {
         this.i(true);
      }

      this.woolAccumulationCount = 0;
   }

   @Override
   public void updateHungerState() {
      if (this.n() && this.isFullyFed() && !this.h_() && !this.getWearingBreedingHarness()) {
         this.hungerCountdown--;
         this.woolAccumulationCount++;
         if (this.woolAccumulationCount >= 24000) {
            this.setFleeceColor(this.getOriginalFleeceColor());
            this.i(false);
            this.woolAccumulationCount = 0;
            this.worldObj.playAuxSFX(2261, MathHelper.floor_double(this.posX), (int)this.posY + 1, MathHelper.floor_double(this.posZ), 0);
         }
      }

      super.updateHungerState();
   }

   @Override
   protected void entityInit() {
      super.entityInit();
      this.dataWatcher.addObject(20, new Byte((byte)0));
   }

   public int getOriginalFleeceColor() {
      return this.dataWatcher.getWatchableObjectByte(this.originalWoolColorWatcherID) & 15;
   }

   public void setOriginalFleeceColor(int originalColor) {
      byte byte0 = this.dataWatcher.getWatchableObjectByte(this.originalWoolColorWatcherID);
      this.dataWatcher.updateObject(this.originalWoolColorWatcherID, (byte)(byte0 & 240 | originalColor & 15));
   }

   public void setSuperficialFleeceColor(int par1) {
      byte byte0 = this.dataWatcher.getWatchableObjectByte(16);
      this.dataWatcher.updateObject(16, (byte)(byte0 & 240 | par1 & 15));
   }

   public SheepEntity spawnHardcoreBaby(EntityAgeable parentAnimal) {
      SheepEntity parentSheep = (SheepEntity)parentAnimal;
      SheepEntity babySheep = (SheepEntity)EntityList.createEntityOfType(SheepEntity.class, this.worldObj);
      int iMutationChance = this.rand.nextInt(100);
      if (iMutationChance == 0) {
         int iBabyColor = this.getMutantColor(this, parentSheep);
         babySheep.setFleeceColor(iBabyColor);
      } else if (iMutationChance <= 3) {
         int iBabyColor = 15;
         babySheep.setFleeceColor(iBabyColor);
      } else if (iMutationChance <= 23) {
         int iBabyColor = this.blendParentColors(this, parentSheep);
         babySheep.setFleeceColor(iBabyColor);
      } else if (this.rand.nextBoolean()) {
         babySheep.setFleeceColor(this.getOriginalFleeceColor());
      } else {
         babySheep.setFleeceColor(parentSheep.getOriginalFleeceColor());
      }

      return babySheep;
   }

   public int blendParentColors(SheepEntity papa, SheepEntity mama) {
      int papaItemColor = BlockCloth.getBlockFromDye(papa.getOriginalFleeceColor());
      int mamaItemColor = BlockCloth.getBlockFromDye(mama.getOriginalFleeceColor());
      SheepEntity.ColorBlendEntry blendEntry = SheepEntity.ColorBlendEntry.getCachedEntry(papaItemColor, mamaItemColor);
      if (blendEntry != null) {
         if (blendEntry == SheepEntity.ColorBlendEntry.getCachedEntry(ColorUtils.BLACK.colorID, ColorUtils.WHITE.colorID)) {
            return this.rand.nextBoolean() ? BlockCloth.getBlockFromDye(ColorUtils.GRAY.colorID) : BlockCloth.getBlockFromDye(ColorUtils.LIGHT_GRAY.colorID);
         } else {
            return blendEntry.getOutputColor();
         }
      } else {
         return this.rand.nextBoolean() ? BlockCloth.getBlockFromDye(papaItemColor) : BlockCloth.getBlockFromDye(mamaItemColor);
      }
   }

   public int getMutantColor(SheepEntity papa, SheepEntity mama) {
      int iRandomFactor = this.rand.nextInt(3);
      switch (iRandomFactor) {
         case 0:
            return 3;
         case 1:
            return 5;
         default:
            return 6;
      }
   }

   private void dropMutton(int iLootingModifier) {
      if (!this.hasHeadCrabbedSquid() && !this.isStarving()) {
         int iNumDropped = this.rand.nextInt(2) + 1 + this.rand.nextInt(1 + iLootingModifier);
         if (this.isFamished()) {
            iNumDropped /= 2;
         }

         for (int iTempCount = 0; iTempCount < iNumDropped; iTempCount++) {
            if (this.ae()) {
               if (this.worldObj.getDifficulty().shouldBurningMobsDropCookedMeat()) {
                  this.b(BTWItems.cookedMutton.itemID, 1);
               } else {
                  this.b(BTWItems.burnedMeat.itemID, 1);
               }
            } else {
               this.b(BTWItems.rawMutton.itemID, 1);
            }
         }
      }
   }

   private boolean checkForWolfBomb() {
      if (!this.worldObj.isRemote && this.worldObj.worldInfo.getWorldTime() % 20L == 0L) {
         int iSheepI = MathHelper.floor_double(this.posX);
         int iSheepJ = MathHelper.floor_double(this.posY);
         int iSheepK = MathHelper.floor_double(this.posZ);
         int iTopBlockJ = this.worldObj.getPrecipitationHeight(iSheepI, iSheepK) - 1;
         if (iSheepJ - iTopBlockJ >= 16) {
            int iTopBlockID = this.worldObj.getBlockId(iSheepI, iTopBlockJ, iSheepK);
            Block topBlock = Block.blocksList[iTopBlockID];
            if (topBlock != null && !topBlock.blockMaterial.isLiquid() && this.isPossessableWolfWithinRangeOfBlock(iSheepI, iTopBlockJ, iSheepK, 8)) {
               this.initiateWolfBomb();
               this.worldObj.playSoundAtEntity(this, this.bd(), this.ba(), (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
               this.worldObj.playSoundAtEntity(this, "mob.slime.attack", this.ba(), (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 0.6F);
               return true;
            }
         }
      }

      return false;
   }

   private boolean isPossessableWolfWithinRangeOfBlock(int i, int j, int k, int iRange) {
      AxisAlignedBB possessionBox = AxisAlignedBB.getAABBPool().getAABB(i - iRange, j - iRange, k - iRange, i + 1 + iRange, j + 1 + iRange, k + 1 + iRange);

      for (WolfEntity tempWolf : this.worldObj.getEntitiesWithinAABB(WolfEntity.class, possessionBox)) {
         if (!tempWolf.isLivingDead && !tempWolf.isPossessed()) {
            return true;
         }
      }

      return false;
   }

   private void initiateWolfBomb() {
      this.i(true);
      int iItemCount = 1 + this.rand.nextInt(3);

      for (int iTempCount = 0; iTempCount < iItemCount; iTempCount++) {
         EntityItem tempStack = this.a(new ItemStack(BTWItems.wool.itemID, 1, BlockCloth.getDyeFromBlock(this.m())), 1.0F);
         tempStack.motionY = tempStack.motionY + this.rand.nextFloat() * 0.05F;
         tempStack.motionX = tempStack.motionX + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.1F;
         tempStack.motionZ = tempStack.motionZ + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.1F;
      }
   }

   protected boolean isTooHungryToProduceWool() {
      return this.hungerCountdown < 18000;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void handleHealthUpdate(byte bUpdateType) {
      if (bUpdateType == 10) {
         this.grazeProgressCounter = this.getGrazeDuration();
      } else {
         super.handleHealthUpdate(bUpdateType);
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public String getTexture() {
      if (this.getWearingBreedingHarness()) {
         return "/btwmodtex/fc_mr_sheep.png";
      } else {
         int iHungerLevel = this.getHungerLevel();
         if (iHungerLevel == 1) {
            return "/btwmodtex/fcSheepFamished.png";
         } else {
            return iHungerLevel == 2 ? "/btwmodtex/fcSheepStarving.png" : super.N();
         }
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public float func_70894_j(float fPartialTick) {
      return this.getGrazeHeadVerticalOffset(fPartialTick);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public float func_70890_k(float fPartialTick) {
      return this.getGrazeHeadRotation(fPartialTick);
   }

   @Override
   public boolean onBlockDispenserConsume(BlockDispenserBlock blockDispenser, BlockDispenserTileEntity tileEntity) {
      if (!this.n() && !this.h_()) {
         this.i(true);
         InventoryUtils.addSingleItemToInventory(tileEntity, BTWItems.wool.itemID, BlockCloth.getDyeFromBlock(this.m()));
         this.a(DamageSource.generic, 0);

         for (int tempCount = 0; tempCount < 2; tempCount++) {
            blockDispenser.spitOutItem(this.worldObj, tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord, new ItemStack(Item.silk));
         }

         return true;
      } else {
         return false;
      }
   }

   static {
      try {
         colorBlendList.add(new SheepEntity.ColorBlendEntry(ColorUtils.BLACK.colorID, ColorUtils.WHITE.colorID, ColorUtils.GRAY.colorID));
         colorBlendList.add(new SheepEntity.ColorBlendEntry(ColorUtils.BLACK.colorID, ColorUtils.LIGHT_GRAY.colorID, ColorUtils.GRAY.colorID));
         colorBlendList.add(new SheepEntity.ColorBlendEntry(ColorUtils.GRAY.colorID, ColorUtils.WHITE.colorID, ColorUtils.LIGHT_GRAY.colorID));
         colorBlendList.add(new SheepEntity.ColorBlendEntry(ColorUtils.BLACK.colorID, ColorUtils.LIGHT_BLUE.colorID, ColorUtils.BLUE.colorID));
         colorBlendList.add(new SheepEntity.ColorBlendEntry(ColorUtils.BLACK.colorID, ColorUtils.LIME.colorID, ColorUtils.GREEN.colorID));
         colorBlendList.add(new SheepEntity.ColorBlendEntry(ColorUtils.BLACK.colorID, ColorUtils.PINK.colorID, ColorUtils.RED.colorID));
         colorBlendList.add(new SheepEntity.ColorBlendEntry(ColorUtils.BLUE.colorID, ColorUtils.PINK.colorID, ColorUtils.MAGENTA.colorID));
         colorBlendList.add(new SheepEntity.ColorBlendEntry(ColorUtils.RED.colorID, ColorUtils.LIGHT_BLUE.colorID, ColorUtils.MAGENTA.colorID));
         colorBlendList.add(new SheepEntity.ColorBlendEntry(ColorUtils.BLACK.colorID, ColorUtils.MAGENTA.colorID, ColorUtils.PURPLE.colorID));
         colorBlendList.add(new SheepEntity.ColorBlendEntry(ColorUtils.RED.colorID, ColorUtils.BLUE.colorID, ColorUtils.PURPLE.colorID));
         colorBlendList.add(new SheepEntity.ColorBlendEntry(ColorUtils.BLUE.colorID, ColorUtils.GREEN.colorID, ColorUtils.CYAN.colorID));
         colorBlendList.add(new SheepEntity.ColorBlendEntry(ColorUtils.RED.colorID, ColorUtils.GREEN.colorID, ColorUtils.YELLOW.colorID));
         colorBlendList.add(new SheepEntity.ColorBlendEntry(ColorUtils.CYAN.colorID, ColorUtils.RED.colorID, ColorUtils.BROWN.colorID));
         colorBlendList.add(new SheepEntity.ColorBlendEntry(ColorUtils.YELLOW.colorID, ColorUtils.RED.colorID, ColorUtils.ORANGE.colorID));
      } catch (InstanceAlreadyExistsException var1) {
         var1.printStackTrace();
      }
   }

   public static class ColorBlendEntry {
      private final int color1;
      private final int color2;
      private final int outputColor;
      private static Map<Integer, SheepEntity.ColorBlendEntry> entryCache = new HashMap<>();

      public ColorBlendEntry(int color1, int color2, int outputColor) throws InstanceAlreadyExistsException {
         if (color1 > color2) {
            int tempColor = color1;
            color1 = color2;
            color2 = tempColor;
         }

         this.color1 = color1;
         this.color2 = color2;
         this.outputColor = outputColor;
         int index = (color1 & 15) << 4 | color2 & 15;
         if (entryCache.get(index) != null) {
            throw new InstanceAlreadyExistsException("Cannot make more than one instance of an entry per color pair");
         } else {
            entryCache.put(index, this);
         }
      }

      public int getOutputColor() {
         return BlockCloth.getBlockFromDye(this.outputColor);
      }

      public static SheepEntity.ColorBlendEntry getCachedEntry(int color1, int color2) {
         if (color1 > color2) {
            int tempColor = color1;
            color1 = color2;
            color2 = tempColor;
         }

         int index = (color1 & 15) << 4 | color2 & 15;
         return entryCache.get(index);
      }
   }
}
