package btw.item.util;

import java.util.Random;
import net.minecraft.src.ItemStack;
import net.minecraft.src.WeightedRandom;
import net.minecraft.src.WeightedRandomItem;

public class RandomItemStack extends WeightedRandomItem {
   private ItemStack stack = null;
   private int minStackSize;
   private int maxStackSize;

   public RandomItemStack(int iItemID, int iItemDamage, int iMinStackSize, int iMaxStackSize, int iWeight) {
      super(iWeight);
      this.stack = new ItemStack(iItemID, 1, iItemDamage);
      this.minStackSize = iMinStackSize;
      this.maxStackSize = iMaxStackSize;
   }

   public static ItemStack getRandomStack(Random rand, RandomItemStack[] itemStackArray) {
      RandomItemStack randItemStack = (RandomItemStack)WeightedRandom.getRandomItem(rand, itemStackArray);
      int iStackSize = randItemStack.minStackSize + rand.nextInt(randItemStack.minStackSize - randItemStack.minStackSize + 1);
      ItemStack newStack = randItemStack.stack.copy();
      newStack.stackSize = iStackSize;
      return newStack;
   }
}
