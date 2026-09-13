package net.minecraft.src;

import btw.BTWMod;
import btw.client.gui.CraftingGuiWorkbench;
import btw.item.BTWItems;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;

@Environment(EnvType.CLIENT)
public class EntityPlayerSP extends EntityPlayer {
   public MovementInput movementInput;
   protected Minecraft mc;
   protected int sprintToggleTimer = 0;
   public int sprintingTicksLeft = 0;
   public float renderArmYaw;
   public float renderArmPitch;
   public float prevRenderArmYaw;
   public float prevRenderArmPitch;
   private MouseFilter field_71162_ch = new MouseFilter();
   private MouseFilter field_71160_ci = new MouseFilter();
   private MouseFilter field_71161_cj = new MouseFilter();
   public float timeInPortal;
   public float prevTimeInPortal;
   private static final float MINIMUM_GLOOM_CAVE_SOUND_CHANCE = 0.01F;
   private static final float MAXIMUM_GLOOM_CAVE_SOUND_CHANCE = 0.05F;
   private static final float MINIMUM_GLOOM_CAVE_SOUND_VOLUME = 0.1F;
   private static final float MAXIMUM_GLOOM_CAVE_SOUND_VOLUME = 4.0F;
   private static final float MINIMUM_GLOOM_GROWL_SOUND_CHANCE = 0.01F;
   private static final float MAXIMUM_GLOOM_GROWL_SOUND_CHANCE = 0.05F;
   private static final float MINIMUM_GLOOM_GROWL_SOUND_VOLUME = 0.1F;
   private static final float MAXIMUM_GLOOM_GROWL_SOUND_VOLUME = 4.0F;
   private static final float MAXIMUM_GLOOM_FOV_MULTIPLIER = 1.5F;
   private static final float GLOOM_FOV_MULTIPLIER_TIME_FOR_TRANSITION_IN = 10.0F;
   private static final float GLOOM_FOV_MULTIPLIER_TIME_FOR_TRANSITION_OUT = 2.0F;
   private static final float GLOOM_FOV_MULTIPLIER_DELTA_IN_PER_TICK = 0.0025F;
   private static final float GLOOM_FOV_MULTIPLIER_DELTA_OUT_PER_TICK = 0.0125F;
   private float currentGloomFOVMultiplier = 1.0F;
   private int previousGloomLevel = 0;
   public boolean exhaustionAddedSinceLastGuiUpdate = false;

   public EntityPlayerSP(Minecraft par1Minecraft, World par2World, Session par3Session, int par4) {
      super(par2World);
      this.mc = par1Minecraft;
      this.dimension = par4;
      if (par3Session != null && par3Session.username != null && par3Session.username.length() > 0) {
         this.skinUrl = "http://skins.minecraft.net/MinecraftSkins/" + StringUtils.stripControlCodes(par3Session.username) + ".png";
      }

      this.username = par3Session.username;
   }

   @Override
   public void moveEntity(double par1, double par3, double par5) {
      super.d(par1, par3, par5);
   }

   @Override
   public void updateEntityActionState() {
      super.updateEntityActionState();
      this.moveStrafing = this.movementInput.moveStrafe;
      this.moveForward = this.movementInput.moveForward;
      this.isJumping = this.movementInput.jump;
      this.prevRenderArmYaw = this.renderArmYaw;
      this.prevRenderArmPitch = this.renderArmPitch;
      this.renderArmPitch = (float)(this.renderArmPitch + (this.rotationPitch - this.renderArmPitch) * 0.5);
      this.renderArmYaw = (float)(this.renderArmYaw + (this.rotationYaw - this.renderArmYaw) * 0.5);
   }

   @Override
   protected boolean isClientWorld() {
      return true;
   }

