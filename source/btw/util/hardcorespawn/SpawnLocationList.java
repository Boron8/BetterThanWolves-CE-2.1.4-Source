package btw.util.hardcorespawn;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.NBTTagList;

public class SpawnLocationList {
   public List spawnLocations = new ArrayList();

   public void loadFromNBT(NBTTagList tagList) {
      this.spawnLocations.clear();

      for (int iTempCount = 0; iTempCount < tagList.tagCount(); iTempCount++) {
         NBTTagCompound tempCompound = (NBTTagCompound)tagList.tagAt(iTempCount);
         SpawnLocation newPoint = new SpawnLocation(tempCompound);
         this.spawnLocations.add(newPoint);
      }
   }

   public NBTTagList saveToNBT() {
      NBTTagList tagList = new NBTTagList("SpawnLocations");
      Iterator tempIterator = this.spawnLocations.iterator();

      while (tempIterator.hasNext()) {
         NBTTagCompound tempTagCompound = new NBTTagCompound();
         SpawnLocation tempPoint = (SpawnLocation)tempIterator.next();
         tempPoint.writeToNBT(tempTagCompound);
         tagList.appendTag(tempTagCompound);
      }

      return tagList;
   }

   public void addPoint(int iIPos, int iJPos, int iKPos, long lSpawnTime) {
      SpawnLocation newPoint = new SpawnLocation(iIPos, iJPos, iKPos, lSpawnTime);
      this.spawnLocations.add(newPoint);
   }

   public SpawnLocation getClosestSpawnLocationForPosition(double dXPos, double dZPos) {
      SpawnLocation closestLocation = null;
      double dClosestDistSq = 0.0;

      for (SpawnLocation tempPoint : this.spawnLocations) {
         double dDeltaI = tempPoint.posX - dXPos;
         double dDeltaK = tempPoint.posZ - dZPos;
         double dTempDistSq = dDeltaI * dDeltaI + dDeltaK * dDeltaK;
         if (closestLocation == null || dTempDistSq < dClosestDistSq) {
            closestLocation = tempPoint;
            dClosestDistSq = dTempDistSq;
         }
      }

      return closestLocation;
   }

   public SpawnLocation getMostRecentSpawnLocation() {
      SpawnLocation mostRecent = null;

      for (SpawnLocation tempPoint : this.spawnLocations) {
         if (mostRecent == null || tempPoint.spawnTime > mostRecent.spawnTime) {
            mostRecent = tempPoint;
         }
      }

      return mostRecent;
   }

   public boolean doesListContainPoint(int iIPos, int iJPos, int iKPos, long lSpawnTime) {
      for (SpawnLocation tempPoint : this.spawnLocations) {
         if (tempPoint.posX == iIPos && tempPoint.posZ == iKPos && tempPoint.posY == iJPos && tempPoint.spawnTime == lSpawnTime) {
            return true;
         }
      }

      return false;
   }

   public void addPointIfNotAlreadyPresent(int iIPos, int iJPos, int iKPos, long lSpawnTime) {
      if (!this.doesListContainPoint(iIPos, iJPos, iKPos, lSpawnTime)) {
         this.addPoint(iIPos, iJPos, iKPos, lSpawnTime);
      }
   }
}
