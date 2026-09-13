package btw.entity.mechanical.source;

import btw.block.BTWBlocks;
import btw.block.blocks.AxleBlock;
import btw.block.util.MechPowerUtils;
import btw.item.BTWItems;
import btw.world.util.BlockPos;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import net.minecraft.src.Block;
import net.minecraft.src.BlockCloth;
import net.minecraft.src.EntityLightningBolt;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.MathHelper;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.Packet;
import net.minecraft.src.Packet250CustomPayload;
import net.minecraft.src.World;

public class WindMillEntity extends MechanicalPowerSourceEntityHorizontal {
   public static final float HEIGHT = 12.8F;
   public static final float WIDTH = 12.8F;
   public static final float DEPTH = 0.8F;
   private static final int MAX_DAMAGE = 160;
   private static final float ROTATION_PER_TICK = -0.12F;
   private static final float ROTATION_PER_TICK_IN_STORM = -2.0F;
   private static final float ROTATION_PER_TICK_IN_HELL = -0.07F;
   private static final int TICKS_PER_FULL_UPDATE = 20;
   private static final int UPDATES_TO_OVERPOWER = 30;
   private static final int BLADE_COLOR_0_DATA_WATCHER_ID = 23;
   private static final int BLADE_COLOR_1_DATA_WATCHER_ID = 24;
   private static final int BLADE_COLOR_2_DATA_WATCHER_ID = 25;
   private static final int BLADE_COLOR_3_DATA_WATCHER_ID = 26;
   private int currentBladeColoringIndex;
   protected int overpowerTimer;
   private boolean legacyWindMill;

   public WindMillEntity(World world) {
      super(world);
      this.currentBladeColoringIndex = 0;
      this.legacyWindMill = false;
   }

   public WindMillEntity(World world, double x, double y, double z, boolean bIAligned) {
      super(world, x, y, z, bIAligned);
   }

   @Override
   protected void entityInit() {
      super.a();
      this.dataWatcher.addObject(23, new Byte((byte)0));
      this.dataWatcher.addObject(24, new Byte((byte)0));
      this.dataWatcher.addObject(25, new Byte((byte)0));
      this.dataWatcher.addObject(26, new Byte((byte)0));
   }

   public int getBladeColor(int iBladeIndex) {
      return this.dataWatcher.getWatchableObjectByte(23 + iBladeIndex);
   }

   public void setBladeColor(int iBladeIndex, int iColor) {
      this.dataWatcher.updateObject(23 + iBladeIndex, (byte)iColor);
   }

   @Override
   protected void writeEntityToNBT(NBTTagCompound nbttagcompound) {
      nbttagcompound.setBoolean("bWindMillIAligned", this.alignedToX);
      nbttagcompound.setFloat("fRotation", this.rotation);
      nbttagcompound.setBoolean("bProvidingPower", this.providingPower);
      nbttagcompound.setInteger("iOverpowerTimer", this.overpowerTimer);
      nbttagcompound.setInteger("iBladeColors0", this.getBladeColor(0));
      nbttagcompound.setInteger("iBladeColors1", this.getBladeColor(1));
      nbttagcompound.setInteger("iBladeColors2", this.getBladeColor(2));
      nbttagcompound.setInteger("iBladeColors3", this.getBladeColor(3));
      nbttagcompound.setBoolean("fcLegacy", this.legacyWindMill);
   }

   @Override
   protected void readEntityFromNBT(NBTTagCompound nbttagcompound) {
      this.alignedToX = nbttagcompound.getBoolean("bWindMillIAligned");
      this.rotation = nbttagcompound.getFloat("fRotation");
      this.providingPower = nbttagcompound.getBoolean("bProvidingPower");
      this.overpowerTimer = nbttagcompound.getInteger("iOverpowerTimer");
      this.setBladeColor(0, nbttagcompound.getInteger("iBladeColors0"));
      this.setBladeColor(1, nbttagcompound.getInteger("iBladeColors1"));
      this.setBladeColor(2, nbttagcompound.getInteger("iBladeColors2"));
      this.setBladeColor(3, nbttagcompound.getInteger("iBladeColors3"));
      if (nbttagcompound.hasKey("fcLegacy")) {
         this.legacyWindMill = nbttagcompound.getBoolean("fcLegacy");
      } else {
         this.legacyWindMill = true;
      }

      this.initBoundingBox();
   }

