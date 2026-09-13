package btw.item.blockitems;

import btw.block.BTWBlocks;
import btw.block.blocks.PlanksBlock;
import net.minecraft.src.ItemBlock;
import net.minecraft.src.ItemStack;

public class WoodSidingDecorativeStubBlockItem extends ItemBlock {
   public static final int TYPE_BENCH = 0;
   public static final int TYPE_FENCE = 1;

   public WoodSidingDecorativeStubBlockItem(int iItemID) {
      super(iItemID);
      this.a(true);
   }

   @Override
   public int getBlockIDToPlace(int iItemDamage, int iFacing, float fClickX, float fClickY, float fClickZ) {
      int iWoodType = getWoodType(iItemDamage);
      switch (iWoodType) {
         case 0:
            return BTWBlocks.oakWoodSidingAndCorner.blockID;
         case 1:
            return BTWBlocks.spruceWoodSidingAndCorner.blockID;
         case 2:
            return BTWBlocks.birchWoodSidingAndCorner.blockID;
         case 3:
            return BTWBlocks.jungleWoodSidingAndCorner.blockID;
         default:
            return BTWBlocks.bloodWoodSidingAndCorner.blockID;
      }
   }

   @Override
   public int getMetadata(int iItemDamage) {
      int iBlockType = getBlockType(iItemDamage);
      return iBlockType == 0 ? 12 : 14;
   }

   @Override
   public String getUnlocalizedName(ItemStack itemstack) {
      int iWoodType = getWoodType(itemstack.getItemDamage());
      String sWoodTypeName;
      if (iWoodType == 0) {
         sWoodTypeName = "oak";
      } else if (iWoodType == 1) {
         sWoodTypeName = "spruce";
      } else if (iWoodType == 2) {
         sWoodTypeName = "birch";
      } else if (iWoodType == 3) {
         sWoodTypeName = "jungle";
      } else {
         sWoodTypeName = "blood";
      }

      int iBlockType = itemstack.getItemDamage() >> 2;
      String sBlockTypeName;
      if (iBlockType == 0) {
         sBlockTypeName = "bench";
      } else {
         sBlockTypeName = "fence";
      }

      return super.getUnlocalizedName() + "." + sWoodTypeName + "." + sBlockTypeName;
   }

   @Override
   public int getFurnaceBurnTime(int iItemDamage) {
      return PlanksBlock.getFurnaceBurnTimeByWoodType(getWoodType(iItemDamage)) / 2;
   }

   public static int getWoodType(int iItemDamage) {
      return iItemDamage & 3 | iItemDamage >> 4 << 2;
   }

   public static int getBlockType(int iItemDamage) {
      return iItemDamage >> 2 & 3;
   }

   public static int getItemDamageForType(int iWoodType, int iBlockType) {
      return iWoodType & 3 | iWoodType >> 2 << 4 | iBlockType << 2;
   }
}
