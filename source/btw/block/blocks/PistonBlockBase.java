package btw.block.blocks;

import btw.world.util.BlockPos;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.BlockPistonBase;
import net.minecraft.src.BlockPistonExtension;
import net.minecraft.src.BlockPistonMoving;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.Facing;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Icon;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.TileEntityPiston;
import net.minecraft.src.World;

public class PistonBlockBase extends BlockPistonBase {
   @Environment(EnvType.CLIENT)
   public static boolean isRenderingExtendedBase = false;

   public PistonBlockBase(int iBlockID, boolean bIsSticky) {
      super(iBlockID, bIsSticky);
      this.setPicksEffectiveOn(true);
      this.initBlockBounds(0.0, 0.0, 0.0, 1.0, 1.0, 1.0);
   }

   @Override
   public boolean canContainPistonPackingToFacing(World world, int i, int j, int k, int iFacing) {
      int iMetadata = world.getBlockMetadata(i, j, k);
      return !e(iMetadata) || Block.getOppositeFacing(d(iMetadata)) == iFacing;
   }

   @Override
   public int getFacing(int iMetadata) {
      return iMetadata & 7;
   }

   @Override
   public int setFacing(int iMetadata, int iFacing) {
      iMetadata &= -8;
      return iMetadata | iFacing;
   }

   @Override
   public boolean canRotateOnTurntable(IBlockAccess blockAccess, int i, int j, int k) {
      return !e(blockAccess.getBlockMetadata(i, j, k));
   }

   @Override
   public boolean canTransmitRotationHorizontallyOnTurntable(IBlockAccess blockAccess, int i, int j, int k) {
      return !e(blockAccess.getBlockMetadata(i, j, k));
   }

   @Override
   public boolean canTransmitRotationVerticallyOnTurntable(IBlockAccess blockAccess, int i, int j, int k) {
      return !e(blockAccess.getBlockMetadata(i, j, k));
   }

   @Override
   public int rotateMetadataAroundJAxis(int iMetadata, boolean bReverse) {
      return !e(iMetadata) ? super.rotateMetadataAroundJAxis(iMetadata, bReverse) : iMetadata;
   }

   @Override
   public boolean canBlockBePushedByPiston(World world, int i, int j, int k, int iToFacing) {
      return !e(world.getBlockMetadata(i, j, k));
   }

   @Override
   protected void updatePistonState(World world, int i, int j, int k) {
      this.validatePistonState(world, i, j, k);
      super.updatePistonState(world, i, j, k);
   }

   @Override
   protected boolean canExtend(World world, int i, int j, int k, int iToFacing) {
      int iOffsetI = i + Facing.offsetsXForSide[iToFacing];
      int iOffsetJ = j + Facing.offsetsYForSide[iToFacing];
      int iOffsetK = k + Facing.offsetsZForSide[iToFacing];
      int iDist = 0;

      while (iDist < 13) {
         if (iOffsetJ > 0 && iOffsetJ < 255) {
            Block tempBlock = r[world.getBlockId(iOffsetI, iOffsetJ, iOffsetK)];
            if (tempBlock != null) {
               if (!tempBlock.canBlockBePushedByPiston(world, iOffsetI, iOffsetJ, iOffsetK, iToFacing)) {
                  return false;
               }

               int iMobility = tempBlock.getMobilityFlag();
               int iShovelEjectDirection = this.getPistonShovelEjectionDirection(world, iOffsetI, iOffsetJ, iOffsetK, iToFacing);
               if (iMobility != 1 && iShovelEjectDirection < 0) {
                  if (iDist == 12) {
                     return false;
                  }

                  iOffsetI += Facing.offsetsXForSide[iToFacing];
                  iOffsetJ += Facing.offsetsYForSide[iToFacing];
                  iOffsetK += Facing.offsetsZForSide[iToFacing];
                  iDist++;
                  continue;
               }
            }

            return true;
         }

         return false;
      }

      return true;
   }

