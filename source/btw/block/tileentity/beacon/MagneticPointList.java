package btw.block.tileentity.beacon;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.NBTTagList;

public class MagneticPointList {
   public List magneticPoints = new ArrayList();

   public void loadFromNBT(NBTTagList tagList) {
      this.magneticPoints.clear();

      for (int iTempCount = 0; iTempCount < tagList.tagCount(); iTempCount++) {
         NBTTagCompound tempCompound = (NBTTagCompound)tagList.tagAt(iTempCount);
         MagneticPoint newPoint = new MagneticPoint(tempCompound);
         this.magneticPoints.add(newPoint);
      }
   }

   public NBTTagList saveToNBT() {
      NBTTagList tagList = new NBTTagList("MagneticPoints");
      Iterator tempIterator = this.magneticPoints.iterator();

      while (tempIterator.hasNext()) {
         NBTTagCompound tempTagCompound = new NBTTagCompound();
         MagneticPoint tempPoint = (MagneticPoint)tempIterator.next();
         tempPoint.writeToNBT(tempTagCompound);
         tagList.appendTag(tempTagCompound);
      }

      return tagList;
   }

   public void removePointAt(int iIPos, int iJPos, int iKPos) {
      Iterator tempIterator = this.magneticPoints.iterator();

      while (tempIterator.hasNext()) {
         MagneticPoint tempPoint = (MagneticPoint)tempIterator.next();
         if (tempPoint.posX == iIPos && tempPoint.posZ == iKPos && tempPoint.posY == iJPos) {
            tempIterator.remove();
            return;
         }
      }
   }

   public void addPoint(int iIPos, int iJPos, int iKPos, int iPowerLevel) {
      MagneticPoint newPoint = new MagneticPoint(iIPos, iJPos, iKPos, iPowerLevel);
      this.magneticPoints.add(newPoint);
   }

   public void changePowerLevelOfPointAt(int iIPos, int iJPos, int iKPos, int iPowerLevel) {
      MagneticPoint point = this.getMagneticPointAtLocation(iIPos, iJPos, iKPos);
      if (point != null) {
         point.fieldLevel = iPowerLevel;
      }
   }

   public MagneticPoint getMagneticPointAtLocation(int iIPos, int iJPos, int iKPos) {
      for (MagneticPoint tempPoint : this.magneticPoints) {
         if (tempPoint.posX == iIPos && tempPoint.posZ == iKPos && tempPoint.posY == iJPos) {
            return tempPoint;
         }
      }

      return null;
   }
}
