package btw.block.blocks;

import java.util.Random;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.BlockDragonEgg;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.World;

public class DragonEggBlock extends BlockDragonEgg {
   public DragonEggBlock(int iBlockID) {
      super(iBlockID);
      this.initBlockBounds(0.0625, 0.0, 0.0625, 0.9375, 1.0, 0.9375);
      this.a(CreativeTabs.tabDecorations);
   }

   @Override
   public void updateTick(World world, int i, int j, int k, Random rand) {
      this.checkForFall(world, i, j, k);
   }

   @Override
   public void onBlockDestroyedLandingFromFall(World world, int i, int j, int k, int iMetadata) {
      this.c(world, i, j, k, iMetadata, 0);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean renderBlock(RenderBlocks renderer, int i, int j, int k) {
      renderer.setRenderBounds(this.getBlockBoundsFromPoolBasedOnState(renderer.blockAccess, i, j, k));
      return renderer.renderBlockDragonEgg(this, i, j, k);
   }
}