   @Override
   protected boolean tryExtend(World world, int x, int y, int z, int facingTo) {
      int offsetX = x + Facing.offsetsXForSide[facingTo];
      int offsetY = y + Facing.offsetsYForSide[facingTo];
      int offsetZ = z + Facing.offsetsZForSide[facingTo];
      int distance = 0;

      while (distance < 13) {
         if (offsetY <= 0 || offsetY >= 255) {
            return false;
         }

         int movingBlockID = world.getBlockId(offsetX, offsetY, offsetZ);
         Block movingBlock = r[movingBlockID];
         if (movingBlock == null) {
            break;
         }

         if (!movingBlock.canBlockBePushedByPiston(world, offsetX, offsetY, offsetZ, facingTo)) {
            return false;
         }

         int mobilityFlag = movingBlock.getMobilityFlag();
         int shovelEjectDirection = this.getPistonShovelEjectionDirection(world, offsetX, offsetY, offsetZ, facingTo);
         if (mobilityFlag != 1 && shovelEjectDirection < 0) {
            if (distance == 12) {
               return false;
            }

            offsetX += Facing.offsetsXForSide[facingTo];
            offsetY += Facing.offsetsYForSide[facingTo];
            offsetZ += Facing.offsetsZForSide[facingTo];
            distance++;
         } else {
            int movingBlockMetadata = world.getBlockMetadata(offsetX, offsetY, offsetZ);
            if (shovelEjectDirection >= 0) {
               movingBlockMetadata = movingBlock.adjustMetadataForPistonMove(movingBlockMetadata);
               int ejectX = offsetX + Facing.offsetsXForSide[shovelEjectDirection];
               int ejectY = offsetY + Facing.offsetsYForSide[shovelEjectDirection];
               int ejectZ = offsetZ + Facing.offsetsZForSide[shovelEjectDirection];
               this.onShovelEjectIntoBlock(world, ejectX, ejectY, ejectZ);
               world.setBlock(ejectX, ejectY, ejectZ, Block.pistonMoving.blockID, movingBlockMetadata, 4);
               world.setBlockTileEntity(
                  ejectX, ejectY, ejectZ, PistonBlockMoving.getShoveledTileEntity(movingBlockID, movingBlockMetadata, shovelEjectDirection)
               );
            } else {
               movingBlock.onBrokenByPistonPush(world, offsetX, offsetY, offsetZ, movingBlockMetadata);
            }

            world.setBlockToAir(offsetX, offsetY, offsetZ);
            break;
         }
      }

      int previousOffsetX = offsetX;
      int previousOffsetY = offsetY;
      int previousOffsetZ = offsetZ;
      int blockCounter = 0;
      int[] blockIDList = new int[13];

      while (offsetX != x || offsetY != y || offsetZ != z) {
         int movingX = offsetX - Facing.offsetsXForSide[facingTo];
         int movingY = offsetY - Facing.offsetsYForSide[facingTo];
         int movingZ = offsetZ - Facing.offsetsZForSide[facingTo];
         int movingBlockIDx = world.getBlockId(movingX, movingY, movingZ);
         int movingBlockMetadata = world.getBlockMetadata(movingX, movingY, movingZ);
         NBTTagCompound tileEntityData = getBlockTileEntityData(world, movingX, movingY, movingZ);
         world.removeBlockTileEntity(movingX, movingY, movingZ);
         if (movingBlockIDx == this.blockID && movingX == x && movingY == y && movingZ == z) {
            world.setBlock(offsetX, offsetY, offsetZ, Block.pistonMoving.blockID, facingTo | (this.isSticky ? 8 : 0), 4);
            world.setBlockTileEntity(
               offsetX,
               offsetY,
               offsetZ,
               BlockPistonMoving.getTileEntity(Block.pistonExtension.blockID, facingTo | (this.isSticky ? 8 : 0), facingTo, true, false)
            );
         } else {
            if (Block.blocksList[movingBlockIDx] != null) {
               movingBlockMetadata = Block.blocksList[movingBlockIDx].adjustMetadataForPistonMove(movingBlockMetadata);
            }

            world.setBlock(offsetX, offsetY, offsetZ, Block.pistonMoving.blockID, movingBlockMetadata, 4);
            world.setBlockTileEntity(offsetX, offsetY, offsetZ, BlockPistonMoving.getTileEntity(movingBlockIDx, movingBlockMetadata, facingTo, true, false));
            if (tileEntityData != null) {
               ((TileEntityPiston)world.getBlockTileEntity(offsetX, offsetY, offsetZ)).storeTileEntity(tileEntityData);
            }
         }

         blockIDList[blockCounter++] = movingBlockIDx;
         offsetX = movingX;
         offsetY = movingY;
         offsetZ = movingZ;
      }

      offsetX = previousOffsetX;
      offsetY = previousOffsetY;
      offsetZ = previousOffsetZ;
      blockCounter = 0;

      while (offsetX != x || offsetY != y || offsetZ != z) {
         int movingX = offsetX - Facing.offsetsXForSide[facingTo];
         int movingY = offsetY - Facing.offsetsYForSide[facingTo];
         int movingZ = offsetZ - Facing.offsetsZForSide[facingTo];
         world.notifyBlocksOfNeighborChange(movingX, movingY, movingZ, blockIDList[blockCounter++]);
         offsetX = movingX;
         offsetY = movingY;
         offsetZ = movingZ;
      }

      return true;
   }