   @Override
   public void onLivingUpdate() {
      if (this.sprintingTicksLeft > 0) {
         this.sprintingTicksLeft--;
         if (this.sprintingTicksLeft == 0) {
            this.setSprinting(false);
         }
      }

      if (this.sprintToggleTimer > 0) {
         this.sprintToggleTimer--;
      }

      if (this.mc.playerController.enableEverythingIsScrewedUpMode()) {
         this.posX = this.posZ = 0.5;
         this.posX = 0.0;
         this.posZ = 0.0;
         this.rotationYaw = this.ticksExisted / 12.0F;
         this.rotationPitch = 10.0F;
         this.posY = 68.5;
      } else {
         if (!this.mc.statFileWriter.hasAchievementUnlocked(AchievementList.openInventory)) {
            this.mc.guiAchievement.queueAchievementInformation(AchievementList.openInventory);
         }

         this.prevTimeInPortal = this.timeInPortal;
         if (this.inPortal) {
            if (this.mc.currentScreen != null) {
               this.mc.displayGuiScreen((GuiScreen)null);
            }

            if (this.timeInPortal == 0.0F) {
               this.mc.sndManager.playSoundFX("portal.trigger", 1.0F, this.rand.nextFloat() * 0.4F + 0.8F);
            }

            this.timeInPortal += 0.0125F;
            if (this.timeInPortal >= 1.0F) {
               this.timeInPortal = 1.0F;
            }

            this.inPortal = false;
         } else if (this.a(Potion.confusion) && this.b(Potion.confusion).getDuration() > 60) {
            this.timeInPortal += 0.006666667F;
            if (this.timeInPortal > 1.0F) {
               this.timeInPortal = 1.0F;
            }
         } else {
            if (this.timeInPortal > 0.0F) {
               this.timeInPortal -= 0.05F;
            }

            if (this.timeInPortal < 0.0F) {
               this.timeInPortal = 0.0F;
            }
         }

         if (this.timeUntilPortal > 0) {
            this.timeUntilPortal--;
         }

         boolean var1 = this.movementInput.jump;
         float var2 = 0.8F;
         boolean var3 = this.movementInput.moveForward >= var2;
         boolean wasHoldingSpecial = this.isUsingSpecialKey();
         this.movementInput.updatePlayerMoveState();
         if (this.bX()) {
            this.movementInput.moveStrafe *= 0.2F;
            this.movementInput.moveForward *= 0.2F;
            this.sprintToggleTimer = 0;
         }

         if (this.movementInput.sneak && this.ySize < 0.2F) {
            this.ySize = 0.2F;
         }

         this.pushOutOfBlocks(this.posX - this.width * 0.35, this.boundingBox.minY + 0.5, this.posZ + this.width * 0.35);
         this.pushOutOfBlocks(this.posX - this.width * 0.35, this.boundingBox.minY + 0.5, this.posZ - this.width * 0.35);
         this.pushOutOfBlocks(this.posX + this.width * 0.35, this.boundingBox.minY + 0.5, this.posZ - this.width * 0.35);
         this.pushOutOfBlocks(this.posX + this.width * 0.35, this.boundingBox.minY + 0.5, this.posZ + this.width * 0.35);
         boolean var4 = !this.doesStatusPreventSprinting() || this.capabilities.allowFlying;
         boolean activatedSprint = false;
         if (this.onGround && !var3 && this.movementInput.moveForward >= var2 && !this.ah() && var4 && !this.bX() && !this.a(Potion.blindness)) {
            if (this.sprintToggleTimer == 0) {
               this.sprintToggleTimer = 7;
            } else {
               this.setSprinting(true);
               activatedSprint = true;
               this.sprintToggleTimer = 0;
            }
         }

         if (this.isUsingSpecialKey()
            && !wasHoldingSpecial
            && this.movementInput.moveForward >= var2
            && !this.ah()
            && var4
            && !this.bX()
            && !this.a(Potion.blindness)) {
            this.setSprinting(true);
            activatedSprint = true;
            this.sprintToggleTimer = 0;
         }

         if (this.isSneaking()) {
            this.sprintToggleTimer = 0;
         }

         if (this.ah() && (this.movementInput.moveForward < var2 || !var4 || this.isUsingSpecialKey() && !wasHoldingSpecial && !activatedSprint)) {
            this.setSprinting(false);
         }

         if (this.capabilities.allowFlying && !var1 && this.movementInput.jump) {
            if (this.flyToggleTimer == 0) {
               this.flyToggleTimer = 7;
            } else {
               this.capabilities.isFlying = !this.capabilities.isFlying;
               this.n();
               this.flyToggleTimer = 0;
            }
         }

         if (this.capabilities.isFlying) {
            if (this.movementInput.sneak) {
               this.motionY -= 0.15;
            }

            if (this.movementInput.jump) {
               this.motionY += 0.15;
            }
         }

         super.onLivingUpdate();
         if (this.onGround && this.capabilities.isFlying) {
            this.capabilities.isFlying = false;
            this.n();
         }
      }
   }

