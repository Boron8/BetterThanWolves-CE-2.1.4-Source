package btw.block.blocks;

import btw.block.BTWBlocks;
import btw.block.FluidSource;
import btw.block.MechanicalBlock;
import btw.block.util.Flammability;
import btw.block.util.MechPowerUtils;
import btw.item.BTWItems;
import btw.util.MiscUtils;
import btw.world.util.BlockPos;
import java.util.Random;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Icon;
import net.minecraft.src.IconRegister;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;

public class ScrewPumpBlock extends Block implements MechanicalBlock, FluidSource {
   public static final int TICK_RATE = 20;
   @Environment(EnvType.CLIENT)
   private Icon[] iconBySideArray = new Icon[6];
   @Environment(EnvType.CLIENT)
   private Icon iconFront;

   public ScrewPumpBlock(int iBlockID) {
      super(iBlockID, BTWBlocks.plankMaterial);
      this.c(2.0F);
      this.b(5.0F);
      this.setAxesEffectiveOn(true);
      this.setBuoyancy(1.0F);
      this.setFireProperties(Flammability.PLANKS);
      this.a(g);
      this.c("fcBlockScrewPump");
      this.b(true);
      this.a(CreativeTabs.tabRedstone);
   }

   @Override
   public int tickRate(World world) {
      return 20;
   }