   @Override
   public boolean interact(EntityPlayer player) {
      ItemStack itemstack = player.inventory.getCurrentItem();
      if (itemstack != null && (itemstack.itemID == Item.dyePowder.itemID || itemstack.itemID == BTWItems.dung.itemID)) {
         if (!this.worldObj.isRemote) {
            int iColor = 0;
            if (itemstack.itemID == Item.dyePowder.itemID) {
               iColor = BlockCloth.getBlockFromDye(itemstack.getItemDamage());
            } else {
               iColor = 12;
            }

            this.setBladeColor(this.currentBladeColoringIndex, iColor);
            this.currentBladeColoringIndex++;
            if (this.currentBladeColoringIndex >= 4) {
               this.currentBladeColoringIndex = 0;
            }
         }

         itemstack.stackSize--;
         if (itemstack.stackSize == 0) {
            player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
         }

         return true;
      } else {
         return super.a_(player);
      }
   }

   @Override
   public void onStruckByLightning(EntityLightningBolt par1EntityLightningBolt) {
      if (this.worldObj.getGameRules().getGameRuleBooleanValue("doFireTick") && this.worldObj.getDifficulty().shouldLightningStartFires()) {
         this.w();
      }
   }

   @Override
   public float getWidth() {
      return 12.8F;
   }

   @Override
   public float getHeight() {
      return 12.8F;
   }

   @Override
   public float getDepth() {
      return 0.8F;
   }

   @Override
   public int getMaxDamage() {
      return 160;
   }

   @Override
   public int getTicksPerFullUpdate() {
      return 20;
   }

   @Override
   protected void onFullUpdateServer() {
      super.onFullUpdateServer();
      if (this.overpowerTimer >= 0) {
         if (this.overpowerTimer > 0) {
            this.overpowerTimer--;
         }

         if (this.overpowerTimer <= 0) {
            int iCenterI = MathHelper.floor_double(this.posX);
            int iCenterJ = MathHelper.floor_double(this.posY);
            int iCenterK = MathHelper.floor_double(this.posZ);
            ((AxleBlock)BTWBlocks.axle).overpower(this.worldObj, iCenterI, iCenterJ, iCenterK);
         }
      }
   }

   @Override
   public void destroyWithDrop() {
      if (!this.worldObj.isRemote && !this.isDead) {
         this.a(BTWItems.windMill.itemID, 1, 0.0F);
         this.w();
      }
   }

   @Override
   public boolean validateAreaAroundDevice() {
      int iCenterI = MathHelper.floor_double(this.posX);
      int iCenterJ = MathHelper.floor_double(this.posY);
      int iCenterK = MathHelper.floor_double(this.posZ);
      return windMillValidateAreaAroundBlock(this.worldObj, iCenterI, iCenterJ, iCenterK, this.alignedToX);
   }

   @Override
   public float computeRotation() {
      int iCenterI = MathHelper.floor_double(this.posX);
      int iCenterJ = MathHelper.floor_double(this.posY);
      int iCenterK = MathHelper.floor_double(this.posZ);
      float fRotationAmount = 0.0F;
      if (this.worldObj.provider.dimensionId == -1) {
         if (this.legacyWindMill) {
            fRotationAmount = -0.07F;
         }

         this.overpowerTimer = -1;
      } else if (this.worldObj.provider.dimensionId == 1 || !this.worldObj.canBlockSeeTheSky(iCenterI, iCenterJ, iCenterK)) {
         this.overpowerTimer = -1;
      } else if (this.worldObj.isThundering() && this.worldObj.isPrecipitatingAtPos(iCenterI, iCenterK)) {
         fRotationAmount = -2.0F;
         if (this.overpowerTimer < 0) {
            this.overpowerTimer = 30;
         }
      } else {
         fRotationAmount = -0.12F;
         this.overpowerTimer = -1;
      }

      return fRotationAmount;
   }

