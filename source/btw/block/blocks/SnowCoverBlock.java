package btw.block.blocks;

import btw.BTWMod;
import btw.block.util.RayTraceUtils;
import btw.item.util.ItemUtils;
import btw.world.util.BlockPos;
import java.util.Random;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.BlockFluid;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EnumSkyBlock;
import net.minecraft.src.Explosion;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.IconRegister;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.MovingObjectPosition;
import net.minecraft.src.StatList;
import net.minecraft.src.Vec3;
import net.minecraft.src.World;

public class SnowCoverBlock extends GroundCoverBlock {
   public SnowCoverBlock(int iBlockID) {
      super(iBlockID, Material.snow);
      this.b(true);
      this.a(o);
      this.c("snow");
   }

   @Override
   public void onEntityCollidedWithBlock(World world, int i, int j, int k, Entity entity) {
      if (BTWMod.enableSnowRework && entity.isAffectedByMovementModifiers() && entity.onGround) {
         double snowheight = world.getBlockMetadata(i, j, k);
         entity.motionX *= 1.0 - 0.025 * snowheight;
         entity.motionZ *= 1.0 - 0.025 * snowheight;
      }
   }

   @Override
   public boolean canPlaceBlockAt(World world, int i, int j, int k) {
      int iBlockBelowID = world.getBlockId(i, j - 1, k);
      Block blockBelow = Block.blocksList[iBlockBelowID];
      return blockBelow != null && blockBelow.getIsBlockWarm(world, i, j - 1, k) ? false : super.canPlaceBlockAt(world, i, j, k);
   }

   @Override
   public void harvestBlock(World world, EntityPlayer player, int i, int j, int k, int iMetadata) {
      int iItemID = Item.snowball.itemID;
      int amount = iMetadata / 2 + 1;
      this.b(world, i, j, k, new ItemStack(iItemID, amount, 0));
      world.setBlockToAir(i, j, k);
      player.addStat(StatList.mineBlockStatArray[this.blockID], 1);
   }

   @Override
   public int idDropped(int par1, Random par2Random, int par3) {
      return Item.snowball.itemID;
   }

   @Override
   public int quantityDropped(Random par1Random) {
      return 1;
   }

   @Override
   public boolean canDropFromExplosion(Explosion explosion) {
      return false;
   }

   @Override
   public void onBlockDestroyedWithImproperTool(World world, EntityPlayer player, int i, int j, int k, int metadata) {
      if (!BTWMod.enableSnowRework) {
         metadata = 0;
      }

      this.c(world, i, j, k, metadata, 0);
   }

   @Override
   public boolean canConvertBlock(ItemStack stack, World world, int i, int j, int k) {
      return BTWMod.enableSnowRework;
   }

   @Override
   public boolean convertBlock(ItemStack stack, World world, int i, int j, int k, int iFromSide) {
      int iMetadata = world.getBlockMetadata(i, j, k);
      if (!world.isRemote && iMetadata > 1) {
         world.setBlockMetadataWithNotify(i, j, k, iMetadata - 2);
         world.playAuxSFX(2001, i, j, k, this.blockID);
         ItemUtils.ejectStackFromBlockTowardsFacing(world, i, j, k, new ItemStack(Item.snowball, 1), iFromSide);
      } else if (!world.isRemote) {
         world.setBlockToAir(i, j, k);
         ItemUtils.ejectStackFromBlockTowardsFacing(world, i, j, k, new ItemStack(Item.snowball, 1), iFromSide);
      }

      return true;
   }

   @Override
   public void updateTick(World world, int i, int j, int k, Random rand) {
      int metadata = world.getBlockMetadata(i, j, k);
      if (world.getSavedLightValue(EnumSkyBlock.Block, i, j, k) > 11) {
         if (metadata == 0) {
            world.setBlockToAir(i, j, k);
         } else {
            world.setBlockMetadata(i, j, k, --metadata);
         }
      } else if (world.isSnowingAtPos(i, j, k) && metadata < 7) {
         int onfloorwithoffset = 0;
         int floorblockID = world.getBlockId(i, j - 1, k);
         if (floorblockID != 0) {
            float flooroffset = r[floorblockID].groundCoverRestingOnVisualOffset(world, i, j - 1, k);
            if (flooroffset < 0.0F) {
               onfloorwithoffset = 1;
            }
         }

         int lowestside = 7;
         int steepness = 2;
         boolean sidegrew = false;

         for (int side = 2; side < 6; side++) {
            BlockPos sideblock = new BlockPos(i, j - onfloorwithoffset, k, Block.getOppositeFacing(side));
            int sideblockID = world.getBlockId(sideblock.x, sideblock.y, sideblock.z);
            if (sideblockID != 0) {
               float offset = r[sideblockID].groundCoverRestingOnVisualOffset(world, sideblock.x, sideblock.y, sideblock.z);
               if (offset < 0.0F) {
                  sideblock.y++;
                  sideblockID = world.getBlockId(sideblock.x, sideblock.y, sideblock.z);
               }
            }

            if (sideblockID == Block.snow.blockID) {
               int sidemetadata = world.getBlockMetadata(sideblock.x, sideblock.y, sideblock.z);
               if (sidemetadata <= metadata - steepness) {
                  world.scheduleBlockUpdate(sideblock.x, sideblock.y, sideblock.z, world.getBlockId(sideblock.x, sideblock.y, sideblock.z), this.a(world));
                  sidegrew = true;
               }

               int sidelimiter = sidemetadata + steepness;
               if (sidelimiter < lowestside) {
                  lowestside = sidelimiter;
               }
            } else if (sideblockID == 0 || !world.isBlockOpaqueCube(sideblock.x, sideblock.y, sideblock.z)) {
               lowestside = 1;
               break;
            }
         }

         if (!sidegrew && metadata < lowestside) {
            world.setBlockMetadataWithNotify(i, j, k, ++metadata);
         }
      }
   }

