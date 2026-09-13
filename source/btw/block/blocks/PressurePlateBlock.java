package btw.block.blocks;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.BlockPressurePlate;
import net.minecraft.src.EnumMobType;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Material;
import net.minecraft.src.World;

public class PressurePlateBlock extends BlockPressurePlate {
   protected static final double HORIZONTAL_BORDER = 0.0625;
   protected static final double HEIGHT_DEPRESSED = 0.03125;
   protected static final double HEIGHT_RESTING = 0.0625;
   protected static final double HEIGHT_ITEM = 0.25;
   protected static final double HALF_HEIGHT_ITEM = 0.125;

   public PressurePlateBlock(int iBlockID, String iconName, Material material, EnumMobType mobType) {
      super(iBlockID, iconName, material, mobType);
      this.initBlockBounds(0.0625, 0.0, 0.0625, 0.9375, 0.03125, 0.9375);
   }

   @Override
   public boolean canPlaceBlockAt(World world, int i, int j, int k) {
      return world.doesBlockHaveSolidTopSurface(i, j - 1, k);
   }

   @Override
   public void setBlockBoundsBasedOnState(IBlockAccess blockAccess, int i, int j, int k) {
   }

   @Override
   protected void func_94353_c_(int par1) {
   }

   @Override
   public void setBlockBoundsForItemRender() {
   }

   @Override
   public AxisAlignedBB getBlockBoundsFromPoolBasedOnState(IBlockAccess blockAccess, int i, int j, int k) {
      boolean bDepressed = this.c(blockAccess.getBlockMetadata(i, j, k)) > 0;
      return bDepressed
         ? AxisAlignedBB.getAABBPool().getAABB(0.0625, 0.0, 0.0625, 0.9375, 0.03125, 0.9375)
         : AxisAlignedBB.getAABBPool().getAABB(0.0625, 0.0, 0.0625, 0.9375, 0.0625, 0.9375);
   }

   @Override
   public void onNeighborBlockChange(World world, int i, int j, int k, int iNeighborBlockID) {
      boolean bOnInvalidSurface = false;
      if (!world.doesBlockHaveSolidTopSurface(i, j - 1, k)) {
         bOnInvalidSurface = true;
      }

      if (bOnInvalidSurface && world.getBlockId(i, j, k) == this.blockID) {
         this.c(world, i, j, k, world.getBlockMetadata(i, j, k), 0);
         world.setBlockToAir(i, j, k);
      }
   }

   private boolean isModFence(World world, int i, int j, int k) {
      int iBlockID = world.getBlockId(i, j, k);
      Block block = Block.blocksList[iBlockID];
      if (block != null && block instanceof SidingAndCornerAndDecorativeBlock) {
         int iSubtype = world.getBlockMetadata(i, j, k);
         return iSubtype == 14;
      } else {
         return false;
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public AxisAlignedBB getBlockBoundsFromPoolForItemRender(int iItemDamage) {
      return AxisAlignedBB.getAABBPool().getAABB(0.0, 0.375, 0.0, 1.0, 0.625, 1.0);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean shouldSideBeRendered(IBlockAccess blockAccess, int i, int j, int k, int iSide) {
      return iSide == 0 ? !blockAccess.isBlockOpaqueCube(i, j, k) : true;
   }
}
