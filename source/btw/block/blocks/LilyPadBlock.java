package btw.block.blocks;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.BlockLilyPad;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.World;

public class LilyPadBlock extends BlockLilyPad {
   public LilyPadBlock(int iBlockID) {
      super(iBlockID);
      this.setBuoyant();
      this.initBlockBounds(0.0, 0.0, 0.0, 1.0, 0.015625, 1.0);
   }

   @Override
   public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k) {
      return this.getBlockBoundsFromPoolBasedOnState(world, i, j, k).offset(i, j, k);
   }

   @Override
   protected boolean canGrowOnBlock(World world, int i, int j, int k) {
      return world.getBlockId(i, j, k) == Block.waterStill.blockID;
   }

   @Override
   public boolean canGroundCoverRestOnBlock(World world, int i, int j, int k) {
      return false;
   }

   @Override
   public boolean canBlocksBePlacedAgainstThisBlock(World world, int x, int y, int z) {
      return false;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean renderBlock(RenderBlocks renderer, int i, int j, int k) {
      renderer.setRenderBounds(this.getBlockBoundsFromPoolBasedOnState(renderer.blockAccess, i, j, k));
      return renderer.renderBlockLilyPad(this, i, j, k);
   }
}
