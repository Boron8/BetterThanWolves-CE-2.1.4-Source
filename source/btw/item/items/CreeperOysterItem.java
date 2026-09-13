package btw.item.items;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Potion;

public class CreeperOysterItem extends FoodItem {
   public CreeperOysterItem(int iItemID) {
      super(iItemID, 2, 0.8F, false, "fcItemCreeperOysters");
      this.setBellowsBlowDistance(1);
      this.setFilterableProperties(2);
      this.a(Potion.poison.id, 5, 0, 1.0F);
   }

   @Override
   public void onUsedInCrafting(EntityPlayer player, ItemStack outputStack) {
      if (player.timesCraftedThisTick == 0) {
         player.playSound("mob.slime.attack", 0.5F, (player.worldObj.rand.nextFloat() - player.worldObj.rand.nextFloat()) * 0.1F + 0.7F);
      }
   }
}