   public float getFOVMultiplier() {
      float var1 = 1.0F;
      if (this.capabilities.isFlying) {
         var1 *= 1.1F;
      }

      var1 *= (this.landMovementFactor * this.getFOVSpeedModifier() / this.speedOnGround + 1.0F) / 2.0F;
      var1 *= this.updateGloomFOVMultiplier();
      if (this.bX() && (this.bV().itemID == Item.bow.itemID || this.bV().itemID == BTWItems.compositeBow.itemID)) {
         int var2 = this.bY();
         float var3 = var2 / 20.0F;
         if (var3 > 1.0F) {
            var3 = 1.0F;
         } else {
            var3 *= var3;
         }

         var1 *= 1.0F - var3 * 0.15F;
      }

      return var1;
   }

   @Override
   public void updateCloak() {
      this.cloakUrl = BTWMod.playerCloakURL + fetchUuid(this.username);
   }

   @Override
   public void closeScreen() {
      super.closeScreen();
      this.mc.displayGuiScreen((GuiScreen)null);
   }

   @Override
   public void displayGUIEditSign(TileEntity par1TileEntity) {
      if (par1TileEntity instanceof TileEntitySign) {
         this.mc.displayGuiScreen(new GuiEditSign((TileEntitySign)par1TileEntity));
      } else if (par1TileEntity instanceof TileEntityCommandBlock) {
         this.mc.displayGuiScreen(new GuiCommandBlock((TileEntityCommandBlock)par1TileEntity));
      }
   }

   @Override
   public void displayGUIBook(ItemStack par1ItemStack) {
      Item var2 = par1ItemStack.getItem();
      if (var2 == Item.writtenBook) {
         this.mc.displayGuiScreen(new GuiScreenBook(this, par1ItemStack, false));
      } else if (var2 == Item.writableBook) {
         this.mc.displayGuiScreen(new GuiScreenBook(this, par1ItemStack, true));
      } else if (var2 == BTWItems.ancientProphecy) {
         this.mc.displayGuiScreen(new GuiScreenBook(this, par1ItemStack, false));
      }
   }

   @Override
   public void displayGUIChest(IInventory par1IInventory) {
      this.mc.displayGuiScreen(new GuiChest(this.inventory, par1IInventory));
   }

   @Override
   public void displayGUIHopper(TileEntityHopper par1TileEntityHopper) {
      this.mc.displayGuiScreen(new GuiHopper(this.inventory, par1TileEntityHopper));
   }

   @Override
   public void displayGUIHopperMinecart(EntityMinecartHopper par1EntityMinecartHopper) {
      this.mc.displayGuiScreen(new GuiHopper(this.inventory, par1EntityMinecartHopper));
   }

   @Override
   public void displayGUIWorkbench(int par1, int par2, int par3) {
      this.mc.displayGuiScreen(new CraftingGuiWorkbench(this.inventory, this.worldObj, par1, par2, par3));
   }

   @Override
   public void displayGUIEnchantment(int par1, int par2, int par3, String par4Str) {
      this.mc.displayGuiScreen(new GuiEnchantment(this.inventory, this.worldObj, par1, par2, par3, par4Str));
   }

   @Override
   public void displayGUIAnvil(int par1, int par2, int par3) {
      this.mc.displayGuiScreen(new GuiRepair(this.inventory, this.worldObj, par1, par2, par3));
   }

   @Override
   public void displayGUIFurnace(TileEntityFurnace par1TileEntityFurnace) {
      this.mc.displayGuiScreen(new GuiFurnace(this.inventory, par1TileEntityFurnace));
   }

   @Override
   public void displayGUIBrewingStand(TileEntityBrewingStand par1TileEntityBrewingStand) {
      this.mc.displayGuiScreen(new GuiBrewingStand(this.inventory, par1TileEntityBrewingStand));
   }

   @Override
   public void displayGUIBeacon(TileEntityBeacon par1TileEntityBeacon) {
      this.mc.displayGuiScreen(new GuiBeacon(this.inventory, par1TileEntityBeacon));
   }

   @Override
   public void displayGUIDispenser(TileEntityDispenser par1TileEntityDispenser) {
      this.mc.displayGuiScreen(new GuiDispenser(this.inventory, par1TileEntityDispenser));
   }

   @Override
   public void displayGUIMerchant(IMerchant par1IMerchant, String par2Str) {
      this.mc.displayGuiScreen(new GuiMerchant(this.inventory, par1IMerchant, this.worldObj, par2Str));
   }

   @Override
   public void onCriticalHit(Entity par1Entity) {
      this.mc.effectRenderer.addEffect((EntityFX)EntityList.createEntityOfType(EntityCrit2FX.class, this.mc.theWorld, par1Entity));
   }

