package btw.world.util;

import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.WorldInfo;
import net.minecraft.src.WorldServer;

public interface WorldData {
   void saveWorldDataToNBT(WorldServer var1, NBTTagCompound var2);

   void loadWorldDataFromNBT(WorldServer var1, NBTTagCompound var2);

   void saveGlobalDataToNBT(WorldInfo var1, NBTTagCompound var2);

   void loadGlobalDataFromNBT(WorldInfo var1, NBTTagCompound var2);

   default void createDefaultGlobalData(WorldInfo info) {
   }

   void copyGlobalData(WorldInfo var1, WorldInfo var2);

   String getFilename();
}
