package btw.block.blocks;

import net.minecraft.src.BlockFlowing;
import net.minecraft.src.Entity;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Material;
import net.minecraft.src.PathFinder;
import net.minecraft.src.World;

public class LavaBlockFlowing extends BlockFlowing {
   public LavaBlockFlowing(int iBlockID, Material material) {
      super(iBlockID, material);
   }

   @Override
   public boolean canPathThroughBlock(IBlockAccess blockAccess, int i, int j, int k, Entity entity, PathFinder pathFinder) {
      return entity.handleLavaMovement();
   }

   @Override
   public int getWeightOnPathBlocked(IBlockAccess blockAccess, int i, int j, int k) {
      return -2;
   }

   @Override
   public boolean getDoesFireDamageToEntities(World world, int i, int j, int k) {
      return true;
   }

   @Override
   public boolean getCanBlockLightItemOnFire(IBlockAccess blockAccess, int i, int j, int k) {
      return true;
   }
}
