package btw.item.blockitems;

import btw.block.BTWBlocks;
import btw.block.blocks.PlanksBlock;
import net.minecraft.src.ItemBlock;
import net.minecraft.src.ItemStack;

public class WoodMouldingStubBlockItem extends ItemBlock {
   public WoodMouldingStubBlockItem(int iItemID) {
      super(iItemID);
      this.a(true);
   }

   @Override
   public int getBlockIDToPlace(int iItemDamage, int iFacing, float fClickX, float fClickY, float fClickZ) {
      switch (iItemDamage) {
         case 0:
            return BTWBlocks.oakWoodMouldingAndDecorative.blockID;
         case 1:
            return BTWBlocks.spruceWoodMouldingAndDecorative.blockID;
         case 2:
            return BTWBlocks.birchWoodMouldingAndDecorative.blockID;
         case 3:
            return BTWBlocks.jungleWoodMouldingAndDecorative.blockID;
         default:
            return BTWBlocks.bloodWoodMouldingAndDecorative.blockID;
      }
   }

   @Override
   public String getUnlocalizedName(ItemStack itemstack) {
      if (itemstack.getItemDamage() == 0) {
         return super.getUnlocalizedName() + "." + "oak";
      } else if (itemstack.getItemDamage() == 1) {
         return super.getUnlocalizedName() + "." + "spruce";
      } else if (itemstack.getItemDamage() == 2) {
         return super.getUnlocalizedName() + "." + "birch";
      } else {
         return itemstack.getItemDamage() == 3 ? super.getUnlocalizedName() + "." + "jungle" : super.getUnlocalizedName() + "." + "blood";
      }
   }

   @Override
   public int getFurnaceBurnTime(int iItemDamage) {
      return PlanksBlock.getFurnaceBurnTimeByWoodType(iItemDamage) / 4;
   }
}
