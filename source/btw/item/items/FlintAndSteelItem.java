package btw.item.items;

import java.util.Random;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;

public class FlintAndSteelItem extends FireStarterItem {
   private static final float FLINT_AND_STEEL_EXHAUSTION_PER_USE = 0.01F;

   public FlintAndSteelItem(int iItemID) {
      super(iItemID, 64, 0.01F);
   }

   @Override
   protected boolean checkChanceOfStart(ItemStack stack, Random rand) {
      return rand.nextInt(4) == 0;
   }

   @Override
   protected void performUseEffects(EntityPlayer player) {
      player.playSound("fire.ignite", 1.0F, Item.itemRand.nextFloat() * 0.4F + 0.8F);
   }
}
