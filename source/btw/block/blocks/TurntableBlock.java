package btw.block.blocks;

import btw.block.MechanicalBlock;
import btw.block.tileentity.TurntableTileEntity;
import btw.block.util.MechPowerUtils;
import btw.client.render.util.RenderUtils;
import btw.item.BTWItems;
import btw.item.util.ItemUtils;
import java.util.Random;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.BlockContainer;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Icon;
import net.minecraft.src.IconRegister;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;

public class TurntableBlock extends BlockContainer implements MechanicalBlock {
   private static final int TURNTABLE_TICK_RATE = 10;
   @Environment(EnvType.CLIENT)
   private Icon[] iconBySideArray = new Icon[6];
   @Environment(EnvType.CLIENT)
   private Icon iconSwitch;

   public TurntableBlock(int blockID) {
      super(blockID, Material.rock);
      this.c(2.0F);
      this.a(j);
      this.c("fcBlockTurntable");
      this.a(CreativeTabs.tabRedstone);
   }

   @Override
   public int tickRate(World world) {
      return 10;
   }

   @Override
   public TileEntity createNewTileEntity(World world) {
      return new TurntableTileEntity();
   }

   @Override
   public void onBlockAdded(World world, int x, int y, int z) {
      super.onBlockAdded(world, x, y, z);
      world.scheduleBlockUpdate(x, y, z, this.blockID, this.tickRate(world));
   }

   @Override
   public void onNeighborBlockChange(World world, int x, int y, int z, int blockID) {
      if (!world.isUpdatePendingThisTickForBlock(x, y, z, this.blockID)) {
         world.scheduleBlockUpdate(x, y, z, this.blockID, this.tickRate(world));
      }
   }

   @Override
   public void updateTick(World world, int x, int y, int z, Random random) {
      boolean receivingMechanicalPower = this.isInputtingMechanicalPower(world, x, y, z);
      boolean isMechanicalOn = this.isBlockMechanicalOn(world, x, y, z);
      if (isMechanicalOn != receivingMechanicalPower) {
         this.emitTurntableParticles(world, x, y, z, random);
         this.setBlockMechanicalOn(world, x, y, z, receivingMechanicalPower);
         world.markBlockForUpdate(x, y, z);
      }

      boolean receivingRedstonePower = world.isBlockGettingPowered(x, y, z);
      boolean isRedstoneOn = this.isBlockRedstoneOn(world, x, y, z);
      if (isRedstoneOn != receivingRedstonePower) {
         this.setBlockRedstoneOn(world, x, y, z, receivingRedstonePower);
      }
   }

