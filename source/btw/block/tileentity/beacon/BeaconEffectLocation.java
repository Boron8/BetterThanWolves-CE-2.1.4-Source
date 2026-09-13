package btw.block.tileentity.beacon;

import net.minecraft.src.NBTTagCompound;

public class BeaconEffectLocation {
   public int posX;
   public int posY;
   public int posZ;
   public int effectLevel;
   public int range;
   String effectID;

   public BeaconEffectLocation() {
      this.posX = 0;
      this.posY = 0;
      this.posZ = 0;
      this.effectLevel = 0;
      this.range = 0;
   }

   public BeaconEffectLocation(int posX, int posY, int posZ, int effectLevel, int range, String effectID) {
      this.posX = posX;
      this.posY = posY;
      this.posZ = posZ;
      this.effectLevel = effectLevel;
      this.range = range;
      this.effectID = effectID;
   }

   public BeaconEffectLocation(NBTTagCompound tagCompound) {
      this.loadFromNBT(tagCompound);
   }

   public void loadFromNBT(NBTTagCompound tagCompound) {
      this.posX = tagCompound.getInteger("IPos");
      this.posY = tagCompound.getShort("JPos");
      this.posZ = tagCompound.getInteger("KPos");
      this.effectLevel = tagCompound.getByte("Lvl");
      this.range = tagCompound.getInteger("Rng");
      if (tagCompound.getString("effectID") != null) {
         this.effectID = tagCompound.getString("effectID");
      } else {
         this.effectID = BeaconTileEntity.LOOTING_EFFECT.EFFECT_NAME;
      }
   }

   public NBTTagCompound writeToNBT(NBTTagCompound tagCompound) {
      tagCompound.setInteger("IPos", this.posX);
      tagCompound.setShort("JPos", (short)this.posY);
      tagCompound.setInteger("KPos", this.posZ);
      tagCompound.setByte("Lvl", (byte)this.effectLevel);
      tagCompound.setInteger("Rng", this.range);
      tagCompound.setString("effectID", this.effectID);
      return tagCompound;
   }
}
