package btw.item.items;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;

public class CuredFoodItem extends FoodItem {
   public CuredFoodItem(int iItemID, int iHungerHealed, float fSaturationModifier, String sItemName) {
      super(iItemID, iHungerHealed, fSaturationModifier, false, sItemName);
      this.maxStackSize = 32;
      this.b(sItemName);
   }

   @Override
   public void onCreated(ItemStack stack, World world, EntityPlayer player) {
      if (player.timesCraftedThisTick == 0 && world.isRemote) {
         player.playSound("random.fizz", 0.25F, 2.5F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.2F);
      }

      super.d(stack, world, player);
   }
}
