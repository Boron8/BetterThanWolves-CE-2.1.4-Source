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

public class LegacyCornerBlock extends Block {
   public static final int NUM_SUBTYPES = 16;
   public static final int STONE_TEXTURE_ID = 1;
   public static final int WOOD_TEXTURE_ID = 4;
   private static final double CORNER_WIDTH = 0.5;
   private static final double HALF_CORNER_WIDTH = 0.25;
   private static final double CORNER_WIDTH_OFFSET = 0.5;
   @Environment(EnvType.CLIENT)
   public Icon iconWood;

   public LegacyCornerBlock(int iBlockID) {
      super(iBlockID, Material.wood);
      this.c(1.5F);
      this.setAxesEffectiveOn(true);
      this.setPicksEffectiveOn(true);
      this.setBuoyancy(1.0F);
      this.initBlockBounds(0.25, 0.25, 0.25, 0.75, 0.75, 0.75);
      this.a(g);
      this.c("fcBlockCorner");
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
   public int idDropped(int iMetadata, Random random, int iFortuneModifier) {
      return (iMetadata & 8) == 0 ? BTWItems.woodCornerStubID : BTWBlocks.stoneSidingAndCorner.blockID;
   }

   @Override
   public int damageDropped(int iMetadata) {
      return (iMetadata & 8) != 0 ? 1 : 0;
   }

   @Override
   public AxisAlignedBB getBlockBoundsFromPoolBasedOnState(IBlockAccess blockAccess, int i, int j, int k) {
      double dMinX = 0.0;
      double dMaxX = dMinX + 0.5;
      double dMinY = 0.0;
      double dMaxY = dMinY + 0.5;
      double dMinZ = 0.0;
      double dMaxZ = dMinZ + 0.5;
      if (this.isXOffset(blockAccess, i, j, k)) {
         dMinX += 0.5;
         dMaxX += 0.5;
      }

      if (this.isYOffset(blockAccess, i, j, k)) {
         dMinY += 0.5;
         dMaxY += 0.5;
      }

      if (this.isZOffset(blockAccess, i, j, k)) {
         dMinZ += 0.5;
         dMaxZ += 0.5;
      }

      return AxisAlignedBB.getAABBPool().getAABB(dMinX, dMinY, dMinZ, dMaxX, dMaxY, dMaxZ);
   }

   private boolean isPlayerClickOffsetOnAxis(float fPlayerClick) {
      return fPlayerClick > 0.0F && fPlayerClick >= 0.5F;
   }

   @Override
   public int onBlockPlaced(World world, int i, int j, int k, int iFacing, float fClickX, float fClickY, float fClickZ, int iMetadata) {
      boolean bIOffset = false;
      boolean bJOffset = false;
      boolean bKOffset = false;
      if (iFacing == 0) {
         bJOffset = true;
         bIOffset = this.isPlayerClickOffsetOnAxis(fClickX);
         bKOffset = this.isPlayerClickOffsetOnAxis(fClickZ);
      } else if (iFacing == 1) {
         bIOffset = this.isPlayerClickOffsetOnAxis(fClickX);
         bKOffset = this.isPlayerClickOffsetOnAxis(fClickZ);
      } else if (iFacing == 2) {
         bKOffset = true;
         bIOffset = this.isPlayerClickOffsetOnAxis(fClickX);
         bJOffset = this.isPlayerClickOffsetOnAxis(fClickY);
      } else if (iFacing == 3) {
         bIOffset = this.isPlayerClickOffsetOnAxis(fClickX);
         bJOffset = this.isPlayerClickOffsetOnAxis(fClickY);
      } else if (iFacing == 4) {
         bIOffset = true;
         bJOffset = this.isPlayerClickOffsetOnAxis(fClickY);
         bKOffset = this.isPlayerClickOffsetOnAxis(fClickZ);
      } else if (iFacing == 5) {
         bJOffset = this.isPlayerClickOffsetOnAxis(fClickY);
         bKOffset = this.isPlayerClickOffsetOnAxis(fClickZ);
      }

      return this.setCornerAlignmentInMetadata(iMetadata, bIOffset, bJOffset, bKOffset);
   }

   @Override
   public boolean doesBlockBreakSaw(World world, int i, int j, int k) {
      return this.getIsStone(world, i, j, k);
   }

   @Override
   public boolean canRotateOnTurntable(IBlockAccess blockAccess, int i, int j, int k) {
      return !this.isYOffset(blockAccess, i, j, k);
   }

   @Override
   public int rotateMetadataAroundJAxis(int iMetadata, boolean bReverse) {
      boolean bIOffset = this.isXOffset(iMetadata);
      boolean bJOffset = this.isYOffset(iMetadata);
      boolean bKOffset = this.isZOffset(iMetadata);
      if (bReverse) {
         if (bIOffset) {
            if (bKOffset) {
               bIOffset = false;
            } else {
               bKOffset = true;
            }
         } else if (bKOffset) {
            bKOffset = false;
         } else {
            bIOffset = true;
         }
      } else if (bIOffset) {
         if (bKOffset) {
            bKOffset = false;
         } else {
            bIOffset = false;
         }
      } else if (bKOffset) {
         bIOffset = true;
      } else {
         bKOffset = true;
      }

      return this.setCornerAlignmentInMetadata(iMetadata, bIOffset, bJOffset, bKOffset);
   }

   @Override
   public boolean toggleFacing(World world, int i, int j, int k, boolean bReverse) {
      int iAlignment = this.getCornerAlignment(world, i, j, k);
      if (!bReverse) {
         if (++iAlignment > 7) {
            iAlignment = 0;
         }
      } else if (--iAlignment < 0) {
         iAlignment = 7;
      }

      this.setCornerAlignment(world, i, j, k, iAlignment);
      return true;
   }

   @Override
   public int getFurnaceBurnTime(int iItemDamage) {
      return iItemDamage == 0 ? PlanksBlock.getFurnaceBurnTimeByWoodType(0) / 8 : 0;
   }

   public int getCornerAlignment(IBlockAccess iBlockAccess, int i, int j, int k) {
      return iBlockAccess.getBlockMetadata(i, j, k) & 7;
   }

   public void setCornerAlignment(World world, int i, int j, int k, int iAlignment) {
      int iMetaData = world.getBlockMetadata(i, j, k) & 8;
      iMetaData |= iAlignment;
      world.setBlockMetadataWithNotify(i, j, k, iMetaData);
   }

   public void setCornerAlignment(World world, int i, int j, int k, boolean bIAligned, boolean bJAligned, boolean bKAligned) {
      int iAlignment = 0;
      if (bIAligned) {
         iAlignment |= 4;
      }

      if (bJAligned) {
         iAlignment |= 2;
      }

      if (bKAligned) {
         iAlignment |= 1;
      }

      this.setCornerAlignment(world, i, j, k, iAlignment);
   }

   public int setCornerAlignmentInMetadata(int iMetadata, int iAlignment) {
      iMetadata &= 8;
      return iMetadata | iAlignment;
   }

   public int setCornerAlignmentInMetadata(int iMetadata, boolean bIAligned, boolean bJAligned, boolean bKAligned) {
      int iAlignment = 0;
      if (bIAligned) {
         iAlignment |= 4;
      }

      if (bJAligned) {
         iAlignment |= 2;
      }

      if (bKAligned) {
         iAlignment |= 1;
      }

      return this.setCornerAlignmentInMetadata(iMetadata, iAlignment);
   }

   public boolean isXOffset(IBlockAccess iBlockAccess, int i, int j, int k) {
      return this.isXOffset(iBlockAccess.getBlockMetadata(i, j, k));
   }

   public boolean isXOffset(int iMetadata) {
      return (iMetadata & 4) > 0;
   }

   public boolean isYOffset(IBlockAccess iBlockAccess, int i, int j, int k) {
      return this.isYOffset(iBlockAccess.getBlockMetadata(i, j, k));
   }

   public boolean isYOffset(int iMetadata) {
      return (iMetadata & 2) > 0;
   }

   public boolean isZOffset(IBlockAccess iBlockAccess, int i, int j, int k) {
      return this.isZOffset(iBlockAccess.getBlockMetadata(i, j, k));
   }

   public boolean isZOffset(int iMetadata) {
      return (iMetadata & 1) > 0;
   }

   public boolean getIsStone(IBlockAccess iBlockAccess, int i, int j, int k) {
      return (iBlockAccess.getBlockMetadata(i, j, k) & 8) > 0;
   }

   public void setIsStone(World world, int i, int j, int k, boolean bStone) {
      int iMetaData = world.getBlockMetadata(i, j, k) & 7;
      if (bStone) {
         iMetaData |= 8;
      }

      world.setBlockMetadataWithNotify(i, j, k, iMetaData);
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
      return (iMetadata & 8) > 0 ? this.blockIcon : this.iconWood;
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
