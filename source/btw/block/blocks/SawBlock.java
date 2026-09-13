package btw.block.blocks;

import btw.block.BTWBlocks;
import btw.block.MechanicalBlock;
import btw.block.util.MechPowerUtils;
import btw.client.render.util.RenderUtils;
import btw.crafting.manager.SawCraftingManager;
import btw.item.BTWItems;
import btw.util.CustomDamageSource;
import btw.util.MiscUtils;
import btw.world.util.BlockPos;
import java.util.List;
import java.util.Random;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.DamageSource;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Icon;
import net.minecraft.src.IconRegister;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.World;

public class SawBlock extends Block implements MechanicalBlock {
   private static final int POWER_CHANGE_TICK_RATE = 10;
   private static final int SAW_TIME_BASE_TICK_RATE = 20;
   private static final int SAW_TIME_TICK_RATE_VARIANCE = 4;
   public static final float BASE_HEIGHT = 0.75F;
   public static final float BLADE_LENGTH = 0.625F;
   public static final float BLADE_HALF_LENGTH = 0.3125F;
   public static final float BLADE_WIDTH = 0.015625F;
   public static final float BLADE_HALF_WIDTH = 0.0078125F;
   public static final float BLADE_HEIGHT = 0.25F;
   @Environment(EnvType.CLIENT)
   private Icon iconFront;
   @Environment(EnvType.CLIENT)
   private Icon iconBladeOff;
   @Environment(EnvType.CLIENT)
   private Icon iconBladeOn;

   public SawBlock(int iBlockID) {
      super(iBlockID, BTWBlocks.plankMaterial);
      this.c(2.0F);
      this.setAxesEffectiveOn(true);
      this.setBuoyancy(1.0F);
      this.initBlockBounds(0.0, 0.0, 0.0, 1.0, 0.75, 1.0);
      this.setFireProperties(5, 20);
      this.a(g);
      this.c("fcBlockSaw");
      this.b(true);
      this.a(CreativeTabs.tabRedstone);
   }

   @Override
   public int tickRate(World world) {
      return 10;
   }

   @Override
   public int onBlockPlaced(World world, int i, int j, int k, int iFacing, float fClickX, float fClickY, float fClickZ, int iMetadata) {
      return this.setFacing(iMetadata, Block.getOppositeFacing(iFacing));
   }

   @Override
   public void onBlockPlacedBy(World world, int i, int j, int k, EntityLiving entityLiving, ItemStack stack) {
      int iFacing = MiscUtils.convertPlacingEntityOrientationToBlockFacingReversed(entityLiving);
      this.setFacing(world, i, j, k, iFacing);
   }

   @Override
   public void onBlockAdded(World world, int i, int j, int k) {
      super.onBlockAdded(world, i, j, k);
      world.scheduleBlockUpdate(i, j, k, this.blockID, 10);
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
   public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k) {
      float fBaseHeight = 0.71875F;
      return this.getBlockBoundsFromPoolForBaseHeight(world, i, j, k, fBaseHeight).offset(i, j, k);
   }

   @Override
   public AxisAlignedBB getBlockBoundsFromPoolBasedOnState(IBlockAccess blockAccess, int i, int j, int k) {
      return this.getBlockBoundsFromPoolForBaseHeight(blockAccess, i, j, k, 0.75F);
   }

   @Override
   public void onNeighborBlockChange(World world, int i, int j, int k, int iBlockID) {
      if (!world.isUpdatePendingThisTickForBlock(i, j, k, this.blockID)) {
         this.scheduleUpdateIfRequired(world, i, j, k);
      }
   }

