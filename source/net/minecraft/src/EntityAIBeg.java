package net.minecraft.src;

public class EntityAIBeg extends EntityAIBase {
   private EntityWolf theWolf;
   private EntityPlayer thePlayer;
   private World worldObject;
   private float minPlayerDistance;
   private int field_75384_e;

   public EntityAIBeg(EntityWolf var1, float var2) {
      this.theWolf = var1;
      this.worldObject = var1.worldObj;
      this.minPlayerDistance = var2;
      this.a(2);
   }

   @Override
   public boolean shouldExecute() {
      this.thePlayer = this.worldObject.getClosestPlayerToEntity(this.theWolf, this.minPlayerDistance);
      return this.thePlayer == null ? false : this.hasPlayerGotBoneInHand(this.thePlayer);
   }

   @Override
   public boolean continueExecuting() {
      if (!this.thePlayer.R()) {
         return false;
      } else {
         return this.theWolf.e(this.thePlayer) > this.minPlayerDistance * this.minPlayerDistance
            ? false
            : this.field_75384_e > 0 && this.hasPlayerGotBoneInHand(this.thePlayer);
      }
   }

   @Override
   public void startExecuting() {
      this.theWolf.func_70918_i(true);
      this.field_75384_e = 40 + this.theWolf.aE().nextInt(40);
   }

   @Override
   public void resetTask() {
      this.theWolf.func_70918_i(false);
      this.thePlayer = null;
   }

   @Override
   public void updateTask() {
      this.theWolf
         .az()
         .setLookPosition(
            this.thePlayer.posX, this.thePlayer.posY + this.thePlayer.getEyeHeight(), this.thePlayer.posZ, 10.0F, this.theWolf.getVerticalFaceSpeed()
         );
      this.field_75384_e--;
   }

   private boolean hasPlayerGotBoneInHand(EntityPlayer var1) {
      ItemStack var2 = var1.inventory.getCurrentItem();
      if (var2 == null) {
         return false;
      } else {
         return !this.theWolf.m() && var2.itemID == Item.bone.itemID ? true : this.theWolf.isBreedingItem(var2);
      }
   }
}
