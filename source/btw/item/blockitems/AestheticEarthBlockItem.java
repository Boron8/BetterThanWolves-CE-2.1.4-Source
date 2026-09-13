package btw.item.blockitems;

import net.minecraft.src.ItemBlock;
import net.minecraft.src.ItemStack;

public class AestheticEarthBlockItem extends ItemBlock {
   public AestheticEarthBlockItem(int iItemID) {
      super(iItemID);
      this.e(0);
      this.a(true);
      this.b("fcAestheticOpaqueEarth");
   }

   @Override
   public int getMetadata(int iItemDamage) {
      return iItemDamage;
   }

   @Override
   public String getUnlocalizedName(ItemStack itemstack) {
      switch (itemstack.getItemDamage()) {
         case 0:
         case 1:
         case 2:
            return super.getUnlocalizedName() + "." + "blight";
         case 3:
         case 5:
         default:
            return super.getUnlocalizedName();
         case 4:
            return super.getUnlocalizedName() + "." + "blight3";
         case 6:
            return super.getUnlocalizedName() + "." + "packed";
         case 7:
            return super.getUnlocalizedName() + "." + "dung";
      }
   }
}
