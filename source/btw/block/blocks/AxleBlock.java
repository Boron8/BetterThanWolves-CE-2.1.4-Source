package btw.block.blocks;

import btw.block.BTWBlocks;
import btw.block.MechanicalBlock;
import btw.block.util.MechPowerUtils;
import btw.client.render.util.RenderUtils;
import btw.item.BTWItems;
import btw.world.util.BlockPos;
import java.util.Random;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Icon;
import net.minecraft.src.IconRegister;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.World;

public class AxleBlock extends Block {
   protected static final double AXLE_WIDTH = 0.25;
   protected static final double AXLE_HALF_WIDTH = 0.125;
   public static final int AXLE_TICK_RATE = 1;
   protected static final int[][] axleFacingsForAlignment = new int[][]{{0, 1}, {2, 3}, {4, 5}};
   @Environment(EnvType.CLIENT)
   public Icon iconSide;
   @Environment(EnvType.CLIENT)
   public Icon iconSideOn;
   @Environment(EnvType.CLIENT)
   public Icon iconSideOnOverpowered;
   @Environment(EnvType.CLIENT)
   public boolean isPowerOnForCurrentRender;
   @Environment(EnvType.CLIENT)
   public boolean isOverpoweredForCurrentRender;

   public AxleBlock(int iBlockID) {
      super(iBlockID, BTWBlocks.plankMaterial);
      this.c(2.0F);
      this.setAxesEffectiveOn(true);
      this.setBuoyancy(1.0F);
      this.initBlockBounds(0.375, 0.375, 0.0, 0.625, 0.625, 1.0);
      this.a(g);
      this.c("fcBlockAxle");
      this.a(CreativeTabs.tabRedstone);
   }

   @Override
   public int tickRate(World world) {
      return 1;
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
      return this.setAxisAlignmentInMetadataBasedOnFacing(iMetadata, iFacing);
   }

   @Override
   public void onBlockAdded(World world, int i, int j, int k) {
      super.onBlockAdded(world, i, j, k);
      world.scheduleBlockUpdate(i, j, k, this.blockID, this.tickRate(world));
   }

   @Override
   public void updateTick(World world, int i, int j, int k, Random random) {
      this.setPowerLevel(world, i, j, k, 0);
      this.validatePowerLevel(world, i, j, k);
   }

   @Override
   public AxisAlignedBB getBlockBoundsFromPoolBasedOnState(IBlockAccess blockAccess, int i, int j, int k) {
      int iAxis = this.getAxisAlignment(blockAccess, i, j, k);
      switch (iAxis) {
         case 0:
            return AxisAlignedBB.getAABBPool().getAABB(0.375, 0.0, 0.375, 0.625, 1.0, 0.625);
         case 1:
            return AxisAlignedBB.getAABBPool().getAABB(0.375, 0.375, 0.0, 0.625, 0.625, 1.0);
         default:
            return AxisAlignedBB.getAABBPool().getAABB(0.0, 0.375, 0.375, 1.0, 0.625, 0.625);
      }
   }

   @Override
   public void onNeighborBlockChange(World world, int i, int j, int k, int iBlockID) {
      this.validatePowerLevel(world, i, j, k);
   }

   @Override
   public int getMobilityFlag() {
      return 1;
   }

   @Override
   public boolean hasCenterHardPointToFacing(IBlockAccess blockAccess, int i, int j, int k, int iFacing, boolean bIgnoreTransparency) {
      return this.isAxleOrientedTowardsFacing(blockAccess, i, j, k, iFacing);
   }

   @Override
   public int getMechanicalPowerLevelProvidedToAxleAtFacing(World world, int i, int j, int k, int iFacing) {
      int iAlignment = this.getAxisAlignment(world, i, j, k);
      return iFacing >> 1 == iAlignment ? this.getPowerLevel(world, i, j, k) : 0;
   }

   @Override
   public int getHarvestToolLevel(IBlockAccess blockAccess, int i, int j, int k) {
      return 2;
   }

