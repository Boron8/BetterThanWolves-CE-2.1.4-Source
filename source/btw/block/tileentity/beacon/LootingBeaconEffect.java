package btw.block.tileentity.beacon;

import btw.BTWMod;

public class LootingBeaconEffect extends AmbientBeaconEffect {
   public LootingBeaconEffect() {
      super("Looting");
   }

   @Override
   public void onUpdate(BeaconTileEntity beacon) {
      if (!beacon.worldObj.isRemote) {
         this.applyPotionEffectToPlayersInRange(BTWMod.potionLooting.getId(), beacon.l() - 1, beacon);
      }
   }
}
