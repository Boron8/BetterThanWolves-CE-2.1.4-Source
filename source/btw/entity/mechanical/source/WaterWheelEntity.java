package btw.entity.mechanical.source;

import btw.item.BTWItems;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import net.minecraft.src.Block;
import net.minecraft.src.BlockFluid;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Material;
import net.minecraft.src.MathHelper;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.Packet;
import net.minecraft.src.Packet250CustomPayload;
import net.minecraft.src.Vec3;
import net.minecraft.src.World;

public class WaterWheelEntity extends MechanicalPowerSourceEntityHorizontal {
   public static final float HEIGHT = 4.8F;
   public static final float WIDTH = 4.8F;
   public static final float DEPTH = 0.8F;
   public static final int MAX_DAMAGE = 160;
   public static final float ROTATION_PER_TICK = 0.25F;
   public static final int TICKS_PER_FULL_UPDATE = 20;

   public WaterWheelEntity(World world) {
      super(world);
   }

   public WaterWheelEntity(World world, double x, double y, double z, boolean bIAligned) {
      super(world, x, y, z, bIAligned);
   }

   @Override
   protected void entityInit() {
      super.a();
   }

   @Override
   protected void writeEntityToNBT(NBTTagCompound nbttagcompound) {
      nbttagcompound.setBoolean("bWaterWheelIAligned", this.alignedToX);
      nbttagcompound.setFloat("fRotation", this.rotation);
      nbttagcompound.setBoolean("bProvidingPower", this.providingPower);
   }

   @Override
   protected void readEntityFromNBT(NBTTagCompound nbttagcompound) {
      this.alignedToX = nbttagcompound.getBoolean("bWaterWheelIAligned");
      this.rotation = nbttagcompound.getFloat("fRotation");
      this.providingPower = nbttagcompound.getBoolean("bProvidingPower");
      this.initBoundingBox();
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

         dataStream.writeInt(2);
         dataStream.writeInt(this.entityId);
         dataStream.writeInt(MathHelper.floor_double(this.posX * 32.0));
         dataStream.writeInt(MathHelper.floor_double(this.posY * 32.0));
         dataStream.writeInt(MathHelper.floor_double(this.posZ * 32.0));
         dataStream.writeByte(bIAligned);
         dataStream.writeInt(this.getRotationSpeedScaled());
      } catch (Exception var4) {
         var4.printStackTrace();
      }

