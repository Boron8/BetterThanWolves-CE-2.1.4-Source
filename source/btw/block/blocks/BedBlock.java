package btw.block.blocks;

import btw.block.BTWBlocks;
import btw.item.BTWItems;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.Block;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Item;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.World;

public class BedBlock extends BedBlockBase {
   public BedBlock(int blockID) {
      super(blockID);
      this.a(Block.soundClothFootstep);
      this.setBlockMaterial(BTWBlocks.plankMaterial);
      this.initBlockBounds(0.0, 0.0, 0.0, 1.0, 0.5625, 1.0);
   }

   @Override
   public boolean dropComponentItemsOnBadBreak(World world, int x, int y, int z, int iMetadata, float chanceOfDrop) {
      this.dropItemsIndividually(world, x, y, z, BTWItems.sawDust.itemID, 3, 0, chanceOfDrop);
      this.dropItemsIndividually(world, x, y, z, Item.stick.itemID, 1, 0, chanceOfDrop);
      this.dropItemsIndividually(world, x, y, z, BTWItems.padding.itemID, 2, 0, chanceOfDrop);
      return true;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean renderBlock(RenderBlocks renderer, int i, int j, int k) {
      renderer.setRenderBounds(this.getBlockBoundsFromPoolBasedOnState(renderer.blockAccess, i, j, k));
      return renderer.renderBlockBed(this, i, j, k);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean shouldSideBeRendered(IBlockAccess blockAccess, int iNeighborI, int iNeighborJ, int iNeighborK, int iSide) {
      return this.currentBlockRenderer.shouldSideBeRenderedBasedOnCurrentBounds(iNeighborI, iNeighborJ, iNeighborK, iSide);
   }

   @Override
   public int idPicked(World world, int x, int y, int z) {
      return Item.bed.itemID;
   }
}
