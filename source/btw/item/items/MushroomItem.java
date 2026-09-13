package btw.item.items;

import btw.world.util.BlockPos;
import net.minecraft.src.Block;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;

public class MushroomItem extends FoodItem {
   public static final int BROWN_MUSHROOM_HUNGER_HEALED = 1;
   public static final float BROWN_MUSHROOM_SATURATION_MODIFIER = 0.0F;
   public static final String BROWN_MUSHROOM_ITEM_NAME = "fcItemMushroomBrown";
   public static final int RED_MUSHROOM_HUNGER_HEALED = 1;
   public static final float RED_MUSHROOM_SATURATION_MODIFIER = 0.0F;
   public static final String RED_MUSHROOM_ITEM_NAME = "fcItemMushroomRed";
   public final int placedBlockID;

   public MushroomItem(int iItemID, int iHungerHealed, float fSaturationModifier, String sItemName, int iPlacedBlockID) {
      super(iItemID, iHungerHealed, fSaturationModifier, false, sItemName);
      this.placedBlockID = iPlacedBlockID;
   }

   @Override
   public boolean onItemUse(
      ItemStack itemStack, EntityPlayer player, World world, int i, int j, int k, int iFacing, float fClickX, float fClickY, float fClickZ
   ) {
      if (iFacing == 1 && (player == null || player.canPlayerEdit(i, j, k, iFacing, itemStack) && player.canPlayerEdit(i, j + 1, k, iFacing, itemStack))) {
         Block placedBlock = Block.blocksList[this.placedBlockID];
         if (world.isAirBlock(i, j + 1, k) && placedBlock != null && placedBlock.canPlaceBlockAt(world, i, j + 1, k)) {
            world.setBlockWithNotify(i, j + 1, k, this.placedBlockID);
            world.playSoundEffect(
               i + 0.5,
               j + 0.5,
               k + 0.5,
               Block.soundGrassFootstep.getPlaceSound(),
               (Block.soundGrassFootstep.getPlaceVolume() + 1.0F) / 2.0F,
               Block.soundGrassFootstep.getPlacePitch() * 0.8F
            );
            itemStack.stackSize--;
            return true;
         }
      }

      return false;
   }

   @Override
   public boolean onItemUsedByBlockDispenser(ItemStack stack, World world, int i, int j, int k, int iFacing) {
      BlockPos targetPos = new BlockPos(i, j, k, iFacing);
      Block placedBlock = Block.blocksList[this.placedBlockID];
      if (world.isAirBlock(targetPos.x, targetPos.y, targetPos.z)
         && placedBlock != null
         && placedBlock.canPlaceBlockAt(world, targetPos.x, targetPos.y, targetPos.z)) {
         world.setBlockWithNotify(targetPos.x, targetPos.y, targetPos.z, this.placedBlockID);
         world.playSoundEffect(
            targetPos.x + 0.5,
            targetPos.y + 0.5,
            targetPos.z + 0.5,
            Block.soundGrassFootstep.getPlaceSound(),
            (Block.soundGrassFootstep.getPlaceVolume() + 1.0F) / 2.0F,
            Block.soundGrassFootstep.getPlacePitch() * 0.8F
         );
         return true;
      } else {
         return false;
      }
   }

   @Override
   public int getHungerRestored() {
      return this.g();
   }
}
