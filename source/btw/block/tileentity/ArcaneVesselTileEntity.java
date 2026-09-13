package btw.block.tileentity;

import btw.block.blocks.ArcaneVesselBlock;
import btw.util.MiscUtils;
import btw.world.util.BlockPos;
import btw.world.util.WorldUtils;
import net.minecraft.src.Block;
import net.minecraft.src.EntityList;
import net.minecraft.src.EntityXPOrb;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.Packet;
import net.minecraft.src.Packet132TileEntityData;
import net.minecraft.src.TileEntity;
import net.minecraft.src.Vec3;
import net.minecraft.src.World;

public class ArcaneVesselTileEntity extends TileEntity implements TileEntityDataPacketHandler {
   public static final int MAX_CONTAINED_EXPERIENCE = 1000;
   private static final int MIN_TEMPLE_EXPERIENCE = 200;
   private static final int MAX_TEMPLE_EXPERIENCE = 256;
   public static final int MAX_VISUAL_EXPERIENCE_LEVEL = 10;
   private final int xpEjectUnitSize = 20;
   private int visualExperienceLevel = 0;
   private int containedRegularExperience = 0;
   private int containedDragonExperience = 0;

   @Override
   public void writeToNBT(NBTTagCompound nbttagcompound) {
      super.writeToNBT(nbttagcompound);
      nbttagcompound.setInteger("regXP", this.containedRegularExperience);
      nbttagcompound.setInteger("dragXP", this.containedDragonExperience);
   }

   @Override
   public void readFromNBT(NBTTagCompound nbttagcompound) {
      super.readFromNBT(nbttagcompound);
      this.containedRegularExperience = nbttagcompound.getInteger("regXP");
      this.containedDragonExperience = nbttagcompound.getInteger("dragXP");
      int iTotalExperience = this.containedRegularExperience + this.containedDragonExperience;
      this.visualExperienceLevel = (int)(10.0F * (iTotalExperience / 1000.0F));
      if (iTotalExperience > 0 && this.visualExperienceLevel == 0) {
         this.visualExperienceLevel = 1;
      }
   }

   @Override
   public Packet getDescriptionPacket() {
      NBTTagCompound nbttagcompound = new NBTTagCompound();
      nbttagcompound.setByte("x", (byte)this.visualExperienceLevel);
      return new Packet132TileEntityData(this.xCoord, this.yCoord, this.zCoord, 1, nbttagcompound);
   }

   @Override
   public void updateEntity() {
      if (!this.worldObj.isRemote) {
         int iBlockID = this.worldObj.getBlockId(this.xCoord, this.yCoord, this.zCoord);
         Block block = Block.blocksList[iBlockID];
         if (block != null && block instanceof ArcaneVesselBlock) {
            ArcaneVesselBlock vesselBlock = (ArcaneVesselBlock)block;
            if (vesselBlock.getMechanicallyPoweredFlag(this.worldObj, this.xCoord, this.yCoord, this.zCoord)) {
               int iTiltFacing = vesselBlock.getTiltFacing(this.worldObj, this.xCoord, this.yCoord, this.zCoord);
               this.attemptToSpillXPFromInv(iTiltFacing);
            }
         }
      }
   }

   @Override
   public void readNBTFromPacket(NBTTagCompound nbttagcompound) {
      this.visualExperienceLevel = nbttagcompound.getByte("x");
      this.worldObj.markBlockRangeForRenderUpdate(this.xCoord, this.yCoord, this.zCoord, this.xCoord, this.yCoord, this.zCoord);
   }

   public int getVisualExperienceLevel() {
      return this.visualExperienceLevel;
   }

   public int getContainedRegularExperience() {
      return this.containedRegularExperience;
   }

   public void setContainedRegularExperience(int iExperience) {
      this.containedRegularExperience = iExperience;
      this.validateVisualExperience();
   }

   public int getContainedDragonExperience() {
      return this.containedDragonExperience;
   }

   public void setContainedDragonExperience(int iExperience) {
      this.containedDragonExperience = iExperience;
      this.validateVisualExperience();
   }

   public int getContainedTotalExperience() {
      return this.containedDragonExperience + this.containedRegularExperience;
   }

   public void validateVisualExperience() {
      int iTotalExperience = this.containedRegularExperience + this.containedDragonExperience;
      int iNewVisualExperience = (int)(10.0F * (iTotalExperience / 1000.0F));
      if (iTotalExperience > 0 && iNewVisualExperience == 0) {
         iNewVisualExperience = 1;
      }

      if (iNewVisualExperience != this.visualExperienceLevel) {
         this.visualExperienceLevel = iNewVisualExperience;
         this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
      }
   }

   public void initTempleExperience() {
      this.setContainedRegularExperience(this.worldObj.rand.nextInt(56) + 200);
   }

   public boolean attemptToSwallowXPOrb(World world, int i, int j, int k, EntityXPOrb entityXPOrb) {
      int iTotalContainedXP = this.containedRegularExperience + this.containedDragonExperience;
      int iRemainingSpace = 1000 - iTotalContainedXP;
      if (iRemainingSpace > 0) {
         int iXPToAddToInventory = 0;
         boolean bIsDragonOrb = entityXPOrb.notPlayerOwned;
         if (entityXPOrb.xpValue <= iRemainingSpace) {
            iXPToAddToInventory = entityXPOrb.xpValue;
            entityXPOrb.w();
         } else {
            iXPToAddToInventory = iRemainingSpace;
         }

         if (bIsDragonOrb) {
            this.setContainedDragonExperience(this.containedDragonExperience + iXPToAddToInventory);
         } else {
            this.setContainedRegularExperience(this.containedRegularExperience + iXPToAddToInventory);
         }

         return true;
      } else {
         return false;
      }
   }

