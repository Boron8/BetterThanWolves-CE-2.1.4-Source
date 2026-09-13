package btw.entity.item;

import btw.block.BTWBlocks;
import btw.block.blocks.PlanterBlock;
import btw.entity.EntityWithCustomPacket;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import net.minecraft.src.Block;
import net.minecraft.src.EntityItem;
import net.minecraft.src.ItemStack;
import net.minecraft.src.MathHelper;
import net.minecraft.src.Packet;
import net.minecraft.src.Packet250CustomPayload;
import net.minecraft.src.World;

public class BloodWoodSaplingItemEntity extends EntityItem implements EntityWithCustomPacket {
   public BloodWoodSaplingItemEntity(World world, double dPosX, double dPosY, double dPosZ, ItemStack itemStack) {
      super(world, dPosX, dPosY, dPosZ, itemStack);
      this.isImmuneToFire = true;
   }

   public BloodWoodSaplingItemEntity(World world) {
      super(world);
      this.isImmuneToFire = true;
   }

   @Override
   public void onUpdate() {
      super.onUpdate();
      if (!this.isDead && !this.worldObj.isRemote && this.onGround) {
         int i = MathHelper.floor_double(this.posX);
         int iBlockBelowJ = MathHelper.floor_double(this.boundingBox.minY - 0.1F);
         int k = MathHelper.floor_double(this.posZ);
         int iBlockBelowID = this.worldObj.getBlockId(i, iBlockBelowJ, k);
         this.checkForBloodWoodPlant(i, iBlockBelowJ, k);
      }
   }

   @Override
   public int getTrackerViewDistance() {
      return 64;
   }

   @Override
   public int getTrackerUpdateFrequency() {
      return 20;
   }

   @Override
   public boolean getTrackMotion() {
      return true;
   }

   @Override
   public boolean shouldServerTreatAsOversized() {
      return false;
   }

   @Override
   public Packet getSpawnPacketForThisEntity() {
      ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
      DataOutputStream dataStream = new DataOutputStream(byteStream);

      try {
         dataStream.writeInt(4);
         dataStream.writeInt(this.entityId);
         dataStream.writeInt(MathHelper.floor_double(this.posX * 32.0));
         dataStream.writeInt(MathHelper.floor_double(this.posY * 32.0));
         dataStream.writeInt(MathHelper.floor_double(this.posZ * 32.0));
         dataStream.writeInt(this.d().itemID);
         dataStream.writeInt(this.d().stackSize);
         dataStream.writeInt(this.d().getItemDamage());
         dataStream.writeByte((byte)(this.motionX * 128.0));
         dataStream.writeByte((byte)(this.motionY * 128.0));
         dataStream.writeByte((byte)(this.motionZ * 128.0));
      } catch (Exception var4) {
         var4.printStackTrace();
      }

      return new Packet250CustomPayload("BTW|SE", byteStream.toByteArray());
   }

   public void checkForBloodWoodPlant(int i, int j, int k) {
      Block blockAbove = Block.blocksList[this.worldObj.getBlockId(i, j + 1, k)];
      if (blockAbove == null || blockAbove.isAirBlock() || blockAbove.isGroundCover()) {
         int iBlockID = this.worldObj.getBlockId(i, j, k);
         if (iBlockID == Block.slowSand.blockID
            || iBlockID == BTWBlocks.planter.blockID && ((PlanterBlock)BTWBlocks.planter).getPlanterType(this.worldObj, i, j, k) == 8) {
            this.worldObj.setBlockAndMetadataWithNotify(i, j + 1, k, BTWBlocks.aestheticVegetation.blockID, 2);
            this.d().stackSize--;
            if (this.d().stackSize <= 0) {
               this.w();
            }
         }
      }
   }
}
