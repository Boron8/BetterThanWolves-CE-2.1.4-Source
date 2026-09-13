package btw.entity.mob;

import btw.item.BTWItems;
import net.minecraft.src.Enchantment;
import net.minecraft.src.EntityBlaze;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;

public class BlazeEntity extends EntityBlaze {
   public BlazeEntity(World world) {
      super(world);
   }

   @Override
   protected void dropFewItems(boolean bKilledByPlayer, int iLootingModifier) {
      super.dropFewItems(true, iLootingModifier);
   }

   @Override
   public void checkForScrollDrop() {
      if (this.rand.nextInt(500) == 0) {
         ItemStack itemstack = new ItemStack(BTWItems.arcaneScroll, 1, Enchantment.flame.effectId);
         this.a(itemstack, 0.0F);
      }
   }
}
