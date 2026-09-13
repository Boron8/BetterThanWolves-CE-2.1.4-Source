package btw.entity.mechanical.source;

import btw.block.BTWBlocks;
import btw.block.blocks.AxleBlock;
import btw.block.util.MechPowerUtils;
import btw.item.BTWItems;
import btw.world.util.BlockPos;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import net.minecraft.src.AxisAlignedBB;
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

public class VerticalWindMillEntity extends MechanicalPowerSourceEntity {
   public static final float HEIGHT = 6.8F;
   public static final float WIDTH = 8.8F;
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
   private static final int BLADE_COLOR_4_DATA_WATCHER_ID = 27;
   private static final int BLADE_COLOR_5_DATA_WATCHER_ID = 28;
   private static final int BLADE_COLOR_6_DATA_WATCHER_ID = 29;
   private static final int BLADE_COLOR_7_DATA_WATCHER_ID = 30;
   private int currentBladeColoringIndex;
   protected int overpowerTimer;

   public VerticalWindMillEntity(World world) {
      super(world);
      this.currentBladeColoringIndex = 0;
   }

   public VerticalWindMillEntity(World world, double x, double y, double z) {
      super(world, x, y, z);
   }

   @Override
   protected void entityInit() {
      super.entityInit();
      this.dataWatcher.addObject(23, new Byte((byte)0));
      this.dataWatcher.addObject(24, new Byte((byte)0));
      this.dataWatcher.addObject(25, new Byte((byte)0));
      this.dataWatcher.addObject(26, new Byte((byte)0));
      this.dataWatcher.addObject(27, new Byte((byte)0));
      this.dataWatcher.addObject(28, new Byte((byte)0));
      this.dataWatcher.addObject(29, new Byte((byte)0));
      this.dataWatcher.addObject(30, new Byte((byte)0));
   }

   public int getBladeColor(int iBladeIndex) {
      return this.dataWatcher.getWatchableObjectByte(23 + iBladeIndex);
   }

   public void setBladeColor(int iBladeIndex, int iColor) {
      this.dataWatcher.updateObject(23 + iBladeIndex, (byte)iColor);
   }

   @Override
   protected void writeEntityToNBT(NBTTagCompound nbttagcompound) {
      nbttagcompound.setFloat("fRotation", this.rotation);
      nbttagcompound.setBoolean("bProvidingPower", this.providingPower);
      nbttagcompound.setInteger("iOverpowerTimer", this.overpowerTimer);
      nbttagcompound.setInteger("iBladeColors0", this.getBladeColor(0));
      nbttagcompound.setInteger("iBladeColors1", this.getBladeColor(1));
      nbttagcompound.setInteger("iBladeColors2", this.getBladeColor(2));
      nbttagcompound.setInteger("iBladeColors3", this.getBladeColor(3));
      nbttagcompound.setInteger("iBladeColors4", this.getBladeColor(4));
      nbttagcompound.setInteger("iBladeColors5", this.getBladeColor(5));
      nbttagcompound.setInteger("iBladeColors6", this.getBladeColor(6));
      nbttagcompound.setInteger("iBladeColors7", this.getBladeColor(7));
   }

