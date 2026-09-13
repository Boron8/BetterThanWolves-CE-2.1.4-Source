package btw.block.blocks;

import btw.block.BTWBlocks;
import btw.world.util.BlockPos;
import btw.world.util.WorldUtils;
import java.util.Random;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Icon;
import net.minecraft.src.IconRegister;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.MathHelper;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.Tessellator;
import net.minecraft.src.World;

public class LadderBlockBase extends Block {
   protected static final float LADDER_THICKNESS = 0.1875F;
   protected static final AxisAlignedBB boxCollision = new AxisAlignedBB(0.0, 0.0, 0.8125, 1.0, 1.0, 1.0);
   @Environment(EnvType.CLIENT)
   protected static final double LADDER_HORIZONTAL_OFFSET = 0.05F;
   @Environment(EnvType.CLIENT)
   private Icon filterIcon;

   protected LadderBlockBase(int iBlockID) {
      super(iBlockID, Material.circuits);
      this.c(0.4F);
      this.setAxesEffectiveOn(true);
      this.setBuoyant();
      this.a(p);
   }

   @Override
   public int idDropped(int iMetadata, Random rand, int iFortuneModifier) {
      return BTWBlocks.ladder.blockID;
   }

   @Override
   public AxisAlignedBB getBlockBoundsFromPoolBasedOnState(IBlockAccess blockAccess, int i, int j, int k) {
      AxisAlignedBB transformedBox = boxCollision.makeTemporaryCopy();
      transformedBox.rotateAroundYToFacing(this.getFacing(blockAccess, i, j, k));
      return transformedBox;
   }

   @Override
   public boolean isOpaqueCube() {
      return false;
   }

   @Override
   public boolean renderAsNormalBlock() {
      return false;
   }

   @Override
   public boolean canPlaceBlockAt(World world, int i, int j, int k) {
      for (int iTempFacing = 2; iTempFacing <= 5; iTempFacing++) {
         if (this.canAttachToFacing(world, i, j, k, Block.getOppositeFacing(iTempFacing))) {
            return true;
         }
      }

      return false;
   }

   @Override
   public int onBlockPlaced(World world, int i, int j, int k, int iFacing, float fClickX, float fClickY, float fClickZ, int iMetadata) {
      if (this.canAttachToFacing(world, i, j, k, Block.getOppositeFacing(iFacing))) {
         iMetadata = this.setFacing(iMetadata, iFacing);
      } else {
         for (int iTempFacing = 2; iTempFacing <= 5; iTempFacing++) {
            if (this.canAttachToFacing(world, i, j, k, iTempFacing)) {
               iMetadata = this.setFacing(iMetadata, Block.getOppositeFacing(iTempFacing));
               break;
            }
         }
      }

      return iMetadata;
   }

   @Override
   public void onNeighborBlockChange(World world, int i, int j, int k, int iChangedBlockID) {
      int iMetadata = world.getBlockMetadata(i, j, k);
      if (!this.canAttachToFacing(world, i, j, k, Block.getOppositeFacing(this.getFacing(iMetadata)))) {
         this.c(world, i, j, k, iMetadata, 0);
         world.setBlockToAir(i, j, k);
      }

      super.onNeighborBlockChange(world, i, j, k, iChangedBlockID);
   }

   @Override
   public int getRenderType() {
      return 8;
   }

   @Override
   public boolean isBlockClimbable(World world, int i, int j, int k) {
      return true;
   }

   @Override
   public int getFacing(int iMetadata) {
      return (iMetadata & 3) + 2;
   }

   @Override
   public int setFacing(int iMetadata, int iFacing) {
      int iFlatFacing = MathHelper.clamp_int(iFacing, 2, 5) - 2;
      iMetadata &= -4;
      return iMetadata | iFlatFacing;
   }

   @Override
   public boolean canRotateAroundBlockOnTurntableToFacing(World world, int i, int j, int k, int iFacing) {
      return iFacing == Block.getOppositeFacing(this.getFacing(world, i, j, k));
   }

   @Override
   public int getNewMetadataRotatedAroundBlockOnTurntableToFacing(World world, int i, int j, int k, int iInitialFacing, int iRotatedFacing) {
      int iOldMetadata = world.getBlockMetadata(i, j, k);
      return this.setFacing(iOldMetadata, Block.getOppositeFacing(iRotatedFacing));
   }

   @Override
   public boolean canItemPassIfFilter(ItemStack filteredItem) {
      int iFilterableProperties = filteredItem.getItem().getFilterableProperties(filteredItem);
      return (iFilterableProperties & 1) == 0;
   }

   @Override
   public boolean canMobsSpawnOn(World world, int i, int j, int k) {
      return false;
   }

