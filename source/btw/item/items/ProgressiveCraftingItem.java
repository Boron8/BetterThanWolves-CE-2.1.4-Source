package btw.item.items;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EnumAction;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;

public class ProgressiveCraftingItem extends Item {
   public static final int PROGRESS_TIME_INTERVAL = 4;
   public static final int DEFAULT_MAX_DAMAGE = 600;

   public ProgressiveCraftingItem(int iItemID) {
      super(iItemID);
      this.maxStackSize = 1;
      this.e(this.getProgressiveCraftingMaxDamage());
   }

   @Override
   public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
      player.setItemInUse(stack, this.getMaxItemUseDuration(stack));
      return stack;
   }

   @Override
   public EnumAction getItemUseAction(ItemStack par1ItemStack) {
      return EnumAction.miscUse;
   }

   @Override
   public int getMaxItemUseDuration(ItemStack par1ItemStack) {
      return 72000;
   }

   @Override
   public void updateUsingItem(ItemStack stack, World world, EntityPlayer player) {
      int iUseCount = player.getItemInUseCount();
      if (this.getMaxItemUseDuration(stack) - iUseCount > this.getItemUseWarmupDuration()) {
         if (iUseCount % 4 == 0) {
            this.playCraftingFX(stack, world, player);
         }

         if (!world.isRemote && iUseCount % 4 == 0) {
            int iDamage = stack.getItemDamage();
            if (--iDamage > 0) {
               stack.setItemDamage(iDamage);
            } else {
               player.setItemInUseCount(1);
            }
         }
      }
   }

   @Override
   public boolean ignoreDamageWhenComparingDuringUse() {
      return true;
   }

   protected void playCraftingFX(ItemStack stack, World world, EntityPlayer player) {
   }

   protected int getProgressiveCraftingMaxDamage() {
      return 600;
   }
}
