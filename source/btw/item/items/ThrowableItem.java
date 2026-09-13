package btw.item.items;

import btw.world.util.BlockPos;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EntityThrowable;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;

public abstract class ThrowableItem extends Item {
   public ThrowableItem(int iItemID) {
      super(iItemID);
   }

   @Override
   public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
      if (!player.capabilities.isCreativeMode) {
         stack.stackSize--;
      }

      world.playSoundAtEntity(player, "random.bow", 0.5F, 0.4F / (e.nextFloat() * 0.4F + 0.8F));
      if (!world.isRemote) {
         this.spawnThrownEntity(stack, world, player);
      }

      return stack;
   }

   @Override
   public boolean onItemUsedByBlockDispenser(ItemStack stack, World world, int i, int j, int k, int iFacing) {
      BlockPos offsetPos = new BlockPos(0, 0, 0, iFacing);
      double dXPos = i + offsetPos.x * 0.6 + 0.5;
      double dYPos = j + offsetPos.y * 0.6 + 0.5;
      double dZPos = k + offsetPos.z * 0.6 + 0.5;
      double dYHeading;
      if (iFacing > 2) {
         dYHeading = 0.1;
      } else {
         dYHeading = offsetPos.y;
      }

      EntityThrowable entity = this.getEntityFiredByByBlockDispenser(world, dXPos, dYPos, dZPos);
      entity.setThrowableHeading(offsetPos.x, dYHeading, offsetPos.z, 1.1F, 6.0F);
      world.spawnEntityInWorld(entity);
      world.playAuxSFX(1002, i, j, k, 0);
      return true;
   }

   protected abstract void spawnThrownEntity(ItemStack var1, World var2, EntityPlayer var3);

   protected abstract EntityThrowable getEntityFiredByByBlockDispenser(World var1, double var2, double var4, double var6);
}