   @Override
   public boolean dropComponentItemsOnBadBreak(World world, int i, int j, int k, int iMetadata, float fChanceOfDrop) {
      this.dropItemsIndividually(world, i, j, k, BTWItems.hempFibers.itemID, 4, 0, fChanceOfDrop);
      this.dropItemsIndividually(world, i, j, k, BTWItems.sawDust.itemID, 2, 0, fChanceOfDrop);
      return true;
   }

   @Override
   public int getFacing(int iMetadata) {
      return this.getAxisAlignmentFromMetadata(iMetadata) << 1;
   }

   @Override
   public int setFacing(int iMetadata, int iFacing) {
      return this.setAxisAlignmentInMetadataBasedOnFacing(iMetadata, iFacing);
   }

   @Override
   public boolean toggleFacing(World world, int i, int j, int k, boolean bReverse) {
      int iAxisAlignment = this.getAxisAlignment(world, i, j, k);
      if (!bReverse) {
         if (++iAxisAlignment > 2) {
            iAxisAlignment = 0;
         }
      } else if (--iAxisAlignment < 0) {
         iAxisAlignment = 2;
      }

      this.setAxisAlignment(world, i, j, k, iAxisAlignment);
      world.markBlockRangeForRenderUpdate(i, j, k, i, j, k);
      this.setPowerLevel(world, i, j, k, 0);
      this.validatePowerLevel(world, i, j, k);
      world.markBlockForUpdate(i, j, k);
      return true;
   }

   @Override
   public boolean canGroundCoverRestOnBlock(World world, int i, int j, int k) {
      return world.doesBlockHaveSolidTopSurface(i, j - 1, k);
   }

   @Override
   public float groundCoverRestingOnVisualOffset(IBlockAccess blockAccess, int i, int j, int k) {
      return -1.0F;
   }

   public int getAxisAlignment(IBlockAccess iBlockAccess, int i, int j, int k) {
      return iBlockAccess.getBlockMetadata(i, j, k) >> 2;
   }

   public void setAxisAlignment(World world, int i, int j, int k, int iAxisAlignment) {
      int iMetaData = world.getBlockMetadata(i, j, k) & 3;
      iMetaData |= iAxisAlignment << 2;
      world.setBlockMetadataWithNotify(i, j, k, iMetaData);
   }

   public int getAxisAlignmentFromMetadata(int iMetadata) {
      return iMetadata >> 2;
   }

   public void setAxisAlignmentBasedOnFacing(World world, int i, int j, int k, int iFacing) {
      int iAxis;
      switch (iFacing) {
         case 0:
         case 1:
            iAxis = 0;
            break;
         case 2:
         case 3:
            iAxis = 1;
            break;
         default:
            iAxis = 2;
      }

      int iMetaData = world.getBlockMetadata(i, j, k) & 3;
      iMetaData |= iAxis << 2;
      world.setBlockMetadataWithNotify(i, j, k, iMetaData);
   }

   public int setAxisAlignmentInMetadataBasedOnFacing(int iMetadata, int iFacing) {
      int iAxis;
      switch (iFacing) {
         case 0:
         case 1:
            iAxis = 0;
            break;
         case 2:
         case 3:
            iAxis = 1;
            break;
         default:
            iAxis = 2;
      }

      iMetadata &= 3;
      return iMetadata | iAxis << 2;
   }

   public int getPowerLevel(IBlockAccess iBlockAccess, int i, int j, int k) {
      return this.getPowerLevelFromMetadata(iBlockAccess.getBlockMetadata(i, j, k));
   }

   public int getPowerLevelFromMetadata(int iMetadata) {
      return iMetadata & 3;
   }

   public void setPowerLevel(World world, int i, int j, int k, int iPowerLevel) {
      int iMetadata = world.getBlockMetadata(i, j, k);
      iMetadata = this.setPowerLevelInMetadata(iMetadata, iPowerLevel);
      world.setBlockMetadataWithNotifyNoClient(i, j, k, iMetadata);
   }

   public int setPowerLevelInMetadata(int iMetadata, int iPowerLevel) {
      iPowerLevel &= 3;
      iMetadata &= 12;
      return iMetadata | iPowerLevel;
   }

