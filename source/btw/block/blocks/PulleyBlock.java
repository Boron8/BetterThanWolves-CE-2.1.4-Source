package btw.block.blocks;

import btw.BTWMod;
import btw.block.BTWBlocks;
import btw.block.MechanicalBlock;
import btw.block.tileentity.PulleyTileEntity;
import btw.block.util.Flammability;
import btw.block.util.MechPowerUtils;
import btw.inventory.BTWContainers;
import btw.inventory.container.PulleyContainer;
import btw.inventory.util.InventoryUtils;
import btw.item.BTWItems;
import java.util.Random;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.BlockContainer;
import net.minecraft.src.Container;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EntityPlayerMP;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.IInventory;
import net.minecraft.src.Icon;
import net.minecraft.src.IconRegister;
import net.minecraft.src.Item;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;

public class PulleyBlock extends BlockContainer implements MechanicalBlock {
   private static final int PULLEY_TICK_RATE = 10;
   @Environment(EnvType.CLIENT)
   private Icon[] iconBySideArray = new Icon[6];

   public PulleyBlock(int iBlockID) {
      super(iBlockID, BTWBlocks.plankMaterial);
      this.c(2.0F);
      this.setAxesEffectiveOn(true);
      this.setBuoyancy(1.0F);
      this.setFireProperties(Flammability.PLANKS);
      this.a(g);
      this.c("fcBlockPulley");
      this.b(true);
      this.a(CreativeTabs.tabRedstone);
   }