   @Override
   public void setBlockBoundsBasedOnState(IBlockAccess blockAccess, int i, int j, int k) {
   }

   @Override
   public void setBlockBoundsForItemRender() {
   }

   @Override
   public AxisAlignedBB getBlockBoundsFromPoolBasedOnState(IBlockAccess blockAccess, int i, int j, int k) {
      int iMetadata = blockAccess.getBlockMetadata(i, j, k);
      if (e(iMetadata)) {
         switch (d(iMetadata)) {
            case 0:
               return AxisAlignedBB.getAABBPool().getAABB(0.0, 0.25, 0.0, 1.0, 1.0, 1.0);
            case 1:
               return AxisAlignedBB.getAABBPool().getAABB(0.0, 0.0, 0.0, 1.0, 0.75, 1.0);
            case 2:
               return AxisAlignedBB.getAABBPool().getAABB(0.0, 0.0, 0.25, 1.0, 1.0, 1.0);
            case 3:
               return AxisAlignedBB.getAABBPool().getAABB(0.0, 0.0, 0.0, 1.0, 1.0, 0.75);
            case 4:
               return AxisAlignedBB.getAABBPool().getAABB(0.25, 0.0, 0.0, 1.0, 1.0, 1.0);
            case 5:
               return AxisAlignedBB.getAABBPool().getAABB(0.0, 0.0, 0.0, 0.75, 1.0, 1.0);
         }
      }

      return super.getBlockBoundsFromPoolBasedOnState(blockAccess, i, j, k);
   }

   @Override
   public void addCollisionBoxesToList(World world, int i, int j, int k, AxisAlignedBB intersectingBox, List list, Entity entity) {
      this.getCollisionBoundingBoxFromPool(world, i, j, k).addToListIfIntersects(intersectingBox, list);
   }

