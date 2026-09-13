package btw.block.blocks;

import btw.block.BTWBlocks;
import btw.block.MechanicalBlock;
import btw.block.util.MechPowerUtils;
import btw.item.BTWItems;
import btw.item.util.ItemUtils;
import btw.util.MiscUtils;
import btw.world.util.BlockPos;
import java.util.List;
import java.util.Random;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityItem;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Icon;
import net.minecraft.src.IconRegister;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.Vec3;
import net.minecraft.src.World;

public class BellowsBlock extends Block implements MechanicalBlock {
   private static final int BELLOWS_TICK_RATE = 35;
   public static final float BELLOWS_CONTRACTED_HEIGHT = 0.6875F;
   private static final double BLOW_ITEM_STRENGTH = 0.2;
   private static final double PARTICLE_SPEED = 0.1F;
   @Environment(EnvType.CLIENT)
   private Icon[] iconBySideArray = new Icon[6];
   @Environment(EnvType.CLIENT)
   private Icon iconFront;

   public BellowsBlock(int iBlockID) {
      super(iBlockID, Material.wood);
      this.c(2.0F);
      this.setAxesEffectiveOn(true);
      this.setBuoyancy(1.0F);
      this.initBlockBounds(0.0, 0.0, 0.0, 1.0, 0.6875, 1.0);
      this.a(g);
      this.c("fcBlockBellows");
      this.b(true);
      this.a(CreativeTabs.tabRedstone);
   }

   @Override
   public int tickRate(World world) {
      return 35;
   }

   @Override
   public int onBlockPlaced(World world, int i, int j, int k, int iFacing, float fClickX, float fClickY, float fClickZ, int iMetadata) {
      if (iFacing < 2) {
         iFacing = 2;
      }

      return this.setFacing(iMetadata, iFacing);
   }

   @Override
   public void onBlockPlacedBy(World world, int i, int j, int k, EntityLiving entityLiving, ItemStack stack) {
      int iFacing = MiscUtils.convertOrientationToFlatBlockFacingReversed(entityLiving);
      this.setFacing(world, i, j, k, iFacing);
   }