   protected boolean canAttachToFacing(World world, int i, int j, int k, int iFacing) {
      if (iFacing >= 2) {
         BlockPos targetPos = new BlockPos(i, j, k, iFacing);
         return WorldUtils.doesBlockHaveLargeCenterHardpointToFacing(world, targetPos.x, targetPos.y, targetPos.z, Block.getOppositeFacing(iFacing));
      } else {
         return false;
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void registerIcons(IconRegister register) {
      this.blockIcon = register.registerIcon("ladder");
      this.filterIcon = register.registerIcon("fcBlockHopper_ladder");
   }

   @Environment(EnvType.CLIENT)
   @Override
   public Icon getHopperFilterIcon() {
      return this.filterIcon;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean renderBlock(RenderBlocks renderer, int x, int y, int z) {
      renderer.setRenderBounds(this.getBlockBoundsFromPoolBasedOnState(renderer.blockAccess, x, y, z));
      return this.renderLadder(renderer, x, y, z);
   }

   @Environment(EnvType.CLIENT)
   public boolean renderLadder(RenderBlocks renderBlocks, int x, int y, int z) {
      IBlockAccess blockAccess = renderBlocks.blockAccess;
      int facing = this.getFacing(blockAccess, x, y, z);
      Tessellator tessellator = Tessellator.instance;
      tessellator.setBrightness(this.e(blockAccess, x, y, z));
      float brightness = 1.0F;
      tessellator.setColorOpaque_F(brightness, brightness, brightness);
      Icon icon = this.blockIcon;
      if (renderBlocks.hasOverrideBlockTexture()) {
         icon = renderBlocks.getOverrideTexture();
      }

      double minU = icon.getMinU();
      double minV = icon.getMinV();
      double maxU = icon.getMaxU();
      double maxV = icon.getMaxV();
      if (facing == 5) {
         tessellator.addVertexWithUV(x + 0.05F, y + 1, z + 1, minU, minV);
         tessellator.addVertexWithUV(x + 0.05F, y + 0, z + 1, minU, maxV);
         tessellator.addVertexWithUV(x + 0.05F, y + 0, z + 0, maxU, maxV);
         tessellator.addVertexWithUV(x + 0.05F, y + 1, z + 0, maxU, minV);
         tessellator.addVertexWithUV(x + 0.05F, y + 1, z + 0, maxU, minV);
         tessellator.addVertexWithUV(x + 0.05F, y + 0, z + 0, maxU, maxV);
         tessellator.addVertexWithUV(x + 0.05F, y + 0, z + 1, minU, maxV);
         tessellator.addVertexWithUV(x + 0.05F, y + 1, z + 1, minU, minV);
      } else if (facing == 4) {
         tessellator.addVertexWithUV(x + 1 - 0.05F, y + 0, z + 1, maxU, maxV);
         tessellator.addVertexWithUV(x + 1 - 0.05F, y + 1, z + 1, maxU, minV);
         tessellator.addVertexWithUV(x + 1 - 0.05F, y + 1, z + 0, minU, minV);
         tessellator.addVertexWithUV(x + 1 - 0.05F, y + 0, z + 0, minU, maxV);
         tessellator.addVertexWithUV(x + 1 - 0.05F, y + 0, z + 0, minU, maxV);
         tessellator.addVertexWithUV(x + 1 - 0.05F, y + 1, z + 0, minU, minV);
         tessellator.addVertexWithUV(x + 1 - 0.05F, y + 1, z + 1, maxU, minV);
         tessellator.addVertexWithUV(x + 1 - 0.05F, y + 0, z + 1, maxU, maxV);
      } else if (facing == 3) {
         tessellator.addVertexWithUV(x + 1, y + 0, z + 0.05F, maxU, maxV);
         tessellator.addVertexWithUV(x + 1, y + 1, z + 0.05F, maxU, minV);
         tessellator.addVertexWithUV(x + 0, y + 1, z + 0.05F, minU, minV);
         tessellator.addVertexWithUV(x + 0, y + 0, z + 0.05F, minU, maxV);
         tessellator.addVertexWithUV(x + 0, y + 0, z + 0.05F, minU, maxV);
         tessellator.addVertexWithUV(x + 0, y + 1, z + 0.05F, minU, minV);
         tessellator.addVertexWithUV(x + 1, y + 1, z + 0.05F, maxU, minV);
         tessellator.addVertexWithUV(x + 1, y + 0, z + 0.05F, maxU, maxV);
      } else if (facing == 2) {
         tessellator.addVertexWithUV(x + 1, y + 1, z + 1 - 0.05F, minU, minV);
         tessellator.addVertexWithUV(x + 1, y + 0, z + 1 - 0.05F, minU, maxV);
         tessellator.addVertexWithUV(x + 0, y + 0, z + 1 - 0.05F, maxU, maxV);
         tessellator.addVertexWithUV(x + 0, y + 1, z + 1 - 0.05F, maxU, minV);
         tessellator.addVertexWithUV(x + 0, y + 1, z + 1 - 0.05F, maxU, minV);
         tessellator.addVertexWithUV(x + 0, y + 0, z + 1 - 0.05F, maxU, maxV);
         tessellator.addVertexWithUV(x + 1, y + 0, z + 1 - 0.05F, minU, maxV);
         tessellator.addVertexWithUV(x + 1, y + 1, z + 1 - 0.05F, minU, minV);
      }

      return true;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean shouldSideBeRendered(IBlockAccess blockAccess, int iNeighborI, int iNeighborJ, int iNeighborK, int iSide) {
      return this.currentBlockRenderer.shouldSideBeRenderedBasedOnCurrentBounds(iNeighborI, iNeighborJ, iNeighborK, iSide);
   }
}
