package btw.block.blocks;

import btw.block.BTWBlocks;
import btw.client.render.util.RenderUtils;
import btw.item.BTWItems;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Icon;
import net.minecraft.src.IconRegister;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.StepSound;
import net.minecraft.src.World;

public class SidingAndCornerBlock extends Block {
   protected static final double SIDING_HEIGHT = 0.5;
   protected static final double CORNER_WIDTH = 0.5;
   protected static final double CORNER_WIDTH_OFFSET = 0.5;
   String textureName;

   protected SidingAndCornerBlock(int iBlockID, Material material, String sTextureName, float fHardness, float fResistance, StepSound stepSound, String name) {
      super(iBlockID, material);
      this.c(fHardness);
      this.b(fResistance);
      this.a(stepSound);
      this.c(name);
      this.textureName = sTextureName;
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
   public int damageDropped(int iMetadata) {
      return iMetadata & 1;
   }

   @Override
   public AxisAlignedBB getBlockBoundsFromPoolBasedOnState(IBlockAccess blockAccess, int i, int j, int k) {
      int iFacing = this.getFacing(blockAccess, i, j, k);
      if (!this.getIsCorner(blockAccess, i, j, k)) {
         AxisAlignedBB sidingBox = AxisAlignedBB.getAABBPool().getAABB(0.0, 0.0, 0.0, 1.0, 0.5, 1.0);
         sidingBox.tiltToFacingAlongY(iFacing);
         return sidingBox;
      } else {
         AxisAlignedBB cornerBox = AxisAlignedBB.getAABBPool().getAABB(0.0, 0.0, 0.0, 0.5, 0.5, 0.5);
         if (this.isCornerFacingXOffset(iFacing)) {
            cornerBox.minX += 0.5;
            cornerBox.maxX += 0.5;
         }

         if (this.isCornerFacingYOffset(iFacing)) {
            cornerBox.minY += 0.5;
            cornerBox.maxY += 0.5;
         }

         if (this.isCornerFacingZOffset(iFacing)) {
            cornerBox.minZ += 0.5;
            cornerBox.maxZ += 0.5;
         }

         return cornerBox;
      }
   }

   @Override
   public int onBlockPlaced(World world, int i, int j, int k, int iFacing, float fClickX, float fClickY, float fClickZ, int iMetadata) {
      if (!this.getIsCorner(iMetadata)) {
         return this.setFacing(iMetadata, iFacing);
      } else {
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

         return this.setCornerFacingInMetadata(iMetadata, bIOffset, bJOffset, bKOffset);
      }
   }

   @Override
   public boolean hasLargeCenterHardPointToFacing(IBlockAccess blockAccess, int i, int j, int k, int iFacing, boolean bIgnoreTransparency) {
      if (!this.getIsCorner(blockAccess, i, j, k)) {
         int iBlockFacing = this.getFacing(blockAccess, i, j, k);
         return iFacing == Block.getOppositeFacing(iBlockFacing);
      } else {
         return false;
      }
   }

   @Override
   public boolean canGroundCoverRestOnBlock(World world, int i, int j, int k) {
      if (super.canGroundCoverRestOnBlock(world, i, j, k)) {
         return true;
      } else {
         if (!this.getIsCorner(world, i, j, k)) {
            int iFacing = this.getFacing(world, i, j, k);
            if (iFacing == 1) {
               return true;
            }
         }

         return false;
      }
   }

   @Override
   public float groundCoverRestingOnVisualOffset(IBlockAccess blockAccess, int i, int j, int k) {
      if (!this.getIsCorner(blockAccess, i, j, k)) {
         int iFacing = this.getFacing(blockAccess, i, j, k);
         if (iFacing == 1) {
            return -0.5F;
         }
      }

      return 0.0F;
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
      return !this.getIsCorner(blockAccess, i, j, k) ? iFacing != 0 : !this.isCornerFacingYOffset(iFacing);
   }

   @Override
   public boolean canTransmitRotationVerticallyOnTurntable(IBlockAccess blockAccess, int i, int j, int k) {
      if (!this.getIsCorner(blockAccess, i, j, k)) {
         int iFacing = this.getFacing(blockAccess, i, j, k);
         if (iFacing > 1) {
            return true;
         }
      }

      return false;
   }

   @Override
   public int rotateMetadataAroundJAxis(int iMetadata, boolean bReverse) {
      int iFacing = this.getFacing(iMetadata);
      if ((iMetadata & 1) == 0) {
         return super.rotateMetadataAroundJAxis(iMetadata, bReverse);
      } else {
         boolean bIOffset = this.isCornerFacingXOffset(iFacing);
         boolean bJOffset = this.isCornerFacingYOffset(iFacing);
         boolean bKOffset = this.isCornerFacingZOffset(iFacing);
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

         return this.setCornerFacingInMetadata(iMetadata, bIOffset, bJOffset, bKOffset);
      }
   }

   @Override
   public boolean toggleFacing(World world, int i, int j, int k, boolean bReverse) {
      int iFacing = this.getFacing(world, i, j, k);
      if (!this.getIsCorner(world, i, j, k)) {
         iFacing = Block.cycleFacing(iFacing, bReverse);
      } else if (!bReverse) {
         if (++iFacing > 7) {
            iFacing = 0;
         }
      } else if (--iFacing < 0) {
         iFacing = 7;
      }

      this.setFacing(world, i, j, k, iFacing);
      return true;
   }

   @Override
   public float mobSpawnOnVerticalOffset(World world, int i, int j, int k) {
      int iFacing = this.getFacing(world, i, j, k);
      if (this.getIsCorner(world, i, j, k)) {
         if (!this.isCornerFacingYOffset(iFacing)) {
            return -0.5F;
         }
      } else if (iFacing == 1) {
         return -0.5F;
      }

      return 0.0F;
   }

   public boolean getIsCorner(IBlockAccess iBlockAccess, int i, int j, int k) {
      return this.getIsCorner(iBlockAccess.getBlockMetadata(i, j, k));
   }

   public boolean getIsCorner(int iMetadata) {
      return (iMetadata & 1) > 0;
   }

   public boolean isCornerFacingXOffset(int iFacing) {
      return (iFacing & 4) > 0;
   }

   public boolean isCornerFacingYOffset(int iFacing) {
      return (iFacing & 2) > 0;
   }

   public boolean isCornerFacingZOffset(int iFacing) {
      return (iFacing & 1) > 0;
   }

   private boolean isPlayerClickOffsetOnAxis(float fPlayerClick) {
      return fPlayerClick > 0.0F && fPlayerClick >= 0.5F;
   }

   public void setCornerFacing(World world, int i, int j, int k, boolean bIAligned, boolean bJAligned, boolean bKAligned) {
      int iFacing = 0;
      if (bIAligned) {
         iFacing |= 4;
      }

      if (bJAligned) {
         iFacing |= 2;
      }

      if (bKAligned) {
         iFacing |= 1;
      }

      this.setFacing(world, i, j, k, iFacing);
   }

   public int setCornerFacingInMetadata(int iMetadata, boolean bIAligned, boolean bJAligned, boolean bKAligned) {
      int iFacing = 0;
      if (bIAligned) {
         iFacing |= 4;
      }

      if (bJAligned) {
         iFacing |= 2;
      }

      if (bKAligned) {
         iFacing |= 1;
      }

      return this.setFacing(iMetadata, iFacing);
   }

   public static int getCornerAlignmentOffsetAlongAxis(int iCornerFacing, int iAxis) {
      return (iCornerFacing & 4 >> iAxis) > 0 ? 1 : -1;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void registerIcons(IconRegister register) {
      this.blockIcon = register.registerIcon(this.textureName);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void getSubBlocks(int iBlockID, CreativeTabs creativeTabs, List list) {
      list.add(new ItemStack(iBlockID, 1, 0));
      list.add(new ItemStack(iBlockID, 1, 1));
   }

   @Environment(EnvType.CLIENT)
   @Override
   public int idPicked(World world, int i, int j, int k) {
      return this.a(world.getBlockMetadata(i, j, k), world.rand, 0);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean shouldSideBeRendered(IBlockAccess blockAccess, int iNeighborI, int iNeighborJ, int iNeighborK, int iSide) {
      return this.currentBlockRenderer.shouldSideBeRenderedBasedOnCurrentBounds(iNeighborI, iNeighborJ, iNeighborK, iSide);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void renderBlockAsItem(RenderBlocks renderBlocks, int iItemDamage, float fBrightness) {
      if (this.blockID != BTWItems.woodSidingStubID && ((iItemDamage & 1) != 0 || this.blockID == BTWItems.woodCornerStubID)) {
         renderBlocks.setRenderBounds(0.25, 0.25, 0.25, 0.75, 0.75, 0.75);
      } else {
         renderBlocks.setRenderBounds(0.0, 0.0, 0.0, 0.5, 1.0, 1.0);
      }

      if (this.blockID != BTWItems.woodSidingStubID && this.blockID != BTWItems.woodCornerStubID) {
         RenderUtils.renderInvBlockWithMetadata(renderBlocks, this, -0.5F, -0.5F, -0.5F, 0);
      } else {
         Icon woodTexture;
         switch (iItemDamage) {
            case 1:
               woodTexture = BTWBlocks.spruceWoodSidingAndCorner.blockIcon;
               break;
            case 2:
               woodTexture = BTWBlocks.birchWoodSidingAndCorner.blockIcon;
               break;
            case 3:
               woodTexture = BTWBlocks.jungleWoodSidingAndCorner.blockIcon;
               break;
            case 4:
               woodTexture = BTWBlocks.bloodWoodSidingAndCorner.blockIcon;
               break;
            default:
               woodTexture = BTWBlocks.oakWoodSidingAndCorner.blockIcon;
         }

         RenderUtils.renderInvBlockWithTexture(renderBlocks, this, -0.5F, -0.5F, -0.5F, woodTexture);
      }
   }
}
