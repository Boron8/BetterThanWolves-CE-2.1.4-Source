package btw.block.blocks;

import java.util.Random;
import net.minecraft.src.Block;
import net.minecraft.src.BlockStationary;
import net.minecraft.src.Entity;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Material;
import net.minecraft.src.PathFinder;
import net.minecraft.src.World;

public class LavaBlockStationary extends BlockStationary {
   public LavaBlockStationary(int iBlockID, Material material) {
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

   @Override
   public void updateTick(World world, int i, int j, int k, Random rand) {
      if (world.getGameRules().getGameRuleBooleanValue("doFireTick")) {
         this.checkForDirectSetOnFireToNeighborsInContact(world, i, j, k);
         this.checkForStartingFiresAroundLava(world, i, j, k, rand);
      }
   }

   private boolean canBlockMaterialBurn(World world, int i, int j, int k) {
      return world.getBlockMaterial(i, j, k).getCanBurn();
   }

   private void checkForDirectSetOnFireToNeighborsInContact(World world, int i, int j, int k) {
      this.checkForDirectSetOnFire(world, i, j - 1, k);
      this.checkForDirectSetOnFire(world, i - 1, j, k);
      this.checkForDirectSetOnFire(world, i + 1, j, k);
      this.checkForDirectSetOnFire(world, i, j, k - 1);
      this.checkForDirectSetOnFire(world, i, j, k + 1);
   }

   private void checkForDirectSetOnFire(World world, int i, int j, int k) {
      Block tempBlock = Block.blocksList[world.getBlockId(i, j, k)];
      if (tempBlock != null && tempBlock.getCanBeSetOnFireDirectly(world, i, j, k)) {
         tempBlock.setOnFireDirectly(world, i, j, k);
      }
   }

   private void checkForStartingFiresAroundLava(World world, int i, int j, int k, Random rand) {
      int iNumAttempts = rand.nextInt(3);
      if (iNumAttempts == 0) {
         int iSourceI = i;
         int iSourceK = k;

         for (int var9 = 0; var9 < 3; var9++) {
            i = iSourceI + rand.nextInt(3) - 1;
            k = iSourceK + rand.nextInt(3) - 1;
            if (world.isAirBlock(i, j + 1, k) && this.canBlockMaterialBurn(world, i, j, k)) {
               world.setBlock(i, j + 1, k, Block.fire.blockID);
            } else {
               Block tempBlock = Block.blocksList[world.getBlockId(i, j, k)];
               if (tempBlock != null && tempBlock.getCanBeSetOnFireDirectly(world, i, j, k)) {
                  tempBlock.setOnFireDirectly(world, i, j, k);
               }
            }
         }
      } else {
         for (int iTempCount = 0; iTempCount < iNumAttempts; iTempCount++) {
            i += rand.nextInt(3) - 1;
            j++;
            k += rand.nextInt(3) - 1;
            int iTempBlockID = world.getBlockId(i, j, k);
            Block tempBlock = Block.blocksList[iTempBlockID];
            if (tempBlock != null && !tempBlock.isAirBlock()) {
               if (tempBlock.getCanBeSetOnFireDirectly(world, i, j, k)) {
                  tempBlock.setOnFireDirectly(world, i, j, k);
                  break;
               }

               if (tempBlock.blockMaterial.blocksMovement()) {
                  return;
               }
            } else if (this.canBlockMaterialBurn(world, i - 1, j, k)
               || this.canBlockMaterialBurn(world, i + 1, j, k)
               || this.canBlockMaterialBurn(world, i, j, k - 1)
               || this.canBlockMaterialBurn(world, i, j, k + 1)
               || this.canBlockMaterialBurn(world, i, j - 1, k)
               || this.canBlockMaterialBurn(world, i, j + 1, k)) {
               world.setBlock(i, j, k, Block.fire.blockID);
               return;
            }
         }
      }
   }
}
