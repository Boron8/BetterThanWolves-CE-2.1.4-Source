package btw.item.items;

import btw.crafting.util.FurnaceBurnTime;
import net.minecraft.src.Item;

public class BucketItemLava extends Item {
   public BucketItemLava(int iItemID) {
      super(iItemID);
      this.setfurnaceburntime(FurnaceBurnTime.LAVA_BUCKET);
      this.b("bucketLava");
   }
}
