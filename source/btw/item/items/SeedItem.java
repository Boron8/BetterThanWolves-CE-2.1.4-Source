package btw.item.items;

import btw.world.util.BlockPos;
import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;

public class SeedItem extends Item {
   protected final int cropBlockID;

   public SeedItem(int iItemID, int iCropBlockID) {
      super(iItemID);
      this.cropBlockID = iCropBlockID;
      this.setBuoyant();
      this.setBellowsBlowDistance(2);
      this.setIncineratedInCrucible();
      this.setFilterableProperties(8);
      this.a(CreativeTabs.tabMaterials);
   }

   @Override
   public boolean onItemUse(
      ItemStack itemStack, EntityPlayer player, World world, int i, int j, int k, int iFacing, float fClickX, float fClickY, float fClickZ
   ) {
      if (iFacing == 1 && (player == null || player.canPlayerEdit(i, j, k, iFacing, itemStack) && player.canPlayerEdit(i, j + 1, k, iFacing, itemStack))) {
         Block cropBlock = Block.blocksList[this.cropBlockID];
         if (cropBlock != null && cropBlock.canPlaceBlockAt(world, i, j + 1, k)) {
            world.setBlockWithNotify(i, j + 1, k, this.cropBlockID);
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
      Block cropBlock = Block.blocksList[this.cropBlockID];
      if (cropBlock != null && cropBlock.canPlaceBlockAt(world, targetPos.x, targetPos.y, targetPos.z)) {
         world.setBlockWithNotify(targetPos.x, targetPos.y, targetPos.z, this.cropBlockID);
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
}
