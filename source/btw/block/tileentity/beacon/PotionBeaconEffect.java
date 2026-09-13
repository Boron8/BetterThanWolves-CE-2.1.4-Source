package btw.block.tileentity.beacon;

public class PotionBeaconEffect extends BeaconEffect {
   private final int POTION_ID;
   private final boolean SCALES_WITH_LEVEL;

   public PotionBeaconEffect(int potionID, boolean scalesWithLevel) {
      this.POTION_ID = potionID;
      this.SCALES_WITH_LEVEL = scalesWithLevel;
   }

   @Override
   public void onUpdate(BeaconTileEntity beacon) {
      if (!beacon.worldObj.isRemote) {
         if (this.SCALES_WITH_LEVEL) {
            this.applyPotionEffectToPlayersInRange(this.POTION_ID, beacon.l() - 1, beacon);
         } else {
            this.applyPotionEffectToPlayersInRange(this.POTION_ID, 0, beacon);
         }
      }
   }
}
