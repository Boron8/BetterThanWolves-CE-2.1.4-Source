package btw.block.blocks;

import btw.block.util.Flammability;
import java.util.Random;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.BlockVine;
import net.minecraft.src.Direction;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.ItemStack;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.World;

public class VineBlock extends BlockVine {
   public VineBlock(int iBlockID) {
      super(iBlockID);
      this.c(0.2F);
      this.setAxesEffectiveOn(true);
      this.setBuoyant();
      this.setFireProperties(Flammability.VINES);
      this.initBlockBounds(0.0, 0.0, 0.0, 1.0, 1.0, 1.0);
      this.a(i);
      this.c("vine");
   }

   @Override
   public void updateTick(World world, int i, int j, int k, Random random) {
      if (world.provider.dimensionId != 1 && world.rand.nextInt(4) == 0) {
         byte var6 = 4;
         int var7 = 5;
         boolean var8 = false;

         label137:
         for (int var9 = i - var6; var9 <= i + var6; var9++) {
            for (int var10 = k - var6; var10 <= k + var6; var10++) {
               for (int var11 = j - 1; var11 <= j + 1; var11++) {
                  if (world.getBlockId(var9, var11, var10) == this.blockID) {
                     if (--var7 <= 0) {
                        var8 = true;
                        break label137;
                     }
                  }
               }
            }
         }

         int var15 = world.getBlockMetadata(i, j, k);
         int var10 = world.rand.nextInt(6);
         int var11x = Direction.facingToDirection[var10];
         if (var10 == 1 && j < 255 && world.isAirBlock(i, j + 1, k)) {
            if (var8) {
               return;
            }

            int var12 = world.rand.nextInt(16) & var15;
            if (var12 > 0) {
               for (int var13 = 0; var13 <= 3; var13++) {
                  if (!this.canBePlacedOn(world.getBlockId(i + Direction.offsetX[var13], j + 1, k + Direction.offsetZ[var13]))) {
                     var12 &= ~(1 << var13);
                  }
               }

               if (var12 > 0) {
                  world.setBlockAndMetadataWithNotify(i, j + 1, k, this.blockID, var12);
               }
            }
         } else if (var10 >= 2 && var10 <= 5 && (var15 & 1 << var11x) == 0) {
            if (var8) {
               return;
            }

            int var12 = world.getBlockId(i + Direction.offsetX[var11x], j, k + Direction.offsetZ[var11x]);
            if (world.isAirBlock(i + Direction.offsetX[var11x], j, k + Direction.offsetZ[var11x]) || Block.blocksList[var12] == null) {
               int var13x = var11x + 1 & 3;
               int var14 = var11x + 3 & 3;
               if ((var15 & 1 << var13x) != 0
                  && this.canBePlacedOn(
                     world.getBlockId(i + Direction.offsetX[var11x] + Direction.offsetX[var13x], j, k + Direction.offsetZ[var11x] + Direction.offsetZ[var13x])
                  )) {
                  world.setBlockAndMetadataWithNotify(i + Direction.offsetX[var11x], j, k + Direction.offsetZ[var11x], this.blockID, 1 << var13x);
               } else if ((var15 & 1 << var14) != 0
                  && this.canBePlacedOn(
                     world.getBlockId(i + Direction.offsetX[var11x] + Direction.offsetX[var14], j, k + Direction.offsetZ[var11x] + Direction.offsetZ[var14])
                  )) {
                  world.setBlockAndMetadataWithNotify(i + Direction.offsetX[var11x], j, k + Direction.offsetZ[var11x], this.blockID, 1 << var14);
               } else if ((var15 & 1 << var13x) != 0
                  && world.isAirBlock(i + Direction.offsetX[var11x] + Direction.offsetX[var13x], j, k + Direction.offsetZ[var11x] + Direction.offsetZ[var13x])
                  && this.canBePlacedOn(world.getBlockId(i + Direction.offsetX[var13x], j, k + Direction.offsetZ[var13x]))) {
                  world.setBlockAndMetadataWithNotify(
                     i + Direction.offsetX[var11x] + Direction.offsetX[var13x],
                     j,
                     k + Direction.offsetZ[var11x] + Direction.offsetZ[var13x],
                     this.blockID,
                     1 << (var11x + 2 & 3)
                  );
               } else if ((var15 & 1 << var14) != 0
                  && world.isAirBlock(i + Direction.offsetX[var11x] + Direction.offsetX[var14], j, k + Direction.offsetZ[var11x] + Direction.offsetZ[var14])
                  && this.canBePlacedOn(world.getBlockId(i + Direction.offsetX[var14], j, k + Direction.offsetZ[var14]))) {
                  world.setBlockAndMetadataWithNotify(
                     i + Direction.offsetX[var11x] + Direction.offsetX[var14],
                     j,
                     k + Direction.offsetZ[var11x] + Direction.offsetZ[var14],
                     this.blockID,
                     1 << (var11x + 2 & 3)
                  );
               } else if (this.canBePlacedOn(world.getBlockId(i + Direction.offsetX[var11x], j + 1, k + Direction.offsetZ[var11x]))) {
                  world.setBlockAndMetadataWithNotify(i + Direction.offsetX[var11x], j, k + Direction.offsetZ[var11x], this.blockID, 0);
               }
            } else if (Block.blocksList[var12].blockMaterial.isOpaque() && Block.blocksList[var12].renderAsNormalBlock()) {
               world.setBlockMetadataWithNotify(i, j, k, var15 | 1 << var11x);
            }
         } else if (j > 1) {
            int var12 = world.getBlockId(i, j - 1, k);
            if (world.isAirBlock(i, j - 1, k)) {
               int var13x = world.rand.nextInt(16) & var15;
               if (var13x > 0) {
                  world.setBlockAndMetadataWithNotify(i, j - 1, k, this.blockID, var13x);
               }
            } else if (var12 == this.blockID) {
               int var13x = world.rand.nextInt(16) & var15;
               int var14 = world.getBlockMetadata(i, j - 1, k);
               if (var14 != (var14 | var13x)) {
                  world.setBlockMetadataWithNotify(i, j - 1, k, var14 | var13x);
               }
            }
         }
      }
   }