   @Override
   public void onBlockAdded(World world, int i, int j, int k) {
      super.onBlockAdded(world, i, j, k);
      world.scheduleBlockUpdate(i, j, k, this.blockID, this.tickRate(world));
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
   public void onNeighborBlockChange(World world, int i, int j, int k, int iBlockID) {
      if (!world.isUpdatePendingThisTickForBlock(i, j, k, this.blockID)) {
         world.scheduleBlockUpdate(i, j, k, this.blockID, this.tickRate(world));
      }
   }

   @Override
   public void updateTick(World world, int i, int j, int k, Random random) {
      boolean bIsJammed = this.isJammed(world, i, j, k);
      if (bIsJammed) {
         BlockPos sourcePos = new BlockPos(i, j, k);
         sourcePos.addFacingAsOffset(this.getFacing(world, i, j, k));
         int iSourceBlockID = world.getBlockId(sourcePos.x, sourcePos.y, sourcePos.z);
         if (iSourceBlockID != Block.waterMoving.blockID && iSourceBlockID != Block.waterStill.blockID) {
            this.setIsJammed(world, i, j, k, false);
         }
      }

      boolean bReceivingPower = this.isInputtingMechanicalPower(world, i, j, k);
      boolean bOn = this.isMechanicalOn(world, i, j, k);
      if (bReceivingPower != bOn) {
         this.setMechanicalOn(world, i, j, k, bReceivingPower);
         world.markBlockForUpdate(i, j, k);
         if (this.isPumpingWater(world, i, j, k)) {
            world.scheduleBlockUpdate(i, j, k, this.blockID, this.tickRate(world));
         }

         if (!bReceivingPower && this.isJammed(world, i, j, k)) {
            this.setIsJammed(world, i, j, k, false);
         }
      } else if (bOn) {
         if (this.isPumpingWater(world, i, j, k)) {
            boolean bSourceValidated = false;
            int iTargetBlockID = world.getBlockId(i, j + 1, k);
            if (iTargetBlockID != Block.waterMoving.blockID && iTargetBlockID != Block.waterStill.blockID) {
               if (world.isAirBlock(i, j + 1, k)) {
                  if (this.startPumpSourceCheck(world, i, j, k)) {
                     world.setBlockAndMetadataWithNotify(i, j + 1, k, Block.waterMoving.blockID, 7);
                     world.scheduleBlockUpdate(i, j, k, this.blockID, this.tickRate(world));
                  } else {
                     this.setIsJammed(world, i, j, k, true);
                  }
               }
            } else if (this.onNeighborChangeShortPumpSourceCheck(world, i, j, k)) {
               int iTargetHeight = world.getBlockMetadata(i, j + 1, k);
               if (iTargetHeight > 1 && iTargetHeight < 8) {
                  world.setBlockAndMetadataWithNotify(i, j + 1, k, Block.waterMoving.blockID, iTargetHeight - 1);
                  world.scheduleBlockUpdate(i, j, k, this.blockID, this.tickRate(world));
               }
            } else {
               this.setIsJammed(world, i, j, k, true);
            }
         } else {
            int iTargetBlockID = world.getBlockId(i, j + 1, k);
            if (iTargetBlockID == Block.waterMoving.blockID || iTargetBlockID == Block.waterStill.blockID) {
               Block.blocksList[iTargetBlockID].onNeighborBlockChange(world, i, j + 1, k, this.blockID);
            }
         }
      }
   }

   @Override
   public void randomUpdateTick(World world, int i, int j, int k, Random rand) {
      boolean bWasJammed = this.isJammed(world, i, j, k);
      boolean bMechanicalOn = this.isMechanicalOn(world, i, j, k);
      boolean bReceivingPower = this.isInputtingMechanicalPower(world, i, j, k);
      if (bReceivingPower != bMechanicalOn && !world.isUpdateScheduledForBlock(i, j, k, this.blockID)) {
         world.scheduleBlockUpdate(i, j, k, this.blockID, this.tickRate(world));
      } else {
         boolean bIsJammed;
         if (bMechanicalOn) {
            BlockPos sourcePos = new BlockPos(i, j, k);
            sourcePos.addFacingAsOffset(this.getFacing(world, i, j, k));
            int iSourceBlockID = world.getBlockId(sourcePos.x, sourcePos.y, sourcePos.z);
            if (iSourceBlockID != Block.waterMoving.blockID && iSourceBlockID != Block.waterStill.blockID) {
               bIsJammed = false;
            } else {
               int iDistanceToCheck = this.getRandomDistanceForSourceCheck(rand);
               bIsJammed = !MiscUtils.doesWaterHaveValidSource(world, sourcePos.x, sourcePos.y, sourcePos.z, iDistanceToCheck);
               if (!bIsJammed && bWasJammed) {
                  world.scheduleBlockUpdate(i, j, k, this.blockID, this.tickRate(world));
               }
            }
         } else {
            bIsJammed = false;
         }

         if (bWasJammed != bIsJammed) {
            this.setIsJammed(world, i, j, k, bIsJammed);
         }
      }
   }

   @Override
   public int getHarvestToolLevel(IBlockAccess blockAccess, int i, int j, int k) {
      return 2;
   }

   @Override
   public boolean dropComponentItemsOnBadBreak(World world, int i, int j, int k, int iMetadata, float fChanceOfDrop) {
      this.dropItemsIndividually(world, i, j, k, BTWItems.screw.itemID, 1, 0, fChanceOfDrop);
      this.dropItemsIndividually(world, i, j, k, Item.stick.itemID, 4, 0, fChanceOfDrop);
      this.dropItemsIndividually(world, i, j, k, BTWItems.sawDust.itemID, 4, 0, fChanceOfDrop);
      return true;
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
   public boolean isIncineratedInCrucible() {
      return false;
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
      return MechPowerUtils.isBlockPoweredByAxleToSide(world, i, j, k, 0);
   }

   @Override
   public boolean canInputAxlePowerToFacing(World world, int i, int j, int k, int iFacing) {
      return iFacing == 0;
   }

   @Override
   public boolean isOutputtingMechanicalPower(World world, int i, int j, int k) {
      return false;
   }

   @Override
   public void overpower(World world, int i, int j, int k) {
      this.breakScrewPump(world, i, j, k);
   }

   @Override
   public int isSourceToFluidBlockAtFacing(World world, int i, int j, int k, int iFacing) {
      if (iFacing == 1 && this.isPumpingWater(world, i, j, k)) {
         int iTargetBlockID = world.getBlockId(i, j + 1, k);
         if (iTargetBlockID == Block.waterMoving.blockID || iTargetBlockID == Block.waterStill.blockID) {
            int iSourceHeight = 0;
            int iTargetHeight = world.getBlockMetadata(i, j + 1, k);
            if (iTargetHeight > 0 && iTargetHeight < 8) {
               iSourceHeight = iTargetHeight - 1;
            }

            return iSourceHeight;
         }
      }

      return -1;
   }

   public boolean isMechanicalOn(IBlockAccess blockAccess, int i, int j, int k) {
      return (blockAccess.getBlockMetadata(i, j, k) & 4) > 0;
   }

   public void setMechanicalOn(World world, int i, int j, int k, boolean bOn) {
      int iMetadata = world.getBlockMetadata(i, j, k) & -5;
      if (bOn) {
         iMetadata |= 4;
      }

      world.setBlockMetadataWithNotify(i, j, k, iMetadata);
   }

   public boolean isJammed(IBlockAccess blockAccess, int i, int j, int k) {
      return (blockAccess.getBlockMetadata(i, j, k) & 8) > 0;
   }

   public void setIsJammed(World world, int i, int j, int k, boolean bJammed) {
      int iMetadata = world.getBlockMetadata(i, j, k) & -9;
      if (bJammed) {
         iMetadata |= 8;
      }

      world.setBlockMetadataWithNotify(i, j, k, iMetadata);
   }

   public boolean isPumpingWater(World world, int i, int j, int k) {
      if (this.isMechanicalOn(world, i, j, k) && !this.isJammed(world, i, j, k)) {
         BlockPos sourcePos = new BlockPos(i, j, k);
         sourcePos.addFacingAsOffset(this.getFacing(world, i, j, k));
         int iSourceBlockID = world.getBlockId(sourcePos.x, sourcePos.y, sourcePos.z);
         if (iSourceBlockID == Block.waterMoving.blockID || iSourceBlockID == Block.waterStill.blockID) {
            return true;
         }
      }

      return false;
   }

   private boolean startPumpSourceCheck(World world, int i, int j, int k) {
      BlockPos sourcePos = new BlockPos(i, j, k);
      sourcePos.addFacingAsOffset(this.getFacing(world, i, j, k));
      int iSourceBlockID = world.getBlockId(sourcePos.x, sourcePos.y, sourcePos.z);
      if (iSourceBlockID != Block.waterMoving.blockID && iSourceBlockID != Block.waterStill.blockID) {
         return false;
      } else {
         int iDistanceToCheck = 128;
         return MiscUtils.doesWaterHaveValidSource(world, sourcePos.x, sourcePos.y, sourcePos.z, iDistanceToCheck);
      }
   }

   private boolean onNeighborChangeShortPumpSourceCheck(World world, int i, int j, int k) {
      BlockPos sourcePos = new BlockPos(i, j, k);
      sourcePos.addFacingAsOffset(this.getFacing(world, i, j, k));
      int iSourceBlockID = world.getBlockId(sourcePos.x, sourcePos.y, sourcePos.z);
      if (iSourceBlockID != Block.waterMoving.blockID && iSourceBlockID != Block.waterStill.blockID) {
         return false;
      } else {
         int iDistanceToCheck = 4;
         return MiscUtils.doesWaterHaveValidSource(world, sourcePos.x, sourcePos.y, sourcePos.z, iDistanceToCheck);
      }
   }

   private int getRandomDistanceForSourceCheck(Random rand) {
      int iDistanceToCheck = 32;
      int iRandomFactor = rand.nextInt(32);
      if (iRandomFactor == 0) {
         iDistanceToCheck = 512;
      } else if (iRandomFactor <= 2) {
         iDistanceToCheck = 256;
      } else if (iRandomFactor <= 6) {
         iDistanceToCheck = 128;
      } else if (iRandomFactor <= 14) {
         iDistanceToCheck = 64;
      }

      return iDistanceToCheck;
   }

   private void breakScrewPump(World world, int i, int j, int k) {
      this.dropComponentItemsOnBadBreak(world, i, j, k, world.getBlockMetadata(i, j, k), 1.0F);
      world.playAuxSFX(2235, i, j, k, 0);
      world.setBlockWithNotify(i, j, k, 0);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void registerIcons(IconRegister register) {
      Icon sideIcon = register.registerIcon("fcBlockScrewPump_side");
      this.blockIcon = sideIcon;
      this.iconFront = register.registerIcon("fcBlockScrewPump_front");
      this.iconBySideArray[0] = register.registerIcon("fcBlockScrewPump_bottom");
      this.iconBySideArray[1] = register.registerIcon("fcBlockScrewPump_top");
      this.iconBySideArray[2] = sideIcon;
      this.iconBySideArray[3] = sideIcon;
      this.iconBySideArray[4] = sideIcon;
      this.iconBySideArray[5] = sideIcon;
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
      return iFacing == iSide ? this.iconFront : this.iconBySideArray[iSide];
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void randomDisplayTick(World world, int i, int j, int k, Random random) {
      if (this.isMechanicalOn(world, i, j, k)) {
         this.emitPoweredParticles(world, i, j, k, random);
      }
   }

   @Environment(EnvType.CLIENT)
   public void emitPoweredParticles(World world, int i, int j, int k, Random random) {
      int iBlockAboveID = world.getBlockId(i, j + 1, k);
      if (iBlockAboveID != Block.waterMoving.blockID && iBlockAboveID != Block.waterStill.blockID) {
         for (int counter = 0; counter < 5; counter++) {
            float smokeX = i + random.nextFloat();
            float smokeY = j + random.nextFloat() * 0.5F + 1.0F;
            float smokeZ = k + random.nextFloat();
            world.spawnParticle("smoke", smokeX, smokeY, smokeZ, 0.0, 0.0, 0.0);
         }
      } else {
         for (int counter = 0; counter < 5; counter++) {
            float smokeX = i + random.nextFloat();
            float smokeY = j + random.nextFloat() * 0.1F + 1.0F;
            float smokeZ = k + random.nextFloat();
            world.spawnParticle("bubble", smokeX, smokeY, smokeZ, 0.0, 0.0, 0.0);
         }
      }
   }
}
