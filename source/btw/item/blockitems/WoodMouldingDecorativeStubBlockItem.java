package btw.item.blockitems;

import btw.block.BTWBlocks;
import btw.block.blocks.PlanksBlock;
import net.minecraft.src.ItemBlock;
import net.minecraft.src.ItemStack;

public class WoodMouldingDecorativeStubBlockItem extends ItemBlock {
   public static final int TYPE_COLUMN = 0;
   public static final int TYPE_PEDESTAL = 1;
   public static final int TYPE_TABLE = 2;

   public WoodMouldingDecorativeStubBlockItem(int iItemID) {
      super(iItemID);
      this.a(true);
   }

   @Override
   public int getBlockIDToPlace(int iItemDamage, int iFacing, float fClickX, float fClickY, float fClickZ) {
      int iWoodType = getWoodType(iItemDamage);
      switch (iWoodType) {
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
   public int getMetadata(int iItemDamage) {
      int iBlockType = getBlockType(iItemDamage);
      if (iBlockType == 0) {
         return 12;
      } else {
         return iBlockType == 1 ? 13 : 15;
      }
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

      int iBlockType = getBlockType(itemstack.getItemDamage());
      String sBlockTypeName;
      if (iBlockType == 0) {
         sBlockTypeName = "column";
      } else if (iBlockType == 1) {
         sBlockTypeName = "pedestal";
      } else {
         sBlockTypeName = "table";
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
