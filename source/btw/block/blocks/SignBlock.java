package btw.block.blocks;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.BlockSign;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.TileEntitySign;
import net.minecraft.src.World;

public class SignBlock extends BlockSign {
   protected final boolean freeStanding;

   public SignBlock(int iBlockID, boolean bFreeStanding) {
      super(iBlockID, TileEntitySign.class, bFreeStanding);
      this.freeStanding = bFreeStanding;
      this.c(1.0F);
      this.setBuoyant();
      this.initBlockBounds(0.25, 0.0, 0.25, 0.75, 1.0, 0.75);
      this.a(g);
      this.c("sign");
      this.D();
   }

   @Override
   public boolean doesBlockHopperEject(World world, int i, int j, int k) {
      return false;
   }

   @Override
   public boolean getPreventsFluidFlow(World world, int i, int j, int k, Block fluidBlock) {
      return true;
   }

   @Override
   public void setBlockBoundsBasedOnState(IBlockAccess blockAccess, int i, int j, int k) {
   }

   @Override
   public AxisAlignedBB getBlockBoundsFromPoolBasedOnState(IBlockAccess blockAccess, int i, int j, int k) {
      if (!this.freeStanding) {
         int var5 = blockAccess.getBlockMetadata(i, j, k);
         float var6 = 0.28125F;
         float var7 = 0.78125F;
         float var8 = 0.0F;
         float var9 = 1.0F;
         float var10 = 0.125F;
         if (var5 == 2) {
            return AxisAlignedBB.getAABBPool().getAABB(var8, var6, 1.0F - var10, var9, var7, 1.0);
         } else if (var5 == 3) {
            return AxisAlignedBB.getAABBPool().getAABB(var8, var6, 0.0, var9, var7, var10);
         } else if (var5 == 4) {
            return AxisAlignedBB.getAABBPool().getAABB(1.0F - var10, var6, var8, 1.0, var7, var9);
         } else {
            return var5 == 5
               ? AxisAlignedBB.getAABBPool().getAABB(0.0, var6, var8, var10, var7, var9)
               : AxisAlignedBB.getAABBPool().getAABB(0.0, 0.0, 0.0, 1.0, 1.0, 1.0);
         }
      } else {
         return super.getBlockBoundsFromPoolBasedOnState(blockAccess, i, j, k);
      }
   }

   public boolean isFreeStanding() {
      return this.freeStanding;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean renderBlock(RenderBlocks renderBlocks, int i, int j, int k) {
      return false;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int i, int j, int k) {
      return this.getBlockBoundsFromPoolBasedOnState(world, i, j, k).offset(i, j, k);
   }

   public String getSignTexture() {
      return "/item/sign.png";
   }
}
