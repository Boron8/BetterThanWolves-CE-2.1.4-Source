package btw.item.blockitems;

import net.minecraft.src.ItemBlock;

public class DamageToMetadataBlockItem extends ItemBlock {
   public DamageToMetadataBlockItem(int iItemID) {
      super(iItemID);
      this.e(0);
      this.a(true);
   }

   @Override
   public int getMetadata(int iItemDamage) {
      return iItemDamage;
   }
}
