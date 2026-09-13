package btw.block.blocks;

import btw.block.BTWBlocks;
import btw.block.util.Flammability;
import btw.crafting.util.FurnaceBurnTime;
import btw.item.BTWItems;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Icon;
import net.minecraft.src.IconRegister;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;

public class PlanksBlock extends Block {
   public static final String[] woodTypes = new String[]{"oak", "spruce", "birch", "jungle", "blood"};
   public static final String[] woodTextureTypes = new String[]{"wood", "wood_spruce", "wood_birch", "wood_jungle", "fcBlockPlanks_blood"};
   @Environment(EnvType.CLIENT)
   private Icon[] iconArray;

   public PlanksBlock(int iBlockID) {
      super(iBlockID, BTWBlocks.plankMaterial);
      this.setAxesEffectiveOn();
      this.c(1.0F);
      this.b(5.0F);
      this.setFireProperties(Flammability.PLANKS);
      this.setBuoyant();
      this.a(g);
      this.c("wood");
      this.a(CreativeTabs.tabBlock);
   }

   @Override
   public int damageDropped(int iMetadata) {
      return iMetadata;
   }

   @Override
   public int getHarvestToolLevel(IBlockAccess blockAccess, int i, int j, int k) {
      return 2;
   }

   @Override
   public boolean dropComponentItemsOnBadBreak(World world, int i, int j, int k, int iMetadata, float fChanceOfDrop) {
      this.dropItemsIndividually(world, i, j, k, BTWItems.sawDust.itemID, 2, 0, fChanceOfDrop);
      return true;
   }

   @Override
   public int getFurnaceBurnTime(int iItemDamage) {
      return getFurnaceBurnTimeByWoodType(iItemDamage);
   }

   public static int getFurnaceBurnTimeByWoodType(int iWoodType) {
      if (iWoodType == 0) {
         return FurnaceBurnTime.PLANKS_OAK.burnTime;
      } else if (iWoodType == 1) {
         return FurnaceBurnTime.PLANKS_SPRUCE.burnTime;
      } else if (iWoodType == 2) {
         return FurnaceBurnTime.PLANKS_BIRCH.burnTime;
      } else {
         return iWoodType == 3 ? FurnaceBurnTime.PLANKS_JUNGLE.burnTime : FurnaceBurnTime.PLANKS_BLOOD.burnTime;
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public Icon getIcon(int iSide, int iMetadata) {
      return this.iconArray[iMetadata];
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void getSubBlocks(int iBlockID, CreativeTabs creativeTabs, List list) {
      for (int iTempType = 0; iTempType < woodTypes.length; iTempType++) {
         list.add(new ItemStack(iBlockID, 1, iTempType));
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void registerIcons(IconRegister register) {
      this.iconArray = new Icon[woodTextureTypes.length];

      for (int iTempIndex = 0; iTempIndex < this.iconArray.length; iTempIndex++) {
         this.iconArray[iTempIndex] = register.registerIcon(woodTextureTypes[iTempIndex]);
      }
   }
}
