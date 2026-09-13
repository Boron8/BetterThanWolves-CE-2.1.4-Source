package btw.block.blocks;

import btw.block.BTWBlocks;
import java.util.Random;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.Block;
import net.minecraft.src.BlockCactus;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.World;

public class CactusBlock extends BlockCactus {
   public CactusBlock(int iBlockID) {
      super(iBlockID);
      this.setAxesEffectiveOn(true);
      this.c(0.4F);
      this.setBuoyant();
      this.a(m);
      this.c("cactus");
   }

   @Override
   public void updateTick(World world, int i, int j, int k, Random rand) {
      if (world.provider.dimensionId != 1) {
         super.updateTick(world, i, j, k, rand);
      }
   }

   @Override
   public boolean canPlaceBlockAt(World world, int x, int y, int z) {
      if (!super.canPlaceBlockAt(world, x, y, z)) {
         return false;
      } else {
         int blockBelowID = world.getBlockId(x, y - 1, z);
         return blockBelowID == BTWBlocks.planter.blockID || blockBelowID == BTWBlocks.planterWithSoil.blockID || blockBelowID == this.blockID;
      }
   }

   @Override
   public boolean canBlockStay(World world, int i, int j, int k) {
      if (this.canStayNextToBlock(world, i - 1, j, k)
         && this.canStayNextToBlock(world, i + 1, j, k)
         && this.canStayNextToBlock(world, i, j, k - 1)
         && this.canStayNextToBlock(world, i, j, k + 1)) {
         int iBlockBelowID = world.getBlockId(i, j - 1, k);
         Block blockBelow = Block.blocksList[iBlockBelowID];
         return iBlockBelowID == this.blockID || blockBelow != null && blockBelow.canCactusGrowOnBlock(world, i, j - 1, k);
      } else {
         return false;
      }
   }

   @Override
   public void onStruckByLightning(World world, int i, int j, int k) {
      world.setBlockToAir(i, j, k);
      world.playAuxSFX(2282, i, j, k, 0);
      if (world.getBlockId(i, j - 1, k) == this.blockID) {
         this.onStruckByLightning(world, i, j - 1, k);
      }
   }

   protected boolean canStayNextToBlock(World world, int i, int j, int k) {
      return !world.getBlockMaterial(i, j, k).isSolid() || world.getBlockId(i, j, k) == BTWBlocks.web.blockID;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean renderBlock(RenderBlocks renderer, int i, int j, int k) {
      renderer.setRenderBounds(this.getBlockBoundsFromPoolBasedOnState(renderer.blockAccess, i, j, k));
      return renderer.renderBlockCactus(this, i, j, k);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean shouldSideBeRendered(IBlockAccess blockAccess, int i, int j, int k, int iSide) {
      return iSide != 0 && iSide != 1 ? true : !blockAccess.isBlockOpaqueCube(i, j, k) && blockAccess.getBlockId(i, j, k) != Block.cactus.blockID;
   }
}
