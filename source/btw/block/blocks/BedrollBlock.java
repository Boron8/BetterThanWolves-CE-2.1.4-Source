package btw.block.blocks;

import btw.item.BTWItems;
import java.util.Random;
import net.minecraft.src.Block;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Icon;
import net.minecraft.src.IconRegister;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.World;

public class BedrollBlock extends BedBlockBase {
   private static final double BEDROLL_HEIGHT = 0.125;

   public BedrollBlock(int blockID) {
      super(blockID);
      this.a(Block.soundClothFootstep);
      this.initBlockBounds(0.0, 0.0, 0.0, 1.0, 0.125, 1.0);
   }

   @Override
   public boolean blocksHealing() {
      return true;
   }

   @Override
   public int idDropped(int meta, Random rand, int par3) {
      return e_(meta) ? 0 : BTWItems.bedroll.itemID;
   }

   @Override
   public Icon getIcon(int side, int meta) {
      return this.blockIcon;
   }

   @Override
   public boolean renderBlock(RenderBlocks renderer, int x, int y, int z) {
      renderer.setRenderBounds(this.getBlockBoundsFromPoolBasedOnState(renderer.blockAccess, x, y, z));
      renderer.renderStandardBlock(this, x, y, z);
      int meta = renderer.blockAccess.getBlockMetadata(x, y, z);
      if (e_(meta)) {
         int direction = j(meta);
         double distOuter = 0.125;
         double distY = 0.0625;
         double distInner = 0.5;
         switch (direction) {
            case 0:
               renderer.setRenderBounds(distOuter, 0.125, 1.0 - distInner, 1.0 - distOuter, 0.125 + distY, 1.0 - distOuter);
               break;
            case 1:
               renderer.setRenderBounds(distOuter, 0.125, distOuter, 1.0 - distInner, 0.125 + distY, 1.0 - distOuter);
               break;
            case 2:
               renderer.setRenderBounds(distOuter, 0.125, distOuter, 1.0 - distOuter, 0.125 + distY, 1.0 - distInner);
               break;
            case 3:
               renderer.setRenderBounds(distInner, 0.125, distOuter, 1.0 - distOuter, 0.125 + distY, 1.0 - distOuter);
         }

         renderer.renderStandardBlock(this, x, y, z);
      }

      return true;
   }

   @Override
   public void registerIcons(IconRegister register) {
      this.blockIcon = register.registerIcon("cloth_0");
   }

   @Override
   public boolean shouldSideBeRendered(IBlockAccess blockAccess, int neighborX, int neighborY, int neighborZ, int side) {
      return side == 0 ? this.currentBlockRenderer.shouldSideBeRenderedBasedOnCurrentBounds(neighborX, neighborY, neighborZ, side) : true;
   }

   @Override
   public int idPicked(World world, int x, int y, int z) {
      return BTWItems.bedroll.itemID;
   }
}
