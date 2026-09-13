package btw.block.blocks;

import btw.block.BTWBlocks;
import btw.item.BTWItems;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.BlockTrapDoor;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Facing;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Icon;
import net.minecraft.src.ItemStack;
import net.minecraft.src.MathHelper;
import net.minecraft.src.MovingObjectPosition;
import net.minecraft.src.PathFinder;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.Vec3;
import net.minecraft.src.World;

public class TrapDoorBlock extends BlockTrapDoor {
   protected static final double THICKNESS = 0.1875;
   protected static final double HALF_THICKNESS = 0.09375;
   private static boolean flagBetterPlaceRule;
   private static boolean betterPlaceRuleIsLower;

   public TrapDoorBlock(int blockID) {
      super(blockID, BTWBlocks.plankMaterial);
      this.c(1.5F);
      this.setAxesEffectiveOn();
      this.setBuoyant();
      this.initBlockBounds(0.0, 0.40625, 0.0, 1.0, 0.59375, 1.0);
      this.a(g);
      this.c("trapdoor");
      this.D();
   }

   @Override
   public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
      int metadata = world.getBlockMetadata(x, y, z);
      world.setBlockMetadataWithNotify(x, y, z, metadata ^ 4);
      world.playAuxSFXAtEntity(player, 1003, x, y, z, 0);
      return true;
   }

   @Override
   public void onNeighborBlockChange(World world, int x, int y, int z, int neighborID) {
   }

   @Override
   public void onPoweredBlockChange(World world, int x, int y, int z, boolean isPowered) {
   }

   @Override
   public boolean canPlaceBlockOnSide(World world, int x, int y, int z, int side) {
      return true;
   }

   @Override
   public int onBlockPlaced(World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int metadata) {
      int newMetadata = 0;
      if (side == 2) {
         newMetadata = 0;
      }

      if (side == 3) {
         newMetadata = 1;
      }

      if (side == 4) {
         newMetadata = 2;
      }

      if (side == 5) {
         newMetadata = 3;
      }

      if (side != 1 && side != 0 && hitY > 0.5F) {
         newMetadata += 8;
      }

      if (side == 0 || side == 1) {
         flagBetterPlaceRule = true;
         betterPlaceRuleIsLower = side == 1;
      }

      return newMetadata;
   }

   @Override
   public void onBlockPlacedBy(World world, int x, int y, int z, EntityLiving entity, ItemStack itemStack) {
      if (flagBetterPlaceRule) {
         int facing = MathHelper.floor_double(entity.rotationYaw * 4.0F / 360.0F + 0.5) & 3;
         int metadataMod = betterPlaceRuleIsLower ? 0 : 8;
         if (facing == 0) {
            world.setBlockMetadataWithNotify(x, y, z, metadataMod + 0, 2);
         }

         if (facing == 1) {
            world.setBlockMetadataWithNotify(x, y, z, metadataMod + 3, 2);
         }

         if (facing == 2) {
            world.setBlockMetadataWithNotify(x, y, z, metadataMod + 1, 2);
         }

         if (facing == 3) {
            world.setBlockMetadataWithNotify(x, y, z, metadataMod + 2, 2);
         }

         flagBetterPlaceRule = false;
      }
   }

   @Override
   public boolean isBlockClimbable(World world, int x, int y, int z) {
      Block blockBelow = Block.blocksList[world.getBlockId(x, y - 1, z)];
      return blockBelow instanceof LadderBlockBase && world.getBlockMetadata(x, y - 1, z) == (world.getBlockMetadata(x, y, z) & 3);
   }

   @Override
   public int getWeightOnPathBlocked(IBlockAccess blockAccess, int x, int y, int z) {
      return -4;
   }

   @Override
   public boolean canPathThroughBlock(IBlockAccess blockAccess, int x, int y, int z, Entity entity, PathFinder pathFinder) {
      return pathFinder.CanPathThroughClosedWoodDoor() || pathFinder.canPathThroughOpenWoodDoor() && this.getBlocksMovement(blockAccess, x, y, z);
   }

   @Override
   public boolean isBreakableBarricade(IBlockAccess blockAccess, int x, int y, int z) {
      return true;
   }

   @Override
   public boolean isBreakableBarricadeOpen(IBlockAccess blockAccess, int x, int y, int z) {
      return f(blockAccess.getBlockMetadata(x, y, z));
   }

   @Override
   public boolean getBlocksMovement(IBlockAccess blockAccess, int x, int y, int z) {
      return f(blockAccess.getBlockMetadata(x, y, z));
   }

   @Override
   public int getHarvestToolLevel(IBlockAccess blockAccess, int x, int y, int z) {
      return 2;
   }

   @Override
   public boolean dropComponentItemsOnBadBreak(World world, int i, int j, int k, int iMetadata, float fChanceOfDrop) {
      this.dropItemsIndividually(world, i, j, k, BTWItems.sawDust.itemID, 2, 0, fChanceOfDrop);
      return true;
   }

   @Override
   public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k) {
      return this.getBlockBoundsFromPoolBasedOnState(world, i, j, k).offset(i, j, k);
   }

   @Override
   public void setBlockBoundsBasedOnState(IBlockAccess blockAccess, int x, int y, int z) {
   }

   @Override
   public void setBlockBoundsForItemRender() {
   }

   @Override
   public AxisAlignedBB getBlockBoundsFromPoolBasedOnState(IBlockAccess blockAccess, int x, int y, int z) {
      int metadata = blockAccess.getBlockMetadata(x, y, z);
      if (f(metadata)) {
         int direction = metadata & 3;
         switch (direction) {
            case 0:
               return AxisAlignedBB.getAABBPool().getAABB(0.0, 0.0, 0.8125, 1.0, 1.0, 1.0);
            case 1:
               return AxisAlignedBB.getAABBPool().getAABB(0.0, 0.0, 0.0, 1.0, 1.0, 0.1875);
            case 2:
               return AxisAlignedBB.getAABBPool().getAABB(0.8125, 0.0, 0.0, 1.0, 1.0, 1.0);
            default:
               return AxisAlignedBB.getAABBPool().getAABB(0.0, 0.0, 0.0, 0.1875, 1.0, 1.0);
         }
      } else {
         return (metadata & 8) != 0
            ? AxisAlignedBB.getAABBPool().getAABB(0.0, 0.8125, 0.0, 1.0, 1.0, 1.0)
            : AxisAlignedBB.getAABBPool().getAABB(0.0, 0.0, 0.0, 1.0, 0.1875, 1.0);
      }
   }

   @Override
   public MovingObjectPosition collisionRayTrace(World world, int x, int y, int z, Vec3 startRay, Vec3 endRay) {
      AxisAlignedBB collisionBox = this.getBlockBoundsFromPoolBasedOnState(world, x, y, z).offset(x, y, z);
      MovingObjectPosition collisionPoint = collisionBox.calculateIntercept(startRay, endRay);
      if (collisionPoint != null) {
         collisionPoint.blockX = x;
         collisionPoint.blockY = y;
         collisionPoint.blockZ = z;
      }

      return collisionPoint;
   }

   @Override
   public boolean canItemPassIfFilter(ItemStack filteredItem) {
      int filterableProperties = filteredItem.getItem().getFilterableProperties(filteredItem);
      return (filterableProperties & 14) != 0;
   }

   @Override
   public boolean getCanGrassGrowUnderBlock(World world, int x, int y, int z, boolean var5) {
      return true;
   }

   @Override
   public boolean hasLargeCenterHardPointToFacing(IBlockAccess blockAccess, int x, int y, int z, int facing, boolean var6) {
      int metadata = blockAccess.getBlockMetadata(x, y, z);
      int facingOpposite = Facing.oppositeSide[facing];
      switch (facingOpposite) {
         case 0:
            return metadata >= 8 && metadata < 12;
         case 1:
            return metadata < 4;
         case 2:
            return metadata == 4 || metadata == 12;
         case 3:
            return metadata == 5 || metadata == 13;
         case 4:
            return metadata == 6 || metadata == 14;
         case 5:
            return metadata == 7 || metadata == 15;
         default:
            return false;
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public Icon getHopperFilterIcon() {
      return this.blockIcon;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z) {
      return this.getBlockBoundsFromPoolBasedOnState(world, x, y, z).offset(x, y, z);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean shouldSideBeRendered(IBlockAccess blockAccess, int neighborX, int neighborY, int neighborZ, int side) {
      return this.currentBlockRenderer.shouldSideBeRenderedBasedOnCurrentBounds(neighborX, neighborY, neighborZ, side);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean renderBlock(RenderBlocks renderBlocks, int x, int y, int z) {
      super.setBlockBoundsForItemRender();
      super.setBlockBoundsBasedOnState(renderBlocks.blockAccess, x, y, z);
      renderBlocks.setRenderBounds(this.getBlockBoundsFromPoolBasedOnState(renderBlocks.blockAccess, x, y, z));
      switch (renderBlocks.blockAccess.getBlockMetadata(x, y, z)) {
         case 0:
         case 8:
            renderBlocks.setUVRotateTop(3);
            renderBlocks.setUVRotateBottom(3);
            break;
         case 1:
         case 9:
            renderBlocks.setUVRotateTop(0);
            renderBlocks.setUVRotateBottom(0);
            break;
         case 2:
         case 10:
         case 14:
         case 15:
            renderBlocks.setUVRotateTop(1);
            renderBlocks.setUVRotateBottom(1);
            break;
         case 3:
         case 11:
            renderBlocks.setUVRotateTop(2);
            renderBlocks.setUVRotateBottom(2);
            break;
         case 4:
         case 5:
            renderBlocks.setUVRotateEast(3);
            renderBlocks.setUVRotateWest(3);
            renderBlocks.setUVRotateNorth(1);
            renderBlocks.setUVRotateSouth(1);
            renderBlocks.setUVRotateTop(1);
            renderBlocks.setUVRotateBottom(1);
            break;
         case 6:
         case 7:
            renderBlocks.setUVRotateEast(1);
            renderBlocks.setUVRotateWest(1);
            renderBlocks.setUVRotateNorth(3);
            renderBlocks.setUVRotateSouth(3);
            renderBlocks.setUVRotateTop(1);
            renderBlocks.setUVRotateBottom(1);
         case 12:
         case 13:
      }

      renderBlocks.renderStandardBlock(this, x, y, z);
      renderBlocks.clearUVRotation();
      return true;
   }
}