   public void setPowerLevelWithoutNotify(World world, int i, int j, int k, int iPowerLevel) {
      int iMetadata = world.getBlockMetadata(i, j, k);
      iMetadata = this.setPowerLevelInMetadata(iMetadata, iPowerLevel);
      world.setBlockMetadata(i, j, k, iMetadata);
   }

   public boolean isAxleOrientedTowardsFacing(IBlockAccess iBlockAccess, int i, int j, int k, int iFacing) {
      int iAxis = this.getAxisAlignment(iBlockAccess, i, j, k);
      switch (iAxis) {
         case 0:
            if (iFacing == 0 || iFacing == 1) {
               return true;
            }
            break;
         case 1:
            if (iFacing == 2 || iFacing == 3) {
               return true;
            }
            break;
         default:
            if (iFacing == 4 || iFacing == 5) {
               return true;
            }
      }

      return false;
   }

   public void breakAxle(World world, int i, int j, int k) {
      if (world.getBlockId(i, j, k) == this.blockID) {
         this.dropComponentItemsOnBadBreak(world, i, j, k, world.getBlockMetadata(i, j, k), 1.0F);
         world.playAuxSFX(2235, i, j, k, 0);
         world.setBlockWithNotify(i, j, k, 0);
      }
   }

   protected void validatePowerLevel(World world, int i, int j, int k) {
      int iCurrentPower = this.getPowerLevel(world, i, j, k);
      int iAxis = this.getAxisAlignment(world, i, j, k);
      int iMaxNeighborPower = 0;
      int iGreaterPowerNeighbors = 0;

      for (int iTempSourceIndex = 0; iTempSourceIndex < 2; iTempSourceIndex++) {
         int iTempFacing = axleFacingsForAlignment[iAxis][iTempSourceIndex];
         BlockPos tempSourcePos = new BlockPos(i, j, k, iTempFacing);
         int iTempBlockID = world.getBlockId(tempSourcePos.x, tempSourcePos.y, tempSourcePos.z);
         if (iTempBlockID != 0) {
            Block tempBlock = Block.blocksList[iTempBlockID];
            int iTempPowerLevel = tempBlock.getMechanicalPowerLevelProvidedToAxleAtFacing(
               world, tempSourcePos.x, tempSourcePos.y, tempSourcePos.z, Block.getOppositeFacing(iTempFacing)
            );
            if (iTempPowerLevel > iMaxNeighborPower) {
               iMaxNeighborPower = iTempPowerLevel;
            }

            if (iTempPowerLevel > iCurrentPower) {
               iGreaterPowerNeighbors++;
            }
         }
      }

      if (iGreaterPowerNeighbors >= 2) {
         this.breakAxle(world, i, j, k);
      } else {
         int var15;
         if (iMaxNeighborPower > iCurrentPower) {
            if (iMaxNeighborPower == 1) {
               this.breakAxle(world, i, j, k);
               return;
            }

            var15 = iMaxNeighborPower - 1;
         } else {
            var15 = 0;
         }

         if (var15 != iCurrentPower) {
            this.setPowerLevel(world, i, j, k, var15);
         }
      }
   }

   private void emitAxleParticles(World world, int i, int j, int k, Random random) {
      for (int counter = 0; counter < 2; counter++) {
         float smokeX = i + random.nextFloat();
         float smokeY = j + random.nextFloat() * 0.5F + 0.625F;
         float smokeZ = k + random.nextFloat();
         world.spawnParticle("smoke", smokeX, smokeY, smokeZ, 0.0, 0.0, 0.0);
      }
   }

   public void overpower(World world, int i, int j, int k) {
      int iAxis = this.getAxisAlignment(world, i, j, k);
      switch (iAxis) {
         case 0:
            this.overpowerBlockToFacing(world, i, j, k, iAxis, 0);
            this.overpowerBlockToFacing(world, i, j, k, iAxis, 1);
            break;
         case 1:
            this.overpowerBlockToFacing(world, i, j, k, iAxis, 2);
            this.overpowerBlockToFacing(world, i, j, k, iAxis, 3);
            break;
         default:
            this.overpowerBlockToFacing(world, i, j, k, iAxis, 4);
            this.overpowerBlockToFacing(world, i, j, k, iAxis, 5);
      }
   }

