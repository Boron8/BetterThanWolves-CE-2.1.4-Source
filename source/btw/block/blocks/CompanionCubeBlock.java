package btw.block.blocks;

import btw.block.BTWBlocks;
import btw.block.util.Flammability;
import btw.item.items.SwordItem;
import btw.item.util.ItemUtils;
import btw.util.MiscUtils;
import btw.world.util.BlockPos;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Icon;
import net.minecraft.src.IconRegister;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.World;

public class CompanionCubeBlock extends Block {
   public static final int NUM_SUBTYPES = 16;
   @Environment(EnvType.CLIENT)
   private Icon iconFront;
   @Environment(EnvType.CLIENT)
   private Icon iconGuts;

   public CompanionCubeBlock(int iBlockID) {
      super(iBlockID, Material.cloth);
      this.c(0.4F);
      this.setBuoyancy(1.0F);
      this.setFireProperties(Flammability.CLOTH);
      this.a(Block.soundClothFootstep);
      this.c("fcBlockCompanionCube");
      this.a(CreativeTabs.tabBlock);
   }

   @Override
   public int onBlockPlaced(World world, int i, int j, int k, int iFacing, float fClickX, float fClickY, float fClickZ, int iMetadata) {
      if (!this.getIsSlabFromMetadata(iMetadata)) {
         return this.setFacing(iMetadata, Block.getOppositeFacing(iFacing));
      } else {
         return iFacing != 0 && (iFacing == 1 || !(fClickY > 0.5)) ? iMetadata : this.setFacing(iMetadata, 1);
      }
   }

   @Override
   public int damageDropped(int iMetaData) {
      return (iMetaData & 8) > 0 ? 1 : 0;
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
      if (this.getIsSlab(blockAccess, i, j, k)) {
         return !this.getIsUpsideDownSlab(blockAccess, i, j, k)
            ? AxisAlignedBB.getAABBPool().getAABB(0.0, 0.0, 0.0, 1.0, 0.5, 1.0)
            : AxisAlignedBB.getAABBPool().getAABB(0.0, 0.5, 0.0, 1.0, 1.0, 1.0);
      } else {
         return AxisAlignedBB.getAABBPool().getAABB(0.0, 0.0, 0.0, 1.0, 1.0, 1.0);
      }
   }

   @Override
   public void onBlockPlacedBy(World world, int i, int j, int k, EntityLiving entityliving, ItemStack stack) {
      if (!this.getIsSlab(world, i, j, k)) {
         spawnHearts(world, i, j, k);
         int iFacing = MiscUtils.convertPlacingEntityOrientationToBlockFacingReversed(entityliving);
         this.setFacing(world, i, j, k, iFacing);
      }
   }

   @Override
   public void breakBlock(World world, int i, int j, int k, int iBlockID, int iMetadata) {
      if (!this.getIsSlab(world, i, j, k)) {
         world.playSoundEffect(i + 0.5F, j + 0.5F, k + 0.5F, "mob.wolf.whine", 0.5F, 2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F);
      }
   }

   @Override
   public void onDestroyedByFire(World world, int i, int j, int k, int iFireAge, boolean bForcedFireSpread) {
      if (!this.getIsSlab(world, i, j, k)) {
         world.playAuxSFX(2242, i, j, k, 0);
      }

      super.onDestroyedByFire(world, i, j, k, iFireAge, bForcedFireSpread);
   }

   @Override
   public boolean doesBlockBreakSaw(World world, int i, int j, int k) {
      return false;
   }

   @Override
   public boolean onBlockSawed(World world, int i, int j, int k, int iSawPosI, int iSawPosJ, int iSawPosK) {
      int iSawFacing = ((SawBlock)BTWBlocks.saw).getFacing(world, iSawPosI, iSawPosJ, iSawPosK);
      if (!this.getIsSlab(world, i, j, k)) {
         if (iSawFacing != 0 && iSawFacing != 1) {
            ItemUtils.ejectSingleItemWithRandomOffset(world, i, j, k, BTWBlocks.companionCube.blockID, 1);
            this.setIsSlab(world, i, j, k, true);
            this.setFacing(world, i, j, k, 0);
            world.markBlockRangeForRenderUpdate(i, j, k, i, j, k);
         } else {
            for (int iTempCount = 0; iTempCount < 2; iTempCount++) {
               ItemUtils.ejectSingleItemWithRandomOffset(world, i, j, k, BTWBlocks.companionCube.blockID, 1);
            }

            world.setBlockWithNotify(i, j, k, 0);
         }

         BlockPos bloodPos = new BlockPos(i, j, k);
         bloodPos.addFacingAsOffset(Block.getOppositeFacing(iSawFacing));
         world.playSoundEffect(i + 0.5, j + 0.5, k + 0.5, "mob.wolf.hurt", 5.0F, (world.rand.nextFloat() - world.rand.nextFloat()) * 0.2F + 1.0F);
      } else {
         if (iSawFacing != 0 && iSawFacing != 1) {
            return false;
         }

         ItemUtils.ejectSingleItemWithRandomOffset(world, i, j, k, BTWBlocks.companionCube.blockID, 1);
         world.setBlockWithNotify(i, j, k, 0);
      }

      return true;
   }

   @Override
   public boolean hasLargeCenterHardPointToFacing(IBlockAccess blockAccess, int i, int j, int k, int iFacing, boolean bIgnoreTransparency) {
      if (this.getIsSlab(blockAccess, i, j, k)) {
         return this.getIsUpsideDownSlab(blockAccess, i, j, k) ? iFacing == 1 : iFacing == 0;
      } else {
         return true;
      }
   }

