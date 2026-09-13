package btw.block.tileentity.beacon;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.PotionEffect;

public abstract class BeaconEffect {
   public static final int EFFECT_DURATION = 180;
   public static final double[] rangePerLevel = new double[]{0.0, 20.0, 40.0, 80.0, 160.0};

   public abstract void onUpdate(BeaconTileEntity var1);

   public void onPowerOn(BeaconTileEntity beacon) {
      if (beacon.worldObj.isRemote) {
         beacon.worldObj
            .playSound(
               beacon.xCoord + 0.5,
               beacon.yCoord + 0.5,
               beacon.zCoord + 0.5,
               "mob.wither.spawn",
               1.0F + beacon.worldObj.rand.nextFloat() * 0.1F,
               1.0F + beacon.worldObj.rand.nextFloat() * 0.1F
            );
      }
   }

   public void onPowerOff(BeaconTileEntity beacon) {
      if (beacon.worldObj.isRemote) {
         beacon.worldObj
            .playSound(
               beacon.xCoord + 0.5,
               beacon.yCoord + 0.5,
               beacon.zCoord + 0.5,
               "mob.wither.death",
               1.0F + beacon.worldObj.rand.nextFloat() * 0.1F,
               1.0F + beacon.worldObj.rand.nextFloat() * 0.1F
            );
      }
   }

   public void onPowerChange(int newPowerLevel, int oldPowerLevel, BeaconTileEntity beacon) {
      if (!beacon.worldObj.isRemote) {
         this.updateGlobalMagneticFieldListForStateChange(newPowerLevel, oldPowerLevel, beacon);
      }
   }

   protected void applyPotionEffectToPlayersInRange(int effectID, int effectLevel, BeaconTileEntity beacon) {
      if (beacon.updatedPowerState) {
         double range = rangePerLevel[beacon.l()];

         for (Object o : beacon.worldObj.playerEntities) {
            EntityPlayer player = (EntityPlayer)o;
            double deltaX = Math.abs(beacon.xCoord - player.posX);
            if (deltaX <= range) {
               double deltaZ = Math.abs(beacon.zCoord - player.posZ);
               if (deltaZ <= range && !player.isDead) {
                  player.d(new PotionEffect(effectID, 180, effectLevel, true));
               }
            }
         }
      }
   }

   protected void updateGlobalMagneticFieldListForStateChange(int newPowerLevel, int oldPowerLevel, BeaconTileEntity beacon) {
      if (newPowerLevel <= 0) {
         beacon.worldObj.getMagneticPointList().removePointAt(beacon.xCoord, beacon.yCoord, beacon.zCoord);
      } else if (oldPowerLevel <= 0) {
         beacon.worldObj.getMagneticPointList().addPoint(beacon.xCoord, beacon.yCoord, beacon.zCoord, this.getMagneticFieldLevel(newPowerLevel));
      } else if (oldPowerLevel != newPowerLevel) {
         beacon.worldObj
            .getMagneticPointList()
            .changePowerLevelOfPointAt(beacon.xCoord, beacon.yCoord, beacon.zCoord, this.getMagneticFieldLevel(newPowerLevel));
      }
   }

   protected int getMagneticFieldLevel(int powerLevel) {
      return powerLevel;
   }
}
