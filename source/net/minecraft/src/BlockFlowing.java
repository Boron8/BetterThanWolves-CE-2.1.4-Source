package net.minecraft.src;

import btw.block.BTWBlocks;
import btw.world.util.WorldUtils;
import java.util.Random;

public class BlockFlowing extends BlockFluid {
   int numAdjacentSources = 0;
   boolean[] isOptimalFlowDirection = new boolean[4];
   int[] flowCost = new int[4];

   protected BlockFlowing(int par1, Material par2Material) {
      super(par1, par2Material);
   }

   private void updateFlow(World par1World, int par2, int par3, int par4) {
      int var5 = par1World.getBlockMetadata(par2, par3, par4);
      par1World.setBlock(par2, par3, par4, this.blockID + 1, var5, 2);
   }

   @Override
   public boolean getBlocksMovement(IBlockAccess par1IBlockAccess, int par2, int par3, int par4) {
      return this.blockMaterial != Material.lava;
   }

   @Override
   public void updateTick(World par1World, int par2, int par3, int par4, Random par5Random) {
      int var6 = this.k_(par1World, par2, par3, par4);
      byte var7 = 1;
      if (this.blockMaterial == Material.lava && !par1World.provider.isHellWorld) {
         var7 = 2;
      }

      boolean var8 = true;
      if (var6 > 0) {
         byte var9 = -100;
         this.numAdjacentSources = 0;
         int var12 = this.getSmallestFlowDecay(par1World, par2 - 1, par3, par4, var9);
         var12 = this.getSmallestFlowDecay(par1World, par2 + 1, par3, par4, var12);
         var12 = this.getSmallestFlowDecay(par1World, par2, par3, par4 - 1, var12);
         var12 = this.getSmallestFlowDecay(par1World, par2, par3, par4 + 1, var12);
         var12 = this.getSmallestFlowDecayFromCustomSources(par1World, par2, par3, par4, var12);
         int iTickRate = this.a(par1World);
         int var10 = var12 + var7;
         if (var10 >= 8 || var12 < 0) {
            var10 = -1;
         }

         if (this.k_(par1World, par2, par3 + 1, par4) >= 0) {
            int var11 = this.k_(par1World, par2, par3 + 1, par4);
            if (var11 >= 8) {
               var10 = var11;
            } else {
               var10 = var11 + 8;
            }
         }

         if (this.numAdjacentSources >= 2 && this.blockMaterial == Material.water) {
            if (par1World.getBlockMaterial(par2, par3 - 1, par4).isSolid()) {
               var10 = 0;
            } else if (par1World.getBlockMaterial(par2, par3 - 1, par4) == this.blockMaterial && par1World.getBlockMetadata(par2, par3 - 1, par4) == 0) {
               var10 = 0;
            }
         }

         if (this.blockMaterial == Material.lava && var6 < 8 && var10 < 8 && var10 > var6) {
            iTickRate += iTickRate * (1 + par5Random.nextInt(4));
         }

         if (var10 == var6) {
            if (var8) {
               this.updateFlow(par1World, par2, par3, par4);
            }
         } else {
            var6 = var10;
            if (var10 < 0) {
               par1World.setBlockToAir(par2, par3, par4);
            } else {
               par1World.setBlockMetadataWithNotify(par2, par3, par4, var10, 2);
               par1World.scheduleBlockUpdate(par2, par3, par4, this.blockID, iTickRate);
               par1World.notifyBlocksOfNeighborChange(par2, par3, par4, this.blockID);
            }
         }
      } else {
         this.updateFlow(par1World, par2, par3, par4);
      }

      if (this.liquidCanDisplaceBlock(par1World, par2, par3 - 1, par4)) {
         if (this.blockMaterial == Material.lava && par1World.getBlockMaterial(par2, par3 - 1, par4) == Material.water) {
            par1World.setBlock(par2, par3 - 1, par4, BTWBlocks.lavaPillow.blockID);
            this.j(par1World, par2, par3 - 1, par4);
            return;
         }

         if (var6 >= 8) {
            this.flowIntoBlock(par1World, par2, par3 - 1, par4, var6);
         } else {
            this.flowIntoBlock(par1World, par2, par3 - 1, par4, var6 + 8);
         }
      } else if (var6 >= 0 && (var6 == 0 || this.blockBlocksFlow(par1World, par2, par3 - 1, par4))) {
         boolean[] var13 = this.getOptimalFlowDirections(par1World, par2, par3, par4);
         int var10x = var6 + var7;
         if (var6 >= 8) {
            var10x = 1;
         }

         if (var10x >= 8) {
            return;
         }

         if (var13[0]) {
            this.flowIntoBlock(par1World, par2 - 1, par3, par4, var10x);
         }

         if (var13[1]) {
            this.flowIntoBlock(par1World, par2 + 1, par3, par4, var10x);
         }

         if (var13[2]) {
            this.flowIntoBlock(par1World, par2, par3, par4 - 1, var10x);
         }

         if (var13[3]) {
            this.flowIntoBlock(par1World, par2, par3, par4 + 1, var10x);
         }
      }
   }

   private void flowIntoBlock(World par1World, int par2, int par3, int par4, int par5) {
      if (this.liquidCanDisplaceBlock(par1World, par2, par3, par4)) {
         int var6 = par1World.getBlockId(par2, par3, par4);
         if (var6 > 0) {
            if (this.blockMaterial == Material.lava) {
               this.j(par1World, par2, par3, par4);
            } else {
               Block.blocksList[var6].onFluidFlowIntoBlock(par1World, par2, par3, par4, this);
            }
         }

         par1World.setBlock(par2, par3, par4, this.blockID, par5, 3);
      }
   }

