package btw.item.items;

import btw.block.BTWBlocks;
import btw.util.MiscUtils;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EnumMovingObjectType;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.MovingObjectPosition;
import net.minecraft.src.World;

public class BucketItemEmpty extends BucketItem {
   public BucketItemEmpty(int iItemID) {
      super(iItemID);
      this.d(16);
      this.b("bucket");
   }

   @Override
   public int g() {
      return BTWBlocks.placedBucket.blockID;
   }

   @Override
   public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
      MovingObjectPosition posClicked = MiscUtils.getMovingObjectPositionFromPlayerHitWaterAndLava(world, player, true);
      if (posClicked != null && posClicked.typeOfHit == EnumMovingObjectType.TILE) {
         int i = posClicked.blockX;
         int j = posClicked.blockY;
         int k = posClicked.blockZ;
         int iBlockID = world.getBlockId(i, j, k);
         if (world.getBlockMaterial(i, j, k) == Material.water) {
            if (MiscUtils.doesWaterHaveValidSource(world, i, j, k, 128)) {
               if (--itemStack.stackSize <= 0) {
                  return new ItemStack(Item.bucketWater);
               }

               if (!player.inventory.addItemStackToInventory(new ItemStack(Item.bucketWater))) {
                  player.dropPlayerItem(new ItemStack(Item.bucketWater.itemID, 1, 0));
               }
            }

            return itemStack;
         }

         if (world.getBlockMaterial(i, j, k) == Material.lava) {
            player.e(1);
            world.playSoundEffect(i + 0.5, j + 0.5, k + 0.5, "random.fizz", 0.5F, 2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F);
            if (world.isRemote) {
               for (int l = 0; l < 8; l++) {
                  world.spawnParticle("largesmoke", i + Math.random(), j + Math.random(), k + Math.random(), 0.0, 0.0, 0.0);
               }
            }

            return itemStack;
         }
      }

      return itemStack;
   }

   private boolean isPlayerClickingOnWaterOrLava(ItemStack stack, World world, EntityPlayer player) {
      MovingObjectPosition pos = MiscUtils.getMovingObjectPositionFromPlayerHitWaterAndLava(world, player, true);
      if (pos != null && pos.typeOfHit == EnumMovingObjectType.TILE) {
         Material material = world.getBlockMaterial(pos.blockX, pos.blockY, pos.blockZ);
         if (material == Material.water || material == Material.lava) {
            return true;
         }
      }

      return false;
   }
}