   @Override
   public void updateTick(World world, int i, int j, int k, Random rand) {
      boolean bReceivingPower = this.isInputtingMechanicalPower(world, i, j, k);
      boolean bOn = this.isBlockOn(world, i, j, k);
      if (bOn != bReceivingPower) {
         this.emitSawParticles(world, i, j, k, rand);
         this.setBlockOn(world, i, j, k, bReceivingPower);
         if (bReceivingPower) {
            world.playSoundEffect(i + 0.5, j + 0.5, k + 0.5, "minecart.base", 1.0F + rand.nextFloat() * 0.1F, 1.5F + rand.nextFloat() * 0.1F);
            this.scheduleUpdateIfRequired(world, i, j, k);
         } else {
            world.playSoundEffect(i + 0.5, j + 0.5, k + 0.5, "minecart.base", 1.0F + rand.nextFloat() * 0.1F, 0.75F + rand.nextFloat() * 0.1F);
         }
      } else if (bOn) {
         this.sawBlockToFront(world, i, j, k, rand);
      }
   }

   @Override
   public void randomUpdateTick(World world, int i, int j, int k, Random rand) {
      if (!world.isUpdateScheduledForBlock(i, j, k, this.blockID)) {
         this.scheduleUpdateIfRequired(world, i, j, k);
      }
   }

   @Override
   public void onEntityCollidedWithBlock(World world, int i, int j, int k, Entity entity) {
      if (!world.isRemote) {
         if (this.isBlockOn(world, i, j, k) && entity instanceof EntityLiving) {
            int iFacing = this.getFacing(world, i, j, k);
            float fHalfLength = 0.3125F;
            float fHalfWidth = 0.0078125F;
            float fBlockHeight = 0.25F;
            AxisAlignedBB sawBox;
            switch (iFacing) {
               case 0:
                  sawBox = AxisAlignedBB.getAABBPool().getAABB(0.5F - fHalfLength, 0.0, 0.5F - fHalfWidth, 0.5F + fHalfLength, fBlockHeight, 0.5F + fHalfWidth);
                  break;
               case 1:
                  sawBox = AxisAlignedBB.getAABBPool()
                     .getAABB(0.5F - fHalfLength, 1.0F - fBlockHeight, 0.5F - fHalfWidth, 0.5F + fHalfLength, 1.0, 0.5F + fHalfWidth);
                  break;
               case 2:
                  sawBox = AxisAlignedBB.getAABBPool().getAABB(0.5F - fHalfLength, 0.5F - fHalfWidth, 0.0, 0.5F + fHalfLength, 0.5F + fHalfWidth, fBlockHeight);
                  break;
               case 3:
                  sawBox = AxisAlignedBB.getAABBPool()
                     .getAABB(0.5F - fHalfLength, 0.5F - fHalfWidth, 1.0F - fBlockHeight, 0.5F + fHalfLength, 0.5F + fHalfWidth, 1.0);
                  break;
               case 4:
                  sawBox = AxisAlignedBB.getAABBPool().getAABB(0.0, 0.5F - fHalfWidth, 0.5F - fHalfLength, fBlockHeight, 0.5F + fHalfWidth, 0.5F + fHalfLength);
                  break;
               default:
                  sawBox = AxisAlignedBB.getAABBPool()
                     .getAABB(1.0F - fBlockHeight, 0.5F - fHalfWidth, 0.5F - fHalfLength, 1.0, 0.5F + fHalfWidth, 0.5F + fHalfLength);
            }

            sawBox = sawBox.getOffsetBoundingBox(i, j, k);
            List collisionList = null;
            collisionList = world.getEntitiesWithinAABB(EntityLiving.class, sawBox);
            if (collisionList != null && collisionList.size() > 0) {
               DamageSource source = CustomDamageSource.damageSourceSaw;
               int iDamage = 4;
               BlockPos targetPos = new BlockPos(i, j, k);
               targetPos.addFacingAsOffset(iFacing);
               int iTargetBlockID = world.getBlockId(targetPos.x, targetPos.y, targetPos.z);
               int iTargetMetadata = world.getBlockMetadata(targetPos.x, targetPos.y, targetPos.z);
               if (iTargetBlockID == BTWBlocks.aestheticOpaque.blockID && (iTargetMetadata == 13 || iTargetMetadata == 12)) {
                  source = CustomDamageSource.damageSourceChoppingBlock;
                  iDamage *= 3;
                  if (iTargetMetadata == 13) {
                     world.setBlockMetadataWithNotify(targetPos.x, targetPos.y, targetPos.z, 12);
                  }
               }

               for (int iTempListIndex = 0; iTempListIndex < collisionList.size(); iTempListIndex++) {
                  EntityLiving tempTargetEntity = (EntityLiving)collisionList.get(iTempListIndex);
                  if (tempTargetEntity.attackEntityFrom(source, iDamage)) {
                     world.playAuxSFX(2223, i, j, k, iFacing);
                  }
               }
            }
         }
      }
   }

