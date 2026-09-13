package btw.block.blocks;

import btw.block.util.RayTraceUtils;
import btw.world.util.BlockPos;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.Entity;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.IconRegister;
import net.minecraft.src.Material;
import net.minecraft.src.MovingObjectPosition;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.StepSound;
import net.minecraft.src.Vec3;
import net.minecraft.src.World;

public class MouldingBlock extends Block {
   protected static final double MOULDING_WIDTH = 0.5;
   protected static final double MOULDING_HALF_WIDTH = 0.25;
   protected static final double MOULDING_LENGTH = 1.0;
   private static final int[][] facingOfConnections = new int[][]{
      {-1, 4, -1, 5, 5, 4, 4, 5, -1, 4, -1, 5}, {1, 1, 1, 1, -1, -1, -1, -1, 0, 0, 0, 0}, {3, -1, 2, -1, 3, 3, 2, 2, 3, -1, 2, -1}
   };
   private static final int[][] alignmentOffsetAlongAxis = new int[][]{
      {0, 1, 0, -1, -1, 1, 1, -1, 0, 1, 0, -1}, {-1, -1, -1, -1, 0, 0, 0, 0, 1, 1, 1, 1}, {-1, 0, 1, 0, -1, -1, 1, 1, -1, 0, 1, 0}
   };
   protected int matchingCornerBlockID;
   String textureName;

