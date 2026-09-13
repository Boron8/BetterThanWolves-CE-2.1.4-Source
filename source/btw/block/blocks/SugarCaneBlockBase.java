package btw.block.blocks;

import btw.block.BTWBlocks;
import java.util.Random;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.Entity;
import net.minecraft.src.Material;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.World;

public abstract class SugarCaneBlockBase extends Block {
   private static final double WIDTH = 0.75;
   private static final double HALF_WIDTH = 0.375;

   public SugarCaneBlockBase(int id) {
      super(id, Material.plants);
      this.c(0.0F);
      this.setBuoyant();
      this.a(i);
      this.D();
      this.b(true);
      this.initBlockBounds(0.125, 0.0, 0.125, 0.875, 1.0, 0.875);
   }

   @Override
   public void updateTick(World world, int x, int y, int z, Random rand) {
      if (world.provider.dimensionId != 1 && this.canBlockStay(world, x, y, z) && world.isAirBlock(x, y + 1, z)) {
         int reedHeight = 1;

         while (Block.blocksList[world.getBlockId(x, y - reedHeight, z)] instanceof SugarCaneBlockBase) {
            reedHeight++;
         }

         if (reedHeight < 3) {
            int metadata = world.getBlockMetadata(x, y, z);
            if (metadata == 15) {
               world.setBlock(x, y + 1, z, BTWBlocks.sugarCane.blockID);
               world.SetBlockMetadataWithNotify(x, y, z, 0, 4);
            } else {
               world.SetBlockMetadataWithNotify(x, y, z, metadata + 1, 4);
            }
         }
      }
   }

   @Override
   public boolean isOpaqueCube() {
      return false;
   }

   @Override
   public boolean renderAsNormalBlock() {
      return false;
   }

   @Override
   public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
      return null;
   }

   @Override
   public void onNeighborBlockChange(World world, int x, int y, int z, int neighborID) {
      if (!this.canBlockStay(world, x, y, z)) {
         this.c(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
         world.setBlockToAir(x, y, z);
      }
   }

   @Override
   public boolean canBlockStay(World world, int x, int y, int z) {
      return this.canPlaceBlockAt(world, x, y, z);
   }

   @Override
   public boolean canPlaceBlockAt(World world, int x, int y, int z) {
      if (world.getBlockMaterial(x, y, z) == Material.water) {
         return false;
      } else {
         int blockBelowID = world.getBlockId(x, y - 1, z);
         Block blockBelow = Block.blocksList[blockBelowID];
         return blockBelowID == this.blockID
            || blockBelow != null
               && blockBelow.canReedsGrowOnBlock(world, x, y - 1, z)
               && blockBelow.isConsideredNeighbouringWaterForReedGrowthOn(world, x, y - 1, z);
      }
   }

   @Override
   public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity) {
      if (entity.isAffectedByMovementModifiers() && entity.onGround) {
         entity.motionX *= 0.8;
         entity.motionZ *= 0.8;
      }
   }

   @Override
   public boolean doesBlockDropAsItemOnSaw(World world, int z, int y, int x) {
      return true;
   }

   @Override
   public boolean getPreventsFluidFlow(World world, int x, int y, int z, Block fluidBlock) {
      return false;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean renderBlock(RenderBlocks renderer, int x, int y, int z) {
      renderer.setRenderBounds(this.getBlockBoundsFromPoolBasedOnState(renderer.blockAccess, x, y, z));
      return renderer.renderCrossedSquares(this, x, y, z);
   }
}
