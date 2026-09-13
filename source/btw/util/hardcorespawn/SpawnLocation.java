package btw.util.hardcorespawn;

import net.minecraft.src.NBTTagCompound;

public class SpawnLocation {
   public int posX;
   public int posY;
   public int posZ;
   public long spawnTime;

   public SpawnLocation() {
      this.posX = 0;
      this.posY = 0;
      this.posZ = 0;
      this.spawnTime = 0L;
   }

   public SpawnLocation(int iIPos, int iJPos, int iKPos, long iSpawnTime) {
      this.posX = iIPos;
      this.posY = iJPos;
      this.posZ = iKPos;
      this.spawnTime = iSpawnTime;
   }

   public SpawnLocation(NBTTagCompound tagCompound) {
      this.loadFromNBT(tagCompound);
   }

   public void loadFromNBT(NBTTagCompound tagCompound) {
      this.posX = tagCompound.getInteger("IPos");
      this.posY = tagCompound.getShort("JPos");
      this.posZ = tagCompound.getInteger("KPos");
      this.spawnTime = tagCompound.getLong("SpawnTime");
   }

   public NBTTagCompound writeToNBT(NBTTagCompound tagCompound) {
      tagCompound.setInteger("IPos", this.posX);
      tagCompound.setShort("JPos", (short)this.posY);
      tagCompound.setInteger("KPos", this.posZ);
      tagCompound.setLong("SpawnTime", this.spawnTime);
      return tagCompound;
   }
}
