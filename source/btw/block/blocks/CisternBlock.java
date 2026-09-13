package btw.block.blocks;

import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.BlockCauldron;
import net.minecraft.src.Entity;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.World;

public class CisternBlock extends BlockCauldron {
   public CisternBlock(int iBlockID) {
      super(iBlockID);
      this.initBlockBounds(0.0, 0.0, 0.0, 1.0, 1.0, 1.0);
   }

   @Override
   public void addCollisionBoxesToList(World world, int i, int j, int k, AxisAlignedBB intersectingBox, List list, Entity entity) {
      AxisAlignedBB tempBox = this.b(world, i, j, k);
      tempBox.addToListIfIntersects(intersectingBox, list);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean renderBlock(RenderBlocks renderer, int i, int j, int k) {
      renderer.setRenderBounds(this.getBlockBoundsFromPoolBasedOnState(renderer.blockAccess, i, j, k));
      return renderer.renderBlockCauldron(this, i, j, k);
   }
}
