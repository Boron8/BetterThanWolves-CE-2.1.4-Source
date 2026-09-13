package btw.block.blocks;

import btw.BTWMod;
import btw.block.BTWBlocks;
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
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Icon;
import net.minecraft.src.IconRegister;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;

public class GearBoxBlock extends Block implements MechanicalBlock {
   public static final int TICK_RATE = 10;
   private static final int TURN_ON_TICK_RATE = 10;
   private static final int TURN_OFF_TICK_RATE = 9;
   @Environment(EnvType.CLIENT)
   private Icon iconInput;
   @Environment(EnvType.CLIENT)
   private Icon iconOutput;

   public GearBoxBlock(int iBlockID) {
      super(iBlockID, BTWBlocks.plankMaterial);
      this.c(2.0F);
      this.setAxesEffectiveOn(true);
      this.setBuoyant();
      this.setFireProperties(Flammability.PLANKS);
      this.a(g);
      this.b(true);
      this.c("fcBlockGearBox");
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
      world.scheduleBlockUpdate(i, j, k, this.blockID, this.tickRate(world));
   }

   @Override
   public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer player, int iFacing, float fXClick, float fYClick, float fZClick) {
      if (player.getCurrentEquippedItem() == null && !MechPowerUtils.doesBlockHaveAnyFacingAxles(world, i, j, k)) {
         if (!world.isRemote) {
            this.toggleFacing(world, i, j, k, false);
            MiscUtils.playPlaceSoundForBlock(world, i, j, k);
         }

         return true;
      } else {
         return false;
      }
   }

   @Override
   public void updateTick(World world, int i, int j, int k, Random rand) {
      boolean bMechPowered = this.isInputtingMechanicalPower(world, i, j, k);
      this.updateMechPoweredState(world, i, j, k, bMechPowered);
   }

   @Override
   public void randomUpdateTick(World world, int i, int j, int k, Random rand) {
      if (!this.isCurrentStateValid(world, i, j, k) && !world.isUpdateScheduledForBlock(i, j, k, this.blockID)) {
         world.scheduleBlockUpdate(i, j, k, this.blockID, this.tickRate(world));
      }
   }

   @Override
   public void onNeighborBlockChange(World world, int i, int j, int k, int iBlockID) {
      if (!this.isCurrentStateValid(world, i, j, k)
         && !world.isUpdateScheduledForBlock(i, j, k, this.blockID)
         && !world.isUpdatePendingThisTickForBlock(i, j, k, this.blockID)) {
         if (!BTWMod.disableGearBoxPowerDrain && this.isGearBoxOn(world, i, j, k)) {
            world.scheduleBlockUpdate(i, j, k, this.blockID, 9);
         } else {
            world.scheduleBlockUpdate(i, j, k, this.blockID, 10);
         }
      }
   }

   @Override
   public int getMechanicalPowerLevelProvidedToAxleAtFacing(World world, int i, int j, int k, int iFacing) {
      return this.isGearBoxOn(world, i, j, k) && this.getFacing(world, i, j, k) != iFacing ? 4 : 0;
   }

   @Override
   public int getHarvestToolLevel(IBlockAccess blockAccess, int i, int j, int k) {
      return 2;
   }

   @Override
   public boolean dropComponentItemsOnBadBreak(World world, int i, int j, int k, int iMetadata, float fChanceOfDrop) {
      this.dropItemsIndividually(world, i, j, k, Item.stick.itemID, 2, 0, fChanceOfDrop);
      this.dropItemsIndividually(world, i, j, k, BTWItems.sawDust.itemID, 3, 0, fChanceOfDrop);
      this.dropItemsIndividually(world, i, j, k, BTWItems.gear.itemID, 2, 0, fChanceOfDrop);
      return true;
   }

   @Override
   public int getFacing(int iMetadata) {
      return iMetadata & 7;
   }

   @Override
   public int setFacing(int iMetadata, int iFacing) {
      iMetadata &= 8;
      return iMetadata | iFacing;
   }

   @Override
   public boolean rotateAroundJAxis(World world, int i, int j, int k, boolean bReverse) {
      int iFacing = this.getFacing(world, i, j, k);
      int iNewFacing = Block.rotateFacingAroundY(iFacing, bReverse);
      if (iNewFacing != iFacing) {
         if (this.isGearBoxOn(world, i, j, k)) {
            this.setGearBoxOn(world, i, j, k, false);
         }

         this.setFacing(world, i, j, k, iNewFacing);
         world.markBlockRangeForRenderUpdate(i, j, k, i, j, k);
         world.scheduleBlockUpdate(i, j, k, this.blockID, this.tickRate(world));
         MechPowerUtils.destroyHorizontallyAttachedAxles(world, i, j, k);
         return true;
      } else {
         return false;
      }
   }

   @Override
   public boolean toggleFacing(World world, int i, int j, int k, boolean bReverse) {
      if (this.isGearBoxOn(world, i, j, k)) {
         this.setGearBoxOn(world, i, j, k, false);
      }

      int iFacing = this.getFacing(world, i, j, k);
      iFacing = Block.cycleFacing(iFacing, bReverse);
      this.setFacing(world, i, j, k, iFacing);
      world.markBlockRangeForRenderUpdate(i, j, k, i, j, k);
      world.scheduleBlockUpdate(i, j, k, this.blockID, this.tickRate(world));
      world.notifyBlockChange(i, j, k, this.blockID);
      return true;
   }

   @Override
   public boolean canOutputMechanicalPower() {
      return true;
   }

   @Override
   public boolean canInputMechanicalPower() {
      return true;
   }

   @Override
   public boolean isInputtingMechanicalPower(World world, int i, int j, int k) {
      return MechPowerUtils.isBlockPoweredByAxleToSide(world, i, j, k, this.getFacing(world, i, j, k));
   }

   @Override
   public boolean canInputAxlePowerToFacing(World world, int i, int j, int k, int iFacing) {
      int iBlockFacing = this.getFacing(world, i, j, k);
      return iFacing == iBlockFacing;
   }

   @Override
   public boolean isOutputtingMechanicalPower(World world, int i, int j, int k) {
      return this.isGearBoxOn(world, i, j, k);
   }

   @Override
   public void overpower(World world, int i, int j, int k) {
      if (this.isGearBoxOn(world, i, j, k)) {
         this.breakGearBox(world, i, j, k);
      }
   }

   protected void updateMechPoweredState(World world, int i, int j, int k, boolean bShouldBePowered) {
      if (this.isGearBoxOn(world, i, j, k) != bShouldBePowered) {
         this.setGearBoxOn(world, i, j, k, bShouldBePowered);
      }
   }

   protected boolean isCurrentStateValid(World world, int i, int j, int k) {
      return this.isGearBoxOn(world, i, j, k) == this.isInputtingMechanicalPower(world, i, j, k);
   }

   public boolean isGearBoxOn(IBlockAccess blockAccess, int i, int j, int k) {
      return this.isGearBoxOn(blockAccess.getBlockMetadata(i, j, k));
   }

   public boolean isGearBoxOn(int iMetadata) {
      return (iMetadata & 8) > 0;
   }

   public int setGearBoxOn(int iMetadata, boolean bOn) {
      iMetadata &= 7;
      if (bOn) {
         iMetadata |= 8;
      }

      return iMetadata;
   }

   public void setGearBoxOn(World world, int i, int j, int k, boolean bOn) {
      int iMetadata = this.setGearBoxOn(world.getBlockMetadata(i, j, k), bOn);
      world.setBlockMetadataWithNotify(i, j, k, iMetadata);
   }

   public void breakGearBox(World world, int i, int j, int k) {
      this.dropComponentItemsOnBadBreak(world, i, j, k, world.getBlockMetadata(i, j, k), 1.0F);
      world.playAuxSFX(2235, i, j, k, 0);
      world.setBlockWithNotify(i, j, k, 0);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void registerIcons(IconRegister register) {
      super.registerIcons(register);
      this.iconInput = register.registerIcon(this.B() + "_input");
      this.iconOutput = register.registerIcon(this.B() + "_output");
   }

   @Environment(EnvType.CLIENT)
   @Override
   public Icon getIcon(int iSide, int iMetadata) {
      return iSide == 3 ? this.iconInput : this.blockIcon;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public Icon getBlockTexture(IBlockAccess blockAccess, int i, int j, int k, int iSide) {
      int iFacing = this.getFacing(blockAccess, i, j, k);
      if (iSide == iFacing) {
         return this.iconInput;
      } else {
         BlockPos sideBlockPos = new BlockPos(i, j, k);
         sideBlockPos.addFacingAsOffset(iSide);
         if (blockAccess.getBlockId(sideBlockPos.x, sideBlockPos.y, sideBlockPos.z) == BTWBlocks.axle.blockID
            && ((AxleBlock)BTWBlocks.axle).isAxleOrientedTowardsFacing(blockAccess, sideBlockPos.x, sideBlockPos.y, sideBlockPos.z, iSide)) {
            return this.iconOutput;
         } else if (iSide == Block.getOppositeFacing(iFacing)) {
            for (int iTempFacing = 0; iTempFacing <= 5; iTempFacing++) {
               if (iTempFacing != iFacing && MechPowerUtils.doesBlockHaveFacingAxleToSide(blockAccess, i, j, k, iTempFacing)) {
                  return this.blockIcon;
               }
            }

            return this.iconOutput;
         } else {
            return this.blockIcon;
         }
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void randomDisplayTick(World world, int i, int j, int k, Random random) {
      if (this.isGearBoxOn(world, i, j, k)) {
         this.emitGearBoxParticles(world, i, j, k, random);
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void clientNotificationOfMetadataChange(World world, int i, int j, int k, int iOldMetadata, int iNewMetadata) {
      if (!this.isGearBoxOn(iOldMetadata) && this.isGearBoxOn(iNewMetadata)) {
         world.playSound(i + 0.5, j + 0.5, k + 0.5, "random.chestopen", 0.25F, world.rand.nextFloat() * 0.25F + 0.25F);
         this.emitGearBoxParticles(world, i, j, k, world.rand);
      }

      world.markBlockRangeForRenderUpdate(i - 3, j - 3, k - 3, i + 3, j + 3, k + 3);
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
   private void emitGearBoxParticles(World world, int i, int j, int k, Random random) {
      for (int iTempCount = 0; iTempCount < 5; iTempCount++) {
         float smokeX = i + random.nextFloat();
         float smokeY = j + random.nextFloat() * 0.5F + 1.0F;
         float smokeZ = k + random.nextFloat();
         world.spawnParticle("smoke", smokeX, smokeY, smokeZ, 0.0, 0.0, 0.0);
      }
   }
}
