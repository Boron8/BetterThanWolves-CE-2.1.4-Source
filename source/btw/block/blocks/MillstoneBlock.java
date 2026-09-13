package btw.block.blocks;

import btw.block.MechanicalBlock;
import btw.block.model.MillstoneModel;
import btw.block.tileentity.MillstoneTileEntity;
import btw.block.util.MechPowerUtils;
import btw.block.util.RayTraceUtils;
import btw.inventory.util.InventoryUtils;
import btw.item.BTWItems;
import java.util.Random;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.BlockContainer;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.IInventory;
import net.minecraft.src.Icon;
import net.minecraft.src.IconRegister;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.MovingObjectPosition;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.TileEntity;
import net.minecraft.src.Vec3;
import net.minecraft.src.World;

public class MillstoneBlock extends BlockContainer implements MechanicalBlock {
   private static final int TICK_RATE = 10;
   public static final int CONTENTS_NOTHING = 0;
   public static final int CONTENTS_NORMAL_GRINDING = 1;
   public static final int CONTENTS_NETHERRACK = 2;
   public static final int CONTENTS_COMPANION_CUBE = 3;
   public static final int CONTENTS_JAMMED = 4;
   public static final MillstoneModel model = new MillstoneModel();
   @Environment(EnvType.CLIENT)
   private final Icon[] iconsBySide = new Icon[6];
   @Environment(EnvType.CLIENT)
   private final Icon[] iconsBySideFull = new Icon[6];
   @Environment(EnvType.CLIENT)
   private final Icon[] iconsBySideOn = new Icon[6];
   @Environment(EnvType.CLIENT)
   private final Icon[] iconsBySideOnFull = new Icon[6];
   @Environment(EnvType.CLIENT)
   private boolean renderingBase = false;

   public MillstoneBlock(int iBlockID) {
      super(iBlockID, Material.rock);
      this.c(3.5F);
      this.a(j);
      this.c("fcBlockMillStone");
      this.b(true);
      this.a(CreativeTabs.tabRedstone);
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
   public int tickRate(World world) {
      return 10;
   }

   @Override
   public void onBlockAdded(World world, int i, int j, int k) {
      super.onBlockAdded(world, i, j, k);
      world.scheduleBlockUpdate(i, j, k, this.blockID, this.tickRate(world));
   }

   @Override
   public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer player, int iFacing, float fXClick, float fYClick, float fZClick) {
      int iState = this.getCurrentGrindingType(world, i, j, k);
      MillstoneTileEntity tileEntity = (MillstoneTileEntity)world.getBlockTileEntity(i, j, k);
      if (iState == 0) {
         ItemStack heldStack = player.getCurrentEquippedItem();
         if (heldStack != null) {
            if (!world.isRemote) {
               world.playAuxSFX(2231, i, j, k, 0);
               tileEntity.attemptToAddSingleItemFromStack(heldStack);
            } else {
               heldStack.stackSize--;
            }
         }
      } else if (!world.isRemote) {
         world.playAuxSFX(2231, i, j, k, 0);
         tileEntity.ejectContents(iFacing);
      }

      return true;
   }

   @Override
   public void breakBlock(World world, int i, int j, int k, int iBlockID, int iMetadata) {
      InventoryUtils.ejectInventoryContents(world, i, j, k, (IInventory)world.getBlockTileEntity(i, j, k));
      super.breakBlock(world, i, j, k, iBlockID, iMetadata);
   }

   @Override
   public TileEntity createNewTileEntity(World world) {
      return new MillstoneTileEntity();
   }

