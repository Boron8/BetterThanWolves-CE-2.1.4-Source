package btw.block.blocks;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.BlockDirectional;
import net.minecraft.src.BlockRedstoneRepeater;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.World;

public class RedstoneRepeaterBlock extends BlockRedstoneRepeater {
   public RedstoneRepeaterBlock(int iBlockID, boolean bIsLit) {
      super(iBlockID, bIsLit);
      this.c(0.0F);
      this.a(g);
      this.c("diode");
      this.D();
   }

   @Override
   public boolean canRotateOnTurntable(IBlockAccess blockAccess, int i, int j, int k) {
      return true;
   }

   @Override
   public boolean rotateAroundJAxis(World world, int i, int j, int k, boolean bReverse) {
      int iOldMetadata = world.getBlockMetadata(i, j, k);
      int iNewMetadata = this.rotateMetadataAroundJAxis(iOldMetadata, bReverse);
      if (iNewMetadata != iOldMetadata) {
         world.setBlockMetadataWithNotify(i, j, k, iNewMetadata);
         this.a(world, i, j, k, 0);
         int iDirection = BlockDirectional.getDirection(iOldMetadata);
         if (iDirection == 1) {
            world.notifyBlockOfNeighborChange(i + 1, j, k, this.blockID);
            world.notifyBlocksOfNeighborChange(i + 1, j, k, this.blockID, 4);
         } else if (iDirection == 3) {
            world.notifyBlockOfNeighborChange(i - 1, j, k, this.blockID);
            world.notifyBlocksOfNeighborChange(i - 1, j, k, this.blockID, 5);
         } else if (iDirection == 2) {
            world.notifyBlockOfNeighborChange(i, j, k + 1, this.blockID);
            world.notifyBlocksOfNeighborChange(i, j, k + 1, this.blockID, 2);
         } else if (iDirection == 0) {
            world.notifyBlockOfNeighborChange(i, j, k - 1, this.blockID);
            world.notifyBlocksOfNeighborChange(i, j, k - 1, this.blockID, 3);
         }

         return true;
      } else {
         return false;
      }
   }

   @Override
   public int rotateMetadataAroundJAxis(int iMetadata, boolean bReverse) {
      int iDirection = iMetadata & 3;
      if (bReverse) {
         if (++iDirection > 3) {
            iDirection = 0;
         }
      } else if (--iDirection < 0) {
         iDirection = 3;
      }

      return iMetadata & -4 | iDirection;
   }

   @Override
   public void onRemovedByBlockDispenser(World world, int i, int j, int k) {
      int iMetadata = world.getBlockMetadata(i, j, k);
      super.onRemovedByBlockDispenser(world, i, j, k);
      if (this.isRepeaterPowered) {
         this.g(world, i, j, k, iMetadata);
      }
   }

   @Override
   public int onBlockPlaced(World world, int i, int j, int k, int iFacing, float fClickX, float fClickY, float fClickZ, int iMetadata) {
      byte var10;
      if (iFacing == 4) {
         var10 = 1;
      } else if (iFacing == 2) {
         var10 = 2;
      } else if (iFacing == 5) {
         var10 = 3;
      } else {
         var10 = 0;
      }

      return var10;
   }

   @Override
   public void onPostBlockPlaced(World world, int i, int j, int k, int iMetadata) {
      if (this.d(world, i, j, k, iMetadata)) {
         world.scheduleBlockUpdate(i, j, k, this.blockID, 1);
      }
   }

   @Override
   public boolean triggersBuddy() {
      return false;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean renderBlock(RenderBlocks renderer, int i, int j, int k) {
      renderer.setRenderBounds(this.getBlockBoundsFromPoolBasedOnState(renderer.blockAccess, i, j, k));
      return renderer.renderBlockRepeater(this, i, j, k);
   }
}
