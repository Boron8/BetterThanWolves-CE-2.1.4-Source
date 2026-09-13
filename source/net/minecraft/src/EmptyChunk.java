package net.minecraft.src;

import java.util.List;
import java.util.Random;

public class EmptyChunk extends Chunk {
   public EmptyChunk(World var1, int var2, int var3) {
      super(var1, var2, var3);
   }

   @Override
   public boolean isAtLocation(int var1, int var2) {
      return var1 == this.xPosition && var2 == this.zPosition;
   }

   @Override
   public int getHeightValue(int var1, int var2) {
      return 0;
   }

   @Override
   public void generateHeightMap() {
   }

   @Override
   public void generateSkylightMap() {
   }

   @Override
   public int getBlockID(int var1, int var2, int var3) {
      return 0;
   }

   @Override
   public int getBlockLightOpacity(int var1, int var2, int var3) {
      return 255;
   }

   @Override
   public boolean setBlockIDWithMetadata(int var1, int var2, int var3, int var4, int var5) {
      return true;
   }

   @Override
   public int getBlockMetadata(int var1, int var2, int var3) {
      return 0;
   }

   @Override
   public boolean setBlockMetadata(int var1, int var2, int var3, int var4) {
      return false;
   }

   @Override
   public int getSavedLightValue(EnumSkyBlock var1, int var2, int var3, int var4) {
      return 0;
   }

   @Override
   public void setLightValue(EnumSkyBlock var1, int var2, int var3, int var4, int var5) {
   }

   @Override
   public int getBlockLightValue(int var1, int var2, int var3, int var4) {
      return 0;
   }

   @Override
   public void addEntity(Entity var1) {
   }

   @Override
   public void removeEntity(Entity var1) {
   }

   @Override
   public void removeEntityAtIndex(Entity var1, int var2) {
   }

   @Override
   public boolean canBlockSeeTheSky(int var1, int var2, int var3) {
      return false;
   }

   @Override
   public TileEntity getChunkBlockTileEntity(int var1, int var2, int var3) {
      return null;
   }

   @Override
   public void addTileEntity(TileEntity var1) {
   }

   @Override
   public void setChunkBlockTileEntity(int var1, int var2, int var3, TileEntity var4) {
   }

   @Override
   public void removeChunkBlockTileEntity(int var1, int var2, int var3) {
   }

   @Override
   public void onChunkLoad() {
   }

   @Override
   public void onChunkUnload() {
   }

   @Override
   public void setChunkModified() {
   }

   @Override
   public void getEntitiesWithinAABBForEntity(Entity var1, AxisAlignedBB var2, List var3, IEntitySelector var4) {
   }

   @Override
   public void getEntitiesOfTypeWithinAAAB(Class var1, AxisAlignedBB var2, List var3, IEntitySelector var4) {
   }

   @Override
   public boolean needsSaving(boolean var1) {
      return false;
   }

   @Override
   public Random getRandomWithSeed(long var1) {
      return new Random(
         this.worldObj.getSeed()
               + this.xPosition * this.xPosition * 4987142
               + this.xPosition * 5947611
               + this.zPosition * this.zPosition * 4392871L
               + this.zPosition * 389711
            ^ var1
      );
   }

   @Override
   public boolean isEmpty() {
      return true;
   }

   @Override
   public boolean getAreLevelsEmpty(int var1, int var2) {
      return true;
   }
}