   protected MouldingBlock(
      int iBlockID, Material material, String sTextureName, int iMatchingCornerBlockID, float fHardness, float fResistance, StepSound stepSound, String name
   ) {
      super(iBlockID, material);
      this.c(fHardness);
      this.b(fResistance);
      this.initBlockBounds(0.25, 0.25, 0.0, 0.75, 0.75, 1.0);
      this.a(stepSound);
      this.c(name);
      this.matchingCornerBlockID = iMatchingCornerBlockID;
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
   public MovingObjectPosition collisionRayTrace(World world, int i, int j, int k, Vec3 startRay, Vec3 endRay) {
      int iAlignment = this.getMouldingAlignment(world, i, j, k);
      RayTraceUtils rayTrace = new RayTraceUtils(world, i, j, k, startRay, endRay);
      rayTrace.addBoxWithLocalCoordsToIntersectionList(this.getBlockBoundsFromPoolForAlignment(iAlignment));

      for (int iAxis = 0; iAxis <= 2; iAxis++) {
         AxisAlignedBB tempBox = this.getBlockBoundsFromPoolForConnectingBlocksAlongAxis(world, i, j, k, iAxis);
         if (tempBox != null) {
            rayTrace.addBoxWithLocalCoordsToIntersectionList(tempBox);
         }
      }

      return rayTrace.getFirstIntersection();
   }

   @Override
   public void addCollisionBoxesToList(World world, int i, int j, int k, AxisAlignedBB intersectingBox, List list, Entity entity) {
      int iAlignment = this.getMouldingAlignment(world, i, j, k);
      this.getBlockBoundsFromPoolForAlignment(iAlignment).offset(i, j, k).addToListIfIntersects(intersectingBox, list);

      for (int iAxis = 0; iAxis <= 2; iAxis++) {
         AxisAlignedBB tempBox = this.getBlockBoundsFromPoolForConnectingBlocksAlongAxis(world, i, j, k, iAxis);
         if (tempBox != null) {
            tempBox.offset(i, j, k).addToListIfIntersects(intersectingBox, list);
         }
      }
   }

   @Override
   public int onBlockPlaced(World world, int i, int j, int k, int iFacing, float fClickX, float fClickY, float fClickZ, int iMetadata) {
      int iAlignment = 0;
      float fXOffsetFromCenter = Math.abs(fClickX - 0.5F);
      float fYOffsetFromCenter = Math.abs(fClickY - 0.5F);
      float fZOffsetFromCenter = Math.abs(fClickZ - 0.5F);
      byte var14;
      switch (iFacing) {
         case 0:
            if (fXOffsetFromCenter > fZOffsetFromCenter) {
               if (fClickX > 0.5F) {
                  var14 = 9;
               } else {
                  var14 = 11;
               }
            } else if (fClickZ > 0.5F) {
               var14 = 10;
            } else {
               var14 = 8;
            }
            break;
         case 1:
            if (fXOffsetFromCenter > fZOffsetFromCenter) {
               if (fClickX > 0.5F) {
                  var14 = 1;
               } else {
                  var14 = 3;
               }
            } else if (fClickZ > 0.5F) {
               var14 = 2;
            } else {
               var14 = 0;
            }
            break;
         case 2:
            if (fXOffsetFromCenter > fYOffsetFromCenter) {
               if (fClickX > 0.5F) {
                  var14 = 6;
               } else {
                  var14 = 7;
               }
            } else if (fClickY > 0.5F) {
               var14 = 10;
            } else {
               var14 = 2;
            }
            break;
         case 3:
            if (fXOffsetFromCenter > fYOffsetFromCenter) {
               if (fClickX > 0.5F) {
                  var14 = 5;
               } else {
                  var14 = 4;
               }
            } else if (fClickY > 0.5F) {
               var14 = 8;
            } else {
               var14 = 0;
            }
            break;
         case 4:
            if (fZOffsetFromCenter > fYOffsetFromCenter) {
               if (fClickZ > 0.5F) {
                  var14 = 6;
               } else {
                  var14 = 5;
               }
            } else if (fClickY > 0.5F) {
               var14 = 9;
            } else {
               var14 = 1;
            }
            break;
         default:
            if (fZOffsetFromCenter > fYOffsetFromCenter) {
               if (fClickZ > 0.5F) {
                  var14 = 7;
               } else {
                  var14 = 4;
               }
            } else if (fClickY > 0.5F) {
               var14 = 11;
            } else {
               var14 = 3;
            }
      }

      return this.setMouldingAlignmentInMetadata(iMetadata, var14);
   }

   @Override
   public boolean canRotateOnTurntable(IBlockAccess blockAccess, int i, int j, int k) {
      int iAlignment = this.getMouldingAlignment(blockAccess, i, j, k);
      return iAlignment < 8;
   }

   @Override
   public int rotateMetadataAroundJAxis(int iMetadata, boolean bReverse) {
      int iAlignment = this.getMouldingAlignmentFromMetadata(iMetadata);
      if (bReverse) {
         if (++iAlignment == 4) {
            iAlignment = 0;
         } else if (iAlignment == 8) {
            iAlignment = 4;
         } else if (iAlignment >= 12) {
            iAlignment = 8;
         }
      } else if (--iAlignment < 0) {
         iAlignment = 3;
      } else if (iAlignment == 3) {
         iAlignment = 7;
      } else if (iAlignment == 7) {
         iAlignment = 11;
      }

      return this.setMouldingAlignmentInMetadata(iMetadata, iAlignment);
   }

   @Override
   public boolean toggleFacing(World world, int i, int j, int k, boolean bReverse) {
      int iAlignment = this.getMouldingAlignment(world, i, j, k);
      if (!bReverse) {
         if (++iAlignment > 11) {
            iAlignment = 0;
         }
      } else if (--iAlignment < 0) {
         iAlignment = 11;
      }

      this.setMouldingAlignment(world, i, j, k, iAlignment);
      world.markBlockRangeForRenderUpdate(i, j, k, i, j, k);
      return true;
   }

   @Override
   public float mobSpawnOnVerticalOffset(World world, int i, int j, int k) {
      int iAlignment = this.getMouldingAlignment(world, i, j, k);
      return iAlignment < 4 ? -0.5F : 0.0F;
   }

   @Override
   public AxisAlignedBB getBlockBoundsFromPoolBasedOnState(IBlockAccess blockAccess, int i, int j, int k) {
      return this.getBlockBoundsFromPoolForAlignment(this.getMouldingAlignment(blockAccess, i, j, k));
   }

   public AxisAlignedBB getBlockBoundsFromPoolForAlignment(int iAlignment) {
      AxisAlignedBB bounds = AxisAlignedBB.getAABBPool().getAABB(0.0, 0.0, 0.0, 1.0, 1.0, 1.0);
      if (iAlignment == 0) {
         bounds.maxY = bounds.minY + 0.5;
         bounds.maxZ = bounds.minZ + 0.5;
      } else if (iAlignment == 1) {
         bounds.minX += 0.5;
         bounds.maxY = bounds.minY + 0.5;
      } else if (iAlignment == 2) {
         bounds.maxY = bounds.minY + 0.5;
         bounds.minZ += 0.5;
      } else if (iAlignment == 3) {
         bounds.maxX = bounds.minX + 0.5;
         bounds.maxY = bounds.minY + 0.5;
      } else if (iAlignment == 4) {
         bounds.maxX = bounds.minX + 0.5;
         bounds.maxZ = bounds.minZ + 0.5;
      } else if (iAlignment == 5) {
         bounds.minX += 0.5;
         bounds.maxZ = bounds.minZ + 0.5;
      } else if (iAlignment == 6) {
         bounds.minX += 0.5;
         bounds.minZ += 0.5;
      } else if (iAlignment == 7) {
         bounds.maxX = bounds.minX + 0.5;
         bounds.minZ += 0.5;
      } else if (iAlignment == 8) {
         bounds.minY += 0.5;
         bounds.maxZ = bounds.minZ + 0.5;
      } else if (iAlignment == 9) {
         bounds.minX += 0.5;
         bounds.minY += 0.5;
      } else if (iAlignment == 10) {
         bounds.minY += 0.5;
         bounds.minZ += 0.5;
      } else {
         bounds.maxX = bounds.minX + 0.5;
         bounds.minY += 0.5;
      }

      return bounds;
   }

   protected boolean isMouldingOfSameType(IBlockAccess blockAccess, int i, int j, int k) {
      return blockAccess.getBlockId(i, j, k) == this.blockID;
   }

   public int getMouldingAlignment(IBlockAccess iBlockAccess, int i, int j, int k) {
      return this.getMouldingAlignmentFromMetadata(iBlockAccess.getBlockMetadata(i, j, k));
   }

   public int getMouldingAlignmentFromMetadata(int iMetadata) {
      return iMetadata;
   }

   public void setMouldingAlignment(World world, int i, int j, int k, int iAlignment) {
      int iMetadata = world.getBlockMetadata(i, j, k);
      iMetadata = this.setMouldingAlignmentInMetadata(iMetadata, iAlignment);
      world.setBlockMetadataWithNotify(i, j, k, iMetadata);
   }

   public int setMouldingAlignmentInMetadata(int iMetadata, int iAlignment) {
      return iAlignment;
   }

   private float clickOffsetFromCenter(float fClickPos) {
      return Math.abs(fClickPos - 0.5F);
   }

   private static void offsetCornerBoundingBoxAlongAxis(int iAxis, int iOffset, Vec3 min, Vec3 max) {
      if (iOffset > 0) {
         if (iAxis == 0) {
            min.xCoord += 0.5;
            max.xCoord += 0.5;
         } else if (iAxis == 1) {
            min.yCoord += 0.5;
            max.yCoord += 0.5;
         } else {
            min.zCoord += 0.5;
            max.zCoord += 0.5;
         }
      }
   }

   private boolean isAlignedAlongAxis(IBlockAccess blockAccess, int i, int j, int k, int iAxis) {
      int iAlignment = this.getMouldingAlignment(blockAccess, i, j, k);
      return facingOfConnections[iAxis][iAlignment] < 0;
   }

   private int getAlignmentOfConnectingMouldingAtLocation(IBlockAccess blockAccess, int i, int j, int k, int iAlignmentToConnectTo, int iAxisToConnectAlong) {
      int iBlockID = blockAccess.getBlockId(i, j, k);
      if (this.isMouldingOfSameType(blockAccess, i, j, k) && this.isAlignedAlongAxis(blockAccess, i, j, k, iAxisToConnectAlong)) {
         int iTargetAlignment = this.getMouldingAlignment(blockAccess, i, j, k);

         for (int iTempAxis = 0; iTempAxis <= 2; iTempAxis++) {
            if (iTempAxis != iAxisToConnectAlong && facingOfConnections[iTempAxis][iTargetAlignment] == facingOfConnections[iTempAxis][iAlignmentToConnectTo]) {
               return iTargetAlignment;
            }
         }
      }

      return -1;
   }

   private int getConnectingCornerFacingAtLocation(IBlockAccess blockAccess, int i, int j, int k, int iAlignmentToConnectTo, int iAxisToConnectAlong) {
      int iBlockID = blockAccess.getBlockId(i, j, k);
      if (iBlockID == this.matchingCornerBlockID) {
         SidingAndCornerBlock corner = (SidingAndCornerBlock)Block.blocksList[iBlockID];
         int iMetadata = blockAccess.getBlockMetadata(i, j, k);
         if (corner.getIsCorner(iMetadata)) {
            int iCornerFacing = corner.getFacing(iMetadata);
            if (alignmentOffsetAlongAxis[iAxisToConnectAlong][iAlignmentToConnectTo]
               == SidingAndCornerBlock.getCornerAlignmentOffsetAlongAxis(iCornerFacing, iAxisToConnectAlong)) {
               int iReturnValue = -1;

               for (int iTempAxis = 0; iTempAxis <= 2; iTempAxis++) {
                  if (iTempAxis != iAxisToConnectAlong) {
                     if (alignmentOffsetAlongAxis[iTempAxis][iAlignmentToConnectTo] == 0) {
                        iReturnValue = iCornerFacing;
                     } else if (alignmentOffsetAlongAxis[iTempAxis][iAlignmentToConnectTo]
                        != SidingAndCornerBlock.getCornerAlignmentOffsetAlongAxis(iCornerFacing, iTempAxis)) {
                        return -1;
                     }
                  }
               }

               return iReturnValue;
            }
         }
      }

      return -1;
   }

   private AxisAlignedBB getBlockBoundsFromPoolForConnectingBlocksAlongAxis(IBlockAccess blockAccess, int i, int j, int k, int iAxis) {
      int iAlignment = this.getMouldingAlignment(blockAccess, i, j, k);
      int iConnectionToFacing = facingOfConnections[iAxis][iAlignment];
      if (iConnectionToFacing >= 0) {
         BlockPos connectingPos = new BlockPos(i, j, k, iConnectionToFacing);
         int iConnectingAlignment = this.getAlignmentOfConnectingMouldingAtLocation(
            blockAccess, connectingPos.x, connectingPos.y, connectingPos.z, iAlignment, iAxis
         );
         if (iConnectingAlignment >= 0) {
            return this.getBlockBoundsFromPoolForConnectingMoulding(iConnectionToFacing, iConnectingAlignment);
         }

         int iConnectingFacing = this.getConnectingCornerFacingAtLocation(blockAccess, connectingPos.x, connectingPos.y, connectingPos.z, iAlignment, iAxis);
         if (iConnectingFacing >= 0) {
            return this.getBlockBoundsFromPoolForConnectingCorner(iAxis, alignmentOffsetAlongAxis[iAxis][iAlignment], iConnectingFacing);
         }
      }

      return null;
   }

   private AxisAlignedBB getBlockBoundsFromPoolForConnectingMoulding(int iToFacing, int iToAlignment) {
      AxisAlignedBB box = this.getBlockBoundsFromPoolForAlignment(iToAlignment);
      if (iToFacing == 0) {
         box.maxY = 0.5;
      } else if (iToFacing == 1) {
         box.minY = 0.5;
      } else if (iToFacing == 2) {
         box.maxZ = 0.5;
      } else if (iToFacing == 3) {
         box.minZ = 0.5;
      } else if (iToFacing == 4) {
         box.maxX = 0.5;
      } else {
         box.minX = 0.5;
      }

      return box;
   }

   private AxisAlignedBB getBlockBoundsFromPoolForConnectingCorner(int iConnectingAxis, int iOffsetAlongAxis, int iCornerFacing) {
      Vec3 cornerMin = Vec3.createVectorHelper(0.0, 0.0, 0.0);
      Vec3 cornerMax = Vec3.createVectorHelper(0.5, 0.5, 0.5);
      this.offsetBoundingBoxForConnectingCorner(iConnectingAxis, iOffsetAlongAxis, iCornerFacing, cornerMin, cornerMax);
      return AxisAlignedBB.getAABBPool().getAABB(cornerMin.xCoord, cornerMin.yCoord, cornerMin.zCoord, cornerMax.xCoord, cornerMax.yCoord, cornerMax.zCoord);
   }

   private void offsetBoundingBoxForConnectingCorner(int iConnectingAxis, int iOffsetAlongAxis, int iCornerFacing, Vec3 boundingMin, Vec3 boundingMax) {
      for (int iTempAxis = 0; iTempAxis <= 2; iTempAxis++) {
         if (iTempAxis == iConnectingAxis) {
            offsetCornerBoundingBoxAlongAxis(iTempAxis, -iOffsetAlongAxis, boundingMin, boundingMax);
         } else {
            offsetCornerBoundingBoxAlongAxis(
               iTempAxis, SidingAndCornerBlock.getCornerAlignmentOffsetAlongAxis(iCornerFacing, iTempAxis), boundingMin, boundingMax
            );
         }
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void registerIcons(IconRegister register) {
      this.blockIcon = register.registerIcon(this.textureName);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean shouldSideBeRendered(IBlockAccess blockAccess, int iNeighborI, int iNeighborJ, int iNeighborK, int iSide) {
      return this.currentBlockRenderer.shouldSideBeRenderedBasedOnCurrentBounds(iNeighborI, iNeighborJ, iNeighborK, iSide);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int i, int j, int k) {
      int iAlignment = this.getMouldingAlignment(world, i, j, k);
      AxisAlignedBB box = this.getBlockBoundsFromPoolForAlignment(iAlignment);

      for (int iAxis = 0; iAxis <= 2; iAxis++) {
         AxisAlignedBB tempBox = this.getBlockBoundsFromPoolForConnectingBlocksAlongAxis(world, i, j, k, iAxis);
         if (tempBox != null) {
            box.expandToInclude(tempBox);
         }
      }

      return box.offset(i, j, k);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean renderBlock(RenderBlocks renderer, int i, int j, int k) {
      IBlockAccess blockAccess = renderer.blockAccess;
      int iAlignment = this.getMouldingAlignment(blockAccess, i, j, k);
      renderer.setRenderBounds(this.getBlockBoundsFromPoolForAlignment(iAlignment));
      renderer.renderStandardBlock(this, i, j, k);

      for (int iAxis = 0; iAxis <= 2; iAxis++) {
         AxisAlignedBB tempBox = this.getBlockBoundsFromPoolForConnectingBlocksAlongAxis(blockAccess, i, j, k, iAxis);
         if (tempBox != null) {
            renderer.setRenderBounds(tempBox);
            renderer.renderStandardBlock(this, i, j, k);
         }
      }

      return true;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public int idPicked(World world, int i, int j, int k) {
      return this.a(world.getBlockMetadata(i, j, k), world.rand, 0);
   }
}
