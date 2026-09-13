package btw.entity;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityAuraFX;
import net.minecraft.src.EntityFX;
import net.minecraft.src.EntityList;
import net.minecraft.src.EntitySmokeFX;
import net.minecraft.src.MathHelper;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.Packet;
import net.minecraft.src.Packet250CustomPayload;
import net.minecraft.src.World;

public class SoulSandEntity extends Entity implements EntityWithCustomPacket {
   private double targetX;
   private double targetY;
   private double targetZ;
   private int despawnTimer;

   public SoulSandEntity(World world) {
      super(world);
      this.a(0.25F, 0.25F);
   }

   public SoulSandEntity(World world, double x, double y, double z) {
      this(world);
      this.b(x, y, z);
      this.yOffset = 0.0F;
      this.despawnTimer = 0;
   }

   @Override
   protected void entityInit() {
   }

   @Override
   public void setVelocity(double par1, double par3, double par5) {
      this.motionX = par1;
      this.motionY = par3;
      this.motionZ = par5;
      if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F) {
         float var7 = MathHelper.sqrt_double(par1 * par1 + par5 * par5);
         this.prevRotationYaw = this.rotationYaw = (float)(Math.atan2(par1, par5) * 180.0 / Math.PI);
         this.prevRotationPitch = this.rotationPitch = (float)(Math.atan2(par3, var7) * 180.0 / Math.PI);
      }
   }

   @Override
   public void onUpdate() {
      this.lastTickPosX = this.posX;
      this.lastTickPosY = this.posY;
      this.lastTickPosZ = this.posZ;
      super.onUpdate();
      this.posX = this.posX + this.motionX;
      this.posY = this.posY + this.motionY;
      this.posZ = this.posZ + this.motionZ;
      float var1 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
      this.rotationYaw = (float)(Math.atan2(this.motionX, this.motionZ) * 180.0 / Math.PI);
      this.rotationPitch = (float)(Math.atan2(this.motionY, var1) * 180.0 / Math.PI);

      while (this.rotationPitch - this.prevRotationPitch < -180.0F) {
         this.prevRotationPitch -= 360.0F;
      }

      while (this.rotationPitch - this.prevRotationPitch >= 180.0F) {
         this.prevRotationPitch += 360.0F;
      }

      while (this.rotationYaw - this.prevRotationYaw < -180.0F) {
         this.prevRotationYaw -= 360.0F;
      }

      while (this.rotationYaw - this.prevRotationYaw >= 180.0F) {
         this.prevRotationYaw += 360.0F;
      }

      this.rotationPitch = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * 0.2F;
      this.rotationYaw = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * 0.2F;
      if (!this.worldObj.isRemote) {
         double var2 = this.targetX - this.posX;
         double var4 = this.targetZ - this.posZ;
         float var6 = (float)Math.sqrt(var2 * var2 + var4 * var4);
         float var7 = (float)Math.atan2(var4, var2);
         double var8 = var1 + (var6 - var1) * 6.0E-4;
         if (var6 < 1.0F) {
            var8 *= 0.8;
            this.motionY *= 0.8;
         }

         this.motionX = Math.cos(var7) * var8;
         this.motionZ = Math.sin(var7) * var8;
         if (this.posY < this.targetY) {
            this.motionY = this.motionY + (1.0 - this.motionY) * 0.015F;
         } else {
            this.motionY = this.motionY + (-1.0 - this.motionY) * 0.015F;
         }

         this.b(this.posX, this.posY, this.posZ);
         this.despawnTimer++;
         if (this.despawnTimer > 40 && !this.worldObj.isRemote) {
            this.w();
            if (var6 >= 1.0F) {
               this.worldObj.playAuxSFX(2228, (int)Math.round(this.posX), (int)Math.round(this.posY), (int)Math.round(this.posZ), 0);
            }
         }
      } else {
         this.clientUpdateParticles();
      }
   }

   @Override
   public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound) {
   }

   @Override
   public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound) {
   }

   @Override
   public float getShadowSize() {
      return 0.0F;
   }

   @Override
   public float getBrightness(float par1) {
      return 1.0F;
   }

   @Override
   public int getBrightnessForRender(float par1) {
      return 15728880;
   }

   @Override
   public boolean canAttackWithItem() {
      return false;
   }

   @Override
   public int getTrackerViewDistance() {
      return 64;
   }

   @Override
   public int getTrackerUpdateFrequency() {
      return 4;
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
         dataStream.writeInt(11);
         dataStream.writeInt(this.entityId);
         dataStream.writeInt(MathHelper.floor_double(this.posX * 32.0));
         dataStream.writeInt(MathHelper.floor_double(this.posY * 32.0));
         dataStream.writeInt(MathHelper.floor_double(this.posZ * 32.0));
         dataStream.writeByte((byte)(this.motionX * 128.0));
         dataStream.writeByte((byte)(this.motionY * 128.0));
         dataStream.writeByte((byte)(this.motionZ * 128.0));
      } catch (Exception var4) {
         var4.printStackTrace();
      }

      return new Packet250CustomPayload("BTW|SE", byteStream.toByteArray());
   }

   public void moveTowards(double x, double z) {
      double deltaX = x - this.posX;
      double deltaY = z - this.posZ;
      float fDistance = MathHelper.sqrt_double(deltaX * deltaX + deltaY * deltaY);
      if (fDistance > 3.0F) {
         this.targetX = this.posX + deltaX / fDistance * 3.0;
         this.targetZ = this.posZ + deltaY / fDistance * 3.0;
      } else {
         this.targetX = x;
         this.targetZ = z;
      }

      this.targetY = this.posY + 0.1;
      this.despawnTimer = 0;
   }

   @Environment(EnvType.CLIENT)
   private void clientUpdateParticles() {
      float var10 = 0.25F;

      for (int iTempCount = 0; iTempCount < 10; iTempCount++) {
         EntityFX particleEntity = (EntityFX)EntityList.createEntityOfType(
            EntitySmokeFX.class,
            this.worldObj,
            this.posX - this.motionX * var10 + this.rand.nextDouble() * 0.6 - 0.3,
            this.posY - this.motionY * var10 + this.rand.nextDouble() * 0.6 - 0.3 - 0.1,
            this.posZ - this.motionZ * var10 + this.rand.nextDouble() * 0.6 - 0.3,
            this.motionX,
            this.motionY,
            this.motionZ,
            0.33F
         );
         if (this.rand.nextInt(8) == 0) {
            particleEntity.setRBGColorF(1.0F, 1.0F, 1.0F);
         }

         Minecraft.getMinecraft().effectRenderer.addEffect(particleEntity);
         particleEntity = (EntityFX)EntityList.createEntityOfType(
            EntityAuraFX.class,
            this.worldObj,
            this.posX - this.motionX * var10 + this.rand.nextDouble() * 0.6 - 0.3,
            this.posY - this.motionY * var10 - 0.5,
            this.posZ - this.motionZ * var10 + this.rand.nextDouble() * 0.6 - 0.3,
            this.motionX,
            this.motionY,
            this.motionZ
         );
         float fColorMultiplier = 0.1F + this.rand.nextFloat() * 0.9F;
         particleEntity.setRBGColorF(0.42352942F * fColorMultiplier, 0.30588236F * fColorMultiplier, 0.23529412F * fColorMultiplier);
         particleEntity.particleScale *= 0.25F;
         Minecraft.getMinecraft().effectRenderer.addEffect(particleEntity);
      }
   }
}