   @Override
   public void onFluidFlowIntoBlock(World world, int i, int j, int k, BlockFluid newBlock) {
   }

   @Override
   public boolean getCanBeSetOnFireDirectly(IBlockAccess blockAccess, int i, int j, int k) {
      return true;
   }

   @Override
   public boolean getCanBeSetOnFireDirectlyByItem(IBlockAccess blockAccess, int i, int j, int k) {
      return false;
   }

   @Override
   public boolean setOnFireDirectly(World world, int i, int j, int k) {
      world.setBlockToAir(i, j, k);
      return true;
   }

   @Override
   public int getChanceOfFireSpreadingDirectlyTo(IBlockAccess blockAccess, int i, int j, int k) {
      return 60;
   }

   @Override
   public void onBrokenByPistonPush(World world, int i, int j, int k, int iMetadata) {
   }

   public static boolean canSnowCoverReplaceBlock(World world, int i, int j, int k) {
      Block block = Block.blocksList[world.getBlockId(i, j, k)];
      return block == null || block.isAirBlock() || block.isGroundCover() && block != Block.snow;
   }

   public boolean isMaxSnowHeightLimited(World world, int i, int j, int k) {
      int validside = 0;

      for (int side = 2; side < 6; side++) {
         BlockPos sideblock = new BlockPos(i, j, k, Block.getOppositeFacing(side));
         int sideblockID = world.getBlockId(sideblock.x, sideblock.y, sideblock.z);
         if (sideblockID != 0 && r[sideblockID].groundCoverRestingOnVisualOffset(world, sideblock.x, sideblock.y, sideblock.z) < 0.0F) {
         }

         sideblock.y++;
         sideblockID = world.getBlockId(sideblock.x, sideblock.y, sideblock.z);
         if (sideblockID != Block.snow.blockID
            && !world.canSnowAt(sideblock.x, sideblock.y, sideblock.z)
            && !world.isBlockOpaqueCube(sideblock.x, sideblock.y, sideblock.z)) {
            return false;
         }

         validside++;
      }

      return validside < 4;
   }

   public void raiseSnowOnSides(World world, int i, int j, int k) {
      for (int side = 2; side < 6; side++) {
         BlockPos sideblock = new BlockPos(i, j, k, Block.getOppositeFacing(side));
         int sideblockID = world.getBlockId(sideblock.x, sideblock.y, sideblock.z);
         if (sideblockID != 0 && r[sideblockID].groundCoverRestingOnVisualOffset(world, sideblock.x, sideblock.y, sideblock.z) < 0.0F) {
         }

         sideblock.y++;
         sideblockID = world.getBlockId(sideblock.x, sideblock.y, sideblock.z);
         if (sideblockID != Block.snow.blockID
            && !world.canSnowAt(sideblock.x, sideblock.y, sideblock.z)
            && world.isBlockOpaqueCube(sideblock.x, sideblock.y, sideblock.z)) {
         }
      }
   }

   @Override
   public MovingObjectPosition collisionRayTrace(World world, int i, int j, int k, Vec3 startRay, Vec3 endRay) {
      float fVisualOffset = 0.0F;
      int iBlockBelowID = world.getBlockId(i, j - 1, k);
      Block blockBelow = Block.blocksList[iBlockBelowID];
      if (blockBelow != null) {
         fVisualOffset = blockBelow.groundCoverRestingOnVisualOffset(world, i, j - 1, k);
      }

      RayTraceUtils rayTrace = new RayTraceUtils(world, i, j, k, startRay, endRay);
      rayTrace.addBoxWithLocalCoordsToIntersectionList(0.0, fVisualOffset, 0.0, 1.0, this.getSnowHeight(world, i, j, k) + fVisualOffset, 1.0);
      return rayTrace.getFirstIntersection();
   }

   public float getSnowHeight(IBlockAccess blockAccess, int i, int j, int k) {
      return BTWMod.enableSnowRework ? (blockAccess.getBlockMetadata(i, j, k) + 1) * 0.125F : 0.125F;
   }

   @Override
   public AxisAlignedBB getBlockBoundsFromPoolBasedOnState(IBlockAccess blockAccess, int i, int j, int k) {
      float snowheight = this.getSnowHeight(blockAccess, i, j, k);
      return AxisAlignedBB.getAABBPool().getAABB(0.0, 0.0, 0.0, 1.0, snowheight, 1.0);
   }

   @Override
   public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int i, int j, int k) {
      float fVisualOffset = 0.0F;
      int iBlockBelowID = world.getBlockId(i, j - 1, k);
      Block blockBelow = Block.blocksList[iBlockBelowID];
      if (blockBelow != null) {
         fVisualOffset = blockBelow.groundCoverRestingOnVisualOffset(world, i, j - 1, k);
      }

      return this.getBlockBoundsFromPoolBasedOnState(world, i, j, k).offset(i, j + fVisualOffset, k);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void registerIcons(IconRegister register) {
      this.blockIcon = register.registerIcon("snow");
   }
}
