package btw.block.blocks;

import btw.block.BTWBlocks;
import btw.block.util.Flammability;
import btw.item.BTWItems;
import java.util.List;
import java.util.Random;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.Block;
import net.minecraft.src.BlockHalfSlab;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Icon;
import net.minecraft.src.IconRegister;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;

public class WoodSlabBlock extends BlockHalfSlab {
   public static final String[] woodType = new String[]{"oak", "spruce", "birch", "jungle", "blood"};

   public WoodSlabBlock(int iBlockID, boolean bDoubleSlab) {
      super(iBlockID, bDoubleSlab, BTWBlocks.plankMaterial);
      this.c(1.0F);
      this.b(5.0F);
      this.setAxesEffectiveOn();
      this.setBuoyant();
      this.setFireProperties(Flammability.PLANKS);
      this.a(g);
      this.c("woodSlab");
      this.a(CreativeTabs.tabBlock);
   }

   @Override
   public int idDropped(int iMetadata, Random rand, int iFortuneModifier) {
      return Block.woodSingleSlab.blockID;
   }

   @Override
   public String getFullSlabName(int iMetadata) {
      return super.a() + "." + woodType[iMetadata];
   }

   @Override
   protected ItemStack createStackedBlock(int iMetadata) {
      return new ItemStack(Block.woodSingleSlab.blockID, 2, iMetadata & 7);
   }

   @Override
   public int getHarvestToolLevel(IBlockAccess blockAccess, int i, int j, int k) {
      return 2;
   }

   @Override
   public void onBlockDestroyedWithImproperTool(World world, EntityPlayer player, int i, int j, int k, int iMetadata) {
      int iNumItems = this.isDoubleSlab ? 2 : 1;

      for (int iTempCount = 0; iTempCount < iNumItems; iTempCount++) {
         this.b(world, i, j, k, new ItemStack(BTWItems.sawDust));
      }
   }

   @Override
   public int getFurnaceBurnTime(int iItemDamage) {
      int iBurnTime = PlanksBlock.getFurnaceBurnTimeByWoodType(iItemDamage);
      if (!this.isDoubleSlab) {
         iBurnTime >>= 1;
      }

      return iBurnTime;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void getSubBlocks(int iBlockID, CreativeTabs creativeTabs, List list) {
      if (iBlockID != Block.woodDoubleSlab.blockID) {
         for (int iTempDamage = 0; iTempDamage <= 4; iTempDamage++) {
            list.add(new ItemStack(iBlockID, 1, iTempDamage));
         }
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void registerIcons(IconRegister register) {
   }

   @Environment(EnvType.CLIENT)
   @Override
   public Icon getIcon(int iSide, int iMetadata) {
      return Block.planks.getIcon(iSide, iMetadata & 7);
   }
}
