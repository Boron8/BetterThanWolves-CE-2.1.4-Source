package net.minecraft.src;

public class PlayerCapabilities {
   public boolean disableDamage = false;
   public boolean isFlying = false;
   public boolean allowFlying = false;
   public boolean isCreativeMode = false;
   public boolean allowEdit = true;
   private float flySpeed = 0.05F;
   private float walkSpeed = 0.1F;

   public void writeCapabilitiesToNBT(NBTTagCompound var1) {
      NBTTagCompound var2 = new NBTTagCompound();
      var2.setBoolean("invulnerable", this.disableDamage);
      var2.setBoolean("flying", this.isFlying);
      var2.setBoolean("mayfly", this.allowFlying);
      var2.setBoolean("instabuild", this.isCreativeMode);
      var2.setBoolean("mayBuild", this.allowEdit);
      var2.setFloat("flySpeed", this.flySpeed);
      var2.setFloat("walkSpeed", this.walkSpeed);
      var1.setTag("abilities", var2);
   }

   public void readCapabilitiesFromNBT(NBTTagCompound var1) {
      if (var1.hasKey("abilities")) {
         NBTTagCompound var2 = var1.getCompoundTag("abilities");
         this.disableDamage = var2.getBoolean("invulnerable");
         this.isFlying = var2.getBoolean("flying");
         this.allowFlying = var2.getBoolean("mayfly");
         this.isCreativeMode = var2.getBoolean("instabuild");
         if (var2.hasKey("flySpeed")) {
            this.flySpeed = var2.getFloat("flySpeed");
            this.walkSpeed = var2.getFloat("walkSpeed");
         }

         if (var2.hasKey("mayBuild")) {
            this.allowEdit = var2.getBoolean("mayBuild");
         }
      }
   }

   public float getFlySpeed() {
      return this.flySpeed;
   }

   public void setFlySpeed(float var1) {
      this.flySpeed = var1;
   }

   public float getWalkSpeed() {
      return this.walkSpeed;
   }

   public void setPlayerWalkSpeed(float var1) {
      this.walkSpeed = var1;
   }
}