   @Override
   public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int facing, float hitX, float hitY, float hitZ) {
      ItemStack playerEquippedItem = player.getCurrentEquippedItem();
      if (playerEquippedItem != null) {
         return false;
      } else {
         if (!world.isRemote) {
            int switchSetting = this.getSwitchSetting(world, x, y, z);
            if (++switchSetting > 3) {
               switchSetting = 0;
            }

            this.setSwitchSetting(world, x, y, z, switchSetting);
            world.markBlockForUpdate(x, y, z);
            world.markBlockRangeForRenderUpdate(x, y, z, x, y, z);
            world.playAuxSFX(1001, x, y, z, 0);
         }

         return true;
      }
   }

   @Override
   public boolean canRotateOnTurntable(IBlockAccess blockAccess, int x, int y, int z) {
      return false;
   }

   @Override
   public boolean canTransmitRotationHorizontallyOnTurntable(IBlockAccess blockAccess, int x, int y, int z) {
      return false;
   }

   @Override
   public boolean canTransmitRotationVerticallyOnTurntable(IBlockAccess blockAccess, int x, int y, int z) {
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
   public boolean isInputtingMechanicalPower(World world, int x, int y, int z) {
      return MechPowerUtils.isBlockPoweredByAxleToSide(world, x, y, z, 0);
   }

   @Override
   public boolean isOutputtingMechanicalPower(World world, int i, int j, int k) {
      return false;
   }

   @Override
   public boolean canInputAxlePowerToFacing(World world, int x, int y, int z, int facing) {
      return facing == 0;
   }

   @Override
   public void overpower(World world, int x, int y, int z) {
      this.breakTurntable(world, x, y, z);
   }

   public boolean isBlockMechanicalOn(IBlockAccess blockAccess, int x, int y, int z) {
      return (blockAccess.getBlockMetadata(x, y, z) & 1) > 0;
   }

   public void setBlockMechanicalOn(World world, int x, int y, int z, boolean isOn) {
      int metaData = world.getBlockMetadata(x, y, z) & -2;
      if (isOn) {
         metaData |= 1;
      }

      world.setBlockMetadataWithNotify(x, y, z, metaData);
   }

   public boolean isBlockRedstoneOn(IBlockAccess blockAccess, int x, int y, int z) {
      return (blockAccess.getBlockMetadata(x, y, z) & 2) > 0;
   }

   public void setBlockRedstoneOn(World world, int x, int y, int z, boolean isOn) {
      int metaData = world.getBlockMetadata(x, y, z) & -3;
      if (isOn) {
         metaData |= 2;
      }

      world.setBlockMetadataWithNotify(x, y, z, metaData);
   }

   public int getSwitchSetting(IBlockAccess iBlockAccess, int x, int y, int z) {
      return (iBlockAccess.getBlockMetadata(x, y, z) & 12) >> 2;
   }

   public void setSwitchSetting(World world, int x, int y, int z, int setting) {
      if (setting >= 4 || setting < 0) {
         setting = 0;
      }

      int metadata = world.getBlockMetadata(x, y, z) & -13;
      metadata |= setting << 2;
      world.setBlockMetadataWithNotify(x, y, z, metadata);
   }

   public void emitTurntableParticles(World world, int x, int y, int z, Random random) {
      for (int i = 0; i < 5; i++) {
         float smokeX = x + random.nextFloat();
         float smokeY = y + random.nextFloat() * 0.5F + 1.0F;
         float smokeZ = z + random.nextFloat();
         world.spawnParticle("smoke", smokeX, smokeY, smokeZ, 0.0, 0.0, 0.0);
      }
   }

   private void breakTurntable(World world, int x, int y, int z) {
      ItemUtils.ejectSingleItemWithRandomOffset(world, x, y, z, Item.redstone.itemID, 0);
      this.dropItemsIndividually(world, x, y, z, BTWItems.stone.itemID, 16, 0, 0.75F);

      for (int i = 0; i < 2; i++) {
         ItemUtils.ejectSingleItemWithRandomOffset(world, x, y, z, BTWItems.woodSidingStubID, 0);
      }

      world.playAuxSFX(2235, x, y, z, 0);
      world.setBlockWithNotify(x, y, z, 0);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void registerIcons(IconRegister register) {
      this.blockIcon = register.registerIcon("stone");
      this.iconBySideArray[0] = register.registerIcon("fcBlockTurntable_bottom");
      this.iconBySideArray[1] = register.registerIcon("fcBlockTurntable_top");
      Icon sideIcon = register.registerIcon("fcBlockTurntable_side");
      this.iconBySideArray[2] = sideIcon;
      this.iconBySideArray[3] = sideIcon;
      this.iconBySideArray[4] = sideIcon;
      this.iconBySideArray[5] = sideIcon;
      this.iconSwitch = register.registerIcon("fcBlockTurntable_switch");
   }

   @Environment(EnvType.CLIENT)
   @Override
   public Icon getIcon(int iSide, int iMetadata) {
      return this.iconBySideArray[iSide];
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void randomDisplayTick(World world, int x, int y, int z, Random random) {
      if (this.isBlockMechanicalOn(world, x, y, z)) {
         this.emitTurntableParticles(world, x, y, z, random);
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean shouldSideBeRendered(IBlockAccess blockAccess, int neighborX, int neighborY, int neighborZ, int side) {
      return true;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean renderBlock(RenderBlocks renderBlocks, int x, int y, int z) {
      super.renderBlock(renderBlocks, x, y, z);
      int switchSetting = this.getSwitchSetting(renderBlocks.blockAccess, x, y, z);
      float switchOffset = 0.25F + switchSetting * 0.125F;
      renderBlocks.setRenderBounds(switchOffset, 0.3125, 0.0625, switchOffset + 0.125F, 0.4375, 1.0625);
      RenderUtils.renderStandardBlockWithTexture(renderBlocks, this, x, y, z, this.iconSwitch);
      renderBlocks.setRenderBounds(1.0F - (switchOffset + 0.125F), 0.3125, -0.0625, 1.0F - switchOffset, 0.4375, 0.9375);
      RenderUtils.renderStandardBlockWithTexture(renderBlocks, this, x, y, z, this.iconSwitch);
      renderBlocks.setRenderBounds(0.0625, 0.3125, 1.0F - (switchOffset + 0.125F), 1.0625, 0.4375, 1.0F - switchOffset);
      RenderUtils.renderStandardBlockWithTexture(renderBlocks, this, x, y, z, this.iconSwitch);
      renderBlocks.setRenderBounds(-0.0625, 0.3125, switchOffset, 0.9375, 0.4375, switchOffset + 0.125F);
      RenderUtils.renderStandardBlockWithTexture(renderBlocks, this, x, y, z, this.iconSwitch);
      return true;
   }
}