   private void attemptToSpillXPFromInv(int iTiltFacing) {
      int iXPToSpill = 0;
      boolean bSpillDragonOrb = false;
      if (this.containedDragonExperience > 0 || this.containedRegularExperience > 0 && !this.isTiltedOutputBlocked(iTiltFacing)) {
         if (this.containedDragonExperience > 0) {
            bSpillDragonOrb = true;
            if (this.containedDragonExperience < 20) {
               iXPToSpill = this.containedDragonExperience;
            } else {
               iXPToSpill = 20;
            }

            this.setContainedDragonExperience(this.containedDragonExperience - iXPToSpill);
         } else {
            if (this.containedRegularExperience < 20) {
               iXPToSpill = this.containedRegularExperience;
            } else {
               iXPToSpill = 20;
            }

            this.setContainedRegularExperience(this.containedRegularExperience - iXPToSpill);
         }
      }

      if (iXPToSpill > 0) {
         this.spillXPOrb(iXPToSpill, bSpillDragonOrb, iTiltFacing);
      }
   }

   private boolean isTiltedOutputBlocked(int iTiltFacing) {
      BlockPos targetPos = new BlockPos(this.xCoord, this.yCoord, this.zCoord);
      targetPos.addFacingAsOffset(iTiltFacing);
      if (!this.worldObj.isAirBlock(targetPos.x, targetPos.y, targetPos.z)
         && !WorldUtils.isReplaceableBlock(this.worldObj, targetPos.x, targetPos.y, targetPos.z)) {
         int iTargetBlockID = this.worldObj.getBlockId(targetPos.x, targetPos.y, targetPos.z);
         Block targetBlock = Block.blocksList[iTargetBlockID];
         if (targetBlock.blockMaterial.isSolid()) {
            return true;
         }
      }

      return false;
   }

   public void ejectContentsOnBlockBreak() {
      while (this.containedRegularExperience > 0) {
         int iEjectSize = 20;
         if (this.containedRegularExperience < 20) {
            iEjectSize = this.containedRegularExperience;
         }

         this.ejectXPOrbOnBlockBreak(iEjectSize, false);
         this.containedRegularExperience -= iEjectSize;
      }

      while (this.containedDragonExperience > 0) {
         int iEjectSize = 20;
         if (this.containedDragonExperience < 20) {
            iEjectSize = this.containedDragonExperience;
         }

         this.ejectXPOrbOnBlockBreak(iEjectSize, true);
         this.containedDragonExperience -= iEjectSize;
      }
   }

   private void spillXPOrb(int iXPValue, boolean bDragonOrb, int iFacing) {
      Vec3 itemPos = MiscUtils.convertBlockFacingToVector(iFacing);
      itemPos.xCoord *= 0.5;
      itemPos.yCoord *= 0.5;
      itemPos.zCoord *= 0.5;
      itemPos.xCoord = itemPos.xCoord + (this.xCoord + 0.5F);
      itemPos.yCoord = itemPos.yCoord + (this.yCoord + 0.25F);
      itemPos.zCoord = itemPos.zCoord + (this.zCoord + 0.5F + this.worldObj.rand.nextFloat() * 0.3F);
      if (!(itemPos.xCoord > 0.1F) && !(itemPos.xCoord < -0.1F)) {
         itemPos.zCoord = itemPos.zCoord + (this.worldObj.rand.nextFloat() * 0.5F - 0.25F);
      } else {
         itemPos.xCoord = itemPos.xCoord + (this.worldObj.rand.nextFloat() * 0.5F - 0.25F);
      }

      EntityXPOrb xpOrb = (EntityXPOrb)EntityList.createEntityOfType(
         EntityXPOrb.class, this.worldObj, itemPos.xCoord, itemPos.yCoord, itemPos.zCoord, iXPValue, bDragonOrb
      );
      Vec3 itemVel = MiscUtils.convertBlockFacingToVector(iFacing);
      itemVel.xCoord *= 0.1F;
      itemVel.yCoord *= 0.1F;
      itemVel.zCoord *= 0.1F;
      xpOrb.motionX = itemVel.xCoord;
      xpOrb.motionY = itemVel.yCoord;
      xpOrb.motionZ = itemVel.zCoord;
      this.worldObj.spawnEntityInWorld(xpOrb);
   }

   private void ejectXPOrbOnBlockBreak(int iXPValue, boolean bDragonOrb) {
      double xOffset = this.worldObj.rand.nextDouble() * 0.7 + 0.15;
      double yOffset = this.worldObj.rand.nextDouble() * 0.7 + 0.15;
      double zOffset = this.worldObj.rand.nextDouble() * 0.7 + 0.15;
      EntityXPOrb xpOrb = (EntityXPOrb)EntityList.createEntityOfType(
         EntityXPOrb.class, this.worldObj, this.xCoord + xOffset, this.yCoord + yOffset, this.zCoord + zOffset, iXPValue, bDragonOrb
      );
      xpOrb.motionX = (float)this.worldObj.rand.nextGaussian() * 0.05F;
      xpOrb.motionY = (float)this.worldObj.rand.nextGaussian() * 0.05F + 0.2F;
      xpOrb.motionZ = (float)this.worldObj.rand.nextGaussian() * 0.05F;
      this.worldObj.spawnEntityInWorld(xpOrb);
   }
}