   private int calculateFlowCost(World par1World, int par2, int par3, int par4, int par5, int par6) {
      int var7 = 1000;

      for (int var8 = 0; var8 < 4; var8++) {
         if ((var8 != 0 || par6 != 1) && (var8 != 1 || par6 != 0) && (var8 != 2 || par6 != 3) && (var8 != 3 || par6 != 2)) {
            int var9 = par2;
            int var11 = par4;
            if (var8 == 0) {
               var9 = par2 - 1;
            }

            if (var8 == 1) {
               var9++;
            }

            if (var8 == 2) {
               var11 = par4 - 1;
            }

            if (var8 == 3) {
               var11++;
            }

            if (!this.blockBlocksFlow(par1World, var9, par3, var11)
               && (par1World.getBlockMaterial(var9, par3, var11) != this.blockMaterial || par1World.getBlockMetadata(var9, par3, var11) != 0)) {
               if (!this.blockBlocksFlow(par1World, var9, par3 - 1, var11)) {
                  return par5;
               }

               if (par5 < 4) {
                  int var12 = this.calculateFlowCost(par1World, var9, par3, var11, par5 + 1, var8);
                  if (var12 < var7) {
                     var7 = var12;
                  }
               }
            }
         }
      }

      return var7;
   }

   private boolean[] getOptimalFlowDirections(World par1World, int par2, int par3, int par4) {
      for (int var5 = 0; var5 < 4; var5++) {
         this.flowCost[var5] = 1000;
         int var6 = par2;
         int var8 = par4;
         if (var5 == 0) {
            var6 = par2 - 1;
         }

         if (var5 == 1) {
            var6++;
         }

         if (var5 == 2) {
            var8 = par4 - 1;
         }

         if (var5 == 3) {
            var8++;
         }

         if (!this.blockBlocksFlow(par1World, var6, par3, var8)
            && (par1World.getBlockMaterial(var6, par3, var8) != this.blockMaterial || par1World.getBlockMetadata(var6, par3, var8) != 0)) {
            if (this.blockBlocksFlow(par1World, var6, par3 - 1, var8)) {
               this.flowCost[var5] = this.calculateFlowCost(par1World, var6, par3, var8, 1, var5);
            } else {
               this.flowCost[var5] = 0;
            }
         }
      }

      int var8x = this.flowCost[0];

      for (int var6x = 1; var6x < 4; var6x++) {
         if (this.flowCost[var6x] < var8x) {
            var8x = this.flowCost[var6x];
         }
      }

      for (int var10 = 0; var10 < 4; var10++) {
         this.isOptimalFlowDirection[var10] = this.flowCost[var10] == var8x;
      }

      return this.isOptimalFlowDirection;
   }

   protected int getSmallestFlowDecay(World par1World, int par2, int par3, int par4, int par5) {
      int var6 = this.k_(par1World, par2, par3, par4);
      if (var6 < 0) {
         return par5;
      } else {
         if (var6 == 0) {
            this.numAdjacentSources++;
         }

         if (var6 >= 8) {
            var6 = 0;
         }

         return par5 >= 0 && var6 >= par5 ? par5 : var6;
      }
   }

   private boolean liquidCanDisplaceBlock(World par1World, int par2, int par3, int par4) {
      Material var5 = par1World.getBlockMaterial(par2, par3, par4);
      return var5 == this.blockMaterial ? false : (var5 == Material.lava ? false : !this.blockBlocksFlow(par1World, par2, par3, par4));
   }

   @Override
   public void onBlockAdded(World par1World, int par2, int par3, int par4) {
      super.onBlockAdded(par1World, par2, par3, par4);
      if (par1World.getBlockId(par2, par3, par4) == this.blockID) {
         par1World.scheduleBlockUpdate(par2, par3, par4, this.blockID, this.a(par1World));
      }
   }

   @Override
   public boolean func_82506_l() {
      return false;
   }

   @Override
   public void onNeighborBlockChange(World world, int i, int j, int k, int iNeighborBlockID) {
      super.onNeighborBlockChange(world, i, j, k, iNeighborBlockID);
      if (!world.isUpdatePendingThisTickForBlock(i, j, k, this.blockID)) {
         world.scheduleBlockUpdate(i, j, k, this.blockID, this.a(world));
      }
   }

   private int getSmallestFlowDecayFromCustomSources(World world, int i, int j, int k, int iSmallestFlowDecay) {
      if (iSmallestFlowDecay != 0) {
         for (int iFacing = 0; iFacing < 6; iFacing++) {
            int iTargetDecay = WorldUtils.isValidSourceForFluidBlockToFacing(world, i, j, k, iFacing);
            if (iTargetDecay == 0) {
               iSmallestFlowDecay = 0;
               break;
            }

            if (iTargetDecay > 0 && (iSmallestFlowDecay < 0 || iTargetDecay < iSmallestFlowDecay)) {
               iSmallestFlowDecay = iTargetDecay;
            }
         }
      }

      return iSmallestFlowDecay;
   }

   public boolean blockBlocksFlow(World world, int i, int j, int k) {
      Block block = r[world.getBlockId(i, j, k)];
      return block != null && block.getPreventsFluidFlow(world, i, j, k, this);
   }
}
