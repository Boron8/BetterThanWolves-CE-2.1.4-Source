package btw.block.blocks;

import btw.block.BTWBlocks;
import btw.block.util.Flammability;
import btw.entity.MiningChargeEntity;
import btw.world.util.BlockPos;
import btw.world.util.WorldUtils;
import java.util.Random;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EntityList;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Explosion;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Icon;
import net.minecraft.src.IconRegister;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.World;

public class MiningChargeBlock extends Block {
   public static final double BOUNDING_BOX_HEIGHT = 0.5;
   private static final int TICK_RATE = 1;
   @Environment(EnvType.CLIENT)
   private Icon iconBottom;
   @Environment(EnvType.CLIENT)
   private Icon iconTop;
   @Environment(EnvType.CLIENT)
   private Icon iconSide;
   @Environment(EnvType.CLIENT)
   private Icon iconSideFlipped;

   public MiningChargeBlock(int iBlockID) {
      super(iBlockID, Material.tnt);
      this.c(0.0F);
      this.setFireProperties(Flammability.EXPLOSIVES);
      this.initBlockBounds(0.0, 0.0, 0.0, 1.0, 0.5, 1.0);
      this.a(i);
      this.c("fcBlockMiningCharge");
      this.b(true);
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
   public int quantityDropped(Random random) {
      return 0;
   }

   @Override
   public boolean canPlaceBlockAt(World world, int i, int j, int k) {
      for (int iTempFacing = 0; iTempFacing <= 5; iTempFacing++) {
         if (this.isValidAnchorToFacing(world, i, j, k, iTempFacing)) {
            return true;
         }
      }

      return false;
   }

   @Override
   public void onBlockAdded(World world, int i, int j, int k) {
      super.onBlockAdded(world, i, j, k);
      world.scheduleBlockUpdate(i, j, k, this.blockID, this.tickRate(world));
   }

   @Override
   public int onBlockPlaced(World world, int i, int j, int k, int iFacing, float fClickX, float fClickY, float fClickZ, int iMetadata) {
      iFacing = Block.getOppositeFacing(iFacing);
      if (!this.isValidAnchorToFacing(world, i, j, k, iFacing)) {
         iFacing = 0;

         for (int iTempFacing = 0; iTempFacing <= 5; iTempFacing++) {
            if (this.isValidAnchorToFacing(world, i, j, k, iTempFacing)) {
               iFacing = iTempFacing;
               break;
            }
         }
      }

      return this.setFacing(iMetadata, iFacing);
   }

   @Override
   public AxisAlignedBB getBlockBoundsFromPoolBasedOnState(IBlockAccess blockAccess, int i, int j, int k) {
      int iFacing = this.getFacing(blockAccess, i, j, k);
      switch (iFacing) {
         case 0:
            return AxisAlignedBB.getAABBPool().getAABB(0.0, 0.0, 0.0, 1.0, 0.5, 1.0);
         case 1:
            return AxisAlignedBB.getAABBPool().getAABB(0.0, 0.5, 0.0, 1.0, 1.0, 1.0);
         case 2:
            return AxisAlignedBB.getAABBPool().getAABB(0.0, 0.0, 0.0, 1.0, 1.0, 0.5);
         case 3:
            return AxisAlignedBB.getAABBPool().getAABB(0.0, 0.0, 0.5, 1.0, 1.0, 1.0);
         case 4:
            return AxisAlignedBB.getAABBPool().getAABB(0.0, 0.0, 0.0, 0.5, 1.0, 1.0);
         default:
            return AxisAlignedBB.getAABBPool().getAABB(0.5, 0.0, 0.0, 1.0, 1.0, 1.0);
      }
   }

   @Override
   public void onNeighborBlockChange(World world, int i, int j, int k, int iNeighborBlockID) {
      if (!world.isUpdatePendingThisTickForBlock(i, j, k, this.blockID)) {
         world.scheduleBlockUpdate(i, j, k, this.blockID, this.tickRate(world));
      }
   }

   @Override
   public void onBlockDestroyedByExplosion(World world, int i, int j, int k, Explosion explosion) {
      if (!world.isRemote) {
         int iFacing = this.getFacing(world, i, j, k);
         MiningChargeEntity entityMiningCharge = createPrimedEntity(world, i, j, k, iFacing);
         entityMiningCharge.fuse = 1;
      }
   }

   @Override
   public void onBlockDestroyedByPlayer(World world, int i, int j, int k, int iMetaData) {
      if (!world.isRemote) {
         this.b(world, i, j, k, new ItemStack(BTWBlocks.miningCharge.blockID, 1, 0));
      }
   }

   @Override
   public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer player, int iFacing, float fXClick, float fYClick, float fZClick) {
      ItemStack playerEquippedItem = player.getCurrentEquippedItem();
      if (playerEquippedItem != null && playerEquippedItem.itemID == Item.flintAndSteel.itemID) {
         if (!world.isRemote) {
            int iMetaData = world.getBlockMetadata(i, j, k);
            world.setBlockWithNotify(i, j, k, 0);
            createPrimedEntity(world, i, j, k, iMetaData);
         }

         playerEquippedItem.damageItem(1, player);
         return true;
      } else {
         return super.onBlockActivated(world, i, j, k, player, iFacing, fXClick, fYClick, fZClick);
      }
   }

