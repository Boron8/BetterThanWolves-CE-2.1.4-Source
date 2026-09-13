package btw.block.tileentity.beacon;

public class CompanionBeaconEffect extends AmbientBeaconEffect {
   public static int companionStrength = 0;

   public CompanionBeaconEffect() {
      super("Companion");
   }

   @Override
   public void onUpdate(BeaconTileEntity beacon) {
      super.onUpdate(beacon);
      if (beacon.updatedPowerState) {
         this.updateCompanionStrengthUpwards(beacon);
      }
   }

   private void updateCompanionStrengthUpwards(BeaconTileEntity beacon) {
      int power = beacon.l();
      if (power > companionStrength) {
         companionStrength = power;
      }
   }

   private void setCompanionStrength(int strength) {
      companionStrength = strength;
   }

   @Override
   public void onPowerOn(BeaconTileEntity beacon) {
      super.onPowerOn(beacon);
      this.updateCompanionStrengthUpwards(beacon);
      if (beacon.worldObj.isRemote) {
         beacon.worldObj
            .playSound(
               beacon.xCoord + 0.5,
               beacon.yCoord + 0.5,
               beacon.zCoord + 0.5,
               "mob.wolf.howl1",
               1.2F + beacon.worldObj.rand.nextFloat() * 0.2F,
               1.0F - beacon.worldObj.rand.nextFloat() * 0.2F
            );
      }
   }

   @Override
   public void onPowerChange(int newPowerLevel, int oldPowerLevel, BeaconTileEntity beacon) {
      super.onPowerChange(newPowerLevel, oldPowerLevel, beacon);
      this.setCompanionStrength(beacon.l());
   }

   @Override
   public void onPowerOff(BeaconTileEntity beacon) {
      super.onPowerOff(beacon);
      this.setCompanionStrength(0);
   }
}