   @Override
   public void onEnchantmentCritical(Entity par1Entity) {
      EntityCrit2FX var2 = (EntityCrit2FX)EntityList.createEntityOfType(EntityCrit2FX.class, this.mc.theWorld, par1Entity, "magicCrit");
      this.mc.effectRenderer.addEffect(var2);
   }

   @Override
   public void onItemPickup(Entity par1Entity, int par2) {
      this.mc.effectRenderer.addEffect((EntityFX)EntityList.createEntityOfType(EntityPickupFX.class, this.mc.theWorld, par1Entity, this, -0.5F));
   }

   @Override
   public boolean isSneaking() {
      return this.movementInput.sneak && !this.sleeping;
   }

   @Override
   public boolean isUsingSpecialKey() {
      return this.movementInput.special;
   }

   public void setHealth(int par1) {
      int var2 = this.aX() - par1;
      if (var2 <= 0) {
         this.b(par1);
         if (var2 < 0) {
            this.hurtResistantTime = this.maxHurtResistantTime / 2;
         }
      } else {
         this.lastDamage = var2;
         this.b(this.aX());
         this.hurtResistantTime = this.maxHurtResistantTime;
         this.d(DamageSource.generic, var2);
         this.hurtTime = this.maxHurtTime = 10;
      }
   }

   @Override
   public void addChatMessage(String par1Str) {
      this.mc.ingameGUI.getChatGUI().addTranslatedMessage(par1Str);
   }

   @Override
   public void addStat(StatBase par1StatBase, int par2) {
      if (par1StatBase != null) {
         if (par1StatBase.isAchievement()) {
            Achievement var3 = (Achievement)par1StatBase;
            if (var3.parentAchievement == null || this.mc.statFileWriter.hasAchievementUnlocked(var3.parentAchievement)) {
               if (!this.mc.statFileWriter.hasAchievementUnlocked(var3)) {
                  this.mc.guiAchievement.queueTakenAchievement(var3);
               }

               this.mc.statFileWriter.readStat(par1StatBase, par2);
            }
         } else {
            this.mc.statFileWriter.readStat(par1StatBase, par2);
         }
      }
   }

   private boolean isBlockTranslucent(int par1, int par2, int par3) {
      return this.worldObj.isBlockNormalCube(par1, par2, par3);
   }

   @Override
   protected boolean pushOutOfBlocks(double par1, double par3, double par5) {
      int var7 = MathHelper.floor_double(par1);
      int var8 = MathHelper.floor_double(par3);
      int var9 = MathHelper.floor_double(par5);
      double var10 = par1 - var7;
      double var12 = par5 - var9;
      if (this.isBlockTranslucent(var7, var8, var9) || this.isBlockTranslucent(var7, var8 + 1, var9)) {
         boolean var14 = !this.isBlockTranslucent(var7 - 1, var8, var9) && !this.isBlockTranslucent(var7 - 1, var8 + 1, var9);
         boolean var15 = !this.isBlockTranslucent(var7 + 1, var8, var9) && !this.isBlockTranslucent(var7 + 1, var8 + 1, var9);
         boolean var16 = !this.isBlockTranslucent(var7, var8, var9 - 1) && !this.isBlockTranslucent(var7, var8 + 1, var9 - 1);
         boolean var17 = !this.isBlockTranslucent(var7, var8, var9 + 1) && !this.isBlockTranslucent(var7, var8 + 1, var9 + 1);
         byte var18 = -1;
         double var19 = 9999.0;
         if (var14 && var10 < var19) {
            var19 = var10;
            var18 = 0;
         }

         if (var15 && 1.0 - var10 < var19) {
            var19 = 1.0 - var10;
            var18 = 1;
         }

         if (var16 && var12 < var19) {
            var19 = var12;
            var18 = 4;
         }

         if (var17 && 1.0 - var12 < var19) {
            var19 = 1.0 - var12;
            var18 = 5;
         }

         float var21 = 0.1F;
         if (var18 == 0) {
            this.motionX = -var21;
         }

         if (var18 == 1) {
            this.motionX = var21;
         }

         if (var18 == 4) {
            this.motionZ = -var21;
         }

         if (var18 == 5) {
            this.motionZ = var21;
         }
      }

      return false;
   }

   @Override
   public void setSprinting(boolean par1) {
      super.c(par1);
      this.sprintingTicksLeft = par1 ? 600 : 0;
   }

   public void setXPStats(float par1, int par2, int par3) {
      this.experience = par1;
      this.experienceTotal = par2;
      this.experienceLevel = par3;
   }

   @Override
   public void sendChatToPlayer(String par1Str) {
      this.mc.ingameGUI.getChatGUI().printChatMessage(par1Str);
   }

