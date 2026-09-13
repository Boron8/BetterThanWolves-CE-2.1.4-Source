package btw.item.items;

import btw.block.BTWBlocks;
import btw.block.tileentity.PlacedToolTileEntity;
import btw.crafting.util.FurnaceBurnTime;
import btw.item.PlaceableAsItem;
import btw.util.MiscUtils;
import btw.world.util.BlockPos;
import btw.world.util.WorldUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.Enchantment;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EnumEnchantmentType;
import net.minecraft.src.EnumToolMaterial;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;

public abstract class ToolItem extends Item implements PlaceableAsItem {
   protected float efficiencyOnProperMaterial = 4.0F;
   protected int damageVsEntity;
   public EnumToolMaterial toolMaterial;

   protected ToolItem(int iITemID, int iBaseEntityDamage, EnumToolMaterial par3EnumToolMaterial) {
      super(iITemID);
      this.maxStackSize = 1;
      this.toolMaterial = par3EnumToolMaterial;
      this.e(par3EnumToolMaterial.getMaxUses());
      this.efficiencyOnProperMaterial = par3EnumToolMaterial.getEfficiencyOnProperMaterial();
      this.damageVsEntity = iBaseEntityDamage + par3EnumToolMaterial.getDamageVsEntity();
      if (this.toolMaterial == EnumToolMaterial.WOOD) {
         this.setBuoyant();
         this.setfurnaceburntime(FurnaceBurnTime.WOOD_TOOLS);
         this.setIncineratedInCrucible();
      }

      this.setInfernalMaxEnchantmentCost(this.toolMaterial.getInfernalMaxEnchantmentCost());
      this.setInfernalMaxNumEnchants(this.toolMaterial.getInfernalMaxNumEnchants());
      this.a(CreativeTabs.tabTools);
   }

   @Override
   public boolean hitEntity(ItemStack stack, EntityLiving defendingEntity, EntityLiving attackingEntity) {
      stack.damageItem(2, attackingEntity);
      return true;
   }

   @Override
   public boolean onBlockDestroyed(ItemStack stack, World world, int iBlockID, int i, int j, int k, EntityLiving usingEntity) {
      if (Block.blocksList[iBlockID].getBlockHardness(world, i, j, k) > 0.0F) {
         stack.damageItem(1, usingEntity);
      }

      return true;
   }

   @Override
   public int getDamageVsEntity(Entity entity) {
      return this.damageVsEntity;
   }

   @Override
   public int getItemEnchantability() {
      return this.toolMaterial.getEnchantability();
   }

   @Override
   public boolean isEnchantmentApplicable(Enchantment enchantment) {
      return enchantment.type == EnumEnchantmentType.digger ? true : super.isEnchantmentApplicable(enchantment);
   }

   @Override
   public float getStrVsBlock(ItemStack stack, World world, Block block, int i, int j, int k) {
      return this.isEfficientVsBlock(stack, world, block, i, j, k) ? this.efficiencyOnProperMaterial : super.getStrVsBlock(stack, world, block, i, j, k);
   }

   @Override
   public boolean isEfficientVsBlock(ItemStack stack, World world, Block block, int i, int j, int k) {
      return !block.blockMaterial.isToolNotRequired() && this.canHarvestBlock(stack, world, block, i, j, k) ? true : this.isToolTypeEfficientVsBlockType(block);
   }

   @Override
   public void onCreated(ItemStack stack, World world, EntityPlayer player) {
      if (player.timesCraftedThisTick == 0 && world.isRemote) {
         if (this.toolMaterial == EnumToolMaterial.WOOD) {
            player.playSound("mob.zombie.woodbreak", 0.1F, 1.25F + world.rand.nextFloat() * 0.25F);
         } else if (this.toolMaterial == EnumToolMaterial.STONE) {
            player.playSound("random.anvil_land", 0.5F, world.rand.nextFloat() * 0.25F + 1.75F);
         } else {
            player.playSound("random.anvil_use", 0.5F, world.rand.nextFloat() * 0.25F + 1.25F);
         }
      }

      super.onCreated(stack, world, player);
   }

   @Override
   public boolean canItemBeUsedByPlayer(World world, int i, int j, int k, int iFacing, EntityPlayer player, ItemStack stack) {
      return !player.isLocalPlayerAndHittingBlock();
   }

