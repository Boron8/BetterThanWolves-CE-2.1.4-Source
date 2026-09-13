package btw.item.items;

import btw.world.util.BlockPos;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EnumMovingObjectType;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.MovingObjectPosition;
import net.minecraft.src.World;

public abstract class BucketItemFull extends BucketItem {
   public BucketItemFull(int iItemID) {
      super(iItemID);
   }

   @Override
   public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
      MovingObjectPosition posClicked = this.a(world, player, false);
      if (posClicked != null
         && posClicked.typeOfHit == EnumMovingObjectType.TILE
         && world.canMineBlock(player, posClicked.blockX, posClicked.blockY, posClicked.blockZ)) {
         BlockPos targetPos = new BlockPos(posClicked.blockX, posClicked.blockY, posClicked.blockZ, posClicked.sideHit);
         if (player.canPlayerEdit(targetPos.x, targetPos.y, targetPos.z, posClicked.sideHit, itemStack)
            && this.attemptPlaceContentsAtLocation(world, targetPos.x, targetPos.y, targetPos.z)
            && !player.capabilities.isCreativeMode) {
            return new ItemStack(Item.bucketEmpty);
         }
      }

      return itemStack;
   }

   protected abstract boolean attemptPlaceContentsAtLocation(World var1, int var2, int var3, int var4);
}