   @Override
   public boolean canCommandSenderUseCommand(int par1, String par2Str) {
      return par1 <= 0;
   }

   @Override
   public ChunkCoordinates getPlayerCoordinates() {
      return new ChunkCoordinates(MathHelper.floor_double(this.posX + 0.5), MathHelper.floor_double(this.posY + 0.5), MathHelper.floor_double(this.posZ + 0.5));
   }

   @Override
   public ItemStack getHeldItem() {
      return this.inventory.getCurrentItem();
   }

   @Override
   public void playSound(String par1Str, float par2, float par3) {
      this.worldObj.playSound(this.posX, this.posY - this.yOffset, this.posZ, par1Str, par2, par3, false);
   }

   @Override
   public void addExhaustion(float fAmount) {
      if (!this.capabilities.disableDamage) {
         this.exhaustionAddedSinceLastGuiUpdate = true;
      }

      super.addExhaustion(fAmount);
   }

   @Override
   public void addExhaustionWithoutVisualFeedback(float fAmount) {
      super.addExhaustion(fAmount);
   }

   private float getFOVSpeedModifier() {
      float var1 = 1.0F;
      if (this.a(Potion.moveSlowdown)) {
         var1 *= 1.0F - 0.15F * (this.b(Potion.moveSlowdown).getAmplifier() + 1);
      }

      return var1;
   }

   private float updateGloomFOVMultiplier() {
      int iGloomLevel = this.getGloomLevel();
      if (iGloomLevel == 0) {
         this.currentGloomFOVMultiplier -= 0.0125F;
         if (this.currentGloomFOVMultiplier < 1.0F) {
            this.currentGloomFOVMultiplier = 1.0F;
         }
      } else {
         this.currentGloomFOVMultiplier += 0.0025F;
         if (this.currentGloomFOVMultiplier > 1.5F) {
            this.currentGloomFOVMultiplier = 1.5F;
         }
      }

      return this.currentGloomFOVMultiplier;
   }

   @Override
   protected void updateGloomState() {
      int iGloomLevel = this.getGloomLevel();
      if (this.previousGloomLevel != this.getGloomLevel()) {
         this.inGloomCounter = 0;
         this.previousGloomLevel = iGloomLevel;
         if (iGloomLevel == 3) {
            this.playSound("mob.endermen.stare", 1.0F, 1.0F);
         }
      }

      if (iGloomLevel > 0) {
         this.inGloomCounter++;
         float fCounterProgress = this.inGloomCounter / 1200.0F;
         if (fCounterProgress > 1.0F) {
            fCounterProgress = 1.0F;
         }

         float fCaveSoundChance = 0.05F;
         float fCaveSoundVolume = 4.0F;
         if (iGloomLevel > 1) {
            float fGrowlSoundChance = 0.05F;
            float fGrowlSoundVolume = 4.0F;
            if (iGloomLevel <= 2) {
               fGrowlSoundChance = 0.01F + 0.04F * fCounterProgress;
               fGrowlSoundVolume = 0.1F + 3.9F * fCounterProgress;
            }

            if (this.rand.nextFloat() < fGrowlSoundChance) {
               this.playSoundInRandomDirection("mob.wolf.growl", fGrowlSoundVolume, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.05F + 0.55F, 5.0);
            }
         } else {
            fCaveSoundChance = 0.01F + 0.04F * fCounterProgress;
            fCaveSoundVolume = 0.1F + 3.9F * fCounterProgress;
         }

         if (this.rand.nextFloat() < fCaveSoundChance) {
            this.playSoundInRandomDirection("ambient.cave.cave", fCaveSoundVolume, 0.5F + this.rand.nextFloat(), 5.0);
         }
      }
   }

   public void playSoundInRandomDirection(String sSoundName, float fVolume, float fPitch, double dDistance) {
      double dXPos = this.posX;
      double dYPos = this.posY;
      double dZPos = this.posZ;
      double dRandomYaw = this.rand.nextDouble();
      double dXOffset = -MathHelper.sin((float)(dRandomYaw * 360.0)) * dDistance;
      double dZOffset = MathHelper.cos((float)(dRandomYaw * 360.0)) * dDistance;
      dXPos += dXOffset;
      dZPos += dZOffset;
      this.worldObj.playSound(dXPos, dYPos, dZPos, sSoundName, fVolume, fPitch, false);
   }

   @Override
   public boolean isLocalPlayerAndHittingBlock() {
      return this.mc.playerController.isHittingBlock();
   }
}
