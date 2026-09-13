package btw.block.blocks;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.Material;
import net.minecraft.src.RenderBlocks;

public abstract class OreChunkStorageBlock extends FallingFullBlock {
   protected OreChunkStorageBlock(int iBlockID) {
      super(iBlockID, Material.rock);
      this.c(1.0F);
      this.b(5.0F);
      this.setPicksEffectiveOn();
      this.a(Block.soundStoneFootstep);
      this.a(CreativeTabs.tabBlock);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void renderBlockSecondPass(RenderBlocks renderBlocks, int i, int j, int k, boolean bFirstPassResult) {
      this.renderCookingByKiLnOverlay(renderBlocks, i, j, k, bFirstPassResult);
   }
}