   @Override
   public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int i, int j, int k, int iFacing, float fClickX, float fClickY, float fClickZ) {
      if (player.isUsingSpecialKey() && player != null && player.canPlayerEdit(i, j, k, iFacing, stack) && this.getCanBePlacedAsBlock()) {
         BlockPos placementPos = new BlockPos(i, j, k);
         BlockPos stuckInPos = new BlockPos(i, j, k);
         if (!WorldUtils.isReplaceableBlock(world, i, j, k)) {
            placementPos.addFacingAsOffset(iFacing);
         } else {
            iFacing = 1;
            stuckInPos.addFacingAsOffset(0);
         }

         if (WorldUtils.doesBlockHaveCenterHardpointToFacing(world, stuckInPos.x, stuckInPos.y, stuckInPos.z, iFacing, true)
            && BTWBlocks.placedTool.canPlaceBlockAt(world, placementPos.x, placementPos.y, placementPos.z)) {
            Block blockStuckIn = Block.blocksList[world.getBlockId(stuckInPos.x, stuckInPos.y, stuckInPos.z)];
            if (blockStuckIn != null
               && blockStuckIn.canToolsStickInBlock(world, stuckInPos.x, stuckInPos.y, stuckInPos.z)
               && this.canToolStickInBlock(stack, blockStuckIn, world, stuckInPos.x, stuckInPos.y, stuckInPos.z)) {
               int iTargetFacing;
               int iTargetFacingLevel;
               if (iFacing >= 2) {
                  iTargetFacing = Block.getOppositeFacing(iFacing);
                  iTargetFacingLevel = 2;
               } else {
                  iTargetFacing = MiscUtils.convertOrientationToFlatBlockFacing(player);
                  iTargetFacingLevel = Block.getOppositeFacing(iFacing);
               }

               int iMetadata = BTWBlocks.placedTool.setFacing(0, iTargetFacing);
               iMetadata = BTWBlocks.placedTool.setVerticalOrientation(iMetadata, iTargetFacingLevel);
               world.setBlockAndMetadataWithNotify(placementPos.x, placementPos.y, placementPos.z, BTWBlocks.placedTool.blockID, iMetadata);
               TileEntity targetTileEntity = world.getBlockTileEntity(placementPos.x, placementPos.y, placementPos.z);
               if (targetTileEntity != null && targetTileEntity instanceof PlacedToolTileEntity) {
                  ((PlacedToolTileEntity)targetTileEntity).setToolStack(stack);
                  if (!world.isRemote) {
                     this.playPlacementSound(stack, blockStuckIn, world, placementPos.x, placementPos.y, placementPos.z);
                  }

                  stack.stackSize--;
                  return true;
               }
            }
         }
      }

      return false;
   }

   public abstract boolean isToolTypeEfficientVsBlockType(Block var1);

   public ToolItem setDamageVsEntity(int iDamage) {
      this.damageVsEntity = iDamage;
      return this;
   }

   public boolean canToolStickInBlock(ItemStack stack, Block block, World world, int i, int j, int k) {
      return this.isEfficientVsBlock(stack, world, block, i, j, k) || block.canToolStickInBlockSpecialCase(world, i, j, k, this);
   }

   public void playPlacementSound(ItemStack stack, Block blockStuckIn, World world, int i, int j, int k) {
      world.playSoundEffect(
         i + 0.5F,
         j + 0.5F,
         k + 0.5F,
         blockStuckIn.getStepSound(world, i, j, k).getStepSound(),
         (blockStuckIn.getStepSound(world, i, j, k).getVolume() + 1.0F) / 2.0F,
         blockStuckIn.getStepSound(world, i, j, k).getPitch() * 0.8F
      );
   }

   public boolean getCanBePlacedAsBlock() {
      return true;
   }

   @Override
   public float getVisualVerticalOffsetAsBlock() {
      return 0.75F;
   }

   @Override
   public float getVisualHorizontalOffsetAsBlock() {
      return 0.5F;
   }

   @Override
   public float getVisualRollOffsetAsBlock() {
      return 0.0F;
   }

   @Override
   public float getBlockBoundingBoxHeight() {
      return 0.75F;
   }

   @Override
   public float getBlockBoundingBoxWidth() {
      return 0.75F;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean isFull3D() {
      return true;
   }
}
