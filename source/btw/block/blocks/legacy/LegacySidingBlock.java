package btw.block.blocks.legacy;

import btw.block.BTWBlocks;
import btw.block.blocks.PlanksBlock;
import btw.item.BTWItems;
import java.util.Random;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Icon;
import net.minecraft.src.IconRegister;
import net.minecraft.src.Material;
import net.minecraft.src.World;

public class LegacySidingBlock extends Block {
   public static final int NUM_SUBTYPES = 2;
   protected static final double SLAB_HEIGHT = 0.5;
   @Environment(EnvType.CLIENT)
   private Icon iconWood;

   public LegacySidingBlock(int iBlockID) {
      super(iBlockID, Material.wood);
      this.c(2.0F);
      this.setAxesEffectiveOn();
      this.setPicksEffectiveOn();
      this.setBuoyancy(1.0F);
      this.initBlockBounds(0.0, 0.0, 0.0, 1.0, 1.0, 0.5);
      this.a(g);
      this.c("fcBlockOmniSlab");
   }

   @Override
   public int idDropped(int iMetadata, Random rand, int iFortuneModifier) {
      return (iMetadata & 1) > 0 ? BTWItems.woodSidingStubID : BTWBlocks.stoneSidingAndCorner.blockID;
   }

   @Override
   public int damageDropped(int iMetadata) {
      return 0;
   }

   @Override
   public AxisAlignedBB getBlockBoundsFromPoolBasedOnState(IBlockAccess blockAccess, int i, int j, int k) {
      int iFacing = this.getFacing(blockAccess, i, j, k);
      switch (iFacing) {
         case 0:
            return AxisAlignedBB.getAABBPool().getAABB(0.0, 0.5, 0.0, 1.0, 1.0, 1.0);
         case 1:
            return AxisAlignedBB.getAABBPool().getAABB(0.0, 0.0, 0.0, 1.0, 0.5, 1.0);
         case 2:
            return AxisAlignedBB.getAABBPool().getAABB(0.0, 0.0, 0.5, 1.0, 1.0, 1.0);
         case 3:
            return AxisAlignedBB.getAABBPool().getAABB(0.0, 0.0, 0.0, 1.0, 1.0, 0.5);
         case 4:
            return AxisAlignedBB.getAABBPool().getAABB(0.5, 0.0, 0.0, 1.0, 1.0, 1.0);
         default:
            return AxisAlignedBB.getAABBPool().getAABB(0.0, 0.0, 0.0, 0.5, 1.0, 1.0);
      }
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
   public int onBlockPlaced(World world, int i, int j, int k, int iFacing, float fClickX, float fClickY, float fClickZ, int iMetadata) {
      return this.setFacing(iMetadata, iFacing);
   }

   @Override
   public boolean hasLargeCenterHardPointToFacing(IBlockAccess blockAccess, int i, int j, int k, int iFacing, boolean bIgnoreTransparency) {
      int iBlockFacing = this.getFacing(blockAccess, i, j, k);
      return iFacing == Block.getOppositeFacing(iBlockFacing);
   }

   @Override
   public int getFacing(int iMetadata) {
      return iMetadata >> 1;
   }

   @Override
   public int setFacing(int iMetadata, int iFacing) {
      iMetadata &= 1;
      return iMetadata | iFacing << 1;
   }

   @Override
   public boolean canRotateOnTurntable(IBlockAccess blockAccess, int i, int j, int k) {
      int iFacing = this.getFacing(blockAccess, i, j, k);
      return iFacing != 0;
   }

   @Override
   public boolean canTransmitRotationVerticallyOnTurntable(IBlockAccess blockAccess, int i, int j, int k) {
      int iFacing = this.getFacing(blockAccess, i, j, k);
      return iFacing > 1;
   }

   @Override
   public boolean toggleFacing(World world, int i, int j, int k, boolean bReverse) {
      int iFacing = this.getFacing(world, i, j, k);
      iFacing = Block.cycleFacing(iFacing, bReverse);
      this.setFacing(world, i, j, k, iFacing);
      world.markBlockRangeForRenderUpdate(i, j, k, i, j, k);
      return true;
   }

   @Override
   public boolean doesBlockBreakSaw(World world, int i, int j, int k) {
      return !this.isSlabWood(world, i, j, k);
   }

   @Override
   public int getFurnaceBurnTime(int iItemDamage) {
      return (iItemDamage & 1) > 0 ? PlanksBlock.getFurnaceBurnTimeByWoodType(0) / 2 : 0;
   }

   public boolean isSlabWood(IBlockAccess iBlockAccess, int i, int j, int k) {
      return (iBlockAccess.getBlockMetadata(i, j, k) & 1) > 0;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void registerIcons(IconRegister register) {
      this.blockIcon = register.registerIcon("stone");
      this.iconWood = register.registerIcon("wood");
   }

   @Environment(EnvType.CLIENT)
   @Override
   public Icon getIcon(int iSide, int iMetadata) {
      return (iMetadata & 1) > 0 ? this.iconWood : this.blockIcon;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public int idPicked(World world, int i, int j, int k) {
      return this.idDropped(world.getBlockMetadata(i, j, k), world.rand, 0);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean shouldSideBeRendered(IBlockAccess blockAccess, int iNeighborI, int iNeighborJ, int iNeighborK, int iSide) {
      return this.currentBlockRenderer.shouldSideBeRenderedBasedOnCurrentBounds(iNeighborI, iNeighborJ, iNeighborK, iSide);
   }
}
