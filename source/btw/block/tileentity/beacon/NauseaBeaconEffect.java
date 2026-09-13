package btw.block.tileentity.beacon;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Potion;
import net.minecraft.src.PotionEffect;

public class NauseaBeaconEffect extends BeaconEffect {
   @Override
   public void onUpdate(BeaconTileEntity beacon) {
      if (!beacon.worldObj.isRemote) {
         this.applyDungCloudToPlayersInRange(beacon);
      }
   }

   private void applyDungCloudToPlayersInRange(BeaconTileEntity beacon) {
      double range = rangePerLevel[beacon.l()];

      for (EntityPlayer player : beacon.worldObj.playerEntities) {
         double deltaX = Math.abs(beacon.xCoord - player.posX);
         if (deltaX <= range) {
            double deltaZ = Math.abs(beacon.zCoord - player.posZ);
            if (deltaZ <= range && !player.isDead && !player.capabilities.isCreativeMode && !player.isWearingFullSuitSoulforgedArmor()) {
               player.d(new PotionEffect(Potion.confusion.getId(), 180, 0, true));
               player.d(new PotionEffect(Potion.poison.getId(), 180, 0, true));
            }
         }
      }
   }
}
