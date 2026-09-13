package btw.item.blockitems;

import net.minecraft.src.ItemBlock;
import net.minecraft.src.ItemStack;

public class PlanterBlockItem extends ItemBlock {
   public PlanterBlockItem(int i) {
      super(i);
      this.e(0);
      this.a(true);
      this.b("fcBlockPlanter");
   }

   @Override
   public int getMetadata(int i) {
      return i;
   }

   @Override
   public String getItemDisplayName(ItemStack stack) {
      String name = super.l(stack);
      int iDamage = stack.getItemDamage();
      return iDamage != 1 && iDamage != 2 ? name : "Old " + name;
   }
}
