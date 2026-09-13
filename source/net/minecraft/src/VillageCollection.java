package net.minecraft.src;

import btw.block.BTWBlocks;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class VillageCollection extends WorldSavedData {
   private World worldObj;
   private final List villagerPositionsList = new ArrayList();
   private final List newDoors = new ArrayList();
   private final List villageList = new ArrayList();
   private int tickCounter = 0;

   public VillageCollection(String par1Str) {
      super(par1Str);
   }

   public VillageCollection(World par1World) {
      super("villages");
      this.worldObj = par1World;
      this.c();
   }

   public void func_82566_a(World par1World) {
      this.worldObj = par1World;

      for (Village var3 : this.villageList) {
         var3.func_82691_a(par1World);
      }
   }

   public void addVillagerPosition(int par1, int par2, int par3) {
      if (this.villagerPositionsList.size() <= 64 && !this.isVillagerPositionPresent(par1, par2, par3)) {
         this.villagerPositionsList.add(new ChunkCoordinates(par1, par2, par3));
      }
   }

   public void tick() {
      this.tickCounter++;

      for (Village var2 : this.villageList) {
         var2.tick(this.tickCounter);
      }

      this.removeAnnihilatedVillages();
      this.dropOldestVillagerPosition();
      this.addNewDoorsToVillageOrCreateVillage();
      if (this.tickCounter % 400 == 0) {
         this.c();
      }
   }

   private void removeAnnihilatedVillages() {
      Iterator var1 = this.villageList.iterator();

      while (var1.hasNext()) {
         Village var2 = (Village)var1.next();
         if (var2.isAnnihilated()) {
            var1.remove();
            this.c();
         }
      }
   }

   public List getVillageList() {
      return this.villageList;
   }

   public Village findNearestVillage(int par1, int par2, int par3, int par4) {
      Village var5 = null;
      float var6 = Float.MAX_VALUE;

      for (Village var8 : this.villageList) {
         float var9 = var8.getCenter().getDistanceSquared(par1, par2, par3);
         if (var9 < var6) {
            int var10 = par4 + var8.getVillageRadius();
            if (var9 <= var10 * var10) {
               var5 = var8;
               var6 = var9;
            }
         }
      }

      return var5;
   }

   private void dropOldestVillagerPosition() {
      if (!this.villagerPositionsList.isEmpty()) {
         this.addUnassignedWoodenDoorsAroundToNewDoorsList((ChunkCoordinates)this.villagerPositionsList.remove(0));
      }
   }

   private void addNewDoorsToVillageOrCreateVillage() {
      for (int var1 = 0; var1 < this.newDoors.size(); var1++) {
         VillageDoorInfo var2 = (VillageDoorInfo)this.newDoors.get(var1);
         boolean var3 = false;

         for (Village var5 : this.villageList) {
            int var6 = (int)var5.getCenter().getDistanceSquared(var2.posX, var2.posY, var2.posZ);
            int var7 = 32 + var5.getVillageRadius();
            if (var6 <= var7 * var7) {
               var5.addVillageDoorInfo(var2);
               var3 = true;
               break;
            }
         }

         if (!var3) {
            Village var8 = new Village(this.worldObj);
            var8.addVillageDoorInfo(var2);
            this.villageList.add(var8);
            this.c();
         }
      }

      this.newDoors.clear();
   }

   private void addUnassignedWoodenDoorsAroundToNewDoorsList(ChunkCoordinates par1ChunkCoordinates) {
      byte var2 = 16;
      byte var3 = 4;
      byte var4 = 16;

      for (int var5 = par1ChunkCoordinates.posX - var2; var5 < par1ChunkCoordinates.posX + var2; var5++) {
         for (int var6 = par1ChunkCoordinates.posY - var3; var6 < par1ChunkCoordinates.posY + var3; var6++) {
            for (int var7 = par1ChunkCoordinates.posZ - var4; var7 < par1ChunkCoordinates.posZ + var4; var7++) {
               if (this.isWoodenDoorAt(var5, var6, var7)) {
                  VillageDoorInfo var8 = this.getVillageDoorAt(var5, var6, var7);
                  if (var8 == null) {
                     this.addDoorToNewListIfAppropriate(var5, var6, var7);
                  } else {
                     var8.lastActivityTimestamp = this.tickCounter;
                  }
               }
            }
         }
      }
   }

   private VillageDoorInfo getVillageDoorAt(int par1, int par2, int par3) {
      for (VillageDoorInfo var5 : this.newDoors) {
         if (var5.posX == par1 && var5.posZ == par3 && Math.abs(var5.posY - par2) <= 1) {
            return var5;
         }
      }

      for (Village var7 : this.villageList) {
         VillageDoorInfo var6 = var7.getVillageDoorAt(par1, par2, par3);
         if (var6 != null) {
            return var6;
         }
      }

      return null;
   }

   private void addDoorToNewListIfAppropriate(int par1, int par2, int par3) {
      int var4 = ((BlockDoor)Block.doorWood).getDoorOrientation(this.worldObj, par1, par2, par3);
      if (var4 != 0 && var4 != 2) {
         int var5 = 0;

         for (int var6 = -5; var6 < 0; var6++) {
            if (this.worldObj.canBlockSeeTheSky(par1, par2, par3 + var6)) {
               var5--;
            }
         }

         for (int var10 = 1; var10 <= 5; var10++) {
            if (this.worldObj.canBlockSeeTheSky(par1, par2, par3 + var10)) {
               var5++;
            }
         }

         if (var5 != 0) {
            this.newDoors.add(new VillageDoorInfo(par1, par2, par3, 0, var5 > 0 ? -2 : 2, this.tickCounter));
         }
      } else {
         int var5 = 0;

         for (int var6x = -5; var6x < 0; var6x++) {
            if (this.worldObj.canBlockSeeTheSky(par1 + var6x, par2, par3)) {
               var5--;
            }
         }

         for (int var8 = 1; var8 <= 5; var8++) {
            if (this.worldObj.canBlockSeeTheSky(par1 + var8, par2, par3)) {
               var5++;
            }
         }

         if (var5 != 0) {
            this.newDoors.add(new VillageDoorInfo(par1, par2, par3, var5 > 0 ? -2 : 2, 0, this.tickCounter));
         }
      }
   }

   private boolean isVillagerPositionPresent(int par1, int par2, int par3) {
      for (ChunkCoordinates var5 : this.villagerPositionsList) {
         if (var5.posX == par1 && var5.posY == par2 && var5.posZ == par3) {
            return true;
         }
      }

      return false;
   }

   private boolean isWoodenDoorAt(int par1, int par2, int par3) {
      int var4 = this.worldObj.getBlockId(par1, par2, par3);
      return var4 == Block.doorWood.blockID || var4 == BTWBlocks.woodenDoor.blockID;
   }

   @Override
   public void readFromNBT(NBTTagCompound par1NBTTagCompound) {
      this.tickCounter = par1NBTTagCompound.getInteger("Tick");
      NBTTagList var2 = par1NBTTagCompound.getTagList("Villages");

      for (int var3 = 0; var3 < var2.tagCount(); var3++) {
         NBTTagCompound var4 = (NBTTagCompound)var2.tagAt(var3);
         Village var5 = new Village();
         var5.readVillageDataFromNBT(var4);
         this.villageList.add(var5);
      }
   }

   @Override
   public void writeToNBT(NBTTagCompound par1NBTTagCompound) {
      par1NBTTagCompound.setInteger("Tick", this.tickCounter);
      NBTTagList var2 = new NBTTagList("Villages");

      for (Village var4 : this.villageList) {
         NBTTagCompound var5 = new NBTTagCompound("Village");
         var4.writeVillageDataToNBT(var5);
         var2.appendTag(var5);
      }

      par1NBTTagCompound.setTag("Villages", var2);
   }
}
