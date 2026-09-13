package btw.block.blocks;

import btw.block.BTWBlocks;
import btw.crafting.util.FurnaceBurnTime;
import btw.item.BTWItems;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.BlockDirectional;
import net.minecraft.src.BlockFenceGate;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.MathHelper;
import net.minecraft.src.PathFinder;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.World;

public class FenceGateBlock extends BlockFenceGate {
   public FenceGateBlock(int iBlockID) {
      super(iBlockID);
      this.setBlockMaterial(BTWBlocks.plankMaterial);
      this.c(1.5F);
      this.b(5.0F);
      this.setAxesEffectiveOn();
      this.setBuoyant();
      this.setFurnaceBurnTime(FurnaceBurnTime.WOOD_BASED_BLOCK);
      this.a(g);
      this.c("fenceGate");
   }

   @Override
   public boolean canPlaceBlockAt(World world, int x, int y, int z) {
      return true;
   }

   @Override
   public int getWeightOnPathBlocked(IBlockAccess blockAccess, int i, int j, int k) {
      return -3;
   }

   @Override
   public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9) {
      int var10 = par1World.getBlockMetadata(par2, par3, par4);
      if (k_(var10)) {
         par1World.SetBlockMetadataWithNotify(par2, par3, par4, var10 & -5, 3);
      } else {
         int var11 = (MathHelper.floor_double(par5EntityPlayer.rotationYaw * 4.0F / 360.0F + 0.5) & 3) % 4;
         int var12 = j(var10);
         if (var12 == (var11 + 2) % 4) {
            var10 = var11;
         }

         par1World.SetBlockMetadataWithNotify(par2, par3, par4, var10 | 4, 3);
      }

      par1World.playAuxSFXAtEntity(par5EntityPlayer, 1003, par2, par3, par4, 0);
      return true;
   }

   @Override
   public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, int par5) {
   }

   @Override
   public boolean canPathThroughBlock(IBlockAccess blockAccess, int i, int j, int k, Entity entity, PathFinder pathFinder) {
      return pathFinder.CanPathThroughClosedWoodDoor() || pathFinder.canPathThroughOpenWoodDoor() && this.b(blockAccess, i, j, k);
   }

   @Override
   public boolean isBreakableBarricade(IBlockAccess blockAccess, int i, int j, int k) {
      return true;
   }

   @Override
   public boolean isBreakableBarricadeOpen(IBlockAccess blockAccess, int i, int j, int k) {
      int iMetadata = blockAccess.getBlockMetadata(i, j, k);
      return k_(iMetadata);
   }

   @Override
   public int getHarvestToolLevel(IBlockAccess blockAccess, int i, int j, int k) {
      return 2;
   }

   @Override
   public boolean dropComponentItemsOnBadBreak(World world, int i, int j, int k, int iMetadata, float fChanceOfDrop) {
      this.dropItemsIndividually(world, i, j, k, BTWItems.sawDust.itemID, 2, 0, fChanceOfDrop);
      return true;
   }

   @Override
   public void setBlockBoundsBasedOnState(IBlockAccess blockAccess, int i, int j, int k) {
   }

   @Override
   public AxisAlignedBB getBlockBoundsFromPoolBasedOnState(IBlockAccess blockAccess, int i, int j, int k) {
      int iDirection = j(blockAccess.getBlockMetadata(i, j, k));
      return iDirection != 2 && iDirection != 0
         ? AxisAlignedBB.getAABBPool().getAABB(0.375, 0.0, 0.0, 0.625, 1.0, 1.0)
         : AxisAlignedBB.getAABBPool().getAABB(0.0, 0.0, 0.375, 1.0, 1.0, 0.625);
   }

   @Override
   public boolean shouldWallConnectToThisBlockToFacing(IBlockAccess blockAccess, int x, int y, int z, int facing) {
      int metadata = blockAccess.getBlockMetadata(x, y, z);
      int dir = BlockDirectional.getDirection(metadata);
      if (facing != 2 && facing != 3) {
         return facing != 4 && facing != 5 ? false : dir == 0 || dir == 2;
      } else {
         return dir == 1 || dir == 3;
      }
   }

   @Override
   public boolean shouldFenceConnectToThisBlockToFacing(IBlockAccess blockAccess, int x, int y, int z, int facing) {
      int metadata = blockAccess.getBlockMetadata(x, y, z);
      int dir = BlockDirectional.getDirection(metadata);
      if (facing != 2 && facing != 3) {
         return facing != 4 && facing != 5 ? false : dir == 0 || dir == 2;
      } else {
         return dir == 1 || dir == 3;
      }
   }

   protected boolean isNextToWall(IBlockAccess blockAccess, int x, int y, int z) {
      int meta = blockAccess.getBlockMetadata(x, y, z);
      int dir = BlockDirectional.getDirection(meta);
      boolean isNextToWall = false;
      if (dir == 0 || dir == 2) {
         int otherID = blockAccess.getBlockId(x - 1, y, z);
         int otherMeta = blockAccess.getBlockMetadata(x - 1, y, z);
         Block otherBlock = Block.blocksList[otherID];
         if (otherBlock != null && otherBlock.isWall(otherMeta)) {
            isNextToWall = true;
         }

         otherID = blockAccess.getBlockId(x + 1, y, z);
         otherMeta = blockAccess.getBlockMetadata(x + 1, y, z);
         otherBlock = Block.blocksList[otherID];
         if (otherBlock != null && otherBlock.isWall(otherMeta)) {
            isNextToWall = true;
         }
      } else if (dir == 1 || dir == 3) {
         int otherIDx = blockAccess.getBlockId(x, y, z - 1);
         int otherMetax = blockAccess.getBlockMetadata(x, y, z - 1);
         Block otherBlockx = Block.blocksList[otherIDx];
         if (otherBlockx != null && otherBlockx.isWall(otherMetax)) {
            isNextToWall = true;
         }

         otherIDx = blockAccess.getBlockId(x, y, z + 1);
         otherMetax = blockAccess.getBlockMetadata(x, y, z + 1);
         otherBlockx = Block.blocksList[otherIDx];
         if (otherBlockx != null && otherBlockx.isWall(otherMetax)) {
            isNextToWall = true;
         }
      }

      return isNextToWall;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean shouldSideBeRendered(IBlockAccess blockAccess, int iNeighborI, int iNeighborJ, int iNeighborK, int iSide) {
      return this.currentBlockRenderer.shouldSideBeRenderedBasedOnCurrentBounds(iNeighborI, iNeighborJ, iNeighborK, iSide);
   }

   @Override
   public boolean renderBlock(RenderBlocks render, int x, int y, int z) {
      int var6 = render.blockAccess.getBlockMetadata(x, y, z);
      boolean var7 = k_(var6);
      int var8 = j(var6);
      float var9 = 0.375F;
      float var10 = 0.5625F;
      float var11 = 0.75F;
      float var12 = 0.9375F;
      float var13 = 0.3125F;
      float var14 = 1.0F;
      if (this.isNextToWall(render.blockAccess, x, y, z)) {
         var9 -= 0.1875F;
         var10 -= 0.1875F;
         var11 -= 0.1875F;
         var12 -= 0.1875F;
         var13 -= 0.1875F;
         var14 -= 0.1875F;
      }

      render.setRenderAllFaces(true);
      if (var8 != 3 && var8 != 1) {
         float var15 = 0.0F;
         float var17 = 0.125F;
         float var16 = 0.4375F;
         float var18 = 0.5625F;
         render.setRenderBounds(var15, var13, var16, var17, var14, var18);
         render.renderStandardBlock(this, x, y, z);
         var15 = 0.875F;
         var17 = 1.0F;
         render.setRenderBounds(var15, var13, var16, var17, var14, var18);
         render.renderStandardBlock(this, x, y, z);
      } else {
         render.setUVRotateTop(1);
         float var15 = 0.4375F;
         float var17 = 0.5625F;
         float var16 = 0.0F;
         float var18 = 0.125F;
         render.setRenderBounds(var15, var13, var16, var17, var14, var18);
         render.renderStandardBlock(this, x, y, z);
         var16 = 0.875F;
         var18 = 1.0F;
         render.setRenderBounds(var15, var13, var16, var17, var14, var18);
         render.renderStandardBlock(this, x, y, z);
         render.setUVRotateTop(0);
      }

      if (var7) {
         if (var8 == 2 || var8 == 0) {
            render.setUVRotateTop(1);
         }

         if (var8 == 3) {
            render.setRenderBounds(0.8125, var9, 0.0, 0.9375, var12, 0.125);
            render.renderStandardBlock(this, x, y, z);
            render.setRenderBounds(0.8125, var9, 0.875, 0.9375, var12, 1.0);
            render.renderStandardBlock(this, x, y, z);
            render.setRenderBounds(0.5625, var9, 0.0, 0.8125, var10, 0.125);
            render.renderStandardBlock(this, x, y, z);
            render.setRenderBounds(0.5625, var9, 0.875, 0.8125, var10, 1.0);
            render.renderStandardBlock(this, x, y, z);
            render.setRenderBounds(0.5625, var11, 0.0, 0.8125, var12, 0.125);
            render.renderStandardBlock(this, x, y, z);
            render.setRenderBounds(0.5625, var11, 0.875, 0.8125, var12, 1.0);
            render.renderStandardBlock(this, x, y, z);
         } else if (var8 == 1) {
            render.setRenderBounds(0.0625, var9, 0.0, 0.1875, var12, 0.125);
            render.renderStandardBlock(this, x, y, z);
            render.setRenderBounds(0.0625, var9, 0.875, 0.1875, var12, 1.0);
            render.renderStandardBlock(this, x, y, z);
            render.setRenderBounds(0.1875, var9, 0.0, 0.4375, var10, 0.125);
            render.renderStandardBlock(this, x, y, z);
            render.setRenderBounds(0.1875, var9, 0.875, 0.4375, var10, 1.0);
            render.renderStandardBlock(this, x, y, z);
            render.setRenderBounds(0.1875, var11, 0.0, 0.4375, var12, 0.125);
            render.renderStandardBlock(this, x, y, z);
            render.setRenderBounds(0.1875, var11, 0.875, 0.4375, var12, 1.0);
            render.renderStandardBlock(this, x, y, z);
         } else if (var8 == 0) {
            render.setRenderBounds(0.0, var9, 0.8125, 0.125, var12, 0.9375);
            render.renderStandardBlock(this, x, y, z);
            render.setRenderBounds(0.875, var9, 0.8125, 1.0, var12, 0.9375);
            render.renderStandardBlock(this, x, y, z);
            render.setRenderBounds(0.0, var9, 0.5625, 0.125, var10, 0.8125);
            render.renderStandardBlock(this, x, y, z);
            render.setRenderBounds(0.875, var9, 0.5625, 1.0, var10, 0.8125);
            render.renderStandardBlock(this, x, y, z);
            render.setRenderBounds(0.0, var11, 0.5625, 0.125, var12, 0.8125);
            render.renderStandardBlock(this, x, y, z);
            render.setRenderBounds(0.875, var11, 0.5625, 1.0, var12, 0.8125);
            render.renderStandardBlock(this, x, y, z);
         } else if (var8 == 2) {
            render.setRenderBounds(0.0, var9, 0.0625, 0.125, var12, 0.1875);
            render.renderStandardBlock(this, x, y, z);
            render.setRenderBounds(0.875, var9, 0.0625, 1.0, var12, 0.1875);
            render.renderStandardBlock(this, x, y, z);
            render.setRenderBounds(0.0, var9, 0.1875, 0.125, var10, 0.4375);
            render.renderStandardBlock(this, x, y, z);
            render.setRenderBounds(0.875, var9, 0.1875, 1.0, var10, 0.4375);
            render.renderStandardBlock(this, x, y, z);
            render.setRenderBounds(0.0, var11, 0.1875, 0.125, var12, 0.4375);
            render.renderStandardBlock(this, x, y, z);
            render.setRenderBounds(0.875, var11, 0.1875, 1.0, var12, 0.4375);
            render.renderStandardBlock(this, x, y, z);
         }
      } else if (var8 != 3 && var8 != 1) {
         float var21 = 0.375F;
         float var35 = 0.5F;
         float var31 = 0.4375F;
         float var45 = 0.5625F;
         render.setRenderBounds(var21, var9, var31, var35, var12, var45);
         render.renderStandardBlock(this, x, y, z);
         var21 = 0.5F;
         var35 = 0.625F;
         render.setRenderBounds(var21, var9, var31, var35, var12, var45);
         render.renderStandardBlock(this, x, y, z);
         var21 = 0.625F;
         var35 = 0.875F;
         render.setRenderBounds(var21, var9, var31, var35, var10, var45);
         render.renderStandardBlock(this, x, y, z);
         render.setRenderBounds(var21, var11, var31, var35, var12, var45);
         render.renderStandardBlock(this, x, y, z);
         var21 = 0.125F;
         var35 = 0.375F;
         render.setRenderBounds(var21, var9, var31, var35, var10, var45);
         render.renderStandardBlock(this, x, y, z);
         render.setRenderBounds(var21, var11, var31, var35, var12, var45);
         render.renderStandardBlock(this, x, y, z);
      } else {
         render.setUVRotateTop(1);
         float var20 = 0.4375F;
         float var34 = 0.5625F;
         float var27 = 0.375F;
         float var41 = 0.5F;
         render.setRenderBounds(var20, var9, var27, var34, var12, var41);
         render.renderStandardBlock(this, x, y, z);
         var27 = 0.5F;
         var41 = 0.625F;
         render.setRenderBounds(var20, var9, var27, var34, var12, var41);
         render.renderStandardBlock(this, x, y, z);
         var27 = 0.625F;
         var41 = 0.875F;
         render.setRenderBounds(var20, var9, var27, var34, var10, var41);
         render.renderStandardBlock(this, x, y, z);
         render.setRenderBounds(var20, var11, var27, var34, var12, var41);
         render.renderStandardBlock(this, x, y, z);
         var27 = 0.125F;
         var41 = 0.375F;
         render.setRenderBounds(var20, var9, var27, var34, var10, var41);
         render.renderStandardBlock(this, x, y, z);
         render.setRenderBounds(var20, var11, var27, var34, var12, var41);
         render.renderStandardBlock(this, x, y, z);
      }

      render.setRenderAllFaces(false);
      render.setUVRotateTop(0);
      render.setRenderBounds(0.0, 0.0, 0.0, 1.0, 1.0, 1.0);
      return true;
   }
}
