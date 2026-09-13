package btw.block.blocks;

import btw.block.util.RayTraceUtils;
import btw.world.util.BlockPos;
import btw.world.util.WorldUtils;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Material;
import net.minecraft.src.MathHelper;
import net.minecraft.src.MovingObjectPosition;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.Vec3;
import net.minecraft.src.World;

public class StairsBlockBase extends Block {
   @Environment(EnvType.CLIENT)
   private boolean renderingBase = false;

   protected StairsBlockBase(int iBlockID, Material material) {
      super(iBlockID, material);
      this.k(255);
      Block.useNeighborBrightness[iBlockID] = true;
      this.a(CreativeTabs.tabDecorations);
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
   public int getRenderType() {
      return 10;
   }

   @Override
   public void onBlockAdded(World world, int i, int j, int k) {
      this.notifyAllNearbyBlocksFlat(world, i, j, k);
   }

   @Override
   public void breakBlock(World world, int i, int j, int k, int iBlockID, int iMetadata) {
      this.notifyAllNearbyBlocksFlat(world, i, j, k);
   }

   @Override
   public int preBlockPlacedBy(World world, int i, int j, int k, int iMetadata, EntityLiving entityBy) {
      int iFlatFacing = MathHelper.floor_float(entityBy.rotationYaw * 4.0F / 360.0F + 0.5F) & 3;
      if (iFlatFacing == 0) {
         iMetadata = this.setDirection(iMetadata, 2);
      } else if (iFlatFacing == 1) {
         iMetadata = this.setDirection(iMetadata, 1);
      } else if (iFlatFacing == 2) {
         iMetadata = this.setDirection(iMetadata, 3);
      } else {
         iMetadata = this.setDirection(iMetadata, 0);
      }

      return this.validateMetadataForLocation(world, i, j, k, iMetadata);
   }

   @Override
   public int onBlockPlaced(World world, int i, int j, int k, int iFacing, float fClickX, float fClickY, float fClickZ, int iMetadata) {
      if (iFacing == 0 || iFacing != 1 && fClickY > 0.5) {
         iMetadata = this.setUpsideDown(iMetadata);
      }

      return iMetadata;
   }

   @Override
   public void addCollisionBoxesToList(World world, int i, int j, int k, AxisAlignedBB intersectingBox, List list, Entity entity) {
      AxisAlignedBB baseBox = this.getBoundsFromPoolForBase(world, i, j, k).offset(i, j, k);
      baseBox.addToListIfIntersects(intersectingBox, list);
      AxisAlignedBB secondaryBox = AxisAlignedBB.getAABBPool().getAABB(0.0, 0.0, 0.0, 1.0, 1.0, 1.0);
      boolean bIsFullStep = this.getBoundsForSecondaryPiece(world, i, j, k, secondaryBox);
      secondaryBox.offset(i, j, k);
      secondaryBox.addToListIfIntersects(intersectingBox, list);
      if (bIsFullStep) {
         AxisAlignedBB tertiaryBox = AxisAlignedBB.getAABBPool().getAABB(0.0, 0.0, 0.0, 1.0, 1.0, 1.0);
         int iTertiaryFacing = this.getBoundsForTertiaryPiece(world, i, j, k, tertiaryBox);
         if (iTertiaryFacing >= 0) {
            tertiaryBox.offset(i, j, k);
            tertiaryBox.addToListIfIntersects(intersectingBox, list);
         }
      }
   }

   @Override
   public MovingObjectPosition collisionRayTrace(World world, int i, int j, int k, Vec3 startVec, Vec3 endVec) {
      RayTraceUtils rayTrace = new RayTraceUtils(world, i, j, k, startVec, endVec);
      AxisAlignedBB baseBox = this.getBoundsFromPoolForBase(world, i, j, k);
      rayTrace.addBoxWithLocalCoordsToIntersectionList(baseBox);
      AxisAlignedBB secondaryBox = AxisAlignedBB.getAABBPool().getAABB(0.0, 0.0, 0.0, 1.0, 1.0, 1.0);
      boolean bIsFullStep = this.getBoundsForSecondaryPiece(world, i, j, k, secondaryBox);
      rayTrace.addBoxWithLocalCoordsToIntersectionList(secondaryBox);
      if (bIsFullStep) {
         AxisAlignedBB tertiaryBox = AxisAlignedBB.getAABBPool().getAABB(0.0, 0.0, 0.0, 1.0, 1.0, 1.0);
         int iTertiaryFacing = this.getBoundsForTertiaryPiece(world, i, j, k, tertiaryBox);
         if (iTertiaryFacing >= 0) {
            rayTrace.addBoxWithLocalCoordsToIntersectionList(tertiaryBox);
         }
      }

      return rayTrace.getFirstIntersection();
   }

   @Override
   public boolean hasLargeCenterHardPointToFacing(IBlockAccess blockAccess, int i, int j, int k, int iFacing, boolean bIgnoreTransparency) {
      if (iFacing == 0) {
         return !this.getIsUpsideDown(blockAccess, i, j, k);
      } else if (iFacing == 1) {
         return this.getIsUpsideDown(blockAccess, i, j, k);
      } else {
         int iBlockFacing = this.convertDirectionToFacing(this.getDirection(blockAccess, i, j, k));
         if (iFacing == iBlockFacing) {
            return true;
         } else {
            return iFacing != Block.getOppositeFacing(iBlockFacing) ? this.hasSecondaryFullSurfaceToFacing(blockAccess, i, j, k, iFacing) : false;
         }
      }
   }

   @Override
   public boolean isStairBlock() {
      return true;
   }

   @Override
   protected boolean canSilkHarvest() {
      return true;
   }

   @Override
   public boolean hasContactPointToFullFace(IBlockAccess blockAccess, int i, int j, int k, int iFacing) {
      return true;
   }

   @Override
   public boolean hasContactPointToSlabSideFace(IBlockAccess blockAccess, int i, int j, int k, int iFacing, boolean bIsSlabUpsideDown) {
      int iStairFacing = this.convertDirectionToFacing(this.getDirection(blockAccess, i, j, k));
      return iFacing == Block.getOppositeFacing(iStairFacing) ? this.getIsUpsideDown(blockAccess, i, j, k) == bIsSlabUpsideDown : true;
   }

   @Override
   public boolean hasContactPointToStairNarrowVerticalFace(IBlockAccess blockAccess, int i, int j, int k, int iFacing, int iStairFacing) {
      boolean bIsUpsideDown = this.getIsUpsideDown(blockAccess, i, j, k);
      if (bIsUpsideDown == (iFacing == 1)) {
         return true;
      } else {
         int iMyStairFacing = this.convertDirectionToFacing(this.getDirection(blockAccess, i, j, k));
         return iMyStairFacing != Block.getOppositeFacing(iStairFacing);
      }
   }

   @Override
   public boolean hasNeighborWithMortarInContact(World world, int i, int j, int k) {
      int iFacing = this.convertDirectionToFacing(this.getDirection(world, i, j, k));
      boolean bIsUpsideDown = this.getIsUpsideDown(world, i, j, k);
      return this.hasNeighborWithMortarInContact(world, i, j, k, iFacing, bIsUpsideDown);
   }

   @Override
   public boolean canRotateOnTurntable(IBlockAccess blockAccess, int i, int j, int k) {
      return true;
   }

   @Override
   public boolean canTransmitRotationVerticallyOnTurntable(IBlockAccess blockAccess, int i, int j, int k) {
      return true;
   }

   @Override
   public int rotateMetadataAroundJAxis(int iMetadata, boolean bReverse) {
      int iDirection = (iMetadata & 3) + 2;
      iDirection = Block.rotateFacingAroundY(iDirection, !bReverse);
      return iMetadata & -4 | iDirection - 2;
   }

   @Override
   public boolean canMobsSpawnOn(World world, int i, int j, int k) {
      return this.blockMaterial.getMobsCanSpawnOn(world.provider.dimensionId);
   }

   protected int validateMetadataForLocation(World world, int i, int j, int k, int iMetadata) {
      return iMetadata;
   }

   public boolean hasNeighborWithMortarInContact(World world, int i, int j, int k, int iFacing, boolean bIsUpsideDown) {
      if (!bIsUpsideDown) {
         if (WorldUtils.hasNeighborWithMortarInFullFaceContactToFacing(world, i, j, k, 0)
            || WorldUtils.hasNeighborWithMortarInStairNarrowVerticalContactToFacing(world, i, j, k, 1, iFacing)) {
            return true;
         }
      } else if (WorldUtils.hasNeighborWithMortarInFullFaceContactToFacing(world, i, j, k, 1)
         || WorldUtils.hasNeighborWithMortarInStairNarrowVerticalContactToFacing(world, i, j, k, 0, iFacing)) {
         return true;
      }

      int iHalfBlockFacing = Block.getOppositeFacing(iFacing);

      for (int iTempFacing = 2; iTempFacing < 6; iTempFacing++) {
         if (iTempFacing == iHalfBlockFacing) {
            if (WorldUtils.hasNeighborWithMortarInSlabSideContactToFacing(world, i, j, k, iTempFacing, bIsUpsideDown)) {
               return true;
            }
         } else if (WorldUtils.hasNeighborWithMortarInStairShapedContactToFacing(world, i, j, k, iTempFacing)) {
            return true;
         }
      }

      return false;
   }

   protected int convertDirectionToFacing(int iDirection) {
      return 5 - iDirection;
   }

   protected AxisAlignedBB getBoundsFromPoolForBase(IBlockAccess blockAccess, int i, int j, int k) {
      return this.getBoundsFromPoolForBase(blockAccess.getBlockMetadata(i, j, k));
   }

   protected AxisAlignedBB getBoundsFromPoolForBase(int iMetadata) {
      return this.getIsUpsideDown(iMetadata)
         ? AxisAlignedBB.getAABBPool().getAABB(0.0, 0.5, 0.0, 1.0, 1.0, 1.0)
         : AxisAlignedBB.getAABBPool().getAABB(0.0, 0.0, 0.0, 1.0, 0.5, 1.0);
   }

   protected boolean getBoundsForSecondaryPiece(IBlockAccess blockAccess, int i, int j, int k, AxisAlignedBB box) {
      int iMetadata = blockAccess.getBlockMetadata(i, j, k);
      int iBlockDir = this.getDirection(iMetadata);
      boolean bUpsideDown = this.getIsUpsideDown(iMetadata);
      box.minY = 0.5;
      box.maxY = 1.0;
      if (bUpsideDown) {
         box.minY = 0.0;
         box.maxY = 0.5;
      }

      box.minX = 0.0;
      box.minZ = 0.0;
      box.maxX = 1.0;
      box.maxZ = 0.5;
      boolean bIsFullStep = true;
      if (iBlockDir == 0) {
         box.minX = 0.5;
         box.maxZ = 1.0;
         int iNeighborID = blockAccess.getBlockId(i + 1, j, k);
         int iNeighborMetadata = blockAccess.getBlockMetadata(i + 1, j, k);
         if (this.isStairBlock(iNeighborID) && (iMetadata & 4) == (iNeighborMetadata & 4)) {
            int iNeighborDir = iNeighborMetadata & 3;
            if (iNeighborDir == 3 && !this.isStairBlockWithMetadata(blockAccess, i, j, k + 1, iMetadata)) {
               box.maxZ = 0.5;
               bIsFullStep = false;
            } else if (iNeighborDir == 2 && !this.isStairBlockWithMetadata(blockAccess, i, j, k - 1, iMetadata)) {
               box.minZ = 0.5;
               bIsFullStep = false;
            }
         }
      } else if (iBlockDir == 1) {
         box.maxX = 0.5;
         box.maxZ = 1.0;
         int iNeighborID = blockAccess.getBlockId(i - 1, j, k);
         int iNeighborMetadata = blockAccess.getBlockMetadata(i - 1, j, k);
         if (this.isStairBlock(iNeighborID) && (iMetadata & 4) == (iNeighborMetadata & 4)) {
            int iNeighborDir = iNeighborMetadata & 3;
            if (iNeighborDir == 3 && !this.isStairBlockWithMetadata(blockAccess, i, j, k + 1, iMetadata)) {
               box.maxZ = 0.5;
               bIsFullStep = false;
            } else if (iNeighborDir == 2 && !this.isStairBlockWithMetadata(blockAccess, i, j, k - 1, iMetadata)) {
               box.minZ = 0.5;
               bIsFullStep = false;
            }
         }
      } else if (iBlockDir == 2) {
         box.minZ = 0.5;
         box.maxZ = 1.0;
         int iNeighborID = blockAccess.getBlockId(i, j, k + 1);
         int iNeighborMetadata = blockAccess.getBlockMetadata(i, j, k + 1);
         if (this.isStairBlock(iNeighborID) && (iMetadata & 4) == (iNeighborMetadata & 4)) {
            int iNeighborDir = iNeighborMetadata & 3;
            if (iNeighborDir == 1 && !this.isStairBlockWithMetadata(blockAccess, i + 1, j, k, iMetadata)) {
               box.maxX = 0.5;
               bIsFullStep = false;
            } else if (iNeighborDir == 0 && !this.isStairBlockWithMetadata(blockAccess, i - 1, j, k, iMetadata)) {
               box.minX = 0.5;
               bIsFullStep = false;
            }
         }
      } else if (iBlockDir == 3) {
         int iNeighborID = blockAccess.getBlockId(i, j, k - 1);
         int iNeighborMetadata = blockAccess.getBlockMetadata(i, j, k - 1);
         if (this.isStairBlock(iNeighborID) && (iMetadata & 4) == (iNeighborMetadata & 4)) {
            int iNeighborDir = iNeighborMetadata & 3;
            if (iNeighborDir == 1 && !this.isStairBlockWithMetadata(blockAccess, i + 1, j, k, iMetadata)) {
               box.maxX = 0.5;
               bIsFullStep = false;
            } else if (iNeighborDir == 0 && !this.isStairBlockWithMetadata(blockAccess, i - 1, j, k, iMetadata)) {
               box.minX = 0.5;
               bIsFullStep = false;
            }
         }
      }

      return bIsFullStep;
   }

   protected AxisAlignedBB getBoundsFromPoolForSecondaryPiece(int iMetadata) {
      AxisAlignedBB box = AxisAlignedBB.getAABBPool().getAABB(0.0, 0.0, 0.0, 1.0, 1.0, 1.0);
      int iBlockDir = this.getDirection(iMetadata);
      boolean bUpsideDown = this.getIsUpsideDown(iMetadata);
      box.minY = 0.5;
      box.maxY = 1.0;
      if (bUpsideDown) {
         box.minY = 0.0;
         box.maxY = 0.5;
      }

      box.minX = 0.0;
      box.minZ = 0.0;
      box.maxX = 1.0;
      box.maxZ = 0.5;
      if (iBlockDir == 0) {
         box.minX = 0.5;
         box.maxZ = 1.0;
      } else if (iBlockDir == 1) {
         box.maxX = 0.5;
         box.maxZ = 1.0;
      } else if (iBlockDir == 2) {
         box.minZ = 0.5;
         box.maxZ = 1.0;
      }

      return box;
   }

   protected int getBoundsForTertiaryPiece(IBlockAccess blockAccess, int i, int j, int k, AxisAlignedBB box) {
      int iMetadata = blockAccess.getBlockMetadata(i, j, k);
      int iBlockDir = this.getDirection(iMetadata);
      boolean bUpsideDown = this.getIsUpsideDown(iMetadata);
      int iFacing = -1;
      box.minY = 0.5;
      box.maxY = 1.0;
      if (bUpsideDown) {
         box.minY = 0.0;
         box.maxY = 0.5;
      }

      box.minX = 0.0;
      box.minZ = 0.5;
      box.maxX = 0.5;
      box.maxZ = 1.0;
      if (iBlockDir == 0) {
         int iNeighborBlockID = blockAccess.getBlockId(i - 1, j, k);
         int iNeighborMetadata = blockAccess.getBlockMetadata(i - 1, j, k);
         if (this.isStairBlock(iNeighborBlockID) && (iMetadata & 4) == (iNeighborMetadata & 4)) {
            int iNeighborDir = iNeighborMetadata & 3;
            if (iNeighborDir == 3 && !this.isStairBlockWithMetadata(blockAccess, i, j, k - 1, iMetadata)) {
               box.minZ = 0.0;
               box.maxZ = 0.5;
               iFacing = 2;
            } else if (iNeighborDir == 2 && !this.isStairBlockWithMetadata(blockAccess, i, j, k + 1, iMetadata)) {
               box.minZ = 0.5;
               box.maxZ = 1.0;
               iFacing = 3;
            }
         }
      } else if (iBlockDir == 1) {
         int iNeighborBlockID = blockAccess.getBlockId(i + 1, j, k);
         int iNeighborMetadata = blockAccess.getBlockMetadata(i + 1, j, k);
         if (this.isStairBlock(iNeighborBlockID) && (iMetadata & 4) == (iNeighborMetadata & 4)) {
            box.minX = 0.5;
            box.maxX = 1.0;
            int iNeighborDir = iNeighborMetadata & 3;
            if (iNeighborDir == 3 && !this.isStairBlockWithMetadata(blockAccess, i, j, k - 1, iMetadata)) {
               box.minZ = 0.0;
               box.maxZ = 0.5;
               iFacing = 2;
            } else if (iNeighborDir == 2 && !this.isStairBlockWithMetadata(blockAccess, i, j, k + 1, iMetadata)) {
               box.minZ = 0.5;
               box.maxZ = 1.0;
               iFacing = 3;
            }
         }
      } else if (iBlockDir == 2) {
         int iNeighborBlockID = blockAccess.getBlockId(i, j, k - 1);
         int iNeighborMetadata = blockAccess.getBlockMetadata(i, j, k - 1);
         if (this.isStairBlock(iNeighborBlockID) && (iMetadata & 4) == (iNeighborMetadata & 4)) {
            box.minZ = 0.0;
            box.maxZ = 0.5;
            int iNeighborDir = iNeighborMetadata & 3;
            if (iNeighborDir == 1 && !this.isStairBlockWithMetadata(blockAccess, i - 1, j, k, iMetadata)) {
               iFacing = 4;
            } else if (iNeighborDir == 0 && !this.isStairBlockWithMetadata(blockAccess, i + 1, j, k, iMetadata)) {
               box.minX = 0.5;
               box.maxX = 1.0;
               iFacing = 5;
            }
         }
      } else if (iBlockDir == 3) {
         int iNeighborBlockID = blockAccess.getBlockId(i, j, k + 1);
         int iNeighborMetadata = blockAccess.getBlockMetadata(i, j, k + 1);
         if (this.isStairBlock(iNeighborBlockID) && (iMetadata & 4) == (iNeighborMetadata & 4)) {
            int iNeighborDir = iNeighborMetadata & 3;
            if (iNeighborDir == 1 && !this.isStairBlockWithMetadata(blockAccess, i - 1, j, k, iMetadata)) {
               iFacing = 4;
            } else if (iNeighborDir == 0 && !this.isStairBlockWithMetadata(blockAccess, i + 1, j, k, iMetadata)) {
               box.minX = 0.5;
               box.maxX = 1.0;
               iFacing = 5;
            }
         }
      }

      return iFacing;
   }

   private boolean hasSecondaryFullSurfaceToFacing(IBlockAccess blockAccess, int i, int j, int k, int iFacing) {
      AxisAlignedBB box = AxisAlignedBB.getAABBPool().getAABB(0.0, 0.0, 0.0, 1.0, 1.0, 1.0);
      boolean bHasFullStep = this.getBoundsForSecondaryPiece(blockAccess, i, j, k, box);
      return bHasFullStep && iFacing == this.getBoundsForTertiaryPiece(blockAccess, i, j, k, box);
   }

   protected boolean isStairBlock(int iBlockID) {
      Block block = Block.blocksList[iBlockID];
      return block != null ? block.isStairBlock() : false;
   }

   protected boolean getIsUpsideDown(IBlockAccess blockAccess, int i, int j, int k) {
      return this.getIsUpsideDown(blockAccess.getBlockMetadata(i, j, k));
   }

   protected boolean getIsUpsideDown(int iMetadata) {
      return (iMetadata & 4) != 0;
   }

   protected int setUpsideDown(int iMetadata) {
      return iMetadata | 4;
   }

   protected int setIsUpsideDown(int iMetadata, boolean bUpsideDown) {
      if (bUpsideDown) {
         iMetadata |= 4;
      } else {
         iMetadata &= -5;
      }

      return iMetadata;
   }

   protected void setIsUpsideDown(World world, int i, int j, int k, boolean bUpsideDown) {
      int iMetadata = this.setIsUpsideDown(world.getBlockMetadata(i, j, k), bUpsideDown);
      world.setBlockMetadataWithNotify(i, j, k, iMetadata);
   }

   protected void setDirection(World world, int i, int j, int k, int iDirection) {
      int iMetadata = this.setDirection(world.getBlockMetadata(i, j, k), iDirection);
      world.SetBlockMetadataWithNotify(i, j, k, iMetadata, 2);
   }

   protected int setDirection(int iMetadata, int iDirection) {
      iMetadata &= -4;
      return iMetadata | iDirection;
   }

   protected int getDirection(IBlockAccess blockAccess, int i, int j, int k) {
      return this.getDirection(blockAccess.getBlockMetadata(i, j, k));
   }

   protected int getDirection(int iMetadata) {
      return iMetadata & 3;
   }

   protected boolean isStairBlockWithMetadata(IBlockAccess blockAccess, int i, int j, int k, int iMetadata) {
      return WorldUtils.isStairBlock(blockAccess, i, j, k) && blockAccess.getBlockMetadata(i, j, k) == iMetadata;
   }

   protected void notifyAllNearbyBlocksFlat(World world, int i, int j, int k) {
      world.notifyBlockOfNeighborChange(i - 1, j, k, this.blockID);
      world.notifyBlockOfNeighborChange(i - 2, j, k, this.blockID);
      world.notifyBlockOfNeighborChange(i + 1, j, k, this.blockID);
      world.notifyBlockOfNeighborChange(i + 2, j, k, this.blockID);
      world.notifyBlockOfNeighborChange(i, j, k - 1, this.blockID);
      world.notifyBlockOfNeighborChange(i, j, k - 2, this.blockID);
      world.notifyBlockOfNeighborChange(i, j, k + 1, this.blockID);
      world.notifyBlockOfNeighborChange(i, j, k + 2, this.blockID);
      world.notifyBlockOfNeighborChange(i - 1, j, k - 1, this.blockID);
      world.notifyBlockOfNeighborChange(i - 1, j, k + 1, this.blockID);
      world.notifyBlockOfNeighborChange(i + 1, j, k - 1, this.blockID);
      world.notifyBlockOfNeighborChange(i + 1, j, k + 1, this.blockID);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean renderBlock(RenderBlocks renderer, int i, int j, int k) {
      renderer.setRenderBounds(this.getBlockBoundsFromPoolBasedOnState(renderer.blockAccess, i, j, k));
      return this.renderBlockStairs(renderer, i, j, k);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean shouldSideBeRendered(IBlockAccess blockAccess, int iNeighborI, int iNeighborJ, int iNeighborK, int iSide) {
      if (!this.renderingBase && iSide < 2) {
         BlockPos myPos = new BlockPos(iNeighborI, iNeighborJ, iNeighborK, getOppositeFacing(iSide));
         if (this.getIsUpsideDown(blockAccess, myPos.x, myPos.y, myPos.z)) {
            if (iSide == 1) {
               return false;
            }
         } else if (iSide == 0) {
            return false;
         }
      }

      return this.currentBlockRenderer.shouldSideBeRenderedBasedOnCurrentBounds(iNeighborI, iNeighborJ, iNeighborK, iSide);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean shouldRenderNeighborHalfSlabSide(IBlockAccess blockAccess, int i, int j, int k, int iNeighborSlabSide, boolean bNeighborUpsideDown) {
      boolean bUpsideDown = this.getIsUpsideDown(blockAccess, i, j, k);
      if (bUpsideDown == bNeighborUpsideDown) {
         return false;
      } else {
         int iBlockFacing = this.convertDirectionToFacing(this.getDirection(blockAccess, i, j, k));
         return iNeighborSlabSide != Block.getOppositeFacing(iBlockFacing);
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean shouldRenderNeighborFullFaceSide(IBlockAccess blockAccess, int i, int j, int k, int iNeighborSide) {
      if (iNeighborSide < 2) {
         boolean bUpsideDown = this.getIsUpsideDown(blockAccess, i, j, k);
         return iNeighborSide == 0 ? !bUpsideDown : bUpsideDown;
      } else {
         int iBlockFacing = this.convertDirectionToFacing(this.getDirection(blockAccess, i, j, k));
         return iNeighborSide != Block.getOppositeFacing(iBlockFacing);
      }
   }

   @Environment(EnvType.CLIENT)
   private boolean renderBlockStairs(RenderBlocks renderBlocks, int i, int j, int k) {
      this.renderingBase = true;
      renderBlocks.setRenderBounds(this.getBoundsFromPoolForBase(renderBlocks.blockAccess, i, j, k));
      renderBlocks.renderStandardBlock(this, i, j, k);
      this.renderingBase = false;
      AxisAlignedBB secondaryBox = AxisAlignedBB.getAABBPool().getAABB(0.0, 0.0, 0.0, 1.0, 1.0, 1.0);
      boolean bIsFullStep = this.getBoundsForSecondaryPiece(renderBlocks.blockAccess, i, j, k, secondaryBox);
      renderBlocks.setRenderBounds(secondaryBox);
      renderBlocks.renderStandardBlock(this, i, j, k);
      AxisAlignedBB tertiaryBox = AxisAlignedBB.getAABBPool().getAABB(0.0, 0.0, 0.0, 1.0, 1.0, 1.0);
      int iTertiaryFacing = this.getBoundsForTertiaryPiece(renderBlocks.blockAccess, i, j, k, tertiaryBox);
      if (iTertiaryFacing >= 0) {
         renderBlocks.setRenderBounds(tertiaryBox);
         renderBlocks.renderStandardBlock(this, i, j, k);
      }

      return true;
   }
}
