package btw.block.blocks;

import btw.block.BTWBlocks;
import btw.block.util.Flammability;
import btw.client.render.util.RenderUtils;
import btw.entity.item.BloodWoodSaplingItemEntity;
import btw.item.items.ShearsItem;
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
import net.minecraft.src.EntityList;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Icon;
import net.minecraft.src.IconRegister;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.World;

public class AestheticVegetationBlock extends Block {
   public static final int SUBTYPE_VINE_TRAP = 0;
   public static final int SUBTYPE_VINE_TRAP_TRIGGERED_BY_ENTITY = 1;
   public static final int SUBTYPE_BLOOD_WOOD_SAPLING = 2;
   public static final int SUBTYPE_BLOOD_LEAVES = 3;
   public static final int SUBTYPE_VINE_TRAP_UPSIDE_DOWN = 4;
   public static final int SUBTYPE_VINE_TRAP_UPSIDE_DOWN_TRIGGERED_BY_ENTITY = 5;
   public static final int NUM_SUBTYPES = 6;
   private static final double VINE_TRAP_HEIGHT = 0.125;
   private static final float HARDNESS = 0.2F;
   private static final int TICK_RATE = 10;
   public static final int BLOOD_WOOD_SAPLING_MIN_TRUNK_HEIGHT = 4;
   @Environment(EnvType.CLIENT)
   private static final int VINE_TRAP_TEXTURE_ID = 105;
   @Environment(EnvType.CLIENT)
   private static final int BLOOD_WOOD_SAPLING_TEXTURE_ID = 108;
   @Environment(EnvType.CLIENT)
   private static final int BLOOD_LEAVES_TEXTURE_ID = 109;
   @Environment(EnvType.CLIENT)
   private Icon iconVineTrap;
   @Environment(EnvType.CLIENT)
   private Icon iconSaplingBloodWood;
   @Environment(EnvType.CLIENT)
   private Icon iconLeavesBloodWood;

