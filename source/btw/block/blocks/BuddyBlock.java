package btw.block.blocks;

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
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.World;

public class BuddyBlock extends Block {
   private static final int TICK_RATE = 5;
   @Environment(EnvType.CLIENT)
   private Icon iconOn;
   @Environment(EnvType.CLIENT)
   private Icon iconFront;
   @Environment(EnvType.CLIENT)
   private Icon iconFrontOn;

   public BuddyBlock(int iBlockID) {
      super(iBlockID, Material.rock);
      this.c(3.5F);
      this.b(true);
      this.a(j);
      this.c("fcBlockBuddyBlock");
      this.a(CreativeTabs.tabRedstone);
   }

   @Override
   public int tickRate(World world) {
      return 5;
   }

   @Override
   public void onBlockAdded(World world, int i, int j, int k) {
      super.onBlockAdded(world, i, j, k);
      world.scheduleBlockUpdate(i, j, k, this.blockID, 1);
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
   public void onNeighborBlockChange(World world, int i, int j, int k, int iNeighborBlockID) {
      if (!this.isRedstoneOn(world, i, j, k)) {
         Block neighborBlock = r[iNeighborBlockID];
         if (neighborBlock != null && neighborBlock.triggersBuddy() && !world.isUpdatePendingThisTickForBlock(i, j, k, this.blockID)) {
            world.scheduleBlockUpdate(i, j, k, this.blockID, 1);
         }
      }
   }

   @Override
   public void updateTick(World world, int i, int j, int k, Random random) {
      if (this.isRedstoneOn(world, i, j, k)) {
         this.setBlockRedstoneOn(world, i, j, k, false);
      } else {
         this.setBlockRedstoneOn(world, i, j, k, true);
         world.scheduleBlockUpdate(i, j, k, this.blockID, this.tickRate(world));
      }
   }

   @Override
   public void randomUpdateTick(World world, int i, int j, int k, Random rand) {
      if (this.isRedstoneOn(world, i, j, k) && !world.isUpdateScheduledForBlock(i, j, k, this.blockID)) {
         world.scheduleBlockUpdate(i, j, k, this.blockID, this.tickRate(world));
      }
   }

   @Override
   public int isProvidingWeakPower(IBlockAccess blockAccess, int i, int j, int k, int iSide) {
      return this.getPowerProvided(blockAccess, i, j, k, iSide);
   }

   @Override
   public int isProvidingStrongPower(IBlockAccess blockAccess, int i, int j, int k, int iSide) {
      return this.getPowerProvided(blockAccess, i, j, k, iSide);
   }

   @Override
   public boolean canProvidePower() {
      return true;
   }

   @Override
   public int getFacing(int iMetadata) {
      return (iMetadata & -2) >> 1;
   }

   @Override
   public int setFacing(int iMetadata, int iFacing) {
      return iMetadata & 1 | iFacing << 1;
   }

   @Override
   public boolean toggleFacing(World world, int i, int j, int k, boolean bReverse) {
      int iFacing = this.getFacing(world, i, j, k);
      iFacing = Block.cycleFacing(iFacing, bReverse);
      this.setFacing(world, i, j, k, iFacing);
      world.markBlockRangeForRenderUpdate(i, j, k, i, j, k);
      return true;
   }

   @Override
   public boolean triggersBuddy() {
      return false;
   }

   @Override
   public int onPreBlockPlacedByPiston(World world, int i, int j, int k, int iMetadata, int iDirectionMoved) {
      BlockPos originPos = new BlockPos(i, j, k, getOppositeFacing(iDirectionMoved));
      this.notifyNeigborsToFacingOfPowerChange(world, originPos.x, originPos.y, originPos.z, this.getFacing(iMetadata));
      return iMetadata;
   }

   public int getPowerProvided(IBlockAccess blockAccess, int i, int j, int k, int iSide) {
      int iFacing = this.getFacing(blockAccess, i, j, k);
      return Block.getOppositeFacing(iSide) == iFacing && this.isRedstoneOn(blockAccess, i, j, k) ? 15 : 0;
   }

   public boolean isRedstoneOn(IBlockAccess iblockaccess, int i, int j, int k) {
      return (iblockaccess.getBlockMetadata(i, j, k) & 1) > 0;
   }

   public void setBlockRedstoneOn(World world, int i, int j, int k, boolean bOn) {
      if (bOn != this.isRedstoneOn(world, i, j, k)) {
         int iMetaData = world.getBlockMetadata(i, j, k);
         if (bOn) {
            iMetaData |= 1;
            world.playAuxSFX(2234, i, j, k, 0);
         } else {
            iMetaData &= -2;
         }

         world.setBlockMetadataWithClient(i, j, k, iMetaData);
         int iFacing = this.getFacing(world, i, j, k);
         this.notifyNeigborsToFacingOfPowerChange(world, i, j, k, iFacing);
         world.markBlockRangeForRenderUpdate(i, j, k, i, j, k);
      }
   }

   public void notifyNeigborsToFacingOfPowerChange(World world, int i, int j, int k, int iFacing) {
      BlockPos outputPos = new BlockPos(i, j, k);
      outputPos.addFacingAsOffset(iFacing);
      Block outputBlock = Block.blocksList[world.getBlockId(outputPos.x, outputPos.y, outputPos.z)];
      if (outputBlock != null) {
         outputBlock.onNeighborBlockChange(world, outputPos.x, outputPos.y, outputPos.z, this.blockID);
      }

      world.notifyBlocksOfNeighborChange(outputPos.x, outputPos.y, outputPos.z, this.blockID);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void registerIcons(IconRegister register) {
      super.registerIcons(register);
      this.iconOn = register.registerIcon("fcBlockBuddyBlock_on");
      this.iconFront = register.registerIcon("fcBlockBuddyBlock_front");
      this.iconFrontOn = register.registerIcon("fcBlockBuddyBlock_front_on");
   }

   @Environment(EnvType.CLIENT)
   @Override
   public Icon getIcon(int iSide, int iMetadata) {
      return iSide == 3 ? this.iconFront : this.blockIcon;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public Icon getBlockTexture(IBlockAccess blockAccess, int i, int j, int k, int iSide) {
      int iFacing = this.getFacing(blockAccess, i, j, k);
      if (iFacing == iSide) {
         return this.isRedstoneOn(blockAccess, i, j, k) ? this.iconFrontOn : this.iconFront;
      } else {
         return this.isRedstoneOn(blockAccess, i, j, k) ? this.iconOn : this.blockIcon;
      }
   }
}
