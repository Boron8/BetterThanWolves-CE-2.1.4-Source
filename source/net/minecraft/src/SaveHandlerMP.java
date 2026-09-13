package net.minecraft.src;

import java.io.File;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class SaveHandlerMP implements ISaveHandler {
   @Override
   public WorldInfo loadWorldInfo() {
      return null;
   }

   @Override
   public void checkSessionLock() throws MinecraftException {
   }

   @Override
   public IChunkLoader getChunkLoader(WorldProvider par1WorldProvider) {
      return null;
   }

   @Override
   public void saveWorldInfoWithPlayer(WorldInfo par1WorldInfo, NBTTagCompound par2NBTTagCompound) {
   }

   @Override
   public void saveWorldInfo(WorldInfo par1WorldInfo) {
   }

   @Override
   public IPlayerFileData getSaveHandler() {
      return null;
   }

   @Override
   public void flush() {
   }

   @Override
   public File getMapFileFromName(String par1Str) {
      return null;
   }

   @Override
   public String getWorldDirectoryName() {
      return "none";
   }

   @Override
   public void loadModSpecificData(WorldServer world) {
   }

   @Override
   public void saveModSpecificData(WorldServer world) {
   }
}
