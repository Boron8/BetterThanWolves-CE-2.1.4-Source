package btw.item.items;

import btw.block.BTWBlocks;
import btw.block.blocks.AnchorBlock;
import btw.world.util.WorldUtils;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;

public class RopeItem extends Item {
   public RopeItem(int iItemID) {
      super(iItemID);
      this.setBuoyant();
      this.b("fcItemRope");
   }

   @Override
   public boolean onItemUse(
      ItemStack itemStack, EntityPlayer player, World world, int i, int j, int k, int iFacing, float fClickX, float fClickY, float fClickZ
   ) {
      if (itemStack.stackSize == 0) {
         return false;
      } else {
         int iTargetBlockID = world.getBlockId(i, j, k);
         if (iTargetBlockID == BTWBlocks.anchor.blockID && ((AnchorBlock)BTWBlocks.anchor).getFacing(world, i, j, k) != 1
            || iTargetBlockID == BTWBlocks.ropeBlock.blockID) {
            for (int tempj = j - 1; tempj >= 0; tempj--) {
               int iTempBlockID = world.getBlockId(i, tempj, k);
               if (WorldUtils.isReplaceableBlock(world, i, tempj, k)) {
                  int iMetadata = BTWBlocks.ropeBlock.onBlockPlaced(world, i, tempj, k, iFacing, 0.0F, 0.0F, 0.0F, 0);
                  iMetadata = BTWBlocks.ropeBlock.preBlockPlacedBy(world, i, tempj, k, iMetadata, player);
                  if (world.setBlockAndMetadataWithNotify(i, tempj, k, BTWBlocks.ropeBlock.blockID, iMetadata)) {
                     BTWBlocks.ropeBlock.onBlockPlacedBy(world, i, tempj, k, player, itemStack);
                     BTWBlocks.ropeBlock.onPostBlockPlaced(world, i, tempj, k, iMetadata);
                     world.playSoundEffect(
                        i + 0.5F,
                        j + 0.5F,
                        k + 0.5F,
                        BTWBlocks.ropeBlock.stepSound.getPlaceSound(),
                        (BTWBlocks.ropeBlock.stepSound.getPlaceVolume() + 1.0F) / 2.0F,
                        BTWBlocks.ropeBlock.stepSound.getPlacePitch() * 0.8F
                     );
                     itemStack.stackSize--;
                     return true;
                  }

                  return false;
               }

               if (iTempBlockID != BTWBlocks.ropeBlock.blockID) {
                  return false;
               }
            }
         }

         return false;
      }
   }
}
