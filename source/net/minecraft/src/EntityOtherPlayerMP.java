package net.minecraft.src;

import btw.BTWMod;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;

@Environment(EnvType.CLIENT)
public class EntityOtherPlayerMP extends EntityPlayer {
   private boolean isItemInUse = false;
   private int otherPlayerMPPosRotationIncrements;
   private double otherPlayerMPX;
   private double otherPlayerMPY;
   private double otherPlayerMPZ;
   private double otherPlayerMPYaw;
   private double otherPlayerMPPitch;

   public EntityOtherPlayerMP(World par1World, String par2Str) {
      super(par1World);
      this.username = par2Str;
      this.yOffset = 0.0F;
      this.stepHeight = 0.0F;
      if (par2Str != null && par2Str.length() > 0) {
         this.skinUrl = "http://skins.minecraft.net/MinecraftSkins/" + StringUtils.stripControlCodes(par2Str) + ".png";
      }

      this.noClip = true;
      this.field_71082_cx = 0.25F;
      this.renderDistanceWeight = 10.0;
   }

   @Override
   protected void resetHeight() {
      this.yOffset = 0.0F;
   }

   @Override
   public boolean attackEntityFrom(DamageSource par1DamageSource, int par2) {
      return true;
   }

   @Override
   public void setPositionAndRotation2(double par1, double par3, double par5, float par7, float par8, int par9) {
      this.otherPlayerMPX = par1;
      this.otherPlayerMPY = par3;
      this.otherPlayerMPZ = par5;
      this.otherPlayerMPYaw = par7;
      this.otherPlayerMPPitch = par8;
      this.otherPlayerMPPosRotationIncrements = par9;
   }

   @Override
   public void updateCloak() {
      this.cloakUrl = BTWMod.playerCloakURL + StringUtils.stripControlCodes(this.username) + ".png";
   }

   @Override
   public void onUpdate() {
      this.field_71082_cx = 0.0F;
      super.onUpdate();
      this.prevLimbYaw = this.limbYaw;
      double var1 = this.posX - this.prevPosX;
      double var3 = this.posZ - this.prevPosZ;
      float var5 = MathHelper.sqrt_double(var1 * var1 + var3 * var3) * 4.0F;
      if (var5 > 1.0F) {
         var5 = 1.0F;
      }

      this.limbYaw = this.limbYaw + (var5 - this.limbYaw) * 0.4F;
      this.limbSwing = this.limbSwing + this.limbYaw;
      if (!this.isItemInUse && this.aj() && this.inventory.mainInventory[this.inventory.currentItem] != null) {
         ItemStack var6 = this.inventory.mainInventory[this.inventory.currentItem];
         this.a(this.inventory.mainInventory[this.inventory.currentItem], Item.itemsList[var6.itemID].getMaxItemUseDuration(var6));
         this.isItemInUse = true;
      } else if (this.isItemInUse && !this.aj()) {
         this.ca();
         this.isItemInUse = false;
      }
   }

   @Override
   public float getShadowSize() {
      return 0.0F;
   }

   @Override
   public void onLivingUpdate() {
      super.updateEntityActionState();
      if (this.otherPlayerMPPosRotationIncrements > 0) {
         double var1 = this.posX + (this.otherPlayerMPX - this.posX) / this.otherPlayerMPPosRotationIncrements;
         double var3 = this.posY + (this.otherPlayerMPY - this.posY) / this.otherPlayerMPPosRotationIncrements;
         double var5 = this.posZ + (this.otherPlayerMPZ - this.posZ) / this.otherPlayerMPPosRotationIncrements;
         double var7 = this.otherPlayerMPYaw - this.rotationYaw;

         while (var7 < -180.0) {
            var7 += 360.0;
         }

         while (var7 >= 180.0) {
            var7 -= 360.0;
         }

         this.rotationYaw = (float)(this.rotationYaw + var7 / this.otherPlayerMPPosRotationIncrements);
         this.rotationPitch = (float)(this.rotationPitch + (this.otherPlayerMPPitch - this.rotationPitch) / this.otherPlayerMPPosRotationIncrements);
         this.otherPlayerMPPosRotationIncrements--;
         this.b(var1, var3, var5);
         this.b(this.rotationYaw, this.rotationPitch);
      }

      this.prevCameraYaw = this.cameraYaw;
      float var9 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
      float var2 = (float)Math.atan(-this.motionY * 0.2F) * 15.0F;
      if (var9 > 0.1F) {
         var9 = 0.1F;
      }

      if (!this.onGround || this.aX() <= 0) {
         var9 = 0.0F;
      }

      if (this.onGround || this.aX() <= 0) {
         var2 = 0.0F;
      }

      this.cameraYaw = this.cameraYaw + (var9 - this.cameraYaw) * 0.4F;
      this.cameraPitch = this.cameraPitch + (var2 - this.cameraPitch) * 0.8F;
   }

   @Override
   public void setCurrentItemOrArmor(int par1, ItemStack par2ItemStack) {
      if (par1 == 0) {
         this.inventory.mainInventory[this.inventory.currentItem] = par2ItemStack;
      } else {
         this.inventory.armorInventory[par1 - 1] = par2ItemStack;
      }
   }

   @Override
   public float getEyeHeight() {
      return 1.82F;
   }

   @Override
   public void sendChatToPlayer(String par1Str) {
      Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(par1Str);
   }

   @Override
   public boolean canCommandSenderUseCommand(int par1, String par2Str) {
      return false;
   }

   @Override
   public ChunkCoordinates getPlayerCoordinates() {
      return new ChunkCoordinates(MathHelper.floor_double(this.posX + 0.5), MathHelper.floor_double(this.posY + 0.5), MathHelper.floor_double(this.posZ + 0.5));
   }

   @Override
   public double getMountedYOffset() {
      return this.height * 0.93;
   }
}