   @Override
   public void onEntityCollidedWithBlock(World par1World, int par2, int par3, int par4, Entity par5Entity) {
      if (par5Entity.isAffectedByMovementModifiers() && par5Entity.onGround) {
         boolean bIsOnLadder = false;
         if (par5Entity instanceof EntityLiving) {
            bIsOnLadder = ((EntityLiving)par5Entity).isOnLadder();
         }

         if (!bIsOnLadder) {
            par5Entity.motionX *= 0.8;
            par5Entity.motionZ *= 0.8;
         }
      }
   }

   @Override
   public boolean canSpitWebReplaceBlock(World world, int i, int j, int k) {
      return true;
   }

   @Override
   public boolean isReplaceableVegetation(World world, int i, int j, int k) {
      return true;
   }

   @Override
   public boolean isBlockClimbable(World world, int i, int j, int k) {
      return true;
   }

   @Override
   public ItemStack getStackRetrievedByBlockDispenser(World world, int i, int j, int k) {
      int iMetadata = world.getBlockMetadata(i, j, k);
      return this.c_(iMetadata);
   }

   @Override
   public void setBlockBoundsBasedOnState(IBlockAccess blockAccess, int i, int j, int k) {
   }

   @Override
   public void setBlockBoundsForItemRender() {
   }

   @Override
   public AxisAlignedBB getBlockBoundsFromPoolBasedOnState(IBlockAccess blockAccess, int i, int j, int k) {
      int iMetadata = blockAccess.getBlockMetadata(i, j, k);
      float fMinX = 1.0F;
      float fMinY = 1.0F;
      float fMinZ = 1.0F;
      float fMaxX = 0.0F;
      float fMaxY = 0.0F;
      float fMaxZ = 0.0F;
      boolean var13 = iMetadata > 0;
      if ((iMetadata & 2) != 0) {
         fMaxX = Math.max(fMaxX, 0.0625F);
         fMinX = 0.0F;
         fMinY = 0.0F;
         fMaxY = 1.0F;
         fMinZ = 0.0F;
         fMaxZ = 1.0F;
         var13 = true;
      }

      if ((iMetadata & 8) != 0) {
         fMinX = Math.min(fMinX, 0.9375F);
         fMaxX = 1.0F;
         fMinY = 0.0F;
         fMaxY = 1.0F;
         fMinZ = 0.0F;
         fMaxZ = 1.0F;
         var13 = true;
      }

      if ((iMetadata & 4) != 0) {
         fMaxZ = Math.max(fMaxZ, 0.0625F);
         fMinZ = 0.0F;
         fMinX = 0.0F;
         fMaxX = 1.0F;
         fMinY = 0.0F;
         fMaxY = 1.0F;
         var13 = true;
      }

      if ((iMetadata & 1) != 0) {
         fMinZ = Math.min(fMinZ, 0.9375F);
         fMaxZ = 1.0F;
         fMinX = 0.0F;
         fMaxX = 1.0F;
         fMinY = 0.0F;
         fMaxY = 1.0F;
         var13 = true;
      }

      if (!var13 && this.canBePlacedOn(blockAccess.getBlockId(i, j + 1, k))) {
         fMinY = Math.min(fMinY, 0.9375F);
         fMaxY = 1.0F;
         fMinX = 0.0F;
         fMaxX = 1.0F;
         fMinZ = 0.0F;
         fMaxZ = 1.0F;
      }

      return AxisAlignedBB.getAABBPool().getAABB(fMinX, fMinY, fMinZ, fMaxX, fMaxY, fMaxZ);
   }

   private boolean canBePlacedOn(int par1) {
      if (par1 == 0) {
         return false;
      } else {
         Block var2 = Block.blocksList[par1];
         return var2.renderAsNormalBlock() && var2.blockMaterial.blocksMovement();
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean renderBlock(RenderBlocks renderer, int i, int j, int k) {
      return renderer.renderBlockVine(this, i, j, k);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean shouldSideBeRendered(IBlockAccess blockAccess, int i, int j, int k, int iSide) {
      return true;
   }
}
