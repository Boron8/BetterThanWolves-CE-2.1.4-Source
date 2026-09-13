package btw.item.blockitems;

import btw.block.BTWBlocks;
import btw.util.MiscUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.Block;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EnumMovingObjectType;
import net.minecraft.src.ItemBlock;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.MathHelper;
import net.minecraft.src.MovingObjectPosition;
import net.minecraft.src.World;

public class InfinteUnlitTorchBlockItem extends ItemBlock {
   public InfinteUnlitTorchBlockItem(int iItemID) {
      super(iItemID);
      this.b("fcBlockTorchIdle");
   }

   @Override
   public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int i, int j, int k, int iFacing, float fClickX, float fClickY, float fClickZ) {
      return this.isPlayerClickingOnIgniter(stack, world, player) ? false : super.onItemUse(stack, player, world, i, j, k, iFacing, fClickX, fClickY, fClickZ);
   }

   @Override
   public boolean getCanItemBeSetOnFireOnUse(int iItemDamage) {
      return true;
   }

   @Override
   public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
      return this.isPlayerClickingOnIgniter(stack, world, player) ? this.onRightClickOnIgniter(stack, world, player) : super.a(stack, world, player);
   }

   protected ItemStack onRightClickOnIgniter(ItemStack stack, World world, EntityPlayer player) {
      int i = MathHelper.floor_double(player.posX);
      int j = MathHelper.floor_double(player.boundingBox.minY);
      int k = MathHelper.floor_double(player.posZ);
      player.playSound("mob.ghast.fireball", 1.0F, world.rand.nextFloat() * 0.4F + 0.8F);
      return new ItemStack(BTWBlocks.infiniteBurningTorch, stack.stackSize, 0);
   }

   protected boolean isPlayerClickingOnIgniter(ItemStack stack, World world, EntityPlayer player) {
      return this.isPlayerClickingOnSolidIgniter(stack, world, player) || this.isPlayerClickingOnLavaOrFire(stack, world, player);
   }

   private boolean isPlayerClickingOnSolidIgniter(ItemStack stack, World world, EntityPlayer player) {
      MovingObjectPosition pos = this.a(world, player, true);
      if (pos != null && pos.typeOfHit == EnumMovingObjectType.TILE) {
         Block targetBlock = Block.blocksList[world.getBlockId(pos.blockX, pos.blockY, pos.blockZ)];
         if (targetBlock != null && targetBlock.getCanBlockLightItemOnFire(world, pos.blockX, pos.blockY, pos.blockZ)) {
            return true;
         }
      }

      return false;
   }

   private boolean isPlayerClickingOnLavaOrFire(ItemStack stack, World world, EntityPlayer player) {
      MovingObjectPosition pos = MiscUtils.getMovingObjectPositionFromPlayerHitWaterAndLavaAndFire(world, player, true);
      if (pos != null && pos.typeOfHit == EnumMovingObjectType.TILE) {
         Material material = world.getBlockMaterial(pos.blockX, pos.blockY, pos.blockZ);
         if (material == Material.lava || material == Material.fire) {
            return true;
         }
      }

      return false;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean canPlaceItemBlockOnSide(World world, int i, int j, int k, int iFacing, EntityPlayer player, ItemStack stack) {
      return this.isPlayerClickingOnIgniter(stack, world, player) ? true : super.canPlaceItemBlockOnSide(world, i, j, k, iFacing, player, stack);
   }
}