   @Override
   protected void onClientRotationOctantChange() {
      float fSpeed = this.getRotationSpeed();
      if (fSpeed < -0.12F) {
         int iCenterI = MathHelper.floor_double(this.posX);
         int iCenterJ = MathHelper.floor_double(this.posY);
         int iCenterK = MathHelper.floor_double(this.posZ);
         int iCenterBlockID = this.worldObj.getBlockId(iCenterI, iCenterJ, iCenterK);
         if (iCenterBlockID == BTWBlocks.axlePowerSource.blockID) {
            int iAxleAlignment = ((AxleBlock)BTWBlocks.axlePowerSource).getAxisAlignment(this.worldObj, iCenterI, iCenterJ, iCenterK);
            this.clientNotifyGearboxOfOverpoweredOctantChangeInDirection(iCenterI, iCenterJ, iCenterK, iAxleAlignment << 1);
            this.clientNotifyGearboxOfOverpoweredOctantChangeInDirection(iCenterI, iCenterJ, iCenterK, (iAxleAlignment << 1) + 1);
         }
      }
   }

   @Override
   public Packet getSpawnPacketForThisEntity() {
      ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
      DataOutputStream dataStream = new DataOutputStream(byteStream);

      try {
         byte bIAligned = 0;
         if (this.alignedToX) {
            bIAligned = 1;
         }

         dataStream.writeInt(1);
         dataStream.writeInt(this.entityId);
         dataStream.writeInt(MathHelper.floor_double(this.posX * 32.0));
         dataStream.writeInt(MathHelper.floor_double(this.posY * 32.0));
         dataStream.writeInt(MathHelper.floor_double(this.posZ * 32.0));
         dataStream.writeByte(bIAligned);
         dataStream.writeInt(this.getRotationSpeedScaled());
         dataStream.writeByte((byte)this.getBladeColor(0));
         dataStream.writeByte((byte)this.getBladeColor(1));
         dataStream.writeByte((byte)this.getBladeColor(2));
         dataStream.writeByte((byte)this.getBladeColor(3));
      } catch (Exception var4) {
         var4.printStackTrace();
      }

      return new Packet250CustomPayload("BTW|SE", byteStream.toByteArray());
   }

   protected void clientNotifyGearboxOfOverpoweredOctantChangeInDirection(int iCenterI, int iCenterJ, int iCenterK, int iFacing) {
      BlockPos tempPos = new BlockPos(iCenterI, iCenterJ, iCenterK);

      for (int iTempCount = 0; iTempCount < 10; iTempCount++) {
         tempPos.addFacingAsOffset(iFacing);
         int iTempBlockID = this.worldObj.getBlockId(tempPos.x, tempPos.y, tempPos.z);
         if (iTempBlockID != BTWBlocks.axle.blockID && iTempBlockID != BTWBlocks.axlePowerSource.blockID) {
            if (MechPowerUtils.isPoweredGearBox(this.worldObj, tempPos.x, tempPos.y, tempPos.z)) {
               this.worldObj
                  .playSound(tempPos.x + 0.5, tempPos.y + 0.5, tempPos.z + 0.5, "random.chestclosed", 1.5F, this.worldObj.rand.nextFloat() * 0.1F + 0.5F);
            }
            break;
         }

         if (!((AxleBlock)Block.blocksList[iTempBlockID]).isAxleOrientedTowardsFacing(this.worldObj, tempPos.x, tempPos.y, tempPos.z, iFacing)) {
            break;
         }
      }
   }

   public static boolean windMillValidateAreaAroundBlock(World world, int i, int j, int k, boolean bIAligned) {
      int iOffset;
      int kOffset;
      if (bIAligned) {
         iOffset = 0;
         kOffset = 1;
      } else {
         iOffset = 1;
         kOffset = 0;
      }

      for (int iHeightOffset = -6; iHeightOffset <= 6; iHeightOffset++) {
         for (int iWidthOffset = -6; iWidthOffset <= 6; iWidthOffset++) {
            if (iHeightOffset != 0 || iWidthOffset != 0) {
               int tempI = i + iOffset * iWidthOffset;
               int tempJ = j + iHeightOffset;
               int tempK = k + kOffset * iWidthOffset;
               if (!isValidBlockForWindMillToOccupy(world, tempI, tempJ, tempK)) {
                  return false;
               }
            }
         }
      }

      return true;
   }

   public static boolean isValidBlockForWindMillToOccupy(World world, int i, int j, int k) {
      return world.isAirBlock(i, j, k);
   }
}