   public AestheticVegetationBlock(int iBlockID) {
      super(iBlockID, Material.leaves);
      this.c(0.2F);
      this.setAxesEffectiveOn();
      this.setBuoyancy(1.0F);
      this.setFireProperties(Flammability.EXTREME);
      this.a(i);
      this.c("fcBlockAestheticVegetation");
      this.b(true);
      this.a(CreativeTabs.tabDecorations);
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
   public int onBlockPlaced(World world, int i, int j, int k, int iFacing, float fClickX, float fClickY, float fClickZ, int iMetadata) {
      if (iMetadata == 0 && iFacing != 1) {
         boolean bUpsideDown = true;
         if (iFacing >= 2 && fClickY < 0.5F) {
            bUpsideDown = false;
         }

         if (bUpsideDown) {
            return 4;
         }
      }

      return iMetadata;
   }

   @Override
   public void onNeighborBlockChange(World world, int i, int j, int k, int iChangedBlockID) {
      super.onNeighborBlockChange(world, i, j, k, iChangedBlockID);
      int iSubtype = this.getSubtype(world, i, j, k);
      if (iSubtype == 2) {
         this.validateBloodWoodSapling(world, i, j, k);
      }
   }

   @Override
   public boolean canBlockStay(World world, int i, int j, int k) {
      int iSubtype = this.getSubtype(world, i, j, k);
      return iSubtype == 2 ? this.canBloodwoodSaplingStayAtLocation(world, i, j, k) : super.canBlockStay(world, i, j, k);
   }

   @Override
   public int damageDropped(int iMetadata) {
      int iSubtype = iMetadata;
      switch (iMetadata) {
         case 0:
         case 1:
         case 4:
         case 5:
            iSubtype = 0;
         case 2:
         default:
            break;
         case 3:
            iSubtype = 2;
      }

      return iSubtype;
   }

   @Override
   public void dropBlockAsItemWithChance(World world, int i, int j, int k, int iMetaData, float fChance, int iFortuneModifier) {
      if (iMetaData == 3) {
         if (world.isRemote) {
            return;
         }

         int iNumDropped = world.rand.nextInt(20) != 0 ? 0 : 1;

         for (int iTempCount = 0; iTempCount < iNumDropped; iTempCount++) {
            if (!(world.rand.nextFloat() > fChance)) {
               int iItemID = this.a(iMetaData, world.rand, iFortuneModifier);
               if (iItemID > 0) {
                  this.dropBlockAsItem_do(world, i, j, k, new ItemStack(iItemID, 1, this.damageDropped(iMetaData)));
               }
            }
         }
      } else {
         super.dropBlockAsItemWithChance(world, i, j, k, iMetaData, fChance, iFortuneModifier);
      }
   }

   @Override
   protected void dropBlockAsItem_do(World world, int i, int j, int k, ItemStack itemStack) {
      if (itemStack.itemID != this.blockID || itemStack.getItemDamage() != 2) {
         super.dropBlockAsItem_do(world, i, j, k, itemStack);
      } else if (!world.isRemote) {
         float f = 0.7F;
         double d = world.rand.nextFloat() * f + (1.0F - f) * 0.5;
         double d1 = world.rand.nextFloat() * f + (1.0F - f) * 0.5;
         double d2 = world.rand.nextFloat() * f + (1.0F - f) * 0.5;
         EntityItem entityitem = (EntityItem)EntityList.createEntityOfType(BloodWoodSaplingItemEntity.class, world, i + d, j + d1, k + d2, itemStack);
         entityitem.delayBeforeCanPickup = 10;
         world.spawnEntityInWorld(entityitem);
      }
   }

   @Override
   public void harvestBlock(World world, EntityPlayer entityPlayer, int i, int j, int k, int iMetaData) {
      if (!world.isRemote
         && entityPlayer.getCurrentEquippedItem() != null
         && entityPlayer.getCurrentEquippedItem().getItem() instanceof ShearsItem
         && iMetaData == 3) {
         this.dropBlockAsItem_do(world, i, j, k, new ItemStack(BTWBlocks.bloodWoodLeaves, 1, 0));
         entityPlayer.getCurrentEquippedItem().damageItem(1, entityPlayer);
      } else {
         super.harvestBlock(world, entityPlayer, i, j, k, iMetaData);
      }
   }

   @Override
   public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k) {
      int iSubtype = this.getSubtype(world, i, j, k);
      return iSubtype != 0 && iSubtype != 1 && iSubtype != 5 && iSubtype != 4 && iSubtype != 2 ? super.getCollisionBoundingBoxFromPool(world, i, j, k) : null;
   }

   @Override
   public AxisAlignedBB getBlockBoundsFromPoolBasedOnState(IBlockAccess blockAccess, int i, int j, int k) {
      int iSubtype = this.getSubtype(blockAccess, i, j, k);
      return this.getBlockBoundsFromPoolBasedOnSubtype(iSubtype);
   }

   @Override
   public int tickRate(World world) {
      return 10;
   }

   @Override
   public float getMovementModifier(World world, int i, int j, int k) {
      return 0.8F;
   }

   @Override
   public void onEntityCollidedWithBlock(World world, int i, int j, int k, Entity entity) {
      int iSubtype = this.getSubtype(world, i, j, k);
      if ((iSubtype == 0 || iSubtype == 4 || iSubtype == 1 || iSubtype == 5 || iSubtype == 2) && entity.isAffectedByMovementModifiers() && entity.onGround) {
         entity.motionX *= 0.8;
         entity.motionZ *= 0.8;
      }
   }

   @Override
   public void updateTick(World world, int i, int j, int k, Random random) {
      int iSubtype = this.getSubtype(world, i, j, k);
      if (iSubtype == 1 || iSubtype == 5) {
         this.setSubtype(world, i, j, k, iSubtype - 1);
      } else if (iSubtype == 2 && this.validateBloodWoodSapling(world, i, j, k) && random.nextInt(14) == 0 && world.provider.dimensionId == -1) {
         this.attemptToGrowBloodwoodSapling(world, i, j, k, random);
      }
   }

   @Override
   public boolean onBlockSawed(World world, int i, int j, int k) {
      int iSubtype = this.getSubtype(world, i, j, k);
      return iSubtype == 2 ? false : super.onBlockSawed(world, i, j, k);
   }

   @Override
   public boolean doesBlockHopperEject(World world, int i, int j, int k) {
      int iSubtype = this.getSubtype(world, i, j, k);
      return iSubtype != 0 && iSubtype != 1 && iSubtype != 2 && iSubtype != 4 && iSubtype != 5 ? super.doesBlockHopperEject(world, i, j, k) : false;
   }

