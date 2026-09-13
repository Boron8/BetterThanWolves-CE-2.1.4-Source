package btw.item.items;

import btw.block.BTWBlocks;
import btw.block.tileentity.PlacedToolTileEntity;
import btw.crafting.util.FurnaceBurnTime;
import btw.item.PlaceableAsItem;
import btw.util.MiscUtils;
import btw.world.util.BlockPos;
import btw.world.util.WorldUtils;
import net.minecraft.src.Block;
import net.minecraft.src.Enchantment;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EnumEnchantmentType;
import net.minecraft.src.EnumToolMaterial;
import net.minecraft.src.ItemStack;
import net.minecraft.src.ItemSword;
import net.minecraft.src.Material;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;

public class SwordItem extends ItemSword implements PlaceableAsItem {
   private final EnumToolMaterial material;

   public SwordItem(int iItemID, EnumToolMaterial material) {
      super(iItemID, material);
      this.material = material;
      if (this.material == EnumToolMaterial.WOOD) {
         this.setBuoyant();
         this.setfurnaceburntime(FurnaceBurnTime.WOOD_TOOLS);
         this.setIncineratedInCrucible();
      }

      this.setInfernalMaxEnchantmentCost(this.material.getInfernalMaxEnchantmentCost());
      this.setInfernalMaxNumEnchants(this.material.getInfernalMaxNumEnchants());
   }

   @Override
   public boolean canHarvestBlock(ItemStack stack, World world, Block block, int i, int j, int k) {
      return this.isEfficientVsBlock(stack, world, block, i, j, k);
   }

   @Override
   public float getStrVsBlock(ItemStack stack, World world, Block block, int i, int j, int k) {
      if (this.isEfficientVsBlock(stack, world, block, i, j, k)) {
         return 15.0F;
      } else {
         Material material = block.blockMaterial;
         return material != Material.plants
               && material != Material.vine
               && material != Material.coral
               && material == Material.leaves
               && material == Material.pumpkin
            ? super.getStrVsBlock(stack, world, block, i, j, k)
            : 1.5F;
      }
   }

   @Override
   public boolean isEfficientVsBlock(ItemStack stack, World world, Block block, int i, int j, int k) {
      return block.blockID == Block.web.blockID || block.blockID == BTWBlocks.web.blockID;
   }

   @Override
   public boolean isEnchantmentApplicable(Enchantment enchantment) {
      return enchantment.type == EnumEnchantmentType.weapon ? true : super.isEnchantmentApplicable(enchantment);
   }

   @Override
   public void onCreated(ItemStack stack, World world, EntityPlayer player) {
      if (player.timesCraftedThisTick == 0 && world.isRemote) {
         if (this.material == EnumToolMaterial.WOOD) {
            player.playSound("mob.zombie.woodbreak", 0.1F, 1.25F + world.rand.nextFloat() * 0.25F);
         } else if (this.material == EnumToolMaterial.STONE) {
            player.playSound("random.anvil_land", 0.5F, world.rand.nextFloat() * 0.25F + 1.75F);
         } else {
            player.playSound("random.anvil_use", 0.5F, world.rand.nextFloat() * 0.25F + 1.25F);
         }
      }

      super.d(stack, world, player);
   }

   @Override
   public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
      if (!par3EntityPlayer.isUsingSpecialKey()) {
         par3EntityPlayer.setItemInUse(par1ItemStack, this.c_(par1ItemStack));
      }

      return par1ItemStack;
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

   protected boolean canToolStickInBlock(ItemStack stack, Block block, World world, int i, int j, int k) {
      return block.areShovelsEffectiveOn() || block.canToolStickInBlockSpecialCase(world, i, j, k, this);
   }

   protected void playPlacementSound(ItemStack stack, Block blockStuckIn, World world, int i, int j, int k) {
      world.playSoundEffect(
         i + 0.5F,
         j + 0.5F,
         k + 0.5F,
         blockStuckIn.getStepSound(world, i, j, k).getPlaceSound(),
         (blockStuckIn.getStepSound(world, i, j, k).getPlaceVolume() + 1.0F) / 2.0F,
         blockStuckIn.getStepSound(world, i, j, k).getPlacePitch() * 0.8F
      );
   }

   protected boolean getCanBePlacedAsBlock() {
      return true;
   }

   @Override
   public float getVisualVerticalOffsetAsBlock() {
      return 0.4F;
   }

   @Override
   public float getVisualHorizontalOffsetAsBlock() {
      return 0.85F;
   }

   @Override
   public float getVisualRollOffsetAsBlock() {
      return -45.0F;
   }

   @Override
   public float getBlockBoundingBoxHeight() {
      return 0.75F;
   }

   @Override
   public float getBlockBoundingBoxWidth() {
      return 0.75F;
   }
}