   @Override
   public int getFacing(int iMetadata) {
      return iMetadata & -9;
   }

   @Override
   public int setFacing(int iMetadata, int iFacing) {
      iMetadata &= 8;
      return iMetadata | iFacing;
   }

   @Override
   public boolean canRotateOnTurntable(IBlockAccess blockAccess, int i, int j, int k) {
      return !this.getIsSlab(blockAccess, i, j, k);
   }

   @Override
   public boolean canTransmitRotationHorizontallyOnTurntable(IBlockAccess blockAccess, int i, int j, int k) {
      return !this.getIsSlab(blockAccess, i, j, k);
   }

   @Override
   public boolean canTransmitRotationVerticallyOnTurntable(IBlockAccess blockAccess, int i, int j, int k) {
      return !this.getIsSlab(blockAccess, i, j, k);
   }

   @Override
   public boolean rotateAroundJAxis(World world, int i, int j, int k, boolean bReverse) {
      if (!this.getIsSlab(world, i, j, k) && super.rotateAroundJAxis(world, i, j, k, bReverse)) {
         if (world.rand.nextInt(12) == 0) {
            world.playSoundEffect(i + 0.5F, j + 0.5F, k + 0.5F, "mob.wolf.whine", 0.5F, 2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F);
         }

         return true;
      } else {
         return false;
      }
   }

   @Override
   public boolean isNormalCube(IBlockAccess blockAccess, int i, int j, int k) {
      return !this.getIsSlab(blockAccess, i, j, k);
   }

   public boolean getIsSlab(IBlockAccess blockAccess, int i, int j, int k) {
      return this.getIsSlabFromMetadata(blockAccess.getBlockMetadata(i, j, k));
   }

   public void setIsSlab(World world, int i, int j, int k, boolean bState) {
      int iMetadata = world.getBlockMetadata(i, j, k) & -9;
      if (bState) {
         iMetadata |= 8;
      }

      world.setBlockMetadataWithNotify(i, j, k, iMetadata);
   }

   public boolean getIsSlabFromMetadata(int iMetadata) {
      return (iMetadata & 8) > 0;
   }

   public boolean getIsUpsideDownSlab(IBlockAccess blockAccess, int i, int j, int k) {
      return this.getIsUpsideDownSlabFromMetadata(blockAccess.getBlockMetadata(i, j, k));
   }

   public boolean getIsUpsideDownSlabFromMetadata(int iMetadata) {
      return this.getIsSlabFromMetadata(iMetadata) ? this.getFacing(iMetadata) == 1 : false;
   }

   public static void spawnHearts(World world, int i, int j, int k) {
      String s = "heart";

      for (int tempCount = 0; tempCount < 7; tempCount++) {
         double d = world.rand.nextGaussian() * 0.02;
         double d1 = world.rand.nextGaussian() * 0.02;
         double d2 = world.rand.nextGaussian() * 0.02;
         world.spawnParticle(s, (double)i + world.rand.nextFloat(), (double)(j + 1) + world.rand.nextFloat(), (double)k + world.rand.nextFloat(), d, d1, d2);
      }
   }

   @Override
   public boolean canToolStickInBlockSpecialCase(World world, int x, int y, int z, Item toolOrSword) {
      if (toolOrSword instanceof SwordItem) {
         if (!world.isRemote && !this.getIsSlab(world, x, y, z)) {
            world.playSoundEffect(x + 0.5F, y + 0.5F, z + 0.5F, "mob.wolf.whine", 0.5F, 2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F);
         }

         return true;
      } else {
         return false;
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void registerIcons(IconRegister register) {
      super.registerIcons(register);
      this.iconFront = register.registerIcon("fcBlockCompanionCube_front");
      this.iconGuts = register.registerIcon("fcBlockCompanionCube_guts");
   }

   @Environment(EnvType.CLIENT)
   @Override
   public Icon getIcon(int iSide, int iMetadata) {
      if (iMetadata > 0) {
         if (iSide == 1) {
            return this.iconGuts;
         }
      } else if (iSide == 4) {
         return this.iconFront;
      }

      return this.blockIcon;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public Icon getBlockTexture(IBlockAccess blockAccess, int i, int j, int k, int iSide) {
      if (this.getIsSlab(blockAccess, i, j, k)) {
         if (!this.getIsUpsideDownSlab(blockAccess, i, j, k)) {
            if (iSide == 1) {
               return this.iconGuts;
            }
         } else if (iSide == 0) {
            return this.iconGuts;
         }
      } else if (iSide == this.getFacing(blockAccess, i, j, k)) {
         return this.iconFront;
      }

      return this.blockIcon;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void getSubBlocks(int iBlockID, CreativeTabs creativeTabs, List list) {
      list.add(new ItemStack(iBlockID, 1, 0));
      list.add(new ItemStack(iBlockID, 1, 8));
   }

   @Environment(EnvType.CLIENT)
   @Override
   public AxisAlignedBB getBlockBoundsFromPoolForItemRender(int iItemDamage) {
      return iItemDamage > 0 ? AxisAlignedBB.getAABBPool().getAABB(0.0, 0.0, 0.0, 1.0, 0.5, 1.0) : super.getBlockBoundsFromPoolForItemRender(iItemDamage);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean shouldSideBeRendered(IBlockAccess blockAccess, int iNeighborI, int iNeighborJ, int iNeighborK, int iSide) {
      return this.currentBlockRenderer.shouldSideBeRenderedBasedOnCurrentBounds(iNeighborI, iNeighborJ, iNeighborK, iSide);
   }
}
