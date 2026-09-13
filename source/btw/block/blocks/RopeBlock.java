package btw.block.blocks;

import btw.block.BTWBlocks;
import btw.item.BTWItems;
import btw.item.util.ItemUtils;
import java.util.Random;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.Block;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Material;
import net.minecraft.src.World;

public class RopeBlock extends Block {
   public static final double ROPE_WIDTH = 0.125;
   public static final double ROPE_HALF_WIDTH = 0.0625;

   public RopeBlock(int iBlockID) {
      super(iBlockID, Material.circuits);
      this.c(0.5F);
      this.setAxesEffectiveOn(true);
      this.setBuoyancy(1.0F);
      this.initBlockBounds(0.4375, 0.0, 0.4375, 0.5625, 1.0, 0.5625);
      this.a(i);
      this.c("fcBlockRope");
   }

   @Override
   public int idDropped(int iMetadata, Random random, int iFortuneModifier) {
      return BTWItems.rope.itemID;
   }

   @Override
   public void onNeighborBlockChange(World world, int i, int j, int k, int iBlockID) {
      int iBlockAboveID = world.getBlockId(i, j + 1, k);
      boolean bSupported = true;
      if (iBlockAboveID == BTWBlocks.anchor.blockID) {
         int iFacing = ((AnchorBlock)BTWBlocks.anchor).getFacing(world, i, j + 1, k);
         if (iFacing == 1) {
            bSupported = false;
         }
      } else if (iBlockAboveID != this.blockID && iBlockAboveID != BTWBlocks.pulley.blockID) {
         bSupported = false;
      }

      if (!bSupported) {
         this.c(world, i, j, k, world.getBlockMetadata(i, j, k), 0);
         world.setBlockWithNotify(i, j, k, 0);
      }
   }

   @Override
   public boolean canPlaceBlockAt(World world, int i, int j, int k) {
      int iBlockAboveID = world.getBlockId(i, j + 1, k);
      return iBlockAboveID == this.blockID || iBlockAboveID == BTWBlocks.anchor.blockID;
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
   public boolean isBlockClimbable(World world, int i, int j, int k) {
      return true;
   }

   @Override
   public boolean hasSmallCenterHardPointToFacing(IBlockAccess blockAccess, int i, int j, int k, int iFacing, boolean bIgnoreTransparency) {
      return iFacing == 0;
   }

   public void breakRope(World world, int i, int j, int k) {
      ItemUtils.ejectSingleItemWithRandomOffset(world, i, j, k, BTWItems.rope.itemID, 0);
      world.playAuxSFX(2001, i, j, k, this.blockID);
      world.setBlockWithNotify(i, j, k, 0);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public int idPicked(World world, int i, int j, int k) {
      return this.idDropped(world.getBlockMetadata(i, j, k), world.rand, 0);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean shouldSideBeRendered(IBlockAccess blockAccess, int iNeighborI, int iNeighborJ, int iNeighborK, int iSide) {
      if (iSide >= 2) {
         return true;
      } else {
         int iNeighborBlockID = blockAccess.getBlockId(iNeighborI, iNeighborJ, iNeighborK);
         return iNeighborBlockID != this.blockID && super.shouldSideBeRendered(blockAccess, iNeighborI, iNeighborJ, iNeighborK, iSide);
      }
   }
}