   @Override
   public boolean canPlaceBlockOnSide(World world, int i, int j, int k, int iSide, ItemStack stack) {
      return stack != null && stack.getItemDamage() == 2 && !this.canBloodwoodSaplingStayAtLocation(world, i, j, k)
         ? false
         : super.canPlaceBlockOnSide(world, i, j, k, iSide, stack);
   }

   public int getSubtype(IBlockAccess blockAccess, int i, int j, int k) {
      return blockAccess.getBlockMetadata(i, j, k);
   }

   public void setSubtype(World world, int i, int j, int k, int iSubtype) {
      world.setBlockMetadata(i, j, k, iSubtype);
   }

   public AxisAlignedBB getBlockBoundsFromPoolBasedOnSubtype(int iSubtype) {
      switch (iSubtype) {
         case 0:
         case 1:
            return AxisAlignedBB.getAABBPool().getAABB(0.0, 0.0, 0.0, 1.0, 0.125, 1.0);
         case 2:
         case 3:
         default:
            return AxisAlignedBB.getAABBPool().getAABB(0.0, 0.0, 0.0, 1.0, 1.0, 1.0);
         case 4:
         case 5:
            return AxisAlignedBB.getAABBPool().getAABB(0.0, 0.875, 0.0, 1.0, 1.0, 1.0);
      }
   }

   public boolean validateBloodWoodSapling(World world, int i, int j, int k) {
      if (!this.canBlockStay(world, i, j, k)) {
         this.c(world, i, j, k, world.getBlockMetadata(i, j, k), 0);
         world.setBlockWithNotify(i, j, k, 0);
         return false;
      } else {
         return true;
      }
   }

   public void attemptToGrowBloodwoodSapling(World world, int i, int j, int k, Random random) {
      for (int iTempJ = j + 1; iTempJ < j + 4; iTempJ++) {
         if (iTempJ >= 256 || !world.isAirBlock(i, iTempJ, k)) {
            return;
         }
      }

      for (int iTempJx = j; iTempJx < j + 4 - 1; iTempJx++) {
         world.setBlockAndMetadataWithNotify(i, iTempJx, k, BTWBlocks.bloodWoodLog.blockID, 0);
      }

      BloodWoodLogBlock bloodWoodBlock = (BloodWoodLogBlock)BTWBlocks.bloodWoodLog;
      int iTrunkTopJ = j + 4 - 1;
      world.setBlockAndMetadataWithNotify(i, iTrunkTopJ, k, BTWBlocks.bloodWoodLog.blockID, 1);
      bloodWoodBlock.growLeaves(world, i, iTrunkTopJ, k);
      bloodWoodBlock.grow(world, i, iTrunkTopJ, k, random);

      for (int tempI = i - 1; tempI <= i + 1; tempI++) {
         for (int tempJ = iTrunkTopJ; tempJ <= iTrunkTopJ + 1; tempJ++) {
            for (int tempK = k - 1; tempK <= k + 1; tempK++) {
               if (world.getBlockId(tempI, tempJ, tempK) == BTWBlocks.bloodWoodLog.blockID) {
                  int iGrowthDirection = bloodWoodBlock.getFacing(world, tempI, tempJ, tempK);
                  if (iGrowthDirection != 0 && (tempI != i || tempJ != iTrunkTopJ || tempK != k)) {
                     bloodWoodBlock.grow(world, tempI, tempJ, tempK, random);
                  }
               }
            }
         }
      }

      world.playAuxSFX(2228, i, j, k, 0);
   }

   public boolean canBloodwoodSaplingStayAtLocation(World world, int i, int j, int k) {
      int iBlockBelowID = world.getBlockId(i, j - 1, k);
      return iBlockBelowID == Block.slowSand.blockID
         ? true
         : iBlockBelowID == BTWBlocks.planter.blockID && ((PlanterBlock)BTWBlocks.planter).getPlanterType(world, i, j - 1, k) == 8;
   }

