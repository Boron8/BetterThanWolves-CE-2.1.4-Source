package btw.item.items;

import net.minecraft.src.CreativeTabs;
import net.minecraft.src.ItemBook;

public class BookItem extends ItemBook {
   public BookItem(int iItemID) {
      super(iItemID);
      this.setBuoyant();
      this.setIncineratedInCrucible();
      this.b("book");
      this.a(CreativeTabs.tabMisc);
   }

   @Override
   public int getItemEnchantability() {
      return 0;
   }
}