   @Override
   public boolean hasCenterHardPointToFacing(IBlockAccess blockAccess, int i, int j, int k, int iFacing, boolean bIgnoreTransparency) {
      return iFacing != this.getFacing(blockAccess, i, j, k);
   }

   @Override
   public boolean hasLargeCenterHardPointToFacing(IBlockAccess blockAccess, int i, int j, int k, int iFacing, boolean bIgnoreTransparency) {
      return Block.getOppositeFacing(iFacing) == this.getFacing(blockAccess, i, j, k);
   }

   @Override
   public int getHarvestToolLevel(IBlockAccess blockAccess, int i, int j, int k) {
      return 2;
   }

   @Override
   public boolean dropComponentItemsOnBadBreak(World world, int i, int j, int k, int iMetadata, float fChanceOfDrop) {
      this.dropItemsIndividually(world, i, j, k, BTWItems.gear.itemID, 1, 0, fChanceOfDrop);
      this.dropItemsIndividually(world, i, j, k, Item.stick.itemID, 2, 0, fChanceOfDrop);
      this.dropItemsIndividually(world, i, j, k, BTWItems.sawDust.itemID, 3, 0, fChanceOfDrop);
      this.dropItemsIndividually(world, i, j, k, Item.ingotIron.itemID, 2, 0, fChanceOfDrop);
      this.dropItemsIndividually(world, i, j, k, BTWItems.ironNugget.itemID, 4, 0, fChanceOfDrop);
      this.dropItemsIndividually(world, i, j, k, BTWItems.leatherStrap.itemID, 3, 0, fChanceOfDrop);
      return true;
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
   public boolean canRotateOnTurntable(IBlockAccess iBlockAccess, int i, int j, int k) {
      int iFacing = this.getFacing(iBlockAccess, i, j, k);
      return iFacing != 0;
   }

   @Override
   public boolean canTransmitRotationVerticallyOnTurntable(IBlockAccess blockAccess, int i, int j, int k) {
      int iFacing = this.getFacing(blockAccess, i, j, k);
      return iFacing != 0 && iFacing != 1;
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
   public boolean toggleFacing(World world, int i, int j, int k, boolean bReverse) {
      int iFacing = this.getFacing(world, i, j, k);
      iFacing = Block.cycleFacing(iFacing, bReverse);
      this.setFacing(world, i, j, k, iFacing);
      world.scheduleBlockUpdate(i, j, k, this.blockID, this.tickRate(world));
      world.notifyBlockChange(i, j, k, this.blockID);
      return true;
   }

   @Override
   public boolean isIncineratedInCrucible() {
      return false;
   }

   protected boolean isCurrentPowerStateValid(World world, int i, int j, int k) {
      boolean bReceivingPower = this.isInputtingMechanicalPower(world, i, j, k);
      boolean bOn = this.isBlockOn(world, i, j, k);
      return bOn == bReceivingPower;
   }

   public boolean isBlockOn(IBlockAccess iBlockAccess, int i, int j, int k) {
      return (iBlockAccess.getBlockMetadata(i, j, k) & 8) > 0;
   }

   public void setBlockOn(World world, int i, int j, int k, boolean bOn) {
      int iMetaData = world.getBlockMetadata(i, j, k) & 7;
      if (bOn) {
         iMetaData |= 8;
      }

      world.setBlockMetadataWithNotify(i, j, k, iMetaData);
   }

   protected void scheduleUpdateIfRequired(World world, int i, int j, int k) {
      if (!this.isCurrentPowerStateValid(world, i, j, k)) {
         world.scheduleBlockUpdate(i, j, k, this.blockID, 10);
      } else if (this.isBlockOn(world, i, j, k)) {
         int iFacing = this.getFacing(world, i, j, k);
         BlockPos targetPos = new BlockPos(i, j, k, iFacing);
         Block targetBlock = Block.blocksList[world.getBlockId(targetPos.x, targetPos.y, targetPos.z)];
         int targetMetadata = world.getBlockMetadata(targetPos.x, targetPos.y, targetPos.z);
         if (targetBlock != null
            && (
               targetBlock.blockMaterial.isSolid()
                  || SawCraftingManager.instance.getRecipe(targetBlock, targetMetadata) != null
                  || targetBlock.doesBlockDropAsItemOnSaw(world, targetPos.x, targetPos.y, targetPos.z)
            )) {
            world.playSoundEffect(i + 0.5, j + 0.5, k + 0.5, "minecart.base", 1.5F + world.rand.nextFloat() * 0.1F, 1.9F + world.rand.nextFloat() * 0.1F);
            world.scheduleBlockUpdate(i, j, k, this.blockID, 20 + world.rand.nextInt(4));
         }
      }
   }

   public AxisAlignedBB getBlockBoundsFromPoolForBaseHeight(IBlockAccess blockAccess, int i, int j, int k, float fBaseHeight) {
      int iFacing = this.getFacing(blockAccess, i, j, k);
      switch (iFacing) {
         case 0:
            return AxisAlignedBB.getAABBPool().getAABB(0.0, 1.0F - fBaseHeight, 0.0, 1.0, 1.0, 1.0);
         case 1:
            return AxisAlignedBB.getAABBPool().getAABB(0.0, 0.0, 0.0, 1.0, fBaseHeight, 1.0);
         case 2:
            return AxisAlignedBB.getAABBPool().getAABB(0.0, 0.0, 1.0F - fBaseHeight, 1.0, 1.0, 1.0);
         case 3:
            return AxisAlignedBB.getAABBPool().getAABB(0.0, 0.0, 0.0, 1.0, 1.0, fBaseHeight);
         case 4:
            return AxisAlignedBB.getAABBPool().getAABB(1.0F - fBaseHeight, 0.0, 0.0, 1.0, 1.0, 1.0);
         default:
            return AxisAlignedBB.getAABBPool().getAABB(0.0, 0.0, 0.0, fBaseHeight, 1.0, 1.0);
      }
   }

   void emitSawParticles(World world, int i, int j, int k, Random random) {
      int iFacing = this.getFacing(world, i, j, k);
      float fBladeXPos = i;
      float fBladeYPos = j;
      float fBladeZPos = k;
      float fBladeXExtent = 0.0F;
      float fBladeZExtent = 0.0F;
      switch (iFacing) {
         case 0:
            fBladeXPos += 0.5F;
            fBladeZPos += 0.5F;
            fBladeXExtent = 1.0F;
            break;
         case 1:
            fBladeXPos += 0.5F;
            fBladeZPos += 0.5F;
            fBladeYPos++;
            fBladeXExtent = 1.0F;
            break;
         case 2:
            fBladeXPos += 0.5F;
            fBladeYPos += 0.5F;
            fBladeXExtent = 1.0F;
            break;
         case 3:
            fBladeXPos += 0.5F;
            fBladeYPos += 0.5F;
            fBladeZPos++;
            fBladeXExtent = 1.0F;
            break;
         case 4:
            fBladeYPos += 0.5F;
            fBladeZPos += 0.5F;
            fBladeZExtent = 1.0F;
            break;
         default:
            fBladeYPos += 0.5F;
            fBladeZPos += 0.5F;
            fBladeXPos++;
            fBladeZExtent = 1.0F;
      }

      for (int counter = 0; counter < 5; counter++) {
         float smokeX = fBladeXPos + (random.nextFloat() - 0.5F) * fBladeXExtent;
         float smokeY = fBladeYPos + random.nextFloat() * 0.1F;
         float smokeZ = fBladeZPos + (random.nextFloat() - 0.5F) * fBladeZExtent;
         world.spawnParticle("smoke", smokeX, smokeY, smokeZ, 0.0, 0.0, 0.0);
      }
   }

   protected void sawBlockToFront(World world, int i, int j, int k, Random random) {
      int iFacing = this.getFacing(world, i, j, k);
      BlockPos targetPos = new BlockPos(i, j, k, iFacing);
      if (!world.isAirBlock(targetPos.x, targetPos.y, targetPos.z)
         && !this.handleSawingExceptionCases(world, targetPos.x, targetPos.y, targetPos.z, i, j, k, iFacing, random)) {
         Block targetBlock = Block.blocksList[world.getBlockId(targetPos.x, targetPos.y, targetPos.z)];
         if (targetBlock != null) {
            if (targetBlock.doesBlockBreakSaw(world, targetPos.x, targetPos.y, targetPos.z)) {
               this.breakSaw(world, i, j, k);
            } else if (targetBlock.onBlockSawed(world, targetPos.x, targetPos.y, targetPos.z, i, j, k)) {
               this.emitSawParticles(world, targetPos.x, targetPos.y, targetPos.z, random);
            }
         }
      }
   }

   private boolean handleSawingExceptionCases(World world, int i, int j, int k, int iSawI, int iSawJ, int iSawK, int iSawFacing, Random random) {
      int iTargetBlockID = world.getBlockId(i, j, k);
      return iTargetBlockID == Block.pistonMoving.blockID;
   }

   public void breakSaw(World world, int i, int j, int k) {
      this.dropComponentItemsOnBadBreak(world, i, j, k, world.getBlockMetadata(i, j, k), 1.0F);
      world.playAuxSFX(2235, i, j, k, 0);
      world.setBlockWithNotify(i, j, k, 0);
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
      return MechPowerUtils.isBlockPoweredByAxle(world, i, j, k, this);
   }

   @Override
   public boolean canInputAxlePowerToFacing(World world, int i, int j, int k, int iFacing) {
      int iBlockFacing = this.getFacing(world, i, j, k);
      return iFacing != iBlockFacing;
   }

   @Override
   public boolean isOutputtingMechanicalPower(World world, int i, int j, int k) {
      return false;
   }

   @Override
   public void overpower(World world, int i, int j, int k) {
      this.breakSaw(world, i, j, k);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void registerIcons(IconRegister register) {
      super.registerIcons(register);
      this.iconFront = register.registerIcon("fcBlockSaw_front");
      this.iconBladeOff = register.registerIcon("fcBlockSawBlade_off");
      this.iconBladeOn = register.registerIcon("fcBlockSawBlade_on");
   }

   @Environment(EnvType.CLIENT)
   @Override
   public Icon getIcon(int iSide, int iMetadata) {
      return iSide == 1 ? this.iconFront : this.blockIcon;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public Icon getBlockTexture(IBlockAccess blockAccess, int i, int j, int k, int iSide) {
      int iFacing = this.getFacing(blockAccess, i, j, k);
      return iSide == iFacing ? this.iconFront : this.blockIcon;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void randomDisplayTick(World world, int i, int j, int k, Random random) {
      if (this.isBlockOn(world, i, j, k)) {
         this.emitSawParticles(world, i, j, k, random);
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean shouldSideBeRendered(IBlockAccess blockAccess, int iNeighborI, int iNeighborJ, int iNeighborK, int iSide) {
      return this.currentBlockRenderer.shouldSideBeRenderedBasedOnCurrentBounds(iNeighborI, iNeighborJ, iNeighborK, iSide);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean renderBlock(RenderBlocks renderer, int i, int j, int k) {
      IBlockAccess blockAccess = renderer.blockAccess;
      float fHalfLength = 0.5F;
      float fHalfWidth = 0.5F;
      float fBlockHeight = 0.75F;
      int iFacing = this.getFacing(blockAccess, i, j, k);
      renderer.setRenderBounds(this.getBlockBoundsFromPoolBasedOnState(renderer.blockAccess, i, j, k));
      renderer.renderStandardBlock(this, i, j, k);
      fHalfLength = 0.3125F;
      fHalfWidth = 0.0078125F;
      fBlockHeight = 0.25F;
      switch (iFacing) {
         case 0:
            renderer.setRenderBounds(0.5F - fHalfLength, 0.0, 0.5F - fHalfWidth, 0.5F + fHalfLength, 0.999F, 0.5F + fHalfWidth);
            renderer.setUVRotateEast(3);
            renderer.setUVRotateWest(3);
            renderer.setUVRotateSouth(1);
            renderer.setUVRotateNorth(2);
            renderer.setUVRotateBottom(3);
            break;
         case 1:
            renderer.setRenderBounds(0.5F - fHalfLength, 0.001F, 0.5F - fHalfWidth, 0.5F + fHalfLength, 1.0, 0.5F + fHalfWidth);
            renderer.setUVRotateSouth(2);
            renderer.setUVRotateNorth(1);
            break;
         case 2:
            renderer.setRenderBounds(0.5F - fHalfLength, 0.5F - fHalfWidth, 0.0, 0.5F + fHalfLength, 0.5F + fHalfWidth, fBlockHeight);
            renderer.setUVRotateSouth(3);
            renderer.setUVRotateNorth(4);
            renderer.setUVRotateEast(3);
            renderer.setUVRotateWest(3);
            break;
         case 3:
            renderer.setRenderBounds(0.5F - fHalfLength, 0.5F - fHalfWidth, 1.0F - fBlockHeight, 0.5F + fHalfLength, 0.5F + fHalfWidth, 1.0);
            renderer.setUVRotateSouth(4);
            renderer.setUVRotateNorth(3);
            renderer.setUVRotateTop(3);
            renderer.setUVRotateBottom(3);
            break;
         case 4:
            renderer.setRenderBounds(0.0, 0.5F - fHalfWidth, 0.5F - fHalfLength, fBlockHeight, 0.5F + fHalfWidth, 0.5F + fHalfLength);
            renderer.setUVRotateEast(4);
            renderer.setUVRotateWest(3);
            renderer.setUVRotateTop(2);
            renderer.setUVRotateBottom(1);
            renderer.setUVRotateNorth(3);
            renderer.setUVRotateSouth(4);
            break;
         default:
            renderer.setRenderBounds(1.0F - fBlockHeight, 0.5F - fHalfWidth, 0.5F - fHalfLength, 1.0, 0.5F + fHalfWidth, 0.5F + fHalfLength);
            renderer.setUVRotateEast(3);
            renderer.setUVRotateWest(4);
            renderer.setUVRotateTop(1);
            renderer.setUVRotateBottom(2);
            renderer.setUVRotateSouth(4);
            renderer.setUVRotateNorth(3);
      }

      renderer.setRenderAllFaces(true);
      Icon bladeIcon = this.iconBladeOff;
      if (this.isBlockOn(blockAccess, i, j, k)) {
         bladeIcon = this.iconBladeOn;
      }

      RenderUtils.renderStandardBlockWithTexture(renderer, this, i, j, k, bladeIcon);
      renderer.setRenderAllFaces(false);
      renderer.clearUVRotation();
      return true;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void renderBlockAsItem(RenderBlocks renderBlocks, int iItemDamage, float fBrightness) {
      renderBlocks.setRenderBounds(0.0, 0.0, 0.0, 1.0, 0.75, 1.0);
      RenderUtils.renderInvBlockWithMetadata(renderBlocks, this, -0.5F, -0.5F, -0.5F, 1);
      renderBlocks.setRenderBounds(0.1875, 0.001F, 0.4921875, 0.8125, 1.0, 0.5078125);
      RenderUtils.renderInvBlockWithTexture(renderBlocks, this, -0.5F, -0.5F, -0.5F, this.iconBladeOff);
   }
}
