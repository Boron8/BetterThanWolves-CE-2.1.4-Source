package btw.block.tileentity.beacon;

import btw.BTWMod;
import btw.block.BTWBlocks;
import java.util.ArrayList;
import java.util.Random;
import net.minecraft.src.Block;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.Potion;
import net.minecraft.src.TileEntityBeacon;

public class BeaconTileEntity extends TileEntityBeacon {
   private static final long TICKS_UPDATES = 80L;
   private static ArrayList<BeaconEffectDescriptor>[] beaconEffectsByBlockID = new ArrayList[4096];
   private static Random rand = new Random();
   private long updateOffset;
   public boolean updatedPowerState = true;
   public boolean playerRespawnedAtBeacon = false;
   public int belowBlockID = -1;
   public int belowMetadata = -1;
   public static final BeaconEffect HASTE_EFFECT = new PotionBeaconEffect(Potion.digSpeed.getId(), true);
   public static final BeaconEffect FORTUNE_EFFECT = new PotionBeaconEffect(BTWMod.potionFortune.getId(), true);
   public static final BeaconEffect TRUE_SIGHT_EFFECT = new PotionBeaconEffect(BTWMod.potionTrueSight.getId(), false);
   public static final BeaconEffect NIGHT_VISION_EFFECT = new PotionBeaconEffect(Potion.nightVision.getId(), false);
   public static final BeaconEffect FIRE_RESIST_EFFECT = new PotionBeaconEffect(Potion.fireResistance.getId(), false);
   public static final BeaconEffect MAGNETIC_POLE_EFFECT = new MagneticPointBeaconEffect();
   public static final BeaconEffect SPAWN_ANCHOR_EFFECT = new SpawnAnchorBeaconEffect();
   public static final BeaconEffect DECORATIVE_EFFECT = new DecorativeBeaconEffect();
   public static final BeaconEffect NAUSEA_EFFECT = new NauseaBeaconEffect();
   public static final BeaconEffect ENDER_ANTENNA_EFFECT = new EnderAntennaBeaconEffect();
   public static final AmbientBeaconEffect LOOTING_EFFECT = new LootingBeaconEffect();
   public static final CompanionBeaconEffect COMPANION_EFFECT = new CompanionBeaconEffect();
   public static final AmbientBeaconEffect JUNGLE_SPIDER_REPELLENT = new AmbientBeaconEffect("JungleRepel");
   public BeaconEffect beaconEffect;

   public BeaconTileEntity() {
      this.updateOffset = rand.nextInt(80);
   }

   @Override
   public void updateEntity() {
      if ((this.worldObj.getTotalWorldTime() + this.updateOffset) % 80L == 0L) {
         this.updatePowerState();
         this.updatedPowerState = true;
      }

      if (this.beaconEffect != null && this.isOn() && this.l() > 0) {
         this.beaconEffect.onUpdate(this);
      }

      if (this.updatedPowerState) {
         this.updatedPowerState = false;
      }
   }

   @Override
   public void readFromNBT(NBTTagCompound par1NBTTagCompound) {
      super.readFromNBT(par1NBTTagCompound);
      this.belowBlockID = par1NBTTagCompound.getInteger("belowBlockID");
      this.belowMetadata = par1NBTTagCompound.getInteger("belowMetadata");
      this.isBeaconActive = par1NBTTagCompound.getBoolean("isBeaconActive");
      if (this.isBeaconActive) {
         BeaconEffectDescriptor effectDescriptor = this.getEffectDescriptor(this.belowBlockID, this.belowMetadata);
         if (effectDescriptor != null && effectDescriptor.EFFECT != null && effectDescriptor.EFFECT != this.beaconEffect) {
            this.beaconEffect = effectDescriptor.EFFECT;
         }
      }
   }

   @Override
   public void writeToNBT(NBTTagCompound par1NBTTagCompound) {
      super.writeToNBT(par1NBTTagCompound);
      par1NBTTagCompound.setInteger("belowBlockID", this.belowBlockID);
      par1NBTTagCompound.setInteger("belowMetadata", this.belowMetadata);
      par1NBTTagCompound.setBoolean("isBeaconActive", this.isBeaconActive);
   }

   private boolean isPyramidLevelValid(int level, int blockIDToCheck, int metadataToCheck) {
      int j = this.yCoord - level;
      if (j < 0) {
         return false;
      } else {
         for (int i = this.xCoord - level; i <= this.xCoord + level; i++) {
            for (int k = this.zCoord - level; k <= this.zCoord + level; k++) {
               int blockID = this.worldObj.getBlockId(i, j, k);
               if (blockID != blockIDToCheck || metadataToCheck != -1 && this.worldObj.getBlockMetadata(i, j, k) != metadataToCheck) {
                  return false;
               }
            }
         }

         return true;
      }
   }

