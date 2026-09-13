package btw.block.blocks;

import net.minecraft.src.BlockStationary;
import net.minecraft.src.Entity;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Material;
import net.minecraft.src.PathFinder;

public class WaterBlockStationary extends BlockStationary {
   public WaterBlockStationary(int iBlockID, Material material) {
      super(iBlockID, material);
   }

   @Override
   public boolean canPathThroughBlock(IBlockAccess blockAccess, int i, int j, int k, Entity entity, PathFinder pathFinder) {
      return pathFinder.canPathThroughWater();
   }

   @Override
   public int getWeightOnPathBlocked(IBlockAccess blockAccess, int i, int j, int k) {
      return -1;
   }

   @Override
   public int adjustPathWeightOnNotBlocked(int iPreviousWeight) {
      return 2;
   }
}