   @Override
   protected void readEntityFromNBT(NBTTagCompound nbttagcompound) {
      this.rotation = nbttagcompound.getFloat("fRotation");
      this.providingPower = nbttagcompound.getBoolean("bProvidingPower");
      this.overpowerTimer = nbttagcompound.getInteger("iOverpowerTimer");
      this.setBladeColor(0, nbttagcompound.getInteger("iBladeColors0"));
      this.setBladeColor(1, nbttagcompound.getInteger("iBladeColors1"));
      this.setBladeColor(2, nbttagcompound.getInteger("iBladeColors2"));
      this.setBladeColor(3, nbttagcompound.getInteger("iBladeColors3"));
      this.setBladeColor(4, nbttagcompound.getInteger("iBladeColors4"));
      this.setBladeColor(5, nbttagcompound.getInteger("iBladeColors5"));
      this.setBladeColor(6, nbttagcompound.getInteger("iBladeColors6"));
      this.setBladeColor(7, nbttagcompound.getInteger("iBladeColors7"));
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
            if (this.currentBladeColoringIndex >= 8) {
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
   public void setDead() {
      if (this.providingPower) {
         boolean[] m_bAxlesPresent = new boolean[7];

         for (int iTempIndex = 0; iTempIndex < 7; iTempIndex++) {
            m_bAxlesPresent[iTempIndex] = false;
         }

         int iCenterI = MathHelper.floor_double(this.posX);
         int iCenterJ = MathHelper.floor_double(this.posY);
         int iCenterK = MathHelper.floor_double(this.posZ);
         AxleBlock blockAxle = (AxleBlock)BTWBlocks.axlePowerSource;

         for (int iTempJ = iCenterJ - 2; iTempJ <= iCenterJ + 2; iTempJ++) {
            int iTempBlockID = this.worldObj.getBlockId(iCenterI, iTempJ, iCenterK);
            if (iTempBlockID == BTWBlocks.axlePowerSource.blockID) {
               int iAxisAlignment = blockAxle.getAxisAlignment(this.worldObj, iCenterI, iTempJ, iCenterK);
               if (iAxisAlignment == 0) {
                  int iAxleIndex = iTempJ - iCenterJ + 3;
                  m_bAxlesPresent[iAxleIndex] = true;
                  this.worldObj.setBlock(iCenterI, iTempJ, iCenterK, BTWBlocks.axle.blockID, 0, 2);
               }
            }
         }

         for (int iTempJx = iCenterJ - 3; iTempJx <= iCenterJ + 3; iTempJx += 6) {
            int iTempBlockID = this.worldObj.getBlockId(iCenterI, iTempJx, iCenterK);
            if (iTempBlockID == BTWBlocks.axlePowerSource.blockID) {
               int iAxisAlignment = blockAxle.getAxisAlignment(this.worldObj, iCenterI, iTempJx, iCenterK);
               if (iAxisAlignment == 0) {
                  this.worldObj.setBlock(iCenterI, iTempJx, iCenterK, BTWBlocks.axle.blockID, 0, 3);
               }
            }
         }
      }

      super.w();
   }

   @Override
   public void onStruckByLightning(EntityLightningBolt par1EntityLightningBolt) {
      if (this.worldObj.getGameRules().getGameRuleBooleanValue("doFireTick")) {
         this.setDead();
      }
   }

   @Override
   public float getWidth() {
      return 8.8F;
   }

   @Override
   public float getHeight() {
      return 6.8F;
   }

   @Override
   public float getDepth() {
      return 8.8F;
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
         this.a(BTWItems.verticalWindMill.itemID, 1, 0.0F);
         this.setDead();
      }
   }

   @Override
   public boolean validateAreaAroundDevice() {
      int iCenterI = MathHelper.floor_double(this.posX);
      int iCenterJ = MathHelper.floor_double(this.posY);
      int iCenterK = MathHelper.floor_double(this.posZ);
      return windMillValidateAreaAroundBlock(this.worldObj, iCenterI, iCenterJ, iCenterK);
   }

   @Override
   public float computeRotation() {
      int iCenterI = MathHelper.floor_double(this.posX);
      int iCenterJ = MathHelper.floor_double(this.posY);
      int iCenterK = MathHelper.floor_double(this.posZ);
      float fRotationAmount = 0.0F;
      if (this.worldObj.provider.dimensionId == -1) {
         fRotationAmount = -0.07F;
         this.overpowerTimer = -1;
      } else if (this.worldObj.provider.dimensionId == 1 || !this.canSeeSky()) {
         this.overpowerTimer = -1;
      } else if (this.worldObj.isThundering() && this.isBeingPrecipitatedOn()) {
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
   protected boolean validateConnectedAxles() {
      int iCenterI = MathHelper.floor_double(this.posX);
      int iCenterJ = MathHelper.floor_double(this.posY);
      int iCenterK = MathHelper.floor_double(this.posZ);

      for (int iTempJ = iCenterJ - 3; iTempJ <= iCenterJ + 3; iTempJ++) {
         int iTempBlockID = this.worldObj.getBlockId(iCenterI, iTempJ, iCenterK);
         if (!MechPowerUtils.isBlockIDAxle(iTempBlockID)) {
            return false;
         }

         int iAxisAlignment = ((AxleBlock)Block.blocksList[iTempBlockID]).getAxisAlignment(this.worldObj, iCenterI, iTempJ, iCenterK);
         if (iAxisAlignment != 0) {
            return false;
         }
      }

      if (!this.providingPower) {
         for (int iTempJ = iCenterJ - 3; iTempJ <= iCenterJ + 3; iTempJ++) {
            int iTempBlockIDx = this.worldObj.getBlockId(iCenterI, iTempJ, iCenterK);
            if (((AxleBlock)Block.blocksList[iTempBlockIDx]).getPowerLevel(this.worldObj, iCenterI, iTempJ, iCenterK) > 0) {
               return false;
            }
         }
      } else {
         for (int iTempJx = iCenterJ - 3; iTempJx <= iCenterJ + 3; iTempJx++) {
            int iTempBlockIDx = this.worldObj.getBlockId(iCenterI, iTempJx, iCenterK);
            if (iTempBlockIDx != BTWBlocks.axlePowerSource.blockID) {
               this.powerAxleColumn();
               break;
            }
         }
      }

      return true;
   }

   @Override
   public void transferPowerStateToConnectedAxles() {
      if (this.providingPower) {
         this.powerAxleColumn();
      } else {
         this.depowerAxleColumn();
      }
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
         dataStream.writeInt(9);
         dataStream.writeInt(this.entityId);
         dataStream.writeInt(MathHelper.floor_double(this.posX * 32.0));
         dataStream.writeInt(MathHelper.floor_double(this.posY * 32.0));
         dataStream.writeInt(MathHelper.floor_double(this.posZ * 32.0));
         dataStream.writeInt(this.getRotationSpeedScaled());
         dataStream.writeByte((byte)this.getBladeColor(0));
         dataStream.writeByte((byte)this.getBladeColor(1));
         dataStream.writeByte((byte)this.getBladeColor(2));
         dataStream.writeByte((byte)this.getBladeColor(3));
         dataStream.writeByte((byte)this.getBladeColor(4));
         dataStream.writeByte((byte)this.getBladeColor(5));
         dataStream.writeByte((byte)this.getBladeColor(6));
         dataStream.writeByte((byte)this.getBladeColor(7));
      } catch (Exception var4) {
         var4.printStackTrace();
      }

      return new Packet250CustomPayload("BTW|SE", byteStream.toByteArray());
   }

   private void powerAxleColumn() {
      int iCenterI = MathHelper.floor_double(this.posX);
      int iCenterJ = MathHelper.floor_double(this.posY);
      int iCenterK = MathHelper.floor_double(this.posZ);

      for (int iTempJ = iCenterJ - 2; iTempJ <= iCenterJ + 2; iTempJ++) {
         this.worldObj.setBlock(iCenterI, iTempJ, iCenterK, BTWBlocks.axlePowerSource.blockID, 0, 2);
      }

      this.worldObj.setBlock(iCenterI, iCenterJ + 3, iCenterK, BTWBlocks.axlePowerSource.blockID, 0, 3);
      this.worldObj.setBlock(iCenterI, iCenterJ + -3, iCenterK, BTWBlocks.axlePowerSource.blockID, 0, 3);
   }

   private void depowerAxleColumn() {
      int iCenterI = MathHelper.floor_double(this.posX);
      int iCenterJ = MathHelper.floor_double(this.posY);
      int iCenterK = MathHelper.floor_double(this.posZ);

      for (int iTempJ = iCenterJ - 2; iTempJ <= iCenterJ + 2; iTempJ++) {
         this.worldObj.setBlock(iCenterI, iTempJ, iCenterK, BTWBlocks.axle.blockID, 0, 2);
      }

      this.worldObj.setBlock(iCenterI, iCenterJ + 3, iCenterK, BTWBlocks.axle.blockID, 0, 3);
      this.worldObj.setBlock(iCenterI, iCenterJ + -3, iCenterK, BTWBlocks.axle.blockID, 0, 3);
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

   public static boolean windMillValidateAreaAroundBlock(World world, int i, int j, int k) {
      for (int iTempI = i - 4; iTempI <= i + 4; iTempI++) {
         for (int iTempJ = j - 3; iTempJ <= j + 3; iTempJ++) {
            for (int iTempK = k - 4; iTempK <= k + 4; iTempK++) {
               if ((iTempI != i || iTempK != k) && !isValidBlockForWindMillToOccupy(world, iTempI, iTempJ, iTempK)) {
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

   @Override
   public void initBoundingBox() {
      this.boundingBox
         .setBounds(
            this.posX - this.getWidth() * 0.5F,
            this.posY - this.getHeight() * 0.5F,
            this.posZ - this.getWidth() * 0.5F,
            this.posX + this.getWidth() * 0.5F,
            this.posY + this.getHeight() * 0.5F,
            this.posZ + this.getWidth() * 0.5F
         );
   }

   private boolean canSeeSky() {
      int iCenterI = MathHelper.floor_double(this.posX);
      int iCenterJ = MathHelper.floor_double(this.posY);
      int iCenterK = MathHelper.floor_double(this.posZ);

      for (int iTempI = iCenterI - 4; iTempI <= iCenterI + 4; iTempI++) {
         for (int iTempK = iCenterK - 4; iTempK <= iCenterK + 4; iTempK++) {
            if (this.worldObj.canBlockSeeTheSky(iTempI, iCenterJ, iTempK)) {
               return true;
            }
         }
      }

      return false;
   }

   private boolean isBeingPrecipitatedOn() {
      if (this.worldObj.isRaining()) {
         int iCenterI = MathHelper.floor_double(this.posX);
         int iCenterJ = MathHelper.floor_double(this.posY);
         int iCenterK = MathHelper.floor_double(this.posZ);

         for (int iTempI = iCenterI - 4; iTempI <= iCenterI + 4; iTempI++) {
            for (int iTempK = iCenterK - 4; iTempK <= iCenterK + 4; iTempK++) {
               if (this.worldObj.isPrecipitatingAtPos(iTempI, iTempK)) {
                  return true;
               }
            }
         }
      }

      return false;
   }

   @Override
   public AxisAlignedBB getDeviceBounds() {
      return AxisAlignedBB.getAABBPool()
         .getAABB(
            this.posX - this.getWidth() * 0.5F,
            this.posY - this.getHeight() * 0.5F,
            this.posZ - this.getWidth() * 0.5F,
            this.posX + this.getWidth() * 0.5F,
            this.posY + this.getHeight() * 0.5F,
            this.posZ + this.getWidth() * 0.5F
         );
   }
}