   @Override
   public boolean attemptToAffectBlockWithSoul(World world, int x, int y, int z) {
      int iTargetSubType = world.getBlockMetadata(x, y, z);
      if (iTargetSubType == 2) {
         this.attemptToGrowBloodwoodSapling(world, x, y, z, world.rand);
         return true;
      } else {
         return false;
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void registerIcons(IconRegister register) {
      Icon vineIcon = register.registerIcon("fcBlockVineTrap");
      this.blockIcon = vineIcon;
      this.iconVineTrap = vineIcon;
      this.iconSaplingBloodWood = register.registerIcon("fcBlockSaplingBloodWood");
      this.iconLeavesBloodWood = register.registerIcon("fcBlockLeavesBloodWood_old");
   }

   @Environment(EnvType.CLIENT)
   @Override
   public Icon getIcon(int iSide, int iMetadata) {
      switch (iMetadata) {
         case 0:
         case 1:
         case 4:
         case 5:
            return this.iconVineTrap;
         case 2:
            return this.iconSaplingBloodWood;
         case 3:
            return this.iconLeavesBloodWood;
         default:
            return this.blockIcon;
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void getSubBlocks(int iBlockID, CreativeTabs creativeTabs, List list) {
      list.add(new ItemStack(iBlockID, 1, 0));
      list.add(new ItemStack(iBlockID, 1, 2));
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean shouldSideBeRendered(IBlockAccess blockAccess, int iNeighborI, int iNeighborJ, int iNeighborK, int iSide) {
      if (iSide >= 2 && blockAccess.getBlockId(iNeighborI, iNeighborJ, iNeighborK) == this.blockID) {
         int iTargetSubtype = blockAccess.getBlockMetadata(iNeighborI, iNeighborJ, iNeighborK);
         if (iTargetSubtype != 0 && iTargetSubtype != 1) {
            if (iTargetSubtype == 4 || iTargetSubtype == 5) {
               BlockPos sourcePos = new BlockPos(iNeighborI, iNeighborJ, iNeighborK);
               sourcePos.addFacingAsOffset(Block.getOppositeFacing(iSide));
               int iSourceSubtype = blockAccess.getBlockMetadata(sourcePos.x, sourcePos.y, sourcePos.z);
               if (iSourceSubtype == 4 || iSourceSubtype == 5) {
                  return false;
               }
            }
         } else {
            BlockPos sourcePos = new BlockPos(iNeighborI, iNeighborJ, iNeighborK);
            sourcePos.addFacingAsOffset(Block.getOppositeFacing(iSide));
            int iSourceSubtype = blockAccess.getBlockMetadata(sourcePos.x, sourcePos.y, sourcePos.z);
            if (iSourceSubtype == 0 || iSourceSubtype == 1) {
               return false;
            }
         }
      }

      return true;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public int idPicked(World world, int i, int j, int k) {
      int iMetadata = world.getBlockMetadata(i, j, k);
      return iMetadata == 3 ? BTWBlocks.bloodWoodLeaves.blockID : this.a(iMetadata, world.rand, 0);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public int getDamageValue(World world, int i, int j, int k) {
      int iMetadata = world.getBlockMetadata(i, j, k);
      return iMetadata == 3 ? 0 : super.getDamageValue(world, i, j, k);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean renderBlock(RenderBlocks renderBlocks, int i, int j, int k) {
      IBlockAccess blockAccess = renderBlocks.blockAccess;
      int iSubtype = this.getSubtype(blockAccess, i, j, k);
      renderBlocks.setRenderBounds(this.getBlockBoundsFromPoolBasedOnSubtype(iSubtype));
      switch (iSubtype) {
         case 2:
            return renderBlocks.renderCrossedSquares(this, i, j, k);
         default:
            return renderBlocks.renderStandardBlock(this, i, j, k);
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void renderBlockAsItem(RenderBlocks renderBlocks, int iItemDamage, float fBrightness) {
      renderBlocks.setRenderBounds(this.getBlockBoundsFromPoolBasedOnSubtype(iItemDamage));
      RenderUtils.renderInvBlockWithMetadata(renderBlocks, this, -0.5F, -0.5F, -0.5F, iItemDamage);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean doesItemRenderAsBlock(int iItemDamage) {
      return iItemDamage == 2 ? false : super.doesItemRenderAsBlock(iItemDamage);
   }
}