   @Override
   public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer player, int iFacing, float fXClick, float fYClick, float fZClick) {
      if (!world.isRemote) {
         PulleyTileEntity tileEntityPulley = (PulleyTileEntity)world.getBlockTileEntity(i, j, k);
         if (player instanceof EntityPlayerMP) {
            PulleyContainer container = new PulleyContainer(player.inventory, tileEntityPulley);
            BTWMod.serverOpenCustomInterface((EntityPlayerMP)player, container, BTWContainers.pulleyContainerID);
         }
      }

      return true;
   }

   @Override
   public TileEntity createNewTileEntity(World world) {
      return new PulleyTileEntity();
   }

   @Override
   public void onBlockAdded(World world, int i, int j, int k) {
      super.onBlockAdded(world, i, j, k);
      world.scheduleBlockUpdate(i, j, k, this.blockID, this.tickRate(world));
   }

   @Override
   public void breakBlock(World world, int i, int j, int k, int iBlockID, int iMetadata) {
      TileEntity tileEntity = world.getBlockTileEntity(i, j, k);
      if (tileEntity != null) {
         InventoryUtils.ejectInventoryContents(world, i, j, k, (IInventory)tileEntity);
      }

      super.breakBlock(world, i, j, k, iBlockID, iMetadata);
   }

   @Override
   public int tickRate(World world) {
      return 10;
   }

   @Override
   public void updateTick(World world, int i, int j, int k, Random random) {
      boolean bReceivingPower = this.isInputtingMechanicalPower(world, i, j, k);
      boolean bOn = this.isBlockOn(world, i, j, k);
      boolean bStateChanged = false;
      if (bOn != bReceivingPower) {
         this.setBlockOn(world, i, j, k, bReceivingPower);
         bStateChanged = true;
      }

      boolean bRedstoneOn = this.isRedstoneOn(world, i, j, k);
      boolean bReceivingRedstone = world.isBlockGettingPowered(i, j, k) || world.isBlockGettingPowered(i, j + 1, k);
      if (bRedstoneOn != bReceivingRedstone) {
         this.setRedstoneOn(world, i, j, k, bReceivingRedstone);
         bStateChanged = true;
      }

      if (bStateChanged) {
         ((PulleyTileEntity)world.getBlockTileEntity(i, j, k)).notifyPulleyEntityOfBlockStateChange();
      }
   }

   @Override
   public void randomUpdateTick(World world, int i, int j, int k, Random rand) {
      if (!this.isCurrentStateValid(world, i, j, k) && !world.isUpdateScheduledForBlock(i, j, k, this.blockID)) {
         world.scheduleBlockUpdate(i, j, k, this.blockID, this.tickRate(world));
      }
   }

   @Override
   public void onNeighborBlockChange(World world, int i, int j, int k, int iBlockID) {
      if (!this.isCurrentStateValid(world, i, j, k) && !world.isUpdatePendingThisTickForBlock(i, j, k, this.blockID)) {
         world.scheduleBlockUpdate(i, j, k, this.blockID, this.tickRate(world));
      }
   }

   @Override
   public int getHarvestToolLevel(IBlockAccess blockAccess, int i, int j, int k) {
      return 2;
   }

   @Override
   public boolean dropComponentItemsOnBadBreak(World world, int i, int j, int k, int iMetadata, float fChanceOfDrop) {
      this.dropItemsIndividually(world, i, j, k, Item.stick.itemID, 2, 0, fChanceOfDrop);
      this.dropItemsIndividually(world, i, j, k, BTWItems.sawDust.itemID, 2, 0, fChanceOfDrop);
      this.dropItemsIndividually(world, i, j, k, BTWItems.gear.itemID, 1, 0, fChanceOfDrop);
      this.dropItemsIndividually(world, i, j, k, Item.goldNugget.itemID, 2, 0, fChanceOfDrop);
      this.dropItemsIndividually(world, i, j, k, Item.ingotIron.itemID, 1, 0, fChanceOfDrop);
      return true;
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
      return MechPowerUtils.isBlockPoweredByAxle(world, i, j, k, this) || MechPowerUtils.isBlockPoweredByHandCrank(world, i, j, k);
   }

   @Override
   public boolean canInputAxlePowerToFacing(World world, int i, int j, int k, int iFacing) {
      return iFacing >= 2;
   }

   @Override
   public boolean isOutputtingMechanicalPower(World world, int i, int j, int k) {
      return false;
   }

   @Override
   public void overpower(World world, int i, int j, int k) {
      this.breakPulley(world, i, j, k);
   }

   @Override
   public boolean hasComparatorInputOverride() {
      return true;
   }

   @Override
   public int getComparatorInputOverride(World par1World, int par2, int par3, int par4, int par5) {
      return Container.calcRedstoneFromInventory((IInventory)par1World.getBlockTileEntity(par2, par3, par4));
   }

   public boolean isBlockOn(IBlockAccess iBlockAccess, int i, int j, int k) {
      return (iBlockAccess.getBlockMetadata(i, j, k) & 1) > 0;
   }

   public void setBlockOn(World world, int i, int j, int k, boolean bOn) {
      int iMetaData = world.getBlockMetadata(i, j, k);
      if (bOn) {
         iMetaData |= 1;
      } else {
         iMetaData &= -2;
      }

      world.setBlockMetadataWithNotify(i, j, k, iMetaData);
   }

   public boolean isRedstoneOn(IBlockAccess iBlockAccess, int i, int j, int k) {
      return (iBlockAccess.getBlockMetadata(i, j, k) & 2) > 0;
   }

   public void setRedstoneOn(World world, int i, int j, int k, boolean bOn) {
      int iMetaData = world.getBlockMetadata(i, j, k) & -3;
      if (bOn) {
         iMetaData |= 2;
      }

      world.setBlockMetadataWithNotify(i, j, k, iMetaData);
   }

   void emitPulleyParticles(World world, int i, int j, int k, Random random) {
      for (int counter = 0; counter < 5; counter++) {
         float smokeX = i + random.nextFloat();
         float smokeY = j + random.nextFloat() * 0.5F + 1.0F;
         float smokeZ = k + random.nextFloat();
         world.spawnParticle("smoke", smokeX, smokeY, smokeZ, 0.0, 0.0, 0.0);
      }
   }

   public void breakPulley(World world, int i, int j, int k) {
      this.dropComponentItemsOnBadBreak(world, i, j, k, world.getBlockMetadata(i, j, k), 1.0F);
      world.playAuxSFX(2227, i, j, k, 0);
      world.setBlockWithNotify(i, j, k, 0);
   }

   public boolean isCurrentStateValid(World world, int i, int j, int k) {
      boolean bReceivingPower = this.isInputtingMechanicalPower(world, i, j, k);
      boolean bOn = this.isBlockOn(world, i, j, k);
      if (bReceivingPower != bOn) {
         return false;
      } else {
         boolean bRedstoneOn = this.isRedstoneOn(world, i, j, k);
         boolean bReceivingRedstone = world.isBlockGettingPowered(i, j, k) || world.isBlockGettingPowered(i, j + 1, k);
         return bRedstoneOn == bReceivingRedstone;
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void registerIcons(IconRegister register) {
      Icon sideIcon = register.registerIcon("fcBlockPulley_side");
      this.blockIcon = sideIcon;
      this.iconBySideArray[0] = register.registerIcon("fcBlockPulley_bottom");
      this.iconBySideArray[1] = register.registerIcon("fcBlockPulley_top");
      this.iconBySideArray[2] = sideIcon;
      this.iconBySideArray[3] = sideIcon;
      this.iconBySideArray[4] = sideIcon;
      this.iconBySideArray[5] = sideIcon;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public Icon getIcon(int iSide, int iMetadata) {
      return this.iconBySideArray[iSide];
   }
}
