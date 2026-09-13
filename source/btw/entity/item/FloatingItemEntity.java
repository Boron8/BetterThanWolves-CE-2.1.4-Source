package btw.entity.item;

import btw.entity.EntityWithCustomPacket;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.EntityItem;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.MathHelper;
import net.minecraft.src.Packet;
import net.minecraft.src.Packet250CustomPayload;
import net.minecraft.src.World;

public class FloatingItemEntity extends EntityItem implements EntityWithCustomPacket {
   public FloatingItemEntity(World world, double dPosX, double dPosY, double dPosZ, ItemStack itemStack) {
      super(world, dPosX, dPosY, dPosZ, itemStack);
   }

   public FloatingItemEntity(World world) {
      super(world);
   }

   @Override
   public void onUpdate() {
      this.x();
      if (this.delayBeforeCanPickup > 0) {
         this.delayBeforeCanPickup--;
      }

      this.prevPosX = this.posX;
      this.prevPosY = this.posY;
      this.prevPosZ = this.posZ;
      int numDepthChecks = 5;
      double d = 0.0;
      double dBoundingYOffset = 0.1;

      for (int j = 0; j < numDepthChecks; j++) {
         double d2 = this.boundingBox.minY + (this.boundingBox.maxY - this.boundingBox.minY) * (j + 0) + dBoundingYOffset;
         double d8 = this.boundingBox.minY + (this.boundingBox.maxY - this.boundingBox.minY) * (j + 1) + dBoundingYOffset;
         AxisAlignedBB axisalignedbb = AxisAlignedBB.getAABBPool()
            .getAABB(this.boundingBox.minX, d2, this.boundingBox.minZ, this.boundingBox.maxX, d8, this.boundingBox.maxZ);
         if (this.worldObj.isAABBInMaterial(axisalignedbb, Material.water)) {
            d += 1.0 / numDepthChecks;
         }
      }

      if (this.worldObj.getBlockMaterial(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ))
         == Material.lava) {
         this.motionY = 0.2000000029802322;
         this.motionX = (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F;
         this.motionZ = (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F;
         this.worldObj.playSoundAtEntity(this, "random.fizz", 0.4F, 2.0F + this.rand.nextFloat() * 0.4F);
      } else if (d < 1.0) {
         double d6 = d * 2.0 - 1.0;
         this.motionY += 0.04 * d6;
      } else {
         if (this.motionY < 0.0) {
            this.motionY /= 2.0;
         }

         this.motionY += 0.007;
      }

      if (!this.worldObj.isRemote) {
         this.i(this.posX, (this.boundingBox.minY + this.boundingBox.maxY) / 2.0, this.posZ);
      }

      this.d(this.motionX, this.motionY, this.motionZ);
      float f = 0.98F;
      if (this.onGround) {
         f = 0.5880001F;
         int i = this.worldObj
            .getBlockId(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.boundingBox.minY) - 1, MathHelper.floor_double(this.posZ));
         if (i > 0) {
            f = Block.blocksList[i].slipperiness * 0.98F;
         }
      }

      this.motionX *= f;
      this.motionY *= 0.98F;
      this.motionZ *= f;
      if (this.onGround) {
         this.motionY *= -0.5;
      }

      this.age++;
      if (this.age >= 6000) {
         this.w();
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
         dataStream.writeInt(5);
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
}
