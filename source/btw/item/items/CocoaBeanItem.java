package btw.item.items;

import btw.world.util.BlockPos;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.Block;
import net.minecraft.src.BlockLog;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IconRegister;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;

public class CocoaBeanItem extends FoodItem {
   public static final int HUNGER_HEALED = 1;
   public static final float SATURATION_MODIFIER = 0.0F;
   public static final String ITEM_NAME = "fcItemCocoaBeans";

   public CocoaBeanItem(int iItemID) {
      super(iItemID, 1, 0.0F, false, "fcItemCocoaBeans");
      this.setBellowsBlowDistance(1);
      this.setFilterableProperties(2);
   }

   @Override
   public boolean onItemUse(
      ItemStack itemStack, EntityPlayer player, World world, int i, int j, int k, int iFacing, float fClickX, float fClickY, float fClickZ
   ) {
      if (!this.attemptPlaceOn(world, i, j, k, iFacing, fClickX, fClickY, fClickZ)) {
         return false;
      } else {
         if (player == null || !player.capabilities.isCreativeMode) {
            itemStack.stackSize--;
         }

         return true;
      }
   }

   protected boolean attemptPlaceOn(World world, int i, int j, int k, int iFacing, float fClickX, float fClickY, float fClickZ) {
      int iTargetBlockID = world.getBlockId(i, j, k);
      int iTargetMetadata = world.getBlockMetadata(i, j, k);
      if (iFacing >= 2 && iTargetBlockID == Block.wood.blockID && BlockLog.limitToValidMetadata(iTargetMetadata) == 3) {
         BlockPos targetPos = new BlockPos(i, j, k, iFacing);
         if (world.isAirBlock(targetPos.x, targetPos.y, targetPos.z)) {
            int iCocoaBlockID = Block.cocoaPlant.blockID;
            int iMetadata = Block.blocksList[iCocoaBlockID].onBlockPlaced(world, targetPos.x, targetPos.y, targetPos.z, iFacing, fClickX, fClickY, fClickZ, 0);
            world.setBlockAndMetadataWithNotify(targetPos.x, targetPos.y, targetPos.z, iCocoaBlockID, iMetadata);
            return true;
         }
      }

      return false;
   }

   @Override
   public int getHungerRestored() {
      return this.g();
   }

   @Override
   public boolean onItemUsedByBlockDispenser(ItemStack stack, World world, int i, int j, int k, int iFacing) {
      BlockPos block2AwayPos = new BlockPos(i, j, k, iFacing);
      block2AwayPos.addFacingAsOffset(iFacing);
      if (this.attemptPlaceOn(world, block2AwayPos.x, block2AwayPos.y, block2AwayPos.z, Block.getOppositeFacing(iFacing), 0.0F, 0.0F, 0.0F)) {
         world.playAuxSFX(2236, i, j, k, Block.cocoaPlant.blockID);
         return true;
      } else {
         return false;
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void registerIcons(IconRegister register) {
      this.itemIcon = register.registerIcon("dyePowder_brown");
   }
}