   private void updatePowerState() {
      if (this.worldObj.checkChunksExist(this.xCoord - 4, this.yCoord, this.zCoord - 4, this.xCoord + 4, this.yCoord, this.zCoord + 4)) {
         this.updateBelowBlockType();
         if (!this.canBeaconSeeSky()) {
            this.setPowerState(false, 0, null);
         } else {
            int level = 0;
            BeaconEffectDescriptor effectDescriptor = this.getEffectDescriptor(this.belowBlockID, this.belowMetadata);
            if (effectDescriptor != null) {
               int metadataToCheck = effectDescriptor.BLOCK_METADATA;
               int i = 1;

               while (i <= 4 && this.isPyramidLevelValid(i, this.belowBlockID, metadataToCheck)) {
                  level = i++;
               }

               if (effectDescriptor.EFFECT != null) {
                  if (level > 0) {
                     this.setPowerState(true, level, effectDescriptor.EFFECT);
                  } else {
                     this.setPowerState(false, 0, null);
                  }
               }
            }
         }
      }
   }

   private void updateBelowBlockType() {
      this.belowBlockID = this.worldObj.getBlockId(this.xCoord, this.yCoord - 1, this.zCoord);
      if (this.belowBlockID > 0) {
         this.belowMetadata = this.worldObj.getBlockMetadata(this.xCoord, this.yCoord - 1, this.zCoord);
      }
   }

   public void setPowerState(boolean isOn, int newPowerLevel, BeaconEffect effectClass) {
      int oldPowerLevel = this.l();
      BeaconEffect oldEffect = this.beaconEffect;
      this.beaconEffect = effectClass;
      if (this.beaconEffect != null) {
         if (newPowerLevel != oldPowerLevel) {
            effectClass.onPowerChange(newPowerLevel, oldPowerLevel, this);
         }

         if (oldPowerLevel <= 0 && newPowerLevel > 0) {
            effectClass.onPowerOn(this);
         }
      } else if (oldEffect != null) {
         if (newPowerLevel != oldPowerLevel) {
            oldEffect.onPowerChange(newPowerLevel, oldPowerLevel, this);
         }

         if (newPowerLevel <= 0 && oldPowerLevel > 0) {
            oldEffect.onPowerOff(this);
         }
      }

      this.setIsOn(isOn);
      this.setLevelsServerSafe(newPowerLevel);
   }

   private boolean canBeaconSeeSky() {
      if (this.worldObj.provider.dimensionId != -1) {
         return this.worldObj.canBlockSeeTheSky(this.xCoord, this.yCoord + 1, this.zCoord);
      } else if (!this.worldObj.isAirBlock(this.xCoord, this.yCoord + 1, this.zCoord)) {
         return false;
      } else {
         for (int iTempY = this.yCoord + 2; iTempY < 256; iTempY++) {
            if (!this.worldObj.isAirBlock(this.xCoord, iTempY, this.zCoord)) {
               int iBlockID = this.worldObj.getBlockId(this.xCoord, iTempY, this.zCoord);
               return iBlockID == Block.bedrock.blockID;
            }
         }

         return true;
      }
   }

   private BeaconEffectDescriptor getEffectDescriptor(int blockID, int metadata) {
      ArrayList<BeaconEffectDescriptor> descriptorArray = beaconEffectsByBlockID[blockID];

      for (int i = 0; i < descriptorArray.size(); i++) {
         BeaconEffectDescriptor tempDescriptor = descriptorArray.get(i);
         if (tempDescriptor.BLOCK_METADATA == -1 || tempDescriptor.BLOCK_METADATA == metadata) {
            return tempDescriptor;
         }
      }

      return null;
   }

   public BeaconEffect getActiveEffect() {
      return this.beaconEffect;
   }

   public static void addBeaconEffect(int iBlockID, BeaconEffect effectClass) {
      addBeaconEffect(iBlockID, -1, effectClass);
   }

   public static void addBeaconEffect(int iBlockID, int iBlockMetadata, BeaconEffect effectClass) {
      beaconEffectsByBlockID[iBlockID].add(new BeaconEffectDescriptor(iBlockMetadata, effectClass));
   }

   public static void initializeEffectsByBlockID() {
      for (int i = 0; i < 4096; i++) {
         beaconEffectsByBlockID[i] = new ArrayList<>();
      }

      addBeaconEffect(Block.blockGold.blockID, HASTE_EFFECT);
      addBeaconEffect(Block.blockDiamond.blockID, FORTUNE_EFFECT);
      addBeaconEffect(Block.blockEmerald.blockID, LOOTING_EFFECT);
      addBeaconEffect(Block.blockLapis.blockID, TRUE_SIGHT_EFFECT);
      addBeaconEffect(Block.glowStone.blockID, NIGHT_VISION_EFFECT);
      addBeaconEffect(BTWBlocks.aestheticOpaque.blockID, 3, FIRE_RESIST_EFFECT);
      addBeaconEffect(Block.blockIron.blockID, MAGNETIC_POLE_EFFECT);
      addBeaconEffect(BTWBlocks.soulforgedSteelBlock.blockID, SPAWN_ANCHOR_EFFECT);
      addBeaconEffect(Block.glass.blockID, DECORATIVE_EFFECT);
      addBeaconEffect(BTWBlocks.aestheticEarth.blockID, 7, NAUSEA_EFFECT);
      addBeaconEffect(BTWBlocks.aestheticOpaque.blockID, 14, ENDER_ANTENNA_EFFECT);
      addBeaconEffect(BTWBlocks.companionCube.blockID, COMPANION_EFFECT);
      addBeaconEffect(BTWBlocks.spiderEyeBlock.blockID, JUNGLE_SPIDER_REPELLENT);
   }
}
