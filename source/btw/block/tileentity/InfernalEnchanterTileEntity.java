package btw.block.tileentity;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.TileEntity;

public class InfernalEnchanterTileEntity extends TileEntity {
   private int[] timeSinceLastCandleFlame = new int[4];
   public boolean playerNear;
   private static final int MAX_TIME_BETWEEN_FLAME_UPDATES = 10;

   public InfernalEnchanterTileEntity() {
      for (int iTemp = 0; iTemp < 4; iTemp++) {
         this.timeSinceLastCandleFlame[iTemp] = 0;
      }

      this.playerNear = false;
   }

   @Override
   public void updateEntity() {
      super.updateEntity();
      EntityPlayer entityplayer = this.worldObj.getClosestPlayer(this.xCoord + 0.5F, this.yCoord + 0.5F, this.zCoord + 0.5F, 4.5);
      if (entityplayer != null) {
         if (!this.playerNear) {
            this.lightCandles();
            this.playerNear = true;
         } else {
            this.updateCandleFlames();
         }
      } else {
         this.playerNear = false;
      }
   }

   private void lightCandles() {
      for (int iTemp = 0; iTemp < 4; iTemp++) {
         this.displayCandleFlameAtIndex(iTemp);
      }

      this.worldObj
         .playSoundEffect(this.xCoord + 0.5, this.yCoord + 0.5, this.zCoord + 0.5, "mob.ghast.fireball", 1.0F, this.worldObj.rand.nextFloat() * 0.4F + 0.8F);
   }

   private void updateCandleFlames() {
      for (int iTemp = 0; iTemp < 4; iTemp++) {
         this.timeSinceLastCandleFlame[iTemp]++;
         if (this.timeSinceLastCandleFlame[iTemp] > 10 || this.worldObj.rand.nextInt(5) == 0) {
            this.displayCandleFlameAtIndex(iTemp);
         }
      }
   }

   private void displayCandleFlameAtIndex(int iCandleIndex) {
      double flameX = this.xCoord + 0.125;
      double flameY = this.yCoord + 0.5F + 0.25F + 0.175F;
      double flameZ = this.zCoord + 0.125;
      if (iCandleIndex == 1 || iCandleIndex == 3) {
         flameX = this.xCoord + 0.875;
      }

      if (iCandleIndex == 2 || iCandleIndex == 3) {
         flameZ = this.zCoord + 0.875;
      }

      this.displayCandleFlameAtLoc(flameX, flameY, flameZ);
      this.timeSinceLastCandleFlame[iCandleIndex] = 0;
   }

   private void displayCandleFlameAtLoc(double xCoord, double yCoord, double zCoord) {
      this.worldObj.spawnParticle("smoke", xCoord, yCoord, zCoord, 0.0, 0.0, 0.0);
      this.worldObj.spawnParticle("flame", xCoord, yCoord, zCoord, 0.0, 0.0, 0.0);
   }
}