   @Override
   public void onBlockAdded(World world, int i, int j, int k) {
      super.onBlockAdded(world, i, j, k);
      world.scheduleBlockUpdate(i, j, k, this.blockID, this.tickRate(world));
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
   public AxisAlignedBB getBlockBoundsFromPoolBasedOnState(IBlockAccess blockAccess, int i, int j, int k) {
      return this.isBlockMechanicalOn(blockAccess, i, j, k)
         ? AxisAlignedBB.getAABBPool().getAABB(0.0, 0.0, 0.0, 1.0, 0.6875, 1.0)
         : AxisAlignedBB.getAABBPool().getAABB(0.0, 0.0, 0.0, 1.0, 1.0, 1.0);
   }

   @Override
   public void onNeighborBlockChange(World world, int i, int j, int k, int iNeighborBlockID) {
      boolean bUpdateAlreadyScheduled = world.isUpdateScheduledForBlock(i, j, k, this.blockID);
      if (!bUpdateAlreadyScheduled) {
         if (!this.isCurrentStateValid(world, i, j, k) && !world.isUpdatePendingThisTickForBlock(i, j, k, this.blockID)) {
            world.scheduleBlockUpdate(i, j, k, this.blockID, this.tickRate(world));
            this.setIsContinuousMechanicalStateChange(world, i, j, k, true);
         }
      } else {
         boolean bContinuousChange = this.isContinuousMechanicalStateChange(world, i, j, k);
         if (bContinuousChange && this.isCurrentStateValid(world, i, j, k)) {
            this.setIsContinuousMechanicalStateChange(world, i, j, k, false);
         }
      }
   }

   @Override
   public void updateTick(World world, int i, int j, int k, Random random) {
      boolean bReceivingMechanicalPower = this.isInputtingMechanicalPower(world, i, j, k);
      boolean bMechanicalOn = this.isBlockMechanicalOn(world, i, j, k);
      boolean bContinuousChange = this.isContinuousMechanicalStateChange(world, i, j, k);
      if (bMechanicalOn != bReceivingMechanicalPower) {
         if (bContinuousChange) {
            this.setIsContinuousMechanicalStateChange(world, i, j, k, false);
            this.setBlockMechanicalOn(world, i, j, k, bReceivingMechanicalPower);
            if (bReceivingMechanicalPower) {
               this.blow(world, i, j, k);
            } else {
               this.liftCollidingEntities(world, i, j, k);
            }
         } else {
            world.scheduleBlockUpdate(i, j, k, this.blockID, this.tickRate(world));
            this.setIsContinuousMechanicalStateChange(world, i, j, k, true);
         }
      } else if (bContinuousChange) {
         this.setIsContinuousMechanicalStateChange(world, i, j, k, false);
      }
   }

   @Override
   public void randomUpdateTick(World world, int i, int j, int k, Random rand) {
      if (!this.isCurrentStateValid(world, i, j, k) && !world.isUpdateScheduledForBlock(i, j, k, this.blockID)) {
         world.scheduleBlockUpdate(i, j, k, this.blockID, this.tickRate(world));
         this.setIsContinuousMechanicalStateChange(world, i, j, k, true);
      }
   }

   @Override
   public int getFacing(int iMetadata) {
      return (iMetadata & 3) + 2;
   }

   @Override
   public int setFacing(int iMetadata, int iFacing) {
      iMetadata &= -4;
      if (iFacing >= 2) {
         iFacing -= 2;
      } else {
         iFacing = 0;
      }

      return iMetadata | iFacing;
   }

   @Override
   public boolean canRotateOnTurntable(IBlockAccess iBlockAccess, int i, int j, int k) {
      return true;
   }

   @Override
   public boolean rotateAroundJAxis(World world, int i, int j, int k, boolean bReverse) {
      if (super.rotateAroundJAxis(world, i, j, k, bReverse)) {
         world.scheduleBlockUpdate(i, j, k, this.blockID, this.tickRate(world));
         MechPowerUtils.destroyHorizontallyAttachedAxles(world, i, j, k);
         return true;
      } else {
         return false;
      }
   }

   @Override
   public boolean canOutputMechanicalPower() {
      return false;
   }

   @Override
   public boolean canInputMechanicalPower() {
      return true;
   }

   @Override
   public boolean isInputtingMechanicalPower(World world, int i, int j, int k) {
      return MechPowerUtils.isBlockPoweredByAxle(world, i, j, k, this) || MechPowerUtils.isBlockPoweredByHandCrank(world, i, j, k);
   }

   @Override
   public boolean canInputAxlePowerToFacing(World world, int i, int j, int k, int iFacing) {
      int iBlockFacing = this.getFacing(world, i, j, k);
      return iFacing != iBlockFacing && iFacing != 1;
   }

   @Override
   public boolean isOutputtingMechanicalPower(World world, int i, int j, int k) {
      return false;
   }

   @Override
   public void overpower(World world, int i, int j, int k) {
      this.breakBellows(world, i, j, k);
   }

   public boolean isBlockMechanicalOn(IBlockAccess blockAccess, int i, int j, int k) {
      return this.getIsBlockMechanicalOnFromMetadata(blockAccess.getBlockMetadata(i, j, k));
   }

   public void setBlockMechanicalOn(World world, int i, int j, int k, boolean bOn) {
      int iMetadata = world.getBlockMetadata(i, j, k) & -5;
      if (bOn) {
         iMetadata |= 4;
      }

      world.setBlockMetadataWithNotify(i, j, k, iMetadata);
   }

   public boolean getIsBlockMechanicalOnFromMetadata(int iMetadata) {
      return (iMetadata & 4) > 0;
   }

   public boolean isContinuousMechanicalStateChange(IBlockAccess blockAccess, int i, int j, int k) {
      return (blockAccess.getBlockMetadata(i, j, k) & 8) > 0;
   }

   public void setIsContinuousMechanicalStateChange(World world, int i, int j, int k, boolean bContinuous) {
      int iMetadata = world.getBlockMetadata(i, j, k) & -9;
      if (bContinuous) {
         iMetadata |= 8;
      }

      world.setBlockMetadata(i, j, k, iMetadata);
   }

   public boolean isCurrentStateValid(World world, int i, int j, int k) {
      boolean bReceivingMechanicalPower = this.isInputtingMechanicalPower(world, i, j, k);
      boolean bMechanicalOn = this.isBlockMechanicalOn(world, i, j, k);
      return bReceivingMechanicalPower == bMechanicalOn;
   }

   private void blow(World world, int i, int j, int k) {
      this.stokeFiresInFront(world, i, j, k);
      this.blowLightItemsInFront(world, i, j, k);
   }

   private void stokeFiresInFront(World world, int i, int j, int k) {
      int iFacing = this.getFacing(world, i, j, k);
      int iFacingSide1 = Block.rotateFacingAroundY(iFacing, false);
      int iFacingSide2 = Block.rotateFacingAroundY(iFacing, true);
      BlockPos tempTargetPos = new BlockPos(i, j, k);

      for (int iTempCount = 0; iTempCount < 3; iTempCount++) {
         tempTargetPos.addFacingAsOffset(iFacing);
         int tempBlockID = world.getBlockId(tempTargetPos.x, tempTargetPos.y, tempTargetPos.z);
         if (tempBlockID != Block.fire.blockID && tempBlockID != BTWBlocks.stokedFire.blockID) {
            if (!world.isAirBlock(tempTargetPos.x, tempTargetPos.y, tempTargetPos.z)) {
               break;
            }
         } else {
            this.stokeFire(world, tempTargetPos.x, tempTargetPos.y, tempTargetPos.z);
         }

         BlockPos tempSidePos1 = new BlockPos(tempTargetPos.x, tempTargetPos.y, tempTargetPos.z);
         tempSidePos1.addFacingAsOffset(iFacingSide1);
         tempBlockID = world.getBlockId(tempSidePos1.x, tempSidePos1.y, tempSidePos1.z);
         if (tempBlockID == Block.fire.blockID || tempBlockID == BTWBlocks.stokedFire.blockID) {
            this.stokeFire(world, tempSidePos1.x, tempSidePos1.y, tempSidePos1.z);
         }

         BlockPos tempSidePos2 = new BlockPos(tempTargetPos.x, tempTargetPos.y, tempTargetPos.z);
         tempSidePos2.addFacingAsOffset(iFacingSide2);
         tempBlockID = world.getBlockId(tempSidePos2.x, tempSidePos2.y, tempSidePos2.z);
         if (tempBlockID == Block.fire.blockID || tempBlockID == BTWBlocks.stokedFire.blockID) {
            this.stokeFire(world, tempSidePos2.x, tempSidePos2.y, tempSidePos2.z);
         }
      }
   }

   private void blowLightItemsInFront(World world, int i, int j, int k) {
      int iFacing = this.getFacing(world, i, j, k);
      BlockPos targetPos = new BlockPos(i, j, k);
      targetPos.addFacingAsOffset(iFacing);
      List collisionList = null;
      int iBlowRange = this.computeBlowRange(world, i, j, k);
      if (iBlowRange > 0) {
         AxisAlignedBB blowBox = this.createBlowBoundingBox(world, i, j, k, iBlowRange);
         if (blowBox != null) {
            collisionList = world.getEntitiesWithinAABB(EntityItem.class, blowBox);
            if (collisionList != null && collisionList.size() > 0) {
               Vec3 blowVector = MiscUtils.convertBlockFacingToVector(iFacing);
               blowVector.xCoord *= 0.2;
               blowVector.yCoord *= 0.2;
               blowVector.zCoord *= 0.2;

               for (int listIndex = 0; listIndex < collisionList.size(); listIndex++) {
                  EntityItem targetEntityItem = (EntityItem)collisionList.get(listIndex);
                  if (!targetEntityItem.isDead) {
                     ItemStack stack = targetEntityItem.getEntityItem();
                     int iItemBlowDistance = stack.getItem().getBellowsBlowDistance(stack.getItemDamage());
                     if (iItemBlowDistance > 0
                        && (iItemBlowDistance >= iBlowRange || this.isEntityWithinBlowRange(world, i, j, k, iItemBlowDistance, targetEntityItem))) {
                        targetEntityItem.motionX = targetEntityItem.motionX + blowVector.xCoord;
                        targetEntityItem.motionY = targetEntityItem.motionY + blowVector.yCoord;
                        targetEntityItem.motionZ = targetEntityItem.motionZ + blowVector.zCoord;
                     }
                  }
               }
            }
         }
      }
   }

   private boolean isEntityWithinBlowRange(World world, int i, int j, int k, int iBlowRange, Entity entity) {
      AxisAlignedBB blowBox = this.createBlowBoundingBox(world, i, j, k, iBlowRange);
      return blowBox.intersectsWith(entity.boundingBox);
   }

   private AxisAlignedBB createBlowBoundingBox(World world, int i, int j, int k, int iBlowRange) {
      AxisAlignedBB blowBox = null;
      if (iBlowRange > 0) {
         int iFacing = this.getFacing(world, i, j, k);
         BlockPos targetPos = new BlockPos(i, j, k);
         targetPos.addFacingAsOffset(iFacing);
         blowBox = AxisAlignedBB.getAABBPool().getAABB(targetPos.x, targetPos.y, targetPos.z, targetPos.x + 1, targetPos.y + 1, targetPos.z + 1);
         if (iBlowRange > 1) {
            Vec3 blowVector = MiscUtils.convertBlockFacingToVector(iFacing);
            double dMultiplier = iBlowRange - 1;
            blowVector.xCoord *= dMultiplier;
            blowVector.yCoord *= dMultiplier;
            blowVector.zCoord *= dMultiplier;
            blowBox = blowBox.addCoord(blowVector.xCoord, blowVector.yCoord, blowVector.zCoord);
         }
      }

      return blowBox;
   }

   private int computeBlowRange(World world, int i, int j, int k) {
      int iBlowRange = 0;
      int iFacing = this.getFacing(world, i, j, k);
      BlockPos targetPos = new BlockPos(i, j, k);

      for (int iTempRange = 0; iTempRange < 3; iTempRange++) {
         targetPos.addFacingAsOffset(iFacing);
         if (!this.canBlowThroughBlock(world, targetPos.x, targetPos.y, targetPos.z)) {
            break;
         }

         iBlowRange++;
      }

      return iBlowRange;
   }

   private boolean canBlowThroughBlock(World world, int i, int j, int k) {
      if (!world.isAirBlock(i, j, k)) {
         int iBlockID = world.getBlockId(i, j, k);
         if (iBlockID != Block.fire.blockID
            && iBlockID != BTWBlocks.stokedFire.blockID
            && iBlockID != Block.trapdoor.blockID
            && Block.blocksList[iBlockID].getCollisionBoundingBoxFromPool(world, i, j, k) != null) {
            return false;
         }
      }

      return true;
   }

   private void stokeFire(World world, int i, int j, int k) {
      if (world.getBlockId(i, j - 1, k) == BTWBlocks.hibachi.blockID) {
         if (world.getBlockId(i, j, k) == BTWBlocks.stokedFire.blockID) {
            world.setBlockMetadata(i, j, k, 0);
         } else {
            world.setBlockWithNotify(i, j, k, BTWBlocks.stokedFire.blockID);
         }

         if (world.isAirBlock(i, j + 1, k)) {
            world.setBlockWithNotify(i, j + 1, k, av.blockID);
         }
      } else {
         world.setBlockWithNotify(i, j, k, 0);
      }
   }

   private void liftCollidingEntities(World world, int i, int j, int k) {
      List list = world.getEntitiesWithinAABBExcludingEntity(null, AxisAlignedBB.getAABBPool().getAABB(i, j + 0.6875F, k, i + 1, j + 1, k + 1));
      float extendedMaxY = j + 1;
      if (list != null && list.size() > 0) {
         for (int j1 = 0; j1 < list.size(); j1++) {
            Entity tempEntity = (Entity)list.get(j1);
            if (!tempEntity.isDead && (tempEntity.canBePushed() || tempEntity instanceof EntityItem)) {
               double tempEntityMinY = tempEntity.boundingBox.minY;
               if (tempEntityMinY < extendedMaxY) {
                  double entityYOffset = extendedMaxY - tempEntityMinY;
                  tempEntity.setPosition(tempEntity.posX, tempEntity.posY + entityYOffset, tempEntity.posZ);
               }
            }
         }
      }
   }

   public void breakBellows(World world, int i, int j, int k) {
      for (int iTemp = 0; iTemp < 2; iTemp++) {
         ItemUtils.ejectSingleItemWithRandomOffset(world, i, j, k, BTWItems.woodSidingStubID, 0);
      }

      for (int iTemp = 0; iTemp < 1; iTemp++) {
         ItemUtils.ejectSingleItemWithRandomOffset(world, i, j, k, BTWItems.gear.itemID, 0);
      }

      for (int iTemp = 0; iTemp < 2; iTemp++) {
         ItemStack itemStack = new ItemStack(BTWItems.tannedLeather.itemID, 4, 0);
         ItemUtils.ejectStackWithRandomOffset(world, i, j, k, itemStack);
      }

      world.playAuxSFX(2235, i, j, k, 0);
      world.setBlockWithNotify(i, j, k, 0);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void registerIcons(IconRegister register) {
      Icon sideIcon = register.registerIcon("fcBlockBellows_side");
      this.blockIcon = sideIcon;
      this.iconBySideArray[0] = register.registerIcon("fcBlockBellows_bottom");
      this.iconBySideArray[1] = register.registerIcon("fcBlockBellows_top");
      this.iconBySideArray[2] = sideIcon;
      this.iconBySideArray[3] = sideIcon;
      this.iconBySideArray[4] = sideIcon;
      this.iconBySideArray[5] = sideIcon;
      this.iconFront = register.registerIcon("fcBlockBellows_front");
   }

   @Environment(EnvType.CLIENT)
   @Override
   public Icon getIcon(int iSide, int iMetadata) {
      return iSide == 3 ? this.iconFront : this.iconBySideArray[iSide];
   }

   @Environment(EnvType.CLIENT)
   @Override
   public Icon getBlockTexture(IBlockAccess blockAccess, int i, int j, int k, int iSide) {
      int iFacing = this.getFacing(blockAccess, i, j, k);
      return iSide == iFacing ? this.iconFront : this.iconBySideArray[iSide];
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void clientNotificationOfMetadataChange(World world, int i, int j, int k, int iOldMetadata, int iNewMetadata) {
      if (!this.getIsBlockMechanicalOnFromMetadata(iOldMetadata) && this.getIsBlockMechanicalOnFromMetadata(iNewMetadata)) {
         this.blowLightItemsInFront(world, i, j, k);
         world.playSound(i + 0.5, j + 0.5, k + 0.5, "mob.cow.say4", 0.25F, world.rand.nextFloat() * 0.4F + 2.0F);
         int iFacing = this.getFacing(iNewMetadata);
         this.emitBellowsParticles(world, i, j, k, iFacing, world.rand);
      } else if (this.getIsBlockMechanicalOnFromMetadata(iOldMetadata) && !this.getIsBlockMechanicalOnFromMetadata(iNewMetadata)) {
         this.liftCollidingEntities(world, i, j, k);
         world.playSound(i + 0.5, j + 0.5, k + 0.5, "mob.cow.say2", 1.0F, world.rand.nextFloat() * 0.4F + 2.0F);
      }
   }

   @Environment(EnvType.CLIENT)
   private void emitBellowsParticles(World world, int i, int j, int k, int iFacing, Random random) {
      BlockPos targetPos = new BlockPos(i, j, k);
      targetPos.addFacingAsOffset(iFacing);
      Vec3 blowVector = MiscUtils.convertBlockFacingToVector(iFacing);
      blowVector.xCoord *= 0.1F;
      blowVector.yCoord *= 0.1F;
      blowVector.zCoord *= 0.1F;

      for (int counter = 0; counter < 10; counter++) {
         float smokeX = targetPos.x + random.nextFloat();
         float smokeY = targetPos.y + random.nextFloat() * 0.5F;
         float smokeZ = targetPos.z + random.nextFloat();
         world.spawnParticle(
            "smoke",
            smokeX,
            smokeY,
            smokeZ,
            blowVector.xCoord + random.nextFloat() * 0.1F - 0.05F,
            blowVector.yCoord + random.nextFloat() * 0.1F - 0.05F,
            blowVector.zCoord + random.nextFloat() * 0.1F - 0.05F
         );
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean shouldSideBeRendered(IBlockAccess blockAccess, int iNeighborI, int iNeighborJ, int iNeighborK, int iSide) {
      return this.currentBlockRenderer.shouldSideBeRenderedBasedOnCurrentBounds(iNeighborI, iNeighborJ, iNeighborK, iSide);
   }
}