      return new Packet250CustomPayload("BTW|SE", byteStream.toByteArray());
   }

   @Override
   public float getWidth() {
      return 4.8F;
   }

   @Override
   public float getHeight() {
      return 4.8F;
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
   public void destroyWithDrop() {
      if (!this.worldObj.isRemote && !this.isDead) {
         this.a(BTWItems.waterWheel.itemID, 1, 0.0F);
         this.w();
      }
   }

   @Override
   public boolean validateAreaAroundDevice() {
      int iCenterI = MathHelper.floor_double(this.posX);
      int iCenterJ = MathHelper.floor_double(this.posY);
      int iCenterK = MathHelper.floor_double(this.posZ);
      return waterWheelValidateAreaAroundBlock(this.worldObj, iCenterI, iCenterJ, iCenterK, this.alignedToX);
   }

   @Override
   public float computeRotation() {
      int iCenterI = MathHelper.floor_double(this.posX);
      int iCenterJ = MathHelper.floor_double(this.posY);
      int iCenterK = MathHelper.floor_double(this.posZ);
      float fRotationAmount = 0.0F;
      int iFlowJ = iCenterJ - 2;
      int iFlowBlockID = this.worldObj.getBlockId(iCenterI, iFlowJ, iCenterK);
      if (iFlowBlockID == Block.waterMoving.blockID || iFlowBlockID == Block.waterStill.blockID) {
         Vec3 flowVector = this.getFlowVector(this.worldObj, iCenterI, iFlowJ, iCenterK);
         if (this.alignedToX) {
            if (flowVector.zCoord > 0.33F) {
               fRotationAmount = -0.25F;
            } else if (flowVector.zCoord < -0.33F) {
               fRotationAmount = 0.25F;
            }
         } else if (flowVector.xCoord > 0.33F) {
            fRotationAmount = 0.25F;
         } else if (flowVector.xCoord < -0.33F) {
            fRotationAmount = -0.25F;
         }
      }

      int kOffset;
      int iOffset;
      if (this.alignedToX) {
         iOffset = 0;
         kOffset = 2;
      } else {
         iOffset = 2;
         kOffset = 0;
      }

      iFlowBlockID = this.worldObj.getBlockId(iCenterI + iOffset, iCenterJ, iCenterK - kOffset);
      if (iFlowBlockID == Block.waterMoving.blockID || iFlowBlockID == Block.waterStill.blockID) {
         BlockFluid fluidBlock = (BlockFluid)Block.blocksList[iFlowBlockID];
         fRotationAmount -= 0.25F;
      }

      iFlowBlockID = this.worldObj.getBlockId(iCenterI - iOffset, iCenterJ, iCenterK + kOffset);
      if (iFlowBlockID == Block.waterMoving.blockID || iFlowBlockID == Block.waterStill.blockID) {
         BlockFluid fluidBlock = (BlockFluid)Block.blocksList[iFlowBlockID];
         fRotationAmount += 0.25F;
      }

      if (fRotationAmount > 0.25F) {
         fRotationAmount = 0.25F;
      } else if (fRotationAmount <= -0.25F) {
         fRotationAmount = -0.25F;
      }

      return fRotationAmount;
   }

   public static boolean waterWheelValidateAreaAroundBlock(World world, int i, int j, int k, boolean bIAligned) {
      int iOffset;
      int kOffset;
      if (bIAligned) {
         iOffset = 0;
         kOffset = 1;
      } else {
         iOffset = 1;
         kOffset = 0;
      }

      for (int iHeightOffset = -2; iHeightOffset <= 2; iHeightOffset++) {
         for (int iWidthOffset = -2; iWidthOffset <= 2; iWidthOffset++) {
            if (iHeightOffset != 0 || iWidthOffset != 0) {
               int tempI = i + iOffset * iWidthOffset;
               int tempJ = j + iHeightOffset;
               int tempK = k + kOffset * iWidthOffset;
               if (!isValidBlockForWaterWheelToOccupy(world, tempI, tempJ, tempK)) {
                  return false;
               }
            }
         }
      }

      return true;
   }

   public static boolean isValidBlockForWaterWheelToOccupy(World world, int i, int j, int k) {
      if (!world.isAirBlock(i, j, k)) {
         int iBlockID = world.getBlockId(i, j, k);
         if (iBlockID != Block.waterMoving.blockID && iBlockID != Block.waterStill.blockID) {
            return false;
         }
      }

      return true;
   }

   private Vec3 getFlowVector(IBlockAccess par1IBlockAccess, int par2, int par3, int par4) {
      Vec3 vec3 = par1IBlockAccess.getWorldVec3Pool().getVecFromPool(0.0, 0.0, 0.0);
      int i = this.getEffectiveFlowDecay(par1IBlockAccess, par2, par3, par4);

      for (int j = 0; j < 4; j++) {
         int k = par2;
         int i1 = par4;
         if (j == 0) {
            k = par2 - 1;
         }

         if (j == 1) {
            i1 = par4 - 1;
         }

         if (j == 2) {
            k++;
         }

         if (j == 3) {
            i1++;
         }

         int j1 = this.getEffectiveFlowDecay(par1IBlockAccess, k, par3, i1);
         if (j1 < 0) {
            if (!par1IBlockAccess.getBlockMaterial(k, par3, i1).blocksMovement()) {
               j1 = this.getEffectiveFlowDecay(par1IBlockAccess, k, par3 - 1, i1);
               if (j1 >= 0) {
                  int k1 = j1 - (i - 8);
                  vec3 = vec3.addVector((k - par2) * k1, (par3 - par3) * k1, (i1 - par4) * k1);
               }
            }
         } else if (j1 >= 0) {
            int l1 = j1 - i;
            vec3 = vec3.addVector((k - par2) * l1, (par3 - par3) * l1, (i1 - par4) * l1);
         }
      }

      if (par1IBlockAccess.getBlockMetadata(par2, par3, par4) >= 8) {
         boolean flag = false;
         if (flag || this.isBlockSolid(par1IBlockAccess, par2, par3, par4 - 1, 2)) {
            flag = true;
         }

         if (flag || this.isBlockSolid(par1IBlockAccess, par2, par3, par4 + 1, 3)) {
            flag = true;
         }

         if (flag || this.isBlockSolid(par1IBlockAccess, par2 - 1, par3, par4, 4)) {
            flag = true;
         }

         if (flag || this.isBlockSolid(par1IBlockAccess, par2 + 1, par3, par4, 5)) {
            flag = true;
         }

         if (flag || this.isBlockSolid(par1IBlockAccess, par2, par3 + 1, par4 - 1, 2)) {
            flag = true;
         }

         if (flag || this.isBlockSolid(par1IBlockAccess, par2, par3 + 1, par4 + 1, 3)) {
            flag = true;
         }

         if (flag || this.isBlockSolid(par1IBlockAccess, par2 - 1, par3 + 1, par4, 4)) {
            flag = true;
         }

         if (flag || this.isBlockSolid(par1IBlockAccess, par2 + 1, par3 + 1, par4, 5)) {
            flag = true;
         }

         if (flag) {
            vec3 = vec3.normalize().addVector(0.0, -6.0, 0.0);
         }
      }

      return vec3.normalize();
   }

   private boolean isBlockSolid(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5) {
      Material material = par1IBlockAccess.getBlockMaterial(par2, par3, par4);
      if (material == Block.waterMoving.blockMaterial) {
         return false;
      } else if (par5 == 1) {
         return true;
      } else {
         return material == Material.ice ? false : par1IBlockAccess.getBlockMaterial(par2, par3, par4).isSolid();
      }
   }

   private int getEffectiveFlowDecay(IBlockAccess iblockaccess, int i, int j, int k) {
      if (iblockaccess.getBlockMaterial(i, j, k) != Block.waterMoving.blockMaterial) {
         return -1;
      } else {
         int l = iblockaccess.getBlockMetadata(i, j, k);
         if (l >= 8) {
            l = 0;
         }

         return l;
      }
   }
}