   private void overpowerBlockToFacing(World world, int i, int j, int k, int iSourceAxis, int iFacing) {
      BlockPos targetPos = new BlockPos(i, j, k);
      targetPos.addFacingAsOffset(iFacing);
      int iTempBlockID = world.getBlockId(targetPos.x, targetPos.y, targetPos.z);
      if (iTempBlockID == BTWBlocks.axle.blockID || iTempBlockID == BTWBlocks.axlePowerSource.blockID) {
         int iTempAxis = this.getAxisAlignment(world, targetPos.x, targetPos.y, targetPos.z);
         if (iTempAxis == iSourceAxis) {
            this.overpowerBlockToFacing(world, targetPos.x, targetPos.y, targetPos.z, iSourceAxis, iFacing);
         }
      } else if (Block.blocksList[iTempBlockID] instanceof MechanicalBlock) {
         MechanicalBlock mechDevice = (MechanicalBlock)Block.blocksList[iTempBlockID];
         if (mechDevice.canInputAxlePowerToFacing(world, targetPos.x, targetPos.y, targetPos.z, Block.getOppositeFacing(iFacing))) {
            mechDevice.overpower(world, targetPos.x, targetPos.y, targetPos.z);
         }
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void registerIcons(IconRegister register) {
      this.blockIcon = register.registerIcon("fcBlockAxle_end");
      this.iconSide = register.registerIcon("fcBlockAxle_side");
      this.iconSideOn = register.registerIcon("fcBlockAxle_side_on");
      this.iconSideOnOverpowered = register.registerIcon("fcBlockAxle_side_on_fast");
   }

   @Environment(EnvType.CLIENT)
   @Override
   public Icon getIcon(int iSide, int iMetadata) {
      return iSide != 2 && iSide != 3 ? this.iconSide : this.blockIcon;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public Icon getBlockTexture(IBlockAccess blockAccess, int i, int j, int k, int iSide) {
      int iAxisAlignment = this.getAxisAlignment(blockAccess, i, j, k);
      if (iAxisAlignment == 0) {
         if (iSide >= 2) {
            return this.getAxleSideTextureForOnState(this.isPowerOnForCurrentRender);
         }
      } else if (iAxisAlignment == 1) {
         if (iSide != 2 && iSide != 3) {
            return this.getAxleSideTextureForOnState(this.isPowerOnForCurrentRender);
         }
      } else if (iSide < 4) {
         return this.getAxleSideTextureForOnState(this.isPowerOnForCurrentRender);
      }

      return this.blockIcon;
   }

   @Environment(EnvType.CLIENT)
   public Icon getAxleSideTextureForOnState(boolean bIsOn) {
      return bIsOn ? this.iconSideOn : this.iconSide;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void randomDisplayTick(World world, int i, int j, int k, Random random) {
      if (this.clientCheckIfPowered(world, i, j, k)) {
         this.emitAxleParticles(world, i, j, k, random);
         if (random.nextInt(200) == 0) {
            world.playSound(i + 0.5, j + 0.5, k + 0.5, "random.chestopen", 0.075F, random.nextFloat() * 0.1F + 0.5F);
         }
      }
   }

   @Environment(EnvType.CLIENT)
   public boolean clientCheckIfPowered(IBlockAccess blockAccess, int i, int j, int k) {
      int iCurrentPower = this.getPowerLevel(blockAccess, i, j, k);
      int iAxis = this.getAxisAlignment(blockAccess, i, j, k);

      for (int iTempFacingIndex = 0; iTempFacingIndex < 2; iTempFacingIndex++) {
         BlockPos targetPos = new BlockPos(i, j, k);
         int iFacingOfCheck = axleFacingsForAlignment[iAxis][iTempFacingIndex];

         for (int iTempDistance = 1; iTempDistance <= 3; iTempDistance++) {
            targetPos.addFacingAsOffset(iFacingOfCheck);
            int iTempBlockID = blockAccess.getBlockId(targetPos.x, targetPos.y, targetPos.z);
            if (iTempBlockID != this.blockID || this.getAxisAlignment(blockAccess, targetPos.x, targetPos.y, targetPos.z) != iAxis) {
               if (iTempBlockID == BTWBlocks.axlePowerSource.blockID && this.getAxisAlignment(blockAccess, targetPos.x, targetPos.y, targetPos.z) == iAxis) {
                  return true;
               }

               if (MechPowerUtils.isPoweredGearBox(blockAccess, targetPos.x, targetPos.y, targetPos.z)) {
                  return true;
               }
               break;
            }
         }
      }

      return false;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean shouldSideBeRendered(IBlockAccess blockAccess, int iNeighborI, int iNeighborJ, int iNeighborK, int iSide) {
      BlockPos myPos = new BlockPos(iNeighborI, iNeighborJ, iNeighborK, getOppositeFacing(iSide));
      if (this.isAxleOrientedTowardsFacing(blockAccess, myPos.x, myPos.y, myPos.z, iSide)) {
         int iNeighborBlockID = blockAccess.getBlockId(iNeighborI, iNeighborJ, iNeighborK);
         if (iNeighborBlockID != this.blockID) {
            return RenderUtils.shouldRenderNeighborFullFaceSide(blockAccess, iNeighborI, iNeighborJ, iNeighborK, iSide);
         }

         if (this.getAxisAlignment(blockAccess, myPos.x, myPos.y, myPos.z) == this.getAxisAlignment(blockAccess, iNeighborI, iNeighborJ, iNeighborK)) {
            return false;
         }
      }

      return true;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void clientBreakBlock(World world, int i, int j, int k, int iBlockID, int iMetadata) {
      world.markBlockRangeForRenderUpdate(i - 3, j - 3, k - 3, i + 3, j + 3, k + 3);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void clientBlockAdded(World world, int i, int j, int k) {
      world.markBlockRangeForRenderUpdate(i - 3, j - 3, k - 3, i + 3, j + 3, k + 3);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean renderBlock(RenderBlocks renderer, int i, int j, int k) {
      IBlockAccess blockAccess = renderer.blockAccess;
      int iAlignment = this.getAxisAlignment(blockAccess, i, j, k);
      if (iAlignment == 0) {
         renderer.setUVRotateEast(1);
         renderer.setUVRotateWest(1);
         renderer.setUVRotateSouth(1);
         renderer.setUVRotateNorth(1);
         renderer.setUVRotateTop(0);
         renderer.setUVRotateBottom(0);
      } else if (iAlignment == 1) {
         renderer.setUVRotateEast(0);
         renderer.setUVRotateWest(0);
         renderer.setUVRotateSouth(0);
         renderer.setUVRotateNorth(3);
         renderer.setUVRotateTop(2);
         renderer.setUVRotateBottom(2);
      } else {
         renderer.setUVRotateEast(0);
         renderer.setUVRotateWest(3);
         renderer.setUVRotateSouth(0);
         renderer.setUVRotateNorth(0);
         renderer.setUVRotateTop(3);
         renderer.setUVRotateBottom(0);
      }

      if (this.clientCheckIfPowered(blockAccess, i, j, k)) {
         this.isPowerOnForCurrentRender = true;
      } else {
         this.isPowerOnForCurrentRender = false;
      }

      renderer.setRenderBounds(this.getBlockBoundsFromPoolBasedOnState(renderer.blockAccess, i, j, k));
      renderer.renderStandardBlock(this, i, j, k);
      renderer.clearUVRotation();
      return true;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void renderBlockAsItem(RenderBlocks renderer, int iItemDamage, float fBrightness) {
      renderer.setRenderBounds(this.getBlockBoundsFromPoolForItemRender(iItemDamage));
      renderer.setUVRotateEast(0);
      renderer.setUVRotateWest(0);
      renderer.setUVRotateSouth(0);
      renderer.setUVRotateNorth(3);
      renderer.setUVRotateTop(2);
      renderer.setUVRotateBottom(2);
      RenderUtils.renderInvBlockWithMetadata(renderer, this, -0.5F, -0.5F, -0.5F, iItemDamage);
      renderer.clearUVRotation();
   }
}