   @Override
   public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k) {
      return this.getBlockBoundsFromPoolBasedOnState(world, i, j, k).offset(i, j, k);
   }

   @Override
   public boolean canSupportFallingBlocks(IBlockAccess blockAccess, int i, int j, int k) {
      return true;
   }

   @Override
   public int onBlockPlaced(World world, int i, int j, int k, int iFacing, float fClickX, float fClickY, float fClickZ, int iMetadata) {
      return getOppositeFacing(iFacing);
   }

   @Override
   public int preBlockPlacedBy(World world, int i, int j, int k, int iMetadata, EntityLiving entityBy) {
      int facing = a(world, i, j, k, entityBy);
      if (entityBy.isUsingSpecialKey()) {
         facing = Facing.oppositeSide[facing];
      }

      return facing;
   }

   @Override
   public void onBlockPlacedBy(World world, int i, int j, int k, EntityLiving entityBy, ItemStack stack) {
   }

   @Override
   public void onPostBlockPlaced(World world, int i, int j, int k, int iMetadata) {
      if (!world.isRemote) {
         this.updatePistonState(world, i, j, k);
      }
   }

   protected void validatePistonState(World world, int i, int j, int k) {
      int iMetadata = world.getBlockMetadata(i, j, k);
      if (!e(iMetadata)) {
         int iFacing = d(iMetadata);
         BlockPos targetPos = new BlockPos(i, j, k, iFacing);
         int iTargetBlockID = world.getBlockId(targetPos.x, targetPos.y, targetPos.z);
         if (iTargetBlockID == Block.pistonExtension.blockID) {
            int iTargetMetadata = world.getBlockMetadata(targetPos.x, targetPos.y, targetPos.z);
            if (BlockPistonExtension.getDirectionMeta(iTargetMetadata) == iFacing) {
               world.SetBlockMetadataWithNotify(i, j, k, iMetadata | 8, 2);
            }
         }
      }
   }

   protected int getPistonShovelEjectionDirection(World world, int i, int j, int k, int iToFacing) {
      Block block = Block.blocksList[world.getBlockId(i, j, k)];
      if (block != null && block.canBePistonShoveled(world, i, j, k)) {
         int iOppFacing = Block.getOppositeFacing(iToFacing);
         int iShovelI = i + Facing.offsetsXForSide[iOppFacing];
         int iShovelJ = j + Facing.offsetsYForSide[iOppFacing];
         int iShovelK = k + Facing.offsetsZForSide[iOppFacing];
         Block shovelBlock = Block.blocksList[world.getBlockId(iShovelI, iShovelJ, iShovelK)];
         if (shovelBlock != null) {
            int iShovelEjectDirection = shovelBlock.getPistonShovelEjectDirection(world, iShovelI, iShovelJ, iShovelK, iToFacing);
            if (iShovelEjectDirection >= 0 && this.canShovelEjectToFacing(world, i, j, k, iShovelEjectDirection)) {
               return iShovelEjectDirection;
            }
         }
      }

      return -1;
   }

   protected boolean canShovelEjectToFacing(World world, int i, int j, int k, int iFacing) {
      int iDestI = i + Facing.offsetsXForSide[iFacing];
      int iDestJ = j + Facing.offsetsYForSide[iFacing];
      int iDestK = k + Facing.offsetsZForSide[iFacing];
      Block destBlock = Block.blocksList[world.getBlockId(iDestI, iDestJ, iDestK)];
      return destBlock != null ? destBlock.getMobilityFlag() == 1 : true;
   }

   protected void onShovelEjectIntoBlock(World world, int i, int j, int k) {
      Block block = Block.blocksList[world.getBlockId(i, j, k)];
      if (block != null && block.getMobilityFlag() == 1) {
         block.dropBlockAsItem(world, i, j, k, world.getBlockMetadata(i, j, k), 0);
         world.setBlockToAir(i, j, k);
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public Icon getIcon(int iSide, int iMetadata) {
      int iFacing = d(iMetadata);
      if (iFacing > 5) {
         return this.topIcon;
      } else if (iSide == iFacing) {
         return !isRenderingExtendedBase ? this.topIcon : this.innerTopIcon;
      } else {
         return iSide == getOppositeFacing(iFacing) ? this.bottomIcon : this.blockIcon;
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean renderBlock(RenderBlocks renderer, int i, int j, int k) {
      renderer.setRenderBounds(this.getBlockBoundsFromPoolBasedOnState(renderer.blockAccess, i, j, k));
      return renderer.renderPistonBase(this, i, j, k, false);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean shouldSideBeRendered(IBlockAccess blockAccess, int iNeighborI, int iNeighborJ, int iNeighborK, int iSide) {
      return this.currentBlockRenderer.shouldSideBeRenderedBasedOnCurrentBounds(iNeighborI, iNeighborJ, iNeighborK, iSide);
   }
}
