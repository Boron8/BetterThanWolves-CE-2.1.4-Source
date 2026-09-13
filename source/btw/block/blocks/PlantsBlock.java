package btw.block.blocks;

import btw.block.BTWBlocks;
import btw.world.util.WorldUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Material;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.World;

public abstract class PlantsBlock extends Block {
   protected PlantsBlock(int iBlockID, Material material) {
      super(iBlockID, material);
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
   public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k) {
      return null;
   }

   @Override
   public void breakBlock(World world, int i, int j, int k, int iBlockID, int iMetadata) {
      super.breakBlock(world, i, j, k, iBlockID, iMetadata);
      Block blockBelow = Block.blocksList[world.getBlockId(i, j - 1, k)];
      if (blockBelow != null) {
         blockBelow.notifyOfPlantAboveRemoved(world, i, j - 1, k, this);
      }
   }

   @Override
   public void onEntityCollidedWithBlock(World world, int i, int j, int k, Entity entity) {
      if (entity.isAffectedByMovementModifiers() && entity.onGround) {
         entity.motionX *= 0.8;
         entity.motionZ *= 0.8;
      }
   }

   @Override
   public boolean canPlaceBlockAt(World world, int i, int j, int k) {
      return super.canPlaceBlockAt(world, i, j, k) && this.canGrowOnBlock(world, i, j - 1, k);
   }

   @Override
   public boolean canBlockStay(World world, int i, int j, int k) {
      return this.canGrowOnBlock(world, i, j - 1, k);
   }

   @Override
   public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer player, int iFacing, float fXClick, float fYClick, float fZClick) {
      if (this.getWeedsGrowthLevel(world, i, j, k) > 0) {
         if (!world.isRemote) {
            this.removeWeeds(world, i, j, k);
            world.playAuxSFX(2001, i, j, k, Block.crops.blockID + 24576);
         }

         return true;
      } else {
         return false;
      }
   }

   @Override
   public int getWeedsGrowthLevel(IBlockAccess blockAccess, int i, int j, int k) {
      int iBlockBelowID = blockAccess.getBlockId(i, j - 1, k);
      Block blockBelow = Block.blocksList[iBlockBelowID];
      return blockBelow != null && iBlockBelowID != this.blockID ? blockBelow.getWeedsGrowthLevel(blockAccess, i, j - 1, k) : 0;
   }

   @Override
   public void removeWeeds(World world, int i, int j, int k) {
      Block blockBelow = Block.blocksList[world.getBlockId(i, j - 1, k)];
      if (blockBelow != null) {
         blockBelow.removeWeeds(world, i, j - 1, k);
      }
   }

   @Override
   public boolean attemptToApplyFertilizerTo(World world, int i, int j, int k) {
      Block blockBelow = Block.blocksList[world.getBlockId(i, j - 1, k)];
      return blockBelow != null ? blockBelow.attemptToApplyFertilizerTo(world, i, j - 1, k) : false;
   }

   @Override
   public float groundCoverRestingOnVisualOffset(IBlockAccess blockAccess, int i, int j, int k) {
      return -1.0F;
   }

   @Override
   public boolean canGroundCoverRestOnBlock(World world, int i, int j, int k) {
      return world.doesBlockHaveSolidTopSurface(i, j - 1, k) && this.blockID != Block.waterlily.blockID;
   }

   @Override
   public void clientBreakBlock(World world, int i, int j, int k, int iBlockID, int iMetadata) {
      WorldUtils.clearAnyGroundCoverOnBlock(world, i, j, k);
   }

   protected boolean canGrowOnBlock(World world, int i, int j, int k) {
      Block blockOn = Block.blocksList[world.getBlockId(i, j, k)];
      return blockOn != null && blockOn.canWildVegetationGrowOnBlock(world, i, j, k);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean renderBlock(RenderBlocks renderer, int i, int j, int k) {
      renderer.setRenderBounds(this.getBlockBoundsFromPoolBasedOnState(renderer.blockAccess, i, j, k));
      renderer.renderCrossedSquares(this, i, j, k);
      BTWBlocks.weeds.renderWeeds(this, renderer, i, j, k);
      return true;
   }
}
