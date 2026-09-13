package btw.block.tileentity.beacon;

import net.minecraft.src.NBTTagCompound;

public class MagneticPoint {
   private static double[] fieldStrengthMultipliersByLevel = new double[]{0.0, 1.0, 8.0, 27.0, 64.0, 125.0, 216.0, 343.0, 4096.0};
   private static double[] maxRangeSquaredForLevelWithNoise = new double[]{
      0.0, 100.0, 400.0, 1600.0, 6400.0, 25600.0, 102400.0, 409600.0, Double.POSITIVE_INFINITY
   };
   public int posX;
   public int posY;
   public int posZ;
   public int fieldLevel;

   public MagneticPoint() {
      this.posX = 0;
      this.posY = 0;
      this.posZ = 0;
      this.fieldLevel = 0;
   }

   public MagneticPoint(int iIPos, int iJPos, int iKPos, int iFieldLevel) {
      this.posX = iIPos;
      this.posY = iJPos;
      this.posZ = iKPos;
      this.fieldLevel = iFieldLevel;
   }

   public MagneticPoint(NBTTagCompound tagCompound) {
      this.loadFromNBT(tagCompound);
   }

   public void loadFromNBT(NBTTagCompound tagCompound) {
      this.posX = tagCompound.getInteger("IPos");
      this.posY = tagCompound.getShort("JPos");
      this.posZ = tagCompound.getInteger("KPos");
      this.fieldLevel = tagCompound.getByte("Lvl");
   }

   public NBTTagCompound writeToNBT(NBTTagCompound tagCompound) {
      tagCompound.setInteger("IPos", this.posX);
      tagCompound.setShort("JPos", (short)this.posY);
      tagCompound.setInteger("KPos", this.posZ);
      tagCompound.setByte("Lvl", (byte)this.fieldLevel);
      return tagCompound;
   }

   public double getFieldStrengthRelativeToPosition(double dRelativeX, double dRelativeZ) {
      double dDeltaX = this.posX - dRelativeX;
      double dDeltaZ = this.posZ - dRelativeZ;
      double dDistanceSq = dDeltaX * dDeltaX + dDeltaZ * dDeltaZ;
      return fieldStrengthMultipliersByLevel[this.fieldLevel] / dDistanceSq;
   }

   public double getFieldStrengthRelativeToPositionWithBackgroundNoise(double dRelativeX, double dRelativeZ) {
      double dDeltaX = this.posX - dRelativeX;
      double dDeltaZ = this.posZ - dRelativeZ;
      double dDistanceSq = dDeltaX * dDeltaX + dDeltaZ * dDeltaZ;
      return dDistanceSq <= maxRangeSquaredForLevelWithNoise[this.fieldLevel] ? fieldStrengthMultipliersByLevel[this.fieldLevel] / dDistanceSq : -1.0;
   }
}
