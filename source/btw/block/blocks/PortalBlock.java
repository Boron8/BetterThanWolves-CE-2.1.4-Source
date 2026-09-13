package btw.block.blocks;

import btw.block.BTWBlocks;
import btw.world.util.WorldUtils;
import java.util.Random;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.BlockPortal;
import net.minecraft.src.EntityCreature;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;

public class PortalBlock extends BlockPortal {
   private static final int CHANCE_OF_CHECKING_FOR_POSSESSION = 10;
   public static final int CREATURE_POSSESSION_RANGE = 16;
   private static final int MIN_PORTAL_WIDTH = 4;
   private static final int MIN_PORTAL_HEIGHT = 5;
   private static final int MAX_PORTAL_WIDTH = 23;
   private static final int MAX_PORTAL_HEIGHT = 23;

   public PortalBlock(int blockID) {
      super(blockID);
      this.c(-1.0F);
      this.a(0.75F);
      this.a(l);
      this.c("portal");
   }

   @Override
   public void updateTick(World world, int x, int y, int z, Random rand) {
      if (rand.nextInt(10) == 0 && world.provider.isSurfaceWorld()) {
         EntityCreature.attemptToPossessCreaturesAroundBlock(world, x, y, z, 1, 16);
      }

      WorldUtils.gameProgressSetNetherBeenAccessedServerOnly();
   }

   @Override
   public void onNeighborBlockChange(World world, int x, int y, int z, int neighborID) {
      byte isX = 0;
      byte isZ = 1;
      if (world.getBlockId(x - 1, y, z) == this.blockID || world.getBlockId(x + 1, y, z) == this.blockID) {
         isX = 1;
         isZ = 0;
      }

      int a = 0;
      int b = 0;
      int c = 0;
      int d = 0;

      while (world.getBlockId(x, y - a, z) == this.blockID) {
         a++;
      }

      while (world.getBlockId(x, y + b, z) == this.blockID) {
         b++;
      }

      while (world.getBlockId(x - isX * c, y, z - isZ * c) == this.blockID) {
         c++;
      }

      while (world.getBlockId(x + isX * d, y, z + isZ * d) == this.blockID) {
         d++;
      }

      if (world.getBlockId(x, y - a, z) != Block.obsidian.blockID
         || world.getBlockId(x, y + b, z) != Block.obsidian.blockID
         || world.getBlockId(x - isX * c, y, z - isZ * c) != Block.obsidian.blockID
         || world.getBlockId(x + isX * d, y, z + isZ * d) != Block.obsidian.blockID) {
         world.setBlockToAir(x, y, z);
      }
   }

   @Override
   public boolean tryToCreatePortal(World world, int x, int y, int z) {
      int xDistPos = 0;
      int xDistNeg = 0;
      int zDistPos = 0;
      int zDistNeg = 0;

      for (int i = 0; i < 22; i++) {
         if (world.getBlockId(x + i, y, z) == Block.obsidian.blockID && xDistPos == 0) {
            xDistPos = i;
         }

         if (world.getBlockId(x - i, y, z) == Block.obsidian.blockID && xDistNeg == 0) {
            xDistNeg = -i;
         }

         if (world.getBlockId(x, y, z + i) == Block.obsidian.blockID && zDistPos == 0) {
            zDistPos = i;
         }

         if (world.getBlockId(x, y, z - i) == Block.obsidian.blockID && zDistNeg == 0) {
            zDistNeg = -i;
         }
      }

      int xDiff = xDistPos - xDistNeg + 1;
      int zDiff = zDistPos - zDistNeg + 1;
      if ((xDiff >= 4 || zDiff >= 4) && (xDiff <= 23 || zDiff <= 23) && (xDiff <= 23 || zDiff >= 4) && (zDiff <= 23 || xDiff >= 4)) {
         int isX = 0;
         int isZ = 0;
         if (xDistPos != 0 && xDistNeg != 0) {
            zDistPos = 0;
            zDistNeg = 0;
            isX = 1;
         } else if (zDistPos != 0 && zDistNeg != 0) {
            xDistPos = 0;
            xDistNeg = 0;
            isZ = 1;
         }

         int yDist = 0;

         for (int i = 3; i < 22; i++) {
            if (world.getBlockId(x, y + i, z) == Block.obsidian.blockID) {
               yDist = i + 1;
               break;
            }
         }

         if (yDist == 0) {
            return false;
         } else {
            int lowerBound = xDistNeg + zDistNeg;
            int upperBound = xDistPos + zDistPos;

            for (int ix = lowerBound; ix <= upperBound; ix++) {
               for (int j = -1; j < yDist; j++) {
                  int id = world.getBlockId(x + isX * ix, y + j, z + isZ * ix);
                  if (ix != lowerBound && ix != upperBound || j != -1 && j != yDist - 1) {
                     if (ix != lowerBound && ix != upperBound && j != -1 && j != yDist - 1) {
                        if (!world.isAirBlock(x + isX * ix, y + j, z + isZ * ix)
                           && id != Block.fire.blockID
                           && id != BTWBlocks.largeCampfire.blockID
                           && id != BTWBlocks.mediumCampfire.blockID
                           && id != BTWBlocks.smallCampfire.blockID
                           && id != BTWBlocks.unlitCampfire.blockID) {
                           return false;
                        }
                     } else if (id != Block.obsidian.blockID) {
                        return false;
                     }
                  }
               }
            }

            for (int ix = lowerBound + 1; ix < upperBound; ix++) {
               for (int jx = 0; jx < yDist - 1; jx++) {
                  world.setBlock(x + isX * ix, y + jx, z + isZ * ix, Block.portal.blockID, 0, 2);
               }
            }

            WorldUtils.gameProgressSetNetherBeenAccessedServerOnly();
            return true;
         }
      } else {
         return false;
      }
   }

   @Override
   public void setBlockBoundsBasedOnState(IBlockAccess blockAccess, int i, int j, int k) {
   }

   @Override
   public AxisAlignedBB getBlockBoundsFromPoolBasedOnState(IBlockAccess blockAccess, int i, int j, int k) {
      float fHalfWidth;
      float fHalfDepth;
      if (blockAccess.getBlockId(i - 1, j, k) != this.blockID && blockAccess.getBlockId(i + 1, j, k) != this.blockID) {
         fHalfWidth = 0.125F;
         fHalfDepth = 0.5F;
      } else {
         fHalfWidth = 0.5F;
         fHalfDepth = 0.125F;
      }

      return AxisAlignedBB.getAABBPool().getAABB(0.5F - fHalfWidth, 0.0, 0.5F - fHalfDepth, 0.5F + fHalfWidth, 1.0, 0.5F + fHalfDepth);
   }

   @Override
   public ItemStack getStackRetrievedByBlockDispenser(World world, int i, int j, int k) {
      return null;
   }
}
