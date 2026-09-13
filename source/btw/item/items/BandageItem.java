package btw.item.items;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Potion;
import net.minecraft.src.PotionEffect;
import net.minecraft.src.World;

public class BandageItem extends PotionItem {
   public BandageItem(int itemID) {
      super(itemID);
      this.b("fcItemBandage");
   }

   @Override
   public ItemStack onEaten(ItemStack itemStack, World world, EntityPlayer player) {
      if (!player.capabilities.isCreativeMode) {
         itemStack.stackSize--;
      }

      if (!world.isRemote) {
         player.d(new PotionEffect(Potion.heal.id, 1, 1));
      }

      return itemStack;
   }

   @Override
   public int getMaxItemUseDuration(ItemStack par1ItemStack) {
      return 120;
   }
}
