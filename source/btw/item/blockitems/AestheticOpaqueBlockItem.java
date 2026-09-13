package btw.item.blockitems;

import btw.block.BTWBlocks;
import net.minecraft.src.ItemBlock;
import net.minecraft.src.ItemStack;

public class AestheticOpaqueBlockItem extends ItemBlock {
   public AestheticOpaqueBlockItem(int iItemID) {
      super(iItemID);
      this.e(0);
      this.a(true);
      this.b("fcAestheticOpaque");
   }

   @Override
   public int getMetadata(int iItemDamage) {
      return iItemDamage == 2 ? 0 : iItemDamage;
   }

   @Override
   public String getUnlocalizedName(ItemStack itemstack) {
      switch (itemstack.getItemDamage()) {
         case 0:
            return super.getUnlocalizedName() + "." + "wicker";
         case 1:
            return super.getUnlocalizedName() + "." + "dung";
         case 2:
            return super.getUnlocalizedName() + "." + "steel";
         case 3:
            return super.getUnlocalizedName() + "." + "hellfire";
         case 4:
            return super.getUnlocalizedName() + "." + "padding";
         case 5:
            return super.getUnlocalizedName() + "." + "soap";
         case 6:
            return super.getUnlocalizedName() + "." + "rope";
         case 7:
            return super.getUnlocalizedName() + "." + "flint";
         case 8:
         default:
            return super.getUnlocalizedName();
         case 9:
            return super.getUnlocalizedName() + "." + "whitestone";
         case 10:
            return super.getUnlocalizedName() + "." + "whitecobble";
         case 11:
            return super.getUnlocalizedName() + "." + "barrel";
         case 12:
         case 13:
            return super.getUnlocalizedName() + "." + "choppingblock";
         case 14:
            return super.getUnlocalizedName() + "." + "enderblock";
         case 15:
            return super.getUnlocalizedName() + "." + "bone";
      }
   }

   @Override
   public int getBlockIDToPlace(int iItemDamage, int iFacing, float fClickX, float fClickY, float fClickZ) {
      return iItemDamage == 2 ? BTWBlocks.soulforgedSteelBlock.blockID : super.getBlockIDToPlace(iItemDamage, iFacing, fClickX, fClickY, fClickZ);
   }

   @Override
   public float getBuoyancy(int iItemDamage) {
      switch (iItemDamage) {
         case 0:
         case 1:
         case 4:
         case 5:
         case 6:
         case 15:
            return 1.0F;
         case 2:
         case 3:
         case 7:
         case 8:
         case 9:
         case 10:
         case 11:
         case 12:
         case 13:
         case 14:
         default:
            return super.getBuoyancy(iItemDamage);
      }
   }
}
