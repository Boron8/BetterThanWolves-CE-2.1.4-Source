package btw.block.blocks;

import btw.block.BTWBlocks;
import btw.item.BTWItems;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.RenderBlocks;

public class CarrotBlockFlowers extends CarrotBlockBase {
   public CarrotBlockFlowers(int iBlockID) {
      super(iBlockID);
      this.c("fcBlockCarrotFlowers");
   }

   @Override
   protected int getCropItemID() {
      return BTWItems.carrotSeeds.itemID;
   }

   @Override
   protected int getSeedItemID() {
      return BTWItems.carrotSeeds.itemID;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean renderBlock(RenderBlocks renderer, int x, int y, int z) {
      renderer.setRenderBounds(this.getBlockBoundsFromPoolBasedOnState(renderer.blockAccess, x, y, z));
      BTWBlocks.weeds.renderWeeds(this, renderer, x, y, z);
      return renderer.renderCrossedSquares(this, x, y, z);
   }
}
