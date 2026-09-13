package net.minecraft.src;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;

@Environment(EnvType.CLIENT)
public class EntityClientPlayerMP extends EntityPlayerSP {
   public NetClientHandler sendQueue;
   private double oldPosX;
   private double oldMinY;
   private double oldPosY;
   private double oldPosZ;
   private float oldRotationYaw;
   private float oldRotationPitch;
   private boolean wasOnGround = false;
   private boolean shouldStopSneaking = false;
   private boolean wasSneaking = false;
   private boolean isUsingSpecial = false;
   private int field_71168_co = 0;
   private boolean hasSetHealth = false;

   public EntityClientPlayerMP(Minecraft par1Minecraft, World par2World, Session par3Session, NetClientHandler par4NetClientHandler) {
      super(par1Minecraft, par2World, par3Session, 0);
      this.sendQueue = par4NetClientHandler;
   }

   @Override
   public boolean attackEntityFrom(DamageSource par1DamageSource, int par2) {
      return false;
   }

   @Override
   public void heal(int par1) {
   }

   @Override
   public void onUpdate() {
      if (this.worldObj.blockExists(MathHelper.floor_double(this.posX), 0, MathHelper.floor_double(this.posZ))) {
         super.l_();
         this.sendMotionUpdates();
      }
   }

   public void sendMotionUpdates() {
      int actionState = -1;
      boolean sneaking = this.ah();
      if (sneaking != this.wasSneaking) {
         if (sneaking) {
            actionState = 4;
         } else {
            actionState = 5;
         }

         this.wasSneaking = sneaking;
      }

      boolean sprinting = this.ag();
      if (sprinting != this.shouldStopSneaking) {
         if (sprinting) {
            actionState = 1;
         } else {
            actionState = 2;
         }

         this.shouldStopSneaking = sprinting;
      }

      boolean usingSpecial = this.isUsingSpecialKey();
      if (usingSpecial != this.isUsingSpecial) {
         if (actionState == -1) {
            actionState = 0;
         }

         if (usingSpecial) {
            actionState |= 8;
         }

         this.isUsingSpecial = usingSpecial;
      }

      if (actionState != -1) {
         this.sendQueue.addToSendQueue(new Packet19EntityAction(this, actionState));
      }

      double var3 = this.posX - this.oldPosX;
      double var5 = this.boundingBox.minY - this.oldMinY;
      double var7 = this.posZ - this.oldPosZ;
      double var9 = this.rotationYaw - this.oldRotationYaw;
      double var11 = this.rotationPitch - this.oldRotationPitch;
      boolean var13 = var3 * var3 + var5 * var5 + var7 * var7 > 9.0E-4 || this.field_71168_co >= 20;
      boolean var14 = var9 != 0.0 || var11 != 0.0;
      if (this.ridingEntity != null) {
         this.sendQueue
            .addToSendQueue(new Packet13PlayerLookMove(this.motionX, -999.0, -999.0, this.motionZ, this.rotationYaw, this.rotationPitch, this.onGround));
         var13 = false;
      } else if (var13 && var14) {
         this.sendQueue
            .addToSendQueue(
               new Packet13PlayerLookMove(this.posX, this.boundingBox.minY, this.posY, this.posZ, this.rotationYaw, this.rotationPitch, this.onGround)
            );
      } else if (var13) {
         this.sendQueue.addToSendQueue(new Packet11PlayerPosition(this.posX, this.boundingBox.minY, this.posY, this.posZ, this.onGround));
      } else if (var14) {
         this.sendQueue.addToSendQueue(new Packet12PlayerLook(this.rotationYaw, this.rotationPitch, this.onGround));
      } else {
         this.sendQueue.addToSendQueue(new Packet10Flying(this.onGround));
      }

      this.field_71168_co++;
      this.wasOnGround = this.onGround;
      if (var13) {
         this.oldPosX = this.posX;
         this.oldMinY = this.boundingBox.minY;
         this.oldPosY = this.posY;
         this.oldPosZ = this.posZ;
         this.field_71168_co = 0;
      }

      if (var14) {
         this.oldRotationYaw = this.rotationYaw;
         this.oldRotationPitch = this.rotationPitch;
      }
   }

   @Override
   public EntityItem dropOneItem(boolean par1) {
      int var2 = par1 ? 3 : 4;
      this.sendQueue.addToSendQueue(new Packet14BlockDig(var2, 0, 0, 0, 0));
      return null;
   }

   @Override
   protected void joinEntityItemWithWorld(EntityItem par1EntityItem) {
   }

   public void sendChatMessage(String par1Str) {
      this.sendQueue.addToSendQueue(new Packet3Chat(par1Str));
   }

   @Override
   public void swingItem() {
      super.bK();
      this.sendQueue.addToSendQueue(new Packet18Animation(this, 1));
   }

   @Override
   public void respawnPlayer() {
      this.sendQueue.addToSendQueue(new Packet205ClientCommand(1));
   }

   @Override
   protected void damageEntity(DamageSource par1DamageSource, int par2) {
      if (!this.aq()) {
         this.b(this.aX() - par2);
      }
   }

   @Override
   public void closeScreen() {
      this.sendQueue.addToSendQueue(new Packet101CloseWindow(this.openContainer.windowId));
      this.func_92015_f();
   }

   public void func_92015_f() {
      this.inventory.setItemStack((ItemStack)null);
      super.closeScreen();
   }

   @Override
   public void setHealth(int par1) {
      if (this.hasSetHealth) {
         super.setHealth(par1);
      } else {
         this.b(par1);
         this.hasSetHealth = true;
      }
   }

   @Override
   public void addStat(StatBase par1StatBase, int par2) {
      if (par1StatBase != null && par1StatBase.isIndependent) {
         super.addStat(par1StatBase, par2);
      }
   }

   public void incrementStat(StatBase par1StatBase, int par2) {
      if (par1StatBase != null && !par1StatBase.isIndependent) {
         super.addStat(par1StatBase, par2);
      }
   }

   @Override
   public void sendPlayerAbilities() {
      this.sendQueue.addToSendQueue(new Packet202PlayerAbilities(this.capabilities));
   }

   @Override
   public boolean func_71066_bF() {
      return true;
   }

   @Override
   public void setVelocity(double par1, double par3, double par5) {
      this.attackedAtYaw = (float)(Math.atan2(this.motionZ - par5, this.motionX - par1) * 180.0 / Math.PI - this.rotationYaw);
      super.h(par1, par3, par5);
   }
}
