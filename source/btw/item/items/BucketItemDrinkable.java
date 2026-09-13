package btw.item.items;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EnumAction;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;

public abstract class BucketItemDrinkable extends BucketItem {
   private int hungerHealed;
   private float saturationModifier;

   public BucketItemDrinkable(int iItemID, int iHungerHealed, float fSaturationModifier) {
      super(iItemID);
      this.hungerHealed = iHungerHealed;
      this.saturationModifier = fSaturationModifier;
   }

   @Override
   public int getMaxItemUseDuration(ItemStack stack) {
      return 32;
   }

   @Override
   public EnumAction getItemUseAction(ItemStack stack) {
      return EnumAction.drink;
   }

   @Override
   public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
      if (player.canDrink()) {
         player.setItemInUse(stack, this.getMaxItemUseDuration(stack));
      } else {
         player.onCantConsume();
      }

      return stack;
   }

   @Override
   public ItemStack onEaten(ItemStack itemStack, World world, EntityPlayer player) {
      if (!player.capabilities.isCreativeMode) {
         itemStack.stackSize--;
      }

      if (!world.isRemote) {
         player.getFoodStats().addStats(this.hungerHealed, this.saturationModifier);
      }

      return itemStack.stackSize <= 0 ? new ItemStack(Item.bucketEmpty) : itemStack;
   }
}