   @Override
   public void updateTick(World world, int i, int j, int k, Random random) {
      boolean bReceivingPower = this.isInputtingMechanicalPower(world, i, j, k);
      boolean bOn = this.getIsMechanicalOn(world, i, j, k);
      if (bOn != bReceivingPower) {
         this.setIsMechanicalOn(world, i, j, k, bReceivingPower);
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
   public MovingObjectPosition collisionRayTrace(World world, int i, int j, int k, Vec3 startRay, Vec3 endRay) {
      RayTraceUtils rayTrace = new RayTraceUtils(world, i, j, k, startRay, endRay);
      model.addToRayTrace(rayTrace);
      rayTrace.addBoxWithLocalCoordsToIntersectionList(model.boxBase);
      return rayTrace.getFirstIntersection();
   }

   @Override
   public boolean hasCenterHardPointToFacing(IBlockAccess blockAccess, int i, int j, int k, int iFacing, boolean bIgnoreTransparency) {
      return true;
   }

   @Override
   public boolean hasLargeCenterHardPointToFacing(IBlockAccess blockAccess, int i, int j, int k, int iFacing, boolean bIgnoreTransparency) {
      return iFacing == 0 || bIgnoreTransparency;
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
      return iFacing < 2;
   }

   @Override
   public boolean isOutputtingMechanicalPower(World world, int i, int j, int k) {
      return false;
   }

   @Override
   public void overpower(World world, int i, int j, int k) {
      this.breakMillStone(world, i, j, k);
   }

   @Override
   public boolean hasComparatorInputOverride() {
      return true;
   }

   @Override
   public int getComparatorInputOverride(World par1World, int par2, int par3, int par4, int par5) {
      return ((MillstoneTileEntity)par1World.getBlockTileEntity(par2, par3, par4)).stackMilling == null ? 0 : 15;
   }

   public boolean getIsMechanicalOn(IBlockAccess blockAccess, int i, int j, int k) {
      return this.getIsMechanicalOn(blockAccess.getBlockMetadata(i, j, k));
   }

   public boolean getIsMechanicalOn(int iMetadata) {
      return (iMetadata & 1) > 0;
   }

   public void setIsMechanicalOn(World world, int i, int j, int k, boolean bOn) {
      int iMetadata = this.setIsMechanicalOn(world.getBlockMetadata(i, j, k), bOn);
      world.setBlockMetadataWithNotify(i, j, k, iMetadata);
   }

   public int setIsMechanicalOn(int iMetadata, boolean bOn) {
      return bOn ? iMetadata | 1 : iMetadata & -2;
   }

   public int getCurrentGrindingType(IBlockAccess blockAccess, int i, int j, int k) {
      return this.getCurrentGrindingType(blockAccess.getBlockMetadata(i, j, k));
   }

   public int getCurrentGrindingType(int iMetadata) {
      return (iMetadata & 14) >> 1;
   }

   public void setCurrentGrindingType(World world, int i, int j, int k, int iGrindingType) {
      int iMetadata = this.setCurrentGrindingType(world.getBlockMetadata(i, j, k), iGrindingType);
      world.setBlockMetadataWithClient(i, j, k, iMetadata);
   }

   public int setCurrentGrindingType(int iMetadata, int iGrindingType) {
      iMetadata &= -15;
      return iMetadata | iGrindingType << 1;
   }

   private void breakMillStone(World world, int i, int j, int k) {
      this.dropItemsIndividually(world, i, j, k, BTWItems.stone.itemID, 16, 0, 0.75F);
      world.playAuxSFX(2235, i, j, k, 0);
      world.setBlockWithNotify(i, j, k, 0);
   }

   public boolean isCurrentStateValid(World world, int i, int j, int k) {
      boolean bReceivingPower = this.isInputtingMechanicalPower(world, i, j, k);
      boolean bOn = this.getIsMechanicalOn(world, i, j, k);
      return bOn == bReceivingPower;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void registerIcons(IconRegister register) {
      this.blockIcon = register.registerIcon("stone");
      this.iconsBySideFull[0] = this.iconsBySide[0] = register.registerIcon("fcBlockMillStone_bottom");
      this.iconsBySideFull[1] = this.iconsBySide[1] = register.registerIcon("fcBlockMillStone_top");
      this.iconsBySideOn[0] = this.iconsBySideOnFull[0] = register.registerIcon("fcBlockMillStone_bottom_on");
      this.iconsBySideOn[1] = this.iconsBySideOnFull[1] = register.registerIcon("fcBlockMillStone_top_on");
      Icon sideIcon = register.registerIcon("fcBlockMillStone_side");
      Icon sideIconFull = register.registerIcon("fcBlockMillStone_side_full");
      Icon sideIconOn = register.registerIcon("fcBlockMillStone_side_on");
      Icon sideIconOnFull = register.registerIcon("fcBlockMillStone_side_on_full");

      for (int iTempSide = 2; iTempSide <= 5; iTempSide++) {
         this.iconsBySide[iTempSide] = sideIcon;
         this.iconsBySideFull[iTempSide] = sideIconFull;
         this.iconsBySideOn[iTempSide] = sideIconOn;
         this.iconsBySideOnFull[iTempSide] = sideIconOnFull;
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public Icon getIcon(int iSide, int iMetadata) {
      if (this.renderingBase) {
         return this.iconsBySide[iSide];
      } else if (this.getIsMechanicalOn(iMetadata)) {
         return this.getCurrentGrindingType(iMetadata) == 0 ? this.iconsBySideOn[iSide] : this.iconsBySideOnFull[iSide];
      } else {
         return this.getCurrentGrindingType(iMetadata) == 0 ? this.iconsBySide[iSide] : this.iconsBySideFull[iSide];
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean shouldSideBeRendered(IBlockAccess blockAccess, int iNeighborI, int iNeighborJ, int iNeighborK, int iSide) {
      return true;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean renderBlock(RenderBlocks renderBlocks, int i, int j, int k) {
      this.renderingBase = true;
      model.boxBase.renderAsBlock(renderBlocks, this, i, j, k);
      this.renderingBase = false;
      return model.renderAsBlock(renderBlocks, this, i, j, k);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void renderBlockAsItem(RenderBlocks renderBlocks, int iItemDamage, float fBrightness) {
      this.renderingBase = true;
      model.boxBase.renderAsItemBlock(renderBlocks, this, iItemDamage);
      this.renderingBase = false;
      model.renderAsItemBlock(renderBlocks, this, iItemDamage);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void randomDisplayTick(World world, int i, int j, int k, Random random) {
      if (this.getIsMechanicalOn(world, i, j, k)) {
         int iCurrentGrindingType = this.getCurrentGrindingType(world, i, j, k);
         this.emitMillingParticles(world, i, j, k, iCurrentGrindingType, random);
         if (iCurrentGrindingType == 4) {
            world.playSound(i + 0.5, j + 0.5, k + 0.5, "minecart.base", 1.0F + world.rand.nextFloat() * 0.1F, 1.25F + world.rand.nextFloat() * 0.1F);
         } else if (iCurrentGrindingType != 0) {
            world.playSound(i + 0.5, j + 0.5, k + 0.5, "minecart.base", 1.0F + world.rand.nextFloat() * 0.1F, 0.75F + world.rand.nextFloat() * 0.1F);
         } else if (random.nextInt(2) == 0) {
            world.playSound(i + 0.5, j + 0.5, k + 0.5, "minecart.base", 1.5F + random.nextFloat() * 0.1F, 0.5F + random.nextFloat() * 0.1F);
         }

         if (iCurrentGrindingType == 2) {
            if (random.nextInt(3) <= 1) {
               world.playSound(i + 0.5, j + 0.5, k + 0.5, "mob.ghast.scream", 0.75F, random.nextFloat() * 0.4F + 0.8F);
            }
         } else if (iCurrentGrindingType == 3 && random.nextInt(3) <= 1) {
            world.playSound(i + 0.5F, j + 0.5F, k + 0.5F, "mob.wolf.hurt", 2.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F);
         }
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int i, int j, int k) {
      AxisAlignedBB transformedBox = model.boxSelection.makeTemporaryCopy();
      transformedBox.offset(i, j, k);
      return transformedBox;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void clientNotificationOfMetadataChange(World world, int i, int j, int k, int iOldMetadata, int iNewMetadata) {
      if (!this.getIsMechanicalOn(iOldMetadata) && this.getIsMechanicalOn(iNewMetadata)) {
         int iGrindType = this.getCurrentGrindingType(iNewMetadata);
         if (iGrindType == 3) {
            world.playSound(i + 0.5, j + 0.5, k + 0.5, "mob.wolf.hurt", 5.0F, (world.rand.nextFloat() - world.rand.nextFloat()) * 0.2F + 1.0F);
         }

         world.playSound(i + 0.5, j + 0.5, k + 0.5, "minecart.base", 1.5F + world.rand.nextFloat() * 0.1F, 0.5F + world.rand.nextFloat() * 0.1F);
         this.emitMillingParticles(world, i, j, k, iGrindType, world.rand);
      }
   }

   @Environment(EnvType.CLIENT)
   private void emitMillingParticles(World world, int i, int j, int k, int iGrindType, Random rand) {
      String sParticle;
      if (iGrindType == 0) {
         sParticle = "smoke";
      } else if (iGrindType == 4) {
         sParticle = "largesmoke";
      } else {
         sParticle = "fcwhitesmoke";
      }

      for (int iTempCount = 0; iTempCount < 5; iTempCount++) {
         float smokeX = i + rand.nextFloat();
         float smokeY = j + rand.nextFloat() * 0.5F + 1.0F;
         float smokeZ = k + rand.nextFloat();
         world.spawnParticle(sParticle, smokeX, smokeY, smokeZ, 0.0, 0.0, 0.0);
      }
   }
}