   @Override
   public void updateTick(World world, int i, int j, int k, Random random) {
      if (this.isGettingRedstonePower(world, i, j, k)) {
         int iMetaData = world.getBlockMetadata(i, j, k);
         world.setBlockWithNotify(i, j, k, 0);
         createPrimedEntity(world, i, j, k, iMetaData);
      } else if (!this.isValidAnchorToFacing(world, i, j, k, this.getFacing(world, i, j, k))) {
         world.setBlockWithNotify(i, j, k, 0);
         this.b(world, i, j, k, new ItemStack(BTWBlocks.miningCharge.blockID, 1, 0));
      }
   }

   @Override
   public void onDestroyedByFire(World world, int i, int j, int k, int iFireAge, boolean bForcedFireSpread) {
      createPrimedEntity(world, i, j, k, world.getBlockMetadata(i, j, k));
      super.onDestroyedByFire(world, i, j, k, iFireAge, bForcedFireSpread);
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
      return iFacing != 1;
   }

   @Override
   public boolean rotateAroundJAxis(World world, int i, int j, int k, boolean bReverse) {
      if (super.rotateAroundJAxis(world, i, j, k, bReverse)) {
         world.scheduleBlockUpdate(i, j, k, this.blockID, this.tickRate(world));
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
      return true;
   }

   public boolean isGettingRedstonePower(World world, int i, int j, int k) {
      return world.isBlockIndirectlyGettingPowered(i, j, k);
   }

   public boolean isValidAnchorToFacing(World world, int i, int j, int k, int iFacing) {
      BlockPos anchorBlockPos = new BlockPos(i, j, k, iFacing);
      return WorldUtils.doesBlockHaveLargeCenterHardpointToFacing(world, anchorBlockPos.x, anchorBlockPos.y, anchorBlockPos.z, getOppositeFacing(iFacing), true);
   }

   public static MiningChargeEntity createPrimedEntity(World world, int i, int j, int k, int iMetaData) {
      MiningChargeEntity entityMiningCharge = (MiningChargeEntity)EntityList.createEntityOfType(
         MiningChargeEntity.class, world, i, j, k, BTWBlocks.miningCharge.getFacing(iMetaData)
      );
      world.spawnEntityInWorld(entityMiningCharge);
      world.playSoundAtEntity(entityMiningCharge, "random.fuse", 1.0F, 1.0F);
      return entityMiningCharge;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void registerIcons(IconRegister register) {
      this.iconBottom = register.registerIcon("fcBlockMiningCharge_bottom");
      this.iconTop = register.registerIcon("fcBlockMiningCharge_top");
      this.iconSide = register.registerIcon("fcBlockMiningCharge_side");
      this.iconSideFlipped = register.registerIcon("fcBlockMiningCharge_side_vert");
      this.blockIcon = this.iconSide;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public Icon getIcon(int iSide, int iMetadata) {
      if (iSide <= 3) {
         return this.iconSideFlipped;
      } else {
         return iSide == 4 ? this.iconTop : this.iconBottom;
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public Icon getBlockTexture(IBlockAccess blockAccess, int i, int j, int k, int iSide) {
      int iFacing = this.getFacing(blockAccess, i, j, k);
      if (iFacing <= 1) {
         if (iSide <= 3) {
            return this.iconSideFlipped;
         } else {
            return iSide == 4 ? this.iconTop : this.iconBottom;
         }
      } else if (iSide == 0) {
         return this.iconBottom;
      } else {
         return iSide == 1 ? this.iconTop : this.iconSide;
      }
   }

   @Environment(EnvType.CLIENT)
   public Icon getBlockTextureFromMetadataCustom(int iSide, int iMetadata) {
      int iFacing = this.getFacing(iMetadata);
      if (iFacing <= 1) {
         if (iSide <= 3) {
            return this.iconSideFlipped;
         } else {
            return iSide == 4 ? this.iconTop : this.iconBottom;
         }
      } else if (iSide == 0) {
         return this.iconBottom;
      } else {
         return iSide == 1 ? this.iconTop : this.iconSide;
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean shouldSideBeRendered(IBlockAccess blockAccess, int iNeighborI, int iNeighborJ, int iNeighborK, int iSide) {
      return this.currentBlockRenderer.shouldSideBeRenderedBasedOnCurrentBounds(iNeighborI, iNeighborJ, iNeighborK, iSide);
   }
}
