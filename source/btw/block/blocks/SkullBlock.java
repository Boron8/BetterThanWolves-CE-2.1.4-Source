package btw.block.blocks;

import btw.block.BTWBlocks;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.BlockSkull;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.ItemStack;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.TileEntity;
import net.minecraft.src.TileEntitySkull;
import net.minecraft.src.World;

public class SkullBlock extends BlockSkull {
   public SkullBlock(int iBlockID) {
      super(iBlockID);
      this.setAxesEffectiveOn(true);
      this.c(1.0F);
      this.initBlockBounds(0.25, 0.0, 0.25, 0.75, 0.5, 0.75);
      this.a(j);
      this.c("skull");
   }

   @Override
   public void setBlockBoundsBasedOnState(IBlockAccess blockAccess, int i, int j, int k) {
   }

   @Override
   public AxisAlignedBB getBlockBoundsFromPoolBasedOnState(IBlockAccess blockAccess, int i, int j, int k) {
      int iFacing = blockAccess.getBlockMetadata(i, j, k) & 7;
      switch (iFacing) {
         case 2:
            return AxisAlignedBB.getAABBPool().getAABB(0.25, 0.25, 0.5, 0.75, 0.75, 1.0);
         case 3:
            return AxisAlignedBB.getAABBPool().getAABB(0.25, 0.25, 0.0, 0.75, 0.75, 0.5);
         case 4:
            return AxisAlignedBB.getAABBPool().getAABB(0.5, 0.25, 0.25, 1.0, 0.75, 0.75);
         case 5:
            return AxisAlignedBB.getAABBPool().getAABB(0.0, 0.25, 0.25, 0.5, 0.75, 0.75);
         default:
            return AxisAlignedBB.getAABBPool().getAABB(0.25, 0.0, 0.25, 0.75, 0.5, 0.75);
      }
   }

   @Override
   public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k) {
      return this.getBlockBoundsFromPoolBasedOnState(world, i, j, k).offset(i, j, k);
   }

   @Override
   public void makeWither(World world, int i, int j, int k, TileEntitySkull tileEntity) {
   }

   public boolean isBlockSummoningBase(World world, int i, int j, int k) {
      if (world.getBlockId(i, j, k) == BTWBlocks.aestheticOpaque.blockID) {
         int iSubtype = world.getBlockMetadata(i, j, k);
         if (iSubtype == 15) {
            return true;
         }
      }

      return false;
   }

   @Override
   public boolean isBlockRestingOnThatBelow(IBlockAccess blockAccess, int i, int j, int k) {
      int iMetadata = blockAccess.getBlockMetadata(i, j, k);
      return iMetadata == 1;
   }

   @Override
   public boolean canRotateOnTurntable(IBlockAccess blockAccess, int i, int j, int k) {
      return true;
   }

   @Override
   public boolean rotateAroundJAxis(World world, int i, int j, int k, boolean bReverse) {
      TileEntity tileEnt = world.getBlockTileEntity(i, j, k);
      if (tileEnt != null && tileEnt instanceof TileEntitySkull) {
         TileEntitySkull skullEnt = (TileEntitySkull)tileEnt;
         int iSkullFacing = skullEnt.getSkullRotationServerSafe();
         if (bReverse) {
            iSkullFacing += 4;
            if (iSkullFacing > 15) {
               iSkullFacing -= 16;
            }
         } else {
            iSkullFacing -= 4;
            if (iSkullFacing < 0) {
               iSkullFacing += 16;
            }
         }

         skullEnt.setSkullRotation(iSkullFacing);
         world.markBlockForUpdate(i, j, k);
         return true;
      } else {
         return false;
      }
   }

   @Override
   public ItemStack getStackRetrievedByBlockDispenser(World world, int i, int j, int k) {
      return null;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean renderBlock(RenderBlocks renderBlocks, int i, int j, int k) {
      return false;
   }
}
