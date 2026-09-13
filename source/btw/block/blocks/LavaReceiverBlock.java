package btw.block.blocks;

import btw.client.render.util.RenderUtils;
import java.util.Random;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.Block;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Icon;
import net.minecraft.src.Material;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.World;

public abstract class LavaReceiverBlock extends MortarReceiverBlock {
   public static final int LAVA_FILL_TICK_RATE = 20;
   public static final int LAVA_HARDEN_TICK_RATE = 2;

   public LavaReceiverBlock(int iBlockID, Material material) {
      super(iBlockID, material);
      this.b(true);
   }

   @Override
   public void onBlockAdded(World world, int i, int j, int k) {
      if (!this.scheduleUpdatesForLavaAndWaterContact(world, i, j, k)) {
         super.onBlockAdded(world, i, j, k);
      }
   }

   @Override
   public void updateTick(World world, int i, int j, int k, Random rand) {
      if (!this.checkForFall(world, i, j, k)) {
         if (this.getHasLavaInCracks(world, i, j, k)) {
            if (this.hasWaterAbove(world, i, j, k)) {
               world.playAuxSFX(2227, i, j, k, 0);
               world.setBlockAndMetadataWithNotify(i, j, k, Block.stone.blockID, this.getStrata(world, i, j, k));
               return;
            }
         } else if (this.hasLavaAbove(world, i, j, k)) {
            this.setHasLavaInCracks(world, i, j, k, true);
         }
      }
   }

   @Override
   public void randomUpdateTick(World world, int i, int j, int k, Random rand) {
      if (this.getHasLavaInCracks(world, i, j, k) && world.isRainingAtPos(i, j + 1, k)) {
         world.playAuxSFX(2227, i, j, k, 0);
         world.setBlockAndMetadataWithNotify(i, j, k, Block.stone.blockID, this.getStrata(world, i, j, k));
      }
   }

   @Override
   public void onNeighborBlockChange(World world, int i, int j, int k, int iNeighborBlockID) {
      if (!this.scheduleUpdatesForLavaAndWaterContact(world, i, j, k)) {
         super.a(world, i, j, k, iNeighborBlockID);
      }
   }

   @Override
   protected boolean canSilkHarvest() {
      return false;
   }

   @Override
   public boolean getIsBlockWarm(IBlockAccess blockAccess, int i, int j, int k) {
      return this.getHasLavaInCracks(blockAccess, i, j, k);
   }

   public int getStrata(IBlockAccess blockAccess, int i, int j, int k) {
      return this.getStrata(blockAccess.getBlockMetadata(i, j, k));
   }

   public int getStrata(int iMetadata) {
      return (iMetadata & 12) >>> 2;
   }

   protected boolean getHasLavaInCracks(int iMetadata) {
      return (iMetadata & 1) != 0;
   }

   protected boolean getHasLavaInCracks(IBlockAccess blockAccess, int i, int j, int k) {
      return this.getHasLavaInCracks(blockAccess.getBlockMetadata(i, j, k));
   }

   protected int setHasLavaInCracks(int iMetadata, boolean bHasLava) {
      if (bHasLava) {
         iMetadata |= 1;
      } else {
         iMetadata &= -2;
      }

      return iMetadata;
   }

   protected void setHasLavaInCracks(World world, int i, int j, int k, boolean bHasLava) {
      int iMetadata = this.setHasLavaInCracks(world.getBlockMetadata(i, j, k), bHasLava);
      world.setBlockMetadataWithNotify(i, j, k, iMetadata);
   }

   protected boolean hasLavaAbove(IBlockAccess blockAccess, int i, int j, int k) {
      Block blockAbove = Block.blocksList[blockAccess.getBlockId(i, j + 1, k)];
      return blockAbove != null && blockAbove.blockMaterial == Material.lava;
   }

   protected boolean hasWaterAbove(IBlockAccess blockAccess, int i, int j, int k) {
      Block blockAbove = Block.blocksList[blockAccess.getBlockId(i, j + 1, k)];
      return blockAbove != null && blockAbove.blockMaterial == Material.water;
   }

   protected boolean scheduleUpdatesForLavaAndWaterContact(World world, int i, int j, int k) {
      if (this.getHasLavaInCracks(world, i, j, k)) {
         if (this.hasWaterAbove(world, i, j, k)) {
            if (!world.isUpdatePendingThisTickForBlock(i, j, k, this.blockID)) {
               world.scheduleBlockUpdate(i, j, k, this.blockID, 2);
            }

            return true;
         }
      } else if (this.hasLavaAbove(world, i, j, k)) {
         if (!world.isUpdatePendingThisTickForBlock(i, j, k, this.blockID)) {
            world.scheduleBlockUpdate(i, j, k, this.blockID, 20);
         }

         return true;
      }

      return false;
   }

   @Environment(EnvType.CLIENT)
   protected abstract Icon getLavaCracksOverlay();

   @Environment(EnvType.CLIENT)
   @Override
   public void renderBlockSecondPass(RenderBlocks renderBlocks, int i, int j, int k, boolean bFirstPassResult) {
      if (bFirstPassResult && this.getHasLavaInCracks(renderBlocks.blockAccess, i, j, k)) {
         RenderUtils.renderBlockFullBrightWithTexture(renderBlocks, renderBlocks.blockAccess, i, j, k, this.getLavaCracksOverlay());
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void renderFallingBlock(RenderBlocks renderBlocks, int i, int j, int k, int iMetadata) {
      renderBlocks.setRenderAllFaces(true);
      renderBlocks.setRenderBounds(this.getFixedBlockBoundsFromPool());
      renderBlocks.renderStandardFallingBlock(this, i, j, k, iMetadata);
      if (this.getHasLavaInCracks(iMetadata)) {
         RenderUtils.renderBlockFullBrightWithTexture(renderBlocks, renderBlocks.blockAccess, i, j, k, this.getLavaCracksOverlay());
      }

      renderBlocks.setRenderAllFaces(false);
   }
}
